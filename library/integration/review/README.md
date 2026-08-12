# `:library:integration:review` Logic Graph

## Purpose

Encapsulates Google Play in-app review eligibility, prompting, and persisted request throttling.

## Owns

- Review repository contract/implementation and review outcome/host models.
- Normal and forced in-app-review use cases.
- Activity extension helpers for the Play review flow.

## Does not own

- UI decisions about when to ask for a review, owned by consuming features.
- Preference storage implementation, owned by `:library:core:datastore`.

## Depends on

- [`:library:core:common`](../../core/common/README.md) for shared contracts and results.
- [`:library:core:datastore`](../../core/datastore/README.md) for prompt history/eligibility persistence.

## Used by

- `:sample` and `:library:apptoolkit`.
- `:library:feature:about` for shared application flows.
- `:library:feature:help` to request a review after relevant help interactions.

## Flow chart

```mermaid
flowchart LR
    Feature[Feature event] --> UseCase[Review use case]
    UseCase --> Repo[ReviewRepository]
    Repo --> Store[Prompt history]
    Repo --> Play[Play ReviewManager]
    Play --> Outcome[ReviewOutcome]
```

## Public contracts

- `ReviewRepository`, review use cases, `ReviewHost`, and `ReviewOutcome`.

## Internal implementations

- Eligibility/throttling persistence and Play ReviewManager calls.

## Current risks

Repository packages retain the historical `playservices` name even though the active Gradle module is `:library:integration:review`, which can obscure current ownership.
