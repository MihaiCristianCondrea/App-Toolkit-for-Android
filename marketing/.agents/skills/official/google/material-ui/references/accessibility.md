# Accessibility and interaction states

What to check so a Material UI works for everyone and in every state. Material's own extracts are in
`interaction.md` (states, gestures, inputs, selection, usability, building for all) and
`foundations.md` (the "Designing" accessibility pages). Per-component accessibility rules are in each
`components/*.md` file and in `documentation/components/<component>/accessibility.md`.

Official pages: `documentation/foundations/overview/assistive-technology.md`,
`documentation/foundations/designing/` (overview, color contrast, structure, flow, elements),
`documentation/foundations/interaction/states/`, `documentation/foundations/writing/text-resizing.md`.

## Targets

- Android: interactive elements need a touch target of at least 48 by 48dp. The visible icon can be
  smaller. Material components provide this by default; custom components must add it.
- Web: meet WCAG 2.2 target size (24 by 24 CSS px minimum, with spacing exceptions) and prefer
  Material sizes for touch-first layouts.
- Keep enough space between opposite or destructive actions that a slip does not trigger the wrong
  one.
- Expanded hit areas of neighbors must not overlap ambiguously.

## Semantics and labels

- Use the Material or native control whose role matches the job, so role, state, and actions are
  exposed without extra code.
- Give icon-only controls a localized accessible name.
- Do not repeat adjacent visible text in an image description.
- Mark decorative images and icons as decorative.
- Merge descendant semantics only when the merged reading unit is better, for example a list item read
  as one entry.
- Expose selection, toggle state, progress values, errors, headings, and collection structure.
- Announce important asynchronous results without moving focus unexpectedly.
- Associate error text with its field and say how to recover.

## Focus and keyboard

- Focus order follows reading and visual order.
- Focus is visible on every surface, in every theme and contrast level, and does not shift layout.
- A permanent border is not a focus indicator. Focus needs its own change.
- Modal dialogs and sheets keep focus inside while open and return it to the trigger on close.
- Support the platform keys for each component: Tab and Shift+Tab, Enter and Space, Escape, and arrow
  keys where the pattern uses them.
- On Android large screens, support keyboard, mouse, trackpad, and stylus where the workflow needs
  them. Tooltips help pointer and keyboard users with unfamiliar icon-only actions.
- Every drag interaction that matters has a non-drag alternative.

## Text size and zoom

- Use scalable units: sp on Android, rem on the web.
- At large font scales, reflow instead of clipping. Let buttons, list items, app bars, and navigation
  labels wrap or grow where the component allows.
- Do not put essential text in images.
- Keep reading lines constrained on wide windows instead of shrinking text.
- Test 200% text size or browser zoom.

## Contrast and non-color cues

- Text: 4.5:1 for small text and 3:1 for large text. Material role pairs are designed to meet these;
  check custom pairs and anything placed over images.
- Non-text: a boundary or icon required to understand a control needs 3:1 against adjacent colors.
- Focus, selection, error, disabled, and pressed states need more than color to be perceived.
- Disabled controls stay understandable. Do not fade them until the label disappears.
- Validate rendered pairs in light, dark, dynamic color, and the medium and high contrast levels. A
  token name does not prove contrast.
- On the web, a strong author-supplied focus indicator can follow the WCAG 2.2 AAA focus appearance
  criterion: an area at least as large as a 2 CSS px perimeter with 3:1 change. AA requires only that
  focus is visible.

## Motion and timing

- Respect reduced motion and animator duration settings. On Android, the system "Remove animations"
  setting; on the web, `prefers-reduced-motion`.
- Do not make completion depend on watching an animation.
- Avoid flashing and rapid repeated movement.
- Give timed messages enough time to read and act, and keep critical information persistent.
- Expose progress to assistive technology and give a determinate value when it is known.

## Interaction states

Material states and their state-layer opacities (`interaction.md`, `component-tokens.md`):

| State | State layer opacity |
|---|---|
| Hovered | 8% |
| Focused | 10% |
| Pressed | 10% |
| Dragged | 16% |

Source: `documentation/foundations/interaction/states/state-layers.md`. Disabled label and icon
opacity is 0.38 in the AndroidX tokens; disabled container opacity is 0.1 for most buttons and 0.12
for some other components. Read the exact component row in `component-tokens.md` before quoting it.

Selected is a separate state with its own treatment per component (for example a filled indicator or
a container color change), and it combines with the other states.

## State and content matrix

Check only the states the feature can reach, but do not skip important ones.

| Dimension | Cases |
|---|---|
| Content | Short, typical, longest localized, empty, dense. |
| Data | Loading, partial, success, stale or offline, error. |
| Interaction | Enabled, hovered, focused, pressed, dragged, selected, disabled. |
| Theme | Light, dark, dynamic or custom color, medium and high contrast. |
| Text | Default size, largest font scale, display zoom. |
| Window | Compact portrait, compact height, medium, expanded and larger, resizing. |
| System UI | Gesture navigation, display cutout, IME open, edge-to-edge. |
| Input | Touch, keyboard, pointer, screen reader; stylus or rotary where relevant. |

## Loading and forms

- Expose loading, progress, success, and failure through semantics or live regions.
- Announce meaningful transitions, not every skeleton row or progress tick.
- Keep stale content available during refresh when it is still useful.
- Keep field values and labels after validation or network failure.
- For multi-field submit errors, summarize them and move focus to the first actionable error after
  submit. Do not move focus during passive validation.
- Keep the IME action, focus order, and submit action consistent with the form's completion order.
- Make sure the keyboard does not cover the focused field or the submit action.

## Verification by change type

| Change | Check |
|---|---|
| Theme or tokens | Representative components in light, dark, dynamic, contrast levels, and all states. |
| Component | Semantics, target size, every supported state, long labels. |
| Layout | The reported window plus sizes around each affected breakpoint. |
| Navigation or panes | Resize at runtime; check selection, back, deep links, and focus. |
| Motion | Interrupted animations, reduced motion, correct end state. |
| Custom control | Role, input, semantics, and state tests. If the tests grow large, replace it with a standard component. |

Compilation and unit tests cannot prove spacing, clipping, contrast, or overlap. Inspect rendered UI
with TalkBack or a screen reader where accessibility is part of the change. Android tooling: the
Compose semantics tree in tests, Accessibility Scanner, and TalkBack. Web: keyboard-only passes, a
screen reader, and an automated scan as a supplement.
