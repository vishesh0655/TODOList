package com.example.to_dolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

enum class Category {
    PERSONAL,
    WORK,
    STUDY,
    SHOPPING,
    OTHER
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val category: Category = Category.OTHER,
    val dueDate: Long? = null,
    val isImportant: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

typealias TaskEntity = Task
