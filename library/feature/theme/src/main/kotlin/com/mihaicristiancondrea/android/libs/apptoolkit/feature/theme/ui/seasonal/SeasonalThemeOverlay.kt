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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.rainfall.RainfallStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.rainfall.rainfall
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.snowfall.SnowfallStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.snowfall.snowfall
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.isAppInDarkTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemeOverlayEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.views.HolidayGreetingDialog
import org.koin.compose.viewmodel.koinViewModel

/**
 * The seasonal layer drawn over one activity: the weather effect (snow while the Christmas palette
 * is worn, or rain when picked in the theme settings), and the holiday greeting when one is due.
 *
 * [SeasonalThemeManager] puts this over every activity. A host that would rather place it itself,
 * inside its own root composable, can call it directly and skip the manager. It fills its parent and
 * takes no input, so it belongs last in a `Box` above the content.
 *
 * Snow and rain are skipped when the person has turned animations off system-wide.
 *
 * It composes no app theme of its own until there is a greeting to show. It sits over every
 * activity, and a second theme there meant a second set of preference collectors and a second
 * color scheme per screen, most of the year for nothing. The weather only needs to know light from
 * dark.
 */
@Composable
fun SeasonalThemeOverlay(modifier: Modifier = Modifier) {
    val viewModel: SeasonalThemeOverlayViewModel = koinViewModel()
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = screenState.data ?: return

    val context = LocalContext.current
    val animationsDisabled: Boolean = remember(context) { context.isSystemAnimationDisabled() }
    val isDarkSurface: Boolean = isAppInDarkTheme(themeMode = state.themeMode)
    val snowStyle: SnowfallStyle = remember(isDarkSurface) {
        SnowfallStyle(colors = if (isDarkSurface) darkSurfaceSnow else lightSurfaceSnow)
    }
    val rainStyle: RainfallStyle = remember(isDarkSurface) {
        RainfallStyle(colors = if (isDarkSurface) darkSurfaceRain else lightSurfaceRain)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .snowfall(style = snowStyle, enabled = state.showSnowfall && !animationsDisabled)
            .rainfall(style = rainStyle, enabled = state.showRain && !animationsDisabled),
    )

    state.greeting?.let { season ->
        AppTheme {
            HolidayGreetingDialog(
                season = season,
                onAnswer = { useHolidayTheme ->
                    viewModel.onEvent(SeasonalThemeOverlayEvent.AnswerGreeting(useHolidayTheme))
                },
            )
        }
    }
}

private val darkSurfaceSnow: List<Color> = listOf(Color.White, Color(0xFFDCE8F5))

/** White snow vanishes on a light surface, so light themes get a cold blue-grey instead. */
private val lightSurfaceSnow: List<Color> = listOf(Color(0xFF8FAACB), Color(0xFFA9C0DB))

private val darkSurfaceRain: List<Color> = listOf(Color(0xFFB3C7DD), Color(0xFFD6E2EF))

/** Pale streaks disappear on a light surface, so light themes get a deeper slate blue. */
private val lightSurfaceRain: List<Color> = listOf(Color(0xFF5E7A99), Color(0xFF7890AD))
