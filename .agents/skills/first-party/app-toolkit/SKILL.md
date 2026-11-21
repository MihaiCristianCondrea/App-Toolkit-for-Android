---
name: app-toolkit
description: >
  Coordinate implementation, code review, and integration for App Toolkit for Android,
  its sample app, and Android apps built on it. Route work to focused architecture,
  UI, data, domain, code style, and validation skills using the target project's contracts.
---

# App Toolkit

Start here for App Toolkit work. Identify whether the task changes the reusable library,
the sample app, or a consumer app. Follow the target's local instructions and identify the owning
module before proposing a change.

- **Toolkit source available.** Read the affected module README, implementation, tests, and build
  configuration. For integration, resolve the Toolkit dependency or source substitution used by
  the target app. Use the current source for library work and the resolved public contract for
  consumer work. Treat examples here as patterns, with signatures subject to verification.
- **Toolkit source unavailable.** Use the references below to make architecture and placement
  decisions. Verify specific API calls against the consumer's dependencies, IDE declarations,
  sources JAR, artifact, or published docs for its version. If a symbol cannot be verified,
  identify the gap and keep the design independent of that symbol. A Toolkit checkout is optional.

Read only the references and installed specialist skills that the task needs. Look up specialist
skills by name in the active catalog, since installations can use different directory layouts.
The references here describe the Toolkit conventions even when a specialist is unavailable.

| Task | Read | Specialist scope |
| --- | --- | --- |
| Ownership, naming, dependencies | [Architecture](references/architecture.md) | `layered-tree-review` owns placement, `android-data-layer` owns repository and data source behavior, and `android-domain-layer` decides when a use case earns its place. |
| Compose screen, route-facing content, messages | [Screen contracts](references/screen-contracts.md) | `android-ui-layer` owns rendering and screen state. Load `navigation-3` when changing navigation. |
| ViewModel, events, coroutine policy | [ViewModel conventions](references/viewmodel-conventions.md) | `android-ui-layer` owns state and effects. `traceable-code` guides readable dispatch and control flow. |
| Reusable library feature or public API | [Library development](references/library-development.md) | Use the owners above for the affected layers and `changelog` for consumer-facing changes. |
| Sample or consumer integration | [Consumer integration](references/consumer-integration.md) | Use the owner of the capability being integrated, such as `android-ui-layer` for screen wiring. |
| Code review | [Code review](references/code-review.md) | Load the owners for the code under review. |
| Verification | [Validation](references/validation.md) | `android-build-validation` owns environment setup and Gradle checks. |

Load the relevant specialist for work involving storage, localization, telemetry or another
specific Android API. Keep the target project's approval and design workflows in force. Each
specialist retains ownership of its subject.
