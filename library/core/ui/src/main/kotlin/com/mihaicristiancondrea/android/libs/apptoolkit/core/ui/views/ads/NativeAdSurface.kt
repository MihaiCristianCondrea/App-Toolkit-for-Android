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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R

/**
 * Compose container for a loaded native ad. Composed only after an ad is available to avoid
 * leaving an empty card when loading fails.
 *
 * @param shape Container shape applied directly to the [Card].
 * @param showContainer `false` leaves the surface to the host.
 * @param containerColor [Color.Unspecified] uses Material card defaults; set a color to match a
 * custom host surface.
 */
@Composable
internal fun NativeAdSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    showContainer: Boolean = true,
    containerColor: Color = Color.Unspecified,
    content: @Composable () -> Unit,
) {
    if (!showContainer) {
        Box(modifier = modifier) { content() }
        return
    }

    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        content()
    }
}

/**
 * What an ad slot draws in `@Preview` and in the layout inspector, where no ad can load.
 */
@Composable
internal fun NativeAdPlaceholder(
    presentation: NativeAdPresentation,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    showContainer: Boolean = true,
    containerColor: Color = Color.Unspecified,
) {
    NativeAdSurface(
        modifier = modifier,
        shape = shape,
        showContainer = showContainer,
        containerColor = containerColor,
    ) {
        Box(
            modifier = if (presentation is NativeAdPresentation.Grid) {
                Modifier.fillMaxSize()
            } else {
                Modifier.fillMaxWidth()
            }.padding(all = MaterialTheme.spacing.large),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.sponsored_ad_label_plain),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
