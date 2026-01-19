package com.d4rk.android.libs.apptoolkit.core.ui.base

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.AppTheme

/**
 * Activity that provides common Compose setup for cleanup screens.
 */
abstract class BaseCleanupActivity :
    AppCompatActivity() { // TODO: Make the other screens rely on this activity

    /**
     * Compose content to be rendered by this activity.
     */
    @Composable
    protected abstract fun ScreenContent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                ScreenContent()
            }
        }
    }
}
