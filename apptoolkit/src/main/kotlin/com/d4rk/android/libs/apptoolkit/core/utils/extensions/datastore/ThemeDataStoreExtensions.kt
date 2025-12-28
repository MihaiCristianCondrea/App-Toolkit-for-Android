package com.d4rk.android.libs.apptoolkit.core.utils.extensions.datastore

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.colorscheme.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.data.datastore.rememberCommonDataStore


// TODO && FIXME: Move somewhere else to respect code structure
data class ThemePreferencesState(
    val themeMode: String,
    val dynamicColors: Boolean,
    val amoledMode: Boolean,
    val dynamicPaletteVariant: Int,
    val staticPaletteId: String,
)

/**
 * Collects [themePreferencesState] as state within a composable.
 */
@Composable
fun rememberThemePreferencesState(
    // TODO && FIXME: Move somewhere else to respect code structure
    themeModeDefault: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColorsDefault: Boolean = true,
    amoledModeDefault: Boolean = false,
    dynamicPaletteVariantDefault: Int = 0,
    staticPaletteIdDefault: String = StaticPaletteIds.DEFAULT,
): ThemePreferencesState {
    val dataStore: CommonDataStore = rememberCommonDataStore()
    return dataStore.themePreferencesState(
        themeModeDefault = themeModeDefault,
        dynamicColorsDefault = dynamicColorsDefault,
        amoledModeDefault = amoledModeDefault,
        dynamicPaletteVariantDefault = dynamicPaletteVariantDefault,
        staticPaletteIdDefault = staticPaletteIdDefault,
    ).collectAsStateWithLifecycle(
        initialValue = ThemePreferencesState(
            themeMode = themeModeDefault,
            dynamicColors = dynamicColorsDefault,
            amoledMode = amoledModeDefault,
            dynamicPaletteVariant = dynamicPaletteVariantDefault,
            staticPaletteId = staticPaletteIdDefault,
        )
    ).value
}
