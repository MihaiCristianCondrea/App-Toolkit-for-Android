---
title: "Buttons"
source_url: "https://m3.material.io/components/buttons/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/108b24f85d53-mmm1qv5u-01.png"
  - "assets/images/015c1315a60f-mmm1tos0-03.png"
  - "assets/images/8feba5852b6e-mmm1vatd-04.png"
  - "assets/images/22971d1e1b5a-mmm1vtk3-05.png"
---

# Buttons

Buttons prompt most actions in a UI

## Overview

-   Two variants: default and toggle

-   Can contain an optional leading icon

-   Five color options: elevated, filled, tonal, outlined, and text

-   Five size recommendations: extra small, small, medium, large, and extra large

-   Two shape options: round and square

-   Keep labels concise and use sentence case

![5 variants of buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm1qv5u-01.png?alt=media&token=70d30001-94b9-4bc2-a894-3b71a78dfb2e>)

_Elevated button; Filled button; Filled tonal button; Outlined button; Text button_

## Availability & resources

### STATUS_TABLE

| Type | Resource | Status | URL |
| --- | --- | --- | --- |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android) | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md |
| ANDROID_IMPLEMENTATION | Android Views (MDC-Android): Expressive | AVAILABLE | https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose: Expressive | AVAILABLE | https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#Button(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.material3.ButtonColors,androidx.compose.material3.ButtonElevation,androidx.compose.foundation.BorderStroke,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1) |
| LIT_IMPLEMENTATION | Web: Expressive | UNAVAILABLE |  |
| LIT_IMPLEMENTATION | Web | AVAILABLE | https://github.com/material-components/material-web/blob/main/docs/components/button.md |
| JETPACK_IMPLEMENTATION | Jetpack Compose | AVAILABLE | https://developer.android.com/develop/ui/compose/components/button |
| FIGMA | Design Kit (Figma) | AVAILABLE | https://www.figma.com/community/file/1035203688168086460 |
| FLUTTER_IMPLEMENTATION | Flutter | AVAILABLE | https://api.flutter.dev/flutter/material/ThemeData/useMaterial3.html |

## M3 Expressive update

**May 2025**

Buttons now have a wider variety of shapes and sizes, toggle functionality, and can change shape when selected. [More on M3 Expressive](../../blog/building-with-m3-expressive.md)

Variants and naming:

-   Default and toggle (selection)

-   Color styles are now configurations (elevated, filled, tonal, outlined, text)


Shapes: 

-   Round and square

-   Shape morphs when pressed

-   Shape morphs when selected


Sizes:

-   Extra small

-   Small (existing, default)

-   Medium

-   Large

-   Extra large


New padding for **small** buttons:

-   16dp (recommended to match padding of new sizes)

-   24dp (no longer recommended)

![4 button changes in the expressive update.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm1tos0-03.png?alt=media&token=dd50e746-4e7b-41f5-8e51-1f26afb86139>)

_Five sizes; Toggle (selection); Two shapes; Two small padding widths_

## Differences from M2

-   Color: New color mappings and compatibility with dynamic color. Icons and labels now share the same color. Neutral text button is no longer recommended.

-   Icons: Standard size for leading and trailing icons is now 20dp

-   Shape: Fully-rounded corner radius and additional height options

![Rectangular M2 buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm1vatd-04.png?alt=media&token=9224dca7-f21c-4dff-812d-3dfb3fa035e6>)

_M2: Buttons have a height of 36dp and slightly rounded corner radius_

![Round-cornered M3 buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm1vtk3-05.png?alt=media&token=1f0e0136-6117-4e71-8d6c-d006b6ef3804>)

_M3: Default buttons are taller at 40dp and have fully rounded corners_
