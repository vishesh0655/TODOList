package com.example.to_dolist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.to_dolist.data.model.Category
import com.example.to_dolist.data.model.Priority
import com.example.to_dolist.data.model.Task
import com.example.to_dolist.ui.components.EmptyState
import com.example.to_dolist.ui.components.FilterChipsRow
import com.example.to_dolist.ui.components.TaskItemCard
import com.example.to_dolist.ui.viewmodel.SmartSection
import com.example.to_dolist.ui.viewmodel.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    selectedTask: Task?,
    smartSection: SmartSection,
    searchQuery: String,
    selectedCategory: Category?,
    selectedPriority: Priority?,
    sortOption: SortOption,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (Category?) -> Unit,
    onPrioritySelected: (Priority?) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    onTaskClick: (Task) -> Unit,
    onCheckedChange: (Task, Boolean) -> Unit,
    onStarClick: (Task) -> Unit,
    onAddTaskClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Search Bar & Sort button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Search tasks...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            Box {
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort tasks")
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Due Date") },
                        onClick = {
                            onSortOptionSelected(SortOption.DUE_DATE)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Priority") },
                        onClick = {
                            onSortOptionSelected(SortOption.PRIORITY)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Title") },
                        onClick = {
                            onSortOptionSelected(SortOption.TITLE)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Created Date") },
                        onClick = {
                            onSortOptionSelected(SortOption.CREATED)
                            showSortMenu = false
                        }
                    )
                }
            }
        }

        // Filter Chips
        FilterChipsRow(
            selectedCategory = selectedCategory,
            selectedPriority = selectedPriority,
            sortOption = sortOption,
            onCategorySelected = onCategorySelected,
            onPrioritySelected = onPrioritySelected,
            onSortOptionSelected = onSortOptionSelected
        )

        // Task List or Empty State
        if (tasks.isEmpty()) {
            val (emptyTitle, emptySubtitle) = when {
                searchQuery.isNotBlank() -> "No matching tasks" to "Try adjusting your search or filters"
                smartSection == SmartSection.COMPLETED -> "No completed tasks yet" to "Tasks you complete will show up here"
                smartSection == SmartSection.TODAY -> "No tasks due today" to "Enjoy your free time or add a new task!"
                else -> "No tasks found" to "Tap the + button below to create your first task"
            }
            EmptyState(
                title = emptyTitle,
                subtitle = emptySubtitle,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskItemCard(
                        task = task,
                        isSelected = selectedTask?.id == task.id,
                        onTaskClick = { onTaskClick(task) },
                        onCheckedChange = { checked -> onCheckedChange(task, checked) },
                        onStarClick = { onStarClick(task) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}
