package com.example.cryptoviewer.ui.favourites

import android.app.Application
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptoviewer.database.CryptoCurrencyDao
import com.example.cryptoviewer.database.CryptoDatabase
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.database.SortOrder
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.network.ApiVsCurrency
import com.example.cryptoviewer.network.CryptoApiService
import com.example.cryptoviewer.network.RetrofitInstance
import com.example.cryptoviewer.preferences.PreferencesDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class FavouritesViewModel(application: Application) : AndroidViewModel(application) {

    private val cryptoDao: CryptoCurrencyDao = CryptoDatabase.getDatabase(application).cryptoDao()
    private val cryptoApi: CryptoApiService = RetrofitInstance.api
    private val appContext: Application = application


    val favoriteIdsFlow: Flow<Set<String>> = PreferencesDataStore.getFavoriteIds(appContext)

    private val _cryptos = MutableLiveData<List<CryptoCurrency>>(emptyList())
    val cryptos: LiveData<List<CryptoCurrency>> = _cryptos

    private val _order = MutableLiveData<Pair<SortField, SortOrder>> (
        Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING)
    )
    // val order: LiveData<Pair<SortField, SortOrder>> = _order

    val lazyListState: LazyListState by lazy {
        LazyListState()
    }

    private var batchJob: Job? = null

    fun debug() {
        Log.d("ViewModel", "navigated back to FavouritesScreen")
        Log.d("ViewModel", "_cryptos: ${_cryptos.value?.size} cryptos.")
        Log.d("ViewModel", "order by ${_order.value?.first} ${_order.value?.second}")
    }

    init {
        Log.d("ViewModel", "ViewModel initialized")
        viewModelScope.launch {
            PreferencesDataStore.getFavoriteIds(appContext).collect { favourites ->
                // val fetched : List<CryptoCurrency> = cryptoApi.fetchCryptos(
                //     vsCurrency = ApiVsCurrency.USD,
                //     ids = favourites.joinToString(", ")
                // )
                // cryptoDao.insertCryptos(fetched)
                _cryptos.postValue(fetchFavouritesFromDb(favourites.toList()))
            }
        }
    }

    fun changeOrder(newField : SortField) {
        val currentOrder : Pair<SortField, SortOrder>? = _order.value
        if (currentOrder == null) _order.postValue(Pair(newField, SortOrder.ASCENDING))
        else if (currentOrder.first == newField)
            _order.postValue(Pair(currentOrder.first, currentOrder.second.toggle()))

        else if (newField == SortField.MARKET_CAP_RANK || newField == SortField.SYMBOL)
            _order.postValue(Pair(newField, SortOrder.ASCENDING))
        else _order.postValue(Pair(newField, SortOrder.DESCENDING))

        Log.d("changeOrder", "new order: ${_order.value?.first}    ${_order.value?.second}")

        _cryptos.postValue(emptyList())
        batchJob = viewModelScope.launch {
            val favourites = PreferencesDataStore.getFavoriteIds(appContext).first().toList()
            fetchFavouritesFromDb(favourites)
        }
    }

    private suspend fun fetchFavouritesFromDb(ids: List<String>) : List<CryptoCurrency> {
        val currentOrder = _order.value

        var cryptosFromDb: List<CryptoCurrency> = emptyList()

        if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.ASCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapRankAsc(ids)
        } else if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.DESCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapRankDsc(ids)
        }
        else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.ASCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderBySymbolAsc(ids)
        } else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.DESCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderBySymbolDsc(ids)
        }
        else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.ASCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByCurrentPriceAsc(ids)
        } else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.DESCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByCurrentPriceDsc(ids)
        }
        else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.ASCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByPriceChangePercentageAsc(ids)
        } else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.DESCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByPriceChangePercentageDsc(ids)
        }
        else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.ASCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapAsc(ids)
        } else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.DESCENDING) {
            cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapDsc(ids)
        }
        return cryptosFromDb
    }

    fun addToFavourites(id: String) {
        viewModelScope.launch {
            PreferencesDataStore.addToFavorites(appContext, id)

            // scalable, slow
            val favourites = PreferencesDataStore.getFavoriteIds(appContext).first().toList()
            _cryptos.postValue(fetchFavouritesFromDb(favourites))

            // not scalable, faster
            // _cryptos.postValue(_cryptos.value?.plus(fetchFavouritesFromDb(listOf(id))))
        }
    }

    fun removeFromFavourites(id: String) {
        viewModelScope.launch {
            PreferencesDataStore.removeFromFavourites(appContext, id)
        }
    }
}