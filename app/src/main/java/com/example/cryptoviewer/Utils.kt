package com.example.cryptoviewer

import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

fun formatTimestampDouble(timestamp: Double): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM")
    val date = Instant.ofEpochMilli(timestamp.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    Log.d("formatter", formatter.format(date))
    return formatter.format(date)
}

fun formatTimestampsDouble(timestamps: List<Double>) : List<String> {
    val formatedTimestamps = mutableListOf<String>()
    for (timestamp: Double in timestamps) {
        val formated = formatTimestampDouble(timestamp)
        if (formated.split(" ")[0].toInt() % 5 == 0) {
            formatedTimestamps.add(formated)
        }
        else {
            formatedTimestamps.add("")
        }
    }
    return formatedTimestamps
}

fun formatDateISO8601(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm")
    val localDateTime = Instant.parse(date)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    return localDateTime.format(formatter)
}

