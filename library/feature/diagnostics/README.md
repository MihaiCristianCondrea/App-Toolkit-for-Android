# :library:feature:diagnostics

## Responsibility and consumers

Owns usage/diagnostics preferences presentation, the privacy choices dialog, UsageAndDiagnosticsSettings,
UsageAndDiagnosticsRepository and its default implementation, and diagnosticsSettingsModule.
Settings embeds the list; onboarding also consumes the ViewModel, state and dialog. The main toolkit module assembles DI.

## Dependencies and flow

Depends on core common, DataStore, and UI, plus integration consent.
UsageAndDiagnosticsViewModel observes the repository and coordinates consent application through
ConsentRepository. The repository combines and updates the shared preference source; FirebaseController
receives diagnostic breadcrumbs. Defaults depend on the supplied build configuration.

## Screen shape

The settings screen is the ads screen's layout: one switch over a single preference that opens the
consent surface belonging to it. There it is the AdMob consent form; here it is `FirebaseConsentDialog`,
the privacy choices dialog the onboarding flow shows, so a person meets the same surface in both
places and the two settings screens asking the same kind of question look alike.

That dialog lives here rather than in onboarding, because it reads and writes this feature's state;
onboarding depends on this module, not the other way round. Its `privacy_choices_*` strings moved
with it. The four granular consents were also drawn on the settings screen as an expandable block of
switch cards — a second, plainer copy of what the dialog's Details tab already explains — and that
block, `ConsentToggleCard`, `ConsentSectionHeader` and `ExpandableConsentSectionHeader` are gone.

`AllowAllConsent` and `AllowEssentialConsent` are events rather than five calls at each call site:
what "everything" and "essentials" cover is a product rule, and both screens showing the dialog have
to agree on it. Both also turn reporting on, since a person choosing what to share has said they
are sharing something.

## Contracts and boundaries

Public entry points include UsageAndDiagnosticsList, FirebaseConsentDialog, UsageAndDiagnosticsViewModel,
its state/events, UsageAndDiagnosticsRepository, and diagnosticsSettingsModule. The domain model describes preference
values; no pass-through use case is required. Core DataStore owns persistence, integration consent
owns SDK operations, and this feature owns localized UI resources.

## Validation and risks

DefaultUsageAndDiagnosticsRepositoryTest covers preference behavior. Changes affect both settings
and onboarding and must preserve consent defaults, persisted values, and SDK application ordering.
