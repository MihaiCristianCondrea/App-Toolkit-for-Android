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

package com.wstxda.toolkit.manager.musicsearch

import android.content.Context
import android.content.Intent

object MusicSearchDispatcher {

    private const val GOOGLE_PACKAGE = "com.google.android.googlequicksearchbox"
    private const val MUSIC_SEARCH_ACTION = "$GOOGLE_PACKAGE.MUSIC_SEARCH"

    fun launchMusicSearch(context: Context): Boolean {
        return try {
            val intent = Intent(MUSIC_SEARCH_ACTION).apply {
                setPackage(GOOGLE_PACKAGE)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}