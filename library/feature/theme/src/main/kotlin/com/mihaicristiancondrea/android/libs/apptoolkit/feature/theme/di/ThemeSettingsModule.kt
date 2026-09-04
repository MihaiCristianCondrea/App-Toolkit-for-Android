package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.blue.bluePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.green.greenPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.red.redPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.yellow.yellowPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.monochrome.monochromePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.rose.rosePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.special.christmas.christmasPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.special.skin.skinPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.ThemeSettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val themeSettingsModule: Module = module {
    viewModel { ThemeSettingsViewModel(preferences = get()) }

    single<ColorPalette>(named(AppToolkitDiConstants.MONOCHROME_THEME_PALETTE)) { monochromePalette }
    single<ColorPalette>(named(AppToolkitDiConstants.BLUE_THEME_PALETTE)) { bluePalette }
    single<ColorPalette>(named(AppToolkitDiConstants.GREEN_THEME_PALETTE)) { greenPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.RED_THEME_PALETTE)) { redPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.YELLOW_THEME_PALETTE)) { yellowPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.ROSE_THEME_PALETTE)) { rosePalette }
    single<ColorPalette>(named(AppToolkitDiConstants.CHRISTMAS_THEME_PALETTE)) { christmasPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.SKIN_THEME_PALETTE)) { skinPalette }
    single<ColorPalette> {
        getOrNull<ColorPalette>(named(AppToolkitDiConstants.DEFAULT_THEME_PALETTE))
            ?: get(named(AppToolkitDiConstants.BLUE_THEME_PALETTE))
    }
}
