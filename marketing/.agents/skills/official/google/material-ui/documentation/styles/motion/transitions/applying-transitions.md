---
title: "Transitions"
source_url: "https://m3.material.io/styles/motion/transitions/applying-transitions"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/videos/a4b284133695-lygy2t4f-GM3-Styles-Motion-Transitions-ApplyingTransitions-1-v01.mp4"
  - "assets/videos/20ed616f71fb-lygy4536-GM3-Styles-Motion-Transitions-ApplyingTransitions-2-v01.mp4"
  - "assets/videos/ee3bb78be47f-lygy6hgi-GM3-Styles-Motion-Transitions-ApplyingTransitions-3-1-v01.mp4"
  - "assets/videos/61ebac6b459f-lygy75ou-GM3-Styles-Motion-Transitions-ApplyingTransitions-3-2-v01.mp4"
  - "assets/videos/319b68a07ff1-lygy7y9g-GM3-Styles-Motion-Transitions-ApplyingTransitions-4-1-v01.mp4"
  - "assets/videos/db50bbddbea2-lygy8zr2-GM3-Styles-Motion-Transitions-ApplyingTransitions-4-2-v01.mp4"
  - "assets/videos/59f2217c6f12-lygya5u9-GM3-Styles-Motion-Transitions-ApplyingTransitions-5-1-v01.mp4"
  - "assets/videos/f2933b1c2404-lygyav9v-GM3-Styles-Motion-Transitions-ApplyingTransitions-5-2-v01.mp4"
  - "assets/videos/06d27af8e1ae-lygybwyn-GM3-Styles-Motion-Transitions-ApplyingTransitions-6-1-v01.mp4"
  - "assets/videos/68b3b9ce1981-lygycnpj-GM3-Styles-Motion-Transitions-ApplyingTransitions-6-2-v01.mp4"
  - "assets/videos/f493744c0829-lygydts6-GM3-Styles-Motion-Transitions-ApplyingTransitions-7-1-v01.mp4"
  - "assets/videos/ee3948c7d7fb-lygyekln-GM3-Styles-Motion-Transitions-ApplyingTransitions-7-2-v01.mp4"
  - "assets/videos/2a40d23de7d1-lygyfxw6-GM3-Styles-Motion-Transitions-ApplyingTransitions-8-v01.mp4"
  - "assets/videos/012b5aba985b-lygyi588-GM3-Styles-Motion-Transitions-ApplyingTransitions-9-1-v01.mp4"
  - "assets/videos/6de6f73e71f6-lygyja80-GM3-Styles-Motion-Transitions-ApplyingTransitions-9-2-v01.mp4"
  - "assets/videos/45e909a5065a-lygykdgf-GM3-Styles-Motion-Transitions-ApplyingTransitions-10-1-v01.mp4"
  - "assets/videos/75c4ea822d5b-lygyl1ui-GM3-Styles-Motion-Transitions-ApplyingTransitions-10-2-v01.mp4"
  - "assets/videos/06a6ce402b06-lygyn2vj-GM3-Styles-Motion-Transitions-ApplyingTransitions-11-v01.mp4"
  - "assets/videos/1a73393f7360-lygyo6nk-GM3-Styles-Motion-Transitions-ApplyingTransitions-12-1-v01.mp4"
  - "assets/videos/57c39c2d311d-lygyoxdg-GM3-Styles-Motion-Transitions-ApplyingTransitions-12-2-v01.mp4"
  - "assets/videos/6770ea47e761-lygyq7yo-GM3-Styles-Motion-Transitions-ApplyingTransitions-13-1-v01.mp4"
  - "assets/videos/e173368cebce-lygyr0oo-GM3-Styles-Motion-Transitions-ApplyingTransitions-13-2-v01.mp4"
  - "assets/videos/a762f89dd21b-lygysi3q-GM3-Styles-Motion-Transitions-ApplyingTransitions-14-v01.mp4"
  - "assets/videos/e7be681348d9-lygytotc-GM3-Styles-Motion-Transitions-ApplyingTransitions-15-1-v01.mp4"
  - "assets/videos/5d668744e2bc-lygyuc8i-GM3-Styles-Motion-Transitions-ApplyingTransitions-15-2-v01.mp4"
  - "assets/videos/97f9a5b9af53-lygyvia4-GM3-Styles-Motion-Transitions-ApplyingTransitions-16-1-v01.mp4"
  - "assets/videos/6a3f825dc23c-lygywcfr-GM3-Styles-Motion-Transitions-ApplyingTransitions-16-2-v01.mp4"
---

# Transitions

Transitions help guide people as they navigate an app

## Applying transitions

star

Note:

M3 transitions use the legacy easing and duration system. They'll eventually be updated to use the motion physics system.

## What makes a good transition?

Well-designed transitions should have these characteristics:

### Follows accessibility settings

Most platforms have a reduced animation setting to help users with a sensitivity to motion. If that setting is on, transitions should:

-   Use subtle fades instead of intense sliding or scaling animations
-   Disable decorative effects like parallax or shape morphing

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy2t4f-GM3-Styles-Motion-Transitions-ApplyingTransitions-1-v01.mp4?alt=media&amp;token=fa2a937d-d78a-40e2-b173-1df231d43a25" title="Podcast is selected and exited quickly on the left, while a podcast is selected and exited more slowly on the right."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy2t4f-GM3-Styles-Motion-Transitions-ApplyingTransitions-1-v01.mp4?alt=media&token=fa2a937d-d78a-40e2-b173-1df231d43a25>)

### Consistent

Consistently applying the right type of transition helps make apps feel cohesive and predictable to use.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy4536-GM3-Styles-Motion-Transitions-ApplyingTransitions-2-v01.mp4?alt=media&amp;token=3e39ddab-c487-4fc7-a1b0-ed4d44607bea" title="Multiple apps move in tandem with the same motion effect, appearing in sync."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy4536-GM3-Styles-Motion-Transitions-ApplyingTransitions-2-v01.mp4?alt=media&token=3e39ddab-c487-4fc7-a1b0-ed4d44607bea>)

### Stable layouts

Use skeleton loaders so that UI elements are coherent and stable during a transition. Avoid content shifting positions or instantly popping in as it loads. It can be distracting and frustrating to use.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy6hgi-GM3-Styles-Motion-Transitions-ApplyingTransitions-3-1-v01.mp4?alt=media&amp;token=cc8be2fb-be03-4593-b660-228e3ec752ff" title="Podcast window opens with pulsing outline that frames content added slowly."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy6hgi-GM3-Styles-Motion-Transitions-ApplyingTransitions-3-1-v01.mp4?alt=media&token=cc8be2fb-be03-4593-b660-228e3ec752ff>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy75ou-GM3-Styles-Motion-Transitions-ApplyingTransitions-3-2-v01.mp4?alt=media&amp;token=63bb733f-6d3d-41a2-a555-07a17fdf7c3d" title="Podcast window opens with no outline, and content added into haphazard clusters."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy75ou-GM3-Styles-Motion-Transitions-ApplyingTransitions-3-2-v01.mp4?alt=media&token=63bb733f-6d3d-41a2-a555-07a17fdf7c3d>)

### No jarring jump cuts

Jump cuts should generally be avoided as a default setting since they can be disorienting. Instantly transitioning from one screen to the next offers no clues to help a user orient themselves.

If pure efficiency is a top priority, like opening a menu in a productivity app, a jump cut may be preferred.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy7y9g-GM3-Styles-Motion-Transitions-ApplyingTransitions-4-1-v01.mp4?alt=media&amp;token=0593dada-d203-46cb-94ff-e77013860d3f" title="Items in a music library are smoothly transitioned between."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy7y9g-GM3-Styles-Motion-Transitions-ApplyingTransitions-4-1-v01.mp4?alt=media&token=0593dada-d203-46cb-94ff-e77013860d3f>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy8zr2-GM3-Styles-Motion-Transitions-ApplyingTransitions-4-2-v01.mp4?alt=media&amp;token=28543c41-8af9-4581-a2fe-ea1df61fe367" title="Items in a library are selected with stark, quick transitions."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygy8zr2-GM3-Styles-Motion-Transitions-ApplyingTransitions-4-2-v01.mp4?alt=media&token=28543c41-8af9-4581-a2fe-ea1df61fe367>)

### Coherent spatial model

Transitions are used to establish a coherent spatial model. This helps users understand the physical layout of an app.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygya5u9-GM3-Styles-Motion-Transitions-ApplyingTransitions-5-1-v01.mp4?alt=media&amp;token=52f9a8d8-4bad-4a86-9d73-0129dbf5755d" title="An image within a horizontal carousel is also scrolled horizontally."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygya5u9-GM3-Styles-Motion-Transitions-ApplyingTransitions-5-1-v01.mp4?alt=media&token=52f9a8d8-4bad-4a86-9d73-0129dbf5755d>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyav9v-GM3-Styles-Motion-Transitions-ApplyingTransitions-5-2-v01.mp4?alt=media&amp;token=70a694a8-87f0-4aac-92ca-e35839278d67" title="An image within a horizontal carousel is scrolled vertically."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyav9v-GM3-Styles-Motion-Transitions-ApplyingTransitions-5-2-v01.mp4?alt=media&token=70a694a8-87f0-4aac-92ca-e35839278d67>)

### Unified direction

A transition should have a unified direction of movement. Elements are grouped and move along a primary axis instead of moving in independent directions. Only important elements like hero images remain persistent throughout the transition. This helps guide a users focus.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygybwyn-GM3-Styles-Motion-Transitions-ApplyingTransitions-6-1-v01.mp4?alt=media&amp;token=8196d5ad-4a88-4115-9a49-85658394f5cf" title="Selected song expands in a cohesive upward direction, marked by two blue lines showing unified movement."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygybwyn-GM3-Styles-Motion-Transitions-ApplyingTransitions-6-1-v01.mp4?alt=media&token=8196d5ad-4a88-4115-9a49-85658394f5cf>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygycnpj-GM3-Styles-Motion-Transitions-ApplyingTransitions-6-2-v01.mp4?alt=media&amp;token=d80d50cd-5995-43d4-8bc8-cbd915fdfe4b" title="Selected song expands in a jarring manner with multiple elements moving in different directions, marked by multiple red lines."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygycnpj-GM3-Styles-Motion-Transitions-ApplyingTransitions-6-2-v01.mp4?alt=media&token=d80d50cd-5995-43d4-8bc8-cbd915fdfe4b>)

### Clean fades

Fully fade out content before fading new content in. This avoids the overlap of partially transparent elements resulting in distracting and messy frames.

If a cross fade needs to occur, keep it quick and hide it during the fastest part of the transition.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygydts6-GM3-Styles-Motion-Transitions-ApplyingTransitions-7-1-v01.mp4?alt=media&amp;token=78f2f378-2cf8-49b3-b4bb-5503c0fb3db5" title="Selected restaurant information fades out as it shrinks to reveal Google Map beneath.."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygydts6-GM3-Styles-Motion-Transitions-ApplyingTransitions-7-1-v01.mp4?alt=media&token=78f2f378-2cf8-49b3-b4bb-5503c0fb3db5>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyekln-GM3-Styles-Motion-Transitions-ApplyingTransitions-7-2-v01.mp4?alt=media&amp;token=433fc94f-c7f5-4ab1-80e4-b623db721de2" title="Restaurant information remains visible as it shrinks and reveals the map beneath."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyekln-GM3-Styles-Motion-Transitions-ApplyingTransitions-7-2-v01.mp4?alt=media&token=433fc94f-c7f5-4ab1-80e4-b623db721de2>)

Don't slowly fade components on top of other content as they enter or exit. This creates distracting cross faded frames. If a fade is needed, like with a Dialog entering in the middle of the screen, the fade should use a short duration to hide that part of the transition.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyfxw6-GM3-Styles-Motion-Transitions-ApplyingTransitions-8-v01.mp4?alt=media&amp;token=2376992f-18fd-4b9a-b261-20e362cc6721" title="Message window remains transparent with details visible as it shrinks causing text beneath to become obscured."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyfxw6-GM3-Styles-Motion-Transitions-ApplyingTransitions-8-v01.mp4?alt=media&token=2376992f-18fd-4b9a-b261-20e362cc6721>)

### Simple style

Transitions are not receptive to highly stylized motion. They're frequent, often occupy large portions of the screen, and are primarily meant to help users accomplish a task.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyi588-GM3-Styles-Motion-Transitions-ApplyingTransitions-9-1-v01.mp4?alt=media&amp;token=f130ce91-0fc7-4fdf-ac08-88bc74fd4e9a" title="Calendar event expands evenly and quickly comes to a rest."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyi588-GM3-Styles-Motion-Transitions-ApplyingTransitions-9-1-v01.mp4?alt=media&token=f130ce91-0fc7-4fdf-ac08-88bc74fd4e9a>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyja80-GM3-Styles-Motion-Transitions-ApplyingTransitions-9-2-v01.mp4?alt=media&amp;token=0a238ff8-344a-4732-a814-16d3aa28d0fa" title="Calendar event expands with window bouncing momentarily and bouncing again on collapse."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyja80-GM3-Styles-Motion-Transitions-ApplyingTransitions-9-2-v01.mp4?alt=media&token=0a238ff8-344a-4732-a814-16d3aa28d0fa>)

## Choosing a transition pattern

Consider the following to choose the right transition for a given use case:

### Container transform

This pattern is highly effective at creating a relationship between elements. It's also the most dramatic pattern in terms of style and should be reserved for the right context. Consider using it for:

1.  Hero moments that should be expressive
2.  Shallow hierarchies where you expand an element for more detail then collapse it
3.  Creating a seamless connection between elements

**Read the research for the benefits of container transform** **[here](https://material.io/blog/motion-research-container-transform).**

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygykdgf-GM3-Styles-Motion-Transitions-ApplyingTransitions-10-1-v01.mp4?alt=media&amp;token=f68eb6c5-da7a-4083-b92d-12c5de44078b" title="Image within a carousel is selected and expands to fill the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygykdgf-GM3-Styles-Motion-Transitions-ApplyingTransitions-10-1-v01.mp4?alt=media&token=f68eb6c5-da7a-4083-b92d-12c5de44078b>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyl1ui-GM3-Styles-Motion-Transitions-ApplyingTransitions-10-2-v01.mp4?alt=media&amp;token=dbf61b87-7dd4-4bb9-a2b5-0089aa24655e" title="Battery life menu item among many setting options is selected and expanded to fill the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyl1ui-GM3-Styles-Motion-Transitions-ApplyingTransitions-10-2-v01.mp4?alt=media&token=dbf61b87-7dd4-4bb9-a2b5-0089aa24655e>)

Use a container transform transition for hero moments rather than a forward and backward transition.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyn2vj-GM3-Styles-Motion-Transitions-ApplyingTransitions-11-v01.mp4?alt=media&amp;token=3dc00c81-255a-4949-aecc-9d7eea660815" title="A One-Year-Ago photo memory is selected and expanded to take over the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyn2vj-GM3-Styles-Motion-Transitions-ApplyingTransitions-11-v01.mp4?alt=media&token=3dc00c81-255a-4949-aecc-9d7eea660815>)

### Forward and backward

Both Android and iOS should use platform defaults for forward and backward navigation. It's easy to implement and stays current as platforms update. They have a simple motion style suitable for such a common transition.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyo6nk-GM3-Styles-Motion-Transitions-ApplyingTransitions-12-1-v01.mp4?alt=media&amp;token=22d57293-6a2e-483c-bded-47eba23c5d2b" title="Individual message is selected with simple motion, and moves back with simple motion."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyo6nk-GM3-Styles-Motion-Transitions-ApplyingTransitions-12-1-v01.mp4?alt=media&token=22d57293-6a2e-483c-bded-47eba23c5d2b>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyoxdg-GM3-Styles-Motion-Transitions-ApplyingTransitions-12-2-v01.mp4?alt=media&amp;token=45cdc5a3-f523-4333-8cf4-5d8d4cd36553" title="Individual message is selected, causing a more radical take-over of the screen."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyoxdg-GM3-Styles-Motion-Transitions-ApplyingTransitions-12-2-v01.mp4?alt=media&token=45cdc5a3-f523-4333-8cf4-5d8d4cd36553>)

### Lateral

Lateral transitions are used to browse peer content that's part of the same set, like navigating between tabs in a media library. By sliding content horizontally, it hints at being able to swipe the content area to navigate between peers.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyq7yo-GM3-Styles-Motion-Transitions-ApplyingTransitions-13-1-v01.mp4?alt=media&amp;token=c39877a5-0920-4662-8380-90a83c5f0643" title="Tabs in a music app shift horizontally without interruption when selected."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyq7yo-GM3-Styles-Motion-Transitions-ApplyingTransitions-13-1-v01.mp4?alt=media&token=c39877a5-0920-4662-8380-90a83c5f0643>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyr0oo-GM3-Styles-Motion-Transitions-ApplyingTransitions-13-2-v01.mp4?alt=media&amp;token=cc21a566-1e6d-4994-adbd-12cd0607d5ed" title="Tabs in a music app fade as they rotate horizontally."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyr0oo-GM3-Styles-Motion-Transitions-ApplyingTransitions-13-2-v01.mp4?alt=media&token=cc21a566-1e6d-4994-adbd-12cd0607d5ed>)

Don't use a Lateral transition for navigating hierarchical screens. Sliding content the full width of the screen is excessive for a high frequency transition. It also implies an equal peer relationship which isn't accurate to the hierarchy of the screens.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygysi3q-GM3-Styles-Motion-Transitions-ApplyingTransitions-14-v01.mp4?alt=media&amp;token=b0016c76-ff2f-4192-82cc-3291a864a9ec" title="Message selected within messaging app moves laterally."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygysi3q-GM3-Styles-Motion-Transitions-ApplyingTransitions-14-v01.mp4?alt=media&token=b0016c76-ff2f-4192-82cc-3291a864a9ec>)

### Top level

When tapping a navigation bar, rail or drawer, a quick fade is used to transition to a new destination. Top level destinations aren't necessarily related, so the motion intentionally does not create a connection between screens.

A lateral transition pattern is not recommended for this type of navigation. It implies you can swipe between top level destinations which conflicts with other components like carousels or swipe-able list items.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygytotc-GM3-Styles-Motion-Transitions-ApplyingTransitions-15-1-v01.mp4?alt=media&amp;token=949878aa-a1be-410e-a1fc-a3e275e7bc06" title="Navigation between home and music app occurs with a quick fade, suggesting that items in each are distinct."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygytotc-GM3-Styles-Motion-Transitions-ApplyingTransitions-15-1-v01.mp4?alt=media&token=949878aa-a1be-410e-a1fc-a3e275e7bc06>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyuc8i-GM3-Styles-Motion-Transitions-ApplyingTransitions-15-2-v01.mp4?alt=media&amp;token=2490f24e-1064-4e3e-b45c-6ec8ebbfe4be" title="Navigation between home a music app occurs in a seamless lateral scroll, falsely suggesting that items in each are connected."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyuc8i-GM3-Styles-Motion-Transitions-ApplyingTransitions-15-2-v01.mp4?alt=media&token=2490f24e-1064-4e3e-b45c-6ec8ebbfe4be>)

### Enter and Exit

This transition pattern is used to introduce a component in context of the screen’s main UI. It can be modal, like a dialog requiring a user to take action. Or it can allow for simultaneously using both regions of the UI, like a standard bottom sheet over a map.

Don't use this pattern for navigating hierarchical screens. Sliding content the full height of the screen is excessive and it creates an unclear relationship between screens.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyvia4-GM3-Styles-Motion-Transitions-ApplyingTransitions-16-1-v01.mp4?alt=media&amp;token=b38ee2a8-163e-4e4a-92c8-32ceb19c1ae7" title="Animation showing a standard bottom sheet using the enter and exit transition over a map."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygyvia4-GM3-Styles-Motion-Transitions-ApplyingTransitions-16-1-v01.mp4?alt=media&token=b38ee2a8-163e-4e4a-92c8-32ceb19c1ae7>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygywcfr-GM3-Styles-Motion-Transitions-ApplyingTransitions-16-2-v01.mp4?alt=media&amp;token=418b8b05-eb02-4fcd-b251-9e38fa6ab2db" title="Animation showing a card expanding to a full screen on click and using the enter and exit transition. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flygywcfr-GM3-Styles-Motion-Transitions-ApplyingTransitions-16-2-v01.mp4?alt=media&token=418b8b05-eb02-4fcd-b251-9e38fa6ab2db>)
