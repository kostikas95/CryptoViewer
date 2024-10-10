package com.example.cryptoviewer.ui.search

import android.app.Application
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptoviewer.database.CryptoCurrencyDao
import com.example.cryptoviewer.database.CryptoDatabase
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.database.SortOrder
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.network.CryptoApiService
import com.example.cryptoviewer.network.RetrofitInstance
import com.example.cryptoviewer.preferences.PreferencesDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val cryptoDao: CryptoCurrencyDao = CryptoDatabase.getDatabase(application).cryptoDao()
    private val cryptoApi: CryptoApiService = RetrofitInstance.api
    private val appContext: Application = application

    private val _cryptos = MutableLiveData<List<CryptoCurrency>>(emptyList())
    val cryptos: LiveData<List<CryptoCurrency>> = _cryptos

    private val _order = MutableLiveData<Pair<SortField, SortOrder>> (
        Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING)
    )
    // val order: LiveData<Pair<SortField, SortOrder>> = _order

    var searchText = mutableStateOf("")
        private set

    val lazyListState: LazyListState by lazy {
        LazyListState()
    }

    private var searchJob: Job? = null
    private var batchJob: Job? = null
    private var batchesProjected: Int = 0
    private var perDbQuery = 20

    fun debug() {
        Log.d("ViewModel", "navigated back to SearchScreen")
        Log.d("ViewModel", "_cryptos: ${_cryptos.value?.size} cryptos.")
        Log.d("ViewModel", "order by ${_order.value?.first} ${_order.value?.second}")
        Log.d("ViewModel", "batches projected: ${batchesProjected}")
    }

    init {
        Log.d("ViewModel", "ViewModel initialized")
    }

    fun updateSearchText(newText: String) {
        searchText.value = newText

        searchJob?.cancel()
        batchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500)
            _cryptos.postValue(emptyList())
            batchesProjected = 0
            loadNextPage()
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
        batchesProjected = 0
        batchJob = viewModelScope.launch {
            loadNextPage()
        }
    }

    suspend fun loadNextPage() {
        val nextBatch: List<CryptoCurrency> = fetchNextBatchFromDb(
            limit = perDbQuery,
            offset = batchesProjected * perDbQuery)
        batchesProjected++
        Log.d("loadNextPage", "${nextBatch.size} cryptos fetched from db")
        val currentList = _cryptos.value ?: emptyList()
        Log.d("loadNextPage", "${currentList.size} cryptos in live data")
        val updatedList = currentList + nextBatch
        _cryptos.postValue(updatedList)
        Log.d("loadNextPage", "${_cryptos.value?.size} cryptos in live data")
    }

    private suspend fun fetchNextBatchFromDb(limit: Int, offset: Int) : List<CryptoCurrency> {
        val currentOrder = _order.value
        var nextBatch: List<CryptoCurrency> = emptyList()
        val text: String = searchText.value
        if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByMarketCapRankAsc(limit, offset, text)
        } else if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByMarketCapRankDsc(limit, offset, text)
        }
        else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderBySymbolAsc(limit, offset, text)
        } else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderBySymbolDsc(limit, offset, text)
        }
        else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByCurrentPriceAsc(limit, offset, text)
        } else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByCurrentPriceDsc(limit, offset, text)
        }
        else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByPriceChangePercentageAsc(limit, offset, text)
        } else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByPriceChangePercentageDsc(limit, offset, text)
        }
        else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByMarketCapAsc(limit, offset, text)
        } else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosLikeOrderByMarketCapDsc(limit, offset, text)
        }
        return nextBatch
    }

    fun addToFavourites(id: String) {
        viewModelScope.launch {
            PreferencesDataStore.addToFavorites(appContext, id)
        }
    }

    fun removeFromFavourites(id: String) {
        viewModelScope.launch {
            PreferencesDataStore.removeFromFavourites(appContext, id)
        }
    }
}