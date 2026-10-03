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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.BottomAppBarNativeAdBanner
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.robolectric.annotation.Config

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/** Exercises the ad defaults used by scaffold content without an initialized ads SDK. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class NativeAdPlacementTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @After
    fun tearDownKoin() {
        stopKoin()
    }

    @Test
    fun bottom_banner_requires_opt_in_without_resolving_ad_dependencies() {
        val visibility = mutableListOf<Boolean>()
        compose.setContent {
            BottomAppBarNativeAdBanner(
                modifier = Modifier.testTag("native-banner"),
                adUnitId = "unused-unit-id",
                onAdLoaded = visibility::add,
            )
        }

        compose.onNodeWithTag("native-banner").assertDoesNotExist()
        compose.runOnIdle { assertEquals(listOf(false), visibility) }
    }

    @Test
    fun opting_in_renders_the_preview_and_disabling_removes_it() {
        val enabled = mutableStateOf(true)
        val visibility = mutableListOf<Boolean>()
        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    BottomAppBarNativeAdBanner(
                        modifier = Modifier.testTag("native-banner"),
                        adUnitId = "preview-unit-id",
                        enabled = enabled.value,
                        onAdLoaded = visibility::add,
                    )
                }
            }
        }

        compose.onNodeWithTag("native-banner").assertIsDisplayed()
        compose.runOnIdle { enabled.value = false }
        compose.onNodeWithTag("native-banner").assertDoesNotExist()
        compose.runOnIdle { assertEquals(listOf(false), visibility) }
    }

    @Test
    fun the_empty_state_keeps_its_native_ad_enabled_by_default() {
        startKoin {
            modules(module {
                single<AdsConfig>(named(AdsQualifiers.NO_DATA_NATIVE_AD)) {
                    AdsConfig(bannerAdUnitId = "preview-unit-id")
                }
            })
        }
        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    NoDataScreen(message = "No content")
                }
            }
        }

        compose.onNodeWithText(compose.activity.getString(CoreUiR.string.sponsored_ad_label_plain))
            .assertIsDisplayed()
    }
}
