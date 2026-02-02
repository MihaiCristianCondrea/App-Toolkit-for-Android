package com.d4rk.android.apps.apptoolkit.core.data.local

import android.content.Context
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider

class DataStore(
    context: Context,
    dispatchers: DispatcherProvider,
    defaultAdsEnabled: Boolean = !BuildConfig.DEBUG,
) : CommonDataStore(context, dispatchers, defaultAdsEnabled)