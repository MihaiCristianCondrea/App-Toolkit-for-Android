package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import kotlin.io.encoding.Base64

object Base64Decoder {

    // Move it to string extensions
    fun parseBase64String(encodedToken: String): String {
        return runCatching {
            val decodedBytes = Base64.decode(encodedToken)
            String(decodedBytes, Charsets.UTF_8)
        }.getOrDefault("")
    }
}