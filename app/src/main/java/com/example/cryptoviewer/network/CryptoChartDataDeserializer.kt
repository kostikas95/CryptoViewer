package com.example.cryptoviewer.network

import android.util.Log
import com.example.cryptoviewer.model.CryptoChartData
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CryptoChartDataDeserializer : JsonDeserializer<CryptoChartData> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CryptoChartData {
        val jsonObject = json?.asJsonObject

        val pricesArray = jsonObject?.getAsJsonArray("prices")
        val timestamps = mutableListOf<Double>()
        val prices = mutableListOf<Double>()
        val marketCaps = mutableListOf<Double>()
        val totalVolumes = mutableListOf<Double>()

        pricesArray?.forEach { element ->
            val pair = element.asJsonArray
            Log.d("deserialize", "timestamp ${pair[0].asDouble}")
            Log.d("deserialize", "price ${pair[1].asDouble}")
            timestamps.add(pair[0].asDouble)  // timestamp
            prices.add(pair[1].asDouble)      // price
        }

        val marketCapsArray = jsonObject?.getAsJsonArray("market_caps")
        marketCapsArray?.forEach { element ->
            val pair = element.asJsonArray
            Log.d("deserialize", "market cap ${pair[1].asDouble}")
            marketCaps.add(pair[1].asDouble)  // market Cap
        }

        val totalVolumesArray = jsonObject?.getAsJsonArray("total_volumes")
        totalVolumesArray?.forEach { element ->
            val pair = element.asJsonArray
            Log.d("deserialize", "volume ${pair[1].asDouble}")
            totalVolumes.add(pair[1].asDouble)  // volume
        }

        return CryptoChartData(
            timestamps = timestamps,
            prices = prices,
            marketCaps = marketCaps,
            totalVolumes = totalVolumes
        )
    }
}