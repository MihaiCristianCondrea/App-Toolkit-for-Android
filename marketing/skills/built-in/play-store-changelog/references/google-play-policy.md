# Google Play release-note rules and source references

Sources checked: 2026-11-02. Recheck these pages when Google's requirements change.

## Official requirements

Google Play Console Help, **Prepare and roll out a release**:
https://support.google.com/googleplay/android-developer/answer/9859348?hl=en

Google's documented requirements:

- In **What's new in this release?**, the description should describe the update and **must not be used for promotional purposes or to solicit user actions**.
- The editor supports **up to 500 Unicode characters per language**.
- Locale tags appear on their own lines, for example:

  ```text
  <en-US>
  Notes here.
  </en-US>
  ```

- Play Console derives which languages are available from the app's supported translations. Do not assume an arbitrary locale will be configured in the user's console.

### Skill-specific conservative limits

These are **skill recommendations, not extra Google policies**:

- Default hard cap: **499 Unicode code points in the note body**, deliberately strictly under the Google maximum.
- Editorial target: **250–450 characters**, only if there is enough source material. Never pad short bugfix updates.
- Prefer well-supported, descriptive text to emojis. Avoid emojis that consume multiple code points or render inconsistently.
- No Markdown heading symbols. Plain text headings, bullets, and newline separation are readable and predictable.
- Treat the raw note body as the counted unit. Count internal newlines, spaces, bullets, and punctuation. Exclude console-only `<locale>` wrappers, which are outside the per-language body.
- If a tool normalizes line endings, revalidate the actual bytes that will be uploaded. The included validator normalizes CRLF and CR to LF before counting, then strips **boundary** newlines added by the tag layout, not content lines.

## Languages and tags

Google Play Console Help, **Supported languages**:
https://support.google.com/googleplay/android-developer/table/4419860?hl=en

This lists `en-US`, `en-GB`, and `ro` among its language identifiers. That page documents Console interface languages, not necessarily the complete set of per-app release-note locales. **The Play Console release editor is the authority for the tags configured for that particular app.** The Google Play Developer API uses BCP-47 language tags and gives `de-AT` as an example. See:
https://developers.google.com/android-publisher/api-ref/rest/v3/edits.tracks

## Fastlane source

Fastlane `supply` docs, **Changelogs (What's new)**:
https://docs.fastlane.tools/actions/supply/

- Changelog path: `fastlane/metadata/android/<locale>/changelogs/<versionCode>.txt`.
- `default.txt` can supply a default when no version-code match exists.
- These are **plain text per-locale files**. Do not add Play Console angle-bracket locale wrappers inside them.

## Code point counting and Unicode nuance

Google's documentation says “Unicode characters” without specifying the counting algorithm. This skill's validator counts Python Unicode code points, a conservative, consistent approximation. An emoji sequence that looks like one symbol may consist of several code points. Do not promise that every client, API, or rendering system uses exactly the same calculation. Prefer clear margins and verify in Play Console before publication.
