package com.d4rk.android.apps.apptoolkit.app.main.ui

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateHost
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost

/**
 * Builds GMS host abstractions for in-app review and update flows.
 */
class GmsHostFactory {
    fun createReviewHost(activity: Activity): ReviewHost {
        return object : ReviewHost {
            override val activity: Activity = activity
        }
    }

    fun createUpdateHost(
        activity: Activity,
        launcher: ActivityResultLauncher<IntentSenderRequest>,
    ): InAppUpdateHost {
        return object : InAppUpdateHost {
            override val activity: Activity = activity
            override val updateResultLauncher: ActivityResultLauncher<IntentSenderRequest> = launcher
        }
    }
}
