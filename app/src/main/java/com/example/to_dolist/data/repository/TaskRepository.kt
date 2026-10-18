package com.example.to_dolist.data.repository

import com.example.to_dolist.data.dao.TaskDao
import com.example.to_dolist.data.model.Category
import com.example.to_dolist.data.model.Priority
import com.example.to_dolist.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<TaskEntity>> = taskDao.observeAllTasks()

    fun getTaskById(taskId: Long): Flow<TaskEntity?> = taskDao.observeTaskById(taskId)

    suspend fun getTask(taskId: Long): TaskEntity? = taskDao.getTaskById(taskId)

    suspend fun insertTask(task: TaskEntity): Long {
        val currentTime = System.currentTimeMillis()
        val taskToInsert = if (task.createdAt == 0L) {
            task.copy(createdAt = currentTime, updatedAt = currentTime)
        } else {
            task.copy(updatedAt = currentTime)
        }
        return taskDao.insertTask(taskToInsert)
    }

    suspend fun updateTask(task: TaskEntity) {
        val taskToUpdate = task.copy(updatedAt = System.currentTimeMillis())
        taskDao.updateTask(taskToUpdate)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId)
    }

    fun getTasksByCategory(category: Category): Flow<List<TaskEntity>> {
        return taskDao.observeTasksByCategory(category)
    }

    fun getTasksByPriority(priority: Priority): Flow<List<TaskEntity>> {
        return taskDao.observeTasksByPriority(priority)
    }

    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<TaskEntity>> {
        return taskDao.observeTasksByCompletion(isCompleted)
    }

    fun getImportantTasks(): Flow<List<TaskEntity>> {
        return taskDao.observeImportantTasks()
    }

    fun searchTasks(query: String): Flow<List<TaskEntity>> {
        return taskDao.searchTasks(query)
    }
}
