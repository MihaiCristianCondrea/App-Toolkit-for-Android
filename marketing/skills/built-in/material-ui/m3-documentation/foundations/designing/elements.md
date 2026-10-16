---
title: "Designing"
source_url: "https://m3.material.io/foundations/designing/elements"
lastmod: "2025-10-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/043ec3cd22fd-m5r5d64m-9-(1).png"
  - "assets/images/9fc0d63ed778-m5r5dvfi-10-(1).png"
  - "assets/images/dbbec9b8ced2-lwj47jj9-11.png"
  - "assets/images/4f577782a8fe-m5r5epn6-12.png"
  - "assets/images/e4fe1882584f-lwkb4332-13.png"
---

# Designing

Implement intuitive, accessible layouts, considering structure, color, and flow

## Elements

## Labeling elements

Elements can be defined and labeled to enhance understanding of their function and reduce confusion for those navigating with assistive technology. Add accessibility labels to define roles and indicate decorative elements.

### Visual elements that need labels

-   Interactive icons or buttons with no visible text or not enough context in the text (for example, an edit button with a pencil icon)
-   Interactive images
-   Visual cues (including progress bars and error handling)
-   Meaningful icons (such as status icons)
-   Meaningful images (for example, diagrams, substantive photos, and illustrations)

### Text elements need labels to add additional context

-   Generic links (for example, "Learn more")
-   Buttons with generic text (for example, "Save" when there are multiple such buttons on a page)

### Elements that do not need labels

-   Non-interactive UI text, as this will be automatically read by the screen reader
-   Buttons with sufficient text (for example, "Download image")

### Do not include the element name in labels

Do not use an element role (for example, button or menu) in your label. This identifier is automatically added when the element is assigned its proper role, typically by a developer.

### Label language style

This article uses the general term accessibility label to refer to several different types, including ARIA labels and alt tags. When accessibility labels are implemented in code, they'll be translated to the appropriate type for the intended platform. Additionally, the term **role** is used to cover both general component control types and ARIA roles for web apps. [Learn more about writing alt text](../content-design/alt-text.md)

### How to add labels

#### **1\. Label elements**

[Accessibility labels](../content-design/alt-text.md) assist users who cannot rely on a product's visual interface. Thoughtful labels help make the text-based experience as usable as the visual experience. Labels should concisely describe an element's content, purpose and behavior.

![Diagram showing the labels and roles assigned to on-screen icon buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm5r5d64m-9%20(1).png?alt=media&token=1dba85e2-6cc0-4d57-b63f-63de0da2c708>)

_Example: The accessibility labels for these icons describe their purpose—NOT what the icon looks like (for example, "magnifying glass")_

#### **2\. Add labels for meaningful images and interactive elements**

Add labels to visuals that convey meaning or enhance content. 

**Labels should be concise, descriptive, and convey the content and context of the image.**

This applies to infographics and other instructive images found in support docs.

![A microphone icon with the accessibility label "Voice Search."](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm5r5dvfi-10%20(1).png?alt=media&token=8b8ea120-72d0-4eec-9c95-24c19b76edc1>)

_The label “voice search” describes the user task (search) paired with the input method (voice)_

![A microphone icon with the accessibility label "Microphone."](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj47jj9-11.png?alt=media&token=3a943687-0a16-42c7-9c58-612febe88641>)

_Don't include the element type (button, menu, etc.) in your label. This will automatically be added by assigning the element the proper role._

#### **Hiding images**

Decorative icons and images that don't enhance the experience for a visually-impaired user should be annotated as decorative in order to hide them in code.

![Group of icons in a menu collectively described by the accessibility label N/A Hide Images.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm5r5epn6-12.png?alt=media&token=95690b3d-3cd2-4355-aaa6-10bee0a06677>)

_Mark decorative visual elements to "hide"_

#### **3\. Assign a role to interactive elements**

ARIA roles apply to web apps and specify how to increase the accessibility of web pages on top of HTML. 

-   For web, assign ARIA roles for all interactive elements 
-   For non-web, assign roles based on your design system components (button, slider, menu, etc.)

Assign ARIA roles (web) or component type (mobile) to communicate desired interaction patterns into engineering action. Note that some visual elements may look the same, but are intended to behave differently. 

Defining an interactive element's category by assigning it a role helps users of assistive technology establish expectations for how to interact with that element and anticipate what is likely to happen upon interaction.

![Element with the label "Got it button" and the role "Button."](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwkb4332-13.png?alt=media&token=1bc56ada-bfa2-4d87-9f79-9a97e1895d0f>)

_Don't include the control type in the label. Screen readers automatically add the control, so you’d be having it repeat (for example, “Got it button button”)._
