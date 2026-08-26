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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.contracts.AdsSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.states.AdsToggleMode
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class AdsSettingsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun testDispatchers(): DispatcherProvider =
        TestDispatchers(dispatcherExtension.testDispatcher)

    private val firebaseController = FakeFirebaseController()

    private class FakeAdsSettingsRepository(
        limitAds: Boolean = false,
        var shouldFail: Boolean = false
    ) : AdsSettingsRepository {

        private val limitAdsState = MutableStateFlow(limitAds)

        override fun observeLimitAds(): Flow<Boolean> = limitAdsState

        override suspend fun setLimitAds(enabled: Boolean): DataState<Unit, Errors.Database> {
            if (shouldFail) throw IOException("fail")
            limitAdsState.value = enabled
            return DataState.Success(Unit)
        }
    }

    private fun buildInfo(isDebugBuild: Boolean): BuildInfoProvider = object : BuildInfoProvider {
        override val appVersion: String = "1.0.0"
        override val appVersionCode: Int = 1
        override val packageName: String = "com.example"
        override val isDebugBuild: Boolean = isDebugBuild
    }

    private fun createViewModel(
        repository: AdsSettingsRepository,
        isDebugBuild: Boolean = false,
    ): AdsSettingsViewModel {
        return AdsSettingsViewModel(
            repository = repository,
            consentRepository = FakeConsentRepository(),
            dispatchers = testDispatchers(),
            buildInfoProvider = buildInfo(isDebugBuild = isDebugBuild),
            firebaseController = firebaseController,
        )
    }

    @Test
    fun `initial state reflects repository value`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(limitAds = true)
        val viewModel = createViewModel(repo)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.limitAds).isTrue()
    }

    @Test
    fun `emission error keeps the opt-in off and reports the error`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repo = object : AdsSettingsRepository {
                override fun observeLimitAds(): Flow<Boolean> = flow { throw IOException("boom") }
                override suspend fun setLimitAds(enabled: Boolean): DataState<Unit, Errors.Database> =
                    DataState.Success(Unit)
            }

            val viewModel = createViewModel(repo)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat(state.data?.limitAds).isFalse()
        }

    // The toggle is one preference over one code path. All the build type changes is its wording,
    // so these two are the whole difference between a release and a debug ads screen.
    @Test
    fun `a release build labels the toggle Reduce ads`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeAdsSettingsRepository(), isDebugBuild = false)

            advanceUntilIdle()

            assertThat(viewModel.uiState.value.data?.mode).isEqualTo(AdsToggleMode.REDUCE)
        }

    @Test
    fun `a debug build labels the toggle Disable ads`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeAdsSettingsRepository(), isDebugBuild = true)

            advanceUntilIdle()

            assertThat(viewModel.uiState.value.data?.mode).isEqualTo(AdsToggleMode.DISABLE)
        }

    @Test
    fun `the toggle writes the same preference in either build`() =
        runTest(dispatcherExtension.testDispatcher) {
            val debugRepo = FakeAdsSettingsRepository()
            val releaseRepo = FakeAdsSettingsRepository()

            createViewModel(debugRepo, isDebugBuild = true)
                .onEvent(AdsSettingsEvent.SetLimitAds(true))
            createViewModel(releaseRepo, isDebugBuild = false)
                .onEvent(AdsSettingsEvent.SetLimitAds(true))
            advanceUntilIdle()

            assertThat(debugRepo.observeLimitAds().first()).isTrue()
            assertThat(releaseRepo.observeLimitAds().first()).isTrue()
        }

    @Test
    fun `limit ads defaults to off`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeAdsSettingsRepository())

        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.limitAds).isFalse()
    }

    @Test
    fun `setLimitAds success updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository()
        val viewModel = createViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetLimitAds(true))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.limitAds).isTrue()
    }

    @Test
    fun `setLimitAds error reverts state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(shouldFail = true)
        val viewModel = createViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetLimitAds(true))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
        assertThat(state.data?.limitAds).isFalse()
    }

}

private class FakeConsentRepository : ConsentRepository {
    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = flowOf(DataState.Success(Unit))

    override suspend fun applyInitialConsent() = Unit

    override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
}
