package com.example.cryptoviewer.ui.favourites

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.cryptoviewer.ui.reusables.AutoScrollToTopFAB
import com.example.cryptoviewer.ui.reusables.BottomBar
import com.example.cryptoviewer.ui.reusables.CustomBottomSheet
import com.example.cryptoviewer.ui.reusables.ScrollableList
import com.example.cryptoviewer.ui.reusables.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    navController: NavHostController,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val viewModel: FavouritesScreenViewModel = viewModel(viewModelStoreOwner)

    // states
    // val order by viewModel.order.observeAsState(Pair(SortField.MARKET_CAP_RANK, SortOrder.ASCENDING))
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val cryptos by viewModel.cryptos.observeAsState(emptyList())
    val expandedSheetHeight = LocalConfiguration.current.screenHeightDp.dp * 0.8f
    val isFabVisible by viewModel.isFabVisible.collectAsState()
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var topBarHeight by remember { mutableStateOf(100.dp) }
    val favouriteIds by viewModel.favouriteIds.collectAsState()
    val lazyListState = viewModel.lazyListState
    val scope = rememberCoroutineScope()

    // lambdas
    val onSortingFactorTextClick : (SortField) -> Unit = { newField ->
        viewModel.changeOrder(newField)
    }
    val onListItemClicked : (String) -> Unit = { cryptoId ->
        scope.launch {
            viewModel.showCryptoDetailsSheet(cryptoId)
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
    val onScrollToTop: () -> Unit = {
        scope.launch {
            viewModel.scrollToTop()
        }
    }
    val toggleFavourite: (String) -> Unit = { cryptoId ->
        viewModel.toggleFavouriteStatus(cryptoId)
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

                if (currentIndex == 0) {
                    topBarHeight = 100.dp
                } else if (currentIndex in 1..2) {
                    val shrinkFactor = currentIndex / 2f
                    topBarHeight = lerp(100.dp, 65.dp, shrinkFactor)
                } else {
                    topBarHeight = 65.dp
                }

                previousIndex = currentIndex
                previousScrollOffset = currentOffset
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
                CustomBottomSheet(
                    customSheetState = viewModel.customSheetState,
                    favouriteIds = favouriteIds,
                    toggleFavourite = toggleFavourite,
                    modifier = Modifier.fillMaxWidth()
                )
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
                TopBar("Favourites", animatedTopBarHeight)

                Box {
                    ScrollableList(
                        lazyListState = lazyListState,
                        cryptos = cryptos,
                        onSortingFactorTextClick = onSortingFactorTextClick,
                        onListItemClicked = onListItemClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
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
                        AutoScrollToTopFAB(onScrollToTop = onScrollToTop)
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
                BottomBar(
                    navController = navController,
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                )
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


