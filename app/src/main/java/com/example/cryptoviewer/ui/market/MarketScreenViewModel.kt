package com.example.cryptoviewer.ui.market

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.database.SortOrder
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.network.ApiVsCurrency
import com.example.cryptoviewer.ui.reusables.BaseScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MarketScreenViewModel(application: Application) : BaseScreenViewModel(application) {

    private var batchesFetched: Int = 0
    private var perApiCall: Int = 250

    init {
        fetchAllCryptosFromApi()
    }

    private fun fetchAllCryptosFromApi() {
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

    fun reload() {
        batchesProjected = 0
        _cryptos.value = emptyList()
        // loadNextPage()
    }

    override suspend fun loadNextPageSuspend() {
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
        batchesProjected++
        val currentList = _cryptos.value ?: emptyList()
        val updatedList = currentList + nextBatch
        _cryptos.postValue(updatedList)
    }
}