package com.d4rk.android.libs.apptoolkit.app.main.data.repository

import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateHost
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateResult
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.InAppUpdateRepository
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Data-layer implementation for Play Core in-app updates.
 */
class InAppUpdateRepositoryImpl : InAppUpdateRepository {
    override fun requestUpdate(host: InAppUpdateHost): Flow<InAppUpdateResult> = callbackFlow {
        val appUpdateManager = AppUpdateManagerFactory.create(host.activity)

        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                val updateAvailability = appUpdateInfo.updateAvailability()
                val isImmediateAllowed =
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

                when (updateAvailability) {
                    UpdateAvailability.UPDATE_AVAILABLE,
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                        if (isImmediateAllowed) {
                            val updateOptions =
                                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                            val started = try {
                                appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    host.updateResultLauncher,
                                    updateOptions,
                                )
                                true
                            } catch (_: Exception) {
                                false
                            }
                            trySend(
                                if (started) {
                                    InAppUpdateResult.Started
                                } else {
                                    InAppUpdateResult.Failed
                                }
                            )
                        } else {
                            trySend(InAppUpdateResult.NotAllowed)
                        }
                    }

                    else -> trySend(InAppUpdateResult.NotAvailable)
                }
                close()
            }
            .addOnFailureListener {
                trySend(InAppUpdateResult.Failed)
                close()
            }

        awaitClose()
    }
}
