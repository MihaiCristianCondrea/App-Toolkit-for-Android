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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.blue.bluePalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.green.greenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.monochrome.monochromePalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.red.redPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.yellow.yellowPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.typography.AppTypography
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.applyDynamicVariant
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore

private val defaultLightScheme: ColorScheme = lightColorScheme() // not used anymore pls fix
private val defaultDarkScheme: ColorScheme = darkColorScheme() // not used anymore pls fix

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
    val isBatterySaverOn = powerManager.isPowerSaveMode
    val shouldUseDarkTheme = isDarkTheme || isBatterySaverOn

    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val useDynamic = isDynamicColors && supportsDynamic

    // Selected static palette (module)
    val selectedPalette: ColorPalette = paletteById(staticPaletteId)

    // Keep your override mechanism intact
    val baseLightScheme = AppThemeConfig.customLightScheme ?: selectedPalette.lightColorScheme
    val baseDarkScheme = AppThemeConfig.customDarkScheme ?: selectedPalette.darkColorScheme

    val dynamicDark: ColorScheme =
        if (supportsDynamic) dynamicDarkColorScheme(context) else baseDarkScheme
    val dynamicLight: ColorScheme =
        if (supportsDynamic) dynamicLightColorScheme(context) else baseLightScheme

    val dynamicDarkVariant = dynamicDark.applyDynamicVariant(dynamicPaletteVariant)
    val dynamicLightVariant = dynamicLight.applyDynamicVariant(dynamicPaletteVariant)

    val chosen: ColorScheme = when {
        useDynamic -> if (shouldUseDarkTheme) dynamicDarkVariant else dynamicLightVariant
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

// TODO: move somewhere else
fun paletteById(id: String): ColorPalette = when (id) {
    StaticPaletteIds.MONOCHROME -> monochromePalette
    StaticPaletteIds.BLUE -> bluePalette
    StaticPaletteIds.GREEN -> greenPalette
    StaticPaletteIds.RED -> redPalette
    StaticPaletteIds.YELLOW -> yellowPalette
    else -> bluePalette
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)

    val themeMode: String =
        dataStore.themeMode.collectAsStateWithLifecycle(
            initialValue = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
        ).value

    val isDynamicColors: Boolean =
        dataStore.dynamicColors.collectAsStateWithLifecycle(initialValue = true).value

    val isAmoledMode: Boolean =
        dataStore.amoledMode.collectAsStateWithLifecycle(initialValue = false).value

    val dynamicPaletteVariant: Int =
        dataStore.dynamicPaletteVariant.collectAsStateWithLifecycle(initialValue = 0).value

    val staticPaletteId: String =
        dataStore.staticPaletteId.collectAsStateWithLifecycle(initialValue = StaticPaletteIds.DEFAULT).value

    val isSystemDarkTheme: Boolean = isSystemInDarkTheme()
    val isDarkTheme: Boolean = when (themeMode) {
        DataStoreNamesConstants.THEME_MODE_DARK -> true
        DataStoreNamesConstants.THEME_MODE_LIGHT -> false
        else -> isSystemDarkTheme
    }

    val colorScheme: ColorScheme = getColorScheme(
        isDarkTheme = isDarkTheme,
        isAmoledMode = isAmoledMode,
        isDynamicColors = isDynamicColors,
        dynamicPaletteVariant = dynamicPaletteVariant,
        staticPaletteId = staticPaletteId,
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