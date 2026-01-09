package com.d4rk.android.libs.apptoolkit.core.utils.extensions.datastore

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.domain.model.theme.ThemePreferencesState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.data.local.datastore.rememberCommonDataStore

/**
 * Collects [themePreferencesState] as state within a composable.
 */
@Composable
fun rememberThemePreferencesState(
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
