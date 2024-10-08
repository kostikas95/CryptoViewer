package com.example.cryptoviewer.ui.market

import android.app.Application
import android.util.Log
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MarketViewModel(application: Application) : AndroidViewModel(application) {

    private val cryptoDao: CryptoCurrencyDao = CryptoDatabase.getDatabase(application).cryptoDao()
    private val cryptoApi: CryptoApiService = RetrofitInstance.api
    private val appContext: Application = application


    private val _cryptos = MutableLiveData<List<CryptoCurrency>>(emptyList())
    val cryptos: LiveData<List<CryptoCurrency>> = _cryptos

    // maybe this will not be needed as live data
    // may be needed to flip the arrow which indicates ascending / descending order
    private val _order = MutableLiveData<Pair<SortField, SortOrder>> (
        Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING)
    )
    // val order: LiveData<Pair<SortField, SortOrder>> = _order

    private var batchesFetched: Int = 0
    private var perApiCall: Int = 100
    private var batchesProjected: Int = 0
    private var perDbQuery = 50

    init {
        fetchAllCryptoFromApi()
    }

    private fun fetchAllCryptoFromApi() {
        viewModelScope.launch {
            while (true) {
                try {
                    // Fetch data batch by batch
                    val batch = cryptoApi.fetchCryptos(
                        vsCurrency = ApiVsCurrency.USD,
                        page = batchesFetched + 1,
                        perPage = perApiCall)
                    if (batch.isEmpty()) break
                    Log.d("fetchAllCryptoFromApi", "first crypto in fetched batch: ${batch.first().id}")
                    cryptoDao.insertCryptos(batch)
                    if (batchesProjected == 0) {
                        val fromDb : List<CryptoCurrency> = cryptoDao.getCryptosOrderByMarketCapRankAsc(limit = perDbQuery, offset = 0)
                        batchesProjected++
                        Log.d("fetchAllCryptoFromApi", "first crypto from db: ${batch.first().id}")
                        _cryptos.postValue(fromDb)
                    }
                    batchesFetched++
                    Log.d("fetchAllCryptoFromApi", "${batch.size} cryptos fetch and stored")
                    // Simulate rate limiting pause (based on 429 error or API docs)
                    delay(7000)  // Customize wait time as per API limit

                } catch (e: Exception) {
                    delay(60000)
                    Log.e("fetchAllCryptoFromApi", "${e.message}")
                }
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
        batchesProjected = 0
        loadNextPage()
    }

    fun reload() {
        batchesProjected = 0
        _cryptos.value = emptyList()
        loadNextPage()
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val nextBatch: List<CryptoCurrency> = fetchNextBatchFromDb()
            batchesProjected++
            Log.d("loadNextPage", "${nextBatch.size} cryptos fetched from db")
            val currentList = _cryptos.value ?: emptyList()
            Log.d("loadNextPage", "${currentList.size} cryptos in live data")
            val updatedList = currentList + nextBatch
            _cryptos.postValue(updatedList)
            Log.d("loadNextPage", "${_cryptos.value?.size} cryptos in live data")
        }
    }

    private suspend fun fetchNextBatchFromDb() : List<CryptoCurrency> {
        val currentOrder = _order.value

        var nextBatch: List<CryptoCurrency> = emptyList()
        if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapRankAsc(perDbQuery, batchesProjected * perDbQuery)
        } else if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapRankDsc(perDbQuery, batchesProjected * perDbQuery)
        }
        else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderBySymbolAsc(perDbQuery, batchesProjected * perDbQuery)
        } else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderBySymbolDsc(perDbQuery, batchesProjected * perDbQuery)
        }
        else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByCurrentPriceAsc(perDbQuery, batchesProjected * perDbQuery)
        } else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByCurrentPriceDsc(perDbQuery, batchesProjected * perDbQuery)
        }
        else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByPriceChangePercentageAsc(perDbQuery, batchesProjected * perDbQuery)
        } else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByPriceChangePercentageDsc(perDbQuery, batchesProjected * perDbQuery)
        }
        else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapAsc(perDbQuery, batchesProjected * perDbQuery)
        } else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapDsc(perDbQuery, batchesProjected * perDbQuery)
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