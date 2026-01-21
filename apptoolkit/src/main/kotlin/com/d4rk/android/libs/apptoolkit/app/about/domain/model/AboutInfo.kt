package com.d4rk.android.libs.apptoolkit.app.about.domain.model

/**
 * Data class representing basic application and device information.
 *
 * @property appVersion The human-readable version name of the application.
 * @property appVersionCode The internal version code of the application.
 * @property deviceInfo A string containing relevant hardware and software specifications of the device.
 */
data class AboutInfo(
    val appVersion: String,
    val appVersionCode: Int,
    val deviceInfo: String,
)
