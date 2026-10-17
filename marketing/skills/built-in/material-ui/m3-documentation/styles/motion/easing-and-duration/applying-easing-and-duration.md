---
title: "Easing and duration"
source_url: "https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/videos/37f9cdf23aea-lxo5kg57-GM3-Styles-Motion-EasingAndDuration-1-v01.mp4"
  - "assets/videos/15752dccf356-lxo5kzx8-GM3-Styles-Motion-EasingAndDuration-2-v01.mp4"
  - "assets/videos/625c025a4230-lxo5meuq-04-Emphasized-easing.mp4"
  - "assets/videos/c5220e123aa9-lxo5ncde-GM3-Styles-Motion-EasingAndDuration-3-2-v01.mp4"
  - "assets/videos/4ca48266f022-lxo5oi36-01-Start-and-End-on-screen_1.mp4"
  - "assets/videos/595857e88c7c-lxo5p261-02-Enter-and-Exit-the-screen.mp4"
  - "assets/videos/aeb5d54843de-lxo5po4q-03-Exit-Temporarily.mp4"
  - "assets/videos/e0b2c3c73206-lxo5s08u-05-Duration-Do-2.mp4"
  - "assets/videos/d958560080e6-lxo5r2t6-04-Duration-Dont-2.mp4"
  - "assets/videos/a4c5ad5b9d67-lxo7qkkx-06-Small-area-2.mp4"
  - "assets/videos/f2c1cd96feb8-lxo7qvzl-08-Large-area-2.mp4"
  - "assets/videos/e31a217c3369-lxo7rtzk-09-Enter-Exit.mp4"
  - "assets/videos/ad90a23942ea-lxo7sfc7-10-Enter-Exit.mp4"
---

# Easing and duration

Easing and duration create responsive and expressive motion

## Applying easing and duration

star

Note:

In the expressive update, components and motion now use the [motion physics system](https://m3.material.io/m3/pages/motion-overview/), which uses springs. Products should migrate to the new system. The easing and duration system is still used for transitions and can be used by teams that haven't yet updated to GM3 Expressive, but is no longer maintained.

## Suggested easing and duration pairs

Choosing the right combination of easing and duration can be complicated. As a simple starting point, these are sensible defaults that will work for most transitions.

| **Easing** | **Duration** | **Transition type** |
| --- | --- | --- |
| Emphasized | 500ms | Begin and end on screen |
| Emphasized decelerate | 400ms | Enter the screen |
| Emphasized accelerate | 200ms | Exit the screen |
| Standard | 300ms | Begin and end on screen |
| Standard decelerate | 250ms | Enter the screen |
| Standard accelerate | 200ms | Exit the screen |

## Easing

In the physical world, objects don’t start or stop instantaneously. Instead, they take time to speed up and slow down. Transitions without easing look stiff and mechanical, while a transition with easing appears more natural.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5kg57-GM3-Styles-Motion-EasingAndDuration-1-v01.mp4?alt=media&amp;token=b0bfb5c9-b5e7-4c77-bfbe-f996b6c65820" title="Motion curve with and without easing."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5kg57-GM3-Styles-Motion-EasingAndDuration-1-v01.mp4?alt=media&token=b0bfb5c9-b5e7-4c77-bfbe-f996b6c65820>)

Compared to the utilitarian style of M2, M3 easing is more expressive. Transitions have snappy take offs and very soft landings. 

Durations are slightly longer compared to M2. This gives transitions time to come to a gentle rest without feeling abrupt.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5kzx8-GM3-Styles-Motion-EasingAndDuration-2-v01.mp4?alt=media&amp;token=a99083ca-5afd-4a10-9220-11c65b1ec8e1" title="Comparison of M2 and M3 easing curves."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5kzx8-GM3-Styles-Motion-EasingAndDuration-2-v01.mp4?alt=media&token=a99083ca-5afd-4a10-9220-11c65b1ec8e1>)

### Choosing an easing set

The [**Emphasized easing set**](tokens-specs.md#cbea5c6e-7b0d-47a0-98c3-767080a38d95) is recommended for most transitions to capture the style of M3.

The [**Standard easing set**](tokens-specs.md#601d5552-a6e6-4d74-9886-ff8f24b9ec35) can be used for small utility focused transitions that need to be quick. The Standard set is also a fallback for platforms that don't support Emphasized easing, like iOS and Web.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5meuq-04%20-%20Emphasized%20easing.mp4?alt=media&amp;token=5b3caf5e-4108-49ab-ba89-9e7664fa9e7d" title="Expanding card in a note taking app."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5meuq-04%20-%20Emphasized%20easing.mp4?alt=media&token=5b3caf5e-4108-49ab-ba89-9e7664fa9e7d>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5ncde-GM3-Styles-Motion-EasingAndDuration-3-2-v01.mp4?alt=media&amp;token=2461a8d8-ff5e-4d4c-9ea1-7f3d4721cbcc" title="Text field selection in an email app."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5ncde-GM3-Styles-Motion-EasingAndDuration-3-2-v01.mp4?alt=media&token=2461a8d8-ff5e-4d4c-9ea1-7f3d4721cbcc>)

### Choosing an easing type

Easing types are chosen based on how a transition moves in relation to the screen.

#### Begin and end on screen

These transitions use **Emphasized** easing. It speeds up quickly and then comes to a gentle rest in order to emphasize the end of the transition.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5oi36-01%20-%20Start%20and%20End%20on%20screen_1.mp4?alt=media&amp;token=df0b5b40-1394-4585-9c34-2f7098056e15" title="Card expanding in a podcast app."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5oi36-01%20-%20Start%20and%20End%20on%20screen_1.mp4?alt=media&token=df0b5b40-1394-4585-9c34-2f7098056e15>)

#### Enter the screen

These transitions use **Emphasized decelerate** easing. It begins at peak velocity then comes to a gentle rest.


#### Exit the screen permanently

These transitions use **Emphasized accelerate** easing. It begins at rest and ends at peak velocity. By ending at peak velocity, it gives the impression the exiting component cannot be retrieved.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5p261-02%20-%20Enter%20and%20Exit%20the%20screen.mp4?alt=media&amp;token=cec5c45c-53d5-4b5a-8540-2a3d7f3d7cb3" title="Card rising from bottom of screen, then retreating quickly back to bottom of screen after being exited."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5p261-02%20-%20Enter%20and%20Exit%20the%20screen.mp4?alt=media&token=cec5c45c-53d5-4b5a-8540-2a3d7f3d7cb3>)

#### Exit the screen temporarily

These transitions use **Emphasized** easing. By ending at rest just off screen, it gives the impression the exiting component can be retrieved.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5po4q-03%20-%20Exit%20Temporarily.mp4?alt=media&amp;token=c43ef98e-fe31-4e2e-9241-45d618d77d39" title="Calendar menu is temporarily collapsed to the left."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5po4q-03%20-%20Exit%20Temporarily.mp4?alt=media&token=c43ef98e-fe31-4e2e-9241-45d618d77d39>)

## Duration

Transitions shouldn’t be jarringly fast or so slow that users feel as though they’re waiting. The right combination of duration and easing produces smooth and responsive transitions.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5s08u-05%20-%20Duration%20-%20Do%202.mp4?alt=media&amp;token=3a26c18a-b31d-400d-8f21-fc3131dcae4e" title="Clock icon is expanded to smoothly take over the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5s08u-05%20-%20Duration%20-%20Do%202.mp4?alt=media&token=3a26c18a-b31d-400d-8f21-fc3131dcae4e>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5r2t6-04%20-%20Duration%20-%20Dont%202.mp4?alt=media&amp;token=961208cd-0b72-4597-93da-e42b8d13fb90" title="Clock icon is expanded rapidly, in abrupt fashion, to take over the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo5r2t6-04%20-%20Duration%20-%20Dont%202.mp4?alt=media&token=961208cd-0b72-4597-93da-e42b8d13fb90>)

### Choosing a duration

Durations are chosen based on these criteria:

#### Transition size

Transitions that cover small areas of the screen have short durations. Those that traverse large areas have long durations. Scaling duration with the size of a transition area gives a consistent sense of speed.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7qkkx-06%20-%20Small%20area%202.mp4?alt=media&amp;token=25551a98-881c-43bd-a04a-b7cd84f4a10e" title="A series of radio buttons are selected on the Settings screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7qkkx-06%20-%20Small%20area%202.mp4?alt=media&token=25551a98-881c-43bd-a04a-b7cd84f4a10e>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7qvzl-08%20-%20Large%20area%202.mp4?alt=media&amp;token=4b40a6da-65ba-435c-9a87-57eac0eca02a" title="An album is selected that takes over the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7qvzl-08%20-%20Large%20area%202.mp4?alt=media&token=4b40a6da-65ba-435c-9a87-57eac0eca02a>)

#### Enter vs. exit transitions

Transitions that exit, dismiss, or collapse an element use shorter durations. Exit transitions are faster because they require less attention than the user’s next task.

Transitions that enter or remain persistent on the screen use longer durations. This helps users focus attention on what's new on screen.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7rtzk-09%20-%20Enter%20Exit.mp4?alt=media&amp;token=8ffff3e4-1ebb-47e8-999f-04c20b360f6a" title="Pop up screen on an email draft has option to delete or cancel."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7rtzk-09%20-%20Enter%20Exit.mp4?alt=media&token=8ffff3e4-1ebb-47e8-999f-04c20b360f6a>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7sfc7-10%20-%20Enter%20Exit.mp4?alt=media&amp;token=b1f8076f-4d05-4c67-886d-f3685b452c1e" title="Bottom sheet uses a longer animation duration to enter and a shorter duration to exit the screen.  "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flxo7sfc7-10%20-%20Enter%20Exit.mp4?alt=media&token=b1f8076f-4d05-4c67-886d-f3685b452c1e>)
