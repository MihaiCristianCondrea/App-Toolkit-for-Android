---
name: android-architecture
description: >
  Design, review, refactor, and explain Android application architecture
  following Google's official Android architecture guidance. Use when working
  with UI, ViewModels, repositories, data sources, domain/use-case layers,
  models, state, threading, caching, sources of truth, dependency direction,
  or when deciding where Android application logic should live.
metadata:
  author: Google LLC
  last-updated: '2026-08-13'
  keywords:
  - architecture
  - data layer
  - repository
  - datasource
  - model
  - source of truth
  - main-safety
  - threading
  - caching
---

# Android Architecture

Apply Google's recommended Android application architecture when designing,
reviewing, explaining, or modifying Android code.

Read `references/architecture.md` before making architecture decisions that
involve repositories, data sources, models, threading, caching, source of
truth, or data-layer responsibilities.

## Core architecture

Prefer the dependency direction:

UI -> Domain (optional) -> Data

The UI layer may access the data layer directly when a domain layer is not
needed.

Never make the data layer depend on the UI layer.

Never allow UI or domain classes to access data sources directly.

Repositories are the public entry points to the data layer.

## Before changing code

Inspect the existing architecture first.

Determine:

1. Which layer owns the code.
2. Which layer should own the responsibility.
3. What the source of truth is.
4. Whether the operation is UI-oriented, app-oriented, or
   business-oriented.
5. Whether the domain layer provides actual value.
6. Whether existing project conventions are compatible with the official
   guidance.

Do not introduce architectural abstractions merely to satisfy a pattern.

Prefer the smallest architecture that preserves separation of concerns,
testability, and clear ownership.

## UI layer

The UI layer displays application state and handles user interaction.

Prefer unidirectional data flow.

Screen-level ViewModels should expose UI state and receive actions from the UI.

Do not place persistence, networking, or data-source access in the UI layer.

Do not expose UI-specific models from lower layers unless the existing
architecture deliberately defines them as shared application models.

UI-specific formatting and presentation transformations belong in the UI layer.

## ViewModels

Use ViewModels as screen-level state holders.

ViewModels may depend directly on repositories.

ViewModels may depend on use cases when a domain layer is justified.

Do not require a use case merely to forward a repository call.

Prefer Flow for observable application data and suspend functions for
one-shot operations.

Keep ViewModels independent from Activity, Fragment, Context, and other
lifecycle-bound Android objects unless an API genuinely requires otherwise
and the responsibility cannot be moved to another layer.

## Domain layer

The domain layer is optional.

Introduce a use case when it:

- encapsulates complex business logic;
- combines multiple repositories;
- is reused by multiple ViewModels;
- significantly simplifies a ViewModel; or
- represents a meaningful application operation.

Do not introduce a use case solely because a repository exists.

Keep each use case focused on one responsibility.

Prefer names such as:

- `GetLatestNewsUseCase`
- `FormatDateUseCase`
- `LogOutUserUseCase`

## Data layer

Repositories are responsible for application data and data-related business
logic.

Repositories may:

- expose application data;
- centralize mutations;
- coordinate data sources;
- resolve conflicts between sources;
- implement caching;
- define a source of truth;
- transform data-source models into application models.

Other layers must not access data sources directly.

A repository may contain zero or more explicit data-source classes.

Do not create a data-source abstraction when it adds no useful separation.

## Data sources

A data source should represent one source of data.

Examples include:

- network;
- database;
- DataStore;
- files;
- platform APIs;
- persistent background tasks.

Prefer names based on responsibility and source:

`NewsRemoteDataSource`
`NewsLocalDataSource`
`UserPreferencesDataSource`

Avoid leaking implementation details into higher layers unnecessarily.

## Models

Do not assume that every layer requires its own model.

Create separate models when the representation or responsibility actually
changes between boundaries.

Typical examples include:

Network model -> application/data model -> UI model

but this is not mandatory for every feature.

Never expose a network DTO merely because mapping it would require another
class when the DTO already represents the application's required data and
does not leak inappropriate external concerns.

Conversely, map external models when their structure does not match what the
application needs.

Keep UI-specific models in the UI layer.

## Source of truth

Determine a single source of truth for each important type of application
data.

For offline-first data, prefer a local persistent source such as Room.

A repository may use an in-memory cache as its source of truth when that
matches the required lifetime.

Repositories are responsible for coordinating sources and keeping the source
of truth consistent.

## Threading

Data-layer APIs must be main-safe.

Do not add `withContext(Dispatchers.IO)` mechanically.

First determine whether the underlying API is already main-safe.

Room suspend APIs, coroutine-based network APIs, and other asynchronous APIs
often already provide appropriate execution behavior.

Move blocking or CPU-intensive work to an appropriate dispatcher in the
class that owns that work.

Prefer injecting dispatchers when doing so improves testability.

## Long-running work

Distinguish operation lifetime from implementation convenience.

UI-oriented work may follow the ViewModel/caller lifecycle.

App-oriented work that should continue after leaving a screen should use a
scope whose lifetime matches the data-layer operation.

Work that must survive process death should use an appropriate persistent
mechanism such as WorkManager.

Do not launch arbitrary independent coroutine scopes simply to prevent
cancellation.

## Interfaces

Do not create interfaces automatically for every class.

Use interfaces when they provide meaningful benefits such as:

- multiple implementations;
- implementation replacement;
- external-resource abstraction;
- testing boundaries;
- architectural/module boundaries.

When an interface exists, prefer meaningful implementation names.

Examples:

`OfflineFirstNewsRepository`
`InMemoryNewsRepository`
`DefaultNewsRepository`

Use `Default` when no more descriptive implementation name exists.

## Testing

Architecture should make important boundaries independently testable.

Prefer fakes over mocks where practical.

Unit test:

- ViewModels;
- repositories;
- data sources containing logic;
- use cases containing logic;
- important Flow/state transformations.

Do not add abstractions solely to make mocking easier if a real or fake
dependency can already be used.

## Review workflow

When reviewing architecture, do not immediately rewrite the code.

First identify:

- current responsibility;
- current dependency direction;
- architectural problem, if any;
- relevant official recommendation;
- whether the recommendation is mandatory, strongly recommended,
  recommended, optional, or situational;
- smallest useful change.

Clearly distinguish between:

1. an actual architectural violation;
2. a Google recommendation;
3. an optional design choice;
4. a project-specific preference.

Do not present preferences as Android architecture requirements.

## When uncertain

Consult `references/architecture.md`.

Prefer the official Android guidance contained in the reference over generic
Clean Architecture conventions.

Do not silently substitute Clean Architecture rules for Google's Android
architecture guidance.

If multiple valid designs exist, explain the tradeoff rather than claiming
that one is universally correct.