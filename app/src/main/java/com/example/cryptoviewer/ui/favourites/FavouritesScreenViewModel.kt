package com.example.cryptoviewer.ui.favourites

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.database.SortOrder
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.network.ApiVsCurrency
import com.example.cryptoviewer.preferences.PreferencesDataStore
import com.example.cryptoviewer.ui.reusables.BaseScreenViewModel
import kotlinx.coroutines.launch


class FavouritesScreenViewModel(application: Application) : BaseScreenViewModel(application) {

    init {
        viewModelScope.launch {
            loadNextPageSuspend()
        }
    }

    override suspend fun loadNextPageSuspend() {

        Log.d("favViewModel", "favourite number: ${_cryptos.value?.size}")
        for (crypto: CryptoCurrency in _cryptos.value!!) {
            Log.d("favViewModel", "${crypto.name}   ${crypto.symbol}")
        }

        PreferencesDataStore.getConversionCurrency(appContext).collect { vsCurrency ->
            val fetched: List<CryptoCurrency> = cryptoApi.fetchCryptos(
                vsCurrency = ApiVsCurrency.entries.find { it.vsCurrency == vsCurrency }!!,
                ids = _favouriteIds.value.joinToString(", ")
            )
            cryptoDao.insertCryptos(fetched)
            val currentOrder = _order.value
            val favouriteIds = _favouriteIds.value.toList()
            var cryptosFromDb: List<CryptoCurrency> = emptyList()
            if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.ASCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapRankAsc(favouriteIds)
            } else if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.DESCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapRankDsc(favouriteIds)
            }
            else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.ASCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderBySymbolAsc(favouriteIds)
            } else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.DESCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderBySymbolDsc(favouriteIds)
            }
            else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.ASCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByCurrentPriceAsc(favouriteIds)
            } else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.DESCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByCurrentPriceDsc(favouriteIds)
            }
            else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.ASCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByPriceChangePercentageAsc(favouriteIds)
            } else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.DESCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByPriceChangePercentageDsc(favouriteIds)
            }
            else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.ASCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapAsc(favouriteIds)
            } else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.DESCENDING) {
                cryptosFromDb = cryptoDao.getCryptosByIdsOrderByMarketCapDsc(favouriteIds)
            }


            _cryptos.postValue(cryptosFromDb)
        }



    }

}