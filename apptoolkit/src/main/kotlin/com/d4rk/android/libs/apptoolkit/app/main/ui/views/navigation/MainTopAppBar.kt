package com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation

import android.content.Context
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
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
@OptIn(ExperimentalMaterial3Api::class)
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
                shape = RoundedCornerShape(SizeConstants.LargeIncreasedSize),
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
