---
title: "Carousel"
source_url: "https://m3.material.io/components/carousel/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/086afdbd42f5-lwutxg2r-GM3-Components-Carousel-Accesibility-1-v01.mp4"
  - "assets/images/358047e3362a-lwut0q8v-2.png"
  - "assets/videos/d99f32007f36-lwutykt0-GM3-Components-Carousel-Accesibility-2-v01.mp4"
  - "assets/images/a08f5ca56ab5-lwut172a-4.png"
  - "assets/images/c5e200aaf9f2-lwykx1vc-5a-don't.png"
  - "assets/images/2e62ba3e4927-lwykxhsn-5b-don't.png"
  - "assets/videos/14cf2d4b9518-lwuu2jae-GM3-Components-Carousel-Accesibility-3-v01.mp4"
  - "assets/videos/1ca93722b36d-lwuu32xk-GM3-Components-Carousel-Accesibility-4-v02.mp4"
  - "assets/images/744e4a9710ae-lwut9phs-8.png"
  - "assets/images/7a3cdf48f6c0-lwuta5uo-9.png"
  - "assets/images/5a12b971faaa-lwutav24-10.png"
  - "assets/images/fa5afdd0ecf2-lwutbdy7-11.png"
  - "assets/videos/c8112e4cf58d-lwuu4mjv-GM3-Components-Carousel-Accesibility-5-v01.mp4"
  - "assets/videos/6a45471566db-lwuu5ddb-GM3-Components-Carousel-Accesibility-6-v01.mp4"
---

# Carousel

Carousels show a collection of items that can be scrolled on and off the screen

## Accessibility

## Use cases

Users should be able to do the following with assistive technology:

-   Navigate to the carousel container
-   Navigate between different carousel items
-   Activate a carousel item
-   Skip over the carousel items

## Requirements on scrolling pages

On vertically-scrolling pages, carousels require an accessible way to view all the items without horizontally scrolling. (This requirement doesn't apply to full-screen carousels.)

Material recommends adding a **Show all** button below the carousel, which opens a dedicated vertically-scrolling page of all carousel items.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwutxg2r-GM3-Components-Carousel-Accesibility-1-v01.mp4?alt=media&amp;token=481dab77-9f29-4618-b6a8-6e37eda180e4" title="Carousel on mobile has a &quot;show all&quot; button that opens a page with all carousel items."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwutxg2r-GM3-Components-Carousel-Accesibility-1-v01.mp4?alt=media&token=481dab77-9f29-4618-b6a8-6e37eda180e4>)

![Measurements of a "show all" button added below a carousel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwut0q8v-2.png?alt=media&token=fd99e7b1-4959-4151-a530-a820e6a115f6>)

_The Show all button should have a padding of 4dp_

If the carousel has a header, you can use an arrow icon button instead. Place the arrow icon directly next to the header or in the same row.

Make sure the header is also displayed on the page of all carousel items.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwutykt0-GM3-Components-Carousel-Accesibility-2-v01.mp4?alt=media&amp;token=f22a4f28-192d-4adf-ae7a-f8e705e94062" title="Carousels in a list on mobile with headers have an arrow icon that opens a page with all carousel items."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwutykt0-GM3-Components-Carousel-Accesibility-2-v01.mp4?alt=media&token=f22a4f28-192d-4adf-ae7a-f8e705e94062>)

![Measurements of an arrow icon button added next to a carousel header.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwut172a-4.png?alt=media&token=79318ca5-9f6e-4973-ba50-09469633894b>)

_Headers should align with the leading edge, and the arrow icon should have a size of 48dp_

Avoid customizing the accessibility solution when possible. However, if your product needs an alternative solution, consider adding a **Show all** button in nearby navigation, or add alternative control buttons close to the carousel.

Avoid adding UI elements, like arrows or other icons, within or beside the carousel.

![Arrow icons on the left and right of the carousel, reducing container width.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwykx1vc-5a-don't.png?alt=media&token=88d824af-cca5-4743-a7f4-5dc1776d152e>)

_Avoid adding buttons into the carousel container or beside it. Place any buttons above or below the carousel._

![Arrow icons floating on top of the carousel edge, concealing items.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwykxhsn-5b-don't.png?alt=media&token=7253d6f2-ad26-4cd6-854c-e5b47725c499>)

_Don't cover the carousel with buttons or other UI_

## Interaction & style

### Touch

Tapping on a carousel item changes the shape slightly, and creates a touch ripple for interaction feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu2jae-GM3-Components-Carousel-Accesibility-3-v01.mp4?alt=media&amp;token=2e678652-4107-4db0-bbde-ab3b09dc2b81" title="Carousel providing a ripple feedback when being tapped."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu2jae-GM3-Components-Carousel-Accesibility-3-v01.mp4?alt=media&token=2e678652-4107-4db0-bbde-ab3b09dc2b81>)

### Cursor

The hover state provides a visual cue that the carousel item is interactive.

When the carousel item is clicked (in both active and inactive states), a ripple appears for interaction feedback.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu32xk-GM3-Components-Carousel-Accesibility-4-v02.mp4?alt=media&amp;token=f5315212-a834-42a4-a3be-5afeb59d29ff" title="Carousel changing state when hovered."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu32xk-GM3-Components-Carousel-Accesibility-4-v02.mp4?alt=media&token=f5315212-a834-42a4-a3be-5afeb59d29ff>)

### Initial focus

When navigating to a carousel using assistive technology, use **Tab** to place initial focus on the first carousel item. Then, use **Tab** or the arrow keys to navigate the carousel items.

Use the up and down arrow keys to leave the carousel and focus on the next element on the page, like the **Show all** button.

![Diagram of how to navigate a carousel using assistive technology.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwut9phs-8.png?alt=media&token=b672b540-9326-4e15-9ce3-3696370d6a10>)

_Set initial focus on the first carousel item, and use arrows to navigate items_

![Diagram of how not to navigate a carousel using assistive technology.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuta5uo-9.png?alt=media&token=4e12e7df-cf8a-4535-9dea-182a8767f066>)

_Avoid focusing on the carousel container_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| **Tab** or **Arrows** | Moves to the previous or next carousel item |
| **Space** or **Enter** | Activates the focused carousel item |

## Labeling elements

The carousel container has the **container** role.

![Accessibility labels of a carousel.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwutav24-10.png?alt=media&token=cdceb9da-aa24-499f-a5ad-e9d8a028493f>)

_The carousel container is labelled appropriately and has the container role_

Each carousel may have a different number of items, so the label reads out the total amount of items and the current item in focus.

![Accessibility labels of a carousel item.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwutbdy7-11.png?alt=media&token=d28c067c-4591-44c8-93a0-ccb5adc4c497>)

_The carousel item label indicates the current item in focus and the total number of items_

## Reduced motion

When reduced motion settings are turned on, the parallax effect should be removed and carousel items should no longer expand as they come into view. All items are the same size.

Make sure carousels with reduced motion reach the edges of the window to avoid clipping visuals.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu4mjv-GM3-Components-Carousel-Accesibility-5-v01.mp4?alt=media&amp;token=6e5e6517-2ba7-40fd-98ba-db01729dfdf8" title="Comparison of a multi-browse carousel with the reduced motion setting off and on."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu4mjv-GM3-Components-Carousel-Accesibility-5-v01.mp4?alt=media&token=6e5e6517-2ba7-40fd-98ba-db01729dfdf8>)

For hero carousels with reduced motion, the small carousel item is only partially shown on screen.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu5ddb-GM3-Components-Carousel-Accesibility-6-v01.mp4?alt=media&amp;token=cd58d17b-d305-456b-8820-7b40a077577d" title="Comparison of a hero carousel with the reduced motion setting off and on."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwuu5ddb-GM3-Components-Carousel-Accesibility-6-v01.mp4?alt=media&token=cd58d17b-d305-456b-8820-7b40a077577d>)
