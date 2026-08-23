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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections.ButtonShowcase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections.FabShowcase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections.FilterShowcase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections.InputShowcase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections.LayoutShowcase
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections.PreferenceShowcase
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.NavigationBarSpacer
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.koinInject
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as ToolkitR

/**
 * Composable that manages state and renders the components showcase screen.
 */
@Composable
fun ComponentsScreen(
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
        val filterFavorites = stringResource(id = R.string.components_filter_favorites)
        val filters = remember(filterPopular, filterRecent, filterFavorites) {
            persistentListOf(filterPopular, filterRecent, filterFavorites)
        }

        val radioSystem = stringResource(id = ToolkitR.string.follow_system)
        val radioLight = stringResource(id = ToolkitR.string.light_mode)
        val radioDark = stringResource(id = ToolkitR.string.dark_mode)
        val radioOptions = remember(radioSystem, radioLight, radioDark) {
            persistentListOf(radioSystem, radioLight, radioDark)
        }

        var selectedDropdownOption by rememberSaveable { mutableStateOf(value = dropdownOptions.first()) }
        var selectedFilter by rememberSaveable { mutableStateOf(value = filters.first()) }
        var selectedRadioOption by rememberSaveable { mutableStateOf(value = radioOptions.first()) }
        var selectedDateMillis by rememberSaveable { mutableLongStateOf(value = System.currentTimeMillis()) }
        var switchEnabled by rememberSaveable { mutableStateOf(value = true) }
        var switchWithDividerEnabled by rememberSaveable { mutableStateOf(value = false) }
        var switchCardEnabled by rememberSaveable { mutableStateOf(value = false) }
        var checkboxChecked by rememberSaveable { mutableStateOf(value = false) }

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

        val switchCardState: State<Boolean> = rememberUpdatedState(switchCardEnabled)
        val carouselState = rememberPagerState { dropdownOptions.size }
        val screenParam = AnalyticsValue.Str("components")

        fun ga4Event(component: String, variant: String? = null): Ga4EventData {
            val params = buildMap {
                put("screen", screenParam)
                put("component", AnalyticsValue.Str(component))
                variant?.let {
                    put("variant", AnalyticsValue.Str(it))
                }
            }
            return Ga4EventData(name = "components_click", params = params)
        }

        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
        ) {
            item {
                ButtonShowcase(
                    firebaseController = firebaseController,
                    onLogEvent = ::ga4Event,
                )
            }

            item {
                FabShowcase(
                    firebaseController = firebaseController,
                    onLogEvent = ::ga4Event,
                )
            }

            item {
                InputShowcase(
                    firebaseController = firebaseController,
                    onLogEvent = ::ga4Event,
                    dateMillis = selectedDateMillis,
                    onDateSelected = { selectedDateMillis = it },
                    selectedDropdownOption = selectedDropdownOption,
                    dropdownOptions = dropdownOptions,
                    onDropdownOptionSelected = { selectedDropdownOption = it },
                )
            }

            item {
                PreferenceShowcase(
                    firebaseController = firebaseController,
                    onLogEvent = ::ga4Event,
                    switchEnabled = switchEnabled,
                    onSwitchEnabledChanged = { switchEnabled = it },
                    switchWithDividerEnabled = switchWithDividerEnabled,
                    onSwitchWithDividerChanged = { switchWithDividerEnabled = it },
                    switchCardState = switchCardState,
                    onSwitchCardChanged = { switchCardEnabled = it },
                    checkboxChecked = checkboxChecked,
                    onCheckboxChanged = { checkboxChecked = it },
                    radioOptions = radioOptions,
                    selectedRadioOption = selectedRadioOption,
                    onRadioOptionSelected = { selectedRadioOption = it },
                )
            }

            item {
                LayoutShowcase(
                    dropdownOptions = dropdownOptions,
                    carouselState = carouselState,
                )
            }

            item {
                FilterShowcase(
                    firebaseController = firebaseController,
                    onLogEvent = ::ga4Event,
                    filters = filters,
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it },
                )
            }

            item {
                NavigationBarSpacer()
            }
        }
    }

    if (isEmbedded) {
        content(paddingValues)
    } else {
        LargeTopAppBarWithScaffold(
            title = stringResource(id = R.string.components_title),
            onBackClicked = { activity?.finish() },
            content = content,
        )
    }
}
