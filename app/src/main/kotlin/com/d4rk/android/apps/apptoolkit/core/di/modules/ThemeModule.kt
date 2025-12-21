package com.d4rk.android.apps.apptoolkit.core.di.modules

import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.blue.bluePalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.green.greenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.monochrome.monochromePalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.red.redPalette
import com.d4rk.android.libs.apptoolkit.app.theme.style.colors.yellow.yellowPalette
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val themeModule: Module = module {
    single(named("monochromePalette")) { monochromePalette }
    single(named("bluePalette")) { bluePalette }
    single(named("greenPalette")) { greenPalette }
    single(named("redPalette")) { redPalette }
    single(named("yellowPalette")) { yellowPalette }

    single<ColorPalette> { get(named("bluePalette")) }
}
