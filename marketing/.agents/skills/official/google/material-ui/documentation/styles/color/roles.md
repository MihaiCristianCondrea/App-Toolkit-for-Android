---
title: "Color roles"
source_url: "https://m3.material.io/styles/color/roles"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/1a32091256d8-ly2ms4t2-1.png"
  - "assets/images/426dc53eee92-ly2msstb-2.png"
  - "assets/images/0bf1cfa38c44-ly2mt0z5-3.png"
  - "assets/images/a3d20450511a-m7atpmau-04.png"
  - "assets/images/2e23ad3b61c8-ly2mtbil-4.png"
  - "assets/images/2acc40d6d8ea-ly2muo2k-5.png"
  - "assets/images/08f251bbbb80-m2kwfth2-6.png"
  - "assets/images/ab898d9befa3-ly2mvccz-7.png"
  - "assets/images/077a888057cc-ly2mvlj0-8.png"
  - "assets/images/d815f4cedf27-ly2mzjt8-9.png"
  - "assets/images/11a1daef333e-ly2n0pba-10.png"
  - "assets/images/f71d478674b5-ly2n10jv-11.png"
  - "assets/images/4a9f09b6e695-ly2n1cbu-12.png"
  - "assets/videos/c0aa757b759d-ly2njabe-GM3-Styles-Color-Color_roles-1-v01.mp4"
  - "assets/images/4ec5661d372a-ly2n20l4-14.png"
  - "assets/images/fdf945671117-ly2n2btl-15.png"
  - "assets/videos/458e3e3e3756-ly2nit0j-GM3-Styles-Color-Color_roles-2-v01.mp4"
  - "assets/images/4043b4d5d33b-ly2n2wcp-17.png"
  - "assets/images/a83edae48197-ly2n4uvc-24.png"
  - "assets/images/a234fd00a30a-ly2n6f78-25.png"
  - "assets/images/84fe2656893b-ly2n6s89-18.png"
  - "assets/images/25ea4346e130-ly2n7081-19.png"
  - "assets/images/957e6a129478-ly2n7i8q-20_dont.png"
  - "assets/images/07a4ab854ac9-ly2n7yd0-21_dont.png"
  - "assets/images/e3b33d98e1db-m38uvq2h-23_dont.png"
  - "assets/images/06c1100e1f7f-m4iofvjf-23_caution.png"
  - "assets/images/6f8447030fb9-ly2n8qum-26.png"
  - "assets/images/b1c059d91857-ly2n922v-27.png"
  - "assets/images/bec115238814-ly2n9b76-28.png"
  - "assets/images/ff1b97465633-ly2n9m0g-29.png"
  - "assets/images/4f995ddea403-ly2n9vnb-30_dont.png"
  - "assets/images/55ac7e293740-ly2na1i9-31_do.png"
  - "assets/images/d2689c8ccbcf-ly2nacto-32.png"
  - "assets/images/8c77a7dce6c6-ly2nappf-33.png"
  - "assets/images/06281861d2a3-ly2nb208-34.png"
  - "assets/images/a1fd3c62ac0b-ly2nbbzl-35.png"
  - "assets/images/109e00d05a75-ly2nf2l7-36.png"
  - "assets/images/668e405887df-ly2nfaia-37.png"
  - "assets/images/dacf21bca0dd-ly2nfhyc-38.png"
  - "assets/images/0f4a124e4a61-ly2nep5x-39.png"
  - "assets/images/dc2cd2e6c510-ly2nfqah-40.png"
---

# Color roles

There are 26 standard color roles organized into six groups: primary, secondary, tertiary, error, surface, and outline

## What are color roles?

Color roles are like the "numbers" in a paint-by-number canvas. They're the connective tissue between elements of the UI and what color goes where.

-   **Color roles are mapped to Material Components**
    You'll use these color roles whether you're using the static baseline scheme or dynamic color. If your product contains custom components, they'll need to be properly mapped to this set of color roles.
-   **Color roles ensure accessibility**
    The color system is built on accessible color pairings. These color pairs provide an accessible minimum 3:1 contrast.
-   **Color roles are tokenized**
    Roles are implemented in design and code through tokens. A design token represents a small, reusable design decision that’s part of a design system's visual style.

![Example color swatches for all 45 color roles including Primary, Secondary, Tertiary, Error, Surfaces, Inverse roles, Scrim and Shadow roles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2ms4t2-1.png?alt=media&token=722d8f55-45a4-4340-98ad-9ae1aa71b7ae>)

_Diagram of all Material color roles, including optional add-on roles for surface colors and fixed accent colors_

## General concepts

Here are helpful-to-know words you'll see in the names of color roles:

-   **Surface** – A role used for backgrounds and large, low-emphasis areas of the screen.
-   **Primary, Secondary, Tertiary** – Accent color roles used to emphasize or de-emphasize foreground elements.
-   **Container** – Roles used as a fill color for foreground elements like buttons. They should not be used for text or icons.
-   **On** – Roles starting with this term indicate a color for text or icons _on top_ of its paired parent color. For example, **on primary** is used for text and icons against the **primary** fill color.
-   **Variant** – Roles ending with this term offer a lower emphasis alternative to its non-variant pair. For example, **outline variant** is a less emphasized version of the **outline** color.

### Pairing and layering colors

To ensure accessible visual contrast in your app, apply colors only in the intended pairs or layering orders described in the following sections.

Combining colors improperly may break contrast necessary for visual accessibility, particularly when colors are adjusted through dynamic color features such as user-controlled contrast.

![Pair of buttons shown at three different contrast levels. At every level, the text on the button is legible because the color roles are properly applied to the buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2msstb-2.png?alt=media&token=1e401465-0ddb-4506-bc85-8e943e993490>)

_Pair and layer color roles as intended to ensure expected visual results and accessibility. In this example, the two buttons mapped with (1) primary, (2) on primary, (3) secondary container, and (4) on secondary container stay legible as the contrast level changes._

![Pair of buttons shown at 3 different contrast levels. The text on the button becomes illegible because the color roles are improperly applied to the buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2mt0z5-3.png?alt=media&token=4129b18b-0f80-4b92-b2cc-f6d2abeb4947>)

_Improper color mappings can produce unintended visual results and break accessibility. In this example, the two buttons mapped with (1) primary, (2) primary container, (3) secondary container, and (4) on surface become illegible as the contrast level changes._

## Accent color roles

Accent color roles include primary, secondary, and tertiary. Assign them to elements in the UI based on importance and needed emphasis. Use caution when changing color roles for visual effect.

-   **Primary roles** are for important actions and elements needing the most emphasis, like a FAB to start a new message.


-   **Secondary roles** are for elements that don’t need immediate attention and don’t need emphasis, like the selected state of a navigation icon or a dismissive button.


-   **Tertiary roles** are for smaller elements that need special emphasis but don't require immediate attention, such as a badge or notification.

![A mail app showing a mix of primary, secondary, and tertiary accent colors used across the screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fm7atpmau-04.png?alt=media&token=0a06e58e-26a7-4052-ae6a-5e34143ee4b2>)

_Primary colors are used for the most important elements, like a starred message and FAB to start a new message; Tertiary colors are used for smaller emphasized elements, like a badge suggesting a follow up; Secondary colors are used for less urgent elements, like the background of an inactive star icon and the selection state of an inbox icon_

## Primary

Use primary roles for the most prominent components across the UI, such as the FAB, high-emphasis buttons, and active states.

-   **Primary:** High-emphasis fills, texts, and icons against surface


-   **On primary:** Text and icons against primary


-   **Primary container:** Standout fill color against surface, for key components like FAB


-   **On primary container:** Text and icons against primary container

![4 color swatches: primary, on primary, primary container and on primary container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2mtbil-4.png?alt=media&token=77137c3b-3782-4366-9d88-729623c2d6c1>)

_Primary color roles include primary and primary container, with their respective “On” colors_

![Primary color used for filled button, and "on primary" color used for the text within that button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2muo2k-5.png?alt=media&token=8ba5fb9a-6079-4dbb-9023-77f1f169e4f8>)

_On primary; Primary_

!["Primary container" color used for FAB and "on primary container" color used for the text and icon within.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm2kwfth2-6.png?alt=media&token=53da80e7-f3f8-4279-9e7c-0400302889fc>)

_On primary container; Primary container_

## Secondary

Use secondary roles for less prominent components in the UI such as filter chips.

There are four secondary roles:

-   **Secondary:** Less prominent fills, text, and icons against surface


-   **On secondary:** Text and icons against secondary


-   **Secondary container:** Less prominent fill color against surface, for recessive components like tonal buttons


-   **On secondary container:** Text and icons against secondary container

![4 color swatches: secondary, on secondary, secondary container, and on secondary container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2mvccz-7.png?alt=media&token=942165b4-8244-4142-8165-aa7667e4a662>)

_Secondary color roles include secondary and secondary container, with their respective "On" colors_

![An icon button using "secondary container" color, and an icon using "on secondary color."](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2mvlj0-8.png?alt=media&token=1443111c-0408-4584-98a9-34980437c35b>)

_Icon: On secondary container; Button: Secondary container_

## Tertiary

Use tertiary roles for contrasting accents that balance primary and secondary colors or bring heightened attention to an element such as an input field.

There are four tertiary roles:

-   **Tertiary:** Complementary fills, text, and icons against surface


-   **On tertiary:** Text and icons against tertiary


-   **Tertiary container:** Complementary container color against surface, for components like input fields


-   **On tertiary container:** Text and icons against tertiary container

![4 color swatches: tertiary, on tertiary, tertiary container, and on tertiary container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2mzjt8-9.png?alt=media&token=9a14358b-d46f-4bbf-854b-8110f14af4e6>)

_Tertiary color roles include tertiary and tertiary container, with their respective "On" colors_

The tertiary color roles can be applied at the designer's discretion. They're intended to support broader color expression.

![A selected element using Tertiary Container as a background color and On Tertiary Container for text. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n0pba-10.png?alt=media&token=c8225d06-dcbd-47da-aa77-b275550f4002>)

_On tertiary container; Tertiary container_

## Error

Use error roles to communicate error states, such as an incorrect password entered into a text field.

There are four error roles:

-   **Error:** Attention-grabbing color against surface for fills, icons, and text, indicating urgency


-   **On error:** Text and icons against error


-   **Error container:** Attention-grabbing fill color against surface


-   **On error container:** Text and icons against error container

Error is an example of a static color (it doesn't change even in dynamic color schemes). Error color roles are made static by default with any dynamic color scheme. They still adapt to light and dark theme.

![4 color swatches: Error, On error, Error container and On error container roles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n10jv-11.png?alt=media&token=2894bbf7-1507-46e4-af6d-a69436f2f89c>)

_Error color roles include error and error container with their respective "On" colors._

## Surface

Use surface roles for more neutral backgrounds, and container colors for components like cards, sheets, and dialogs.

There are three surface roles:

-   **Surface:** Default color for backgrounds


-   **On surface:** Text and icons against any **surface** or **surface container** color


-   **On surface variant:** Lower-emphasis color for text and icons against any **surface** or **surface container** color

![3 color swatches: Surface, On surface and On surface variant.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n1cbu-12.png?alt=media&token=622ece1d-3225-4c06-ba60-ab3d5ae4f717>)

_Surface and on surface roles in light theme_

There are also five surface container roles named based on their level of emphasis:

-   **Surface container lowest:** Lowest-emphasis container color


-   **Surface container low:** Low-emphasis container color


-   **Surface container:** Default container color


-   **Surface container high:** High-emphasis container color


-   **Surface container highest:** Highest-emphasis container color


**Surface container** is the default role, but the others are especially helpful for creating hierarchy and nested containers in [layouts for expanded screens](../../foundations/layout/breakpoints/expanded.md).

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2njabe-GM3-Styles-Color-Color_roles-1-v01.mp4?alt=media&amp;token=8aa7d283-aff1-4ba2-b146-ea330dc45998" title="4 color swatches in light and dark theme: Surface container lowest, Surface container low, Surface container, Surface container high and Surface container highest."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2njabe-GM3-Styles-Color-Color_roles-1-v01.mp4?alt=media&token=8aa7d283-aff1-4ba2-b146-ea330dc45998>)

The most common combination of surface roles uses **surface** for a background area and **surface container** for a navigation area.

Text and icons typically use **on surface** and **on surface variant** on all types of surfaces.

![Email app using Surface for the main background color and Surface Container for the navigation bar background](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n20l4-14.png?alt=media&token=0b8d1cc2-2a4f-4c58-ab0e-393b0cd1996a>)

_Surface; Surface container_

All color mappings – but especially surface colors – should remain the same for layout regions across window size classes. For example, the body area will use the **surface** color and the navigation area will use the **surface container** color on both mobile and tablet.

![Mobile and tablet screens both using Surface for main background and surface container for navigation background.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n2btl-15.png?alt=media&token=1cd58070-3840-4e53-b160-df4309cbf43b>)

_Surface; Surface container_

Depending on necessary hierarchy, feature area, and design logic, you can use [add-on surface colors](https://m3.material.io/m3/pages/color-roles/tab-1#63d6db08-59e2-4341-ac33-9509eefd9b4f) in larger window class sizes as long as colors are consistently applied.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nit0j-GM3-Styles-Color-Color_roles-2-v01.mp4?alt=media&amp;token=f11fcb96-ea21-487e-9e1a-a4517190a291" title="Mobile, foldable, and tablet screens showing how the body and navigation regions have the same color roles across window sizes."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nit0j-GM3-Styles-Color-Color_roles-2-v01.mp4?alt=media&token=f11fcb96-ea21-487e-9e1a-a4517190a291>)

By default, neutral-colored components such as navigation bars, menus, or dialogs are mapped to specific surface container roles, but these [roles can be remapped](advanced/overview.md) by makers to suit user needs.

![Surface container low applied to an elevated button and card, surface container applied to the top and bottom bar, surface container high applied to the FAB and basic dialog, surface container highest applied to an input label and off switch.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n2wcp-17.png?alt=media&token=b86187a0-00e2-4546-9a5f-35593b0297a8>)

_Default surface container roles applied to components: Surface container low; Surface container; Surface container high; Surface container highest_

### Inverse colors

Inverse roles are applied selectively to components to achieve colors that are the reverse of those in the surrounding UI, creating a contrasting effect.

-   **Inverse surface:** Background fills for elements which contrast against surface


-   **Inverse on surface:** Text and icons against inverse surface


-   **I****nverse primary:** Actionable elements, such as text buttons, against inverse surface

![3 color swatches: Inverse surface, inverse on surface and inverse primary roles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n4uvc-24.png?alt=media&token=a0ac78ab-206a-4017-a93b-65caa80157c6>)

_Inverse surface, inverse on surface, and inverse primary roles in the color scheme, shown in light theme_

![Snackbar component using inverse surface for its background, inverse on surface for its text and inverse primary of its text button, shown in light theme](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n6f78-25.png?alt=media&token=f536573c-5c9a-440d-bac9-723c7a65c094>)

_A snackbar which uses: Inverse surface for its background; Inverse on surface for its text; Inverse primary for its text button; /_

## Outline

There are two outline colors to be used against a surface:

-   **Outline:** Important boundaries, such as a text field outline


-   **Outline variant:** Decorative elements, such as dividers, and when other elements provide 4.5:1 contrast

![2 color swatches: Outline and outline variant](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n6s89-18.png?alt=media&token=42a74524-96ff-451f-ab8f-2ea1de7ba188>)

_Outline and outline variant roles in the color scheme, shown in light theme_

![Diagram comparing used of outline and outline variant.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n7081-19.png?alt=media&token=8b2ad35a-0b3b-4652-8de4-204c51723221>)

_A text field which uses outline for its container border; A list item which uses outline variant for its divider line; /_

![Outline color incorrectly used for dividers](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n7i8q-20_dont.png?alt=media&token=accae0d3-0a04-49f5-8f74-10eb22452dc6>)

_Don’t use the outline color for dividers since they have different contrast requirements. Instead, use outline variant._

![Outline color incorrectly used for cards.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n7yd0-21_dont.png?alt=media&token=10fb9ebd-55e3-4afc-9081-8682017fcbdd>)

_Don’t use the outline color for components that contain multiple elements, such as cards. Instead, use outline variant._

![Outline variant color incorrectly used for input and filter chips.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm38uvq2h-23_dont.png?alt=media&token=294e6a8a-6a3a-4dc0-aac8-eeea104c7115>)

_Don’t use the outline variant color to create visual hierarchy or define the visual boundary of targets. Instead, use the outline color or another color providing 3:1 contrast with the surface color._

![Outline variant incorrectly color used for chips.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4iofvjf-23_caution.png?alt=media&token=be594ca5-e4d1-47e5-a2d6-850e59b1720a>)

_The outline variant color can be used for the border of targets like chips and buttons, provided that those targets contain elements inside them that provide visual contrast. In this example of chips and buttons, the icons and text inside the targets meet 4.5:1 contrast._

## Add-on color roles

Most products won't need to use these add-on color roles. However, some products require the greater flexibility and control that add-on roles provide. If you aren't sure whether your product should use the add-on roles, it probably shouldn't and you can ignore them.

### Fixed accent colors

**Primary fixed, secondary fixed,** and **t****ertiary fixed** are fill colors used against surface. These colors maintain the same tone in light and dark themes, as opposed to regular container colors, which change in tone between these themes. The fixed color role may be used instead of the equivalent container role in situations where such fixed behavior is desired.

The **primary fixed dim**, **secondary fixed dim**, and **tertiary fixed dim** roles provide a stronger, more emphasized tone relative to the equivalent fixed color. They may be used where a deeper color but the same fixed behavior is desired.

![6 color swatches: Primary, secondary and tertiary fixed swatches, along with their darker Dim counterparts, shown in both light and dark theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n8qum-26.png?alt=media&token=15044478-5a62-4704-a373-6ba69cbceebd>)

_Fixed and fixed dim color roles for the primary, secondary, and tertiary color groups, shown in both light and dark themes. Note how the colors stay the same between themes._

![FAB in light and dark theme, using the primary fixed role for its container fill color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n922v-27.png?alt=media&token=0cd22363-7942-4433-876f-89a575543901>)

_A FAB which uses primary fixed (1) for its container fill color, shown in light and dark themes. Note how the container color stays the same between themes._

![FAB in light and dark theme, using the primary container role for its container fill color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n9b76-28.png?alt=media&token=c3e3b6d1-90d1-41ca-b96e-7e9461474217>)

_For comparison, a FAB which uses primary container (2) for its container fill color, shown in light and dark themes. Note how the container color changes tone between themes._

![2 email app screens using primary fixed and primary fixed dim.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n9m0g-29.png?alt=media&token=a35b823b-bde5-4421-83df-d71bc48954f5>)

_Examples of fixed and fixed dim colors in use: Primary fixed for a FAB container color; Primary fixed dim for an icon button container; /_

![Permissions screen with Surface used for the background and Primary Fixed incorrectly used for the button fill.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2n9vnb-30_dont.png?alt=media&token=2139b6e3-d8c3-4839-8c39-58d2884edd3a>)

_Fixed colors don't change based on light or dark theme, so they're likely to cause contrast issues. Avoid using them where contrast is necessary. / / / / / /_

![Permissions screen with Surface used for the background and Primary correctly used for the button fill.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2na1i9-31_do.png?alt=media&token=f9e9816f-4017-47ac-9f00-2ca0300ed145>)

_Use primary, secondary, and tertiary roles for accent colors where contrast is needed /_

### On fixed accent colors

**On fixed** colors are used for text and icons which sit on top of the corresponding Fixed color. For example, **on** **primary fixed** is used for text and icons against the **primary fixed** color. The same usage applies for the equivalent secondary and tertiary colors.


**On fixed variant** colors are used for text and icons needing lower emphasis against the corresponding fixed color. For example, **on primary fixed variant** is used for low emphasis text and icons against the **primary fixed** color. The same usage applies for the equivalent secondary and tertiary colors.

![6 color swatches for on fixed and on fixed variant](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nacto-32.png?alt=media&token=07de8ed6-affe-40e8-a005-5f358f0e1f6a>)

_On fixed and on fixed variant color roles for the primary, secondary, and tertiary accent groups, shown in both light and dark themes_

![Banner in a news app using on primary fixed variant and on primary fixed](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nappf-33.png?alt=media&token=560dab4a-68fc-4c3a-8690-00fb04297739>)

_A customized banner which uses on primary fixed variant (1) for its deemphasized text and (2) on primary fixed for its emphasized text_

### Bright and dim surface roles

There are two add-on surface roles:

-   **Surface dim:** Dimmest surface color in light and dark themes
-   **Surface bright:** Brightest surface color in light and dark themes

![3 color swatches: Surface dim, surface, and surface bright](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nb208-34.png?alt=media&token=66472140-f56e-4ea0-a435-d96321b728ee>)

_Surface dim, surface, and surface bright in light theme_

![3 color swatches: Surface dim, surface, and surface bright](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nbbzl-35.png?alt=media&token=48ac623d-4455-4246-a766-ca4042d7d5f7>)

_Surface dim, surface, and surface bright in dark theme_

While the default **surface** color automatically inverts between light and dark themes (it’s a light color in light theme and it flips to a dark color in dark theme), the **surface bright** and **surface dim** colors invert in a slightly different way. More precisely, they keep their relative brightness across both light and dark theme.

For example, in an interface using the default **surface** role, the mapped area is the brightest in light theme and the dimmest in dark theme. In an interface using the **surface bright** role, the mapped area is the brightest in both light and dark theme.

![UI with surface role applied to the body area and surface container applied to the navigation area, shown in light theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nf2l7-36.png?alt=media&token=e51168a4-cbb7-4898-9d6a-c3e2a1853fef>)

_Light theme Surface; Surface container_

![UI with surface role applied to the body area and surface container applied to the navigation area, shown in dark theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nfaia-37.png?alt=media&token=e5e708b8-7301-4d1a-ac38-698439225653>)

_Dark theme Surface; Surface container_

![UI with surface bright role applied to the body area and surface container applied to the navigation area, shown in light theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nfhyc-38.png?alt=media&token=0f6d4117-d6b2-4816-83f0-9b012fa748cd>)

_Light theme Surface bright; Surface container; /_

![UI with surface bright role applied to the body area and surface container applied to the navigation area, shown in dark theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nep5x-39.png?alt=media&token=067cab0d-8d1c-4c8b-b730-d144aa368d0f>)

_Dark theme Surface bright; Surface container; /_

![Large screen chat UI. The surface dim role is applied to the left navigation rail and the surface bright role is applied to the chat window.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2nfqah-40.png?alt=media&token=562063ff-97b5-470c-8664-92eed7f843d5>)

_Navigation rail with surface dim background; Chat window with surface bright background; /_
