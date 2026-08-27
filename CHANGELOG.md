# Changelog

# Unreleased

## Library Changes

### Added

- Added support for a **Reduce Ads** preference that can suppress App Open ads while preserving other supported ad formats.

### Changed

- Expanded automatic App Toolkit configuration so host applications require less manual setup.
- Refined resource and data handling responsibilities inside the toolkit.
- Continued preparing the library for the next publishing release.

### Improved

- Improved localization handling across shared toolkit resources.
- Improved navigation integration and internal routing behavior.
- Improved documentation and KDoc coverage across library modules.
- Improved the Support screen loading experience.

### Fixed

- Fixed navigation inconsistencies affecting toolkit-hosted destinations.
- Fixed translation and resource inconsistencies discovered across supported locales.

## Sample App Changes

### Improved

- Improved translations and wording across multiple screens.

### Fixed

- Fixed incorrect loading-state behavior on the Support screen.
- Fixed several navigation issues in the Sample App.

---

# Version 26.08.9

`1370009`  
**Released August 24, 2026**

A reliability-focused release with major improvements to startup, onboarding, Quick Tools, and the way App Toolkit communicates one-time UI actions.

## Library Changes

### Changed

- Reworked one-time ViewModel actions so startup and onboarding events are delivered reliably.
- Refined onboarding ownership so completion, consent, and navigation actions no longer compete between multiple collectors.
- Improved repository boundaries around theme, display, and onboarding preferences.
- Pinned JitPack builds to a Temurin JDK for more reliable library publishing.

### Improved

- Improved startup handling when consent, permissions, or initialization work does not complete normally.
- Added explicit onboarding failure feedback instead of leaving users on an apparently unresponsive screen.
- Improved plural and localization handling across supported languages.
- Improved theme onboarding integration for host applications.

### Fixed

- Fixed one-time UI actions being lost when emitted before a screen began collecting them.
- Fixed startup becoming permanently stuck in a loading state.
- Fixed startup failing to complete when optional initialization work throws an error.
- Fixed repeated consent requests during activity resume.
- Fixed repeated permission requests during startup.
- Fixed onboarding completion sometimes requiring two taps.
- Fixed missing theme onboarding dependency injection bindings.
- Fixed several onboarding translation issues.

## Sample App Changes

### Changed

- Reworked Quick Tools architecture to more closely follow the project's current architecture conventions.
- Quick Tools categories now remember whether they were expanded or collapsed.

### Improved

- Improved the Components screen layout and presentation.
- Improved translations after the Sample App modularization.
- Improved startup and onboarding behavior using the updated toolkit APIs.

### Fixed

- Fixed the startup dialog flow.
- Fixed several Sample App onboarding and navigation edge cases.

---

# Version 26.08.8

`1370008`  
**Released August 20, 2026**

A major structural release. App Toolkit became substantially more modular, easier to integrate, and safer for host applications while the Sample App was rebuilt as a clearer reference implementation.

## Library Changes

### Added

- Added the new **Dim Flash** Quick Tool.
- Added automated dependency-graph verification for App Toolkit integrations.
- Added manifest contract tests to prevent library modules from accidentally overriding host application configuration.
- Added a unified `appToolkitModules(...)` entry point for loading the standard App Toolkit dependency graph.

### Changed

- Split App Toolkit into more focused feature, integration, and core modules.
- Split the shared preference layer into dedicated data sources for:
    - themes
    - display settings
    - onboarding
    - consent
    - ads
    - favorites
    - reviews
    - changelog state
    - general application state
- Reorganized navigation contracts into a dedicated navigation module.
- Reworked repository and data-layer boundaries to reduce unnecessary abstraction.
- Removed battery percentage, battery temperature, and battery Quick Settings functionality from App Toolkit.
- Removed duplicated DataStore ownership.
- Updated publishing so every required App Toolkit module is distributed correctly.

### Improved

- Reduced unnecessary Compose recompositions across shared UI components.
- Improved scroll-related performance in the main application shell.
- Improved shimmer and loading animations.
- Improved shared modifier performance.
- Improved localization and plural handling across all supported languages.
- Improved compact native ad sizing.
- Improved startup dialog sizing for different content lengths and screen sizes.
- Improved App Toolkit module documentation and integration guidance.
- Improved host integration so fewer individual Koin modules need to be wired manually.

### Fixed

- Fixed Mobile Ads initialization crashes.
- Fixed UMP consent crashes shared by applications using App Toolkit.
- Fixed host applications crashing because toolkit activities inherited an invalid theme.
- Fixed RTL behavior being lost because of manifest configuration boundaries.
- Fixed duplicate DataStore instances.
- Fixed release resource-linking failures after modularization.
- Fixed incorrect Material theme dependency ownership.
- Fixed API 26 to 28 vibration compatibility problems.
- Fixed incorrect ad preference defaults causing the UI and Ads SDK to disagree.
- Fixed several library publishing and JitPack issues.
- Fixed dependency injection errors that previously appeared only when a screen was opened.
- Fixed numerous build, lint, package, resource, and architecture inconsistencies.

## Sample App Changes

### Added

- Added a dedicated Sample App App Toolkit integration module.
- Added module-specific dependency graph tests.
- Added offline caching for the developer applications catalogue.

### Changed

- Rebuilt the Sample App as a modular application with dedicated:
    - app
    - navigation
    - UI
    - DataStore
    - App Toolkit integration
    - feature
    - widget modules
- Reorganized Sample App packages to follow the project's current file-placement conventions.
- Updated the Sample App to demonstrate the new single-entry App Toolkit integration pattern.

### Improved

- Redesigned the FAQ screen.
- Significantly improved the Components showcase.
- Improved widget caching and Glance interactions.
- Improved application catalogue behavior when the network is unavailable.
- Improved translations and shared resources after modularization.
- Improved overall UI and UX across the Sample App.

### Fixed

- Fixed duplicate **Open in Play Store** actions in app details.
- Fixed Sample App build and lint failures introduced during modularization.
- Fixed incorrect application ID configuration.
- Fixed Firebase configuration validation.
- Fixed several module dependency and resource ownership issues.

---

# Version 26.07.2

`1370002`  
**Released July 8, 2026**

A feature-heavy release introducing Toolkit Tiles, richer Quick Tools, and a significantly more capable Navigation 3 foundation.

## Library Changes

### Added

- Added the **Toolkit Tiles** framework for useful Quick Settings tools.
- Added automatic Quick Settings tile-status synchronization.
- Added support for distinctive Toolkit Tile icon backgrounds.
- Added the **Material Colors** Quick Tool.
- Added reusable navigation scenes and transition infrastructure.
- Added centralized version generation with automatic `versionCode` and `versionName`.
- Added support for release metadata through `release.properties`.

### Changed

- Migrated the main navigation foundation to Navigation 3 scenes.
- Reworked top-level and pushed destinations to use dedicated navigation behavior.
- Reworked adaptive navigation for compact and expanded layouts.

### Improved

- Improved predictive back support.
- Improved transitions between top-level destinations.
- Improved tab history so Back can traverse previously visited destinations.
- Improved large-screen navigation behavior.
- Improved Toolkit Tile status refresh when returning from Android system UI.
- Improved grouped Toolkit Tile presentation and actions.

### Fixed

- Fixed multiple Navigation 3 state and transition edge cases.
- Fixed Toolkit Tile manifest and lint requirements.
- Fixed overlapping consent requests.
- Fixed various CI and release-build issues.

## Sample App Changes

### Added

- Added a new landing screen with Quick Tools previews.
- Added richer app-detail quick actions for:
    - notifications
    - permissions
    - storage
    - battery settings
    - sharing
    - system settings
- Added richer app metadata including installation status, version information, repository links, and privacy policy information.

### Changed

- Integrated Favorites into the Apps & Tools experience instead of maintaining a separate top-level Favorites destination.
- Moved settings, Help, permissions, ads, licenses, and related screens into the main navigation hierarchy.
- Updated the Sample App to demonstrate the new Navigation 3 architecture.

### Improved

- Improved the adaptive shell across phones and larger displays.
- Improved application list initialization.
- Improved navigation motion and transition consistency.
- Improved Toolkit Tiles presentation with category-specific styling.

---

# Version 2.0.11

`114`  
**Released March 30, 2026**

## Library Changes

### Improved

- Updated core dependencies to their latest stable versions.

### Fixed

- Fixed collapsed toolbar state handling.

## Sample App Changes

### Improved

- Improved widget interaction behavior.

### Fixed

- Fixed widget interaction issues.

---

# Version 2.0.9

`112`  
**Released March 24, 2026**

## Library Changes

### Changed

- Selected navigation labels now use stronger emphasis.
- Refined expressive loading indicators.
- Restyled grouped Help feedback components.

### Improved

- Improved icon sizing across buttons, app bars, drawers, and navigation rails.
- Improved accessibility for icon-only controls and dialog actions.
- Updated core dependencies.

### Fixed

- Fixed grouped preference alignment issues.

## Sample App Changes

### Improved

- Updated screens to use the latest shared navigation and Help components.

---

# Version 2.0.7

`109`  
**Released March 7, 2026**

## Library Changes

### Improved

- Updated shared resources.
- Updated core dependencies.
- Improved overall library stability.

## Sample App Changes

### Fixed

- Fixed minor integration inconsistencies.

---

# Version 2.0.6

`108`  
**Released March 6, 2026**

## Library Changes

### Improved

- Updated shared components and internal resources.
- Updated dependencies.

## Sample App Changes

### Added

- Added a dedicated **Feature Request** flow for suggesting new functionality.

---

# Version 2.0.5

`106`  
**Released February 21, 2026**

## Library Changes

### Changed

- Redesigned the consent dialog.
- Redesigned Theme Settings with a richer preview.
- Refined shared button components.

### Improved

- Improved large-screen foundations.

### Fixed

- Fixed crashes when buttons were used without icons.
- Fixed inconsistent component corner radii.
- Fixed incorrect Color Settings tab selection.

## Sample App Changes

### Added

- Added a new animated splash screen.

### Fixed

- Fixed navigation drawer selected states.

---

# Version 2.0.1

`101`  
**Released February 3, 2026**

## Library Changes

### Added

- Added several new themes and color palettes.
- Added seasonal theme support.
- Added Navigation 3 foundations.

### Changed

- Migrated shared navigation to Navigation 3.

### Improved

- Improved shared performance and responsiveness.
- Reduced library and application overhead.
- Introduced major internal architecture improvements.

### Fixed

- Fixed several stability issues.

## Sample App Changes

### Improved

- Updated the Sample App to showcase the expanded theme and navigation systems.

---

# Version 1.1.5

`85`  
**Released December 21, 2025**

## Library Changes

### Added

- Added Blue, Green, Red, Yellow, Monochrome, and Rose palettes.
- Added multiple Material You wallpaper palette variants.
- Added a seasonal Christmas palette.
- Added remote FAQ support with a local fallback.
- Added Firebase Cloud Messaging foundations.
- Added online Help fallback support when Google Play reviews are unavailable.

### Changed

- Expanded the theme system around reusable `ColorPalette` providers.
- Increased the minimum supported Android version to Android 8.0.

### Improved

- Improved Compose stability.
- Improved native ad lifecycle handling.
- Improved in-app update behavior.
- Improved window inset handling.

## Sample App Changes

### Added

- Added **Open Random App**.
- Added a hidden About screen easter egg.

### Changed

- Redesigned Theme Settings around the expanded palette system.
- Updated bottom navigation to newer Material 3 components.

### Improved

- Improved app list layouts.
- Improved native ad placement.
- Improved Help and FAQ behavior.

---

# Version 1.1.4

`77`, `78`  
**Released October 24, 2025**  
**Updated October 25, 2025**

## Library Changes

### Improved

- Refined shared UI components.
- Updated core dependencies.
- Improved general performance and stability.

## Sample App Changes

### Added

- Added richer application previews.

### Fixed

- Fixed issues discovered during the initial 1.1.4 rollout.

---

# Version 1.1.2

`65`  
**Released September 15, 2025**

## Library Changes

### Improved

- Added several quality-of-life improvements.
- Improved shared stability.

## Sample App Changes

### Fixed

- Fixed several application stability issues.

---

# Version 1.1.1

`52`  
**Released August 21, 2025**

## Library Changes

### Added

- Added the in-app changelog system.

### Improved

- Improved startup reliability.
- Improved navigation interactions.

### Fixed

- Fixed User Messaging Platform crashes.
- Fixed snackbar action colors.
- Fixed translation and stability issues.

## Sample App Changes

### Added

- Added a new animated startup experience.

### Improved

- Integrated the new changelog and startup components.

---

# Version 1.0.8

`41`  
**Released July 18, 2025**

## Library Changes

### Improved

- Improved general stability and reliability.

---

# Version 1.0.7

`39`  
**Released July 10, 2025**

## Library Changes

### Improved

- Improved overall stability.

### Fixed

- Fixed several minor issues.

---

# Version 1.0.6

`37`  
**Released June 25, 2025**

## Library Changes

### Changed

- Refined shared side-navigation components.
- Improved loading-state components and animations.

### Improved

- Improved diagnostic information available to the issue reporter.
- Updated internal technologies and dependencies.

## Sample App Changes

### Changed

- Simplified the empty Favorites experience.

---

# Version 1.0.5

`36`  
**Released June 20, 2025**

## Sample App Changes

### Added

- Added a dedicated Favorites screen.

## Library Changes

### Improved

- Added smaller internal stability and performance improvements.

---

# Version 1.0.4

`33`  
**Released June 17, 2025**

## Library Changes

### Improved

- Improved internal stability and performance foundations.

---

# Version 1.0.3

`32`  
**Released June 17, 2025**

## Library Changes

### Changed

- Migrated About Libraries to its newer Compose implementation.

### Improved

- Improved shared foundations for future features.

## Sample App Changes

### Added

- Added the first version of the built-in issue reporter.

---

# Version 1.0.2

`29`  
**Released June 12, 2025**

## Library Changes

### Changed

- Simplified consent handling.
- Removed obsolete Ads Settings screens.
- Refined shared layout components.

### Improved

- Improved interaction feedback.

### Fixed

- Fixed onboarding crashes.
- Fixed consent form timing issues.

## Sample App Changes

### Changed

- Reworked the applications list.
- Updated privacy policy and bug-report destinations.

---

# Version 1.0.1

`27`  
**Released June 10, 2025**

## Library Changes

### Added

- Added Play Store fallback handling.
- Added per-app language support.

### Improved

- Improved consent handling.
- Improved onboarding and privacy interaction feedback.

## Sample App Changes

### Improved

- Updated startup privacy information.
- Improved Firebase performance diagnostics.

---

# Version 1.0.0

`25`  
**Released June 6, 2025**

## Library Changes

### Added

- Initial stable App Toolkit library.

## Sample App Changes

### Added

- Initial stable Sample App demonstrating App Toolkit integration.