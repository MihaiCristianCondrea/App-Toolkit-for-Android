---
title: "Time pickers"
source_url: "https://m3.material.io/components/time-pickers/overview"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/e604dc558325-mcyz4dh2-01.png"
  - "assets/images/83e9f2b632c9-mcz00a3d-02.png"
  - "assets/images/b122a101d332-mcz04x96-03.png"
---

# Time pickers

## Overview

-   Time pickers are modal and cover the main content

-   Two variants: dial and input

-   People can select hours, minutes, or periods of time

-   Make sure time can easily be selected by hand on a mobile device

![Dial time picker dial and input time picker.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmcyz4dh2-01.png?alt=media&token=726495da-d131-42f3-a766-cd787c29a235>)

_Time picker dial; Time picker input_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/TimePicker.md |
| LIT_IMPLEMENTATION | Web | UNAVAILABLE |  |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/TimePickerDialog-class.html |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/time-pickers |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color

![Time picker’s old color mappings. The selected hour of 7 and AM text is purple, on a purple background.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmcz00a3d-02.png?alt=media&token=195f2e65-d46e-466a-b952-e7cfa9761d48>)

_M2: Time pickers had different color mappings_

![Time picker's new color mappings. The selected hour of 7 and AM text is black, with different background colors.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmcz04x96-03.png?alt=media&token=a2f4ba54-7a25-4967-b3d9-bb113e40713e>)

_M3: Time pickers have new color mappings compatible with dynamic color_
