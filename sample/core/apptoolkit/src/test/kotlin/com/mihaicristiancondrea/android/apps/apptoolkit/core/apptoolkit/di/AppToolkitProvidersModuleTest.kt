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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import org.junit.jupiter.api.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication

/**
 * Checks the host-wide toolkit answers this module still owns.
 *
 * The settings providers moved to `:sample:feature:settings` with the surfaces they configure, and
 * `SettingsModuleTest` covers them there.
 */
class AppToolkitProvidersModuleTest {

    @Test
    fun `resolves the default theme palette`() {
        val koin = koinApplication {
            modules(appToolkitProvidersModule())
        }.koin

        koin.get<ColorPalette>(named(AppToolkitDiConstants.DEFAULT_THEME_PALETTE))
    }
}
