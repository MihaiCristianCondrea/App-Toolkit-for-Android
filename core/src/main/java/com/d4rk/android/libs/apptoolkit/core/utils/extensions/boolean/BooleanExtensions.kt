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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean

import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Maps a debug flag to the corresponding API environment name.
 */
fun Boolean.toApiEnvironment(): String =
    if (this) ApiEnvironments.ENV_DEBUG else ApiEnvironments.ENV_RELEASE

fun Boolean.asConsentStatus(): FirebaseAnalytics.ConsentStatus =
    if (this) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED