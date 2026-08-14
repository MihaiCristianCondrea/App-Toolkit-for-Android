/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.widgets.apps

import androidx.glance.appwidget.testing.unit.runGlanceAppWidgetUnitTest
import androidx.glance.testing.unit.assertHasClickAction
import androidx.glance.testing.unit.assertHasText
import androidx.glance.testing.unit.hasText
import org.junit.jupiter.api.Test

class AppIconsWidgetContentTest {

    @Test
    fun `message displays supplied text`() = runGlanceAppWidgetUnitTest {
        provideComposable { WidgetMessage("Loading catalogue") }

        onNode(hasText("Loading catalogue")).assertHasText("Loading catalogue")
    }

    @Test
    fun `error displays message and retry label`() = runGlanceAppWidgetUnitTest {
        provideComposable { WidgetError(STRINGS) }

        onNode(hasText(STRINGS.errorMessage)).assertHasText(STRINGS.errorMessage)
        onNode(hasText(STRINGS.retryLabel))
            .assertHasText(STRINGS.retryLabel)
            .assertHasClickAction()
    }

    private companion object {
        val STRINGS = WidgetStrings(
            title = "Apps",
            errorMessage = "Unable to load apps",
            retryLabel = "Try again",
        )
    }
}
