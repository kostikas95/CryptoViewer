package com.example.cryptoviewer.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptoviewer.model.CryptoCurrency
import android.content.Context

@Database(entities = [CryptoCurrency::class], version = 5, exportSchema = false)
abstract class CryptoDatabase : RoomDatabase() {

    abstract fun cryptoDao(): CryptoCurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: CryptoDatabase? = null

        fun getDatabase(context: Context): CryptoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CryptoDatabase::class.java,
                    "crypto_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
