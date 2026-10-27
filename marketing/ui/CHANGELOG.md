# Changelog

---

# Unreleased

### Changed

- Pin Material Next to tested source commit `03707ca` for the simplified gallery, automatic detail frames, the shared copyright footer, a drawer whose footer stays put while its destinations scroll, and a launcher without App Toolkit.

- Moved the screen gallery, preview settings, prototype inspector, comparisons and device frames to Material Next 0.0.2. The desktop navigation drawer starts closed.
- Updated to Material Next 0.0.3: preview settings open from a button in the gallery's top app bar, the prototype scales the device to fit without scrolling or an outlined card, and the inspector no longer repeats each screen's description and Compose source. Screen descriptions are now one plain line for the gallery cards.
- Kept the App Toolkit screen renderers and the motion embed entry point, with shared window rules and assets supplied by Material Next.

### Fixed

- The marketing launchers refresh dependencies after updates even when `node_modules` already exists. Startup stops on installation errors, and the Windows launcher keeps the error visible.

### Removed

- Removed Motion and Specifications from the hub drawer, the product version footer and the Components screen. The catalog now starts with Quick Tools, followed by Numbers and Apps.
