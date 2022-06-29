package com.example.remotewidget.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.remotewidget.presentation.main_screen.MainScreen
import com.example.remotewidget.presentation.main_screen.MainScreenViewModel
import com.example.remotewidget.presentation.main_screen.MainScreenViewModelFactory
import com.example.remotewidget.presentation.main_screen.categories
import com.example.remotewidget.presentation.ui.theme.RemoteWidgetTheme

class RemoteWidgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainScreenViewModel> {
            MainScreenViewModelFactory(application)
        }
        setContent {
            val categoryIndex by viewModel.imageCategoryIndex.collectAsState(categories.lastIndex)
            RemoteWidgetTheme {
                MainScreen(categoryIndex){ categoryIndex->
                    viewModel.setCategory(categoryIndex)
                }
            }
        }
    }
}
