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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Draws [state] with [content], animating with [transition] when its case changes.
 *
 * The case is [contentKey] of the state. By default a value of an enum, a `Boolean`, a number or a
 * string is a case of its own, and any other value is a case by its class, such as a sealed type's
 * subclass: moving from `Idle` to `Cleaning` animates, while new data inside the same case does
 * not. Use it
 * for any state a screen switches on, such as the phases of a flow; [ScreenStateHandler] uses it
 * for a [Loadable].
 *
 * ```
 * StateContent(state = state.phase) { phase ->
 *     when (phase) {
 *         CleanerPhase.Idle -> CleanerWelcome()
 *         is CleanerPhase.Cleaning -> CleaningProgress(phase.progress)
 *         CleanerPhase.Finished -> CleanerFinished()
 *     }
 * }
 * ```
 *
 * @param transition How one case replaces another; by default the app's [ScreenStates.transition],
 * or a cross-fade on the app's motion scheme when it sets none.
 */
@Composable
fun <S> StateContent(
    state: S,
    modifier: Modifier = Modifier,
    contentKey: (S) -> Any? = ::caseOf,
    transition: ContentTransform? = LocalScreenStates.current.transition,
    label: String = "StateContent",
    content: @Composable (S) -> Unit,
) {
    val motion = MaterialTheme.motionScheme
    AnimatedContent(
        targetState = state,
        modifier = modifier,
        transitionSpec = {
            transition ?: (fadeIn(motion.defaultEffectsSpec()) togetherWith fadeOut(motion.fastEffectsSpec()))
        },
        contentKey = contentKey,
        label = label,
    ) { current ->
        content(current)
    }
}

private fun caseOf(state: Any?): Any? = when (state) {
    null, is Enum<*>, is Boolean, is Number, is CharSequence, is Char -> state
    else -> state::class
}
