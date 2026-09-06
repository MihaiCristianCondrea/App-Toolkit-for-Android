# SDK activity startup crashes in consuming apps

Investigation date: September 6, 2026. Status: Billing and Hsdp unresolved; no verified shared fix.

## Reports and ownership

Net Probe supplied three Firebase reports from version `26.09.05` (`137260905`):

| Issue ID                           | Signature                                                               | Owner                                                 |
|------------------------------------|-------------------------------------------------------------------------|-------------------------------------------------------|
| `ab47e167269f091f3ffe0938df7bf5e9` | `ProxyBillingActivity.onCreate`: null `PendingIntent.getIntentSender()` | Google Play Billing, consumed through Toolkit billing |
| `e1da130dcaaae1eb3a1d34287c0750a9` | `HsdpShimActivity.onAttachedToWindow`: `targetPackageName is null`      | Google Play Hsdp, transitive through Mobile Ads       |
| `ffba3123d5d541526c35620da6e18131` | `InvisibleActionTrampolineActivity`: target intent missing              | AndroidX Glance, exposed by the Toolkit facade        |

The original traces and host investigation are in Net Probe's `docs/crashes`. The Billing event
was on a non-rooted OnePlus 8 Pro running Android 11. Keys, logs, and breadcrumbs were empty.
The three events occurred within 31 seconds. Automated activity probing is a hypothesis, not a
confirmed source. Neither the device nor these traces prove that Play Store returned a null intent.

The inspected Net Probe build resolved Toolkit `3.0.0-pre12`, Billing `9.1.0`, Mobile Ads
`1.4.0`, Hsdp `2.0.1`, and Glance `1.2.0`. These are current local resolution evidence, not
verification of the historical crashing APK's dependency versions.

## Assessment of the proposed Billing manifest workaround

Do not propagate expanded `android:configChanges` as a proven crash fix.

The proposal adds `smallestScreenSize|uiMode|fontScale|layoutDirection|locale|density` to both
Billing proxy activities, using `tools:replace`. The existing working trees in Toolkit's billing
manifest and Net Probe's app manifest contain this proposal. Its presence is not validation;
the comments claiming lost extras during recreation describe an unproven cause. Recommendation:
remove these speculative overrides before publishing unless a reproduction establishes benefit.

Inspection of the resolved Billing `9.1.0` `ProxyBillingActivity` using `javap -c -p` shows:

- `onCreate` branches on `savedInstanceState`: bytecode offset 108 jumps to the restoration
  branch at 378 when the bundle is non-null.
- The null-bundle launch branch reads `BUY_INTENT` or `IN_APP_MESSAGE_INTENT`; neither being
  present can leave the pending intent null before the `getIntentSender()` call.
- The saved-state restoration branch restores callback/flow state instead of repeating that
  pending-intent launch path.

This does not reproduce the reported incident, but it does not support the claim that ordinary
saved-state recreation necessarily loses `BUY_INTENT` and executes the failing launch path.
The trace also does not establish which pending-intent extra was expected.

Disabling recreation for selected configuration changes transfers responsibility for those changes
to the activity implementation; it does not prevent all recreation or process death. See
[Android configuration-change guidance](https://developer.android.com/topic/architecture/views/resources/runtime-changes-views).
Toolkit does not own Google's activity implementation. A manifest merge or passing DI tests cannot
verify its purchase UI across theme, locale, density, and font changes. No report here implicates
`ProxyBillingActivityV2` specifically.

## Existing safeguards and their limits

Toolkit's billing repository checks client readiness, validates the host lifecycle, and catches
synchronous failures around `BillingClient.launchBillingFlow`. Preserve these safeguards. They
cannot catch a later exception in the framework's invocation of another activity's `onCreate`.
Matching a sample application's integration is useful evidence, not proof of all runtime behavior.

The activities were already non-exported in the inspected host manifest. Repeating that setting
does not resolve a missing launch payload. Do not add a broad uncaught-exception handler, fabricate
SDK intent extras, or remove required Billing/Hsdp activities to hide these failures.

Previously documented Koin, Mobile Ads initialization, and UMP telemetry fixes address different
signatures. `ConsentSdkCrashGuard` must not be broadened to swallow these main-thread failures.

## Guidance for consuming apps

1. Keep Billing/Hsdp failures classified as unresolved until a normal purchase/ad flow reproduces
   them or an upstream release explicitly addresses the matching signature.
2. Retain the crash APK/AAB, matching R8 mapping, resolved dependency report, and merged manifest.
   Record whether the event came from Play pre-launch testing when that information is available.
3. Reproduce purchases using a licensed test account and a Play-installed test build. Check cancel,
   success, background/resume, and configuration changes while the purchase UI is open. Verify
   completion callbacks as well as absence of a crash. Never infer success from build checks alone.
4. If a reproducible Toolkit defect is found, fix it in the owning integration module, add a focused
   regression check, publish a new immutable Toolkit version, and verify each host's resolved
   artifact
   and runtime flow. Local Toolkit source edits do not change an existing JitPack dependency.
5. Apps with no widgets may exclude the facade's unused `androidx.glance` group after checking their
   own source and resolved Toolkit classes for references. Net Probe did this and verified absence
   of Glance components in debug/release manifests. Do not apply that exclusion to widget-using
   apps.

## Validation of this assessment

Reviewed the proposed manifest diffs, the current billing module documentation/repository, and
resolved Billing `9.1.0` bytecode. This documentation adds no runtime fix. No purchase/ad
reproduction
or configuration-change device test was performed for this assessment.
