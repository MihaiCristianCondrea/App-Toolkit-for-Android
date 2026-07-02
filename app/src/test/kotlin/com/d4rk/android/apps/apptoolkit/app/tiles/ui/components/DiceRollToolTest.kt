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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.components

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DiceRollToolTest {

    @Test
    fun `targetAnglesFor returns expected rotations for each dice value`() {
        // 1 -> Top visible
        assertEquals(-90f to 0f, targetAnglesFor(1))
        // 2 -> Front visible
        assertEquals(0f to 0f, targetAnglesFor(2))
        // 3 -> Right visible
        assertEquals(0f to -90f, targetAnglesFor(3))
        // 4 -> Left visible
        assertEquals(0f to 90f, targetAnglesFor(4))
        // 5 -> Back visible
        assertEquals(0f to 180f, targetAnglesFor(5))
        // 6 -> Bottom visible
        assertEquals(90f to 0f, targetAnglesFor(6))
        // Default
        assertEquals(0f to 0f, targetAnglesFor(7))
    }

    @Test
    fun `ConstantFaceNumbers mapping is stable and follows standard dice layout`() {
        assertEquals(1, ConstantFaceNumbers[DiceCubeFace.Top])
        assertEquals(6, ConstantFaceNumbers[DiceCubeFace.Bottom])
        assertEquals(2, ConstantFaceNumbers[DiceCubeFace.Front])
        assertEquals(5, ConstantFaceNumbers[DiceCubeFace.Back])
        assertEquals(3, ConstantFaceNumbers[DiceCubeFace.Right])
        assertEquals(4, ConstantFaceNumbers[DiceCubeFace.Left])

        // Verify opposite faces sum to 7 (standard dice)
        assertEquals(7, ConstantFaceNumbers.getValue(DiceCubeFace.Top) + ConstantFaceNumbers.getValue(DiceCubeFace.Bottom))
        assertEquals(7, ConstantFaceNumbers.getValue(DiceCubeFace.Front) + ConstantFaceNumbers.getValue(DiceCubeFace.Back))
        assertEquals(7, ConstantFaceNumbers.getValue(DiceCubeFace.Right) + ConstantFaceNumbers.getValue(DiceCubeFace.Left))
    }

    @Test
    fun `DiceFaceDefinitions cover all 6 faces`() {
        val faces = DiceFaceDefinitions.map { it.face }.toSet()
        assertEquals(6, faces.size)
        assertTrue(faces.contains(DiceCubeFace.Top))
        assertTrue(faces.contains(DiceCubeFace.Bottom))
        assertTrue(faces.contains(DiceCubeFace.Front))
        assertTrue(faces.contains(DiceCubeFace.Back))
        assertTrue(faces.contains(DiceCubeFace.Left))
        assertTrue(faces.contains(DiceCubeFace.Right))
    }
}
