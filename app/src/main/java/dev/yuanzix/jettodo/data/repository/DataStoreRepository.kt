package dev.yuanzix.jettodo.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.yuanzix.jettodo.data.model.Priority
import dev.yuanzix.jettodo.util.Constants.PREFERENCE_NAME
import dev.yuanzix.jettodo.util.Constants.PREFERENCE_SORT_ORDER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys {
        val sortOrder = stringPreferencesKey(name = PREFERENCE_SORT_ORDER)
    }

    private val dataStore = context.dataStore

    suspend fun persistSortOrder(priority: Priority) {
        dataStore.edit {
            it[PreferenceKeys.sortOrder] = priority.name
        }
    }

    val readSortOrder: Flow<String> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[PreferenceKeys.sortOrder] ?: Priority.None.name
    }

}