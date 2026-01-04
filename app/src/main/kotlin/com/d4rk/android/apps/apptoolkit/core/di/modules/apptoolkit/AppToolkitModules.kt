package com.d4rk.android.apps.apptoolkit.core.di.modules.apptoolkit

val appToolkitModules =
    listOf( // FIXME: <html>Conflicting declarations:<br/>val appToolkitModules: List&lt;ERROR CLASS: Cannot infer argument for type parameter T&gt;
    appToolkitCoreModule,
    supportModule,
    helpModule,
    issueReporterModule
)
