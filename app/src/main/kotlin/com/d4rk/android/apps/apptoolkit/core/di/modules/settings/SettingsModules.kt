package com.d4rk.android.apps.apptoolkit.core.di.modules.settings

import org.koin.core.module.Module

val settingsModules: List<Module> = listOf(
    settingsRootModule,
    aboutModule,
    advancedSettingsModule,
    generalSettingsModule,
    permissionsModule,
    usageAndDiagnosticsModule,
)
