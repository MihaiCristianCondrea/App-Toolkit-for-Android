---
title: "Introducing Material Design Guidance for Large Screens"
source_url: "https://m3.material.io/blog/material-design-for-large-screens"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/b7b98e56fc79-78oT8GROrcU1-CAKTdsCLUzhSnlS8l_YB33qv52wE1kBlqff0A77JYE8vStC1l__TG8hpqW7RniS-PIOL6fJfiEtEfv0OLpw.png"
  - "assets/images/48b6d6f2a8c4-hfTScd8mpxO4zJ-b0ovcUaRsSMVS_3S8lZ5gOMxLIlvP8x2eV6ubMQVG9Qx5gXpXEN1ny9DjVOV-hZP33h8iL6WE9M-g0kA6.png"
  - "assets/images/fcd6c45f0881-rz3-a6KQw7oFlBKmUGhdLK1R942lQWuSMDbjYSt2yd-YJu620jjhurfObDOu57HonuAzIeGuwuvgvP87L427yB5E3eK5CQJW.png"
---

# Introducing Material Design Guidance for Large Screens

Designing and building for more devices

![illustration of flowers in a small vase, framed by an illustration of more flowers in a large bowl](<https://lh3.googleusercontent.com/78oT8GROrcU1-CAKTdsCLUzhSnlS8l_YB33qv52wE1kBlqff0A77JYE8vStC1l__TG8hpqW7RniS-PIOL6fJfiEtEfv0OLpwKTmhYc94mSCzEuRApkMv>)

_Illustration by Simone Noronha_

Published: 2021-05-14T13:00:00

Material Design has always been built to be adaptable. With today’s updates, Material Design is expanding its adaptive capabilities to help prepare your apps for all form factors, from phones to tablets, desktops, and beyond. Updated guidance and component behaviors will help your app scale and adapt, while maintaining consistent layouts using components optimized for each device.

As a system capability, layout and component responsiveness are now baked into our Material guidelines. Our layout guidance, along with components and their design documentation, have all been updated to describe a method of creating interfaces that can adapt to changes in screen size and shape.

Keep reading to see exactly what’s new, and check our session from I/O 2021: [_5 things you can do to prepare your app for large screens_](https://www.youtube.com/watch?v=UNDZn9GKJGo).

![Side by side image of a large screen and small screen layout, with column grids overlaid](<https://lh3.googleusercontent.com/hfTScd8mpxO4zJ-b0ovcUaRsSMVS_3S8lZ5gOMxLIlvP8x2eV6ubMQVG9Qx5gXpXEN1ny9DjVOV-hZP33h8iL6WE9M-g0kA6ozrMYNaf7b47KEmVw2A>)

New layout guidance defines responsive grid composition and behavior.

## New layout guidance

**Understanding Layout**

The [_Understanding layout_](https://material.io/design/layout/understanding-layout.html) article has been updated with new sections on Layout Anatomy and Composition.

-   **Layout Anatomy** outlines the three primary containers that make up app layouts in Material: body, navigation, and app bars. These containers form a framework for thinking about interfaces that scale across screen types and sizes. This section of the article describes each container and its baseline constraints and behaviors. This section also gives a brief introduction to Material’s responsive layout grid as it relates to the body container.
-   **Composition** provides guidance on composing information and components within a layout. As an app scales to larger screens, there’s more space available to display content and actions, making careful composition crucial to helping users perceive the additional information. This section of the article provides some techniques for creating a readable information hierarchy so users can understand and use your app faster.
-   A new **Resources** section points to our Material Design Community page on Figma for the latest downloads, including sample layout grid frames.

**Responsive layout grid**

The [_Responsive layout grid_](https://material.io/design/layout/responsive-layout-grid.html) article has been updated with a streamlined breakpoint system and new guidance on grid behavior.

-   **Breakpoints** have been streamlined to cover devices from small to large, from phones to desktop screens and beyond. The updated table also provides guidance on body container sizes, margins, and column counts to provide specific references across device types and sizes.
-   **Layout anatomy** introduces the concept of layout regions described in depth in the Understanding Layout article, as they pertain to the responsive column grid.

**Component behavior**

The [_Component behavior_](https://material.io/design/layout/component-behavior.html) article includes new terminology to keep your layouts bidirectional, as well as a section describing new adaptive behaviors.

-   **Positioning Terminology** now includes definitions for “leading edge” and “trailing edge,” allowing for discussion of bidirectional layout configurations. For example, the “leading edge,” where the content of the layout begins, is on the left in LTR languages, while in an RTL language it would be on the right.
-   **Component width** has been replaced by a **Component adaptation** section, which outlines general scaling, orientation, and swapping behavior for components as your layout scales across devices and screen sizes. This section also includes a handy reference for swappable component types.
-   The **overlay** behaviors has been moved to the [_Understanding layout_](https://material.io/design/layout/understanding-layout.html) article, as a distinct layout response.
-   The **push** behavior has been removed, to focus on preserving the context of the body region as a navigation component enters the screen.

![Diagram of a button at minimum and maximum width](<https://lh3.googleusercontent.com/rz3-a6KQw7oFlBKmUGhdLK1R942lQWuSMDbjYSt2yd-YJu620jjhurfObDOu57HonuAzIeGuwuvgvP87L427yB5E3eK5CQJWY4EUMjwD7VtmDQ0E1Cs>)

Component updates define new scaling and adaptation behaviors for individual Material components.

## Component updates

Complementing updated layout guidance, components have been updated as well, providing more specific direction on how Material Design Components can be used in experiences that adapt across devices and screen sizes.

**Design guidance**

Individual component articles have been updated with a _scaling & adaptation_ section under **behavior**. This section details unique scaling considerations not covered in the [_Component behavior_](https://material.io/design/layout/component-behavior.html) article, and can answer questions about specific default adaptative behaviors. Components that have been updated include:

-   [App bars (top)](https://material.io/components/app-bars-top#behavior)
-   [Bottom navigation](https://material.io/components/bottom-navigation#behavior)
-   [Buttons](https://material.io/components/buttons#behavior)
-   [Cards](https://material.io/components/cards#behavior) (_Implicit_ and _explicit containment_ subsections have also been added under _anatomy._)
-   [Dialogs](https://material.io/components/dialogs#behavior)
-   [Image lists](https://material.io/components/image-lists#behavior)
-   [Lists](https://material.io/components/lists#behavior)
-   [Menus](https://material.io/components/menus#behavior)
-   [Navigation drawers](https://material.io/components/navigation-drawer)
-   [Bottom sheets](https://material.io/components/sheets-bottom#behavior)
-   [Side sheets](https://material.io/components/sheets-side#behavior)
-   [Snackbars](https://material.io/components/snackbars#behavior)
-   [Tabs](https://material.io/components/tabs#behavior)
-   [Text fields](https://material.io/components/text-fields)

**Code updates**

#### Navigation rail

The navigation rail is currently available on the following platforms:

-   [Android](https://github.com/material-components/material-components-android/releases/tag/1.4.0-beta01) (in the 1.4.0-beta release)
-   [Flutter](https://api.flutter.dev/flutter/material/NavigationRail-class.html)

For more component updates, check out [_What’s New in Foldables, Tablets, and Large Screens_](https://www.youtube.com/watch?v=Qkiz3QIPJzk) at I/O.

## Resources

The **Material Baseline Design Kit for Figma** has also been updated, adding a series of sample layout grids as components.

To see layout grids that account for newly revised breakpoint, margin, and column behaviors, grab a template for your desired screen size from the _Assets_ panel and toggle _Show Layout Grids_ from the _View_ menu in Figma. The _Layout regions_ property can be toggled in the _Design_ panel to reveal navigation regions and app bars.

Additionally, a bug affecting color mapping on the _Material Theme_ page has been resolved, and we have more updates planned to make full use of Figma's capabilities.

Grab the Design Kit from the [Material Design Figma Community Page](https://www.figma.com/@materialdesign).

## How to get started

Ready to become a pro at adapting to large screens and prepare your app to run on all types of devices? Check out our talk, [_5 things you can do to prepare your app for large screens_](https://www.youtube.com/watch?v=UNDZn9GKJGo), and the accompanying blog post from Google I/O 2021.

In the meantime, check out the resources linked below and reach out to us with questions any time with #AskMaterial [@MaterialDesign on Twitter](https://twitter.com/materialdesign).
