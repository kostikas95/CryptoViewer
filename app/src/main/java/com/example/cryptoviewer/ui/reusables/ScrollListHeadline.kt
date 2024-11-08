package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptoviewer.database.SortField

@Composable
fun ScrollListHeadline(
    onSortingFactorTextClick : (SortField) -> Unit,
    modifier : Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(0.7f)
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.MARKET_CAP_RANK)
                },
                text = "#",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(0.8f)
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.SYMBOL)
                },
                text = "COIN",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1.2f)
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.CURRENT_PRICE)
                },
                text = "PRICE",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1.3f)
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.PRICE_CHANGE)
                },
                text = "24H PRICE\nCHANGE %",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1.3f)
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.MARKET_CAP)
                },
                text = "MARKET\nCAP",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }


    }
}