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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import java.util.concurrent.ConcurrentHashMap
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.VisibilityCascade

/**
 * When a screen's entrance plays. None of them replays it on returning from the background, on a
 * rotation, or for an item scrolled back into view within the visit.
 */
enum class EntranceReplay {
    /** Each time the screen is shown by navigation, including going back to it. */
    EveryVisit,

    /**
     * Each time the screen is opened: a screen opened again after it was closed, or a tab picked
     * from the navigation, enters anew, while going back to it does not.
     */
    OnOpen,

    /** Only the first time each screen is opened while the app runs; opening it again does not. */
    FirstVisit,
}

/**
 * The app's [EntranceReplay], [EntranceReplay.OnOpen] unless the app picks another. The shell
 * provides the app's choice, `appToolkitDefaults { shell { entranceReplay = ... } }`, or the one
 * picked in the developer options.
 */
val LocalEntranceReplay = staticCompositionLocalOf { EntranceReplay.OnOpen }

/**
 * How many times the screen here has been opened again while it stayed in its back stack, such as
 * a tab picked again from the navigation bar or rail. The shell provides it around each tab's
 * screens. When it changes, [EntranceReplay.OnOpen] plays the screen's entrance again; going back
 * to the screen leaves it as it is, so it does not.
 */
val LocalScreenOpening = staticCompositionLocalOf { 0 }

/**
 * One screen's entrance, provided by [ScreenHost]: its own cascade, so two screens never share a
 * wave, the [id] its elements save once they have played, whether the screen has [settled], the
 * [view] its window draws in, so a dialog or a sheet over it can tell it is elsewhere, and whether
 * this is the [firstInRun] copy of the screen, the one [EntranceReplay.FirstVisit] plays in. An
 * element that played before [openedAt], the entrance the screen was last opened in, plays again
 * under [EntranceReplay.OnOpen].
 */
@Stable
internal class ScreenEntrance(
    val id: Long,
    val view: View? = null,
    val firstInRun: Boolean = true,
    val openedAt: Long = id,
) {
    /** Whether the screen is shown and active, its transition over and nothing covering it. */
    var settled: Boolean by mutableStateOf(false)

    val cascade: VisibilityCascade = VisibilityCascade()
}

/** The [ScreenEntrance] of the screen around this point, if it is inside a [ScreenHost]. */
internal val LocalScreenEntrance = staticCompositionLocalOf<ScreenEntrance?> { null }

/**
 * The screens opened so far while the app runs, for [EntranceReplay.FirstVisit]. It lives as long
 * as the process, so the first copy of each screen after a restart enters again.
 */
internal object OpenedScreens {
    private val screens: MutableSet<TrackedScreen> = ConcurrentHashMap.newKeySet()

    /** Records [screen] as opened; true only the first time it is recorded in this run. */
    fun claim(screen: TrackedScreen): Boolean = screens.add(screen)

    /** Forgets every screen, as a new run would. */
    fun forget() = screens.clear()
}
