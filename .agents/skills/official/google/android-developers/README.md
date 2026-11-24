# Android Developers Skill Collection

This directory (`.agents/skills/official/google/android-developers/`) houses a curated collection of
Android development and architecture skills backed by local Markdown copies of Google's
official [Android Developers](https://developer.android.com/) documentation, alongside locally
authored agent instructions and project-specific conventions.

## What This Collection Contains

The collection groups 8 Android skills covering app architecture, Jetpack libraries, notifications,
home-screen widgets, and Android TV asset specifications:

- **Architecture skills**: Data layer, optional domain layer, and UI layer patterns.
- **Jetpack & system API skills**: DataStore, WorkManager, notification views, and Jetpack Glance
  widgets.
- **Design & platform guideline skills**: Android TV launcher icon and banner specifications.
- **Local `references/` mirrors**: Offline Markdown copies of Android Developers guides.

## Relationship to Google's Android Developers Documentation

Google's [Android Developers](https://developer.android.com/) portal is the upstream source of truth
for the platform concepts, API contracts, and recommended architecture patterns captured here.

### Attribution & Provenance

We credit **Google LLC** and the **Android Developers** documentation team for the original
architectural guidance, API documentation, diagrams, and implementation examples preserved across
this collection.

Because different files in this directory serve different roles, provenance is split into three
distinct layers:

1. **Published Google Android Developers Material (Read-Only Reference Copies)**
    - The `references/*.md` files inside [
      `android-data-layer`](android-data-layer/references/architecture.md), [
      `android-domain-layer`](android-domain-layer/references/domain-layer.md), [
      `android-ui-layer`](android-ui-layer/references/ui-layer.md), [
      `android-datastore`](android-datastore/references/datastore.md), [
      `android-workmanager`](android-workmanager/references/workmanager.md), [
      `android-notification-views`](android-notification-views/SKILL.md#references), and [
      `glance-widget`](glance-widget/SKILL.md#official-source-index) are local copies of official
      `developer.android.com` documentation pages.
    - [`tv/tv-app-icon-guidelines/SKILL.md`](tv/tv-app-icon-guidelines/SKILL.md) is a direct
      Markdown copy of the official Android TV app icon design guidelines page.

2. **Locally Authored & Maintained `SKILL.md` Wrappers**
    - Most `SKILL.md` files act as structured agent entrypoints written for this repository: they
      distill key rules, pre-edit inspection steps, decision trees, review checklists, and
      reference-routing tables that point agents to the underlying Google docs (for example, [
      `android-notification-views/SKILL.md`](android-notification-views/SKILL.md), [
      `glance-widget/SKILL.md`](glance-widget/SKILL.md), [
      `android-datastore/SKILL.md`](android-datastore/SKILL.md), and [
      `android-workmanager/SKILL.md`](android-workmanager/SKILL.md)).
    - The UI layer skill credits Google as the documentation author and identifies `android-skills`
      as the routing wrapper maintainer. See its `source` field for upstream documentation.

3. **Project-Specific Adaptations (App Toolkit Conventions)**
    - Several skills layer repository-specific **App Toolkit** architecture conventions on top of
      Google's official guidance. These local rules are maintained in this repository and are
      **not** part of Google's published documentation:
        - The `app-toolkit` skill owns Toolkit screen contracts and starter templates. The
          `android-ui-layer` skill routes to Google's UI architecture references.
        - [`android-data-layer/SKILL.md`](android-data-layer/SKILL.md) incorporates App Toolkit
          conventions for `networkCall { }`, `storageCall { }`, `data/exceptions/`,
          `DiagnosticsReporter`, `TelemetryCollection`, `DispatcherProvider`, and
          `checkModuleBoundaries`.
        - [`android-domain-layer/SKILL.md`](android-domain-layer/SKILL.md) cross-references
          `app-toolkit` for Toolkit-specific UI error mapping.

## Why Documentation Is Stored Locally

Keeping local Markdown snapshots inside each skill's `references/` folder ensures reliable,
repeatable agent execution:

- **Offline and sandbox reliability**: Coding agents often run in restricted environments without
  web access or where `developer.android.com` cannot be fetched reliably.
- **Deterministic guidance**: Pinning documentation snapshots prevents silent drift mid-task and
  avoids hallucinated or outdated API assumptions from model memory.
- **Token-efficient context loading**: Instead of pasting entire multi-page guides into every
  prompt, `SKILL.md` holds concise rules and lets the agent load only the exact reference file
  needed for the current task.

## Available Skills

| Skill                          | Location                                                                     | Backing Materials                                                                                                                                                                        | Purpose                                                                                                                                                                                 |
|:-------------------------------|:-----------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **android-data-layer**         | [`android-data-layer/SKILL.md`](android-data-layer/SKILL.md)                 | 1 reference (`architecture.md`)                                                                                                                                                          | Official Android data-layer architecture (repositories, data sources, sources of truth, caching, main-safety, threading) plus App Toolkit error, telemetry, and dispatcher conventions. |
| **android-domain-layer**       | [`android-domain-layer/SKILL.md`](android-domain-layer/SKILL.md)             | 1 reference (`domain-layer.md`)                                                                                                                                                          | Optional Android domain-layer guidance: when to introduce use cases, avoiding Clean Architecture boilerplate and pass-through use cases, naming, and error propagation.                 |
| **android-ui-layer** | [`android-ui-layer/SKILL.md`](android-ui-layer/SKILL.md) | 12 Android Developers references | UI architecture, state holders, events, and state production. |
| **android-datastore**          | [`android-datastore/SKILL.md`](android-datastore/SKILL.md)                   | 1 reference (`datastore.md`)                                                                                                                                                             | Choosing between Preferences, Proto, and JSON DataStore, enforcing single-instance ownership, data-layer isolation, migrations, and corruption handling.                                |
| **android-workmanager**        | [`android-workmanager/SKILL.md`](android-workmanager/SKILL.md)               | 1 reference (`workmanager.md`)                                                                                                                                                           | Persistent background work with `WorkManager` (`OneTimeWorkRequest`, `PeriodicWorkRequest`, constraints, retries, expedited/long-running work) vs. coroutines or `AlarmManager`.        |
| **android-notification-views** | [`android-notification-views/SKILL.md`](android-notification-views/SKILL.md) | 9 references (`channels.md`, `badges.md`, `metric-style.md`, `live-update.md`, `custom-notification.md`, `time-sensitive.md`, `group.md`, `navigation.md`, `notification-permission.md`) | Designing, auditing, and debugging Android notifications (`NotificationCompat`, channels, `POST_NOTIFICATIONS`, `MetricStyle`, Live Updates, `RemoteViews`, `PendingIntent`, groups).   |
| **glance-widget**              | [`glance-widget/SKILL.md`](glance-widget/SKILL.md)                           | 14 references (`create-app-widget.md`, `build-ui.md`, `glance-app-widget.md`, `user-interaction.md`, `theme.md`, `configuration.md`, `testing.md`, etc.)                                 | Building, updating, configuring, theming, and testing Jetpack Glance home-screen widgets while respecting `AppWidget` and `RemoteViews` constraints.                                    |
| **tv-app-icon-guidelines**     | [`tv/tv-app-icon-guidelines/SKILL.md`](tv/tv-app-icon-guidelines/SKILL.md)   | Standalone `SKILL.md`                                                                                                                                                                    | Android TV launcher icon (`android:icon`), 16:9 banner (`android:banner`), adaptive safe-zone specifications, density tables, and do/don't visual guidelines.                           |

## How `SKILL.md` Files Route Agents to Documentation

Each skill follows a two-tier progressive-disclosure pattern:

1. **Frontmatter discovery**: YAML metadata (`name`, `description`, `metadata.keywords`) allows an
   AI agent to match a user's Android task to the right skill without loading full reference
   files upfront.
2. **Guided reference routing**:
    - The `SKILL.md` body establishes mandatory guardrails, pre-change codebase inspection steps,
      and architectural or API decision criteria.
    - Explicit routing sections (such as the **Reference routing** table in [
      `glance-widget/SKILL.md`](glance-widget/SKILL.md), the **Reference guide** in [
      `android-notification-views/SKILL.md`](android-notification-views/SKILL.md), and the
      **References** table in [`android-ui-layer/SKILL.md`](android-ui-layer/SKILL.md)) instruct the
      agent to open only the specific `references/*.md` or `templates/*.kt.txt` file required for
      the subtask rather than reading every file in the skill directory.
    - Local reference copies are treated as read-only source material; when a question goes beyond
      the local snapshot, agents should verify against official `developer.android.com`
      documentation only as needed.

## Maintaining and Updating This Collection

When refreshing documentation or adjusting wrapper instructions:

1. **Preserve upstream fidelity in `references/*.md`**: Keep copied Google documentation files
   aligned with their corresponding `developer.android.com` pages (for example, the `.md.txt` source
   URLs indexed in [`glance-widget/SKILL.md`](glance-widget/SKILL.md)). Avoid rewriting official
   prose for style unless fixing broken Markdown syntax or formatting artifacts.
2. **Keep Toolkit conventions in their owning skill**: Update Toolkit screen rules and starter
   templates in `first-party/app-toolkit`. Keep the Google UI reference files read-only.
3. **Update routing and metadata**: Whenever a reference file is added, renamed, or refreshed from
   `developer.android.com`, update the routing table/index and `metadata.last-updated` in the owning
   `SKILL.md`.
4. **Validate discovery and links**: Because all 8 skills are nested at depth >= 2 under
   `.agents/skills/`, keep [`.agents/skills.json`](../../../../skills.json) and repository
   validation in sync after adding, moving, or updating skills:

   ```bash
   python scripts/skills_manifest.py validate
   ```

   If a new skill directory is added or relocated, regenerate the Antigravity discovery manifest and
   run the unit test suite:

   ```bash
   python scripts/skills_manifest.py generate
   python -m unittest discover -s tests -v
   ```
