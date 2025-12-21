package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * A Composable that displays a vertical navigation rail on the left side of the screen,
 * typically used for top-level destinations in larger screen layouts like tablets.
 * It can be in an expanded or collapsed state and contains a list of primary (bottom)
 * and secondary (drawer) navigation items. The main content of the screen is displayed
 * to the right of the rail.
 *
 * The rail's width animates between a collapsed state (icon only) and an expanded
 * state (icon and label). The visibility of the labels also animates accordingly.
 *
 * @param bottomItems A list of [BottomBarItem]s to be displayed as primary navigation
 *   destinations at the top of the rail.
 * @param drawerItems A list of [NavigationDrawerItem]s to be displayed as secondary
 *   navigation items at the bottom of the rail.
 * @param currentRoute The route of the currently displayed screen, used to highlight
 *   the selected [BottomBarItem].
 * @param isRailExpanded A boolean to control the expanded/collapsed state of the navigation rail.
 *   `true` for expanded (shows labels), `false` for collapsed (shows only icons).
 * @param onBottomItemClick A lambda function to be invoked when a [BottomBarItem] is clicked.
 *   It is only called if the item is not already selected.
 * @param onDrawerItemClick A lambda function to be invoked when a [NavigationDrawerItem] is clicked.
 * @param centerContent A float value between 0.0 and 1.0 that specifies the width fraction
 *   the main content should occupy within its available space. Defaults to `1f` (100%).
 * @param content The main screen content to be displayed to the right of the navigation rail.
 *   This is a Composable lambda that will be placed inside a `BoxScope`.
 */
@Composable
fun LeftNavigationRail(
    bottomItems: ImmutableList<BottomBarItem> = persistentListOf(),
    drawerItems: ImmutableList<NavigationDrawerItem> = persistentListOf(),
    currentRoute: String?,
    isRailExpanded: Boolean = false,
    paddingValues: PaddingValues,
    onBottomItemClick: (BottomBarItem) -> Unit = {},
    onDrawerItemClick: (NavigationDrawerItem) -> Unit = {},
    centerContent: Float = 1f,
    content: @Composable BoxScope.() -> Unit,
) {
    val railWidth: Dp by animateDpAsState(
        targetValue = if (isRailExpanded) SizeConstants.TwoHundredSize else SizeConstants.SeventyTwoSize,
        animationSpec = tween(durationMillis = 300)
    )
    val textEntryAnimation: EnterTransition =
        fadeIn(animationSpec = tween(durationMillis = 300)) + expandHorizontally() + expandVertically()
    val textExitAnimation: ExitTransition =
        fadeOut(animationSpec = tween(durationMillis = 300)) + shrinkHorizontally() + shrinkVertically()

    Row(
        modifier = Modifier
            .fillMaxHeight()
            .padding(paddingValues = paddingValues)
    ) {
        NavigationRail(
            modifier = Modifier
                .width(width = railWidth)
                .fillMaxHeight()
                .verticalScroll(state = rememberScrollState())
        ) {
            bottomItems.forEach { item: BottomBarItem ->
                val isSelected: Boolean = currentRoute == item.route
                NavigationRailItem(
                    modifier = Modifier.bounceClick(),
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            onBottomItemClick(item)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.icon,
                            contentDescription = stringResource(item.title),
                            modifier = Modifier.bounceClick()
                        )
                    },
                    label = {
                        AnimatedVisibility(
                            visible = isRailExpanded,
                            enter = textEntryAnimation,
                            exit = textExitAnimation
                        ) {
                            Text(
                                text = stringResource(id = item.title),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.weight(weight = 1f))
            drawerItems.forEach { item: NavigationDrawerItem ->
                NavigationRailItem(
                    selected = false,
                    onClick = { onDrawerItemClick(item) },
                    icon = {
                        Icon(
                            imageVector = item.selectedIcon,
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier.bounceClick()
                        )
                    },
                    label = {
                        AnimatedVisibility(
                            visible = isRailExpanded,
                            enter = textEntryAnimation,
                            exit = textExitAnimation
                        ) {
                            Text(text = stringResource(id = item.title))
                        }
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxWidth(fraction = centerContent)) {
                content()
            }
        }
    }
}
