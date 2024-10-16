package com.example.cryptoviewer.ui.market

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cryptoviewer.R
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.ui.reusables.BottomBar
import com.example.cryptoviewer.ui.reusables.CustomBottomSheet
import com.example.cryptoviewer.ui.reusables.ListItem
import com.example.cryptoviewer.ui.reusables.ScrollListHeadline
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    navController: NavHostController,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val viewModel: MarketViewModel = viewModel(viewModelStoreOwner)
    viewModel.debug()

    // states
    // val order by viewModel.order.observeAsState(Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING))
    val scaffoldState = rememberBottomSheetScaffoldState()
    val expandedSheetHeight = LocalConfiguration.current.screenHeightDp.dp * 0.8f
    val scope = rememberCoroutineScope()

    // lambdas
    val onSortingFactorTextClick : (SortField) -> Unit = { newField ->
        viewModel.changeOrder(newField)
    }
    val onListItemClicked : (String) -> Unit = { id ->
        scope.launch {
            scaffoldState.bottomSheetState.expand()
        }
    }
    val onHideSheet: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = MaterialTheme.shapes.large,
        sheetContainerColor = Color.White,
        sheetContent = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(expandedSheetHeight)
            ) {
                CustomBottomSheet()
            }

        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (scaffoldState.bottomSheetState.currentValue != SheetValue.Hidden) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { onHideSheet() }
                )
            }
            Scaffold(
                topBar = { TopBar("Market") },
                bottomBar = { BottomBar(navController) },
                floatingActionButton = { AutoScrollToTopButton() },
                content = { innerPadding ->
                    Content(
                        innerPadding,
                        viewModel,
                        onSortingFactorTextClick,
                        onListItemClicked
                    )
                }
            )
        }
    }
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
fun AutoScrollToTopButton() {

}

@Composable
fun Content(
    innerPadding: PaddingValues,
    viewModel: MarketViewModel,
    onSortingFactorTextClick: (SortField) -> Unit,
    onListItemClicked: (String) -> Unit
) {

    val lazyListState = viewModel.lazyListState
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filter { it == lazyListState.layoutInfo.totalItemsCount - 1 }
            .collect {
                viewModel.loadNextPage()
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
    ) {
        ScrollListHeadline(onSortingFactorTextClick)

        val cryptos by viewModel.cryptos.observeAsState(emptyList())
        LazyColumn(
            state = viewModel.lazyListState,
            modifier = Modifier.background(Color.Blue)
        ) {
            items(items = cryptos, key = {it.id}) { crypto ->
                ListItem(crypto, onListItemClicked)
            }
        }
    }
}