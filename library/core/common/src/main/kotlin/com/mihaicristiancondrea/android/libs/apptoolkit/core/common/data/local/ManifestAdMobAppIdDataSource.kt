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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import kotlinx.coroutines.withContext

/**
 * [AdMobAppIdDataSource] backed by the calling app's own `PackageManager` metadata.
 *
 * The value is resolved once and cached, because the manifest cannot change while the process is
 * alive. A missing or malformed entry is logged and reported as `null`.
 */
class ManifestAdMobAppIdDataSource(
    context: Context,
    private val dispatchers: DispatcherProvider = StandardDispatchers(),
) : AdMobAppIdDataSource {

    private val appContext: Context = context.applicationContext

    private val resolvedAppId: String? by lazy { resolveFromManifest() }

    override suspend fun adMobAppId(): String? = withContext(dispatchers.io) { resolvedAppId }

    private fun resolveFromManifest(): String? {
        val metaData: Bundle? = runCatching { readApplicationMetaData() }
            .onFailure { throwable ->
                Log.w(LOG_TAG, "Failed to read application metadata.", throwable)
            }
            .getOrNull()

        val rawValue: String? = metaData?.getString(AdMobAppIdDataSource.MANIFEST_METADATA_KEY)
        if (rawValue == null) {
            Log.w(
                LOG_TAG,
                "No ${AdMobAppIdDataSource.MANIFEST_METADATA_KEY} meta-data declared by the host app."
            )
            return null
        }

        val sanitized: String? = AdMobAppIdDataSource.sanitize(rawValue)
        if (sanitized == null) {
            Log.w(
                LOG_TAG,
                "Ignoring ${AdMobAppIdDataSource.MANIFEST_METADATA_KEY}: value does not match the " +
                        "canonical AdMob app id format."
            )
        }
        return sanitized
    }

    @Suppress("DEPRECATION")
    private fun readApplicationMetaData(): Bundle? {
        val packageManager: PackageManager = appContext.packageManager
        val packageName: String = appContext.packageName
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()),
            ).metaData
        } else {
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
        }
    }

    private companion object {
        const val LOG_TAG: String = "AdMobAppIdDataSource"
    }
}
