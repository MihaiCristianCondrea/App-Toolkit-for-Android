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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.transformers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeveloperOptionsTest {

    private fun capabilities(
        tabs: Int,
        startOptions: Int = tabs,
        banner: Boolean = false,
        player: Boolean = false,
        framedPages: Boolean = true,
        backNavigation: Boolean = true,
        bottomBar: Boolean = true,
        wide: Boolean = true,
        widthLimit: Boolean = false,
    ) = ShellCapabilities(
        tabCount = tabs,
        startOptionCount = startOptions,
        declaresBanner = banner,
        declaresPlayer = player,
        hasFramedPages = framedPages,
        hasBackNavigation = backNavigation,
        layoutReachesBottomBar = bottomBar,
        layoutReachesWideNavigation = wide,
        hasContentWidthLimit = widthLimit,
    )

    private fun categoriesOf(options: List<DeveloperOption>) =
        options.asSequence().map { it.category }.distinct().toList()

    @Test
    fun `app A, three tabs with a width limit, offers every override but the accessories`() {
        val options = availableDeveloperOptions(capabilities = capabilities(tabs = 3, widthLimit = true))

        assertEquals(DeveloperOption.entries - DeveloperOption.BottomAccessory, options)
        assertFalse(DeveloperCategory.Accessories in categoriesOf(options = options))
    }

    @Test
    fun `app B, one tab, has no tab transition, width or accessory override`() {
        val options = availableDeveloperOptions(capabilities = capabilities(tabs = 1))

        assertFalse(DeveloperOption.TabTransition in options)
        assertFalse(DeveloperOption.ContentWidth in options)
        assertFalse(DeveloperOption.BottomAccessory in options)
        assertFalse(DeveloperOption.StartOverride in options)
        assertTrue(DeveloperOption.NavigationBarStyle in options, "a single tab still draws its bar")
    }

    @Test
    fun `app C, pages only with custom frames, has no navigation or bar overrides`() {
        val options = availableDeveloperOptions(
            capabilities = capabilities(tabs = 0, startOptions = 1, framedPages = false, banner = true),
        )

        assertEquals(
            listOf(
                DeveloperOption.BackEdge,
                DeveloperOption.AnimationSpeed,
                DeveloperOption.EntranceStyle,
                DeveloperOption.MotionTempo,
                DeveloperOption.EntranceReplay,
            ),
            options,
        )
        assertEquals(
            listOf(DeveloperCategory.Navigation, DeveloperCategory.Motion),
            categoriesOf(options = options),
        )
    }

    @Test
    fun `a hidden single tab bar has no bar or banner options but retains the layout option`() {
        val declared = capabilities(tabs = 1).copy(declaresBanner = true, hideSingleTabBottomBar = true)
        val options = availableDeveloperOptions(capabilities = declared)

        assertFalse(DeveloperOption.NavigationBarStyle in options)
        assertFalse(DeveloperOption.HideBottomBarOnScroll in options)
        assertFalse(DeveloperOption.BottomAccessory in options)
        assertTrue(DeveloperOption.Layout in options)
        assertTrue(DeveloperOption.NavigationTint in options)
        assertTrue(
            DeveloperOption.NavigationBarStyle in availableDeveloperOptions(capabilities = declared.copy(tabCount = 2)),
        )
    }

    @Test
    fun `app C with framed pages can still force their app bars`() {
        val options = availableDeveloperOptions(capabilities = capabilities(tabs = 0, startOptions = 1, framedPages = true))

        assertTrue(DeveloperOption.TopBarStyle in options)
        assertTrue(DeveloperOption.HideTopBarOnScroll in options)
        assertFalse(DeveloperOption.Layout in options)
    }

    @Test
    fun `app D, several tabs with a banner and a player, offers every override`() {
        val options = availableDeveloperOptions(
            capabilities = capabilities(tabs = 4, banner = true, player = true, widthLimit = true),
        )

        assertEquals(DeveloperOption.entries, options)
        assertEquals(DeveloperCategory.entries, categoriesOf(options = options))
    }

    @Test
    fun `app F, tabs whose policy never shows a bottom bar, keeps the bottom bar overrides a forced layout reaches`() {
        val options = availableDeveloperOptions(capabilities = capabilities(tabs = 3, bottomBar = false, wide = true))

        assertTrue(DeveloperOption.NavigationBarStyle in options)
        assertTrue(DeveloperOption.HideBottomBarOnScroll in options)
        assertTrue(DeveloperOption.NavigationTint in options)
    }

    @Test
    fun `the start override needs more than one place to start`() {
        assertFalse(
            DeveloperOption.StartOverride in availableDeveloperOptions(
                capabilities = capabilities(tabs = 1, startOptions = 1),
            ),
        )
        assertTrue(
            DeveloperOption.StartOverride in availableDeveloperOptions(
                capabilities = capabilities(tabs = 1, startOptions = 3),
            ),
        )
    }

    @Test
    fun `accessory choices follow what the app declares`() {
        assertFalse(DeveloperOption.BottomAccessory in availableDeveloperOptions(capabilities = capabilities(tabs = 2)))

        val banner = capabilities(tabs = 2, banner = true)
        val player = capabilities(tabs = 2, player = true)
        val both = capabilities(tabs = 2, banner = true, player = true)
        val onOff = listOf(AccessoryMode.AsDeclared, AccessoryMode.None)

        assertTrue(DeveloperOption.BottomAccessory in availableDeveloperOptions(capabilities = banner))
        assertEquals(onOff, accessoryModes(capabilities = banner))
        assertEquals(onOff, accessoryModes(capabilities = player))
        assertEquals(AccessoryMode.entries, accessoryModes(capabilities = both))
    }

    @Test
    fun `effectiveAccessoryMode normalizes single-accessory selections onto offered choices`() {
        val both = capabilities(tabs = 2, banner = true, player = true)
        val bannerOnly = capabilities(tabs = 2, banner = true)
        val playerOnly = capabilities(tabs = 2, player = true)
        val neither = capabilities(tabs = 2)

        AccessoryMode.entries.forEach { mode ->
            assertEquals(mode, effectiveAccessoryMode(mode = mode, capabilities = both))
        }

        assertEquals(AccessoryMode.AsDeclared, effectiveAccessoryMode(mode = AccessoryMode.AsDeclared, capabilities = bannerOnly))
        assertEquals(AccessoryMode.AsDeclared, effectiveAccessoryMode(mode = AccessoryMode.BannerOnly, capabilities = bannerOnly))
        assertEquals(AccessoryMode.None, effectiveAccessoryMode(mode = AccessoryMode.PlayerOnly, capabilities = bannerOnly))
        assertEquals(AccessoryMode.None, effectiveAccessoryMode(mode = AccessoryMode.None, capabilities = bannerOnly))

        assertEquals(AccessoryMode.AsDeclared, effectiveAccessoryMode(mode = AccessoryMode.AsDeclared, capabilities = playerOnly))
        assertEquals(AccessoryMode.AsDeclared, effectiveAccessoryMode(mode = AccessoryMode.PlayerOnly, capabilities = playerOnly))
        assertEquals(AccessoryMode.None, effectiveAccessoryMode(mode = AccessoryMode.BannerOnly, capabilities = playerOnly))
        assertEquals(AccessoryMode.None, effectiveAccessoryMode(mode = AccessoryMode.None, capabilities = playerOnly))

        assertEquals(AccessoryMode.None, effectiveAccessoryMode(mode = AccessoryMode.AsDeclared, capabilities = neither))
    }

    @Test
    fun `developerOptionCategories omits empty headings and preserves display order`() {
        val grouped = developerOptionCategories(
            capabilities = capabilities(tabs = 0, startOptions = 1, framedPages = false, backNavigation = false),
        )

        assertEquals(listOf(DeveloperCategory.Motion), grouped.map { it.first })
        assertEquals(
            listOf(
                DeveloperOption.AnimationSpeed,
                DeveloperOption.EntranceStyle,
                DeveloperOption.MotionTempo,
                DeveloperOption.EntranceReplay,
            ),
            grouped.single().second,
        )
    }

    @Test
    fun `options keep the page's order, grouped under their headings`() {
        val categories = availableDeveloperOptions(
            capabilities = capabilities(tabs = 4, banner = true, player = true, widthLimit = true),
        ).map { it.category }

        assertEquals(categories.sortedBy { it.ordinal }, categories)
    }
}
