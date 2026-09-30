# Changelog

---

# Unreleased

### Added

- Search the quick tools and the apps from the app bar.
- Pick the weather from a new button in the theme settings' top bar, once you find the easter egg on the About screen: snow with the Christmas colours, nothing at all, or rain over any colours.

### Changed

- Settings, Help, Support and the components showcase now open as pages of the main screen, with the same back swipe animation as before, and the Settings shortcut opens Settings directly.
- Every settings page, About, the licenses, permissions and ads settings now open inside the app too. On tablets and wide windows the settings categories open beside the list.
- The first-launch screens now run inside the app, so finishing onboarding takes you straight to your start page.
- The information icon beside the app in Android's permission manager and privacy dashboard opens the app's privacy settings again, and back returns you to the system's settings.
- On tablets, foldables and wide windows the navigation becomes a side rail or a permanent drawer, and long pages keep a comfortable width.
- On tablets and wide windows, Settings, Help and the other pages open beside the side navigation, which stays in place and shows where you are, so you can switch straight to another page or tab.
- Settings, Help & feedback, Updates and Share are always at the bottom of the navigation drawer.
- Layout options such as the app bar and bottom bar style, the navigation colour and the back swipe now live in Display settings. Developer options move to Advanced settings, and appear once you find the easter egg on the About screen.

### Improved

- The menu button turns into a back arrow when you open a screen, instead of popping in and out, and the navigation icons animate when you tap them.
- The welcome and onboarding pages keep a comfortable width on tablets.
- On tablets and wide windows, a page opened from the side navigation looks like a tab, without a back button.
- The labelled floating action buttons shrink to their icon while you scroll down and grow back when you scroll up.
- Settings on a tablet shows its illustration and a Get help button beside the list until you open a category.
- Text is no longer black on some screens in the dark theme.
- Display settings and developer options look like the rest of the settings, and only offer options that change something in this app.
- On tablets, switching between pages from the side navigation no longer flashes a back button, and moving around the app does less work.
- On tablets and in landscape, Help and the other pages from the side navigation fade in place instead of sliding in, the side rail starts collapsed after a rotation, and the app bar next to the rail has square corners.
- Switching between Quick tools and Apps no longer makes the search bar flicker, and a back swipe from the right edge on a tablet's settings detail follows your finger.
- What's new now separates releases with a wavy line.
- Search the settings from the top of the settings list, down to single options such as dark theme or the app bar.
- Moving between Components, Settings and the tabs on a tablet or in landscape no longer flashes grey.
- The app bar can hide as you scroll too, in Display settings, so the whole screen makes room for the content.
- The ad above the bottom navigation is back, and messages such as a consent form failing to load now appear above the navigation bar instead of not at all.

### Fixed

- The holiday greeting's checkbox no longer sits against its text, and its ripple is no longer cut off.
- The favourite star in an app's details now fills and empties as you tap it, as it does on the app's card.
- The display settings dialogs have their icons again.
- The floating button no longer slips under the gesture bar when the bottom bar hides as you scroll.

---

# September 24, 2026

**Version:** `26.09.22` (`137260922`)

### Added

- Fixed some Google Play Store issues.

---

# September 24, 2026

**Version:** `26.09.21` (`137260921`)

### Changed

- The Android theme in dark mode now uses Android's green with navy and light blue, so it no longer looks like the green theme.
- The color previews in Theme settings are back to their plain look without the outline, and the check on the selected color is still easy to see.

### Fixed

- Fixed Theme settings not scrolling the color list to the color you're using.

---

# September 23, 2026

**Version:** `26.09.20` (`137260920`)

### Added

- Added the orange color palette to the theme settings.
- Added holiday greetings. The first time you open the app at Christmas or Halloween, it wishes you happy holidays and offers the holiday theme. Christmas comes with snow falling over the app. When the holiday is over, your usual theme comes back on its own.
- Added a secret: tap the build version five times in About to unlock seasonal themes. A new button on the Theme settings page then lets you keep the Christmas and Halloween themes in the color list all year and turn the snow on or off.
- Added a Dividers section to Components, showing the new wavy divider at its default size, resized live with sliders, and running vertically.

### Changed

- Improved how readable the colors are in every palette, especially buttons and links in the Android, yellow, and Halloween themes in light mode.
- The Christmas theme now mixes red with green and gold.
- The app no longer switches to the Christmas or Halloween colors on its own during the holidays. It asks first.
- The app's startup code is now compiled when it is installed or updated, instead of warming up over the first launches.
- The color picker in Theme settings shows brighter, truer previews of each palette, and the check on the selected one is always visible.
- Theme settings now opens with the color you're using already in view.
- Choosing Light in Theme settings now keeps the app light when battery saver is on. Follow system still turns dark with the rest of the phone.
- Ads on the Apps screen now stay loaded while you scroll. Scrolling back to an ad shows the same ad at once instead of an empty space while a new one loads.

### Fixed

- Fixed tapping the build version in About copying it to the clipboard every time. The other entries still copy their value.
- Fixed the Apps screen crashing when the app catalogue listed the same app more than once. Each app now appears once.

---

# September 20, 2026

**Version:** `26.09.18` (`137260918`), `26.09.19` (`137260919`)

### Changed

- The Animation Showcase in Components now lists each device animation and the media animation once instead of twice. The second copy of each was the same animation running backwards, which the existing replay mode control already plays by switching to Reverse. The device animations are also named for the device now rather than for one direction, so Light replaces Light on and Light off.
- The navigation drawer now shows the app's logo and name above its own destinations, with Settings, Help, Updates and Share moved to the bottom of the drawer.
- The Counter Quick Settings tile now shares its saved value with the in-app Counter, so the count persists across app and tile-service restarts instead of resetting when the service is recreated.
- Quick Settings tiles now declare themselves as Utilities on Android versions that support tile categories.

### Fixed

- Fixed the rate-the-app prompt appearing far too early. The app asked for a review every time you returned to the main screen rather than once per launch, so returning from Settings a few times was enough to count as three separate sessions and bring the prompt up minutes after installing. It now asks once per launch, as intended.
- Fixed the privacy consent check restarting every time you returned to the main screen. It now runs once per launch, which avoids repeated consent requests overlapping each other.
- Stopped re-checking for app updates on every return to the main screen once Google Play has already answered. An update that was interrupted partway through is still picked up when you come back, as before.
- Fixed Coin Flip, Dice Roll and Counter Quick Settings tiles not showing their results on Android 8 and 9, where tile subtitles are unavailable.
- Fixed Counter and Flash Dimmer tiles not always reflecting changes made elsewhere while the Quick Settings panel was open.
- Fixed Flash Dimmer showing `Off` when the flashlight could not actually be changed, such as while the camera is using it.
- Declining Android's add-tile prompt is no longer shown as an error.

---

# September 20, 2026

**Version:** `26.09.17` (`137260917`)

### Added

- Added shake to report gesture, allowing you to shake your device from any screen to quickly report an issue or send feedback.
- Added App Toolkit and Google Play services version details to the About screen.
- Added 30 new animated device and media icons to the Animation Showcase in Components, featuring smart home devices and audio controls.

### Improved

- Upgraded the issue reporter into a modern bottom sheet that opens directly over the current screen, with toast status alerts and a streamlined submission confirmation.
- Standardized settings category labels and navigation across all supported languages.
- Optimized release builds with Android Gradle Plugin 9 application optimization for improved performance and reduced app size.

---

# September 10, 2026

**Version:** `26.09.16` (`1370016`)

### Added

- Added the Reaction Test quick tool.
- Added Text Fields section in the Components showcase covering standard inputs, outlined errors, grouped fields, search boxes, and a Markdown editor with a formatting bar.
- Added Grouped Grid showcase in the Components.
- Added different buttons styles with Expressive sizes.
- Added Animation Playground with loop controls, playback speeds, and restart/reverse options.

### Removed

- Removed the Sound Mode, Caffeine, and Lux Meter.

### Improved

- Streamlined app details quick actions into a clean, grouped layout.
- Refined the expanded tool UI for Dice Roll, Coin Flip, Counter, and SOS with cleaner placement and animated counters.
- Upgraded buttons across the app to Material 3 Expressive styling with larger primary action targets for easier tapping.
- Polished the Components showcase layout, color swatches with one-tap copy, and smoother filter chip animations.
- Clarified Quick Settings tile status messages as "Not added" for a more intuitive setup experience.

### Fixed

- Fixed an issue where Quick Settings tile status updates were delayed after adding tiles.
- Fixed crashes caused by malformed widget action launches and added missing vibration permissions.

---

# August 28, 2026

**Version:** `28.08.13` (`1370013`)

### Added

- Added a **Help & feedback** shortcut to the top app bar on standalone General Settings screens.

### Improved

- Improved translations and wording across multiple screens.

### Fixed

- Fixed several navigation issues in the Sample App.

---

# August 24, 2026

**Version:** `26.08.9` (`1370009`)

Reliability-focused release with major improvements to startup, onboarding, Quick Tools, and the way
App Toolkit communicates one-time UI actions.

### Changed

- Reworked Quick Tools architecture to more closely follow the project's current architecture
  conventions.
- Quick Tools categories now remember whether they were expanded or collapsed.

### Improved

- Improved the Components screen layout and presentation.
- Improved translations after the Sample App modularization.
- Improved startup and onboarding behavior using the updated toolkit APIs.

### Fixed

- Fixed the startup dialog flow.
- Fixed several Sample App onboarding and navigation edge cases.

---

# August 20, 2026

**Version:** `26.08.8` (`1370008`)

Major structural update that separated reusable App Toolkit responsibilities from Sample
App-specific features much more clearly.

### Added

- Added the **Dim Flash** Quick Tool.
- Added offline caching for the developer applications catalogue.
- Added a dedicated Sample App integration module for App Toolkit.

### Changed

- Rebuilt the Sample App into dedicated app, core, feature, navigation, integration, and widget
  modules.
- Reorganized Sample App packages around clearer feature ownership.
- Updated the Sample App to demonstrate the new App Toolkit integration model.
- Removed battery percentage, battery temperature, and battery Quick Settings tools after moving
  that functionality out of the Sample App.

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

# July 8, 2026

**Version:** `26.07.2` (`1370002`)

Feature-heavy Sample App release alongside continued refinement of App Toolkit's reusable navigation
and UI foundations.

### Added

- Added **Toolkit Tiles** for discovering and managing supported Quick Settings tools.
- Added automatic Quick Settings tile-status synchronization.
- Added distinctive visual backgrounds for Toolkit Tile icons.
- Added the **Material Colors** Quick Tool.
- Added a new landing screen with Quick Tools previews.
- Added richer app-detail quick actions for notifications, permissions, storage, battery settings,
  sharing, and system settings.
- Added richer app information including installation state, version information, repository links,
  and privacy policy information.

### Changed

- Integrated Favorites into Apps & Tools instead of maintaining a separate top-level Favorites
  destination.
- Moved settings, Help, permissions, ads, licenses, and related toolkit screens into the main
  application navigation hierarchy.
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
