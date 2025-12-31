package com.d4rk.android.apps.apptoolkit.core.di.modules

import android.content.Context
import com.d4rk.android.apps.apptoolkit.R
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val startupModule: Module = module {
    single<List<String>>(qualifier = named(name = "startup_entries")) {
        get<Context>().resources.getStringArray(R.array.preference_startup_entries).toList()
    }

    single<List<String>>(qualifier = named(name = "startup_values")) {
        get<Context>().resources.getStringArray(R.array.preference_startup_values).toList()
    }
}
