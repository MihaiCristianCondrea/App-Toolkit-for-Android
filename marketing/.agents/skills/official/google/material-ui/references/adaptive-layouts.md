# Adaptive layouts

How to build layouts that adapt across window sizes, postures, and input types. This file covers
implementation. The official values (breakpoints, margins, spacers, pane widths, canonical layouts,
RTL) are in `layout.md`; navigation component rules are in `components/navigation.md`. For Android
adaptive work in depth, also read `.agents/skills/official/google/android/jetpack-compose/adaptive`
at the repository root.

Official pages: `documentation/foundations/layout/` (layout overview, breakpoints, scaffold, grids and
spacing, canonical examples, bidirectionality). Full-screen reference images:
`grep -iE "^foundations_layout_(canonical-examples|layout-overview)" visuals/INDEX.tsv`.

## Decide from the window, not the device

Layout decisions use the app window's size class. A phone in landscape can be expanded, and a tablet
in split screen can be compact.

| Width class | Window width |
|---|---|
| Compact | under 600dp |
| Medium | 600 to 839dp |
| Expanded | 840 to 1199dp |
| Large | 1200 to 1599dp |
| Extra-large | 1600dp and up |

Height matters too: compact height (under 480dp) affects landscape phones, tabletop posture, sheets,
dialogs, and IME-open states. Keep layouts fluid inside each class instead of designing one fixed
width per class.

## Adaptation strategies

Material names three pane strategies (show and hide, levitate, reflow) and three component
strategies (resizing, showing and hiding, presentation changes). In practice:

- Reflow: rearrange rows, columns, or grid tracks with the same content.
- Reveal: show supporting content or controls when space allows.
- Defer: move non-essential content into a sheet or a separate pane on compact windows.
- Substitute: swap the navigation component, or turn a modal into an inline pane.
- Resize: adjust pane proportions and keep readable line lengths.

A layout that only grows its padding, stretches fields, or stacks every desktop column into one long
mobile page has not adapted.

## Panes per window class

From `layout.md`: compact and medium use one pane (two only for low-density content such as settings
at medium), expanded and large usually use two, and extra-large can use three, with a standard side
sheet as the third. Never use more than three panes.

Canonical layouts:

- Feed: many peer items. Grow from one column to more columns; keep a meaningful minimum item width.
- List-detail: a selected item owns a detail view. One pane at a time on compact; side by side when
  two panes fit. Preserve the selection across resizes.
- Supporting pane: a dominant primary task with related tools or content. On compact, put the support
  below, in a sheet, or on its own pane.

Use a canonical layout only when the content relationship matches it.

## Navigation per window class

| Width class | Navigation |
|---|---|
| Compact | Navigation bar, or a modal expanded navigation rail for more than five destinations. |
| Medium | Navigation bar with horizontal items, or a navigation rail. Choose by whether vertical or horizontal space matters more. |
| Expanded and wider | Navigation rail, collapsed or expanded. Not a navigation bar. |

Rules from the navigation pages:

- Keep destinations, order, labels, and badges the same as the component changes.
- Never show a navigation bar and a navigation rail at the same time.
- The navigation drawer is no longer recommended; the expanded navigation rail replaces it.
- Tabs organize content inside a page. They are not app-level navigation.

## Body width and alignment

- Margins are 16dp on compact and 24dp from medium up; the spacer between panes is 24dp
  (`layout.md`).
- Constrain prose, forms, and single-column settings on wide windows. Use extra width for more panes
  or more columns, not wider text.
- Keep source order logical when regions move.
- Spacing uses 4dp increments, with an 8dp rhythm for most layout spacing. Keep spacing in shared
  tokens rather than scattering literal values.

## Foldables and postures

- Treat a hinge or fold reported by the platform as a separating feature. Do not place text,
  controls, or a focal image across it.
- Tabletop posture (horizontal fold, half open): content on the top half, controls on the bottom.
- Book posture (vertical fold, half open): list-detail maps naturally, one pane per side.
- Do not lock orientation or aspect ratio as a layout shortcut.
- Preserve input, scroll position, selection, and navigation state through resizes and posture
  changes.

## Input

Large windows often have a mouse, trackpad, or keyboard.

- Provide hover states and right-click menus where the platform expects them, without making hover
  the only way to find an action.
- Keep 48dp touch targets on large screens; touch still happens there.
- Support keyboard shortcuts on desktop-class windows.
- Drag and drop is more natural on large screens; always provide a non-drag alternative.

## Compose implementation

Artifacts (verify coordinates and versions against the project and `sources.md`):

| Need | API |
|---|---|
| Window size class | `currentWindowAdaptiveInfoV2()` in `material3-adaptive` 1.3 or later. The older `currentWindowAdaptiveInfo()` is deprecated and only reports large and extra-large widths when `supportLargeAndXLargeWidth = true`. |
| Navigation that switches between bar and rail | `NavigationSuiteScaffold` (`material3-adaptive-navigation-suite`). |
| List-detail panes | `ListDetailPaneScaffold`, `NavigableListDetailPaneScaffold` (`adaptive-layout`, `adaptive-navigation`). |
| Supporting pane | `SupportingPaneScaffold`, `NavigableSupportingPaneScaffold`. |
| Navigation 3 integration | `adaptive-navigation3`, only when the app uses Navigation 3. |
| Fold and posture | `currentWindowAdaptiveInfoV2().windowPosture`, or Jetpack WindowManager `FoldingFeature`. |

```kotlin
val adaptiveInfo = currentWindowAdaptiveInfoV2()

NavigationSuiteScaffold(
    navigationSuiteItems = {
        destinations.forEach { destination ->
            item(
                selected = destination == current,
                onClick = { onNavigate(destination) },
                icon = { Icon(destination.icon, contentDescription = null) },
                label = { Text(stringResource(destination.label)) },
            )
        }
    },
) {
    // Destination content. Read adaptiveInfo.windowSizeClass for pane decisions.
}
```

- Do not read raw display metrics or `Configuration.screenWidthDp` for layout decisions when the
  adaptive APIs are available.
- On compact windows, pane-local back returns from detail to list before leaving the destination.
- On wide windows, do not leave an empty detail pane; show a placeholder, the first item, or useful
  supporting content.
- Edge-to-edge and insets are covered in `android.md`.

## Web implementation

Use CSS Grid for regions and Flexbox for one-dimensional groups. Use viewport media queries for page
layout and container queries for components that adapt to their own width.

```css
.app {
  display: grid;
  grid-template-columns: 1fr;
  min-height: 100dvh;
  background: var(--md-sys-color-surface);
  color: var(--md-sys-color-on-surface);
}

@media (min-width: 600px) {
  .app { grid-template-columns: auto 1fr; } /* rail + content */
}

.content {
  padding-inline: 16px;
}

@media (min-width: 600px) {
  .content { padding-inline: 24px; }
}

.reading-column {
  max-inline-size: 70ch; /* constrain prose on wide windows */
}

.card-host { container-type: inline-size; }

@container (min-width: 400px) {
  .media-card { grid-template-columns: 160px 1fr; }
}
```

- Material's dp breakpoints map one to one to CSS px. Use them as defaults, and adjust where content
  or input requires it.
- Respect safe-area insets (`env(safe-area-inset-*)`) where full-bleed content reaches display edges.
- For foldables, the CSS Viewport Segments API (`horizontal-viewport-segments`,
  `vertical-viewport-segments`, `env(viewport-segment-*)`) reports the fold. Check browser support
  before relying on it.
- Avoid fixed heights on text-bearing regions.

## Adaptive review

- The layout is correct at the boundary sizes of each affected class (599, 600, 839, 840, 1199, 1200,
  1599, 1600).
- Resizing at runtime keeps selection, scroll, input, and back behavior.
- Navigation is one component at a time and keeps the same destinations.
- Wide windows add panes or columns instead of stretching content.
- Nothing interactive sits under a hinge, cutout, or system gesture area.
- Compact height (landscape phone, IME open) still shows the primary content and action.
