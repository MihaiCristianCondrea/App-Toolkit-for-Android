package com.d4rk.android.apps.apptoolkit.components.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.components.ui.state.ComponentsUiState
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.FilledIconButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.FilledTonalIconButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralTextButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralTonalButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.IconButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.OutlinedIconButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab.SmallFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.dropdown.CommonDropdownMenuItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.fields.DatePickerTextField
import com.d4rk.android.libs.apptoolkit.core.ui.views.fields.DropdownMenuBox
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.TopListFilters
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.CheckBoxPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.persistentListOf

/**
 * Route-level composable that manages state for the components showcase screen.
 */
@Composable
fun ComponentsRoute(
    paddingValues: PaddingValues,
) {
    val dropdownOptionOne = stringResource(id = R.string.components_option_alpha)
    val dropdownOptionTwo = stringResource(id = R.string.components_option_beta)
    val dropdownOptionThree = stringResource(id = R.string.components_option_gamma)
    val dropdownOptions = remember(dropdownOptionOne, dropdownOptionTwo, dropdownOptionThree) {
        persistentListOf(dropdownOptionOne, dropdownOptionTwo, dropdownOptionThree)
    }

    val filterPopular = stringResource(id = R.string.components_filter_popular)
    val filterRecent = stringResource(id = R.string.components_filter_recent)
    val filterFavorites = stringResource(id = R.string.components_filter_favorites)
    val filters = remember(filterPopular, filterRecent, filterFavorites) {
        persistentListOf(filterPopular, filterRecent, filterFavorites)
    }

    val radioSystem = stringResource(id = R.string.components_radio_system)
    val radioLight = stringResource(id = R.string.components_radio_light)
    val radioDark = stringResource(id = R.string.components_radio_dark)
    val radioOptions = remember(radioSystem, radioLight, radioDark) {
        persistentListOf(radioSystem, radioLight, radioDark)
    }

    var selectedDropdownOption by rememberSaveable { mutableStateOf(dropdownOptions.first()) }
    var selectedFilter by rememberSaveable { mutableStateOf(filters.first()) }
    var selectedRadioOption by rememberSaveable { mutableStateOf(radioOptions.first()) }
    var selectedDateMillis by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    var switchEnabled by rememberSaveable { mutableStateOf(true) }
    var switchWithDividerEnabled by rememberSaveable { mutableStateOf(false) }
    var switchCardEnabled by rememberSaveable { mutableStateOf(false) }
    var checkboxChecked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(dropdownOptions) {
        if (selectedDropdownOption !in dropdownOptions) {
            selectedDropdownOption = dropdownOptions.first()
        }
    }
    LaunchedEffect(filters) {
        if (selectedFilter !in filters) {
            selectedFilter = filters.first()
        }
    }
    LaunchedEffect(radioOptions) {
        if (selectedRadioOption !in radioOptions) {
            selectedRadioOption = radioOptions.first()
        }
    }

    val state = ComponentsUiState(
        dropdownOptions = dropdownOptions,
        selectedDropdownOption = selectedDropdownOption,
        dateMillis = selectedDateMillis,
        filters = filters,
        selectedFilter = selectedFilter,
        switchEnabled = switchEnabled,
        switchWithDividerEnabled = switchWithDividerEnabled,
        switchCardEnabled = switchCardEnabled,
        checkboxChecked = checkboxChecked,
        radioOptions = radioOptions,
        selectedRadioOption = selectedRadioOption,
    )

    ComponentsScreen(
        paddingValues = paddingValues,
        state = state,
        onDropdownOptionSelected = { selectedDropdownOption = it },
        onDateSelected = { selectedDateMillis = it },
        onFilterSelected = { selectedFilter = it },
        onSwitchEnabledChanged = { switchEnabled = it },
        onSwitchWithDividerChanged = { switchWithDividerEnabled = it },
        onSwitchCardChanged = { switchCardEnabled = it },
        onCheckboxChanged = { checkboxChecked = it },
        onRadioOptionSelected = { selectedRadioOption = it },
    )
}

/**
 * Stateless components showcase that renders all custom UI elements in a scrolling list.
 */
@Composable
fun ComponentsScreen(
    paddingValues: PaddingValues,
    state: ComponentsUiState,
    onDropdownOptionSelected: (String) -> Unit,
    onDateSelected: (Long) -> Unit,
    onFilterSelected: (String) -> Unit,
    onSwitchEnabledChanged: (Boolean) -> Unit,
    onSwitchWithDividerChanged: (Boolean) -> Unit,
    onSwitchCardChanged: (Boolean) -> Unit,
    onCheckboxChanged: (Boolean) -> Unit,
    onRadioOptionSelected: (String) -> Unit,
) {
    val iconContentDescription = stringResource(id = R.string.components_icon_content_description)
    val menuLabel = stringResource(id = R.string.components_menu_label)
    val switchCardState: State<Boolean> = rememberUpdatedState(state.switchCardEnabled)

    LazyColumn(
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_buttons))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize)
            ) {
                GeneralButton(
                    label = stringResource(id = R.string.components_button_primary),
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralButton(
                    label = stringResource(id = R.string.components_button_primary),
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralButton(
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralTonalButton(
                    label = stringResource(id = R.string.components_button_tonal),
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralTonalButton(
                    label = stringResource(id = R.string.components_button_tonal),
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralTonalButton(
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralOutlinedButton(
                    label = stringResource(id = R.string.components_button_outlined),
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralOutlinedButton(
                    label = stringResource(id = R.string.components_button_outlined),
                    vectorIcon = Icons.Outlined.Info,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralOutlinedButton(
                    vectorIcon = Icons.Outlined.Info,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralTextButton(
                    label = stringResource(id = R.string.components_button_text),
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralTextButton(
                    label = stringResource(id = R.string.components_button_text),
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                SmallVerticalSpacer()
                GeneralTextButton(
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                )
                ExtraSmallVerticalSpacer()
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        onClick = {},
                        vectorIcon = Icons.Outlined.Info,
                        iconContentDescription = iconContentDescription,
                    )
                    FilledIconButton(
                        onClick = {},
                        vectorIcon = Icons.Outlined.Favorite,
                        iconContentDescription = iconContentDescription,
                    )
                    FilledTonalIconButton(
                        onClick = {},
                        vectorIcon = Icons.Outlined.StarOutline,
                        iconContentDescription = iconContentDescription,
                    )
                    OutlinedIconButton(
                        onClick = {},
                        vectorIcon = Icons.Outlined.FilterAlt,
                        iconContentDescription = iconContentDescription,
                    )
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_fabs))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AnimatedFloatingActionButton(
                        isVisible = true,
                        icon = Icons.Filled.Add,
                        contentDescription = iconContentDescription,
                        onClick = {},
                    )
                    SmallFloatingActionButton(
                        isVisible = true,
                        isExtended = true,
                        icon = Icons.Filled.Add,
                        contentDescription = iconContentDescription,
                        onClick = {},
                    )
                }
                SmallVerticalSpacer()
                AnimatedExtendedFloatingActionButton(
                    onClick = {},
                    visible = true,
                    expanded = true,
                    icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.components_fab_extended)) },
                )
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_inputs))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize)
            ) {
                var showMenu by rememberSaveable { mutableStateOf(false) }
                Column {
                    GeneralOutlinedButton(
                        label = menuLabel,
                        vectorIcon = Icons.Filled.MoreVert,
                        iconContentDescription = iconContentDescription,
                        onClick = { showMenu = true },
                    )
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        CommonDropdownMenuItem(
                            textResId = R.string.components_menu_option_primary,
                            icon = Icons.Outlined.Info,
                            onClick = { showMenu = false },
                        )
                        CommonDropdownMenuItem(
                            textResId = R.string.components_menu_option_secondary,
                            icon = Icons.Outlined.Favorite,
                            onClick = { showMenu = false },
                        )
                    }
                }
                SmallVerticalSpacer()
                DatePickerTextField(
                    dateMillis = state.dateMillis,
                    onDateSelected = onDateSelected,
                )
                SmallVerticalSpacer()
                DropdownMenuBox(
                    selectedText = state.selectedDropdownOption,
                    options = state.dropdownOptions,
                    onOptionSelected = onDropdownOptionSelected,
                )
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_preferences))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize)
            ) {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.components_preference_title),
                    summary = stringResource(id = R.string.components_preference_summary),
                )
                ExtraTinyVerticalSpacer()
                PreferenceItem(
                    title = stringResource(id = R.string.components_preference_secondary_title),
                    summary = stringResource(id = R.string.components_preference_secondary_summary),
                )
                ExtraTinyVerticalSpacer()
                SwitchPreferenceItem(
                    icon = Icons.Outlined.Favorite,
                    title = stringResource(id = R.string.components_switch_title),
                    summary = stringResource(id = R.string.components_switch_summary),
                    checked = state.switchEnabled,
                    onCheckedChange = onSwitchEnabledChanged,
                )
                ExtraTinyVerticalSpacer()
                SwitchPreferenceItemWithDivider(
                    icon = Icons.Outlined.Info,
                    title = stringResource(id = R.string.components_switch_divider_title),
                    summary = stringResource(id = R.string.components_switch_divider_summary),
                    checked = state.switchWithDividerEnabled,
                    onCheckedChange = onSwitchWithDividerChanged,
                    onClick = {},
                    onSwitchClick = {},
                )
                ExtraTinyVerticalSpacer()
                SwitchCardItem(
                    title = stringResource(id = R.string.components_switch_card_title),
                    switchState = switchCardState,
                    onSwitchToggled = onSwitchCardChanged,
                )
                ExtraTinyVerticalSpacer()
                CheckBoxPreferenceItem(
                    icon = Icons.Outlined.Favorite,
                    title = stringResource(id = R.string.components_checkbox_title),
                    summary = stringResource(id = R.string.components_checkbox_summary),
                    checked = state.checkboxChecked,
                    onCheckedChange = onCheckboxChanged,
                )
                ExtraTinyVerticalSpacer()
                state.radioOptions.forEach { option ->
                    RadioButtonPreferenceItem(
                        text = option,
                        isChecked = state.selectedRadioOption == option,
                        onCheckedChange = { onRadioOptionSelected(option) },
                    )
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_filters))
            TopListFilters(
                filters = state.filters,
                selectedFilter = state.selectedFilter,
                onFilterSelected = onFilterSelected,
            )
        }
    }
}
