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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HorizontalRule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers.HorizontalWavyDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers.VerticalWavyDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers.WavyDividerDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer

/**
 * The wavy divider at its default size, resized live, and running vertically.
 *
 * The sliders are there to show the divider adapting: however wide or tall it is made, the wave
 * keeps its shape and ends cleanly on both sides.
 */
@Composable
fun DividerShowcase() {
    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_dividers),
        icon = Icons.Outlined.HorizontalRule,
    )

    var widthFraction: Float by rememberSaveable { mutableFloatStateOf(value = 1f) }
    var waveHeight: Float by rememberSaveable {
        mutableFloatStateOf(value = WavyDividerDefaults.WaveSize.value)
    }

    ShowcaseSection {
        ShowcaseSurface(position = GroupedItemPosition.FIRST) {
            DividerLabel(text = stringResource(id = R.string.components_divider_wavy))
            LargeVerticalSpacer()
            HorizontalWavyDivider()
        }

        ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
            // Scaled with the wave, as il_wavy_line is, so a tall wave is not drawn as a hairline.
            val thickness: Float =
                (waveHeight / WavyDividerDefaults.WaveSize.value).coerceAtLeast(minimumValue = 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MAX_WAVE_HEIGHT.dp),
                contentAlignment = Alignment.Center,
            ) {
                HorizontalWavyDivider(
                    modifier = Modifier.fillMaxWidth(fraction = widthFraction),
                    waveHeight = waveHeight.dp,
                    thickness = thickness.dp,
                )
            }
            SmallVerticalSpacer()
            DividerLabel(text = stringResource(id = R.string.components_divider_width))
            Slider(
                value = widthFraction,
                onValueChange = { widthFraction = it },
                valueRange = MIN_WIDTH_FRACTION..1f,
            )
            DividerLabel(text = stringResource(id = R.string.components_divider_wave_height))
            Slider(
                value = waveHeight,
                onValueChange = { waveHeight = it },
                valueRange = MIN_WAVE_HEIGHT..MAX_WAVE_HEIGHT,
            )
        }

        ShowcaseSurface(position = GroupedItemPosition.LAST) {
            DividerLabel(text = stringResource(id = R.string.components_divider_vertical))
            LargeVerticalSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SizeConstants.ExtraLargeIncreasedSize * 2),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = stringResource(id = R.string.components_option_alpha))
                VerticalWavyDivider()
                Text(text = stringResource(id = R.string.components_option_beta))
                VerticalWavyDivider()
                Text(text = stringResource(id = R.string.components_option_gamma))
            }
        }
    }
}

@Composable
private fun DividerLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}

private const val MIN_WAVE_HEIGHT: Float = 4f
private const val MAX_WAVE_HEIGHT: Float = 24f
private const val MIN_WIDTH_FRACTION: Float = 0.2f
