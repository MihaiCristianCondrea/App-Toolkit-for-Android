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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.R
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.analytics.ADS_SETTINGS_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.analytics.AdsPreferenceKeys
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.models.AdsPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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

    private val UiText.resourceId: Int
        get() = (this as UiText.StringResource).resourceId

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
    fun `reloading while preferences are ready keeps content without flashing loading`() =
        runTest(dispatcherExtension.testDispatcher) {
            val secondReadGate = CompletableDeferred<Unit>()
            val repository = FakeAdsSettingsRepository(adsEnabled = true, reduceAds = false)
            val viewModel = createViewModel(repository)
            advance()
            assertIs<Loadable.Ready<AdsPreferences>>(viewModel.state.value.preferences)

            val observedStates = mutableListOf<Loadable<AdsPreferences>>()
            val collector = launch {
                viewModel.state.collect { observedStates += it.preferences }
            }

            repository.readGate = secondReadGate
            viewModel.onEvent(AdsSettingsEvent.Load)
            advance()

            assertTrue(observedStates.none { it is Loadable.Loading })
            assertEquals(
                Loadable.Ready(AdsPreferences(adsEnabled = true, reduceAds = false)),
                viewModel.state.value.preferences,
            )

            secondReadGate.complete(Unit)
            advance()
            collector.cancel()
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
    fun `turning ads off stores it and logs the preference toggle event`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeAdsSettingsRepository(adsEnabled = true))
            advance()

            viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(enabled = false))
            advance()

            val preferences = assertIs<Loadable.Ready<AdsPreferences>>(viewModel.state.value.preferences)
            assertFalse(preferences.value.adsEnabled)
            assertTrue(viewModel.messages.value.isEmpty())

            val toggleEvent = telemetryRepository.loggedEvents.single {
                it.name == SettingsAnalytics.Events.PREFERENCE_TOGGLE
            }
            assertEquals(
                mapOf(
                    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = ADS_SETTINGS_SCREEN_NAME),
                    SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(value = AdsPreferenceKeys.DISPLAY_ADS),
                    SettingsAnalytics.Params.ENABLED to AnalyticsValue.Str(value = "false"),
                ),
                toggleEvent.params,
            )
        }

    @Test
    fun `reducing ads stores it and logs the preference toggle event`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeAdsSettingsRepository(reduceAds = false))
            advance()

            viewModel.onEvent(AdsSettingsEvent.SetReduceAds(enabled = true))
            advance()

            val preferences = assertIs<Loadable.Ready<AdsPreferences>>(viewModel.state.value.preferences)
            assertTrue(preferences.value.reduceAds)

            val toggleEvent = telemetryRepository.loggedEvents.single {
                it.name == SettingsAnalytics.Events.PREFERENCE_TOGGLE
            }
            assertEquals(
                mapOf(
                    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(value = ADS_SETTINGS_SCREEN_NAME),
                    SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(value = AdsPreferenceKeys.REDUCE_ADS),
                    SettingsAnalytics.Params.ENABLED to AnalyticsValue.Str(value = "true"),
                ),
                toggleEvent.params,
            )
        }

    @Test
    fun `a failed write keeps the switch, shows an error message, and logs no toggle event`() =
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
            assertTrue(
                telemetryRepository.loggedEvents.none {
                    it.name == SettingsAnalytics.Events.PREFERENCE_TOGGLE
                },
            )
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
        var readGate: CompletableDeferred<Unit>? = null,
        private val writeFailure: Throwable? = null,
    ) : AdsSettingsRepository {
        override val adsEnabled = MutableStateFlow(adsEnabled)
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
            readGate?.await()
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
