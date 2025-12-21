package com.d4rk.android.libs.apptoolkit.app.startup.utils.interfaces.providers

import android.content.Context
import android.content.Intent

interface StartupProvider {

    /** Which runtime permissions (if any) should we request? */
    val requiredPermissions: Array<String>

    /** Once everything’s done, where do we go? */
    fun getNextIntent(context: Context): Intent
}
