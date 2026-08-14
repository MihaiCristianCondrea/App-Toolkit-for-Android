/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.models.theme.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.colorscheme.StaticPaletteIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.extensions.themePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore

/** Collects the application-facing theme preference model at the design-system boundary. */
@Composable
internal fun rememberThemePreferencesState(
    themeModeDefault: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
    dynamicColorsDefault: Boolean = true,
    amoledModeDefault: Boolean = false,
    dynamicPaletteVariantDefault: Int = 0,
    staticPaletteIdDefault: String = StaticPaletteIds.DEFAULT,
): ThemePreferencesState {
    val dataStore = rememberCommonDataStore()
    return dataStore.themePreferences.themePreferencesState(
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
        ),
    ).value
}
