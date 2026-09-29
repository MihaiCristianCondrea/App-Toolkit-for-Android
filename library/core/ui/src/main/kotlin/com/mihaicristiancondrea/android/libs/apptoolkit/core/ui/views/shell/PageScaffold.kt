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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ContentCardShape
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalBesideNavigation
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.isTopLevelPage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.FabHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.LocalFabHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ToolkitFabColumn
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.rememberFabScrollBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalPageKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.LocalPaneRole

/**
 * The frame of a page: an app bar with a back button, and a body under it.
 *
 * The back button closes the page, and any detail open beside it, through the shell navigator.
 * Inside a list-detail pane the frame draws no app bar at all: the scene draws one bar across both
 * panes, titled from each page. A large bar becomes a small one on a window too short for it.
 *
 * The body is laid out edge to edge at the bottom: it reaches behind the system navigation bar,
 * which it receives as [LocalContentPadding] instead, so its lists scroll under the bar rather
 * than stopping at a band above it. The top and the sides are padded here.
 *
 * Pages registered with a title get this frame from the shell; pages that need something under
 * their app bar, such as tabs, call it themselves.
 *
 * Floating action buttons come from [fabs], from what the screen inside declares with
 * `ScaffoldFabs`, and from [floatingActionButton] for a button drawn by hand; the frame stacks the
 * described ones in a column at the bottom end.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageScaffold(
    title: String,
    modifier: Modifier = Modifier,
    style: TopBarStyle = TopBarStyle.Large,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    fabs: List<ToolkitFab> = emptyList(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable () -> Unit,
) {
    val navigator = LocalShellNavigator.current
    val pageKey = LocalPageKey.current
    val pane = LocalPaneRole.current
    // Beside a rail or permanent drawer, the page the navigation opened stands in for a tab and is
    // drawn like one: the tab's small bar with no back button, and the tab's card when the
    // navigation and the app bar share a colour.
    val beside = LocalBesideNavigation.current
    val topLevel = pane == PaneRole.None && isTopLevelPage(pageKey)
    val carded = topLevel && beside?.tinted == true
    val declared = LocalTopBarStyleOverride.current ?: if (topLevel) TopBarStyle.Small else style
    val resolvedStyle = if (pane == PaneRole.None) LocalShellLayout.current.topBarFor(declared) else TopBarStyle.Hidden
    val scrollBehavior = rememberTopBarScrollBehavior(resolvedStyle)
    val fabHost = remember { FabHost() }
    val fabScroll = rememberFabScrollBehavior()
    val contentInsets = when (pane) {
        PaneRole.None -> WindowInsets.safeDrawing
        PaneRole.List -> WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.Bottom)
        PaneRole.Detail -> WindowInsets.safeDrawing.only(WindowInsetsSides.End + WindowInsetsSides.Bottom)
    }
    Scaffold(
        modifier = modifier
            .then(if (carded && beside != null) Modifier.drawBehind { drawRect(beside.frameColor()) } else Modifier)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .nestedScroll(fabScroll.nestedScrollConnection),
        topBar = {
            ShellTopAppBar(
                style = resolvedStyle,
                title = title,
                navigationIcon = {
                    if (!topLevel) {
                        AnimatedIconButtonDirection(
                            icon = ToolkitIcon.Vector(Icons.AutoMirrored.Filled.ArrowBack),
                            contentDescription = stringResource(R.string.go_back),
                            onClick = { if (pageKey != null) navigator.close(pageKey) else navigator.goBack() },
                        )
                    }
                },
                actions = actions,
                scrollBehavior = scrollBehavior,
                // Over the shared frame colour, as the tabs' bar is.
                colors = if (carded) {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                    )
                } else {
                    null
                },
            )
        },
        containerColor = if (carded) Color.Transparent else MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ToolkitFabColumn(fabs + fabHost.fabs, expanded = fabScroll.expanded)
                floatingActionButton()
            }
        },
        snackbarHost = { DefaultSnackbarHost(snackbarState = snackbarHostState) },
        contentWindowInsets = contentInsets,
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding.withoutBottom())
                .consumeWindowInsets(padding)
                .then(if (carded) Modifier.clip(ContentCardShape).background(MaterialTheme.colorScheme.surface) else Modifier),
        ) {
            CompositionLocalProvider(
                LocalContentPadding provides PaddingValues(bottom = padding.calculateBottomPadding()),
                LocalPageSnackbarHostState provides snackbarHostState,
                LocalFabHost provides fabHost,
                content = content,
            )
        }
    }
}
