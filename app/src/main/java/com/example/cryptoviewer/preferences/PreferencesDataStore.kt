package com.example.cryptoviewer.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.cryptoviewer.network.ApiVsCurrency
import com.example.cryptoviewer.preferences.PreferencesKeys.CONVERSION_CURRENCY
import com.example.cryptoviewer.preferences.PreferencesKeys.FAVOURITE_IDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

object PreferencesDataStore {

    private const val PREFERENCES_NAME = "user_preferences"
    val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)


    fun initPreferences(context: Context, scope: CoroutineScope) {
        context.dataStore.data.map { preferences ->
            ApiVsCurrency.valueOf(preferences[CONVERSION_CURRENCY] ?: "USD")
        }.onEach { currency ->
            _conversionCurrency.value = currency
        }.launchIn(scope)

        context.dataStore.data.map { preferences ->
            preferences[FAVOURITE_IDS] ?: emptySet()
        }.onEach { favorites ->
            _favouriteIds.value = favorites
        }.launchIn(scope)
    }


    // favorite IDs
    private val _favouriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favouriteIds: StateFlow<Set<String>> get() = _favouriteIds

    fun isCryptoFavourite(cryptoId: String): Boolean {
        return _favouriteIds.value.contains(cryptoId)
    }
    suspend fun toggleFavouriteStatus(context: Context, cryptoId: String) {
        val updatedFavourites = if (isCryptoFavourite(cryptoId)) _favouriteIds.value - cryptoId
        else _favouriteIds.value + cryptoId
        context.dataStore.edit { preferences ->
            preferences[FAVOURITE_IDS] = updatedFavourites
        }
        _favouriteIds.value = updatedFavourites
        Log.d("favourites", "datastore: ${_favouriteIds.value}")
    }

    // conversion currency
    private val _conversionCurrency = MutableStateFlow(ApiVsCurrency.USD)
    val conversionCurrency: StateFlow<ApiVsCurrency> get() =  _conversionCurrency

    suspend fun setConversionCurrency(context: Context, newCurrency: ApiVsCurrency) {
        context.dataStore.edit { preferences ->
            preferences[CONVERSION_CURRENCY] = newCurrency.name
        }
        _conversionCurrency.value = newCurrency
    }

    // // time passed since comparison measurement
    // fun getComparisonTimePassed(context: Context): Flow<String?> =
    //     context.dataStore.data.map { preferences ->
    //         preferences[COMPARISON_TIME]
    //     }
    // suspend fun setComparisonTimePassed(context: Context, time: String) {
    //     context.dataStore.edit { preferences ->
    //         preferences[COMPARISON_TIME] = time
    //     }
    // }

}
