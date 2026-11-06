# Localization and delivery guide

## Language selection

Use only requested locales or the app's known locale set. `en-US` and `ro` are useful examples; **do not assume** that `ro-RO` is interchangeable with `ro` in a particular Play Console editor. Copy the tags actually available for that app. For API workflows, Google's `LocalizedText.language` is BCP-47.

Keep each language independently below the skill's 500-character boundary (default maximum 499). Localize headings naturally:

| English | Romanian |
|---|---|
| What's new | Noutăți |
| Added | Adăugate |
| Changed | Modificate |
| Improved | Îmbunătățiri |
| Fixed | Corectate |
| Improved & Fixed | Îmbunătățiri și corecții |

These are suggestions, not mandatory headings. In Romanian, `Remedieri` or `Probleme remediate` can be more natural than `Corectate`, depending on context. Use correct diacritics: `ă`, `â`, `î`, `ș`, `ț`.

## Play Console locale blocks

```text
<en-US>
Added
• Search tools directly from the top bar.
</en-US>
<ro>
Adăugate
• Caută instrumente direct din bara de sus.
</ro>
```

- Put locale tags **on their own lines**.
- The text **between** the tags is the note body; the tags are outside the 500-character body budget.
- Don't add stray text between locale blocks.
- A single-language plain note may be sufficient when editing one locale manually, if that is the requested output.

## Fastlane supply

File structure:

```text
fastlane/
  metadata/
    android/
      en-US/
        changelogs/
          100100.txt
      ro/
        changelogs/
          100100.txt
```

Content of `100100.txt` is just the note body. **No `<en-US>` wrappers**. Choose the exact locale directory and version code expected by that repository, not an invented version code. `default.txt` may be used as fallback, as specified by Fastlane.

Source: https://docs.fastlane.tools/actions/supply/

## Google Play Developer API

The API uses `releases[].releaseNotes[]` entries of the shape:

```json
{
  "language": "en-US",
  "text": "Added search and fixed sensor-related battery drain."
}
```

For multiple locales, supply separate `LocalizedText` objects. Do not insert angle-bracket tags in `text`. Follow the app's release update workflow and permissions; release-note generation does not authorize publishing.

Source: https://developers.google.com/android-publisher/api-ref/rest/v3/edits.tracks

## Unicode and formatting

- Unicode code points, including line breaks, bullets, diacritics, and emoji components, contribute to the validator's count.
- The validator counts all internal newlines but excludes the **single delimiter newline** immediately after an opening locale tag and immediately before its closing tag. Extra deliberate blank lines at the beginning/end remain and count.
- Windows CRLF is normalized to LF for a stable character count, but the final Play Console editor is authoritative.
- Don't remove diacritics merely to squeeze Romanian text. Shorten phrasing instead.
- Avoid translating product names that are branded in the UI: `Quick Tools`, `SOS`, `Apps`, `Decision Wheel` may be retained when those exact strings appear in the app.

## Quick commands

```bash
python scripts/validate_release_notes.py notes.txt --mode auto
python scripts/validate_release_notes.py notes.txt --mode console --required-locales en-US,ro
python scripts/validate_release_notes.py fastlane/metadata/android/en-US/changelogs/100100.txt --mode plain
python scripts/validate_release_notes.py notes.txt --mode console --json
```
