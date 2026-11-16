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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import java.util.concurrent.ConcurrentHashMap

/**
 * The screens opened so far while the app runs, for [EntranceReplay.FirstVisit]. It lives as long
 * as the process, so the first copy of each screen after a restart enters again.
 */
internal object OpenedScreens {
    private val screens: MutableSet<TrackedScreen> = ConcurrentHashMap.newKeySet()

    /** Records [screen] as opened; true only the first time it is recorded in this run. */
    fun claim(screen: TrackedScreen): Boolean = screens.add(screen)

    /** Forgets every screen, as a new run would. */
    fun forget() = screens.clear()
}
