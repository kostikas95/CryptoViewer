package com.example.cryptoviewer.navigation

interface Destinations {
    val route : String
}

object Market : Destinations {
    override val route = "Market"
}

object Search : Destinations {
    override val route = "Search"
}

object Favourites : Destinations {
    override val route = "Favourites"
}