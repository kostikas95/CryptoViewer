package com.example.cryptoviewer

fun formatNumberForDisplay(value: Double, maxLength: Int): String {
    return when {
        // very small numbers
        value < 0.001 && value != 0.0 -> String.format("%.2e", value) // Ex: 5.4e-18

        // large numbers 'K', 'M', 'B'
        value >= 1_000_000_000 -> String.format("%.2fB", value / 1_000_000_000)
        value >= 1_000_000 -> String.format("%.2fM", value / 1_000_000)
        value >= 1_000 -> String.format("%.2fK", value / 1_000)

        // numbers between 0.001 and 1, or 1 and 1000: show full precision or rounded
        value >= 0.001 && value < 1 -> String.format("%.6f", value).take(maxLength)

        // low digit numbers
        else -> value.toString()
    }
}

fun formatNumberForDisplay(value: Long): String {
    return when {
        value >= 1_000_000_000_000L -> String.format("%.3fT", value / 1_000_000_000_000.0) // Trillions
        value >= 1_000_000_000L -> String.format("%.3fB", value / 1_000_000_000.0)         // Billions
        value >= 1_000_000L -> String.format("%.3fM", value / 1_000_000.0)                 // Millions
        value >= 1_000L -> String.format("%.3fK", value / 1_000.0)                         // Thousands
        else -> value.toString()                                                           // Less than 1K, no abbreviation
    }
}