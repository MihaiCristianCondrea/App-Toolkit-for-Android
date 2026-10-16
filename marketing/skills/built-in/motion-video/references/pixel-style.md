# Pixel and Material You motion style

Part of the [`motion-video`](../SKILL.md) skill. The films and stills it cites are listed in [`index.md`](index.md).

This document reverse-engineers the motion design grammar, compositional staging, shape play, typography, and color systems observed across official Google Pixel Feature Drop and Material You brand films.

---

## 1. Catalog of Analyzed Reference Films

Files are in this folder; [`index.md`](index.md) maps every still to its film and timestamp.

1. **`material-you-brand`** (45.1s; stills only)
   - *Core Subject*: Material Design 3 / Material You design language launch.
   - *Dominant Techniques*: Pure shape morphing, organic color extraction, squircle and scalloped geometry, fluid gesture tracking, variable typography.
2. **`films/pixel-feature-drop-2021-12.mp4`** (101.0s)
   - *Core Subject*: Feature release showcasing Digital Car Key, Quick Tap to Snap, Conversation Mode, and Nearby Share.
   - *Dominant Techniques*: Giant screen-filling Google Sans headlines, 4-lobed Material clover badges, split-screen typographic callouts, minimal floating device frames.
3. **`films/pixel-feature-drop-2022-03.mp4`** (87.1s)
   - *Core Subject*: Battery widget, Night Sight in Snapchat, YouTube live sharing, Live Caption for calls.
   - *Dominant Techniques*: Playful kinetic text ("can see" with googly eyes, "low-light" with organic shape reveal), off-center phone staging with floating squircle widget badges (`[=]`), massive duotone backgrounds.
4. **`films/pixel-feature-drop-2022-06.webm`** (96.3s)
   - *Core Subject*: Pocket Operator beat maker, At a Glance flashlight alert, COVID vaccine card shortcuts.
   - *Dominant Techniques*: **Pure shape play and typography**. Giant multi-line text ("turns your videos", "can add", "in Canada", "the front door", "with a battery widget") enveloped by huge animated organic color blobs, fluted scallops, and butter-yellow / terracotta / sage-green color blocking.

---

## 2. Core Architectural Pillars

### A. Primacy of "Shape Play" (The Material You Geometry)
Rather than displaying flat rectangular cards or complex UI spreadsheets, Google compositions are built from a distinctive vocabulary of **expressive geometric and organic shapes**:

1. **Scalloped / Fluted Cookies (12-sided & 16-sided)**:
   - Used for widgets, time, badges, and attention points.
   - Animate with continuous gentle rotation (6-12 deg/sec) and spring expansion.
2. **Organic Blobs / Amoebas / Kidney Beans**:
   - Giant soft, curved shapes that creep in from canvas corners and edges (`width: 40% - 60%` of viewport).
   - Serve as dynamic framing devices, shifting the viewer's focal point.
3. **4-Lobe Clovers & Rounded Crosses**:
   - Soft squircle variations with symmetrical indentation.
   - Used as central emblem stages for micro-interactions (e.g. car key, flashlight, sanitizer).
4. **Teardrops & Pebbles (Asymmetric Squircles)**:
   - Organic squircle with one or two sharper radii (e.g., `border-radius: 60px 60px 16px 60px`).
   - Creates tactile personality and natural warmth.
5. **Concentric Pills & Rings**:
   - Smooth pill containers that squash and stretch on tap.

### B. Kinetic Typography as the Hero
In these films, **text is not a caption: it is the primary visual actor**:
- **Scale**: Massive font sizing (typically 80px to 140px in vertical 9:16; 60px to 110px in landscape 16:9).
- **Weight**: Bold (`Google Sans` / `Google Sans Flex` at weight 600-800) for hero keywords, paired with medium (weight 400-500) for prepositions.
- **Rhythmic Delivery**: Words arrive in sync with the musical beat sheet. Often single words or two-word phrases drop in, scale with spring overshoot, and either exit or morph into the next statement.
- **Playful Character Interventions**:
  - The letters `"ee"` in `"see"` morphing into cartoon googly eyes.
  - Variable axis modulation: font width (`wdth`) stretching from condensed to wide, or weight (`wght`) increasing as items expand.
  - Staggered baseline reveal: words slide up through masked bounding boxes (`overflow: hidden`).

### C. UI as Graphic Prop (Anti-Mockup Principle)
**Never show a complete, dense app screen when a graphic fragment communicates faster.**
- **No full-screen clutter**: Avoid long lists, dense settings trees, or multi-tab app screens.
- **Isolate the Hero Fragment**:
  - Instead of an entire settings page, show a single **floating pill** with an icon and a toggle.
  - Instead of a full photo gallery, show **one photo framed in an organic scallop mask** with a duplicate collapsing into it.
  - Instead of a complex storage chart, show an **oversized tactile segmented ring** or **4 bold Material shapes** representing categories.
- **Tactile Squash & Stretch**: When a button is tapped, it physically compresses (`scale: 0.92`), ripples, and snaps back with elastic spring overshoot.

### D. Tonal Color Palettes (Material You Dynamic Theming)
Google rejects high-saturation neon or generic SaaS purple gradients. Instead, it employs **earthy, warm, nature-derived duotones and tri-tones**:

| Palette Group | Dominant Color | Supporting Pastel | Accent / Ink |
|---|---|---|---|
| **Sage & Meadow** | Soft Sage (`#8EA690`, `#A2C3B0`) | Pale Cream / Mint (`#E8F5E9`, `#E2F2E7`) | Deep Forest (`#0B1F14`, `#1B3B2B`) |
| **Terracotta & Coral** | Warm Terracotta (`#E06D53`, `#F28B82`) | Soft Blush Peach (`#FFD8D4`, `#FCE8E6`) | Deep Rust (`#8C2D19`) |
| **Butter & Chartreuse** | Pale Butter (`#FFF9C4`, `#F4F6B8`) | Fresh Chartreuse (`#D2E37D`, `#C4D600`) | Olive Ink (`#3B4214`) |
| **Lavender & Lilac** | Soft Lilac (`#D0BCFF`, `#CCC2DC`) | Pale Wisteria (`#E8DEF8`, `#F3EDF7`) | Deep Violet (`#21005D`, `#381E72`) |
| **Slate Navy** | Muted Denim (`#203248`, `#38495E`) | Powder Blue (`#D1E4FF`, `#A8C7FA`) | Crisp White (`#FFFFFF`) |

---

## 3. Concrete Choreography Breakdown by Scene Type

### Pattern 1: Bold Text + Corner Blob Wipes (June 2022 film)
```text
┌──────────────────────────────────────────────┐
│  (Large Sage Blob)                           │
│                                              │
│               can add                        │
│             [Google Sans]                    │
│                                              │
│                               (Coral Petal)  │
└──────────────────────────────────────────────┘
```
1. Screen is flooded with a warm background wash.
2. Two large organic blobs enter from opposite corners with lazy spring rotation.
3. Giant 2-line headline lands on the beat with masked reveal.
4. On the next beat, one blob expands to fill 100% of the screen, creating a seamless geometric color wipe to the next scene.

### Pattern 2: Graphic Badge + Minimal UI Fragment (The "Battery Widget" Pattern)
```text
┌──────────────────────────────────────────────┐
│                                              │
│     with a                                   │
│     battery                                  │
│     widget                                   │
│                                              │
│     ┌──────┐        ┌──────────────────┐     │
│     │ [=]  │        │ Tyler's Pixel 100% │     │
│     └──────┘        │ Pixel Buds   100% │     │
│     (Squircle)      └──────────────────┘     │
│                                              │
└──────────────────────────────────────────────┘
```
1. Huge stacked typography on the left.
2. An oversized squircle icon badge pops in below the text.
3. The isolated UI widget floats on the right, perfectly aligned and uncluttered.

### Pattern 3: The 4-Shape Category Matrix (The "Material Brand" Pattern)
```text
┌──────────────────────┬──────────────────────┐
│      Teardrop        │       Squircle       │
│      (Images)        │       (Videos)       │
├──────────────────────┼──────────────────────┤
│       Scallop        │         Pill         │
│       (Apps)         │     (Cleanable)      │
└──────────────────────┴──────────────────────┘
```
1. 4 distinct Material 3 shapes form a symmetrical, breathing grid.
2. Each shape holds an iconic glyph and a kinetic number.
3. On user interaction, one shape expands to command the entire composition.

---

## 4. Applying the style to a product film

1. **Lead with shape play.** Replace multi-card UI mocks with bold Material shapes: cookie badges for a feature's icon, corner blobs in the palette that scale and rotate through the film, squircle and pebble containers for photos and app icons.
2. **Make type the hero.** Set key phrases large, bring headlines in word by word through masks on the downbeats, and keep the copy short and conversational (the copy rules are in [`writing-style`](../../writing-style/SKILL.md)).
3. **Show features as symbols, not dense screens.** A badge, a big number or a pill with squash and stretch says more than a crowded list. When the app's real UI is shown, it comes from the project's UI kit and matches the app (see [`technical-rules.md`](technical-rules.md), "Fidelity").
4. **Move between scenes with shapes.** Expanding organic shapes, rotating cookie badges and scale-inversion wipes, each caused by something on screen.

---

## 5. Visual reference archive

Stills are in [`stills/<film>/`](stills/), named `<mm>-<ss>-<what-it-shows>.png`. Storyboards are in [`storyboards/`](storyboards/), one tile every 2 seconds. [`index.md`](index.md) describes each still and lists the moments worth studying in every film.

- **Material You brand**: [`stills/material-you-brand/`](stills/material-you-brand/). The 4-shape cluster, clock widgets, pastel palettes, floating pills.
- **Pixel Feature Drop, December 2021**: [`stills/pixel-feature-drop-2021-12/`](stills/pixel-feature-drop-2021-12/). Giant Google Sans headlines, 4-lobed clover badges, squircle badges, the Nearby Share layout.
- **Pixel Feature Drop, March 2022**: [`stills/pixel-feature-drop-2022-03/`](stills/pixel-feature-drop-2022-03/). The "can see" googly eyes, "low-light" with a corner reveal, the battery widget badge.
- **Pixel Feature Drop, June 2022**: [`stills/pixel-feature-drop-2022-06/`](stills/pixel-feature-drop-2022-06/). Shape play and kinetic text ("can add", "in Canada", "the front door") framed by corner blobs.
