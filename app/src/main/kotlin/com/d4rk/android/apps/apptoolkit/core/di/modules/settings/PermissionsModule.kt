package com.d4rk.android.apps.apptoolkit.core.di.modules.settings

import com.d4rk.android.libs.apptoolkit.app.permissions.data.repository.PermissionsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.PermissionsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val permissionsModule: Module =
    module { // FIXME: <html>Conflicting declarations:<br/>val permissionsModule: &lt;implicit&gt;
    single<PermissionsRepository> {
        PermissionsRepositoryImpl(
            context = get(),
            dispatchers = get()
        )
    }
    viewModel {
        PermissionsViewModel(
            permissionsRepository = get(),
        )
    }
}
