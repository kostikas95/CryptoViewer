package com.example.cryptoviewer.network

enum class ApiOrder(val orderBy: String) {
    MARKET_CAP_ASC("market_cap_asc"),
    MARKET_CAP_DSC("market_cap_desc"),
    VOLUME_ASC("volume_asc"),
    VOLUME_DSC("volume_desc"),
    ID_ASC("id_asc"),
    ID_DSC("id_desc")
}