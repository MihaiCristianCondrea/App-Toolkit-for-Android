---
name: android-workmanager
description: >
  Implement, review, refactor, debug, and test persistent background work
  using Jetpack WorkManager in Android applications. Use when working with
  Worker, CoroutineWorker, WorkRequest, periodic work, unique work,
  constraints, retries, backoff, expedited work, long-running workers,
  foreground execution, work chaining, progress, cancellation, or persistent
  tasks that must survive app restarts or device reboots.
---

# Android WorkManager

Use Jetpack WorkManager for persistent work that must run reliably even when
the user leaves the screen, the application exits, or the device restarts.

Read `references/workmanager.md` when implementation details, scheduling
behavior, constraints, retries, expedited work, long-running work, chaining,
or WorkManager APIs are needed.

Follow the project's existing architecture and conventions when they do not
conflict with WorkManager correctness requirements.

## Decide whether WorkManager is appropriate

Do not use WorkManager merely because work happens in the background.

Use WorkManager when the work must be reliably scheduled and should continue
independently of the screen that initiated it.

Typical examples include:

- synchronizing application data;
- uploading user-created content;
- sending queued logs or analytics;
- periodic synchronization;
- persistent cleanup;
- deferred processing;
- work that must survive app restarts;
- work that should resume after device reboot.

Prefer ordinary coroutines when work only matters while the application or
screen is alive.

Prefer lifecycle-aware coroutine scopes for ordinary asynchronous UI work.

Prefer AlarmManager when exact clock-time execution is genuinely required,
such as an exact alarm or calendar notification.

Do not use WorkManager as a generic replacement for coroutines.

## Classify the work first

Before implementing a Worker, determine whether the operation is:

1. immediate;
2. deferrable;
3. periodic;
4. expedited;
5. long-running.

Do not choose a WorkRequest type before understanding the required lifetime
and timing semantics.

## One-time work

Use `OneTimeWorkRequest` for persistent work that needs to execute once.

Examples:

- upload a file;
- synchronize after a user action;
- perform deferred cleanup;
- process persisted data.

Do not assume one-time work executes immediately.

WorkManager schedules work according to system conditions and configured
constraints.

## Periodic work

Use `PeriodicWorkRequest` for work that needs to occur repeatedly.

Periodic WorkManager execution is inexact.

Do not use periodic work when an operation must happen at an exact clock time.

Do not implement periodic behavior manually with:

```kotlin
while (true) {
    doSomething()
    delay(...)
}