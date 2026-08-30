# Modularization Status

## Current state

- Runtime and tests share `sampleAppModules` as the single DI composition declaration.
- Apps, Tiles, and Components own their Navigation 3 route keys and route identifiers; the app
  composes them into the drawer, bottom bar and startup-screen choices.
- Components owns its unlock repository and threshold rule; the app renders the drawer entry.
- The app owns the About-to-Components bridge because it is cross-feature composition.
- Sample analytics identifiers live in `:sample:core:analytics`; Firebase delivery remains in the
  reusable library.
- Sample ad unit IDs, placement qualifiers and `AdsConfig` bindings all live in
  `:sample:integration:ads`, covered by a qualifier graph test.
- Feature Android components are declared by their owning module manifests.
- `checkModuleBoundaries` guards dependency direction, package separation, navigation ownership,
  app composition imports, and inline screen identifiers.

## Deliberate compatibility constraints

- Existing Kotlin packages, application IDs, component class names, and manifest identities have
  not been renamed.
- `:sample:app` remains the expected merge point for the complete feature set and runtime graph.

## Remaining risks

- The shared sample-module convention enables Compose for every sample library, including modules
  that currently contain only contracts.
- App-level feature aggregation can still produce merge conflicts when several destinations are
  added concurrently.
- Boundary checks are targeted architectural guards, not a replacement for compilation, unit
  tests, lint, or on-device validation.
