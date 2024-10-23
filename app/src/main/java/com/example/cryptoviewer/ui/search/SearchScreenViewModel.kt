package com.example.cryptoviewer.ui.search

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.database.SortOrder
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.ui.reusables.BaseScreenViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchScreenViewModel(application: Application) : BaseScreenViewModel(application) {

    var searchText = mutableStateOf("")
        private set

    private var searchJob: Job? = null
    private var batchJob: Job? = null



    fun updateSearchText(newText: String) {
        searchText.value = newText

        searchJob?.cancel()
        batchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500)
            _cryptos.postValue(emptyList())
            batchesProjected = 0
            loadNextPageSuspend()
        }

    }

    override suspend fun loadNextPageSuspend() {
        val currentOrder = _order.value
        var nextBatch: List<CryptoCurrency> = emptyList()
        val limit = perDbQuery
        val offset = batchesProjected * perDbQuery
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
        batchesProjected++
        val currentList = _cryptos.value ?: emptyList()
        val updatedList = currentList + nextBatch
        _cryptos.postValue(updatedList)

    }


}