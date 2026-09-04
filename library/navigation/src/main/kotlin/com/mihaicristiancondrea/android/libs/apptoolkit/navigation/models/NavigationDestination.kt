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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models

import androidx.navigation3.runtime.NavKey

/** Determines how shared back-stack and shell logic treats a destination. */
enum class NavigationDestinationType {
    /** A root shell destination selected from bottom navigation, rail, or drawer UI. */
    TopLevel,

    /** A destination presented with activity-like transition and navigation behavior. */
    ActivityLike,

    /** A child destination pushed above its owning top-level destination. */
    Nested,
}

/** Navigation 3 key contract carrying the behavior category used by shared navigation helpers. */
interface NavigationDestination : NavKey {
    /** Category consumed by back-stack operations and transition selection. */
    val destinationType: NavigationDestinationType
}

/** Whether this key represents a root shell destination. */
val NavigationDestination.isTopLevel: Boolean
    get() = destinationType == NavigationDestinationType.TopLevel

/** Whether this key uses the activity-like presentation category. */
val NavigationDestination.isActivityLike: Boolean
    get() = destinationType == NavigationDestinationType.ActivityLike

/** Whether this key is pushed as a child of another destination. */
val NavigationDestination.isNested: Boolean
    get() = destinationType == NavigationDestinationType.Nested
