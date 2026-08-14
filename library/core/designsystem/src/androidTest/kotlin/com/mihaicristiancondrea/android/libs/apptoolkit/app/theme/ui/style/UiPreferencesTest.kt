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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.junit.Rule
import org.junit.Test

class UiPreferencesTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun defaultsAreSafeWithoutThemeProvider() {
        composeRule.setContent {
            PreferenceValues()
        }

        composeRule.onNodeWithTag(VALUES_TAG).assertTextEquals("true,true,false")
    }

    @Test
    fun providerSuppliesAllUiPreferences() {
        composeRule.setContent {
            CompositionLocalProvider(
                LocalBouncyAnimationsEnabled provides false,
                LocalShowBottomBarLabels provides false,
                LocalAdsEnabled provides true,
            ) {
                PreferenceValues()
            }
        }

        composeRule.onNodeWithTag(VALUES_TAG).assertTextEquals("false,false,true")
    }

    private companion object {
        const val VALUES_TAG = "ui_preferences"
    }
}

@androidx.compose.runtime.Composable
private fun PreferenceValues() {
    Text(
        text = buildString {
            append(LocalBouncyAnimationsEnabled.current)
            append(',')
            append(LocalShowBottomBarLabels.current)
            append(',')
            append(LocalAdsEnabled.current)
        },
        modifier = Modifier.testTag("ui_preferences"),
    )
}
