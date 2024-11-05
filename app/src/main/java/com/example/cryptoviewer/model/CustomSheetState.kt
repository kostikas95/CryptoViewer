package com.example.cryptoviewer.model

sealed class CustomSheetState {
    object CurrencyConversion : CustomSheetState()
    object TimeComparison : CustomSheetState()
    data class CryptoDetails(
        val cryptoId: String,
        val cryptoCurrency: CryptoCurrency,
        val cryptoChartData: CryptoChartData
    ) : CustomSheetState()
}