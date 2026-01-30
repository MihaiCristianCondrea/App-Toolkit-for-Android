package com.d4rk.android.libs.apptoolkit.core.utils

import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

/**
 * No-op Firebase controller for unit tests.
 */
class FakeFirebaseController : FirebaseController {
    override fun updateConsent(
        analyticsGranted: Boolean,
        adStorageGranted: Boolean,
        adUserDataGranted: Boolean,
        adPersonalizationGranted: Boolean,
    ) {
        // no-op
    }

    override fun setAnalyticsEnabled(enabled: Boolean) {
        // no-op
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        // no-op
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        // no-op
    }

    override fun logBreadcrumb(message: String, attributes: Map<String, String>) {
        // no-op
    }

    override fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String>,
    ) {
        // no-op
    }
}
