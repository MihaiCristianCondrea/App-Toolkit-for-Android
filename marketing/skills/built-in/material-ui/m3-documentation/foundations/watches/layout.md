---
title: "Design for watches"
source_url: "https://m3.material.io/foundations/watches/layout"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/9040c7aa8fd3-mp5b7fkb-01.png"
  - "assets/images/d00fd5e2bdbe-mp5bb5xm-02.png"
  - "assets/images/032d06f452ba-mp5b8fz0-03.png"
  - "assets/images/ca7b8c5eaafa-mp5b97lf-04.png"
  - "assets/images/881b28284b7d-mp5bbu4k-05.png"
  - "assets/images/c219b5a4fd56-mp5bcjy6-06.png"
  - "assets/images/bf51650d02ea-mp5bd0o1-07.png"
  - "assets/images/c538f361af6b-mp5bdo60-08.png"
  - "assets/images/895ebb5afe8b-mp5be7hd-09.png"
  - "assets/images/dd8de2ca2c26-mp5beqyb-10.png"
---

# Design for watches

Watches have special design considerations and interaction patterns

## Layout

## Resources

| Type | Resource |
| --- | --- |
| Design | [Wear OS common design layouts](https://developer.android.com/design/ui/wear/guides/foundations/common-layouts) |
| [Adaptive layout for Wear OS](https://developer.android.com/design/ui/wear/guides/foundations/adaptive-design) |  |
| [Figma Design Kit for Wear OS Apps](https://www.figma.com/community/file/1506418396052412186) |  |
| [Figma Design Kit for Wear OS Tiles](https://www.figma.com/community/file/1507852095734722321/m3-wear-os-tiles-design-kit?fuid=1348995927136540612) |  |
| Implementation | [Android Developers: Wear OS](https://developer.android.com/training/wearables) |

## Layout principles

![A social networking scrolling screen showing Followers, sorting and search.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5b7fkb-01.png?alt=media&token=372c44fa-e4a8-4cd1-9108-b3b5a5b31d7f>)

_Prioritize content Place the most important information at the top of the screen._

![An audio interface showing headphones are connected with volume indicator and controls.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bb5xm-02.png?alt=media&token=870f4bf5-5ff6-4299-ab88-6b23889af259>)

_Limit choices Reduce the number of actions to prevent decision fatigue. Focus on critical tasks to help people get things done within seconds._

![A dialog confirming device access to call log and contacts.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5b8fz0-03.png?alt=media&token=6281ff0e-022e-48d4-afe3-6b5b55ff7f9a>)

_Simplify navigation Use a clear, shallow hierarchy so people don't get lost in complex menus. Aim to display content and navigation inline._

## Standard layouts

For scrolling and non-scrolling apps:

-   The time is shown on most app screens

-   Edge-hugging buttons are used for round screens

-   Show an indicator if more content is available on a scrolling apps


Wear OS offers [Figma Design Kits](https://developer.android.com/design/ui/wear/guides/get-started/design-kits) for standard layouts, with components, styles, and variables.

![A watch screen showing a social media app and a watch screen showing a timer with element indicators.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5b97lf-04.png?alt=media&token=e8cf1f6e-0e5c-4597-baff-9cb3bae9cd85>)

_Time text; Page title; Scroll indicator; Action button_

### Non-scrolling layouts

Non-scrolling layouts are for focused tasks or single-screen interactions where all content fits within the display, such as: 

-   Media players

-   Pickers and switchers

-   Fitness tracking screens

-   Confirmation dialogs 


[More on non-scrolling layouts for Wear OS](https://developer.android.com/design/ui/wear/guides/surfaces/apps/layouts/non-scrolling)

![A non-scrollable timer with current time 9:30, timer set for 00:25:52, and a start button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bbu4k-05.png?alt=media&token=ef64cbab-63b9-4c58-8eae-585f1d5918e2>)

_Use non-scrolling layouts for focused tasks like a timer_

### Scrolling layouts

Scrolling layouts can show content that exceeds the screen height, such as lists or dialogs. These might include:

-   Message threads

-   Contact lists

-   Menu options


[More on scrolling layouts for Wear OS](https://developer.android.com/design/ui/wear/guides/surfaces/apps/layouts/scrolling)

![A scrollable layout with current time, search button, 1st follower and avatar on screen, and two followers off screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bcjy6-06.png?alt=media&token=f1e845c4-911a-46e2-8eb2-2fe90ae7caa9>)

_Lists use scrolling layouts to show additional options_

### Tiles

Tiles (or widgets) are designed for glanceability. Use them to show timely updates or to help people perform frequent tasks quickly, such as checking progress towards a goal or viewing the weather.

Tiles are accessible with a swipe from the watch face. They have a fixed screen height and don't scroll.

[More on tiles for Wear OS](https://developer.android.com/design/ui/wear/guides/foundations/common-layouts/tiles)

![Icon buttons for meditation, running, cycling, and a More button](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bd0o1-07.png?alt=media&token=5de6ae1f-1f00-46b6-9f04-4d5a29562647>)

_Use tiles for quick access to to a few key options_

### Notifications

Notifications can be expanded to offer more interactions, such as replying to a message, opening a location on a map, or playing a song.

Wear OS provides notification templates for instant messaging and calendar events.

[More on notifications for Wear OS](https://developer.android.com/training/wearables/notifications)

![A message notification shown in a drawer with dimmed notifications above and below.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bdo60-08.png?alt=media&token=392440c1-fab7-4a6e-9e6c-e3be31254fe8>)

_Notifications should offer easy access to more interactions_

### Adaptive layout

Adaptive design allows apps to adapt to different screen sizes and device contexts. On Wear OS, this means apps scale and reorganize to maximize the available space on both small and large round displays. The [Material 3 Compose component library](https://developer.android.com/jetpack/androidx/releases/compose-material3?_gl=1*1ntglil*_ga*NzMzMjg1Nzc1LjE3NDg5MTM2NjI.*_ga_QPQ2NRV856*czE3NjQ3MDkwMjUkbzc2JGcxJHQxNzY0NzEwNjAyJGo0MCRsMCRoMA..) has built-in adaptive behavior.

-   Design for small screens first: Start by designing for the smallest common screen size

-   Use percentages: Define margins and padding using percentages rather than fixed pixel values. This prevents clipping and ensures content remains centered and proportional as the screen size increases.

-   Add value on larger screens: Use the extra space on screens larger to show more content, such as additional buttons, text lines, or data visualizations

-   Test all font sizes: Font scaling and accessibility settings such as bold text may cause changes in the size of UI elements


[More on adaptive layout for Wear OS](https://developer.android.com/design/ui/wear/guides/foundations/adaptive-design)

![5, 10, and 15 minute alarm buttons plus an edge-hugging more button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5be7hd-09.png?alt=media&token=4e342160-cf72-4b39-8653-068592363c24>)

_Design for small screens first, starting with a 192dp size watch_

![5, 10, 15, 20 &amp; 30 minute alarm buttons plus an edge-hugging more button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5beqyb-10.png?alt=media&token=4bcb6fbb-d9e7-4bec-b54e-3a3e4ffbf486>)

_Show more content on devices that are larger than 225dp_
