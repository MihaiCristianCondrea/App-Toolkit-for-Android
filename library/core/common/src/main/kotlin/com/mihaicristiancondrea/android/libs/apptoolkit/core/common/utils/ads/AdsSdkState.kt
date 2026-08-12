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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads

import com.google.android.libraries.ads.mobile.sdk.MobileAds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Whether the Google Mobile Ads SDK has been initialized in this process.
 *
 * Every ad loader in the SDK throws
 * `IllegalStateException("MobileAds.initialize must be called before using the Google Mobile Ads
 * SDK.")` when called too early, and there is no public way to ask the SDK itself. Initialization is
 * asynchronous and starts during app startup, so an ad surface composed early would otherwise either
 * crash or silently give up and never retry.
 *
 * `AdsCoreManager` is the only writer; ad views observe [isReady] and start their request when — and
 * only when — it turns `true`.
 */
object AdsSdkState {

    private val readyState = MutableStateFlow(value = false)

    /** `true` once `MobileAds.initialize` has been called for this process. */
    val isReady: StateFlow<Boolean> = readyState.asStateFlow()

    /** Marks the SDK as initialized. Called by `AdsCoreManager` and nothing else. */
    fun markInitialized() {
        readyState.value = true
    }

    /**
     * Whether an ad may be requested right now.
     *
     * [isReady] only knows about initialization the toolkit performed. A host that initializes the
     * SDK itself — which the toolkit asks it not to do, but cannot prevent — would otherwise leave
     * every ad slot permanently empty, so the SDK's own `MobileAds.isInitialized` is consulted as a
     * fallback.
     */
    fun canRequestAds(): Boolean =
        readyState.value || runCatching { MobileAds.isInitialized }.getOrDefault(false)
}
