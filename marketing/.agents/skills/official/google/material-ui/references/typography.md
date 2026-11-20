# Typography

The numeric scale for all 30 styles is in `tokens.md`. Official pages:
`documentation/styles/typography/` (overview, fonts, type scale and tokens, applying type, editorial
treatments).

## The five roles

| Role | Purpose | Notes |
|---|---|---|
| Display | The largest text on screen, for short important text or numerals. | Works best on large screens. Expressive fonts such as handwritten or script are allowed. Set the matching optical size. |
| Headline | Short, high-emphasis text on smaller screens. | Marks primary passages or important regions. Expressive typefaces are fine if line height and letter spacing keep the text readable. |
| Title | Medium-emphasis, relatively short text. | Divides secondary passages or regions. Use expressive, display, handwritten, or script fonts with caution. |
| Body | Longer passages. | Use typefaces intended for body text. Avoid expressive or decorative fonts, which are hard to read at small sizes. |
| Label | Text inside components and very small text such as captions. | Buttons use label large. |

Each role has large, medium, and small. No product uses all 15 styles. Select the ones you need, and
when you reduce the scale keep clear contrast between sizes rather than small differences.

### Documented role assignments

| Element | Style |
|---|---|
| Button label | label large |
| Navigation bar destination text | label |
| Top app bar title | title |
| Category header, such as "Top News" | title |
| Article or news card title | title |
| Dialog heading | headline |
| Contact card name | headline |
| Music player timecode | label |
| Article body copy | body |
| Card hero figure or statement | display |

Exact per-component type tokens are in `component-tokens.md` and in each `components/*.md` file.

Roles describe size, which lets them adapt to the device or context. Align type with Material Symbols
for cohesion. In tight regions a narrower width axis fits more characters, such as a label under a
navigation bar icon. Avoid wide styles where space is tight, such as a top app bar.

## Emphasized styles

M3 Expressive added 15 emphasized styles alongside the 15 baseline styles. They keep the same size
and line height and change weight and, in some styles, tracking. Baseline and emphasized styles are
meant to be used together. Emphasized is not a replacement scale.

Swap the token instead of restyling:

```
baseline    md.sys.typescale.display-large
emphasized  md.sys.typescale.emphasized.display-large
```

Material components do not use emphasized styles by default. You opt in per element.

### Where emphasized styles belong

Material recommends them for selection, actions, headlines, and editorial treatments. They work well
with:

- Badges
- Buttons for primary actions
- Extended FAB
- Selected list items
- Selected menu items

Two independent reasons to apply them, usable together:

- Weight: apply emphasized styles to text that already carries weight (medium, bold) to reinforce
  hierarchy.
- Context: apply them selectively to show hierarchy or state, such as selected states, unread
  messages, or key interactions.

The purpose is hierarchy. In Material's words, emphasized type "reinforces the information hierarchy
and draws attention to important actions, like 'begin recording,' or information, like unread
messages."

## Brand and plain typefaces

The scale has two typeface slots:

- Brand: the larger styles (display, headline, title large). Optimize for expression.
- Plain: the smaller styles (title medium and small, body, label). Optimize for readability.
- Roboto is the default for both.

Replacing Roboto increases brand expression, and on emphasized styles it makes important text stand
out more. Baseline and emphasized sets may use different typefaces.

## Default typefaces

| Font | Character | Axes |
|---|---|---|
| Roboto | Default for Android and the M3 type scale; 3,300+ glyphs. | Static. |
| Roboto Flex | Extended weights and widths with size-specific designs; 900+ glyphs (Latin, Greek, Cyrillic). | Slant, width, weight, grade, optical size, plus parametric axes XOPQ, YOPQ, XTRA, YTUC, YTLC, YTAS, YTDE, YTFI. |
| Roboto Serif | Comfortable reading; usable in app interfaces. | Weights and widths across a broad size range. |
| Roboto Mono | Monospaced; for code and aligned numbers. | Weight, italic. |
| Noto Sans | Global fallback; 150+ scripts. | Width, weight, italic. |

Static fonts (Roboto) apply by default to all M3 components. Variable fonts, including Roboto Flex and
Google Sans Flex, are not yet part of the M3 type scale. Google Sans Flex has its own section below.

Fallback chain: Roboto Flex, then Roboto, then the Noto Sans collection. Confirm that the fallback is
configured in the build.

Roboto is the correct default typeface for a Material 3 product. Generic web design advice to avoid
Roboto does not apply when the product follows Material, unless the brand supplies its own typeface.

## Google Sans Flex

Google Sans Flex is the preferred typeface for the apps in this repository's project family when the
project already uses it or a redesign selects it. It is not a Material 3 default and not a
requirement for other products. A project's existing typography wins unless the task is a redesign
that changes it.

Sources, checked 2026-11-20: https://github.com/googlefonts/googlesans-flex (README and `OFL.txt`),
https://design.google/library/google-sans-flex-font, and the Google Fonts `METADATA.pb` for
`ofl/googlesansflex`.

### Availability and license

- License: SIL Open Font License 1.1, without Reserved Font Names. Apps and sites may bundle,
  subset, and convert it (for example to WOFF2). Ship `OFL.txt` with redistributed font files.
- Google Fonts family name: `Google Sans Flex`. Google Fonts serves one variable file,
  `GoogleSansFlex[ROND,opsz,slnt,wdth,wght].ttf`. The repository publishes release archives such as
  `GoogleSansFlex-v5.000.zip`.
- It is based on Google Sans. The design article calls it "a power tool for expression".
- Use the [shared font library](../../../../../../assets/README.md#fonts) for local files and license details. Android apps keep selected files in the owning module's `res/font/`.

### Axes

| Tag | Axis | Range (Google Fonts build) | Default |
|---|---|---|---|
| `wght` | Weight | 1 to 1000 | 400 |
| `wdth` | Width | 25 to 151 | 100 |
| `opsz` | Optical size | 1 to 144 | 18 |
| `slnt` | Slant | -10 to 0 (negative leans right) | 0 |
| `ROND` | Roundedness | 0 to 100 | 0 |

The README lists a sixth axis, `GRAD` (grade, 0 to 100, default 0), in the design sources. The
Google Fonts file does not include it. Use grade only with a build that has the axis, and check the
file's `fvar` table before relying on any range.

What each axis is for:

- Optical size: the article says it "adapts letter shapes to maintain readable proportions at any
  size, from a smartwatch to a billboard". Match `opsz` to the rendered size; browsers do this
  automatically with `font-optical-sizing: auto`.
- Roundedness: softens stroke ends and corners. It pairs with Material's shapes, which Material notes
  "share roundness visual attributes" with Google Sans Flex (`documentation/styles/shape/overview-principles.md`).
  Keep one roundedness value per role so text does not look inconsistent.
- Weight and width: the same rules as the variable axes table below. Light weights only at display
  sizes; narrow widths for tight labels; no wide styles in tight spaces.
- Grade: changes apparent weight without reflowing text, useful in dark theme, where builds include it.
- Slant: an oblique for emphasis inside running text. Do not use it for whole paragraphs.

### Use with Material 3 and Expressive

Material's type scale uses static fonts and does not yet include variable fonts. Using Google Sans
Flex is therefore a typeface customization ("Customizing type styles" below), done the Material way:

- Set it as both the brand and plain typeface, or as the brand typeface with Roboto or Roboto Flex as
  plain.
- Keep the M3 sizes, line heights, and tracking from `tokens.md`. Change only the typeface and, where
  needed, line height and letter spacing.
- Map weights to the scale: Regular 400, Medium 500, Bold 700, the same steps the baseline and
  emphasized styles use.
- Treat `ROND`, `wdth`, and large `opsz` changes as expressive tools for headlines, editorial
  treatments, and hero moments. Body and label text stay at default width and roundedness.
- Tokenize any axis value used in more than one place, as Material advises for editorial treatments.

### Compose

The Android downloadable fonts provider exposes weight and style, not custom axes. To use `ROND`,
`wdth`, or `opsz`, bundle the variable TTF in `res/font/` and set axes per `Font`. Variable font
settings need API 26 or later; older devices get the default instance.

```kotlin
@OptIn(ExperimentalTextApi::class)
private fun googleSansFlex(weight: FontWeight, opticalSize: TextUnit, roundness: Float = 0f) = Font(
    resId = R.font.google_sans_flex,
    weight = weight,
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weight.weight),
        FontVariation.opticalSizing(opticalSize),
        FontVariation.Setting("ROND", roundness),
    ),
)

private val bodyFamily = FontFamily(
    googleSansFlex(FontWeight.Normal, 16.sp),
    googleSansFlex(FontWeight.Medium, 16.sp),
    googleSansFlex(FontWeight.Bold, 16.sp),
)

private val displayFamily = FontFamily(
    googleSansFlex(FontWeight.Normal, 57.sp, roundness = 100f),
    googleSansFlex(FontWeight.Medium, 57.sp, roundness = 100f),
)

val AppTypography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = displayFamily),
        bodyLarge = bodyLarge.copy(fontFamily = bodyFamily),
        // Assign every style the product uses; unassigned styles keep the default typeface.
    )
}
```

- Do not rely on automatic optical sizing. Give each size band its own `FontFamily` with a matching
  `opsz`, as above.
- Check whether `FontVariation` still needs `ExperimentalTextApi` in the installed Compose version.
- Name the file in lowercase (`google_sans_flex.ttf`); resource names cannot contain brackets.

### Web prototypes

```css
@font-face {
  font-family: 'Google Sans Flex';
  src: url('/fonts/google-sans-flex/variable.ttf') format('truetype');
  font-weight: 1 1000;
  font-stretch: 25% 151%;
  font-style: oblique 0deg 10deg;
  font-display: swap;
}

:root {
  --md-ref-typeface-brand: 'Google Sans Flex', 'Roboto Flex', 'Roboto', 'Noto Sans', sans-serif;
  --md-ref-typeface-plain: 'Google Sans Flex', 'Roboto Flex', 'Roboto', 'Noto Sans', sans-serif;
}

body { font-optical-sizing: auto; }

.hero-title { font-variation-settings: 'ROND' 100; }
```

- The CSS URL assumes the asset sync script copies the shared variable TTF into `public/fonts/google-sans-flex/`. If the project uses WOFF2, point the URL and format at its WOFF2 file.
- `font-variation-settings` overrides weight and width set by other properties when it names `wght`
  or `wdth`. Set only the axes that have no CSS property (`ROND`, and `GRAD` where present).
- `marketing-ui` loads fonts through the kit's `theme/` module; keep the same family names there as
  in the app's Compose theme.

### Fallbacks and languages

The Google Fonts build lists these subsets: Latin, Latin Extended, Vietnamese, Canadian Aboriginal,
Cherokee, Nüshu, Syriac, Tifinagh, math, and symbols. Greek, Cyrillic, Arabic, Hebrew, Devanagari,
Thai, and CJK are not listed. Plan for them:

- Android: the system falls back per glyph to the platform fonts (Roboto and Noto), so unsupported
  scripts render, but in a different design. Check every shipped locale.
- Web: list fallbacks explicitly: `'Google Sans Flex', 'Roboto Flex', 'Roboto', 'Noto Sans', sans-serif`,
  plus a script-specific Noto family for locales that need one.
- Fallback faces have different metrics. Test line breaks and truncation in each locale.

### Performance

- A full variable font with five axes is larger than one static weight. On the web, subset to the
  scripts in use, convert to WOFF2, use `font-display: swap`, and preload only the file the first
  screen needs.
- On Android, one variable file replaces several static weights. Measure APK size, and keep axis
  instances to the combinations the type scale uses.
- Do not animate axes on long text or inside scrolling lists; animate them on short headlines.

### Accessibility

- Keep the M3 sizes and preserve font scaling (sp, rem).
- Avoid weights below Regular and widths below the default for body and label text.
- Keep tracking and line height readable after any width or weight change.
- Contrast rules do not change with the typeface: 4.5:1 for small text, 3:1 for large text.

## Customizing type styles

1. Change the brand and plain typeface tokens if the product uses a different typeface.
2. Adjust line height and letter spacing to refine the result. Avoid changing type size, because it
   affects how components render and reflow.
3. Repeat for both the baseline and emphasized sets and keep emphasized styles consistent with each
   other.

Heavier fonts may need wider letter spacing. Fonts with long ascenders and descenders need different
line heights.

Customizing the type scale or individual styles may stop the product from receiving Material
typography token updates.

## Editorial treatments

Editorial treatments are standalone moments driven by type: custom, attention-grabbing sizes, larger
display type, or a blockier look. They depart from functional layouts and from routine steps in a
flow. In the expressive system they combine with motion, shape, or color to create hero moments, and
type can dominate the screen.

Material lists three uses:

1. Celebrating content: take over the screen to mark a user action, memory, or preference. Match the
   type to the tone, for example narrow and thin for calm, bolder and italic for energy.
2. Voice of the user: let users personalize typography or adjust text from their input.
3. Bespoke functionality: express a unique function, for example width and weight axes that increase
   with light brightness.

Best practices:

- Keep similar editorial moments consistent by creating tokens for each.
- Match the emotional tone of the text to the task.
- Don't mix multiple or clashing styles in the same layout.
- Don't mimic personalization theming.
- Don't use editorial treatments in labels or for plain information.

### Variable axes for editorial treatments

| Axis | What it does | Cautions |
|---|---|---|
| Weight | Overall stroke thickness; variable fonts give a continuous range. | Light weights break down on low-resolution displays at small sizes, so use them only at display sizes. Heavy weight at small sizes hurts readability, for example bold navigation bar labels. |
| Grade | A secondary weight modifier that does not change letter widths or line breaks. | Roboto Flex offers +150 and -200. Use a negative grade in dark theme to counter text that appears heavier. Grade changes emphasis without reflowing text. |
| Width | Horizontal space; Material states Roboto Flex ranges from 25 to 150. | Narrow widths fit more characters in small labels. Avoid wide styles in tight spaces such as a top app bar. |
| Optical size | Versions optimized for different sizes. | Match optical size to type size. Don't use large optical sizes at small type sizes. |

## Readability

- Use a line height of about 1.2 times the type size for large type (title, headline, display).
- Use about 1.5 times for small copy (body, label). Too tight hurts flow and too loose breaks
  cohesion.
- Use tabular figures in tables and anywhere values change often, such as clocks. They prevent
  layout shift and keep values aligned.

## Typesetting

Two methods, chosen by platform:

- Padding and bounding boxes (web and iOS): line height equals the bounding box height and text is
  centered by CSS half-leading. Measure spacing from the box height plus top and bottom padding.
- Baseline (Android and platform-agnostic specs): line height is the baseline-to-baseline distance
  and spacing is measured to the baseline. For centering, specify center alignment.

## Units

| | Android | Web |
|---|---|---|
| Font size | sp | rem, where rem = sp / 16 |
| Letter spacing | em = tracking / font size | rem = tracking / font size |

Examples: 10sp is 0.625rem, 12sp is 0.75rem, 14sp is 0.875rem, 16sp is 1rem, 24sp is 1.5rem, 60sp is
3.75rem.

Preserve user font scaling. Use sp on Android and rem on the web. Never convert text to fixed px or
dp to make a layout fit.

## Type tokens on the web

Material Web reads type through `--md-sys-typescale-<style>-<property>` custom properties, where
property is `font`, `weight`, `size`, `line-height`, or `tracking`, and through the reference
typefaces `--md-ref-typeface-brand` and `--md-ref-typeface-plain`.

```css
:root {
  --md-ref-typeface-brand: 'Roboto Flex', 'Roboto', sans-serif;
  --md-ref-typeface-plain: 'Roboto Flex', 'Roboto', sans-serif;
}

.section-title {
  font-family: var(--md-sys-typescale-title-large-font);
  font-weight: var(--md-sys-typescale-title-large-weight);
  font-size: var(--md-sys-typescale-title-large-size);
  line-height: var(--md-sys-typescale-title-large-line-height);
  letter-spacing: var(--md-sys-typescale-title-large-tracking);
}
```

Material Web does not ship emphasized styles. Build them from the emphasized values in `tokens.md`
when a design needs them.

Loading Roboto Flex as a variable font:

```css
@font-face {
  font-family: 'Roboto Flex';
  src: url('/fonts/RobotoFlex-VariableFont.woff2') format('woff2');
  font-weight: 100 1000;
  font-stretch: 25% 151%; /* the font file's declared range */
  font-display: swap;
}
```

## Accessibility

- Contrast: 3:1 for large text and 4.5:1 for small text.
- Default text color is `on surface`; `on surface variant` is a strong alternative.
- Hyperlinks on a surface use `primary`; `tertiary` makes links less prominent. Hyperlinked text must
  also be underlined.
- Test long words, localization, multi-line labels, and large font scales. Let text wrap or the
  component grow instead of truncating an action label.
