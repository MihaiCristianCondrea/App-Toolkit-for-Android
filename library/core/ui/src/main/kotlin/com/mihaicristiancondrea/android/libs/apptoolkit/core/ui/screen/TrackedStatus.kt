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

/**
 * A status that [ScreenHost] reports in the `screen_state` analytics event.
 *
 * [Loadable] implements it. A screen with statuses of its own implements it too, to have them
 * tracked like the Toolkit's.
 */
interface TrackedStatus {
    /** The `state` parameter of the `screen_state` event. */
    val trackingLabel: String
}
