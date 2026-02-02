package com.d4rk.android.libs.apptoolkit.app.main.domain.model

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import android.app.Activity

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
