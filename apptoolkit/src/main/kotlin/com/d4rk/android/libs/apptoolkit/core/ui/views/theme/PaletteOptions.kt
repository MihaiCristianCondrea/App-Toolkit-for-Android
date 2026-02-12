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

package com.d4rk.android.libs.apptoolkit.core.ui.views.theme

import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ThemePaletteProvider.paletteById

/**
 * Returns a list of static palette IDs with duplicate palettes removed.
 *
 * When the injected default palette matches a built-in palette (e.g., blue), the list would
 * otherwise show visually identical swatches twice. This helper keeps the selected palette ID
 * while removing duplicates, so the UI reflects the effective palette without redundancy.
 */
internal fun dedupeStaticPaletteIds(
    options: List<String>,
    selectedPaletteId: String
): List<String> {
    val resolvedPalettes = options.associateWith { paletteById(it) }
    val uniqueIds = mutableListOf<String>()

    for (id in options) {
        val palette = resolvedPalettes.getValue(id)
        val existingIndex = uniqueIds.indexOfFirst { resolvedPalettes.getValue(it) == palette }
        if (existingIndex == -1) {
            uniqueIds.add(id)
        } else if (id == selectedPaletteId && uniqueIds[existingIndex] != selectedPaletteId) {
            uniqueIds[existingIndex] = id
        }
    }

    return uniqueIds
}
