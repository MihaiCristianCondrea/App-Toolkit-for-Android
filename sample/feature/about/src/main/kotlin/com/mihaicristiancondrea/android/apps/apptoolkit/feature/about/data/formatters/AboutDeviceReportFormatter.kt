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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.about.data.formatters

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R

internal fun formatAboutDeviceReport(
    context: Context,
    manufacturer: String,
    model: String,
    androidVersion: String,
    apiLevel: Int,
    supportedAbis: Array<String>,
    isDebugBuild: Boolean,
): String {
    val buildType = if (isDebugBuild) R.string.debug else R.string.release
    return context.getString(
        R.string.app_build,
        "${context.getString(R.string.manufacturer)} $manufacturer",
        "${context.getString(R.string.device_model)} $model",
        "${context.getString(R.string.android_version)} $androidVersion",
        "${context.getString(R.string.api_level)} $apiLevel",
        "${context.getString(R.string.arch)} ${supportedAbis.joinToString()}",
        context.getString(buildType),
    )
}
