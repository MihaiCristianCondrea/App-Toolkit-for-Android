package com.d4rk.android.libs.apptoolkit.app.main.ui.views.navigation

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun NavigationDrawerItemContent(
    item: NavigationDrawerItem,
    dividerRoutes: Set<String> = emptySet(),
    handleNavigationItemClick: () -> Unit = {}
) {
    val title: String = stringResource(id = item.title)
    val view: View = LocalView.current

    NavigationDrawerItem(
        label = { Text(text = title) }, selected = false, onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            handleNavigationItemClick()
        }, icon = {
            Icon(item.selectedIcon, contentDescription = title)
        }, badge = {
            if (item.badgeText.isNotBlank()) {
                Text(text = item.badgeText)
            }
        }, modifier = Modifier
            .padding(paddingValues = NavigationDrawerItemDefaults.ItemPadding)
            .bounceClick()
    )

    if (item.route in dividerRoutes) {
        HorizontalDivider(modifier = Modifier.padding(all = SizeConstants.SmallSize))
    }
}
