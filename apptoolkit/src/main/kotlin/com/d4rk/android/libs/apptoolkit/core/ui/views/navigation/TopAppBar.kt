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

package com.d4rk.android.libs.apptoolkit.core.ui.views.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection

/**
 * Provides a scaffold with a Material 3 large top app bar, back navigation, and optional actions.
 *
 * State ownership:
 * - Callers own the title text, navigation callback, top-bar actions, and body content.
 * - Callers can provide [snackbarHostState] and [scrollBehavior] to integrate with screen-level state.
 *
 * Behavior:
 * - The default [scrollBehavior] is [TopAppBarDefaults.exitUntilCollapsedScrollBehavior], so the
 *   top app bar collapses as content scrolls.
 * - A default empty lambda is used when [floatingActionButton] is not provided.
 *
 * Usage note:
 * - Pass [content] that applies the provided [PaddingValues] to avoid overlap with the top app bar.
 *
 * @param title Title rendered in the large top app bar.
 * @param onBackClicked Callback invoked when the back button is pressed.
 * @param actions Optional action-slot composables displayed on the top app bar.
 * @param floatingActionButton Optional floating action button content for the scaffold.
 * @param snackbarHostState Snackbar host state used by the scaffold.
 * @param scrollBehavior Scroll behavior applied to the top app bar.
 * @param content Main screen content receiving scaffold paddings.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarWithScaffold(
    title: String,
    onBackClicked: () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    floatingActionButton: @Composable (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(modifier = Modifier.animateContentSize(), text = title) },
                navigationIcon = {
                    AnimatedIconButtonDirection(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = com.d4rk.android.libs.apptoolkit.R.string.go_back),
                        onClick = { onBackClicked() })
                },
                actions = actions,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = floatingActionButton ?: {},
    ) { paddingValues ->
        content(paddingValues)
    }
}

/**
 * Provides a scaffold with a scroll-aware large top app bar and optional floating action button.
 *
 * State ownership:
 * - Callers own [title], optional [floatingActionButton], and body [content].
 * - This composable owns an internal enter-always [TopAppBarScrollBehavior].
 *
 * Usage note:
 * - Apply the provided [PaddingValues] inside [content] so list and screen content does not draw
 *   under the app bar.
 *
 * @param title Title rendered in the large top app bar.
 * @param content Main scaffold content that receives top app bar paddings.
 * @param floatingActionButton Optional floating action button content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarScaffold(
    title: String,
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null
) {
    val scrollBehaviorState: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehaviorState.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(modifier = Modifier.animateContentSize(), text = title) },
                scrollBehavior = scrollBehaviorState
            )
        },
        floatingActionButton = floatingActionButton ?: {},
    ) { paddingValues ->
        content(paddingValues)
    }
}