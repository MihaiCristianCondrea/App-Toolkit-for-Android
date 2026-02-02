package com.d4rk.android.libs.apptoolkit.app.main.domain.model

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest

/**
 * Host abstraction for in-app update flows.
 *
 * UI layers should provide an implementation so domain and data layers do not depend on
 * concrete Activity types directly.
 */
interface InAppUpdateHost {
    val activity: Activity
    val updateResultLauncher: ActivityResultLauncher<IntentSenderRequest>
}
