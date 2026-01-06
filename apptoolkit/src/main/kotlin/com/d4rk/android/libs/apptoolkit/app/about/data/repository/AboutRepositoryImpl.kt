package com.d4rk.android.libs.apptoolkit.app.about.data.repository

import android.content.Context
import android.util.Log
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.logging.CLIPBOARD_HELPER_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.copyTextToClipboard

class AboutRepositoryImpl(
    private val deviceProvider: AboutSettingsProvider,
    private val buildInfoProvider: BuildInfoProvider,
    private val context: Context,
) : AboutRepository {

    override suspend fun getAboutInfo(): AboutInfo =
        AboutInfo(
            appVersion = buildInfoProvider.appVersion,
            appVersionCode = buildInfoProvider.appVersionCode,
            deviceInfo = deviceProvider.deviceInfo,
        )

    override fun copyDeviceInfo(label: String, deviceInfo: String): CopyDeviceInfoResult {
        var shouldShowFeedback = false
        val copied = runCatching {
            context.copyTextToClipboard(
                label = label,
                text = deviceInfo,
                onCopyFallback = { shouldShowFeedback = true }
            )
        }.onFailure { throwable ->
            Log.w(CLIPBOARD_HELPER_LOG_TAG, "Failed to copy device info", throwable)
            shouldShowFeedback = true
        }.getOrDefault(false)
        return CopyDeviceInfoResult(
            copied = copied,
            shouldShowFeedback = shouldShowFeedback
        )
    }
}
