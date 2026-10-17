---
title: "Bidirectionality &amp; RTL"
source_url: "https://m3.material.io/foundations/layout/bidirectionality-rtl"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/66230034630b-mp5maeep-01.png"
  - "assets/images/5657e5d3563d-mp3b8jsf-02-dont.png"
  - "assets/images/71a65740cb48-mp3ba9e6-03-dont.png"
  - "assets/images/503cac26447d-mp3bfuuv-04.png"
  - "assets/images/5ccf69d498da-mp3bglub-05.png"
  - "assets/images/bf635fdb979a-mp3bpnel-06.png"
  - "assets/images/2e768f3a6a34-mp5mhhbo-07.png"
  - "assets/images/49b3a7e75659-mp3bw5md-08.png"
  - "assets/images/9b947bc47f72-mp5mhm5a-09.png"
  - "assets/images/e0bb1d76bace-mp3c49y1-10.png"
  - "assets/images/205f12edc4f2-mp5mkcbq-11.png"
  - "assets/images/856d109809ab-mp5mr394-12.png"
  - "assets/images/ba1672ee4154-mp5ms0sj-13.png"
  - "assets/images/7d08d8327fb4-mp5ms3is-14.png"
  - "assets/images/c40f3e2c1682-mp3ct91t-15.png"
  - "assets/images/517a9714257e-mp5msyjc-16.png"
  - "assets/images/df7c459ca352-mp3kj0nq-18.png"
  - "assets/images/3025cf5a7b06-mp5mvifs-17.png"
  - "assets/images/2e2fdd572e93-mp3krmhd-19.png"
  - "assets/images/a7eb4328e9c5-mp3kv6hl-20.png"
  - "assets/images/e58f9d6b9ec3-mp3l10dl-21.png"
  - "assets/videos/0b01c75d881b-mp3l3v9p-GM3_Bidirectionality_Interaction-Gestures_6_IA_v02.mp4"
---

# Bidirectionality &amp; RTL

Design products that adapt to languages that read right-to-left (RTL)

[Over 2 billion people](https://www.w3.org/International/questions/qa-scripts.en.html) read and write in right-to-left (RTL) languages like Arabic, Hebrew, Farsi, and Urdu. Layouts should support both left-to-right (LTR) and RTL languages through mirroring and other best practices to ensure content is easy for global audiences to understand and navigate. Consider the holistic experience including [global writing](../content-design/global-writing/overview.md), localizing voice and [design principles for culturally appropriate icons](../../styles/icons/designing-icons.md#5f0e344b-17f8-4b91-b0e4-45671b9900f4).

Material's components are built to support RTL, such as naming elements and tokens as "leading" and "trailing." However, extra configuration may be needed to achieve specific RTL situations.

## Mirroring

When a layout is changed from LTR to RTL (or vice-versa), or flipped horizontally, it’s often called mirroring. UI elements and text that typically appear on the left in LTR aligns to the right. Reading flow starts from the top right corner, instead of the top left.

Not all elements mirror with RTL languages. For example, graphs and charts maintain a LTR directionality for Persian and Urdu.

![Layout in LTR and mirrored for RTL language.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5maeep-01.png?alt=media&token=62e7a678-88ba-4fa4-9371-07fc56b96f28>)

_A mirrored layout in an RTL language reverses the alignment and ordering of elements_

## Text rendering

Correct text rendering is foundational for a great user experience, and it’s critical for readability and usability. Text rendering has two parts:

1.  Alignment: How the edges of the text box are placed alongside other elements

2.  Directionality: How text and other elements flow within a text box, like left-to-right or right-to-left


In RTL languages, text is usually right-aligned, and elements flow from right-to-left.

Common issues with RTL language rendering are text entry, cursor position, punctuation, phone numbers, and URLs.

Improperly rendering text in RTL languages can create cognitive overload and negatively impact user sentiment and trust.

![Text field incorrectly displaying the word order of an email address and cursor placement.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3b8jsf-02-dont.png?alt=media&token=1b8e902e-4b79-4359-803b-a18d0855144c>)

_Don't reverse the order of the email username and domain (@google.com). The domain should always be to the right of the username. Usernames can still be written RTL, with the cursor moving to the left. Note: This example isn’t translated to illustrate a common issue with text rendering._

![Dialog window incorrectly displaying word order decreasing readability.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ba9e6-03-dont.png?alt=media&token=de78770e-bc7c-4cc7-b742-cebb1d61becd>)

_Don’t apply LTR directionality to RTL content, because it may scramble word order. To ensure readability across all languages, the content should have both RTL alignment and directionality. Note: This example isn’t translated to illustrate a common issue with text rendering._

## Icons & symbols

In RTL languages, directional UI icons, like back and forward, should be mirrored. However, in Hebrew, timelines and media controls on a page should retain left-to-right directionality.

The meaning of icons and symbols can vary significantly across cultures. For additional guidance, refer to [design principles for icons](../../styles/icons/designing-icons.md#5f0e344b-17f8-4b91-b0e4-45671b9900f4).

![Back and forward icons in LTR and RTL.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3bfuuv-04.png?alt=media&token=b112060c-8664-450f-8898-839afe3ad36b>)

_Back and foward icons are mirrored in RTL_

![Send and question mark icons in LTR and RTL.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3bglub-05.png?alt=media&token=25195988-66a7-4260-8262-50e9f895f487>)

_Send buttons are mirrored in RTL. Help icons are mirrored in some RTL languages, like Urdu and Persian._

## Time

Linear representations of time are often mirrored in RTL language experiences.

Linear progress indicators should move from right to left for most RTL languages, except Hebrew where it should remain LTR.

Circular representations of time remain the same.

![RTL linear progress indicator filling from right to left and circular progress indicator filling clockwise.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3bpnel-06.png?alt=media&token=8f3b445c-3751-4171-87f5-3381d25c0a74>)

_RTL linear progress indicator starts to fill progress from the right; Circular progress indicators move clockwise_

### Media players

Media controls for video or audio players are always LTR.

![Media player with control and progress in LTR and all other content is RTL.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5mhhbo-07.png?alt=media&token=5efd5142-de5d-4acb-87c6-7ebabde22a88>)

_In Urdu, controls and progress for media and a podcast title are shown in LTR, while all other content is RTL_

### Clocks

For RTL languages, the directionality of time remains LTR, and clocks still turn clockwise. However, the AM/PM symbols for 12h clocks should be placed to the left. The 24-hour clock is often used in countries where the primary language isn’t English.

Clock icons, circular refresh icons, and progress indicators with arrows pointing clockwise shouldn’t be mirrored.

![24-hour clock in RTL.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3bw5md-08.png?alt=media&token=4c55af62-54bf-4afe-9c04-b505a6af7c36>)

_24-hour clocks in RTL move clockwise, but mirror elements such as buttons_

![12-hour clock in RTL.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5mhm5a-09.png?alt=media&token=abeadbc8-7895-4579-b9a6-048594a35407>)

_12-hour clocks in RTL move clockwise, but mirror UI elements such as AM/PM and buttons_

## Canonical layout examples

### List-detail

The [list-detail layout](canonical-examples/list-detail.md):

-   Is a single-pane at compact breakpoints, switching between list and detail views

-   Divides the window into two side-by-side panes on large screens

-   Is mirrored in RTL

![RTL list layout on mobile.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3c49y1-10.png?alt=media&token=8570f5fd-4d02-4793-bdbe-0974bfeacfe8>)

_List-detail mirrored for RTL, where text and other elements are aligned to the right and flow from right to left_

### Feed

Use a [feed layout](canonical-examples/feed.md) to arrange content elements like cards in a configurable grid for quick, convenient viewing of a large amount of content. The feed layout is mirrored in RTL.

![RTL feed layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5mkcbq-11.png?alt=media&token=f775d890-6c1e-4b59-8cca-350b2b6f332e>)

_Feed layout mirrored for RTL, where the order of text, grid, and other elements align to the right and flow from right to left_

### Supporting pane

Use the [supporting pane layout](canonical-examples/supporting-pane.md) to organize content into primary and secondary display areas. The supporting pane layout is mirrored in RTL.

![RTL supporting pane in an RTL language.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5mr394-12.png?alt=media&token=f76d9f0c-cc2c-48ce-a09b-156b7566c090>)

_Supporting pane to the left of the primary content. Text and other elements within the pane are aligned to the right and flow from right to left._

## Component examples

### Badges

Change the position and alignment of [badges](../../components/badges/overview.md) for RTL languages.

![Small badge on the top left of a folder icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5ms0sj-13.png?alt=media&token=b714fee0-42d5-44dc-adf6-25f9ce100e05>)

_Small badge appears on the top left of the icon_

![Large badge on the top left of an image icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5ms3is-14.png?alt=media&token=02cce79e-7835-45c3-a61d-cd05b781ccd4>)

_Large badge appears on the top left of the icon_

### Toolbars

[Toolbars](../../components/toolbars/overview.md) provide actions related to the current page. For RTL languages, mirror the order of the tools.

![RTL floating toolbar. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3ct91t-15.png?alt=media&token=e8117c19-2f66-41e1-8e68-1cf624aac04f>)

_Mirrored floating toolbar, where the FAB appears on the left_

### App bars

[App bars](../../components/app-bars/overview.md) are placed at the top of the screen to help people navigate through a product:

-   Mirror an app bar’s layout in RTL

-   Flip appropriate icons, such as arrows

![4 app bars in RTL.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5msyjc-16.png?alt=media&token=e9adcdc3-1cac-4b08-9aaa-fd254f4fee12>)

_RTL center-aligned, small app bars; RTL medium, flexible app bar; RTL large, flexible app bar_

### Navigation rail

The [navigation rail](../../components/navigation-rail/overview.md) is placed on the leading edge of the screen, on the left side for LTR, and on the right for RTL.

![Nav rail on the right side for an RTL language, and left side for LTR.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3kj0nq-18.png?alt=media&token=9eda99bd-995f-46b0-a779-50c10a48309d>)

_Based on the language, a navigation rail is set on a screen’s leading edge: Right side for RTL languages; Left side for LTR languages_

### Expanded navigation rail

Expanded navigation rails that open from the side are always placed on the leading edge of the screen, on the left for LTR languages, and on the right for RTL.

![RTL expanded navigation rail, including mirrored icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5mvifs-17.png?alt=media&token=744640b0-157d-4f3e-96df-9a58e5fd7cf9>)

_RTL expanded navigation rails open from the leading edge and should include mirrored icons_

### Text fields

Icons in [text fields](../../components/text-fields/guidelines.md#5c8a5f07-b1a5-455f-bf76-7ff0d724f6b0) are optional. Leading and trailing icons change their position based on LTR or RTL contexts.

![Text fields in RTL with leading and trailing icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3krmhd-19.png?alt=media&token=b909d6ab-5466-4fd9-9ed2-ed6a6db7884c>)

_Icons, symbols, and label text for RTL: Icon signifier; Valid or error icon; Clear icon; Voice input icon; Dropdown icon; Image_

### Chips

The leading icon of input chips can be an icon, logo, or circular image.

The trailing icon is always aligned to the end side of the container. It’s placed on the right for LTR and on the left for RTL.

![Filter chips with checkmark icons in RTL layout.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3kv6hl-20.png?alt=media&token=4a4f624a-8980-44ce-92a1-bbd2960615b4>)

_Filter chips shown in an RTL layout. Note: This example is not translated to help illustrate mirroring._

## Swipe gestures

Gestures are the ways people interact with UI elements using touch or body motion.

People can navigate horizontally between peer views like tabs and to complete actions.

RTL swiping and gestures should mirror their counterparts in LTR. If a product includes a delete icon revealed when swiped from the right for LTR languages, the same should be possible on the left for RTL languages.

![RTL list layout with swipe gesture revealing additional actions.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3l10dl-21.png?alt=media&token=408174c2-deb9-4959-ad91-e5d3e98ca420>)

_Swiping reveals additional action in RTL list layout_

On Android, [predictive back](https://github.com/material-components/material-components-android/blob/master/docs/foundations/PredictiveBack.md) allows people to swipe left or right on the screen to go back or dismiss modal components.

RTL predictive back features should mirror those found in a LTR context.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3l3v9p-GM3_Bidirectionality_Interaction%20Gestures_6_IA_v02.mp4?alt=media&amp;token=76864157-f79f-4b54-a468-20af37b0af23" title="Back swipe for RTL languages. The back swipe on a bottom sheet takes person back to previous screen of a photo feed."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp3l3v9p-GM3_Bidirectionality_Interaction%20Gestures_6_IA_v02.mp4?alt=media&token=76864157-f79f-4b54-a468-20af37b0af23>)
