package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.d4rk.android.libs.apptoolkit.core.utils.constants.help.HelpConstants

object HelpUrlHelper {
    fun faqCatalogUrl(
        baseUrl: String = HelpConstants.FAQ_BASE_URL,
        isDebugBuild: Boolean,
    ): String {
        val catalogEnvironment = ApiEnvironments.fromBuildType(isDebugBuild)
        return "$baseUrl/$catalogEnvironment/catalog.json"
    }
}
