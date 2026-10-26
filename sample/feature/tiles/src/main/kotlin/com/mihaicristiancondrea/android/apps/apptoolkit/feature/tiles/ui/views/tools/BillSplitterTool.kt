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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BillSplitterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BillSplitterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions.toToolLongOrNull
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import java.text.NumberFormat

@Composable
fun BillSplitterTool(state: BillSplitterToolUiState, onEvent: (BillSplitterToolEvent) -> Unit) {
    val locale = LocalConfiguration.current.locales[0]
    val numberFormat = remember(locale) {
        NumberFormat.getNumberInstance(locale).apply { minimumFractionDigits = 2; maximumFractionDigits = 2 }
    }
    val people = state.people.toToolLongOrNull()
    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        OutlinedTextField(
            value = state.bill,
            onValueChange = { onEvent(BillSplitterToolEvent.BillChanged(it)) },
            label = { Text(stringResource(R.string.tool_bill_amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            isError = state.invalidInput,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(stringResource(R.string.tool_bill_tip), style = MaterialTheme.typography.titleSmall)
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            listOf(10, 15, 20).forEachIndexed { index, percent ->
                SegmentedButton(
                    selected = state.tipPercent == percent,
                    onClick = { onEvent(BillSplitterToolEvent.TipSelected(percent)) },
                    shape = SegmentedButtonDefaults.itemShape(index, 3),
                ) {
                    Text(stringResource(R.string.tool_tip_percent, percent))
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onEvent(BillSplitterToolEvent.RemovePerson) },
                enabled = people != null && people > 1,
            ) {
                Icon(Icons.Outlined.Remove, stringResource(R.string.tool_bill_remove_person))
            }
            OutlinedTextField(
                value = state.people,
                onValueChange = { onEvent(BillSplitterToolEvent.PeopleChanged(it)) },
                label = { Text(stringResource(R.string.tool_bill_people)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = state.invalidInput,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { onEvent(BillSplitterToolEvent.AddPerson) },
                enabled = people != null && people in 1 until Long.MAX_VALUE,
            ) {
                Icon(Icons.Outlined.Add, stringResource(R.string.tool_bill_add_person))
            }
        }
        if (state.invalidInput) {
            Text(stringResource(R.string.tool_bill_invalid), color = MaterialTheme.colorScheme.error)
        }
        state.result?.let { result ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Column(
                    modifier = Modifier.padding(SizeConstants.LargeSize),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
                ) {
                    Text(stringResource(R.string.tool_bill_per_person), style = MaterialTheme.typography.labelLarge)
                    Text(numberFormat.format(result.perPerson), style = MaterialTheme.typography.displayMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
                        BillSummary(stringResource(R.string.tool_bill_total), numberFormat.format(result.total), Modifier.weight(1f))
                        BillSummary(stringResource(R.string.tool_bill_tip), numberFormat.format(result.tip), Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
                        Icon(Icons.Outlined.Info, contentDescription = null)
                        Text(stringResource(R.string.tool_bill_rounding), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun BillSummary(label: String, amount: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(amount, style = MaterialTheme.typography.bodyLarge)
    }
}
