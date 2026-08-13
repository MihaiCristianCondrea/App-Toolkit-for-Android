---
name: android-project-tree
description: Review Android/Kotlin project tree and file placement. Use when deciding where screens, ViewModels, repositories, data sources, models, mappers, use cases, navigation, or state files should live. This skill checks structure only; use the dedicated data/domain architecture skills for behavior and architectural semantics.
---

# Android Project Tree

Review file and package placement without redesigning architecture.

Read:

```text
references/android-tree-rules.md
```

Use the sibling architecture skills when behavior matters:

- `android-data-layer` for repositories, data sources, models, source of truth, threading, interfaces, caching, and data behavior.
- `android-domain-layer` for optional domain/use-case decisions.

Do not duplicate or override those rules here.

Prefer small mechanical moves, renames, and package fixes.

Report only meaningful placement problems. If ownership depends on architectural behavior rather than tree structure, defer to the appropriate architecture skill.
