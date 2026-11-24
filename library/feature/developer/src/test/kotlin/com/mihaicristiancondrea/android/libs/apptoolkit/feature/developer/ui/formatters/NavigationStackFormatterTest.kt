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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.formatters

import androidx.navigation3.runtime.NavKey
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NavigationStackFormatterTest {

    private object HomeKey : NavKey {
        override fun toString(): String = "com.example.app.HomeKey@1a2b3c"
    }

    private data class DetailKey(val id: Int) : NavKey {
        override fun toString(): String = "com.example.app.DetailKey(id=$id)"
    }

    @Test
    fun `empty stack formats to an empty string`() {
        assertEquals("", emptyList<NavKey>().describeNavigationStack())
    }

    @Test
    fun `stack strips packages, identity hashes, and data class arguments`() {
        val stack: List<NavKey> = listOf(HomeKey, DetailKey(id = 42))
        assertEquals("HomeKey  \u203A  DetailKey", stack.describeNavigationStack())
    }
}
