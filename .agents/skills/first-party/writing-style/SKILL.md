---
name: writing-style
description: >
  Enforces strict human developer writing guidelines across code, comments, Markdown
  documentation, social captions, and commit messages. Bans em dashes, en dashes,
  emojis, and generic AI copywriting cliches. Defines where technical documentation belongs.
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

## Technical documentation placement

- Put architectural decisions, behavioral policies, integration requirements and rationale in the
  owning module's README. Keep the explanation there when the code already shows the behavior.
- Use concise KDoc for a declaration's contract, invariant, side effect, lifecycle requirement
  or other non-obvious requirement. Document what a caller needs to know.
- Reserve inline comments for exceptional technical constraints that the code cannot explain
  clearly. Remove comments that restate or narrate the implementation.
- Review inherited comments in the code you touch. Remove redundant text and preserve necessary
  explanations in the appropriate place. Convert a comment to KDoc only if it explains a
  declaration-level contract.

## Checking a file

Search before committing or posting:

```bash
LC_ALL=C.UTF-8 grep -nP "[\x{2013}\x{2014}]|[\x{1F300}-\x{1FAFF}\x{2600}-\x{27BF}]" <files>
grep -niE "elevate|unleash|seamless|next-gen|game-changer|delve|tapestry|in the world of|dive into" <files>
```

Both must print nothing.
