package com.example.remotewidget.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.remotewidget.extra.Constants.DataStoreConstants.FILE_URI
import com.example.remotewidget.extra.Constants.WorkerConstants.CURRENT_CATEGORY
import com.example.remotewidget.extra.Constants.WorkerConstants.ERROR_MESSAGE
import com.example.remotewidget.presentation.component.CustomGlanceStateDefinition
import com.example.remotewidget.presentation.component.RemoteWidget
import com.example.remotewidget.presentation.main_screen.categories
import java.io.File
import java.io.FileOutputStream
import java.util.*


@Suppress("BlockingMethodInNonBlockingContext")
class RemoteImageWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(
    context,
    workerParameters
) {
    override suspend fun doWork(): Result {
        return try {

            // getting image category index
            val currentCategoryIndex = inputData.getInt(CURRENT_CATEGORY, categories.lastIndex)
            // getting the category string by the index
            val query =
                if (currentCategoryIndex == categories.lastIndex) "" else categories[currentCategoryIndex].name
            // getting a single image object
            val response = RemoteImageService.serviceInstance.getRandomImageObject(query = query)
            response.body()?.let { remoteImage ->

                // getting image raw bytes from image object url
                RemoteImageService.serviceInstance.getRandomImageBytes(remoteImage.urls.regular)
                    .body()?.let {

                        //creating image file and writing bytes to that file

                        val imageFile = File(context.filesDir, "${UUID.randomUUID()}.jpg")

                        FileOutputStream(imageFile).use { stream ->
                            stream.write(it.bytes())
                        }
                        // deleting the previous image
                        context.filesDir.listFiles()?.let {
                            for (file in it) {
                                if (file.path.endsWith(".jpg") && file.path != imageFile.path)
                                    file.delete()
                            }
                        }

                        //updating the widget
                        updateRemoteImageWidget(imageFile.path)
                        Result.success()
                    }
                    ?: Result.failure(workDataOf(ERROR_MESSAGE to "Error occurred while retrieving image"))
            }
                ?: Result.failure(workDataOf(ERROR_MESSAGE to "Error occurred while retrieving image object"))

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(workDataOf(ERROR_MESSAGE to e.localizedMessage))
        }
    }

    private suspend fun updateRemoteImageWidget(imageUri: String) {
        GlanceAppWidgetManager(context).getGlanceIds(RemoteWidget::class.java).forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { pref ->
                pref[stringPreferencesKey(FILE_URI)] = imageUri
            }
            RemoteWidget().updateAll(context)
        }
    }

}