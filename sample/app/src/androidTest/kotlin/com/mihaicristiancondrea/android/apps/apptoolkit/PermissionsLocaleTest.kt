package com.mihaicristiancondrea.android.apps.apptoolkit

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.PermissionsScreen
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import java.util.Locale
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R as PermissionsR

class PermissionsLocaleTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun permissionTextUpdatesWhenLocaleChangesWithoutRecreatingTheScreen() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        fun localized(language: String) = context.createConfigurationContext(
            Configuration(context.resources.configuration).apply { setLocale(Locale.forLanguageTag(language)) },
        )
        val english = localized("en")
        val romanian = localized("ro-RO")
        val englishLabel = english.getString(PermissionsR.string.billing)
        val romanianLabel = romanian.getString(PermissionsR.string.billing)
        assertNotEquals(englishLabel, romanianLabel)
        val currentContext = mutableStateOf(english)

        compose.setContent {
            CompositionLocalProvider(
                LocalContext provides currentContext.value,
                LocalConfiguration provides currentContext.value.resources.configuration,
            ) {
                MaterialTheme { PermissionsScreen() }
            }
        }
        compose.onNodeWithText(englishLabel).assertExists()

        compose.runOnIdle { currentContext.value = romanian }

        compose.onNodeWithText(romanianLabel).assertExists()
        compose.onNodeWithText(englishLabel).assertDoesNotExist()
    }
}
