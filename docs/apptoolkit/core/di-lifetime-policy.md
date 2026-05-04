# DI Lifetime Policy (Koin)

This policy defines when App Toolkit bindings should use `single` vs `factory`.

## Use cases

1. **Default to `single` for stateless use cases** that only delegate to repositories/services and keep no mutable execution state.
2. Use `factory` **only** when a use case keeps mutable execution state, per-screen/per-request caches, host objects, or cancellation handles.

### Current examples

- `FetchDeveloperAppsUseCase` is a good `single` example because it is a stateless repository wrapper.
- `ObserveFavoritesUseCase`, `ToggleFavoriteUseCase`, `GetFaqUseCase`, and `RequestInAppReviewUseCase` follow the same pattern and should remain `single`.

### Anti-examples for future contributors

Prefer `factory` if a use case:

- stores mutable properties between executions,
- owns request-specific resources (e.g., per-screen host/controller),
- retains cancellable jobs/handles that should not leak across screens.

## Services, managers, and factories

- Use `single` for heavy/stateful managers (for example `BillingRepository`, datastore, network clients).
- Use `factory` for lightweight creators or providers that may later capture Activity/screen references (for example UI content providers with composable lambdas).
- Keep `viewModel` bindings as `viewModel { ... }`.

## Related bindings in this repo

- `GeneralSettingsContentProvider` should remain `factory` because it carries UI content lambdas and is safer as short-lived.
- `GmsHostFactory` is currently `single` because it is a lightweight creator without screen-scoped capture.
