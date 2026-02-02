package com.d4rk.android.libs.apptoolkit.app.review.domain.model

import android.app.Activity

/**
 * Host abstraction for in-app review flows.
 *
 * UI layers should provide an implementation so domain and data layers do not depend on
 * concrete Activity types directly.
 */
interface ReviewHost {
    val activity: Activity
}
