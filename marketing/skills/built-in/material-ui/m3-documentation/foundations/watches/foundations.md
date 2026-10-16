---
title: "Design for watches"
source_url: "https://m3.material.io/foundations/watches/foundations"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/73220fb87fb5-mp5bs5es-01.png"
  - "assets/images/2908a804a353-mp5bsxg5-02.png"
  - "assets/images/0c5f50f626e3-mp5btx3i-04.png"
  - "assets/images/458ee610b28f-mp5bv6zg-05-do.png"
  - "assets/images/9ed4e70d27cc-mp5bvqq9-06-dont.png"
  - "assets/images/721d085c374c-mp5bx4u4-07.png"
  - "assets/images/45f8a6f00680-mp5bxqx4-08.png"
  - "assets/images/a67663a3bf59-mp5byzmd-09-do.png"
  - "assets/images/bf66694290b4-mp5bzq48-10.png"
---

# Design for watches

Watches have special design considerations and interaction patterns

## Foundations

## Resources

| Type | Resource |
| --- | --- |
| Design | [Get Started with M3 Expressive](../../get-started.md) |
| [UI Design for Wear OS](https://developer.android.com/design/ui/wear/guides/get-started/design-for-wearables) |  |
| [Figma Design Kits for Wear OS](https://developer.android.com/design/ui/wear/guides/get-started/design-kits) |  |
| Implementation | [Android Developers: Wear OS](https://developer.android.com/training/wearables) |
| [Jetpack Compose for Wear OS](https://developer.android.com/training/wearables/compose?version=3) |  |

## Anatomy

### The watch face

Watch faces display the time, as well as  other information and can provide access to other functions through apps and tiles.

They can also include complications, self-contained details that can show contextual info like heart rate or progress.

Ongoing activities on the watch face show in-progress actions, like a stopwatch countdown or a workout timer.

![A watch face with hands showing analog time readout, and complications showing the date in a calendar, heart rate, water consumption, and weather.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bs5es-01.png?alt=media&token=16a06854-553f-487a-aec0-d820e0d8c2f9>)

_Complications are details on the watch face that can be customized for style or function_

![A watch face with an entry point for an ongoing exercise activity.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bsxg5-02.png?alt=media&token=68a6bd57-a193-406f-a0f8-b39a42037fcc>)

_1. Ongoing activities, like timers, media players, or workouts, can be accessed from the watch face_

### Physical buttons

Wearable devices can have a variety input surfaces, which include physical buttons and controls.

![3 watch faces with indications showing the locations of a rotating side button, system button, and multifuction button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5btx3i-04.png?alt=media&token=1bb3ba0b-f111-499b-b2be-b77a0e8670d8>)

_Rotating side buttons: Used for volume control, or to scroll through options or lists; System buttons: Dedicated to OS functions like powering on and off, and cannot be customized; Multifunction buttons: Used by apps for custom actions like starting and stopping a stopwatch_

## Design principles

-   Tailor layouts for different screen sizes with [adaptive design](layout.md#63f04f40-d4ad-4d7e-8284-b57749ed810a)

-   Design for short interactions to conserve battery

-   Focus on one or two tasks at a time rather than a full app experience

-   Test designs in situations that involve movement to make sure the design is usable at a glance

![Calendar screen with date, time of dentist appointment, and more button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bv6zg-05-do.png?alt=media&token=5bc9ca83-c365-4b47-b208-bbabd315e52c>)

_At-a-glance views allow people to quickly see calendar events_

![3 columns with times, dates, and appointment names cut off.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bvqq9-06-dont.png?alt=media&token=ca9cab09-8e8a-4791-82bf-f14424382473>)

_Don't create complex and detailed apps such as a calendar grid_

### Always relevant

Watches are always with people. Consider how to update app content based on context, such as time, place, and activity.

![A map search for a garden’s location on a watch and on a phone.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bx4u4-07.png?alt=media&token=d6966fe1-6c27-4be8-8a5c-fb8ece800472>)

_Navigation on a watch complements the experience on a phone_

### Works offline

Design for slow connections and offline use, such as exercising and commuting.

![2 watches: 1 with offline music downloads list. 1 with a dialog about no internet connection with dismiss and accept buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bxqx4-08.png?alt=media&token=0e90a528-4115-4305-a9d9-ce734679cda8>)

_The network state can be communicated through: An offline icon; A dialog_

## Interaction patterns

### Cross-device experiences

Watches are often dependent on connected phones for functionality or complex interactions. In some cases, a watch and a phone are used together to accomplish different parts of the same task.

Consider how experiences can be consistent and complement the strengths of each device.

[More on multidevice development for Android](https://developer.android.com/multi-device-development)

![A contact entry shown on a phone and a watch, with options to call or message.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5byzmd-09-do.png?alt=media&token=a4b07f69-52fa-4ea6-8b17-9d4d13f1fa9d>)

_Consider which actions are appropriate for each device_

### Always on displays

Watches can have always on displays, which allow ambient content to be shown when the watch isn’t in use.

This are especially helpful for ongoing experiences like a timer or a workout that should remain in view. Because they remain on the screen for long time periods, consider limiting the number of pixels that are illuminated.

[More on always-on apps and system ambient mode in Wear OS](https://developer.android.com/training/wearables/always-on)

![A watch face showing progress through a task and the current time.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmp5bzq48-10.png?alt=media&token=4389e3c2-c5c6-455b-9c62-55bdfb8b5060>)

_Limit the number of illuminated pixels for a display that's always on_
