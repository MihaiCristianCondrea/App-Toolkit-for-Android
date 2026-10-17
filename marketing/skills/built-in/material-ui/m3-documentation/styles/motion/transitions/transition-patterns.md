---
title: "Transitions"
source_url: "https://m3.material.io/styles/motion/transitions/transition-patterns"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/videos/356db3dca250-lyfqbdij-GM3-Transitions-TransitionPatterns-1-v01.mp4"
  - "assets/videos/7f23527ca4f9-lyfqc556-GM3-Transitions-TransitionPatterns-2-v01.mp4"
  - "assets/videos/9fac559798f3-lyfqdipk-GM3-Transitions-TransitionPatterns-3-1-v01.mp4"
  - "assets/videos/a3081dfc0546-lyfrbw3w-GM3-Transitions-TransitionPatterns-3-2-v01.mp4"
  - "assets/videos/a9040badce9e-lyfrcr6e-GM3-Transitions-TransitionPatterns-3-3-v01.mp4"
  - "assets/videos/409262821763-lyfrdrxa-GM3-Transitions-TransitionPatterns-3-4-v01.mp4"
  - "assets/videos/6c3ac0233a86-lyfrem0q-GM3-Transitions-TransitionPatterns-3-5-v01.mp4"
  - "assets/videos/01dae70ff2da-lyfrf9ld-GM3-Transitions-TransitionPatterns-3-6-v01.mp4"
  - "assets/videos/189cfb0bf931-lyfrilua-GM3-Transitions-TransitionPatterns-4-v01.mp4"
  - "assets/videos/19245716b8d8-lyfrjhm3-GM3-Transitions-TransitionPatterns-4-1-v01.mp4"
  - "assets/videos/d554d7f84b5b-lyfrkcfn-GM3-Transitions-TransitionPatterns-4-2-v01.mp4"
  - "assets/videos/1a58c6ab4004-lyfrldpn-GM3-Transitions-TransitionPatterns-4-3-v01.mp4"
  - "assets/videos/fea3ddf80878-lyfrmnfg-GM3-Transitions-TransitionPatterns-4-4-v01.mp4"
  - "assets/videos/c74d3e2a1b42-lyfrnslm-GM3-Transitions-TransitionPatterns-5-1-v01.mp4"
  - "assets/videos/10f8bcf6de27-lyfroshf-GM3-Transitions-TransitionPatterns-5-2-v01.mp4"
  - "assets/videos/fe56f7bed3ed-lyfrpmn2-GM3-Transitions-TransitionPatterns-5-3-v01.mp4"
  - "assets/videos/487fe47b9923-lyfrrw39-GM3-Transitions-TransitionPatterns-6-1-v01.mp4"
  - "assets/videos/705d5cbe1254-lyfs0nn2-GM3-Transitions-TransitionPatterns-6-2-v01.mp4"
  - "assets/videos/95bb50dc19d1-lyfs1jfp-GM3-Transitions-TransitionPatterns-6-3-v01.mp4"
  - "assets/videos/6935297f2de1-lyfs2znj-GM3-Transitions-TransitionPatterns-7-1-v01.mp4"
  - "assets/videos/4858c3a0fe42-lyfs4dk9-GM3-Transitions-TransitionPatterns-7-2-v01.mp4"
  - "assets/videos/1d5a42ef6c82-lyfs56ki-GM3-Transitions-TransitionPatterns-7-3-v01.mp4"
  - "assets/videos/31373e115b97-lyfs6out-GM3-Transitions-TransitionPatterns-7-4-v01.mp4"
  - "assets/videos/752548751189-lygxnh2p-GM3-Transitions-TransitionPatterns-7-5-v01.mp4"
  - "assets/videos/3b3e64ca62b1-lygxorck-GM3-Transitions-TransitionPatterns-7-6-v01.mp4"
  - "assets/videos/1cd1de81c699-lygxpdy1-GM3-Transitions-TransitionPatterns-7-7-v01.mp4"
  - "assets/videos/633628cee03b-lygxqtbv-GM3-Transitions-TransitionPatterns-7-8-v01.mp4"
  - "assets/videos/6a3ca7191b60-lygxs24v-GM3-Transitions-TransitionPatterns-8-1-v01.mp4"
  - "assets/videos/b0218a7ef29f-lygxtkjx-GM3-Transitions-TransitionPatterns-8-2-v01.mp4"
  - "assets/videos/754dcd9a49d8-lygxuime-GM3-Transitions-TransitionPatterns-8-3-v01.mp4"
---

# Transitions

Transitions help guide people as they navigate an app

## Transition patterns

star

Note:

M3 transitions use the legacy easing and duration system. They'll eventually be updated to use the motion physics system.

Transitions are short animations that connect individual elements or full-screen views of an app. They are fundamental to a great user experience because they help users understand how an app works. Well-designed transitions makes an experience feel high quality and expressive. They should be the top priority for a strong motion implementation. 

These are six common transition patterns:

1.  [Container transform](transition-patterns.md#b67cba74-6240-4663-a423-d537b6d21187)
2.  [Forward and backward](transition-patterns.md#df9c7d76-1454-47f3-ad1c-268a31f58bad)
3.  [Lateral](transition-patterns.md#8d4ec98f-60dc-47a9-901e-88fa2c43f18a)
4.  [Top level](transition-patterns.md#f852afd2-396f-49fd-a265-5f6d96680e16)
5.  [Enter and exit](transition-patterns.md#e1c2a650-d7a4-4a6d-9025-e6b7845291ed)
6.  [Skeleton loaders](transition-patterns.md#b39a0641-1b44-4864-83f5-fac38e0bd94a)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfqbdij-GM3-Transitions-TransitionPatterns-1-v01.mp4?alt=media&amp;token=37344f9a-dc0e-4bf7-86a3-826d46ba788b" title="Small, colorful blobs are animated with different motion styles corresponding to the six transition patterns."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfqbdij-GM3-Transitions-TransitionPatterns-1-v01.mp4?alt=media&token=37344f9a-dc0e-4bf7-86a3-826d46ba788b>)

## Container transform

This pattern is used to seamlessly transform an element to show more detail, like a Card expanding into a details page.

-   **Commonly used with:** Cards, lists, image galleries, search boxes, sheets, FABs, and chips
-   **Read more:** [UX Research](https://material.io/blog/motion-research-container-transform), [Guidelines](applying-transitions.md#50f9fc3f-c7e2-4099-b614-7c36b1c5285d), [Android implementation](https://github.com/material-components/material-components-android/blob/master/docs/theming/Motion.md#container-transform)

Persistent elements are used to seamlessly connect the start and end state of the transition. The most common persistent element is a container, which is a shape used to represent an enclosed area. It can also be an important element, like a hero image. Of all transition patterns, this one creates the strongest relationship between elements. It's also perceived to be the most expressive.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfqc556-GM3-Transitions-TransitionPatterns-2-v01.mp4?alt=media&amp;token=0c23dd89-b0ec-445b-80fe-50c04378ed4d" title="Podcast app is selected from home screen, then individual card is selected, both taking over the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfqc556-GM3-Transitions-TransitionPatterns-2-v01.mp4?alt=media&token=0c23dd89-b0ec-445b-80fe-50c04378ed4d>)

#### Between full-screen views

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfqdipk-GM3-Transitions-TransitionPatterns-3-1-v01.mp4?alt=media&amp;token=e5a7ca2c-e68c-42f9-8e15-669bc5136c9a" title="1-year memory of flowers is selected within a carousel and takes over screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfqdipk-GM3-Transitions-TransitionPatterns-3-1-v01.mp4?alt=media&token=e5a7ca2c-e68c-42f9-8e15-669bc5136c9a>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrbw3w-GM3-Transitions-TransitionPatterns-3-2-v01.mp4?alt=media&amp;token=30769f42-b523-4302-83a3-3bc23f0f7e11" title="Individual message is selected in messaging app and expands to view complete message."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrbw3w-GM3-Transitions-TransitionPatterns-3-2-v01.mp4?alt=media&token=30769f42-b523-4302-83a3-3bc23f0f7e11>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrcr6e-GM3-Transitions-TransitionPatterns-3-3-v01.mp4?alt=media&amp;token=8dd88ac4-99df-4d53-966f-2e9021a23b4b" title="Grocery list is selected within notes and expands to take over screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrcr6e-GM3-Transitions-TransitionPatterns-3-3-v01.mp4?alt=media&token=8dd88ac4-99df-4d53-966f-2e9021a23b4b>)

#### Within a screen

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrdrxa-GM3-Transitions-TransitionPatterns-3-4-v01.mp4?alt=media&amp;token=7bf2552b-c418-4154-87c4-3ccb336861c0" title="Search box for replies within messaging app expands to reveal recent searches and contacts."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrdrxa-GM3-Transitions-TransitionPatterns-3-4-v01.mp4?alt=media&token=7bf2552b-c418-4154-87c4-3ccb336861c0>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrem0q-GM3-Transitions-TransitionPatterns-3-5-v01.mp4?alt=media&amp;token=7c4de1b8-3f8f-460a-b2f6-5c52f845a5cf" title="FAB is selected and expands to reveal  Compose Now action and shortlist of contacts."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrem0q-GM3-Transitions-TransitionPatterns-3-5-v01.mp4?alt=media&token=7c4de1b8-3f8f-460a-b2f6-5c52f845a5cf>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrf9ld-GM3-Transitions-TransitionPatterns-3-6-v01.mp4?alt=media&amp;token=a5dc8901-3704-4be7-b8a6-ddb0889fe4cd" title="Song is selected from banner on bottom of screen and expands to take over."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrf9ld-GM3-Transitions-TransitionPatterns-3-6-v01.mp4?alt=media&token=a5dc8901-3704-4be7-b8a6-ddb0889fe4cd>)

## Forward and backward

This pattern is used for navigating between screens at consecutive levels of hierarchy, like navigating from an inbox to a message thread.

-   **Commonly used with**: Lists, cards, buttons, links
-   **Read more:** [Guidelines](applying-transitions.md#41b11a78-b88f-4972-904c-880bc348acc8), [Android implementation](https://github.com/material-components/material-components-android/blob/master/docs/theming/Motion.md#shared-axis)

A horizontal sliding motion indicates moving forward or backward between screens. Android and iOS have different default styles:

**1\. Android** uses a fade as screens slide. This reduces the amount of motion, since the screens don't have to slide the full width of the device.

**2.** **iOS** uses a parallax effect, meaning the background slides slower than the foreground. This also reduces the amount of motion.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrilua-GM3-Transitions-TransitionPatterns-4-v01.mp4?alt=media&amp;token=fa5b76e6-9baf-41a5-b0bb-a46b1eae0b07" title="Two motions contrasted in messaging app, with top showing Android’s fading movement and bottom showing iOS’s swiftly moving foreground transition."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrilua-GM3-Transitions-TransitionPatterns-4-v01.mp4?alt=media&token=fa5b76e6-9baf-41a5-b0bb-a46b1eae0b07>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrjhm3-GM3-Transitions-TransitionPatterns-4-1-v01.mp4?alt=media&amp;token=f5f1caac-0bba-4402-9e12-d7550905bc9f" title="WiFi set-up screen demonstrates Android fade effect when next button is selected."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrjhm3-GM3-Transitions-TransitionPatterns-4-1-v01.mp4?alt=media&token=f5f1caac-0bba-4402-9e12-d7550905bc9f>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrkcfn-GM3-Transitions-TransitionPatterns-4-2-v01.mp4?alt=media&amp;token=31167b8c-9aa6-44e0-8583-f1159a42aba2" title="Music card uses iOS transition to move back to music app."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrkcfn-GM3-Transitions-TransitionPatterns-4-2-v01.mp4?alt=media&token=31167b8c-9aa6-44e0-8583-f1159a42aba2>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrldpn-GM3-Transitions-TransitionPatterns-4-3-v01.mp4?alt=media&amp;token=6ca0882e-0f25-4aa5-bcfe-a834b427c409" title="Fading transitions between photos app search bar and selection of Family &amp;amp; friends option."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrldpn-GM3-Transitions-TransitionPatterns-4-3-v01.mp4?alt=media&token=6ca0882e-0f25-4aa5-bcfe-a834b427c409>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrmnfg-GM3-Transitions-TransitionPatterns-4-4-v01.mp4?alt=media&amp;token=f7c9ab56-e46f-43db-8569-b50966797597" title="Email is selected from inbox demonstrating Android transition."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrmnfg-GM3-Transitions-TransitionPatterns-4-4-v01.mp4?alt=media&token=f7c9ab56-e46f-43db-8569-b50966797597>)

## Lateral

This pattern is used for navigating between peer content at the same level of hierarchy, like swiping between tabs of a content library.

-   **Commonly used with:** Tabs, carousels, and image galleries
-   **Read more:** [Guidelines](applying-transitions.md#3d5c16ce-7350-4a33-9d2b-598a7591d4e6)

Lateral transitions use a sliding motion similar to a forward and backward pattern, but it does not use a fade or parallax effect. Instead elements are grouped and slide in unison, creating a strong peer relationship. This also hints at being able to gesturally swipe elements to navigate.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrnslm-GM3-Transitions-TransitionPatterns-5-1-v01.mp4?alt=media&amp;token=09d0ed16-e36b-4ce4-af48-cf21f4f82b5c" title="Toggling between tabs for artists and albums in music app reveals lateral movement."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrnslm-GM3-Transitions-TransitionPatterns-5-1-v01.mp4?alt=media&token=09d0ed16-e36b-4ce4-af48-cf21f4f82b5c>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfroshf-GM3-Transitions-TransitionPatterns-5-2-v01.mp4?alt=media&amp;token=2927b2a2-8ac7-4f9c-9f90-607efa692e93" title="Photos are swiped through horizontally, each filling the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfroshf-GM3-Transitions-TransitionPatterns-5-2-v01.mp4?alt=media&token=2927b2a2-8ac7-4f9c-9f90-607efa692e93>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrpmn2-GM3-Transitions-TransitionPatterns-5-3-v01.mp4?alt=media&amp;token=6ceb61d3-2aef-4e50-aa04-cf1f5087f874" title="Carousel is scrolled through horizontally without use of fades."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrpmn2-GM3-Transitions-TransitionPatterns-5-3-v01.mp4?alt=media&token=6ceb61d3-2aef-4e50-aa04-cf1f5087f874>)

## Top level

This pattern is used to navigate between top-level destinations of an app, like tapping a destination in a Navigation bar.

-   **Commonly used with:** Navigation bar, navigation rail, and navigation drawer
-   **Read more:** [Guidelines](applying-transitions.md#ab8885f6-5517-419d-80de-bea50cd10467), [Android implementation](https://github.com/material-components/material-components-android/blob/master/docs/theming/Motion.md#fade-through)

The exiting screen quickly fades out and then the entering screen fades in. Since the content of top level destinations isn't necessarily related, the motion intentionally does not use grouping or persistent elements to create a strong relationship between screens.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrrw39-GM3-Transitions-TransitionPatterns-6-1-v01.mp4?alt=media&amp;token=870012dd-e750-4cab-b482-38664dc86ad3" title="Home and Music options in the navigation are toggled between with fading indicating distinctness of items."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfrrw39-GM3-Transitions-TransitionPatterns-6-1-v01.mp4?alt=media&token=870012dd-e750-4cab-b482-38664dc86ad3>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs0nn2-GM3-Transitions-TransitionPatterns-6-2-v01.mp4?alt=media&amp;token=b0b86abe-7f8b-4c46-badd-809fbf94192e" title="Inbox menu item selected fades to reveal inbox contents."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs0nn2-GM3-Transitions-TransitionPatterns-6-2-v01.mp4?alt=media&token=b0b86abe-7f8b-4c46-badd-809fbf94192e>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs1jfp-GM3-Transitions-TransitionPatterns-6-3-v01.mp4?alt=media&amp;token=e4eacdac-a0ee-4a02-8d09-e50445052491" title="Chat and Mail options in navigation both produce swift transitions."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs1jfp-GM3-Transitions-TransitionPatterns-6-3-v01.mp4?alt=media&token=e4eacdac-a0ee-4a02-8d09-e50445052491>)

## Enter and exit

This pattern is used to introduce or remove a component on the screen. Components can enter and exit within the screen bounds, like a dialog appearing over an app. They can also enter and exit by crossing the screen bounds, like a navigation drawer or bottom sheet that slides on and off screen.

### Within screen bounds

-   **Commonly used with:** FABs, dialogs, menus, snackbars, time pickers and tooltips
-   **Read more:** [Guidelines](applying-transitions.md#56675bd6-5e69-4fa8-b075-d694e8cb3ad4)

**Android** components expand and collapse along the x or y axis as they enter and exit. Scale and z-axis motion is avoided since they imply elevation change, which doesn't match M3's reduced elevation model.

**iOS** components uniformly scale as they enter and fade out to exit.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs2znj-GM3-Transitions-TransitionPatterns-7-1-v01.mp4?alt=media&amp;token=cc20bc71-2e97-42c5-b08c-4cf7f32ce059" title="Message to permanently delete an email pops up without elevation for both Android and iOS."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs2znj-GM3-Transitions-TransitionPatterns-7-1-v01.mp4?alt=media&token=cc20bc71-2e97-42c5-b08c-4cf7f32ce059>)

The direction a component enters is informed by their location on screen, expanding away from the device edge. A menu at the top of the screen expands downwards, and a snackbar at the bottom of the screen expands upwards.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs4dk9-GM3-Transitions-TransitionPatterns-7-2-v01.mp4?alt=media&amp;token=5ea9b2aa-b72c-4a22-b72b-4a08fa05f323" title="Image menu in upper right corner expands downward."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs4dk9-GM3-Transitions-TransitionPatterns-7-2-v01.mp4?alt=media&token=5ea9b2aa-b72c-4a22-b72b-4a08fa05f323>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs56ki-GM3-Transitions-TransitionPatterns-7-3-v01.mp4?alt=media&amp;token=7ff7b7a9-3ab7-482b-98cd-8ea1add1fa77" title="A snackbar alert pops up from bottom of inbox screen and FAB emerges from lower right."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs56ki-GM3-Transitions-TransitionPatterns-7-3-v01.mp4?alt=media&token=7ff7b7a9-3ab7-482b-98cd-8ea1add1fa77>)

### Beyond screen bounds

-   **Commonly used with:** App bars, banners, navigation bar, navigation rail, navigation drawer, and sheets
-   **Read more:** [Guidelines](applying-transitions.md#1b704202-167d-48d5-bca1-614cf050de1b)

**Android** components expand and collapse along the x or y axis as they slide on and off screen. This emphasizes their shape, making an otherwise simple transition more expressive.

**iOS** components slide on and off screen without changing shape.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs6out-GM3-Transitions-TransitionPatterns-7-4-v01.mp4?alt=media&amp;token=ff23a41c-989a-45a4-95ab-1d898b942148" title="Android and iOS demonstrations of navigation emerging from left edge and new message alert emerging from top edge."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flyfs6out-GM3-Transitions-TransitionPatterns-7-4-v01.mp4?alt=media&token=ff23a41c-989a-45a4-95ab-1d898b942148>)

Components like a side sheet can also enter and exit at the same elevation as the main content. Coplanar sheets shrink the available area for content.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxnh2p-GM3-Transitions-TransitionPatterns-7-5-v01.mp4?alt=media&amp;token=5e781c81-9041-407b-bea7-58a93d13f889" title="Selection of comments icon shrinks the main draft field."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxnh2p-GM3-Transitions-TransitionPatterns-7-5-v01.mp4?alt=media&token=5e781c81-9041-407b-bea7-58a93d13f889>)

Components can enter and exit from beyond the screen bounds based on a scroll gesture. This allows for more screen space to browse.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxorck-GM3-Transitions-TransitionPatterns-7-6-v01.mp4?alt=media&amp;token=bc2ebd28-4182-4699-a155-d2a2e8dcee0b" title="Top Stories header disappears as user scrolls deeper into news."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxorck-GM3-Transitions-TransitionPatterns-7-6-v01.mp4?alt=media&token=bc2ebd28-4182-4699-a155-d2a2e8dcee0b>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxpdy1-GM3-Transitions-TransitionPatterns-7-7-v01.mp4?alt=media&amp;token=c4e1de2b-4fe7-4f6d-869c-b905fd4dcd7f" title="Scrolling lower through photos hides navigation, saving room for browsing."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxpdy1-GM3-Transitions-TransitionPatterns-7-7-v01.mp4?alt=media&token=c4e1de2b-4fe7-4f6d-869c-b905fd4dcd7f>)

The location components enter and exit help establish a coherent spatial model of an app:

1.  A **notification** enters from the top indicating the notification drawer can also be pulled down from the top
2.  A **nav drawer** enters from the left helping users understand where it's located when it's off screen
3.  A **bottom sheet** and the keyboard enters from the bottom of the screen. This is a sensible default location for sheets to enter since the bottom of the screen is easiest to reach.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxqtbv-GM3-Transitions-TransitionPatterns-7-8-v01.mp4?alt=media&amp;token=2a6aaa1b-6445-41f7-84d5-4c49d6d54cfe" title="Email notification emerges from top of screen, navigation emerges from left, and keyboard enter from below, all in sequence."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxqtbv-GM3-Transitions-TransitionPatterns-7-8-v01.mp4?alt=media&token=2a6aaa1b-6445-41f7-84d5-4c49d6d54cfe>)

## Skeleton loaders

This pattern is used to transition from a temporary loading state to a fully loaded UI.

-   **Read more**: [Guidelines](applying-transitions.md#b82b5150-609b-4540-903b-2b900ef830aa)

Skeleton loaders are UI abstractions that hint at where content will appear once it's loaded. They're used in combination with other transitions to reduce perceived latency and stabilize layouts as content loads.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxs24v-GM3-Transitions-TransitionPatterns-8-1-v01.mp4?alt=media&amp;token=2caf8946-4b3f-4f1b-98a2-baad902a2430" title="Music app is selected, producing a skeleton soon populated by individual music items in each corresponding outline."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxs24v-GM3-Transitions-TransitionPatterns-8-1-v01.mp4?alt=media&token=2caf8946-4b3f-4f1b-98a2-baad902a2430>)

Skeleton loaders have a subtle pulsing animation to indicate indeterminate progress. It starts at the top left of the screen and moves down to the bottom right.

Once content is loaded, it quickly fades in on top of the skeleton loader.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxtkjx-GM3-Transitions-TransitionPatterns-8-2-v01.mp4?alt=media&amp;token=199f90b4-51da-4019-a29d-06158f78f198" title="&lt;p&gt;A pulsing animation indicates indeterminant loading&lt;/p&gt;"></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxtkjx-GM3-Transitions-TransitionPatterns-8-2-v01.mp4?alt=media&token=199f90b4-51da-4019-a29d-06158f78f198>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxuime-GM3-Transitions-TransitionPatterns-8-3-v01.mp4?alt=media&amp;token=9909e287-adde-4135-88a4-ce1bfeb9f646" title="&lt;p&gt;Content quickly fades in once it's loaded&lt;/p&gt;"></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygxuime-GM3-Transitions-TransitionPatterns-8-3-v01.mp4?alt=media&token=9909e287-adde-4135-88a4-ce1bfeb9f646>)
