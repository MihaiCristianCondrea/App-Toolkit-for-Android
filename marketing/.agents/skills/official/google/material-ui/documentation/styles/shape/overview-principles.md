---
title: "Shape"
source_url: "https://m3.material.io/styles/shape/overview-principles"
lastmod: "2026-05-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/3a2d77d7eb18-m0c35amt-1.png"
  - "assets/videos/0d2ffe24ee26-m8q813wo-01-shape-Overview-&-principles-2.mp4"
  - "assets/images/6e6621719e02-m0c3rf47-3.png"
  - "assets/images/3e5a2f906b31-m7pa01q8-4.png"
  - "assets/videos/a5f4768582a9-m0fc1aun-03-shape-Overview-&-principles-5.mp4"
  - "assets/images/2e88dee8d6cc-m0fc4dnp-6.png"
  - "assets/videos/ab423aff3632-m7p9sea1-GM3_Expressive_Shape_Overview-and-Principles_7_v17.mp4"
  - "assets/images/2d7cf36857be-m0c73wle-8.png"
  - "assets/videos/597e81f3fe53-m7pa308e-GM3_Expressive_Shape_Overview-and-Principles_9_v17-(1).mp4"
  - "assets/videos/aa643f9bfcc0-m7pa3b28-GM3_Expressive_Shape_Overview-and-Principles_10_4x_V03.mp4"
  - "assets/videos/8a09f29d0372-mnxek75a-GM3-Shape-Overview-12-v12.mp4"
---

# Shape

The M3 shape system includes original shapes, a corner radius scale, and built-in shape morphing

## Overview & principles

-   Use abstract shapes thoughtfully to add emphasis and decorative flair
-   Leverage Material shapes for built-in shape morphing
-   Rectangular shapes are fully rounded in all corners by default
-   Individual corners can be adjusted to create asymmetrical rectangular shapes

![Illustration of range of irregular shapes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c35amt-1.png?alt=media&token=ab563092-217d-4d71-986d-1b4d87b5ba3e>)

_Abstract shapes can help people express themselves_

## Availability & resources

| Type | Resource | Status |
| --- | --- | --- |
| Design | [Shape library](http://figma.com/community/file/1035203688168086460/material-3-design-kit) (Figma Design Kit) | Available |
| Implementation | [Jetpack Compose](https://developer.android.com/reference/kotlin/androidx/compose/material3/MaterialShapes) (Shape Library) | Available |
| [Android Views (MDC-Android)](https://github.com/material-components/material-components-android/blob/master/docs/theming/Shape.md) | Available |  |

## M3 Expressive update

**May 2025**

Added 35 **new shapes** and **shape morphing** to [Material Shape Library](https://www.figma.com/community/file/1035203688168086460) (Figma Design Kit) and [Jetpack Compose](https://developer.android.com/reference/kotlin/androidx/compose/material3/MaterialShapes).

Added new shape principles and a refreshed art direction.

Added corner radii tokens:

-   Large increased (20dp)
-   Extra large increased (32dp)
-   Extra extra large (48dp)
-   Updated fully rounded corners to use **full**. Previously, this was defined using 50% of the component size.

[More on M3 Expressive](../../blog/building-with-m3-expressive.md)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8q813wo-01%20-%20shape-Overview%20%26%20principles%202.mp4?alt=media&amp;token=f7376b02-0d4b-4f63-8cec-5b581fd75404" title="Animation of available Material shapes."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8q813wo-01%20-%20shape-Overview%20%26%20principles%202.mp4?alt=media&token=f7376b02-0d4b-4f63-8cec-5b581fd75404>)

## Shape library

![35 shapes in the shape set.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c3rf47-3.png?alt=media&token=1a6cef2c-83d4-4855-9b91-8121f8a8a0ae>)

_M3 has 35 shapes to easily apply to designs_

## Use shapes and text in harmony

Shapes are expressive elements of Material 3 that echo key visual attributes of [M3 typography](https://m3.material.io/m3/pages/typography/overview/). 

Use shape and type together for products to feel cohesive and polished.

![Fonts and mock UI screens showing a wide range of square, round, thin, and thick shapes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7pa01q8-4.png?alt=media&token=2bd24982-183f-4d94-8da0-d14252f165c4>)

_M3 shapes and Google Sans Flex share roundness visual attributes_

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fc1aun-03%20-%20shape-Overview%20%26%20principles-5.mp4?alt=media&amp;token=81c96d25-cf31-47ab-9c10-c0abdb56b26b" title="While a person taps water and squeezes a soft cube, a button in the middle responds similarly."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fc1aun-03%20-%20shape-Overview%20%26%20principles-5.mp4?alt=media&token=81c96d25-cf31-47ab-9c10-c0abdb56b26b>)

## Morph shapes to connect function and feeling

Shapes should morph to improve understanding and add moments of delight. Use shape morph to better communicate:

-   Interaction states, like when a button is selected
-   Actions in progress, like a friend typing, or a page loading
-   Changes in the environment, like sound, temperature, or time of day

Think about how shapes could react to different interactions, such as tapping, swiping, scrolling, releasing, and long pressing.

## Be bold and dare to embrace tension

Tension happens when the shape story changes unexpectedly, such as when contrasting shapes are used. This can be created using both square and rounded shapes, unconventional shapes, and other contrasting elements.

Material historically focused on rounded shapes. However, using sharp shapes, thereby adding tension, creates more dynamic design, one that’s more memorable and expressive.

This tension can be used in many ways, like conveying states, drawing attention to an element, or to improve the visual aesthetic.

![Round and square shape side-by-side.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0fc4dnp-6.png?alt=media&token=41b40132-0d83-4de4-9c1b-8b49902356e2>)

_Create tension by using a combination of round and square shapes_

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7p9sea1-GM3_Expressive_Shape_Overview%20and%20Principles_7_v17.mp4?alt=media&amp;token=d72dfebe-d9c9-4081-b6da-1338b9eefd93" title="Use different types of shape in loading indicators to show progress."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7p9sea1-GM3_Expressive_Shape_Overview%20and%20Principles_7_v17.mp4?alt=media&token=d72dfebe-d9c9-4081-b6da-1338b9eefd93>)

## Shape is versatile, not semantic

Avoid making shapes literal or assigning a specific function or meaning to a single shape.

For example, the loading indicator can be wavy, but the waveform is not a strict symbol of progression. Progress could just as easily be shown using rotating shapes or shape morph. 

Plus, waveforms could be used in other places unrelated to progress, like button containers.

## Use abstract shapes sparingly

Be intentional when using shapes in product UI. Don’t compromise clarity for the sake of visual design.

When incorporating diverse shapes, think about how they fit into the overall design and consider how they balance with the entire composition. Ensure that shapes resonate with the product's narrative. Consider the 'why' behind their inclusion and the value they contribute to the overall user experience.

![8 shapes with icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0c73wle-8.png?alt=media&token=f1bf9874-6d8e-43c4-ae44-c0d4912ca58b>)

_Shapes without clear meaning behind why they’re different can add more visual clutter than delight_

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7pa308e-GM3_Expressive_Shape_Overview%20and%20Principles_9_v17%20(1).mp4?alt=media&amp;token=e495a6b9-987a-4449-aa23-62cf4f8d5cd0" title="Shapes morphing to indicate different states."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7pa308e-GM3_Expressive_Shape_Overview%20and%20Principles_9_v17%20(1).mp4?alt=media&token=e495a6b9-987a-4449-aa23-62cf4f8d5cd0>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7pa3b28-GM3_Expressive_Shape_Overview%20and%20Principles_10_4x_V03.mp4?alt=media&amp;token=8a05935d-719e-491f-a6c9-5524055fe4d4" title="Shapes being applied as masks on photos to make them more interesting."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm7pa3b28-GM3_Expressive_Shape_Overview%20and%20Principles_10_4x_V03.mp4?alt=media&token=8a05935d-719e-491f-a6c9-5524055fe4d4>)

## Emphasize aesthetic moments with shape

Get creative when using shape in graphics, for photography cropping, personalized avatar masking, and other non-interactive elements.

Decorative moments offer the most flexible and creative uses of shape.

## Shape can be 2.5D

When effectively used, shape and motion can make 2D visuals feel 3D. They provide the illusion of depth and volume, making visuals more eye-catching and natural.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmnxek75a-GM3%20-%20Shape%20-%20Overview%2012%20-%20v12.mp4?alt=media&amp;token=195e7da0-07f2-42ab-9d90-5ee9f1562eb0" title="Shapes spinning and a weather icon transforming into the current temperature."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmnxek75a-GM3%20-%20Shape%20-%20Overview%2012%20-%20v12.mp4?alt=media&token=195e7da0-07f2-42ab-9d90-5ee9f1562eb0>)
