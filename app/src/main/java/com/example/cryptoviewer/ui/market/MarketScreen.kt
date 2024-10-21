package com.example.cryptoviewer.ui.market

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
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


    // states
    // val order by viewModel.order.observeAsState(Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING))
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    val expandedSheetHeight = LocalConfiguration.current.screenHeightDp.dp * 0.8f
    val isFabVisible by viewModel.isFabVisible.collectAsState()
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var topBarHeight by remember { mutableStateOf(150.dp) }
    val lazyListState = viewModel.lazyListState
    val scope = rememberCoroutineScope()

    // lambdas
    val onSortingFactorTextClick : (SortField) -> Unit = { newField ->
        viewModel.changeOrder(newField)
    }
    val onListItemClicked : (String) -> Unit = { cryptoId ->
        scope.launch {
            viewModel.showCryptoDetails(cryptoId)
            scaffoldState.bottomSheetState.expand()
        }
    }
    val onHideSheet: () -> Unit = {
        scope.launch {
            try {
                if (scaffoldState.bottomSheetState.isVisible) {
                    scaffoldState.bottomSheetState.hide()
                }
            } catch (e: Exception) {
                Log.e("sheet", "error with sheet hiding", e)
            }

        }
    }

    val animatedTopBarHeight by animateDpAsState(
        targetValue = topBarHeight,
        animationSpec = tween(durationMillis = 1200),
        label = ""
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.monitorLazyListState().collect { firstIndex ->
            viewModel.updateFabVisibility(firstIndex > 5)
        }
    }

    LaunchedEffect(lazyListState) {
        var previousIndex = lazyListState.firstVisibleItemIndex
        var previousScrollOffset = lazyListState.firstVisibleItemScrollOffset

        snapshotFlow {
            lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentOffset) ->
            scope.launch {
                if (previousIndex == 0 && previousScrollOffset == 0 && currentIndex == 0 && currentOffset == 0) {
                    return@launch
                }

                val isScrollingUp = when {
                    currentIndex < previousIndex -> true
                    currentIndex == previousIndex -> currentOffset < previousScrollOffset
                    else -> false
                }

                if (isScrollingUp) {
                    isBottomBarVisible = true
                } else {
                    isBottomBarVisible = false
                }

                previousIndex = currentIndex
                previousScrollOffset = currentOffset
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { firstVisibleItemIndex ->
                // Determine the new height of the top bar based on scroll offset
                scope.launch {
                    if (firstVisibleItemIndex == 0) {
                        topBarHeight = 150.dp
                    } else if (firstVisibleItemIndex in 1..2) {
                        val shrinkFactor = firstVisibleItemIndex / 2f
                        topBarHeight = lerp(150.dp, 65.dp, shrinkFactor)
                    } else {
                        topBarHeight = 65.dp
                    }
                }
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = MaterialTheme.shapes.large,
        sheetContainerColor = Color.White,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(expandedSheetHeight)
            ) {
                CustomBottomSheet(customSheetState = viewModel.customSheetState)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // pass factor to perform animation on all elements of the top bar
                TopBar("Market", animatedTopBarHeight)


                Box(
                    modifier = Modifier.weight(8.0f)
                ) {

                    ScrollableList(
                        innerPadding = PaddingValues(0.dp),
                        viewModel = viewModel,
                        onSortingFactorTextClick = onSortingFactorTextClick,
                        onListItemClicked = onListItemClicked
                    )

                    // FAB
                    this@Column.AnimatedVisibility(
                        visible = isFabVisible,
                        enter = scaleIn(
                            initialScale = 0f,
                            animationSpec = tween(durationMillis = 600)
                        ),
                        exit = scaleOut(
                            targetScale = 0f,
                            animationSpec = tween(durationMillis = 600)
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 95.dp, end = 50.dp)
                    )  {
                        FloatingActionButton(
                            onClick = {
                                scope.launch {
                                    viewModel.scrollToTop()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Scroll to top"
                            )
                        }
                    }

                }

            }

            // bottom bar
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }
                ),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomBar(navController)
            }



            if (scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(
                            onClick = { onHideSheet() },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
            }
        }
    }
}


@Composable
fun TopBar(
    title: String,
    animatedHeight: Dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .height(animatedHeight)
            .background(Color.Red)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)

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

@Composable
fun ScrollableList(
    innerPadding: PaddingValues,
    viewModel: MarketViewModel,
    onSortingFactorTextClick: (SortField) -> Unit,
    onListItemClicked: (String) -> Unit
) {
    viewModel.debug()
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

