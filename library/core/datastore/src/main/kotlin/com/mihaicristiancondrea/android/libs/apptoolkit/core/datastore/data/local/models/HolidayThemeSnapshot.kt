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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason

/**
 * What the appearance was before a holiday theme was put on, so it can be put back afterwards.
 *
 * @property season The holiday whose palette was applied.
 * @property previousPaletteId The static palette selected before it.
 * @property previousDynamicColors Whether wallpaper colors were on before it. Applying a static
 * palette turns them off, so restoring only the palette would leave most people on the wrong theme.
 */
data class HolidayThemeSnapshot(
    val season: HolidaySeason,
    val previousPaletteId: String,
    val previousDynamicColors: Boolean,
)
