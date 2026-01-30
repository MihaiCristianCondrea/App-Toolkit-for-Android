package com.d4rk.android.libs.apptoolkit.app.consent.domain.model

import android.app.Activity

/**
 * Host abstraction for consent dialogs.
 *
 * UI layers should provide an implementation so domain and data layers do not depend on
 * concrete Activity types directly.
 */
interface ConsentHost {
    val activity: Activity
}
