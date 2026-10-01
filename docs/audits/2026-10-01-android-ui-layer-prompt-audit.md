# Prompt audit: `android-ui-layer` skill

Run with `/claude-api prompt-audit` on 2026-10-01, before the skill was restructured.

## Assumptions

- **Scope:** `.agents/skills/architecture/android-ui-layer/SKILL.md` and
  `.agents/skills/architecture/android-ui-layer/references/ui-layer.md`, as the request named them.
  The instruction files the skill points to or that point to it (`AGENTS.md`, `layered-tree-review`,
  `testing-setup`) were read only to check for conflicts.
- **Target model:** Claude Opus 5.5, the model running the audit. The skill pins no model. It sits
  under `AGENTS.md`, so other coding agents may read it too; nothing in it is specific to one agent.
- **Provenance:** both files were written in this session and are not yet committed, so there is no
  `git blame` history. No line is a mitigation for an older model.
- **Provider markers:** none.

## Summary

The skill has no dated prompting patterns: no pressure language, no thinking scaffolds, no fossils.
Every finding is about the skill against its own repository:

1. A rule forbids overriding `onEvent`, which is final.
2. One aside invites a "preview-only" ViewModel, which contradicts the skill's own rule that the
   content previews take no ViewModel.
3. The test advice ("MockK only for platform types") is stricter than the `testing-setup` skill, and
   the reference implementation, `AboutViewModelTest`, does not follow it.

Outside the skill text, the audit also found that the reference implementation's failure preview
cannot render (finding 7).

| Group                                | Findings                          |
|--------------------------------------|-----------------------------------|
| 1. Dated prompt text                 | 3 (rules without their reason, padding) |
| 2. Skill and configuration files     | 4 (stale fact, conflict, volatile) |
| 3. Tool descriptions                 | not applicable                    |
| 4. Request config and architecture   | not applicable                    |

## Findings

### 1. `onEvent` prohibition guards an impossible failure

- **Location:** `SKILL.md:59`
- **Evidence:** "Handle events in `handleEvent` with an exhaustive `when`. Do not override `onEvent`."
- **Pattern:** 1c, a prohibition against a failure that cannot happen; 2, volatile specifics checked
  against the code.
- **Why obsolete:** `ScreenViewModel.onEvent` is final (`core/ui/screen/ScreenViewModel.kt:64`), so
  the compiler already rejects an override. The real context is why the logic belongs in
  `handleEvent`: `onEvent` logs the event first.
- **Confidence:** High.
- **Action:** rewrite: "Handle events in `handleEvent` with an exhaustive `when`; `onEvent` is final
  and reports the event before calling it."
- **Status:** applied in the restructure (`SKILL.md`, ViewModel rules).

### 2. "Preview-only host" contradicts the content rule

- **Location:** `SKILL.md:55-56`
- **Evidence:** "Use the bare `ScreenViewModel` only for something that must not report, such as a
  test double or a preview-only host."
- **Pattern:** 1c padding, an aside that gets applied where it does not fit.
- **Why obsolete:** the same skill says `XScreenContent` takes no ViewModel and that previews render
  `XScreenContent` (`SKILL.md:102-104`, `:110`). The aside points the model at building a ViewModel
  for a preview.
- **Confidence:** Medium.
- **Action:** rewrite: "Use the bare `ScreenViewModel` only where reporting is unwanted, such as a
  test double."
- **Status:** applied.

### 3. Test doubles rule conflicts with `testing-setup` and the reference implementation

- **Location:** `references/ui-layer.md:325`
- **Evidence:** "Use a fake repository object for the happy and failing paths; MockK only for
  platform types."
- **Pattern:** 2, instruction files that contradict each other; 2, volatile specifics.
- **Why obsolete:** `testing-setup/SKILL.md:96-101` says to use a fake first and to mock what cannot
  be faked. The reference implementation mocks a repository
  (`AboutViewModelTest.kt:72`, `SeasonalThemeRepository` as a relaxed mock), so the skill's own
  example breaks the rule.
- **Confidence:** High.
- **Action:** rewrite: "Prefer fakes, and mock only what cannot be faked, as `testing-setup` says."
  This is a Group 2 edit, which is proposed only.
- **Status:** not carried over. The restructured `references/testing.md` defers to `testing-setup`
  for the choice between fakes and mocks instead of restating a rule. Revert that if you want the
  stricter rule back.

### 4. Two state rules lost their reason

- **Location:** `SKILL.md:79`
- **Evidence:** "Use `ImmutableList` and other `kotlinx.collections.immutable` types for collections."
- **Pattern:** 1c, bullets that separate a rule from its reason.
- **Why obsolete:** without the reason (Compose stability, which lets unchanged rows skip
  recomposition), the model cannot tell when the rule matters, such as for a collection that never
  reaches Compose.
- **Confidence:** Medium.
- **Action:** rewrite: "Use `ImmutableList` and the other `kotlinx.collections.immutable` types, so
  Compose treats the state as stable and skips rows that did not change."
- **Status:** applied.

### 5. `UiTextHelper` rule lost its reason

- **Location:** `SKILL.md:80`
- **Evidence:** "Hold user-facing text as `UiTextHelper`, never a resolved `String`."
- **Pattern:** 1c, as above.
- **Why obsolete:** the reason (text resolves in the UI against the current locale and
  configuration) is what tells the model that a `String` is right when the text is user input or
  already resolved, as in `AboutEvent.CopyToClipboard`.
- **Confidence:** Medium.
- **Action:** rewrite: "Hold text the app writes as `UiTextHelper`, so it resolves in the UI against
  the current locale and configuration. Text the user typed stays a `String`."
- **Status:** applied.

### 6. The reference implementation contradicts the `MessageHost` guidance

- **Location:** `references/ui-layer.md:222-223`, against `AboutScreen.kt`
- **Evidence:** "Pass a `snackbarHostState` only when the screen draws its own host." `AboutScreen`
  passed `rememberPageSnackbarHostState()`, which is the default.
- **Pattern:** 2, volatile specifics: the skill names About as the example to copy.
- **Confidence:** Medium.
- **Action:** fix the code, not the skill.
- **Status:** applied to `AboutScreen.kt`, which now calls `MessageHost(viewModel = viewModel)`.

### 7. The reference implementation's failure preview cannot render

- **Location:** `AboutScreenContent.kt`, `AboutScreenContentFailedPreview`
- **Evidence:** the preview rendered `Loadable.Failed`, whose default screen is `NoDataScreen`.
  `NoDataScreen` calls `koinInject<AdsConfig>` unconditionally (`core/ui/views/layouts/NoDataScreen.kt:85`),
  and a preview has no Koin.
- **Pattern:** 2, volatile specifics; the skill told the model to preview every meaningful state
  (`SKILL.md:110`).
- **Confidence:** High.
- **Action:** remove the preview, and add to the skill that the empty and failure states are not
  previewable while `NoDataScreen` injects its ad unit.
- **Status:** applied (`AboutScreenContent.kt`, `references/screen-and-content.md`).

### 8. Parameter order restates the Compose API guidelines (flag)

- **Location:** `references/ui-layer.md:263`
- **Evidence:** "Parameter order: required state and callbacks first, then `modifier`, then optional
  parameters with defaults."
- **Pattern:** 2, explaining what the model already knows.
- **Confidence:** Low. It is one line and agrees with the guidelines.
- **Action:** flag. Kept in the restructure.

### 9. "There is no `Action` type" and "no shared base state" are migration-relative (flag)

- **Location:** `SKILL.md:69-70`, `:84`
- **Pattern:** 1d, migration-relative phrasing.
- **Confidence:** Low. While `core.ui.base` and `core.ui.states` still exist, the lines are context:
  the model sees `ActionEvent` and `UiState` in features that are not migrated yet.
- **Action:** flag. Remove both lines when the old types are deleted.

## Proposed diff (against the files as audited)

The restructure replaced `references/ui-layer.md` with topical files, so these hunks record the
proposals. They are not patches to apply.

```diff
--- a/.agents/skills/architecture/android-ui-layer/SKILL.md
+++ b/.agents/skills/architecture/android-ui-layer/SKILL.md
@@ finding 2
-- Extend `LoggedScreenViewModel` for every feature screen. Use the bare `ScreenViewModel` only for
-  something that must not report, such as a test double or a preview-only host.
+- Extend `LoggedScreenViewModel` for every feature screen. Use the bare `ScreenViewModel` only where
+  reporting is unwanted, such as a test double.
@@ finding 1
-- Handle events in `handleEvent` with an exhaustive `when`. Do not override `onEvent`.
+- Handle events in `handleEvent` with an exhaustive `when`; `onEvent` is final and reports the event
+  before calling it.
@@ finding 4
-- Use `ImmutableList` and other `kotlinx.collections.immutable` types for collections.
+- Use `ImmutableList` and the other `kotlinx.collections.immutable` types, so Compose treats the
+  state as stable and skips rows that did not change.
@@ finding 5
-- Hold user-facing text as `UiTextHelper`, never a resolved `String`.
+- Hold text the app writes as `UiTextHelper`, so it resolves in the UI against the current locale
+  and configuration. Text the user typed stays a `String`.
--- a/.agents/skills/architecture/android-ui-layer/references/ui-layer.md
+++ b/.agents/skills/architecture/android-ui-layer/references/ui-layer.md
@@ finding 3 (Group 2: proposed only)
-- Use a fake repository object for the happy and failing paths; MockK only for platform types.
+- Prefer fakes, and mock only what cannot be faked, as `testing-setup` says.
```

## Verify

- Findings 1, 6 and 7 are checked against the code: re-read `ScreenViewModel.kt:64`,
  `AboutScreen.kt` and `NoDataScreen.kt:85`.
- Finding 7 needs a preview render in Android Studio to confirm that the remaining Ready preview
  draws.
- Re-run this audit when `core.ui.base` and `core.ui.states` are deleted (finding 9), and after
  the next model release.
