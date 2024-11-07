package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CryptoStatsListItem(
    statName : String,
    statValue : String
) {
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = statName,
                textAlign = TextAlign.Start,
                color = Color.Gray
            )
            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = statValue,
                textAlign = TextAlign.End,
                color = Color.Black
            )
        }
    }

}