package com.example.to_dolist.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.to_dolist.data.model.Category
import com.example.to_dolist.data.model.Priority
import com.example.to_dolist.data.model.Task
import com.example.to_dolist.data.repository.TaskRepository
import com.example.to_dolist.ui.theme.ThemeAccent
import com.example.to_dolist.ui.theme.ThemeModeOption
import com.example.to_dolist.ui.theme.WallpaperOption
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

enum class SmartSection {
    ALL,
    TODAY,
    UPCOMING,
    COMPLETED,
    IMPORTANT
}

enum class SortOption {
    DUE_DATE,
    PRIORITY,
    TITLE,
    CREATED
}

class TaskViewModel(
    private val repository: TaskRepository,
    application: Application
) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("todo_theme_prefs", Context.MODE_PRIVATE)

    private val _themeAccent = MutableStateFlow(
        try {
            ThemeAccent.valueOf(prefs.getString("theme_accent", ThemeAccent.DEFAULT.name) ?: ThemeAccent.DEFAULT.name)
        } catch (e: Exception) {
            ThemeAccent.DEFAULT
        }
    )
    val themeAccent: StateFlow<ThemeAccent> = _themeAccent.asStateFlow()

    private val _themeMode = MutableStateFlow(
        try {
            ThemeModeOption.valueOf(prefs.getString("theme_mode", ThemeModeOption.SYSTEM.name) ?: ThemeModeOption.SYSTEM.name)
        } catch (e: Exception) {
            ThemeModeOption.SYSTEM
        }
    )
    val themeMode: StateFlow<ThemeModeOption> = _themeMode.asStateFlow()

    private val _wallpaperOption = MutableStateFlow(
        try {
            WallpaperOption.valueOf(prefs.getString("wallpaper_option", WallpaperOption.PURPLE_BLUE.name) ?: WallpaperOption.PURPLE_BLUE.name)
        } catch (e: Exception) {
            WallpaperOption.PURPLE_BLUE
        }
    )
    val wallpaperOption: StateFlow<WallpaperOption> = _wallpaperOption.asStateFlow()

    fun setThemeAccent(accent: ThemeAccent) {
        _themeAccent.value = accent
        prefs.edit { putString("theme_accent", accent.name) }
    }

    fun setThemeMode(mode: ThemeModeOption) {
        _themeMode.value = mode
        prefs.edit { putString("theme_mode", mode.name) }
    }

    fun setWallpaperOption(wallpaper: WallpaperOption) {
        _wallpaperOption.value = wallpaper
        prefs.edit { putString("wallpaper_option", wallpaper.name) }
    }

    private val _selectedSmartSection = MutableStateFlow(SmartSection.ALL)
    val selectedSmartSection: StateFlow<SmartSection> = _selectedSmartSection.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _selectedPriority = MutableStateFlow<Priority?>(null)
    val selectedPriority: StateFlow<Priority?> = _selectedPriority.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DUE_DATE)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _isEditDialogVisible = MutableStateFlow(false)
    val isEditDialogVisible: StateFlow<Boolean> = _isEditDialogVisible.asStateFlow()

    private val _taskBeingEdited = MutableStateFlow<Task?>(null)
    val taskBeingEdited: StateFlow<Task?> = _taskBeingEdited.asStateFlow()

    private val _deletedTask = MutableStateFlow<Task?>(null)
    val deletedTask: StateFlow<Task?> = _deletedTask.asStateFlow()

    val rawTasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.allTasks.collect { allTasks ->
                val currentSelected = _selectedTask.value
                if (currentSelected != null) {
                    val matchingTask = allTasks.find { it.id == currentSelected.id }
                    if (matchingTask != currentSelected) {
                        _selectedTask.value = matchingTask
                    }
                }
            }
        }
    }

    val tasks: StateFlow<List<Task>> = combine(
        repository.allTasks,
        _selectedSmartSection,
        _selectedCategory,
        _selectedPriority,
        _searchQuery
    ) { allTasks, smartSection, categoryFilter, priorityFilter, query ->
        allTasks.filter { task ->
            // 1. Smart Section filter
            val matchesSmartSection = when (smartSection) {
                SmartSection.ALL -> true
                SmartSection.TODAY -> isToday(task.dueDate) && !task.isCompleted
                SmartSection.UPCOMING -> isUpcoming(task.dueDate) && !task.isCompleted
                SmartSection.COMPLETED -> task.isCompleted
                SmartSection.IMPORTANT -> task.isImportant && !task.isCompleted
            }

            // 2. Category filter
            val matchesCategory = categoryFilter == null || task.category == categoryFilter

            // 3. Priority filter
            val matchesPriority = priorityFilter == null || task.priority == priorityFilter

            // 4. Search query filter
            val matchesSearch = if (query.isBlank()) {
                true
            } else {
                val q = query.trim().lowercase()
                task.title.lowercase().contains(q) || (task.description?.lowercase()?.contains(q) == true)
            }

            matchesSmartSection && matchesCategory && matchesPriority && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSmartSection(section: SmartSection) {
        _selectedSmartSection.value = section
    }

    fun setCategoryFilter(category: Category?) {
        _selectedCategory.value = category
    }

    fun setPriorityFilter(priority: Priority?) {
        _selectedPriority.value = priority
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectTask(task: Task?) {
        _selectedTask.value = task
    }

    fun showEditDialog(task: Task? = null) {
        _taskBeingEdited.value = task
        _isEditDialogVisible.value = true
    }

    fun hideEditDialog() {
        _taskBeingEdited.value = null
        _isEditDialogVisible.value = false
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            if (_selectedTask.value?.id == task.id) {
                _selectedTask.value = task
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            _deletedTask.value = task
            repository.deleteTask(task)
            if (_selectedTask.value?.id == task.id) {
                _selectedTask.value = null
            }
        }
    }

    fun deleteTaskById(id: Long) {
        viewModelScope.launch {
            val task = repository.getTask(id)
            if (task != null) {
                _deletedTask.value = task
                repository.deleteTaskById(id)
                if (_selectedTask.value?.id == id) {
                    _selectedTask.value = null
                }
            }
        }
    }

    fun undoDelete() {
        val taskToRestore = _deletedTask.value
        if (taskToRestore != null) {
            viewModelScope.launch {
                repository.insertTask(taskToRestore)
                _deletedTask.value = null
            }
        }
    }

    fun clearDeletedTask() {
        _deletedTask.value = null
    }

    fun toggleTaskCompletion(task: Task) {
        val updated = task.copy(
            isCompleted = !task.isCompleted,
            updatedAt = System.currentTimeMillis()
        )
        updateTask(updated)
    }

    fun toggleTaskImportance(task: Task) {
        val updated = task.copy(
            isImportant = !task.isImportant,
            updatedAt = System.currentTimeMillis()
        )
        updateTask(updated)
    }

    private fun isToday(timestamp: Long?): Boolean {
        if (timestamp == null) return false
        val today = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }
        return today.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
    }

    private fun isUpcoming(timestamp: Long?): Boolean {
        if (timestamp == null) return false
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return timestamp > today.timeInMillis
    }

    companion object {
        fun provideFactory(repository: TaskRepository, application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TaskViewModel(repository, application)
            }
        }
    }
}
