package com.d4rk.android.apps.apptoolkit.core.di.modules.apptoolkit

import org.koin.core.module.Module

val appToolkitModules: List<Module> = listOf(
    appToolkitCoreModule,
    supportModule,
    helpModule,
    issueReporterModule,
)
