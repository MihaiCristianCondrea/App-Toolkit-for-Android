---
title: "Canonical layout examples"
source_url: "https://m3.material.io/foundations/layout/canonical-examples/overview"
lastmod: "2026-05-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/f141f3f670f7-mp72kkvb-LD-adapt-C.png"
  - "assets/images/4a548a86e56c-mp72yab6-Feed-adapt-B.png"
  - "assets/images/b04aef3c17b3-mp72ypkq-LD-adapt-B.png"
  - "assets/images/f2f4b13136e5-mp72z1no-SP-adapt-B.png"
  - "assets/images/3d73c38fa4fd-mp731djr-ACL-Adapt-B2.png"
---

# Canonical layout examples

Canonical layout examples are designs for common screen layouts across all breakpoints

## Overview

Canonical layout examples demonstrate how to implement the layout scaffold. They’re also available in code to provide a strong starting point for your product.

Each layout example considers common use cases and components to address expectations and user needs for how products adapt across breakpoints (previously window size classes).

![A messaging app on desktop mapping bar, rail, and pane regions.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp72kkvb-LD%20adapt%20C.png?alt=media&token=8df2d01b-2928-4746-b6cd-a56951fa32ec>)

_A layout scaffold can include bar, rail, and pane regions_

## Availability & resources

| Type | Resource | Status |
| --- | --- | --- |
| Design | [M3 Design Kit (Figma)](https://www.figma.com/community/file/1035203688168086460) | Available |
| Implementation | [Jetpack Compose: Canonical layouts](https://developer.android.com/develop/ui/compose/layouts/adaptive/canonical-layouts) | Available |
| [Android Views (MDC-Android): Canonical layouts](https://github.com/android/user-interface-samples/tree/main/CanonicalLayouts) | Available |  |

## Examples

There are three canonical layout examples: feed, list-detail, and supporting pane. Each example has configurations for compact, medium, and expanded breakpoints.

Use these canonical examples as a starting point to create layouts for a product.

### Feed

Use a feed layout to arrange elements like cards in a configurable grid for a quick, convenient view of a large amount of content.  

[More on feed layouts](https://m3.material.io/m3/pages/canonical-examples/feed/)

![A course list in a feed layout at a medium breakpoint. 8 cards in a single pane, with bar above and leading rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp72yab6-Feed%20adapt%20B.png?alt=media&token=cb5f6703-79cf-4d01-8518-c13612989d40>)

### List-detail

Use a list-detail layout to display explorable lists of items alongside each item’s details. This layout divides the window into two side-by-side panes.

[More on list-detail layouts](https://m3.material.io/m3/pages/canonical-examples/list-detail/)

![List-detail layout of a messaging app. Pane 1 lists all conversations. Pane 2 shows the selected message.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp72ypkq-LD%20adapt%20B.png?alt=media&token=4315342c-1992-4deb-9047-8e566410d1ed>)

__

### Supporting pane

Use a supporting pane layout to organize content into primary and secondary sections:

-   Primary display area: Contains the main content and occupies the majority of the window (typically about two-thirds)

-   Secondary display area: Presents supporting content in a panel that takes up the remainder of the space


[More on supporting pane layouts](https://m3.material.io/m3/pages/canonical-examples/supporting-pane/)

![Supporting pane layout. The primary pane shows course details. The  secondary pane lists “Lessons in this course”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp72z1no-SP%20adapt%20B.png?alt=media&token=4b51d4f8-3107-43e1-bd49-06b3f22d3428>)

## Advanced custom layouts

To create a custom layout, build on top of canonical layouts or layer scaffold elements.

### Layering

Use the [levitate](../scaffold/panes.md#96bf71b8-04b8-4fff-97c7-9bc782fbf401) adaptive strategy to create a layered layout. Layering panes above other content can create a focused, task-oriented experience such as:

-   Reviewing a shopping basket

-   Responding to comments

-   Creating a calendar event

![Custom layout. A shopping basket floats above a clothing product page.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp731djr-ACL%20Adapt%20B2.png?alt=media&token=b1292c0f-0457-49d9-b522-33ed5320fea2>)

_Layering panes helps people focus on a specific task_
