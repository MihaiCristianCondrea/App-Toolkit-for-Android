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

package com.d4rk.android.libs.apptoolkit.core.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResultTest {

    @Test
    fun `Success retains provided data`() {
        val success = Result.Success("payload")

        assertThat(success.data).isEqualTo("payload")
        val (captured) = success
        assertThat(captured).isEqualTo("payload")
    }

    @Test
    fun `Error retains provided exception`() {
        val exception = IllegalStateException("boom")

        val error = Result.Error(exception)

        assertThat(error.exception).isSameInstanceAs(exception)
        val (captured) = error
        assertThat(captured).isSameInstanceAs(exception)
    }

    @Test
    fun `Success equality depends on wrapped data`() {
        val first = Result.Success("data")
        val second = Result.Success("data")
        val third = Result.Success("other")

        assertThat(first).isEqualTo(second)
        assertThat(first).isNotEqualTo(third)
    }

    @Test
    fun `Error equality compares underlying exception`() {
        val exception = IllegalArgumentException("invalid")
        val first = Result.Error(exception)
        val second = Result.Error(exception)
        val third = Result.Error(IllegalArgumentException("invalid"))

        assertThat(first).isEqualTo(second)
        assertThat(first).isNotEqualTo(third)
    }
}
