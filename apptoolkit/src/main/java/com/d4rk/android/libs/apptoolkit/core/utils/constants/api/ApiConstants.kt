package com.d4rk.android.libs.apptoolkit.core.utils.constants.api

import com.d4rk.android.libs.apptoolkit.core.utils.constants.github.GithubConstants

object ApiHost {
    const val API_REPO: String = "com.d4rk.apis"
    const val API_BASE_PATH: String = "api/app_toolkit"
}

object ApiVersions {
    const val V2: String = "v2"
}

object ApiEnvironments {
    const val ENV_DEBUG: String = "debug"
    const val ENV_RELEASE: String = "release"

    // TODO: move to separate file helper
    fun fromBuildType(isDebugBuild: Boolean): String =
        if (isDebugBuild) ENV_DEBUG else ENV_RELEASE
}

object ApiPaths {
    const val DEVELOPER_APPS_API: String = "/en/home/api_android_apps.json"
}

object ApiConstants {
    const val BASE_REPOSITORY_URL: String =
        "${GithubConstants.GITHUB_PAGES}/${ApiHost.API_REPO}/${ApiHost.API_BASE_PATH}/${ApiVersions.V2}"

    // TODO: Move to separate file helper
    fun developerAppsBaseUrl(environment: String): String =
        when (environment.lowercase()) {
            ApiEnvironments.ENV_DEBUG -> "$BASE_REPOSITORY_URL/${ApiEnvironments.ENV_DEBUG}"
            ApiEnvironments.ENV_RELEASE -> "$BASE_REPOSITORY_URL/${ApiEnvironments.ENV_RELEASE}"
            else -> "$BASE_REPOSITORY_URL/${ApiEnvironments.ENV_RELEASE}"
        }
}
