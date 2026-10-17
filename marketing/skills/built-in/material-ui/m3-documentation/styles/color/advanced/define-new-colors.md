---
title: "Advanced customizations"
source_url: "https://m3.material.io/styles/color/advanced/define-new-colors"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/8580963d7d63-lwt7wrjx-1.png"
  - "assets/images/07b6a9f6cf34-lwt7xb07-2.png"
  - "assets/images/07de476fa932-lwt7xlqa-3.png"
  - "assets/images/f789888ba792-lwt7y1c2-4.png"
---

# Advanced customizations

Apply, define, or adjust colors to create a fine-tuned, unique color experience

## Define new colors

You can add colors to your scheme to extend the color roles provided by Material out of the box.

## Define static colors

_Formerly known as custom colors_

You can define additional colors in your scheme that stay static even when other colors dynamically change. When you input a desired reference color, Material will return four derived color roles that align with the design of existing roles in the color scheme.

![Diagram showing (1) a green circle, with an arrow leading from it to (2) a set of four color chips named Success, On Success, Success Container, and On Success Container. Below (3 and 4), the green Success colors are applied to a home control UI.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7wrjx-1.png?alt=media&token=ca9d1728-afc1-4229-8280-ae94ad9f31f8>)

_In this example, a static green color called Success is defined in addition to the scheme, and applied to UI to indicate a success state. Green source color used to generate color values for four new color roles; A set of new "Success" color roles derived from the source color; On success container color applied to the WiFi icon; Success container color applied to a card container_

### Why

You may need to apply static colors in your app for brand expression or to communicate semantic meaning, like a green success state. By defining these colors using the Material system, they'll work with existing Material colors and support features like dynamic color and user-controlled contrast.

### How

Use the Material Theme Builder to input a custom color. Material will return four color roles derived from that reference color. The main color, on-main color, container color, and on-container color all follow the conventions of the accent colors in the main scheme, and can be applied to your UI according to the same relationships. See [map or remap colors on UI elements](apply-colors.md#d15f5373-c03b-4282-a309-db569975d395) for more information.

### Best practices

-   If the colors provided back from your input color appear differently than expected, you can enable or disable color fidelity. [Color fidelity](adjust-existing-colors.md#cb49eeb4-3bbd-4521-9612-0856c27f91ef) is a feature that adjusts colors’ tones to match that of your input color.
-   Material provides the red Error color out of the box as an example of a static color, so you do not need to define your own static color for a semantic red color.
-   If you are using static colors in a dynamic scheme, you can choose to [harmonize your static colors](adjust-existing-colors.md#1cc12e43-237b-45b9-8fe0-9a3549c1f61e) to the scheme’s primary color. This will shift your static colors’ hues slightly warmer or cooler for a more harmonious overall appearance, while retaining the semantic meaning associated with the colors’ hue range.

![Green card in a home control UI shown under three different color schemes: purple, red, and yellow. In each scheme, the green card color appears slightly shifted to look more harmonious with the overall color.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7xb07-2.png?alt=media&token=7b719f7f-7e35-4efc-b95d-62e3e5d341c1>)

_Static colors can be harmonized with dynamic color to appear harmonious with the overall color scheme_

![Transit app UI with orange, green, and red color-coded subway lines and icons. The same screen is shown under a purple, red, and yellow scheme. In each screen, the subway line colors appear the same.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7xlqa-3.png?alt=media&token=2707ae8e-94a9-4133-9693-1ea419940a1b>)

_Colors can stay completely static and forgo harmonization if their values are tied to literal sources, such as brand colors or real-world signage_

## Define custom color roles

You can define custom color roles in addition to those already existing in the color scheme. By defining these roles the same way Material does (specifying a reference palette, starting tones, and contrast requirements), these roles can achieve colors more specific to your needs while working seamlessly with features such as user-controlled contrast.

![(1) a palette of Primary color chips in tones labeled 0 to 100, with tone 50 circled. (2) The chosen color against the primary container color, with 3:1 labeled on the border. (3) The chosen color applied to a large weather icon in a weather widget.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7y1c2-4.png?alt=media&token=fb1cc70e-1323-4ad9-bd3c-3fb2e4229bdd>)

_Example of creating a custom color role: The primary tonal palette, with tone 50 specified as the primary graphic default value; Color swatch showing an accessible 3:1 contrast between primary graphic and primary container; The primary graphic color role is applied in a weather widget against the primary container_

### Why

You may need to define your own custom color roles if the scheme’s existing colors or additional static colors don’t meet your product’s needs. In particular, you should create them within the Material system to respect dynamic colors and unlock other features like user-controlled contrast.

### How

Abstract your new color into a color role by specifying the following criteria:

-   **Palettes and reference tones:** For each color role, you must assign its value from a Material palette (primary, secondary, tertiary, neutral, neutralVariant, error) and a reference tone (for example: primary70, primary80, primary90…) for both light and dark themes.
-   **Color pairings:** You must specify any visual relationships in your design, such as color pairs that are used together as foreground and background, or which should retain a tone delta between them (difference in lightness or darkness).
-   **Contrast:** Confirm that custom foreground and background color pairings meet [Material's contrast minimums](../../../foundations/designing/color-contrast.md).

Once the above criteria are known, you can define the new color roles in your own dynamic color object. For each color role, you may then call Material Color Utilities (MCU) to generate the color value dynamically, according to different conditions such as user theming or contrast level.

### Best practices

Defining custom color roles should be considered only if you cannot achieve your desired colors with other Material color solutions.
