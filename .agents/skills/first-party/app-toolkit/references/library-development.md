# Library development

Read the owning module's README and inspect its dependencies, public signatures, Koin bindings,
and route registration before editing. Place behavior in the module that owns it. The assembled
Toolkit graph wires feature modules and public host contracts. It does not own their implementations.

Declare concrete ViewModels and the types they expose with compatible public visibility.
Keep render-only content and implementation helpers as narrow as their callers allow. Preserve
existing public contracts during routine changes. Review visibility migrations separately and
check publishing configuration, the artifact and compatibility checks when relevant.

Bind Toolkit ViewModels in the feature's Koin module with `viewModel { ... }`. When moving a
legacy screen from `core.ui.base`, `core.ui.states` or `DataState`, trace the repository, ViewModel,
screen and tests together. Map data-layer failures at the UI boundary and preserve existing
retry, user feedback and reporting behavior. Use the [screen contracts](screen-contracts.md)
and [ViewModel conventions](viewmodel-conventions.md) for the target wiring.

Inspect host-facing defaults, persisted identifiers, and telemetry keys before changing them.
For a deliberate API or integration change, update the owning README and consumer-facing changelog,
and check dependent modules. Keep product policy and host-specific strings or integrations in the host.
