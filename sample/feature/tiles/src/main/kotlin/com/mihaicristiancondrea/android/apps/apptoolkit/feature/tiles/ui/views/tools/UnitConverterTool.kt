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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.UnitConverterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.UnitConverterToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle

@Composable
fun UnitConverterTool(state: UnitConverterToolUiState, onEvent: (UnitConverterToolEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
            items(ConversionCategory.entries.size) { index ->
                val category = ConversionCategory.entries[index]
                FilterChip(
                    selected = state.category == category,
                    onClick = { onEvent(UnitConverterToolEvent.CategorySelected(category)) },
                    label = { Text(stringResource(category.labelResId())) },
                )
            }
        }
        ConversionInput(state.left, state.from, R.string.tool_converter_from, state.invalidInput && state.editingLeft,
            onInput = { onEvent(UnitConverterToolEvent.InputChanged(it, left = true)) },
            onUnit = { onEvent(UnitConverterToolEvent.UnitSelected(it, left = true)) })
        FilledTonalIconButton(onClick = { onEvent(UnitConverterToolEvent.Swap) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Icon(Icons.Outlined.SwapVert, stringResource(R.string.swap_units))
        }
        ConversionInput(state.right, state.to, R.string.tool_converter_to, state.invalidInput && !state.editingLeft,
            onInput = { onEvent(UnitConverterToolEvent.InputChanged(it, left = false)) },
            onUnit = { onEvent(UnitConverterToolEvent.UnitSelected(it, left = false)) })
        if (state.invalidInput) Text(stringResource(R.string.tool_converter_invalid), color = MaterialTheme.colorScheme.error)
        if (state.category == ConversionCategory.Data) Text(stringResource(R.string.tool_converter_decimal_note))
    }
}

@Composable
private fun ConversionInput(
    value: String,
    unit: ConversionUnit,
    label: Int,
    invalid: Boolean,
    onInput: (String) -> Unit,
    onUnit: (ConversionUnit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Surface(shape = RoundedCornerShape(24.dp), color = if (label == R.string.tool_converter_to) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh) {
    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onInput,
            label = { Text(stringResource(label)) },
            isError = invalid,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = MaterialTheme.typography.headlineSmall,
        )
        Box {
            GeneralButton(
                onClick = { expanded = true },
                style = GeneralButtonStyle.Tonal,
                label = unit.symbol,
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                ConversionUnit.entries.filter { it.category == unit.category }.forEach { candidate ->
                    DropdownMenuItem(
                        text = { Text(candidate.symbol) },
                        onClick = { expanded = false; onUnit(candidate) },
                    )
                }
            }
        }
    }
    }
}

internal fun ConversionCategory.labelResId(): Int = when (this) {
    ConversionCategory.Length -> R.string.tool_converter_length
    ConversionCategory.Mass -> R.string.tool_converter_mass
    ConversionCategory.Temperature -> R.string.tool_converter_temperature
    ConversionCategory.Volume -> R.string.tool_converter_volume
    ConversionCategory.Data -> R.string.tool_converter_data
}
