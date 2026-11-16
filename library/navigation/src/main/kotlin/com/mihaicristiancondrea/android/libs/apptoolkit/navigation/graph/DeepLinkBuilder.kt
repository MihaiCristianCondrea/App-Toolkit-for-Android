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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import android.content.Intent
import androidx.navigation3.runtime.NavKey

/** Collects [ShellGraphBuilder.deepLinks]. The first link that returns a key wins. */
class DeepLinkBuilder internal constructor() {
    internal val links = mutableListOf<(Intent) -> NavKey?>()

    /** Intents with [action] open the destination [key] returns for them. */
    fun action(action: String, key: (Intent) -> NavKey?) {
        links += { intent -> if (intent.action == action) key(intent) else null }
    }

    /** Any intent [key] recognises, by its data, extras or anything else. */
    fun match(key: (Intent) -> NavKey?) {
        links += key
    }
}
