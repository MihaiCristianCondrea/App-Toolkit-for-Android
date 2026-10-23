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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openPlayStoreForApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.shareApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.AppLauncher
import kotlinx.coroutines.launch

@Composable
fun rememberOpenAppAction(
    appLauncher: AppLauncher,
    isInstalled: suspend (String) -> Boolean,
): (AppSummary) -> Unit {
    val context = LocalContext.current
    val currentContext by rememberUpdatedState(newValue = context)
    val currentIsInstalled by rememberUpdatedState(isInstalled)
    val coroutineScope = rememberCoroutineScope()
    return remember(appLauncher, coroutineScope) {
        { appInfo ->
            coroutineScope.launch {
                val context = currentContext
                if (appInfo.packageName.isNotEmpty()) {
                    val isInstalled = currentIsInstalled(appInfo.packageName)
                    if (isInstalled) {
                        if (!appLauncher.openApp(context, appInfo.packageName)) {
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
fun rememberShareAppAction(): (AppSummary) -> Unit {
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
