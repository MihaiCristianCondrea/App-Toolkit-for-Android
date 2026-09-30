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


package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

/**
 * Set by the shell around the destinations it shows beside a rail or a permanent drawer: the
 * scope their app bar titles share. Null elsewhere.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalBesideNavigationTransitions = staticCompositionLocalOf<SharedTransitionScope?> { null }

/**
 * The modifier for the title of an app bar that stands in for the tab's beside the navigation:
 * the tabs' own bar, and the bar of the page the navigation opened. When one such destination
 * replaces another in place, the old title grows or shrinks into the new one, as a title changing
 * inside one bar does, instead of the new bar's title appearing at its own size.
 *
 * Use it on one title per bar at most, and not on a large bar, which draws its title twice.
 * Anywhere but beside the navigation it is an empty modifier.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun besideNavigationTitle(): Modifier {
    val transitions = LocalBesideNavigationTransitions.current ?: return Modifier
    if (LocalBesideNavigation.current == null) return Modifier
    val destination = LocalNavAnimatedContentScope.current
    return with(transitions) {
        Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = BesideNavigationTitleKey),
            animatedVisibilityScope = destination,
            enter = fadeIn(),
            exit = fadeOut(),
            // Laid out again at each size on the way, so the text ellipsises rather than
            // stretching, the way `animateContentSize` resizes it.
            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
        )
    }
}

private object BesideNavigationTitleKey
