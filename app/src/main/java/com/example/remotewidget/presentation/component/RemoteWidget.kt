package com.example.remotewidget.presentation.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.*
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import com.example.remotewidget.R
import com.example.remotewidget.extra.Constants.DataStoreConstants.FILE_URI
import java.io.File

class RemoteWidget : GlanceAppWidget(errorUiLayout = R.layout.widget_error_layout) {

    override val stateDefinition: GlanceStateDefinition<*>
        get() = CustomGlanceStateDefinition

    @Composable
    override fun Content() {

        val pref = currentState<Preferences>()

        val filePathString: String = remember {
            pref[stringPreferencesKey(FILE_URI)] ?: ""
        }
        val bitmap = rememberImageBitmap(filePathString)

        Image(
            provider = ImageProvider(bitmap),
            contentDescription = null,
            modifier = GlanceModifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

    }

    @Composable
    private fun rememberImageBitmap(filePathString: String): Bitmap {
        val context = LocalContext.current
        return remember {
            if (filePathString.isEmpty()) {
                val file = context.applicationContext.filesDir
                    .listFiles()
                    ?.find { it.path.endsWith(".jpg") }
                val path = file?.toURI()?.path
                BitmapFactory.decodeFile(path)
            } else
                BitmapFactory.decodeFile(filePathString)
        }
    }
}

object CustomGlanceStateDefinition : GlanceStateDefinition<Preferences> {
    private const val fileName = "widget_preference"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        return context.dataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return File(context.applicationContext.filesDir, "datastore/$fileName")
    }

    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = fileName)
}

