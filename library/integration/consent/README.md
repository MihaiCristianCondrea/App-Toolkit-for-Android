# `:library:integration:consent` Logic Graph

## Purpose

Coordinates user consent state between persisted toolkit preferences and Google's User Messaging Platform (UMP).

## Owns

- Consent domain models and the repository contract covering apply/request.
- `ConsentRepositoryImpl` and the UMP remote data source abstraction/implementation.
- Mapping host availability into consent behavior.

## Does not own

- Ads loading/presentation, owned by `:library:integration:ads`.
- Diagnostics screen UI, owned by `:library:feature:settings`.
- Preference storage implementation, owned by `:library:core:datastore`.

## Depends on

- [`:library:core:common`](../../core/common/README.md) for Firebase and shared contracts.
- [`:library:core:datastore`](../../core/datastore/README.md) for consent preference persistence.
- [`:library:core:network`](../../core/network/README.md) for shared error/result handling.

## Used by

- `:sample` and `:library:apptoolkit`.
- `:library:feature:about`, `:library:feature:onboarding`, and `:library:feature:settings` for privacy/diagnostics flows.
- `:library:integration:ads` before enabling ads.

## Flow chart

```mermaid
flowchart TD
    UI[Onboarding or settings] --> Repo[ConsentRepository]
    Repo --> Store[Consent preferences]
    Repo --> UMP[UMP remote source]
    Repo --> Host[ConsentHost callbacks]
```

## Public contracts

- `ConsentRepository`, `ConsentSettings`, `ConsentHost`, and `ConsentHostAvailability`.

## Internal implementations

- `ConsentRepositoryImpl` and UMP SDK orchestration.

## Current risks

Consent behavior spans persistence, host callbacks, UMP state, Firebase toggles, and ads consumers; changes require coordinated validation across those boundaries.

## Migration notes

### UMP 4.0.0 empty-error-body process crash

The toolkit previously encountered a process-killing failure after a consent request failed. UMP
4.0.0 performs a metrics request on its own executor and reads a non-success response body with
`Scanner.next()`. An empty body throws `NoSuchElementException` on that executor thread. Because the
throw happens outside the toolkit coroutine/callback path, catches in the remote source or the
repository cannot intercept it.

The following invariants reduce failed/overlapping UMP requests and must be preserved:

- `ConsentRepositoryImpl` permits one consent round trip at a time. A caller joins the replaying
  in-flight state instead of launching another request.
- The in-flight request is keyed by `showIfRequired`; an explicit form request must not join a
  request that only shows a form when required.
- A host that is finishing or destroyed is rejected before UMP is called.
- `UmpConsentRemoteDataSource` checks `ConsentHost.canShowConsentForm` again in the asynchronous
  callback before displaying a form.
- Every starter or joining caller observes `Loading` followed by one terminal state.
- UMP receives an AdMob application ID only when the host-manifest provider resolves a valid value;
  there is no library fallback ID.

[`ConsentSdkCrashGuard`](../../core/common/README.md) narrowly covers any remaining SDK telemetry
failure. Remove that guard only after the exact empty-body read is verified as fixed in the released
UMP artifact, preferably by inspecting the artifact rather than relying only on release notes.
