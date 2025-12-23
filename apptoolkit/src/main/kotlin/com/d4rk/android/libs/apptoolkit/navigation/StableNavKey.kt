package com.d4rk.android.libs.apptoolkit.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey

/**
 * Marker interface to signal Compose stability for navigation keys.
 */
@Immutable
interface StableNavKey : NavKey // TODO: Move the file somewhere else to be stable in the library in the core/ui
