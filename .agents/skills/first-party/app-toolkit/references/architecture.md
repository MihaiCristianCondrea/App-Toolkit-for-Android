# Architecture and placement

The reusable Toolkit is organized into core, features, integrations, navigation, shell, and the
host-facing assembly module. The sample is a host with its own composition root. A consumer or standalone app
can use the same ownership model with fewer modules. Apply dependency boundaries to the modules
it already has.

- Core owns shared infrastructure and contracts. Core, integrations, navigation and shell stay
  independent of concrete Toolkit features and the assembled Toolkit module.
- Features own their screens and feature behavior. Keep sibling features independent. Use shared
  route keys for navigation and an existing shared contract for cross-feature capabilities. The
  assembly layer connects the implementations.
- The app module assembles the consumer's dependencies, graph and host providers. Reusable Toolkit
  code stays independent of sample and product code. Give each Kotlin package one module owner.

Keep a feature's primary screen and ViewModel under `ui/`. Place events in `ui/contracts/`,
state in `ui/states/`, screen-specific models in `ui/models/`, and reusable Compose pieces in
`ui/views/`. Give a distinct subfeature its own `ui/features/<name>/` when its screen or flow warrants a separate owner. Keep labels and display conversion close to their UI owner. Extract a named
responsibility when it needs an independent contract. Once extracted, place it by responsibility
using `layered-tree-review`. Local state copies and private ViewModel action names stay with the
operation that owns them.

Repositories are the data layer entry points. Put repositories in `data/repositories/`, actual
local or remote sources in `data/local/` or `data/remote/`, and data models in `data/models/`
when the feature has no domain layer. Add `domain/usecases/` for meaningful business logic,
coordination or reuse. A ViewModel can call a repository directly. A forwarding use case or a
reducer that just wraps a state copy adds indirection.

Data APIs must be main-safe. The owner of a blocking call switches only that work to the
appropriate injected dispatcher. Suspend DataStore, Room and network calls that are already
main-safe need no switch. Keep product analytics in UI or at the relevant product boundary.
Repositories use diagnostics for operational failures.

## Example: a genuinely new feature

Suppose the app needs a catalog absent from the host and the resolved Toolkit API. Use
`android-ui-layer` for the screen contract and `android-data-layer` for fetching. Read the
placement reference before drafting files. A typical remote catalog has `ui/CatalogViewModel.kt`,
`ui/contracts/CatalogEvent.kt`, `ui/states/CatalogUiState.kt` and a repository under
`data/repositories/`. Put wire DTOs and DTO-to-model mapping under `data/remote/` in their
designated packages. The ViewModel can call the repository directly unless a domain operation
adds meaningful policy or coordinates distinct data sources through repositories. Plan tests for
load, failure and recovery with the project's existing tooling.
