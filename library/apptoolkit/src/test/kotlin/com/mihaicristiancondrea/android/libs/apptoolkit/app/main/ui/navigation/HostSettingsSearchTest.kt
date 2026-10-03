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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.settingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.di.displaySettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.di.privacyModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.di.settingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.ShellHost
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import io.mockk.mockk
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.compose.KoinIsolatedContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.robolectric.annotation.Config
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R as DeveloperR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R as DisplayR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.R as PrivacyR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.R as SupportR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

/**
 * An app's own settings page in the Toolkit's settings search, declared the way a host declares
 * it: a `settingsSearchProvider` bound in Koin under a name of its own, beside the Toolkit's
 * privacy page. The host page's labels reuse Toolkit strings, since a library's unit tests have no
 * resources of their own.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class HostSettingsSearchTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private var notificationActions = 0
    private lateinit var koinApplication: KoinApplication

    private val hostSearch: SettingsSearchProvider = settingsSearchProvider(
        section = SupportR.string.paid_support,
        destination = HostPage,
    ) {
        preference(SupportR.string.web_ad, summary = SupportR.string.summary_donations)
        preference(SupportR.string.non_paid_support, destination = HostSubpage)
        preference(SupportR.string.purchase_cancelled, destination = UnregisteredPage)
    }

    @Before
    fun setUp() {
        koinApplication = startKoin {
            modules(
                module {
                    single { CommonDataStore(context) }
                    single<TelemetryRepository> { mockk(relaxed = true) }
                    single<SettingsProvider> { HostSettingsProvider { notificationActions++ } }
                    single<SettingsSearchProvider>(named("host")) { hostSearch }
                },
                settingsModule,
                privacyModule,
                displaySettingsModule,
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `a host page's row is found and opens the host page`() {
        openSettings()
        search("web ad")

        compose.onNodeWithText(context.getString(SupportR.string.web_ad)).performClick()
        compose.waitForIdle()

        compose.onNodeWithText("Host page body").assertExists()
    }

    @Test
    fun `a host row that names its own page opens that page`() {
        openSettings()
        search("non-paid")

        compose.onNodeWithText(context.getString(SupportR.string.non_paid_support)).performClick()
        compose.waitForIdle()

        compose.onNodeWithText("Host subpage body").assertExists()
    }

    @Test
    fun `host and Toolkit rows are found together`() {
        openSettings()
        search("ad")

        compose.onNodeWithText(context.getString(SupportR.string.web_ad)).assertExists()
        compose.onNodeWithText(context.getString(PrivacyR.string.ads)).assertExists()
    }

    @Test
    fun `a host row whose page is not registered is not offered`() {
        openSettings()
        search("cancelled")

        compose.onAllNodesWithText(context.getString(SupportR.string.purchase_cancelled)).assertCountEquals(0)
    }

    @Test
    fun `a root row's action still runs from search when its fallback page is not registered`() {
        openSettings()
        search("alerts")

        compose.onNodeWithText("Alerts").performClick()
        compose.waitForIdle()

        assertEquals(1, notificationActions)
        compose.onNodeWithText("Alerts").assertExists()
    }

    @Test
    fun `app B, one tab, finds the display rows its page shows and none it hides`() {
        openSettings()

        search("language")
        compose.onNodeWithText(context.getString(DisplayR.string.language)).assertExists()

        val labels = context.getString(DisplayR.string.show_labels_on_bottom_bar)
        search(labels.lowercase())
        compose.onAllNodesWithText(labels).assertCountEquals(0)
    }

    @Test
    fun `the shell overrides moved to the developer options are not searchable`() {
        openSettings()

        for (title in listOf(DeveloperR.string.shell_top_bar, DeveloperR.string.shell_hide_top_bar, DeveloperR.string.shell_back_edge)) {
            val text = context.getString(title)
            search(text.lowercase())
            compose.onAllNodesWithText(text).assertCountEquals(0)
        }
    }

    private fun openSettings() {
        compose.setContent {
            // Each test starts a new Koin instance; Compose must not reuse a closed default context.
            KoinIsolatedContext(koinApplication) {
                AppTheme {
                    ShellHost(graph = HostGraph, preferences = InMemoryShellPreferences())
                }
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Open settings").fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("Open settings").performClick()
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun search(query: String) {
        compose.onNode(hasSetTextAction()).performTextReplacement("")
        compose.onNode(hasSetTextAction()).performTextInput(query)
        compose.waitForIdle()
    }
}

/** The host's root settings: one row whose action handles the click before a page that is not registered. */
private class HostSettingsProvider(private val onNotifications: () -> Unit) : SettingsProvider {
    override fun provideSettingsConfig() = SettingsConfig(
        categories = listOf(
            SettingsCategory(
                preferences = listOf(
                    SettingsPreference(
                        key = "alerts",
                        title = "Alerts",
                        destination = UnregisteredPage,
                        action = {
                            onNotifications()
                            true
                        },
                    ),
                ),
            ),
        ),
    )
}

// Navigation 3 discovers object serializers by reflection, which requires JVM-visible route classes.
@Serializable
internal data object HostHome : NavKey

@Serializable
internal data object HostPage : NavKey

@Serializable
internal data object HostSubpage : NavKey

@Serializable
internal data object UnregisteredPage : NavKey

private val HostGraph = toolkitGraph(appTitle = CommonR.string.app_name) {
    tab(HostHome, NavigationR.string.updates, ToolkitIcon.Vector(Icons.Outlined.Home)) {
        val navigator = LocalShellNavigator.current
        Text("Open settings", Modifier.clickable { navigator.navigate(SettingsRoute) }.padding(16.dp))
    }
    page<HostPage>(paneRole = PaneRole.Detail, title = { "Host page" }) { Text("Host page body") }
    page<HostSubpage>(paneRole = PaneRole.Detail, title = { "Host subpage" }) { Text("Host subpage body") }
}
