# Failure Triage

A red Gradle run needs classification.

Do not hide it and do not immediately blame the current change.

## Failure categories

### Regression

The current change introduced the failure.

Evidence may include:

- a new test fails
- an affected module was green before the change
- reverting the change removes the failure
- the failure directly matches the modified code or contract

Fix the regression before reporting completion.

### Pre-existing baseline failure

The same failure already exists on the project's accepted baseline.

Good evidence includes:

- repository documentation lists it
- CI already shows the failure on the base branch
- a clean worktree at the base commit reproduces it
- the exact same unrelated module and failure signature existed before the current change

Report it clearly.

Do not copy a known-failure list from another project.

Do not assume an old baseline list is still current.

### Deterministic repository or configuration failure

The repository itself cannot configure, compile, or test correctly in the expected environment.

Examples:

- missing resource referenced by source
- broken Gradle configuration
- invalid checked-in dependency version
- resource linking failure
- broken generated source setup
- manifest merge failure caused by repository state

This is a real repository failure even when unrelated to the edited feature.

### Environmental or tooling failure

The project may be healthy, but the current machine cannot execute the validation.

Examples:

- required dependency downloads are blocked
- filesystem is read-only
- no compatible Android runtime exists for a required instrumented test
- emulator virtualization is unavailable
- required hardware is unavailable
- package download servers cannot be reached

A missing Android SDK should normally be fixed locally before it is accepted as an environment
limitation.

### Flaky or nondeterministic failure

Do not label a test flaky after one failure.

Confirm with reruns.

Possible causes include:

- shared mutable state
- race conditions
- uncontrolled clocks
- real network access
- random seeds
- test ordering
- background coroutines
- database state
- file-system state

Useful reruns may include:

```bash
./gradlew <failing-task> --rerun-tasks
```

or running the individual test repeatedly.

Record the evidence.

### Device or emulator validation gap

A JVM suite can be green while Android runtime behavior remains unverified.

Examples:

- permission dialogs
- framework-managed services
- MediaStore confirmation
- native ad rendering
- low-memory callbacks

Report this as:

```text
Runtime validation required but not performed.
```

not as a pass.

## Baseline comparison

Preferred order:

1. read repository-maintained test documentation
2. inspect CI status for the base branch when available
3. reproduce the failure on the base commit when practical
4. compare exact task, test name, and failure signature

Avoid destructive branch switching when the current worktree contains uncommitted changes.

A separate worktree is safer:

```bash
git worktree add /tmp/project-baseline <base-ref>
```

Run the minimum task required to confirm whether the same failure exists.

Clean up the temporary worktree afterward when appropriate.

## Preserve evidence

For long runs:

```bash
./gradlew test --console=plain > build.log 2>&1
```

Then inspect likely failure markers:

```bash
grep -nE "FAILED|FAILURE:|Exception|error:" build.log
```

Use the grep result as an index.

Read the surrounding output before classifying the failure.

## Common Android JVM-test signatures

Some failures often indicate Android test-environment problems, for example:

```text
Method ... in android.* not mocked
```

or:

```text
No instrumentation registered
```

These may indicate:

- Robolectric is missing
- the code should be abstracted away from Android framework classes
- `unitTests.returnDefaultValues` is relevant
- the test should be instrumented
- the test setup is incomplete

Do not classify them automatically.

Check whether the same failure exists on the baseline.

## Identify the real failing phase

Tests may never start.

Find the first failing phase:

```text
Gradle wrapper
plugin resolution
project configuration
resource processing
KSP / KAPT / code generation
Kotlin compilation
Java compilation
DEX or packaging
test execution
```

Do not report:

```text
Unit tests failed.
```

when the real failure was resource linking or project configuration.

## Timeouts

A process killed by the external execution environment is not automatically a project failure.

If the command was terminated externally:

- preserve partial logs
- confirm whether Gradle was still making progress
- rerun with a more suitable execution method when possible
- avoid buffered output that makes long-running commands appear idle

If the test itself reaches a deterministic timeout, treat that as a real test failure.

## Final reporting format

Prefer precise output.

Example:

```text
Targeted tests
:feature:scanner:testDebugUnitTest
PASS, 42 tests

Repository verification
checkModuleBoundaries
PASS

checkLocalizedResources
PASS

Full JVM suite
./gradlew test
FAIL

Regression failures
None

Pre-existing baseline failures
:feature:imageoptimizer
Method android.net.Uri.parse not mocked

Runtime validation
Not required for this change.
```

Avoid vague summaries such as:

```text
Everything looks fine.
Most tests pass.
Only environment problems.
```
