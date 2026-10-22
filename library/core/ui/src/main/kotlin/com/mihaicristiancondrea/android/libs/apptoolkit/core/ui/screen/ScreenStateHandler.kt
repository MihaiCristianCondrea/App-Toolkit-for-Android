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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.EntranceStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.LocalEntranceStyle

/**
 * Draws [state], animating between its cases with the app's [ScreenStates].
 *
 * Every case but [Loadable.Ready] is drawn by the nearest [LocalScreenStates], so a screen passes
 * only what differs: its [icon], whether an empty result is worth another try, whether the ad
 * shows. A failure always shows something, with a retry button when [onRetry] is given and the
 * failure is retryable. The animation runs when the case changes, not when the content inside
 * [Loadable.Ready] does.
 *
 * Inside a [ScreenHost], the loading content stays until the screen's entrance can begin: a result
 * that arrives while the screen is still sliding in waits there, then the loading content fades as
 * the result's elements enter, so the screen is never blank between the two.
 *
 * ```
 * ScreenStateHandler(state = state.config, icon = Icons.Outlined.Settings, onRetry = onRetry) { ready ->
 *     SettingsList(ready.value)
 * }
 * ```
 *
 * Use it for one [Loadable] field of a screen's state; a screen with several draws one each.
 *
 * @param contentPadding Padding the loading, empty and failure content keeps, usually the screen's
 * content padding.
 * @param onRetry What the failure content's retry button does; without it there is none.
 * @param retryWhenEmpty Whether the empty content offers [onRetry] too, for a result that may
 * change, such as a catalog that may have been updated.
 * @param icon The icon of the empty and failure content: any [ToolkitIcon], such as a drawable or
 * an animated icon. An overload takes an [ImageVector].
 * @param showAd Whether the empty and failure content show their native ad.
 * @param onLoading Replaces the loading content for this section only, such as with a skeleton of
 * its layout.
 * @param onEmpty Replaces the empty content for this section only.
 * @param onError Replaces the failure content for this section only.
 * @param onSuccess Draws the content. It receives the whole [Loadable.Ready], so it can mark a
 * refresh or a stale copy.
 */
@Composable
fun <T> ScreenStateHandler(
    state: Loadable<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onRetry: (() -> Unit)? = null,
    retryWhenEmpty: Boolean = false,
    icon: ToolkitIcon = ToolkitIcon.Vector(Icons.Default.Info),
    showAd: Boolean = true,
    onLoading: @Composable () -> Unit = { DefaultLoading(contentPadding, icon, showAd) },
    onEmpty: @Composable (Loadable.Empty) -> Unit = { empty ->
        DefaultEmpty(empty, contentPadding, icon, onRetry.takeIf { retryWhenEmpty }, showAd)
    },
    onError: @Composable (Loadable.Failed) -> Unit = { failed ->
        DefaultFailed(failed, contentPadding, icon, onRetry.takeIf { failed.retryable }, showAd)
    },
    onSuccess: @Composable (Loadable.Ready<T>) -> Unit,
) {
    StateContent(state = heldWhileEntering(state), modifier = modifier, label = "ScreenStateHandler") { current ->
        when (current) {
            Loadable.Loading -> onLoading()
            is Loadable.Empty -> onEmpty(current)
            is Loadable.Failed -> onError(current)
            is Loadable.Ready -> onSuccess(current)
        }
    }
}

/** [ScreenStateHandler] with an [ImageVector] [icon]. */
@Composable
fun <T> ScreenStateHandler(
    state: Loadable<T>,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onRetry: (() -> Unit)? = null,
    retryWhenEmpty: Boolean = false,
    showAd: Boolean = true,
    onLoading: @Composable () -> Unit = { DefaultLoading(contentPadding, ToolkitIcon.Vector(icon), showAd) },
    onEmpty: @Composable (Loadable.Empty) -> Unit = { empty ->
        DefaultEmpty(empty, contentPadding, ToolkitIcon.Vector(icon), onRetry.takeIf { retryWhenEmpty }, showAd)
    },
    onError: @Composable (Loadable.Failed) -> Unit = { failed ->
        DefaultFailed(failed, contentPadding, ToolkitIcon.Vector(icon), onRetry.takeIf { failed.retryable }, showAd)
    },
    onSuccess: @Composable (Loadable.Ready<T>) -> Unit,
) {
    ScreenStateHandler(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
        onLoading = onLoading,
        onEmpty = onEmpty,
        onError = onError,
        onSuccess = onSuccess,
    )
}

/**
 * [state], or [Loadable.Loading] while loading content is showing and the screen's entrance cannot
 * begin yet, so the result does not replace it with content still hidden for its entrance.
 */
@Composable
private fun <T> heldWhileEntering(state: Loadable<T>): Loadable<T> {
    val entrance: ScreenEntrance? = LocalScreenEntrance.current
    val style: EntranceStyle = LocalEntranceStyle.current
    val context = LocalContext.current
    val shown = remember { LoadingShown() }
    val holds = shown.loading &&
        state !is Loadable.Loading &&
        entrance != null &&
        !entrance.settled &&
        style != EntranceStyle.None &&
        !context.isSystemAnimationDisabled()
    val result: Loadable<T> = if (holds) Loadable.Loading else state
    shown.loading = result is Loadable.Loading
    return result
}

/** Whether the loading content was the last case shown. */
private class LoadingShown {
    var loading: Boolean = false
}

@Composable
private fun DefaultLoading(contentPadding: PaddingValues, icon: ToolkitIcon, showAd: Boolean) {
    LocalScreenStates.current.loading(StateSlot(contentPadding, icon, onRetry = null, showAd))
}

@Composable
private fun DefaultEmpty(
    empty: Loadable.Empty,
    contentPadding: PaddingValues,
    icon: ToolkitIcon,
    onRetry: (() -> Unit)?,
    showAd: Boolean,
) {
    LocalScreenStates.current.empty(StateSlot(contentPadding, icon, onRetry, showAd), empty)
}

@Composable
private fun DefaultFailed(
    failed: Loadable.Failed,
    contentPadding: PaddingValues,
    icon: ToolkitIcon,
    onRetry: (() -> Unit)?,
    showAd: Boolean,
) {
    LocalScreenStates.current.failed(StateSlot(contentPadding, icon, onRetry, showAd), failed)
}
