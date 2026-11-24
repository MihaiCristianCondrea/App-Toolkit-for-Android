# Sources and authority

Which source wins, where to find it, and which facts go stale.

## Authority order

1. Material specification: the local snapshot in `documentation/` (captured 2026-05-21 from
   m3.material.io). It is the authority for design intent, anatomy, measurements, color roles, and
   do's and don'ts. Use it offline; do not fetch m3.material.io, which is a single-page app that
   returns an empty shell to plain HTTP requests.
2. Installed library APIs: the project's actual dependency versions and their API reference or
   sources in the Gradle cache or `node_modules`. This decides whether code can use an API.
3. AndroidX generated tokens: `component-tokens.md` and the token tables in `tokens.md`, extracted
   from `androidx.compose.material3.tokens`. These are what Compose ships, including values the spec
   publishes only inside images.
4. Platform documentation: Android Developers for adaptive layouts, insets, accessibility, and
   Wear; material-web.dev for Material Web.
5. Curated references in this skill: summaries and extracts of the above, plus labelled heuristics.
6. Secondary sources: blog posts, demos, screenshots, community libraries, generated examples.

When sources disagree, the higher item wins. Record the disagreement in the handoff instead of
silently picking one. The snapshot can lag the live site; for platform API availability, the
installed library and its current release notes win over the snapshot.

## Using the local documentation

- Index: `documentation/README.md` lists every page with links.
- Pages follow the site routes: `documentation/components/<component>/{overview,specs,guidelines,accessibility}.md`,
  `documentation/styles/<style>/...`, `documentation/foundations/<topic>/...`,
  `documentation/develop/...`, `documentation/blog/<slug>.md`.
- Each page has frontmatter with `source_url`, `lastmod`, and `snapshot_at`. Quote `source_url` when
  citing a rule.
- Images inside pages point to remote URLs. The local, captioned copies are in `visuals/`; see
  `visuals.md`.
- Search before reading: `grep -ril "split button" documentation/components` finds the pages; then
  read the one section you need.

## Library status (checked 2026-11-20)

| Library | Latest | Notes |
|---|---|---|
| `androidx.compose.material3:material3` | 1.5.0 stable (2026-11-18); 1.6.0-alpha01 | 1.5.0 graduated most Expressive components and Expressive theming. Loading indicators and `MaterialShapes` remain experimental. |
| `androidx.compose.material3.adaptive:*` | 1.3.0 stable (2026-08-12); 1.4.0-alpha03 | `currentWindowAdaptiveInfo` was deprecated in favor of a V2 in 1.3.0-alpha10. |
| `com.google.android.material:material` | 1.14.0 | Maintenance mode; no more planned feature releases for Views. 1.14 requires `minSdk` 23. |
| `@material/web` | 2.5.0 (2026-07-14) | Maintenance mode pending new maintainers. |
| `androidx.wear.compose:*` | 1.7.2 stable (2026-11-18) on the Wear Compose release line | Check the Wear Compose release page; the dedicated Wear Material 3 page lagged behind. |

These change. Before adding or upgrading a dependency, read the current release page and the
project's compatibility constraints. Do not hard-code these versions into an app from this table.

## Links

### Material

- Material Design 3: https://m3.material.io/
- Components: https://m3.material.io/components
- Building with M3 Expressive: https://m3.material.io/blog/building-with-m3-expressive
- Material is Compose-first: https://m3.material.io/blog/material-is-compose-first
- What's new at I/O 2026: https://m3.material.io/blog/whats-new-at-io26

### Android

- Compose Material 3 release notes: https://developer.android.com/jetpack/androidx/releases/compose-material3
- Compose Material 3 API: https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
- Material 3 in Compose: https://developer.android.com/develop/ui/compose/designsystems/material3
- Material 2 to Material 3 migration: https://developer.android.com/develop/ui/compose/designsystems/material2-material3
- Compose Material 3 Adaptive release notes: https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive
- Window size classes: https://developer.android.com/develop/adaptive-apps/guides/use-window-size-classes
- Canonical layouts: https://developer.android.com/develop/adaptive-apps/guides/canonical-layouts
- Adaptive navigation: https://developer.android.com/develop/adaptive-apps/guides/build-adaptive-navigation
- Edge-to-edge: https://developer.android.com/design/ui/mobile/guides/layout-and-content/edge-to-edge
- Android 16 behavior changes: https://developer.android.com/about/versions/16/behavior-changes-16
- Material components and containment: https://developer.android.com/design/ui/mobile/guides/components/material-overview
- Compose accessibility: https://developer.android.com/develop/ui/compose/accessibility
- Accessibility testing in Compose: https://developer.android.com/develop/ui/compose/accessibility/testing
- Wear Material 3 design: https://developer.android.com/design/ui/wear/guides/get-started/design-language
- Wear Compose releases: https://developer.android.com/jetpack/androidx/releases/wear-compose
- Glance Material 3: https://developer.android.com/reference/kotlin/androidx/glance/material3/package-summary
- MDC-Android repository: https://github.com/material-components/material-components-android
- Compose samples: https://github.com/android/compose-samples

### Web

- Material Web repository: https://github.com/material-components/material-web
- Material Web docs: https://material-web.dev/
- Material Web theming: https://material-web.dev/theming/material-theming/
- Angular Material: https://material.angular.dev/
- WCAG 2.2: https://www.w3.org/TR/WCAG22/
- ARIA Authoring Practices: https://www.w3.org/WAI/ARIA/apg/

### Tools

- Material Theme Builder: https://material-foundation.github.io/material-theme-builder/
- Material Color Utilities: https://github.com/material-foundation/material-color-utilities
- Material Symbols: https://fonts.google.com/icons

## Facts that go stale

Re-verify these before relying on them:

- Library versions and which APIs are experimental.
- Maintenance status of MDC-Android and Material Web.
- Component availability per platform.
- Android target-SDK behavior (edge-to-edge enforcement, predictive back).
- Spring and token values after a Compose token generator update. Regenerate
  `component-tokens.md` with `scripts/refresh-component-tokens.js`.

Record the access date when a decision depends on experimental or maintenance status.
