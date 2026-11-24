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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.extensions

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class ConsentHostAvailabilityTest {

    private fun host(
        finishing: Boolean = false,
        destroyed: Boolean = false,
        changingConfigurations: Boolean = false,
        lifecycleState: Lifecycle.State? = null,
    ): ConsentHost {
        val activity = if (lifecycleState != null) {
            val lifecycle = mockk<Lifecycle> { every { currentState } returns lifecycleState }
            mockk<Activity>(moreInterfaces = arrayOf(LifecycleOwner::class)) {
                every { (this@mockk as LifecycleOwner).lifecycle } returns lifecycle
            }
        } else {
            mockk<Activity>()
        }
        every { activity.isFinishing } returns finishing
        every { activity.isDestroyed } returns destroyed
        every { activity.isChangingConfigurations } returns changingConfigurations
        return ConsentHost(activity = activity)
    }

    @Test
    fun `isAlive is false when activity is finishing, destroyed, or changing configurations`() {
        assertFalse(host(finishing = true).isAlive)
        assertFalse(host(destroyed = true).isAlive)
        assertFalse(host(changingConfigurations = true).isAlive)
    }

    @Test
    fun `isAlive and canShowConsentForm fall back to alive check for non-LifecycleOwner activities`() {
        val aliveHost = host()
        assertTrue(aliveHost.isAlive)
        assertTrue(aliveHost.canShowConsentForm)
    }

    @Test
    fun `canShowConsentForm requires at least STARTED lifecycle state`() {
        assertFalse(host(lifecycleState = Lifecycle.State.CREATED).canShowConsentForm)
        assertTrue(host(lifecycleState = Lifecycle.State.STARTED).canShowConsentForm)
        assertTrue(host(lifecycleState = Lifecycle.State.RESUMED).canShowConsentForm)
    }

    @Test
    fun `canShowConsentForm is false when changing configurations even if STARTED`() {
        assertFalse(
            host(
                changingConfigurations = true,
                lifecycleState = Lifecycle.State.STARTED,
            ).canShowConsentForm,
        )
    }
}
