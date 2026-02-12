/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openPlayStoreForApp
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.shareApp
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.packagemanager.isAppInstalled
import com.d4rk.android.libs.apptoolkit.core.utils.platform.AppInfoHelper
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun buildOnAppClick(
    dispatchers: DispatcherProvider = koinInject(),
): (AppInfo) -> Unit {
    val context = LocalContext.current
    val currentContext by rememberUpdatedState(newValue = context)
    val appInfoHelper = remember(dispatchers) { AppInfoHelper(dispatchers) }
    val coroutineScope = rememberCoroutineScope()
    return remember(appInfoHelper, coroutineScope) {
        { appInfo ->
            coroutineScope.launch {
                val context = currentContext
                if (appInfo.packageName.isNotEmpty()) {
                    if (context.isAppInstalled(appInfo.packageName)) {
                        if (!appInfoHelper.openApp(context, appInfo.packageName)) {
                            context.openPlayStoreForApp(appInfo.packageName)
                        }
                    } else {
                        context.openPlayStoreForApp(appInfo.packageName)
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
            currentContext.shareApp(
                shareMessageFormat = R.string.summary_share_message,
                packageName = appInfo.packageName
            )
        }
    }
}
