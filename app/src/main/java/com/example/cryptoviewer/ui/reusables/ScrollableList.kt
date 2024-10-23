package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.model.CryptoCurrency
import kotlinx.coroutines.flow.filter

@Composable
fun ScrollableList(
    innerPadding: PaddingValues,
    lazyListState: LazyListState,
    cryptos: List<CryptoCurrency>,
    onSortingFactorTextClick: (SortField) -> Unit,
    onListItemClicked: (String) -> Unit,
    onLoadNextPage: () -> Unit = {}
) {
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filter { it == lazyListState.layoutInfo.totalItemsCount - 1 }
            .collect {
                onLoadNextPage()
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
    ) {
        ScrollListHeadline(onSortingFactorTextClick)

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.background(Color.Blue)
        ) {
            items(items = cryptos, key = {it.id}) { crypto ->
                ListItem(crypto, onListItemClicked)
            }
        }
    }


}