# :library:feature:diagnostics

## Responsibility and consumers

Owns usage/diagnostics preferences presentation, consent toggle cards, UsageAndDiagnosticsSettings,
UsageAndDiagnosticsRepository and its default implementation, and diagnosticsSettingsModule.
Settings embeds the list; onboarding also consumes the ViewModel and state. The facade assembles DI.

## Dependencies and flow

Depends on core common, DataStore, and UI, plus integration consent.
UsageAndDiagnosticsViewModel observes the repository and coordinates consent application through
ConsentRepository. The repository combines and updates the shared preference source; FirebaseController
receives diagnostic breadcrumbs. Defaults depend on the supplied build configuration.

## Contracts and boundaries

Public entry points include UsageAndDiagnosticsList, UsageAndDiagnosticsViewModel, its state/events,
UsageAndDiagnosticsRepository, and diagnosticsSettingsModule. The domain model describes preference
values; no pass-through use case is required. Core DataStore owns persistence, integration consent
owns SDK operations, and this feature owns localized UI resources.

## Validation and risks

DefaultUsageAndDiagnosticsRepositoryTest covers preference behavior. Changes affect both settings
and onboarding and must preserve consent defaults, persisted values, and SDK application ordering.
