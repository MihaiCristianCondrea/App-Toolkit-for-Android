package com.d4rk.android.apps.apptoolkit.core.utils.extensions

import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper

/**
 * App-specific overrides for mapping [AppErrors] to UI text.
 *
 * The app owns the messaging for its own data fetch flows, while delegating to the shared
 * library mapping for everything else.
 */
fun AppErrors.toErrorMessage(): UiTextHelper {
    return when (this) {
        AppErrors.UseCase.FAILED_TO_LOAD_APPS -> UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
        else -> this.asShared()?.asUiText() ?: UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
    }
}

private fun AppErrors.asShared(): Errors? {
    return when (this) {
        is AppErrors.Common -> value
        else -> null
    }
}
