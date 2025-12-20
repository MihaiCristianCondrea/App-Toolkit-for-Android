package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.net.Uri
import androidx.core.net.toUri
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments

// TODO: Move to boolean extensions
fun Boolean.toApiEnvironment(): String =
    if (this) ApiEnvironments.ENV_DEBUG else ApiEnvironments.ENV_RELEASE

fun String.developerAppsBaseUrl(
    baseRepositoryUrl: String = ApiConstants.BASE_REPOSITORY_URL,
): String =
    when (lowercase()) {
        ApiEnvironments.ENV_DEBUG -> "$baseRepositoryUrl/${ApiEnvironments.ENV_DEBUG}"
        ApiEnvironments.ENV_RELEASE -> "$baseRepositoryUrl/${ApiEnvironments.ENV_RELEASE}"
        else -> "$baseRepositoryUrl/${ApiEnvironments.ENV_RELEASE}"
    }

/**
 * Sanitizes a URL string by trimming whitespace and returning `null` for blank inputs.
 */
fun String?.sanitizeUrlOrNull(): String? {
    if (this == null) return null
    val sanitized = trim()
    return sanitized.takeUnless { it.isEmpty() }
}

/**
 * Produces a [Uri] from a sanitized string, or `null` when the input is blank or invalid.
 */
fun String?.sanitizeUriOrNull(): Uri? =
    sanitizeUrlOrNull()
        ?.let { url ->
            runCatching { url.toUri() }
                .getOrNull()
                ?.takeIf { uri -> !uri.scheme.isNullOrBlank() }
        }

fun String?.normalizeRoute(): String? = this
    ?.substringBefore('?')
    ?.substringBefore('/')
    ?.takeIf { it.isNotBlank() }
