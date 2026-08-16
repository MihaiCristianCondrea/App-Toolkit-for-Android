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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.states.ComponentsUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralTextButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralTonalButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.SmallFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel.CustomCarousel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dropdown.CommonDropdownMenuItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.DatePickerTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.DropdownMenuBox
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.TopListFilters
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedActionList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.CheckBoxPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.koinInject
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as ToolkitR

/**
 * Route-level composable that manages state for the components showcase screen.
 */
@Composable
fun ComponentsRoute(
    paddingValues: PaddingValues = PaddingValues(),
    isEmbedded: Boolean = true,
) {
    val firebaseController: FirebaseController = koinInject()
    val activity = LocalActivity.current

    val content: @Composable (PaddingValues) -> Unit = { innerPadding ->
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
            paddingValues = innerPadding,
            firebaseController = firebaseController,
            state = state,
            onDropdownOptionSelected = { selectedDropdownOption = it },
            onDateSelected = { selectedDateMillis = it },
            onFilterSelected = { selectedFilter = it },
            onSwitchEnabledChanged = { switchEnabled = it },
            onSwitchWithDividerChanged = {
                switchWithDividerEnabled = it
            },
            onSwitchCardChanged = { switchCardEnabled = it },
            onCheckboxChanged = { checkboxChecked = it },
            onRadioOptionSelected = { selectedRadioOption = it },
        )
    }

    if (isEmbedded) {
        content(paddingValues)
    } else {
        LargeTopAppBarWithScaffold(
            title = stringResource(id = R.string.components_title),
            onBackClicked = { activity?.finish() },
            content = content
        )
    }
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
    val carouselState = rememberPagerState(pageCount = { state.dropdownOptions.size })
    val screenParam = AnalyticsValue.Str("components")
    val preferenceItemCount = 6 + state.radioOptions.size

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
            ShowcaseSection {
                ShowcaseSurface(position = GroupedItemPosition.FIRST) {
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
                }
                ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
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
                }
                ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
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
                }
                ShowcaseSurface(position = GroupedItemPosition.LAST) {
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
                    SmallVerticalSpacer()
                    AnimatedIconButtonDirection(
                        icon = Icons.Filled.MoreVert,
                        contentDescription = iconContentDescription,
                        onClick = {},
                        fromRight = true,
                        firebaseController = firebaseController,
                        ga4Event = ga4Event(component = "button", variant = "animated_direction"),
                    )
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_fabs))
            ShowcaseSection {
                ShowcaseSurface(position = GroupedItemPosition.SINGLE) {
                    Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
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
                            onLogClick = {
                                firebaseController.logGa4Event(
                                    ga4Event(component = "fab", variant = "small")
                                )
                            },
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
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_inputs))
            ShowcaseSection {
                ShowcaseSurface(position = GroupedItemPosition.FIRST) {
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
                                ga4Event = ga4Event(
                                    component = "dropdown",
                                    variant = "menu_option_primary"
                                ),
                            )
                            CommonDropdownMenuItem(
                                textResId = R.string.components_menu_option_secondary,
                                icon = Icons.Outlined.Favorite,
                                onClick = { showMenu = false },
                                firebaseController = firebaseController,
                                ga4Event = ga4Event(
                                    component = "dropdown",
                                    variant = "menu_option_secondary"
                                ),
                            )
                        }
                    }
                }
                ShowcaseSurface(position = GroupedItemPosition.LAST) {
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
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_preferences))
            ShowcaseSection {
                SettingsPreferenceItem(
                    modifier = Modifier.groupedCorners(
                        groupedItemPosition(index = 0, size = preferenceItemCount)
                    ),
                    title = stringResource(id = R.string.components_preference_title),
                    summary = stringResource(id = R.string.components_preference_summary),
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(
                        component = "preference",
                        variant = "settings_primary"
                    ),
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .groupedCorners(groupedItemPosition(index = 1, size = preferenceItemCount)),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    PreferenceItem(
                        title = stringResource(id = R.string.components_preference_secondary_title),
                        summary = stringResource(id = R.string.components_preference_secondary_summary),
                        firebaseController = firebaseController,
                        ga4Event = ga4Event(component = "preference", variant = "secondary"),
                    )
                }
                SwitchPreferenceItem(
                    modifier = Modifier.groupedCorners(
                        groupedItemPosition(index = 2, size = preferenceItemCount)
                    ),
                    icon = Icons.Outlined.Favorite,
                    title = stringResource(id = R.string.components_switch_title),
                    summary = stringResource(id = R.string.components_switch_summary),
                    checked = state.switchEnabled,
                    onCheckedChange = onSwitchEnabledChanged,
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "preference", variant = "switch"),
                )
                SwitchPreferenceItemWithDivider(
                    modifier = Modifier.groupedCorners(
                        groupedItemPosition(index = 3, size = preferenceItemCount)
                    ),
                    icon = Icons.Outlined.Info,
                    title = stringResource(id = R.string.components_switch_divider_title),
                    summary = stringResource(id = R.string.components_switch_divider_summary),
                    checked = state.switchWithDividerEnabled,
                    onCheckedChange = onSwitchWithDividerChanged,
                    onClick = {},
                    onSwitchClick = {},
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(
                        component = "preference",
                        variant = "switch_divider"
                    ),
                )
                SwitchCardItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .groupedCorners(
                            groupedItemPosition(index = 4, size = preferenceItemCount)
                        ),
                    title = stringResource(id = R.string.components_switch_card_title),
                    switchState = switchCardState,
                    onSwitchToggled = onSwitchCardChanged,
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "preference", variant = "switch_card"),
                )
                CheckBoxPreferenceItem(
                    modifier = Modifier.groupedCorners(
                        groupedItemPosition(index = 5, size = preferenceItemCount)
                    ),
                    icon = Icons.Outlined.Favorite,
                    title = stringResource(id = R.string.components_checkbox_title),
                    summary = stringResource(id = R.string.components_checkbox_summary),
                    checked = state.checkboxChecked,
                    onCheckedChange = onCheckboxChanged,
                    firebaseController = firebaseController,
                    ga4Event = ga4Event(component = "preference", variant = "checkbox"),
                )
                state.radioOptions.forEachIndexed { index, option ->
                    RadioButtonPreferenceItem(
                        modifier = Modifier.groupedCorners(
                            groupedItemPosition(
                                index = index + 6,
                                size = preferenceItemCount,
                            )
                        ),
                        text = option,
                        isChecked = state.selectedRadioOption == option,
                        onCheckedChange = { onRadioOptionSelected(option) },
                        firebaseController = firebaseController,
                        ga4Event = ga4Event(
                            component = "preference",
                            variant = "radio_$option"
                        ),
                    )
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_title))
            val primaryTitle = stringResource(id = R.string.components_preference_title)
            val primarySummary = stringResource(id = R.string.components_preference_summary)
            val secondaryTitle = stringResource(id = R.string.components_preference_secondary_title)
            val secondarySummary =
                stringResource(id = R.string.components_preference_secondary_summary)
            val groupedActions = remember(
                primaryTitle,
                primarySummary,
                secondaryTitle,
                secondarySummary,
            ) {
                persistentListOf(
                    GroupedAction(
                        title = primaryTitle,
                        description = primarySummary,
                        icon = Icons.Outlined.StarOutline,
                        onClick = {},
                    ),
                    GroupedAction(
                        title = secondaryTitle,
                        description = secondarySummary,
                        icon = Icons.Outlined.Info,
                        onClick = {},
                    ),
                )
            }
            ShowcaseSection {
                GroupedActionList(
                    actions = groupedActions,
                    modifier = Modifier.groupedCorners(GroupedItemPosition.FIRST),
                )
                ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
                    CustomCarousel(
                        items = state.dropdownOptions,
                        sidePadding = SizeConstants.SmallSize,
                        pagerState = carouselState,
                    ) { option ->
                        Text(
                            text = option,
                            modifier = Modifier.padding(SizeConstants.ExtraLargeSize),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
                ShowcaseSurface(position = GroupedItemPosition.LAST) {
                    InfoMessageSection(
                        message = secondarySummary,
                        learnMoreText = stringResource(id = R.string.components_button_text),
                        learnMoreAction = {},
                        newLine = false,
                    )
                    SmallVerticalSpacer()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.components_switch_title),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        CustomSwitch(
                            checked = state.switchEnabled,
                            onCheckedChange = onSwitchEnabledChanged,
                        )
                    }
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.components_section_filters))
            ShowcaseSection {
                ShowcaseSurface(position = GroupedItemPosition.SINGLE) {
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
            }
        }

        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
private fun ShowcaseSection(
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = SizeConstants.TwoHundredFiftySixSize * 3)
                .fillMaxWidth()
                .padding(horizontal = SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
            content = content,
        )
    }
}

@Composable
private fun ShowcaseSurface(
    position: GroupedItemPosition,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .groupedCorners(position = position),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.padding(SizeConstants.LargeSize),
            content = content,
        )
    }
}
