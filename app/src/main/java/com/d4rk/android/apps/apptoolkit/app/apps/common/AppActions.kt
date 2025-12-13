package com.d4rk.android.apps.apptoolkit.app.apps.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.AppInfoHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import kotlinx.coroutines.launch

@Composable
fun buildOnAppClick(
    dispatchers: DispatcherProvider,
    context: Context
): (AppInfo) -> Unit { // FIXME: Parameter 'dispatchers' has runtime-determined stability && Parameter 'context' has runtime-determined stability
    val appInfoHelper = remember { AppInfoHelper(dispatchers) }
    val coroutineScope = rememberCoroutineScope()
    return remember(context, coroutineScope) {
        { appInfo ->
            coroutineScope.launch {
                if (appInfo.packageName.isNotEmpty()) {
                    if (appInfoHelper.isAppInstalled(context, appInfo.packageName)) {
                        if (!appInfoHelper.openApp(context, appInfo.packageName)) {
                            IntentsHelper.openPlayStoreForApp(context, appInfo.packageName)
                        }
                    } else {
                        IntentsHelper.openPlayStoreForApp(context, appInfo.packageName)
                    }
                }
            }
        }
    }
}

@Composable
fun buildOnShareClick(context: Context): (AppInfo) -> Unit =
    // FIXME: Parameter 'context' has runtime-determined stability
    remember(context) {
        { appInfo ->
            IntentsHelper.shareApp(
                context = context,
                shareMessageFormat = R.string.summary_share_message,
                packageName = appInfo.packageName
            )
        }
    }
