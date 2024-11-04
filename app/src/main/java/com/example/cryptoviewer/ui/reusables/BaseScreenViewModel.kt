package com.example.cryptoviewer.ui.reusables

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseScreenViewModel(application: Application) : AndroidViewModel(application) {

    protected val cryptoDao: CryptoCurrencyDao = CryptoDatabase.getDatabase(application).cryptoDao()
    protected val cryptoApi: CryptoApiService = RetrofitInstance.api
    protected val appContext: Application = application

    protected var offset: Int = 0
    protected val perDbQuery = 20

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
    abstract suspend fun loadNextPageSuspend()
    fun loadNextPage() {
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


}