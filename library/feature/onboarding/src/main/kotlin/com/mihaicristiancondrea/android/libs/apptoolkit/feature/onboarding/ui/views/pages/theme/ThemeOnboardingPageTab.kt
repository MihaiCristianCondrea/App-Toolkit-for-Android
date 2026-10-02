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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.views.pages.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemeModePicker
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.ThemePalettePicker
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.isAmoledAllowed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.OnboardingThemeViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingThemeEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingThemeUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.views.pages.theme.cards.AmoledModeToggleCard
import org.koin.compose.viewmodel.koinViewModel

/**
 * The theme page of onboarding: the theme mode, AMOLED, and a wallpaper or static palette. An app
 * adds it as an `OnboardingPage.CustomPage` from its `OnboardingProvider`. A failed write shows
 * through the onboarding screen's snackbar host.
 */
@Composable
fun ThemeOnboardingPageTab() {
    val viewModel: OnboardingThemeViewModel = koinViewModel()
    val state: OnboardingThemeUiState by viewModel.state.collectAsStateWithLifecycle()

    ThemeOnboardingPageTabContent(
        state = state,
        onEvent = viewModel::onEvent,
    )

    MessageHost(viewModel = viewModel)
}

/**
 * Renders the theme choices for [state] with the same [ThemeModePicker] and [ThemePalettePicker]
 * the theme settings page uses, under the page's own title, and with the AMOLED switch as a card.
 *
 * @param onEvent Receives the events [OnboardingThemeViewModel] handles.
 */
@Composable
internal fun ThemeOnboardingPageTabContent(
    state: OnboardingThemeUiState,
    onEvent: (OnboardingThemeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val themeMode: String = state.preferences.themeMode
    val amoledAllowed: Boolean = isAmoledAllowed(themeMode)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = SizeConstants.LargeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        Text(
            text = stringResource(R.string.onboarding_theme_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 30.sp, textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.onboarding_theme_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
        )

        ThemeModePicker(
            selectedMode = themeMode,
            onSelected = { mode -> onEvent(OnboardingThemeEvent.SelectThemeMode(mode)) },
        )

        AmoledModeToggleCard(
            isAmoledMode = state.preferences.amoledMode,
            enabled = amoledAllowed,
            onCheckedChange = { isChecked ->
                if (amoledAllowed) onEvent(OnboardingThemeEvent.SetAmoledMode(isChecked))
            }
        )

        ThemePalettePicker(
            preferences = state.preferences,
            seasonalThemesUnlocked = state.seasonalThemesUnlocked,
            onDynamicPaletteSelected = { variant -> onEvent(OnboardingThemeEvent.SelectDynamicPalette(variant)) },
            onStaticPaletteSelected = { id, _ -> onEvent(OnboardingThemeEvent.SelectStaticPalette(id)) },
            horizontalPadding = SizeConstants.ZeroSize,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeOnboardingPageTabContentPreview() {
    MaterialTheme {
        ThemeOnboardingPageTabContent(
            state = OnboardingThemeUiState(),
            onEvent = {},
        )
    }
}
