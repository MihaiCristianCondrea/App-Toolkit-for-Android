# Protocol 03: Delegation and worker contracts

## Contract quality is the main optimization

Every dispatched task must be specific enough that a clean-context worker can finish without reconstructing the entire plan. Use `templates/WORK_UNIT.md`. Each contract includes:

- Stable task ID and narrow outcome.
- Relevant architecture decisions and existing tested example (link/file, not the entire conversation).
- Exact owned files/directories, read-only dependencies, and **do-not-touch** paths.
- API signatures and invariants; expected behavior and edge cases.
- Allowed commands and **targeted** validation command(s).
- Dependencies already integrated, definition of done, output/report schema, and when to stop/escalate.

Prefer cohesive units of **1–5 changed files**, but never split a necessary atomic change merely to hit a number. If paths are unknown, have a limited read-only exploration first and then freeze scope before allowing edits.

## Spawn strategy

- Use a new `flash-worker` custom subagent (`model: flash`) for each unrelated work unit. For complex work, select `pro-worker` only if host supports its tier.
- In `/swarm`, explicitly name workers, choose lower-cost models, state the worker-specific assignments, and use the shared board. Check the actual model identity before expensive work; workers otherwise inherit coordinator model.
- Prefer an isolated Git worktree when workers would edit concurrently or when a change has integration risk. If the environment cannot isolate work, schedule nonoverlapping edits with strict file ownership.
- Do not invoke an undocumented tool API. Rely on host-exposed `invoke_subagent`, Swarm tools, or supported UI commands. A slash command typed by the user is not necessarily callable as a nested agent function.
- A fresh worker has a fresh conversation; resuming or messaging an idle worker retains its previous context. Never claim an idle worker was reset.
- The parent should not pass secrets, broad credentials, or unrelated private project materials to workers.

## Worker execution rules

1. Read the work contract and only relevant source paths. Follow existing project-specific skills and architecture rules.
2. Confirm target files are the intended ones and baseline workspace is safe.
3. Make the smallest change satisfying the contract and avoid unrelated refactoring.
4. Run only focused, feasible tests. Do not repeatedly invoke expensive builds with unchanged inputs.
5. Stop when acceptance criteria are met. Do not continue inspecting unrelated files or adding unrequested features.
6. If blocked, contradicting the approved plan, lacking permission, or needing a cross-module API change, stop and report the exact blocker, with evidence and proposed options.
7. Return `templates/WORKER_REPORT.md` filled with changed paths, evidence, limits, and integration location.

## Worker correction policy

A failed test is not a reason to improvise architecture. The coordinator interprets the failure, creates a corrected bounded task, and invokes a **fresh** worker for it. If two different approaches fail on the same root issue, escalate to Gemini 4 or Pro, and update the architecture contract before continuing.

## Finalization of isolated work

Before killing/removing workers or cleaning isolated worktrees, ensure changes are safely landed/integrated, including untracked required files. Do not merely record 'done' based on a board message. Preserve useful diffs and test outputs.
