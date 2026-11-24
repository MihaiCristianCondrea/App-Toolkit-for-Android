---
title: "Unlocking component flexibility with slots in Figma"
source_url: "https://m3.material.io/blog/material-3-slot-components-figma"
lastmod: "2025-05-13"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/24588d9b0566-ZEXG-W7U-laQVPWKv9dDO63A3X4_53Wye7OjxT287BY0D66dpxl7bsT3FX5MxkjYUIgWfVjJkBSqy9w7XX67qvtbAF8sgx1Q.png"
  - "assets/images/5361be6e04fc-oq47vhxQrCvbMPNH9LpLs6DfR-J5wW1wL4u04Re-uv8kf2jMw0FR0uoICaQqPqsBX6hTvHDKrsn5wXrobE8Cv-lRYFh2t0ZE.png"
  - "assets/images/63874ea95c65-hMrc5mbn8t_fzhVKuSiyzM7md0Jz3Dzq5vs-1mB5iiCFWNTUK1kotqjlnoTSrLbs4bI6HDZPbUYr6QdmR3v2PZt8E0t_Sfe5.png"
  - "assets/images/f8aecfe38b2b-J3Hi9r0ELFo6DKy00RoNQ4R_yp7QJW8Q_92wpLC0Fh7j492ub16ZBZ0n-R_i6uwlmsm_TbMzJlmoCxsraHrA7hx_BRJy22ed.png"
  - "assets/images/26ed6e492af7-Bh8f6ZE_Ijpuar1XcbbFawIfvZNWybPMpMQZI12HPA_cMQLZcPA_Gh3NtiJETt-Ev8PR9F8830migCgJzx0L5Va6BMbaayYX.png"
---

# Unlocking component flexibility with slots in Figma

One less reason to “detach instance”

![abstract diagrammatic illustration](<https://lh3.googleusercontent.com/ZEXG-W7U-laQVPWKv9dDO63A3X4_53Wye7OjxT287BY0D66dpxl7bsT3FX5MxkjYUIgWfVjJkBSqy9w7XX67qvtbAF8sgx1QWzTSe2dieKq4sOSodJg>)

Published: 2024-11-04T13:00:00

Material Design is excited to introduce **Slot Components**, a powerful technique that allows you to create flexible and dynamic layouts with ease in the Material 3 Figma Design Kit.

Slot components unlock local custom component nesting in Figma. With this added flexibility to make changes and edits to components, slot components remove the need to disconnect from the main Material Design Kit. Components can now remain linked to the main library to receive updates and improvements as you change them to suit your needs.

Slot components are a step toward supporting component flexibility that makers need by:

-   Allowing teams to extend component possibilities while still inheriting future updates and fixes.
-   Designers no longer need to Detach components for unique variants.
-   Material components are no longer fixed to only preconfigured offerings. Designers can now add custom variations to them!
-   Material Design components are simpler and more powerful than ever.

Adding slots to components enables them to better express the maker’s needs. Slots gives designers the flexibility to create new component variations while still using the guidelines of Material’s design system.

This aligns with how these container-like components are designed with how they are built–– crafting the design toolkit to mimic the component capabilities in code.

![Material default card component beside slot enabled card](<https://lh3.googleusercontent.com/oq47vhxQrCvbMPNH9LpLs6DfR-J5wW1wL4u04Re--uv8kf2jMw0FR0uoICaQqPqsBX6hTvHDKrsn5wXrobE8Cv-lRYFh2t0ZE-X5FLVOZRb_huqXaIM>)

## How to use slots

Explore the Material Slot Component [Playground file in the Figma Community](https://www.figma.com/community/file/1424088109026491617/slots-playground).

Some container oriented components (i.e. cards) now support Slots.

Each slot enabled component will have a unique instance for activating the slot.

Select the Slot property to change to the Slot placeholder content and reveal options related to the slot

![Card component showing figma layout choices to enable slot](<https://lh3.googleusercontent.com/hMrc5mbn8t_fzhVKuSiyzM7md0Jz3Dzq5vs-1mB5iiCFWNTUK1kotqjlnoTSrLbs4bI6HDZPbUYr6QdmR3v2PZt8E0t_Sfe5ZGPJqY-ILhDomrd_jLs>)

Slot enabled components will provide a layout override

The slot can then be swapped for a local instance of another component.

![Slot component showing Figma instance swap functionality](<https://lh3.googleusercontent.com/J3Hi9r0ELFo6DKy00RoNQ4R_yp7QJW8Q_92wpLC0Fh7j492ub16ZBZ0n-R_i6uwlmsm_TbMzJlmoCxsraHrA7hx_BRJy22edBWAXoMeKOZnRBclpfw>)

Slot swap allows for quick replacement for local components

## Prior to slots

Prior to slots, the design kit offered a robust range of variations and possible component permutations for designs–– allowing different aspects to be manipulated or toggled on/off to ensure safe guide-rails and preserve component boundaries and design intent.

Behind the scenes this requires very complex library building to capture all possible iterations, exacerbated by newly introduced component features.

These complex components served as a great “jumping off point” for most users of the design kit. However for other use cases, subtle deviations (that would otherwise still adhere to guidelines) would require designers to detach from the master library and break these components to accomplish their goals. The result is designers lose their connection to the Material Design Kit library, component variations, state controls, and future library updates.

## Flexibility with slots

Slots serve as an empty component placeholder that designers can swap for any kind of custom content like Material or local custom components.

Components like Cards, Sheets, Lists, Menus and Toolbars are containers that house other elements within such as buttons and strings of text. Introducing Slots to container components  adds the flexibility for components to contain any kind of content rather than having the content fixed or baked into the component itself. This also brings closer alignment with how these components are built in code.

([Guide to components in Figma](https://help.figma.com/hc/en-us/articles/360038662654-Guide-to-components-in-Figma))

![Local component of grouped avatars replacing a leading slot position of a list item](<https://lh3.googleusercontent.com/Bh8f6ZE_Ijpuar1XcbbFawIfvZNWybPMpMQZI12HPA_cMQLZcPA_Gh3NtiJETt-Ev8PR9F8830migCgJzx0L5Va6BMbaayYX24g26v4gFh17Ehw9Qn8>)

The **slot component** technique is a powerful method that can help you create flexible and dynamic layouts with ease. As the Material library grows, more components will offer slot functionality. It's a great addition to [Material's Figma Design Kit](https://www.figma.com/community/file/1035203688168086460/material-3-design-kit) and we encourage you to try it out today!
