# Startup (AppToolkit)

The **Startup** feature is the AppToolkit entry layer responsible for preparing app-level
dependencies,
bootstrapping initial checks, and rendering a lightweight launch experience before handing off to
the
main navigation flow.

---

## Responsibilities

- Render a startup/loading surface with Compose in `StartupActivity`.
- Run app initialization orchestration from the startup domain layer.
- Provide a single integration point for host apps that want a consistent splash-to-main transition.
- Keep startup logic isolated from downstream feature screens.

---

## Architecture

### Domain

- Contains startup-oriented use cases and orchestration utilities.
- Encapsulates logic that should happen before users interact with feature screens.

### UI

- `StartupActivity` hosts `StartupScreen`.
- `StartupScreen` is intentionally minimal and optimized for quick first-frame rendering.

### Utilities

- Shared helpers handle startup triggers and transition timing.

---

## Host app integration

Launch startup as your initial activity (or as an explicit handoff entry point):

```kotlin
startActivity(Intent(context, StartupActivity::class.java))
```

Recommended host flow:

1. Launch `StartupActivity` from launcher intent.
2. Let startup orchestration complete required checks.
3. Route to your app's main screen/navigation graph.

---

## Implementation notes

- Keep startup work deterministic and bounded in time.
- Avoid heavy, long-running operations directly in UI composition.
- Delegate business rules to domain/use cases and keep the screen as a rendering coordinator.
- Prefer observable state transitions so loading/error/success behaviors remain testable.

---

## Related docs

- `docs/apptoolkit/screens/main/main.md` – main-screen destination after startup.
- `docs/viewmodel-rules-coroutines-flows-state.md` – flow/event/state guidance for startup
  ViewModels.
