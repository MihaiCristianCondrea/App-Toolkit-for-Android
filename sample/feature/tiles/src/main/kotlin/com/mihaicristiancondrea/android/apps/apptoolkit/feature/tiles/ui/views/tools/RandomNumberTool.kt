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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RandomNumberToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions.toToolLongOrNull
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.digits.AnimatedDigit
import java.text.NumberFormat

@Composable
fun RandomNumberTool(state: RandomNumberToolUiState, onEvent: (RandomNumberToolEvent) -> Unit) {
    val locale = LocalConfiguration.current.locales[0]
    val numberFormat = remember(locale) { NumberFormat.getIntegerInstance(locale) }
    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
            OutlinedTextField(
                value = state.minimum,
                onValueChange = { onEvent(RandomNumberToolEvent.MinimumChanged(it)) },
                label = { Text(stringResource(R.string.tool_random_minimum)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = state.invalidInput,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = state.maximum,
                onValueChange = { onEvent(RandomNumberToolEvent.MaximumChanged(it)) },
                label = { Text(stringResource(R.string.tool_random_maximum)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = state.invalidInput,
                modifier = Modifier.weight(1f),
            )
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
            val maxima = listOf(6L, 10L, 100L, 2L)
            items(maxima.size) { index ->
                val maximum = maxima[index]
                FilterChip(
                    selected = state.minimum.toToolLongOrNull() == 1L && state.maximum.toToolLongOrNull() == maximum,
                    onClick = { onEvent(RandomNumberToolEvent.PresetSelected(maximum)) },
                    label = { Text(stringResource(R.string.tool_random_range, 1, maximum)) },
                )
            }
        }
        if (state.invalidInput) Text(stringResource(R.string.tool_random_invalid), color = MaterialTheme.colorScheme.error)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Box(
                modifier = Modifier.heightIn(min = 100.dp).padding(SizeConstants.LargeSize),
                contentAlignment = Alignment.Center,
            ) {
                val result = state.result?.let(numberFormat::format) ?: stringResource(R.string.tool_random_placeholder)
                val textStyle = if (result.length > 12) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.displayLarge
                if (state.result == null) {
                    Text(result, style = textStyle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    // Keep signed, localized numbers in reading order and announce the complete value once.
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        FlowRow(
                            modifier = Modifier.clearAndSetSemantics { text = AnnotatedString(result) },
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            result.forEachIndexed { index, char ->
                                key(result.length - index) {
                                    if (char.isDigit()) {
                                        AnimatedDigit(digit = char, textStyle = textStyle)
                                    } else {
                                        Text(char.toString(), style = textStyle)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        GeneralButton(
            onClick = { onEvent(RandomNumberToolEvent.Generate) },
            label = stringResource(R.string.tool_random_generate),
            enabled = !state.invalidInput,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
