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

package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.d4rk.android.libs.apptoolkit.R
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class UiTextHelperInstrumentationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun asString_uses_updated_configuration() {
        val baseContext = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration(baseContext.resources.configuration)
        config.setLocale(Locale.FRANCE)
        val localizedContext = baseContext.createConfigurationContext(config)

        val expected = localizedContext.getString(R.string.welcome)
        var actual: String? = null

        composeRule.setContent {
            CompositionLocalProvider(LocalContext provides localizedContext) {
                actual = UiTextHelper.StringResource(R.string.welcome).asString()
            }
        }

        composeRule.runOnIdle {
            assertThat(actual).isEqualTo(expected)
        }
    }
}
