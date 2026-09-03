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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.ads.mobile.sdk.nativead.MediaView
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import org.koin.compose.koinInject

/**
 * A loaded native ad's assets, for a screen that draws its own ad in Compose.
 *
 * [NativeAdPresentation] covers the shapes the toolkit ships. A screen whose ad has to match a
 * layout the toolkit does not know about — a list row with its own badge shape, typography and
 * spacing — reaches for this instead of a [NativeAdViewFactory]: a factory has to rebuild that
 * layout in Android views, resolve theme attributes by hand, and still cannot use the app's own
 * composables, which is fidelity lost for nothing.
 *
 * Every asset here is still a real, registered view inside a `NativeAdView`, as AdMob requires.
 * What each asset draws is Compose, hosted in that view, so text uses [MaterialTheme] typography,
 * a badge can use `MaterialShapes`, and the ad reads as part of the screen.
 *
 * Contract for callers:
 * - Draw [SponsoredLabel] somewhere the user can see it. The disclosure is not optional, and an
 *   ad drawn by the host is the one place the toolkit cannot add it for you.
 * - Ask before you draw: [body], [advertiser], [callToAction] and [icon] are optional assets and
 *   are absent on plenty of ads. Drawing an asset the ad does not have renders nothing, but the
 *   space and spacing around it are yours to omit.
 * - Do not draw ad text yourself with [Text]. Only the asset composables here are registered with
 *   the SDK, and unregistered ad content is an AdMob policy violation.
 */
@Stable
interface NativeAdScope {

    /** The ad's headline. Always present. */
    val headline: String

    /** The ad's body text, when it has one. */
    val body: String?

    /** The advertiser's name, when the ad carries one. */
    val advertiser: String?

    /** The call-to-action label, when the ad carries one. */
    val callToAction: String?

    /** Whether this ad has an icon to draw. */
    val hasIcon: Boolean

    /** Whether this ad has media to draw. */
    val hasMedia: Boolean

    /** The headline, drawn as Compose text and registered as the ad's headline view. */
    @Composable
    fun Headline(
        modifier: Modifier = Modifier,
        style: TextStyle? = null,
        color: Color = Color.Unspecified,
        maxLines: Int = Int.MAX_VALUE,
    )

    /** The body text. Renders nothing when the ad has none. */
    @Composable
    fun Body(
        modifier: Modifier = Modifier,
        style: TextStyle? = null,
        color: Color = Color.Unspecified,
        maxLines: Int = Int.MAX_VALUE,
    )

    /** The advertiser's name. Renders nothing when the ad has none. */
    @Composable
    fun Advertiser(
        modifier: Modifier = Modifier,
        style: TextStyle? = null,
        color: Color = Color.Unspecified,
        maxLines: Int = 1,
    )

    /**
     * The call to action, registered as the ad's click target.
     *
     * [content] draws the button itself, so the screen's own button chrome carries the ad. It
     * receives the ad's [callToAction] label. Renders nothing when the ad has no call to action.
     */
    @Composable
    fun CallToAction(
        modifier: Modifier = Modifier,
        content: @Composable (label: String) -> Unit,
    )

    /**
     * The advertiser's icon, filling the space [modifier] gives it.
     *
     * The icon is the advertiser's artwork and is drawn as-is; shape, size and any badge behind it
     * are the caller's, applied through [modifier]. Renders nothing when the ad has no icon.
     */
    @Composable
    fun Icon(modifier: Modifier = Modifier)

    /**
     * The ad's media, filling the space [modifier] gives it.
     *
     * Give it an aspect ratio: `MediaView` takes the height it is offered. Renders nothing when the
     * ad has no media.
     */
    @Composable
    fun Media(modifier: Modifier = Modifier)

    /** The sponsored disclosure. Plain Compose text, not an ad asset; style it like the screen. */
    @Composable
    fun SponsoredLabel(
        modifier: Modifier = Modifier,
        style: TextStyle? = null,
        color: Color = Color.Unspecified,
    )
}

/**
 * A native ad slot whose layout is drawn by the caller in Compose.
 *
 * The loading, the ads preference, SDK readiness, failure reporting, the debug placeholder and
 * asset registration are the same as the [NativeAdPresentation]-based [NativeAdSlot]; only the
 * layout moves to the caller. Nothing is drawn until an ad is bound, so a caller that reserves
 * space for the slot should collapse it from [onAdLoaded] rather than around this composable.
 *
 * ```
 * NativeAdSlot(adUnitId = adUnitId) {
 *     Row(verticalAlignment = Alignment.CenterVertically) {
 *         Icon(modifier = Modifier.size(44.dp).clip(CircleShape))
 *         Column {
 *             Headline(style = MaterialTheme.typography.titleMedium, maxLines = 1)
 *             SponsoredLabel(style = MaterialTheme.typography.labelMedium)
 *         }
 *     }
 * }
 * ```
 *
 * @param adUnitId the AdMob native ad unit to request.
 * @param slotName how this placement is named in logs and Crashlytics.
 * @param onAdLoaded invoked with whether an ad is currently displayed.
 * @param content draws the ad from the assets [NativeAdScope] exposes.
 */
@Composable
fun NativeAdSlot(
    adUnitId: String,
    modifier: Modifier = Modifier,
    slotName: String = adUnitId,
    onAdLoaded: (Boolean) -> Unit = {},
    content: @Composable NativeAdScope.() -> Unit,
) {
    val currentOnAdLoaded: (Boolean) -> Unit by rememberUpdatedState(newValue = onAdLoaded)

    if (LocalInspectionMode.current) {
        Box(modifier = modifier) { PreviewNativeAdScope.content() }
        return
    }

    val adsEnabled: Boolean = rememberAdsEnabled()
    val reporter: AdLoadReporter = koinInject()
    val slotState: NativeAdSlotState = rememberNativeAdState(
        adUnitId = adUnitId,
        enabled = adsEnabled,
        slotName = slotName,
    )
    val nativeAd: NativeAd? = slotState.ad

    LaunchedEffect(nativeAd) {
        currentOnAdLoaded(nativeAd != null)
    }

    if (nativeAd == null) {
        val failure: AdSlotFailure? = slotState.failure
        if (adsEnabled && failure != null && reporter.showsDebugPlaceholder) {
            AdSlotDebugPlaceholder(
                slotName = slotName,
                reason = failure,
                modifier = modifier,
                detail = slotState.detail,
            )
        }
        return
    }

    NativeAdComposeHost(
        nativeAd = nativeAd,
        modifier = modifier,
        content = content,
    )
}

/**
 * Hosts [content] inside a real `NativeAdView`.
 *
 * The ad view is the root and holds one `ComposeView`, which is given this composition as its
 * parent so the ad keeps the host's theme and composition locals. Each asset composable inside is
 * its own view, handed to the registrar; [NativeAdView.registerNativeAd] runs from an effect once
 * those registrations have settled, because the assets compose in the child composition and are
 * therefore not known while the ad view itself is being created.
 */
@Composable
private fun NativeAdComposeHost(
    nativeAd: NativeAd,
    modifier: Modifier,
    content: @Composable NativeAdScope.() -> Unit,
) {
    val registrar: NativeAdAssetRegistrar = remember(nativeAd) { NativeAdAssetRegistrar() }
    val scope: NativeAdScope = remember(nativeAd, registrar) {
        BoundNativeAdScope(nativeAd = nativeAd, registrar = registrar)
    }
    val compositionContext = rememberCompositionContext()
    val currentContent: @Composable NativeAdScope.() -> Unit by rememberUpdatedState(content)
    var adView: NativeAdView? by remember { mutableStateOf(value = null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            NativeAdView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                addView(
                    ComposeView(context).apply {
                        setParentCompositionContext(compositionContext)
                        setContent { scope.currentContent() }
                    }
                )
            }
        },
        update = { view -> adView = view },
    )

    val registrationVersion: Int = registrar.version
    LaunchedEffect(nativeAd, registrationVersion, adView) {
        adView?.let { view -> registrar.bind(root = view, nativeAd = nativeAd) }
    }
}

/** The asset views a [NativeAdView] has to be told about before it registers an ad. */
private enum class NativeAdAssetSlot { HEADLINE, BODY, ADVERTISER, CALL_TO_ACTION }

/**
 * Collects the views the asset composables create, and registers them with the ad view.
 *
 * [version] changes whenever a new view arrives, which is what makes the caller re-register: the
 * assets are composed in a child composition, so they are not available when the ad view is first
 * created and can appear or disappear as the caller's layout changes.
 */
private class NativeAdAssetRegistrar {
    private val assets: MutableMap<NativeAdAssetSlot, View> = mutableMapOf()
    // The icon and media views are kept in their own fields rather than in the map above, so each
    // is handed to the ad view as the type that property declares.
    private var icon: ImageView? = null
    private var media: MediaView? = null

    var version: Int by mutableIntStateOf(value = 0)
        private set

    fun register(slot: NativeAdAssetSlot, view: View) {
        if (assets[slot] === view) return
        assets[slot] = view
        version++
    }

    fun registerIcon(view: ImageView) {
        if (icon === view) return
        icon = view
        version++
    }

    fun registerMedia(view: MediaView) {
        if (media === view) return
        media = view
        version++
    }

    fun bind(root: NativeAdView, nativeAd: NativeAd) {
        root.headlineView = assets[NativeAdAssetSlot.HEADLINE]
        root.bodyView = assets[NativeAdAssetSlot.BODY]
        root.advertiserView = assets[NativeAdAssetSlot.ADVERTISER]
        root.iconView = icon
        root.callToActionView = assets[NativeAdAssetSlot.CALL_TO_ACTION]
        root.registerNativeAd(nativeAd, media)
    }
}

/** [NativeAdScope] over a loaded ad. */
@Stable
private class BoundNativeAdScope(
    private val nativeAd: NativeAd,
    private val registrar: NativeAdAssetRegistrar,
) : NativeAdScope {

    override val headline: String get() = nativeAd.headline?.toString().orEmpty()
    override val body: String? get() = nativeAd.body?.toString()
    override val advertiser: String? get() = nativeAd.advertiser?.toString()
    override val callToAction: String? get() = nativeAd.callToAction?.toString()
    override val hasIcon: Boolean get() = nativeAd.icon?.drawable != null
    override val hasMedia: Boolean get() = nativeAd.mediaContent != null

    @Composable
    override fun Headline(
        modifier: Modifier,
        style: TextStyle?,
        color: Color,
        maxLines: Int,
    ) {
        AssetText(
            slot = NativeAdAssetSlot.HEADLINE,
            text = headline,
            modifier = modifier,
            style = style,
            color = color,
            maxLines = maxLines,
        )
    }

    @Composable
    override fun Body(
        modifier: Modifier,
        style: TextStyle?,
        color: Color,
        maxLines: Int,
    ) {
        val text: String = body ?: return
        AssetText(
            slot = NativeAdAssetSlot.BODY,
            text = text,
            modifier = modifier,
            style = style,
            color = color,
            maxLines = maxLines,
        )
    }

    @Composable
    override fun Advertiser(
        modifier: Modifier,
        style: TextStyle?,
        color: Color,
        maxLines: Int,
    ) {
        val text: String = advertiser ?: return
        AssetText(
            slot = NativeAdAssetSlot.ADVERTISER,
            text = text,
            modifier = modifier,
            style = style,
            color = color,
            maxLines = maxLines,
        )
    }

    @Composable
    override fun CallToAction(
        modifier: Modifier,
        content: @Composable (label: String) -> Unit,
    ) {
        val label: String = callToAction ?: return
        AssetHost(slot = NativeAdAssetSlot.CALL_TO_ACTION, modifier = modifier) {
            content(label)
        }
    }

    @Composable
    override fun Icon(modifier: Modifier) {
        val drawable = nativeAd.icon?.drawable ?: return
        AndroidView(
            modifier = modifier,
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                }
            },
            update = { view ->
                view.setImageDrawable(drawable)
                registrar.registerIcon(view = view)
            },
        )
    }

    @Composable
    override fun Media(modifier: Modifier) {
        val mediaContent = nativeAd.mediaContent ?: return
        AndroidView(
            modifier = modifier,
            factory = { context ->
                MediaView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                }
            },
            update = { view ->
                view.mediaContent = mediaContent
                registrar.registerMedia(view = view)
            },
        )
    }

    @Composable
    override fun SponsoredLabel(modifier: Modifier, style: TextStyle?, color: Color) {
        NativeAdSponsoredText(modifier = modifier, style = style, color = color)
    }

    /**
     * One ad asset: Compose content inside its own view, so the SDK has a view to register.
     *
     * The nested `ComposeView` is given the surrounding composition as its parent, so the asset
     * keeps the host's theme, density and composition locals instead of starting a bare one.
     */
    @Composable
    private fun AssetHost(
        slot: NativeAdAssetSlot,
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        val compositionContext = rememberCompositionContext()
        val currentContent: @Composable () -> Unit by rememberUpdatedState(newValue = content)
        AndroidView(
            modifier = modifier,
            factory = { context ->
                ComposeView(context).apply {
                    setParentCompositionContext(compositionContext)
                    setContent { currentContent() }
                }
            },
            update = { view -> registrar.register(slot = slot, view = view) },
        )
    }

    @Composable
    private fun AssetText(
        slot: NativeAdAssetSlot,
        text: String,
        modifier: Modifier,
        style: TextStyle?,
        color: Color,
        maxLines: Int,
    ) {
        AssetHost(slot = slot, modifier = modifier) {
            Text(
                text = text,
                style = style ?: LocalTextStyle.current,
                color = color,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * What a Compose-drawn slot renders in `@Preview` and the layout inspector, where no ad can load.
 *
 * The caller's own layout is composed, with placeholder assets in it, so a preview shows the
 * screen's spacing rather than an empty box.
 */
private object PreviewNativeAdScope : NativeAdScope {
    override val headline: String = "Sponsored headline"
    override val body: String = "A short line of ad body text."
    override val advertiser: String = "Advertiser"
    override val callToAction: String = "Install"
    override val hasIcon: Boolean = true
    override val hasMedia: Boolean = true

    @Composable
    override fun Headline(modifier: Modifier, style: TextStyle?, color: Color, maxLines: Int) {
        PreviewText(headline, modifier, style, color, maxLines)
    }

    @Composable
    override fun Body(modifier: Modifier, style: TextStyle?, color: Color, maxLines: Int) {
        PreviewText(body, modifier, style, color, maxLines)
    }

    @Composable
    override fun Advertiser(modifier: Modifier, style: TextStyle?, color: Color, maxLines: Int) {
        PreviewText(advertiser, modifier, style, color, maxLines)
    }

    @Composable
    override fun CallToAction(modifier: Modifier, content: @Composable (label: String) -> Unit) {
        Box(modifier = modifier) { content(callToAction) }
    }

    @Composable
    override fun Icon(modifier: Modifier) {
        PreviewAsset(modifier = modifier)
    }

    @Composable
    override fun Media(modifier: Modifier) {
        PreviewAsset(modifier = modifier)
    }

    @Composable
    override fun SponsoredLabel(modifier: Modifier, style: TextStyle?, color: Color) {
        NativeAdSponsoredText(modifier = modifier, style = style, color = color)
    }

    @Composable
    private fun PreviewAsset(modifier: Modifier) {
        Box(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RectangleShape,
            ),
        )
    }

    @Composable
    private fun PreviewText(
        text: String,
        modifier: Modifier,
        style: TextStyle?,
        color: Color,
        maxLines: Int,
    ) {
        Text(
            text = text,
            modifier = modifier,
            style = style ?: LocalTextStyle.current,
            color = color,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun NativeAdSponsoredText(modifier: Modifier, style: TextStyle?, color: Color) {
    Text(
        text = stringResource(id = R.string.sponsored_ad_label),
        modifier = modifier,
        style = style ?: LocalTextStyle.current,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
