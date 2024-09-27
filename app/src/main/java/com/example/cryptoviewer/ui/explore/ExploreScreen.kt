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
import com.example.cryptoviewer.ui.reusables.ListItem
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
        topBar = { TopBar("Explore") },
        bottomBar = { BottomBar() },
        floatingActionButton = { AutoScrollToTopButton() },
        content = { innerPadding ->
            Content(innerPadding,
                cryptos,
                lazyListState,
                onSortingFactorTextClick
            )
        }
    )
}

@Composable
fun TopBar(
    title: String
) {
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
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(R.drawable.top_app_bar_image),
                contentDescription = "topAppBarImage"
            )
        }
    }
}

@Composable
fun BottomBar(/* pass active window to make it reusable */) {
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
fun Content(
    innerPadding: PaddingValues,
    cryptos: List<CryptoCurrency>,
    lazyListState: LazyListState,
    onSortingFactorTextClick: (SortField) -> Unit
    // onLoadMore : suspend () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(1f)
                .background((Color.Yellow)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            /*
            Maybe this row contains 5 boxes { Text }
            The boxes will have the same weights as the elements of the lazy column.
            Then I can modify the text to achieve the desired text space and position,
            without worrying about destroying the alignment with the values in the
            lazy column. Also, by doing that, the clickable space will be the text's
            space and not the box's space.
             */

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
                modifier = Modifier.weight(1f)
                    .clickable {
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
                modifier = Modifier.weight(1f)
                    .clickable {
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
                modifier = Modifier.weight(1f)
                    .clickable {
                        onSortingFactorTextClick(SortField.MARKET_CAP)
                    },
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
    }
}



@Preview(showBackground = true)
@Composable
fun Preview() {
    ExploreScreen()

}