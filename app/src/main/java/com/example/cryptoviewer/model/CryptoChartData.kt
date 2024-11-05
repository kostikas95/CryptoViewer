package com.example.cryptoviewer.model


data class CryptoChartData(
    val timestamps : List<Double>,
    val prices: List<Double>,
    val marketCaps: List<Double>,
    val totalVolumes: List<Double>
)
