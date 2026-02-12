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

package com.d4rk.android.libs.apptoolkit.core.utils.interfaces

/**
 * Interface definition for a callback to be invoked when an ad display is completed.
 *
 * This interface provides a single method, [onShowAdComplete], which is called after an ad has finished displaying,
 * regardless of whether the user interacted with it or not. This can be used to perform actions after the ad has been displayed,
 * such as resuming gameplay, updating UI elements, or other necessary cleanup.
 */
interface OnShowAdCompleteListener {
    fun onShowAdComplete()
}