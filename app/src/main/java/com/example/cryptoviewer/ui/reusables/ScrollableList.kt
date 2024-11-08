package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.model.CryptoCurrency
import kotlinx.coroutines.flow.filter

@Composable
fun ScrollableList(
    lazyListState: LazyListState,
    cryptos: List<CryptoCurrency>,
    onSortingFactorTextClick: (SortField) -> Unit,
    onListItemClicked: (String) -> Unit,
    onLoadNextPage: () -> Unit = {},
    modifier: Modifier
) {
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filter { it == lazyListState.layoutInfo.totalItemsCount - 1 }
            .collect {
                onLoadNextPage()
            }
    }

    Column {
        ScrollListHeadline(
            onSortingFactorTextClick,
            modifier = modifier
        )

        HorizontalDivider(
            modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 3.dp),
            thickness = 2.dp,
            color = Color(0xFFA0A5BA)
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = cryptos, key = {it.id}) { crypto ->
                ListItem(
                    crypto = crypto,
                    modifier = modifier,
                    onListItemClicked = onListItemClicked
                )
                HorizontalDivider(
                    modifier = modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }
        }
    }


}