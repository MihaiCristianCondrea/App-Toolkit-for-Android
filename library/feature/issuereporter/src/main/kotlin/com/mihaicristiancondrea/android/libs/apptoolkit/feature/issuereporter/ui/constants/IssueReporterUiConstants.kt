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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.constants

import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

internal object IssueReporterUiConstants {
    /** Outward-facing corner radius shared by every grouped block on the report screen. */
    val GROUP_OUTER_RADIUS: Dp = SizeConstants.LargeMediumSize

    /** Minimum visible rows for the issue description field. */
    const val DESCRIPTION_MIN_LINES: Int = 5

    /** Maximum rows the issue description field grows to before scrolling internally. */
    const val DESCRIPTION_MAX_LINES: Int = 12
}
