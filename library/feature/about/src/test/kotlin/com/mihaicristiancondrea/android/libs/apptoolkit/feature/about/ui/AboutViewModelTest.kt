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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.mappers.toAboutItems
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class AboutViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val defaultAboutInfo = AboutInfo(
        appVersion = "1.0",
        appVersionCode = 1,
        appToolkitVersion = "3.0.0-test",
        googlePlayServicesVersion = "24.01.12",
        deviceInfo = "device-info",
    )

    private val expectedItemKeys: List<String> = defaultAboutInfo.toAboutItems().map { it.key }

    private val telemetryRepository = FakeTelemetryRepository()

    private val seasonalThemes: SeasonalThemeRepository = mockk(relaxed = true)

    private val clipboard = FakeClipboardRepository()

    private fun createViewModel(
        repository: AboutRepository = object : AboutRepository {
            override suspend fun getAboutInfo(): AboutInfo = defaultAboutInfo
        },
    ): AboutViewModel = AboutViewModel(
        aboutRepository = repository,
        clipboardRepository = clipboard,
        telemetryRepository = telemetryRepository,
        seasonalThemes = seasonalThemes,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun AboutViewModel.onlyMessage(): UiMessage = messages.value.single()

    private val UiMessage.resourceId: Int
        get() = (text as UiTextHelper.StringResource).resourceId

    @Test
    fun `initial load shows the entries`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        val items = viewModel.state.value.items
        assertThat(items).isInstanceOf(Loadable.Ready::class.java)
        assertThat((items as Loadable.Ready).value.map { it.key }).isEqualTo(expectedItemKeys)
    }

    @Test
    fun `copy confirms with the generic message when the action has none`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            advance()

            val message = viewModel.onlyMessage()
            assertThat(message.resourceId).isEqualTo(R.string.snack_copied_to_clipboard)
            assertThat(message.isError).isFalse()
        }

    @Test
    fun `copy confirms with the action's own message when it has one`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(
                AboutEvent.CopyToClipboard(
                    label = "label",
                    text = "device-info",
                    successMessage = UiTextHelper.StringResource(R.string.snack_device_info_copied),
                )
            )
            advance()

            assertThat(viewModel.onlyMessage().resourceId).isEqualTo(R.string.snack_device_info_copied)
        }

    @Test
    fun `copy stays silent when the system confirms copies itself`() =
        runTest(dispatcherExtension.testDispatcher) {
            clipboard.confirmsCopies = true
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            advance()

            assertThat(viewModel.messages.value).isEmpty()
        }

    @Test
    fun `copy failure is reported even when the system confirms copies itself`() =
        runTest(dispatcherExtension.testDispatcher) {
            clipboard.accepts = false
            clipboard.confirmsCopies = true
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            advance()

            val message = viewModel.onlyMessage()
            assertThat(message.resourceId).isEqualTo(R.string.snack_copy_failed)
            assertThat(message.isError).isTrue()
        }

    @Test
    fun `copy failure surfaces the failure message`() =
        runTest(dispatcherExtension.testDispatcher) {
            clipboard.accepts = false
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            advance()

            val message = viewModel.onlyMessage()
            assertThat(message.resourceId).isEqualTo(R.string.snack_copy_failed)
            assertThat(message.isError).isTrue()
            assertThat(telemetryRepository.loggedEvents.map { it.name }).contains("vm_op_error")
        }

    @Test
    fun `copy writes the requested label and text to the clipboard`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(
                AboutEvent.CopyToClipboard(label = "Device info", text = "shown-device-info")
            )
            advance()

            assertThat(clipboard.copies).containsExactly("Device info" to "shown-device-info")
        }

    @Test
    fun `copying one row then another writes both to the clipboard`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "App name", text = "App Toolkit"))
            advance()
            viewModel.onEvent(
                AboutEvent.CopyToClipboard(label = "App Toolkit version", text = "3.0.0-test")
            )
            advance()

            assertThat(clipboard.copies)
                .containsExactly("App name" to "App Toolkit", "App Toolkit version" to "3.0.0-test")
                .inOrder()
        }

    @Test
    fun `a shown message leaves the queue`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
        advance()
        viewModel.messageShown(viewModel.onlyMessage().id)

        assertThat(viewModel.messages.value).isEmpty()
    }

    @Test
    fun `repeated copies each queue their own message`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            advance()

            val ids = viewModel.messages.value.map { it.id }
            assertThat(ids).hasSize(2)
            assertThat(ids.toSet()).hasSize(2)
        }

    @Test
    fun `repository error shows the failure state with a retry`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : AboutRepository {
                override suspend fun getAboutInfo(): AboutInfo = throw IllegalStateException("fail")
            }

            val viewModel = createViewModel(repository = repository)
            advance()

            val items = viewModel.state.value.items as Loadable.Failed
            assertThat((items.message as UiTextHelper.StringResource).resourceId)
                .isEqualTo(R.string.snack_device_info_failed)
            assertThat(items.retryable).isTrue()
            assertThat(telemetryRepository.loggedEvents.map { it.name }).contains("vm_op_error")
        }

    @Test
    fun `a failure the user can act on shows its own text instead of the screen's`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : AboutRepository {
                override suspend fun getAboutInfo(): AboutInfo =
                    throw StorageException(StorageException.Reason.BUSY)
            }

            val viewModel = createViewModel(repository = repository)
            advance()

            val items = viewModel.state.value.items as Loadable.Failed
            assertThat((items.message as UiTextHelper.StringResource).resourceId)
                .isEqualTo(CoreUiR.string.screen_error_storage_busy)
            assertThat(items.retryable).isTrue()
        }

    @Test
    fun `a failure that repeats the same way offers no retry`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : AboutRepository {
                override suspend fun getAboutInfo(): AboutInfo =
                    throw StorageException(StorageException.Reason.CORRUPT)
            }

            val viewModel = createViewModel(repository = repository)
            advance()

            val items = viewModel.state.value.items as Loadable.Failed
            assertThat((items.message as UiTextHelper.StringResource).resourceId)
                .isEqualTo(R.string.snack_device_info_failed)
            assertThat(items.retryable).isFalse()
        }

    @Test
    fun `retrying after a failed load shows the entries`() =
        runTest(dispatcherExtension.testDispatcher) {
            var fail = true
            val repository = object : AboutRepository {
                override suspend fun getAboutInfo(): AboutInfo =
                    if (fail) throw IllegalStateException("fail") else defaultAboutInfo
            }
            val viewModel = createViewModel(repository = repository)
            advance()
            assertThat(viewModel.state.value.items).isInstanceOf(Loadable.Failed::class.java)

            fail = false
            viewModel.onEvent(AboutEvent.Load)
            advance()

            assertThat(viewModel.state.value.items).isInstanceOf(Loadable.Ready::class.java)
        }

    @Test
    fun `the easter egg unlocks seasonal themes and says so the first time`() =
        runTest(dispatcherExtension.testDispatcher) {
            coEvery { seasonalThemes.unlockSeasonalThemes() } returns true
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(AboutEvent.EasterEggFound)
            advance()

            coVerify { seasonalThemes.unlockSeasonalThemes() }
            assertThat(viewModel.onlyMessage().resourceId).isEqualTo(R.string.snack_seasonal_themes_unlocked)
            val achievement = telemetryRepository.loggedEvents.single { it.name == "unlock_achievement" }
            assertThat(achievement.params["achievement_id"]).isEqualTo(AnalyticsValue.Str("seasonal_themes"))
        }

    @Test
    fun `finding the easter egg again stays quiet`() = runTest(dispatcherExtension.testDispatcher) {
        coEvery { seasonalThemes.unlockSeasonalThemes() } returns false
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(AboutEvent.EasterEggFound)
        advance()

        assertThat(viewModel.messages.value).isEmpty()
        assertThat(telemetryRepository.loggedEvents.map { it.name }).doesNotContain("unlock_achievement")
    }

    /** Records each accepted copy; [accepts] and [confirmsCopies] set how the system behaves. */
    private class FakeClipboardRepository(
        var accepts: Boolean = true,
        override var confirmsCopies: Boolean = false,
    ) : ClipboardRepository {
        val copies: MutableList<Pair<String, String>> = mutableListOf()

        override fun copyText(label: String, text: String, isSensitive: Boolean) {
            check(accepts) { "Clipboard rejected the copy for \"$label\"" }
            copies += label to text
        }
    }
}
