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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.extensions

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ActivityBillingExtensionsTest {

    @Test
    fun `returns true for an active plain activity`() {
        val activity = plainActivity()

        assertTrue(activity.isValidForBilling())
    }

    @Test
    fun `returns false when the activity is finishing`() {
        val activity = plainActivity(isFinishing = true)

        assertFalse(activity.isValidForBilling())
    }

    @Test
    fun `returns false when the activity is destroyed`() {
        val activity = plainActivity(isDestroyed = true)

        assertFalse(activity.isValidForBilling())
    }

    @Test
    fun `returns false when the activity is changing configurations`() {
        val activity = plainActivity(isChangingConfigurations = true)

        assertFalse(activity.isValidForBilling())
    }

    @Test
    fun `returns true for a resumed lifecycle owner activity`() {
        val activity = lifecycleActivity(state = Lifecycle.State.RESUMED)

        assertTrue(activity.isValidForBilling())
    }

    @Test
    fun `returns false for a started lifecycle owner activity`() {
        val activity = lifecycleActivity(state = Lifecycle.State.STARTED)

        assertFalse(activity.isValidForBilling())
    }

    @Test
    fun `returns false for a created lifecycle owner activity`() {
        val activity = lifecycleActivity(state = Lifecycle.State.CREATED)

        assertFalse(activity.isValidForBilling())
    }

    private fun plainActivity(
        isFinishing: Boolean = false,
        isDestroyed: Boolean = false,
        isChangingConfigurations: Boolean = false,
    ): Activity = mockk(relaxed = true) {
        every { this@mockk.isFinishing } returns isFinishing
        every { this@mockk.isDestroyed } returns isDestroyed
        every { this@mockk.isChangingConfigurations } returns isChangingConfigurations
    }

    private fun lifecycleActivity(
        state: Lifecycle.State,
        isFinishing: Boolean = false,
        isDestroyed: Boolean = false,
        isChangingConfigurations: Boolean = false,
    ): Activity {
        val lifecycle: Lifecycle = mockk {
            every { currentState } returns state
        }
        return mockk<Activity>(moreInterfaces = arrayOf(LifecycleOwner::class), relaxed = true) {
            every { this@mockk.isFinishing } returns isFinishing
            every { this@mockk.isDestroyed } returns isDestroyed
            every { this@mockk.isChangingConfigurations } returns isChangingConfigurations
            every { (this@mockk as LifecycleOwner).lifecycle } returns lifecycle
        }
    }
}
