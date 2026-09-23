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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.mappers.toUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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

    private val expectedItemKeys: List<String> = defaultAboutInfo.toUiState().items.map { it.key }

    private val firebaseController = FakeFirebaseController()

    private val seasonalThemes: SeasonalThemeRepository = mockk(relaxed = true)

    private lateinit var context: Context
    private lateinit var clipboardManager: ClipboardManager

    @BeforeEach
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.w(any(), any<String>(), any()) } returns 0
        mockkStatic(ClipData::class)
        every { ClipData.newPlainText(any(), any()) } returns mockk(relaxed = true)
        clipboardManager = mockk()
        justRun { clipboardManager.setPrimaryClip(any()) }
        context = mockk()
        every { context.getSystemService(ClipboardManager::class.java) } returns clipboardManager
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ClipData::class)
        unmockkStatic(Log::class)
    }

    private fun createViewModel(
        testDispatcher: TestDispatcher = dispatcherExtension.testDispatcher,
        repository: AboutRepository = object : AboutRepository {
            override suspend fun getAboutInfo(): AboutInfo = defaultAboutInfo
        },
        clipboardContext: Context = context,
        sdkInt: Int = Build.VERSION_CODES.S_V2,
    ): AboutViewModel {
        val testDispatchers: DispatcherProvider = TestDispatchers(testDispatcher)

        return AboutViewModel(
            aboutRepository = repository,
            context = clipboardContext,
            dispatchers = testDispatchers,
            firebaseController = firebaseController,
            seasonalThemes = seasonalThemes,
            sdkIntProvider = { sdkInt },
        )
    }

    @Test
    fun `initial load populates ui state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.data?.items?.map { it.key }).isEqualTo(expectedItemKeys)
    }

    @Test
    fun `copy confirms with the generic message when the action has none`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            val snackbar = viewModel.uiState.value.snackbar!!
            val message = snackbar.message as UiTextHelper.StringResource
            assertThat(message.resourceId).isEqualTo(R.string.snack_copied_to_clipboard)
            assertThat(snackbar.isError).isFalse()
        }

    @Test
    fun `copy confirms with the action's own message when it has one`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(
                AboutEvent.CopyToClipboard(
                    label = "label",
                    text = "device-info",
                    successMessage = UiTextHelper.StringResource(
                        R.string.snack_device_info_copied,
                    ),
                )
            )
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            val message = viewModel.uiState.value.snackbar!!.message as UiTextHelper.StringResource
            assertThat(message.resourceId).isEqualTo(R.string.snack_device_info_copied)
        }

    @Test
    fun `copy stays silent when the platform shows its own clipboard preview`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(sdkInt = Build.VERSION_CODES.TIRAMISU)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.uiState.value.snackbar).isNull()
        }

    @Test
    fun `copy failure is reported even when the platform shows a clipboard preview`() =
        runTest(dispatcherExtension.testDispatcher) {
            every { context.getSystemService(ClipboardManager::class.java) } returns null

            val viewModel = createViewModel(sdkInt = Build.VERSION_CODES.TIRAMISU)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            val snackbar = viewModel.uiState.value.snackbar!!
            val message = snackbar.message as UiTextHelper.StringResource
            assertThat(message.resourceId).isEqualTo(R.string.snack_copy_failed)
            assertThat(snackbar.isError).isTrue()
        }

    @Test
    fun `copy failure surfaces the failure message`() =
        runTest(dispatcherExtension.testDispatcher) {
            every { context.getSystemService(ClipboardManager::class.java) } returns null

            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            val snackbar = viewModel.uiState.value.snackbar!!
            val message = snackbar.message as UiTextHelper.StringResource
            assertThat(message.resourceId).isEqualTo(R.string.snack_copy_failed)
            assertThat(snackbar.isError).isTrue()
        }

    @Test
    fun `copy writes the requested label and text to the clipboard`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(
                AboutEvent.CopyToClipboard(label = "Device info", text = "shown-device-info")
            )
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            verify { ClipData.newPlainText("Device info", "shown-device-info") }
        }

    @Test
    fun `copying one row then another writes both to the clipboard`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "App name", text = "App Toolkit"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(
                AboutEvent.CopyToClipboard(label = "App Toolkit version", text = "3.0.0-test")
            )
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            verify { ClipData.newPlainText("App name", "App Toolkit") }
            verify { ClipData.newPlainText("App Toolkit version", "3.0.0-test") }
        }

    @Test
    fun `copying one row then another still writes both when the platform previews clipboard`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(sdkInt = Build.VERSION_CODES.TIRAMISU)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "App name", text = "App Toolkit"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(
                AboutEvent.CopyToClipboard(label = "Play services", text = "24.01.12")
            )
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            verify { ClipData.newPlainText("App name", "App Toolkit") }
            verify { ClipData.newPlainText("Play services", "24.01.12") }
        }

    @Test
    fun `dismiss snackbar resets state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        viewModel.onEvent(AboutEvent.DismissSnackbar)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNull()
    }

    @Test
    fun `repeated copy events replace the snackbar`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            val first = viewModel.uiState.value.snackbar!!.timeStamp

            viewModel.onEvent(AboutEvent.CopyToClipboard(label = "label", text = "text"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            val second = viewModel.uiState.value.snackbar!!.timeStamp

            assertThat(second).isNotEqualTo(first)
        }

    @Test
    fun `repository error shows the load failure snackbar`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : AboutRepository {
                override suspend fun getAboutInfo(): AboutInfo = throw IllegalStateException("fail")
            }

            val viewModel = createViewModel(repository = repository)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            val message = viewModel.uiState.value.snackbar?.message as? UiTextHelper.StringResource
            assertThat(message?.resourceId).isEqualTo(R.string.snack_device_info_failed)
        }

    @Test
    fun `the easter egg unlocks seasonal themes and says so the first time`() =
        runTest(dispatcherExtension.testDispatcher) {
            coEvery { seasonalThemes.unlockSeasonalThemes() } returns true
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.EasterEggFound)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            coVerify { seasonalThemes.unlockSeasonalThemes() }
            val message = viewModel.uiState.value.snackbar!!.message as UiTextHelper.StringResource
            assertThat(message.resourceId).isEqualTo(R.string.snack_seasonal_themes_unlocked)
            val achievement = firebaseController.loggedEvents
                .single { it.name == "unlock_achievement" }
            assertThat(achievement.params["achievement_id"])
                .isEqualTo(AnalyticsValue.Str("seasonal_themes"))
        }

    @Test
    fun `finding the easter egg again stays quiet`() = runTest(dispatcherExtension.testDispatcher) {
        coEvery { seasonalThemes.unlockSeasonalThemes() } returns false
        val viewModel = createViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AboutEvent.EasterEggFound)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.snackbar).isNull()
        assertThat(firebaseController.loggedEvents.map { it.name })
            .doesNotContain("unlock_achievement")
    }
}
