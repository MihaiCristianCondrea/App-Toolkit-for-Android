---
title: "Chips"
source_url: "https://m3.material.io/components/chips/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/94e7b51fb887-lzthj7vk-1.png"
  - "assets/images/83589bd4c6c3-m8sdpshu-02.png"
  - "assets/images/a41025871955-m8se5ler-05.png"
  - "assets/images/f652fd25cc5e-m8se6c0w-06.png"
---

# Chips

Chips help people enter information, make selections, filter content, or trigger actions

## Overview

-   Use chips to show options for a specific context

-   Four variants: assist, filter, input, and suggestion

-   Chip elevation defaults to 0 but can be elevated if they need more visual separation

![4 chip variants.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flzthj7vk-1.png?alt=media&token=87bf4249-1c98-406e-bf83-32e0e1b6d5a6>)

_Assist chip; Filter chip; Input chip; Suggestion chip_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Chip.md |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/chip.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/chip |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/ThemeData/useMaterial3.html |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## Updates

**Aug 2024**

Updated stroke color from **outline** to **outline variant**.

![A chip with a clear outline is now a chip with a subtle outline.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sdpshu-02.png?alt=media&token=66986121-1317-4638-8ba4-3119d622eada>)

_The stroke color was softened to improve visual hierarchy between chips and buttons_

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color

-   Shape: Rounded rectangle 

-   Variants: Action chips have been separated into assist chips and suggestion chips. Choice chips are now a subset of filter chips

![M2 chip variants.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8se5ler-05.png?alt=media&token=dd95b9eb-911c-4a6d-a1be-1052ac547d3e>)

_M2: Variants of chips are input, choice, filter, and action chips_

![M3 chip variants.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm8se6c0w-06.png?alt=media&token=8c430631-a8f6-47e3-98e7-32b23f2c4eea>)

_M3: Variants of chips updated to assist, filter, input, and suggestion chips_
