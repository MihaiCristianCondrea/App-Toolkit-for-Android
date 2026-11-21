---
name: traceable-code
description: Write, refactor, and review application code in a direct, easy-to-follow style. Use for Kotlin/Android and other programming tasks when choosing control flow, simplifying code, or improving readability. Prefer visible cause-and-effect, idiomatic language constructs, and minimal mental indirection over clever brevity. Use this skill for Kotlin named arguments and call-site readability.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-21'
  keywords:
    - kotlin
    - named arguments
    - readability
    - control flow
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
7. **Use explicit Kotlin argument names.** Name ordinary arguments even when a call takes one argument. This applies to constructors, Compose calls, state updates, events, and DI bindings. Keep trailing lambdas for DSLs and block-style APIs. Keep positional syntax for Java APIs, function-type calls, operators, and varargs where adding a name would require repacking arguments. Do not add wrappers or change overloads to obtain named syntax.
8. **Preserve semantics.** Keep return values, first-match behavior, side effects, iteration order, performance-sensitive behavior, error handling, and tests intact. In Kotlin, be aware of non-local returns from inline lambdas.
9. **Make learning possible.** On request, explain code as a short execution trace using concrete values. Do not turn every coding answer into a tutorial.

```kotlin
onEvent(event = CatalogEvent.Load)
Text(text = title)
single { CatalogRepository(client = get()) }
```

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
- Do eligible Kotlin arguments use their declared names?
- Did behavior and tests remain unchanged?
- Apply `writing-style` when reviewing Kotlin documentation and comments.
