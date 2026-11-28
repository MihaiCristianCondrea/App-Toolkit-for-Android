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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SpacingTest {

    @Test
    fun `default spacing steps have their documented values`() {
        val spacing = Spacing()

        assertEquals(0.dp, spacing.none)
        assertEquals(2.dp, spacing.extraTiny)
        assertEquals(4.dp, spacing.extraSmall)
        assertEquals(6.dp, spacing.extraSmallIncreased)
        assertEquals(8.dp, spacing.small)
        assertEquals(10.dp, spacing.smallIncreased)
        assertEquals(12.dp, spacing.medium)
        assertEquals(14.dp, spacing.mediumIncreased)
        assertEquals(16.dp, spacing.large)
        assertEquals(20.dp, spacing.largeIncreased)
        assertEquals(24.dp, spacing.extraLargeCompact)
        assertEquals(28.dp, spacing.extraLarge)
        assertEquals(32.dp, spacing.extraLargeIncreased)
        assertEquals(40.dp, spacing.extraExtraLargeCompact)
        assertEquals(48.dp, spacing.extraExtraLarge)
        assertEquals(56.dp, spacing.extraExtraLargeIncreased)
        assertEquals(64.dp, spacing.huge)
        assertEquals(72.dp, spacing.extraHuge)
    }

    @Test
    fun `steps named after Material 3 shapes match the shape corner of the same name`() {
        val spacing = Spacing()

        assertEquals(ShapeDefaults.ExtraSmall, RoundedCornerShape(size = spacing.extraSmall))
        assertEquals(ShapeDefaults.Small, RoundedCornerShape(size = spacing.small))
        assertEquals(ShapeDefaults.Medium, RoundedCornerShape(size = spacing.medium))
        assertEquals(ShapeDefaults.Large, RoundedCornerShape(size = spacing.large))
        assertEquals(ShapeDefaults.LargeIncreased, RoundedCornerShape(size = spacing.largeIncreased))
        assertEquals(ShapeDefaults.ExtraLarge, RoundedCornerShape(size = spacing.extraLarge))
        assertEquals(
            ShapeDefaults.ExtraLargeIncreased,
            RoundedCornerShape(size = spacing.extraLargeIncreased),
        )
        assertEquals(
            ShapeDefaults.ExtraExtraLarge,
            RoundedCornerShape(size = spacing.extraExtraLarge),
        )
    }

    @Test
    fun `overriding one step leaves the others at their defaults`() {
        val custom = Spacing(large = 18.dp)

        assertEquals(18.dp, custom.large)
        assertEquals(SpacingDefaults.Medium, custom.medium)
        assertEquals(SpacingDefaults.LargeIncreased, custom.largeIncreased)
    }
}
