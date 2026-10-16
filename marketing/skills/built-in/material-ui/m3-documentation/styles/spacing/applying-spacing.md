---
title: "Spacing"
source_url: "https://m3.material.io/styles/spacing/applying-spacing"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/ca6f6d354d27-mp472g1b-01.png"
  - "assets/images/462d65dccc9a-mp475gv4-02.png"
  - "assets/images/dd50411d6b43-mp475tmf-03.png"
  - "assets/images/c34b81555882-mp4783qn-04.png"
  - "assets/images/60bbb4600ac1-mp47e5lm-08.png"
  - "assets/images/414617edd287-mp47j8c0-09.png"
  - "assets/videos/4a85efa69fb7-mp47k75s-GM3-Spacing-Applying-01-IA-v01.mp4"
  - "assets/videos/783da11efacd-mp47kp67-GM3-Spacing-Applying-02-IA-v01.mp4"
---

# Spacing

Spacing is the distance around and between component and layout elements

## Applying spacing

Material’s spacing system is intentionally designed to be a simple linear scale. Unlike the color system, which adjusts light and dark theme logic across all components at once, tailored spacing logic is built within each component.

![Component and system token mapping for the leading padding of three different icon buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp472g1b-01.png?alt=media&token=faa1b24c-a165-4354-b169-fc162674a082>)

_Components have padding tokens that map to system tokens, which resolve to final values_

## What to use

### Pre-tokenized components

Some Material components map to spacing system tokens out of the box. This mapping can be customized by products to adapt to form factor or density.

Note: Work is ongoing to hook up all Material components to spacing tokens.

### System tokens

Spacing system tokens define the recommended values. Apply these to your product’s custom components and layouts, replacing any hardcoded values.

If the right system token doesn’t exist, [customize the system](applying-spacing.md#f1a6df59-f03d-4949-b1e6-d2dd4422c730) and add your own.

![Button with spacing tokens for all padding and gaps.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp475gv4-02.png?alt=media&token=d9282ad6-6edc-49c7-898d-1ee6447ccdb3>)

_Many Material components map to spacing system tokens_

![A list of spacing system tokens from 100 to 400.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp475tmf-03.png?alt=media&token=267b98d3-faa5-422e-82b5-611308686964>)

_System tokens define all spacing values recommended by Material_

### Spacing complements text scaling

When text is scaled up to 200%, the same spacing should be preserved by default.

![A button with text scaled to 200% uses the same spacing tokens as an unscaled button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp4783qn-04.png?alt=media&token=430e5fcc-38c8-4834-ad99-b2f9cd77b880>)

_Keep the same spacing when text scales_

## Customizing the system

The spacing system is meant to capture Material’s design intent, but customization is expected and often necessary. How you customize the system depends on your needs:

### Customize Material’s existing component spacing

Use this approach to customize how the base component appears across the entire product.

-   For example, change the “button top padding” mapping from **space125** to **space200** for a taller default button.

![A button’s vertical padding is changed from space125 to space200.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp47e5lm-08.png?alt=media&token=4125df00-d67d-414f-8c2e-1bf678da66d3>)

_Customize component spacing to change it across the entire product_

### Add custom system spacing & patterns

Use this approach when you need spacing units beyond what Material provides, or have common adaptive spacing patterns in your product.

-   Follow the multiplier pattern for new space tokens, so **space225 = 18dp** (8dp x 2.25).

-   Spacing patterns unique to your product can be tokenized.


-   For example, if cards and sheets adapt horizontal content padding the same way, you could create a **surface content horizontal padding** token for that pattern.

![An outlined card and a bottom sheet both use the surface-content.padding.horizontal spacing pattern token.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp47j8c0-09.png?alt=media&token=6645788e-b661-4525-a3e8-ceeb553432a6>)

_For products that adapt spacing the exact same way, consider creating a token for that pattern_

### Add adaptive layout & density

Use this approach when you want the same core component, but want it to appear differently in specific situations, like screen sizes and density settings.

-   Adaptive layout: Map the spacing to different system tokens for each device type, such as mobile or desktop

-   Density: Adapt vertical padding to different spacing values for each setting

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp47k75s-GM3%20-%20Spacing%20-%20Applying%2001%20-%20%20IA%20-%20v01.mp4?alt=media&amp;token=dd6dc0fc-646f-4a8c-8afe-86bb6f067126" title="A button’s spacing changes based on if it’s on mobile, desktop, cars, XR, or TVs."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp47k75s-GM3%20-%20Spacing%20-%20Applying%2001%20-%20%20IA%20-%20v01.mp4?alt=media&token=dd6dc0fc-646f-4a8c-8afe-86bb6f067126>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp47kp67-GM3%20-%20Spacing%20-%20Applying%2002%20-%20%20IA%20-%20v01.mp4?alt=media&amp;token=59436e7e-acc6-412f-a59d-d6ca2aba340d" title="A list item’s spacing changes based on density settings of 0 to -4."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmp47kp67-GM3%20-%20Spacing%20-%20Applying%2002%20-%20%20IA%20-%20v01.mp4?alt=media&token=59436e7e-acc6-412f-a59d-d6ca2aba340d>)
