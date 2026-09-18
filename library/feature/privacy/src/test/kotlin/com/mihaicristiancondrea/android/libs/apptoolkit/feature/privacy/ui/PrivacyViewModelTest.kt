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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class PrivacyViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private class RecordingProvider : PrivacySettingsProvider {
        var permissionsOpened: Int = 0
        var adsOpened: Int = 0
        var diagnosticsOpened: Int = 0

        override val privacyPolicyUrl: String = "https://example.test/privacy"

        override fun openPermissionsScreen() { permissionsOpened += 1 }
        override fun openAdsScreen() { adsOpened += 1 }
        override fun openUsageAndDiagnosticsScreen() { diagnosticsOpened += 1 }
    }

    private fun createViewModel(provider: PrivacySettingsProvider = RecordingProvider()) =
        PrivacyViewModel(provider = provider, firebaseController = FakeFirebaseController())

    @Test
    fun `initial load exposes the provider's entries`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            val keys = viewModel.uiState.value.data?.items?.map { it.key }
            assertThat(keys).contains(PrivacyItemKey.PRIVACY_POLICY)
            assertThat(keys).contains(PrivacyItemKey.LICENSE)
        }

    @Test
    fun `a url row leaves as an action for the screen to open`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            val actions = mutableListOf<PrivacyAction>()
            val job = launch { viewModel.actionEvent.collect { actions.add(it) } }
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(
                PrivacyEvent.ItemClicked(
                    action = PrivacyItemAction.OpenUrl(url = "https://example.test/privacy"),
                )
            )
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(actions)
                .containsExactly(PrivacyAction.OpenUrl(url = "https://example.test/privacy"))
            job.cancel()
        }

    @Test
    fun `host destinations are opened through the provider, not emitted as actions`() =
        runTest(dispatcherExtension.testDispatcher) {
            val provider = RecordingProvider()
            val viewModel = createViewModel(provider = provider)
            val actions = mutableListOf<PrivacyAction>()
            val job = launch { viewModel.actionEvent.collect { actions.add(it) } }
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(PrivacyEvent.ItemClicked(PrivacyItemAction.OpenPermissions))
            viewModel.onEvent(PrivacyEvent.ItemClicked(PrivacyItemAction.OpenAds))
            viewModel.onEvent(PrivacyEvent.ItemClicked(PrivacyItemAction.OpenUsageAndDiagnostics))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(provider.permissionsOpened).isEqualTo(1)
            assertThat(provider.adsOpened).isEqualTo(1)
            assertThat(provider.diagnosticsOpened).isEqualTo(1)
            assertThat(actions).isEmpty()
            job.cancel()
        }
}
