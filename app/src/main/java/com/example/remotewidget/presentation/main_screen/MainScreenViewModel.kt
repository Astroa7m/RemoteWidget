package com.example.remotewidget.presentation.main_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.remotewidget.data.PreferencesManager
import com.example.remotewidget.data.RemoteImageWorker
import com.example.remotewidget.extra.Constants.WorkerConstants.CURRENT_CATEGORY
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

// TODO: fix to check if its first time to launch the app or not
// TODO: fix when adding a new widget we get glance error

class MainScreenViewModel (
    application: Application
) : ViewModel() {

    private val preferencesManager = PreferencesManager(application)
    val imageCategoryIndex = preferencesManager.imageCategoryPref
    private var workManager = WorkManager.getInstance(application)

    init {
        imageCategoryIndex.onEach {
            setWork(it)
        }.launchIn(viewModelScope)
    }

    fun setCategory(categoryIndex: Int) {
        viewModelScope.launch {
            preferencesManager.setImageCategory(categoryIndex)
        }
    }

    private fun setWork(categoryIndex: Int) {
        val inputData = Data.Builder().putInt(CURRENT_CATEGORY, categoryIndex).build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val work = PeriodicWorkRequestBuilder<RemoteImageWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        workManager.enqueue(work)
    }

}

class MainScreenViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            MainScreenViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}