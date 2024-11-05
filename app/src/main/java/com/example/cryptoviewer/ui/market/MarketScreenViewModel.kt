package com.example.cryptoviewer.ui.market

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarketScreenViewModel(application: Application) : AndroidViewModel(application) {

    protected val cryptoDao: CryptoCurrencyDao = CryptoDatabase.getDatabase(application).cryptoDao()
    protected val cryptoApi: CryptoApiService = RetrofitInstance.api
    protected val appContext: Application = application

    protected var offset: Int = 0
    protected val perDbQuery = 20
    private var batchesFetched: Int = 0
    private var perApiCall: Int = 250

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


    init {
        // change that poop
        // fetchAllCryptosFromApi()
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
                    if (offset == 0) {
                        val fromDb : List<CryptoCurrency> = cryptoDao.getCryptosOrderByMarketCapRankAsc(limit = perDbQuery, offset = 0)
                        offset += fromDb.size
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
        offset = 0
        _cryptos.value = emptyList()
        // loadNextPage()
    }

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

    fun loadNextPage() {
        viewModelScope.launch {
            loadNextPageSuspend()
        }
    }
    private suspend fun loadNextPageSuspend() {
        val currentOrder = _order.value
        var nextBatch: List<CryptoCurrency> = emptyList()
        if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapRankAsc(perDbQuery, offset)
        } else if (currentOrder?.first == SortField.MARKET_CAP_RANK && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapRankDsc(perDbQuery, offset)
        }
        else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderBySymbolAsc(perDbQuery, offset)
        } else if (currentOrder?.first == SortField.SYMBOL && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderBySymbolDsc(perDbQuery, offset)
        }
        else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByCurrentPriceAsc(perDbQuery, offset)
        } else if (currentOrder?.first == SortField.CURRENT_PRICE && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByCurrentPriceDsc(perDbQuery, offset)
        }
        else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByPriceChangePercentageAsc(perDbQuery, offset)
        } else if (currentOrder?.first == SortField.PRICE_CHANGE && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByPriceChangePercentageDsc(perDbQuery, offset)
        }
        else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.ASCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapAsc(perDbQuery, offset)
        } else if (currentOrder?.first == SortField.MARKET_CAP && currentOrder.second == SortOrder.DESCENDING) {
            nextBatch = cryptoDao.getCryptosOrderByMarketCapDsc(perDbQuery, offset)
        }
        offset += nextBatch.size
        val currentList = _cryptos.value ?: emptyList()
        val updatedList = currentList + nextBatch
        _cryptos.postValue(updatedList)
    }

    fun monitorLazyListState() = snapshotFlow { lazyListState.firstVisibleItemIndex }
    suspend fun scrollToTop() {
        lazyListState.animateScrollToItem(0)
    }

    fun updateFabVisibility(isVisible: Boolean) {
        _isFabVisible.value = isVisible
    }

    suspend fun showCryptoDetailsSheet(cryptoId: String) {
        val cryptoChartData = cryptoApi.getCryptoChartData(
            id = cryptoId,
            vsCurrency = ApiVsCurrency.USD,
            days = "30"
        )
        val cryptoCurrency = cryptoDao.getCryptoById(cryptoId)
        customSheetState = CustomSheetState.CryptoDetails(cryptoId, cryptoCurrency, cryptoChartData)
    }
    suspend fun showCurrencyConversionSheet() {
        customSheetState = CustomSheetState.CurrencyConversion
    }
    suspend fun showTimeComparisonSheet() {
        customSheetState = CustomSheetState.TimeComparison
    }
}