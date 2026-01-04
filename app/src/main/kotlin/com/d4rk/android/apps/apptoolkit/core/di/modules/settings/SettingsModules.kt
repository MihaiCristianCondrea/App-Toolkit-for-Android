package com.d4rk.android.apps.apptoolkit.core.di.modules.settings

val settingsModules =
    listOf( // FIXME: <html>Conflicting declarations:<br/>val settingsModules: List&lt;Module&gt;
    settingsRootModule,
        aboutModule, // FIXME: <html>Overload resolution ambiguity between candidates:<br/>val aboutModule: &lt;implicit&gt;<br/>val aboutModule: Module
    advancedSettingsModule,
    generalSettingsModule,
        permissionsModule, // FIXME: <html>Overload resolution ambiguity between candidates:<br/>val permissionsModule: &lt;implicit&gt;<br/>val permissionsModule: Module
        usageAndDiagnosticsModule // FIXME: <html>Overload resolution ambiguity between candidates:<br/>val usageAndDiagnosticsModule: Module<br/>val usageAndDiagnosticsModule: &lt;implicit&gt;
)
