---
name: glance-widget
description: >
  Design, implement, review, debug, and test Android home-screen app widgets
  with Jetpack Glance following Google's official Android guidance. Use for
  GlanceAppWidget, GlanceAppWidgetReceiver, widget UI, sizing, state and
  updates, actions, configuration, previews, pinning, error handling, testing,
  metrics, themes, or RemoteViews interoperability.
metadata:
  author: Google LLC
  last-updated: '2026-08-13'
  keywords:
    - glance
    - glance app widget
    - app widget
    - home screen widget
    - glanceappwidget
    - glanceappwidgetreceiver
    - jetpack
    - compose
    - widget
    - remoteviews
    - widget configuration
    - widget preview
    - widget actions
    - widget testing
---

# Android Glance Widget

Follow Google's official Jetpack Glance guidance.

The `references/` directory contains local copies of the official Google
documentation used by this skill.

Treat those files as read-only reference material:

- Do not edit, rewrite, summarize into, reformat, or otherwise modify them.
- Do not fetch the same documentation again when the local reference already exists.
- Read only the reference files relevant to the current task.
- Prefer the local references over general architectural assumptions or memory.
- If the local documentation does not cover a question, consult the official
  Android documentation only when additional verification is actually needed.

## Core rules

- Use Glance APIs for widget UI. Do not assume regular Jetpack Compose UI
  elements are directly interoperable with Glance.
- Keep `GlanceAppWidget` stateless and passive; keep durable application state
  in the application's source of truth and use Glance state only for
  widget-specific state when appropriate.
- Update widgets intentionally when data/state changes. Use persistent
  scheduling only when the update must outlive the current process/lifecycle.
- Use Glance action APIs for widget interaction.
- Respect AppWidget/RemoteViews limitations even when the API looks Compose-like.
- Prefer the smallest implementation supported by the official guidance.

## Reference routing

Read:

- `references/create-app-widget.md` for initial widget setup, receiver,
  manifest/provider metadata, sizing modes, and basic creation.
- `references/enhance.md` for widget picker name/description and optional
  experience improvements.
- `references/configuration.md` for configuration activities and reconfiguration.
- `references/generated-previews.md` for Android 15+ generated previews and
  preview fallbacks.
- `references/pin-in-app.md` for requesting widget pinning from inside the app.
- `references/error-handling.md` for composition errors, fallback layouts, and
  custom error handling.
- `references/testing.md` for Glance unit testing, matchers, context, and size.
- `references/user-interaction.md` for clicks, activities, services,
  broadcasts, callbacks, and action parameters.
- `references/metrics.md` for widget interaction/event metrics.
- `references/glance-app-widget.md` for widget state, updates, update timing,
  `update`, `updateAll`, `updateIf`, and background update work.
- `references/build-ui.md` for layouts, components, responsive sizing, and
  Glance UI construction.
- `references/theme.md` for `GlanceTheme`, colors, Material interop, and
  widget styling.
- `references/interoperability.md` for `AndroidRemoteViews` and XML/
  `RemoteViews` interoperability.
- `references/additional-resources.md` for official samples, demos, design
  resources, and API references.

## Workflow

1. Inspect the existing widget, receiver, provider XML, manifest registration,
   state/update path, and relevant app data source.
2. Identify the Glance topic involved.
3. Read the matching official reference before changing behavior.
4. Follow existing project conventions unless they conflict with the official
   Glance contract.
5. Make the smallest complete change.
6. Add or update tests when widget behavior being changed is testable with the
   Glance testing APIs.
7. Do not introduce regular Compose UI APIs into Glance code merely because
   both use `@Composable`.

## Review

Check for:

- regular Compose UI APIs mixed into Glance without supported interoperability;
- incorrect receiver/provider/manifest setup;
- in-memory widget state relied on as durable state;
- missing or excessive widget updates;
- unsupported interaction patterns;
- sizing/layout assumptions that ignore widget host sizes;
- configuration, preview, pinning, or error behavior that contradicts the
  corresponding official guide;
- missing relevant Glance tests.

When uncertain, read the relevant official reference rather than guessing.

# Official source index

- `create-app-widget.md` → https://developer.android.com/develop/ui/compose/glance/create-app-widget.md.txt
- `enhance.md` → https://developer.android.com/develop/ui/compose/glance/enhance.md.txt
- `configuration.md` → https://developer.android.com/develop/ui/compose/glance/configuration.md.txt
- `generated-previews.md` → https://developer.android.com/develop/ui/compose/glance/generated-previews.md.txt
- `pin-in-app.md` → https://developer.android.com/develop/ui/compose/glance/pin-in-app.md.txt
- `error-handling.md` → https://developer.android.com/develop/ui/compose/glance/error-handling.md.txt
- `testing.md` → https://developer.android.com/develop/ui/compose/glance/testing.md.txt
- `user-interaction.md` → https://developer.android.com/develop/ui/compose/glance/user-interaction.md.txt
- `metrics.md` → https://developer.android.com/develop/ui/compose/glance/metrics.md.txt
- `glance-app-widget.md` → https://developer.android.com/develop/ui/compose/glance/glance-app-widget.md.txt
- `build-ui.md` → https://developer.android.com/develop/ui/compose/glance/build-ui.md.txt
- `theme.md` → https://developer.android.com/develop/ui/compose/glance/theme.md.txt
- `interoperability.md` → https://developer.android.com/develop/ui/compose/glance/interoperability.md.txt
- `additional-resources.md` → https://developer.android.com/develop/ui/compose/glance/additional-resources.md.txt

