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

package com.d4rk.android.libs.apptoolkit.app.about.domain.model

/**
 * Result of a device info copy attempt.
 *
 * @param copied whether the clipboard operation succeeded.
 * @param shouldShowFeedback true when legacy platforms require in-app confirmation because no system
 * clipboard preview is available.
 */
data class CopyDeviceInfoResult(
    val copied: Boolean,
    val shouldShowFeedback: Boolean,
)
