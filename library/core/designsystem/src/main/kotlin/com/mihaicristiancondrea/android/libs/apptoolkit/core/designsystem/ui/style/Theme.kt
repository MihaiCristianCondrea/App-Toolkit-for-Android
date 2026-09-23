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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.colorscheme.applyDynamicVariant
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.rememberCommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider.paletteById
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.typography.AppTypography

/**
 * The host's own light and dark schemes for the `default` palette.
 *
 * These are configuration, not state: set them before the first composition, normally in
 * `Application.onCreate`. `AppTheme` reads them when one of its theme inputs changes, so a value
 * assigned while a screen is showing is not picked up until the next theme change.
 */
object AppThemeConfig {
    var customLightScheme: ColorScheme? = null
    var customDarkScheme: ColorScheme? = null
}

/**
 * Resolves the color scheme the app is drawn in.
 *
 * The wallpaper-based schemes are only built when they are used: each one reads roughly fifty
 * colors from the system, and this runs whenever an input changes.
 */
private fun resolveColorScheme(
    isDarkTheme: Boolean,
    isAmoledMode: Boolean,
    isDynamicColors: Boolean,
    dynamicPaletteVariant: Int,
    staticPaletteId: String,
    customLightScheme: ColorScheme?,
    customDarkScheme: ColorScheme?,
    context: Context,
): ColorScheme {
    val chosen: ColorScheme = if (isDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val dynamic = if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        dynamic.applyDynamicVariant(dynamicPaletteVariant)
    } else {
        val selectedPalette: ColorPalette = paletteById(staticPaletteId)
        val useCustomOverride: Boolean = staticPaletteId == StaticPaletteIds.DEFAULT
        if (isDarkTheme) {
            (if (useCustomOverride) customDarkScheme else null) ?: selectedPalette.darkColorScheme
        } else {
            (if (useCustomOverride) customLightScheme else null) ?: selectedPalette.lightColorScheme
        }
    }

    return if (isAmoledMode && isDarkTheme) {
        chosen.copy(surface = Color.Black, background = Color.Black)
    } else {
        chosen
    }
}

/**
 * Whether the app draws in its dark theme for [themeMode]: the person's explicit choice, or the
 * system setting when they follow it.
 *
 * Battery saver does not override an explicit choice. On Android 10 and later the system switches
 * its own dark theme on in battery saver, so "follow system" already goes dark there; someone who
 * picked Light gets light.
 */
@Composable
fun isAppInDarkTheme(themeMode: String): Boolean = when (themeMode) {
    DataStoreNamesConstants.THEME_MODE_DARK -> true
    DataStoreNamesConstants.THEME_MODE_LIGHT -> false
    else -> isSystemInDarkTheme()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val context: Context = LocalContext.current
    val themePreferences = rememberThemePreferencesState()
    val dataStore = rememberCommonDataStore()
    val bouncyAnimationsEnabled = dataStore.bouncyButtons
        .collectAsStateWithLifecycle(initialValue = true)
    val showBottomBarLabels = dataStore.getShowBottomBarLabels()
        .collectAsStateWithLifecycle(initialValue = true)
    val adsEnabled = dataStore.adsEnabledFlow
        .collectAsStateWithLifecycle()

    val isDarkTheme: Boolean = isAppInDarkTheme(themeMode = themePreferences.themeMode)
    val customLightScheme: ColorScheme? = AppThemeConfig.customLightScheme
    val customDarkScheme: ColorScheme? = AppThemeConfig.customDarkScheme

    // Rebuilt only when an input changes, not on every recomposition of the root.
    val colorScheme: ColorScheme = remember(
        isDarkTheme,
        themePreferences,
        customLightScheme,
        customDarkScheme,
        context,
    ) {
        resolveColorScheme(
            isDarkTheme = isDarkTheme,
            isAmoledMode = themePreferences.amoledMode,
            isDynamicColors = themePreferences.dynamicColors,
            dynamicPaletteVariant = themePreferences.dynamicPaletteVariant,
            staticPaletteId = themePreferences.staticPaletteId,
            customLightScheme = customLightScheme,
            customDarkScheme = customDarkScheme,
            context = context,
        )
    }

    val view: View = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window: Window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !isDarkTheme
        }
    }

    CompositionLocalProvider(
        LocalBouncyAnimationsEnabled provides bouncyAnimationsEnabled.value,
        LocalShowBottomBarLabels provides showBottomBarLabels.value,
        LocalAdsEnabled provides adsEnabled.value,
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
