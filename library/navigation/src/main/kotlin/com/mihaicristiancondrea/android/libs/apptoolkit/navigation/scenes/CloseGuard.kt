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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Runs one close animation at a time. The list-detail scene closes its detail after the animation
 * ends, so a second request while one plays would close twice and pop the list page as well.
 */
internal class CloseGuard(private val scope: CoroutineScope) {
    private var job: Job? = null

    /** Whether a close started by [launch] has not finished. */
    val isRunning: Boolean get() = job?.isActive == true

    /** Runs [close] in the scope, unless a close is already running, in which case it is ignored. */
    fun launch(close: suspend () -> Unit) {
        if (isRunning) return
        job = scope.launch { close() }
    }

    /** Stops any running close animation so it cannot pop a screen that is already leaving. */
    fun cancel() {
        job?.cancel()
        job = null
    }
}
