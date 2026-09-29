/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified

/**
 * Centres [content] in a column no wider than [maxWidth], leaving equal margins on both sides once
 * the space is wider, the way a website lays out its page. [Dp.Unspecified] fills the space.
 */
@Composable
fun ContentWidthBox(maxWidth: Dp, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    if (!maxWidth.isSpecified) {
        Box(modifier.fillMaxSize()) { content() }
        return
    }
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Box(
            Modifier
                .widthIn(max = maxWidth)
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            content()
        }
    }
}
