---
title: "Design tokens"
source_url: "https://m3.material.io/foundations/design-tokens/how-to-use-tokens"
lastmod: "2025-11-27"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets: []
---

# Design tokens

Design tokens are the building blocks of all UI elements. The same tokens are used in designs, tools, and code.

## How to use tokens

## Download Material baseline tokens

Material Design’s baseline theme includes design tokens and default values. [Download the theme](http://github.com/material-foundation/material-tokens) as a Design System Package (DSP) to customize, collaborate on, and use in your own designs and product code.  [Learn about the DSP JSON format](https://github.com/AdobeXD/design-system-package-dsp)

## Use tokens in Figma

To begin, install the [Material Theme Builder](https://goo.gle/material-theme-builder-figma) Figma plugin from the community page.


### Generate tokens

1.  Open Figma and navigate to: **Plugins** > **Material Theme Builder** > **Open Plugin**
2.  Select **Get started**, this will create **material-theme** with baseline values by default. Color and text styles will begin populating the right hand design panel. When your tokens are fully generated, your artboard will contain tonal palettes for light and dark color schemes, as well as a default type scale.
3.  Your tokens are now represented as [Figma styles](https://help.figma.com/hc/en-us/articles/360039238753-Styles-in-Figma) that can be used throughout your designs


### Update token values

#### Using the Material Theme Builder Figma plugin (updates colors only)

1.  Open Figma and navigate to: **Plugins** > **Material Theme Builder** > **Open Plugin**
2.  Choose the colors. Updated color and text styles will begin populating the right hand design panel.
3.  Your updated tokens are now represented as [Figma styles](https://help.figma.com/hc/en-us/articles/360039238753-Styles-in-Figma) that can be used throughout your designs


#### Using Figma styles

1.  In Figma, navigate to the file in which the tokenized style is defined. Shortcut: right click the style in the right hand sidebar and select **Go to style definition.**
2.  In the right hand sidebar, hover over the style you want to update and **select the adjust icon when it appears**. Or, right click the style in the style picker and select **Edit style**.
3.  Make your changes to the token name, description, properties, etc. using the **Edit style panel**. Close the panel when finished.


### Use tokens in product mock-ups

1.  Instead of manually setting the color or typography for elements in a layout, apply the [Figma styles](https://help.figma.com/hc/en-us/articles/360039238753-Styles-in-Figma) representing your design tokens. This helps ensure that developers will correctly understand and apply your design choices.


### Use tokens with the Material Design Kit

1.  Duplicate the Material Design Kit in Figma
2.  Navigate to **Plugins** > **Material Theme Builder** > **Open plugin**
3.  With components selected, **select swap**. This will swap the baseline Material token style values with your own generated token style values.


### Export tokens

1.  Open Figma and navigate to: **Plugins** > **Material Theme Builder** > **Open Plugin**
2.  Navigate to the **Export tab**. Select the format you want to export for (Android, Jetpack Compose)
3.  Name your .zip file and select **Save**

Your tokens are ready to share!
