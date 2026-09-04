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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.settings

import android.content.Context
import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider

/**
 * Formats sample build/device metadata for the reusable About screen.
 *
 * Build type comes from [hostBuildConfig], not this library module's `BuildConfig`, so the label
 * describes the final application variant.
 */
class AppAboutSettingsProvider(
    val context: Context,
    private val hostBuildConfig: AppToolkitHostBuildConfig,
) : AboutSettingsProvider {
    override val deviceInfo: String
        get() {
            return context.getString(
                com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.app_build,
                "${context.getString(com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.manufacturer)} ${Build.MANUFACTURER}",
                "${context.getString(com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.device_model)} ${Build.MODEL}",
                "${context.getString(com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.android_version)} ${Build.VERSION.RELEASE}",
                "${context.getString(com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.api_level)} ${Build.VERSION.SDK_INT}",
                "${context.getString(com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.arch)} ${Build.SUPPORTED_ABIS.joinToString()}",
                if (hostBuildConfig.isDebugBuild) context.getString(com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.debug) else context.getString(
                    com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.release
                )
            )
        }
}
