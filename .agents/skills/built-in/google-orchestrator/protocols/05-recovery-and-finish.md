# Protocol 05: Recovery, quota management, and completion

## Failure taxonomy

- **Local implementation bug**: one/two files, clear expected behavior → fresh Flash repair task.
- **Bad contract**: worker followed directions but result is wrong → revise spec but don't punish worker with repeated retries.
- **Architectural contradiction**: API/lifecycle/module conflicts → coordinator reasoning and possibly a new reference slice.
- **Host/model routing defect**: requested Flash but inherited Gemini 4, wrong effort, unavailable tools → stop affected execution, inspect actual host capabilities, retry only with a verified route.
- **Runtime quota exhaustion**: preserve state, do not automatically restart paused agents, and avoid consuming the coordinator budget on unbounded recovery.
- **Integration conflict**: pause colliding tasks, reconcile ownership; never discard changes without confirmation.
- **Unverifiable build/test**: report missing environmental prerequisites, don't pretend the result passes.

## Two-attempt rule

For a given root cause, permit at most two genuinely different bounded implementation attempts before escalation. A second attempt must have an updated contract grounded in observed evidence. Avoid indefinite retry and tool-call loops. This is a **behavioral policy**, so please respect any stricter host controls.

## Preserve a checkpoint

At every meaningful milestone record:

- Completed and integrated task IDs and relevant commit identifiers or file paths.
- Accepted architecture decisions and important changed APIs.
- Last verified commands/results and known baseline failures.
- Pending READY/BLOCKED work units, their dependencies and file owners.
- Model routing confirmed or not confirmed; usage statistics only if actually available.

Prefer a concise run-state file (`templates/RUN_STATE.md`) or native plan artifacts. Please never silently commit execution logs with confidential content.

## Termination policy

Before ending workers, ensure isolated changes have been merged or otherwise preserved. A killed subagent cannot be revived, and temporary worktrees may be deleted. Reuse a worker only for clarification on the **same** task when it is cheaper than spawning a new one. Use new sessions for new work units.

## Completion criteria

Stop when:

1. All approved requirements are covered by integrated work.
2. Tests/checks required by acceptance criteria pass, or clearly documented environmental blockers prevent them.
3. No outstanding architecture violations or unintegrated critical worker changes remain.
4. Required review and user approvals have been respected.

Do not add unsolicited rounds of polish or tests after criteria pass. If optional improvement is identified, report it as a suggestion, not another automatic task.

## Final report (short)

Use this structure:

- **Outcome**: delivered vs incomplete items.
- **Architecture**: key decisions and any deviations.
- **Workers**: roles and verified models and any model/effort uncertainty.
- **Validation**: precise commands and results, including what was not run.
- **Integration**: commits/paths and remaining issues.
- **Usage**: measured steps/quota deltas if available, otherwise 'not measured'.

Do not claim a quota saving without a comparable baseline run.
