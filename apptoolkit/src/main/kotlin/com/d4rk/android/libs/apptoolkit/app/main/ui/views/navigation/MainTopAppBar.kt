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

package com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.ui.SupportActivity
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.d4rk.android.libs.apptoolkit.core.ui.views.dropdown.CommonDropdownMenuItem
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openActivity

/**
 * A top app bar for the main screen of the application.
 *
 * It includes the application title, a navigation icon, and an actions menu.
 * The actions menu currently contains a "Support Us" item that opens the [SupportActivity].
 * The navigation icon's action is configurable.
 *
 * @param navigationIcon The [ImageVector] to be displayed as the navigation icon.
 * @param onNavigationIconClick A lambda to be executed when the navigation icon is clicked.
 * @param scrollBehavior A [TopAppBarScrollBehavior] to be applied to the top app bar,
 * which defines its behavior when content is scrolled.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainTopAppBar(
    navigationIcon: ImageVector,
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context: Context = LocalContext.current

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            AnimatedIconButtonDirection(
                icon = navigationIcon,
                contentDescription = stringResource(id = R.string.go_back),
                onClick = onNavigationIconClick,
                feedback = ButtonFeedback(hapticFeedbackType = null)
            )
        },
        actions = {
            val (expandedMenu, setExpandedMenu) = remember { mutableStateOf(false) }

            AnimatedIconButtonDirection(
                fromRight = true,
                icon = Icons.Outlined.MoreVert,
                contentDescription = stringResource(id = R.string.content_description_more_options),
                onClick = { setExpandedMenu(true) },
            )

            DropdownMenu(
                expanded = expandedMenu,
                shape = MaterialTheme.shapes.largeIncreased,
                onDismissRequest = { setExpandedMenu(false) }
            ) {
                CommonDropdownMenuItem(
                    textResId = R.string.support_us,
                    icon = Icons.Outlined.VolunteerActivism,
                    onClick = {
                        setExpandedMenu(false)
                        context.openActivity(SupportActivity::class.java)
                    }
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
