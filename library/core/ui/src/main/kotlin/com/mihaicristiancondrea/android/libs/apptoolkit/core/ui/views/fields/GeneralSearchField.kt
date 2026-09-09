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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle

/**
 * The pill-shaped search input, drawn by [GeneralTextFieldStyle.Search].
 *
 * Built on [SearchBarDefaults.InputField], the current Material search input, and used on its own
 * rather than inside a [androidx.compose.material3.SearchBar]: the collapsed search bar intercepts
 * the soft keyboard, so typing only happens once it expands into an
 * [androidx.compose.material3.ExpandedDockedSearchBar], and that surface reserves a 240dp minimum
 * height for the results it expects to host. A field that filters the content behind it as you type
 * has no results of its own, so the expanded surface would only lay an empty panel over that
 * content. The [SearchBarState] is still hoisted here because the input field drives it, and it
 * keeps the field's focus and keyboard behavior intact.
 *
 * This is also why the search style is the one style the `TextFieldValue` overload refuses: the
 * Material input owns its text state, and there is no caret to hand over.
 *
 * [value] stays the single source of truth. Edits flow out through [onValueChange], and changes made
 * outside the field, such as a clear action, flow back into its own [TextFieldState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GeneralSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    readOnly: Boolean,
    textStyle: TextStyle,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    shape: Shape?,
    colors: TextFieldColors?,
    onSearch: ((String) -> Unit)?,
) {
    val searchBarState: SearchBarState = rememberSearchBarState()
    val textFieldState: TextFieldState = rememberTextFieldState(initialText = value)
    val focusManager: FocusManager = LocalFocusManager.current
    val currentOnValueChange: (String) -> Unit by rememberUpdatedState(newValue = onValueChange)

    LaunchedEffect(key1 = textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .collect { text -> currentOnValueChange(text) }
    }

    LaunchedEffect(key1 = value) {
        if (textFieldState.text.toString() != value) {
            textFieldState.setTextAndPlaceCursorAtEnd(text = value)
        }
    }

    SearchBarDefaults.InputField(
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        onSearch = { query ->
            // Submitting a filter that already applied itself as it was typed means "I am done
            // typing", so the keyboard goes away whether or not the host does anything else.
            focusManager.clearFocus()
            onSearch?.invoke(query)
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape ?: CircleShape,
        colors = colors ?: SearchBarDefaults.inputFieldColors(),
    )
}
