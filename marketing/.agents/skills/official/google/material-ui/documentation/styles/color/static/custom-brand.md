---
title: "Static color schemes"
source_url: "https://m3.material.io/styles/color/static/custom-brand"
lastmod: "2026-02-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "styles"
assets:
  - "assets/images/ec2f59139e5d-ln4ilrfy-brand-baseline.png"
---

# Static color schemes

Static color schemes are ideal for branded products that should have a consistent, uniform design

## Custom brand

In a brand-based static scheme, the colors are hand-picked by your team to align with your product's brand color. Brand-based schemes are entirely created and maintained by your team, so this approach requires a larger investment of time and effort.

**With a brand color scheme, end-users see**

-   An accessible UI with static colors
-   A product that "looks like its brand"

![Left: A swirling red, magenta and green sphere representing a meditation app logo. Center: A color scheme created from the three brand colors in the sphere. Right: A screen of the meditation app colored in red, magenta, and green UI.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgm3sandbox%2Fimages%2Fln4ilrfy-brand-baseline.png?alt=media&token=7831c3fc-1a36-4aa2-a583-65474ec97d3d>)

_This example meditation app uses a static scheme created from its brand colors._

## Create a custom brand color scheme

1.  Open your Figma design file. Select the **Actions** menu (or Ctrl/Command+K).

2.  Find the [Material Theme Builder plugin](https://www.figma.com/community/plugin/1034969338659738588/material-theme-builder) and select **Run**. This will open a plugin dialog showing the default color scheme, including Core colors and Extended colors.

3.  Open the plugin's **Settings** (gear icon at lower right of dialog) and select the checkboxes for both **New theme color diagram** and **Generate State Layers**. This will create a handy visualization of your branded color scheme and also generate state layers essential for designing interactions. [Learn more about state layers](https://m3.material.io/m3/pages/interaction-states/state-layers)

4.  Navigate out of settings.

5.  Open the Current Theme dropdown at the top of the dialog and select **\+ ADD NEW THEME.**

6.  Give your theme a short name (this name will become the prefix of your color roles in Figma).

7.  Select **ADD THEME.**

8.  With **Custom** selected, select **Primary**. This opens a dialog prompting you to select a custom source color.

9.  Enter the Hex value for your brand color and hit **Apply**.


This will generate a full custom color scheme. You can use the scheme as-is or repeat steps 5 and 6 to set custom sources for the Secondary, Tertiary, Error, Neutral, and Neutral Variant colors.

**Want to further adjust your brand color scheme? Check out** [**Advanced customizations**](../advanced/overview.md)

## Design with brand colors

### Use brand colors in new design files

1.  Create your Figma file. Enable the [M3 Design Kit](https://www.figma.com/community/file/1035203688168086460) in your Assets panel.

2.  Copy your scheme's color diagram and paste it into the file (this makes the color roles available in the Design panel on the right of the screen as part of your local styles).

3.  Apply your brand color roles to custom components and UI elements by hovering on the element's color property in the Design panel on the right of the screen and selecting the **Style** icon (four dots). This opens a selection dialog.

4.  Search for your theme's name to see your brand color roles.

5.  Select the brand color role that most closely matches the use case and intent (see [Color roles](../roles.md) for more information on what color to use where).

6.  Repeat until all custom elements are using your brand color roles.


### Apply brand colors to an existing file or M3 Design Kit components

**First, get your brand colors into your file**

1.  Copy your scheme's color diagram and paste it into the file (this makes the color roles available in the Design panel on the right of the screen as part of your local styles)


**Swap colors in M3 Design Kit components for your brand colors**

1.  Find the [Material Theme Builder plugin](https://www.figma.com/community/plugin/1034969338659738588/material-theme-builder) and select **Run**. This will open a plugin dialog showing the default color scheme, including Core colors and Extended colors.

2.  In the Current Theme dropdown at the top of the dialog, select your scheme.

3.  Select the frames or M3 Design Kit components in your file that need a color update and then hit **Swap** in the bottom right of the dialog. This will automatically update their colors from baseline colors to your brand colors


**Then, update any remaining non-brand color styles**

1.  Manually change any hex values or non-brand color styles by selecting all and looking through the Selection colors in the Design panel on the right of the screen.

2.  Any colors that don't start with your theme name need to be replaced with a corresponding brand color.

3.  Hover on a non-brand color row in the Design panel and select the **Style** icon (four dots). This opens a selection dialog.

4.  Search for your theme name to see the brand color roles.

5.  Select the brand color role that most closely matches that color's use case (see [Color roles](../roles.md) for more information on what color to use where) and select **Use style** to apply it to the selected objects.

6.  Repeat until all non-brand colors in the file have been replaced with brand color roles.


**Need to make adjustments to the scheme? Check out** [**Advanced customizations**](../advanced/overview.md)

## Develop with brand colors

-   Export your branded color scheme from the Material Theme Builder (Available for Jetpack Compose, Android Views, Flutter, Web, or as a JSON file)
-   Android: [Customize the default theme](https://developer.android.com/develop/ui/views/theming/themes#CustomizeTheme)
