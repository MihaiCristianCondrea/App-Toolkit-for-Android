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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PrivacyViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(
        provider: PrivacySettingsProvider = FakePrivacySettingsProvider(),
    ): PrivacyViewModel =
        PrivacyViewModel(provider = provider, telemetryRepository = telemetryRepository)

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `the first load shows the provider's entries`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        val items = (viewModel.state.value.items as Loadable.Ready).value
        val policy = items.single { it.key == PrivacyItemKey.PRIVACY_POLICY } as PrivacyItem.Preference
        assertEquals(PrivacyItemAction.OpenUrl(url = "https://example.test/privacy"), policy.action)
        assertTrue(items.any { it.key == PrivacyItemKey.LICENSE })
    }

    @Test
    fun `a provider that throws shows a retryable failure and reports it`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakePrivacySettingsProvider(failure = IllegalStateException("fail")))
            advance()

            val items = assertIs<Loadable.Failed>(viewModel.state.value.items)
            assertEquals(
                CoreUiR.string.screen_error_generic,
                (items.message as UiTextHelper.StringResource).resourceId,
            )
            assertTrue(items.retryable)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "vm_op_error" })
        }

    @Test
    fun `retrying after a failure shows the entries`() = runTest(dispatcherExtension.testDispatcher) {
        val provider = FakePrivacySettingsProvider(failure = IllegalStateException("fail"))
        val viewModel = createViewModel(provider)
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.items)

        provider.failure = null
        viewModel.onEvent(PrivacyEvent.Load)
        advance()

        assertIs<Loadable.Ready<*>>(viewModel.state.value.items)
    }

    @Test
    fun `a tapped row is reported as the open operation and leaves the entries as they are`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()
            val loaded = viewModel.state.value

            viewModel.onEvent(PrivacyEvent.ItemClicked(action = PrivacyItemAction.OpenPermissions))
            advance()

            assertEquals(loaded, viewModel.state.value)
            assertTrue(
                telemetryRepository.loggedEvents.any { event ->
                    event.name == "vm_op_start" && event.params["action"] == AnalyticsValue.Str("openPrivacyItem")
                }
            )
        }

    /** Supplies one URL of its own, and throws from it while [failure] is set. */
    private class FakePrivacySettingsProvider(var failure: Throwable? = null) : PrivacySettingsProvider {
        override val privacyPolicyUrl: String
            get() {
                failure?.let { throw it }
                return "https://example.test/privacy"
            }
    }
}
