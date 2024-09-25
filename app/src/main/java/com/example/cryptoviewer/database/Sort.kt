package com.example.cryptoviewer.database

enum class SortField(val field: String) {
    MARKET_CAP_RANK("marketCapRank"),
    NAME("name"),
    CURRENT_PRICE("currentPrice"),
    PRICE_CHANGE("priceChangePercentage24h"),
    MARKET_CAP("marketCap")
}

enum class SortOrder(val order: String) {
    ASCENDING("ASC"),
    DESCENDING("DESC");

    fun toggle() : SortOrder {
        return if (this == ASCENDING) DESCENDING else ASCENDING
    }
}
