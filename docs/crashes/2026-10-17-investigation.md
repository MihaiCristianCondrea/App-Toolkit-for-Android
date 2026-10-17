# Crash investigation, October 17, 2026

Reviewed the six reports imported under `open`, their supplied sessions and the current code.
Original evidence is preserved with each incident. A fixed reporting policy does not establish
that device networking or an upstream SDK defect has been repaired.

## Implemented

- Ad-slot network failures keep diagnostic breadcrumbs without nonfatal reports. Unknown SDK
  failures and requests that cannot start still report errors.
- The sample's automatic consent request treats host unavailability as an interrupted attempt.
  It clears the session guard so a later resume can retry. Other consent errors still report.

Consumer adoption is documented in [`sample/app/README.md`](../../sample/app/README.md#architectural-decisions)
and the [3.0 migration guide](../migration/3.0.0.md). A library release carries the shared ad fix;
each consumer's app-owned ViewModel needs the consent change separately.

## Remaining fatal reports

- [Compose node measurement](open/compose-measure-deactivated-node-exception/README.md): session
  narrows the trigger to Quick Tools filtering/category toggles. Obtain the matching R8 map and
  reproduce on Android 15 before changing animations or layout ownership.
- [Billing proxy activity](open/billing-proxy-activity-null-intent/README.md): the Net Probe report
  needs its actual Billing dependency and purchase-flow evidence. The current manifest workaround
  remains unproven and needs licensed-device validation.
- [Inline helper](open/inline-class-helper-illegal-argument-exception/README.md): missing exception
  message and caller prevent a safe fix. Obtain a full retraced stack.
- [WorkManager startup](open/job-scheduler-for-namespace-no-such-method-error/README.md): Glance also
  requires WorkManager. Verify the affected device framework or an upstream fallback before removing
  initialization or changing scheduling behavior.

## Performance and architecture audit

Scope: `MainViewModel`, consent repository/source, native ad state/cache/reporting, and the Quick
Tools catalogue and category expansion path. This is a source audit, not a whole-project profile.

- Existing repositories are useful boundaries: UMP and persistence are coordinated behind the
  consent repository, while UI session/retry policy stays in the app. No forwarding use case or
  extra error-policy abstraction was added. The SDK source interface supports real callback fakes.
- The consent repository's shared round trip is justified by concurrent startup/main callers in
  the exported sessions. Its known risk of separate explicit/implicit requests remains documented
  in the module README. Do not replace this coordination with another layer.
- Catalogue filtering/list construction is remembered by its inputs. Lazy rows have primitive keys
  and category/ad content types; bounded category contents use `Column`. No custom node measurement,
  layout-coordinate feedback loop, shader, or legacy `Modifier.composed` was found in the inspected
  catalogue/expansion path. Existing bounded collection annotations were left alone.
- `rememberNativeAdState` collects process-wide SDK readiness in each slot. Consider sharing that
  observation at the screen/host level if profiling shows subscription overhead. Preserve the public
  standalone slot API and SDK-startup retry behavior when doing so.
- `NativeAdSlot` uses `LaunchedEffect` only to invoke `onAdLoaded`; catalogue toast/request effects
  also contain synchronous work. Consider reducing coroutine allocations only after checking effect
  timing, callback freshness and the resolved runtime API. These are performance candidates, not
  established causes of node deactivation.
- Keep the ad cache and its loader seam: they own ad destruction, late callbacks and reuse across
  lazy-item disposal. Removing them for fewer classes would lose tested lifecycle behavior.

Validation details and remaining runtime gaps are recorded in each incident. No measured frame-time
or CPU improvement is claimed.

## Verification

- Focused UI, consent and sample app JVM tasks: 110 tests passed. `checkModuleBoundaries` passed.
- Broad `testDebugUnitTest --continue`: 1,029 tests, 1,015 passed, 12 skipped, two existing failures
  in `ToolkitTilesViewModelTest` (initial load and retry). Its unchanged fixture uses category
  `decisions`, which the unchanged UI mapper rejects. The suite remains red.
- `:sample:app:assembleDebug` completed in the same invocation despite the test failures.
- Installed that APK with existing app data on the API 37 emulator. Startup, the Not added filter,
  eight Utilities category toggles and background/resume completed with the same app process alive
  and no app entry in the crash buffer. The emulator's unrelated UWB service aborts were excluded
  from the app result. Screenshot and logs are under local `build/crash-review*`.
- This was not an Android 15 reproduction, a licensed Play Billing test, a live UMP host-loss
  callback test, or an ad-server failure injection. Those runtime gaps remain explicit.
- `git diff --check` passed.
