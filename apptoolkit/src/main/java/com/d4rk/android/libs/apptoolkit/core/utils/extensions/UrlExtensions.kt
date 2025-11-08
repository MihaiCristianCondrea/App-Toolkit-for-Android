package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.net.Uri
import androidx.core.net.toUri

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
fun String?.sanitizeUriOrNull(): Uri? = sanitizeUrlOrNull()?.let { url ->
    runCatching { url.toUri() }.getOrNull()
}
