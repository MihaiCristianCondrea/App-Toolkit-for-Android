---
name: android-localization
description: >
  Translate and maintain Android XML string resources safely across supported
  locales. Use for strings.xml, plurals.xml, errors.xml, and other localized
  resource files.
metadata:
  last-updated: '2026-08-13'
  keywords:
  - android
  - localization
  - translation
  - strings.xml
  - plurals.xml
  - errors.xml
  - resources
  - i18n
  - l10n
---

# Android Localization

Translate Android XML resources while preserving the existing resource structure.

## Before changing translations

Inspect the target module first:

- Determine supported locales from the existing `res/values-*` directories and Gradle/resource configuration.
- Do not invent locales or create new locale directories unless explicitly requested.
- Check which resource file owns each entry.

Common files include:

```text
strings.xml   general strings
plurals.xml   plurals
errors.xml    errors
````

Keep translations in the corresponding file. Do not move entries between resource files.

## Rules

Preserve exactly:

* resource names and attributes
* XML comments and ordering
* placeholders such as `%1$s`, `%2$d`
* plural quantities
* HTML/XML markup
* `\\n`, escaped quotes, apostrophes, and other escapes
* intentional whitespace
* remove duplicated string resources if they are already exists

Escape Android XML correctly:

```text
&  -> &amp;
<  -> &lt;
>  -> &gt;
'  -> \\'
```

Do not translate:

* app and brand names
* file paths
* placeholders
* code/API names
* technical abbreviations such as API, URL, QR, Wi-Fi (they belong to `untranslatable_strings.xml`)

Translations must be natural and context-aware, not literal.

Use appropriate UX tone for the target language.

Skip a locale rather than adding a translation whose meaning is uncertain.

## Existing translations

When touching localized resources:

* Fix clearly incorrect or outdated translations encountered in the affected entries.
* Preserve good existing translations.
* Do not rewrite unrelated strings.
* Do not reorder or reformat the file unnecessarily.
* Keep translations length similar to original strings written in English if possible.

## Plurals

Preserve every existing `<item quantity="...">`.

Translate only the text and keep placeholders intact.

Do not assume plural categories are identical between languages. Follow the structure already required by the target locale/project.

## Output

When asked only for translations, return valid Android XML ready to place in the corresponding:

```text
values-<locale>/strings.xml
values-<locale>/plurals.xml
values-<locale>/errors.xml
```

Do not add explanations, signatures, metadata comments, or placeholder translations unless explicitly requested.