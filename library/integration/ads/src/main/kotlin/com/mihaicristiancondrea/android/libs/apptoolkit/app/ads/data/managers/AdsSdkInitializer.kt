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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.managers

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig

/**
 * The single call site of `MobileAds.initialize`.
 *
 * It exists as a seam so the initialization contract, initialize once, with the host's own app id,
 * before anything loads an ad, can be tested without a live SDK.
 */
fun interface AdsSdkInitializer {

    /** Initializes the Mobile Ads SDK with [config]. */
    fun initialize(context: Context, config: InitializationConfig)

    companion object {
        /**
         * The production initializer.
         *
         * `android.permission.INTERNET` is declared by this module's own manifest rather than left
         * to the host, so the permission the SDK needs merges in wherever the toolkit is consumed.
         */
        @get:RequiresPermission(Manifest.permission.INTERNET)
        val Default: AdsSdkInitializer = AdsSdkInitializer { context, config ->
            MobileAds.initialize(context, config) {}
        }
    }
}
