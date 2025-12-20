package com.d4rk.android.libs.apptoolkit.core.utils.constants.help

object HelpConstants {
    const val FAQ_PRODUCT_ID: String = "com.d4rk.toolkit"
    const val FAQ_BASE_URL: String =
        "https://mihaicristiancondrea.github.io/com.d4rk.apis/api/faq/v1"

    // TODO: Move to an object helper
    fun faqCatalogUrl(isDebugBuild: Boolean): String {
        val catalogEnvironment = if (isDebugBuild) "debug" else "release"
        return "$FAQ_BASE_URL/$catalogEnvironment/catalog.json"
    }
}
