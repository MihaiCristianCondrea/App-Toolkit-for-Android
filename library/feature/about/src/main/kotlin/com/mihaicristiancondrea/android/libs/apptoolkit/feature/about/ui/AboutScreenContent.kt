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

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

/** Taps on the version row that launch the konfetti and report the easter egg. */
private const val EASTER_EGG_TAPS: Int = 5

private val konfettiBurst: Party = Party(
    speed = 0f,
    maxSpeed = 30f,
    damping = 0.9f,
    spread = Spread.ROUND,
    position = Position.Relative(0.5, 0.3),
    emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(amount = 100),
)

private val konfettiRain: Party = Party(
    emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(amount = 60),
    angle = Angle.BOTTOM,
    spread = Spread.SMALL,
    speed = 5f,
    maxSpeed = 15f,
    timeToLive = 3000L,
    position = Position.Relative(x = 0.0, y = 0.0)
        .between(value = Position.Relative(x = 1.0, y = 0.0)),
)

/**
 * Renders the About list for [state] and reports what the user does through callbacks.
 *
 * This is the stateless half of [AboutScreen]: it holds no ViewModel, injects nothing and does not
 * navigate, so it can be previewed and tested with any [AboutUiState]. The only state it keeps is
 * the version-tap counters and the konfetti flag, which are visual and die with the screen.
 *
 * @param state What to render.
 * @param onEvent Receives the events the ViewModel handles: retry, copy and the easter egg.
 * @param onOpenLicenses Invoked by the licenses row. Navigation belongs to the caller.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 * @param onVersionTap Invoked with the cumulative number of taps on the app version row.
 */
@Composable
internal fun AboutScreenContent(
    state: AboutUiState,
    onEvent: (AboutEvent) -> Unit,
    onOpenLicenses: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onVersionTap: (Int) -> Unit = {},
) {
    val context: Context = LocalContext.current
    var showKonfetti: Boolean by rememberSaveable { mutableStateOf(false) }
    var versionTapCount: Int by rememberSaveable { mutableIntStateOf(0) }
    var versionTotalTapCount: Int by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(showKonfetti) {
        if (showKonfetti) {
            delay(3000.milliseconds)
            showKonfetti = false
        }
    }

    fun onPreferenceClick(item: AboutItem.Preference) {
        if (item.countsVersionTap) {
            versionTotalTapCount += 1
            onVersionTap(versionTotalTapCount)
            versionTapCount += 1
            if (versionTapCount >= EASTER_EGG_TAPS) {
                versionTapCount = 0
                showKonfetti = true
                // AboutViewModel reports the achievement, once, when this first unlocks the
                // seasonal themes.
                onEvent(AboutEvent.EasterEggFound)
            }
        }
        when (val action = item.action) {
            is AboutItemAction.CopyToClipboard -> onEvent(
                AboutEvent.CopyToClipboard(
                    label = action.label.asString(context),
                    text = action.text.asString(context),
                    successMessage = action.successMessage,
                )
            )

            AboutItemAction.OpenLicenses -> onOpenLicenses()

            null -> Unit
        }
    }

    Box(modifier = modifier.fillMaxHeight()) {
        ScreenStateHandler(
            state = state.items,
            contentPadding = contentPadding,
            onRetry = { onEvent(AboutEvent.Load) },
            onSuccess = { ready ->
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
                ) {
                    items(
                        items = ready.value,
                        key = { it.key },
                    ) { item ->
                        when (item) {
                            is AboutItem.Header -> PreferenceCategoryItem(title = item.title.asString())

                            is AboutItem.Preference -> SettingsPreferenceItem(
                                title = item.title.asString(),
                                summary = item.summary.asString(),
                                onClick = { onPreferenceClick(item) },
                                ga4Event = item.action?.let {
                                    aboutPreferenceTapEvent(preferenceKey = item.key)
                                },
                                modifier = Modifier.groupedPreferenceItem(
                                    position = item.position,
                                    outerRadius = SizeConstants.LargeMediumSize,
                                ),
                            )
                        }
                    }
                }
            },
        )

        if (showKonfetti) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(konfettiBurst, konfettiRain),
            )
        }
    }
}

private fun aboutPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(ABOUT_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun AboutScreenContentPreview() {
    MaterialTheme {
        AboutScreenContent(
            state = AboutUiState(
                items = Loadable.Ready(
                    persistentListOf(
                        AboutItem.Header(
                            key = AboutItemKey.HEADER_APP_INFO,
                            title = UiTextHelper.DynamicString("App information"),
                        ),
                        AboutItem.Preference(
                            key = AboutItemKey.APP_NAME,
                            title = UiTextHelper.DynamicString("App name"),
                            summary = UiTextHelper.DynamicString("App Toolkit Sample"),
                            position = GroupedItemPosition.FIRST,
                        ),
                        AboutItem.Preference(
                            key = AboutItemKey.APP_BUILD_VERSION,
                            title = UiTextHelper.DynamicString("Version"),
                            summary = UiTextHelper.DynamicString("1.0.0 (100)"),
                            position = GroupedItemPosition.LAST,
                            countsVersionTap = true,
                        ),
                    )
                ),
            ),
            onEvent = {},
            onOpenLicenses = {},
        )
    }
}
