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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

/**
 * Traverses the context chain and returns the first [ComponentActivity] if present.
 */
fun Context.findActivity(): ComponentActivity? {
    var context: Context = this
    while (true) {
        when (context) {
            is ComponentActivity -> return context
            is ContextWrapper -> {
                val base = context.baseContext ?: return null
                if (base === context) return null
                context = base
            }

            else -> return null
        }
    }
}