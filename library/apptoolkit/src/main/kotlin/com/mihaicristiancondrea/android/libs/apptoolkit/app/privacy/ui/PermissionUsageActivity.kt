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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.privacy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.mihaicristiancondrea.android.libs.apptoolkit.app.privacy.ui.navigation.permissionUsageGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.ShellHost

/**
 * The screen Android's permission manager and privacy dashboard open to explain why the app uses
 * a permission, from the information icon beside the app (`VIEW_PERMISSION_USAGE` and
 * `VIEW_PERMISSION_USAGE_FOR_PERIOD`). It shows the privacy page.
 *
 * It is declared in this module's manifest, so every app built on the Toolkit gets the icon without
 * declaring anything. Only the system can start it: it requires
 * `android.permission.START_VIEW_PERMISSION_USAGE`, which only the permission controller holds.
 *
 * It is the one activity besides the app's own, and deliberately so. It opens over the system's
 * settings, in their task, and back returns there. Routing the intent to the app's main activity
 * instead would bring the app's own task forward (or, for a `singleTask` activity, push the page
 * onto whatever the person had open) and back would then stay in the app.
 */
class PermissionUsageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ShellHost(graph = remember { permissionUsageGraph() })
            }
        }
    }
}
