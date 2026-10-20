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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TopBarScrollModeTest {
    private data object Home : NavKey
    private data object Child : NavKey
    private data object Page : NavKey
    private data object Fallback : NavKey

    private val icon = ToolkitIcon.Vector(Icons.Outlined.Home)

    @Test
    fun `existing registrations keep the bar following the scroll`() {
        val graph = shellGraph(appTitle = 0) {
            tab(Home, label = 0, icon = icon) {}
            child<Child>(title = { "Child" }) {}
            page<Page>(title = { "Page" }) {}
            pageIfAbsent<Fallback>(paneRole = PaneRole.None, title = { "Fallback" }) {}
        }

        listOf(Home, Child, Page, Fallback).forEach {
            assertEquals(TopBarScrollMode.Default, graph.destination(it).topBarScrollMode)
        }
    }

    @Test
    fun `every registration keeps a static mode without changing its kind`() {
        val graph = shellGraph(appTitle = 0) {
            tab(Home, label = 0, icon = icon, topBarScrollMode = TopBarScrollMode.Static) {}
            child<Child>(title = { "Child" }, topBarScrollMode = TopBarScrollMode.Static) {}
            page<Page>(title = { "Page" }, topBarScrollMode = TopBarScrollMode.Static) {}
            pageIfAbsent<Fallback>(paneRole = PaneRole.Detail, title = { "Fallback" }, topBarScrollMode = TopBarScrollMode.Static) {}
        }

        listOf(Home, Child, Page, Fallback).forEach {
            assertEquals(TopBarScrollMode.Static, graph.destination(it).topBarScrollMode)
        }
        assertEquals(DestinationKind.Child, graph.destination(Child).kind)
        assertEquals(PaneRole.Detail, graph.destination(Fallback).paneRole)
    }
}
