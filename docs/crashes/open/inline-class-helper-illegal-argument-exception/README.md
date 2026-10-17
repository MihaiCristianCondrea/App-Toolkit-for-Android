# Incident Report: InlineClassHelperKt IllegalArgumentException Crash

* **Status:** Open (Unresolved)
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.10.31` (`137261031`)
* **Date:** October 8, 2026 at 14:41:04

## Behavior and Impact

When running application version `26.10.31` on Android 15 (Samsung SM-A055F), a fatal `java.lang.IllegalArgumentException` occurred originating from Kotlin's runtime helper `InlineClassHelperKt.throwIllegalArgumentException`.

## Failure Details

* **Exception:** `java.lang.IllegalArgumentException`
* **Location:** `kotlin.jvm.internal.InlineClassHelperKt.throwIllegalArgumentException`
* **Stack Trace Summary:**
  ```text
  Fatal Exception: java.lang.IllegalArgumentException
         at kotlin.jvm.internal.InlineClassHelperKt.throwIllegalArgumentException(InlineClassHelper.kt)
  ```

## Affected Area

* **Component:** Unconfirmed validation helper (`InlineClassHelperKt`); the exported frame does
  not establish Kotlin runtime ownership.

## Guidance & Open Questions

1. Keep this issue classified as **Open / Unresolved** until a full stack trace identifying the specific caller site is obtained.
2. Use the full retraced caller stack to identify the failed precondition before changing value classes.
3. Watch Firebase Crashlytics for additional session events, breadcrumbs, or user actions preceding the crash.

## Investigation: October 17, 2026

The supplied stack contains only the exception type and helper frame, with no message or caller.
It does not identify which inline value or precondition failed. No session export accompanies this
report. Keep open until the full retraced stack, matching release mapping and triggering screen or
inputs are available. Adding clamps, catches or changing value classes would be speculative.

## References

* [Resolved AAR audit, October 17](../../aar-audit-2026-10-17.md): the resolved Kotlin stdlib has
  no such class; Compose UI has a helper with this name in a different package. This is a lead,
  not proof that this report duplicates the Compose layout crash.
* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
