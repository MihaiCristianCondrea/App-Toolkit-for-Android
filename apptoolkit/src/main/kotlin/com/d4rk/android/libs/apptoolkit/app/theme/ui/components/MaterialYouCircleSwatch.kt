package com.d4rk.android.libs.apptoolkit.app.theme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color

@Composable
fun MaterialYouCircleSwatch(
    primary: Color,
    secondary: Color,
    tertiary: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
    ) {
        Column(Modifier.fillMaxSize()) {
            // top half
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds(),
            ) {
                Spacer(
                    Modifier
                        .fillMaxSize()
                        .background(primary)
                )
            }

            // bottom half split
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(secondary)
                )
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(tertiary)
                )
            }
        }
    }
}