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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

object NavigationDrawerRoutes {
    const val ROUTE_SETTINGS: String = "settings"
    const val ROUTE_HELP_AND_FEEDBACK: String = "help_and_feedback"
    const val ROUTE_SUPPORT: String = "support"
    const val ROUTE_UPDATES: String = "updates"
    const val ROUTE_SHARE: String = "share"

    /**
     * The entries every toolkit host has, as opposed to the destinations an app adds of its own.
     *
     * A drawer that holds nothing else is just this list, so it renders as one block. Once an app
     * adds a destination outside this set, these become the drawer's footer: see
     * `NavigationDrawerSheet`.
     */
    val StandardRoutes: ImmutableSet<String> = persistentSetOf(
        ROUTE_SETTINGS,
        ROUTE_HELP_AND_FEEDBACK,
        ROUTE_SUPPORT,
        ROUTE_UPDATES,
        ROUTE_SHARE,
    )
}
