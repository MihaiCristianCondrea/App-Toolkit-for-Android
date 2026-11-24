# Code review

Trace the normal entry point through graph and DI, screen, event, ViewModel, repository and tests.
Compare a peer with the same operation type, such as Changelog for a one-shot load and Theme or
Developer Options for preference observation. Differences should have a reason grounded in the
feature's behavior.

- Check ownership and package placement. Point out a misplaced file, a redundant use case or
  wrapper, or an existing core API the feature can reuse. Ask for a mapper, reducer or use case
  only when it would own behavior that is currently duplicated or difficult to follow.
- Check concrete ViewModel visibility, exposed types, declaration order and operation names.
  Verify the exhaustive type-based branches in [ViewModel conventions](viewmodel-conventions.md)
  and that private methods carry out substantial work. Look for dropped
  interactions, cancelled saves, overlapping observers, and flows that stop on failure with no
  way to resume.
- Check first load, empty content, usable state after a failure, retry, and the distinction
  between write success and a tap. Check that diagnostics use stable names and bounded context.
- Check eligible Kotlin call sites against the named-argument rules in `traceable-code`.
- Apply `writing-style` to new and inherited documentation. Review public KDoc coverage and
  meaningful internal operations, then check the owning README and Mermaid diagram against the
  implementation. Resolve stale identifiers and links. Use the project's Dokka checks where
  configured.
- Check public API impact and feature string ownership. Use relevant specialist skills if
  the review covers localization, security, navigation or another specialized concern.

Report concrete findings with code locations, user or consumer impact, and the smallest correction.
Separate defects from optional cleanup. State which behavior was verified and what needs a check.
