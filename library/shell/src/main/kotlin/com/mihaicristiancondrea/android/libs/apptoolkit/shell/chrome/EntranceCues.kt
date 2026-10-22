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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.EntranceCue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LocalEntranceCue

/**
 * How far into the transition bringing a screen in its entrance begins: late enough that the
 * screen has nearly landed, early enough that its content arrives as it does instead of after.
 */
internal const val ENTRANCE_CUE_FRACTION: Float = 0.6f

/**
 * Gives the screens in [content] an [EntranceCue] reached once the transition of the entry they
 * are in is [ENTRANCE_CUE_FRACTION] through, and the cue around it is reached too: a tab's screen
 * waits for its own transition and for the shell's, under a page that is still opening.
 */
@Composable
internal fun ProvideEntranceCue(content: @Composable () -> Unit) {
    val transition: Transition<EnterExitState> = LocalNavAnimatedContentScope.current.transition
    val around: EntranceCue? = LocalEntranceCue.current
    val cue = remember(transition, around) {
        EntranceCue { transition.isFarEnough() && around?.reached() != false }
    }
    CompositionLocalProvider(LocalEntranceCue provides cue, content = content)
}

/** Whether this transition is bringing its content in and is far enough along, or is over. */
internal fun Transition<EnterExitState>.isFarEnough(): Boolean = when {
    targetState != EnterExitState.Visible -> false
    currentState == EnterExitState.Visible -> true
    else -> playTimeNanos >= (totalDurationNanos * ENTRANCE_CUE_FRACTION).toLong()
}
