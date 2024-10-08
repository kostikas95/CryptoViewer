package com.example.cryptoviewer.network

import com.example.cryptoviewer.model.CryptoCurrency
import retrofit2.http.GET
import retrofit2.http.Query


interface CryptoApiService {
    @GET("coins/markets")
    suspend fun fetchCryptos(
        @Query("vs_currency") vsCurrency : ApiVsCurrency,
        @Query("ids") ids : String? = null,
        @Query("order") order : ApiOrder = ApiOrder.MARKET_CAP_DSC,
        @Query("per_page") perPage : Int = 100,
        @Query("page") page : Int = 1,
        @Query("price_change_percentage") priceChangePercentage : String = "24h",
        @Query("precision") precision : String = "3"
    ) : List<CryptoCurrency>
}
