package com.d4rk.android.libs.apptoolkit.core.ui.components.ads

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * A Composable that displays a native ad in a card format, specifically styled for a "help" or "discovery" context.
 *
 * This function handles the entire ad loading and display lifecycle. It checks for user preferences
 * (whether ads are enabled) and the validity of the ad configuration before attempting to load an ad.
 *
 * Features:
 * - Fetches ad enabled status from `CommonDataStore`.
 * - Uses `AndroidView` to inflate a traditional XML layout (`R.layout.native_ad_help_card`) for the native ad.
 * - Loads a native ad using the Google Mobile Ads SDK.
 * - Binds the loaded `NativeAd` data to the corresponding views within the inflated layout.
 * - Manages the ad's lifecycle, destroying it when the composable leaves the composition.
 * - Shows a `HelpNativeAdPreview` placeholder in Jetpack Compose preview mode.
 * - The ad will not be displayed if ads are disabled by the user or if the ad unit ID is missing.
 *
 * @param modifier The modifier to be applied to the ad card.
 * @param adsConfig Configuration object containing the necessary ad unit IDs, in this case, `bannerAdUnitId` is used for the native ad.
 */
@SuppressLint("InflateParams")
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun HelpNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val dataStore: CommonDataStore = remember { CommonDataStore.getInstance(context = context) }
    val showAds: Boolean by dataStore.adsEnabledFlow.collectAsStateWithLifecycle(initialValue = true)

    if (inspectionMode) {
        HelpNativeAdPreview(modifier = modifier)
        return
    }

    if (!showAds || adUnitId.isBlank()) {
        return
    }

    val adRequest: AdRequest = remember { AdRequest.Builder().build() }

    var nativeAdView by remember { mutableStateOf<NativeAdView?>(null) }
    var currentNativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            currentNativeAd?.destroy()
            currentNativeAd = null
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = SizeConstants.ExtraSmallSize),
    ) {
        AndroidView( // FIXME: Calling a UI Composable composable function where a androidx.compose.ui.UiComposable composable was expected
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                LayoutInflater.from(ctx)
                    .inflate(R.layout.native_ad_help_card, null) as NativeAdView
            },
            update = { view ->
                if (nativeAdView !== view) {
                    nativeAdView = view
                }
            }
        )
    }

    LaunchedEffect(nativeAdView, adUnitId, adRequest) {
        val view: NativeAdView = nativeAdView ?: return@LaunchedEffect

        val adLoader: AdLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                bindHelpNativeAd(adView = view, nativeAd = nativeAd)
                view.isVisible = true
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    view.isVisible = false
                }
            })
            .build()

        view.isVisible = false
        adLoader.loadAd(adRequest)
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun HelpNativeAdPreview(modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = SizeConstants.ExtraSmallSize),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(SizeConstants.ExtraTinySize)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.LargeSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(SizeConstants.ExtraExtraLargeSize)
                        .clip(RoundedCornerShape(SizeConstants.LargeSize))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ad",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(SizeConstants.LargeSize))
                Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraSmallSize)) {
                    Text(
                        text = "Discover helpful tools",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Ad preview placeholder",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            OutlinedButton(onClick = { }, enabled = false) {
                Text(text = "Learn more")
            }
        }
    }
}

private fun bindHelpNativeAd(adView: NativeAdView, nativeAd: NativeAd) {
    val headlineView: TextView = adView.findViewById(R.id.native_ad_headline)
    adView.headlineView = headlineView
    headlineView.text = nativeAd.headline

    val bodyView: TextView = adView.findViewById(R.id.native_ad_body)
    adView.bodyView = bodyView
    val bodyText: CharSequence? = nativeAd.body
    if (bodyText.isNullOrEmpty()) {
        bodyView.isVisible = false
    } else {
        bodyView.text = bodyText
        bodyView.isVisible = true
    }

    val advertiserView: TextView = adView.findViewById(R.id.native_ad_advertiser)
    adView.advertiserView = advertiserView
    val advertiserText: CharSequence? = nativeAd.advertiser
    if (advertiserText.isNullOrEmpty()) {
        advertiserView.isVisible = false
    } else {
        advertiserView.text = advertiserText
        advertiserView.isVisible = true
    }

    val iconContainer: View = adView.findViewById(R.id.native_ad_icon_container)
    val iconBackground: ImageView = adView.findViewById(R.id.native_ad_icon_background)
    val iconView: ImageView = adView.findViewById(R.id.native_ad_icon)
    adView.iconView = iconView
    val icon = nativeAd.icon
    if (icon == null) {
        iconView.isVisible = false
        iconBackground.isVisible = true
        iconContainer.isVisible = true
    } else {
        iconView.setImageDrawable(icon.drawable)
        iconView.isVisible = true
        iconBackground.isVisible = true
        iconContainer.isVisible = true
    }

    val callToActionView: Button = adView.findViewById(R.id.native_ad_call_to_action)
    adView.callToActionView = callToActionView
    val callToActionText: CharSequence? = nativeAd.callToAction
    if (callToActionText.isNullOrEmpty()) {
        callToActionView.isVisible = false
    } else {
        callToActionView.text = callToActionText
        callToActionView.isVisible = true
    }

    adView.setNativeAd(nativeAd)
}
