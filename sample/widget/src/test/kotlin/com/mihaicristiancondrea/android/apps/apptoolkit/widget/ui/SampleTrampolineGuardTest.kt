package com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SampleTrampolineGuardTest {
    @Test
    fun `only missing Glance envelope failures are recoverable`() {
        for (message in listOf(
            "List adapter activity trampoline invoked without specifying target intent.",
            "List adapter activity trampoline invoked without trampoline type",
        )) {
            val failure = IllegalArgumentException(message)
            assertTrue(failure.isMissingGlanceAction(missingEnvelope = true))
            assertFalse(failure.isMissingGlanceAction(missingEnvelope = false))
        }
        assertFalse(IllegalArgumentException("another error").isMissingGlanceAction(missingEnvelope = true))
    }
}
