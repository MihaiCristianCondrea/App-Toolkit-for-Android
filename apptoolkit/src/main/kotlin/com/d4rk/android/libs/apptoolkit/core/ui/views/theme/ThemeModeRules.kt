package com.d4rk.android.libs.apptoolkit.core.ui.views.theme

import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants

/**
 * Returns whether the AMOLED toggle should be enabled for the selected theme mode.
 *
 * AMOLED is only applicable to dark or follow-system themes. When the user selects the light
 * theme, the AMOLED toggle is disabled to prevent unsupported combinations.
 */
internal fun isAmoledAllowed(themeModeKey: String): Boolean {
    return themeModeKey != DataStoreNamesConstants.THEME_MODE_LIGHT
}
