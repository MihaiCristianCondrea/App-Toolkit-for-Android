package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.BottomAppBarNativeAdBanner
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun BottomNavigationBar(
    navController: NavController, // FIXME: Unstable parameter 'navController' prevents composable from being skippable
    items: List<BottomBarItem>, // FIXME: Parameter 'items' has runtime-determined stability
    modifier: Modifier = Modifier,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val adsConfig: AdsConfig = koinInject(qualifier = named("bottom_nav_bar_native_ad"))
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: navController.currentDestination?.route
    val showLabels: Boolean =
        dataStore.getShowBottomBarLabels().collectAsStateWithLifecycle(initialValue = true).value

    Column(modifier = modifier) {
        key("bottom_ad") {
            BottomAppBarNativeAdBanner(
                modifier = Modifier.fillMaxWidth(),
                adsConfig = adsConfig
            )
        }

        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == item.route) item.selectedIcon else item.icon,
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier.bounceClick()
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.title) , overflow = TextOverflow.Ellipsis , modifier = Modifier.basicMarquee()
                        )
                    },
                    alwaysShowLabel = showLabels,
                    selected = currentRoute == item.route,
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)

                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}