package com.example.remotewidget.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.remotewidget.data.PreferencesManager.Companion.pref
import com.example.remotewidget.extra.Constants.DataStoreConstants.IMAGE_CATEGORY
import com.example.remotewidget.presentation.main_screen.categories
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class PreferencesManager(
    private val application: Application,
) {
    private companion object{
        val Context.pref: DataStore<Preferences> by preferencesDataStore(
            name = "pref_settings"
        )
    }

    val imageCategoryPref = application.applicationContext.pref.data
        .catch { e ->
            if (e is IOException)
                emit(emptyPreferences())
            else
                throw e
        }.map { preferences ->
            val imageCategory = preferences[intPreferencesKey(IMAGE_CATEGORY)] ?: categories.lastIndex
            imageCategory
        }

    suspend fun setImageCategory(categoryIndex: Int) {
        application.applicationContext.pref.edit { preferences ->
            preferences[intPreferencesKey(IMAGE_CATEGORY)] = categoryIndex
        }
    }
}