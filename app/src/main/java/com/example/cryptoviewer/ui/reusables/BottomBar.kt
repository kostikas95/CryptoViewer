package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cryptoviewer.R
import com.example.cryptoviewer.navigation.Favourites
import com.example.cryptoviewer.navigation.Market
import com.example.cryptoviewer.navigation.Search

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val onNavigate : (String) -> Unit = { destination ->
        navController.navigate(destination)
    }

    Row(
        modifier = Modifier.fillMaxWidth(1f)
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.chart_line),
            contentDescription = "market image",
            modifier = Modifier.size(32.dp, 32.dp)
                .clickable {
                    onNavigate(Market.route)
                },
            contentScale = ContentScale.Fit
        )
        Image(
            painterResource(R.drawable.search),
            contentDescription = "search image",
            modifier = Modifier.size(32.dp, 32.dp)
                .clickable {
                    onNavigate(Search.route)
                },
            contentScale = ContentScale.Fit
        )
        Image(
            painterResource(R.drawable.heart),
            contentDescription = "favourites image",
            modifier = Modifier.size(32.dp, 32.dp)
                .clickable {
                    onNavigate(Favourites.route)
                },
            contentScale = ContentScale.Fit
        )
    }
}