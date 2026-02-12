/*
 * Copyright (c) 2026 Mihai-Cristian Condrea
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

/*
 * Copyright (c) $2026 Mihai-Cristian Condrea
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

package com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules

import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.blue.bluePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.green.greenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.red.redPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.yellow.yellowPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.monochrome.monochromePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.rose.rosePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.special.christmas.christmasPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.special.skin.skinPalette
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val themeModule: Module = module {
    single<ColorPalette>(named("monochromePalette")) { monochromePalette }
    single<ColorPalette>(named("bluePalette")) { bluePalette }
    single<ColorPalette>(named("greenPalette")) { greenPalette }
    single<ColorPalette>(named("redPalette")) { redPalette }
    single<ColorPalette>(named("yellowPalette")) { yellowPalette }
    single<ColorPalette>(named("rosePalette")) { rosePalette }
    single<ColorPalette>(named("christmasPalette")) { christmasPalette }
    single<ColorPalette>(named("skinPalette")) { skinPalette }
    single<ColorPalette> { get(named("bluePalette")) }
}
