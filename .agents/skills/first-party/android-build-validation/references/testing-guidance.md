# Testing Guidance

Use this reference after inspecting the repository.

The exact tasks depend on the project. Do not assume every Android repository uses the same test
layout.

## Required validation

Implementation work should finish with the broadest repository-supported unit-test suite that
applies to the change.

For many Gradle Android projects:

```bash
./gradlew test
```

runs the aggregate JVM unit-test suite.

Confirm that this task is appropriate for the project before relying on it.

A failing task is not converted into a passing task because:

- the Android SDK was missing
- the machine started with no local SDK
- Gradle had to download dependencies
- a required installable tool was absent

If the environment can be prepared locally, prepare it and rerun.

Instrumented tests are different. They require an Android runtime such as:

- physical device
- emulator
- Gradle Managed Device
- another supported Android test environment

## Repository verification

Inspect the project for additional verification gates.

Possible examples:

```bash
./gradlew check
./gradlew lint
./gradlew checkModuleBoundaries
./gradlew checkLocalizedResources
```

Custom tasks may verify:

- module ownership
- dependency boundaries
- localization parity
- Android resources
- API compatibility
- binary compatibility
- architecture rules
- generated source consistency
- license or notice rules
- static analysis
- code style

Only run custom tasks that the repository actually defines.

Good ways to discover them:

- repository documentation
- CI workflow files
- Gradle build logic
- convention plugins
- `./gradlew tasks --all`

## Targeted tests

Run owning-module tests while iterating.

Then broaden the validation scope before finishing.

Examples:

```bash
./gradlew :feature:scanner:testDebugUnitTest
./gradlew :core:navigation:test
```

Use the real task names from the repository.

Targeted tests are useful for:

- fast iteration
- confirming a regression fix
- verifying a changed contract
- checking one ViewModel
- checking one repository or use case
- checking dependency injection wiring
- checking one feature module before the full suite

## Add tests when behavior changes

Add or update tests when the implementation changes behavior.

This is especially important when changing:

- persistence
- deletion
- permissions
- navigation
- analytics
- module ownership
- dependency wiring
- process recovery
- state transitions
- concurrency
- retry behavior
- error handling

Prefer contract tests before moving behavior across module boundaries.

## ViewModel testing

For ViewModels, cover the behavior that matters to the user and to the surrounding architecture.

Common cases include:

- UI event to state transition
- UI event to one-shot action
- loading state
- success state
- no-data state
- error state
- retry behavior
- cancellation
- concurrent updates
- repeated events
- duplicate-action protection
- terminal error paths

Avoid testing implementation details that do not represent observable behavior.

## Cleanup and deletion testing

For cleanup or deletion flows, consider:

- success
- partial success
- complete failure
- cancellation
- no-op result
- count of affected items
- byte totals when part of the contract
- process-recovery identifiers
- permission or confirmation hand-off
- repeated execution
- stale input
- missing input

JVM tests can validate decision logic, but system confirmation flows may still require runtime
validation.

## Permission testing

JVM tests can cover:

- whether permission is required
- state transitions
- denied/granted handling logic
- API-level decision logic when abstracted

Use device or instrumented validation for:

- runtime permission prompts
- permission callbacks
- system-owned dialogs
- behavior that changes by Android API level

## Navigation testing

Cover:

- destination selection
- route argument handling
- saved and restored state
- invalid arguments
- deep links when relevant
- ownership boundaries after navigation refactors

## Analytics testing

For analytics, cover:

- exact event name
- required parameters
- allowed values
- privacy restrictions
- duplicate emission
- emission timing
- successful completion events only after successful completion
- failure events only when the failure actually occurred
- screen tracking when screen identity changes

Do not test analytics by checking only that `logEvent()` was called. Validate the contract.

## Coroutine and Flow testing

Consider:

- cancellation propagation
- exception handling
- restart behavior
- multiple collectors
- repeated subscriptions
- timeout behavior
- terminal states
- concurrent actions

Prefer deterministic test dispatchers and virtual time over real delays.

## Device and emulator validation

Use Android runtime validation when local JVM tests cannot prove the behavior.

Common examples:

- MediaStore deletion or write consent
- runtime permissions
- foreground services
- WorkManager after process death
- WorkManager notifications
- framework-created services
- broadcast receivers
- native ads
- ad mediation
- low-memory callbacks
- hardware APIs
- storage APIs with framework behavior
- camera
- Bluetooth
- NFC
- telephony
- real Android UI rendering
- API-specific framework behavior

When the behavior is release-critical, record:

```text
Environment: emulator / physical device / managed device
API level: <level>
Build variant: <variant>
Scenario: <what was tested>
Result: pass / fail
Notes: <important limitation or observation>
```

Do not claim runtime validation when only JVM tests ran.

## Build variants

Do not assume `debug` is always enough.

Inspect whether the project uses:

- product flavors
- build types
- generated source sets
- benchmark modules
- baseline-profile modules
- release-only shrinking
- release-only manifests
- feature flags
- test fixtures

Run the variant that exercises the changed behavior.

For release-critical work, also consider whether release compilation or packaging should be
verified.

## Suggested validation sequence

```text
Owning-module tests
      ↓
Related integration or contract tests
      ↓
Repository verification gates
      ↓
Full JVM unit-test suite
      ↓
Device or emulator validation where required
```

## Preserve useful logs

For long Gradle runs:

```bash
./gradlew test --console=plain > build.log 2>&1
```

or:

```bash
./gradlew test --console=plain 2>&1 | tee build.log
```

Avoid output handling that hides progress and makes a healthy long-running build appear frozen.

Preserve enough output to identify:

- failing task
- failing test
- exception type
- first useful stack trace
- resource failure
- Gradle configuration failure
- missing SDK package
- dependency resolution failure
