# Design quality

Guidance for producing Material screens that read clearly and avoid decoration that carries no
meaning. Most of this file is the skill's own design judgment, built on Material principles. Where a
rule is official Material guidance, it says so and points to the page. Everything else is a
heuristic: apply it by default, and override it when the product, its existing design system, or a
supplied design gives a reason.

## Order of decisions

Settle a screen in this order. Later steps cannot repair earlier ones.

1. The user's goal and what content matters most.
2. Platform conventions and the input model (touch, pointer, keyboard, rotary).
3. Navigation and information structure.
4. Which Material component does each job.
5. Theme roles and adaptive layout.
6. Brand expression.
7. Decorative detail, only where it supports the hierarchy.

## Hierarchy

- Title the screen with its destination or task. Skip eyebrows, slogans, and explanatory banners
  unless the content needs them.
- Give each task region one dominant action. Two filled buttons side by side usually mean the
  priority has not been decided.
- Put controls next to the content they affect. Global actions belong in app bars, toolbars, or
  navigation.
- Order content by task sequence or user value, not by what fits a symmetric grid.
- Move advanced, destructive, or rare controls behind progressive disclosure.
- Keep operational screens dense enough to scan. Material does not require large empty zones.
- Check hierarchy in grayscale. If the screen only works in color, hierarchy depends on color alone.

## Containment

Material distinguishes implicit containment (alignment, whitespace, typography) from explicit
containment (surfaces, cards, dividers). Android's design guidance recommends grouping with implicit
containment first and adding a visible boundary when it helps
(`documentation/foundations/layout/layout-overview/parts-of-layout.md`, Android "Material components
and containment"). The rules below turn that into review checks.

- Add a visible container only when it communicates something the layout cannot: separate ownership,
  an independent interaction, selection, or independent scrolling.
- A child container must not repeat what its parent already says. One boundary, one responsibility.
- Do not put cards inside cards.
- Do not stack a surface change, an outline, a divider, and a shadow on the same boundary. Pick the
  lightest one that works.
- Use lists for dense, homogeneous items and cards for discrete, heterogeneous items that act
  independently. A card per settings row is a list drawn badly.

Review heuristic (not a Material rule): count explicit authored containers inside a parent surface.
More than about one nested container per three meaningful content units, or more than two levels of
authored nesting from the region surface to the content, is a signal to flatten or to promote the
unit to a sibling surface. Layout-only rows, columns, and grids, and the internal anatomy of standard
components, do not count. Treat the numbers as a prompt to justify each layer, not as a limit to
enforce mechanically.

## Strokes and borders

Each stroke should have one job.

| Job | Use |
|---|---|
| Identify a component | The official outlined variant and its default border. |
| Separate adjacent groups | One divider, using `outline variant`. Do not also outline the group. |
| Show state | The component's own focused, error, selected, and disabled treatments and state layers. |
| Show keyboard focus | The platform or component focus indicator. A decorative border is not a focus indicator. |
| Decoration | Usually nothing. Omit the stroke unless it adds meaning. |

Official color rules (`color.md`): `outline` is for important boundaries and has the contrast for it;
`outline variant` is for decorative separation such as dividers. A boundary required to identify a
control needs 3:1 against adjacent colors. On Compose, use the installed version's defaults
(`CardDefaults.outlinedCardBorder()`, `ButtonDefaults.outlinedButtonBorder`,
`OutlinedTextFieldDefaults`) instead of a hand-written `BorderStroke`, after confirming the API exists
in that version.

Keep borders inside the component's geometry, so a focus or error state does not resize the component
or shift its neighbors.

## Elevation and shadow

Official guidance (`documentation/styles/elevation/applying-elevation.md`): M3 surfaces use tonal
difference to show separation by default. Shadows are for cases that need extra protection against a
background or that encourage interaction, and "when it comes to applying shadows, less is more".

- Use surface container roles for stationary hierarchy.
- Use shadow when an element overlaps other content and the overlap needs depth: a FAB over a list, a
  menu, a dialog.
- Do not elevate every card. If everything floats, depth means nothing.
- Avoid colored shadows, glows, large blur radii, and floating shells around the whole app.
- In dark theme, check that surfaces separate; do not compensate with bright outlines everywhere.

## Transparency and glass

Material surfaces are opaque roles with defined contrast. Material 3 does not use frosted or liquid
glass surfaces as a visual language.

Avoid by default:

- Transparent or translucent cards, app bars, and navigation.
- `backdrop-filter` blur or frosted panels behind routine content.
- Translucent white overlays that wash out color roles.
- Glass borders, inner highlights, and alpha gradients used for polish.
- Important text, forms, or controls placed over images without a stable surface.

Translucency has legitimate uses: scrims, immersive media controls, system overlays, or a supplied
brand direction. Keep it local, test contrast against the worst background it can sit on, check light,
dark, dynamic color, and large text, and provide an opaque fallback.

## Shape restraint

- Start from the theme shape scale and the component defaults.
- Use strong shape contrast on one prominent element, not on every surface.
- Do not give every button, field, sheet, image, and panel the same pill or oversized radius.
- Clip images only when the container owns them, and never so that important content is cut off.

See `shape.md` for the official rules on abstract shapes and nested radii.

## Color restraint

- Do not give every section its own accent color.
- Do not use several unrelated source colors to create variety.
- Do not use `primary` for every icon, heading, and link. Primary marks the most important elements.
- Do not encode success, warning, selection, or error by color alone.
- Validate real foreground and background pairs in light and dark schemes; pastel on pastel fails.
- Gradients are not a replacement for tonal roles. Use them only when the brand or content calls for
  them.

## Component jobs

Choosing the wrong component is a design error, not a styling detail. The detailed selection rules
are in `components/*.md`; these are the frequent cases.

### Actions

| Need | Use | Avoid |
|---|---|---|
| Main completion action | Filled button | Several filled peers |
| Important supporting action | Filled tonal button | An arbitrary accent color |
| Medium-emphasis action | Outlined button | Also outlining the surrounding section |
| Low-emphasis action | Text button | An unlabeled icon with unclear meaning |
| Compact, familiar action | Icon button with an accessible name | A tiny bare icon without a 48dp target |
| Frequent screen-level creation | FAB | A FAB for navigation or rare actions |
| One action plus related options | Split button | Two unrelated actions fused together |
| Related actions shown together | Button group | Decorative segmented pills |

### Selection and input

| Need | Use | Avoid |
|---|---|---|
| Independent options, applied later | Checkbox | A switch that implies an immediate effect |
| Setting that takes effect immediately | Switch | A checkbox with a separate Apply step |
| One option from a small visible set | Radio buttons | A dropdown that hides the comparison |
| One option from a long set | Menu or exposed dropdown | A very long radio list |
| Filter a collection | Filter chips | Chips used as navigation |
| Enter text | Filled or outlined text field | An unlabeled custom box |
| Date or time | The Material date or time picker | A hand-built grid without semantics |

### Navigation and structure

| Need | Use | Avoid |
|---|---|---|
| 3 to 5 top-level destinations, compact window | Navigation bar | Tabs for app-wide navigation |
| Top-level destinations, wider window | Navigation rail (collapsed or expanded) | A stretched bottom bar |
| Peer views inside one destination | Tabs | Tabs for sequential steps |
| Dense, homogeneous content | List | A card per row |
| Discrete, heterogeneous items | Cards or a feed | A card around the whole feed |
| Selected item with detail | List-detail layout | Two unrelated columns |

The navigation drawer is no longer recommended in M3 Expressive; the expanded navigation rail replaces
it (`components/navigation.md`).

### Transient UI

- Snackbar: brief, non-blocking feedback with at most one action. Not for errors that need a decision.
- Tooltip: names or explains a control, especially for pointer and keyboard users.
- Menu: a compact set of contextual actions or choices.
- Bottom or side sheet: a supporting task that benefits from keeping the background context.
- Dialog: a short, focused decision or small form that blocks the flow.
- Full screen: a complex, multi-step, or content-heavy task.

Do not open a modal only to show decorative success copy.

## States

Design every state the workflow can reach: enabled, hovered, focused, pressed, dragged, selected,
disabled, loading, empty, error, offline, and success. Do not show state through color alone. Keep
layout stable while state changes. Interaction state details are in `interaction.md`.

## Loading

Loading is part of the screen's structure. Design the path from no data to useful data.

| Situation | Use | Avoid |
|---|---|---|
| No structure known yet | A small loading indicator or a structural skeleton | A large branded spinner with no context |
| Layout known, content arrives separately | Skeletons matching the final geometry | Gray bars that shift the layout later |
| One region refreshes | Progress inside that region | Blocking the whole screen |
| Long work with a known fraction | Determinate progress with status and cancel or retry | An indeterminate spinner |
| Sub-second action | An immediate state change or state layer | A full-screen loader |
| Refreshing existing content | Keep the content visible and show refresh status | Replacing content with a blank loader |
| Independent sections | Reveal each section when ready | Waiting for the slowest request |
| Safe, reversible mutation | Update immediately, mark pending, reconcile or undo | Faking success for irreversible work |

Material's wait-time guidance for progress and loading indicators is in `components/feedback.md`.

Skeleton rules: match final block sizes and counts, use shimmer only when it signals an active wait,
replace shimmer with a static placeholder under reduced motion, and do not show skeletons for content
that is already available or for waits so short they would only flash.

Async actions follow `idle, pending, success, recoverable error, blocked error`. While pending, show
what is working and prevent duplicate submits without trapping the user. Confirm success at the
affected content. Keep entered data on a recoverable error and offer retry, edit, or cancel. Separate
local pending work from server-confirmed data.

## Forms

- Group fields by the user's model or completion order, with one primary submit action and a clear
  cancel or back action.
- Every field has a visible label. Placeholder text is not a label.
- Keep helper and error text next to the field and associated with it for assistive technology.
- Choose the input type, keyboard, and autofill hints that match the data.
- Mark required and optional fields consistently, and explain the marker.
- Validate when it helps: local format errors inline, server rules on submit or blur. Avoid
  per-keystroke validation that makes the layout jump.
- Write errors that state what is wrong and how to fix it.
- Keep values after a failed validation, rotation, resize, or recoverable network error.
- Keep the submit button's label while pending; add a progress state instead of replacing it with an
  unexplained spinner.
- Constrain form width on large windows. Do not stretch fields across a desktop window.

## Generic AI-generated layouts

Patterns that make a Material screen look machine-made. Remove them unless the product asks for them:

- A dashboard of identical metric cards with icons in colored tiles and invented numbers.
- Hero copy, gradients, and large empty areas on an operational screen.
- Every section in its own outlined, rounded, elevated card.
- Decorative charts, badges, or status chips with no data behind them.
- Animations on hover or scroll that explain nothing.
- Abstract shapes behind text.

The opposite failure is a lifeless screen: one surface color, no type hierarchy, no emphasis on the
primary action, no state feedback. Material 3 Expressive expects visible hierarchy through shape,
color, type, containment, and motion. Aim for clear emphasis in the right places, not for the absence
of emphasis.

## Review questions

- Is the hierarchy clear in grayscale and without shadows?
- Can any card, outline, divider, or surface be removed without losing meaning?
- Is the primary action obvious without coloring every action?
- Does each component do the job it was designed for?
- Does the layout become more useful, not just wider, as space increases?
- Is expression concentrated on the moments that matter?
- Are theme roles, states, and type roles reused instead of one-off values?
