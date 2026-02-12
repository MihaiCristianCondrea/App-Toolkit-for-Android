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

package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import kotlinx.coroutines.delay

/**
 * Switches between different UI states with a fade animation.
 *
 * The handler observes [screenState] and renders the provided content
 * based on its [ScreenState]. Loading and empty states receive their own
 * composables while successful data is passed to [onSuccess].
 *
 * @param screenState The state holder describing screen status and data.
 * @param onLoading Composable displayed while loading.
 * @param onEmpty Composable shown when there is no data.
 * @param onSuccess Composable invoked with loaded data on success.
 * @param onError Optional composable shown when an error occurs.
 */
@Composable
fun <T> ScreenStateHandler(
    screenState: UiStateScreen<T>,
    onLoading: @Composable () -> Unit,
    onEmpty: @Composable () -> Unit,
    onSuccess: @Composable (T) -> Unit,
    onError: (@Composable () -> Unit)? = null
) {
    var currentScreenState: ScreenState by remember { mutableStateOf(value = screenState.screenState) }

    LaunchedEffect(screenState.screenState) {
        if (screenState.screenState is ScreenState.IsLoading) {
            delay(timeMillis = 2500)
        }
        currentScreenState = screenState.screenState
    }
    val animationSpec: ContentTransform = remember {
        fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith fadeOut(
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }

    AnimatedContent(
        targetState = currentScreenState,
        transitionSpec = { animationSpec },
        label = "ScreenStateTransition"
    ) { currentState ->
        when (currentState) {
            is ScreenState.IsLoading -> {
                onLoading()
            }

            is ScreenState.NoData -> {
                onEmpty()
            }

            is ScreenState.Success -> {
                screenState.data?.let {
                    onSuccess(it)
                }
            }

            is ScreenState.Error -> {
                onError?.invoke()
            }
        }
    }
}