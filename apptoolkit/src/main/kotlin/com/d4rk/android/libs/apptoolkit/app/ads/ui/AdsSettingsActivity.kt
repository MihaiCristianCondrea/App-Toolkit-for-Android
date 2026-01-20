package com.d4rk.android.libs.apptoolkit.app.ads.ui

import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity

/**
 * An activity that displays advertisement-related settings to the user.
 *
 * This activity hosts the [AdsSettingsScreen] composable, which provides the user interface
 * for managing ad preferences, such as consenting to personalized ads or disabling them.
 */
class AdsSettingsActivity : BaseActivity() {

    @Composable
    override fun ScreenContent() {
        AdsSettingsScreen()
    }
}
