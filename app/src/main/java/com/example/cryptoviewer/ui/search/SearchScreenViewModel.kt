package com.example.cryptoviewer.ui.search

import android.app.Application
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptoviewer.database.CryptoCurrencyDao
import com.example.cryptoviewer.database.CryptoDatabase
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.database.SortOrder
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.model.CustomSheetState
import com.example.cryptoviewer.network.ApiVsCurrency
import com.example.cryptoviewer.network.CryptoApiService
import com.example.cryptoviewer.network.RetrofitInstance
import com.example.cryptoviewer.preferences.PreferencesDataStore
import com.example.cryptoviewer.ui.reusables.BaseScreenViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel(application: Application) : AndroidViewModel(application) {

    protected val cryptoDao: CryptoCurrencyDao = CryptoDatabase.getDatabase(application).cryptoDao()
    protected val cryptoApi: CryptoApiService = RetrofitInstance.api
    protected val appContext: Application = application

    var searchText = mutableStateOf("")
        private set

    private var searchJob: Job? = null
    protected var offset: Int = 0
    protected val perDbQuery = 20
    private lateinit var trendingCoinIds : List<String>

    val favouriteIds: StateFlow<Set<String>> = PreferencesDataStore.favouriteIds
    val conversionCurrency: StateFlow<ApiVsCurrency> = PreferencesDataStore.conversionCurrency

    protected val _cryptos = MutableLiveData<List<CryptoCurrency>>(emptyList())
    val cryptos: LiveData<List<CryptoCurrency>> get() = _cryptos
    protected val _order = MutableLiveData<Pair<SortField, SortOrder>> (
        Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING)
    )
    // val order: LiveData<Pair<SortField, SortOrder>> get() = _order

    val lazyListState: LazyListState by lazy {
        LazyListState()
    }

    private val _isFabVisible = MutableStateFlow(false)
    val isFabVisible: StateFlow<Boolean> = _isFabVisible.asStateFlow()

    var customSheetState by mutableStateOf<CustomSheetState>(CustomSheetState.CurrencyConversion)


    fun isCryptoFavourite(cryptoId: String): Boolean {
        return PreferencesDataStore.isCryptoFavourite(cryptoId)
    }
    fun toggleFavouriteStatus(cryptoId: String) {
        viewModelScope.launch {
            PreferencesDataStore.toggleFavouriteStatus(appContext, cryptoId)
            Log.d("favourites", "base view model: ${favouriteIds.value}")
        }
    }

    fun setConversionCurrency(newCurrency: ApiVsCurrency) {
        viewModelScope.launch {
            PreferencesDataStore.setConversionCurrency(appContext, newCurrency)
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

        _cryptos.postValue(emptyList())
        offset = 0
        viewModelScope.launch {
            loadNextPageSuspend()
        }
    }

    fun monitorLazyListState() = snapshotFlow { lazyListState.firstVisibleItemIndex }
    suspend fun scrollToTop() {
        lazyListState.animateScrollToItem(0)
    }

    fun updateFabVisibility(isVisible: Boolean) {
        _isFabVisible.value = isVisible
    }

    suspend fun showCryptoDetailsSheet(cryptoId: String) {
        customSheetState = CustomSheetState.CryptoDetails(cryptoId)
    }
    suspend fun showCurrencyConversionSheet() {
        customSheetState = CustomSheetState.CurrencyConversion
    }
    suspend fun showTimeComparisonSheet() {
        customSheetState = CustomSheetState.TimeComparison
    }



    init {
        viewModelScope.launch {
            fetchAndStoreTrendingCryptos()
            loadNextPageSuspend()
        }
    }

    fun updateSearchText(newText: String) {
        searchText.value = newText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            offset = 0
            _cryptos.postValue(emptyList())
            loadNextPageSuspend()

            if (_cryptos.value.isNullOrEmpty()) {
                fetchAndStoreRelativeCryptos()
                loadNextPageSuspend()
            }
        }

    }
    fun loadNextPage() {
        viewModelScope.launch {
            loadNextPageSuspend()
        }
    }
    suspend fun loadNextPageSuspend() {
        val currentOrder = _order.value
        var nextBatch: List<CryptoCurrency> = emptyList()
        val text: String = searchText.value

        if (text.isEmpty()) {
            if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByMarketCapRankAsc(perDbQuery, offset, trendingCoinIds)
            } else if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByMarketCapRankDsc(perDbQuery, offset, trendingCoinIds)
            }
            else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderBySymbolAsc(perDbQuery, offset, trendingCoinIds)
            } else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderBySymbolDsc(perDbQuery, offset, trendingCoinIds)
            }
            else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByCurrentPriceAsc(perDbQuery, offset, trendingCoinIds)
            } else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByCurrentPriceDsc(perDbQuery, offset, trendingCoinIds)
            }
            else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByPriceChangePercentageAsc(perDbQuery, offset, trendingCoinIds)
            } else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByPriceChangePercentageDsc(perDbQuery, offset, trendingCoinIds)
            }
            else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByMarketCapAsc(perDbQuery, offset, trendingCoinIds)
            } else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getCryptosByIdsOrderByMarketCapDsc(perDbQuery, offset, trendingCoinIds)
            }
            offset += nextBatch.size
            val currentList = _cryptos.value ?: emptyList()
            val updatedList = currentList + nextBatch
            Log.d("searchViewModel", "${nextBatch.size} more trending cryptos found in db")
            _cryptos.postValue(updatedList)
        }
        else {
            if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByMarketCapRankAsc(perDbQuery, offset, text)
            } else if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByMarketCapRankDsc(perDbQuery, offset, text)
            }
            else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderBySymbolAsc(perDbQuery, offset, text)
            } else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderBySymbolDsc(perDbQuery, offset, text)
            }
            else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByCurrentPriceAsc(perDbQuery, offset, text)
            } else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByCurrentPriceDsc(perDbQuery, offset, text)
            }
            else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByPriceChangePercentageAsc(perDbQuery, offset, text)
            } else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByPriceChangePercentageDsc(perDbQuery, offset, text)
            }
            else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.ASCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByMarketCapAsc(perDbQuery, offset, text)
            } else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.DESCENDING) {
                nextBatch = cryptoDao.getRelativeCryptosOrderByMarketCapDsc(perDbQuery, offset, text)
            }
            offset += nextBatch.size
            val currentList = _cryptos.value ?: emptyList()
            val updatedList = currentList + nextBatch
            Log.d("searchViewModel", "${nextBatch.size} more cryptos relative to text found in db")
            _cryptos.postValue(updatedList)
        }
    }

    private suspend fun fetchAndStoreTrendingCryptos() {
        try {
            val response = cryptoApi.fetchTrendingCoinIds()
            trendingCoinIds = response.coins.map { it.item.id }
            Log.d("searchViewModel", "got ${trendingCoinIds.size} trending coin ids from the api")

            val trendingCryptos = cryptoApi.fetchCryptos(
                vsCurrency = ApiVsCurrency.USD,
                ids = trendingCoinIds.joinToString(separator = ",")
            )
            Log.d("searchViewModel", "fetched ${trendingCryptos.size} trending coins from the api")
            cryptoDao.insertCryptos(trendingCryptos)
        } catch (e: Exception) {
            Log.e("searchViewModel", "Error fetching trending cryptos: ${e.message}")
        }
    }

    private suspend fun fetchAndStoreRelativeCryptos() {
        try {
            val text: String = searchText.value
            val response = cryptoApi.fetchRelativeCoinIds(text)
            val relativeCoinIds = response.coins.map { it.item.id }
            Log.d("searchViewModel", "got ${relativeCoinIds.size} relative coin ids from the api")

            val relativeCryptos = cryptoApi.fetchCryptos(
                vsCurrency = ApiVsCurrency.USD,
                ids = relativeCoinIds.joinToString(separator = ",")
            )
            Log.d("searchViewModel", "fetched ${relativeCoinIds.size} relative coins from the api")
            cryptoDao.insertCryptos(relativeCryptos)
        } catch (e: Exception) {
            Log.e("searchViewModel", "Error fetching relative cryptos: ${e.message}")
        }
    }
}