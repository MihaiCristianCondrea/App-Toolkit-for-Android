---
name: play-store-changelog
description: Convert raw Android changelogs, Git commits, PR summaries, and release notes into concise, accurate, user-friendly Google Play Store What's new text. Use when asked to write, shorten, format, translate, or validate Play Console release notes, including multi-language locale blocks, 500-character limits, and Fastlane metadata changelogs.
compatibility: Python 3.9+ is optional for the bundled validator; writing instructions work without Python.
metadata:
  version: "1.0.0"
  category: android-publishing
---

# Play Store Changelog

Turn engineering changes into release notes that tell users what actually changed. Prioritize clarity, factual accuracy, and fit over filling the character budget.

## Non-negotiable requirements

1. **Google Play Console allows up to 500 Unicode characters per language.** This skill deliberately sets a safer delivery limit of **at most 499 Unicode code points in each note body**. Never exceed it. Count spaces, punctuation, symbols, and internal line breaks. Do **not** count the opening or closing locale tag as part of its note body. Treat 500 as valid by Google but outside this skill's stricter default.
2. **Aim for 250–450 characters when the source warrants it.** This is an editorial target, not a lower bound. A short honest note is better than invented filler. Short maintenance releases may need only 40–150 characters.
3. **No invented claims.** Never infer faster startup, lower battery use, fewer crashes, offline access, enhanced security, or device support solely from implementation details. Use measurable claims only when explicitly supported by provided evidence. If all you know is a refactor, either omit it or describe a defensible, narrow user-facing effect.
4. **No advertising or calls to action.** Google says release notes should inform users about the update, not promote products or solicit actions. No review requests, referral codes, marketing slogans, or store links.
5. **Preserve semantics.** Distinguish added, changed, improved, and fixed. Do not call a planned, internal-only, experimental, or disabled feature publicly available without confirmation.
6. **Honor requested languages and output destination.** For pasted Play Console releases use locale tags only when requested or useful for multiple locales. For Fastlane, write **body text without locale tags** to locale-specific changelog files. Never force an English header on other locales.
7. **Validate the final text** after the last edit, independently for every language. Run `scripts/validate_release_notes.py` when a local runtime exists. Otherwise count carefully and disclose that automated validation was unavailable.

## Workflow

### 1. Understand source and target

- Accept plain notes, Markdown, commit logs, git diffs, PR titles, tickets, version comparison, or release descriptions.
- Determine whether the output is for Play Console, Fastlane, Google Play Developer API, or a plain text answer. If unspecified, give concise **plain text** notes that can be pasted into Play Console; use locale tags if multiple locales are requested.
- Read only the commits/notes in the release's scope. Exclude unreleased, reverted, draft, developer-only, experimental, and duplicate changes unless the user specifically wants test-track notes.
- For a code repository, investigate the relevant release range when accessible; do not pretend uninspected commits were verified.
- If release content is ambiguous, prefer conservative language and flag significant ambiguity rather than fabricating an explanation.

### 2. Build an internal facts list

For each meaningful change, classify:
- **Added:** new user-visible feature or tool.
- **Changed:** layout, navigation, existing behavior, policy, or removed feature.
- **Improved:** supported performance, usability, accessibility, or reliability improvement.
- **Fixed:** a concrete defect resolved.
- **Internal:** refactors, DI, ViewModels, build files, logging, test infrastructure, baseline profiles, dependency updates. Include only when a clear, supported user benefit is warranted.

Sort by significance: important new features; important changes/removals; visible improvements; high-impact fixes; minor fixes. Combine repeats and avoid listing invisible work just because it took many commits.

### 3. Write for users

- Lead with the most valuable visible change. Use simple, specific phrases, ideally one idea per bullet.
- Suggested headings only **when useful**: `Added`, `Changed`, `Improved`, `Fixed`, or `Improved & Fixed`. A short patch can use one or two bullets without headings.
- Use `•` for bullets, actual newlines, and **plain text**, not Markdown heading syntax or fenced code in the note body.
- Optional opening: `What's new:` / `Noutăți:`. Skip it if it wastes useful space. Emojis are optional, preferably omitted for production notes unless requested.
- Do not use vague statements (`various improvements`) when specific fixes are available. Avoid version names, release dates, internal issue IDs, or changelog category headings that add no useful information.
- Translate engineering language into effects **only if grounded**. See [rewriting rules](references/editorial-guidelines.md).

### 4. Compress intelligently

Cut in this order:
1. Redundant intro/version label, repeated headers, and filler adjectives.
2. Low-impact internal maintenance.
3. Duplicated or closely related fixes, merged into one precise bullet.
4. Overly technical implementation details.
5. Secondary low-priority changes, while preserving the most important facts.

Never compress by altering the meaning, deleting a critical warning, or implying unavailable functionality. Avoid aggressively cryptic abbreviations. More than one honest, readable note length can be correct.

### 5. Localize, don't just translate

- Preserve product names and established feature labels. Translate common terms, headings, and natural phrasing for each locale.
- Recount **each localized body**; translations have different lengths. Romanian diacritics count as Unicode characters too.
- Use the exact language tag presented by the target Play Console or specified by the user. Do not silently change `ro` to `ro-RO` or vice versa.
- Read [localization and delivery](references/localization-and-delivery.md) for locale tags, Fastlane, and API output.

### 6. Final quality gate

Check every item:

- [ ] True for this exact release and publicly relevant to the intended track.
- [ ] No unsupported quantitative or qualitative claim.
- [ ] Clear, natural wording without developer-only jargon.
- [ ] No promotional solicitation, link, or misleading promise.
- [ ] Valid locale wrappers for Play Console, or no wrappers for Fastlane.
- [ ] **Every locale body is 1–499 Unicode code points**, counting internal newlines.
- [ ] Headings and line breaks are counted as part of the body.
- [ ] The final delivered bytes are the ones validated.

Example validation:

```bash
python scripts/validate_release_notes.py examples/play-console.txt --mode console --max-chars 499
python scripts/validate_release_notes.py examples/fastlane/en-US/100100.txt --mode plain --max-chars 499
```

A nonzero exit code means a violation. Validator messages about the 250–450 editorial target are warnings, not errors.

## Output contract

- Default to **only the release-ready notes**, preserving requested tags/format. If helpful, append a brief line outside the copyable notes with `en-US: N characters` and analogous counts for other locales.
- For a single locale without an explicit tag request, show the note body alone.
- For multiple Play Console locales, format each block like:

```text
<en-US>
Added
• Search Quick Tools and Apps from the top bar.
</en-US>
<ro>
Adăugate
• Caută în Instrumente rapide și Aplicații din bara de sus.
</ro>
```

- If asked for machine-consumable metadata, follow the requested format. Google Play API accepts `releaseNotes` items with `language` and `text`, not Console angle-bracket wrappers.
- If editing release-note files was requested, write the files in the repository's established locale paths and validate them. Never publish/release automatically unless explicitly asked and authorized.

## Reference directory

- [Google Play limits, allowed content, and authoritative links](references/google-play-policy.md)
- [Editorial guidelines, priority, and technical-term translations](references/editorial-guidelines.md)
- [Locales, console tags, Fastlane files, API, and newline rules](references/localization-and-delivery.md)
- [Worked examples: feature release, patch, multilingual release, pitfalls](references/examples.md)
- [Ready-to-fill templates](assets/templates.md)
- [Sample original source](examples/raw-changelog.md)
- [Validated English/Romanian Play Console output](examples/play-console.txt)
- [CLI validator](scripts/validate_release_notes.py)
- [Validator unit tests](tests/test_validator.py)
