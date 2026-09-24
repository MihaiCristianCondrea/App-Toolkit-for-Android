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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme

/**
 * Stored state of the seasonal themes.
 *
 * @property unlocked Whether the person found the About screen easter egg. Once found, the Christmas
 * and Halloween palettes stay in the palette list all year, and snow falls with the Christmas
 * palette outside the Christmas season too.
 * @property holidayThemeInUse The holiday whose palette was applied from the holiday greeting and
 * will be taken off again when the holiday ends, or null when none is.
 */
data class SeasonalThemeState(
    val unlocked: Boolean = false,
    val holidayThemeInUse: HolidaySeason? = null,
)
