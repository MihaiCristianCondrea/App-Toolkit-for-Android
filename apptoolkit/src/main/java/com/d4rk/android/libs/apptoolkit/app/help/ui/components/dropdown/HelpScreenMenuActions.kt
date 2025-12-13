package com.d4rk.android.libs.apptoolkit.app.help.ui.components.dropdown

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Shop
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.HelpScreenConfig
import com.d4rk.android.libs.apptoolkit.app.licenses.LicensesActivity
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.AnimatedIconButtonDirection
import com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs.VersionInfoAlertDialog
import com.d4rk.android.libs.apptoolkit.core.ui.components.dropdown.CommonDropdownMenuItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper

@Composable
fun HelpScreenMenuActions(
    context: Context,
    activity: Activity,
    showDialog: MutableState<Boolean>,
    config: HelpScreenConfig // FIXME: Unstable parameter 'activity' prevents composable from being skippable && Parameter 'context' has runtime-determined stability
) {
    var showMenu : Boolean by remember { mutableStateOf(value = false) }

    AnimatedIconButtonDirection(
        fromRight = true , contentDescription = null , icon = Icons.Default.MoreVert , onClick = { showMenu = true })

    DropdownMenu(
        expanded = showMenu ,
        shape = RoundedCornerShape(SizeConstants.LargeIncreasedSize),
        onDismissRequest = {
            showMenu = false // FIXME: Assigned value is never read
    }) {
        CommonDropdownMenuItem(
            textResId = R.string.view_in_google_play_store,
            icon = Icons.Outlined.Shop,
            onClick = {
                IntentsHelper.openUrl(context = context, url = "${AppLinks.PLAY_STORE_APP}${activity.packageName}")
            }
        )
        CommonDropdownMenuItem(
            textResId = R.string.version_info,
            icon = Icons.Outlined.Info,
            onClick = { showDialog.value = true }
        )
        CommonDropdownMenuItem(
            textResId = R.string.beta_program,
            icon = Icons.Outlined.Science,
            onClick = {
                IntentsHelper.openUrl(context = context, url = "${AppLinks.PLAY_STORE_BETA}${activity.packageName}")
            }
        )
        CommonDropdownMenuItem(
            textResId = R.string.terms_of_service,
            icon = Icons.Outlined.Description,
            onClick = { IntentsHelper.openUrl(context = context, url = AppLinks.TERMS_OF_SERVICE) }
        )
        CommonDropdownMenuItem(
            textResId = R.string.privacy_policy,
            icon = Icons.Outlined.PrivacyTip,
            onClick = { IntentsHelper.openUrl(context = context, url = AppLinks.PRIVACY_POLICY) }
        )
        CommonDropdownMenuItem(
            textResId = R.string.oss_license_title,
            icon = Icons.Outlined.Balance,
            onClick = {
                IntentsHelper.openActivity(context = context, activityClass = LicensesActivity::class.java)
            }
        )
    }

    if (showDialog.value) {
        VersionInfoAlertDialog(onDismiss = { showDialog.value = false } , copyrightString = R.string.copyright , appName = R.string.app_full_name , versionName = config.versionName , versionString = R.string.version)
    }
}