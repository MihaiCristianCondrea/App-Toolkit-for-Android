# Protocol 02: Scheduling and model routing

## Decision tree

1. Is this a single small change with little coordination overhead? **Do directly**, or use one Flash worker if it usefully preserves coordinator quota.
2. Is it difficult mainly because the reasoning is deep (a race condition or algorithm)? Keep strong reasoning with Gemini 4 optionally use specialized reasoning tools if user approves the cost.
3. Is the task broad and separable by files/packages? Use fresh Flash or Pro workers, with a coordinator-owned plan.
4. Is the task both broad and tightly coupled? Solve and validate architectural contracts first then you can parallelize after that.
5. Is the requested worker model not actually available or verifiable? Do not silently substitute the Gemini 4 coordinator. Revise the routing and report the limitation.

## Routing policy

- **Flash**: routine migration following an example, focused composable changes, individual tests, docs, mechanical API adoption, isolated bugfixes with known expected behavior.
- **Pro**: implementation with nontrivial local reasoning, complex state transitions or medium-sized design decisions, but only when an actual `pro` worker choice is supported in this host/account. The `pro` model tier is not the Google AI Pro subscription.
- **Gemini 4**: architectural planning, ambiguous diagnosis, interface disputes, contradictory specs, complex integration failures, or final risk review.
- **No agent**: deterministic formatting, tests, static analysis, straightforward scripted validation when human-approved tools can perform it directly.

Select the cheapest model likely to pass the unit's acceptance criteria on the first or second attempt. Avoid assigning Flash vague goals that require it to reconstruct whole-project architecture.

## Effort settings and model identity

`model: flash` / `model: pro` are documented custom-subagent *tiers*. The frontmatter schema does not specify a High/Medium effort property. Treat requested Flash High as a preference that requires host support and **verification**, not as an automatically satisfied guarantee.

Swarm workers normally inherit the coordinator's selected model. Explicitly request each cheaper worker's model, inspect actual worker model IDs (e.g. in Swarm Members / agent view), and stop/retarget any worker accidentally assigned the strong model. Do not switch a waiting worker's model to claim a fresh context. A switched worker retains its history.

## Work allocation

- Detect actual concurrent-worker limit and current active occupancy. The user's current observed maximum is **four parallel agents**. Treat this as an operating ceiling until the host confirms otherwise, not a permanent hardcoded limit and not a total task cap.
- Account for any verifier and coordinator slots the host counts. Never over-allocate.
- Fill available slots with the highest-priority independent READY units. Prefer one worker per nonoverlapping file set.
- If a worker completes, verify/integrate its work, release the slot, and dispatch the next READY unit. Do not wait for all workers in an arbitrary wave unless a dependency gate requires it.
- Serialise changes to shared build logic, module manifests, central APIs, shared theme/token definitions, lockfiles, generated sources, and project-wide config.
- Avoid simultaneous Gradle builds against the same writable build output. Prefer isolated worktrees and carefully scoped build/test execution, or one integration verifier.
- Only parallelize when expected speed/quality gain exceeds additional coordination and quota overhead.

## Quota hygiene

Before large runs, inspect available usage state when possible and record the baseline; never infer exact pricing from labels. Bound high-cost coordinator work to initial planning, architecture gates, exception handling, and final review. Do not ask the strong model to narrate each file change. Require concise worker reports with links/paths to evidence. Pause rather than retry blindly when rate limits or quota errors occur.

## Scheduler state transitions

`BLOCKED -> READY -> ASSIGNED -> IMPLEMENTED -> VERIFIED -> INTEGRATED -> DONE`, with `FAILED` / `NEEDS_ARCHITECTURE` as explicit exception states. Only integrated work satisfies downstream dependencies. Reassign failed units with a **new** worker and updated brief, not by repeating the same prompt.
