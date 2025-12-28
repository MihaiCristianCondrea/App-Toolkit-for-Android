package com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme

/**
 * Palette variant indices for dynamic color schemes.
 */
object DynamicPaletteVariant {
    const val MIN: Int = 0
    const val MAX: Int = 7
    val indices: IntRange = MIN..MAX

    fun clamp(value: Int): Int = value.coerceIn(MIN, MAX)
}

/**
 * Supported static palette identifiers.
 */
object StaticPaletteIds {
    const val MONOCHROME = "monochrome"
    const val BLUE = "blue"
    const val GREEN = "green"
    const val RED = "red"
    const val YELLOW = "yellow"

    const val ROSE = "rose"
    const val CHRISTMAS = "christmas"

    const val DEFAULT = "default"

    private val supportedOrder = listOf(
        MONOCHROME,
        BLUE,
        GREEN,
        RED,
        YELLOW,
        ROSE,
        CHRISTMAS,
    )

    val withDefault: List<String> = listOf(DEFAULT) + supportedOrder

    fun sanitize(id: String): String = when (id) {
        DEFAULT -> DEFAULT
        in supportedOrder -> id
        else -> DEFAULT
    }
}
