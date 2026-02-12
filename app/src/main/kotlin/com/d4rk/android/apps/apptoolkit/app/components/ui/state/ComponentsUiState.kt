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

package com.d4rk.android.apps.apptoolkit.app.components.ui.state

import kotlinx.collections.immutable.ImmutableList

/**
 * UI state holder for the components showcase screen.
 */
data class ComponentsUiState(
    val dropdownOptions: ImmutableList<String>,
    val selectedDropdownOption: String,
    val dateMillis: Long,
    val filters: ImmutableList<String>,
    val selectedFilter: String,
    val switchEnabled: Boolean,
    val switchWithDividerEnabled: Boolean,
    val switchCardEnabled: Boolean,
    val checkboxChecked: Boolean,
    val radioOptions: ImmutableList<String>,
    val selectedRadioOption: String,
)
