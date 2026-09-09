---
name: android-build-validation
description: Use when validating Android project changes, running Gradle tests, working in Linux or CI-style environments, fixing a missing Android SDK, preparing Android build tooling, checking repository verification tasks, separating regressions from existing failures, or deciding when a device or emulator is required. Inspect the repository first, prepare the environment when needed, run targeted tests while iterating, then run the broadest applicable repository validation before reporting the result.
---

# Android Build Validation

Validate Android changes in the environment you actually have.

A missing Android SDK is usually an environment setup problem, not a reason to skip testing. If the current machine has network access and writable storage, prepare the Android SDK locally and continue.

This skill is designed for Android repositories that may be opened in:

- Linux containers
- CI environments
- remote development sessions
- local Linux, macOS, or Windows machines
- temporary coding environments with no Android SDK installed

The goal is to make validation reliable without changing the project just to fit the machine.

## Main principles

1. **Inspect the repository before running or installing anything.**

   Do not assume the project uses a specific SDK version, JDK, module name, build-tools version, verification task, or test layout.

2. **Do not treat a failed test as passing because the environment was incomplete.**

   If the Android SDK is missing and can be installed locally, install it and rerun the task.

3. **Use the project's own configuration as the source of truth.**

   Resolve `compileSdk`, JDK requirements, Gradle tasks, and test strategy from the same files the build uses.

4. **Run narrow tests while iterating, then broaden validation before finishing.**

5. **Keep local JVM validation separate from Android runtime validation.**

   JVM tests cannot prove runtime permission dialogs, framework services, MediaStore consent, native ads, WorkManager process recovery, or other Android runtime behavior.

6. **Separate regressions from pre-existing failures.**

   A red suite still needs to be reported, even when the failure already exists on the base branch.

7. **Do not modify product code to compensate for a missing local environment.**

   Fix the machine first whenever possible.

## Workflow

### 1. Inspect the project

Read the files that define the build and test setup.

Common locations include:

- `settings.gradle`
- `settings.gradle.kts`
- root and module `build.gradle`
- root and module `build.gradle.kts`
- `gradle/libs.versions.toml`
- `gradle.properties`
- project-specific version files such as `release.properties`
- convention plugins under `build-logic/` or similar
- CI workflow files
- repository documentation
- test documentation
- contribution or agent instructions

Determine:

- which modules are affected by the change
- `compileSdk`
- whether the project uses a preview or minor SDK platform
- AGP version
- Gradle version
- JDK or Java toolchain requirement
- explicitly configured build-tools version
- NDK or CMake requirements
- targeted unit-test tasks
- repository-wide unit-test tasks
- lint or static verification tasks
- repository-specific verification gates
- instrumented-test requirements
- documented baseline failures

Read [`references/testing-guidance.md`](references/testing-guidance.md) when deciding what must be tested.

### 2. Check the environment

Start with:

```bash
java -version
./gradlew --version
command -v android || true
command -v sdkmanager || true
printf 'ANDROID_HOME=%s\n' "${ANDROID_HOME:-}"
```

Also check whether the repository already points to an Android SDK through `local.properties` or another environment configuration.

If the required Android SDK or packages are missing, read:

[`references/android-sdk-provisioning.md`](references/android-sdk-provisioning.md)

### 3. Run targeted tests while iterating

Use the smallest task that exercises the changed code.

Examples:

```bash
./gradlew :feature:scanner:testDebugUnitTest
./gradlew :core:navigation:test
```

These are examples only. Use tasks that actually exist in the project.

Prefer targeted tests for fast feedback while implementing.

When behavior changes, add or update tests before broad repository validation.

### 4. Run repository verification tasks

Inspect the project for custom verification tasks.

Possible examples include:

```bash
./gradlew check
./gradlew lint
./gradlew checkModuleBoundaries
./gradlew checkLocalizedResources
```

Do not assume these names exist.

Use the repository documentation, CI configuration, Gradle build logic, or:

```bash
./gradlew tasks --all
```

to discover the real tasks.

### 5. Run the broad unit-test suite

For many Android projects this is:

```bash
./gradlew test
```

If the repository defines another aggregate test task, use that instead.

A build that stops because the Android SDK is missing has not passed.

If the current environment can reasonably be prepared, prepare it and rerun the validation.

### 6. Classify failures

Read:

[`references/failure-triage.md`](references/failure-triage.md)

Classify each failure as one of:

- regression introduced by the current change
- pre-existing baseline failure
- deterministic project or configuration failure
- environmental or tooling failure
- flaky or nondeterministic failure
- device or emulator validation gap

Do not call something an environment issue without evidence.

### 7. Run Android runtime validation when needed

Use a device, emulator, managed device, or instrumented test when JVM tests cannot prove the behavior.

Examples include:

- runtime permissions
- Android framework service entry points
- MediaStore confirmation flows
- WorkManager after process recreation
- notifications
- foreground services
- native ads
- low-memory callbacks
- hardware-specific behavior
- platform-specific storage behavior
- UI behavior that depends on the Android framework
- API-level-specific behavior

Do not install emulator system images unless runtime validation is actually needed.

### 8. Report the result accurately

The final report should include:

- targeted tests run
- repository verification tasks run
- full unit-test suite result
- device or emulator checks performed
- API level when runtime validation matters
- failures and their classification
- environment setup performed
- anything that could not be verified and the exact reason

Do not say:

```text
All tests passed.
```

unless the relevant required test scope actually passed.

## Recommended validation order

```text
Inspect project
      ↓
Prepare environment
      ↓
Changed-module tests
      ↓
Related contract or integration tests
      ↓
Repository verification gates
      ↓
Full JVM unit-test suite
      ↓
Device or emulator validation where needed
      ↓
Classify failures
      ↓
Report result
```

## What this skill should not do

Do not:

- hardcode one project's `compileSdk`
- hardcode one project's module names
- hardcode one project's locale count
- reuse another repository's known failing tests
- assume `debug` is the only relevant variant
- install the newest SDK when the project requires another version
- upgrade AGP, Gradle, Kotlin, JDK, or `compileSdk` just to make the local machine work
- commit `local.properties`
- commit Android SDK files
- commit machine-specific paths
- hide failing tests
- remove tests simply because they are difficult to execute
- mark Android runtime behavior as tested when only JVM tests ran

## Load on demand

| Need | Reference |
| --- | --- |
| Test scope, targeted tests, full-suite expectations, device validation | `references/testing-guidance.md` |
| Android SDK provisioning in Linux, containers, macOS, and Windows | `references/android-sdk-provisioning.md` |
| Red-suite analysis, baseline failures, flaky tests, and evidence | `references/failure-triage.md` |
| Current authoritative Android tooling documentation | `references/official-sources.md` |
