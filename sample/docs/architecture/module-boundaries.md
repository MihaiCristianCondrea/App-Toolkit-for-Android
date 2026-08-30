# Sample Module Boundaries

The sample follows one dependency direction: the app composes independent feature, core,
integration, and widget modules. Reusable modules never depend back on the app.

| Module type | Path | Responsibility |
| --- | --- | --- |
| App | `:sample:app` | Android packaging, startup, complete DI graph, navigation aggregation, and cross-feature bridges. |
| Core | `:sample:core:*` | Neutral contracts and capabilities reused by multiple features. |
| Feature | `:sample:feature:*` | A user-facing vertical slice, including its routes, state, data ownership, and DI bindings. |
| Integration | `:sample:integration:*` | Host configuration for an external or reusable SDK boundary. |
| Widget | `:sample:widget` | Home-screen widget UI, receiver, and widget-specific data access. |

## Enforced rules

- Core and integration modules cannot depend on feature modules.
- Feature modules cannot depend on sibling features.
- Core, feature, and integration modules cannot depend on `:sample:app`.
- A Kotlin package cannot be split across sample modules.
- App composition packages cannot be imported from reusable sample modules.
- Core navigation cannot import feature implementations.
- Analytics screen names must come from `AppScreenTracking`, not inline literals.

`./gradlew checkModuleBoundaries` performs the repository-wide source checks. The dependency rules
are evaluated while sample modules are configured, and each sample module's `check` task depends on
the repository-wide check.

## Ownership guidance

- A feature owns its route key and route identifier, and exports its screen and Koin module. It
  does not register itself anywhere: the app aggregates entry builders, drawer items, bottom-bar
  items and startup-screen choices, because it is the only module that may see every feature.
- A feature owns persistence and business rules used only by that feature.
- Cross-feature adapters belong in the app composition root. They should connect public contracts
  without making either feature depend on its sibling.
- The reusable library owns transport and generic UI contracts. Product-specific analytics names,
  ad IDs, and host configuration stay in the sample.

Package names are not required to mirror Gradle paths. Existing package and Android component
identities remain stable unless a separate compatibility-aware migration is approved.
