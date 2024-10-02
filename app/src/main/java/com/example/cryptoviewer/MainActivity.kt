package com.example.cryptoviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cryptoviewer.navigation.Navigation
import com.example.cryptoviewer.ui.market.MarketScreen
import com.example.cryptoviewer.ui.theme.CryptoViewerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CryptoViewerTheme {
                Navigation()
            }
        }
    }
}



