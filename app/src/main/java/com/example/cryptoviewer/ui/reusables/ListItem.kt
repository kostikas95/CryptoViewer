package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cryptoviewer.formatNumberForDisplay
import com.example.cryptoviewer.model.CryptoCurrency
import java.util.Locale

@Composable
fun ListItem(
    crypto: CryptoCurrency,
    onClick: (String) -> Unit
) {

    val painter = rememberAsyncImagePainter(crypto.imageUrl)
    Row(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth(1f)
            .height(72.dp)
            .border(1.dp, Color.Red)
            .clickable {
                onClick(crypto.id)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(0.7f)
                .border(1.dp, Color.Green)
                .padding(5.dp),
            text = crypto.marketCapRank.toString(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier.weight(0.8f)
                .border(1.dp, Color.Green),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painter,
                contentDescription = "crypto image",
                modifier = Modifier.size(30.dp, 30.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = crypto.symbol,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
        Text(
            modifier = Modifier.weight(1.2f)
                .border(1.dp, Color.Green),
            text = "$" + formatNumberForDisplay(crypto.currentPrice, maxLength = 6),
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
        Text(
            modifier = Modifier.weight(1.3f)
                .border(1.dp, Color.Green),
            text = String.format(Locale.US, "%.3f", crypto.priceChangePercentage24h) + "%",
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
        Text(
            modifier = Modifier.weight(1.3f)
                .border(1.dp, Color.Green),
            text = "$" + formatNumberForDisplay(crypto.marketCap),
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
    }
}
