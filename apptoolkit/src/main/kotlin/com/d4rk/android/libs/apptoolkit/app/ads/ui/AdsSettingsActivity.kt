package com.d4rk.android.libs.apptoolkit.app.ads.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.AppTheme

/**
 * An activity that displays advertisement-related settings to the user.
 *
 * This activity hosts the [AdsSettingsScreen] composable, which provides the user interface
 * for managing ad preferences, such as consenting to personalized ads or disabling them.
 */
class AdsSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AdsSettingsScreen()
            }
        }
    }
}
