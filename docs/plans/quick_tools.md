# Quick Tools & Screen Redistribution Master Plan

This document details the completed improvements for Quick Tools (`:sample:feature:tiles`) and the incremental plan to redistribute existing tools into dedicated screens without duplicating standalone portfolio apps.

---

## 1. Core Product Philosophy & Portfolio Boundaries

1. **No Portfolio Duplication**:
   * **Shopping Lists & Discounts**: Already owned by *Shopping Cart Calculator* (`com.d4rk.cartcalculator`).
   * **Battery, Storage & Installed App Inspection**: Already owned by *Smart Cleaner for Android* (`com.d4rk.cleaner`).
   * **Network Tools**: Already owned by *Net Probe* (`com.d4rk.netprobe`).
   * **Sound Meter**: Already owned by a dedicated standalone app and requires `RECORD_AUDIO`.
   * **Scratchpad**: Dropped; we only redesign and reorganize capabilities we already have in App Toolkit.
2. **Untouched Screens**:
   * **Apps (`:sample:feature:apps`)**: Remains untouched in its current bottom navigation position.
   * **Quick Tools (`:sample:feature:tiles`)**: Remains in place as the home for Quick Settings tile-ready tools and instant system/utility actions. As tools migrate to new dedicated screens, they are removed from Quick Tools one by one.

---

## 2. Completed Foundation in `:sample:feature:tiles`

* **Counter Decrement (`-`)**: Clamped at zero with shared DataStore and Quick Settings tile synchronization.
* **Category Filters & Default Collapsed State**: Functional category chips (`All`, `Sensors`, `Utilities`, `System`, `Wellbeing`), all categories collapsed by default (`emptySet()`) on fresh install, persisted user expansion state, and hidden collapse controls when forced open.
* **Ad Placement & Stability**: App-open ads disabled; empty-state ads disabled; single native ad placed at the category midpoint; outer `Column + verticalScroll` layout preventing lookahead deactivation crashes during category expansion.
* **Polished Offline Tools**:
  * `UNIT_CONVERTER`: Length, Mass, Temperature, Volume, and Data with localized numeral/decimal normalization.
  * `RULER`: Full-screen edge-anchored mm/inch ruler with M3 segmented buttons and vertical 30 mm calibration reference.
  * `SCREEN_LIGHT`: Full-screen brightness and warmth controls with scrollable layout fallback.
  * `PROTRACTOR`: 0-180 degree draggable arm and accessible slider.
  * `BILL_SPLITTER`: M3 `primaryContainer` hero card, segmented 10%/15%/20% tip buttons, and bounded `+/-` people steppers.
  * `RANDOM_NUMBER`: Side-by-side Min/Max bounds, preset chips (`1-2`, `1-6`, `1-10`, `1-100`), and `AnimatedDigit` hero card.

---

## 3. Dedicated Screen Base & Incremental 1-by-1 Tool Migration

Instead of adding new unrelated features, we split our existing 17 tools into focused screens where the UI is tailored to the activity rather than a uniform catalog list.

### Tool Redistribution Map

| Destination Screen | Tools Hosted | Status | UI & Interaction Pattern |
| :--- | :--- | :--- | :--- |
| **1. Numbers Screen (`NumbersRoute`, `NumbersScreen`)** | • **Calculate & convert**: `BILL_SPLITTER`, `UNIT_CONVERTER`<br>• **Count & chance**: Live `COUNTER`, `RANDOM_NUMBER`, `DICE_ROLL` | **Completed** | Warm gold hero banner ("Count, split & convert"), clickable cards for Bill Splitter & Unit Converter, live interactive Counter card, and clickable cards for Random Number Picker & Dice Roll. |
| **2. Quick Tools** *(Remains)* | `COIN_FLIP`, `DICE_ROLL`, `COUNTER`, `FLASH_DIMMER`, `SOS`, `MORSE`, `SCREEN_LIGHT`, `BREATHING`, `REACTION_TEST`, `RULER`, `BUBBLE_LEVEL`, `COMPASS`, `PROTRACTOR`, `material_colors` | **Active** | Tile-ready tools and hardware/system/wellbeing controls. |
| **3. Apps** *(Untouched)* | Existing developer portfolio catalog | **Active** | Untouched 2-column app grid with Installed/Not installed filters and random app FAB. |
