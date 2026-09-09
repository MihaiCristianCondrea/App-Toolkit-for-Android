# Reliability & Crash Investigation Framework

Reliable software is built by understanding failures, not by hiding them.

This directory is the central place for documenting crashes, ANRs, and other stability issues
discovered through Firebase Crashlytics, logs, testing, telemetry, user reports, or other diagnostic
sources.

The goal is simple: keep active investigations easy to find, preserve useful evidence, and retain
enough context from resolved issues so the same mistakes are easier to recognize in the future.

## Directory Structure

Crash investigations are grouped into two stages: active issues and resolved issues.

```text
docs/
└── crashlytics/
    ├── README.md
    ├── open/
    │   ├── scanner-security-exception/
    │   │   ├── README.md
    │   │   └── stacktrace.txt
    │   └── speed-test-anr/
    │       ├── README.md
    │       └── stacktrace.txt
    └── fixed/
        └── navigation-illegal-state/
            ├── README.md
            └── stacktrace.txt
```

## Lifecycle

### 1. `open/` - Active Investigation

The `open/` directory contains crashes and bugs that are not yet resolved.

An issue may be at any stage of investigation. Some folders may contain only a raw stack trace or
Firebase export. Others may already include notes, possible causes, reproduction details, and a
partial investigation.

A `README.md` is useful when there is enough context to summarize what is known, but it is not
required for newly collected raw evidence.

If there is not enough evidence to identify a reasonable root cause, keep the issue in `open/`.

Do not force a fix simply because an issue exists. A speculative fix can hide the real problem,
introduce new behavior, or make the crash harder to understand later.

When more information is needed, document that clearly. A short note such as "more data is needed"
is better than pretending the cause is known.

### 2. `fixed/` - Resolved Issues and Historical Records

Once an issue is understood and resolved, move it to `fixed/`.

These records exist so past crashes remain useful after the immediate problem is gone. They should
help explain what failed, why it failed, and what changed to prevent it from happening again.

A resolved issue should normally contain a `README.md` covering:

* **Behavior and impact:** What the user or system experienced.
* **Failure details:** The relevant exception, ANR, crash signature, or failure condition.
* **Root cause:** Why the issue occurred, when this is known with reasonable confidence.
* **Affected area:** The relevant screen, module, feature, service, repository, or code path.
* **Fix:** What was changed and where.
* **Verification:** How the fix was tested or validated.
* **Timeline:** Useful dates such as discovery, investigation, fix, or documentation.
* **References:** Relevant commits, pull requests, implementation records, or external issue links.
* **Artifacts:** Stack traces, logs, Firebase exports, or other evidence worth keeping.

Keep useful diagnostic files alongside the `README.md`.

Do not remove evidence simply because the issue is fixed.

## Naming and Organization

### One directory per issue

Use one directory for each distinct crash, ANR, or Crashlytics issue.

If Firebase groups many events under the same issue, they should normally remain part of the same
crash record rather than being split into separate directories.

### Use descriptive names

Prefer short, descriptive, kebab-case names:

```text
scanner-security-exception/
speed-test-anr/
navigation-illegal-state/
```

Avoid generic names such as:

```text
crash-1/
bug/
issue/
firebase-error/
```

The directory name should make the affected problem reasonably clear without needing to open the
files first.

### Normalize imported evidence

Crash folders or files downloaded directly from Firebase or another diagnostic tool may not follow
the project structure or naming conventions.

When working on an issue, normalize the structure where practical.

For example, raw exports may be renamed or reorganized into files such as:

```text
README.md
stacktrace.txt
logs.txt
breadcrumbs.txt
device-info.md
```

Do not rename or rewrite diagnostic data in a way that changes its meaning.

Keep paths short and avoid unnecessary nesting.

## Investigation Workflow

When asked to investigate or fix a crash:

1. **Inspect the evidence**

   Find the relevant issue under `open/` and read all available information before changing code.

   This may include stack traces, Firebase logs, breadcrumbs, device information, reproduction
   notes, or an existing `README.md`.

2. **Inspect the affected code**

   Trace the relevant execution path and inspect the surrounding implementation, not only the exact
   line shown in the stack trace.

   The crashing line is often where the problem becomes visible, not necessarily where the problem
   begins.

3. **Determine what is actually known**

   Separate confirmed evidence from assumptions.

   A stack trace can identify where the application failed, but it does not always explain why the
   state leading to the failure existed.

4. **Establish a plausible root cause**

   Try to explain the crash using the available evidence and the actual implementation.

   If the evidence is not sufficient, stop the investigation before introducing a speculative fix.

5. **Implement the smallest appropriate fix**

   When the cause is understood well enough, fix the underlying problem with the smallest change
   that addresses it correctly.

   Avoid unrelated refactoring or architectural changes unless they are required for the fix.

6. **Verify the result**

   Use the verification methods that make sense for the issue.

   These may include:

    * automated tests
    * unit tests
    * instrumentation tests
    * emulator testing
    * physical device testing
    * reproduction attempts
    * affected Android version testing
    * build verification
    * monitoring after release

7. **Document what was learned**

   Add or update the issue `README.md`.

   Record the important findings, root cause, fix, verification, and references.

8. **Move the issue to `fixed/`**

   Move the complete issue directory only after the problem is reasonably understood, fixed, and
   verified.

   Keep the original diagnostic evidence with it.

9. **Keep unresolved issues open**

   If there is not enough information to safely resolve the problem, leave it in `open/`.

   Add a short `README.md` when useful to explain:

    * what is currently known
    * what has already been checked
    * which assumptions remain unverified
    * what additional data would help

## Changelog

After a crash or bug is fixed, update the changelog that belongs to the affected deliverable when
the change is meaningful enough to record.

* App-specific fixes belong in the app changelog.
* Library-specific fixes belong in the library changelog.

Do not automatically add the same fix to both.

Keep changelog entries short and focused on the outcome.

The detailed explanation of the failure, investigation, and implementation belongs in the crash
record.

## Guiding Principles

### Follow the evidence

Treat Firebase reports, logs, and stack traces as diagnostic evidence.

They show what happened, but they do not always prove why it happened.

Inspect the surrounding code and application state before deciding on the root cause.

### Do not guess just to close an issue

An unresolved issue with clearly documented uncertainty is better than a speculative fix presented
as a confirmed solution.

If more data is needed, keep the issue open.

### Preserve useful context

Keep diagnostic evidence and investigation notes that may help explain similar problems later.

Do not rewrite historical records merely because the architecture or implementation has changed
since the crash occurred.

### Fix the cause, not only the symptom

Defensive checks can be useful, but they should not automatically replace understanding why an
invalid state occurred.

Prefer fixes that address the underlying failure when the evidence supports them.

### Keep records focused

Crash documentation should explain the crash and its investigation.

Implementation records should explain broader implementation work.

Changelogs should summarize user-facing or developer-facing changes.

Link between them when useful instead of copying the same information into several places.

### Keep the system practical

Not every crash record needs the same amount of documentation.

A newly discovered issue may begin with nothing more than a stack trace.

A difficult resolved issue may need a detailed investigation record.

Add structure when it helps future investigation, not simply to satisfy a template.
