package com.d4rk.android.libs.apptoolkit.app.about.data.repository

import android.content.Context
import android.os.Build
import android.util.Log
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.logging.CLIPBOARD_HELPER_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.copyTextToClipboard

/**
 * Provides About-related data and handles clipboard interactions for device info.
 *
 * @param sdkIntProvider Supplies the current SDK version for clipboard feedback decisions.
 */
class AboutRepositoryImpl(
    private val deviceProvider: AboutSettingsProvider,
    private val buildInfoProvider: BuildInfoProvider,
    private val context: Context,
    private val firebaseController: FirebaseController,
    private val sdkIntProvider: () -> Int = { Build.VERSION.SDK_INT },
) : AboutRepository {

    override suspend fun getAboutInfo(): AboutInfo =
        AboutInfo(
            appVersion = buildInfoProvider.appVersion,
            appVersionCode = buildInfoProvider.appVersionCode,
            deviceInfo = deviceProvider.deviceInfo,
        )

    override fun copyDeviceInfo(label: String, deviceInfo: String): CopyDeviceInfoResult {
        firebaseController.logBreadcrumb(
            message = "Copy device info requested",
            attributes = mapOf("label" to label),
        )
        val allowFeedback = sdkIntProvider() <= Build.VERSION_CODES.S_V2
        var shouldShowFeedback = false
        val copied = runCatching {
            context.copyTextToClipboard(
                label = label,
                text = deviceInfo,
                onCopyFallback = {
                    if (allowFeedback) {
                        shouldShowFeedback = true
                    }
                }
            )
        }.onFailure { throwable ->
            Log.w(CLIPBOARD_HELPER_LOG_TAG, "Failed to copy device info", throwable)
            if (allowFeedback) {
                shouldShowFeedback = true
            }
        }.getOrDefault(false)
        return CopyDeviceInfoResult(
            copied = copied,
            shouldShowFeedback = shouldShowFeedback
        )
    }
}
