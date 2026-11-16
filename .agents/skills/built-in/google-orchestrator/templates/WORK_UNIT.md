# Work unit: <ID> - <short objective>

- **Status**: READY | BLOCKED | ASSIGNED | IMPLEMENTED | VERIFIED | INTEGRATED | DONE
- **Assigned model**: flash | pro (if available) | Gemini 4 by exception
- **Depends on integrated units**: <IDs or none>
- **Workspace / branch**: <path / branch / worktree>
- **Exclusive write ownership**: <exact files and paths>
- **Read-only references**: <paths, stable architecture document, reference implementation>
- **Forbidden edits**: <shared/core modules and other workers' paths>

## Objective

<One measurable behavior change.>

## Implementation recipe

1. <Current code entry point and exact target API/signature.>
2. <Changes to make, ordered.>
3. <State/behavior requirements and edge cases.>
4. <Test case or known working example to follow.>

## Constraints

- Follow repository's architecture and code style.
- Do not make unrelated changes or invent new public APIs.
- Report unexpected architecture conflicts rather than silently altering the plan.
- Follow the writing rules of the project then the architecture, localization, changelog, analytics, built-in and official skills.

## Acceptance criteria

- [ ] <Observable outcome>
- [ ] <Compatibility / behavior invariant>
- [ ] <Targeted test passes>
- [ ] Changes are confined to assigned paths

## Validation commands

```text
<exact focused command, with working directory>
```

## Stop / escalation conditions

Stop and report if <blocker>. Do not repeatedly retry an identical failed command without changing inputs.

## Required report

Return `WORKER_REPORT.md` format with real evidence, changed files, unresolved issues, integration location. Stop immediately after reporting completion.
