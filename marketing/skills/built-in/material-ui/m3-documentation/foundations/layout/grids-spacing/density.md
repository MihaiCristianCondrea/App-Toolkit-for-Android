---
title: "Grids &amp; spacing"
source_url: "https://m3.material.io/foundations/layout/grids-spacing/density"
lastmod: "2026-05-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/videos/8afc04a4ecf5-mp5c4fqq-m6fec25z-GM3-Components-Density-2-v03.mp4"
  - "assets/videos/18f87849bcf4-mp5c5sj5-m6fedlg1-GM3-Components-Density-1-v02.mp4"
  - "assets/videos/b04d4bed8621-mp5but2k-m6feg3jn-GM3-Components-Density-3-v02.mp4"
  - "assets/images/c75aa85965a6-mp5bjd4j-03.png"
  - "assets/images/8088a95ed0ef-mp5bh69d-04.png"
  - "assets/images/cc4e107e53d6-mp5bi6cs-05.png"
  - "assets/images/29a86cba929e-mp5b9chq-07.png"
  - "assets/images/68493922f64d-mp5b68d4-08.png"
  - "assets/images/ba0897910e99-mp5b7f5f-09.png"
  - "assets/images/7528bf2a13bd-mp5b2pqp-10-dont.png"
  - "assets/images/2462dfafc05a-mp5b47ji-11-dont.png"
  - "assets/videos/7f630350f999-m7fb8wiw-GM3_Expressive_Density_12a_IA_v03.mp4"
  - "assets/images/2c933cf869cd-mp58kiob-13-caution.png"
  - "assets/images/f2e83cd61354-mp58gdij-14.png"
  - "assets/images/4eaa3f116fd9-mp58i79t-15.png"
  - "assets/images/60e62e8ca1cf-mp586kzq-16.png"
  - "assets/images/bdb1d5ac9809-mp587qzn-17.png"
  - "assets/images/7294e26c094e-mp57upl7-18.png"
  - "assets/images/8b70ea996968-mp57w9fy-19.png"
---

# Grids &amp; spacing

Grids and spacing organize content and actions for any layout

## Density

-   Information density is the consideration of the amount of information visible on the screen

-   The default target size should be at least 48x48 CSS pixels

-   People can change density as long as the density controls are accessible

-   Apply density thoughtfully; not every layout needs it

-   Layout and component scaling (component adaptation or component density) can allow people to scan, view, or compare more information at once

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5c4fqq-m6fec25z-GM3-Components-Density-2-v03.mp4?alt=media&amp;token=a22edf15-85b4-48ea-8052-f29c533956fd" title="A website design with a denser arrangement of text and graphics."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5c4fqq-m6fec25z-GM3-Components-Density-2-v03.mp4?alt=media&token=a22edf15-85b4-48ea-8052-f29c533956fd>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5c5sj5-m6fedlg1-GM3-Components-Density-1-v02.mp4?alt=media&amp;token=6b087963-ddcc-40e9-83bb-7628df356d6b" title="5 components scaling with multiple size examples."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5c5sj5-m6fedlg1-GM3-Components-Density-1-v02.mp4?alt=media&token=6b087963-ddcc-40e9-83bb-7628df356d6b>)

**Information density**

-   Information density can be achieved through layout and design decisions without using component scaling

-   Some people may not benefit from increased density

**Component scaling**

-   Components can adapt and change dimensions to help people scan, view, or compare different amounts of information

-   Don't apply component scaling by default if it would result in a target below 48x48 CSS pixels

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5but2k-m6feg3jn-GM3-Components-Density-3-v02.mp4?alt=media&amp;token=ed3019bd-6a4e-428a-b4b3-9a4c2697b5c4" title="An email app with “Appearance settings” open to change information density between cozy, comfortable, and compact."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5but2k-m6feg3jn-GM3-Components-Density-3-v02.mp4?alt=media&token=ed3019bd-6a4e-428a-b4b3-9a4c2697b5c4>)

## Information density

Information density refers to the amount of content (such as text, images, or videos) in a given space.

A layout’s spacing dimensions, including margins, spacers, and padding, can change to increase or decrease its information density. High density layouts are useful when people need to scan, view, or compare a lot of information, such as in a data table. Increasing the layout density of lists, tables, and long forms makes more content available on-screen.

Consider density settings in the context of a device. Although a person may prefer a denser layout for desktop, they may not for mobile. Density shouldn’t automatically change across breakpoints or orientation unless a person changes it.

![2 layouts: 1 with low density and 1 with high density.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5bjd4j-03.png?alt=media&token=f2d98641-43b3-4be6-813e-b788a638eadf>)

_Consider using higher density information design when people need to scan lots of information_

![News website on desktop displaying a high information density. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5bh69d-04.png?alt=media&token=83e62cde-d2ca-451d-8b01-339cab7fa6af>)

_Consider the amount and priority of information on-screen. Higher density can be useful for data-rich products where people expect to scan lots of information quickly. Examples: News, financial portals, dashboards_

![News website on desktop displaying a low information density.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5bi6cs-05.png?alt=media&token=8dc0f800-b3aa-4927-bfc5-585d0f7a8513>)

_Lower density can be better for sites prioritizing aesthetics, a focused message, less information, or easier navigation_

## Component scaling

The component density scale controls the internal spacing of individual components.

The density scale is numbered, starting at 0 for a component’s default density. The scale moves to negative numbers (-1, -2, -3) as space decreases, creating higher density.

Higher density is typically applied by decreasing the top and bottom padding or overall height by 4dp.

![3 buttons with densities  of 0, -1, -2.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5b9chq-07.png?alt=media&token=38a1cee0-2071-46f7-9bf5-acf853ad4775>)

_Apply component density based on the needs and layout of a design_

Center the grouped element within the component container.

Text size shouldn’t change as the container size scales.

![Text field showing 20dp between label and input](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5b68d4-08.png?alt=media&token=8c5cc11c-001b-42bc-b0f5-dbf7714e503a>)

_The measurement between the label and input is 20dp_

![Parent container showing label above input.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5b7f5f-09.png?alt=media&token=f56f5d6f-18cd-4244-8ad2-219b353925a1>)

_The label and input are centered within their parent container_

![Dropdown menu with high density items and selectable space height of 38dp.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5b2pqp-10-dont.png?alt=media&token=bc17826a-5b44-4cc5-84e1-70357fe2e69a>)

_Don’t increase density in UIs that involve focused tasks, such as selecting from a menu. It reduces usability by limiting selectable space._

![Single-line snackbar with high density.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp5b47ji-11-dont.png?alt=media&token=e5339b42-6c49-4881-9d69-9b2886f21bf3>)

_Don't increase the density in components that alert a person of changes, such as snackbars or dialogs_

### Avoid applying component scaling by default

People should be able to **opt in** to dense layouts and components.

To ensure density settings can be easily reverted, settings interactions must use default target sizes (48x48 CSS pixels).  

Don't scale layouts below 48x48dp by default.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7fb8wiw-GM3_Expressive_Density_12a_IA_v03.mp4?alt=media&amp;token=58f22bac-a396-447c-aa5d-aa666e9c180b" title="A density menu with large, medium, and small options to customize the screen layout of a table on desktop."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7fb8wiw-GM3_Expressive_Density_12a_IA_v03.mp4?alt=media&token=58f22bac-a396-447c-aa5d-aa666e9c180b>)

## Targets

Dense components can be less accessible because interactive elements are smaller, so use caution when increasing information density.

![Selectable target of only 40dp.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp58kiob-13-caution.png?alt=media&token=f0e95ca4-7692-4b33-9fa8-79198007756b>)

_Use caution when applying component scaling where selectable targets will be reduced to less than 48x48dp. Only apply density where it provides a better experience._

Use caution when applying density to interaction targets. Accessible targets should retain a minimum of 48x48dp, even if the visual element, such as an icon, is smaller.

![Settings button icon is 24x24dp, but has interaction target of 48x48dp.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp58gdij-14.png?alt=media&token=9eb2d101-aabf-47e8-9e84-254391cfffce>)

_The target should remain 48x48dp, even if the icon is smaller_

![Button with height of 36dp and interaction target of 48dp.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp58i79t-15.png?alt=media&token=29992fdf-bc4e-46c2-afde-7d477b3bd803>)

_The interaction target of a common button can be larger, as long as it meets the 48x48dp minimum size_

## Pixel density

Pixel density is the number of pixels per inch. High-density screens have more pixels per inch than low-density ones. Elements with the same pixel dimensions appear larger on low-density screens and smaller on high-density screens.

To calculate pixel density:

Pixel density = Screen width (or height) in pixels / Screen width (or height) in inches

![Magnified UI element  showing a high number of pixels in the focus area.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp586kzq-16.png?alt=media&token=635b6806-b358-48bf-927b-670a1c848fde>)

_High-density elements have more pixels per inch_

![Magnified UI element  showing a low number of pixels in the focus area.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp587qzn-17.png?alt=media&token=e2b7ead4-9211-4000-9a76-9596875282b4>)

_Low-density elements have fewer pixels per inch_

### Density-independent pixels

Density-independent pixels, written as dp, are flexible units that scale to have uniform dimensions on any screen. They provide a flexible way to accommodate a design across devices. The Material design system uses density-independent pixels to display elements consistently on screens with different densities.

A dp is equal to one physical pixel on a screen with a density of 160.

To calculate dp:
dp = (width in pixels \* 160) / screen density

![Screen with grid representing a low number of pixels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp57upl7-18.png?alt=media&token=d73cdac2-1ff8-4a67-8808-f40ba6ffc395>)

_Low-density screen displayed with density independence_

![Screen with grid representing a high number of pixels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp57w9fy-19.png?alt=media&token=8a92b118-e18b-40fb-a4a5-4a740ce3ac69>)

_High-density screen displayed with density independence_

| Screen physical width | Screen density | Screen width in pixels | Screen width in dps |
| --- | --- | --- | --- |
| 1.5 in | 120 | 180 px | 240dp |
| 1.5 in | 160 | 240 px |  |
| 1.5 in | 240 | 360 px |  |
