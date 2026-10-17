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

internal const val TARGET_PACKAGE = "com.d4rk.android.apps.apptoolkit"

/** Requires English UI and completed onboarding, so consent and setup are outside measured work. */
internal fun MacrobenchmarkScope.awaitCatalog() {
    check(device.wait(Until.hasObject(By.text("All tools")), 15_000)) {
        "Complete onboarding in English and select Quick Tools before running performance tests."
    }
    check(device.wait(Until.hasObject(By.text("Sensors & Measurement")), 15_000)) {
        "Clear catalog search, select All tools and scroll to the top before running performance tests."
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
