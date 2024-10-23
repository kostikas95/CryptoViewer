package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoviewer.model.CustomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    customSheetState: CustomSheetState
) {
    val scrollState = rememberScrollState()

    when (customSheetState) {
        is CustomSheetState.CryptoDetails ->
            CryptoDetailsSheet(cryptoId = customSheetState.cryptoId)
        is CustomSheetState.CurrencyConversion -> CurrencyConversionSheet()
        is CustomSheetState.TimeComparison -> TimeComparisonSheet()
    }
}

@Composable
fun CryptoDetailsSheet(
    cryptoId: String
) {
    Row {
        Text(text = "Details for Crypto: $cryptoId")

        Icon(
            imageVector = Icons.Filled.FavoriteBorder,
            contentDescription = "favourite toggle",
        )
    }
}

@Composable
fun CurrencyConversionSheet() {
    Text(text = "Choose a currency for conversion")
}

@Composable
fun TimeComparisonSheet() {
    Text(text = "Choose a time comparison")
}



