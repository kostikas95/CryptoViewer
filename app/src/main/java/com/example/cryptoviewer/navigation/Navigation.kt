package com.example.cryptoviewer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptoviewer.ui.favourites.FavouritesScreen
import com.example.cryptoviewer.ui.favourites.FavouritesViewModel
import com.example.cryptoviewer.ui.market.MarketScreen
import com.example.cryptoviewer.ui.market.MarketViewModel
import com.example.cryptoviewer.ui.search.SearchScreen
import com.example.cryptoviewer.ui.search.SearchViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Market.route
    ) {
        composable(Market.route) {
            val viewModelStoreOwner = remember {
                navController.getViewModelStoreOwner(navController.graph.id)
            }
            MarketScreen(navController, viewModelStoreOwner)
        }
        composable(Search.route) {
            val viewModelStoreOwner = remember {
                navController.getViewModelStoreOwner(navController.graph.id)
            }
            SearchScreen(navController, viewModelStoreOwner)
        }
        composable(Favourites.route) {
            val viewModelStoreOwner = remember {
                navController.getViewModelStoreOwner(navController.graph.id)
            }
            FavouritesScreen(navController, viewModelStoreOwner)
        }
    }
}