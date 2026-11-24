# Copy-ready templates

Templates are **structures, not content to publish verbatim**. Include categories only where backed by real changes; remove unused categories, bracket placeholders, and excess line breaks.

## Feature update (single language)

```text
Added
• [Highest-impact user-facing addition]
• [Other important addition]

Changed
• [Meaningful existing behavior or UI change]

Fixed
• [Specific bug fix]
```

## Maintenance release

```text
Fixed
• [Specific resolved problem]
• [Other notable fix, if any]
```

## Very short release

```text
• [One meaningful feature or fix.]
```

## Multi-locale Play Console paste

```text
<en-US>
[English note body]
</en-US>
<ro>
[Romanian note body]
</ro>
```

## Fastlane path template

`fastlane/metadata/android/<locale>/changelogs/<versionCode>.txt`

Put only the corresponding language's **body** in that file, not the wrappers.
