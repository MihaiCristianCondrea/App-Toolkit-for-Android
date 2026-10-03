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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen

private val StateFade: ContentTransform =
    fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
        fadeOut(animationSpec = tween(durationMillis = 300))

/**
 * Draws [state], fading between its cases.
 *
 * Every case but [Loadable.Ready] has a default from the design system, so a screen passes only
 * what differs; in particular a failure always shows something, with a retry button when
 * [onRetry] is given and the failure is retryable. The fade runs when the case changes, not when
 * the content inside [Loadable.Ready] does.
 *
 * Use it for one [Loadable] field of a screen's state; a screen with several draws one each.
 *
 * @param contentPadding Padding the default loading, empty and failure content keeps, usually the
 * screen's content padding.
 * @param onRetry What the default failure content's retry button does; without it there is none.
 * @param onSuccess Draws the content. It receives the whole [Loadable.Ready], so it can mark a
 * refresh or a stale copy.
 */
@Composable
fun <T> ScreenStateHandler(
    state: Loadable<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onRetry: (() -> Unit)? = null,
    onLoading: @Composable () -> Unit = { LoadingScreen(paddingValues = contentPadding) },
    onEmpty: @Composable (Loadable.Empty) -> Unit = { empty ->
        NoDataScreen(message = empty.message?.asString(), paddingValues = contentPadding)
    },
    onError: @Composable (Loadable.Failed) -> Unit = { failed ->
        NoDataScreen(
            message = failed.message.asString(),
            isError = true,
            showRetry = failed.retryable && onRetry != null,
            onRetry = { onRetry?.invoke() },
            paddingValues = contentPadding,
        )
    },
    onSuccess: @Composable (Loadable.Ready<T>) -> Unit,
) {
    AnimatedContent(
        targetState = state,
        modifier = modifier,
        transitionSpec = { StateFade },
        contentKey = { it::class },
        label = "ScreenStateHandler",
    ) { current ->
        when (current) {
            Loadable.Loading -> onLoading()
            is Loadable.Empty -> onEmpty(current)
            is Loadable.Failed -> onError(current)
            is Loadable.Ready -> onSuccess(current)
        }
    }
}
