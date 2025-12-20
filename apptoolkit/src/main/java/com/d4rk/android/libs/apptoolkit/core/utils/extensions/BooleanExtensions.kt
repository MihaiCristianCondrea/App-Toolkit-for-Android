package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments

/**
 * Maps a debug flag to the corresponding API environment name.
 */
fun Boolean.toApiEnvironment(): String =
    if (this) ApiEnvironments.ENV_DEBUG else ApiEnvironments.ENV_RELEASE
