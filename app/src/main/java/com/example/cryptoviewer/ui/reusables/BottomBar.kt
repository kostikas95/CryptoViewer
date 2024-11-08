package com.example.cryptoviewer.ui.reusables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cryptoviewer.R
import com.example.cryptoviewer.navigation.Favourites
import com.example.cryptoviewer.navigation.Market
import com.example.cryptoviewer.navigation.Search

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routes = listOf(Market.route, Search.route, Favourites.route)
    val onClick : (String) -> Unit = { route ->
        navController.navigate(route)
    }

    Row(
        modifier = modifier
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        routes.forEach { route ->
            val isSelected = currentRoute == route
            val iconColor = if (isSelected) Color.Black else Color.Gray
            val textColor = if (isSelected) Color(0xFF6B32F5) else Color(0XFFA0A5BA)
            val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            val iconResId : Int
            if (route == "Market") iconResId = R.drawable.ic_market
            else if (route == "Search") iconResId = R.drawable.ic_search
            else iconResId = R.drawable.ic_favourites

            Column(
                modifier = Modifier.fillMaxHeight()
                    .width(80.dp)
                    .clickable {
                        onClick(route)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TabIcon(
                    iconResId = iconResId,
                    strokeColor = iconColor
                )
                Text(
                    text = route,
                    fontSize = 13.sp,
                    color = textColor,
                    fontWeight = fontWeight,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TabIcon(
    @DrawableRes iconResId: Int,
    strokeColor: Color
) {
    Box(
        modifier = Modifier.size(32.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(strokeColor),
            modifier = Modifier.size(32.dp)
        )
    }
}

