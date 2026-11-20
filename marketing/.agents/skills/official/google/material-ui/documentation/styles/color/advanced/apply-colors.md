---
title: "Advanced customizations"
source_url: "https://m3.material.io/styles/color/advanced/apply-colors"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/7c418a86c4b2-lwt7bogx-1.png"
  - "assets/images/de1b0ecb41de-lwt7czj9-2.png"
  - "assets/images/98acf247479e-lwt7dkn6-3.png"
  - "assets/images/b8bed2af72ae-lwt7dy9t-4.png"
  - "assets/images/cf3e3832b023-lwt7eaxg-5.png"
---

# Advanced customizations

Apply, define, or adjust colors to create a fine-tuned, unique color experience

## Apply colors

You can apply colors in places or ways that aren’t provided by default.

## Combine multiple color schemes

Use multiple color schemes in the same app experience, such as a baseline scheme combined with a dynamic content-based scheme.

![Smart home control screen showing media controls, light controls, and wifi and thermostat controls. Annotation 1 points to the media controls, which are colored in a teal scheme. Annotation 2 points to the remaining controls, colored in a red scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7bogx-1.png?alt=media&token=4c59af6e-46ca-4c4c-a4f0-2829054456ca>)

_This smart home control screen combines two color schemes: A teal content-based color scheme from the local album art, applied to media controls; A red user-generated color scheme from the user's wallpaper, applied to the rest of the UI_

### Why

If your app features content-rich moments, such as a media player, it can enhance a user’s experience by applying local color based on that content.

### How

-   Start from a [baseline](../static/baseline.md) or [user-generated dynamic](../dynamic/user-generated-source.md) scheme to create a consistent color foundation in your app.
-   On top of this foundation, [map content-based color roles to contained spaces](../dynamic/content-based-source.md) to emphasize or celebrate content. For example, a music app might derive color from a specific album’s artwork to build upon the personal connection to a music library.

### Best practices

-   Consider where content exists in the UI and where content-based color can enhance a person’s experience. Your existing app structure can suggest contained areas for content-based color to live.
-   **Build hierarchy & direct attention:** When many types of information and actions share a screen, use content-based color to add hierarchy and draw attention to the content.
-   **Link and associate content on a screen:** In lists and collections of repeated items that benefit from differentiation, content-based color can help associate related elements. This helps people quickly distinguish and pair related information, such as a list item and its associated action.
-   **Immerse users in content color:** Full-screen content-based color moments can orient users within a content-driven experience, such as a media control or a purchase flow. 
-   **Pair content-based color with its source content:** Keep the source for content-based color visible on a screen using the content color. This way users are shown where a content-based color originates. Avoid applying content-based color in spaces where the content itself isn’t visible.
-   **Limit the number of color source types per screen:** Limit a screen to two color schemes from different source types. Too many color schemes on the same screen may lead to confusion and visual disarray. For example, a baseline or user-generated color scheme can be combined with one type of on-screen content (such as album art).
-   **Don’t replace semantic colors:** Use caution when applying content-based color in places where a semantic color or conventional color meaning is important for usability. For example, a common red error message or a common green positive action shouldn’t be replaced with dynamic content-based color because it may interfere with someone’s understanding.

![Photo options screen in a red theme. Photo editing controls are displayed underneath the photo, and are colored in a teal scheme sourced from the photo.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7czj9-2.png?alt=media&token=a6e7bf59-72ed-4963-a3b5-2489ab3d0648>)

_When many types of information and actions share a screen, use content-based color to add hierarchy and draw attention to the content. This screen uses a content-based scheme sourced from the photo to draw attention to the photo editing controls._

![A feed of cards listing different activities. The first card shows an image of a yellow tape cassette, and its UI is colored in a yellow scheme. The second card shows an image of a green plant, and its UI is colored in a green scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7dkn6-3.png?alt=media&token=52cd3d4a-51f7-4b52-bb97-e26c218b279f>)

_In lists and collections of repeated items that benefit from differentiation, content-based color can help associate related elements. This helps people quickly distinguish and pair related information, such as a list item and its associated action. In this list of activities, each card is colored with a scheme sourced from its main image._

![Media control screen where a podcast called Early Aughts is playing. The screen displays a teal colored album art for the podcast. The entire screen is colored in a teal scheme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7dy9t-4.png?alt=media&token=46406651-a27d-4f5b-b7a8-0f55e31c7438>)

_Full-screen content-based color moments can orient users within a content-driven experience, such as a media control or a purchase flow. This media control screen is colored entirely in a scheme sourced from the in-context album art._

## Map or remap colors on UI elements

You can change a component’s default color mapping, or apply colors to your own custom components.

![A custom volume slider component next to a design software UI palette displaying a list of color roles. The primary color role is linked to the mute button on the volume slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwt7eaxg-5.png?alt=media&token=837bf820-4912-462f-9d8f-a1e37ff97d2f>)

_Colors can be remapped on existing Material components, or can be mapped as desired to custom-built components, such as this unique volume slider._

### Why

You want to map colors to a custom-built component or change a Material Component’s default color mapping to improve its function (such as visual contrast) or style.

### How

Choose an appropriate color role based on how the color is used (see [color roles](../roles.md)) and how well the role supports your intended design expression.

#### Design

-   In Figma, select the component or element you want to remap so that you see its colors in the Design panel on the right of the screen
-   To remap a color, hover on the color row in the Design panel and select the **Style** icon (four dots). This opens a selection dialog.
-   Search for your theme name to see the available color roles
-   Select the color role that most closely matches that color's use case in the component. For example, the background color of a component could be replaced with the **surface** color role and the color for text or icons could be **on surface**. See [Color roles](../roles.md) for more information on what color to use where.
-   Select **Use style** to apply that color to the selected objects
-   Repeat until all colors in the component have been replaced with color roles from your scheme

### Best practices

-   Make sure to use color roles that support Material's contrast requirements for the component. Any color roles starting with "on-" are guaranteed to have sufficient contrast with the corresponding color role. Other color role pairs may not meet the 4.5:1 (small text) and 3:1 (large text) Material contrast requirements.
-   If you’re applying a dynamic scheme, test how the color on the component appears under different themes (such as light and dark; red, yellow, green and blue) to ensure it looks as desired in dynamic color
-   Always apply color roles rather than static values or tonal palette values, as these colors will break with light and dark themes, contrast control, and other features. If the color in a role does not meet your needs, you can define new colors or adjust existing colors.
