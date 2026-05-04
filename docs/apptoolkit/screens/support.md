# Support (AppToolkit)

The **Support** feature provides a reusable support hub where users can contact the team, rate the
app,
and access purchase/donation entry points when billing is enabled.

---

## Responsibilities

- Expose a dedicated support UI (`SupportScreen`) that can be hosted by `SupportActivity` or
  embedded
  in a Navigation 3 app shell.
- Centralize support-oriented actions (contact links, review/rating entry points, optional billing).
- Keep host-app wiring simple with one screen-level integration point.

---

## Architecture

### Domain

- Defines support interactions and contracts around purchase/contact operations.
- Keeps feature behavior independent from concrete billing/UI implementations.

### UI

- `SupportActivity` is the standalone entry activity for the support surface.
- `SupportScreen(isEmbedded = false)` renders support actions with its own top app bar.
- `SupportScreen(isEmbedded = true)` renders the same content without its own top app bar for
  host-provided navigation chrome.

### Billing

- Integrates Play Billing helper abstractions when purchase/donation actions are enabled.

### Utilities

- Shared utility helpers support link opening, intent safety, and common support actions.

---

## Host app integration

Open the support feature as a standalone screen from any context:

```kotlin
startActivity(Intent(context, SupportActivity::class.java))
```

Embed it in a shared Navigation 3 shell by adding the library builders:

```kotlin
val builders = appToolkitNavigationEntryBuilders()
```

Typical usage:

1. Add a “Support” entry in settings or an app bar action menu.
2. Navigate to the shared `SupportRoute` when the host shell embeds AppToolkit destinations, or
   launch
   `SupportActivity` for standalone flows.
3. Let users complete support or donation actions and return to previous flow.

---

## Implementation notes

- Keep external actions (browser/store/email) behind safe intent wrappers.
- Treat billing as optional and guard UI affordances when unavailable.
- Keep support copy and action labels localized via string resources.
- Avoid embedding business logic in composables; delegate orchestration to ViewModel/domain.

---

## Related docs

- `docs/apptoolkit/screens/help.md` – FAQ-oriented companion screen.
- `docs/apptoolkit/screens/settings/settings.md` – common host entry points to Support.
- `docs/apptoolkit/ads/native-ads.md` – optional monetization guidance if combined with support
  surfaces.
