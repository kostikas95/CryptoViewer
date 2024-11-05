package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.cryptoviewer.model.CryptoChartData
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.model.CustomSheetState

@Composable
fun CustomBottomSheet(
    customSheetState: CustomSheetState,
    favouriteIds: Set<String> = emptySet(),
    toggleFavourite: (String) -> Unit = {},
    modifier: Modifier
) {
    when (customSheetState) {
        is CustomSheetState.CryptoDetails ->
            CryptoDetailsSheet(
                cryptoId = customSheetState.cryptoId,
                cryptoCurrency = customSheetState.cryptoCurrency,
                cryptoChartData = customSheetState.cryptoChartData,
                favouriteIds = favouriteIds,
                toggleFavourite = toggleFavourite,
                modifier = modifier
            )
        is CustomSheetState.CurrencyConversion -> CurrencyConversionSheet(
            modifier = modifier
        )
        is CustomSheetState.TimeComparison -> TimeComparisonSheet(
            modifier = modifier
        )
    }
}

@Composable
fun CryptoDetailsSheet(
    cryptoId: String,
    cryptoCurrency: CryptoCurrency,
    cryptoChartData: CryptoChartData,
    favouriteIds: Set<String>,
    toggleFavourite: (String) -> Unit = {},
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    val isFavourite = favouriteIds.contains(cryptoId)
    val painter = rememberAsyncImagePainter(cryptoCurrency.imageUrl)

    ConstraintLayout(
        modifier = modifier
    ) {
        val (image, coin, favIcon, stats) = createRefs()

        // image
        Image(
            modifier = Modifier.size(50.dp, 50.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                },
            painter = painter,
            contentDescription = "crypto image",
            contentScale = ContentScale.Fit
        )

        // coin
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.constrainAs(coin) {
                start.linkTo(image.end, 8.dp)
                top.linkTo(image.top)
                bottom.linkTo(image.bottom)
            }
        ) {
            Text(
                text = cryptoCurrency.symbol,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = cryptoCurrency.name,
                fontSize = 15.sp
            )
        }

        // favIcon
        Icon(
            modifier = Modifier.clickable {
                toggleFavourite(cryptoId)
                }
                .constrainAs(favIcon) {
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                    end.linkTo(parent.end, 32.dp)

                },
            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavourite) "Remove from Favourites" else "Add to Favourites",
            tint = if (isFavourite) Color.Red else Color.Gray,
        )

        // stats
        Column(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .constrainAs(stats) {
                top.linkTo(image.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            CryptoStatsListItem(
                statName = "Market Cap Rank",
                statValue = "#" + cryptoCurrency.marketCapRank.toString()
            )
            CryptoStatsListItem(
                statName = "Market Cap",
                statValue = cryptoCurrency.marketCap.toString()
            )
            // CryptoStatsListItem(
            //     statName = "Total volume",
            //     statValue = cryptoCurrency.marketCap.toString()
            // )
            CryptoStatsListItem(
                statName = "Current Price",
                statValue = cryptoCurrency.currentPrice.toString()
            )
            CryptoStatsListItem(
                statName = "Price Change % 24H",
                statValue = cryptoCurrency.priceChangePercentage24h.toString()
            )
            // CryptoStatsListItem(
            //     statName = "Fully Diluted Valuation",
            //     statValue = cryptoCurrency.marketCap.toString()
            // )
            CryptoStatsListItem(
                statName = "24H High",
                statValue = cryptoCurrency.currentPrice.toString()
            )
            CryptoStatsListItem(
                statName = "24H Low",
                statValue = cryptoCurrency.currentPrice.toString()
            )
            // CryptoStatsListItem(
            //     statName = "ATH",
            //     statValue = cryptoCurrency.currentPrice.toString()
            // )
            // CryptoStatsListItem(
            //     statName = "ATL",
            //     statValue = cryptoCurrency.currentPrice.toString()
            // )
            // CryptoStatsListItem(
            //     statName = "Circulating Supply",
            //     statValue = cryptoCurrency.currentPrice.toString()
            // )
            // CryptoStatsListItem(
            //     statName = "Total Supply",
            //     statValue = cryptoCurrency.currentPrice.toString()
            // )
            // CryptoStatsListItem(
            //     statName = "Max Supply",
            //     statValue = cryptoCurrency.currentPrice.toString()
            // )
        }

        
    }
}

@Composable
fun CurrencyConversionSheet(
    modifier: Modifier
) {
}

@Composable
fun TimeComparisonSheet(
    modifier: Modifier
) {
}



