package com.d4rk.android.libs.apptoolkit.core.ui.model.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey

/**
 * Marker interface to signal Compose stability for navigation keys.
 *
 * Located in the core domain layer so that both the library and consuming
 * applications can share the same stable navigation contract.
 */
@Immutable
@Stable
interface StableNavKey : NavKey
