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

package com.mihaicristiancondrea.android.apps.apptoolkit.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import java.util.regex.Pattern

internal const val TARGET_PACKAGE = "com.d4rk.android.apps.apptoolkit"

/** Requires English UI and completed onboarding, so consent and setup are outside measured work. */
internal fun MacrobenchmarkScope.awaitCatalog() {
    check(device.wait(Until.hasObject(By.text("Quick Tools")), 15_000)) {
        "Complete onboarding in English and select Quick Tools before running performance tests."
    }
    check(device.wait(Until.hasObject(By.text("Sensors")), 15_000)) {
        "Clear the tool search and open Quick Tools on its first section before running performance tests."
    }
}

internal fun MacrobenchmarkScope.scrollCatalog() {
    val list = checkNotNull(device.wait(Until.findObject(By.scrollable(true)), 10_000)) {
        "Quick Tools has no scrollable catalog."
    }
    list.setGestureMargin(device.displayWidth / 5)
    repeat(3) { list.fling(Direction.DOWN) }
    repeat(3) { list.fling(Direction.UP) }
}

/** Selects the tab whose navigation item reads [label] and waits for its page to settle. */
internal fun MacrobenchmarkScope.openTab(label: String) {
    val tab = checkNotNull(device.wait(Until.findObject(By.text(label)), 10_000)) {
        "No tab labelled $label."
    }
    tab.click()
    device.waitForIdle()
}

/**
 * Opens the tool card titled [title] on the current tab, scrolling down to it when it is off
 * screen, waits for its sheet, then closes the sheet with Back. Cards draw some titles in capitals,
 * so the title is matched without case.
 */
internal fun MacrobenchmarkScope.openAndCloseTool(title: String) {
    val list = checkNotNull(device.wait(Until.findObject(By.scrollable(true)), 10_000)) {
        "The tab has no scrollable list."
    }
    list.setGestureMargin(device.displayWidth / 5)
    val titleSelector = By.text(Pattern.compile("^${Pattern.quote(title)}$", Pattern.CASE_INSENSITIVE))
    var card = device.findObject(titleSelector)
    var scrolls = 0
    while (card == null && scrolls < MAX_SCROLLS) {
        list.scroll(Direction.DOWN, 0.8f)
        card = device.findObject(titleSelector)
        scrolls++
    }
    checkNotNull(card) { "No tool titled $title." }.click()
    check(device.wait(Until.hasObject(CloseSheet), 10_000)) { "The $title sheet did not open." }
    device.waitForIdle()
    device.pressBack()
    check(device.wait(Until.gone(CloseSheet), 10_000)) { "The $title sheet did not close." }
}

/** Opens Settings from the navigation drawer, then the Display page, and returns to the tabs. */
internal fun MacrobenchmarkScope.openSettingsAndDisplay() {
    val menu = checkNotNull(device.wait(Until.findObject(By.desc("Open navigation")), 10_000)) {
        "No navigation menu button."
    }
    menu.click()
    // The drawer slides in; a tap during the slide can miss its entry.
    device.waitForIdle()
    checkNotNull(device.wait(Until.findObject(By.text("Settings")), 10_000)) {
        "The navigation drawer has no Settings entry."
    }.click()
    device.waitForIdle()
    checkNotNull(device.wait(Until.findObject(By.text("Display")), 10_000)) {
        "Settings has no Display entry."
    }.click()
    device.waitForIdle()
    device.pressBack()
    device.waitForIdle()
    device.pressBack()
    device.waitForIdle()
}

private const val MAX_SCROLLS = 6

/** The close button every tool sheet shows. */
private val CloseSheet = By.desc("Close quick tool")
