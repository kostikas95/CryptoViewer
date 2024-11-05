package com.example.cryptoviewer.network

import com.example.cryptoviewer.model.CryptoChartData
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.coingecko.com/api/v3/"

    val gson = GsonBuilder()
        .registerTypeAdapter(CryptoChartData::class.java, CryptoChartDataDeserializer())
        .create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: CryptoApiService by lazy {
        retrofit.create(CryptoApiService::class.java)
    }
}
