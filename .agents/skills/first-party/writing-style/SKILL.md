---
name: writing-style
description: >
  Enforces strict human developer writing guidelines across code, comments, Markdown
  documentation, social captions, and commit messages. Bans em dashes, en dashes,
  emojis, and generic AI copywriting cliches. Defines Kotlin API documentation and module README standards.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-21'
  keywords:
  - writing-style
  - punctuation
  - tone-of-voice
  - no-em-dashes
  - no-emojis
  - human-copy
  - documentation
  - kdoc
  - dokka
  - mermaid
  - code comments
---

# Human Writing Style and Copywriting Guidelines

A core workspace writing standard for all code comments, Markdown documentation, campaign copy, social captions, commit messages, and PR descriptions.

This skill ensures all generated text reads naturally, professionally, and cleanly, eliminating AI-like punctuation and buzzword cliches.

This is the only place these rules are written. Every other marketing skill applies them by linking here; when a rule changes, change it here.

---

## Core Writing Rules

1. **No Em Dashes (`—`) or En Dashes (`–`)**:
   - Never use `—` or `–` as pauses, separators, or list bullet breaks. They are distinct AI-generated artifacts.
   - Use commas, colons, hyphens (`-`), or parentheses as a human developer would write.

2. **No Emojis**:
   - Never include emojis in Markdown headers, documentation, code comments, commit messages, or social captions unless the user explicitly requests them.

3. **Banned AI Copywriting Cliches**:
   - Do not use generic, hyperbolic AI buzzwords: "elevate", "unleash", "seamless", "next-gen", "game-changer", "delve", "tapestry", "in the world of", "dive into".
   - Use plain, active, and specific verbs describing concrete functionality.

4. **Direct, Concise Human Tone**:
   - Keep sentences short, confident, and direct.
   - Write clear technical documentation that explains public contracts, side effects, and architectural assumptions without stating the obvious.

5. **Plain punctuation**:
   - Use straight quotes and apostrophes (`'`, `"`) in files; let the platform typeset them.
   - Use the middle dot (`·`) or a colon to separate a number from a title, never a dash.

## Technical documentation ownership

| Documentation | Responsibility |
| --- | --- |
| KDoc | Declaration purpose, usage requirements, parameters, results, side effects and invariants |
| Module README | Module architecture, dependencies, integration, behavioral policies and rationale |
| Mermaid | Relationships, ownership and important control or data flows |
| Inline comments | Exceptional technical constraints that need a local explanation |
| Tests | Observable guarantees and regression coverage |

### Kotlin declarations

Document public types, including objects, sealed interfaces, UI state and events, and public
functions, including composables and extensions. Document public properties and constants when
their meaning, value or use forms part of the contract. Preserve or improve useful public KDoc
during refactoring, including when the module README discusses related behavior. Rewrite verbose
or informal KDoc into a direct description of what callers can rely on.

Prefer short KDoc for distinct internal and private operations when purpose, effects or boundaries
need explanation. Trivial overrides, generated declarations, simple accessors and self-explanatory
implementation details need no added prose. Put a shared contract on its owning interface. Document
an implementation separately when it adds a meaningful guarantee or requirement.

Start with a concise summary. Use resolvable declaration links and tags where they add information:
`@param` or `@property` for inputs and exposed state, `@return` for result semantics,
`@throws` for failures callers should handle, `@see` for related APIs and `@sample` for an
existing sample in a Dokka source set. Avoid restating names or signatures.

Review inherited KDoc and comments in changed code. Remove stray inline commentary and narration.
Preserve explanations of exceptional constraints. Use KDoc for declaration contracts and the
module README for architectural rationale. Add a local comment only where the constraint needs it.

### Module README

Inspect the module's current README and nearby modules first. Preserve the established logic-graph
structure. Cover the applicable sections: Purpose, Owns, Does not own, Host requirements, Depends on,
Used by, Flow chart, Architectural decisions, Public contracts, Internal implementations and Current
risks. Add relevant sections when needed and omit sections with no useful content.

Treat Mermaid as part of the technical documentation. Review the diagram whenever ownership,
dependencies, class names, navigation keys or operation flows change. Use the actual entry points,
screens, state and event owners, repositories, sources, use cases and integrations that participate.
Show the relationships that explain the flow and verify identifiers against the implementation.

Check Markdown links, paths and KDoc references in the affected module. Run the project's Dokka
generation and warning checks when available. Review coverage directly when undocumented
declarations are excluded from warnings.

## Checking a file

Search before committing or posting:

```bash
LC_ALL=C.UTF-8 grep -nP "[\x{2013}\x{2014}]|[\x{1F300}-\x{1FAFF}\x{2600}-\x{27BF}]" <files>
grep -niE "elevate|unleash|seamless|next-gen|game-changer|delve|tapestry|in the world of|dive into" <files>
```

Both must print nothing.
