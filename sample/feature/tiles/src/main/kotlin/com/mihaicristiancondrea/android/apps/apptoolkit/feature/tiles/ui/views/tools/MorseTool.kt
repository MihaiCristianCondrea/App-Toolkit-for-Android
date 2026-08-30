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

/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseInputError
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ResultPill
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

@Composable
fun MorseTool(
    state: MorseToolState,
    onInputChanged: (String) -> Unit,
    onToggle: () -> Unit,
) {
    val playback = state.playback
    val errorMessage = state.inputError?.let { error ->
        stringResource(
            when (error) {
                MorseInputError.Empty -> R.string.morse_error_empty
                MorseInputError.TooLong -> R.string.morse_error_length
                MorseInputError.UnsupportedCharacters -> R.string.morse_error_characters
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        ResultPill(
            label = stringResource(
                when {
                    !playback.isAvailable -> R.string.flash_dimmer_unavailable
                    playback.isActive -> R.string.morse_active
                    else -> R.string.morse_ready
                }
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = state.input,
            onValueChange = onInputChanged,
            modifier = Modifier.fillMaxWidth(),
            enabled = !playback.isActive,
            label = { Text(stringResource(R.string.morse_message_label)) },
            supportingText = errorMessage?.let { { Text(it) } },
            isError = errorMessage != null,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            shape = RoundedCornerShape(SizeConstants.ExtraLargeCompactSize),
            minLines = 2,
            maxLines = 3,
        )

        if (playback.isActive && playback.currentCharacter != null) {
            Text(
                text = stringResource(
                    R.string.morse_current_character,
                    playback.currentCharacter.toString(),
                    playback.currentCode,
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Button(
            onClick = onToggle,
            modifier = Modifier.fillMaxWidth(),
            enabled = playback.isAvailable && (playback.isActive || state.input.isNotBlank()),
        ) {
            Text(
                stringResource(if (playback.isActive) R.string.morse_stop else R.string.morse_start)
            )
        }
    }
}
