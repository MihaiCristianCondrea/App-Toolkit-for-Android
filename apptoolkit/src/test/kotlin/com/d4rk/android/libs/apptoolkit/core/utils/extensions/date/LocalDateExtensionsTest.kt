package com.d4rk.android.libs.apptoolkit.core.utils.extensions.date

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

class LocalDateExtensionsTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2024-12-24",
            "2024-12-25",
            "2024-12-26",
            "2024-12-31",
            "2025-01-01",
            "2025-01-05",
            "2025-01-07",
        ]
    )
    fun `christmas season should include December 24th through January 7th`(date: String) {
        assertTrue(LocalDate.parse(date).isChristmasSeason)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2024-12-23",
            "2025-01-08",
            "2024-11-15",
            "2025-02-01",
        ]
    )
    fun `christmas season should exclude dates outside the window`(date: String) {
        assertFalse(LocalDate.parse(date).isChristmasSeason)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2024-10-31",
            "2024-11-01",
            "2024-11-02",
        ]
    )
    fun `halloween season should include October 31st through November 2nd`(date: String) {
        assertTrue(LocalDate.parse(date).isHalloweenSeason)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2024-10-30",
            "2024-11-03",
            "2024-09-15",
            "2024-12-24",
        ]
    )
    fun `halloween season should exclude dates outside the window`(date: String) {
        assertFalse(LocalDate.parse(date).isHalloweenSeason)
    }
}
