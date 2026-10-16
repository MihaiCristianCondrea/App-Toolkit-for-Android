---
title: "Shape"
source_url: "https://m3.material.io/styles/shape/corner-radius-scale"
lastmod: "2026-05-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/7f14dca9228a-m0c80f7i-1.png"
  - "assets/images/faba2a67c419-m0c881mj-1.png"
  - "assets/images/6ce86af8892a-m0fc5xxg-3.png"
  - "assets/images/27ea88511062-m0fc5nak-4.png"
  - "assets/images/0d71b34fe331-m0c8fgh9-2.png"
  - "assets/videos/f3e01c89a6f4-m0fbcwaf-08-Shape-Corner-Radius-Scale-6.mp4"
  - "assets/videos/57ca91e9abcd-m0fbdqeg-09-Diagram-Corner-Rounding.mp4"
  - "assets/videos/29b05ed857da-ma463q2b-10-shape-Corner-Radius-Scale-8.mp4"
  - "assets/images/17c3f7203627-m0c9cdvx-8.png"
  - "assets/images/b918f8ea8e4c-m0c9bsb5-9.png"
  - "assets/images/521853b9c21a-m0c9fged-6-1.png"
  - "assets/images/f4a3658c32cd-m0c9h8uv-6.png"
  - "assets/images/ccb0abf734b8-m0c9j8dt-7.png"
  - "assets/images/ad5b1d799b45-m0c9mtiu-Frame-2134280407.png"
---

# Shape

The M3 shape system includes original shapes, a corner radius scale, and built-in shape morphing

## Corner radius scale

Material components use a corner radius scale to define all rectangular shapes, such as buttons, carousels, and dialogs.

![Illustration of range of shapes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c80f7i-1.png?alt=media&token=2bf0cf29-448a-4c9e-a5cf-6b61020ff7ed>)

_M3 defines corner radii using a shape scale. This can be used to create both uniform and asymmetrical shapes._

## Shape tokens

Material has shape corner tokens to define all corners, and corner-value tokens for individual corners. [Learn more about design tokens](../../foundations/design-tokens/overview.md)

### TOKEN_TYPE_UNSPECIFIED

Component: Sliders

### Corner radius scale

The Material 3 shape system uses a size-based scale with ten styles. Styles are assigned to components based on the desired amount of roundedness. 

1.  None - 0dp
2.  Extra small - 4dp
3.  Small - 8dp
4.  Medium - 12dp
5.  Large - 16dp
6.  Large increased - 20dp
7.  Extra large - 28dp
8.  Extra large increased - 32dp
9.  Extra extra large - 48dp
10.  Full - fully rounded corners

[Apply shape styles using tokens](../../foundations/design-tokens/overview.md)

![10 corner radii styles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c881mj-1.png?alt=media&token=fcb5e003-c5d3-4208-af8a-8766b55ad7ee>)

_Steps on the scale are named for the amount of roundedness applied to the corner_

![Components illustrating the old 3-level shape scale.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fc5xxg-3.png?alt=media&token=874497dc-6354-4952-9e46-cc37c088b431>)

_M2: Three-level shape scale based on the size of the component container_

![Components illustrating the new 10-level shape scale.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fc5nak-4.png?alt=media&token=76b165c6-fa5b-448c-abd8-cc7aef1c6bb4>)

_M3: Ten-level shape scale based on the roundedness of shape corners_

## Symmetry

Components can have either symmetric or asymmetric corner shapes. Symmetric shapes have the same values for all corners, while asymmetric shapes can have corners with different values.

Both symmetric and asymmetric shapes use the same 10-step scale.

Asymmetrical shapes are used in M3 components with closely-grouped items, such as menus and split buttons. These are called **inner corners**.

![3 shapes illustrating symmetrical and asymmetrical styles.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c8fgh9-2.png?alt=media&token=e9d62c5c-3f8c-44b3-b487-bfe8c1cc0d6c>)

_Inner corner component tokens always map to individual corner shape tokens_

## Customizing shapes

Generally, products should consistently use the Material 3 shape styles. However, customization is sometimes necessary, and even encouraged, for hero moments or custom components. Shapes can be customized at the **style** or **component** level.

### Style changes

The corner radius shape style, like **medium**, can be customized to be a different size.

This applies the change to all components mapped to that shape style, unless they have an override.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fbcwaf-08%20-%20Shape%20-%20Corner%20Radius%20Scale%206.mp4?alt=media&amp;token=2a9a25cd-8429-42cb-8a0b-a741ce900430" title="Shapes with different corner radii."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fbcwaf-08%20-%20Shape%20-%20Corner%20Radius%20Scale%206.mp4?alt=media&token=2a9a25cd-8429-42cb-8a0b-a741ce900430>)

### Component changes 

The style of a specific component, such as a button, can be changed by customizing which corner radius shape style it maps to.

For example, by default, buttons are mapped to the **full** corner radius shape style. If your product needs a less rounded shape, remap the token to another style in the shape scale, such as **small** or **medium**.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fbdqeg-09%20-%20Diagram%20Corner%20Rounding.mp4?alt=media&amp;token=7767b025-5f33-4415-867b-af89fa105612" title="Components with different corner radii."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fbdqeg-09%20-%20Diagram%20Corner%20Rounding.mp4?alt=media&token=7767b025-5f33-4415-867b-af89fa105612>)

The shape style family can be customized from **rounded** to **cut**. This makes the corner a straight line instead of curved.  

Add extra padding to avoid cutting off content in information-dense components.   

For example, a large cut corner on a card will clip content and images in the area more than a rounded corner of the same size.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma463q2b-10%20-%20shape%20-%20Corner%20Radius%20Scale%208.mp4?alt=media&amp;token=982b9a51-c03d-4b4d-86fb-536b49701a44" title="Card with text and full corners."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fma463q2b-10%20-%20shape%20-%20Corner%20Radius%20Scale%208.mp4?alt=media&token=982b9a51-c03d-4b4d-86fb-536b49701a44>)

![Carousel with images with rounded corners.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c9cdvx-8.png?alt=media&token=563b34cc-3146-49fc-bb3f-0f10e7721697>)

_Shapes can be intentionally rounder to add more visual variety_

![Carousel with full rounded shapes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c9bsb5-9.png?alt=media&token=659af9fc-48eb-4dde-9246-1537ca7ca5fb>)

_Add unexpected moments by switching between square and fully rounded shapes_

### Adjust for optical roundness

When nesting rounded objects, avoid using the same corner radii for both objects. This can make the corners look unbalanced.

Instead, adjust the corner radii to be proportional to each other; this is called optical roundness. To calculate optical roundness:

-   Outer radius - padding = inner radius
-   For example: 48dp - 14dp = 34dp

![3 parts of corner radii to adjust.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c9fged-6-1.png?alt=media&token=e35e86ab-c7d6-4ade-bba4-9c151eb561f8>)

_Padding; Outer radius; Inner radius_

![Nested carousel with optical roundness.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c9h8uv-6.png?alt=media&token=896b2204-2886-49ac-82a3-bcd76cfce14e>)

_Use different corner radii values for nested components so they have optical roundness_

![Nested radii with the same roundness as its container.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c9j8dt-7.png?alt=media&token=56cb6075-ef00-4915-8792-6a95257a4d6d>)

_Avoid using the same corner radius value for nested objects_

### Using the shape library

The Material 3 shape library can be used to create more interesting containers. Use the shape library for mostly visual elements. Avoid applying unconventional shapes to text-heavy containers. 

Shapes should be used sparingly to provide a stronger emphasis and moments of delight.

![Unexpected shapes in carousel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c9mtiu-Frame%202134280407.png?alt=media&token=b4a4f136-7509-4d92-b9f7-4c1e11e1a27e>)

_Leverage the Material shape library for moments of delight_
