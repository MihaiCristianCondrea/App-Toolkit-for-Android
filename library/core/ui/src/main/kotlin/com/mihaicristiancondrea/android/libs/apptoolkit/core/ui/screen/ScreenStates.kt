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

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen

/**
 * What a [ScreenStateHandler] knows when it draws a case other than [Loadable.Ready].
 *
 * @property contentPadding The padding the content keeps, usually the screen's content padding.
 * @property icon The screen's icon for its empty and failure content.
 * @property onRetry Tries again, or null when trying again does not apply to this case.
 * @property showAd Whether the content may show its native ad.
 */
@Immutable
data class StateSlot(
    val contentPadding: PaddingValues,
    val icon: ToolkitIcon,
    val onRetry: (() -> Unit)?,
    val showAd: Boolean,
)

/**
 * How an app draws loading, empty and failed content, and how one case replaces another. Every
 * [ScreenStateHandler] and [StateContent] follows the nearest [LocalScreenStates], so an app sets
 * its own once around its content and every screen follows; a single section passes its own
 * `onLoading`, `onEmpty` or `onError` instead.
 *
 * ```
 * CompositionLocalProvider(
 *     LocalScreenStates provides ScreenStates(loading = { slot -> AppLoading(slot.contentPadding) }),
 * ) {
 *     ShellHost(graph = graph)
 * }
 * ```
 *
 * The defaults are the Toolkit's: [LoadingScreen], and [NoDataScreen] with the slot's icon, retry
 * and ad, faded over 300 ms.
 */
@Immutable
class ScreenStates(
    val loading: @Composable (StateSlot) -> Unit = { slot -> LoadingScreen(paddingValues = slot.contentPadding) },
    val empty: @Composable (StateSlot, Loadable.Empty) -> Unit = { slot, empty ->
        NoDataScreen(
            message = empty.message?.asString(),
            icon = slot.icon,
            showRetry = slot.onRetry != null,
            onRetry = { slot.onRetry?.invoke() },
            showAd = slot.showAd,
            paddingValues = slot.contentPadding,
        )
    },
    val failed: @Composable (StateSlot, Loadable.Failed) -> Unit = { slot, failed ->
        NoDataScreen(
            message = failed.message.asString(),
            icon = slot.icon,
            isError = true,
            showRetry = slot.onRetry != null,
            onRetry = { slot.onRetry?.invoke() },
            showAd = slot.showAd,
            paddingValues = slot.contentPadding,
        )
    },
    val transition: ContentTransform = StateFade,
)

/** The [ScreenStates] screens draw with here. */
val LocalScreenStates = staticCompositionLocalOf { ScreenStates() }

private val StateFade: ContentTransform =
    fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
        fadeOut(animationSpec = tween(durationMillis = 300))
