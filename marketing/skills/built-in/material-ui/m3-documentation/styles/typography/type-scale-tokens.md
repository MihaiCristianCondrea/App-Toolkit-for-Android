---
title: "Typography"
source_url: "https://m3.material.io/styles/typography/type-scale-tokens"
lastmod: "2026-05-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/038eba240c40-macb26zl-1.png"
  - "assets/images/3fd62be687d9-macdkln0-2.png"
  - "assets/images/843f529eeb87-mace0z70-3.png"
  - "assets/images/e392dff627ff-mace6o98-4.png"
  - "assets/images/e0c351787ba8-maceaanz-5.png"
  - "assets/images/e4b974708f82-maceu8zs-6.png"
  - "assets/images/81348988cb5d-mafu03rn-7.png"
  - "assets/images/eb220fc06d8f-mafu0asd-8.png"
  - "assets/images/450daeb45257-mafu0fju-9.png"
---

# Typography

Use typography to make content readable and beautiful

## Type scale & tokens

## Type scale

A **type scale** is a selection of type styles used across a product to ensure consistency.

### M3 type scale

Material 3 has one **type scale** containing two sets of **type styles: 15 baseline** and **15 emphasized**. Both of these style sets follow the same scale from Display Large to Label Small.

The **emphasized** styles were added in the expressive update. They have a higher weight and other minor adjustments compared to the baseline styles, and are best applied to bold, selection, and other areas of emphasis. Baseline and emphasized styles are meant to be used together.

![Diagram of the M3 type scale.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacb26zl-1.png?alt=media&token=85df7aac-915a-4d7b-81b2-3768e32d3cd1>)

_The scale is a range of contrasting styles that support the needs of various product contexts and content. No single product will use all the styles. Instead, select styles from the scale that are most appropriate._

## Type scale tokens

Each of the 30 styles has a single token that captures all the default properties. Tokens are separated into the **baseline** and **emphasized** sets. Each axis and property, such as font, line height, size, tracking, and weight, also has an individual token for greater customization. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### Baseline type style tokens

### TYPOGRAPHY

Component: Sliders

Configured context tags: Static, 3P, Android

## Emphasized type styles

The M3 type scale has 15 emphasized type styles. Use both baseline and emphasized type styles together to achieve expressive experiences. Material recommends using emphasized styles for selection, actions, headlines, and other [editorial treatments](editorial-treatments.md#19e5796e-9db8-4687-b20c-c6cee77e7df8).

### Emphasized type style tokens

### TYPOGRAPHY

Component: Sliders

Configured context tags: Static, 3P, Android

## Where emphasized styles can be used

### Components

When used in components, emphasized type styles can communicate hierarchy or importance, such as an active or selected component, or an unread message. The emphasized styles work well with:

-   Badges
-   Buttons (for primary actions)
-   Extended FAB
-   Selected list items
-   Selected menu items

Material components don’t use emphasized type styles by default. To use an emphasized type style, swap the baseline token for the emphasized token of the same style. For example:

-   Baseline: md.sys.typescale.display-large
-   Emphasized: md.sys.typescale.**emphasized**.display-large

### Weight

Use the emphasized styles on text that already uses weight (such as medium, bold) to communicate hierarchy.

### Context

Use emphasized styles to draw attention to specifics aspects of components, such as selected states, unread messages, or key interactions.

Emphasized context and weight can be used at the same time.

![Diagram showing emphasized styles being applied in and UI of an active call with live translation turned on.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmacdkln0-2.png?alt=media&token=5a6210f3-6f9b-4a9f-a696-dd9c5fb72d93>)

_Weight: Apply emphasized styles to text already bolded for an expressive style; Context: Apply emphasized styles to text in selective places to better communicate hierarchy or state_

## Customize the typeface

The M3 type scale has the option to set different typefaces at different sizes. 

-   The **brand** typeface is used for larger type styles, like Headline and Display, to focus on expression. 
-   The **plain** typeface is used for smaller type styles, like Body and Label, to focus on readability. 
-   Roboto is the default for both typefaces.

Consider replacing Roboto with different typefaces to boost brand expression in your product. On emphasized styles, this can help important text stand out even more.

![Diagram showing emphasized styles being applied in a UI of an active call with live translation turned on.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmace0z70-3.png?alt=media&token=33689c0e-cfd1-447c-bc91-4d0b4923186c>)

_Roboto can be replaced with another font, like Roboto Flex_

### Brand and plain typeface tokens

### TYPOGRAPHY

Component: Sliders

Configured context tags: Static, 3P, Android

## Customizing type styles

To customize existing type styles, follow these steps:

1.  If using a different typeface, change the **brand** and **plain** typeface tokens. 
2.  Adjust properties like line height and letter spacing to refine the appearance. Avoid changing the type size; this can affect how components render and reflow.
3.  Repeat for both baseline and emphasized type styles. Try to keep emphasized styles visually consistent, like all wider than baseline.

Heavier fonts may require wider letter spacing, while fonts with long ascenders and descenders will require different line heights. Axes can be further adjusted as necessary.

**Note:** Customizing the M3 type scale or individual styles may prevent you from receiving typography token updates from Material.

![Example type scales with custom typefaces and weights.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmace6o98-4.png?alt=media&token=6137e281-ba4e-433d-95df-73919fab36be>)

_Adjust variable axes, like weight and width, to customize fonts like Roboto Flex_

Different typefaces can be used for baseline and emphasized type styles.

![Example type scales with custom typefaces and weights.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaceaanz-5.png?alt=media&token=535cedd3-5009-4e2f-ba09-58491b257c94>)

_Custom typefaces can be used together, like Baskervville and Jacquard_

## Customizing your type scale

When different sizes from the defaults are needed, such as for different devices, you can customize the **type scale** by adding or removing styles, and even swapping out Roboto for a font of your choice.

Material Design uses the [Major Second](https://cieden.com/book/sub-atomic/typography/different-type-scale-types#:~:text=with%2520dense%2520content.-,Major%2520Second%2520\(1.125\),-The%2520Major%2520Second) type scale with 14 as its key base size. This anchors to the most essential style used most often for typesetting body text.

![Material Design’s default type scale; showing display, headline, and title sizes shown next to the Major Second type scale.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmaceu8zs-6.png?alt=media&token=db16dfbe-1515-4a50-b087-d94eefd86868>)

_The Material Design type scale uses the Major Second scale (1.125)_

Sizes on the rendered type scale should aim to provide impactful contrast between sizes by avoiding small differences.

![Material Design’s default type scale; showing display, headline, and title sizes shown next to the Major Second type scale ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmafu03rn-7.png?alt=media&token=5efa840c-9af9-4768-ae1e-96bf43bcda65>)

_Material’s default typescale of 15 styles allows distinction between each_

![ Display, headline, and title sizes shown next to the Major Second type scale.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmafu0asd-8.png?alt=media&token=15982f63-7760-4c29-a1cc-542da5bdde34>)

_Your product likely will not need all 15 default styles from the Material Design type scale. In this example, five sizes are chosen for a reduced set while the rest are removed._

![A size modification of Material Design’s default type scale where display medium is a larger size than the original scale.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmafu0fju-9.png?alt=media&token=3567a3d3-2a94-43b3-97dd-4bd9b04dbe85>)

_If the default sizes from the Material Design type scale do not meet your needs, values can be changed instead. Here the default size of display medium is adjusted to another size from the Major Second type scale._

### Font size units

The following units are used to express font size on Android and the web.

| Platform | Android | Web |
| --- | --- | --- |
| Font size unit | sp | rem |
| Conversion ratio | 1.0 | 0.0625 |

Web browsers calculate the REM (the root em size) based on the root element size. The default for modern web browsers is 16px, so the conversion is SP\_SIZE/16 = rem.

#### Example conversions

| Android | Web |
| --- | --- |
| 10sp | 0.625rem |
| 12sp | 0.75rem |
| 24sp | 1.5rem |
| 60sp | 3.75rem |

### Letter spacing units

The following units are for spacing letters in a UI.

| Platform | Android | Web |
| --- | --- | --- |
| Letter spacing unit | em | rem |
| Conversion ratio | (Tracking value in px / font size in sp) = letter spacing | (Tracking value in px / font size in sp) = letter spacing |

#### Letter spacing examples

| Android | Web |
| --- | --- |
| (.2 tracking / 16sp font size) = 0.0125 em | (.2 tracking / 16px font size) = 0.0125 rem |
