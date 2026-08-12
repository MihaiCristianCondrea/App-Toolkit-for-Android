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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log

/**
 * Resolves the AdMob application id that belongs to the *host* app.
 *
 * The manifest entry `com.google.android.gms.ads.APPLICATION_ID` is the single source of truth:
 * the Google Mobile Ads SDK reads it at startup, and the same value has to be handed to UMP so the
 * consent request is scoped to the publisher app that is actually running.
 *
 * The toolkit deliberately has **no fallback constant**. A library-owned default is inherited by
 * every consumer app that does not override it, which silently points both the consent request and
 * `MobileAds.initialize` at somebody else's publisher account. When the id cannot be resolved this
 * provider returns `null` and callers skip the affected configuration step instead.
 */
fun interface AdMobAppIdProvider {

    /**
     * The host app's AdMob application id, or `null` when the manifest declares no usable value.
     */
    fun adMobAppId(): String?

    companion object {
        /** Manifest `<meta-data>` key read by the Google Mobile Ads SDK. */
        const val MANIFEST_METADATA_KEY: String = "com.google.android.gms.ads.APPLICATION_ID"

        /**
         * Canonical AdMob app id format.
         *
         * Example: `ca-app-pub-3940256099942544~3347511713`
         */
        val AD_MOB_APP_ID_REGEX: Regex = Regex(pattern = "^ca-app-pub-[0-9]{16}~[0-9]{10}$")

        /**
         * Normalizes and validates a raw manifest value.
         *
         * @return the trimmed id when it matches [AD_MOB_APP_ID_REGEX], `null` otherwise.
         */
        fun sanitize(rawValue: String?): String? {
            val trimmed: String = rawValue?.trim().orEmpty()
            return trimmed.takeIf { it.matches(AD_MOB_APP_ID_REGEX) }
        }
    }
}

/**
 * [AdMobAppIdProvider] backed by the calling app's own `PackageManager` metadata.
 *
 * The value is resolved once and cached, because the manifest cannot change while the process is
 * alive. A missing or malformed entry is logged and reported as `null`.
 */
class ManifestAdMobAppIdProvider(context: Context) : AdMobAppIdProvider {

    private val appContext: Context = context.applicationContext

    private val resolvedAppId: String? by lazy { resolveFromManifest() }

    override fun adMobAppId(): String? = resolvedAppId

    private fun resolveFromManifest(): String? {
        val metaData: Bundle? = runCatching { readApplicationMetaData() }
            .onFailure { throwable ->
                Log.w(LOG_TAG, "Failed to read application metadata.", throwable)
            }
            .getOrNull()

        val rawValue: String? = metaData?.getString(AdMobAppIdProvider.MANIFEST_METADATA_KEY)
        if (rawValue == null) {
            Log.w(
                LOG_TAG,
                "No ${AdMobAppIdProvider.MANIFEST_METADATA_KEY} meta-data declared by the host app."
            )
            return null
        }

        val sanitized: String? = AdMobAppIdProvider.sanitize(rawValue)
        if (sanitized == null) {
            Log.w(
                LOG_TAG,
                "Ignoring ${AdMobAppIdProvider.MANIFEST_METADATA_KEY}: value does not match the " +
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
        const val LOG_TAG: String = "AdMobAppIdProvider"
    }
}
