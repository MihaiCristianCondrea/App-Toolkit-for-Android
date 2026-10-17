---
title: "Sliders"
source_url: "https://m3.material.io/components/sliders/guidelines"
lastmod: "2026-05-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/c2ee114ad7a2-m7m4gc9t-slider-motion.mp4"
  - "assets/images/e11b1ef17fd5-mabg8tbj-02.png"
  - "assets/videos/b5a3519b5f60-m7llbz94-GM3_Expressive_Slider_guideline_3b_IA_V02-.mp4"
  - "assets/images/c5315155e153-m7lk01w5-04.png"
  - "assets/images/1cfca454d06a-m7lk1j0t-05.png"
  - "assets/images/11664060d529-m7lk3lyv-06.png"
  - "assets/images/c6637e756c35-m7lk4tsn-07.png"
  - "assets/images/2257dd269168-m7lk6981-08.png"
  - "assets/images/e8c810f9569a-m7ln45vm-09-don't.png"
  - "assets/images/ae12298d12c3-m7m553sk-04-1.png"
  - "assets/images/98fcb5356926-m7lk9dsb-11.png"
  - "assets/images/410e88c176fa-m7lkaurk-12.png"
  - "assets/images/43dd7ea80d4b-m7lkbdhm-13.png"
  - "assets/images/29e6da791207-lx1s2vu5-13.png"
  - "assets/images/1aecbcb46ceb-m7lkclo7-14.png"
  - "assets/videos/a38dc699845c-lx2tgm3i-GM3-Components-Sliders-3-v01.mp4"
  - "assets/images/8a6ac13c1dee-lx1s1ozg-10.png"
  - "assets/images/657de37f4f86-m7lkfou6-17.png"
  - "assets/images/e2f40b9867c3-m7lkhuu1-18.png"
  - "assets/images/e8e299ea03ff-m7lkj1b8-19.png"
  - "assets/images/ab08d4362b09-m7lkmte8-20.png"
  - "assets/images/44dc4faaa4b8-m7lni1k4-21-don't.png"
  - "assets/images/0f4998d06b0e-m7lnlfie-22-don't.png"
  - "assets/images/86ad75aaa083-m7lnor7e-23-don't.png"
  - "assets/images/2cb7c46a91b8-m7lkolpk-24.png"
  - "assets/images/2b37c421db4f-m7lkr4j4-25.png"
  - "assets/videos/43c4e103d1ae-lx2tddmn-GM3-Components-Sliders-4-1-v01.mp4"
  - "assets/videos/5c199da2b021-lx2tdv5o-GM3-Components-Sliders-4-2-v01.mp4"
  - "assets/videos/b866e0c9af8c-lx2tec5h-GM3-Components-Sliders-5-1-v01.mp4"
  - "assets/videos/b217db5eabc5-lx2tep43-GM3-Components-Sliders-5-2-v01.mp4"
  - "assets/videos/9e1f6a8cee17-lx2tf2t6-GM3-Components-Sliders-6-1-v01.mp4"
  - "assets/videos/6414291b0d19-lx2tffc3-GM3-Components-Sliders-6-2-v01.mp4"
---

# Sliders

Sliders let users make selections from a range of values

## Guidelines

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7m4gc9t-slider%20motion.mp4?alt=media&amp;token=95c022a4-1968-4b5a-9175-2407bbf7fe20" title="Photo editor with sliders labeled pop, sharpen, and denoise being adjusted."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7m4gc9t-slider%20motion.mp4?alt=media&token=95c022a4-1968-4b5a-9175-2407bbf7fe20>)

## Usage

Sliders are used to select values along a track. They’re ideal for adjusting settings such as volume and brightness, or changing the intensity of image filters.
Sliders can use icons or labels to represent a numeric or relative scale.

![Sound settings screen with continuous sliders labeled call volume and alarm volume.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmabg8tbj-02.png?alt=media&token=ada3febd-596c-46d4-ae20-0aa3b4228c1e>)

_Use sliders to pick a value from a range, like volume loudness_

Changes made with sliders must take effect immediately, so people can understand the effects of their selection as they're moving the slider.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7llbz94-GM3_Expressive_Slider_guideline_3b_IA_V02%20.mp4?alt=media&amp;token=134b459d-b198-4115-8188-1321d56605a5" title="The brightness slider changes the screen’s brightness in real time."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7llbz94-GM3_Expressive_Slider_guideline_3b_IA_V02%20.mp4?alt=media&token=134b459d-b198-4115-8188-1321d56605a5>)

There are three different variants of sliders: **standard**, **centered**, and **range:**

Standard sliders select one value from a range of values. Use this when the slider should start from zero or the beginning of a sequence.

![Horizontal standard slider with an end stop indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lk01w5-04.png?alt=media&token=6cd6a040-a82f-4ef7-8d4b-badc8c6eb27b>)

_Horizontal standard slider_

![Vertical standard slider  with an end stop indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lk1j0t-05.png?alt=media&token=3b884058-21ca-4d72-ba5e-5afdb59fa01e>)

_Vertical standard slider_

Centered sliders select a value from a positive and negative value range. Use this when zero, or the default value, is in the middle of the range.

![Horizontal centered slider with a negative value and visible stop indicators at each end.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lk3lyv-06.png?alt=media&token=eeb21a3d-5711-46a4-8751-5995fadab337>)

_Horizontal centered slider_

![Vertical centered slider with a negative value and visible stop indicators at each end.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lk4tsn-07.png?alt=media&token=55d86dc7-7b06-47fa-9d1a-7ae9dff73c91>)

_Vertical centered slider_

Range sliders select two values on one slider to create a range. Use this when defining a minimum and maximum value.

Avoid using range sliders vertically, as this can add too much cognitive load. People are used to most sliders being horizontal.

![Horizontal range selection slider with 2 values selected, and a stop indicator at each end.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lk6981-08.png?alt=media&token=67bf8680-0cc1-4109-aee0-b50e319a3ae9>)

_Horizontal range slider_

![Vertical range slider with 2 values selected, and a stop indicator at each end.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7ln45vm-09%20-%20don't.png?alt=media&token=bb49eedd-2d76-45c9-99ab-8e9be6103673>)

_Because of the additional cognitive load of a range slider, avoid using it in vertical orientation._

## Anatomy

![6 elements of a slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7m553sk-04-1.png?alt=media&token=03f97ae2-c5e8-41b8-8416-ba8201534a1b>)

_Value indicator (optional); Stop indicators (optional); Active track; Handle; Inactive track; Inset icon (optional)_

### Track

The track shows the full range of values that can be selected on the slider. It has two sections: active and inactive.

-   The **active** section of the track is from the minimum value to the handle. For range sliders, the active track is between the two handles.
-   The **inactive** section of the track is from the handle to the maximum value, or outside the two handles of a range slider.

For left-to-right (LTR) languages, the values increase from left to right. For right-to-left (RTL) languages, this is reversed.

![Sliders for font size and display size with stop indicators along the track.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lk9dsb-11.png?alt=media&token=ac3c7b80-ec76-498e-931e-6f6b573be9c6>)

_The track on a slider shows the available range_

### Handle

The handle can be moved along the track to choose a value.

When sliders have two handles, the handles choose the minimum and maximum values in a range.

The handle changes shape to indicate when it’s pressed.

![The handle is a vertical line on the current value of the slider. It shrinks in width when selected.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkaurk-12.png?alt=media&token=8ee46da2-b5f5-4990-a851-44f5acaadba6>)

_A handle changes shape when it's being pressed or dragged_

![2 unselected handles on a slider with range selection.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkbdhm-13.png?alt=media&token=bc2bb7bf-9f18-4ff2-988b-34a85bdc8084>)

_Two handles are used for sliders with range selection_

## Configurations

### Value

The value displays the specific value that corresponds with the handle’s placement. 

A value appears when interacting with the corresponding handle. For range sliders, only one value should be shown at a time.

If the value is shown elsewhere, the indicator is not required.

![A value of 50 is above a slider handle in the middle of the track.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx1s2vu5-13.png?alt=media&token=94e8f2b1-9fac-4303-ab60-acb9e7a32bd9>)

_A value can appear while the handle is being pressed or dragged_

![A value of 75 is above the pressed range slider handle. The unselected slider doesn’t show the value.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkclo7-14.png?alt=media&token=68ab779d-bed3-4d20-a2fb-e9f742ee269c>)

_For range sliders, the value only appears on one handle at a time_

Instead of showing the built-in value label, a separate text input field can be added outside of the slider. If this is added, the slider and value in this text field should automatically update to match each other.

Make sure people can tab to the text field directly after the slider.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tgm3i-GM3-Components-Sliders-3-v01.mp4?alt=media&amp;token=ff66bc1d-5841-4740-a9b0-d73fae991edb" title="Navigating to an adjacent text label, changing the value to 100 using arrow keys. The slider auto-updates based on input value."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tgm3i-GM3-Components-Sliders-3-v01.mp4?alt=media&token=ff66bc1d-5841-4740-a9b0-d73fae991edb>)

### Stop indicators

Stop indicators show which predetermined values can be chosen on the slider. The slider handle snaps to the closest stop. 

Avoid having too many stop indicators on a slider, because it can become visually crowded and difficult to adjust the value.

All sliders have stops at the end of the inactive track to ensure at least a 3:1 contrast with the background. If the inactive track has this level of contrast already, the end stops can be removed.

![Stop indicators are equally spaced out on a slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx1s1ozg-10.png?alt=media&token=313ed48c-5478-4e59-9e89-4baf04fa38fd>)

_Stop indicators show each available value on a slider_

Icons or text can be added outside the slider to indicate the range of values and make the slider more accessible. This can be used instead of a stop indicator.

![Plus and minus icons on each end of the slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkfou6-17.png?alt=media&token=1a441aac-51d1-452b-8a1a-ec87e9f5dafd>)

_Plus and minus icons, or text, can be added to the left and right of the slider_

### Orientation

Sliders can be oriented either horizontally or vertically, depending on what is best for your use case.

![Horizontal slider.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkhuu1-18.png?alt=media&token=9195aa36-4723-4ce8-91c3-c74694c2824e>)

_Standard slider in horizontal orientation_

![Vertical slider. Zero is at the bottom.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkj1b8-19.png?alt=media&token=09bb620d-8745-48b4-8b03-34cbdc78d9a2>)

_Standard slider in vertical orientation_

### Inset icon

Standard sliders that are M, L, or XL can include an icon within the track. This icon should illustrate what the slider controls. Avoid adding inset icons to XS or S sliders.

When there’s not enough space for the icon on the active track, like at a low value, the icon moves to the inactive track.

Consider swapping which icon is displayed at zero, like a volume icon becoming a mute icon.

![Inset icon on the active track when the handle is at 50%, and on the inactive track when the handle is at 0.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkmte8-20.png?alt=media&token=fef6e46e-5b1c-42fe-bf92-904ef3d1611a>)

_Inset icons change placement based on the handle_

![An inset icon on an XS slider. The icon bounds are cut off by the slider container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lni1k4-21%20-%20don't.png?alt=media&token=f36c7b22-9cce-489e-a73e-42d3a6dcae59>)

_Don’t use an inset icon with sliders that have track thicknesses under 40dp_

Don’t use inset icons on centered or range sliders. It makes it unclear where the start of the slider is.

![Centered slider with an inset icon on one end, and a stop indicator on the other.
](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lnlfie-22%20-%20don't.png?alt=media&token=2deb8b76-5ccd-4412-81ba-a098a903d102>)

_Don’t use an inset icon on a centered slider_

![Range slider with an inset icon on one end, and a stop indicator on the other.
](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lnor7e-23%20-%20don't.png?alt=media&token=6de66860-09fe-425f-b6b9-c6aa960411cc>)

_Don’t use an inset icon on a range slider_

### Size

Sliders come in different sizes: XS, S, M, L, and XL. Use larger sizes to increase the targets and provide a larger visual emphasis.

The active and inactive tracks should always be the same size.

![5 sizes of sliders.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkolpk-24.png?alt=media&token=f87a9fd7-2914-4706-bcce-9edf4814fbc1>)

_XS: 16dp; S: 24dp; M: 40dp; L: 56dp; XL: 96dp_

XL sliders should be reserved for hero moments, where the slider itself is the most important element on the page.

![An XL slider used to adjust living room temperature on mobile. No other controls are on screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7lkr4j4-25.png?alt=media&token=e382cedc-e5ef-4cd5-9fd8-eb0dd4b0e729>)

_XL sliders should be the focus of the page_

## Behaviors

### Select & drag

Select a value by dragging the handle.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tddmn-GM3-Components-Sliders-4-1-v01.mp4?alt=media&amp;token=17272aa8-8278-45d0-82b0-8de39ccde83b" title="Slider handle moves smoothly. The value increases and decreases by single digits."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tddmn-GM3-Components-Sliders-4-1-v01.mp4?alt=media&token=17272aa8-8278-45d0-82b0-8de39ccde83b>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tdv5o-GM3-Components-Sliders-4-2-v01.mp4?alt=media&amp;token=387283e5-b7ce-4ee2-b8d6-58f88d0a8af2" title="Slider handle snaps to the nearest multiple of 10 when moved."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tdv5o-GM3-Components-Sliders-4-2-v01.mp4?alt=media&token=387283e5-b7ce-4ee2-b8d6-58f88d0a8af2>)

### Select jump

Select a value by selecting part of the track.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tec5h-GM3-Components-Sliders-5-1-v01.mp4?alt=media&amp;token=ed186b3e-0cc8-4875-a2b7-c541130754e2" title="Slider handle moves smoothly to selected value of the slider."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tec5h-GM3-Components-Sliders-5-1-v01.mp4?alt=media&token=ed186b3e-0cc8-4875-a2b7-c541130754e2>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tep43-GM3-Components-Sliders-5-2-v01.mp4?alt=media&amp;token=f31f5f31-9bcd-4b8c-be5d-eb2b8268e0ba" title="Slider handle moves smoothly to the closest stop indicator value."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tep43-GM3-Components-Sliders-5-2-v01.mp4?alt=media&token=f31f5f31-9bcd-4b8c-be5d-eb2b8268e0ba>)

### Select & arrow

Select a value using the keyboard.

**Tab:** Focus lands on handle 

**Arrows:** Selected value increases or decreases by one value or stop indicator

**Space & arrows:** Selected value increases or decreases by a larger interval or stop indicator

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tf2t6-GM3-Components-Sliders-6-1-v01.mp4?alt=media&amp;token=ac491860-373d-4f82-a5c0-8b49514e8521" title="Tab selects the slider, and arrow keys move the value by 1."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tf2t6-GM3-Components-Sliders-6-1-v01.mp4?alt=media&token=ac491860-373d-4f82-a5c0-8b49514e8521>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tffc3-GM3-Components-Sliders-6-2-v01.mp4?alt=media&amp;token=ea5e3c11-2f1c-47ea-99ed-d688be210ad2" title="Tab selects the slider, and arrow keys move the value by 10."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx2tffc3-GM3-Components-Sliders-6-2-v01.mp4?alt=media&token=ea5e3c11-2f1c-47ea-99ed-d688be210ad2>)
