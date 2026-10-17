---
title: "Writing and text"
source_url: "https://m3.material.io/foundations/writing/text-truncation"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/e229e5cc3f66-lwj5ao0k-18.png"
  - "assets/images/ac20efad34f3-lwj5b36c-19.png"
  - "assets/images/3b873d197d14-lwj5bfye-20.png"
  - "assets/images/20a328394471-lwj5c5w5-21.png"
  - "assets/videos/f7c1f26f2a25-lwj5e7k2-GM3-Foundations-WritingAndText-Accessibility-1-v01.mp4"
  - "assets/images/713796caf353-m0qvmgvh-24.png"
---

# Writing and text

Ensure text is helpful, clear, and resilient to change

## Text truncation

## Text truncation

Information should always be available to readers, even if text is truncated or wrapped.

### Background

Increased size of text, increased spacing between text, and translation into longer languages shouldn’t result in losing content. This requires designing for text truncation and creating designs flexible enough to accommodate any viewport size or increase in zoom. Some common methods of designing for larger text include text wrapping, increased height or width of components, and truncation with ellipses and hover or link.

### Requirements

Content, understandability, and functionality must not be lost when users modify their type settings. There may be exceptions to these requirements for non-Latin alphabet languages.

### Text wrapping

-   “Wrapped” text extends from one line to another, increasing the height of the text container
-   Text should be wrapped when it’s critical, to ensure understandability, or when there’s space in the component

![Body copy  text wrapped  inside a dialogue with an option to expand content.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj5ao0k-18.png?alt=media&token=9823f564-5494-43f3-85ad-21af8dd5b024>)

_Wrap text, and if it still doesn’t fit, provide a way for users to see more_

![Body copy  text wrapped  inside a dialogue, cut off at the bottom, without an option to expand.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj5b36c-19.png?alt=media&token=f638592f-e9a7-446c-b7e3-84ef22c0d79c>)

_Don’t cut off text without providing a way for users to view it_

### Height and width of components

-   Some components can extend vertically or horizontally for more text

![Layout of UI- cards of different sizes organized in a stacked grid.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj5bfye-20.png?alt=media&token=fa20bfc7-0b26-4ed1-a256-8cb933dbacb4>)

_Use flexible component containers that change size to fit their content_

![A stretched UI card  with a small headline and a large photograph. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj5c5w5-21.png?alt=media&token=76cd69f0-f8db-4e21-8a38-221cd4d51984>)

_Avoid setting text size limits that don’t fit the space in a component. Use all space available._

### Ellipses with hover or link

-   Truncated text can be replaced with an ellipsis if the text is available through a tooltip or link
-   Links can be used when they’re contained in the text that’s truncated, and when the link displays what's been truncated
-   If there's an ellipsis, but no way to show the truncated text, it is not accessible
-   Note that this option can add difficulty for some people

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj5e7k2-GM3-Foundations-WritingAndText-Accessibility-1-v01.mp4?alt=media&amp;token=2fbeb491-6ae1-4c4c-930b-02914cdf93af" title="A calendar with a cursor hovering  over a day of the week displays a tooltip that reads “Tuesday.”"></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj5e7k2-GM3-Foundations-WritingAndText-Accessibility-1-v01.mp4?alt=media&token=2fbeb491-6ae1-4c4c-930b-02914cdf93af>)

![On a multiple selection list, the marked checkbox’s label is truncated by an ellipsis.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qvmgvh-24.png?alt=media&token=db765429-c069-476c-85ef-f831e9bc6bd7>)

_Don’t truncate content without providing users another way to see it_
