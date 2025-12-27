package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.data.datastore.rememberCommonDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class ThemePreferencesState(
    val themeMode: String,
    val dynamicColors: Boolean,
    val amoledMode: Boolean,
    val dynamicPaletteVariant: Int,
    val staticPaletteId: String,
)

fun CommonDataStore.themePreferencesState(
    themeModeDefault: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColorsDefault: Boolean = true, // FIXME: Parameter "dynamicColorsDefault" is never used
    amoledModeDefault: Boolean = false, // FIXME: Parameter "amoledModeDefault" is never used
    dynamicPaletteVariantDefault: Int = 0, // FIXME: Parameter "dynamicPaletteVariantDefault" is never used
    staticPaletteIdDefault: String = StaticPaletteIds.DEFAULT,
): Flow<ThemePreferencesState> = combine(
    themeMode,
    dynamicColors,
    amoledMode,
    dynamicPaletteVariant,
    staticPaletteId,
) { themeModeValue, dynamicColorsValue, amoledModeValue, dynamicPaletteVariantValue, staticPaletteIdValue ->
    ThemePreferencesState(
        themeMode = themeModeValue.ifBlank { themeModeDefault },
        dynamicColors = dynamicColorsValue,
        amoledMode = amoledModeValue,
        dynamicPaletteVariant = dynamicPaletteVariantValue,
        staticPaletteId = staticPaletteIdValue.ifBlank { staticPaletteIdDefault },
    )
}

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
