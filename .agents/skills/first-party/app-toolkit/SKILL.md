---
name: app-toolkit
description: >
  Coordinate implementation, code review, and integration for App Toolkit for Android,
  its sample app, and Android apps built on it. Apply the shared architecture and ViewModel
  conventions in standalone Android projects using their existing AndroidX stack.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-22'
  keywords:
    - app toolkit
    - android architecture
    - jetpack compose
    - viewmodel
    - navigation
    - consumer integration
    - feature templates
    - typed events
    - module documentation
---

# App Toolkit

## Discover before designing

1. Establish the target. For **Toolkit development**, search Toolkit feature modules, the assembled
   graph, and the sample. For an **installed Toolkit dependency**, resolve the actual artifact
   version and search the host's graph and bindings plus that version's public API. Toolkit source
   and the sample can suggest where to look. For **standalone Android**, search the app's own
   destinations and shared components and use its AndroidX stack.
2. Search by capability and by user-facing, route or module names. Read the owning README,
   implementation, tests and host example where available. Identify the entry point, extension
   points and what the host must supply.
3. Decide whether to reuse, configure, extend or build. For a consumer, verify every API used
   against the resolved public contract. If the available evidence is incomplete, say what remains
   unverified. [Consumer integration](references/consumer-integration.md) describes how to verify and reuse an installed capability.

Read only the references and installed specialist skills that the task needs. Look up specialist
skills by name in the active catalog, since installations can use different directory layouts.
Load `android-ui-layer` for Google's guidance on UI state, events and state holders. This skill
owns Toolkit-specific code and [starter templates](references/feature-template.md).
Use `layered-tree-review` for placement and verify Toolkit APIs against the target checkout or
resolved dependency.

| Task                                           | Read                                                         | Specialist scope                                                                                                                                                         |
|------------------------------------------------|--------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Ownership, naming, dependencies                | [Architecture](references/architecture.md)                   | `layered-tree-review` owns placement, `android-data-layer` owns repository and data source behavior, and `android-domain-layer` decides when a use case earns its place. |
| Compose screen, route-facing content, messages | [Screen contracts](references/screen-contracts.md)           | `android-ui-layer` owns rendering and screen state. Load `navigation-3` when changing navigation.                                                                        |
| ViewModel, events, coroutine policy            | [ViewModel conventions](references/viewmodel-conventions.md) | `android-ui-layer` owns state and effects. `traceable-code` owns Kotlin call sites and readable control flow.                                                                   |
| Reusable library feature or public API         | [Library development](references/library-development.md)     | Use the owners above for the affected layers and `app-changelog` for consumer-facing changes.                                                                                |
| Sample or consumer integration                 | [Consumer integration](references/consumer-integration.md)   | Use the owner of the capability being integrated, such as `android-ui-layer` for screen wiring.                                                                          |
| Code review                                    | [Code review](references/code-review.md)                     | Load the owners for the code under review.                                                                                                                               |
| Verification                                   | [Validation](references/validation.md)                       | `android-build-validation` owns environment setup and Gradle checks.                                                                                                     |

Before implementation, identify the affected responsibilities and checks:

- For a new or moved declaration, read the actual placement reference from `layered-tree-review`
  before proposing the file tree. When data behavior changes, use `android-data-layer` and
  evaluate whether `android-domain-layer` applies. A direct repository call may be enough.
- Use `navigation-3` for destination or graph changes in a Navigation 3 project, `android-localization`
  for localized resources, and `android-datastore` only for persistence work involving DataStore.
- Use `navigation-event` for back handling, `edge-to-edge` and `adaptive` for insets and
  window-size layouts, `android-permissions-security` for runtime permissions,
  `play-billing-library-version-upgrade` when changing Play Billing, and `r8-analyzer` for a
  published module's consumer keep rules.
- Use `traceable-code` for Kotlin call sites and `writing-style` for KDoc, module READMEs and Mermaid diagrams. Apply the [code review checks](references/code-review.md) to the touched code.
- For implementation, use `android-build-validation` to select checks. Use the existing test
  setup and the relevant specialist's testing guidance. Load `testing-setup` when the project
  needs a test strategy or infrastructure.

Use `ga4-app-analytics` with the existing telemetry contract when changing instrumentation, including
the `XAnalytics` and `XOperationNames` templates and tracked screens. Keep the target project's
approval and design workflows in force.
