package com.example.to_dolist

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.to_dolist.data.dao.TaskDao
import com.example.to_dolist.data.database.TodoDatabase
import com.example.to_dolist.data.model.Category
import com.example.to_dolist.data.model.Priority
import com.example.to_dolist.data.model.Task
import com.example.to_dolist.data.repository.TaskRepository
import com.example.to_dolist.ui.theme.ThemeAccent
import com.example.to_dolist.ui.theme.ThemeModeOption
import com.example.to_dolist.ui.theme.WallpaperOption
import com.example.to_dolist.ui.viewmodel.SmartSection
import com.example.to_dolist.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskViewModelTest {
    private lateinit var database: TodoDatabase
    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TaskViewModel
    private lateinit var application: Application

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(application, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.taskDao()
        repository = TaskRepository(taskDao)
        viewModel = TaskViewModel(repository, application)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun testAddTaskAndFiltering() = runBlocking {
        val task1 = Task(title = "Task 1", category = Category.WORK, priority = Priority.HIGH, isImportant = true)
        val task2 = Task(title = "Task 2", category = Category.PERSONAL, priority = Priority.LOW)

        viewModel.addTask(task1)
        viewModel.addTask(task2)

        val tasks = viewModel.tasks.first { it.size == 2 }
        assertEquals(2, tasks.size)

        // Test category filter
        viewModel.setCategoryFilter(Category.WORK)
        val workTasks = viewModel.tasks.first { it.size == 1 }
        assertEquals(1, workTasks.size)
        assertEquals("Task 1", workTasks[0].title)

        // Reset category filter
        viewModel.setCategoryFilter(null)

        // Test smart section IMPORTANT
        viewModel.setSmartSection(SmartSection.IMPORTANT)
        val importantTasks = viewModel.tasks.first { it.size == 1 }
        assertEquals(1, importantTasks.size)
        assertEquals("Task 1", importantTasks[0].title)
    }

    @Test
    fun testToggleCompletionAndUndoDelete() = runBlocking {
        val task = Task(title = "Task to delete")
        viewModel.addTask(task)

        val tasks = viewModel.tasks.first { it.size == 1 }
        val insertedTask = tasks[0]

        viewModel.deleteTask(insertedTask)
        val emptyTasks = viewModel.tasks.first { it.isEmpty() }
        assertTrue(emptyTasks.isEmpty())

        // Test undo delete
        viewModel.undoDelete()
        val restoredTasks = viewModel.tasks.first { it.size == 1 }
        assertEquals(1, restoredTasks.size)
        assertEquals("Task to delete", restoredTasks[0].title)
    }

    @Test
    fun testThemeSettings() {
        viewModel.setThemeAccent(ThemeAccent.BLUE)
        assertEquals(ThemeAccent.BLUE, viewModel.themeAccent.value)

        viewModel.setThemeMode(ThemeModeOption.TRANSLUCENT)
        assertEquals(ThemeModeOption.TRANSLUCENT, viewModel.themeMode.value)

        viewModel.setWallpaperOption(WallpaperOption.SUNSET_ORANGE)
        assertEquals(WallpaperOption.SUNSET_ORANGE, viewModel.wallpaperOption.value)
    }
}
