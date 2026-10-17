---
title: "Segmented buttons"
source_url: "https://m3.material.io/components/segmented-buttons/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/a787535c9521-lw7q4935-1.png"
  - "assets/images/c4bdd6a534ed-lw7q6k39-2_do.png"
  - "assets/images/cf5155ceb7d9-lw7q87p2-3_dont.png"
  - "assets/images/596df7810750-lw7q9t4x-4.png"
  - "assets/images/e75caa90a157-lw7qcq3h-5.png"
  - "assets/images/a99a7b7071e6-lw7qpwe0-6.png"
---

# Segmented buttons

Segmented buttons help people select options, switch views, or sort elements

## Accessibility

star

Note:

Segmented buttons are no longer recommended in the Material 3 expressive update. For those who have updated, use the [connected button group](https://m3.material.io/m3/pages/button-groups/overview/) instead, which has mostly the same functionality but with an updated visual design.

## Use cases

Users should be able to:

-   Navigate to and activate segmented buttons with assistive tech
-   Understand what each segment selection will do

### Interaction & style

For keyboard navigation, **Tab** focuses on an individual segment. 

For single-select segments, **Space** or **Enter** will select or unselect the focused segment.

For multi-select segments, **Space** or **Enter** will:

-   select an un-selected segment
-   select all of the segments
-   un-select a selected segment

![Tab moves the focus through segments of a segmented button.  Space or enter selects and unselects segments.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7q4935-1.png?alt=media&token=99802213-76ba-4f6a-8e7b-ea8daf0b2bc3>)

_Use Tab to navigate through segments and Space/Enter to select/unselect._

### Color contrast

Segmented buttons are clusters of similar components, so the outline should have at least a 3:1 contrast ratio with the background or surface. This helps distinguish each button.

Both a checkmark icon and a color change are used to distinguish selection. Make sure color isn’t the only way to show selection.

![The outline of segmented buttons on the surface passes the minimum contrast of 3:1.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7q6k39-2_do.png?alt=media&token=15790494-3f50-40ef-990b-7dde7e714d61>)

_Use an outline with a surface contrast of at least 3:1 /_

![The outline of segmented buttons on the surface fails the minimum contrast of 3:1.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7q87p2-3_dont.png?alt=media&token=f935fbc2-5a75-4d24-91a3-ed026030dd63>)

_The segmented button shouldn't have a contrast outline less than 3:1_

### Initial focus

Focus will start in the first segment. Depending on the direction of the language, it is either the most left or the most right segment.

For single select and multi-select, the first segment will be focused regardless of selection state.

![Segmented button focus starts on the left for left-to-right languages and on the right for right-to-left languages.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7q9t4x-4.png?alt=media&token=df0560b5-2880-402a-a0ad-ae84c4e2e2c1>)

_Focus begins on the left for left-to-right languages and on the right for right-to-left languages_

### Keyboard navigation

| Keys | Actions (single select) | Actions (multi select) |
| --- | --- | --- |
| **Tab** | Focus lands on next enabled segment | Focus lands on next enabled segment |
| **Space** or **Enter** | Select focused segment | Select and unselect focused segment |

### Labeling elements

The accessibility label for a segmented button comes from the visible label text on such as **Relevance** and **Distance**. If the segmented button displays icons without label text, the accessibility label describes the action that the button is expressing, such as **Inexpensive** for one currency symbol.

![The the text and accessibility role for the left-most section of a segmented button following “sort by” is  is “relevance.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7qcq3h-5.png?alt=media&token=24534d45-c04c-46d9-a747-04aeeefd11ff>)

_The label for segmented button matches the text label_

Single-select segmented buttons behave like radio buttons: only one option can be selected at a time. The label is **Radiogroup**.

Multi-select buttons behave like checkboxes: more than one option can be selected. The label is **Checkbox**.

![Selected price range segmented buttons range from $ to $$$$. The accessibility label of the $ button is “inexpensive.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7qpwe0-6.png?alt=media&token=a73ad3f4-0a14-4075-b015-dbe86ef3ec0f>)

_The role for the multi-select segmented button is Checkbox_
