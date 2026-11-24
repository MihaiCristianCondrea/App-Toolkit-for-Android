---
title: "Spacing"
source_url: "https://m3.material.io/styles/spacing/overview"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/a612533a0c03-mp45pjnu-01.png"
  - "assets/images/dcafbd32258d-mp46hu30-02.png"
  - "assets/images/4a66287c14c8-mp46iygv-03.png"
  - "assets/images/7168e6faf383-mp46qg1t-04.png"
  - "assets/images/88c4ec0277e4-mp46u5o7-05.png"
  - "assets/images/15e81f87ffd6-mp46ujx2-06.png"
  - "assets/images/ae23e3e03ec7-mp46vt0p-07.png"
  - "assets/images/328f908cde5b-mp46wxwi-08.png"
  - "assets/images/58407636d9eb-mp46ywq1-09.png"
  - "assets/images/c111f090449c-mp46yjrr-10.png"
  - "assets/images/3d78ce7cc136-mp46z9b3-11.png"
---

# Spacing

Spacing is the distance around and between component and layout elements

## Overview

-   Apply spacing tokens to the margins, padding, and gaps of a component, UI element, or layout

-   Adapt spacing to different values based on context, like mobile vs desktop, or density settings

-   The spacing system is measured on an 8dp scale, where **space100 = 8dp**

-   Spacing is applied to the flow of elements (horizontal, vertical), or in relation to the elements (leading, trailing, top, bottom, gap)

-   [More details on grid & spacing design principles](../../foundations/layout/grids-spacing/density.md)

![Bottom padding of small and large buttons is space200 and space400. Leading padding is space300 and space600.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp45pjnu-01.png?alt=media&token=4bc20db7-589b-4e9f-8a41-6ef0912181bf>)

_Spacing should adapt to component size, layout, form factor, and other contexts_

## Availability & resources

| Type | Resource | Status |
| --- | --- | --- |
| Implementation | Android Views (MDC-Android) | Unavailable |
| Jetpack Compose | Available |  |
| Web | Unavailable |  |

## Spacing units

Spacing units follow an 8dp scale. Rather than defining every value, Material only defines the most recommended spacing unit values on the scale. The system can be extended to add more spacing units and patterns as needed. [More on spacing tokens](https://m3.material.io/m3/pages/spacing/tokens/)

![A spacing scale shows 2, 4, 6, and 8 at the bottom range and 48, 56, 64, and 72 at the top of the range.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46hu30-02.png?alt=media&token=8c945825-e613-4d44-8b78-754d4f368664>)

_Spacing units are are created as a multiplier from the baseline unit of 8dp, which is space100_

### Component layouts

In **components**, spacing units define the padding and gaps between individual elements of a component, such as text, icons, and controls.

![Padding and gaps applied to a mobile layout and 2 components.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46iygv-03.png?alt=media&token=1ffbf4cf-d3d8-492a-bf87-543363d09c2a>)

_Spacing concepts for components: Vertical padding (top & bottom); Vertical gap; Horizontal padding (leading & trailing); Horizontal gap_

### Page layouts

In **layouts**, spacing units standardize the overall composition of the page, like where text, UI elements, and components go.

Layouts use:

-   Panes, spacers, and margins to structure the page

-   Padding and gaps to organize content within the panes.

![Padding and gaps applied to a mobile layout and 2 components.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46qg1t-04.png?alt=media&token=de6ad9a3-9fd4-419c-afb9-845b48380977>)

_Spacing concepts for layouts: Margin; Top padding; Horizontal padding (leading & trailing); Spacer (gap); Vertical gap_

## Parts of spacing

Spacing has three categories: **padding**, **gaps**, and **margins**.

-   Padding: Space inside an element

-   Gap: Space between elements in a grid or container

-   Margins: Space outside an element


The position of the spacing can be **vertical**, **top**, **bottom**, **horizontal**, **leading**, or **trailing**.

-   **Leading** and **trailing** edges swap sides in right-to-left (RTL) languages. [More on RTL layout design](../../foundations/layout/bidirectionality-rtl.md)


For example, the [search](../../components/search/specs.md) container has:

-   8dp vertical padding

-   8dp horizontal gaps

-   24dp horizontal margins (12dp when focused)

![Default search spacing.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46u5o7-05.png?alt=media&token=ec9f8771-d0f9-40dc-bda9-5de5123f3961>)

_Search horizontal margins are 24dp by default to ensure accurate placement from the screen edge_

![Active search spacing.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46ujx2-06.png?alt=media&token=117fe589-68f0-4c73-b242-a6f6e0970b13>)

_Search margins change to 12dp when focused, while the container padding and horizontal gaps remain the same_

### Padding

Padding is the spacing inside an element. It adds a buffer from the container edge to the content, like text and icons.

![Examples of each possible padding value.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46vt0p-07.png?alt=media&token=052c258f-0747-4f64-9379-70eaf64e6389>)

_Padding can be horizontal or vertical, or on a specific edge: leading, trailing, top, or bottom_

### Gaps

Gaps are the spaces between elements in a grid or container. Horizontal gaps are between side-by-side elements, and vertical gaps are between stacked elements.

Use a **horizontal gap** and **vertical gap** for simple components where gaps are always the same size.

Complex components with many different gaps should define them by the elements on each side, like an **icon-label gap.**

![Examples of each possible gap value.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46wxwi-08.png?alt=media&token=2aa763aa-37db-4c60-8ebe-ed2c068891ac>)

_Gaps can be horizontal, vertical, or defined by the elements around it_

### Margins

Margins are the spacing outside an element. They add a buffer between the element and the parent container or screen edge.

**Use padding & gaps before using margins:**

-   Material rarely uses margins in components; padding and gaps tend to apply spacing in a more uniform way

-   Only use margins to apply further spacing beyond the parent container’s padding, or in layouts

![Examples of each possible margin value.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46ywq1-09.png?alt=media&token=c7d6c7ca-77f2-4f73-b0bb-1f47c60f09c1>)

_Margins can be horizontal or vertical, or on a specific edge: leading, trailing, top, or bottom_

![Button with uniform container horizontal padding.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46yjrr-10.png?alt=media&token=7a66f957-b8d4-491d-939a-1bac71e540c7>)

_Define padding and gaps on the parent container to organize all elements inside_

![Button icon with different leading and trailing margin values.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp46z9b3-11.png?alt=media&token=16464639-7f1c-4c21-a46b-7126cd037463>)

_Avoid defining margins on child elements as they usually aren’t uniform, and require more tokens_
