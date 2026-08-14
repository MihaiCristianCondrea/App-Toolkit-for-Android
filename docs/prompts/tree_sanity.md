# Architecture Sanitization

I want to review and sanitize the current project architecture and structure.

Use the project architecture skills:

[`architecture`](../../.agents/skills/architecture)

Treat the skills, `AGENTS.md`, relevant project documentation, and existing module READMEs as the
guidance for this review.

## Goal

Review the project as it currently exists and improve its architecture where meaningful.

Inspect the project broadly before making changes, including:

* Gradle modules and dependencies
* source and package structure
* feature/module boundaries
* architecture layers
* dependency injection
* navigation
* shared/core code
* public contracts
* tests and documentation

Use the architecture skills to identify code or structure that:

* is in the wrong layer, feature, package, or module
* violates established project boundaries
* creates unnecessary coupling or dependencies
* duplicates responsibilities
* contains obsolete architectural leftovers
* uses unnecessary abstractions
* has become inconsistent with the rest of the project

## Approach

Do not redesign the project from scratch.

Preserve architecture that is already reasonable, even when another valid approach exists.

Prefer small, meaningful improvements over restructuring for visual consistency or theoretical
purity.

Do not force every feature into an identical structure. The structure should follow the
responsibilities and complexity of the feature.

Before moving, removing, or restructuring something, inspect its actual usages and understand why it
currently exists.

Preserve behavior, compatibility, user data, public contracts, and existing functionality unless a
change is explicitly necessary.

## Documentation

Keep documentation synchronized when the architecture meaningfully changes.

If a module has a local `README.md`, update it when its responsibilities, dependencies, consumers,
public contracts, important flows, or architectural risks change.

Do not update documentation for trivial implementation changes.

## Validation

After changes, validate the affected modules and relevant tests.

Do not claim something was validated unless it was actually run.

## Result

The project should end up easier to understand and maintain, with clearer ownership and boundaries
and less unnecessary structural complexity.

At the end, summarize:

* what architectural problems were found
* what was changed and why
* what was intentionally left unchanged
* any remaining concerns worth reviewing later
* validation performed
