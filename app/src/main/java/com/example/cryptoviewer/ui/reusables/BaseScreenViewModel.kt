package com.example.cryptoviewer.ui.reusables

import android.app.Application
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



    protected val _conversionCurrency = MutableStateFlow(ApiVsCurrency.USD)
    val conversionCurrency: StateFlow<ApiVsCurrency> = _conversionCurrency

    protected val _favouriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favouriteIds: StateFlow<Set<String>> = _favouriteIds

    protected val _isCryptoFavourite = MutableStateFlow(false)
    val isCryptoFavourite: StateFlow<Boolean> = _isCryptoFavourite

    init {
        viewModelScope.launch {
            PreferencesDataStore.getFavouriteIds(appContext).collect { favouriteIds ->
                _favouriteIds.value = favouriteIds
            }
        }
    }

    protected var batchesProjected: Int = 0
    protected val perDbQuery = 20
    protected val _cryptos = MutableLiveData<List<CryptoCurrency>>(emptyList())
    val cryptos: LiveData<List<CryptoCurrency>> = _cryptos
    protected val _order = MutableLiveData<Pair<SortField, SortOrder>> (
        Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING)
    )
    // val order: LiveData<Pair<SortField, SortOrder>> = _order
    fun changeOrder(newField : SortField) {
        val currentOrder : Pair<SortField, SortOrder>? = _order.value
        if (currentOrder == null) _order.postValue(Pair(newField, SortOrder.ASCENDING))
        else if (currentOrder.first == newField)
            _order.postValue(Pair(currentOrder.first, currentOrder.second.toggle()))

        else if (newField == SortField.MARKET_CAP_RANK || newField == SortField.SYMBOL)
            _order.postValue(Pair(newField, SortOrder.ASCENDING))
        else _order.postValue(Pair(newField, SortOrder.DESCENDING))

        _cryptos.postValue(emptyList())
        batchesProjected = 0
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




    val lazyListState: LazyListState by lazy {
        LazyListState()
    }
    fun monitorLazyListState() = snapshotFlow { lazyListState.firstVisibleItemIndex }
    suspend fun scrollToTop() {
        lazyListState.animateScrollToItem(0)
    }




    private val _isFabVisible = MutableStateFlow(false)
    val isFabVisible: StateFlow<Boolean> = _isFabVisible.asStateFlow()
    fun updateFabVisibility(isVisible: Boolean) {
        _isFabVisible.value = isVisible
    }



    var customSheetState by mutableStateOf<CustomSheetState>(CustomSheetState.CurrencyConversion)

    suspend fun showCryptoDetailsSheet(cryptoId: String) {
        customSheetState = CustomSheetState.CryptoDetails(cryptoId)
    }
    fun checkIfFavourite(cryptoId: String) : Boolean {
        _isCryptoFavourite.value = _favouriteIds.value.contains(cryptoId)
        return _isCryptoFavourite.value
    }
    fun addToFavourites(cryptoId: String) {
        viewModelScope.launch {
            PreferencesDataStore.addToFavourites(appContext, cryptoId)
        }
    }
    fun removeFromFavourites(cryptoId: String) {
        viewModelScope.launch {
            PreferencesDataStore.removeFromFavourites(appContext, cryptoId)
        }
    }
    // fun toggleFavourite(cryptoId: String) {
    //     viewModelScope.launch {
    //         if (checkIfFavourite(cryptoId)) {
    //             PreferencesDataStore.removeFromFavourites(appContext, cryptoId)
    //         }
    //         else {
    //             PreferencesDataStore.addToFavourites(appContext, cryptoId)
    //         }
    //     }
    // }
    suspend fun showCurrencyConversionSheet() {
        customSheetState = CustomSheetState.CurrencyConversion
    }
    suspend fun showTimeComparisonSheet() {
        customSheetState = CustomSheetState.TimeComparison
    }


}