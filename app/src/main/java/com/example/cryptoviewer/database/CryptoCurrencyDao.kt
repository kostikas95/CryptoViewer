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

    // _____________________________________________________________________________________________

    // get a single crypto by id
    @Query("SELECT * FROM cryptocurrencies WHERE id = :cryptoId LIMIT 1")
    suspend fun getCryptoById(cryptoId: String): CryptoCurrency

    // _____________________________________________________________________________________________

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


    // _____________________________________________________________________________________________

    // order by market cap rank
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY marketCapRank ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByMarketCapRankAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY marketCapRank DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByMarketCapRankDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by symbol
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY symbol ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderBySymbolAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY symbol DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderBySymbolDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by id
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY id ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByIdAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY id DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByIdDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by current price
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY currentPrice ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByCurrentPriceAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY currentPrice DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByCurrentPriceDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by price change percentage in 24h
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY priceChangePercentage24h ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByPriceChangePercentageAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY priceChangePercentage24h DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByPriceChangePercentageDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    // order by market cap
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY marketCap ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByMarketCapAsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE symbol LIKE '%' || :text || '%'
        OR name  LIKE '%' || :text || '%'
        OR id  LIKE '%' || :text || '%'
        ORDER BY marketCap DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getRelativeCryptosOrderByMarketCapDsc(limit: Int, offset: Int, text: String): List<CryptoCurrency>


    // _____________________________________________________________________________________________

    // order by market cap rank
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCapRank ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByMarketCapRankAsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCapRank DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByMarketCapRankDsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    // order by symbol
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY symbol ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderBySymbolAsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY symbol DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderBySymbolDsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    // order by id
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY id ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByIdAsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY id DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByIdDsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    // order by current price
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY currentPrice ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByCurrentPriceAsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY currentPrice DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByCurrentPriceDsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    // order by price change percentage in 24h
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY priceChangePercentage24h ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByPriceChangePercentageAsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY priceChangePercentage24h DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByPriceChangePercentageDsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    // order by market cap
    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCap ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByMarketCapAsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    @Query("""
        SELECT * FROM cryptocurrencies
        WHERE id IN (:ids)
        ORDER BY marketCap DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCryptosByIdsOrderByMarketCapDsc(limit: Int, offset: Int, ids: List<String>): List<CryptoCurrency>

    // _____________________________________________________________________________________________





    @Query("DELETE FROM cryptocurrencies")
    suspend fun deleteAllCryptos()
}
