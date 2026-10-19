package com.example.to_dolist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.to_dolist.ui.theme.ThemeModeOption
import com.example.to_dolist.ui.viewmodel.SmartSection
import com.example.to_dolist.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val selectedSmartSection by viewModel.selectedSmartSection.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedPriority by viewModel.selectedPriority.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val selectedTask by viewModel.selectedTask.collectAsStateWithLifecycle()
    val isEditDialogVisible by viewModel.isEditDialogVisible.collectAsStateWithLifecycle()
    val taskBeingEdited by viewModel.taskBeingEdited.collectAsStateWithLifecycle()
    val deletedTask by viewModel.deletedTask.collectAsStateWithLifecycle()

    val themeAccent by viewModel.themeAccent.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val wallpaperOption by viewModel.wallpaperOption.collectAsStateWithLifecycle()

    var showSettingsDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
    val canNavigateBack = navigator.canNavigateBack()

    BackHandler(enabled = canNavigateBack) {
        scope.launch {
            navigator.navigateBack()
        }
        viewModel.selectTask(null)
    }

    // Snackbar for Undo Delete
    LaunchedEffect(deletedTask) {
        val taskToProcess = deletedTask
        if (taskToProcess != null) {
            val result = snackbarHostState.showSnackbar(
                message = "Task \"${taskToProcess.title}\" deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearDeletedTask()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "TO-DO List",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                val sections = listOf(
                    Triple(SmartSection.ALL, "All Tasks", Icons.AutoMirrored.Outlined.List),
                    Triple(SmartSection.TODAY, "Today", Icons.Outlined.Today),
                    Triple(SmartSection.UPCOMING, "Upcoming", Icons.Outlined.Event),
                    Triple(SmartSection.IMPORTANT, "Important", Icons.Outlined.Star),
                    Triple(SmartSection.COMPLETED, "Completed", Icons.Outlined.DoneAll)
                )

                sections.forEach { (section, label, icon) ->
                    NavigationDrawerItem(
                        icon = { Icon(icon, contentDescription = null) },
                        label = { Text(label) },
                        selected = selectedSmartSection == section,
                        onClick = {
                            viewModel.setSmartSection(section)
                            if (canNavigateBack) {
                                scope.launch {
                                    navigator.navigateBack()
                                }
                                viewModel.selectTask(null)
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        val sectionTitle = when (selectedSmartSection) {
                            SmartSection.ALL -> "All Tasks"
                            SmartSection.TODAY -> "Today"
                            SmartSection.UPCOMING -> "Upcoming"
                            SmartSection.COMPLETED -> "Completed"
                            SmartSection.IMPORTANT -> "Important"
                        }
                        Text(sectionTitle)
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open navigation drawer")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSettingsDialog = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer.let {
                            if (themeMode == ThemeModeOption.TRANSLUCENT) it.copy(alpha = 0.3f) else it
                        }
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.showEditDialog() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                listPane = {
                    TaskListScreen(
                        tasks = tasks,
                        selectedTask = selectedTask,
                        smartSection = selectedSmartSection,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        selectedPriority = selectedPriority,
                        sortOption = sortOption,
                        onSearchQueryChanged = { viewModel.setSearchQuery(it) },
                        onCategorySelected = { viewModel.setCategoryFilter(it) },
                        onPrioritySelected = { viewModel.setPriorityFilter(it) },
                        onSortOptionSelected = { viewModel.setSortOption(it) },
                        onTaskClick = { task ->
                            viewModel.selectTask(task)
                            scope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, task.id)
                            }
                        },
                        onCheckedChange = { task, _ ->
                            viewModel.toggleTaskCompletion(task)
                        },
                        onStarClick = { task ->
                            viewModel.toggleTaskImportance(task)
                        },
                        onAddTaskClick = {
                            viewModel.showEditDialog()
                        }
                    )
                },
                detailPane = {
                    TaskDetailScreen(
                        task = selectedTask,
                        onBackClick = if (canNavigateBack) {
                            {
                                scope.launch {
                                    navigator.navigateBack()
                                }
                                viewModel.selectTask(null)
                            }
                        } else null,
                        onEditTask = { task ->
                            viewModel.showEditDialog(task)
                        },
                        onDeleteTask = { task ->
                            viewModel.deleteTask(task)
                            if (canNavigateBack) {
                                scope.launch {
                                    navigator.navigateBack()
                                }
                            }
                        },
                        onToggleCompletion = { task ->
                            viewModel.toggleTaskCompletion(task)
                        },
                        onToggleImportance = { task ->
                            viewModel.toggleTaskImportance(task)
                        }
                    )
                }
            )
        }
    }

    if (isEditDialogVisible) {
        TaskEditDialog(
            taskToEdit = taskBeingEdited,
            onDismiss = { viewModel.hideEditDialog() },
            onSave = { task ->
                if (task.id == 0L) {
                    viewModel.addTask(task)
                } else {
                    viewModel.updateTask(task)
                }
            }
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            currentThemeAccent = themeAccent,
            currentThemeMode = themeMode,
            currentWallpaper = wallpaperOption,
            onThemeAccentSelected = { viewModel.setThemeAccent(it) },
            onThemeModeSelected = { viewModel.setThemeMode(it) },
            onWallpaperSelected = { viewModel.setWallpaperOption(it) },
            onDismiss = { showSettingsDialog = false }
        )
    }
}
