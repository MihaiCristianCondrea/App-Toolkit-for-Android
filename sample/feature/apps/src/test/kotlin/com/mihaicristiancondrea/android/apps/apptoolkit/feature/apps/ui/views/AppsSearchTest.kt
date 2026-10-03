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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.screens.search
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AppsSearchTest {

    private val apps = listOf(
        AppInfo(name = "Smart Cleaner", packageName = "com.example.cleaner", iconUrl = ""),
        AppInfo(name = "Music", packageName = "com.example.player", iconUrl = "", shortDescription = "Plays offline songs"),
        AppInfo(name = "Notes", packageName = "com.example.notes", iconUrl = ""),
    )

    @Test
    fun `a blank query keeps every app`() {
        assertEquals(apps, apps.search("  "))
    }

    @Test
    fun `the query matches the name, the package or the description, ignoring case`() {
        assertEquals(listOf("Smart Cleaner"), apps.search("CLEAN").map { it.name })
        assertEquals(listOf("Music"), apps.search("player").map { it.name })
        assertEquals(listOf("Music"), apps.search("offline").map { it.name })
        assertEquals(emptyList(), apps.search("camera"))
    }
}
