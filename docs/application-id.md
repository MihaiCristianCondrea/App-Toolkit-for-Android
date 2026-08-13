# Application ID

The sample app ships to Google Play as:

```text
com.d4rk.android.apps.apptoolkit
```

This is fixed. It is not derived from the Kotlin package or the Gradle `namespace`, and it must not
be changed to match them.

## Why it does not follow the source packages

The project's source packages and Gradle namespaces use `com.mihaicristiancondrea.*`. The app was
released under `com.d4rk.*` before that rename, and Google Play treats the application ID as the
app's permanent identity:

- publishing under a different ID creates a **new** app rather than an update;
- every existing install is stranded on the last build of the old ID, with no update path;
- the old ID cannot be reused or reclaimed once taken.

Android separates the two concepts precisely so a rename like this is possible:

| Setting         | What it names                                   | Safe to change            |
|-----------------|-------------------------------------------------|---------------------------|
| `namespace`     | Generated `R` and `BuildConfig` classes          | Yes, it is source-only    |
| `applicationId` | The app's identity on the device and on Play     | No, once released         |

So `sample/app/build.gradle.kts` deliberately sets an `applicationId` that does not match its
`namespace`. That mismatch is correct and should not be "tidied up".

There is no `applicationIdSuffix`. The ID is written out in full, because a suffix split across two
declarations is how the wrong value got shipped in the first place — the pieces read as plausible
on their own.

## What is tied to the application ID

Changing it, or getting it wrong, breaks all of these:

- **Firebase** — `google-services.json` is matched by package name.
- **AdMob** — the app ID in the manifest belongs to a specific Play listing.
- **Play Billing** — products are registered against the application ID.
- **Deep links, backup, shortcuts** — anything keyed on the package name.

## google-services.json

The build applies the Firebase plugins only when `google-services.json` contains a client for the
application ID above. The check is deliberately stricter than "the file exists":

- if the file is **missing**, Firebase is skipped — contributors can build without the real config;
- if the file is **present but has no matching client**, the build warns and skips Firebase, rather
  than failing with the plugin's `No matching client found for package name`, which reads as a build
  misconfiguration instead of what it is;
- a **release** build in that state fails outright. Shipping a release whose crash reporting silently
  did not initialise is worse than not shipping.

### Current state

The committed `sample/app/google-services.json` has a client for
`com.mihaicristiancondrea.android.apps.apptoolkit` only, so **Firebase is currently disabled** for
this app and release builds will refuse to run.

To fix it, in the Firebase console for project `app-toolkit-for-android`:

1. Add an Android app with package name `com.d4rk.android.apps.apptoolkit`.
2. Add the release and debug signing SHA-1/SHA-256 fingerprints to it.
3. Download the regenerated `google-services.json` and replace `sample/app/google-services.json`.

The regenerated file keeps the existing client, so nothing that already works stops working.

This cannot be done from the repository — the file carries API keys and client IDs issued by
Firebase, and inventing them would produce a build that looks configured and fails at runtime.

## Verifying

```bash
# Confirm the ID that will actually be packaged.
./gradlew :sample:app:assembleDebug
unzip -p sample/app/build/outputs/apk/debug/app-debug.apk AndroidManifest.xml | strings | grep -i d4rk
```

The warning below means the Firebase step above has not been done yet:

```text
google-services.json has no client for 'com.d4rk.android.apps.apptoolkit', so Firebase
(Analytics, Crashlytics, Performance) is disabled for this build.
```
