# AGENTS.md

Work with the existing project architecture and conventions.

Inspect the affected modules, their documentation, the relevant skills, and the surrounding code
before making changes. Prefer the smallest complete solution over broad refactors.

This file is shared across the App Toolkit project family. It is intentionally focused on rules that
apply across those projects. Detailed Android guidance belongs in the skills, and module-specific
knowledge belongs in each module's `README.md`.

## Project family

These projects are built around App Toolkit for Android.

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

Skills contain the detailed technical guidance that should not be duplicated in this file.

### Finding skills

Prefer a skill set in this repository:

```text
.agents/skills/
```

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

| Work                                                            | Skill                                                       |
|-----------------------------------------------------------------|-------------------------------------------------------------|
| Build validation, Gradle checks, SDK setup                      | `.agents/skills/android-build-validation/SKILL.md`          |
| Repository, data source, model, threading and caching decisions | `.agents/skills/architecture/android-data-layer/SKILL.md`   |
| Domain layer and use-case decisions                             | `.agents/skills/architecture/android-domain-layer/SKILL.md` |
| Module and package placement                                    | `.agents/skills/architecture/layered-tree-review/SKILL.md`  |
| Navigation 3                                                    | `.agents/skills/navigation/navigation-3/SKILL.md`           |
| Navigation events                                               | `.agents/skills/navigation/navigation-event/SKILL.md`       |
| Adaptive Compose layouts                                        | `.agents/skills/jetpack-compose/adaptive/SKILL.md`          |
| Compose styles and theming                                      | `.agents/skills/jetpack-compose/theming/styles/SKILL.md`    |
| Edge-to-edge and system UI                                      | `.agents/skills/system/edge-to-edge/SKILL.md`               |
| DataStore                                                       | `.agents/skills/android-datastore/SKILL.md`                 |
| WorkManager                                                     | `.agents/skills/android-workmanager/SKILL.md`               |
| Notifications                                                   | `.agents/skills/android-notification-views/SKILL.md`        |
| Glance widgets                                                  | `.agents/skills/glance-widget/SKILL.md`                     |
| Analytics and Firebase telemetry                                | `.agents/skills/ga4-app-analytics/SKILL.md`                 |
| Localization                                                    | `.agents/skills/localization/SKILL.md`                      |
| Testing                                                         | `.agents/skills/testing/testing-setup/SKILL.md`             |
| R8 and keep rules                                               | `.agents/skills/performance/r8-analyzer/SKILL.md`           |
| Intent and component security                                   | `.agents/skills/security/android-intent-security/SKILL.md`  |
| Changelog updates                                               | `.agents/skills/changelog/SKILL.md`                         |

Additional specialized skills exist under areas such as:

```text
build-system/
device-ai/
devtools/
kotlin/
play/
profilers/
wear/
```

Discover the available `SKILL.md` files instead of assuming this table contains every skill.

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
* Avoid comments and KDoc that only repeat what the code already says.
* Add or improve KDoc when it explains a public contract, invariant, side effect, ownership rule,
  lifecycle requirement, or non-obvious behavior.

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

Documentation must explain things that are not obvious from the code itself.

Useful KDoc and comments explain:

* public contracts
* lifecycle requirements
* ownership
* invariants
* side effects
* compatibility requirements
* unusual platform behavior
* reasons for a non-obvious implementation decision

Do not add comments that merely translate Kotlin into English.

If existing documentation becomes inaccurate because of the change, update it.

## Repository-level docs

Module technical documentation belongs with the module.

Use a repository-level `docs/` directory only for documentation that genuinely applies beyond one
module or for established special-purpose records such as crash investigations.

Do not duplicate module README content into `docs/` or into this file.

## Changelog

Use the changelog skill when a change may be meaningful to:

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

Describe the result, not the implementation history.

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
```

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

The App Toolkit repository currently contains a `checkModuleBoundaries` verification task for the
sample application.

Its rules include:

* `:sample:core:*` cannot depend on `:sample:feature:*` or `:sample:app`
* `:sample:integration:*` cannot depend on `:sample:feature:*` or `:sample:app`
* sibling `:sample:feature:*` modules cannot depend on each other
* feature modules cannot depend on `:sample:app`
* packages in the sample must not be split across modules
* non-app sample modules must not import app-owned composition packages
* `:sample:core:navigation` must not import product feature implementations
* inline analytics screen names are rejected

These rules are build-enforced inside App Toolkit's sample.

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

Use clear, natural, direct language in all project writing.

This applies to:

- documentation
- README files
- changelogs
- release notes
- KDoc
- code comments
- commit messages when asked to write them
- user-facing technical copy

### Style rules

- Never use em dashes (`—`). Use commas, parentheses, colons, or separate sentences instead.
- Avoid generic AI wording and unnecessary corporate language.
- Prefer simple technical words over inflated alternatives.
- Do not use words such as `facade`, `orchestrate`, `synergy`, `robust`, `seamless`, or similar wording when a simpler and more precise term exists.
- Do not describe ordinary code as "powerful", "comprehensive", "sophisticated", "elegant", or "production-ready" without a concrete reason.
- Avoid filler introductions and conclusions.
- Avoid repeating the same point using different wording.
- Prefer short paragraphs and direct sentences.
- Prefer concrete names such as `app module`, `repository`, `provider`, `screen`, `worker`, or `navigation entry` over vague terms such as `layer`, `system`, `solution`, or `mechanism` when the concrete thing is known.
- Explain why something exists when the reason is important. Do not merely restate what the code does.
- Keep technical documentation factual. Do not praise the implementation.
- Do not use exaggerated claims such as `ultimate`, `perfect`, `best`, or `fully optimized` in technical documentation.
- Do not write in a conversational assistant voice inside repository files.
- Match the tone and terminology already used by nearby project documentation.

### Formatting

- Use Markdown headings and lists only when they improve readability.
- Do not over-section short documentation.
- Keep bullet points concise.
- Use code blocks for commands, paths, APIs, or examples when that makes them easier to read.
- Use backticks for code symbols, module names, file names, Gradle tasks, resource names, and paths.
- Avoid decorative emoji in technical documentation unless the existing document already uses them intentionally.

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
