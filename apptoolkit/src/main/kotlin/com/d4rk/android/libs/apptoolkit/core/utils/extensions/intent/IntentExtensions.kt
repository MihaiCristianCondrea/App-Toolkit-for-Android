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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.intent

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Ensures FLAG_ACTIVITY_NEW_TASK is set when launching from a non-Activity [Context].
 *
 * Mutates and returns the same [Intent] instance for convenience.
 */
fun Intent.requireNewTask(context: Context): Intent = apply {
    if (context !is Activity && (flags and Intent.FLAG_ACTIVITY_NEW_TASK) == 0) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
