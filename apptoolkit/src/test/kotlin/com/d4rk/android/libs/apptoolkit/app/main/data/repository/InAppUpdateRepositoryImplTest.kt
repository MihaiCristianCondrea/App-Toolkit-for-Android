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

package com.d4rk.android.libs.apptoolkit.app.main.data.repository

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateHost
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateResult
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InAppUpdateRepositoryImplTest {

    private val repository = InAppUpdateRepositoryImpl()

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `emits started when immediate update is available`() = runTest {
        val activity = mockk<Activity>()
        val launcher = mockk<ActivityResultLauncher<IntentSenderRequest>>(relaxed = true)
        val host = mockk<InAppUpdateHost> {
            every { this@mockk.activity } returns activity
            every { updateResultLauncher } returns launcher
        }
        val manager = mockk<AppUpdateManager>()
        val info = mockk<AppUpdateInfo>()
        mockkStatic(AppUpdateManagerFactory::class)
        every { AppUpdateManagerFactory.create(activity) } returns manager
        every { manager.appUpdateInfo } returns successTask(info)
        every { info.updateAvailability() } returns UpdateAvailability.UPDATE_AVAILABLE
        every { info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) } returns true
        every {
            manager.startUpdateFlowForResult(info, launcher, any<AppUpdateOptions>())
        } returns true

        val result = repository.requestUpdate(host = host).first()

        assertEquals(InAppUpdateResult.Started, result)
    }

    @Test
    fun `emits not allowed when immediate updates are disabled`() = runTest {
        val activity = mockk<Activity>()
        val launcher = mockk<ActivityResultLauncher<IntentSenderRequest>>(relaxed = true)
        val host = mockk<InAppUpdateHost> {
            every { this@mockk.activity } returns activity
            every { updateResultLauncher } returns launcher
        }
        val manager = mockk<AppUpdateManager>()
        val info = mockk<AppUpdateInfo>()
        mockkStatic(AppUpdateManagerFactory::class)
        every { AppUpdateManagerFactory.create(activity) } returns manager
        every { manager.appUpdateInfo } returns successTask(info)
        every { info.updateAvailability() } returns UpdateAvailability.UPDATE_AVAILABLE
        every { info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) } returns false

        val result = repository.requestUpdate(host = host).first()

        assertEquals(InAppUpdateResult.NotAllowed, result)
    }

    @Test
    fun `emits not available when no update exists`() = runTest {
        val activity = mockk<Activity>()
        val launcher = mockk<ActivityResultLauncher<IntentSenderRequest>>(relaxed = true)
        val host = mockk<InAppUpdateHost> {
            every { this@mockk.activity } returns activity
            every { updateResultLauncher } returns launcher
        }
        val manager = mockk<AppUpdateManager>()
        val info = mockk<AppUpdateInfo>()
        mockkStatic(AppUpdateManagerFactory::class)
        every { AppUpdateManagerFactory.create(activity) } returns manager
        every { manager.appUpdateInfo } returns successTask(info)
        every { info.updateAvailability() } returns UpdateAvailability.UPDATE_NOT_AVAILABLE
        every { info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) } returns true

        val result = repository.requestUpdate(host = host).first()

        assertEquals(InAppUpdateResult.NotAvailable, result)
    }

    @Test
    fun `emits failed when update info request fails`() = runTest {
        val activity = mockk<Activity>()
        val launcher = mockk<ActivityResultLauncher<IntentSenderRequest>>(relaxed = true)
        val host = mockk<InAppUpdateHost> {
            every { this@mockk.activity } returns activity
            every { updateResultLauncher } returns launcher
        }
        val manager = mockk<AppUpdateManager>()
        mockkStatic(AppUpdateManagerFactory::class)
        every { AppUpdateManagerFactory.create(activity) } returns manager
        every { manager.appUpdateInfo } returns failureTask(Exception("boom"))

        val result = repository.requestUpdate(host = host).first()

        assertEquals(InAppUpdateResult.Failed, result)
    }

    private fun <T> successTask(result: T): Task<T> {
        val task = mockk<Task<T>>(relaxed = true)
        every { task.addOnSuccessListener(any()) } answers {
            firstArg<OnSuccessListener<T>>().onSuccess(result)
            task
        }
        every { task.addOnFailureListener(any()) } returns task
        return task
    }

    private fun <T> failureTask(exception: Exception): Task<T> {
        val task = mockk<Task<T>>(relaxed = true)
        every { task.addOnSuccessListener(any()) } returns task
        every { task.addOnFailureListener(any()) } answers {
            firstArg<OnFailureListener>().onFailure(exception)
            task
        }
        return task
    }
}
