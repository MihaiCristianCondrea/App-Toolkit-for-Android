# Protocol 04: Integration and independent review

## Integration gates

Review at **meaningful boundaries**, not after every worker's individual command:

1. Verify worker identity and actual model routing against the task ledger.
2. Check the worker's diff and required file ownership; reject out-of-scope edits.
3. Confirm code follows the approved public API, architecture, safety, and UI behavior.
4. Reconcile isolated worktree branches/commits with the chosen target branch. Do not lose independent or untracked work.
5. Run focused validation on the combined changes. Use broader Gradle/build/integration checks at module or milestone boundaries, not for every small worker edit.
6. Verify evidence yourself if actual command exit codes, test reports, expected generated artifacts, and (where material) visual/UI inspection.
7. Mark the work unit `INTEGRATED` only after confirmed landing; unlock dependent tasks.

A passed worker-local test is evidence for that worker's branch, **not** proof the integrated repository passes. Never claim an integration was validated by tests that ran only before merge.

## Independent verification

- Prefer deterministic checks first: compile, unit tests, static analysis, public API compatibility, type checking, preview/screenshot tests where available.
- For a major milestone or complex behavior change, use an independent verification agent with a narrow read-only task and a cheaper verified model if sufficient. Reserve Gemini 4 for difficult cross-cutting review.
- The verifier should receive the frozen spec and integration diff, not the implementer's narrative as the sole truth.
- Ignore vanity verification, duplicate tests without meaningful changes, and redundant post-completion commands.

## Android-specific gates (when relevant)

- Kotlin: compiler errors, explicit nullability contracts, coroutine cancellation/structured concurrency, no unexpected blocking on Main.
- Compose: state hoisting, lifecycle collection, recomposition stability, state restoration, previews, navigation/back stack integration, and UI transitions.
- Koin/DI: correct scopes, presenter lifecycle, module wiring, test doubles.
- Library: binary/source compatibility as required by the plan, published API surface, sample app consumers, dependency graph, tests.
- UI: screenshots or previews for visually meaningful changes, not just successful compilation.
- Dokka/build logic: isolated Gradle plugin compilation, docs generation, local-versus-CI parity.
- Per-module README documentation: Ensure the module README documentation stays up-to-date and accurate.

Do not execute every check for every task. Choose tests according to the actual change surface and record omissions.

## Integration verdict

For each milestone choose:

- `ACCEPT`: evidence meets the contract, changes integrated.
- `REPAIR`: isolated, specific defect; draft a new Flash worker contract.
- `ESCALATE`: architectural contradiction, repeated failure, or ambiguous cross-module regression; strong-model analysis first.
- `BLOCK`: destructive operation, unavailable permission, missing model routing, unresolvable merge conflict, or unavailable essential check; preserve state and surface blocker.

Use the task ledger instead of repeatedly restating lengthy context to every agent.
