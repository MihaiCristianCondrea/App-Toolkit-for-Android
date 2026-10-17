---
title: "What’s new at Google I/O 2026"
source_url: "https://m3.material.io/blog/whats-new-at-io26"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/06f720059c08-G43IvlJGVU11dBAU71P9RY24Ps5l4Ea7GjzZz26tiDF25ayml6gkwjmXKQWryF5vcByAtg_2X3aorc3g5cw6bMUrD6rlwpAr.png"
  - "assets/images/688c0bfc3168-XOJm03fcmzIsqWMYWf_rlfSzprq9uFyEpf2LOIvIbM_4FYL3MPuytghRLE_OI1pWD6Jc1F4VC4vWGYqqr2twhP1t_cHInlr5.png"
  - "assets/images/ab84e260c926-5625MJiJGRDzEYbV8wr8J3OFvwYcLYUCtmUyjbPGSPqPBUZGMsW-3YkKe0yhGygDFgWGo7vPSMbsQl0m_ueFCHBLN9wdbVgD.png"
  - "assets/images/8e5e3522de3f-Dli63T2_nx84mxXr4IFvigXEBkPOpazLbiV6urNJ_OKrSSrqT9wRqp2r0qXzDufG9GvPKY10aRsCF1cIlAcAianusqjlP7Rb.png"
  - "assets/images/d9462ca1cf25-kPbmMI-p29M71VYjtdmpsuZJ8djcyYjIK2jdCbckSAos7Q-MQoI6LTDwH8neAoP1X8-kzP4u5I2aNoiET071RBvHSSP7wGGV.png"
  - "assets/images/a33fbe43c693-R1uK-1Czo386AyWkPz0k1a2QPcXPywhKqAO6hS3moQwablhBGaohR0PFMF1nCYet7ultJvbjPPk_oyBGCzHkbpmceSiYDvsN.png"
  - "assets/images/0c2d9bb7358d-psnY8cwGsozvdpKU_ShiJ4ikqGndGxar_uXiQal16WuaUFtckWTDswV0gRHGU1VhsjLxoBMISdKX5ytRDmyD41kv0mcq_oXk.png"
  - "assets/videos/827e8e136ff0-GM3_-_Search_Bar_-_Overview_02_-v2.mp4"
  - "assets/videos/7908cea7e9fb-00_Design-Hub_Hero_2X_16x9.mp4"
  - "assets/videos/9a64e2097a0f-1_IllustratingGemini-16x9.mp4"
---

# What’s new at Google I/O 2026

Material’s latest updates make it easier to create expressive, adaptive products

![Image](<https://lh3.googleusercontent.com/G43IvlJGVU11dBAU71P9RY24Ps5l4Ea7GjzZz26tiDF25ayml6gkwjmXKQWryF5vcByAtg_2X3aorc3g5cw6bMUrD6rlwpAr_MhtpI1QT69uA-SOt0Q>)

Published: 2026-05-19T08:00:00

At [Google I/O 2026](https://io.google/2026/), Material announced the Expressive layout system, lists and menus on Android, and the future of Material Android being Compose-first.

## Expressive layout: Design for all devices

The new layout scaffold helps designers adapt interfaces for mobile, desktop, spatial devices, and more. This update also includes a new spacing system incorporated into Material components, and new design guidance for watches and XR devices.

### Layout principles & guidelines

Check out the updated layout principles and guidelines:

-   [Layout overview](https://m3.material.io/foundations/layout/layout-overview)
-   [Scaffold](../foundations/layout/scaffold/overview.md)
-   [Grids & spacing](../foundations/layout/grids-spacing/overview.md)
-   [Breakpoints](../foundations/layout/breakpoints/overview.md)
-   [Bidirectionality & right-to-left (RTL)](../foundations/layout/bidirectionality-rtl.md)
-   [Canonical examples](https://m3.material.io/foundations/layout/canonical-examples)

![Compact and expanded layout scaffolds.](<https://lh3.googleusercontent.com/XOJm03fcmzIsqWMYWf_rlfSzprq9uFyEpf2LOIvIbM_4FYL3MPuytghRLE_OI1pWD6Jc1F4VC4vWGYqqr2twhP1t_cHInlr5-vxcCdrG5_mJ8eAkaK8>)

Use the expressive layout scaffold to adapt screens across breakpoints

### New spacing system

The spacing system is the foundation for adaptive design, built on an 8dp scale. It can be used to adapt components and layouts to contexts like device type or density settings.

[More on the spacing system](../styles/spacing/overview.md)

![A 2-pane layout showing padding, gap, and margin spacing.](<https://lh3.googleusercontent.com/5625MJiJGRDzEYbV8wr8J3OFvwYcLYUCtmUyjbPGSPqPBUZGMsW-3YkKe0yhGygDFgWGo7vPSMbsQl0m_ueFCHBLN9wdbVgDASGqNHyQ7aQANuk05Rw2>)

Apply spacing tokens to the margins, padding, and gaps of a component or layout so it can programmatically adapt to contexts like device type or density settings

### Design across devices

Follow these form factor principles to create apps that are optimized for [watches](../foundations/watches/overview.md) and [immersive XR](../foundations/xr/design/overview.md).

![A smartwatch music app using expressive motion.](<https://lh3.googleusercontent.com/Dli63T2_nx84mxXr4IFvigXEBkPOpazLbiV6urNJ_OKrSSrqT9wRqp2r0qXzDufG9GvPKY10aRsCF1cIlAcAianusqjlP7RbFG6nCC7oxtGBNX38CQ>)

<strong>Design for watches</strong> using a physics-based motion system, arc text styles, and edge-hugging containers

![An XR app with 2 spatial panels, 1 orbiter, and an immersive background.](<https://lh3.googleusercontent.com/kPbmMI-p29M71VYjtdmpsuZJ8djcyYjIK2jdCbckSAos7Q-MQoI6LTDwH8neAoP1X8-kzP4u5I2aNoiET071RBvHSSP7wGGVP2DdNKoWYKqxwJ_KNv4>)

<strong>Design for immersive XR</strong> to create immersive environments using spatial panels and depth-based elevation

## Expressive lists & menus

Expressive lists and menus got a lot of buzz at Google I/O. Check out the vibrant styles, flexible configurations, and see examples of them in action:

-   [Expressive lists](../components/lists/overview.md)
-   [Expressive menus](../components/menus/overview.md)

![Expressive lists and menus showcasing vibrant, expressive designs.](<https://lh3.googleusercontent.com/R1uK-1Czo386AyWkPz0k1a2QPcXPywhKqAO6hS3moQwablhBGaohR0PFMF1nCYet7ultJvbjPPk_oyBGCzHkbpmceSiYDvsNNpJg9xB0Pq4fApLHWw>)

## Material Android is Compose-first

Read the blog post about the upcoming stable Compose release, including 14 expressive components, Styles API integration, our plans for Android Views, and more.

[Read the Compose-first article](material-is-compose-first.md)

![Text: “Compose Material 3 is going stable”.](<https://lh3.googleusercontent.com/psnY8cwGsozvdpKU_ShiJ4ikqGndGxar_uXiQal16WuaUFtckWTDswV0gRHGU1VhsjLxoBMISdKX5ytRDmyD41kv0mcq_oXk1sWvXickvh1vYSEmdiJp>)

## More recent updates

### Expressive search & search app bar

Expressive search has a new visual style, motion, more flexibility for trailing icons, and is available for Jetpack Compose.

[Expressive search](../components/search/overview.md)

[Expressive search app bar](../components/app-bars/overview.md)

[Video: A recipe search with “Search recipes” hinted text, “Mexican dishes” is entered, then results appear in a list.](https://kstatic.googleusercontent.com/files/972ba37c1f857dab8dd18bf16ae8aee3a7dacb13b58142726f5c79c4768f2244f49f7bcfe294c3639ece02a0a2b632568eb5ea95382f4a5a5b02989fb7284cee)

## Learn about Google Design

Discover the people and stories behind [Google Design](https://design.google/).

### Designing for Transparent Screens

Behind the scenes of Jetpack Compose Glimmer, the new design system for display AI glasses.

[Read the story on Google Design](https://design.google/library/transparent-screens)

[Video: A message overlayed on a view of the Golden Gate Bridge, using AI glasses.](https://kstatic.googleusercontent.com/files/025520f0f4a5d3570295a2bb2d850aa4de825ea49875cbd71764def76ec430c01c99a818c9835b94a0f85f1c5584f1ec07a8c21d712a22d1b4eab3b62a3d4fba)

### Illustrating the Gemini App

How dynamic cues help people discover, learn, and master an AI assistant's evolving features

[Read the story on Google Design](https://design.google/library/gemini-ai-visual-design)

[Video: Animated visual cues for gestures, motion, iconography, UI, and color.](https://kstatic.googleusercontent.com/files/22ac66996edac9ad65cb9815da517171fa5308aad2bc014c3970ed872e4b511fb48853522bf4f031b9d236dd7edb13707bfa9a4235fc292a42777e1365a3df1b)
