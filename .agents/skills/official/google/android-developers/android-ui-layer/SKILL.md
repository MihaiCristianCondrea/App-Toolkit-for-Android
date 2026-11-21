---
name: android-ui-layer
description: >
  Apply Google's Android UI layer guidance when designing screens, exposing UI state, choosing
  state holders, handling UI events, or producing state from asynchronous sources.
metadata:
  author: Google
  maintainer: android-skills
  source: https://developer.android.com/topic/architecture/ui-layer
  last-updated: '2026-11-21'
  keywords:
    - android architecture
    - ui layer
    - ui state
    - state holders
    - ui events
    - unidirectional data flow
---

# Android UI layer

Use this skill for the platform architecture. Read the relevant local copy of the Android
Developers guidance before deciding how the screen should collect state and handle events.

| Decision | Reference |
| --- | --- |
| Separate rendering from state production and apply unidirectional data flow | [UI layer](references/ui-layer.md) |
| Choose a ViewModel or a plain state holder and decide which logic it owns | [State holders](references/stateholders.md) |
| Handle user input, navigation, and feedback at the appropriate owner | [UI events](references/events.md) |
| Produce observable state from synchronous or asynchronous inputs | [State production](references/state-production.md) |

## Lifecycle and ViewModel APIs

| Decision | Reference |
| --- | --- |
| Scope work to a lifecycle and collect flows safely | [Lifecycle](references/lifecycle-components/lifecycles/lifecycle.md), [coroutines](references/lifecycle-components/coroutines.md) |
| Choose a ViewModel owner and retrieval API | [ViewModel overview](references/lifecycle-components/viewmodel/viewmodel.md), [APIs](references/lifecycle-components/viewmodel/viewmodel-apis.md), [cheat sheet](references/lifecycle-components/viewmodel/viewmodel-cheatsheet.md) |
| Supply dependencies when constructing a ViewModel | [Factories](references/lifecycle-components/viewmodel/viewmodel-factories.md) |
| Decide what survives recreation or process death | [Saving UI state](references/lifecycle-components/saving-states.md), [ViewModel saved state](references/lifecycle-components/viewmodel/viewmodel-savedstate.md) |

Follow the project's established UI stack. Pass state down and send actions to the owner that
can handle them. Keep business logic in a state holder and local UI behavior close to the
rendering code. Collect observable UI state with lifecycle awareness.

Use `android-data-layer` for repository contracts and `android-domain-layer` when deciding whether
to add a use case. For Toolkit-owned components, file layout and starter templates, load
`app-toolkit`. Google authored the reference documents. The `android-skills` project maintains this routing wrapper.
