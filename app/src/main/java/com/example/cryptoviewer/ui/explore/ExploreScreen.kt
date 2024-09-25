package com.example.cryptoviewer.ui.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptoviewer.R
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.formatNumberForDisplay
import com.example.cryptoviewer.model.CryptoCurrency
import kotlinx.coroutines.flow.filter
import java.util.Locale

@Composable
fun ExploreScreen() {
    // view model
    val viewModel : ExploreViewModel = viewModel()

    // states and data
    val cryptos by viewModel.cryptos.observeAsState(emptyList())
    val lazyListState = rememberLazyListState(0)
    // val order by viewModel.order.observeAsState(Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING))

    // lambdas
    val onSortingFactorTextClick : (SortField) -> Unit = { newField ->
        viewModel.changeOrder(newField)
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filter { it == lazyListState.layoutInfo.totalItemsCount - 1 }
            .collect {
                viewModel.loadNextPage()
            }
    }
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() },
        floatingActionButton = { AutoScrollToTopButton() },
        content = { innerPadding ->
            ScrollableList(innerPadding,
                cryptos,
                lazyListState,
                onSortingFactorTextClick
            )
        //  { viewModel.loadNextPage() }
        }
    )
}

@Composable
fun TopBar() {
    /*
    <screen title>                              <app logo>

    <dropdownList       <dropdownList      <dropdownList
    for vsCurrency>    for item number>     for field that
                                           change with time>
     */

    Column(
        modifier = Modifier.height(65.dp)
            .background(Color.Red)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Explore",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(R.drawable.top_app_bar_image),
                contentDescription = "topAppBarImage"
            )
        }

        Row {

        }
    }

}

@Composable
fun BottomBar() {
    /*
    <explore icon>     <search icon>       <favourites icon>
    */
    Row(
        modifier = Modifier.fillMaxWidth(1f)
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.chart_line),
            contentDescription = "explore image",
            modifier = Modifier.size(32.dp, 32.dp),
            contentScale = ContentScale.Fit
        )
        Image(
            painterResource(R.drawable.search),
            contentDescription = "search image",
            modifier = Modifier.size(32.dp, 32.dp),
            contentScale = ContentScale.Fit
        )
        Image(
            painterResource(R.drawable.heart),
            contentDescription = "favourites image",
            modifier = Modifier.size(32.dp, 32.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun AutoScrollToTopButton() {

}

// pass cryptos here
@Composable
fun ScrollableList(
    innerPadding: PaddingValues,
    cryptos: List<CryptoCurrency>,
    lazyListState: LazyListState,
    onSortingFactorTextClick: (SortField) -> Unit
    // onLoadMore : suspend () -> Unit
) {
    /*
    (tapping on these will change the order ASC <-> DESC)
    <coin name>     <current price>    <time>     <market cap>
    ITEM
    ITEM
    ITEM
    ...
     */
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
    ) {
        // ScrollableListHeader(onSortingFactorTextClick)
        Row(
            modifier = Modifier.fillMaxWidth(1f)
                .background((Color.Yellow)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f)
                    .clickable {
                    onSortingFactorTextClick(SortField.MARKET_CAP_RANK)
                    },
                text = "#",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
            Text(
                modifier = Modifier.weight(1f),
                //     .clickable {
                //         onSortingFactorTextClick(SortField.NAME)
                //     },
                text = "name",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
            Text(
                modifier = Modifier.weight(1f),
                    // .clickable {
                    //     onSortingFactorTextClick(SortField.CURRENT_PRICE)
                    // },
                text = "current\nprice",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
            Text(
                modifier = Modifier.weight(1f),
                    // .clickable {
                    //     onSortingFactorTextClick(SortField.PRICE_CHANGE)
                    // },
                text = "price\nchange(%)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "market\ncap",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.background(Color.Blue)
        ) {
            items(items = cryptos, key = {it.id}) { crypto ->
                // Pass the crypto data to the ListItem
                ListItem(crypto)
            }
        }
        // LaunchedEffect(lazyListState) {
        //     val visibleItemsInfo = lazyListState.layoutInfo.visibleItemsInfo
        //     if (visibleItemsInfo.isNotEmpty() && lazyListState.firstVisibleItemIndex > cryptos.size - 10) {
        //         onLoadMore()
        //     }
        // }
    }
}

@Composable
fun ScrollableListHeader(
    onSortingFactorTextClick: (SortField) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f)
            .background((Color.Yellow)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            //     .clickable {
            //     onSortingFactorTextClick(SortField.MARKET_CAP_RANK)
            // },
            text = "#",
            fontSize = 16.sp,
            fontWeight = FontWeight.Thin,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            softWrap = true
        )
        Text(
            modifier = Modifier.weight(1f)
                .clickable {
                onSortingFactorTextClick(SortField.NAME)
            },
            text = "name",
            fontSize = 16.sp,
            fontWeight = FontWeight.Thin,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            softWrap = true
        )
        Text(
            modifier = Modifier.weight(1f).clickable {
                onSortingFactorTextClick(SortField.CURRENT_PRICE)
            },
            text = "current\nprice",
            fontSize = 16.sp,
            fontWeight = FontWeight.Thin,
            maxLines = 2,
            lineHeight = 18.sp,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            softWrap = true
        )
        Text(
            modifier = Modifier.weight(1f).clickable {
                onSortingFactorTextClick(SortField.PRICE_CHANGE)
            },
            text = "price\nchange(%)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Thin,
            maxLines = 2,
            lineHeight = 18.sp,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            softWrap = true
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "market\ncap",
            fontSize = 16.sp,
            fontWeight = FontWeight.Thin,
            maxLines = 2,
            lineHeight = 18.sp,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            softWrap = true
        )
    }
}

@Composable
fun ListItem(crypto: CryptoCurrency) {
    /*
    <coin image>
    <coin symbol>    <coin price>    <coin price change>   <coin market cap>
     */

    Row(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth(1f)
            .height(72.dp)
            .border(1.dp, Color.Red),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(0.7f)
                .border(1.dp, Color.Green)
                .padding(5.dp),
            text = crypto.marketCapRank.toString(),
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
        Column(
            modifier = Modifier.weight(1f)
                .border(1.dp, Color.Green)
                .padding(end = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painterResource(R.drawable.top_app_bar_image),
                contentDescription = "crypto image",
                modifier = Modifier.size(30.dp, 30.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = crypto.symbol,
                fontSize = 18.sp
            )

        }
        Text(
            modifier = Modifier.weight(1.2f)
                .border(1.dp, Color.Green)
                .padding(5.dp),
            text = "$" + formatNumberForDisplay(crypto.currentPrice, maxLength = 6),
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
        Text(
            modifier = Modifier.weight(1.5f).
                padding(end = 10.dp)
                .border(1.dp, Color.Green),
            text = String.format(Locale.US, "%.3f", crypto.priceChangePercentage24h) + "%",
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
        Text(
            modifier = Modifier.weight(1.3f)
                .border(1.dp, Color.Green),
            text = "$" + formatNumberForDisplay(crypto.marketCap),
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    ExploreScreen()

}