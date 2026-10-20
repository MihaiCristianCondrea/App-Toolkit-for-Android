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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models

import androidx.compose.runtime.Immutable

/**
 * A screen as analytics names it.
 *
 * [name] becomes the GA4 `screen_name` and the `screen` of its `screen_state` events, and
 * [className] the `screen_class`. Both are identifiers dashboards group by, so they never change
 * once shipped, and they are written as literals: R8 renames classes, so a name read from a class
 * is unreadable in release reports. Declare each app's screens once, in one place, as the sample's
 * `AppScreenTracking` does.
 */
@Immutable
data class TrackedScreen(
    val name: String,
    val className: String? = null,
)
