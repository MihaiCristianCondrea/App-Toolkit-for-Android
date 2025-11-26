package com.d4rk.android.libs.apptoolkit.app.main.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

/**
 * Helper object for performing in-app updates using the Play Core library.
 */
object InAppUpdateHelper {

    /**
     * Checks for available updates and attempts to start the update flow if an update is found.
     *
     * @param appUpdateManager The [AppUpdateManager] instance used to query and start updates.
     * @param updateResultLauncher Launcher used to start the update flow.
     */
    fun performUpdate(
        appUpdateManager: AppUpdateManager,
        updateResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
    ) {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                val updateAvailability = appUpdateInfo.updateAvailability()
                val isImmediateAllowed = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

                when (updateAvailability) {
                    UpdateAvailability.UPDATE_AVAILABLE if isImmediateAllowed
                        -> {
                        startImmediateUpdate(appUpdateManager, appUpdateInfo, updateResultLauncher)
                    }

                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS if isImmediateAllowed
                        -> {
                        startImmediateUpdate(appUpdateManager, appUpdateInfo, updateResultLauncher)
                    }

                    else -> {}
                }
            }
    }

    private fun startImmediateUpdate(
        appUpdateManager: AppUpdateManager,
        appUpdateInfo: AppUpdateInfo,
        updateResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
    ) {
        val appUpdateOptions: AppUpdateOptions =
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()

        runCatching {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateResultLauncher,
                appUpdateOptions,
            )
        }
    }
}
