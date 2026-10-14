# Incident Report: Consent Host Unable To Show Form Non-Fatal

* **Status:** Open (Unresolved)
* **Issue ID:** `5ac89e319e1711bd40359b78f8fe7d63`
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.10.31` (`137261031`)
* **Date:** October 11, 2026 at 00:21:25 EEST

## Behavior and Impact

When user consent form requested via UMP (User Messaging Platform / Google Mobile Ads SDK), the consent host activity or lifecycle state is no longer in a valid state to present the form (e.g., host activity destroyed, backgrounded, or finished prior to form presentation callback).

This logs a non-fatal `Consent host is no longer able to show a form.` exception to Crashlytics.

## Failure Details

* **Exception:** Non-fatal Exception (`Consent host is no longer able to show a form.`)
* **Location:** UMP Consent Form presenter (`ConsentInformation` / `ConsentForm.show`)
* **Stack Trace Summary:**
  ```text
  Non-fatal Exception: ob1: Consent host is no longer able to show a form.
         at ea8.a(r8-map-id-26bba04e034a85e1a8b05826a43cfd9d6bdb8ca505b2ad4571d1a1b59d0617fb:16)
         at do9.run(r8-map-id-26bba04e034a85e1a8b05826a43cfd9d6bdb8ca505b2ad4571d1a1b59d0617fb:187)
  ```

## Affected Area

* **Module:** `:library:integration:ump` / consent management
* **Component:** `ConsentForm`, `UserMessagingPlatform`

## Guidance & Open Questions

1. Verify host Activity lifecycle state (`isFinishing` / `isDestroyed`) before calling `ConsentForm.show(activity, ...)`.
2. Inspect if consent request callbacks check lifecycle state before presenting UI.
3. Keep as **Open / Unresolved** until consent host lifecycle safety checks are updated.

## References

* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
* [session.json](session.json)
