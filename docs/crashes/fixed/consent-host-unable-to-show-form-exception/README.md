# Incident Report: Consent Host Unable To Show Form Non-Fatal

* **Status:** Fixed (automatic request policy verified by JVM tests)
* **Issue ID:** `5ac89e319e1711bd40359b78f8fe7d63`
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.10.31` (`137261031`)
* **Date:** October 11, 2026 at 00:21:25 EEST

## Behavior and Impact

When user consent form requested via UMP (User Messaging Platform / Google Mobile Ads SDK), the consent host activity or lifecycle state is no longer in a valid state to present the form (e.g., host activity destroyed, backgrounded, or finished prior to form presentation callback).

This logs a non-fatal `Consent host is no longer able to show a form.` exception to Crashlytics.

## Failure Details

* **Exception:** Non-fatal Exception (`Consent host is no longer able to show a form.`)
* **Location:** Toolkit's `UmpConsentRemoteDataSource.showForm` lifecycle guard, reported by the
  automatic `MainViewModel` request.
* **Stack Trace Summary:**
  ```text
  Non-fatal Exception: ob1: Consent host is no longer able to show a form.
         at ea8.a(r8-map-id-26bba04e034a85e1a8b05826a43cfd9d6bdb8ca505b2ad4571d1a1b59d0617fb:16)
         at do9.run(r8-map-id-26bba04e034a85e1a8b05826a43cfd9d6bdb8ca505b2ad4571d1a1b59d0617fb:187)
  ```

## Affected Area

* **Modules:** `:library:integration:consent` and `:sample:app`
* **Components:** `UmpConsentRemoteDataSource`, `MainViewModel`

## Fix and Verification: October 17, 2026

The remote source already checks the host both before form presentation and after explicit form
loading. The exact message is produced by that lifecycle guard, not an uncaught SDK crash. The
session confirms that `MainViewModel.requestConsent` reported the guard's `HOST_UNAVAILABLE`
exception through generic `launchReport` handling and permanently set its once-per-session flag.

The automatic request now catches only `ConsentException.Reason.HOST_UNAVAILABLE` inside
`launchReport`, records a breadcrumb, clears the session guard and returns. A later resume retries
with the current host. SDK request/form failures still use the existing error path. The source's
lifecycle guards and repository contract remain intact; explicit privacy actions retain their
own error handling.

All 26 `:sample:app:testDebugUnitTest` tests pass. The new regression simulates host loss, checks
that no message or operation-error event appears, retries successfully, and checks that later
resumes do not repeat a completed request. Consent repository tests (15), UI tests (69), and
`checkModuleBoundaries` also pass. A real UMP callback arriving after host loss was not reproduced.

Consumer apps with their own main ViewModel must adopt the pattern in
[`sample/app/README.md`](../../../../sample/app/README.md#architectural-decisions); a library upgrade
alone cannot change their automatic-request guard.

## References

* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
* [session.json](session.json)
