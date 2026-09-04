# Changelog

---

# Unreleased

## Library Changes

### Changed

- Standardized library APIs under module-owned `feature.*`, `core.*`, and `integration.*` package roots; consumers must update imports to the new packages.
- Moved library dependency-injection bindings into the owning feature/integration modules and
  exposed the datastore module from `core.datastore.di`; the toolkit facade now only composes those
  modules.
- Moved `ThemePreferencesState`, `BaseCoreManager`, and `FirebaseControllerImpl` into their
  layer-specific packages; consumers must update imports to `core.common.domain.models.theme`,
  `core.common.data.managers`, and `integration.firebase.data.repositories`.
- Moved support donation product IDs to `feature.support.domain.models`; consumers importing
  `DonationProductIds` must update to the new package.
- Settings, advanced, diagnostics, and display now expose their resources through their own `R`
  classes. Direct consumers of the removed settings resource artifact must update dependencies and
  imports; resource keys and translations are preserved.

### Improved

- Standardized changelog, alert-dialog, and date-picker actions with consistent button styling, haptic feedback, and press animations.

---

# August 28, 2026

**Version:** `3.0.0-pre12`

### Changed

- Display ads now defaults to on in debug builds as well as release, so a fresh debug install renders ads instead of none. A stored choice still wins in every build. Reduce ads continues to default to off everywhere.
- `dataStoreModule()` no longer takes `isDebugBuild`, which it only used to pick that default.

### Added

- Added `AdLoadReporter`, which logs every ad load failure, adds a Crashlytics breadcrumb, and records a non-fatal for the failures that are not simply no fill.
- Added `AdSlotDebugPlaceholder`, shown by `NativeAdSlot` on debug builds where an empty ad slot would otherwise render nothing.
- Added `rememberNativeAdState`, which returns why a slot is empty alongside the ad.

---

# August 28, 2026

**Version:** `3.0.0-pre11`

### Added

- Added a **Help & feedback** action to the standalone General Settings top app bar.

### Documentation

- Documented how a host should render ads: use `NativeAdSlot`, `rememberNativeAd` or `AdBanner` rather than the Mobile Ads loaders directly, and what the toolkit handles on the host's behalf.

### Fixed

- Fixed the ads migration removing an explicit opt-in as well as an opt-out, which on debug builds turned ads off again at every launch.
- Fixed the **Help & feedback** overflow action appearing on every standalone settings sub-page instead of on the settings root.

---

# August 26, 2026

**Version:** `3.0.0-pre10`

### Added

- Added a **Reduce Ads** preference for suppressing App Open ads while keeping other supported ad formats enabled.

### Changed

- Integrated the Reduce Ads preference into the shared ads settings flow and persistent DataStore configuration.
- Updated the release Ads Settings screen to expose Reduce Ads while retaining the full Display Ads control for debug builds.
- Updated shared DataStore access to use the App Toolkit dependency graph.

### Fixed

- Improved persistence error handling for ads preferences.

---

# August 24, 2026

**Version:** `3.0.0-pre9`

### Improved

- Expanded module documentation across the library.
- Improved KDoc coverage for reusable APIs, contracts, ownership, and non-obvious behavior.

---

# August 24, 2026

**Version:** `3.0.0-pre8`

### Changed

- Moved additional shared application resources and configuration under App Toolkit ownership.
- Moved reusable themes, splash resources, locale configuration, backup rules, and extraction rules into the library.
- Reduced the amount of host application configuration required when integrating App Toolkit.

### Improved

- Improved manifest contracts and ownership between App Toolkit and host applications.
- Improved reusable Support state handling.
- Improved shared navigation destinations and back-stack helpers.

---

# August 22, 2026

**Version:** `3.0.0-pre7`

### Changed

- Advanced the App Toolkit 3.0 preview publishing version.

No meaningful consumer-facing library behavior changed in this preview.

---

# August 22, 2026

**Version:** `3.0.0-pre6`

### Improved

- Improved JitPack publishing reliability by pinning builds to a supported Temurin JDK.
- Added onboarding completion failure translations to every supported locale.

### Fixed

- Fixed missing onboarding translations that caused library lint failures.

---

# August 22, 2026

**Version:** `3.0.0-pre5`

### Changed

- Refined reusable onboarding preference ownership.
- Improved separation between onboarding, display, and theme state.

### Improved

- Added visible feedback when onboarding completion fails.

### Fixed

- Improved onboarding persistence failure handling.

---

# August 21, 2026

**Version:** `3.0.0-pre4`

### Improved

- Made startup more resilient when consent, permissions, or optional initialization cannot complete.
- Added safe fallback behavior when startup initialization fails.

### Fixed

- Fixed startup becoming permanently stuck on the loading screen.
- Fixed startup failing to complete when optional initialization work throws an error.
- Fixed missing onboarding dependency injection configuration.
- Fixed one-time startup actions being lost before the UI begins collecting them.
- Fixed repeated permission and consent work during startup.

---

# August 20, 2026

**Version:** `3.0.0-pre3`

### Changed

- Advanced the App Toolkit 3.0 preview publishing version.

No meaningful consumer-facing library behavior changed in this preview.

---

# August 16, 2026

**Version:** `3.0.0-pre2`

### Changed

- Refined the internal file and package structure of reusable components.

No public library behavior changed.

---

# August 15, 2026

**Version:** `3.0.0-pre1`

The first preview of the redesigned App Toolkit 3.0 architecture.

### Added

- Added a unified `appToolkitModules(...)` entry point for loading the standard App Toolkit dependency graph.
- Added automated dependency-graph verification for library integrations.
- Added manifest contract tests to prevent reusable modules from overriding host application configuration.
- Added dedicated reusable core, feature, integration, navigation, testing, and DataStore modules.

### Changed

- Split the previous App Toolkit structure into focused reusable modules.
- Split shared preferences into responsibility-specific data sources for themes, display, onboarding, consent, ads, review state, changelog state, favorites, and general application state.
- Reorganized reusable navigation contracts into dedicated modules.
- Reworked repository and data-layer boundaries to reduce unnecessary abstraction.
- Removed duplicate DataStore ownership.
- Updated publishing so all required App Toolkit modules are exposed correctly.
- Reworked host integration around clearer provider and dependency-injection contracts.

### Improved

- Reduced unnecessary Compose recompositions across reusable components.
- Improved shared loading and animation performance.
- Improved reusable modifier performance.
- Improved localization and plural handling across the library.
- Improved compact native ad sizing.
- Improved startup dialog behavior across screen sizes and content lengths.
- Improved host integration so fewer individual Koin modules need to be registered manually.
- Improved testing infrastructure for reusable modules.

### Fixed

- Fixed Mobile Ads SDK initialization crashes.
- Fixed UMP consent crashes.
- Fixed host applications inheriting incorrect toolkit theme configuration.
- Fixed RTL configuration being lost through manifest ownership.
- Fixed duplicate DataStore instances.
- Fixed release resource-linking failures after modularization.
- Fixed incorrect Material theme dependency ownership.
- Fixed API 26 to 28 vibration compatibility issues.
- Fixed mismatched ad preference defaults.
- Fixed dependency injection errors that previously appeared only when affected screens were opened.
- Fixed several publishing, manifest, resource, and module ownership issues.

---

# August 4, 2026

**Version:** `2.0.19`

### Changed

- Updated library publishing and build configuration in preparation for App Toolkit 3.0.

No public library behavior changed.

---

# August 4, 2026

**Version:** `2.0.18`

### Improved

- Expanded the reusable custom carousel API with configurable corner radius sizing.

---

# July 10, 2026

**Version:** `2.0.17`

### Changed

- Updated library publishing and versioning configuration.

---

# July 9, 2026

**Version:** `2.0.16`

### Changed

- Updated library publishing and versioning configuration.

---

# July 8, 2026

**Version:** `2.0.15`

### Changed

- Updated library publishing and versioning configuration.

---

# July 8, 2026

**Version:** `2.0.14`

### Changed

- Updated library publishing and versioning configuration.

---

# April 17, 2026

**Version:** `2.0.12`

### Added

- Added an `enabled` parameter to `AnimatedIconButtonDirection`.

### Improved

- Updated reusable dependencies and Navigation 3 foundations.

---

# March 30, 2026

**Version:** `2.0.11`

### Fixed

- Fixed Collapsed Toolbar state handling.

### Improved

- Updated reusable dependencies.

---

# March 26, 2026

**Version:** `2.0.10`

### Changed

- Updated reusable Google Android color palettes.

### Improved

- Updated shared Compose and AndroidX dependencies.

---

# March 24, 2026

**Version:** `2.0.9`

### Improved

- Updated shared dependencies.
- Improved error handling in reusable repositories and utilities.

---

# March 7, 2026

**Version:** `2.0.8`

### Changed

- Selected navigation labels now use stronger emphasis.
- Refined expressive loading indicators.
- Restyled grouped Help feedback components.

### Improved

- Improved icon sizing across reusable buttons, app bars, drawers, and navigation rails.
- Improved accessibility for icon-only controls and dialog actions.

### Fixed

- Fixed feedback sheet icon alignment.
- Fixed grouped preference card presentation.

---

# March 7, 2026

**Version:** `2.0.7`

### Improved

- Updated shared resources and reusable dependencies.
- Improved general library stability.

---

# March 6, 2026

**Version:** `2.0.6`

### Improved

- Updated reusable components and internal resources.
- Updated AndroidX, Compose, Firebase, Ktor, Coil, and build dependencies.

---

# February 21, 2026

**Version:** `2.0.5`

### Changed

- Updated App Toolkit publishing configuration for JitPack.

### Improved

- Improved large-screen foundations for reusable screens.
- Improved navigation drawer state handling.

### Fixed

- Fixed several shared component and state-management issues.

---

# February 20, 2026

**Version:** `2.0.4`

### Changed

- Updated reusable splash screen branding.

---

# January 25, 2026

**Version:** `2.0.1`

### Changed

- Continued migration of reusable screens and components to the newer App Toolkit architecture.

### Improved

- Updated shared dependencies.
- Improved component consistency and internal stability.

---

# January 10, 2026

**Version:** `2.0.0`

A major library release focused on Navigation 3, theming, architecture, and distribution.

### Added

- Added multiple reusable themes and color palettes.
- Added seasonal theme support.
- Added Navigation 3 foundations.

### Changed

- Migrated reusable navigation infrastructure to Navigation 3.
- Made JitPack the primary App Toolkit distribution method.
- Removed the previous Maven Central publishing configuration.

### Improved

- Improved reusable UI performance and responsiveness.
- Reduced library overhead.
- Improved internal architecture and dependency ownership.
- Improved JitPack repository configuration.

### Fixed

- Fixed several shared stability issues.

---

# December 22, 2025

**Version:** `1.1.7`

### Added

- Added centralized Maven publishing coordinates.
- Added generated source and documentation artifacts to Maven publications.
- Added complete POM metadata including project, license, developer, and SCM information.

### Changed

- Added support for switching publishing coordinates between JitPack and Maven environments.
- Centralized publishing configuration.

---

# December 21, 2025

**Version:** `1.1.6`

### Changed

- Updated the published App Toolkit library version to `1.1.6`.

---

# December 21, 2025

**Version:** `1.1.5`

### Added

- Added Blue, Green, Red, Yellow, Monochrome, and Rose palettes.
- Added multiple Material You wallpaper palette variants.
- Added a seasonal Christmas palette.
- Added reusable remote FAQ support with a local fallback.
- Added Firebase Cloud Messaging foundations.
- Added online Help fallback behavior when in-app review is unavailable.

### Changed

- Expanded the reusable theme system around `ColorPalette` providers.

### Improved

- Improved Compose stability.
- Improved native ad lifecycle handling.
- Improved in-app update behavior.
- Improved window inset handling.

---

# December 21, 2025

**Version:** `1.1.4`

### Improved

- Refined reusable UI components.
- Improved overall library performance and stability.

---

# October 3, 2025

**Version:** `1.1.3`

### Changed

- Added circular clipping to reusable dropdown menu items.

---

# September 14, 2025

**Version:** `1.1.2`

### Improved

- Improved shared reliability and reusable components.

---

# August 19, 2025

**Version:** `1.1.1`

### Improved

- Improved reusable startup handling.
- Improved shared navigation interactions.

### Fixed

- Fixed User Messaging Platform crashes.
- Fixed snackbar action colors.
- Fixed shared translation and stability issues.

---

# July 25, 2025

**Version:** `1.1.0`

### Changed

- Updated the published App Toolkit library to `1.1.0`.

### Improved

- Improved reusable startup and changelog foundations.

---

# July 24, 2025

**Version:** `1.0.42`

### Changed

- Updated dependency declarations to use named arguments.

No public library behavior changed.

---

# July 23, 2025

**Version:** `1.0.41`

### Changed

- Updated shared App Toolkit naming and copyright resources.

---

# July 23, 2025

**Version:** `1.0.40`

### Changed

- Removed the `v` prefix from library version references for consistency.

---

# July 23, 2025

**Version:** `1.0.39`

### Improved

- Added haptic feedback, sound feedback, and bounce interaction to expandable sections in the reusable Issue Reporter.

---

# July 17, 2025

**Version:** `1.0.38`

### Changed

- Updated reusable dialog dismiss actions to use `OutlinedButton`.
- Corrected an Arabic theme translation.

---

# July 11, 2025

**Version:** `1.0.37`

### Changed

- Consolidated lifecycle helpers into `LifecycleEventsEffect`.
- Renamed the previous lifecycle helper to `ActivityLifecycleEffect`.

### Improved

- Updated reusable Android and Compose dependencies.

---

# July 7, 2025

**Version:** `1.0.36`

### Improved

- Improved reusable ads test coverage and reliability.

---

# June 24, 2025

**Version:** `1.0.35`

### Improved

- Improved reusable side-navigation presentation and behavior.
- Improved loading-state components and animations.
- Improved diagnostic data available to the Issue Reporter.

---

# June 20, 2025

**Version:** `1.0.34`

### Fixed

- Fixed reusable library build issues.

---

# June 20, 2025

**Version:** `1.0.33`

### Improved

- Completed localization updates for reusable resources.

---

# June 20, 2025

**Version:** `1.0.32`

### Changed

- Updated shared library integration and release configuration.

---

# June 19, 2025

**Version:** `1.0.31`

### Changed

- Updated the published App Toolkit library version to `1.0.31`.

---

# June 18, 2025

**Version:** `1.0.30`

### Changed

- Updated the published App Toolkit library version to `1.0.30`.

---

# June 18, 2025

**Version:** `1.0.29`

### Changed

- Marked `OnboardingActivity` with `noHistory` so completed onboarding does not remain in the host application's back stack.

---

# June 17, 2025

**Version:** `1.0.28`

### Changed

- Updated the library compile SDK to Android API 36.
- Refined public reusable component APIs and parameter ordering.

---

# June 17, 2025

**Version:** `1.0.27`

### Fixed

- Corrected the French translation for the reusable Open action.

---

# June 12, 2025

**Version:** `1.0.26`

### Improved

- Completed localization updates for reusable review messaging.

---

# June 8, 2025

**Version:** `1.0.25`

### Changed

- Removed obsolete Ads Settings and consent abstractions.
- Simplified reusable Ads Settings integration.

---

# June 7, 2025

**Version:** `1.0.24`

### Added

- Added `AppLocalesMetadataHolderService` support for Android per-app language configuration.

---

# June 6, 2025

**Version:** `1.0.23`

### Improved

- Refined reusable consent-toggle interactions.

---

# June 6, 2025

**Version:** `1.0.22`

### Changed

- Removed redundant bounce animations from standard preference components.

### Improved

- Added haptic feedback to onboarding pager interactions.
- Improved theme onboarding component presentation and clipping.

---

# June 6, 2025

**Version:** `1.0.21`

### Improved

- Expanded KDoc across reusable APIs and components.
- Added haptic drawer interactions and button bounce feedback.
- Improved reusable error handling and error types.
- Updated tooltip components.

---

# June 4, 2025

**Version:** `1.0.20`

### Added

- Added `ScreenHelper` utilities for detecting landscape, tablet, and combined adaptive layouts.

---

# June 4, 2025

**Version:** `1.0.19`

### Added

- Added `TooltipIconButton`.
- Added reusable radio-button and checkbox preference components.
- Added reusable light and dark color-scheme foundations.

---

# June 4, 2025

**Version:** `1.0.18`

### Changed

- Refined shared DataStore constants.

---

# June 3, 2025

**Version:** `1.0.17`

### Changed

- Exposed the underlying DataStore from `CommonDataStore`.

---

# May 28, 2025

**Version:** `1.0.16`

### Added

- Added `CommonDataStore` for shared application preferences.
- Added reusable DataStore key constants.

---

# May 26, 2025

**Version:** `1.0.15`

### Changed

- Updated the published App Toolkit library version to `1.0.15`.

---

# May 18, 2025

**Version:** `1.0.12`

### Added

- Added `OnResumeEffect` for running callbacks when the host lifecycle reaches `ON_RESUME`.

### Improved

- Updated Android Gradle Plugin and reusable dependencies.

---

# May 13, 2025

**Version:** `1.0.11`

### Changed

- Refactored reusable library code and internal organization.

---

# May 9, 2025

**Version:** `1.0.10`

### Added

- Added optional ad support to `NoDataScreen`.

### Changed

- Added configuration parameters for controlling the ad slot and `AdsConfig`.

### Fixed

- Corrected retry-button text handling in `NoDataScreen`.

---

# May 9, 2025

**Version:** `1.0.9`

### Added

- Added one-time action support to the reusable base ViewModel infrastructure.

---

# May 8, 2025

**Version:** `1.0.8`

### Improved

- Updated Gradle, Compose, DataStore, Lifecycle, Navigation, and related reusable dependencies.

---

# May 5, 2025

**Version:** `1.0.7`

### Added

- Added `AnimatedFloatingActionButton` with visibility animation, bounce feedback, and click sound.

---

# May 3, 2025

**Version:** `1.0.6`

### Fixed

- Fixed out-of-bounds animation state access.

### Improved

- Improved shared animation reliability.

---

# May 1, 2025

**Version:** `1.0.5`

### Added

- Added `CustomSnackbarVisuals`.
- Added `DefaultSnackbarHandler`.
- Added `DefaultSnackbarHost`.

### Changed

- Replaced the previous status snackbar implementation with the new reusable snackbar infrastructure.
- Refined reusable About and Help state handling.

### Fixed

- Fixed ads preference checking in `AdsCoreManager`.

---

# May 1, 2025

**Version:** `1.0.4`

### Improved

- Simplified reusable animated visibility state handling.
- Improved defensive animation-state access in Help components.

---

# April 17, 2025

**Version:** `1.0.2`

### Changed

- Updated the published App Toolkit library version to `1.0.2`.

---

# April 17, 2025

**Version:** `1.0.1`

### Improved

- Refined reusable navigation, animation, update-checking, About, and ads infrastructure.

---

# April 7, 2025

**Version:** `1.0.0`

The first stable 1.x App Toolkit library release.

### Changed

- Refined reusable settings section shapes and typography.
- Standardized icon sizing across preference and button components.

### Improved

- Improved visual consistency across reusable preference components.

---

# January 30, 2025

**Version:** `0.0.47`

### Changed

- Renamed the reusable app description resource to `app_short_description`.
- Added a reusable `device_info` string resource.

---

# January 30, 2025

**Version:** `0.0.46`

### Improved

- Improved the reusable About screen.
- Added clearer EULA and changelog loading and error messages.
- Improved reusable Settings resource descriptions.
- Updated Compose dependencies.

---

# January 30, 2025

**Version:** `0.0.45`

### Added

- Added reusable app-update notification infrastructure.
- Added reusable app-usage notification infrastructure.
- Added the User Messaging Platform dependency.

### Fixed

- Fixed an incorrect notification summary.

---

# January 30, 2025

**Version:** `0.0.44`

### Added

- Reintroduced reusable app-update and app-usage notification infrastructure.

### Fixed

- Fixed an incorrect notification summary.

---

# January 28, 2025

**Version:** `0.0.43`

### Changed

- Removed an unused shortcut settings icon.

---

# January 28, 2025

**Version:** `0.0.41`

### Added

- Added reusable app-update notification management.
- Added reusable app-usage notification scheduling and workers.
- Added update and important-notification icons.

### Improved

- Improved theme selection and theme summary handling.

---

# January 26, 2025

**Version:** `0.0.40`

### Improved

- Updated translations and reusable dependencies.
- Improved usage and diagnostics descriptions.

---

# January 25, 2025

**Version:** `0.0.39`

### Added

- Added `ThemeSettingsList`.
- Added `UsageAndDiagnosticsList`.
- Added `UsageAndDiagnosticsSettingsProvider`.
- Added `DrawerStyle`.
- Added reusable display, privacy, advanced, and about settings provider contracts.

---

# January 25, 2025

**Version:** `0.0.38`

### Changed

- Refactored reusable theme and privacy settings infrastructure.
- Introduced `DrawerStyle` customization.

---

# January 25, 2025

**Version:** `0.0.37`

### Added

- Added reusable Theme Settings.
- Added system, dark, and light theme selection.
- Added AMOLED mode support.

---

# January 25, 2025

**Version:** `0.0.36`

### Added

- Added reusable Display Settings.
- Added startup-page selection.
- Added language-selection support.
- Added app-language settings integration.
- Expanded reusable Privacy Settings links.

---

# January 24, 2025

**Version:** `0.0.35`

### Changed

- Simplified settings provider contracts by removing unnecessary `Context` parameters.

### Improved

- Updated reusable dependencies and settings implementation.

---

# January 24, 2025

**Version:** `0.0.34`

### Added

- Added a reusable Privacy Settings screen.
- Added privacy policy, terms, code of conduct, permissions, ads, diagnostics, legal-notice, and license preferences.
- Added `PrivacySettingsProvider` for host customization.

---

# January 24, 2025

**Version:** `0.0.33`

### Improved

- Improved reusable About Settings state handling.

---

# January 24, 2025

**Version:** `0.0.32`

### Changed

- Improved reusable About Settings integration.
- Expanded `AboutSettingsProvider` with package and version information.
- Simplified reusable intent helpers.

---

# January 24, 2025

**Version:** `0.0.31`

### Added

- Added reusable About Settings.
- Added `AboutSettingsProvider`.
- Added app and device information presentation.

---

# January 24, 2025

**Version:** `0.0.30`

### Fixed

- Prevented Ktor client initialization failures from crashing host applications.

---

# January 24, 2025

**Version:** `0.0.29`

### Changed

- Updated ads behavior to follow persisted user preferences.

---

# January 24, 2025

**Version:** `0.0.28`

### Added

- Added an ads-enabled flag to `AdsCoreManager`.

### Changed

- Ads are initialized and displayed only when enabled.

---

# January 24, 2025

**Version:** `0.0.27`

### Changed

- Moved advertisement SDK initialization from `BaseCoreManager` into `AdsCoreManager`.

---

# January 24, 2025

**Version:** `0.0.26`

### Improved

- Refactored application initialization around coroutines and concurrent work.
- Improved initialization error handling.
- Improved advertisement SDK initialization and loading reliability.

### Added

- Added ad completion callbacks.
- Added the Internet permission required by network-backed integrations.

---

# January 24, 2025

**Version:** `0.0.25`

### Added

- Added `AdsCoreManager`.
- Added App Open ad initialization and presentation support.

---

# January 18, 2025

**Version:** `0.0.24`

Maintenance release.

---

# January 18, 2025

**Version:** `0.0.23`

### Added

- Added reusable `ErrorHandler`.
- Added the `ErrorReporter` contract.
- Added user-facing Snackbar handling for initialization and runtime errors.

### Improved

- Updated core reusable dependencies.

---

# January 16, 2025

**Version:** `0.0.21`

### Improved

- Expanded KDoc across reusable classes and components.
- Added haptic drawer feedback.
- Added bounce interaction feedback for buttons.
- Expanded reusable error types.
- Updated tooltip infrastructure.

---

# January 15, 2025

**Version:** `0.0.20`

### Added

- Added screen utilities for orientation and tablet detection.

---

# January 14, 2025

**Version:** `0.0.19`

### Added

- Added `TooltipIconButton`.
- Added reusable radio-button and checkbox preferences.
- Added reusable light and dark color schemes.

---

# January 13, 2025

**Version:** `0.0.18`

### Changed

- Refined DataStore key infrastructure.

---

# January 13, 2025

**Version:** `0.0.17`

### Changed

- Exposed the DataStore instance from `CommonDataStore`.

---

# January 13, 2025

**Version:** `0.0.16`

### Added

- Added `CommonDataStore`.
- Added shared DataStore preference keys.

---

# January 13, 2025

**Version:** `0.0.15`

### Improved

- Expanded documentation across reusable models, enums, constants, and components.

---

# January 12, 2025

**Version:** `0.0.14`

### Added

- Added `LoadingScreen`.
- Added reusable Snackbar presentation.
- Added `ClipboardHelper`.

### Improved

- Improved app icon loading in the reusable version information dialog.

---

# January 12, 2025

**Version:** `0.0.13`

### Added

- Added `ButtonState`.
- Added debug ad constants.
- Added `NavigationDrawerItem`.
- Added the App Open ad completion listener.
- Added `UiErrorModel`.

### Changed

- Reorganized open-source license utilities.

---

# January 12, 2025

**Version:** `0.0.12`

### Changed

- Updated `VersionInfoAlertDialog` to accept a copyright string resource.

---

# January 12, 2025

**Version:** `0.0.11`

### Changed

- Exposed Coil dependencies through the library API.

### Improved

- Improved changelog and EULA loading reliability.
- Improved reusable HTML state loading.

---

# January 11, 2025

**Version:** `0.0.10`

### Added

- Added `VersionInfoAlertDialog`.
- Added reusable switch, settings, and category preference components.
- Added reusable horizontal and vertical spacer components.
- Added `ErrorType`.
- Added Coil image-loading support.

---

# January 11, 2025

**Version:** `0.0.9`

### Added

- Added an animated extended floating action button.
- Added reusable error categorization.
- Added open-source license, EULA, and changelog utilities.
- Added developer-contact intent helpers.
- Added clipboard Snackbar callbacks.

---

# January 11, 2025

**Version:** `0.0.8`

### Changed

- Moved hardcoded reusable strings into Android resources for localization.

---

# January 11, 2025

**Version:** `0.0.7`

### Changed

- Exposed required dependencies through the library API.

### Improved

- Updated Gradle, Android Gradle Plugin, and reusable dependencies.

---

# January 10, 2025

**Version:** `0.0.6`

### Added

- Added clipboard helpers.
- Added app-sharing helpers.
- Added developer email helpers.
- Added reusable open-source licenses, EULA, and changelog navigation.
- Added a reusable error dialog.

### Improved

- Added Ktor timeouts and default headers.

---

# January 8, 2025

**Version:** `0.0.5`

### Added

- Added reusable About Libraries functionality.

### Improved

- Improved helper APIs and internal organization.

---

# January 8, 2025

**Version:** `0.0.4`

### Added

- Added reusable spacer components.
- Added preference components.
- Added a reusable error dialog.
- Added bug-report and feature-request foundations.

---

# January 8, 2025

**Version:** `0.0.3`

- Initial reusable App Toolkit foundations.

---

# January 8, 2025

**Version:** `0.0.3_pre1`

- Initial preview of the App Toolkit library.

---

# January 8, 2025

**Version:** `v0.0.2`

- Initial library development release.

---

# January 8, 2025

**Version:** `v0.0.1`

- Initial library release.
