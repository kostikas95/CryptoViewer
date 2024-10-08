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

    // order by market cap rank
    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByMarketCapRankAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCapRank DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByMarketCapRankDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // order by symbol
    @Query("SELECT * FROM cryptocurrencies ORDER BY symbol ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderBySymbolAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY symbol DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderBySymbolDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // order by name
    @Query("SELECT * FROM cryptocurrencies ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByNameAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY name DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByNameDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // order by id
    @Query("SELECT * FROM cryptocurrencies ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByIdAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByIdDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // order by current price
    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByCurrentPriceAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY currentPrice DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByCurrentPriceDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // order by price change percentage in 24h
    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByPriceChangePercentageAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY priceChangePercentage24h DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByPriceChangePercentageDsc(limit: Int, offset: Int): List<CryptoCurrency>

    // order by market cap
    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap ASC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByMarketCapAsc(limit: Int, offset: Int): List<CryptoCurrency>

    @Query("SELECT * FROM cryptocurrencies ORDER BY marketCap DESC LIMIT :limit OFFSET :offset")
    suspend fun getCryptosOrderByMarketCapDsc(limit: Int, offset: Int): List<CryptoCurrency>


    // ---------------------------------- SEARCH SCREEN --------------------------------------------
    // order by market cap rank
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY marketCapRank ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByMarketCapRankAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY marketCapRank DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByMarketCapRankDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by symbol
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY symbol ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderBySymbolAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY symbol DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderBySymbolDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by id
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY id ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByIdAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY id DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByIdDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by current price
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY currentPrice ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByCurrentPriceAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY currentPrice DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByCurrentPriceDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by price change percentage in 24h
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY priceChangePercentage24h ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByPriceChangePercentageAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY priceChangePercentage24h DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByPriceChangePercentageDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by market cap
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY marketCap ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByMarketCapAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        ORDER BY marketCap DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosLikeOrderByMarketCapDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>


    // -------------------------------- FAVOURITES SCREEN ------------------------------------------
    // order by market cap rank
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCapRank ASC
    """)
    suspend fun getCryptosByIdsOrderByMarketCapRankAsc(ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCapRank DESC
    """)
    suspend fun getCryptosByIdsOrderByMarketCapRankDsc(ids: List<String>): List<CryptoCurrency>

    // order by symbol
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY symbol ASC
    """)
    suspend fun getCryptosByIdsOrderBySymbolAsc(ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY symbol DESC
    """)
    suspend fun getCryptosByIdsOrderBySymbolDsc(ids: List<String>): List<CryptoCurrency>

    // order by id
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY id ASC
    """)
    suspend fun getCryptosByIdsOrderByIdAsc(ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY id DESC
    """)
    suspend fun getCryptosByIdsOrderByIdDsc(ids: List<String>): List<CryptoCurrency>

    // order by current price
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY currentPrice ASC
    """)
    suspend fun getCryptosByIdsOrderByCurrentPriceAsc(ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY currentPrice DESC
    """)
    suspend fun getCryptosByIdsOrderByCurrentPriceDsc(ids: List<String>): List<CryptoCurrency>

    // order by price change percentage in 24h
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY priceChangePercentage24h ASC
    """)
    suspend fun getCryptosByIdsOrderByPriceChangePercentageAsc(ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY priceChangePercentage24h DESC
    """)
    suspend fun getCryptosByIdsOrderByPriceChangePercentageDsc(ids: List<String>): List<CryptoCurrency>

    // order by market cap
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCap ASC
    """)
    suspend fun getCryptosByIdsOrderByMarketCapAsc(ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCap DESC
    """)
    suspend fun getCryptosByIdsOrderByMarketCapDsc(ids: List<String>): List<CryptoCurrency>







    @Query("DELETE FROM cryptocurrencies")
    suspend fun deleteAllCryptos()
}
