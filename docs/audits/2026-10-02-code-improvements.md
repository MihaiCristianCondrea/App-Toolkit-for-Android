# Code improvements, October 2, 2026

This review checks the current consent API, its callers and tests, verification rules, library
packaging, and the existing Compose stability FIXME comments. It is a focused follow-up to the
[October 1 code audit](2026-10-01-code-audit.md), with findings checked against the updated source.
P1 means address before the next release; P2 means a follow-up. Runtime and performance findings
below still need device or profiler evidence where stated.

## Completed in this change

- Startup calls suspend `ConsentRepository.requestConsent()` directly inside
  `withTimeoutOrNull(CONSENT_TIMEOUT)`. It no longer calls `Flow.first()` on `Unit`. Kotlin infers
  the timeout result from the suspend call without an explicit type argument.
- Onboarding and the sample's main ViewModel use reported suspend calls too. Startup still settles
  on success, failure, a missing host, or 15 seconds. The main ViewModel retains its session guard,
  loading/success/error breadcrumbs, and the existing consent error message.
- Their consent fakes now implement the suspend contract. Startup tests check the exact timeout
  boundary and cancellation during replacement. Main tests cover failure messages and cancellation.
- The consent remote test fake completes a snapshot of pending answers. Resuming one answer can
  start a new request immediately, so iterating the live list caused `ConcurrentModificationException`
  in the destroyed-host test. Both successful and failed completions use the snapshot.
- Module READMEs and changelogs describe the suspend contract. Existing shell stability FIXME
  comments remain unchanged.
- Apps package-name copying uses the same `ClipboardRepository.confirmsCopies` contract as About.
  Android 13 and newer use system confirmation only; Android 12L and earlier retain the existing
  toast. Tests cover both feedback paths, invalid packages, and rejected writes. They test the
  repository contract with a fake, not a real Android clipboard UI. The API boundary follows
  [Android's clipboard feedback guidance](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste#avoid-duplicate-notifications).
- Two Apps tests explicitly compare `List<AppInfo>?` values, retaining their catalogue and retry
  assertions while fixing test type inference after the state moved to immutable collections.

## How migrated screens show errors

- A failed load becomes `Loadable.Failed(message, retryable)`. `ScreenStateHandler` renders the
  shared error view with that text, and a Retry button when `retryable` is true and the screen
  supplies `onRetry`. A non-retryable failure has no Retry button.
- A failed action on existing content uses `showMessage(error.toErrorMessage(fallback))` instead
  of replacing that content. `MessageHost` shows queued errors as long-duration snackbars through
  the owning page or shell scaffold.
- Error mapping uses the shared reason text where one exists and the screen's fallback otherwise.
  The exception's raw message is not shown. Cancellation is neither a failure message nor an
  operation error.
- Startup is intentionally different: consent failure or timeout settles its consent status so
  the only Continue path remains available. Onboarding reports a consent failure without blocking
  its pages.

## P1: finish updating tests alongside the UI migration

**Evidence:**
[AdsSettingsViewModelTest](../../library/integration/ads/src/test/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/integration/ads/ui/AdsSettingsViewModelTest.kt)
still passes `dispatchers`, reads `uiState` and `ScreenState`, returns `DataState` from preference
writes, and implements consent as a flow. Its production ViewModel already uses `state`, `Loadable`,
messages, and suspend writes.
`DefaultAdsSettingsRepositoryTest` also expects result wrappers from the repository's suspend
writes. Both test files fail compilation in the aggregate run.

Update remaining tests to the contracts their production classes now expose. Preserve the assertions
about failed writes, retry, and keeping existing content. Add failure-message assertions rather than
only renaming properties until compilation succeeds. Require the aggregate `testDebugUnitTest` to
pass alongside `checkModuleBoundaries` before releasing a migrated API. Assembly alone misses test
source compilation errors.

## P1: serialize consent requests with different form modes

**Evidence:**
[DefaultConsentRepository.requestConsent](../../library/integration/consent/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/integration/consent/data/repositories/DefaultConsentRepository.kt)
joins a request only when `showIfRequired` matches. A different value starts another remote request
while the first is pending. The test `a request that forces the form does not attach to an implicit
one` currently permits two simultaneous requests. Waiting five seconds for a destroyed host can
also start another request while the old SDK operation has not answered.

Keep explicit and conditional requests distinct, but queue incompatible requests behind the active
SDK operation. A mutex around selecting the request does not serialize the remote round trips.
Define the destroyed-host recovery policy separately, since cancelling a coroutine does not prove
that UMP cancelled its SDK work. Add tests asserting that the explicit request starts only after the
conditional request ends and still shows the explicitly requested form.

## P1: verify that consumers receive the intended shrinker rules

**Evidence:**
[apptoolkit/consumer-rules.pro](../../library/apptoolkit/consumer-rules.pro) contains serialization
rules, but no current library build file or convention plugin configures `consumerProguardFiles`.
Having the file beside the build script does not attach it to the published AAR.

Review which rules are still required, register those in the module that owns the affected API, and
inspect the resulting release AAR. Then verify a consumer release build that uses serialized
navigation keys. Do not expand the broad package keep rule without a demonstrated need. The empty
navigation rules file needs no contents unless a specific requirement is established.

## P2: bound shared consent work and make scope ownership explicit

**Evidence:** the repository's default `requestScope` creates its own `SupervisorJob`, and
`InFlightConsentRequest` holds a `ConsentHost` until the remote call answers. Startup's timeout only
cancels that caller's wait. If UMP never calls back, the shared operation and host can remain live,
and later matching callers join it. The deferred also has no parent; launching into a scope that
was already cancelled may leave it without an outcome.

Specify who owns and closes the process scope, and how callers receive a terminal result when the
scope cannot start or finishes. Add tests for a cancelled request scope, an absent SDK callback, and
a late callback after the host is destroyed. Choose timeout and host-release behavior together with
the SDK serialization policy above. Do not assume releasing a wait makes starting a second SDK
request safe. Host retention is a source-level risk here, not a measured leak.

## P2: read consent settings as one storage snapshot

**Evidence:**
[DefaultUsageAndDiagnosticsRepository.observeSettings](../../library/feature/diagnostics/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/feature/diagnostics/data/repositories/DefaultUsageAndDiagnosticsRepository.kt)
combines five per-key flows. `DefaultConsentRepository.readPersistedSettings()` starts five separate
`first()` reads. An atomic bundle write does not make those downstream reads or emissions a single
snapshot.

Expose a bundle from one DataStore preferences emission and use it for observation and initial SDK
application. Keep the existing debug/release defaults and stored key names. Test changing all five
choices together without observing a mixture of old and new values. Remove redundant dispatcher
switches only after the owning data-source APIs are confirmed main-safe.

## P2: distinguish reusable test fixtures in repository-placement checks

**Evidence:**
[RepositoryConventionsTest](../../library/apptoolkit/src/test/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/app/RepositoryConventionsTest.kt)
scans every `src/main` file and requires names ending in `Repository.kt` to live in
`data/repositories`. This includes `core/testing/FakeTelemetryRepository.kt`, although the testing
module is documented as shared fixtures consumed through `testImplementation`.

Choose a documented fixture policy: a narrow testing-module exception, or fixture packages aligned
with the rule. Keep production repository checks strict, retain package ownership checks, and
verify that fixtures stay off published runtime dependency graphs. Avoid renaming a public fixture
merely to hide it from the filename check.

## P2: investigate the six shell stability FIXMEs with measurements

**Evidence:**
[ShellHost](../../library/shell/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/shell/ShellHost.kt)
and
[NavigationSurfaces](../../library/shell/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/shell/chrome/NavigationSurfaces.kt)
mark the start callback, preferences, tab and drawer lists, and selected navigation keys as having
runtime-determined stability. These diagnostics alone do not establish a visible performance bug.

Use Compose compiler reports and a profile of rail expansion, drawer opening, and tab selection to
identify costly recomposition. Consider immutable collections or a stable observable contract only
where their promises match the implementation. Do not annotate mutable or externally owned
navigation keys as immutable to silence a diagnostic. Verify selection and back-stack restoration
after any contract change. The FIXME comments remain in place while this investigation is open.

## Verification

Passed:

- Startup, onboarding, consent, sample-app, Apps, and About unit tests: 127 tests, no failures or
  skipped tests across those six modules. Four of the Apps tests cover the copy-feedback contract.
- `checkModuleBoundaries`, `:sample:app:assembleDebug`, and repository-wide `lintDebug`.
- `git diff --check`. The repository has no configured formatting task.

The final aggregate command was:

```powershell
.\gradlew.bat checkModuleBoundaries testDebugUnitTest :sample:app:assembleDebug lintDebug --continue --offline --console=plain
```

It still fails on three unrelated, unchanged checks:

- `:library:apptoolkit:testDebugUnitTest`: the repository-placement check rejects
  `FakeTelemetryRepository` in the testing module. See the fixture policy item above.
- `:library:core:designsystem:testDebugUnitTest`: the rainfall wind-smoothness assertion fails.
- `:library:integration:ads:compileDebugUnitTestKotlin`: Ads ViewModel and repository tests still
  target earlier result/state contracts. See the test migration item above.

The earlier Apps test compilation errors and consent fake's concurrent-modification failure are
resolved. No emulator or device checks were run. A real UMP form, Android clipboard overlays,
consumer release installation, and Compose performance profiling remain unverified.
