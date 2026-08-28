# `:sample:feature:components` Logic Graph

## Purpose

The hidden components showcase and the unlock gesture that reveals it.

## Owns

- The concrete `ComponentsShowcaseRepository`, which owns the unlock flag.
- `ComponentsActivity`, `ComponentsScreen`, `ComponentsUnlockViewModel` and its contracts.
- `ComponentsNavigationBuilder`.
- Localized strings for the component showcase.

## Does not own

- Where the unlock gesture is performed. That is the About settings content in
  [`:sample:feature:settings`](../settings/README.md), which drives this module's ViewModel.
- The drawer entry that opens the showcase, owned by [`:sample:feature:home`](../home/README.md).

## Depends on

- `:sample:core:navigation`, `:sample:core:datastore`.
- [`:library:apptoolkit`](../../../library/apptoolkit/README.md) for the screen and state contracts.

## Used by

- `:sample:feature:settings` (unlock gesture), `:sample:feature:home` (drawer entry),
  `:sample:app` (DI).

## Flow chart

```mermaid
flowchart TD
    About[Version taps in About content] --> Event[ComponentsUnlockEvent]
    Event --> VM[ComponentsUnlockViewModel]
    VM --> Counter{Unlock threshold reached?}
    Counter -->|no| State[Updated tap progress]
    Counter -->|yes| Repo[ComponentsShowcaseRepository]
    Repo -->|persist unlocked| Store[DatastoreInterface]
    Store --> Home[NavigationConfigurationRepository]
    Home --> Drawer[Conditional drawer entry]
    Drawer --> Activity[ComponentsActivity]
    Activity --> Screen[ComponentsScreen]
    Screen --> Sections[Button / FAB / filter / input / layout / preference showcases]
```

## Architectural decisions

- The About surface owns the gesture location, while this feature owns threshold state and the
  persisted unlock mutation.
- The home feature observes the stored flag and decides whether to render navigation; the components
  feature does not reach into the drawer.
- A concrete repository is sufficient because there is one DataStore-backed implementation and no
  module boundary that requires substitution.

## Public contracts

- `ComponentsShowcaseRepository`, `ComponentsUnlockViewModel`, `ComponentsActivity`,
  `ComponentsNavigationBuilder`.

## Internal implementations

- Tap counting and the showcase screen content.

## Current risks

Two other feature modules depend on this one, which is more inbound coupling than a leaf feature
usually carries. Both edges are real, the gesture is in settings and the entry point is in the
drawer, but they mean this module cannot be removed without touching both.

## Migration notes

`ComponentsShowcaseRepository` was created when `UnlockComponentsShowcaseUseCase` was removed. It is
a concrete repository because the sample has one implementation; it still prevents the ViewModel
from holding a data source directly without adding an unused abstraction.
