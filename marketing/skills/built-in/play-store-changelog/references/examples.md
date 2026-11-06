# Worked examples

## 1. A large Android utility update

The source release includes search, theme effects, removed banner ads, new tablet layouts, in-app settings/help, collapsing controls, an Apps cache, battery and UI fixes, plus internal performance work.

**Editorial decision:** prioritize what users can see, preserve the banner removal and supported battery fix, group navigation changes. Only include performance claims if independently confirmed.

Validated example files:
- `examples/raw-changelog.md` contains the verbose source.
- `examples/play-console.txt` contains English and Romanian Play Console blocks.
- `examples/fastlane/en-US/100100.txt` and `examples/fastlane/ro/100100.txt` contain their plain-text equivalents.

## 2. A small hotfix

Source:
`Fixed consent request interruption: retry does not show a false error. Updated libraries and CI workflow.`

Good (no padding):

```text
Fixed
• Consent setup now retries correctly after interruptions.
```

Don't invent a 250-character description for this tiny release.

## 3. A feature release with categories

Source:
`Unit Converter, Bill Splitter and Protractor were added. Counter now supports subtraction. Tablet padding was fixed. Several package dependencies were updated.`

Good:

```text
Added
• Unit Converter, Bill Splitter, and Protractor.

Improved & Fixed
• Counter now supports subtraction.
• Fixed spacing on tablets.
```

No dependency-update bullet: it does not describe a user-facing change.

## 4. A poor rewrite to avoid

Technical source:
`Added baseline profiles and moved package checks off the main thread.`

Bad:
`• 50% faster startup and zero UI lag!`

Why: no performance measurements were provided, and the claim of zero lag cannot be established. Better to omit this from the consumer note until corroborated.

## 5. A Romanian localization

English:
`• Removed the bottom banner ad.`

Romanian:
`• Am eliminat reclama banner din partea de jos.`

The Romanian sentence is localized rather than word-for-word. Count it independently.

## 6. When the source conflicts

Early commit:
`Added an Apps tab.`

Later commit:
`Removed Apps tab, replaced it with a More apps carousel.`

Correct release note:
`• Replaced the Apps tab with a More apps carousel.`

Do not list the tab as newly added and removed in the same release, unless that behavior genuinely reached users between separate releases.
