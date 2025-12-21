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
}

object ApiPaths {
    const val DEVELOPER_APPS_API: String = "/en/home/api_android_apps.json"
}

object ApiConstants {
    const val BASE_REPOSITORY_URL: String =
        "${GithubConstants.GITHUB_PAGES}/${ApiHost.API_REPO}/${ApiHost.API_BASE_PATH}/${ApiVersions.V2}"
}
