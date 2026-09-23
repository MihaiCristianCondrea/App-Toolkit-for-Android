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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Whether a holiday greeting is on screen anywhere in the process.
 *
 * Every activity gets its own seasonal overlay, and each one asks whether a greeting is due. The
 * answer is stored only once the person closes the greeting, so an activity that starts while the
 * first greeting is still open would otherwise open a second one on top of it.
 */
internal object HolidayGreetingPresence {

    private val shown = AtomicBoolean(false)

    /** Takes the greeting slot, returning false when another activity already holds it. */
    fun claim(): Boolean = shown.compareAndSet(false, true)

    /** Frees the slot once the greeting that held it is gone. */
    fun release() {
        shown.set(false)
    }
}
