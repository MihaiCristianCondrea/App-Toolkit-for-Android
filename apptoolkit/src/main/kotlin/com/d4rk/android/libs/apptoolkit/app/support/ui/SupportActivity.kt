package com.d4rk.android.libs.apptoolkit.app.support.ui

import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity

class SupportActivity : BaseActivity() {
    @Composable
    override fun ScreenContent() {
        SupportComposable()
    }
}
