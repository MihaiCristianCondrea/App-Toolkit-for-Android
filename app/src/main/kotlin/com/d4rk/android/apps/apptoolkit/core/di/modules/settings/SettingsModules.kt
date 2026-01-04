package com.d4rk.android.apps.apptoolkit.core.di.modules.settings

import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.aboutModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.advancedSettingsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.generalSettingsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.permissionsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.settingsRootModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.usageAndDiagnosticsModule
import org.koin.core.module.Module

val settingsModules: List<Module> = listOf(
    settingsRootModule,
    aboutModule,
    advancedSettingsModule,
    generalSettingsModule,
    permissionsModule,
    usageAndDiagnosticsModule,
)
