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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.backstack

import androidx.compose.runtime.toMutableStateList
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDestination
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDestinationType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NavigationBackStackActionsTest {

    private data class TestDestination(
        val id: String,
        override val destinationType: NavigationDestinationType,
    ) : NavigationDestination

    private val home = TestDestination("home", NavigationDestinationType.TopLevel)
    private val search = TestDestination("search", NavigationDestinationType.TopLevel)
    private val settings = TestDestination("settings", NavigationDestinationType.TopLevel)
    private val detail = TestDestination("detail", NavigationDestinationType.Nested)
    private val subDetail = TestDestination("sub_detail", NavigationDestinationType.Nested)
    private val profileActivity = TestDestination("profile", NavigationDestinationType.ActivityLike)

    @Test
    fun `navigateTopLevel throws when destination is not top level`() {
        val stack = listOf(home).toMutableStateList()

        assertThrows(IllegalStateException::class.java) {
            stack.navigateTopLevel(detail)
        }
        assertThrows(IllegalStateException::class.java) {
            stack.navigateTopLevel(profileActivity)
        }
    }

    @Test
    fun `navigateTopLevel removes nested entries before adding new top level destination`() {
        val stack = listOf(home, detail, subDetail).toMutableStateList()

        stack.navigateTopLevel(search)

        assertEquals(listOf(home, search), stack.toList())
    }

    @Test
    fun `navigateTopLevel does not duplicate current top level destination`() {
        val stack = listOf(home, detail).toMutableStateList()

        stack.navigateTopLevel(home)

        assertEquals(listOf(home), stack.toList())
    }

    @Test
    fun `navigateSingleTop appends destination when not already at top`() {
        val stack = listOf(home).toMutableStateList()

        stack.navigateSingleTop(detail)

        assertEquals(listOf(home, detail), stack.toList())
    }

    @Test
    fun `navigateSingleTop ignores duplicate if destination is already last entry`() {
        val stack = listOf(home, detail).toMutableStateList()

        stack.navigateSingleTop(detail)

        assertEquals(listOf(home, detail), stack.toList())
    }

    @Test
    fun `navigateBack pops top entry when stack has multiple entries`() {
        val stack = listOf(home, detail).toMutableStateList()
        var finished = false

        stack.navigateBack { finished = true }

        assertEquals(listOf(home), stack.toList())
        assertFalse(finished)
    }

    @Test
    fun `navigateBack invokes onFinish when only root entry remains`() {
        val stack = listOf(home).toMutableStateList()
        var finished = false

        stack.navigateBack { finished = true }

        assertEquals(listOf(home), stack.toList())
        assertTrue(finished)
    }
}
