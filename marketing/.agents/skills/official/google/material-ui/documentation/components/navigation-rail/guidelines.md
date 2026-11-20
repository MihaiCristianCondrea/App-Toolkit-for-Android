---
title: "Navigation rail"
source_url: "https://m3.material.io/components/navigation-rail/guidelines"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/8bf7ead408fd-m0fueyh1-01.png"
  - "assets/images/0489d3943df4-malbc2cg-02.png"
  - "assets/images/71e6282281ed-m0fuf9qz-03.png"
  - "assets/images/4fde178157bb-m0fufhuq-04.png"
  - "assets/images/e65395badf22-m0fug0sy-05.png"
  - "assets/images/0f373611ed83-m0fugb5b-06.png"
  - "assets/images/e817d81f417e-macys6lz-07.png"
  - "assets/images/f2a164db839a-m0fuh0i5-08.png"
  - "assets/images/dea2224157ac-m0fuh7km-09.png"
  - "assets/images/cf9c1a2a801d-malbkctg-10.png"
  - "assets/images/750fd1ea0459-m0fuhrl8-11.png"
  - "assets/images/e5cbd05a483c-m0fuhyto-12.png"
  - "assets/images/d05ff03b93d5-m0fuicr9-13.png"
  - "assets/images/1c64593c3ef0-m0fujiob-14.png"
  - "assets/images/60e87d04a3b2-m0fujt7v-15.png"
  - "assets/images/e736c19847e2-m0fujxyu-16.png"
  - "assets/images/6e20d700b790-m0fuk4ze-17.png"
  - "assets/images/70c068fe092a-m0fukavr-18.png"
  - "assets/images/d6c45bb1e89f-m0fukhnw-19.png"
  - "assets/images/5af3ed06a0d4-m0fune8p-20.png"
  - "assets/images/8a91c01caedf-m0funlrw-21.png"
  - "assets/images/9fef5d4ae7d8-m0funqlw-22.png"
  - "assets/images/25befbe27d6c-m0fuomdi-23.png"
  - "assets/images/4512c6a1d31d-m0fuouq0-24.png"
  - "assets/videos/328829548808-mapifwv9-GM3_Expressive_Nav-Rail_Guidelines_32_IA_V10.mp4"
  - "assets/images/03ee68c713db-m0fupcvd-26.png"
  - "assets/videos/4f44e362be04-malqalnw-GM3_Expressive_Nav-Rail_Guidelines_27_IA_V07.mp4"
  - "assets/videos/ee7db26c732a-m5v1vvd7-J-Scrolling-RENDER-2x.mp4"
  - "assets/videos/b3567cecbcf4-m0gtjndl-K-Side-Scroll-RENDER-2x_240821.mp4"
  - "assets/videos/e2c390d1d3e8-mbs2jj48-GM3_Expressive_Nav-Rail_Guidelines_30_IA_v01-(1).mp4"
  - "assets/videos/ef17ff636c71-m0fvmjbe-L-NAV-Selection-RENDER-2x_240821.mp4"
  - "assets/videos/d925769cda5b-mapifi58-GM3_Expressive_Nav-Rail_Guidelines_PredictiveBack_IA_V02.mp4"
---

# Navigation rail

Navigation rails let people switch between UI views on mid-sized devices

## Guidelines

![Colorful, purple navigation rail shown collapsed and expanded.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fueyh1-01.png?alt=media&token=c9edba18-9dee-44b1-9657-4dd1cdf2ff74>)

_Use the menu icon to transition between collapsed and expanded navigation rails_

## Usage

The navigation rail can display navigation items, a menu, and a floating action button (FAB) in a vertical orientation.

There are two variants of navigation rails, **collapsed** and **expanded**, which can easily transform into each other when the menu button is selected.

### Collapsed

The **collapsed** nav rail runs along the leading edge of the window, and should contain 3–7 navigation items. It should not be hidden.

It can be used in medium to extra large window sizes, such as tablets and desktop. In  medium windows with few destinations, consider using a navigation bar instead. Compact windows should always use a navigation bar.

![Collapsed navigation rail with “timer” icon on FAB.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmalbc2cg-02.png?alt=media&token=8df4aa93-c874-4f14-98aa-8ebadcee272f>)

_A navigation rail should be the only visible navigation element_

### Expanded

The **expanded** navigation rail can be standard or modal, and should always open from a menu icon. An expanded rail can reveal secondary destinations not visible when collapsed.

The **standard** configuration is placed beside body content. It’s best for larger windows with lots of available space.

The **modal** configuration overlaps the body content, and should be opened from a menu icon. Use the modal configuration for:

-   Information dense layouts where space is limited
-   Products with many navigation items

![Expanded navigation rail shown expanded by default and expanded over screen content.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuf9qz-03.png?alt=media&token=3377b89c-7c1c-4a94-8282-d48530c4d81e>)

_A navigation rail can be expanded by default on larger screen sizes, or can be expanded over content on smaller screen sizes_

In immersive experiences, the expanded navigation rail can be hidden entirely, appearing only when the menu icon is selected.

The collapsed navigation rail should not be hidden.

![Navigation rail and hidden navigation rail with menu icon button for expansion.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fufhuq-04.png?alt=media&token=e13c10f7-7c71-4702-a6e5-a4d6842ad1b9>)

_The expanded navigation rail can also be hidden, appearing only when the menu icon is selected_

## Anatomy

![10 elements of expanded and collapsed navigation rails.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fug0sy-05.png?alt=media&token=465bf07d-fe91-4cfa-97c3-e9624609ed50>)

_Container; Menu (optional); Floating action button (FAB) (optional); Icon - active; Label text - active; Active indicator; Icon - inactive; Large badge (optional); Large badge label; Small badge; Label text - inactive_

### Container

The navigation rail should be placed on the leading edge of the window. This is the left side for left-to-right languages, and the right side for right-to-left languages.

The container fill can be turned off so the nav rail appears directly on the surface. When doing this, make sure all items have a minimum of 3:1 color contrast.

![Right-to-left navigation rail in Hebrew, and left-to-right navigation rail in English.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fugb5b-06.png?alt=media&token=9bd7ddad-b14f-4131-bdd4-bbdb07283569>)

_The navigation rail should be placed on the leading edge of the window_

The navigation rail should always run vertically along the side of a layout. Don’t make it horizontal.

Use a navigation bar for horizontal navigation.

![Horizontal navigation rail on timer screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmacys6lz-07.png?alt=media&token=274b5d1b-6978-4123-9456-4a822df58bc7>)

_Don’t use the navigation rail horizontally. Use a navigation bar instead._

Navigation rail items can be aligned as a group to the top or center of a layout. On tablets, use center alignment to make it easier to reach items.

The menu icon and FAB should always be top-aligned.

![Navigation rails with different alignments.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuh0i5-08.png?alt=media&token=32641191-5400-4aba-829c-138e2f3c6d1e>)

_Top and center aligned rail destination placement_

### Menu (optional)

The menu button can transition between the **collapsed** and **expanded** navigation rails.

Once expanded, the rail can reveal secondary destinations.

When the navigation rail is expanded, the menu icon should change to represent that it can be collapsed.

![Expanded and collapsed navigation rails controlled by a menu icon button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuh7km-09.png?alt=media&token=ae84210a-74d7-4fac-82f1-f7a0356b23d0>)

_A navigation rail can expand to reveal more destinations_

### Floating action button (FAB) (optional)

The container of the navigation rail is ideal for anchoring the FAB to the top of a screen, placing the app’s key action above navigation destinations.

When nested within another component, such as the navigation rail, the FAB's resting elevation should be [level 0](../../styles/elevation/applying-elevation.md).

![Navigation rail with a FAB button at the top of the screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmalbkctg-10.png?alt=media&token=41722433-f09f-412e-983f-1b585481a362>)

_A top-aligned FAB in the navigation rail_

![Navigation rail with a FAB button at the bottom of the screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuhrl8-11.png?alt=media&token=5e953e4b-20d1-4d7f-99f0-c89d39b052e9>)

_Avoid placing the FAB below navigation items_

The top of the rail can also be used for a logo, however avoid using logos that could be mistaken as buttons.

Don’t use a logo as a menu button to expand the navigation rail.

![Navigation rail with Material design logo at the top of the screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuhyto-12.png?alt=media&token=3e219498-f5cc-4a46-824c-4fb660bc7743>)

_Use caution when placing logos in the rail where they might be confused with an action or destination_

### Active indicator

The active indicator shows which page is being displayed.

![Navigation rail with active indicators present for the current screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuicr9-13.png?alt=media&token=7755879a-0c18-40e2-89c8-e51757d7c7b2>)

_Use the active indicator only for the current open page_

![Navigation rail with active indicators present for all navigation items.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fujiob-14.png?alt=media&token=ed0b9441-30cf-444e-9fde-c84be948b56d>)

_Don’t use the active indicator for more than one navigation item at a time_

The active indicator hugs the label text in the expanded nav rail. To achieve a similar style to the baseline navigation drawer, consider modifying the active indicator to fill the container.

The target area should always span the full width.

![Navigation rail with active indicator that hugs the text and icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fujt7v-15.png?alt=media&token=8405c84a-2929-4d68-8bbd-97120c4c41ab>)

_The active indicator hugs contents in the expanded nav rail_

![Navigation rail with active indicator that is larger than the content within it.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fujxyu-16.png?alt=media&token=bb433446-a6c0-433d-bdcb-181431e6e126>)

_Override the indicator to fill the container to more closely resemble the baseline navigation drawer_

### Icons

Navigation rail items must use icons that symbolize the content of their page. Browse popular icons on [Google Fonts](http://fonts.google.com/icons).

![Navigation rail with icons that fit the destinations, like a timer icon and label leading to a timer feature.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuk4ze-17.png?alt=media&token=8210a6c6-b488-46e3-aa46-ec3c18a3021d>)

_Icons should symbolize the content of the page they open_

When a destination is selected, the icon fills and changes color. An active indicator appears behind the icon.

![Icons with and without an active indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fukavr-18.png?alt=media&token=fa0ab3db-3f7a-47ff-84c2-b0d63ac890f7>)

_Selected navigation items have an active indicator, a filled icon, and a more prominent color_

### Label text

The label text should be a short, meaningful description of each navigation destination and another way for users to understand an icon’s meaning.

All navigation items require a one word label text.

![Navigation rail with clear text labels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fukhnw-19.png?alt=media&token=a2fe92ce-45d4-4c7b-8c51-5066b3cf7596>)

_Write clear and concise labels that describe the destination page_

Avoid wrapping long labels when possible. If necessary, create a line break between words, or hyphenate longer words.

![Navigation rail with lengthy text labels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fune8p-20.png?alt=media&token=aaa6d4fd-a007-4a85-ac3c-8063f4f0767e>)

_Break up longer phrases into two text lines if necessary_

Labels should be short enough to not be truncated. Don’t shrink the type scale to fit longer text labels.

![Navigation rail with truncated text label with ellipses. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0funlrw-21.png?alt=media&token=756fbcf4-1cf8-4157-8a41-69c43e76bc2e>)

_Don’t truncate or display an ellipsis in place of label text_

![Navigation rail with small text label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0funqlw-22.png?alt=media&token=f25512e4-0821-4d53-9b71-41f2870830fc>)

_Don’t reduce the type size to fit more characters into a destination label_

### Badges

Navigation rail icons can include badges to communicate dynamic information about the  destination, such as counts or status.

In compact nav rails, the badge is placed in the upper right corner of the icon. In expanded nav rails, the badge should be placed next to the label text.

![Navigation rail with badges on each icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuomdi-23.png?alt=media&token=faa3cb30-f478-4450-946a-5be944155717>)

_1. Small badge on a rail destination / 2. Large badge with a number / 3. Large badge with a maximum character count_

### Divider (optional)

A vertical divider can help separate the rail from app content. The divider should be positioned on the edge of the rail container that’s adjacent to the app’s content area.

![Navigation rail with divider separating it from screen content.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fuouq0-24.png?alt=media&token=92b886dc-b887-4991-9487-e7b720f909a8>)

_A divider can make the navigation rail container distinct from other on-screen content_

## Placement

In adaptive layouts, the navigation rail should be placed outside any panes, always along the leading edge of the window. Don’t place it within body content.

When the navigation rail is hidden, the body content can fill in the remaining space as long as the menu icon is still accessible.

Tabs can be used alongside a navigation rail to create an extra layer of visible navigation.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmapifwv9-GM3_Expressive_Nav-Rail_Guidelines_32_IA_V10.mp4?alt=media&amp;token=27d22a5d-f968-4b02-8f93-f1f8ed02abc9" title="Navigation rail collapsed next to screen content, and expanded overlaying screen content with secondary destinations."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmapifwv9-GM3_Expressive_Nav-Rail_Guidelines_32_IA_V10.mp4?alt=media&token=27d22a5d-f968-4b02-8f93-f1f8ed02abc9>)

## Adaptive design

For more, see [adaptive design](https://m3.material.io/m3/pages/layout-overview/adaptive-design/).

### Resizing

When moving from a large screen to a small screen, a navigation rail can transform into a navigation bar, providing the same quick access in a configuration that’s easier to use on smaller displays. Never use the navigation rail and navigation bar simultaneously. 

Only use navigation rails for medium window size classes and larger. Don’t use a navigation bar. If there are more than five destinations, consider using a modal expanded nav rail instead.

**Compact:** Don’t use a standard navigation rail for compact layouts due to space constraints. Use a navigation bar instead.

**Medium:** Use a navigation rail, especially if prioritizing persistent vertical navigation over maximizing vertical content space.

**Expanded to extra-large:** Use a navigation rail, not a navigation bar. Consider available horizontal space and the number of destinations when choosing between standard and modal.

![Navigation bar on a phone screen and navigation rail on a tablet screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fupcvd-26.png?alt=media&token=b1915086-e325-4b2a-946e-eb41d37a9ae7>)

_On smaller devices, use a navigation bar. On larger displays, use a navigation rail._

### Presentation

When the navigation rail transitions from collapsed to expanded, the contents of the page should automatically adjust to fit.

The contents of the navigation rail also expand to fill the space. For example, the FAB should transition into an extended FAB. 

Extra destinations can be shown in an expanded nav rail.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmalqalnw-GM3_Expressive_Nav-Rail_Guidelines_27_IA_V07.mp4?alt=media&amp;token=3769dd07-2a42-43fe-96d0-fac669ee36e3" title="Extra destinations in an expanded navigation rail."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmalqalnw-GM3_Expressive_Nav-Rail_Guidelines_27_IA_V07.mp4?alt=media&token=3769dd07-2a42-43fe-96d0-fac669ee36e3>)

## Behavior

### Scrolling

Destinations in the navigation rail should remain visible and fixed when scrolling vertically.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm5v1vvd7-J%20-%20Scrolling%20RENDER%20-%202x.mp4?alt=media&amp;token=1f63cc9d-8eab-4033-b6b6-ebce8724011e" title="Screen content scrolls while rails stay fixed."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm5v1vvd7-J%20-%20Scrolling%20RENDER%20-%202x.mp4?alt=media&token=1f63cc9d-8eab-4033-b6b6-ebce8724011e>)

If a layout scrolls horizontally, the rail can scroll off-screen or remain fixed. To distinguish that content is scrolling underneath the rail, use a divider or add elevation to the rail.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0gtjndl-K%20-%20Side%20Scroll%20RENDER%20-2x_240821.mp4?alt=media&amp;token=629dce43-bf6e-4ad6-89ca-71bb39bec10e" title="A nav rail with a divider between it and the page’s content."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0gtjndl-K%20-%20Side%20Scroll%20RENDER%20-2x_240821.mp4?alt=media&token=629dce43-bf6e-4ad6-89ca-71bb39bec10e>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmbs2jj48-GM3_Expressive_Nav-Rail_Guidelines_30_IA_v01%20(1).mp4?alt=media&amp;token=cbb06c17-5435-4dc4-a925-655fb6d559f3" title="A nav rail with slight elevation."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmbs2jj48-GM3_Expressive_Nav-Rail_Guidelines_30_IA_v01%20(1).mp4?alt=media&token=cbb06c17-5435-4dc4-a925-655fb6d559f3>)

### Selection

When a destination is tapped, the destination screen uses a [top level](../../styles/motion/transitions/transition-patterns.md#f852afd2-396f-49fd-a265-5f6d96680e16) transition pattern. In addition, the icon becomes filled and the active indicator expands from the center of the icon.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fvmjbe-L%20-%20NAV%20Selection%20-%20RENDER%202x_240821.mp4?alt=media&amp;token=cd5079bc-e9e6-4dc1-952d-a07105f70120" title="Selection transition between one navigation item and another."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fvmjbe-L%20-%20NAV%20Selection%20-%20RENDER%202x_240821.mp4?alt=media&token=cd5079bc-e9e6-4dc1-952d-a07105f70120>)

### Back

On Android, a gesture called predictive back allows people to swipe left or right on the screen to go back or dismiss modal components.

-   Previous screen is revealed in a preview to signal the destination

-   Predictive back only applies to the **modal expanded** navigation rail.

A list of compatible components is available on the [gestures page](https://m3.material.io/m3/pages/gestures/).

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmapifi58-GM3_Expressive_Nav-Rail_Guidelines_PredictiveBack_IA_V02.mp4?alt=media&amp;token=95c874c8-3be9-48c9-bbf3-3f1edb7cab09" title="Modal expanded navigation rail with Android predictive back feature."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmapifi58-GM3_Expressive_Nav-Rail_Guidelines_PredictiveBack_IA_V02.mp4?alt=media&token=95c874c8-3be9-48c9-bbf3-3f1edb7cab09>)
