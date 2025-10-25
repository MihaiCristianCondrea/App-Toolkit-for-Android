package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.BottomBarItem
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.utils.ads.helpers.bindArticleNativeAd
import com.d4rk.android.libs.apptoolkit.core.utils.ads.NativeAdManager
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomBarItem>,
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
            BottomNavigationNativeAdBanner(
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
                            text = stringResource(id = item.title),
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.basicMarquee()
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

@Composable
private fun BottomNavigationNativeAdBanner(
    adsConfig: AdsConfig,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(Unit) {
        NativeAdManager.loadNativeAds(
            context = context,
            unitId = adsConfig.bannerAdUnitId
        )

        snapshotFlow { NativeAdManager.adQueue.size }
            .filter { it > 0 && nativeAd == null }
            .collect {
                nativeAd = NativeAdManager.adQueue.removeAt(0)
            }
    }

    DisposableEffect(nativeAd) {
        onDispose { nativeAd?.destroy() }
    }

    nativeAd?.let { ad ->
        Surface(
            modifier = modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            AndroidView(
                modifier = modifier.fillMaxWidth(),
                factory = { ctx -> createBottomNavigationAdView(ctx) },
                update = { view ->
                    view.visibility = View.VISIBLE
                    bindArticleNativeAd(view, ad)
                }
            )
        }
    } ?: run {
        Surface(
            modifier = modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            val horizontalPadding = dimensionResource(id = R.dimen.native_ad_bottom_bar_horizontal_padding)
            val verticalPadding = dimensionResource(id = R.dimen.native_ad_bottom_bar_vertical_padding)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .padding(
                        start = horizontalPadding,
                        top = verticalPadding,
                        end = horizontalPadding,
                        bottom = verticalPadding
                    ),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

private fun createBottomNavigationAdView(ctx: Context): NativeAdView {
    val inflater = LayoutInflater.from(ctx)

    val parent = FrameLayout(ctx).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val container = inflater.inflate(R.layout.native_ad_bottom_bar, parent, false) as NativeAdView
    container.layoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    container.headlineView = container.findViewById<TextView>(R.id.native_ad_headline)
    container.bodyView = container.findViewById<TextView>(R.id.native_ad_body)
    container.advertiserView = container.findViewById<TextView>(R.id.native_ad_advertiser)
    container.iconView = container.findViewById<ImageView>(R.id.native_ad_icon)
    container.callToActionView =
        container.findViewById<Button>(R.id.native_ad_call_to_action)

    return container
}

