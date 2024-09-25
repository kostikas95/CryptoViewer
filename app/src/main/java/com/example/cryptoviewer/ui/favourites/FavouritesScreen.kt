package com.example.cryptoviewer.ui.favourites

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cryptoviewer.ui.explore.AutoScrollToTopButton
import com.example.cryptoviewer.ui.explore.ScrollableList

@Composable
fun FavouritesScreen() {
    // Scaffold(
    //     topBar = { TopAppBar() },
    //     bottomBar = { BottomAppBar() },
    //     floatingActionButton = { AutoScrollToTopButton() },
    //     content = { innerPadding -> ScrollableList(innerPadding) }
    // )
}


@Preview(showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    FavouritesScreen()
}