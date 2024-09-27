package com.example.cryptoviewer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptoviewer.model.CryptoCurrency

@Dao
interface CryptoCurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptos(cryptos: List<CryptoCurrency>)

    // --------------------------------- EXPLORE SCREEN -------------------------------------------

    // get by market cap rank
    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank ASC")
    suspend fun getCryptosByMarketCapRankAsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByMarketCapRankAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank DESC")
    suspend fun getCryptosByMarketCapRankDsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByMarketCapRankDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // get by name
    @Query("SELECT * FROM cryptocurrencies ORDER BY name ASC")
    suspend fun getCryptosByNameAsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByNameAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY name DESC")
    suspend fun getCryptosByNameDsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY name DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByNameDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // get by id
    @Query("SELECT * FROM cryptocurrencies ORDER BY id ASC")
    suspend fun getCryptosByIdAsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByIdAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY id DESC")
    suspend fun getCryptosByIdDsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByIdDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // get by current price
    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice ASC")
    suspend fun getCryptosByCurrentPriceAsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByCurrentPriceAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice DESC")
    suspend fun getCryptosByCurrentPriceDsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByCurrentPriceDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // get by price change percentage in 24h
    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h ASC")
    suspend fun getCryptosByPriceChangePercentageAsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByPriceChangePercentageAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h DESC")
    suspend fun getCryptosByPriceChangePercentageDsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByPriceChangePercentageDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // get by market cap
    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap ASC")
    suspend fun getCryptosByMarketCapAsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByMarketCapAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap DESC")
    suspend fun getCryptosByMarketCapDsc(): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosByMarketCapDsc(limit: Int, offset: Int): List<CryptoCurrency>


    // ---------------------------------- SEARCH SCREEN --------------------------------------------



    // @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank ASC LIMIT :limit OFFSET :offset")
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY marketCapRank ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeByMarketCapRankAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>



    // @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank DESC LIMIT :limit OFFSET :offset")
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY marketCapRank DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeByMarketCapRankDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // get by name

    @Query("SELECT * FROM cryptocurrencies ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByNameAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY name DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByNameDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // get by id

    @Query("SELECT * FROM cryptocurrencies ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByIdAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByIdDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // get by current price
    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByCurrentPriceAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByCurrentPriceDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // get by price change percentage in 24h

    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByPriceChangePercentageAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByPriceChangePercentageDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // get by market cap

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByMarketCapAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosLikeByMarketCapDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>









    @Query("DELETE FROM cryptocurrencies")
    suspend fun deleteAllCryptos()
}
