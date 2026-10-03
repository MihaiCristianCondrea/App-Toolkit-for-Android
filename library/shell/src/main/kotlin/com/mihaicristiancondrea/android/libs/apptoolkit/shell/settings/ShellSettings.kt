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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle

/** Replaces every destination's own app bar style, to try one style across the whole app. */
enum class TopBarOverride(val style: TopBarStyle?) {
    AsDeclared(null),
    Small(TopBarStyle.Small),
    CenterAligned(TopBarStyle.CenterAligned),
    Large(TopBarStyle.Large),
    LargeCollapsed(TopBarStyle.LargeCollapsed),
    Hidden(TopBarStyle.Hidden),
}

/** Which of the app's bottom accessories the shell shows. */
enum class AccessoryMode(val showsBanner: Boolean, val showsPlayer: Boolean) {
    AsDeclared(showsBanner = true, showsPlayer = true),
    None(showsBanner = false, showsPlayer = false),
    BannerOnly(showsBanner = true, showsPlayer = false),
    PlayerOnly(showsBanner = false, showsPlayer = true),
}

/** Which Material navigation bar the bottom bar layout uses. */
enum class NavigationBarStyle {
    /** Material 3's `NavigationBar`: 80dp, labels under the icons. */
    Standard,

    /** Material 3 Expressive's `ShortNavigationBar`: 64dp, more compact. */
    Short,
}

/**
 * Whether the rail or permanent drawer and the app bar share one container colour on wide
 * windows, framing the content as a card.
 */
enum class NavigationTint {
    /** The navigation and app bar are the surface colour; the app bar tints as content scrolls. */
    None,

    /** Always tinted, with the content in a rounded card beside them. */
    Always,

    /** Tinted together, rail included, once content scrolls under the app bar. */
    OnScroll,
}

/** How a back swipe from the right edge moves the page. */
enum class BackEdgeStyle {
    /**
     * The default: a swipe from the right mirrors one from the left, so the page follows the
     * finger to the left edge.
     */
    FollowFinger,

    /**
     * Exactly as Android animates activities: from the right edge the page shrinks in place,
     * without following the finger sideways.
     */
    System,
}

/** Slows every shell transition down, to watch the back animation frame by frame. */
enum class AnimationSpeed(val durationScale: Float) {
    Normal(1f),
    Slow(2f),
    VerySlow(5f),
}

/**
 * The shell's persisted overrides: how it lays out, moves and draws its chrome.
 *
 * Every one is a developer option, offered by `:library:feature:developer` once unlocked, to
 * force, compare and exercise the shell's variations. The defaults leave the app exactly as its
 * graph and layout policy declare it, and `ShellPreferences.resetDeveloperOptions` returns to them.
 * The person's own preferences, such as the theme or the navigation labels, are not kept here.
 */
@Immutable
data class ShellSettings(
    val layoutMode: ShellLayoutMode = ShellLayoutMode.Auto,
    val topBarOverride: TopBarOverride = TopBarOverride.AsDeclared,
    val tabTransition: TabTransitionStyle = TabTransitionStyle.Directional,
    val accessoryMode: AccessoryMode = AccessoryMode.AsDeclared,
    val hideBottomBarOnScroll: Boolean = true,
    /** Whether content keeps to the layout policy's maximum width on large windows. */
    val limitContentWidth: Boolean = true,
    val animationSpeed: AnimationSpeed = AnimationSpeed.Normal,
    val backEdgeStyle: BackEdgeStyle = BackEdgeStyle.FollowFinger,
    /** Where the next launch opens, as an index into the graph's start options; -1 as declared. */
    val startOverride: Int = -1,
    val navigationBarStyle: NavigationBarStyle = NavigationBarStyle.Standard,
    val navigationTint: NavigationTint = NavigationTint.Always,
    /**
     * Whether the app bar slides away as content scrolls down, whatever its style, and comes back
     * as soon as it scrolls up. A large bar collapses first, and the bar leaves entirely, from under
     * the status bar too, so content scrolls on beneath it.
     */
    val hideTopBarOnScroll: Boolean = false,
)
