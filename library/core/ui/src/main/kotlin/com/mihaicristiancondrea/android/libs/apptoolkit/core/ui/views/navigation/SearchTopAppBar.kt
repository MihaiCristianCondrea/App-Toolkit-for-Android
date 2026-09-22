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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldStyle

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR

/** Height the search field is pinned to, so the bar does not grow when the field appears. */
private val SearchFieldHeight = SizeConstants.FortyFourSize + SizeConstants.ExtraSmallSize

/**
 * A top app bar whose title slot swaps for a search field.
 *
 * It is the [MainTopAppBar] of a screen that filters what is behind it: the same navigation icon,
 * overflow action and destination actions, with the title crossfading into a
 * [GeneralTextFieldStyle.Search] field while [showSearch] is true. The bar owns only that swap; the
 * query and whether search is showing stay with the caller, so the same state drives the bar and
 * the filtering underneath it.
 *
 * Filters are optional and go in [filters], the row that sits inside the field, to the left of the
 * clear button. [SearchFilterAction] is the ready-made toggle for one: it renders tonally while the
 * filter is applied and as text while it is not, so an active sort or filter reads as active
 * without a second surface. Actions that belong to the bar rather than to the query go in [actions].
 *
 * The Support overflow item is rendered only when the host supplies [onSupportClick], matching
 * [MainTopAppBar], so this module stays free of any dependency on a support feature.
 *
 * @param title Shown while [showSearch] is false; defaults to the host's app name.
 * @param navigationIcon The [ImageVector] drawn as the navigation icon, or null for none.
 * @param onNavigationIconClick Invoked when the navigation icon is clicked.
 * @param showSearch Whether the search field replaces the title.
 * @param searchQuery The current query.
 * @param onSearchQueryChange Receives every edit, including the clear button's empty string.
 * @param modifier The [Modifier] applied to the bar.
 * @param searchPlaceholder Shown while the field is empty.
 * @param clearSearchContentDescription Accessibility description of the clear button.
 * @param centerTitle Whether the title is centered; the search field always fills the slot.
 * @param onSearch Called when the keyboard's search action is used. A filter that already applied
 *   itself as the query was typed needs nothing here.
 * @param filters Row placed inside the search field, before the clear button. Empty by default.
 * @param actions Destination-owned app bar actions, drawn after the Support overflow.
 * @param onSupportClick Host callback that opens Support. The overflow action is hidden when it is
 *   null, so hosts without a support surface get no dead menu entry.
 * @param showSupportAction Whether to render the Support overflow action when a callback exists.
 * @param scrollBehavior Applied to the bar, defining how it behaves as content is scrolled.
 * @param windowInsets Insets the bar consumes.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchTopAppBar(
    navigationIcon: ImageVector?,
    onNavigationIconClick: () -> Unit,
    showSearch: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(id = CommonR.string.app_name),
    searchPlaceholder: String = stringResource(id = R.string.search),
    clearSearchContentDescription: String = stringResource(id = R.string.clear_search),
    centerTitle: Boolean = false,
    onSearch: ((String) -> Unit)? = null,
    filters: @Composable RowScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    onSupportClick: (() -> Unit)? = null,
    showSupportAction: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    // The title animates in on the first composition that is not already searching, so a screen that
    // opens straight into search does not flash its title first.
    val titleEverShown = rememberSaveable { mutableStateOf(value = false) }

    LaunchedEffect(key1 = Unit) {
        if (!showSearch) titleEverShown.value = true
    }

    val titleSlot: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                ),
            contentAlignment = if (centerTitle) Alignment.Center else Alignment.CenterStart,
        ) {
            AnimatedVisibility(
                visible = showSearch,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 180, delayMillis = 40),
                ) + scaleIn(
                    initialScale = 0.96f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                ) + expandHorizontally(
                    expandFrom = Alignment.CenterHorizontally,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 120),
                ) + scaleOut(
                    targetScale = 0.98f,
                    animationSpec = tween(durationMillis = 120),
                ) + shrinkHorizontally(
                    shrinkTowards = Alignment.CenterHorizontally,
                    animationSpec = tween(durationMillis = 160),
                ),
            ) {
                GeneralTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .height(height = SearchFieldHeight)
                        .fillMaxWidth(),
                    style = GeneralTextFieldStyle.Search,
                    placeholder = searchPlaceholder,
                    textStyle = MaterialTheme.typography.labelMedium,
                    onSearch = onSearch,
                    trailingContent = {
                        filters()

                        AnimatedVisibility(
                            visible = searchQuery.isNotEmpty(),
                            enter = SearchActionEnter,
                            exit = SearchActionExit,
                        ) {
                            GeneralButton(
                                onClick = { onSearchQueryChange("") },
                                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Close),
                                contentDescription = clearSearchContentDescription,
                                iconSize = SizeConstants.ButtonIconSize,
                            )
                        }
                    },
                )
            }

            AnimatedVisibility(
                visible = !showSearch && titleEverShown.value,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 200, delayMillis = 60),
                ) + scaleIn(
                    initialScale = 0.96f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 120),
                ) + scaleOut(
                    targetScale = 0.98f,
                    animationSpec = tween(durationMillis = 120),
                ),
            ) {
                Text(
                    text = title,
                    textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                )
            }
        }
    }

    val navigationSlot: @Composable () -> Unit = {
        navigationIcon?.let { icon ->
            AnimatedIconButtonDirection(
                icon = ToolkitIcon.Vector(imageVector = icon),
                contentDescription = stringResource(id = R.string.go_back),
                onClick = onNavigationIconClick,
                feedback = ButtonFeedback(hapticFeedbackType = null),
                iconSize = SizeConstants.TwentyFourSize,
            )
        }
    }

    val actionsSlot: @Composable RowScope.() -> Unit = {
        if (showSupportAction && onSupportClick != null) {
            SupportMenuAction(onSupportClick = onSupportClick)
        }
        actions()
    }

    if (centerTitle) {
        CenterAlignedTopAppBar(
            title = titleSlot,
            modifier = modifier,
            navigationIcon = navigationSlot,
            actions = actionsSlot,
            scrollBehavior = scrollBehavior,
            windowInsets = windowInsets,
        )
    } else {
        TopAppBar(
            title = titleSlot,
            modifier = modifier,
            navigationIcon = navigationSlot,
            actions = actionsSlot,
            scrollBehavior = scrollBehavior,
            windowInsets = windowInsets,
        )
    }
}

/**
 * A filter or sort toggle for the [SearchTopAppBar] search field.
 *
 * It renders tonally while [active] and as text while it is not, which is what tells a person a
 * filter is still narrowing the results they are looking at. [visible] animates the whole control
 * in and out, for a filter that only applies to some of the content the screen can show.
 *
 * @param contentDescription Accessibility description of the toggle; defaults to "Sort by".
 * @param onClick Invoked when the toggle is clicked. Opening a menu and applying a filter directly
 *   are both callers' business; the toggle only reports the click.
 * @param modifier The [Modifier] applied to the toggle.
 * @param icon Drawn while the filter is not applied.
 * @param activeIcon Drawn while it is; defaults to [icon].
 * @param active Whether the filter is currently narrowing the results.
 * @param visible Whether the toggle is shown at all.
 */
@Composable
fun SearchFilterAction(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    activeIcon: ImageVector = icon,
    contentDescription: String = stringResource(id = R.string.sort_by),
    active: Boolean = false,
    visible: Boolean = true,
) {
    AnimatedVisibility(
        visible = visible,
        enter = SearchActionEnter,
        exit = SearchActionExit,
    ) {
        GeneralButton(
            onClick = onClick,
            modifier = modifier,
            style = if (active) GeneralButtonStyle.Tonal else GeneralButtonStyle.Text,
            icon = ToolkitIcon.Vector(imageVector = if (active) activeIcon else icon),
            contentDescription = contentDescription,
            iconSize = SizeConstants.ButtonIconSize,
        )
    }
}

private val SearchActionEnter = fadeIn(
    animationSpec = tween(durationMillis = 120),
) + scaleIn(
    initialScale = 0.88f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium,
    ),
)

private val SearchActionExit = fadeOut(
    animationSpec = tween(durationMillis = 90),
) + scaleOut(
    targetScale = 0.88f,
    animationSpec = tween(durationMillis = 90),
)
