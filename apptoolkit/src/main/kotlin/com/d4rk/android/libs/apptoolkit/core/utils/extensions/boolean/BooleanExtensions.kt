package com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean

import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Maps a debug flag to the corresponding API environment name.
 */
fun Boolean.toApiEnvironment(): String =
    if (this) ApiEnvironments.ENV_DEBUG else ApiEnvironments.ENV_RELEASE

fun Boolean.asConsentStatus(): FirebaseAnalytics.ConsentStatus =
    if (this) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED