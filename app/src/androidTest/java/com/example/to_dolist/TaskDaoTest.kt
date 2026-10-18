package com.example.to_dolist

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.to_dolist.data.dao.TaskDao
import com.example.to_dolist.data.database.TodoDatabase
import com.example.to_dolist.data.model.Category
import com.example.to_dolist.data.model.Priority
import com.example.to_dolist.data.model.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var database: TodoDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun writeTaskAndReadInList() = runBlocking {
        val task = Task(
            title = "Test Task",
            description = "Test Description",
            priority = Priority.HIGH,
            category = Category.WORK,
            isImportant = true
        )
        taskDao.insertTask(task)

        val tasks = taskDao.observeAllTasks().first()
        assertEquals(1, tasks.size)
        assertEquals("Test Task", tasks[0].title)
        assertEquals(Priority.HIGH, tasks[0].priority)
        assertEquals(Category.WORK, tasks[0].category)
        assertEquals(true, tasks[0].isImportant)
    }

    @Test
    fun updateTask() = runBlocking {
        val task = Task(title = "Initial Title")
        val id = taskDao.insertTask(task)

        val inserted = taskDao.getTaskById(id)
        assertNotNull(inserted)

        val updated = inserted!!.copy(title = "Updated Title", isCompleted = true)
        taskDao.updateTask(updated)

        val result = taskDao.getTaskById(id)
        assertNotNull(result)
        assertEquals("Updated Title", result!!.title)
        assertEquals(true, result.isCompleted)
    }

    @Test
    fun deleteTask() = runBlocking {
        val task = Task(title = "To Delete")
        val id = taskDao.insertTask(task)

        assertNotNull(taskDao.getTaskById(id))

        taskDao.deleteTaskById(id)

        assertNull(taskDao.getTaskById(id))
    }
}
