package com.example.cryptoviewer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptoviewer.ui.favourites.FavouritesScreen
import com.example.cryptoviewer.ui.market.MarketScreen
import com.example.cryptoviewer.ui.search.SearchScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Market.route
    ) {
        composable(Market.route) {
            MarketScreen(navController)
        }
        composable(Search.route) {
            SearchScreen(navController)
        }
        composable(Favourites.route) {
            FavouritesScreen(navController)
        }
    }
}