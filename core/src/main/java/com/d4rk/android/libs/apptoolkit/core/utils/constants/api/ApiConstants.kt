/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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

object ApiLanguages {
    const val DEFAULT: String = "en"
}

object ApiPaths {
    const val DEVELOPER_APPS_API: String = "home/api_android_apps.json"
}

object ApiConstants {
    const val BASE_REPOSITORY_URL: String =
        "${GithubConstants.GITHUB_PAGES}/${ApiHost.API_REPO}/${ApiHost.API_BASE_PATH}/${ApiVersions.V2}"
}
