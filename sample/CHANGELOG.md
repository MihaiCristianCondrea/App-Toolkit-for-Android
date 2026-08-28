# Changelog

# Unreleased

### Improved

- Improved translations and wording across multiple screens.

### Fixed

- Fixed several navigation issues in the Sample App.

---

# August 24, 2026

**Version:** `26.08.9` (`1370009`)

Reliability-focused release with major improvements to startup, onboarding, Quick Tools, and the way App Toolkit communicates one-time UI actions.

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

# August 22, 2026

### Improved

- Updated the Sample App to use the corrected onboarding behavior.

---

# August 20, 2026

**Version:** `26.08.8` (`1370008`)

Major structural update that separated reusable App Toolkit responsibilities from Sample App-specific features much more clearly.

### Added

- Added the **Dim Flash** Quick Tool.
- Added offline caching for the developer applications catalogue.
- Added a dedicated Sample App integration module for App Toolkit.

### Changed

- Rebuilt the Sample App into dedicated app, core, feature, navigation, integration, and widget modules.
- Reorganized Sample App packages around clearer feature ownership.
- Updated the Sample App to demonstrate the new App Toolkit integration model.
- Removed battery percentage, battery temperature, and battery Quick Settings tools after moving that functionality out of the Sample App.

### Improved

- Redesigned the FAQ screen.
- Significantly improved the Components showcase.
- Improved widget caching and Glance interactions.
- Improved application catalogue behavior when the network is unavailable.
- Improved translations and Sample App resource ownership.
- Improved overall Sample App UI and UX.

### Fixed

- Fixed duplicate **Open in Play Store** actions in app details.
- Fixed Sample App build and lint failures introduced during modularization.
- Fixed incorrect Sample App application configuration.
- Fixed Firebase configuration validation.
- Fixed several Sample App module dependency and resource ownership issues.

---

# August 2026

### Changed

- Updated the Sample App to consume the new modular App Toolkit structure instead of owning reusable infrastructure directly.

---

# July 8, 2026

**Version:** `26.07.2` (`1370002`)

Feature-heavy Sample App release alongside continued refinement of App Toolkit's reusable navigation and UI foundations.

### Added

- Added **Toolkit Tiles** for discovering and managing supported Quick Settings tools.
- Added automatic Quick Settings tile-status synchronization.
- Added distinctive visual backgrounds for Toolkit Tile icons.
- Added the **Material Colors** Quick Tool.
- Added a new landing screen with Quick Tools previews.
- Added richer app-detail quick actions for notifications, permissions, storage, battery settings, sharing, and system settings.
- Added richer app information including installation state, version information, repository links, and privacy policy information.

### Changed

- Integrated Favorites into Apps & Tools instead of maintaining a separate top-level Favorites destination.
- Moved settings, Help, permissions, ads, licenses, and related toolkit screens into the main application navigation hierarchy.
- Updated the Sample App to demonstrate the newer Navigation 3 architecture.
- Added centralized Sample App version generation and release metadata.

### Improved

- Improved transitions between top-level destinations.
- Added navigation history between previously visited tabs.
- Improved adaptive layouts across phones and larger displays.
- Improved Toolkit Tile status refresh and presentation.
- Improved application list initialization.

### Fixed

- Fixed several Sample App Navigation 3 state and transition issues.
- Fixed Toolkit Tile manifest and lint requirements.
- Fixed overlapping consent requests.
- Fixed release and manifest configuration issues.

---

# March 30, 2026

**Version:** `2.0.11` (`114`)

### Improved

- Improved widget interactions.

### Fixed

- Fixed widget interaction issues.

---

# March 24, 2026

**Version:** `2.0.9` (`112`)

### Improved

- Updated Sample App screens to use the latest shared navigation and Help components.

---

# March 7, 2026

**Version:** `2.0.7` (`109`)

---

# March 6, 2026

**Version:** `2.0.6` (`108`)

### Added

- Added a dedicated **Feature Request** flow.

---

# February 21, 2026

**Version:** `2.0.5` (`106`)

### Added

- Added a new animated splash screen.

### Fixed

- Fixed navigation drawer selected states.

---

# February 3, 2026

**Version:** `2.0.1` (`101`)

### Improved

- Updated the Sample App to demonstrate the expanded theme and navigation systems.

---

# December 21, 2025

**Version:** `1.1.5` (`85`)

### Added

- Added **Open Random App**.
- Added a hidden About screen easter egg.

### Changed

- Redesigned Theme Settings around the expanded palette system.
- Increased the minimum supported Android version to Android 8.0.
- Updated bottom navigation to newer Material 3 components.

### Improved

- Improved app list layouts.
- Improved native ad placement.
- Improved Help and FAQ behavior.

---

# October 24, 2025

**Version:** `1.1.4` (`77`, `78`)

### Added

- Added richer application previews.

---

# September 15, 2025

**Version:** `1.1.2` (`65`)

### Improved

- Added several quality-of-life improvements.
- Improved application stability.

---

# August 21, 2025

**Version:** `1.1.1` (`52`)

### Added

- Added a new animated startup experience.

### Improved

- Integrated the new changelog and startup components.

---

# July 18, 2025

**Version:** `1.0.8` (`41`)

### Improved

- Improved general stability and reliability.

---

# July 10, 2025

**Version:** `1.0.7` (`39`)

### Improved

- Improved overall stability.

### Fixed

- Fixed several minor issues.

---

# June 25, 2025

**Version:** `1.0.6` (`37`)

### Changed

- Simplified the empty Favorites experience.

---

# June 20, 2025

**Version:** `1.0.5` (`36`)

### Added

- Added a dedicated Favorites screen.

### Improved

- Added smaller internal stability and performance improvements.

---

# June 17, 2025

**Version:** `1.0.3` (`32`), `1.0.4` (`33`)

### Added

- Added the first version of the built-in issue reporter.

---

# June 12, 2025

**Version:** `1.0.2` (`29`)

### Changed

- Reworked the applications list.
- Updated privacy policy and bug-report destinations.

---

# June 10, 2025

**Version:** `1.0.1` (`27`)

### Improved

- Updated startup privacy information.
- Improved Firebase performance diagnostics.

---

# June 6, 2025

**Version:** `1.0.0` (`25`)

### Added

- Initial stable Sample App demonstrating App Toolkit integration.