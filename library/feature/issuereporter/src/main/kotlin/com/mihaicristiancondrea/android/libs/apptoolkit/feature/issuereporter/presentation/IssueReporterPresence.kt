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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation

/**
 * Whether a report sheet is on screen anywhere in the process.
 *
 * The reporter has two entry points that cannot see each other: a host composing
 * `IssueReporterBottomSheet` in its own screen, and the shake gesture, which mounts one from
 * outside any composition. Without a shared answer, shaking the device on a screen that already
 * shows the reporter would stack a second sheet on the first.
 *
 * One sheet at a time is the whole invariant, so a counter is enough. It is touched only from the
 * main thread, by composition enter and exit, which is also why it cannot be left set: a sheet
 * leaves composition when it is dismissed and when the activity holding it is torn down.
 */
object IssueReporterPresence {

    private var shown: Int = 0

    /** True while at least one report sheet is composed. */
    val isShowing: Boolean
        get() = shown > 0

    internal fun onShown() {
        shown++
    }

    internal fun onHidden() {
        if (shown > 0) shown--
    }
}
