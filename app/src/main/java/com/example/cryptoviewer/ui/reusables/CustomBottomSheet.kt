package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cryptoviewer.model.CustomSheetState
import com.example.cryptoviewer.network.ApiVsCurrency

@Composable
fun CustomBottomSheet(
    customSheetState: CustomSheetState,
    isCryptoFavourite: Boolean,
    // checkIfFavourite: (String) -> Boolean = { false },
    toggleFavourite: (String) -> Unit = {},
    onConversionCurrencyChanged: (ApiVsCurrency) -> Unit = {},
) {
    when (customSheetState) {
        is CustomSheetState.CryptoDetails ->
            CryptoDetailsSheet(
                cryptoId = customSheetState.cryptoId,
                isCryptoFavourite = isCryptoFavourite,
                // checkIfFavourite = checkIfFavourite,
                toggleFavourite = toggleFavourite
            )
        is CustomSheetState.CurrencyConversion ->
            CurrencyConversionSheet(
                onConversionCurrencyChanged = onConversionCurrencyChanged
        )
        is CustomSheetState.TimeComparison -> TimeComparisonSheet()
    }
}

@Composable
fun CryptoDetailsSheet(
    cryptoId: String,
    isCryptoFavourite: Boolean,
    // checkIfFavourite: (String) -> Boolean = { false },
    toggleFavourite: (String) -> Unit = {}
) {
    // val scrollState = rememberScrollState()
    Row {
        Text(text = "Details for Crypto: $cryptoId")

        Icon(
            imageVector = if (isCryptoFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isCryptoFavourite) "Remove from Favourites" else "Add to Favourites",
            tint = if (isCryptoFavourite) Color.Red else Color.Gray,
            modifier = Modifier.clickable {
                toggleFavourite(cryptoId)
            }
        )
    }
}

@Composable
fun CurrencyConversionSheet(
    onConversionCurrencyChanged: (ApiVsCurrency) -> Unit = {}
) {
    Text(text = "Choose a currency for conversion")
}

@Composable
fun TimeComparisonSheet() {
    Text(text = "Choose a time comparison")
}



