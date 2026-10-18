package com.example.to_dolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.to_dolist.data.database.TodoDatabase
import com.example.to_dolist.data.repository.TaskRepository
import com.example.to_dolist.ui.screens.MainScreen
import com.example.to_dolist.ui.theme.TODOListTheme
import com.example.to_dolist.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TaskViewModel by viewModels {
        val database = TodoDatabase.getDatabase(applicationContext)
        val repository = TaskRepository(database.taskDao())
        TaskViewModel.provideFactory(repository, application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeAccent by viewModel.themeAccent.collectAsStateWithLifecycle()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val wallpaperOption by viewModel.wallpaperOption.collectAsStateWithLifecycle()

            TODOListTheme(
                themeAccent = themeAccent,
                themeMode = themeMode,
                wallpaperOption = wallpaperOption
            ) {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
