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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size

import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SizeTest {

    @Test
    fun `default size steps have their documented values`() {
        val size = Size()

        assertEquals(16.dp, size.extraSmall)
        assertEquals(20.dp, size.small)
        assertEquals(24.dp, size.medium)
        assertEquals(32.dp, size.large)
        assertEquals(40.dp, size.extraLarge)
        assertEquals(48.dp, size.extraExtraLarge)
        assertEquals(64.dp, size.huge)
    }

    @Test
    fun `overriding one step leaves the others at their defaults`() {
        val custom = Size(medium = 28.dp)

        assertEquals(28.dp, custom.medium)
        assertEquals(SizeDefaults.Small, custom.small)
        assertEquals(SizeDefaults.Large, custom.large)
    }
}
