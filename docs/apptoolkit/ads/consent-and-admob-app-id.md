# Consent (UMP) and the AdMob app id

This page documents two toolkit-owned rules that consumer apps depend on:

1. the AdMob application id always comes from the **host app's manifest**, and
2. the toolkit runs **one** UMP consent round trip at a time.

Both exist because of a crash that killed the process in every app built on the toolkit.

---

## The crash

`user-messaging-platform:4.0.0` pings a metrics reporting URL after a consent request fails. When the
response is not `200`/`204` it reads the error body with:

```java
int code = connection.getResponseCode();
if (code != 200 && code != 204) {
    if (connection.getErrorStream() != null) {                 // null-checked
        String body = new Scanner(connection.getErrorStream())
            .useDelimiter("\\A")
            .next();                                           // throws when the body is EMPTY
```

The *stream* is null-checked; the *body* is not. An empty error body makes `Scanner.next()` throw
`NoSuchElementException` on the SDK's own `ThreadPoolExecutor`, so no `catch` in
`UmpConsentRemoteDataSource`, `ConsentRepositoryImpl`, or `RequestConsentUseCase` can ever see it —
an uncaught throwable on a plain executor thread reaches the default uncaught-exception handler and
takes the process down. 4.0.0 is the newest published version, so there is no upgrade that fixes the
read.

An empty-bodied error response crashes UMP regardless of what was sent, so the read itself is
Google's bug. What the toolkit controls is how often the consent request *fails* in the first place.

---

## Rule 1 — the AdMob app id comes from the host manifest

`com.google.android.gms.ads.APPLICATION_ID` in the host app's `AndroidManifest.xml` is the single
source of truth. `AdMobAppIdProvider` (`:core`) reads it through `PackageManager` metadata, validates
it against the canonical format `ca-app-pub-[0-9]{16}~[0-9]{10}`, and returns `null` when it is
missing or malformed.

```kotlin
single<AdMobAppIdProvider> { ManifestAdMobAppIdProvider(context = get()) }
```

- `UmpConsentRemoteDataSource` calls `setAdMobAppId` only for a resolved id.
- `AdsCoreManager.initializeAds` initializes the Mobile Ads SDK with the resolved id, and **skips
  initialization entirely** when there is none.
- There is **no library fallback constant**. The library used to ship `R.string.ad_mob_app_id` in
  `apptoolkit/res/values/ad_units.xml` holding the demo app's id; a consumer that declared its own id
  under a different resource name never overrode it, so both the consent request and SDK
  initialization silently used the demo app's publisher account. That resource now lives in the demo
  app.

### What consumer apps must do

```xml
<application>
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="@string/ad_mob_app_id" />
</application>
```

Declare `ad_mob_app_id` (or any name you like — only the `meta-data` key matters) **in your own app
module**. Do not call `MobileAds.initialize` yourself: `AdsCoreManager.initializeAds` owns
initialization, and a second call with a different id re-introduces the mismatch. If you need the
SDK's native ad validator disabled, pass `disableNativeValidator = true`:

```kotlin
adsCoreManager.initializeAds(
    appOpenUnitId = AdsConstants.APP_OPEN_UNIT_ID,
    disableNativeValidator = true,
)
```

### Who decides that ads are enabled

`AdsCoreManager` and the ad views must agree, or the views will request ads for an SDK that was never
initialized — and every loader in the SDK throws for that, from inside composition, killing the
process. Both read **`CommonDataStore.adsEnabledFlow`**, which carries the default the host
configured (`defaultAdsEnabled`). Never read the preference with a locally chosen default.

`AdsCoreManager` *observes* that flow rather than sampling it once, so enabling ads at runtime
initializes the SDK instead of waiting for the next process start. `AdsSdkState.isReady` publishes
when initialization completed; ad views wait on it, so a slot composed during startup requests its ad
as soon as the SDK is up rather than failing once and giving up.

---

## Rule 2 — one consent round trip at a time

Consent is requested from `OnboardingActivity`, `StartupActivity`, and each host's `MainActivity`.
Those can fire within the same second, and one of them is often an activity that is already
finishing.

`ConsentRepositoryImpl` now single-flights the request:

- a `Mutex` guards a shared, replaying `StateFlow<DataState<Unit, Errors.UseCase>>`;
- a second caller **attaches** to the in-flight request instead of starting another UMP round trip;
- the flight is keyed on `showIfRequired`, so an explicit "show the form now" request
  (`showIfRequired = false`) is never answered by an in-flight "show only if required" one;
- requests from a host that is finishing or destroyed are rejected without touching UMP;
- `UmpConsentRemoteDataSource` re-checks `ConsentHost.canShowConsentForm` before showing a form,
  because the info-update callback arrives after the host may have stopped.

Callers always observe exactly `[Loading, terminal]`, whether they started the request or joined one.

---

## Rule 3 — the crash guard covers the remainder

`ConsentSdkCrashGuard` is installed from `BaseCoreManager.onCreate()` immediately after
`Firebase.initialize` returns, which is when Crashlytics has registered its uncaught-exception
handler — so the guard wraps it. Every consumer app extends `BaseCoreManager`, so no per-app wiring
is needed.

The predicate is deliberately narrow; all four conditions must hold:

- the throwable is a `NoSuchElementException`;
- the stack trace contains a `java.util.Scanner` frame;
- the stack trace contains a `com.google.android.gms.internal.consent_sdk` frame;
- the crashing thread is not the main thread.

Anything else is delegated, unchanged, to the handler that was installed before it. Swallowed
throwables are reported through `FirebaseController.recordNonFatal(throwable, attributes)` so they
still show up in Crashlytics as non-fatals.

Swallowing is scoped to a telemetry ping with no functional effect: the consent flow has already
reported its own error to the caller by that point.

Opt out by overriding:

```kotlin
override val installsConsentSdkCrashGuard: Boolean = false
```

### Removing the guard

The guard should be deleted once a `user-messaging-platform` release fixes the read — verified by
decompiling the released artifact, not by reading release notes.
