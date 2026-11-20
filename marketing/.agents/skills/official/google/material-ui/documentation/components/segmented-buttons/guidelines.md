---
title: "Segmented buttons"
source_url: "https://m3.material.io/components/segmented-buttons/guidelines"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/c34371b7149d-lw7mlxdq-1.png"
  - "assets/images/b82ea1e551bb-lw7mnnk2-2.png"
  - "assets/images/bd83bf9c7482-lw7mz9ev-3.png"
  - "assets/images/b03cac3c48c8-lw7n59nt-4.png"
  - "assets/images/e5addedb2736-lw7nap4m-5.png"
  - "assets/images/f497041df3bd-lw7ngd39-6_do.png"
  - "assets/images/aeeffd6a0336-lw7o4u2i-7_dont.png"
  - "assets/images/a13e5beab0c3-lw7o7un8-8.png"
  - "assets/images/fd699b27327c-lw7okkk9-9.png"
  - "assets/images/846169a9465f-lw7omu8a-10.png"
  - "assets/images/3be3d2522ea6-lw7pqz1e-11_do.png"
  - "assets/images/bf406302bb3e-lw7prory-12_dont.png"
  - "assets/images/a64d5d9c9a9b-lw7psieh-13_do.png"
  - "assets/images/c3bd25b56551-lw7pt9gl-14_caution.png"
  - "assets/images/af7e496810ca-lw7ptzzf-15_dont.png"
  - "assets/images/5679157a28ab-lw7puwq3-16.png"
  - "assets/images/9615b603cd0d-lw7pvpf7-17.png"
  - "assets/images/954f8e58d569-lw7pwh9i-18.png"
  - "assets/images/227e767bdd3e-lw7pxu9q-18-a_do.png"
  - "assets/images/42e30c850f4c-lw7pyzy1-18-b_dont.png"
  - "assets/images/36e0b62b3c0c-lw7q0vrm-19.png"
  - "assets/videos/d2d978288e50-lw8tx7wx-GM3-Components-SegmentedButtons-Guidelines-1-v01.mp4"
---

# Segmented buttons

Segmented buttons help people select options, switch views, or sort elements

## Guidelines

star

Note:

Segmented buttons are no longer recommended in the Material 3 expressive update. For those who have updated, use the [connected button group](https://m3.material.io/m3/pages/button-groups/overview/) instead, which has mostly the same functionality but with an updated visual design.

![Two types of segmented buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7mlxdq-1.png?alt=media&token=e2fd2652-48e7-4a3d-9203-c8451a77c27a>)

_Single-select; Multi-select_

## Usage

Segmented buttons help people select options, switch views, or sort elements.

![A segmented button for switching between restaurants and bar options. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7mnnk2-2.png?alt=media&token=0621a46b-ddcb-4ee9-b3de-b1b7be908b9d>)

_A segmented button can help switch between viewing restaurant and bar options_

There are 2 variants of segmented buttons:

1.  Single-select

2.  Multi-select

![Side by side view of single and multi-select segmented buttons](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7mz9ev-3.png?alt=media&token=6ac60657-7e18-4b03-81cb-23e97d4596c0>)

_Single-select segmented button can only have 1 segment selected; Multi-select segmented button can have multiple segments selected_

## Anatomy

![Diagram of segmented button indicating 5 parts of its anatomy](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7n59nt-4.png?alt=media&token=42169c2e-9e9e-45a1-b3a7-44b5354fcddd>)

_Segment; Container; Icon (optional); Label text (optional); Selected icon_

### Segments

Segmented buttons can have 2-5 segments. Each segment is clearly divided and contains label text, an icon, or both.

![Side by side view of segmented buttons each with additional segment starting from 2 to 5](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7nap4m-5.png?alt=media&token=0561fbc5-65b2-4d96-a0de-8f4ec03c3431>)

_There can be anywhere from 2 to 5 segments in single-select and multi-select segmented buttons_

![Mobile UI of data usage screen with segmented button](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7ngd39-6_do.png?alt=media&token=17b77bb9-147e-4b1a-b0fb-8e4ea445e7a0>)

_Segmented buttons are best used for selecting between 2 and 5 choices_

![Incorrect use of segmented button with 6 segments](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7o4u2i-7_dont.png?alt=media&token=75e065ae-9d24-4a76-9261-7db3988ff869>)

_Don’t use more than five segments in a single segmented button. Choices should be scoped. If you have more than five choices, consider using another component, such as chips._

### Container

Like common buttons, segmented buttons have fully rounded corners by default.

![Close up detail of segmented button with fully rounded corners](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7o7un8-8.png?alt=media&token=0ccf0509-584f-437c-8a5f-dbfb97aae0ed>)

_Segmented buttons have fully rounded corners_

### Icons

Icons may be used as labels by themselves or alongside text. 

If an icon is used without label text, it must clearly communicate the option it represents.

![Side by side view of segmented buttons with different configurations of icons and label text](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7okkk9-9.png?alt=media&token=b185e6ab-2a5e-4d29-9989-52b66176df7b>)

_Segmented buttons can include icons_

### Label text

Labels should be short and succinct. If a label is too long to fit within its segment, consider using an icon alone.

![Mobile UI of music app showing a segmented button with options for music, albums, podcasts](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7omu8a-10.png?alt=media&token=a0044742-6660-4d12-b7eb-c168a9e4ea3a>)

_Use labels that are as clear and short as possible_

![Segmented button with options for day, week, month](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7pqz1e-11_do.png?alt=media&token=5f954b64-e2d8-4fef-a0a5-e56c9fba0bd3>)

_Keep labels short and consistent in length_

![Segmented button with 4 segments. 3 are next to each other. The 4th is wrapped on a new line.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7prory-12_dont.png?alt=media&token=72743d41-4ba5-48b8-a3eb-0c386ab4ac66>)

_Don’t allow segments to wrap onto a new line_

![Segmented button with text labels reading day, week, month](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7psieh-13_do.png?alt=media&token=30b7bff6-d892-4765-93c8-a4712aaf1cd3>)

_Use consistent label types_

![Segmented button with icons only labels for walking, transit, driving](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7pt9gl-14_caution.png?alt=media&token=94a71ac1-5be5-4376-8f8e-8eac93aa3f1b>)

_Icons can be used in place of labels, but they must clearly communicate their meaning_

![Segmented button with 2 icon only options indicating favorite and bookmark and 3rd option with text label reading recent](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7ptzzf-15_dont.png?alt=media&token=bbfbde3d-e104-4cd8-9aae-6a7e3aed50e9>)

_Avoid mixing icon-only labels with text labels. Choose one label type and use that type for all segments._

## Single-select

Use a single-select segmented button to select one option from a set, switch between views, or sort elements from up to five options. 

For example, use a single-select segmented button to choose one of a set of sizes, such as this beverage size selector.

![Mobile UI for ecommerce app with segmented button with 3 beverage size options](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7puwq3-16.png?alt=media&token=523ecc2f-a2df-4442-8e24-cf65c64c83d7>)

_A single select segmented button for choosing beverage size_

## Multi-select

Use a multi-select segmented button to select or sort from two to five options. Unlike single-select, selection is not required and a user may concurrently select anywhere from all to none of the options. 

For example, multi-select segmented buttons can be used to filter by price range when searching for a restaurant.

![Mobile UI for ecommerce app with multi-select segmented button with 4 price range options ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7pvpf7-17.png?alt=media&token=3aa4f45c-50e9-4de0-a6c1-6325df4a89a1>)

_A multi-select segmented button for filtering restaurant search options_

## Placement

Segmented buttons should have adequate margins from the edge of the viewport or frame. 

On larger screens, set a maximum padding for all button segments so the set doesn't fill the screen.

![Mobile UI with 2-segment segmented button and 4-segment segmented button each with same margins to the viewport edge.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7pwh9i-18.png?alt=media&token=cd37d565-662f-48b9-b036-57fdce8f001d>)

_Allow adequate space for margins. The button container shouldn’t reach the edge of the viewport._

![Game store UI with a segmented button the proper width](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7pxu9q-18-a_do.png?alt=media&token=4c28374e-4793-49c8-9988-4c8ebdc009d7>)

_Set a maximum padding within the segments to ensure usability on larger screens_

![Game store UI with a segmented button improperly spanning the entire width of the screen](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7pyzy1-18-b_dont.png?alt=media&token=d6675cb8-414c-4214-a099-1643383404b9>)

_Don’t allow segmented buttons to span the full width of larger screens or panes. This can leave too much padding on either side of the segment label, making the button less usable._

Segmented buttons can be placed on other components, such as bottom sheets or full-screen dialogs.

![Mobile UI with segmented button in bottom sheet](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw7q0vrm-19.png?alt=media&token=f3152999-f72c-4cfd-9c6b-b095bd997085>)

_A segmented button can be placed on a bottom sheet_

## Behavior

When using both icons and label text in segmented buttons, the icon label is replaced by the checkmark icon when the segment is selected.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8tx7wx-GM3-Components-SegmentedButtons-Guidelines-1-v01.mp4?alt=media&amp;token=5cad284b-7802-403c-99c4-cbfa0f04af8b" title="Segmented button with icon and text labels with checkmark icon in selected segment"></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw8tx7wx-GM3-Components-SegmentedButtons-Guidelines-1-v01.mp4?alt=media&token=5cad284b-7802-403c-99c4-cbfa0f04af8b>)
