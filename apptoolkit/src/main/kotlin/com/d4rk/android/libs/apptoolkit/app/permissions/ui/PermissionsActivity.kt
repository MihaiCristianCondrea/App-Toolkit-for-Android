package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity

/** Hosts the permissions screen. */
class PermissionsActivity : BaseActivity() {

    @Composable
    override fun ScreenContent() {
        PermissionsScreen()
    }
}
