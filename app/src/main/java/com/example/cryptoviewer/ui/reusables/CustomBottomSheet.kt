package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.cryptoviewer.model.CustomSheetState

@Composable
fun CustomBottomSheet(customSheetState: CustomSheetState) {

    val scrollState = rememberScrollState()

    when (customSheetState) {
        is CustomSheetState.CryptoDetails -> CryptoDetailsSheet(cryptoId = customSheetState.cryptoId)
        is CustomSheetState.CurrencyConversion -> CurrencyConversionSheet()
        is CustomSheetState.TimeComparison -> TimeComparisonSheet()
    }
}

@Composable
fun CryptoDetailsSheet(cryptoId: String) {
    Text(text = "Details for Crypto: $cryptoId")
}

@Composable
fun CurrencyConversionSheet() {
    Text(text = "Choose a currency for conversion")
}

@Composable
fun TimeComparisonSheet() {
    Text(text = "Choose a time comparison")
}



