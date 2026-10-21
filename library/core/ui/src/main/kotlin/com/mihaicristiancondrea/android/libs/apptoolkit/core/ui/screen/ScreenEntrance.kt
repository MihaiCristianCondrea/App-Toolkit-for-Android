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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import android.view.View
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.VisibilityCascade

/** When an entrance plays again: on each return to the screen, or only the first time it is shown. */
enum class EntranceReplay {
    /** Each time the screen is returned to by navigation, its elements enter again. */
    EveryVisit,

    /** Elements enter the first time the screen shows them, and stay after. */
    FirstVisit,
}

/**
 * The app's [EntranceReplay]. The shell provides the app's choice,
 * `appToolkitDefaults { shell { entranceReplay = ... } }`, or the one picked in the developer
 * options.
 */
val LocalEntranceReplay = staticCompositionLocalOf { EntranceReplay.EveryVisit }

/**
 * One screen's entrance, provided by [ScreenHost]: its own cascade, so two screens never share a
 * wave, the [id] its elements save once they have played, whether the screen has [settled], and
 * the [view] its window draws in, so a dialog or a sheet over it can tell it is elsewhere.
 */
@Stable
internal class ScreenEntrance(val id: Long, val view: View? = null) {
    /** Whether the screen is shown and active, its transition over and nothing covering it. */
    var settled: Boolean by mutableStateOf(false)

    val cascade: VisibilityCascade = VisibilityCascade()
}

/** The [ScreenEntrance] of the screen around this point, if it is inside a [ScreenHost]. */
internal val LocalScreenEntrance = staticCompositionLocalOf<ScreenEntrance?> { null }
