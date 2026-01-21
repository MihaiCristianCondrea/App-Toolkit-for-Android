package com.d4rk.android.libs.apptoolkit.core.ui.model

/**
 * Data class representing the version information of an application.
 *
 * @property versionName The user-visible version string (e.g., "1.0.0"). Can be null if not defined.
 * @property versionCode The internal version number used to determine whether one version is more recent than another.
 */
data class AppVersionInfo(
    val versionName: String?,
    val versionCode: Long,
)
