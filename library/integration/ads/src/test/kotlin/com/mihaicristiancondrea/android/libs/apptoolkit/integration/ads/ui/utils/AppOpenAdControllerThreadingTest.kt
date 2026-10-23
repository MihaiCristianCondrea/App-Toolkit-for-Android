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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.utils

import android.app.Activity
import android.content.Context
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.remote.AdsSdkInitializer
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AppOpenAdControllerThreadingTest {
    @Test
    fun `load callbacks publish their state only on the injected main dispatcher`() = runTest {
        val dispatchers = object : DispatcherProvider {
            override val main: CoroutineDispatcher = StandardTestDispatcher(testScheduler)
            override val io: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
            override val default: CoroutineDispatcher = io
            override val unconfined: CoroutineDispatcher = io
        }
        val context = mockk<Context>()
        val controller = AppOpenAdController(
            context = context,
            buildInfoRepository = mockk<BuildInfoRepository>(),
            dispatchers = dispatchers,
            adMobAppIdDataSource = { "ca-app-pub-1234567890123456~1234567890" },
            adsSdkInitializer = AdsSdkInitializer { _, _ -> },
            adsSettingsRepository = FakeAdsSettingsRepository(),
        )
        val callback = slot<AdLoadCallback<AppOpenAd>>()
        mockkObject(AppOpenAd.Companion)
        every { AppOpenAd.load(any(), capture(callback)) } returns Unit
        try {
            val initialization = async { controller.initializeAds("unit") }
            runCurrent()
            initialization.await()
            controller.showAdIfAvailable(mockk<Activity>(), backgroundScope)
            runCurrent()
            val manager = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager").apply {
                isAccessible = true
            }.get(controller)
            val adField = manager.javaClass.getDeclaredField("appOpenAd").apply { isAccessible = true }
            val ad = mockk<AppOpenAd>()
            callback.captured.onAdLoaded(ad)
            assertNull(adField.get(manager))
            runCurrent()
            assertSame(ad, adField.get(manager))
        } finally {
            controller.close()
            unmockkObject(AppOpenAd.Companion)
        }
    }
}
