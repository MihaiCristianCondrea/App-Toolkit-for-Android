package com.d4rk.android.libs.apptoolkit.core.utils.extensions

fun String?.normalizeRoute(): String? = this
    ?.substringBefore('?')
    ?.substringBefore('/')
    ?.takeIf { it.isNotBlank() }
