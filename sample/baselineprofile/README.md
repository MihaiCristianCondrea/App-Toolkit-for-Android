# `:sample:baselineprofile`

Generates the sample application's baseline and startup profiles and measures its cold startup and
Quick Tools scrolling. This is a standalone Android test APK targeting `:sample:app`; it contributes
no production code or runtime dependency to the application or library.

## Journeys

- Cold startup to the Quick Tools catalog, included in the startup profile.
- Catalog scrolling in both directions, included only in the baseline profile.
- Cold-start benchmarks with compilation disabled and with the packaged profile required.
- Catalog frame timings with the packaged profile required.

The generator and scrolling benchmark share `AppJourneys.kt`. The target package is the released
`applicationId`, not the app's namespace. Select English, finish onboarding and any consent dialogs,
and leave Quick Tools selected with All tools, an empty search and the catalog at the top before running.
Tests fail if that precondition is missing, rather
than recording setup screens as the catalog journey. Keep the same catalog, ad settings, animation
settings and device configuration when comparing measurements.

## Generate profiles

Connect an API 33 or newer device or emulator (older devices require root), then run:

```powershell
.\gradlew.bat :sample:app:generateReleaseBaselineProfile "-Pandroid.injected.androidTest.leaveApksInstalledAfterRun=true"
```

The Baseline Profile Gradle plugin builds and installs a non-debuggable target, runs
`BaselineProfileGenerator`, and writes profiles to `sample/app/src/release/generated/baselineProfiles`.
Inspect and commit those generated files. Ordinary release builds consume the saved profiles;
they do not start an emulator or regenerate them automatically.

The app disables unified optimization only for profile generation. Performance-test variants use
debug signing so they can replace a debug install without erasing its setup. Normal release signing
and optimization remain unchanged.

## Measure

Use a physical device for timing comparisons. After generating the profiles, run:

```powershell
.\gradlew.bat :sample:baselineprofile:connectedBenchmarkReleaseAndroidTest "-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=Macrobenchmark" "-Pandroid.injected.androidTest.leaveApksInstalledAfterRun=true"
```

Results and Perfetto traces are under this module's `build/outputs` directory. Emulator runs can
validate generation and navigation, but do not establish performance gains. No benchmark error
suppression is configured.

The library modules' hand-picked profiles remain fallback coverage for hosts that do not generate
their own profiles. The generated sample profile records the code actually exercised by these
journeys, including library and dependency code.
