---
title: "XR components"
source_url: "https://m3.material.io/foundations/xr/components/dialogs"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/7cfdd50a69a7-mp6grqe5-hero-image.png"
  - "assets/images/2df61bb43496-mp5rmvr7-01-3P.png"
  - "assets/images/351b97cee0bb-mox7g5yi-02.png"
  - "assets/images/ddf84f7cca02-mox7nrut-03.png"
  - "assets/images/0dfe9ac63e56-mox7s3sr-04.png"
  - "assets/images/44649890a609-mox7ytt7-05.png"
  - "assets/images/42964cde623f-mox83eps-06.png"
  - "assets/videos/ca94976b55a2-mox8cric-XR_Dialog_Behavior_01_IA_v02-.mp4"
  - "assets/videos/85572194daf2-mox8drwx-XR_Dialog_Behavior_02_IA_v02-.mp4"
  - "assets/videos/323fbcb5d651-mox8ubbg-GM3_XR_Dialogs_Motion_Placement_01_IA_v03.mp4"
  - "assets/videos/257f1f9ddc30-mox8yi7x-GM3_XR_Dialogs_Motion_Placement_02_IA_v05.mp4"
---

# XR components

Learn how Material 3 Expressive components adapt to extended reality devices

![XR components](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp6grqe5-hero%20image.png?alt=media&token=77848b44-f2d9-4c6e-a51a-d4807d7ea9e4>)

## Dialogs

star

Note:

XR guidelines are primarily intended for designers. Find what’s implemented in code in the [design kit](https://www.figma.com/community/file/1035203688168086460).

Extended reality (XR) introduces spatial capabilities, such as using depth to make dialogs stand out from the background. Currently, [spatial dialogs](https://www.figma.com/community/file/1035203688168086460) are only available in full space. For home space, follow Material’s general [dialog guidance](../../../components/dialogs/overview.md).

## Color & elevation

XR uses color roles to communicate the elevation of UI elements. Dialogs can use two color options: **surface container high** or **surface container highest**.

star

Note:

Color and elevation for spatial dialogs aren’t available in Jetpack Compose yet. These need to be customized manually.

![2 spatially elevated dialogs with surface-container-high and surface-container-highest color roles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5rmvr7-01-3P.png?alt=media&token=1dde2ef8-1536-4fd5-932b-4701ab757581>)

_Surface container high; Surface container highest_

For effective visual hierarchy, a dialog should be the most prominent element. 

Add a scrim behind a dialog to improve its visibility. Scrims prevent other content from being selected until the dialog action is complete.

![Dialog with surface-container-highest color and a scrim.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox7g5yi-02.png?alt=media&token=7a21ccfd-5b6b-4840-a471-7dd6e803a83f>)

_Make sure a spatial dialog’s color is higher than all other UI elements, and use a scrim_

The dialog should have the highest elevation in the product.

For example, if the dialog is **surface container high**, don’t use **surface container highest** for any other elements.

![Dialog with surface-container-high color and no scrim. An orbiter is at a higher elevation than the dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox7nrut-03.png?alt=media&token=91aeb244-3d82-4210-bcfc-a4588dafbd4a>)

_If a dialog’s color is surface container high, don’t use surface container highest for any other element_

## Usage

Only use [basic dialogs](../../../components/dialogs/guidelines.md#97ac3858-3932-4084-ae8e-73e42b7cb752) in XR. This keeps the required action in the person’s [field of view](https://developer.android.com/design/ui/xr/guides/spatial-ui#where-place).

![Basic dialog in XR.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox7s3sr-04.png?alt=media&token=5665a385-4ac8-4c18-a89d-7d3857645d09>)

_A basic dialog elevated above an app in home space_

![Full-screen dialog in XR.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox7ytt7-05.png?alt=media&token=592b5c9f-8a12-46d1-9660-eae5205e2334>)

_Avoid using full-screen dialogs in XR. Required actions could appear beyond a person’s field of view._

## Spatial dialogs

In full space, dialogs can be elevated spatially via [overrides](https://developer.android.com/develop/xr/jetpack-xr-sdk/material-design#use-enablexrcomponentoverrides). This helps dialogs stand out from their background in XR.

![Side view of basic dialog showcasing spatial elevation.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox83eps-06.png?alt=media&token=dda2abbf-f23a-4c77-bb5d-bd5e429707e1>)

_Side view of a basic dialog with spatial elevation in full space_

## Behavior

### Effect

The spatial dialog should scale uniformly. It also fades in when appearing, and fades out when disappearing. 

The dialog's scrim only fades in and out.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8cric-XR_Dialog_Behavior_01_IA_v02%20.mp4?alt=media&amp;token=e460f495-9098-4668-954c-27b70eea9d29" title="A direct view of a spatial dialog appearing and disappearing."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8cric-XR_Dialog_Behavior_01_IA_v02%20.mp4?alt=media&token=e460f495-9098-4668-954c-27b70eea9d29>)

### Movement

When activated, the spatial dialog rises from the app to the highest resting level on the Z-axis. 

When the action is complete, it returns to a normal resting level.

The dialog's scrim stays at the app content level at all times.

To prevent motion sickness, use [standard easing](../../../styles/motion/easing-and-duration/tokens-specs.md#601d5552-a6e6-4d74-9886-ff8f24b9ec35) and [long duration](../../../styles/motion/easing-and-duration/tokens-specs.md#48bf653e-46f9-48f5-87e0-eaf8ea3fe716) motion tokens.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8drwx-XR_Dialog_Behavior_02_IA_v02%20.mp4?alt=media&amp;token=e278a231-4ee1-4db8-93eb-3defa565b07d" title="A spatial dialog elevating on the Z-axis, as seen from a side angle."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8drwx-XR_Dialog_Behavior_02_IA_v02%20.mp4?alt=media&token=e278a231-4ee1-4db8-93eb-3defa565b07d>)

## Placement

Consider factors like field of view, viewing distance, and possible interactions when deciding where to place dialogs in XR.

A dialog’s placement can be adjusted to accommodate specific needs, such as improved ergonomics or [right-to-left (RTL) languages](../../layout/bidirectionality-rtl.md).

### Elevation: Highest resting level

Display spatial dialogs at the **highest resting level**. When setting the depth value of the highest resting level, make sure the elevated dialog is at a comfortable viewing distance from the person. [More on spatial elevation](https://developer.android.com/design/ui/xr/guides/spatial-ui#spatial-elevation)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8ubbg-GM3_XR_Dialogs_Motion_Placement_01_IA_v03.mp4?alt=media&amp;token=dcaaeff6-4d44-4d07-8288-f57acf7e9a40" title="An animated side view of a dialog moving from the lowest to the highest resting level."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8ubbg-GM3_XR_Dialogs_Motion_Placement_01_IA_v03.mp4?alt=media&token=dcaaeff6-4d44-4d07-8288-f57acf7e9a40>)

### Center spatial dialogs in field of view

Spatial dialogs should be centered in a person’s [field of view](https://developer.android.com/design/ui/xr/guides/spatial-ui#where-place). If the dialog **can’t** track head movements, position it in the center of the app’s content. 

If the dialog **can** track head movements, configure it with a lazy follow behavior. This keeps the dialog anchored to the center of a person’s field of view until an action is taken.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8yi7x-GM3_XR_Dialogs_Motion_Placement_02_IA_v05.mp4?alt=media&amp;token=6f357063-b36f-4819-9064-2cc7421f7960" title="A dialog follows a person’s head movements, remaining centered in their field of view."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmox8yi7x-GM3_XR_Dialogs_Motion_Placement_02_IA_v05.mp4?alt=media&token=6f357063-b36f-4819-9064-2cc7421f7960>)

## Accessibility considerations

[XR accessibility](../design/accessibility.md) guidelines are still evolving. Spatial dialogs should follow applicable Material [dialog accessibility standards](../../../components/dialogs/accessibility.md).
