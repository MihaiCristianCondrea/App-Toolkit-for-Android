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

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole

/**
 * Shows list and detail pages side by side while [enabled], which follows the window width.
 *
 * With [framed] false, for the tabs inside the shell, the scene draws no app bar and no window
 * frame, and a list with nothing open beside it stays a single, full-width pane.
 */
class ListDetailSceneStrategy<T : Any>(
    private val enabled: Boolean,
    private val framed: Boolean = true,
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (!enabled) return null
        val last = entries.last()
        return when (last.shellInfo?.paneRole) {
            PaneRole.List -> if (!framed) null else ListDetailScene(
                key = last.contentKey,
                listEntry = last,
                detailEntry = null,
                previousEntries = entries.dropLast(1),
                onBack = onBack,
            )

            PaneRole.Detail -> {
                val list = entries.getOrNull(entries.lastIndex - 1)
                    ?.takeIf { it.shellInfo?.paneRole == PaneRole.List }
                    ?: return null
                ListDetailScene(
                    key = list.contentKey,
                    listEntry = list,
                    detailEntry = last,
                    previousEntries = entries.dropLast(1),
                    onBack = onBack,
                    framed = framed,
                )
            }

            else -> null
        }
    }
}
