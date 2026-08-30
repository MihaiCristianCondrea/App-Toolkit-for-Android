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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.core.data.local.datastore.DatastoreInterface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ShowcaseUnlockRepositoryTest {

    private val dataStore = mockk<DatastoreInterface>()
    private val firebaseController = mockk<FirebaseController>(relaxed = true)

    @Test
    fun `release build unlocks at seven taps only once`() = runTest {
        val unlocked = MutableStateFlow(false)
        every { dataStore.componentsShowcaseUnlocked } returns unlocked
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } coAnswers {
            unlocked.value = firstArg()
        }
        val repository = ShowcaseUnlockRepository(
            dataStore = dataStore,
            firebaseController = firebaseController,
            isDebugBuild = false,
        )

        repository.unlockAfterVersionTaps(tapCount = 6)
        repository.unlockAfterVersionTaps(tapCount = 7)
        repository.unlockAfterVersionTaps(tapCount = 8)

        coVerify(exactly = 1) { dataStore.saveComponentsShowcaseUnlocked(isUnlocked = true) }
    }

    @Test
    fun `debug build does not persist an unlock flag`() = runTest {
        every { dataStore.componentsShowcaseUnlocked } returns MutableStateFlow(false)
        val repository = ShowcaseUnlockRepository(
            dataStore = dataStore,
            firebaseController = firebaseController,
            isDebugBuild = true,
        )

        repository.unlockAfterVersionTaps(tapCount = 7)

        coVerify(exactly = 0) { dataStore.saveComponentsShowcaseUnlocked(any()) }
    }
}
