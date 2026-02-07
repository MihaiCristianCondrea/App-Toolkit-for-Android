package com.d4rk.android.libs.apptoolkit.core.ui.views.analytics

import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData

/**
 * Logs a GA4 event for reusable UI components when both the controller and event data exist.
 */
fun FirebaseController?.logGa4Event(ga4Event: Ga4EventData?) {
    if (this == null || ga4Event == null) return
    logEvent(ga4Event.toAnalyticsEvent())
}
