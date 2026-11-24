# Material audit and verification

How to review a screen, app, or page against Material 3, and how to verify a Material change before
reporting it as done. The scoring scheme is the skill's own review method, not a Material standard.

## When to audit

- The user asks to audit, review, or check Material compliance.
- A redesign or migration is finished and needs a check before handoff.
- A screen looks generic or over-decorated and needs a structured critique.

## Procedure

1. Identify the target: source files, a running app, a URL, a screenshot, or a design file.
2. Identify the platform and the Material library and version in use.
3. Read the existing theme and shared components first. Judge the screen against the product's design
   system as well as against Material.
4. Inspect each category below. For each finding, cite the file and line (or screen region), the
   Material rule with its `documentation/` path, and the fix.
5. Separate official rule violations from heuristic findings. Label heuristic findings as such.
6. Write the report.

## Categories

| Category | What to check |
|---|---|
| Color | Roles from the theme (`MaterialTheme.colorScheme`, theme attributes, `--md-sys-color-*`) instead of literal colors; correct pairs (`on X` on `X`); `outline` versus `outline variant`; dark theme; dynamic color fallback; contrast levels. |
| Typography | Type roles from the theme; correct role per element (`typography.md`); emphasized styles used for emphasis only; font scaling preserved. |
| Shape | Shape tokens instead of magic radii; component defaults respected; nested radii follow `inner = outer - padding`; abstract shapes kept off text containers. |
| Elevation | Tonal surface roles for hierarchy; shadows only where overlap needs depth; no surface color derived from elevation. |
| Components | The right component for each job (`design-quality.md`, `components/*.md`); correct variants and emphasis; no deprecated choices (navigation drawer, baseline navigation bar) in new work. |
| Layout | Window size classes; canonical layouts where the content fits; margins and spacers from `layout.md`; constrained width on large windows; nothing across a hinge. |
| Navigation | One navigation component per window class; consistent destinations; predictive back where applicable. |
| Motion | Spatial versus effects springs; motion scheme instead of hand-written durations; transitions on the legacy easing tokens; reduced motion respected. |
| Accessibility | Target sizes; semantics and labels; focus order and visibility; contrast in all themes; non-color cues; large text. See `accessibility.md`. |
| Theming | One theme provider; light, dark, and dynamic schemes complete; no scattered overrides. |

Score each 0 to 10:

- 9 to 10: follows Material and the product's system; no meaningful issues.
- 7 to 8: minor issues, such as a few literal values.
- 4 to 6: partial adherence with significant gaps.
- 1 to 3: major violations, or mostly M2 or non-Material patterns.
- 0: not applicable or absent.

Status: pass at 7 and above, warn at 4 to 6, fail at 3 and below.

## Report template

```
# Material 3 audit

Target: <path, URL, or screen>
Platform and library: <for example Compose Material 3 1.5.0>
Date: <date>
Overall: <sum of category scores>/100

| Category | Score | Status |
|---|---|---|
| Color | x/10 | pass, warn, or fail |
| ... | | |

## Critical issues
<score 0 to 3: location, rule and source path, fix>

## Warnings
<score 4 to 6>

## Working well
<score 7 to 10, briefly>

## Fixes in priority order
1. ...
```

## Quick source checks

Starting points only; tune them to the codebase.

```bash
# Compose: literal colors and radii outside the theme
rg -n "Color\(0x" --glob "*.kt"
rg -n "RoundedCornerShape\([0-9]+\.dp\)" --glob "*.kt"

# Compose: hard-coded text sizes
rg -n "fontSize = [0-9]+\.sp" --glob "*.kt"

# Web: literal colors in styles
rg -n "#[0-9a-fA-F]{3,8}\b" --glob "*.{css,scss}"

# Web: M2 packages mixed with Material Web
rg -n "@material/mdc-" --glob "*.{js,ts,json}"
```

A match is a question, not a violation. Theme files legitimately contain literal colors.

## Dense utility screen review

For settings, admin, finance, or data-entry screens, check in this order:

1. Can the user scan it? Lists and aligned rows beat card grids for homogeneous data.
2. Count explicit containers and strokes. Remove any whose job is already done by spacing, alignment,
   type, or one divider (`design-quality.md`).
3. Shadows, gradients, glass, and colored icon tiles: remove unless they carry meaning.
4. Is there one clear primary action per region?
5. Are type roles doing the hierarchy work, instead of color or containers?
6. Motion: standard or fast springs on repeated actions; no decorative animation.
7. Do not flatten it into a single gray surface. Keep surface tones, selected states, and the primary
   action's emphasis.

## Verification before reporting

For any visible UI change, compilation is not visual proof.

- Run the smallest relevant compile, lint, or test task.
- Render the exact target: a device or emulator, Compose previews, or a browser at the reported
  viewport. For phone and tablet layouts, include a compact phone and every other affected window
  class.
- Check long localized text, large font scale, dark theme, dynamic or custom color, and the loading,
  empty, and error states the feature can reach.
- Run the platform checks in `android.md` (Android verification) or `web.md` (web verification).
  Build and test commands belong to the `android-build-validation` skill.
- Confirm 48dp touch targets on Android and appropriate hit targets on the web.
- Confirm contrast and that meaning does not depend on color alone.
- Confirm there are no clipped labels, overlapping controls, accidental nested scrolling, or layout
  jumps.

## Handoff report

State:

- The Material library and version, and any experimental APIs used.
- The hierarchy, components, and adaptive behavior implemented.
- Which values came from Material and which were design decisions.
- Accessibility and system UI behavior covered.
- The checks that actually ran and the window sizes, devices, or browsers actually inspected.
- Remaining risks: devices not tested, dependency or migration work deferred.

Do not claim Material 3 Expressive, adaptive, accessible, or visually verified behavior from naming,
dependency presence, or a successful build alone.
