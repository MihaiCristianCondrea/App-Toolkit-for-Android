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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size.size
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.UnitConverterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.formatters.displayName
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.labelResId
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.UnitConverterToolUiState

@Composable
fun UnitConverterTool(state: UnitConverterToolUiState, onEvent: (UnitConverterToolEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            items(ConversionCategory.entries.size) { index ->
                val category = ConversionCategory.entries[index]
                FilterChip(
                    selected = state.category == category,
                    onClick = { onEvent(UnitConverterToolEvent.CategorySelected(category)) },
                    label = { Text(stringResource(category.labelResId())) },
                    // A selected chip trades its icon for a check mark, as the kit's filter chips do.
                    leadingIcon = {
                        Icon(
                            imageVector = if (category == state.category) ToolSymbols.Check else when (category) {
                                ConversionCategory.Length -> ToolSymbols.Rounded.Straighten
                                ConversionCategory.Mass -> ToolSymbols.Rounded.Weight
                                ConversionCategory.Temperature -> ToolSymbols.Rounded.Thermostat
                                ConversionCategory.Volume -> ToolSymbols.Rounded.WaterDrop
                                ConversionCategory.Data -> ToolSymbols.Rounded.Storage
                            },
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    },
                )
            }
        }
        Box {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                ConversionInput(state.left, state.from, R.string.tool_converter_from, state.invalidInput && state.editingLeft,
                    onInput = { onEvent(UnitConverterToolEvent.InputChanged(it, left = true)) },
                    onUnit = { onEvent(UnitConverterToolEvent.UnitSelected(it, left = true)) })
                ConversionInput(state.right, state.to, R.string.tool_converter_to, state.invalidInput && !state.editingLeft,
                    onInput = { onEvent(UnitConverterToolEvent.InputChanged(it, left = false)) },
                    onUnit = { onEvent(UnitConverterToolEvent.UnitSelected(it, left = false)) })
            }
            FilledTonalIconButton(onClick = { onEvent(UnitConverterToolEvent.Swap) },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = MaterialTheme.spacing.largeIncreased)
                    .size(size = MaterialTheme.size.extraExtraLarge).bounceClick().border(4.dp, MaterialTheme.colorScheme.surface, CircleShape).padding(MaterialTheme.spacing.extraSmall)) {
                Icon(ToolSymbols.SwapVert, stringResource(R.string.swap_units))
            }
        }
        if (state.invalidInput) {
            Text(stringResource(R.string.tool_converter_invalid), color = MaterialTheme.colorScheme.error)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                Icon(ToolSymbols.Edit, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(stringResource(R.string.tool_converter_hint), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
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
    val result = label == R.string.tool_converter_to
    Surface(
        shape = if (result) RoundedCornerShape(8.dp, 8.dp, 24.dp, 24.dp) else RoundedCornerShape(24.dp, 24.dp, 8.dp, 8.dp),
        color = if (result) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(Modifier.fillMaxWidth().padding(MaterialTheme.spacing.largeIncreased), verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(unit.displayName(), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                UnitPicker(unit, onUnit)
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                ),
                value = value,
                onValueChange = onInput,
                label = { Text(stringResource(label)) },
                isError = invalid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

@Composable
private fun UnitPicker(unit: ConversionUnit, onUnit: (ConversionUnit) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = unit.symbol,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(stringResource(R.string.tool_converter_unit)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.width(140.dp).menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ConversionUnit.entries.filter { it.category == unit.category }.forEach { candidate ->
                DropdownMenuItem(
                    text = { Column { Text(candidate.symbol); Text(candidate.displayName(), style = MaterialTheme.typography.bodySmall) } },
                    onClick = { expanded = false; onUnit(candidate) },
                )
            }
        }
    }
}
