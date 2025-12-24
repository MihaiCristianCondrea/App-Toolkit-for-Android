package com.d4rk.android.libs.apptoolkit.app.about.data.repository

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
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

    override fun copyDeviceInfo(label: String, deviceInfo: String) {
        context.copyTextToClipboard(label = label, text = deviceInfo)
    }
}
