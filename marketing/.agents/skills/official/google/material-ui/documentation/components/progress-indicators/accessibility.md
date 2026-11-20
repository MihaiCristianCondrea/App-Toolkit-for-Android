---
title: "Progress indicators"
source_url: "https://m3.material.io/components/progress-indicators/accessibility"
lastmod: "2026-02-27"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/430185c65104-mlepc1r2-01.png"
  - "assets/images/2df9282dbc18-mlepcmau-02.png"
  - "assets/images/ffeccbde50d5-mlepd6dk-03.png"
  - "assets/images/12d92dff1935-mlepdozy-04.png"
  - "assets/images/d2f8cf9228d9-mlepe18t-05.png"
  - "assets/images/0961d4b40c0a-mlepereg-06.png"
  - "assets/images/4b212f9d9e96-mlepf1vr-07.png"
---

# Progress indicators

## Accessibility

## Use cases

People should be able to do the following using the assistive technology:

-   Navigate to the progress indicator
-   Understand what progress the indicator is communicating

## Interaction & style

The active indicator, which displays progress, provides visual contrast of at least 3:1 against most background colors.

![Dark line of progress indicator stands out against the lighter colored track.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepc1r2-01.png?alt=media&token=db30ced4-ea77-4efd-b2e1-369f42d4961c>)

_The progress indicator and stop indicator provide visual contrast of at least 3:1 against most background colors /_

When integrated into another component, such as a button, make sure that the active indicator provides visual contrast of at least 3:1 against the other component. 

For the active indicator, use the same color as the label text or icon. The track should be removed.

![Circular indicator on button passes 3 to 1 contrast test.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepcmau-02.png?alt=media&token=817a604f-513b-4d2b-bce8-576092f29233>)

_Ensure the indicator’s color provides at least 3:1 contrast against the surface it's on_

![Circular indicator on button fails 3 to 1 contrast test.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepd6dk-03.png?alt=media&token=28250ceb-a903-4145-9423-59a609cf5049>)

_Avoid using a color below 3:1 contrast_

For linear progress indicators, the stop indicator is required if the track has a contrast below 3:1 with its container or the surface behind the container.

Essentially, the end of the track must be easy to identify.

![Bright container holding the progress bar is on a dark surface, passing the 3:1 color contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepdozy-04.png?alt=media&token=5c4c02ed-76e6-41bb-9431-d215f1cec071>)

_Only remove the stop indicator when the linear progress indicator has at least a 3:1 color contrast with surrounding containers and surfaces_

![Bright container holding progress indicator is on a bright surface, failing the 3:1 color contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepe18t-05.png?alt=media&token=58f9d951-ddc2-47c4-9ab8-3707bfac503d>)

_Avoid removing the stop indicator if any adjacent containers or surfaces are below the 3:1 color contrast_

## Labeling elements

Since the progress indicator is a visual cue, it needs an accessibility label to describe the kind and amount of progress made.

Use the **progress bar** accessibility role, and write an accessibility label that describes the purpose of the progress indicator. The label should include the process, such as "loading,” and the affected content, such as a page, article, or episode. For example: "Loading news article" or "Refreshing page."

![Determinate linear progress indicator has an accessibility label of “loading news article” and role of “progressbar”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepereg-06.png?alt=media&token=71630831-8fa7-41a6-acb2-ae63c344ab9f>)

_Progress indicator labels should explain which items are loading_

![Indeterminate linear progress indicator has an accessibility label of “loading my episodes” and role of “progressbar.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepf1vr-07.png?alt=media&token=31415e5b-41fb-4ebe-a455-8ebbc9f47abd>)

_A label on an intedeterminate progress indicator on a screen which is loading a set of podcast episodes_
