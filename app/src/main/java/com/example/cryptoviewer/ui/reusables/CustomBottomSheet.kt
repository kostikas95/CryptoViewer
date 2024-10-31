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
import com.example.cryptoviewer.model.CustomSheetState
import com.example.cryptoviewer.network.ApiVsCurrency

@Composable
fun CustomBottomSheet(
    customSheetState: CustomSheetState,
    favouriteIds: Set<String> = emptySet(),
    toggleFavourite: (String) -> Unit = {},
    onConversionCurrencyChanged: (ApiVsCurrency) -> Unit = {},
) {
    when (customSheetState) {
        is CustomSheetState.CryptoDetails ->
            CryptoDetailsSheet(
                cryptoId = customSheetState.cryptoId,
                favouriteIds = favouriteIds,
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
    favouriteIds: Set<String>,
    toggleFavourite: (String) -> Unit = {}
) {
    // val scrollState = rememberScrollState()
    val isFavourite = favouriteIds.contains(cryptoId)
    Row {
        Text(text = "Details for Crypto: $cryptoId")

        Icon(
            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavourite) "Remove from Favourites" else "Add to Favourites",
            tint = if (isFavourite) Color.Red else Color.Gray,
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



