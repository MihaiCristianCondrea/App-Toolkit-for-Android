---
title: "Buttons"
source_url: "https://m3.material.io/components/buttons/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/31a5e2cce7d3-mmm13hkn-01.png"
  - "assets/images/e55f05ede969-mmm14hok-02-caution.png"
  - "assets/videos/0775d3e911c6-mmm15x7t-GM3_Buttons_Accesibility-04-v02.mp4"
  - "assets/images/bb288e8e7f91-mmm17kt7-03.png"
---

# Buttons

Buttons prompt most actions in a UI

## Accessibility

## Use cases

People should be able to do the following with assistive technology: 

-   Use a button to perform an action
-   Navigate to and activate a button

## Interaction & style

### Color contrast

Enabled buttons need a 3:1 contrast ratio with the background to meet accessibility best practices. 

This is measured from the container for elevated, filled, and tonal button styles, and the label text for outlined and text button styles.

![Diagram of color contrast ratios for buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm13hkn-01.png?alt=media&token=430a76cd-fd79-4d94-9524-a728d101de7e>)

_Higher contrast helps differentiate elements_

### 200% text size

Avoid excessive text wrapping or truncation by choosing concise strings. 

On Android, button labels should be kept concise enough to fit within two lines after the text size is increased to 200%. If a button label exceeds this limit and gets truncated, provide an alternative way to access the full content in a single tap.

![200% text size on a mobile screen. The overly long button text wraps to a second line: “Download playlist for offline access”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm14hok-02-caution.png?alt=media&token=0bccb054-e80b-44a1-9602-edbfc79ccbe0>)

_Avoid excessive text wrapping or truncation by choosing concise strings_

### Rapid clicks

On the web, you can use a modified motion curve to avoid resonant effects from overlapping animations. This provides a smoother experience for interactions where you anticipate multiple clicks or taps in succession.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm15x7t-GM3_Buttons_Accesibility%2004%20-%20v02.mp4?alt=media&amp;token=3e6f0315-1423-46f7-9357-4506333a0264" title="A media player where the “next track” button is clicked rapidly, and is transformed with a smooth motion effect."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm15x7t-GM3_Buttons_Accesibility%2004%20-%20v02.mp4?alt=media&token=3e6f0315-1423-46f7-9357-4506333a0264>)

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab | Navigate to a button |
| Space or Enter | Activate a button |

## Labeling elements

The accessibility label for a button should match the visible label text on the button such as **Done**, **Send**, or **Reply**. 

It can contain extra contextual information if necessary.

![Accessibility tags for a text-only button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmmm17kt7-03.png?alt=media&token=90f85f9d-911a-4ad5-9a83-495e6e6445e3>)
