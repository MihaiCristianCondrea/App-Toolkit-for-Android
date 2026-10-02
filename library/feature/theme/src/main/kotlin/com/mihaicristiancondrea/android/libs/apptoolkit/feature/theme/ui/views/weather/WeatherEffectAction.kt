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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.views.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.ThemeSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.contracts.ThemeSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.states.ThemeSettingsUiState
import org.koin.compose.viewmodel.koinViewModel

/**
 * The theme page's app bar action, once the About screen's easter egg is found: a button showing
 * the weather effect in use, which opens a dialog to choose another.
 *
 * It shares the page's [ThemeSettingsViewModel], so it appears once the page has loaded and
 * disappears for anyone who has not found the easter egg. A failed save shows through the page's
 * messages.
 */
@Composable
internal fun WeatherEffectAction() {
    val viewModel: ThemeSettingsViewModel = koinViewModel()
    val state: ThemeSettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    if (!state.seasonalThemesUnlocked) return
    var open by rememberSaveable { mutableStateOf(false) }

    AnimatedIconButtonDirection(
        fromRight = true,
        icon = ToolkitIcon.Vector(state.weatherEffect.icon),
        contentDescription = stringResource(R.string.weather_effect),
        onClick = { open = true },
    )

    if (open) {
        WeatherEffectDialog(
            current = state.weatherEffect,
            onDismiss = { open = false },
            onSelected = { viewModel.onEvent(ThemeSettingsEvent.SetWeatherEffect(it)) },
        )
    }
}

/**
 * Chooses what falls over the app, the way the display settings choose the startup page: radio
 * rows, applied with Done.
 */
@Composable
internal fun WeatherEffectDialog(
    current: WeatherEffect,
    onDismiss: () -> Unit,
    onSelected: (WeatherEffect) -> Unit,
) {
    var selected by rememberSaveable(current) { mutableStateOf(current) }
    val options = WeatherEffect.entries

    BasicAlertDialog(
        onDismiss = onDismiss,
        onConfirm = {
            onSelected(selected)
            onDismiss()
        },
        icon = selected.icon,
        showDismissButton = false,
        confirmButtonText = stringResource(id = CoreUiR.string.done_button_content_description),
        title = stringResource(id = R.string.weather_effect),
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                options.forEachIndexed { index, effect ->
                    RadioButtonPreferenceItem(
                        modifier = Modifier.groupedPreferenceItem(
                            position = groupedItemPosition(index = index, size = options.size),
                            outerRadius = SizeConstants.LargeMediumSize,
                            horizontalPadding = SizeConstants.ZeroSize,
                        ),
                        text = stringResource(id = effect.label),
                        isChecked = selected == effect,
                        onCheckedChange = { selected = effect },
                    )
                }
                MediumVerticalSpacer()
                InfoMessageSection(message = stringResource(id = R.string.weather_effect_info))
            }
        },
    )
}

private val WeatherEffect.icon: ImageVector
    get() = when (this) {
        WeatherEffect.Automatic -> Icons.Outlined.AutoAwesome
        WeatherEffect.Snow -> Icons.Outlined.AcUnit
        WeatherEffect.Rain -> Icons.Outlined.WaterDrop
        WeatherEffect.Off -> Icons.Outlined.CloudOff
    }

private val WeatherEffect.label: Int
    get() = when (this) {
        WeatherEffect.Automatic -> R.string.weather_effect_automatic
        WeatherEffect.Snow -> R.string.weather_effect_snow
        WeatherEffect.Rain -> R.string.weather_effect_rain
        WeatherEffect.Off -> R.string.weather_effect_off
    }
