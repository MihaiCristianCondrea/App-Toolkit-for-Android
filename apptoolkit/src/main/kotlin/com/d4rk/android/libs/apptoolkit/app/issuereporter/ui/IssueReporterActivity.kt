package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui

import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity

class IssueReporterActivity : BaseActivity() {

    @Composable
    override fun ScreenContent() {
        IssueReporterScreen(
            onBackClicked = {
                this.finish()
            }
        )
    }
}
