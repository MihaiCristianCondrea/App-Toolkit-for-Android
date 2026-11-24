---
name: material-ui
description: >
  Design, implement, and review Material Design 3 (Material You) and Material 3 Expressive
  interfaces for Jetpack Compose, Android Views, Wear OS, and the web (Material Web). Covers
  color, typography (including Google Sans Flex), shape, elevation, and motion tokens; component
  selection and exact specs; adaptive layouts and navigation; accessibility and interaction states;
  and Material audits. Ships an offline copy of the official m3.material.io documentation and 2,587
  indexed official images. Use for any Material, M3, MD3, Material You, or "make it expressive" UI
  task. Do not apply Material to a product that uses another design system unless asked.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-20'
  keywords:
  - material-design-3
  - material-you
  - m3-expressive
  - jetpack-compose
  - material-web
  - design-tokens
  - google-sans-flex
  - adaptive-layout
  - accessibility
---

# Material UI

The Material 3 authority for this repository: which Material rule applies, where the exact value
lives, and how to check the result. Detailed knowledge is in `references/`; this file routes to it.

## What is in this skill

| Path | Contents | Edit? |
|---|---|---|
| `documentation/` | Offline snapshot of m3.material.io (374 pages, captured 2026-05-21). Index: `documentation/README.md`. | Never. Google's text, kept unchanged. |
| `visuals/` | 2,587 official images, captioned in `visuals/INDEX.tsv`. | Never. |
| `references/` | Curated extracts, AndroidX token values, implementation guidance, labelled heuristics. | Yes, with care. Token tables keep their values. |
| `scripts/` | Refresh scripts for the token extract and the page and image snapshot. | Yes. |
| `NOTICE.md` | Sources, licenses, attribution. | Keep accurate. |

## Source rules

1. Look up every number, token name, and component name before stating it: system scales in
   `references/tokens.md`, per-component dp values in `references/component-tokens.md` (including
   values m3.material.io shows only in images).
2. `documentation/` outranks the curated references for design intent. The project's installed
   library outranks both for API availability. Full order: `references/sources.md`.
3. When a value is not in Material, label it:

   > From M3: the button label uses `label large` (14sp, 20sp line height, 0.1sp tracking, Medium).
   > Design decision (not in M3): 12dp gap between the two action buttons.

4. Material publishes no stagger values, per-component choreography timings, or reduce-motion scheme
   swap. Those are always design decisions.
5. Heuristics in this skill are labelled as such. Never present one as a Material requirement.
6. Do not fetch m3.material.io; it returns an empty shell to plain requests and the snapshot has the
   content. Fetch only release notes and API references when checking versions.

## Workflow

1. Identify the platform (Compose, Views, Wear, web), the objective (new screen, fix, review,
   migration, prototype), and the output (spec, code, audit, prototype).
2. Inspect the project: theme, typography, shared components, Material library and version, and
   whether a design-system or app framework library such as App Toolkit is a dependency. When one is
   present, its public theme and components come first. Do not replace existing design decisions
   unless the task is a redesign.
3. Decide standard M3 or Expressive per screen (`references/expressive.md`).
4. Load only the references the router names for the task. Open the matching `documentation/` page
   when the exact requirement matters, and one or two images from `visuals/INDEX.tsv` when anatomy or
   a do and don't example helps.
5. Check API availability before writing code (`references/android.md`, `references/platforms.md`).
6. Verify the rendered result and report what was checked (`references/audit.md`).

### New Android screens

Root `AGENTS.md` owns this sequence. Load each skill when its phase starts, not before.

| Phase | Owner |
|---|---|
| 1. Inspect | Root `AGENTS.md`: modules, README files, dependencies, theme, reusable components. |
| 2. Plan | [`android-ui-layer`](../../../../../../.agents/skills/official/google/android-developers/android-ui-layer/SKILL.md) for state and events, [`layered-tree-review`](../../../../../../.agents/skills/first-party/layered-tree-review/SKILL.md) for placement. |
| 3. Material design | This skill: components, tokens, typography, states, adaptive behavior, Expressive choices, motion spec. |
| 4. Interactive prototype | [`marketing-ui`](../../../first-party/marketing/marketing-ui/SKILL.md) in `marketing/ui`, using the phase 3 spec. |
| 5. Approval | `marketing-ui` [design handoff](../../../first-party/marketing/marketing-ui/references/design-handoff.md). No Compose UI changes before approval unless the user waived it. |
| 6. Compose implementation | `android-ui-layer` and `layered-tree-review`, with this skill for Material APIs (`references/android.md`). |
| 7. Validation | [`android-build-validation`](../../../../../../.agents/skills/first-party/android-build-validation/SKILL.md), plus the visual checks in `references/audit.md`. |
| 8. Parity | `marketing-ui` parity check against the implemented screen. |

For an existing screen (Compose to web prototype), `marketing-ui` reads the Compose source; use this
skill only to resolve tokens, components, and Material Web limits. Motion principles come from
[`motion-design`](../../lottiefiles/motion-design/SKILL.md) when a phase needs them. Shared prose
rules are in [`writing-style`](../../../../../../.agents/skills/first-party/writing-style/SKILL.md).

Other Android skills to load when the task needs them:
[`adaptive`](../../../../../../.agents/skills/official/google/android/jetpack-compose/adaptive/SKILL.md),
[`edge-to-edge`](../../../../../../.agents/skills/official/google/android/system/edge-to-edge/SKILL.md),
[`wear-compose-m3`](../../../../../../.agents/skills/official/google/android/wear/wear-compose-m3/SKILL.md),
[`glance-widget`](../../../../../../.agents/skills/official/google/android-developers/glance-widget/SKILL.md).
For web and store pages, [`taste-skill`](../../../third-party/leonxlnx/taste-skill/SKILL.md) and
[`ui-unslop`](../../../third-party/microck/ui-unslop/SKILL.md) give direction but never override
Material component contracts.

## Router

Reference paths are relative to `references/`; official paths are relative to `documentation/`.

| Task | Load | Official pages |
|---|---|---|
| Exact numbers: type scale, radii, springs, elevation, contrast | `tokens.md` | `styles/` |
| Per-component dp, shape, elevation | `component-tokens.md` (search the section) | `components/<x>/specs.md` |
| A component's files and platform names | `component-index.md` | `components.md` |
| Buttons, icon buttons, button groups, split and segmented buttons | `components/buttons.md` | `components/all-buttons.md` |
| FAB, extended FAB, FAB menu | `components/fabs.md` | `components/floating-action-button/` |
| Navigation bar, rail, drawer, tabs | `components/navigation.md` | `components/navigation-rail/` |
| App bars, toolbars | `components/bars.md` | `components/app-bars/` |
| Cards, carousel, lists, divider, sheets, dialogs | `components/containment.md` | `components/cards/` |
| Checkbox, radio, switch, slider, chips, text fields, search | `components/input.md` | `components/text-fields/` |
| Badges, progress, loading indicator, snackbar, tooltip, menus, pickers | `components/feedback.md` | `components/progress-indicators/` |
| Color roles and pairing | `color.md` (long extract: `color-schemes.md`) | `styles/color/` |
| Theme setup, dynamic color, brand colors, dark theme | `theming.md` | `styles/color/choosing-a-scheme.md` |
| Typography, Google Sans Flex | `typography.md` | `styles/typography/` |
| Shape, shape library, morph | `shape.md` | `styles/shape/` |
| Motion springs and schemes | `motion.md` | `styles/motion/overview/` |
| Transitions, legacy easing and duration | `transitions.md` | `styles/motion/transitions/` |
| Elevation, spacing, icons, design tokens, customization | `foundations.md` | `styles/elevation/`, `foundations/design-tokens/` |
| Breakpoints, panes, canonical layouts, RTL (spec values) | `layout.md` | `foundations/layout/` |
| Adaptive implementation: navigation switching, panes, foldables, CSS | `adaptive-layouts.md` | `foundations/layout/canonical-examples/` |
| States, gestures, inputs, selection | `interaction.md` | `foundations/interaction/` |
| Accessibility checks and the state matrix | `accessibility.md` | `foundations/designing/` |
| Hierarchy, containment, decoration, loading, forms | `design-quality.md` | `foundations/layout/layout-overview/parts-of-layout.md` |
| M3 Expressive tactics and how much to use | `expressive.md` | `blog/building-with-m3-expressive.md` |
| Compose and Views implementation | `android.md`, then `theming.md` | `develop/android/` |
| Material Web, HTML, CSS | `web.md` | `develop/web.md` |
| What exists on which platform | `platforms.md` | `develop.md` |
| Versions, links, which source wins | `sources.md` | |
| Audit, review, verification before handoff | `audit.md` | |
| Official images | `visuals.md` | |

Image search, from this skill's directory:

```bash
grep -i "^components_split-button/" visuals/INDEX.tsv
grep -i "don't" visuals/INDEX.tsv | grep -i "^components_cards/"
```

If the project's type scale uses Google Sans Flex, see the [shared marketing assets](../../../../../assets/README.md) for the local files and licensing details.

## Quick decisions

| Decision | Answer | Source |
|---|---|---|
| Motion scheme | Expressive for most products and key interactions; standard for utilitarian products; per-element override allowed. | `motion.md` |
| Spring type | Position, size, rotation, corner radius: spatial. Color and opacity: effects, which never overshoot. | `motion.md` |
| Corner scale | 0, 4, 8, 12, 16, 20, 28, 32, 48dp, full. Nested radius = outer minus padding. | `tokens.md`, `shape.md` |
| Abstract shapes | Decorative and image elements only, never text-heavy containers. | `shape.md` |
| Typeface | Keep the project's typeface. Roboto is the Material default. Google Sans Flex is preferred for our own apps when selected. | `typography.md` |
| Emphasized type | `md.sys.typescale.emphasized.X`: same size, heavier weight, opt-in, for selection, actions, headlines, editorial moments. | `typography.md` |
| Accent roles | Primary: most important. Secondary: no immediate attention. Tertiary: small elements needing special emphasis. | `color.md` |
| Region colors | Body `surface`, navigation `surface container`, at every window size. | `color.md` |
| Borders | `outline` identifies a control; `outline variant` for dividers and decoration. | `color.md` |
| Elevation | Tonal surface roles for hierarchy; shadow only where overlap needs depth. | `foundations.md` |
| Contrast and targets | 4.5:1 small text, 3:1 large text and required non-text cues; 48dp targets on Android. | `accessibility.md` |
| Button emphasis | Filled for the main action, then tonal, outlined, text. One filled action per region. | `components/buttons.md` |
| Navigation | Compact: bar. Medium: bar or rail. Expanded and wider: rail. Drawer no longer recommended. | `adaptive-layouts.md` |
| Window classes | Compact under 600dp, medium to 839, expanded to 1199, large to 1599, extra-large 1600 and up. | `layout.md` |
| Hero moments | One or two per product. | `expressive.md` |
| Platform status | Compose Expressive mostly stable since Material 3 1.5.0; Views and Material Web in maintenance mode. | `platforms.md` |

## Common mistakes

| Mistake | Correct |
|---|---|
| A bouncy fade or color change | Effects springs (damping 1.0) for opacity and color. |
| Hand-written durations for component motion | `MaterialTheme.motionScheme` or spring tokens; converted curves on the web. |
| Same radius on a container and its child | `inner = outer - padding`. |
| Large or full corners on cards and other dense components | A smaller step; large corners clip content. |
| `container` roles on text or icons | Containers are fills; use the paired `on` role. |
| Surface color derived from elevation | Surfaces are tone-based since 2023. |
| Literal colors, radii, and text sizes in screens | Theme roles and tokens. |
| Cards inside cards, borders on every section, decorative shadows or glass | `design-quality.md`. |
| A flattened screen with no emphasis | Keep shape contrast, surface tones, emphasized type, and responsive motion where they guide attention. |
| Promising a feature on the user's platform | Check `platforms.md` and the installed library. |
| Claiming "verified" after a build | Render, inspect, and report what was observed (`audit.md`). |

## Output

- Spec or design: components and variants, tokens, states, adaptive behavior, and motion, each value
  marked as from M3 or a design decision.
- Code: theme roles and public component APIs, narrow opt-ins, the library version, and any
  experimental API.
- Review: the report template in `references/audit.md`, separating rule violations from heuristic
  findings.

## Maintenance

- `node scripts/refresh-component-tokens.js` regenerates `references/component-tokens.md` from the
  AndroidX token sources.
- `node scripts/refresh-m3.js pages <dir>` and `images <dir>` re-scrape m3.material.io. The page
  output uses flat file names and a different format from `documentation/`, so use it to find
  changes, not to replace the snapshot. The image output matches `visuals/` and `INDEX.tsv`. Write to
  a separate directory, compare, and update `NOTICE.md` when assets change.
- After a refresh, update `references/sources.md` and `references/platforms.md`.
