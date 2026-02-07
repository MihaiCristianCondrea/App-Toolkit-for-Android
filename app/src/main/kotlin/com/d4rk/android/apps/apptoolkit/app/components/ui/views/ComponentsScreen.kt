package com.d4rk.android.apps.apptoolkit.app.components.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.components.ui.state.ComponentsUiState
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralTextButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralTonalButton
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
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.koinInject
import com.d4rk.android.libs.apptoolkit.R as ToolkitR

/**
 * Route-level composable that manages state for the components showcase screen.
 */
@Composable
fun ComponentsRoute(
    paddingValues: PaddingValues,
) {
    val firebaseController: FirebaseController = koinInject()
    val dropdownOptionOne = stringResource(id = R.string.components_option_alpha)
    val dropdownOptionTwo = stringResource(id = R.string.components_option_beta)
    val dropdownOptionThree = stringResource(id = R.string.components_option_gamma)
    val dropdownOptions = remember(dropdownOptionOne, dropdownOptionTwo, dropdownOptionThree) {
        persistentListOf(dropdownOptionOne, dropdownOptionTwo, dropdownOptionThree)
    }

    val filterPopular = stringResource(id = R.string.components_filter_popular)
    val filterRecent = stringResource(id = R.string.components_filter_recent)
    val filterFavorites = stringResource(id = R.string.favorite_apps)
    val filters = remember(filterPopular, filterRecent, filterFavorites) {
        persistentListOf(filterPopular, filterRecent, filterFavorites)
    }

    val radioSystem = stringResource(id = ToolkitR.string.follow_system)
    val radioLight = stringResource(id = ToolkitR.string.light_mode)
    val radioDark = stringResource(id = ToolkitR.string.dark_mode)
    val radioOptions = remember(radioSystem, radioLight, radioDark) {
        persistentListOf(radioSystem, radioLight, radioDark)
    }

    var selectedDropdownOption by rememberSaveable { mutableStateOf(dropdownOptions.first()) }
    var selectedFilter by rememberSaveable { mutableStateOf(filters.first()) }
    var selectedRadioOption by rememberSaveable { mutableStateOf(radioOptions.first()) }
    var selectedDateMillis by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
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
        firebaseController = firebaseController,
        state = state,
        onDropdownOptionSelected = { selectedDropdownOption = it },
        onDateSelected = { selectedDateMillis = it }, // FIXME: Assigned value is never read
        onFilterSelected = { selectedFilter = it }, // FIXME: Assigned value is never read
        onSwitchEnabledChanged = { switchEnabled = it }, // FIXME: Assigned value is never read
        onSwitchWithDividerChanged = {
            switchWithDividerEnabled = it
        }, // FIXME: Assigned value is never read
        onSwitchCardChanged = { switchCardEnabled = it }, // FIXME: Assigned value is never read
        onCheckboxChanged = { checkboxChecked = it }, // FIXME: Assigned value is never read
        onRadioOptionSelected = { selectedRadioOption = it },
    )
}

/**
 * Stateless components showcase that renders all custom UI elements in a scrolling list.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ComponentsScreen(
    paddingValues: PaddingValues,
    firebaseController: FirebaseController,
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
    val screenParam = AnalyticsValue.Str("components")

    fun ga4Event(component: String, variant: String? = null): Ga4EventData {
        val params = buildMap {
            put("screen", screenParam)
            put("component", AnalyticsValue.Str(component))
            if (variant != null) {
                put("variant", AnalyticsValue.Str(variant))
            }
        }
        return Ga4EventData(name = "components_click", params = params)
    }

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
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "primary"),
                )
                SmallVerticalSpacer()
                GeneralButton(
                    label = stringResource(id = R.string.components_button_primary),
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "primary_icon"),
                )
                SmallVerticalSpacer()
                GeneralButton(
                    vectorIcon = Icons.Outlined.StarOutline,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "primary_icon_only"),
                )
                SmallVerticalSpacer()
                GeneralTonalButton(
                    label = stringResource(id = R.string.components_button_tonal),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "tonal"),
                )
                SmallVerticalSpacer()
                GeneralTonalButton(
                    label = stringResource(id = R.string.components_button_tonal),
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "tonal_icon"),
                )
                SmallVerticalSpacer()
                GeneralTonalButton(
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "tonal_icon_only"),
                )
                SmallVerticalSpacer()
                GeneralOutlinedButton(
                    label = stringResource(id = R.string.components_button_outlined),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "outlined"),
                )
                SmallVerticalSpacer()
                GeneralOutlinedButton(
                    label = stringResource(id = R.string.components_button_outlined),
                    vectorIcon = Icons.Outlined.Info,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "outlined_icon"),
                )
                SmallVerticalSpacer()
                GeneralOutlinedButton(
                    vectorIcon = Icons.Outlined.Info,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "outlined_icon_only"),
                )
                SmallVerticalSpacer()
                GeneralTextButton(
                    label = stringResource(id = R.string.components_button_text),
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "text"),
                )
                SmallVerticalSpacer()
                GeneralTextButton(
                    label = stringResource(id = R.string.components_button_text),
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "text_icon"),
                )
                SmallVerticalSpacer()
                GeneralTextButton(
                    vectorIcon = Icons.Outlined.Favorite,
                    iconContentDescription = iconContentDescription,
                    onClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "button", variant = "text_icon_only"),
                )
                ExtraSmallVerticalSpacer()
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
                        firebaseController = firebaseController,
                        ga4Event = ga4Event(component = "fab", variant = "animated"),
                    )
                    SmallFloatingActionButton(
                        isVisible = true,
                        isExtended = true,
                        icon = Icons.Filled.Add,
                        contentDescription = iconContentDescription,
                        onClick = {},
                        firebaseController = firebaseController,
                        ga4Event = ga4Event(component = "fab", variant = "small"),
                    )
                }
                SmallVerticalSpacer()
                AnimatedExtendedFloatingActionButton(
                    onClick = {},
                    visible = true,
                    expanded = true,
                    icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.components_fab_extended)) },
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "fab", variant = "extended"),
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
                        firebaseController = firebaseController,
                        ga4Event = ga4Event(component = "dropdown", variant = "menu_button"),
                    )
                    DropdownMenu(
                        expanded = showMenu,
                        shape = MaterialTheme.shapes.largeIncreased,
                        onDismissRequest = { showMenu = false },
                    ) {
                        CommonDropdownMenuItem(
                            textResId = R.string.components_menu_option_primary,
                            icon = Icons.Outlined.Info,
                            onClick = { showMenu = false },
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "dropdown", variant = "menu_option_primary"),
                        )
                        CommonDropdownMenuItem(
                            textResId = R.string.components_menu_option_secondary,
                            icon = Icons.Outlined.Favorite,
                            onClick = { showMenu = false },
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "dropdown", variant = "menu_option_secondary"),
                        )
                    }
                }
                SmallVerticalSpacer()
                DatePickerTextField(
                    dateMillis = state.dateMillis,
                    onDateSelected = onDateSelected,
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "input", variant = "date_picker"),
                )
                SmallVerticalSpacer()
                DropdownMenuBox(
                    selectedText = state.selectedDropdownOption,
                    options = state.dropdownOptions,
                    onOptionSelected = onDropdownOptionSelected,
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "input", variant = "dropdown"),
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(SizeConstants.LargeSize),
                ) {
                    Column {
                        SettingsPreferenceItem(
                            title = stringResource(id = R.string.components_preference_title),
                            summary = stringResource(id = R.string.components_preference_summary),
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "preference", variant = "settings_primary"),
                        )
                        ExtraTinyVerticalSpacer()
                        PreferenceItem(
                            title = stringResource(id = R.string.components_preference_secondary_title),
                            summary = stringResource(id = R.string.components_preference_secondary_summary),
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "preference", variant = "secondary"),
                        )
                        ExtraTinyVerticalSpacer()
                        SwitchPreferenceItem(
                            icon = Icons.Outlined.Favorite,
                            title = stringResource(id = R.string.components_switch_title),
                            summary = stringResource(id = R.string.components_switch_summary),
                            checked = state.switchEnabled,
                            onCheckedChange = onSwitchEnabledChanged,
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "preference", variant = "switch"),
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
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "preference", variant = "switch_divider"),
                        )
                        ExtraTinyVerticalSpacer()
                        SwitchCardItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = SizeConstants.MediumSize * 2),
                            title = stringResource(id = R.string.components_switch_card_title),
                            switchState = switchCardState,
                            onSwitchToggled = onSwitchCardChanged,
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "preference", variant = "switch_card"),
                        )
                        ExtraTinyVerticalSpacer()
                        CheckBoxPreferenceItem(
                            icon = Icons.Outlined.Favorite,
                            title = stringResource(id = R.string.components_checkbox_title),
                            summary = stringResource(id = R.string.components_checkbox_summary),
                            checked = state.checkboxChecked,
                            onCheckedChange = onCheckboxChanged,
                            firebaseController = firebaseController,
                            ga4Event = ga4Event(component = "preference", variant = "checkbox"),
                        )
                        ExtraTinyVerticalSpacer()
                        state.radioOptions.forEach { option ->
                            RadioButtonPreferenceItem(
                                text = option,
                                isChecked = state.selectedRadioOption == option,
                                onCheckedChange = { onRadioOptionSelected(option) },
                                firebaseController = firebaseController,
                                ga4Event = ga4Event(component = "preference", variant = "radio_$option"),
                            )
                        }
                    }
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_filters))
            TopListFilters(
                filters = state.filters,
                selectedFilter = state.selectedFilter,
                onFilterSelected = onFilterSelected,
                firebaseController = firebaseController,
                ga4EventProvider = { filter ->
                    ga4Event(component = "filter", variant = filter)
                },
            )
        }

        item {
            NavigationBarSpacer()
        }
    }
}
