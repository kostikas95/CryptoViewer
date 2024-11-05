package com.example.cryptoviewer.network

import com.example.cryptoviewer.model.CryptoChartData
import com.example.cryptoviewer.model.CryptoCurrency
import com.example.cryptoviewer.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("search")
    suspend fun fetchRelativeCoinIds(
        @Query("id") id: String
    ) : SearchResponse

    @GET("search/trending")
    suspend fun fetchTrendingCoinIds() : SearchResponse

    @GET("coins/{id}/market_chart")
    suspend fun getCryptoChartData(
        @Path("id") id : String,
        @Query("vs_currency") vsCurrency : ApiVsCurrency,
        @Query("days") days : String,
        @Query("interval") interval : String? = null
    ) : CryptoChartData
}
