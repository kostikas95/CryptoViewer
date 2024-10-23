package com.example.cryptoviewer.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.cryptoviewer.network.ApiVsCurrency
import com.example.cryptoviewer.preferences.PreferencesKeys.COMPARISON_TIME
import com.example.cryptoviewer.preferences.PreferencesKeys.CONVERSION_CURRENCY
import com.example.cryptoviewer.preferences.PreferencesKeys.FAVORITE_IDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesDataStore {

    private const val PREFERENCES_NAME = "user_preferences"
    val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

    // favorite IDs
    fun getFavoriteIds(context: Context): Flow<Set<String>> =
        context.dataStore.data.map { preferences ->
            preferences[FAVORITE_IDS] ?: emptySet()
        }
    suspend fun addToFavourites(context: Context, id: String) {
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
    fun getConversionCurrency(context: Context): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[CONVERSION_CURRENCY] ?: ApiVsCurrency.USD.vsCurrency
        }
    suspend fun setConversionCurrency(context: Context, vsCurrency: ApiVsCurrency) {
        context.dataStore.edit { preferences ->
            preferences[CONVERSION_CURRENCY] = vsCurrency.vsCurrency
        }
    }

    // time passed since comparison measurement
    fun getComparisonTimePassed(context: Context): Flow<String?> =
        context.dataStore.data.map { preferences ->
            preferences[COMPARISON_TIME]
        }
    suspend fun setComparisonTimePassed(context: Context, time: String) {
        context.dataStore.edit { preferences ->
            preferences[COMPARISON_TIME] = time
        }
    }

}
