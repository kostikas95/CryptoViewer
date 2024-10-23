package com.example.cryptoviewer.ui.reusables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun AutoScrollToTopFAB(
    onScrollToTop: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onScrollToTop()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Scroll to top"
        )
    }
}