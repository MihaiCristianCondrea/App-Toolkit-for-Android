package com.d4rk.android.libs.apptoolkit.app.about.domain.model

/**
 * Result of a device info copy attempt.
 *
 * @param copied whether the clipboard operation succeeded.
 * @param shouldShowFeedback true when legacy platforms require in-app confirmation because no system
 * clipboard preview is available.
 */
data class CopyDeviceInfoResult(
    val copied: Boolean,
    val shouldShowFeedback: Boolean,
)
