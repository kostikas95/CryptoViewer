package com.example.cryptoviewer.ui.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptoviewer.database.SortField

@Composable
fun ScrollListHeadline(
    onSortingFactorTextClick : (SortField) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .background((Color.Yellow)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.weight(0.7f)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.MARKET_CAP_RANK)
                }
                    .align(Alignment.Center)
                    .padding(start = 5.dp, end = 5.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = RectangleShape
                    ),
                text = "#",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            modifier = Modifier.weight(1f)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.SYMBOL)
                }
                    .align(Alignment.Center)
                    .padding(end = 15.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = RectangleShape
                    ),
                text = "COIN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            modifier = Modifier.weight(1.2f)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.CURRENT_PRICE)
                }
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = RectangleShape
                    ),
                text = "CURRENT\nPRICE",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            modifier = Modifier.weight(1.3f)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.PRICE_CHANGE)
                }
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = RectangleShape
                    ),
                text = "PRICE\nCHANGE(%)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }

        Box(
            modifier = Modifier.weight(1.3f)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
        ) {
            Text(
                modifier = Modifier.clickable {
                    onSortingFactorTextClick(SortField.MARKET_CAP)
                }
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = RectangleShape
                    ),
                text = "MARKET\nCAP",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                lineHeight = 18.sp,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }


    }
}