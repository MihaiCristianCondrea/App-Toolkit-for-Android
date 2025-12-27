package com.d4rk.android.libs.apptoolkit.app.theme.style

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.view.View
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.d4rk.android.libs.apptoolkit.app.theme.style.ThemePaletteProvider.paletteById
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.typography.AppTypography
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.applyDynamicVariant
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.rememberThemePreferencesState

object AppThemeConfig {
    var customLightScheme: ColorScheme? = null
    var customDarkScheme: ColorScheme? = null
}

private fun getColorScheme(
    isDarkTheme: Boolean,
    isAmoledMode: Boolean,
    isDynamicColors: Boolean,
    dynamicPaletteVariant: Int,
    staticPaletteId: String,
    context: Context
): ColorScheme {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val shouldUseDarkTheme = isDarkTheme || powerManager.isPowerSaveMode

    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val selectedPalette: ColorPalette = paletteById(staticPaletteId)

    val useCustomOverride: Boolean = staticPaletteId == StaticPaletteIds.DEFAULT

    val baseLightScheme: ColorScheme =
        if (useCustomOverride) AppThemeConfig.customLightScheme ?: selectedPalette.lightColorScheme
        else selectedPalette.lightColorScheme

    val baseDarkScheme: ColorScheme =
        if (useCustomOverride) AppThemeConfig.customDarkScheme ?: selectedPalette.darkColorScheme
        else selectedPalette.darkColorScheme

    val dynamicDark: ColorScheme =
        if (supportsDynamic) dynamicDarkColorScheme(context) else baseDarkScheme
    val dynamicLight: ColorScheme =
        if (supportsDynamic) dynamicLightColorScheme(context) else baseLightScheme

    val dynamicDarkVariant = dynamicDark.applyDynamicVariant(dynamicPaletteVariant)
    val dynamicLightVariant = dynamicLight.applyDynamicVariant(dynamicPaletteVariant)

    val chosen: ColorScheme = when {
        isDynamicColors && supportsDynamic -> if (shouldUseDarkTheme) dynamicDarkVariant else dynamicLightVariant
        else -> if (shouldUseDarkTheme) baseDarkScheme else baseLightScheme
    }

    return when {
        isAmoledMode && shouldUseDarkTheme -> chosen.copy(
            surface = Color.Black,
            background = Color.Black,
        )

        else -> chosen
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val context: Context = LocalContext.current
    val themePreferences = rememberThemePreferencesState()

    val isSystemDarkTheme: Boolean = isSystemInDarkTheme()
    val isDarkTheme: Boolean = when (themePreferences.themeMode) {
        DataStoreNamesConstants.THEME_MODE_DARK -> true
        DataStoreNamesConstants.THEME_MODE_LIGHT -> false
        else -> isSystemDarkTheme
    }

    val colorScheme: ColorScheme = getColorScheme(
        isDarkTheme = isDarkTheme,
        isAmoledMode = themePreferences.amoledMode,
        isDynamicColors = themePreferences.dynamicColors,
        dynamicPaletteVariant = themePreferences.dynamicPaletteVariant,
        staticPaletteId = themePreferences.staticPaletteId,
        context = context
    )

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

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
