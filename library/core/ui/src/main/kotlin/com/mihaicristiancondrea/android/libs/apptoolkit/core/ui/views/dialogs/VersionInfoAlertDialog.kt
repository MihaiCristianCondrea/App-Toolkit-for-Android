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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer

/**
 * Version-information dialog with caller-owned dismissal.
 *
 * @param versionString String resource formatting [versionName].
 */
@Composable
fun VersionInfoAlertDialog(
    onDismiss: () -> Unit,
    copyrightString: Int,
    appName: Int,
    versionName: String,
    versionString: Int
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            VersionInfoAlertDialogContent(
                copyrightString = copyrightString,
                appName = appName,
                versionName = versionName,
                versionString = versionString
            )
        },
        confirmButton = {},
    )
}

/**
 * Version-information content using the host app icon. The icon is resolved once per context
 * and loaded through Coil's shared loader.
 *
 * @param versionString String resource formatting [versionName].
 */
@Composable
fun VersionInfoAlertDialogContent(
    copyrightString: Int,
    appName: Int,
    versionName: String,
    versionString: Int
) {
    val context: Context = LocalContext.current
    val appIcon: Drawable = remember(context) {
        context.packageManager.getApplicationIcon(context.packageName)
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = appIcon,
            contentDescription = null,
            modifier = Modifier.size(size = SizeConstants.LauncherIconSize),
        )
        LargeHorizontalSpacer()
        Column {
            Text(text = stringResource(appName), style = MaterialTheme.typography.titleLarge)
            Text(
                text = stringResource(versionString, versionName),
                style = MaterialTheme.typography.bodyMedium
            )
            LargeVerticalSpacer()
            Text(
                text = stringResource(id = copyrightString),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}