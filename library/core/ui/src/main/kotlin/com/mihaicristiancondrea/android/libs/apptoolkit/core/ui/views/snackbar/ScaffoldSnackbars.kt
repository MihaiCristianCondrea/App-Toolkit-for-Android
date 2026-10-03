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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.CustomSnackbarVisuals
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ToolkitSnackbarColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ToolkitSnackbarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberPageSnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The snackbars of the Toolkit scaffold around a screen: a page's frame or the shell's tabs. The
 * scaffold draws them above its bottom bar, its floating action buttons and the system bars; the
 * screen only says what to show.
 *
 * ```
 * val snackbars = rememberScaffoldSnackbars()
 * Button(onClick = {
 *     snackbars.post(
 *         message = context.getString(R.string.deleted),
 *         actionLabel = context.getString(R.string.undo),
 *         onAction = { viewModel.onEvent(Event.Undo) },
 *     )
 * })
 * ```
 *
 * A new snackbar replaces the one showing. Each can take the [ToolkitSnackbarStyle.Normal] or
 * [ToolkitSnackbarStyle.Error] look, an icon, colours of its own, or be drawn entirely by the
 * screen through `content`.
 *
 * @property hostState The scaffold's host, for code written against Material's API.
 */
@Stable
class ScaffoldSnackbars(
    val hostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {

    /**
     * Shows a snackbar and suspends until it leaves, replacing any snackbar showing.
     *
     * @param message The text.
     * @param style The normal or the error look.
     * @param actionLabel The action's label; none without it.
     * @param withDismissAction Whether it has a close button.
     * @param duration How long it stays; by default long for an error or with an action, short
     * otherwise.
     * @param icon Drawn before the message.
     * @param colors Replace the colours of [style].
     * @param content Draws the snackbar instead of the Toolkit's.
     * @return Whether its action was performed or it was dismissed.
     */
    suspend fun show(
        message: String,
        style: ToolkitSnackbarStyle = ToolkitSnackbarStyle.Normal,
        actionLabel: String? = null,
        withDismissAction: Boolean = true,
        duration: SnackbarDuration? = null,
        icon: ToolkitIcon? = null,
        colors: ToolkitSnackbarColors? = null,
        content: (@Composable (SnackbarData) -> Unit)? = null,
    ): SnackbarResult {
        hostState.currentSnackbarData?.dismiss()
        return hostState.showSnackbar(
            CustomSnackbarVisuals(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration ?: defaultDuration(style, actionLabel),
                isError = style == ToolkitSnackbarStyle.Error,
                icon = icon,
                colors = colors,
                content = content,
            ),
        )
    }

    /**
     * Shows a snackbar from anywhere, a click handler included, without waiting for it: [onAction]
     * runs if its action is performed, [onDismiss] if it leaves otherwise. The parameters are those
     * of [show].
     */
    fun post(
        message: String,
        style: ToolkitSnackbarStyle = ToolkitSnackbarStyle.Normal,
        actionLabel: String? = null,
        withDismissAction: Boolean = true,
        duration: SnackbarDuration? = null,
        icon: ToolkitIcon? = null,
        colors: ToolkitSnackbarColors? = null,
        content: (@Composable (SnackbarData) -> Unit)? = null,
        onAction: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
    ): Job = scope.launch {
        val result = show(message, style, actionLabel, withDismissAction, duration, icon, colors, content)
        when (result) {
            SnackbarResult.ActionPerformed -> onAction?.invoke()
            SnackbarResult.Dismissed -> onDismiss?.invoke()
        }
    }

    /** Hides the snackbar showing, if any. */
    fun dismiss() {
        hostState.currentSnackbarData?.dismiss()
    }

    private fun defaultDuration(style: ToolkitSnackbarStyle, actionLabel: String?): SnackbarDuration =
        if (style == ToolkitSnackbarStyle.Error || actionLabel != null) SnackbarDuration.Long else SnackbarDuration.Short
}

/** The [ScaffoldSnackbars] of the Toolkit scaffold around this point of the composition, if any. */
val LocalScaffoldSnackbars = staticCompositionLocalOf<ScaffoldSnackbars?> { null }

/**
 * The snackbars of the Toolkit scaffold around this screen. Outside one, snackbars go to a host of
 * their own that nothing draws until the caller puts a [DefaultSnackbarHost] on it.
 */
@Composable
fun rememberScaffoldSnackbars(): ScaffoldSnackbars {
    LocalScaffoldSnackbars.current?.let { return it }
    val hostState = rememberPageSnackbarHostState()
    val scope = rememberCoroutineScope()
    return remember(hostState, scope) { ScaffoldSnackbars(hostState, scope) }
}
