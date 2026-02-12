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

package com.d4rk.android.libs.apptoolkit.app.help.ui.views.dropdowns

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Shop
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.licenses.ui.LicensesActivity
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.d4rk.android.libs.apptoolkit.core.ui.views.dialogs.VersionInfoAlertDialog
import com.d4rk.android.libs.apptoolkit.core.ui.views.dropdown.CommonDropdownMenuItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openActivity
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HelpScreenMenuActions(
    config: AppVersionInfo,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val showMenu = rememberSaveable { mutableStateOf(false) }

    AnimatedIconButtonDirection(
        fromRight = true,
        contentDescription = null,
        icon = Icons.Default.MoreVert,
        onClick = { showMenu.value = true }
    )

    DropdownMenu(
        expanded = showMenu.value,
        shape = MaterialTheme.shapes.largeIncreased,
        onDismissRequest = { showMenu.value = false }
    ) {
        fun closeMenu() {
            showMenu.value = false
        }

        CommonDropdownMenuItem(
            textResId = R.string.view_in_google_play_store,
            icon = Icons.Outlined.Shop,
            onClick = {
                closeMenu()
                context.openUrl(
                    "${AppLinks.PLAY_STORE_APP}${context.packageName}"
                )
            }
        )

        CommonDropdownMenuItem(
            textResId = R.string.version_info,
            icon = Icons.Outlined.Info,
            onClick = {
                closeMenu()
                onShowDialogChange(true)
            }
        )

        CommonDropdownMenuItem(
            textResId = R.string.beta_program,
            icon = Icons.Outlined.Science,
            onClick = {
                closeMenu()
                context.openUrl(
                    "${AppLinks.PLAY_STORE_BETA}${context.packageName}"
                )
            }
        )

        CommonDropdownMenuItem(
            textResId = R.string.terms_of_service,
            icon = Icons.Outlined.Description,
            onClick = {
                closeMenu()
                context.openUrl(AppLinks.TERMS_OF_SERVICE)
            }
        )

        CommonDropdownMenuItem(
            textResId = R.string.privacy_policy,
            icon = Icons.Outlined.PrivacyTip,
            onClick = {
                closeMenu()
                context.openUrl(AppLinks.PRIVACY_POLICY)
            }
        )

        CommonDropdownMenuItem(
            textResId = R.string.oss_license_title,
            icon = Icons.Outlined.Balance,
            onClick = {
                closeMenu()
                context.openActivity(
                    LicensesActivity::class.java
                )
            }
        )
    }

    if (showDialog) {
        VersionInfoAlertDialog(
            onDismiss = { onShowDialogChange(false) },
            copyrightString = R.string.copyright,
            appName = R.string.app_full_name,
            versionName = config.versionName.orEmpty(),
            versionString = R.string.version
        )
    }
}
