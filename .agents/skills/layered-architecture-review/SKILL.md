---
name: android-layered-architecture-review
description: Review Android or Kotlin Multiplatform projects for UI, domain, and data layer consistency. Use when checking feature structure, layer boundaries, model ownership, mappers, use cases, repositories, TODO/FIXME comments, Compose UI flows, performance bottlenecks, or platform-specific separation.
---

# Android Layered Architecture Review

Use this skill when reviewing Android code that follows a UI -> Domain -> Data architecture.

The goal is not to redesign features.

The goal is to keep the project consistent, predictable, testable, and safe to maintain over time.

Prefer small, mechanical, behavior-preserving improvements.

Do not rewrite entire features unless the user explicitly asks for that.

## Reference rules

Before making architectural decisions, read:

```text
references/android-layer-rules.md