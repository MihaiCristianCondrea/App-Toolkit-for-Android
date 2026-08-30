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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.models.TorchState
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.ResultPill
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import kotlin.math.roundToInt

@Composable
fun FlashDimmerTool(
    state: TorchState,
    onLevelChanged: (Int) -> Unit,
    onPresetSelected: (TorchPreset) -> Unit,
) {
    val capabilities = state.capabilities

    if (!capabilities.isAvailable) {
        ResultPill(
            label = stringResource(R.string.flash_dimmer_unavailable),
            modifier = Modifier.fillMaxWidth(),
        )
        return
    }

    if (!capabilities.supportsDimming) {
        SimpleTorchControl(state = state, onPresetSelected = onPresetSelected)
        return
    }

    val maximumLevel = capabilities.maximumLevel
    val levelDescription = stringResource(
        R.string.flash_dimmer_level,
        state.currentLevel,
        maximumLevel,
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        Text(
            text = levelDescription,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(SizeConstants.TwoHundredFortySize),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PresetControls(
                currentLevel = state.currentLevel,
                maximumLevel = maximumLevel,
                onPresetSelected = onPresetSelected,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
            )
            SegmentedLevelControl(
                currentLevel = state.currentLevel,
                maximumLevel = maximumLevel,
                stateDescription = levelDescription,
                onLevelChanged = onLevelChanged,
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(horizontal = SizeConstants.MediumSize),
            )
        }
    }
}

@Composable
private fun PresetControls(
    currentLevel: Int,
    maximumLevel: Int,
    onPresetSelected: (TorchPreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val halfLevel = ((maximumLevel + 1) / 2).coerceAtLeast(1)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        PresetButton(
            label = stringResource(R.string.flash_dimmer_maximum),
            selected = currentLevel == maximumLevel,
            onClick = { onPresetSelected(TorchPreset.Maximum) },
            modifier = Modifier.weight(1f),
        )
        PresetButton(
            label = stringResource(R.string.flash_dimmer_half),
            selected = currentLevel == halfLevel,
            onClick = { onPresetSelected(TorchPreset.Half) },
            modifier = Modifier.weight(1f),
        )
        PresetButton(
            label = stringResource(R.string.flash_dimmer_minimum),
            selected = currentLevel == 1,
            onClick = { onPresetSelected(TorchPreset.Minimum) },
            modifier = Modifier.weight(1f),
        )
        PresetButton(
            label = stringResource(R.string.flash_dimmer_turn_off),
            selected = currentLevel == 0,
            onClick = { onPresetSelected(TorchPreset.Off) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PresetButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = if (selected) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.filledTonalButtonColors()
        },
    ) {
        Text(text = label)
    }
}

@Composable
private fun SegmentedLevelControl(
    currentLevel: Int,
    maximumLevel: Int,
    stateDescription: String,
    onLevelChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Canvas(
        modifier = modifier
            .semantics {
                this.stateDescription = stateDescription
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = currentLevel.toFloat(),
                    range = 0f..maximumLevel.toFloat(),
                    steps = (maximumLevel - 1).coerceAtLeast(0),
                )
                setProgress { targetLevel ->
                    onLevelChanged(targetLevel.roundToInt().coerceIn(0, maximumLevel))
                    true
                }
            }
            .pointerInput(maximumLevel, onLevelChanged) {
                awaitEachGesture {
                    var lastRequestedLevel = Int.MIN_VALUE
                    fun updateLevel(y: Float) {
                        val selectedLevel = (
                            maximumLevel *
                                (1f - y.coerceIn(0f, size.height.toFloat()) / size.height)
                            ).roundToInt().coerceIn(0, maximumLevel)
                        if (selectedLevel != lastRequestedLevel) {
                            lastRequestedLevel = selectedLevel
                            onLevelChanged(selectedLevel)
                        }
                    }

                    var change = awaitFirstDown()
                    updateLevel(change.position.y)
                    while (change.pressed) {
                        val event = awaitPointerEvent()
                        change = event.changes.first()
                        updateLevel(change.position.y)
                        change.consume()
                    }
                }
            },
    ) {
        val segmentGap = minOf(3.dp.toPx(), size.height / maximumLevel * 0.3f)
        val segmentHeight = (
            (size.height - segmentGap * (maximumLevel - 1)) / maximumLevel
            ).coerceAtLeast(1f)
        val cornerRadius = CornerRadius(SizeConstants.SmallSize.toPx())

        repeat(maximumLevel) { index ->
            val representedLevel = maximumLevel - index
            val top = index * (segmentHeight + segmentGap)
            drawRoundRect(
                color = when {
                    currentLevel >= representedLevel -> activeColor
                    else -> inactiveColor
                },
                topLeft = Offset(x = 0f, y = top),
                size = Size(width = size.width, height = segmentHeight),
                cornerRadius = cornerRadius,
            )
        }
    }
}

@Composable
private fun SimpleTorchControl(
    state: TorchState,
    onPresetSelected: (TorchPreset) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        ResultPill(
            label = stringResource(
                if (state.isEnabled) R.string.flash_dimmer_on else R.string.flash_dimmer_off
            )
        )
        Button(
            onClick = {
                onPresetSelected(if (state.isEnabled) TorchPreset.Off else TorchPreset.Maximum)
            },
        ) {
            Text(
                stringResource(
                    if (state.isEnabled) R.string.flash_dimmer_turn_off
                    else R.string.flash_dimmer_turn_on
                )
            )
        }
    }
}
