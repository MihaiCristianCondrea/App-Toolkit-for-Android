# Incident Report: Glance ActionTrampoline Missing Target Intent Crash

* **Status:** Resolved
* **Issue ID:** `babbf348be7e1a6e02bf8b613922247b`
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.08.12` (`1370012`)
* **Date:** September 1, 2026 at 15:10:32 EEST

## Behavior and Impact

When the sample app widget's invisible trampoline activity (`InvisibleActionTrampolineActivity` from
AndroidX Glance) was launched without a valid target action intent extra (e.g. from malformed intent
broadcasts, pre-launch crawlers, or external intent probes), AndroidX Glance threw a fatal
`IllegalArgumentException: List adapter activity trampoline invoked without specifying target intent.`

This resulted in a crash on app startup / widget interaction when handling incomplete Glance
trampoline intents.

## Failure Details

* **Exception:**
  `java.lang.RuntimeException: Unable to start activity ComponentInfo{com.d4rk.android.apps.apptoolkit/androidx.glance.appwidget.action.InvisibleActionTrampolineActivity}: java.lang.IllegalArgumentException: List adapter activity trampoline invoked without specifying target intent.`
* **Caused By:**
  `java.lang.IllegalArgumentException: List adapter activity trampoline invoked without specifying target intent.`
* **Location:** `androidx.glance.appwidget.action.ActionTrampolineKt.launchTrampolineAction` (line
  101)
* **Stack Trace Summary:**
  ```text
  Fatal Exception: java.lang.RuntimeException: Unable to start activity ComponentInfo{com.d4rk.android.apps.apptoolkit/androidx.glance.appwidget.action.InvisibleActionTrampolineActivity}: java.lang.IllegalArgumentException: List adapter activity trampoline invoked without specifying target intent.
         at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3433)
         at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3607)
         ...
  Caused by java.lang.IllegalArgumentException: List adapter activity trampoline invoked without specifying target intent.
         at androidx.glance.appwidget.action.ActionTrampolineKt.launchTrampolineAction(ActionTrampoline.kt:101)
         at androidx.glance.appwidget.action.InvisibleActionTrampolineActivity.onCreate(InvisibleActionTrampolineActivity.kt:31)
  ```

## Root Cause

AndroidX Glance's `InvisibleActionTrampolineActivity` assumes that all incoming intents contain a
populated action payload (`ACTION_INTENT`). When triggered with an empty or missing action payload,
`ActionTrampolineKt.launchTrampolineAction` unconditionally throws `IllegalArgumentException`.

## Affected Area

* **Module:** `:sample:widget` (`sample/widget`)
* **Component:** `androidx.glance.appwidget.action.InvisibleActionTrampolineActivity`

## Fix

1. **Guarded Activity Trampoline Subclass:** Implemented `SampleInvisibleActionTrampolineActivity`
   in `:sample:widget` extending `InvisibleActionTrampolineActivity`.
2. **Intent Guard & Exception Catching:** Catches missing-envelope failures (
   `IllegalArgumentException` when incoming intent extra is missing or malformed) and finishes
   gracefully without crashing, while allowing valid Glance action callbacks to continue through
   AndroidX intact.
3. **Manifest Alias:** Replaced direct component exposure in `AndroidManifest.xml` with a
   non-exported activity alias pointing to `SampleInvisibleActionTrampolineActivity`, preserving
   component address resolution for valid widget actions.

## Verification

* **Unit Tests:** `SampleTrampolineGuardTest` in `:sample:widget` verifies that missing intent
  payloads finish safely without throwing, while valid payloads delegate correctly.
* **Instrumentation/Emulator Tests:** Verified on emulator by launching the original component with
  empty/missing intent extras and ensuring valid callbacks still deliver payloads correctly.

## Timeline

* **2026-09-01 15:10:32 EEST:** Crash reported on Sample App version `26.08.12`.
* **2026-09-06:** Root cause investigated and `SampleInvisibleActionTrampolineActivity` guard
  subclass implemented in `:sample:widget`.
* **2026-09-06:** Verified with unit and instrumentation tests.

## References

* [`sample/widget/README.md`](../../../../sample/widget/README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
