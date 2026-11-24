---
title: "Color system"
source_url: "https://m3.material.io/styles/color/system/how-the-system-works"
lastmod: "2026-05-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/d7293f5bfed3-m4mxjs2l-1.png"
  - "assets/images/8dbbd7d90143-m4mxjwiz-2.png"
  - "assets/images/167bb096a331-m4mxk5mu-3.png"
  - "assets/images/68bbd0744d97-m4mxkasm-4.png"
  - "assets/images/73d129769a1c-ln9uzzb5-from-source-color-to-UI.png"
  - "assets/videos/c8b8cb23f050-linbo537-MAT020.03-v3.mp4"
  - "assets/images/27de3802c264-ln9v1jfc-wallpaper-source-color.png"
  - "assets/images/d52656736d89-ln9v2356-in-app-content.png"
  - "assets/images/63b8a11144cd-ln9v32x5-hand-picked-color.png"
  - "assets/images/ae37fc069fb5-ln9v56si-content-vs-user-left-user.png"
  - "assets/images/89135bced30c-ln9v5j63-content-vs-user-right-content.png"
  - "assets/images/2416468f0867-lnc8pq3o-content-vs-user-right-content.png"
  - "assets/images/c098290dd985-ln9w1sps-from-source-color-to-key-colors.png"
  - "assets/images/f520d4aa3a9e-m612xcmj-13.png"
  - "assets/images/5b46a2786df5-m612xkza-14.png"
  - "assets/images/e02c61573112-m612xrj4-15.png"
  - "assets/images/99398974478c-lnem28e4-color-roles-dark-theme.png"
  - "assets/images/4053b80ffaf3-lna5nn2o-color-roles-UI-elements.png"
  - "assets/images/1af33a54a13e-lna5ow9i-user-controlled-contrast_04.png"
  - "assets/images/b88c5876a258-lna5p9nh-user-controlled-contrast_05.png"
  - "assets/images/2d6ea1366dd8-lna5pkhc-user-controlled-contrast_06.png"
  - "assets/images/2b409ac2fb19-lna5qia9-user-controlled-contrast_07.png"
  - "assets/images/f16316d8726b-lna5qyca-user-controlled-contrast_08.png"
  - "assets/images/651f20a146f4-lna5s0mi-user-controlled-contrast_12.png"
  - "assets/images/bfb0f09eddbe-lna5shuk-user-controlled-contrast_13.png"
  - "assets/images/492f05e6d74f-m612y8rt-25.png"
  - "assets/images/03682d346d98-m612yd3c-26.png"
  - "assets/images/9e119638c170-m6134eza-27.png"
  - "assets/videos/e07cb2ccc1bb-lfmpm0pq-MAT020.02-v3.mp4"
  - "assets/images/0cde0cf03ad1-lna5w3u0-hue.png"
  - "assets/images/028da004c7f3-lna5x1is-chroma.png"
  - "assets/images/886397a15276-lna5xncz-tone.png"
---

# Color system

Create accessible, personal color schemes communicating your product's hierarchy, state, and brand

## How the system works

## It's like paint-by-number

Imagine your product screen as a paint-by-number canvas:

-   Each element on the screen has a number
-   Each number is assigned a color

![UI in "x-ray" view where each element has a number instead of a color](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4mxjs2l-1.png?alt=media&token=a123a83d-6785-40ed-9dae-13d566e3399d>)

_Each part of a UI is assigned a "number," and each "number" is assigned a color_

You can hand-pick a color for every "number" to create a static color scheme.

![Green icon button in the UI, assigned a hand-picked color using a color picker](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4mxjwiz-2.png?alt=media&token=b53af031-c021-4fb8-ba5e-1c6c2b4c9c10>)

_Static colors are hand-picked, like this green icon button_

But now, you can also use Material's dynamic color system to automatically generate an entire palette of accessible colors for each "number" from a single source.

This source can be a user's wallpaper, or in-app content like imagery. If the source changes, the product colors update to match.

![Image showcasing how a source color is automatically applied to each "number"](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4mxk5mu-3.png?alt=media&token=b2543282-21d2-472f-87ed-ef63ec926c4f>)

_Colors are generated dynamically from a user's wallpaper or in-app content_

You can customize how dynamic color appears in your product by:

-   Setting the color source
-   Adding static or harmonized colors
-   Changing which "numbers" are assigned to which elements

[Learn about advanced customizations](../advanced/define-new-colors.md)

![Image showing a color wheel where a light red color is picked, which then populates the UI. In this example, some UI elements have been mapped to different "numbers"](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm4mxkasm-4.png?alt=media&token=b0cb1a67-16ef-44dd-b951-d63f75ce011b>)

_The color source can be changed, automatically changing the color scheme. The UI elements can have other "numbers" assigned to them._

## Essential terms

### Color role 

Like the "numbers" on a paint-by-number canvas, color roles are assigned to specific UI elements. They have semantic names like **primary**, **on primary**, and **primary container,** and matching color tokens. [See all color roles](../roles.md)

### Dynamic color

Dynamic color takes a single color from a user's wallpaper or in-app content and creates an accessible color scheme assigned to elements in the UI. If the user's wallpaper or the in-app content changes, the colors in the UI will change to match.

### Static color

UI colors that don't change based on the user's wallpaper or in-app content. Static colors can be hand-picked or generated in design tools like the Material Theme Builder. Once assigned to their respective color roles and UX elements, the colors remain constant.

#### **Baseline static color**

The default static color scheme for Material products. [See the baseline color scheme](https://m3.material.io/m3/pages/static/)

![Diagram illustrating the steps from source color to key colors to tonal palettes to color roles to the UI](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln9uzzb5-from-source-color-to-UI.png?alt=media&token=962f5bc2-868a-404e-a314-37b1467ecba8>)

_The dynamic color process is automatic. A single source color is used to generate five key colors, which are used to make tonal palettes. Tones from the palettes are then assigned to color roles, which are in turn assigned to elements of the UI._

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flinbo537-MAT020.03-v3.mp4?alt=media&amp;token=d22f7ac8-7c43-4730-831d-90ceaddfc783" title="How dynamic color schemes are created."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flinbo537-MAT020.03-v3.mp4?alt=media&token=d22f7ac8-7c43-4730-831d-90ceaddfc783>)

## How dynamic color generates color schemes

### 1\. It starts with a source color

There are three ways to get a source color.

#### **A. Generate it from a wallpaper**

User-generated color is sourced from a user's personal wallpaper. The wallpaper is digitally analyzed through a process called quantization, and a single color is selected as the source color.

![Red source color is extracted from a wallpaper](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln9v1jfc-wallpaper-source-color.png?alt=media&token=6fe82ae6-f02c-488e-b0e1-e1b94ee1ca78>)

_Source color from a user's wallpaper_

#### **B. Generate it from in-app content**

Content-based color is sources from in-app content, like an album thumbnail image, logo, or video preview. 

Like user-generated color, the image is digitally analyzed through quantization, and a single color selected as the source color.

![Blue source color is extracted from an podcast cover](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln9v2356-in-app-content.png?alt=media&token=07c31cfd-52e9-493a-a4d6-b92fbeaf80ea>)

_Source color from in-app-content_

#### **C. Pick it by hand**

A hand-picked source color is deliberately selected by a designer.

**Did you know?** The baseline static color scheme uses a hand-picked source color.

![Green source color selected from a color picker](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln9v32x5-hand-picked-color.png?alt=media&token=66bd9129-5bbd-447c-bb2d-98ce379b9bf1>)

_Source color hand-picked by a designer_

### 2\. Feed the source color into an algorithm

Dynamic color is powered by the [Material Color Utilities](https://github.com/material-foundation/material-color-utilities) (MCU), a set of color libraries containing algorithms and utilities that develop color themes and schemes in your app.

There are many color algorithms, but the most common ones are:

-   **User-generated color algorithm
    **Uses personal wallpaper to identify source color. Maps colors of specific tones (lighter or darker) into the scheme according to a combination of system design choices and user preferences.


-   **Content-based color algorithm**
    Uses image for source color. Tones are adjusted to match the appearance of the source image, while maintaining accessible contrast. 


-   **Custom colors** 
    Colors closely match the chosen input colors, such as those representing brand or semantic meaning.

![Color palette made by the user-generated algorithm](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln9v56si-content-vs-user-left-user.png?alt=media&token=e6855edb-f3f7-4c8b-96c5-53a54ae48ca6>)

_1. When run through the user-generated color algorithm, the source color is turned into a full color scheme_

![Color palette made by the content-based algorithm](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln9v5j63-content-vs-user-right-content.png?alt=media&token=7fd0ac00-3154-4d51-a4cf-75758d48022b>)

_2. When run through content-based color algorithm, the same source color creates a slightly different color scheme. Some tones are adjusted to better match the appearance of the source image._

![Color palette made by the custom color algorithm](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flnc8pq3o-content-vs-user-right-content.png?alt=media&token=c5300a4d-cbf4-4670-8e80-0dab375f0e6a>)

_3. Custom colors, such as brand colors, can individually run through the algorithm to create a custom scheme that matches the brand_

### 3\. The algorithm generates key colors

Material's color algorithms manipulate the source color's hue and chroma to generate **five complimentary key colors**.

1.  Primary
2.  Secondary
3.  Tertiary
4.  Neutral
5.  Neutral variant

![Diagram of a source color generating five key colors: primary, secondary, tertiary, neutral and neutral variant](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln9w1sps-from-source-color-to-key-colors.png?alt=media&token=44238439-ec0c-4d45-b7a2-6f2aea5f3dee>)

_A source color generates five key colors_

### 4\. The algorithm creates tonal palettes

The system then manipulates tone and chroma values to create a **tonal palette** for each key color. Colors in these palettes are given a number from 0 to 100 in increments of 10, as well as 95, 98, and 99. Some palettes include more values.

![Primary, secondary, tertiary, neutral and neutral variant tonal palettes, consisting of 13 tonal steps](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm612xcmj-13.png?alt=media&token=8cdf6484-55c8-45f7-b989-b2becd9ef8f8>)

_The smaller the tonal value, the darker the color_

### 5\. The algorithm assigns tones to color roles

The algorithm uses accessible color relationships to **assign specific tones to the 26** **color roles** in both light and dark theme.

For example, the algorithm assigns the color tone primary40 to the **p****rimary** role and the tone primary100 to the **o****n primary** role.

[See all color roles](../roles.md)

![Diagram mapping color tones to roles](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm612xkza-14.png?alt=media&token=65142248-ae42-4818-9ade-d30b5cf2c110>)

_Tones from the tonal palette are assigned to different roles_

![Diagram of tonal palettes mapped to all color roles across light and dark themes](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm612xrj4-15.png?alt=media&token=141b2627-4abd-4db8-9342-37432e4b162b>)

_Colors from the five tonal palettes are assigned to color roles. For example, primary roles are picked from the primary tonal palette, while surface roles are picked from the neutral tonal palette._

Dark theme colors are also automatically assigned so that apps receive both light and dark themes through a single set of color roles.

![Diagram of dark theme colors](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flnem28e4-color-roles-dark-theme.png?alt=media&token=cfc39eb0-67a9-4414-9bb6-b0e43087babf>)

_The same color roles are used in light and dark themes_

### 6\. The new colors are applied to the UI

The 26 standard color roles are already assigned to elements of the UI. When a new source color is picked, the UI dynamically changes color.

![Diagram of all the color roles with an example of how it would look applied to a UI](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5nn2o-color-roles-UI-elements.png?alt=media&token=09e3359f-ca02-4ee0-b762-d2e14e1bfe33>)

_Color roles assigned to the UI_

## Color roles support three levels of contrast

In addition to light and dark theme, color roles also support three levels of contrast. This helps people select the contrast setting that best suits their vision needs:

-   Standard (default)
-   Medium
-   High

The standard contrast emphasizes visual hierarchy using high and low contrast elements. People with vision disabilities may choose medium or high contrast options for better support.

![Email app in standard contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5ow9i-user-controlled-contrast_04.png?alt=media&token=b554e395-de19-4132-9763-a4096030656a>)

_Standard contrast The baseline color scheme already uses mixed levels of contrast to reduce cognitive load_

![Email app in medium contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5p9nh-user-controlled-contrast_05.png?alt=media&token=29407208-146b-490f-8571-31e43c5c66bb>)

_Medium contrast Provides a minimum contrast ratio of 3:1 for those who need more contrast, but may experience visual discomfort with higher contrasts from effects like halation._

![Email app in high contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5pkhc-user-controlled-contrast_06.png?alt=media&token=0f3ac5f5-5030-45bf-adce-3d0b96de2680>)

_High contrast Further emphasizes essential elements with a 7:1 contrast ratio to reduce visual distractions and enable users to focus. For example, high contrast is applied to the content in a card but not the card container._

The contrast settings are automatically applied to both light and dark themes.

![The same mail app at three contrast levels, all in light theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5qia9-user-controlled-contrast_07.png?alt=media&token=021ee96e-c069-488f-bbb4-c1f507001ee2>)

_Light theme_

![The same mail app at three contrast levels, all in dark theme.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5qyca-user-controlled-contrast_08.png?alt=media&token=a7815dcb-53cf-4090-93c4-703638708d80>)

_Dark theme_

Custom components can support contrast levels by using Material's appropriate color roles. For example, use **primary container** and **on primary container**.

Use design tokens to apply color roles to custom components.

![Custom volume slider component using Primary Container and On Primary Container roles, whose colors change automatically at standard contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5s0mi-user-controlled-contrast_12.png?alt=media&token=7a4b2a09-68dc-44e6-bcc2-84ae230ac73f>)

_A custom volume slider can use primary container and on primary container color roles to support contrast levels_

![Custom volume slider component using Primary Container and On Primary Container roles, whose colors change automatically at standard contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5shuk-user-controlled-contrast_13.png?alt=media&token=98766331-252a-48d2-a820-53dc11630029>)

_At medium and other contrast levels, those color roles apply the necessary new color values_

## Pairing accessible tones

The system manipulates hue, chroma, and tone (HCT) values to **create a tonal palette for each color** with tones ranging from 0 to 100.

Color has physical limitations—whether it's actual physics, our own biological visual limitations, or the limitations of on-screen color rendering. For example, some hues cannot exist with certain chroma or tones. Color limitations are the reason colors such as bright light blue or bright light red are not quite possible. This is why the chroma value may increase or decrease for some tones in a tonal palette.

![13 tones derived from a key color. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm612y8rt-25.png?alt=media&token=e4d5c0e1-185c-44fa-908b-9c3002839c62>)

_Tonal values range from 0 (black) to 100 (white). The smaller the tonal value, the darker the color._

Material's color algorithms use these palettes to find and **pair contrasting tones,** creating accessible color combinations.

Because tone can describe the lightness or darkness of a color, it's used to define accessible color relationships. Those relationships are built into Material's color algorithms.

For example, the algorithms assign a dark tone to a button's container color and a light tone to its label color, ensuring that the colors have a 3:1 contrast.

![Colors of tones 50 and 98 used for button fill color and background fill color, which create a contrast greater than 3:1 between a button and its background.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm612yd3c-26.png?alt=media&token=1ebd4f8b-1e78-4589-bc02-f411dbf29283>)

_Using tones 50 and 98 for a button and its label creates an accessible 3:1 contrast_

For even more contrast, the algorithms assign tones even farther apart, achieving a 7:1 contrast.

This is the concept powering **user-controlled contrast** features.

![Colors of tones 30 and 98 used for button fill color and background fill color, which create a contrast greater than 7:1 between a button and its background.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm6134eza-27.png?alt=media&token=17bc3773-7657-4067-9821-a118a42a0909>)

_Using colors of tones 30 and 98 for a button and its label create a 7:1 contrast_

## Defining colors with hue, chroma, and tone (HCT)

**The system uses a color space called HCT**, which defines all colors using three dimensions: hue, chroma, and tone.

Changing HCT values lets you manipulate colors in flexible but predictable ways. Unlike other color spaces (like HSL or RGB), HCT allows the manipulation of a color's hue and chroma without affecting its tone. Watch to learn more:

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Four-fake-design-system%2Fimages%2Flfmpm0pq-MAT020.02-v3.mp4?alt=media&amp;token=39ca7b87-2339-4ad8-b2e2-2b69286c7e2e" title="Animated video explaining the three color dimensions (hue, chroma, and tone) and how they relate to accessible color schemes."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Four-fake-design-system%2Fimages%2Flfmpm0pq-MAT020.02-v3.mp4?alt=media&token=39ca7b87-2339-4ad8-b2e2-2b69286c7e2e>)

### Hue

Hue is the perception of a color as red, orange, yellow, green, blue, violet, and so on. Hue is quantified by a number ranging from 0-360 and is a circular spectrum (values 0 and 360 are the same hue).

![The hue spectrum looks like a rainbow circle.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5w3u0-hue.png?alt=media&token=61b24e9f-2268-4135-b3e3-df102b6ee769>)

_360 degree hue spectrum_

### Chroma

Chroma is how colorful or neutral (grey, black or white) a color appears. Chroma is quantified by a number ranging from 0 (completely grey, black or white) to infinity (most vibrant), though Chroma values in HCT top out at roughly 120.

Because of biological and screen rendering limitations, different hues and different tones will have different maximal chroma values.

![Diagram showing chroma range from 120 (maximum intensity) to 0 (pure grey). A second diagram shows how pure black and pure white also correspond to 0 chroma](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5x1is-chroma.png?alt=media&token=f7e93869-ad3a-4938-95c3-66f5d344339f>)

_The higher the color purity, the higher the chroma; Note how lightening and darkening a hue also affects its chroma value_

### Tone

Tone is how light or dark a color appears. Tone is sometimes also referred to as luminance. Tone is quantified by a number ranging from 0 (pure black, no luminance) to 100 (pure white, complete luminance).

Tone is crucial for visual accessibility because it determines contrast. Colors with a greater difference in tone create higher contrast, while those with a smaller difference create lower contrast.

![Gradient showing the range of tones from 1 (black) to 100 (white)](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Flna5xncz-tone.png?alt=media&token=665bed68-9373-41e6-b15d-d5d7d1e1a05f>)

_The 100 tone is always 100% white, the lightest tone in the range; the 0 tone is 100% black, the darkest tone in the range_
