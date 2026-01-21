package com.d4rk.android.apps.apptoolkit.components.ui.state

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
