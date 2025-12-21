package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

/**
 * 8 dynamic palette variants (0..7).
 *
 * 0 = Default (no changes)
 * 1 = Soft (swap accents with containers, per-role)
 * 2 = Rotate forward (tertiary -> primary -> secondary -> tertiary)
 * 3 = Swap primary <-> tertiary
 * 4 = Swap primary <-> secondary
 * 5 = Swap secondary <-> tertiary
 * 6 = Rotate backward (secondary -> primary -> tertiary -> secondary)
 * 7 = Fixed-Dim accents (use *FixedDim + on*Fixed for primary/secondary/tertiary when available)
 */
object DynamicPaletteVariant {
    const val MIN: Int = 0
    const val MAX: Int = 7
    val indices: IntRange = MIN..MAX

    fun clamp(value: Int): Int = value.coerceIn(MIN, MAX)
}

object StaticPaletteIds {
    const val MONOCHROME = "monochrome"
    const val BLUE = "blue"
    const val GREEN = "green"
    const val RED = "red"
    const val YELLOW = "yellow"

    const val ROSE = "rose"

    const val DEFAULT = "default"
}

fun ColorScheme.applyDynamicVariant(variant: Int): ColorScheme =
    when (DynamicPaletteVariant.clamp(variant)) {
        0 -> this
        1 -> softSwapAccentsWithContainers()
        2 -> rotateForward()
        3 -> swapPrimaryTertiary()
        4 -> swapPrimarySecondary()
        5 -> swapSecondaryTertiary()
        6 -> rotateBackward()
        7 -> useFixedDimAccents()
        else -> this
    }

private data class AccentGroup(
    val accent: Color,
    val onAccent: Color,
    val container: Color,
    val onContainer: Color,
    val fixed: Color,
    val fixedDim: Color,
    val onFixed: Color,
    val onFixedVariant: Color,
)

private fun ColorScheme.primaryGroup(): AccentGroup = AccentGroup(
    accent = primary,
    onAccent = onPrimary,
    container = primaryContainer,
    onContainer = onPrimaryContainer,
    fixed = primaryFixed,
    fixedDim = primaryFixedDim,
    onFixed = onPrimaryFixed,
    onFixedVariant = onPrimaryFixedVariant,
)

private fun ColorScheme.secondaryGroup(): AccentGroup = AccentGroup(
    accent = secondary,
    onAccent = onSecondary,
    container = secondaryContainer,
    onContainer = onSecondaryContainer,
    fixed = secondaryFixed,
    fixedDim = secondaryFixedDim,
    onFixed = onSecondaryFixed,
    onFixedVariant = onSecondaryFixedVariant,
)

private fun ColorScheme.tertiaryGroup(): AccentGroup = AccentGroup(
    accent = tertiary,
    onAccent = onTertiary,
    container = tertiaryContainer,
    onContainer = onTertiaryContainer,
    fixed = tertiaryFixed,
    fixedDim = tertiaryFixedDim,
    onFixed = onTertiaryFixed,
    onFixedVariant = onTertiaryFixedVariant,
)

private fun ColorScheme.withGroups(
    primary: AccentGroup,
    secondary: AccentGroup,
    tertiary: AccentGroup,
    surfaceTintOverride: Color = primary.accent,
): ColorScheme = copy(
    primary = primary.accent,
    onPrimary = primary.onAccent,
    primaryContainer = primary.container,
    onPrimaryContainer = primary.onContainer,
    primaryFixed = primary.fixed,
    primaryFixedDim = primary.fixedDim,
    onPrimaryFixed = primary.onFixed,
    onPrimaryFixedVariant = primary.onFixedVariant,

    secondary = secondary.accent,
    onSecondary = secondary.onAccent,
    secondaryContainer = secondary.container,
    onSecondaryContainer = secondary.onContainer,
    secondaryFixed = secondary.fixed,
    secondaryFixedDim = secondary.fixedDim,
    onSecondaryFixed = secondary.onFixed,
    onSecondaryFixedVariant = secondary.onFixedVariant,

    tertiary = tertiary.accent,
    onTertiary = tertiary.onAccent,
    tertiaryContainer = tertiary.container,
    onTertiaryContainer = tertiary.onContainer,
    tertiaryFixed = tertiary.fixed,
    tertiaryFixedDim = tertiary.fixedDim,
    onTertiaryFixed = tertiary.onFixed,
    onTertiaryFixedVariant = tertiary.onFixedVariant,

    // Keep tonal elevation feeling consistent with the “current” primary
    surfaceTint = surfaceTintOverride,
)

/**
 * Variant 1: "Soft" — swap accents with containers *within each role*.
 * Keeps fixed roles untouched.
 */
private fun ColorScheme.softSwapAccentsWithContainers(): ColorScheme {
    fun AccentGroup.swapAccentContainer(): AccentGroup = copy(
        accent = container,
        onAccent = onContainer,
        container = accent,
        onContainer = onAccent,
    )

    val p = primaryGroup().swapAccentContainer()
    val s = secondaryGroup().swapAccentContainer()
    val t = tertiaryGroup().swapAccentContainer()

    return withGroups(primary = p, secondary = s, tertiary = t, surfaceTintOverride = p.accent)
}

/**
 * Variant 2: "Rotate forward" — tertiary -> primary -> secondary -> tertiary.
 */
private fun ColorScheme.rotateForward(): ColorScheme {
    val p = primaryGroup()
    val s = secondaryGroup()
    val t = tertiaryGroup()
    return withGroups(primary = t, secondary = p, tertiary = s, surfaceTintOverride = t.accent)
}

/**
 * Variant 3: "Swap" — primary <-> tertiary.
 */
private fun ColorScheme.swapPrimaryTertiary(): ColorScheme {
    val p = primaryGroup()
    val s = secondaryGroup()
    val t = tertiaryGroup()
    return withGroups(primary = t, secondary = s, tertiary = p, surfaceTintOverride = t.accent)
}

/**
 * Variant 4: swap primary <-> secondary.
 */
private fun ColorScheme.swapPrimarySecondary(): ColorScheme {
    val p = primaryGroup()
    val s = secondaryGroup()
    val t = tertiaryGroup()
    return withGroups(primary = s, secondary = p, tertiary = t, surfaceTintOverride = s.accent)
}

/**
 * Variant 5: swap secondary <-> tertiary.
 */
private fun ColorScheme.swapSecondaryTertiary(): ColorScheme {
    val p = primaryGroup()
    val s = secondaryGroup()
    val t = tertiaryGroup()
    return withGroups(primary = p, secondary = t, tertiary = s, surfaceTintOverride = p.accent)
}

/**
 * Variant 6: "Rotate backward" — secondary -> primary -> tertiary -> secondary.
 * (Reverse direction vs rotateForward, but keeps the same “nice mapping” rules.)
 */
private fun ColorScheme.rotateBackward(): ColorScheme {
    val p = primaryGroup()
    val s = secondaryGroup()
    val t = tertiaryGroup()
    return withGroups(primary = s, secondary = t, tertiary = p, surfaceTintOverride = s.accent)
}

/**
 * Variant 7: "Fixed-Dim accents" — use *FixedDim + on*Fixed for accents when available.
 * Containers remain unchanged, so the UI stays familiar.
 *
 * This is safe even if fixed roles are Color.Unspecified (we just fall back to the current accents).
 */
private fun ColorScheme.useFixedDimAccents(): ColorScheme {
    fun pickAccent(current: Color, fixedDim: Color): Color =
        if (fixedDim != Color.Unspecified) fixedDim else current

    fun pickOnAccent(current: Color, onFixed: Color): Color =
        if (onFixed != Color.Unspecified) onFixed else current

    val newPrimary = pickAccent(primary, primaryFixedDim)
    val newSecondary = pickAccent(secondary, secondaryFixedDim)
    val newTertiary = pickAccent(tertiary, tertiaryFixedDim)

    return copy(
        primary = newPrimary,
        onPrimary = pickOnAccent(onPrimary, onPrimaryFixed),

        secondary = newSecondary,
        onSecondary = pickOnAccent(onSecondary, onSecondaryFixed),

        tertiary = newTertiary,
        onTertiary = pickOnAccent(onTertiary, onTertiaryFixed),

        surfaceTint = newPrimary,
    )
}
