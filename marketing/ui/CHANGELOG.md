# Changelog

---

# Unreleased

### Changed

- Use Material-Next 0.2.0 from npm: every framed phone, in the gallery, the prototype page, captures and the store listing, is Material-Next's own 3D Pixel 9 Pro XL. The BlenderKit model, its Draco decoder and `src/kit/pixel-3d.js` are gone, and three.js comes with Material-Next.
- Use Material Next `0.0.7` from npm for launcher Edit-mode and drag animations and compact drawer alignment.
- Use Material Next 0.0.6 from npm. The gallery and each screen's prototype are one page (`mountMarketingHub`): opening a screen no longer reloads, and All screens or Back return where the gallery was scrolled. The gallery and the reference pages have no drawer, menu button or footer; the reference pages get a back button. The device fills the stage height, and the palette menu no longer makes the settings panel scroll.
- Used Material Next 0.0.4 from npm for the simplified gallery, automatic detail frames, the shared copyright footer, a drawer whose footer stays put while its destinations scroll, and a launcher without App Toolkit.

- Moved the screen gallery, preview settings, prototype inspector, comparisons and device frames to Material Next 0.0.2. The desktop navigation drawer starts closed.
- Updated to Material Next 0.0.3: preview settings open from a button in the gallery's top app bar, the prototype scales the device to fit without scrolling or an outlined card, and the inspector no longer repeats each screen's description and Compose source. Screen descriptions are now one plain line for the gallery cards.
- Kept the App Toolkit screen renderers and the motion embed entry point, with shared window rules and assets supplied by Material Next.

### Fixed

- Tip & Bill Splitter uses the Android currency-code button and dropdown, preserving more room for the bill amount.

- Water Eject previews match the Android readout, visual size, button labels and guidance. Stills can hold a running countdown without starting audio or resetting the preview.

- Keep Open random app 16px above navigation in phone, fold and desktop previews, including three-button navigation and RTL. Browser checks use fixture data to verify its bounds.

- The marketing launchers refresh dependencies after updates even when `node_modules` already exists. Startup stops on installation errors, and the Windows launcher keeps the error visible.

### Removed

- Removed Motion and Specifications from the hub drawer, the product version footer and the Components screen. The catalog now starts with Quick Tools, followed by Numbers and Apps.
