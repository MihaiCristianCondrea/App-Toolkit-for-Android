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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories

import kotlinx.coroutines.flow.Flow

/**
 * The way to read and change display preferences: language, startup destination, and the small
 * interaction settings that go with them.
 */
interface DisplayPreferencesRepository {

    /** Emits whether bottom-bar labels are shown. */
    val showBottomBarLabels: Flow<Boolean>

    /** Emits whether the bounce-click button animation is enabled. */
    val bouncyButtons: Flow<Boolean>

    /** Emits the stored language tag. */
    val language: Flow<String>

    /**
     * Emits the route the app opens on.
     *
     * @param default what to emit while no route has been chosen.
     */
    fun startupPage(default: String): Flow<String>

    /** Shows or hides bottom-bar labels. */
    suspend fun setShowBottomBarLabels(show: Boolean)

    /** Turns the bounce-click animation on or off. */
    suspend fun setBouncyButtons(enabled: Boolean)

    /** Stores the language tag the app should use. */
    suspend fun setLanguage(language: String)

    /** Stores the route the app opens on. */
    suspend fun setStartupPage(route: String)
}
