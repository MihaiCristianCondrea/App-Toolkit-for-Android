package com.d4rk.android.libs.apptoolkit.app.licenses.ui

import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity

class LicensesActivity : BaseActivity() {
    @Composable
    override fun ScreenContent() {
        LicensesScreen(
            onBackClicked = {
                this.finish()
            }
        )
    }
}
