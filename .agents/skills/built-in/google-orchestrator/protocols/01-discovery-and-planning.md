# Protocol 01: Discovery and planning

## 1. Resolve the actual objective

Capture in one place:

- User's requested **behavioral outcome** (not a presumed implementation strategy), acceptance criteria, excluded changes, and constraints.
- Current repository status and uncommitted work. Do not overwrite existing user edits.
- Module boundaries and applicable project-specific architecture skills/guidelines. Read relevant official Android skills or other existing repository skill instructions when a task requires them.
- Baseline build/test results and appropriate target tests. Distinguish failing baseline from new regression.
- Project-specific safety concerns (authentication, permissions, user data, publishing, performance).

Ask only questions that truly block safe execution. Otherwise state bounded assumptions in the plan.

## 2. One strong-model architecture pass

Inspect relevant module graphs, entry points, DI, state ownership, lifecycle, and API boundaries. Avoid repository-wide repeated search; gather one compact map that workers can reference. For Kotlin/Compose, pay particular attention to recomposition, state restoration, navigation transitions, coroutine ownership, Koin scopes, preview/test support, and library compatibility.

For an architecture migration, explicitly describe the old and new contracts and how to retain behavior. Do not assume a label such as MVI or MVP uniquely determines the interfaces. The coordinator is responsible for choosing a coherent pattern.

## 3. Produce an executable plan

The plan must contain:

1. Target architecture and invariants; observable user behavior that must remain stable.
2. API decisions and module ownership, including approved package moves.
3. Dependency graph (which work units block others).
4. Reference implementation or vertical slice, and its validation gate.
5. Candidate work units with target paths, complexity, and acceptance checks.
6. Integration order, tests, rollback/stop conditions, and changes requiring user approval.
7. Explicit model-routing expectations and approximate concurrency budget (without hardcoding total workers).

For long-horizon work, use Planning Mode or `/plan` when available and give the user a reviewable artifact. **Do not implement a large migration before the user approves the plan** when approval is part of the workflow. Plan-only prompts must remain plan-only.

## 4. Reference implementation gate

Before parallelizing architectural repetition, choose one representative feature or screen with realistic edge cases. Have one worker implement the approved pattern (or coordinator only if truly architectural), run focused tests, inspect actual behavior, and refine the contract. Then distribute the frozen pattern with examples to all subsequent workers.

No mass migration of an unvalidated pattern. Stop if the reference slice reveals contradictory API requirements.

## 5. Adaptive work decomposition

Determine the work required from the user's objective, repository context, available skills, and evidence.

Do not assume the task is implementation-oriented.

1. Understand the intended outcome and quality requirements.
2. Load all explicitly requested domain skills.
3. Identify what requires investigation, implementation, verification, independent critique, or synthesis.
4. Divide substantial work into independently executable units.
5. Determine dependencies, appropriate worker models, and opportunities for parallel execution.
6. Generate stable internal task IDs using a context-appropriate prefix. IDs are tracking references, not user-facing deliverables.
7. Delegate ready tasks to fresh workers whenever this improves quality, efficiency, or both.
8. Allow completed work to reveal additional investigations or corrections. The total number of workers is adaptive.
