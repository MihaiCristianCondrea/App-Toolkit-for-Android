package com.d4rk.android.libs.apptoolkit.app.help.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.AppTheme
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import org.koin.android.ext.android.inject

class HelpActivity : AppCompatActivity() {
    private val config: AppVersionInfo by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                HelpScreen(config = config)
            }
        }
    }
}
