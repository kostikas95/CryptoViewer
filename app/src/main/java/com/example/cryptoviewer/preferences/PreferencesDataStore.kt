package com.example.cryptoviewer.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesDataStore {

    private const val PREFERENCES_NAME = "user_preferences"
    val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

    // favorite IDs
    val FAVORITE_IDS = stringSetPreferencesKey("favorite_ids")

    fun getFavoriteIds(context: Context): Flow<Set<String>> =
        context.dataStore.data.map { preferences ->
            preferences[FAVORITE_IDS] ?: emptySet()
        }
    suspend fun addToFavorites(context: Context, id: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_IDS] ?: emptySet()
            preferences[FAVORITE_IDS] = currentFavorites + id
        }
    }
    suspend fun removeFromFavourites(context: Context, id: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_IDS] ?: emptySet()
            preferences[FAVORITE_IDS] = currentFavorites - id
        }
    }


    // conversion currency

    // time passed since comparison measurement

    // theme
}
