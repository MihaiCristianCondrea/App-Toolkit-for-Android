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

import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

enum class GeneralTextFieldStyle {

    /** The Material filled field, the platform default. */
    Filled,

    Outlined,

    /**
     * A filled field without its indicator line, cut to the corners of the group it sits in. Use it
     * for a block of fields that reads as one card, such as a form; `position` says where in the
     * group this one is.
     */
    Grouped,

    /**
     * The pill-shaped Material search input, for a field that filters the content behind it as it
     * is typed, such as a top app bar that swaps its title for a search box, say. It leads with a search
     * icon unless another one is given, and takes its clear or filter actions in the trailing slot.
     *
     * It is a different Material component rather than a rounded text field, so the parameters that
     * describe a form field (label, supportingText, errorText, minLines, maxLines,
     * markdown, position) do not apply to it, and it is rejected by the `TextFieldValue`
     * overload because the Material input owns its text state.
     */
    Search,

    /**
     * The search field as an outlined one: the Material outlined text field, fully rounded and
     * leading with a search icon. Take it where a filled pill would disappear into the surface
     * behind it (a search box on a page rather than in an app bar) or where the rest of the screen
     * is outlined. Unlike [Search] it is an ordinary text field, so every parameter applies to it and
     * the `TextFieldValue` overload accepts it.
     */
    SearchOutlined,
}
