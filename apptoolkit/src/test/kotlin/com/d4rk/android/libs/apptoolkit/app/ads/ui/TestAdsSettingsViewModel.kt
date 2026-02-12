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

package com.d4rk.android.libs.apptoolkit.app.ads.ui

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.ObserveAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.SetAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TestAdsSettingsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun testDispatchers(): DispatcherProvider =
        TestDispatchers(dispatcherExtension.testDispatcher)

    private val firebaseController = FakeFirebaseController()

    private class FakeAdsSettingsRepository(
        override val defaultAdsEnabled: Boolean = true,
        var shouldFail: Boolean = false
    ) : AdsSettingsRepository {

        private val state = MutableStateFlow(defaultAdsEnabled)

        override fun observeAdsEnabled(): Flow<Boolean> = state

        override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> {
            if (shouldFail) throw IOException("fail")
            state.value = enabled
            return Result.Success(Unit)
        }
    }

    private fun createViewModel(repository: AdsSettingsRepository): AdsSettingsViewModel {
        val observeUseCase = ObserveAdsEnabledUseCase(repository, firebaseController)
        val setUseCase = SetAdsEnabledUseCase(repository, firebaseController)
        return AdsSettingsViewModel(
            observeAdsEnabled = observeUseCase,
            setAdsEnabled = setUseCase,
            requestConsentUseCase = RequestConsentUseCase(FakeConsentRepository(), firebaseController),
            repository = repository,
            dispatchers = testDispatchers(),
            firebaseController = firebaseController,
        )
    }

    @Test
    fun `initial state reflects repository value`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = createViewModel(repo)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.adsEnabled).isTrue()
    }

    @Test
    fun `emission error sets default and error state`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repo = object : AdsSettingsRepository {
                override val defaultAdsEnabled: Boolean = false
                override fun observeAdsEnabled(): Flow<Boolean> = flow { throw IOException("boom") }
                override suspend fun setAdsEnabled(enabled: Boolean): Result<Unit> =
                    Result.Success(Unit)
            }

            val viewModel = createViewModel(repo)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
            assertThat(state.data?.adsEnabled).isFalse()
        }

    @Test
    fun `setAdsEnabled success updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true)
        val viewModel = createViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.adsEnabled).isFalse()
    }

    @Test
    fun `setAdsEnabled error reverts state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeAdsSettingsRepository(defaultAdsEnabled = true, shouldFail = true)

        val viewModel = createViewModel(repo)
        advanceUntilIdle()

        viewModel.onEvent(AdsSettingsEvent.SetAdsEnabled(false))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
        assertThat(state.data?.adsEnabled).isTrue()
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
