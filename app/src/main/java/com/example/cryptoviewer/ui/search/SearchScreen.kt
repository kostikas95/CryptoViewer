package com.example.cryptoviewer.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cryptoviewer.R
import com.example.cryptoviewer.database.SortField
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.ui.market.MarketViewModel
import com.example.cryptoviewer.ui.reusables.BottomBar
import com.example.cryptoviewer.ui.reusables.ListItem
import com.example.cryptoviewer.ui.reusables.ScrollListHeadline

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val viewModel: SearchViewModel = viewModel(viewModelStoreOwner)
    viewModel.debug()

    // states and data
    val cryptos by viewModel.cryptos.observeAsState(emptyList())
    val lazyListState = rememberLazyListState(0)
    val searchText by viewModel.searchText

    // lambdas
    val onSortingFactorTextClick : (SortField) -> Unit = { newField ->
        viewModel.changeOrder(newField)
    }
    val onSearchTextChanged : (String) -> Unit = { newText ->
        viewModel.updateSearchText(newText)
    }
    val onListItemClicked : (String) -> Unit = { id ->
        viewModel.addToFavourites(id)
    }

    Scaffold(
        topBar = { TopBar("Search") },
        bottomBar = { BottomBar(navController) },
        content = { innerPadding ->
            Content(
                innerPadding,
                searchText,
                cryptos,
                lazyListState,
                onSortingFactorTextClick,
                onSearchTextChanged,
                onListItemClicked
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
fun Content(
    innerPadding: PaddingValues,
    searchText: String,
    cryptos: List<CryptoCurrency>,
    lazyListState: LazyListState,
    onSortingFactorTextClick: (SortField) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onListItemClicked: (String) ->Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText ->
                onSearchTextChanged(newText)
            },
            label = { Text("Search name or id") }
        )

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