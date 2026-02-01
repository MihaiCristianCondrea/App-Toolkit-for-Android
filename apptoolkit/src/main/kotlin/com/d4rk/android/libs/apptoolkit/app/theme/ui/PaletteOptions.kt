package com.d4rk.android.libs.apptoolkit.app.theme.ui

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
