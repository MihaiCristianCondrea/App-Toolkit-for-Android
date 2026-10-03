---
name: android-domain-layer
description: >
  Design, implement, review, refactor, and decide when to use the optional
  Android domain layer and use cases following Google's Android architecture
  guidance. Use when working with use cases, interactors, reusable business
  logic, ViewModel complexity, repository coordination, domain-layer
  dependencies, use-case naming, threading, or deciding whether a use case
  should exist at all.
metadata:
  author: Google LLC
  last-updated: '2026-08-13'
  keywords:
  - architecture
  - domain layer
  - use case
  - business logic
  - interactor
  - viewModel complexity
---

# Android Domain Layer

Use Google's Android definition of the optional domain layer.

The domain layer sits between the UI layer and data layer when it provides
meaningful value.

Read `references/domain-layer.md` when detailed guidance about use cases,
dependencies, threading, lifecycle, data-layer access, or testing is needed.

Do not substitute generic Clean Architecture rules for Google's Android
domain-layer guidance.

## The domain layer is optional

Do not create a domain layer automatically.

Introduce domain-layer use cases when they solve an actual problem.

Typical reasons include:

- complex business logic;
- business logic reused by multiple ViewModels;
- coordination between multiple repositories;
- reducing meaningful complexity in a ViewModel;
- reusable application operations needed by multiple consumers.

A ViewModel may access a repository directly when a use case would provide
little or no additional value.

Both are valid:

UI -> ViewModel -> Repository

and:

UI -> ViewModel -> UseCase -> Repository

Choose according to the application's requirements.

## Do not confuse this with Clean Architecture

Google's Android domain layer is not necessarily equivalent to the domain
layer described by Clean Architecture, DDD, or other architectural systems.

Do not introduce rules from those architectures unless the project explicitly
uses them.

In particular, do not assume that Google's Android architecture requires:

- every repository interface to live in domain;
- every repository call to go through a use case;
- domain entities for every data model;
- strict UI -> domain -> data access for every operation;
- a domain module in every application.

Follow the architecture actually used by the project.

## Decide whether a use case is needed

Before creating a use case, ask:

1. Does it contain meaningful business logic?
2. Is the logic reused by multiple ViewModels or consumers?
3. Does it coordinate multiple repositories?
4. Does extracting it substantially simplify the caller?
5. Does it represent a meaningful reusable application operation?

If the answer to all of these is no, a use case might not provide enough value.

Do not introduce a use case merely because a repository function exists.

## Avoid pass-through use cases

Be skeptical of use cases whose only responsibility is forwarding one call.

For example:

```kotlin
class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): User =
        userRepository.getUser()
}
```

adds a class, a binding and a test without adding behavior; the ViewModel can call the repository.
Keep such a use case only when the project's conventions require every call to go through one.

For naming, dependencies, threading, lifecycle, combining repositories, and testing use cases, read
`references/domain-layer.md`.

## Errors

A use case lets the repository's exceptions pass through, and throws its own only when a business
rule is what failed. Mapping a failure to text is not a reason to add a use case: the UI layer does
that, as the `android-ui-layer` skill describes in `references/errors.md`.
