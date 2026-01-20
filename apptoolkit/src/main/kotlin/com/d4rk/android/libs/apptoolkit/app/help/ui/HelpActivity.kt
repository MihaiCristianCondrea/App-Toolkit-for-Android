package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import org.koin.android.ext.android.inject

class HelpActivity : BaseActivity() {
    private val config: AppVersionInfo by inject()

    @Composable
    override fun ScreenContent() {
        HelpScreen(config = config)
    }
}
