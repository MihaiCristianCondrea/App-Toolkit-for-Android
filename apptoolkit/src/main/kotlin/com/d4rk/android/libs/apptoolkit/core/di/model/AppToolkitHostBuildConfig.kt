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

package com.d4rk.android.libs.apptoolkit.core.di.model

/**
 * Host-owned build/runtime values required by AppToolkit DI modules.
 *
 * The toolkit is published as a library, so it cannot rely on a host app's generated BuildConfig directly.
 * Passing these values explicitly keeps module boundaries clean while still allowing host-specific behavior.
 */
data class AppToolkitHostBuildConfig(
    val applicationId: String,
    val isDebugBuild: Boolean,
    val versionName: String,
    val versionCode: Long,
    val githubToken: String,
    val faqProductId: String,
    val githubRepository: String = "App-Toolkit-for-Android",
)
