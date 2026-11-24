# AGENTS.md

Work with the existing project architecture and conventions.

Inspect the affected modules, their documentation, the relevant skills, and the surrounding code
before making changes. Prefer the smallest complete solution over broad refactors.

This file is shared across the App Toolkit project family. It is maintained in the
`android-skills` repository and copied unchanged into each project, the same way as the skills, so
every project reads the same rules. It is intentionally focused on rules that apply across those
projects. Detailed Android guidance belongs in the skills, module-specific knowledge belongs in each
module's `README.md`, and a project's own verification tasks live in its build configuration.

## Project family

These projects are built around App Toolkit for Android. The skills also serve Android projects
without it: inspect the project's dependencies before assuming App Toolkit exists. When it is absent,
follow the project's established patterns and the applicable Android skills; the App Toolkit rules
in this file do not apply.

App Toolkit itself has two main parts:

* `:library:*` contains the reusable Android library.
* `:sample:*` is the reference application showing how a real app integrates and extends the
  library.

A normal consumer application follows the same general architecture as `:sample:*`, but without the
`:sample:` prefix.

Typical consumer modules include:

```text
:app
:core:*
:feature:*
:integration:*
:widget
```

The exact modules depend on the application.

Do not create modules simply to make a consumer app look identical to the App Toolkit sample. Follow
the same ownership and dependency rules while keeping only the modules the product actually needs.

### App Toolkit as the reference

When working in a consumer application:

* Use App Toolkit's published public API.
* Use App Toolkit's sample application as the reference implementation for integration patterns.
* Prefer Toolkit components, providers, themes, navigation contracts, and extension points over
  local copies of the same functionality.
* Do not copy Toolkit implementation code into the application when the Toolkit already exposes an
  appropriate public API or host extension point.
* Check which App Toolkit version the application actually depends on before using an API from the
  App Toolkit repository.

The current App Toolkit `develop` branch may contain APIs that have not yet been published.

The consumer application's declared dependency version is the compatibility target unless the task
also upgrades App Toolkit.

## Start every task with context

Before making a non-trivial change:

1. Identify the affected module or modules.
2. Read their local `README.md` files when present.
3. Inspect the relevant implementation and Gradle configuration.
4. Find and read the applicable skills.
5. Check nearby code for established project patterns.
6. Determine the smallest complete change.
7. Determine how the change should be verified.

Do not design from assumptions when the repository can answer the question.

Do not invent architecture that is not supported by the current project.

When a task is sufficiently defined, execute it without unnecessary questions or additional
architectural decisions.

## Sources of project guidance

Different sources answer different questions.

### Module README.md

Defines the owning module's purpose, responsibilities, dependencies, public contracts, important
flows, architectural decisions, and known risks.

Read the owning module README before changing its contracts.

### SKILL.md

Provides focused technical guidance for Android APIs, architecture, Compose, testing, localization,
performance, Play integration, and other specialized work.

Skills explain how to solve a type of problem. They do not replace knowledge of the module being
changed.

### Existing code and build configuration

The implementation and Gradle configuration show what the repository currently does.

If documentation and implementation disagree, investigate the difference. Do not silently assume
either one is correct.

When a task intentionally changes the architecture or a contract, update the affected documentation
with the code.

## Skills

Start with [`app-toolkit`](.agents/skills/first-party/app-toolkit/SKILL.md) when implementing, reviewing,
or integrating App Toolkit features in the library, sample, or a consumer app. Use it to select
only the references and specialist skills needed for the task. For standalone Android architecture
and ViewModel work, use its portable guidance with the project's existing AndroidX stack.

Skills contain the detailed technical guidance that should not be duplicated in this file.

### Finding skills

Prefer a skill set in this repository:

```text
.agents/skills/
├── first-party/       # project family skills
├── official/          # vendor skills grouped by publisher
└── third-party/       # externally maintained skills
```

`.agents/skills/`, `marketing/.agents/skills/`, their `skills.json` manifests, and this `AGENTS.md`
are copies of the `android-skills` repository, which is their source of truth. Change a shared skill
or rule there first, then copy it into each repository that carries it, so the copies never drift.
`skills.json` provides nested skill discovery for Antigravity; other agents may discover skills
differently, and skills may be nested several directories deep.

For a consumer application that does not contain its own skill set, App Toolkit may provide the
shared skills from a sibling checkout:

```text
../App-Toolkit-for-Android/.agents/skills/
```

Do not assume the sibling repository exists. Check first.

If neither location is available, continue using the repository's own documentation and
implementation. Do not claim that a skill was consulted when it was not accessible.

### Skill workflow

For a task that has a matching skill:

1. Find the relevant `SKILL.md`.
2. Read its `name` and `description`.
3. Read the full skill before making decisions covered by it.
4. Load files from its `references/` directory only when the task requires that extra detail.
5. Use more than one skill when the task crosses several areas.
6. Apply project-specific module contracts and invariants together with the skill.
7. Do not copy the skill's instructions into project documentation.

Skills are guidance for the work, not documentation that must be reproduced in every repository.

### Common skill routing

The following are common entry points. This list is not exhaustive.

| Work                                                            | Skill                                                                       |
|-----------------------------------------------------------------|-----------------------------------------------------------------------------|
| Build validation, Gradle checks, SDK setup                      | `.agents/skills/first-party/android-build-validation/SKILL.md`                 |
| Repository, data source, model, threading and caching decisions | `.agents/skills/official/google/android-developers/android-data-layer/SKILL.md`          |
| Domain layer and use-case decisions                             | `.agents/skills/official/google/android-developers/android-domain-layer/SKILL.md`        |
| Google UI architecture, state, events and state holders          | `.agents/skills/official/google/android-developers/android-ui-layer/SKILL.md` |
| Toolkit screen contracts, ViewModels and starter templates      | `.agents/skills/first-party/app-toolkit/SKILL.md` |
| Module and package placement                                    | `.agents/skills/first-party/layered-tree-review/SKILL.md`         |
| Navigation 3                                                    | `.agents/skills/official/google/android/navigation/navigation-3/SKILL.md`          |
| Navigation events                                               | `.agents/skills/official/google/android/navigation/navigation-event/SKILL.md`      |
| Adaptive Compose layouts                                        | `.agents/skills/official/google/android/jetpack-compose/adaptive/SKILL.md`         |
| Compose styles and theming                                      | `marketing/.agents/skills/official/google/material-ui/SKILL.md`   |
| Edge-to-edge and system UI                                      | `.agents/skills/official/google/android/system/edge-to-edge/SKILL.md`              |
| DataStore                                                       | `.agents/skills/official/google/android-developers/android-datastore/SKILL.md`                        |
| WorkManager                                                     | `.agents/skills/official/google/android-developers/android-workmanager/SKILL.md`                      |
| Notifications                                                   | `.agents/skills/official/google/android-developers/android-notification-views/SKILL.md`               |
| Glance widgets                                                  | `.agents/skills/official/google/android-developers/glance-widget/SKILL.md`                            |
| Analytics and Firebase telemetry                                | `.agents/skills/first-party/ga4-app-analytics/SKILL.md`                        |
| Localization                                                    | `.agents/skills/first-party/android-localization/SKILL.md`                     |
| Testing                                                         | `.agents/skills/official/google/android/testing/testing-setup/SKILL.md`            |
| R8 and keep rules                                               | `.agents/skills/official/google/android/performance/r8-analyzer/SKILL.md`          |
| Intent and component security                                   | `.agents/skills/official/google/android/security/android-intent-security/SKILL.md` |
| Runtime permissions                                             | `.agents/skills/official/google/android/security/android-permissions-security/SKILL.md` |
| Play Billing Library upgrades                                   | `.agents/skills/official/google/android/play/play-billing-library-version-upgrade/SKILL.md` |
| Kotlin call sites and readable control flow                     | `.agents/skills/first-party/code-style/traceable-code/SKILL.md`             |
| Docs, KDoc, comments and commit messages                        | `.agents/skills/first-party/writing-style/SKILL.md`                         |
| Changelog updates                                               | `.agents/skills/first-party/app-changelog/SKILL.md`                                |

Additional specialized skills exist under `official/google/android/` (such as `build-system/`,
`device-ai/`, `devtools/`, `play/`, `profilers/` and `wear/`) and under `official/jetbrains/kotlin/`.

Discover the available `SKILL.md` files instead of assuming this table contains every skill.

Use `layered-tree-review` when creating, moving, renaming, or reorganizing Kotlin files, packages, or
modules, alongside the architecture skill that owns the behavior.

### Task workflows

Do not load the entire skill collection or large third-party suites (such as `remotion/`,
`tiktok-skills/`, or `imagegen-skills/`) unless the task explicitly requires them.

| Task | Load `marketing/AGENTS.md`? | Select these skills | Exclude unless requested |
| --- | --- | --- | --- |
| New user-facing screen or substantial UI/UX redesign | Yes (for the prototype, approval, and parity phases) | `material-ui` (Material design phase), `layered-tree-review`, `android-ui-layer` (Google UI architecture), `app-toolkit` (Toolkit implementation), plus `android-data-layer` and `android-domain-layer` as needed, `ponytail`, `traceable-code`, `android-build-validation`, plus `marketing-ui` via `marketing/AGENTS.md` | `remotion/`, `social-captions`, `play-store-*`, `tiktok-skills/` |
| Existing bug fix or nonvisual Android work (repository, data source, DataStore, WorkManager, refactor) | No | The relevant `android-developers` skill, `layered-tree-review`, `ponytail`, `traceable-code`, `android-build-validation` | All `marketing/` skills, `ga4-app-analytics` (unless telemetry changed) |
| New feature needing repositories or use cases | Only if adding or redesigning UI | `layered-tree-review`, `android-data-layer`, `android-domain-layer` (only when justified), `ponytail`, `traceable-code`, `android-build-validation` | Pass-through use cases, empty layers, `marketing/` campaign skills |
| Compose UI animation or transition | No (read the design skill directly if needed) | `material-ui` and `motion-design` (principles), `android-ui-layer` (screen state that drives the motion) | `motion-video`, `remotion/*`, `taste-skill` |
| GA4 instrumentation review or implementation | No | `ga4-app-analytics`, `android-ui-layer`, `android-data-layer` (for telemetry boundaries) | `ga-gsc-skills` (unless reading existing reports); do not implement before approval |
| GA4, Search Console, or Trends data analysis | Only if informing marketing work | The applicable `zenith779/ga-gsc-skills` skill | `ga4-app-analytics` edits, automatic tracking changes |
| Marketing assets, promo video, Play Store listing or screenshots, social campaigns | Yes | Defer to the `marketing/AGENTS.md` routing table | Unrequested Compose or Android code changes |
| `CHANGELOG.md` vs Play Store What's new | Play Store notes only | `app-changelog` for `CHANGELOG.md`; `play-store-changelog` for Play Store notes | Social captions for internal releases |

### Writing and documentation

Use `writing-style` whenever writing or editing Markdown documentation and README files, KDoc and
code comments, user-facing strings and descriptions, changelogs and release notes, commit messages
and other project text. The skill is the only place the writing rules are written; do not repeat them
here or in project documentation.

Apply the relevant `unslop` skill as a final editorial pass:

- `official/cursor/cursor-unslop` for removing generic AI writing patterns.
- `third-party/theclaymethod/unslop` for rewriting, prose audits, and author voice.

Preserve technical meaning, resource placeholders, API names, and existing documentation contracts.
Editorial cleanup must not change behavior or technical requirements.

### Code implementation

Use `ponytail` when writing, fixing, or refactoring code. Follow its smallest-complete-change
approach while preserving the project's architecture, validation, security, and compatibility
requirements. Do not use minimal code size as a reason to omit necessary functionality.

Use `traceable-code` during implementation and as a final code-style review. Before finishing a code
change, verify that control flow is easy to follow, names communicate their purpose, no unnecessary
abstractions or intermediate state were introduced, project conventions are respected, and the change
has no unrelated edits.

For substantial or risky changes, use `ponytail-review` to examine correctness, security, edge cases,
tests, performance, and unnecessary complexity. Code-style review does not replace compilation or
functional testing.

### Analytics

Use `ga4-app-analytics` when implementing, reviewing, or changing analytics instrumentation.

For meaningful changes to user-facing functionality, review whether the affected user journey should
be measured. Check existing events and the project's telemetry architecture before proposing changes.

Do not add events merely because a screen, button, or feature exists, or for routine nonvisual fixes.
Avoid duplicate events, unstable event names, sensitive data, and unnecessary parameters.

Follow the GA4 skill's approval gate before implementing proposed instrumentation unless the user
already requested direct implementation. An analytics review does not automatically authorize new
tracking.

### Analytics reporting and research

For analysis of existing analytics data, use the applicable skill from
`.agents/skills/third-party/zenith779/ga-gsc-skills/`:

- `google-analytics` for GA4 property reporting, traffic, engagement, and source or device comparisons.
- `google-search-console` for website search performance, query and page CTR, and URL inspection.
- `google-trends` for keyword interest and trending-topic research.

These are reporting and research skills, not substitutes for Android instrumentation guidance. When a
task spans measurement design and existing-data analysis, use both the reporting skill and
`ga4-app-analytics`.

Use only data sources the project is authorized to access. Keep service-account keys, tokens,
credentials, and private exports out of the repository. Report missing access rather than inventing
metrics. Analytics findings may inform marketing decisions, but do not authorize tracking changes or
unrelated code changes.

## Marketing and design workflow

Marketing and application development are connected workflows with separate instruction files:

- This file governs Android architecture, Compose implementation, code quality, and validation.
- `marketing/AGENTS.md` governs design exploration, `marketing/ui`, motion and video, store assets,
  and campaign production.

Load `marketing/AGENTS.md` when working on a new user-facing screen, a substantial UI/UX redesign,
marketing UI parity, promotional assets, trailers, or store listings. Load each `AGENTS.md` file only
once per session; never reload them recursively.

Skip `marketing/AGENTS.md` for repository and data-layer fixes, nonvisual refactors, ordinary bug
fixes that keep the approved design intact, and standalone GA4 instrumentation tasks. Do not produce
social captions, marketing campaigns, or Remotion setups for ordinary Android feature work.

### Design-first development

For new user-facing screens or substantial UI/UX redesigns, follow this sequence, loading each skill
when its phase starts:

1. **Inspect**: identify affected modules, local `README.md` files, dependencies (including whether
   App Toolkit is used), navigation, the application theme, and reusable components.
2. **Plan**: use `android-ui-layer` for Google's UI architecture, `app-toolkit` for Toolkit screen
   contracts, `layered-tree-review` for module and package placement, and `android-data-layer` (plus
   `android-domain-layer` only when justified) for data needs. Decide navigation, DI, permissions,
   persistence, background work, and test requirements.
3. **Material design**: use `material-ui` to choose components, tokens, typography, states, adaptive
   behavior, and Expressive and motion treatment. Add `motion-design` only when the motion needs
   choreography beyond Material's specs.
4. **Interactive prototype**: load `marketing/AGENTS.md` once and build the screen in `marketing/ui`
   with `marketing-ui` across realistic states (`loading`, `empty`, `populated`, `error`, selection)
   and supported windows (`phone`, `fold`, `desktop`).
5. **Approval**: present the design, key interactions, and handoff
   (`marketing/.agents/skills/first-party/marketing/marketing-ui/references/design-handoff.md`).
   **Do not modify Compose or Kotlin UI files until the design is approved**, unless the user
   explicitly authorized proceeding without a separate approval checkpoint.
6. **Compose implementation**: build the approved design using `app-toolkit` for Toolkit screen
   wiring, `android-ui-layer` for UI architecture, `layered-tree-review`, `ponytail`,
   `traceable-code`, `material-ui` for Material APIs, and existing components, without unapproved
   visual changes.
7. **Validation**: validate the build and tests with `android-build-validation`, and inspect the
   rendered UI.
8. **Parity**: compare the Compose UI against `marketing/ui` (states, interactions, accessibility,
   adaptive layouts) and update the prototype and handoff where implementation constraints changed
   the design.

For an existing screen, `marketing-ui` also works in reverse: it translates the Compose source into
`marketing/ui` for synchronization and marketing assets, with no approval step.

The marketing UI is the design exploration and approval environment. The Android implementation
remains authoritative for runtime behavior, supported states, and technical contracts. Keep both
synchronized without inventing app behavior in previews. Do not modify unrelated marketing resources
during development tasks.

## General engineering rules

* Reuse existing project patterns before introducing new abstractions.
* Do not perform unrelated cleanup or refactoring.
* Remove code made unused by the change.
* Do not add a dependency unless it is genuinely needed.
* Prefer existing App Toolkit APIs over adding parallel implementations.
* Preserve naming and package conventions already used by the owning module.
* Do not introduce architectural layers merely for symmetry.
* Do not change frameworks or architectural approaches unless the task requires it.
* Keep public APIs small and deliberate.
* Keep implementation details in the module that owns them.

## Module boundaries

The application module is the composition root.

In App Toolkit this is:

```text
:sample:app
```

In consumer applications this is normally:

```text
:app
```

The app module may connect otherwise independent modules and assemble the final dependency graph.

### Core modules

`:core:*` modules contain shared application infrastructure and contracts.

Core modules must not depend on:

```text
:feature:*
:app
```

Shared behavior needed by several features should normally move into an appropriate core module
rather than creating feature-to-feature dependencies.

### Integration modules

`:integration:*` modules own product-specific connections to external SDKs or services.

Integration modules must not depend on:

```text
:feature:*
:app
```

They may expose contracts or implementations consumed by the app, core, or features according to the
existing architecture.

### Feature modules

A feature owns its own UI and feature-specific behavior.

A feature must not depend on:

```text
another sibling :feature:*
:app
```

When two features need the same capability, extract the shared contract or implementation into an
appropriate common module instead of coupling the features.

### App-owned composition

Code whose purpose is to connect otherwise independent product modules belongs in the app
composition layer.

Core and feature modules must not import app-owned composition code.

### Navigation

Core navigation may define shared navigation contracts, keys, state, and infrastructure.

It must not know the concrete implementation of product features.

The application composition layer connects navigation contracts to feature destinations.

### Package ownership

Do not split the same Kotlin package across several modules.

A package should have one clear module owner.

When moving files between modules, update their package so ownership remains clear.

### App Toolkit library boundaries

`:library:*` is reusable code.

Library modules must not depend on the sample application or on product-specific consumer
application code.

`:library:apptoolkit` assembles and exposes the Toolkit's host-facing graph. It is not the default
home for implementations that belong to a specific core, feature, navigation, or integration module.

## Architecture and layers

Use the Android architecture skills for detailed decisions.

The usual dependency direction is:

```text
UI <-> Domain (optional) <-> Data
```

The domain layer is optional.

### Repositories

Repositories are the public entry point to the data layer.

UI and domain code must not reach through a repository to use its data sources directly.

Repositories may:

* coordinate data sources
* map external models into application models
* manage caching
* resolve sources of truth
* perform data-related transformations
* expose observable application data

### Data sources

A data source represents an actual source of data or platform state, such as:

```text
network
database
DataStore
files
Android platform API
persistent background work
```

Do not create a data-source abstraction merely to satisfy a pattern.

### Domain layer

Do not create a `domain/` package simply because a feature has data models.

If a ViewModel or state holder reads a repository directly and there is no meaningful domain logic,
that module does not need a domain layer.

In that case, application models may live under the data layer, for example:

```text
data/models/
```

Do not keep a `domain/` package that contains only models in order to make the tree look
symmetrical.

### Use cases

Do not create a use case that only forwards a repository call.

Do not create a use case solely to trim, sort, filter, de-duplicate, or map data coming from one
repository when that work naturally belongs to the repository's data transformation.

Introduce a use case when it provides meaningful value, such as:

* reusable business logic
* coordination between multiple repositories
* a meaningful application operation
* substantial reduction of state-holder complexity
* logic reused by several consumers

A ViewModel may depend directly on a repository when a use case would add no useful behavior.

### Threading and main safety

Data-layer APIs must be safe to call from the main thread.

Do not add `withContext(Dispatchers.IO)` mechanically.

First determine whether the underlying API is already asynchronous or main-safe.

Move genuinely blocking or CPU-heavy work to the appropriate dispatcher in the class that owns that
work.

Inject dispatchers when doing so improves testability or follows the existing project pattern.

Already main-safe, so never wrapped in `withContext` or `flowOn`:

* DataStore reads, writes and flows
* Room suspend and `Flow` APIs
* Ktor and Retrofit suspend calls
* Play Core, Play Billing, UMP and other callback or `Task` APIs, awaited with `await` or a
  `suspendCancellableCoroutine`; their callbacks already arrive on the main thread

Blocking or CPU-heavy, so switched in the class that does it:

* `PackageManager` lookups, `ContentResolver` queries and file I/O (`dispatchers.io`)
* Mobile Ads SDK initialization, which Google asks to run off the main thread (`dispatchers.io`)
* decoding or drawing bitmaps, sorting or mapping large lists (`dispatchers.default`)

Rules that follow from this:

* Take dispatchers from the injected `DispatcherProvider`; never hardcode `Dispatchers.IO`,
  `Dispatchers.Default` or `Dispatchers.Main` in production code. A default value on a constructor
  parameter that tests replace is acceptable.
* Switch around the blocking call only, not around a whole function that also calls main-safe APIs.
* A ViewModel takes a `DispatcherProvider` only for CPU work it does itself, never to move
  repository calls.
* When a call blocks the main thread, a switch in its caller is never the fix; make the owning
  repository or data source main-safe instead.

## App Toolkit usage

Consumer applications should use App Toolkit as the shared foundation rather than reimplementing it.

Prefer Toolkit-provided:

* themes and design-system components
* shared UI components
* settings infrastructure
* navigation contracts
* host provider APIs
* permissions infrastructure
* issue reporting
* analytics infrastructure
* Toolkit integrations already exposed publicly

Product-specific behavior remains in the consumer application.

Do not move product policy into the Toolkit merely because several applications happen to use
similar code. Promote code to the library only when it is genuinely reusable and has a clear
host-facing contract.

When Toolkit behavior is designed to be customized by a host provider, resource override, callback,
or configuration object, use that extension point rather than forking the implementation.

## Technology choices

Follow the technology already configured by the project and its version catalog.

These projects generally use Kotlin and Jetpack Compose with App Toolkit's established Android
stack.

Do not hardcode dependency versions in this file.

Do not replace an existing project choice with an alternative framework because another approach is
possible.

Examples include replacing:

```text
Navigation 3 with Navigation 2
Koin with another DI framework
Ktor with another networking stack
DataStore with SharedPreferences
existing Compose architecture with XML screens
```

unless the task explicitly requires such a migration.

When changing one of these areas, use the relevant skill and inspect the project's current
dependencies first.

## Product identity and external contracts

Treat an already-published application's identity as stable.

### applicationId

Do not change an existing released `applicationId` merely to match:

```text
namespace
Kotlin package names
repository naming
new branding
```

`namespace` and `applicationId` serve different purposes and do not need to match.

Changing a released `applicationId` creates a different Android application from Google Play's point
of view.

If a task explicitly requires changing application identity, treat it as a migration and inspect
every dependent service before making the change.

### Other stable identifiers

Be careful when changing identifiers that may be persisted, referenced outside the module, or
registered with Android or another service.

Examples include:

* deep links
* provider authorities
* Firebase configuration
* AdMob identifiers
* Play Billing configuration
* notification channel IDs
* intent actions
* shortcuts
* widget providers
* service and receiver contracts
* persisted WorkManager behavior
* externally referenced component names

Do not rename these as routine cleanup.

Investigate their compatibility requirements first.

## Secrets and local configuration

Never commit:

* signing keys
* signing passwords
* private API credentials
* private access tokens
* service-account credentials
* machine-specific configuration
* `local.properties`

Treat `google-services.json` according to the repository's established policy. In this project
family it is expected to remain local rather than becoming a source-controlled credential file.

Do not add fake production credentials to make a build pass.

## Localization and string ownership

Use the Android localization skill whenever changing user-facing strings.

Inspect the target module's resources and Gradle configuration before editing translations.

Do not assume the supported locale list.

Do not create new locale directories unless the task requires them.

Preserve:

* resource names
* placeholders
* formatting tokens
* plural quantities
* markup
* escaping
* intentional whitespace

### Feature strings

Strings belong to the module that owns the surface rendering them.

A feature's product copy belongs in that feature module.

Do not move feature strings into a shared resource module simply because that module already
contains a `res/` directory.

### Shared strings

A string shared by unrelated features belongs in a shared module that those features already depend
on when it is truly generic.

Examples include common button labels such as:

```text
OK
Cancel
Save
Back
```

Do not make product-specific text globally shared only to avoid duplication.

### Host-supplied library strings

A library surface whose text is intentionally provided by the host may declare resource placeholders
and let the application provide the translated product copy.

Keep ownership clear and document the host contract in the owning library module.

## Module documentation

Each active module may contain a local `README.md`.

Treat it as the primary written source for that module's responsibilities and contracts.
Use `writing-style` for the README and Mermaid diagram, and the `app-toolkit` starter for a new module.

Before substantial changes:

* read the module README
* understand what the module owns
* understand what it explicitly does not own
* check its dependencies
* check its public contracts
* inspect important documented flows and architectural decisions

Update the README when the change affects documented:

* responsibilities
* ownership
* dependencies
* public contracts
* important flows
* architectural decisions
* integration requirements
* known risks

Do not update module documentation for:

* formatting
* cosmetic changes
* internal refactors with unchanged behavior
* routine maintenance
* test-only changes that do not alter a documented contract

Preserve the README structure already used by the repository.

When creating documentation for a new module, use nearby module READMEs as the structural reference
instead of inventing a new format.

Do not enforce a fixed section count when the module needs additional sections such as migration
notes, internal implementations, publishing information, architecture guards, or current risks.

## KDoc and comments

Apply `writing-style` to public API KDoc, meaningful internal operations and inline comments.
Include inherited documentation and the owning module's flow chart in review when the affected code
changes. Use the configured Dokka checks for published modules.

## Repository-level docs

Module technical documentation belongs with the module.

Use a repository-level `docs/` directory only for documentation that genuinely applies beyond one
module or for established special-purpose records such as crash investigations.

Do not duplicate module README content into `docs/` or into this file.

## Changelog

Use `app-changelog` when a change may be meaningful to:

* application users
* sample app users
* library consumers
* public API users
* release behavior

Update the changelog for externally meaningful changes such as:

* new features
* public API additions
* behavior changes
* important bug fixes
* compatibility changes
* meaningful reliability improvements
* meaningful performance improvements
* deprecations or removals

Do not add changelog entries for ordinary:

* refactors
* formatting
* tests
* file moves
* internal renames
* documentation-only work
* dependency bumps with no observable effect

Describe the result, not the implementation history. For Play Store "What's new" release notes,
use `play-store-changelog` instead.

## Validation

Use the Android build validation skill before reporting a coding task as complete.

Do not assume one fixed command is correct for every project.

Inspect the repository for the actual verification tasks.

### Validation order

Prefer this progression:

```text
affected module tests
        ↓
related integration or contract tests
        ↓
repository-specific verification tasks
        ↓
broad JVM test suite
        ↓
lint or other static checks when applicable
        ↓
device or emulator validation when runtime behavior requires it
        ↓
final review with traceable-code, and ponytail-review for substantial or risky changes
        ↓
documentation, localization, analytics, and changelog requirements where relevant
```

A successful build does not establish that animations, gestures, navigation, or visual behavior work
correctly.

Examples of repository-specific checks may include:

```text
checkModuleBoundaries
checkLocalizedResources
check
lint
test
```

These are examples. Run only tasks that actually exist in the current project.

### App Toolkit

The App Toolkit repository contains a `checkModuleBoundaries` verification task for the sample
application and the library.

Its sample rules include:

* `:sample:core:*` cannot depend on `:sample:feature:*` or `:sample:app`
* `:sample:integration:*` cannot depend on `:sample:feature:*` or `:sample:app`
* sibling `:sample:feature:*` modules cannot depend on each other
* feature modules cannot depend on `:sample:app`
* packages in the sample must not be split across modules
* non-app sample modules must not import app-owned composition packages
* `:sample:core:*` must not import product feature implementations
* inline analytics screen names are rejected

Its library rules include:

* `:library:core:*`, `:library:integration:*`, `:library:navigation` and `:library:shell` cannot depend on
  `:library:feature:*` or `:library:apptoolkit`
* no library module can depend on a `:sample:*` module
* sibling `:library:feature:*` modules cannot depend on each other. A feature opens another's page
  by navigating to its key from `:library:navigation`, and shares a contract through
  `:library:core:ui` and Koin (as `IssueReporterSheet` does); only `:library:apptoolkit` names every
  feature
* packages in the library must not be split across modules

Its rules for both the sample and the library include:

* the package vocabulary and file placement of the `layered-tree-review` skill: no `presentation`,
  `datasource` or singular layer packages, DTOs, use cases, UI states, ViewModels and repositories
  in their packages, no `domain` package without a use case, and no mapper named after a layer
* no product analytics in `data` or `domain` code: those layers take `DiagnosticsReporter` for
  breadcrumbs and failures, and `TelemetryCollection` where they apply consent, never
  `TelemetryRepository`, `AnalyticsReporter`, analytics events or the analytics SDK.
  `:library:integration:firebase`, the telemetry transport, is the one exception

These rules are build-enforced inside App Toolkit.

Consumer applications should follow the equivalent architectural rules, but do not claim they are
build-enforced unless that repository actually contains equivalent verification.

### Environment problems

A missing Android SDK or incomplete local environment is not a passing build.

Prepare the environment when reasonable and rerun the validation.

Do not change production code merely to work around a broken development environment.

## Testing changes

When behavior changes, add or update tests where they provide meaningful regression protection.

Test the behavior and contract rather than implementation details where practical.

Prefer the project's existing testing style.

Do not add an abstraction only because it makes mocking easier.

Do not delete or weaken a test simply to make the suite green.

If a failing test existed before the change, identify it as a pre-existing failure rather than
silently ignoring it.

## Change discipline

Keep changes focused on the requested work.

Do not:

* overwrite or discard existing user changes
* rewrite nearby code without a reason
* perform opportunistic architecture migrations
* rename unrelated symbols
* reorganize unrelated packages
* update unrelated translations
* change dependency versions without need
* change public APIs casually
* duplicate existing Toolkit behavior
* broaden the task because another design also looks attractive

A small correct patch is preferred to a large cleanup attached to the same task.

If broader cleanup is genuinely required to make the requested change correct, keep it directly
connected to the task and explain why.

## Writing style

Use `writing-style` for all project writing: documentation, README files, changelogs, release notes,
KDoc, code comments, commit messages, and user-facing technical copy. The skill holds the rules.

## Completion report

When finishing implementation work, report what was actually done.

Include:

* the important behavior changed
* the modules or areas affected
* relevant tests or verification tasks that were run
* device or emulator validation when performed
* anything that could not be verified
* any remaining compatibility or migration concern

Do not report a check as passing unless it was actually run and passed.

Do not say the whole project is verified when only one targeted test was run.

Keep documentation, code, and the completion report consistent with each other.
