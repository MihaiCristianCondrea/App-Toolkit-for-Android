---
name: traceable-code
description: Write, refactor, and review application code in a direct, easy-to-follow style. Use for Kotlin/Android and other programming tasks when choosing control flow, simplifying code, or improving readability. Prefer visible cause-and-effect, idiomatic language constructs, and minimal mental indirection over clever brevity.
---

# Traceable Code

## Goal

Write code a developer can understand by reading from top to bottom and following what happens to each value. Optimize for **low mental effort**, not minimum lines of code. Keep the implementation idiomatic, concise, and faithful to the project's conventions.

## Rules

1. **Keep condition and consequence together.** When a matching item is found, perform or return the result nearby instead of computing an intermediate code, sentinel, or index that must be decoded later.
2. **Prefer visible execution flow.** Express the sequence of decisions and actions directly. Use early returns when they make the outcome clear.
3. **Use the language naturally.** In Kotlin, `forEach`, `forEachIndexed`, `if`, `when`, and other standard idioms are welcome. Do not mechanically replace them with traditional `for` loops. Do not mechanically replace every `indexOfFirst`, `map`, or `filter` either. Choose based on which intent is clearest.
4. **Avoid unnecessary intermediate state.** Do not create a temporary value solely to inspect a sentinel or repackage it if a direct conditional action is clearer. Keep intermediate variables when they name an important concept or prevent repeated work.
5. **Prefer recognizable patterns.** Avoid clever one-liners, deeply chained transformations, unnecessary helpers, and abstractions that force the reader to jump around. Do not expand simple idiomatic code into verbose boilerplate.
6. **Respect local conventions.** Follow existing identifiers, architecture, nullability, and formatting. Use descriptive names when introducing new variables. Preserve public interfaces unless explicitly changing them.
7. **Comments explain why.** Avoid narrating what straightforward code already says. Explain non-obvious behavior, invariants, and constraints.
8. **Preserve semantics.** Keep return values, first-match behavior, side effects, iteration order, performance-sensitive behavior, error handling, and tests intact. In Kotlin, be aware of non-local returns from inline lambdas.
9. **Make learning possible.** On request, explain code as a short execution trace using concrete values. Do not turn every coding answer into a tutorial.

## Example: Kotlin nested menu search

Less direct for a reader who prefers immediate cause-and-effect:

```kotlin
val subIndex = item.subItems.indexOfFirst { it.url == url }
if (subIndex >= 0) {
    return MenuSelection(itemIndex = itemIndex, subItemIndex = subIndex)
}
```

Prefer when searching a match to return its location:

```kotlin
item.subItems.forEachIndexed { subIndex, subItem ->
    if (subItem.url == url) {
        return MenuSelection(itemIndex = itemIndex, subItemIndex = subIndex)
    }
}
```

This is a **preference, not a blanket rewrite rule**. `indexOfFirst` is appropriate when the index is itself the desired result or when it meaningfully improves the surrounding code. `filter` and `map` are appropriate for genuine transformations.

## Review checklist

- Can the reader follow decisions and effects in one pass?
- Is each condition close to its outcome?
- Are there avoidable sentinel checks or throwaway variables?
- Did the change add complexity, verbosity, or abstraction without improving understanding?
- Is it idiomatic for the language and consistent with nearby code?
- Did behavior and tests remain unchanged?
