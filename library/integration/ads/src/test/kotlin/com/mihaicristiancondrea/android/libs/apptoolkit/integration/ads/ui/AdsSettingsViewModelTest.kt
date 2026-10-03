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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui

import android.app.Activity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.R
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.models.AdsPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AdsSettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private val host = ConsentHost(activity = mockk<Activity>(relaxed = true))

    private fun createViewModel(
        repository: AdsSettingsRepository,
        consentRepository: ConsentRepository = FakeConsentRepository(),
    ): AdsSettingsViewModel = AdsSettingsViewModel(
        repository = repository,
        consentRepository = consentRepository,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private val UiTextHelper.resourceId: Int
        get() = (this as UiTextHelper.StringResource).resourceId

    @Test
    fun `the first load shows the stored preferences`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeAdsSettingsRepository(adsEnabled = true, reduceAds = true))
        advance()

        assertEquals(
            Loadable.Ready(AdsPreferences(adsEnabled = true, reduceAds = true)),
            viewModel.state.value.preferences,
        )
    }

    @Test
    fun `a failed read shows a retryable failure with the storage text`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeAdsSettingsRepository(
                readFailure = StorageException(StorageException.Reason.FAILED),
            )
            val viewModel = createViewModel(repository)
            advance()

            val preferences = assertIs<Loadable.Failed>(viewModel.state.value.preferences)
            assertEquals(R.string.error_ads_settings_storage, preferences.message.resourceId)
            assertTrue(preferences.retryable)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `retrying after a failed read shows the preferences`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeAdsSettingsRepository(
            adsEnabled = false,
            readFailure = StorageException(StorageException.Reason.FAILED),
        )
        val viewModel = createViewModel(repository)
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.preferences)

        repository.readFailure = null
        viewModel.onEvent(AdsSettingsEvent.Load)
        advance()

        assertEquals(
            Loadable.Ready(AdsPreferences(adsEnabled = false, reduceAds = false)),
            viewModel.state.value.preferences,
        )
    }

    @Test
    fun `turning ads off stores it and the switch follows`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeAdsSettingsRepository(adsEnabled = true))
        advance()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(enabled = false))
        advance()

        val preferences = assertIs<Loadable.Ready<AdsPreferences>>(viewModel.state.value.preferences)
        assertFalse(preferences.value.adsEnabled)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `reducing ads stores it and the switch follows`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeAdsSettingsRepository(reduceAds = false))
        advance()

        viewModel.onEvent(AdsSettingsEvent.SetReduceAds(enabled = true))
        advance()

        val preferences = assertIs<Loadable.Ready<AdsPreferences>>(viewModel.state.value.preferences)
        assertTrue(preferences.value.reduceAds)
    }

    @Test
    fun `a failed write keeps the switch and shows an error message`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = FakeAdsSettingsRepository(
                adsEnabled = true,
                writeFailure = StorageException(StorageException.Reason.FAILED),
            )
            val viewModel = createViewModel(repository)
            advance()

            viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(enabled = false))
            advance()

            val preferences = assertIs<Loadable.Ready<AdsPreferences>>(viewModel.state.value.preferences)
            assertTrue(preferences.value.adsEnabled)
            val message = viewModel.messages.value.single()
            assertTrue(message.isError)
            assertEquals(R.string.error_ads_settings_storage, message.text.resourceId)
        }

    @Test
    fun `a full disk shows its own text`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeAdsSettingsRepository(writeFailure = StorageException(StorageException.Reason.FULL))
        val viewModel = createViewModel(repository)
        advance()

        viewModel.onEvent(AdsSettingsEvent.SetReduceAds(enabled = true))
        advance()

        val message = viewModel.messages.value.single()
        assertEquals(CoreUiR.string.screen_error_storage_full, message.text.resourceId)
    }

    @Test
    fun `opening the privacy form always shows it`() = runTest(dispatcherExtension.testDispatcher) {
        val consentRepository = FakeConsentRepository()
        val viewModel = createViewModel(FakeAdsSettingsRepository(), consentRepository)
        advance()

        viewModel.onEvent(AdsSettingsEvent.RequestConsent(host))
        advance()

        assertEquals(listOf(false), consentRepository.showIfRequiredCalls)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `a failed consent request shows the consent error`() = runTest(dispatcherExtension.testDispatcher) {
        val consentRepository = FakeConsentRepository(
            failure = ConsentException(ConsentException.Reason.FORM_FAILED, message = "form"),
        )
        val viewModel = createViewModel(FakeAdsSettingsRepository(), consentRepository)
        advance()

        viewModel.onEvent(AdsSettingsEvent.RequestConsent(host))
        advance()

        val message = viewModel.messages.value.single()
        assertTrue(message.isError)
        assertEquals(R.string.error_ads_consent_failed, message.text.resourceId)
    }

    private class FakeAdsSettingsRepository(
        adsEnabled: Boolean = true,
        reduceAds: Boolean = false,
        var readFailure: Throwable? = null,
        private val writeFailure: Throwable? = null,
    ) : AdsSettingsRepository {
        private val adsEnabled = MutableStateFlow(adsEnabled)
        private val reduceAds = MutableStateFlow(reduceAds)

        override fun observeAdsEnabled(): Flow<Boolean> = observe(adsEnabled)

        override fun observeReduceAds(): Flow<Boolean> = observe(reduceAds)

        override suspend fun setAdsEnabled(enabled: Boolean) {
            writeFailure?.let { throw it }
            adsEnabled.value = enabled
        }

        override suspend fun setReduceAds(enabled: Boolean) {
            writeFailure?.let { throw it }
            reduceAds.value = enabled
        }

        private fun observe(source: MutableStateFlow<Boolean>): Flow<Boolean> = flow {
            readFailure?.let { throw it }
            source.collect { emit(it) }
        }
    }

    private class FakeConsentRepository(
        private val failure: Throwable? = null,
    ) : ConsentRepository {
        val showIfRequiredCalls = mutableListOf<Boolean>()

        override suspend fun requestConsent(host: ConsentHost, showIfRequired: Boolean) {
            showIfRequiredCalls += showIfRequired
            failure?.let { throw it }
        }

        override suspend fun applyInitialConsent() = Unit

        override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
    }
}
