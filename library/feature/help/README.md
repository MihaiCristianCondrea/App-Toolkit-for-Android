# `:library:feature:help` Logic Graph

## Purpose

Displays localized FAQ/help content, loading a product-specific catalog locally or remotely and
exposing contact/review actions.

## Owns

- Help screen/activity/ViewModel and their state/event/action contracts.
- FAQ domain model, repository contract, and `GetFaqUseCase`.
- Local and remote FAQ sources, DTOs, mapper, and repository implementation.

## Does not own

- About/navigation route definitions, owned by `:library:feature:about`.
- In-app review implementation, owned by `:library:integration:review`.
- HTTP client construction, owned by `:library:core:network`.
- Host identity strings, supplied as overridable defaults by `:library:core:common`.

## Depends on

- `:library:core:common`, `:library:core:datastore`, `:library:core:network`, and `:library:core:ui`
  for shared configuration, state, persistence access, networking, and UI.
- [`:library:navigation`](../../navigation/README.md) for feature navigation.
- [`:library:integration:review`](../../integration/review/README.md) for review prompts.
- [`:library:navigation`](../../navigation/README.md) for shared AppToolkit routes; the remaining
  About dependency supplies feature-specific settings/navigation integration.

## Used by

- `:sample`, `:library:apptoolkit`, and `:library:feature:settings`.

## Flow chart

```mermaid
flowchart TD
    Screen[HelpScreen] -->|events| VM[HelpViewModel]
    VM --> UseCase[GetFaqUseCase]
    UseCase --> Repo[FaqRepository]
    Repo --> Remote[Remote FAQ data source]
    Remote -->|success| Map[DTO to FaqItem mapping]
    Remote -->|failure or empty catalog| Local[Bundled FAQ data source]
    Local --> Map
    Map --> State[Help UiStateScreen]
    State --> Screen
    Screen -->|review action| VM
    VM --> Review[In-app review use case]
    Screen -->|contact action| Host[Email / external intent]
```

## Architectural decisions

- The repository owns remote-versus-bundled fallback so the ViewModel receives one FAQ contract and
  does not know which source answered.
- Remote DTO mapping stays in the data layer; the localized bundled catalog is also a data source,
  not hardcoded composable content.
- Review and contact actions are triggered by UI intent but executed through their integration or
  platform boundary rather than embedded in card rendering.

## Public contracts

- Help presentation entry points/contracts, `FaqRepository`, `FaqItem`, and `GetFaqUseCase`.

## Internal implementations

- Catalog DTOs/mapping, local fallback, remote fetch, and help card/menu composition.

## Current risks

The module depends on the broad `about` feature for shared navigation definitions, creating more
coupling than the help flow itself requires.
