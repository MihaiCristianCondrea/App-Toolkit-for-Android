# Material 3 on the web

Implementation guidance for HTML and CSS, web components, and framework UIs. Material 3 is a design
system; Android component APIs and behavior do not carry over to the web automatically. Availability
by feature is in `platforms.md`.

Official page: `documentation/develop/web.md`. Library docs: https://material-web.dev.

The `marketing-ui` skill builds prototypes on Material Web. Use this file for the Material rules and
`marketing-ui` for that kit's structure and workflow.

## Status

- `@material/web` (Material Web Components, built with Lit) is in maintenance mode "pending new
  maintainers". The latest release at the time of writing is 2.5.0 (July 2026). Existing apps keep
  working, but plan for no new features.
- M3 Expressive is not implemented on the web: no motion physics in components, no shape library or
  morph, no emphasized type scale, and none of the new Expressive components.
- The legacy `material-components-web` (MDC Web, M2) and its React wrappers are archived. Do not mix
  `@material/mdc-*` packages with `@material/web`.
- Angular Material's current theming is based on Material 3 and is the maintained option for Angular
  apps.

## Choose an implementation

| Situation | Direction |
|---|---|
| Existing `@material/web` app | Keep using its supported components and CSS tokens. |
| New framework-agnostic web-component app | Check component coverage, maintenance status, and project lifetime before adopting `@material/web`. |
| Angular app | Angular Material with its Material 3 theming. |
| React, Vue, Svelte | `@material/web` elements work as custom elements; otherwise semantic HTML styled with `--md-sys-*` tokens. Google publishes no official React Material 3 library. |
| Component missing from the library | The smallest accessible semantic implementation, styled with system tokens. |

Do not switch component libraries during an ordinary UI change. Do not call a third-party library
"official Material 3" unless its own documentation says so.

## Components in `@material/web`

Available: buttons (`md-filled-button`, `md-filled-tonal-button`, `md-elevated-button`,
`md-outlined-button`, `md-text-button`), icon buttons (`md-icon-button`, `md-filled-icon-button`,
`md-filled-tonal-icon-button`, `md-outlined-icon-button`), FAB (`md-fab`, including the extended form
through its `label` attribute, and `md-branded-fab`), checkbox, radio, switch, slider, chips
(`md-chip-set`, `md-assist-chip`, `md-filter-chip`, `md-input-chip`, `md-suggestion-chip`), text
fields (`md-filled-text-field`, `md-outlined-text-field`), select (`md-filled-select`,
`md-outlined-select`, `md-select-option`), menu (`md-menu`, `md-menu-item`, `md-sub-menu`), list
(`md-list`, `md-list-item`), dialog, divider, tabs (`md-tabs`, `md-primary-tab`, `md-secondary-tab`),
linear and circular progress, icon, elevation, ripple, and focus ring.

Not available as supported components: navigation bar, navigation rail, navigation drawer, top app
bar, bottom sheet, side sheet, snackbar, tooltip, card, carousel, badge, date and time pickers,
search, toolbar, button group, split button, segmented button, and loading indicator. Some exist under
`@material/web/labs`, which is unsupported and may change. Build these from semantic HTML and system
tokens, following the component's spec in `components/*.md`.

Check material-web.dev before using an element. Never invent an import path.

## Setup

```bash
npm install @material/web
```

Import only the components a page uses:

```javascript
import '@material/web/button/filled-button.js';
import '@material/web/button/outlined-button.js';
import '@material/web/textfield/outlined-text-field.js';
import '@material/web/icon/icon.js';
```

Importing `@material/web/all.js` pulls in every component. Use it only for quick prototypes.

```html
<link rel="stylesheet"
      href="https://fonts.googleapis.com/css2?family=Roboto+Flex:opsz,wght@8..144,400;8..144,500;8..144,700&display=swap">
<link rel="stylesheet"
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined">

<md-outlined-text-field label="Email" type="email" required></md-outlined-text-field>
<md-filled-button type="submit">Create account</md-filled-button>
```

Custom elements need JavaScript to render. Server-side rendering produces placeholder markup unless
the stack adds Lit SSR or hydration; plan for that before relying on it.

## Tokens and theming

Material Web reads system tokens from CSS custom properties. Generate the full role set from a source
color (`theming.md`) and set it at the root.

```css
:root {
  --md-sys-color-primary: #6750a4;
  --md-sys-color-on-primary: #ffffff;
  --md-sys-color-surface: #fef7ff;
  --md-sys-color-on-surface: #1d1b20;
  /* ...every role from the generated scheme */
  --md-ref-typeface-brand: 'Roboto Flex', sans-serif;
  --md-ref-typeface-plain: 'Roboto Flex', sans-serif;
}
```

These values illustrate the format only. Pasting a few variables does not make a complete theme.

- Put role tokens at the root; scope alternate themes at a deliberate container boundary.
- Use `prefers-color-scheme` only when it matches the product's theme model, and keep an explicit user
  choice when one exists.
- Use component tokens (`--md-filled-button-container-color`) for a deliberate exception, pointed at
  system roles.
- Do not style a component's shadow DOM or internal class names.
- Material Web does not expose every reference, shape, or motion token family. Define the Expressive
  shape steps and motion tokens yourself when a design uses them (`shape.md`, `tokens.md`).
- Use `var(--md-sys-color-*)` for color. A hard-coded hex value breaks dark theme, dynamic color, and
  contrast levels.
- Use `var(--md-sys-shape-corner-*)` for radii rather than raw `border-radius` values.

## Semantic HTML first

Use the element whose behavior matches the task, directly or inside a library component:

- `button` for actions and `a` for navigation.
- Labeled `input`, `textarea`, and `select` for data entry.
- Headings in order, lists, tables, and landmarks for their real structures.
- `dialog` or a proven accessible dialog for modal behavior.

Do not make a clickable `div` act as a button. A custom control must match the native control's role,
name, state, keyboard behavior, form participation, focus, and disabled behavior.

## Layout

Material Web components do not lay out the page. Layout guidance and CSS examples are in
`adaptive-layouts.md`. In short: CSS Grid for regions, Flexbox for groups, container queries for
components that adapt to their own width, fluid sizes between breakpoints, constrained prose and
forms on wide screens, and safe-area insets at display edges.

## Building missing components

Follow the component's spec for anatomy, sizes, color roles, and states, and keep the native semantics
underneath. A navigation rail example:

```html
<nav class="nav-rail" aria-label="Main">
  <a href="/inbox" class="nav-rail__item" aria-current="page">
    <span class="nav-rail__indicator"><md-icon>inbox</md-icon></span>
    <span class="nav-rail__label">Inbox</span>
  </a>
  <a href="/sent" class="nav-rail__item">
    <span class="nav-rail__indicator"><md-icon>send</md-icon></span>
    <span class="nav-rail__label">Sent</span>
  </a>
</nav>
```

```css
.nav-rail {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  inline-size: 96px; /* NavigationRailCollapsed ContainerWidth in component-tokens.md */
  padding-block: 12px;
  background: var(--md-sys-color-surface);
}

.nav-rail__item {
  display: grid;
  justify-items: center;
  gap: 4px;
  color: var(--md-sys-color-on-surface-variant);
  text-decoration: none;
}

.nav-rail__indicator {
  display: grid;
  place-items: center;
  inline-size: 56px;
  block-size: 32px;
  border-radius: var(--md-sys-shape-corner-full);
}

.nav-rail__item[aria-current='page'] .nav-rail__indicator {
  background: var(--md-sys-color-secondary-container);
  color: var(--md-sys-color-on-secondary-container);
}

.nav-rail__item:focus-visible {
  outline: 3px solid var(--md-sys-color-secondary);
  outline-offset: 2px;
}
```

Read container sizes, indicator sizes, and type styles from `components/navigation.md` and
`component-tokens.md` before finalizing. The rail width comes from the tokens; the gap, indicator
size, and focus ring in this example are illustrative and must be checked against those files.

Links with `aria-current="page"` are the correct pattern for navigation. Do not use `role="tab"` for
page navigation.

## Interaction and motion

- Keep native browser behavior for links, forms, text selection, and context menus.
- Provide visible hover and focus states without moving targets under the pointer.
- Use `:focus-visible`; never remove focus outlines without an equal replacement. Focus must be
  distinct from a permanent border.
- Keep transitions state-driven and interruptible. Material Web does not implement springs; use the
  curve and duration fallbacks in `tokens.md`, or a spring library for interruptible motion.
- Honor `prefers-reduced-motion` by removing nonessential movement.
- Do not recreate ripple or physics by hand when the component library already provides feedback.

## Loading and forms

- Use skeletons only when the final structure is known and the wait is long enough to matter.
- Keep available content visible during refresh and mark the region busy (`aria-busy`).
- Use determinate progress for uploads, imports, and other measurable work.
- Keep async errors actionable (retry, edit, cancel); never discard form data behind a generic toast.
- Choose `autocomplete`, `inputmode`, `type`, and `enterkeyhint` for each field, and associate
  descriptions and errors with `aria-describedby`.
- Keep the submit action identifiable while pending and prevent duplicate submits.

## Visual restraint

`design-quality.md` applies on the web too. Common web-specific failures: the whole app inside a large
floating rounded shell, a border around every section, shadows on stationary content, glass and
`backdrop-filter` panels, every control shaped as a pill, and decorative dashboard filler.

## Accessibility

- Target WCAG 2.2 AA unless the product requires more.
- Keep heading, landmark, and source order logical.
- Name icon-only controls; hide decorative icons from assistive technology.
- Test at 200% zoom, with keyboard only, and with a screen reader. Automated scans supplement these
  checks; they do not replace them.

## Web verification

- Run the project's typecheck, build, lint, and relevant tests.
- Inspect the reported viewport and the breakpoints around it.
- Check keyboard operation and focus restoration after dialogs and menus.
- Check light, dark, and contrast variants the product supports.
- Check long content, localization, validation errors, loading, empty, and offline states.
- Check the browser console for custom-element registration and import errors.

Compilation cannot prove layout, focus, clipping, or hierarchy. Use browser screenshots and
interaction checks when available.
