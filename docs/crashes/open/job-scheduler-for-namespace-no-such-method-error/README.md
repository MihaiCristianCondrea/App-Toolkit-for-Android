# Incident Report: JobScheduler34.forNamespace NoSuchMethodError Launch Crash

* **Status:** Open (Unresolved)
* **Issue ID:** `7c080f7cbf38dfe61fdbdd0f0710a58a`
* **Application:** `com.d4rk.android.apps.apptoolkit` version `26.10.31` (`137261031`)
* **Date:** October 12, 2026 at 19:51:06 EEST

## Behavior and Impact

During application startup, `androidx.startup.InitializationProvider` attempts to initialize WorkManager via `WorkManagerInitializer`.

WorkManager checks for API 34+ (Android 14) and invokes `JobScheduler34.forNamespace(String)`. On the affected device (Pixel 8 Pro on Android 14), `android.app.job.JobScheduler.forNamespace` is missing at runtime, resulting in a fatal `java.lang.NoSuchMethodError`. This prevents `InitializationProvider` from completing startup and crashes the application at launch.

## Failure Details

* **Exception:** `java.lang.NoSuchMethodError: No virtual method forNamespace(Ljava/lang/String;)Landroid/app/job/JobScheduler; in class Landroid/app/job/JobScheduler;`
* **Wrapper Exception:** `java.lang.RuntimeException: Unable to get provider androidx.startup.InitializationProvider: androidx.startup.StartupException`
* **Location:** `androidx.work.impl.background.systemjob.JobScheduler34.forNamespace` (line 47)
* **Stack Trace Summary:**
  ```text
  Fatal Exception: java.lang.RuntimeException: Unable to get provider androidx.startup.InitializationProvider: java.lang.NoSuchMethodError: No virtual method forNamespace(Ljava/lang/String;)Landroid/app/job/JobScheduler; in class Landroid/app/job/JobScheduler;
         at android.app.ActivityThread.installProvider(ActivityThread.java:7467)
  Caused by java.lang.NoSuchMethodError: No virtual method forNamespace(Ljava/lang/String;)Landroid/app/job/JobScheduler; in class Landroid/app/job/JobScheduler;
         at androidx.work.impl.background.systemjob.JobScheduler34.forNamespace(JobScheduler34.java:47)
         at androidx.work.impl.background.systemjob.JobSchedulerExtKt.getWmJobScheduler(JobSchedulerExt.kt:40)
         at androidx.work.impl.WorkManagerInitializer.create(WorkManagerInitializer.java:39)
  ```

## Affected Area

* **Dependency:** `androidx.work:work-runtime`; there is no `:library:core:workmanager` module.
* **Component:** `androidx.startup.InitializationProvider`, `androidx.work.impl.background.systemjob.JobScheduler34`

## Guidance & Open Questions

1. Check whether updating `androidx.work:work-runtime` dependency version includes fallback handling for devices missing `JobScheduler.forNamespace`.
2. Obtain the actual device build fingerprint, SDK level and framework implementation. App R8 rules cannot restore a missing Android framework method.
3. Test startup behavior on API 34+ physical devices or custom ROMs to verify if manual WorkManager initialization or safety checks are required.

## Investigation: October 17, 2026

`debugRuntimeClasspath` resolves WorkManager 2.12.0. The Toolkit declares it in the `androidx-core`
bundle and Glance 1.2.0 also requires it transitively. No application-owned workers were found, but
removing WorkManager or its initializer blindly would risk Glance widget work.

[`JobScheduler.forNamespace`](https://developer.android.com/reference/android/app/job/JobScheduler#forNamespace(java.lang.String))
is an Android API 34 method. The report's Android 14 label and missing method are inconsistent with
that contract. A device/framework mismatch is a hypothesis, not a confirmed diagnosis. The same
signature is indexed in [upstream issue 556304570](https://issuetracker.google.com/issues/556304570);
its full discussion requires sign-in and no upstream resolution was verified.

Keep open pending a reproducing device or an upstream fallback verified in the resolved artifact.
Moving initialization later would only defer this failure when Glance first requests work.

## References

* [`docs/crashes/README.md`](../../README.md)

## Artifacts

* [stacktrace.txt](stacktrace.txt)
* [device-info.md](device-info.md)
