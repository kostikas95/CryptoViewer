package com.example.cryptoviewer.ui.reusables

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.example.cryptoviewer.formatDateISO8601
import com.example.cryptoviewer.formatTimestampsDouble
import com.example.cryptoviewer.model.CryptoChartData
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.model.CustomSheetState
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties

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
            .verticalScroll(scrollState)
            .heightIn(min = 0.dp, max = 1000.dp)
    ) {
        val (image, coin, favIcon, chart, stats) = createRefs()

        // image
        Image(
            modifier = Modifier.size(50.dp, 50.dp)
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                }.border(1.dp, Color.Red),
            painter = painter,
            contentDescription = "crypto image",
            contentScale = ContentScale.Fit
        )

        // coin
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.constrainAs(coin) {
                start.linkTo(image.end, 16.dp)
                top.linkTo(image.top)
                bottom.linkTo(image.bottom)
            }.border(1.dp, Color.Red)
        ) {
            Text(
                text = cryptoCurrency.symbol.uppercase(),
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
                }.border(1.dp, Color.Red),
            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavourite) "Remove from Favourites" else "Add to Favourites",
            tint = if (isFavourite) Color.Red else Color.Gray,
        )


        // chart
        LineChart(
            modifier = Modifier
                .height(200.dp)
                .constrainAs(chart) {
                    top.linkTo(image.bottom, 40.dp)
                    start.linkTo(parent.start, 40.dp)
                    end.linkTo(parent.end, 40.dp)
                    width = Dimension.fillToConstraints
                }.border(1.dp, Color.Red),
            gridProperties = GridProperties(enabled = false),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                textStyle = TextStyle.Default.copy(fontSize = 10.sp),
                contentBuilder = {
                    DecimalFormat("0.##E0").format(it)
                }
            ),
            labelProperties = LabelProperties(
                enabled = true,
                textStyle = TextStyle.Default.copy(fontSize = 10.sp),
                labels = formatTimestampsDouble(cryptoChartData.timestamps),
                rotationDegreeOnSizeConflict = -45f
            ),
            popupProperties = PopupProperties(
                enabled = true,
                textStyle = TextStyle.Default.copy(fontSize = 10.sp),
                containerColor = Color(0xFF23af92),
                contentBuilder = {
                    DecimalFormat("0.##E0").format(it)
                }
            ),
            data = listOf(
                Line(
                        label = "Market Cap",
                        values = cryptoChartData.marketCaps,
                        color = SolidColor(Color(0xFF23af92)),
                        firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width = 2.dp),
                    )
            ),
            minValue = cryptoChartData.marketCaps.min(),
            maxValue = cryptoChartData.marketCaps.max(),
            animationMode = AnimationMode.Together(
                delayBuilder = {
                it * 500L
            } ),

        )

        // stats
        Column(
            modifier = modifier
                .padding(horizontal = 30.dp)
                .constrainAs(stats) {
                    top.linkTo(chart.bottom, 20.dp)
                }.border(1.dp, Color.Red)
        ) {
            CryptoStatsListItem(
                statName = "Market Cap Rank",
                statValue = "#" + cryptoCurrency.marketCapRank.toString()
            )
            CryptoStatsListItem(
                statName = "Market Cap",
                statValue = cryptoCurrency.marketCap.toString()
            )
            CryptoStatsListItem(
                statName = "Total volume",
                statValue = cryptoCurrency.totalVolume.toString()
            )
            CryptoStatsListItem(
                statName = "Current Price",
                statValue = cryptoCurrency.currentPrice.toString()
            )
            CryptoStatsListItem(
                statName = "Price Change % 24H",
                statValue = cryptoCurrency.priceChangePercentage24h.toString()
            )
            CryptoStatsListItem(
                statName = "Fully Diluted Valuation",
                statValue = cryptoCurrency.fullyDilutedValuation.toString()
            )
            CryptoStatsListItem(
                statName = "ATH",
                statValue = cryptoCurrency.ath.toString() + " @ " + formatDateISO8601(cryptoCurrency.athDate)
            )
            CryptoStatsListItem(
                statName = "ATL",
                statValue = cryptoCurrency.atl.toString() + " @ " + formatDateISO8601(cryptoCurrency.atlDate)
            )
            CryptoStatsListItem(
                statName = "Circulating Supply",
                statValue = cryptoCurrency.circulatingSupply.toString()
            )
            CryptoStatsListItem(
                statName = "Total Supply",
                statValue = cryptoCurrency.totalSupply.toString()
            )
            CryptoStatsListItem(
                statName = "Max Supply",
                statValue = cryptoCurrency.maxSupply.toString()
            )
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



