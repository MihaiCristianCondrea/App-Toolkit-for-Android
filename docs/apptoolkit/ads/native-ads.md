# Native Ads

Native ads in the **App Toolkit** are rendered by one Kotlin renderer. There are no XML ad layouts
and no `findViewById` binding: a screen picks a **presentation** and calls **one composable**.

> New ad surfaces add a presentation, not a component.

---

## The API

```kotlin
// apptoolkit/core/ui/views/ads

sealed interface NativeAdPresentation {
    data object Featured : NativeAdPresentation  // MediaView, headline, body, icon + advertiser + CTA
    data object Compact  : NativeAdPresentation  // icon, headline, body, advertiser, trailing CTA
    data object Grid     : NativeAdPresentation  // square cell for grids and decks, no CTA
    data object BarRow   : NativeAdPresentation  // full-width strip for a bottom bar
}

@Composable
fun NativeAdSlot(
    adUnitId: String,
    presentation: NativeAdPresentation,
    modifier: Modifier = Modifier,
    position: GroupedItemPosition = GroupedItemPosition.SINGLE,
    showContainer: Boolean = true,
    cornerRadius: Dp = SizeConstants.ExtraLargeSize,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
)
```

Behaviour worth knowing:

| Situation | What happens |
| --- | --- |
| Ad not loaded yet, or load failed | **Nothing is rendered** — no container, no spacing. `onAdLoaded(false)` fires so the host can collapse the slot. |
| Ad loaded | The container (unless `showContainer = false`) plus a registered `NativeAdView`. `onAdLoaded(true)` fires. |
| User disabled ads, or `adUnitId` is blank | No request is made at all. |
| `LocalInspectionMode` (previews, layout inspector) | A `NativeAdPlaceholder` is drawn; nothing is loaded. |
| `adUnitId` changes | The previous `NativeAd` is destroyed before the new request starts. |

---

## The four internals

Each is separately replaceable and separately testable.

### 1. `rememberNativeAd(adUnitId, enabled)`

Owns the ad's whole lifecycle in a **single** `DisposableEffect`: loads, posts the callback to the
main thread, destroys on dispose *and* on a key change, and destroys an ad that arrives after
disposal.

It waits for `AdsSdkState.canRequestAds()`. Every entry point of the Mobile Ads SDK throws
`IllegalStateException("MobileAds.initialize must be called before using the Google Mobile Ads
SDK.")` until initialization has run, and initialization is asynchronous — an unguarded request from
composition takes the process down. The effect is keyed on `AdsSdkState.isReady`, so the request
starts by itself the moment the SDK is up, and the call is wrapped in `runCatching` regardless: an ad
slot must never be able to crash its host.

Loading goes through `NativeAdLoaderClient`, the one seam that touches the network:

```kotlin
CompositionLocalProvider(LocalNativeAdLoaderClient provides fakeClient) {
    NativeAdSlot(adUnitId = "unit", presentation = NativeAdPresentation.Featured)
}
```

### 2. `nativeAdPalette()`

Snapshots `MaterialTheme.colorScheme` into an ARGB `NativeAdPalette`. Views cannot read
`MaterialTheme`, so the colours are handed across the boundary explicitly. The palette is applied in
the `AndroidView` **update** block, so an in-app theme change that does not recreate the activity
repaints the ad instead of leaving stale colours behind.

### 3. `NativeAdRenderer`

An `AndroidView` wrapping a programmatically built `NativeAdView`. The factory builds the view tree
for the presentation once and stores a holder with strong references to every child; `update`
applies the palette and binds the ad.

**"No XML" does not mean "no `NativeAdView`."** Ad assets must still be rendered inside a registered
`NativeAdView` with a real `MediaView` and a `registerNativeAd` call — drawing headline, icon and CTA
as pure Compose would break AdMob policy and click reporting. What the migration removed is the
inflater and `findViewById`, not the ad view.

### 4. `NativeAdSurface` / `NativeAdPlaceholder`

The Compose card the ad sits in, honouring `position` (grouped corners) and `showContainer`, plus the
preview stand-in. Kept separate from the renderer so the container is only composed once an ad
exists.

By default it applies **no `colors` override**: an ad sits among ordinary content and should read as
ordinary content. Pass `cornerRadius` to match whatever card the slot is interleaved with.

Screens that build their rows differently are the exception, and they say so at the call site:

```kotlin
NativeAdSlot(
    adUnitId = adUnitId,
    presentation = NativeAdPresentation.Compact,
    position = position,
    // Toolkit Tiles rows are surfaceContainerLow surfaces, not default cards.
    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
)
```

`containerColor` is on `NativeAdSlot` and on every shipped wrapper, because a consumer app's screens
are its own — an app hosting the toolkit will have surfaces the toolkit has never seen. Overriding at
the call site keeps the default honest instead of pushing one screen's exception onto every other.

---

## Shipped components

Every one of these is now a thin wrapper over `NativeAdSlot`; their names and parameters are
unchanged, so no screen needed edits.

| Component | Presentation | Container | Used by |
| --- | --- | --- | --- |
| `SupportNativeAdCard` | `Featured` | card | Support screen |
| `NoDataNativeAdCard` | `Featured` | card | `NoDataScreen` |
| `HelpNativeAdCard` | `Compact` | card, grouped corners | Help screen list |
| `AppDetailsNativeAd` | `Compact` | none | App details sheet |
| `AppsListNativeAdCard` | `Grid` | card, 1:1 | Apps grid |
| `BottomAppBarNativeAdBanner` | `BarRow` | none | Bottom app bar |
| `QuickToolsNativeAdCard` (demo app) | `Compact` | card, grouped corners | Toolkit Tiles list |

`NativeAdViewHost` is `@Deprecated` and exists only to keep one release of source compatibility. It
inflates XML and has no replacement path other than `NativeAdSlot`.

---

## Configuration and DI

Ad unit ids still come from `AdsConfig` bound in the app module's `AdsModule` under named qualifiers
(`apps_list_native_ad`, `app_details_native_ad`, `no_data_native_ad`, `bottom_nav_bar_native_ad`,
`help_large_banner_ad`, `support_native_ad`). Screens inject the qualifier they need and pass
`bannerAdUnitId` down. Debug builds resolve to Google's sample unit ids through `AdsConstants`.

The AdMob **application** id is a different thing and is never configured here — it is read from the
host app's manifest. See [Consent (UMP) and the AdMob app id](consent-and-admob-app-id.md).

---

## Policy and accessibility

- The disclosure label is `R.string.sponsored_ad_label`, a translated string used by every
  presentation. It used to be a hardcoded English literal (`"Sponsored"` in Kotlin, `"● Sponsored"`
  in XML) in an app shipping 25 locales.
- The call to action carries **no forced height** — it wraps its label plus padding, like the rest of
  the app's buttons. A hardcoded minimum height made it taller than the icon and advertiser it shares
  a row with.
- Missing assets (no body, no icon, no advertiser, no CTA) are hidden before `registerNativeAd`, so
  the layout stays correct across creative payloads. Layouts do not lean on an optional view to hold
  space: the Featured footer pushes its CTA to the trailing edge with a spacer, so a creative with no
  advertiser line does not drag the CTA back to the left.
- The disclosure badge sits in a rounded `surfaceVariant` chip on every card presentation. `BarRow`
  is the exception — the strip is too shallow to carry one.
- Render an ad only after consent and ads settings allow serving. Placement policy belongs to the
  screen; `NativeAdSlot` is a view-layer primitive.

---

## Adding a new ad surface

1. Check whether an existing `NativeAdPresentation` fits. It usually does.
2. If not, add a `data object` to `NativeAdPresentation` and a `create…` builder in
   `NativeAdRenderer.kt`. Register every asset view on the `NativeAdView` and pass the `MediaView` to
   `registerNativeAd`.
3. Register an `AdsConfig` qualifier in the app module's `AdsModule`.
4. Call `NativeAdSlot` from the screen. Do not write a new component with its own view tree.

---

## Screen-level Provisioning (Custom Styling)

If a specific screen needs an ad to look different from the library defaults (e.g., circular icons, custom padding), use the `NativeAdViewFactory` pattern.

### 1. Implement `NativeAdViewFactory`
Create a factory in the host app (typically in the feature's `views/ads` package) and delegate unknown presentations to the toolkit's default.

```kotlin
class FeatureNativeAdViewFactory : NativeAdViewFactory {
    private val defaultFactory = DefaultNativeAdViewFactory()

    override fun createViewHolder(context: Context, presentation: NativeAdPresentation): NativeAdViewHolder {
        return if (presentation is NativeAdPresentation.Compact) {
            createCustomCompact(context)
        } else {
            defaultFactory.createViewHolder(context, presentation)
        }
    }

    private fun createCustomCompact(context: Context): NativeAdViewHolder {
        // Build your custom View hierarchy here using nativeAdRoot, verticalContent, etc.
    }
}
```

### 2. Provide the factory locally
Wrap your screen content in a `CompositionLocalProvider` using `LocalNativeAdViewFactory`. This "shadows" the default factory only for that screen.

```kotlin
@Composable
fun FeatureScreen(...) {
    CompositionLocalProvider(
        LocalNativeAdViewFactory provides remember { FeatureNativeAdViewFactory() }
    ) {
        FeatureContent(...)
    }
}
```
