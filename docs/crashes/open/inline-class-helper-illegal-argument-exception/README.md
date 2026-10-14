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

* **Component:** Kotlin runtime / inline value class validation helper (`InlineClassHelperKt`)

## Guidance & Open Questions

1. Keep this issue classified as **Open / Unresolved** until a full stack trace identifying the specific caller site is obtained.
2. Inspect recent usage of Kotlin `value class` or inline class validation checks across feature modules.
3. Watch Firebase Crashlytics for additional session events, breadcrumbs, or user actions preceding the crash.

## References

* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
