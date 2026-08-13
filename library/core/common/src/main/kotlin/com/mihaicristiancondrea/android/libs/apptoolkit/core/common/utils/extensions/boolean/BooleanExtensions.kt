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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.boolean

import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Maps a debug flag to the corresponding API environment name.
 */
fun Boolean.toApiEnvironment(): String =
    if (this) DEBUG_API_ENVIRONMENT else RELEASE_API_ENVIRONMENT

fun Boolean.asConsentStatus(): FirebaseAnalytics.ConsentStatus =
    if (this) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED

private const val DEBUG_API_ENVIRONMENT: String = "debug"
private const val RELEASE_API_ENVIRONMENT: String = "release"
