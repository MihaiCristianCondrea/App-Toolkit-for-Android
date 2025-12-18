package com.d4rk.android.apps.apptoolkit.app.apps.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.AppInfoHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun buildOnAppClick(
    dispatchers: DispatcherProvider = koinInject(), // FIXME: Parameter 'dispatchers' has runtime-determined stability
): (AppInfo) -> Unit {
    val context = LocalContext.current
    val currentContext by rememberUpdatedState(newValue = context)
    val appInfoHelper = remember(dispatchers) { AppInfoHelper(dispatchers) }
    val coroutineScope = rememberCoroutineScope()
    return remember(appInfoHelper, coroutineScope) {
        { appInfo ->
            coroutineScope.launch {
                val ctx = currentContext
                if (appInfo.packageName.isNotEmpty()) {
                    if (appInfoHelper.isAppInstalled(ctx, appInfo.packageName)) {
                        if (!appInfoHelper.openApp(ctx, appInfo.packageName)) {
                            IntentsHelper.openPlayStoreForApp(ctx, appInfo.packageName)
                        }
                    } else {
                        IntentsHelper.openPlayStoreForApp(ctx, appInfo.packageName)
                    }
                }
            }
        }
    }
}

@Composable
fun buildOnShareClick(): (AppInfo) -> Unit {
    val context = LocalContext.current
    val currentContext by rememberUpdatedState(newValue = context)
    return remember(currentContext) {
        { appInfo ->
            IntentsHelper.shareApp(
                context = currentContext,
                shareMessageFormat = R.string.summary_share_message,
                packageName = appInfo.packageName
            )
        }
    }
}
