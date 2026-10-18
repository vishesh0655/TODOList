package com.example.to_dolist.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.to_dolist.data.model.Category
import com.example.to_dolist.data.model.Priority
import com.example.to_dolist.ui.viewmodel.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipsRow(
    selectedCategory: Category?,
    selectedPriority: Priority?,
    sortOption: SortOption,
    onCategorySelected: (Category?) -> Unit,
    onPrioritySelected: (Priority?) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // All Categories chip
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("All Categories") }
        )

        Category.entries.forEach { category ->
            val label = category.name.lowercase().replaceFirstChar { it.uppercase() }
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(if (selectedCategory == category) null else category) },
                label = { Text(label) },
                trailingIcon = if (selectedCategory == category) {
                    {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear category filter",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .height(24.dp)
                .width(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Priority.entries.forEach { priority ->
            val label = priority.name.lowercase().replaceFirstChar { it.uppercase() } + " Priority"
            FilterChip(
                selected = selectedPriority == priority,
                onClick = { onPrioritySelected(if (selectedPriority == priority) null else priority) },
                label = { Text(label) },
                trailingIcon = if (selectedPriority == priority) {
                    {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear priority filter",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )
        }
    }
}
