package com.example.cryptoviewer.model

data class SearchResponse(
    val coins: List<CoinIdWrapper>
)

data class CoinIdWrapper(
    val item: CoinId
)

data class CoinId(
    val id: String
)