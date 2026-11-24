---
title: "Navigation rail"
source_url: "https://m3.material.io/components/navigation-rail/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/4e25a25955ef-m0guemtv-01-static.png"
  - "assets/images/5481413a1cda-m0guep9g-02-static.png"
  - "assets/images/2f5e89734cca-m0gud02c-03-static.png"
  - "assets/images/59b72873d039-m0fut28q-04.png"
  - "assets/images/752117e26841-m0fut791-05.png"
  - "assets/images/5bb685fb5e42-m2kvazns-09.png"
  - "assets/images/4c0fe4ad21eb-m36brxfu-04.png"
  - "assets/images/e3a47df61e90-m36bs1jw-05.png"
  - "assets/images/67442fa1d772-m0futkj8-07.png"
  - "assets/images/1a3176dbdb8c-m0futowb-08.png"
  - "assets/images/3f2bf21dfbc2-m0futxf6-09.png"
  - "assets/images/0ac4170ce86b-m0fv1bxc-10.png"
  - "assets/images/c9c029a32e1e-m0fuvxlx-11.png"
---

# Navigation rail

Navigation rails let people switch between UI views on mid-sized devices

## Accessibility

## Use cases

People should be able to do the following using the assistive technology:

-   Navigate between navigation destinations
-   Select a particular navigation destination from a set
-   Get appropriate feedback based on input type

## Interaction & style

When a navigation item is tapped, the active indicator appears, providing the following feedback to the user that it is selected:

-   A ripple passes through the indicator
-   The icon switches from outlined to filled
-   The icon and text change color

When hovered, the hover state appears, providing a visual cue that the destination is interactive.

![Colorful, purple navigation rail shown collapsed and expanded.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0guemtv-01-static.png?alt=media&token=3758e551-ad90-4db7-92a7-d8c19e95e973>)

_Touch: Tap_

![Tap indicator on a collapsed nav rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0guep9g-02-static.png?alt=media&token=bac52c48-506a-47e5-8124-90a1d960485a>)

_Cursor: Hover, Click_

The target area for expanded navigation rails spans the full width of the container, even though the active indicator visually hugs the content.

![Touch indicator on a nav rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0gud02c-03-static.png?alt=media&token=e49fb92a-9671-4422-8c7f-25750ab3ca13>)

_Touch: Tap_

Use a filled icon for the active destination and outlined icons for inactive destinations.

Active and inactive icon colors need sufficient contrast against the container.

![Navigation rail with filled element.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fut28q-04.png?alt=media&token=2d41d871-683b-485e-befb-2770dfb712d6>)

_Use the default color scheme to ensure proper contrast and emphasis on the active destination_

![Nav rail with multiple navigation destinations and multi-colored contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fut791-05.png?alt=media&token=c9ae906c-6e53-474e-83a7-c26f630a8eb5>)

_Don’t use more than two colors for destinations or low-contrast colors in the navigation rail. This will make distinguishing active items difficult._

If an icon doesn’t have a filled style, use the semibold icon weight instead.

![Icon button with semibold weight, without filled options.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm2kvazns-09.png?alt=media&token=91143806-b128-41b8-a02f-9c97f171d073>)

_An icon with no filled option should use the semibold weight when active_

### Text scaling and truncation

When someone sets their device to show a larger text size, the navigation rail items should grow vertically to accommodate larger labels while retaining the default padding. It’s okay for scaled text to wrap in navigation items.

To remain accessible, ensure the full label is always visible on-screen at up to 2x text sizing. Beyond this size, text can truncate.

![Nav rail with text scaled to 1.5x size. All labels are on one line.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm36brxfu-04.png?alt=media&token=a23060fe-42ba-4e21-8eec-36fe12115a0d>)

_Text scaled to 1.5 size_

![Nav rail with text scaled to 2x size. Some labels wrap to two lines.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm36bs1jw-05.png?alt=media&token=fd8f29ed-e05c-4397-8443-13902974567a>)

_Text scaled to 2x size_

### Initial focus

Initial focus lands directly on the first interactive item, whether it’s the menu, the FAB, or the first navigation item.

From the FAB or menu, **Tab** brings the person to the navigation items. **Tab** or **Arrows** then navigate between items.

![Arrows help people move between pages.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0futkj8-07.png?alt=media&token=96c198ca-69ed-4388-81d4-76785f7c7960>)

_Use arrows to move between navigation items_

![Space/enter help people choose a navigation destination.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0futowb-08.png?alt=media&token=6a263d5a-f534-4cc7-b669-c5828633d338>)

_Use space/enter to activate the focused navigation item_

### Visual indicators

Icons give the dominant cue of the navigation state. Use a filled icon for the selected destination to contrast with outlined icons for the non-selected destinations.

![Nav bar with an active, filled icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0futxf6-09.png?alt=media&token=813363dc-6ed6-40c4-951b-d996abdf8b44>)

_Use a filled icon variant on the selected navigation item to differentiate from inactive navigation items_

![Selected navigation item without filled icon style.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fv1bxc-10.png?alt=media&token=07e1a3b5-ec7f-48ef-b48b-d8e4c1aaa4e1>)

_Avoid using the same unfilled icon style for both selected and unselected items because it lacks important visual feedback cue_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab / Arrows | Navigate between interactive elements |
| Space / Enter | Selects an interactive element |

## Labeling elements

The accessibility label for a navigation item is typically the same as the adjacent text label.

When the visible UI text is ambiguous, accessibility labels need to be more descriptive. For example, a navigation item visibly labeled **Recent** would benefit from additional information in its accessibility label to clarify the destination's intent.

Note: On Android Views (MDC-Android), a more descriptive accessibility label is not available and the role is not announced.

![“Maps” is both the icon label text and the accessibility label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuvxlx-11.png?alt=media&token=ec0c7c6d-3c55-4515-81d8-6c0a220e1f92>)

_While the visible label text reads Recent, the accessibility label for this switch clarifies its function: Recent images_
