---
title: "Building a Material Theme on Android: Color"
source_url: "https://m3.material.io/blog/android-material-theme-color"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/a47b4728bf09-1xWolfPsT-609CJjfZQkU0YpR69DVltAb4IL4NVbcxNKbnya_LvQmo4csc_tQjbyUBw6JKLiMX82veLqYzDq7IC06UJE48kA.png"
  - "assets/images/76aff2153f34-WSEx_H0IQA9vu59OUeW2-5rKvdBPip6FGPBQwIPFgGMnmrtAnCKG_1MILDjOzIEvUW-y9O4AwAVfx27ox-cyq2yTuMQ6P-uF.png"
  - "assets/images/d35f841fee5e-c6rSVlgF6wR6ziI6Xv1fkik7yJ7-P_2F7XoV9yj4gf9y8lMz0iSgKxSbO3PM7i8QqRb1ywWlzl4_5CdMgzUbTY3zkUmp0kPj.png"
  - "assets/images/cd446775b2d3-AqxNVkIrZ5zZfOBclmWoilz62CVP3slvtf9sxXO1GorK-P4omr2w2VsiQFQqZMqAhYY3ZMzo7vArP25a4jrzcR7O5q9Yx5K3.png"
  - "assets/images/b1c682949d87-9rKRhZ0G1kkjF4zHDhdQOEV96itrBYsek1aiudofB72zqDSLL9-AtrIWZ7_HFTmwT4t2N2l4soobdSukX1jqWfAZvkKACZNT.png"
  - "assets/images/214d54c4bb7d-MAmKwjV4z13vVbNGNeW3XgegBXDDAGJzC6OUDWgINp9LSKziWM90NwmzA46ox3Zd3cc7JnHaUkDzyGN_ehqFKRSjPBTtNVb8.png"
  - "assets/images/3c4a1b4be54e-COT53HX7UojIhPnhJQG4tnsFmooxO9OoYXP1uQmhFI1JY6wxNqyz_jVH-7FgAy3lnHJhmNOtvrEpu1r7WbxfaCI6Y9hU7lOd.png"
  - "assets/images/c71ecfbb41a4-jjQy3wbrigJjqel0UJVAh3OcgM-MfUQ-a0H7uS1t9sI6QMeW_RI3Kp1mclZ-4m0DIywAlKNMyTkXBpNGLsdjomH8I6pnDq8G.png"
  - "assets/images/48d5798dc873-k4xePGofQjSnzipMkSXxc2lz6WhETD-wMhAkF7Yid__0nj7kNy4ggdzsyyLMSwANGJv1IZRMYFI1VXIEr4KuDezTY1CJOfbj.png"
  - "assets/images/bfa6bb17bcfb-d5qv8fRyb6QuacMZ-3DzdnjXqOF8wgVgrDouJS5dDCyu01h3WeutmL5QlyZ86xvOr0SFY8oOADKnS5HsDE2Sp4_uKMJ6diEL.png"
---

# Building a Material Theme on Android: Color

Color theming on Android using the MDC library

![Image](<https://lh3.googleusercontent.com/1xWolfPsT-609CJjfZQkU0YpR69DVltAb4IL4NVbcxNKbnya_LvQmo4csc_tQjbyUBw6JKLiMX82veLqYzDq7IC06UJE48kAZBJ-HIk>)

Published: 2020-09-01T13:07:00

[Material Theming](https://material.io/design/material-theming/overview.html#material-theming) is a way to customize [Material Components](https://material.io/components) to align with your brand. A Material theme includes [color](https://material.io/design/color/), [typography](https://material.io/design/typography/) and [shape](https://material.io/design/shape/) parameters which you can adjust to get near-infinite variations of the components – all while maintaining their core anatomy and usability.

![Material button transforming from baseline style to show off different options for color, shape, typography, and label](<https://lh3.googleusercontent.com/WSEx_H0IQA9vu59OUeW2-5rKvdBPip6FGPBQwIPFgGMnmrtAnCKG_1MILDjOzIEvUW-y9O4AwAVfx27ox-cyq2yTuMQ6P-uFwrL0ZQ>)

On Android, Material Theming can be implemented using the [Material Components (MDC) library](https://github.com/material-components/material-components-android), from version `1.1.0` onwards. If you’re looking to migrate from the Design Support Library or MDC `1.0.0`, take a look at [our migration guide](https://medium.com/androiddevelopers/migrating-to-material-components-for-android-ec6757795351).

This article will be focusing on **color** theming.

## Color attributes

Material Design provides 12 color “slots” that make up the overall palette of your app. Each of these have a design term (eg. “Primary”) along with a corresponding color attribute that can be overridden in your app theme (eg. `colorPrimary`). There are default “baseline” values for both your light theme and dark theme.

![Image](<https://lh3.googleusercontent.com/c6rSVlgF6wR6ziI6Xv1fkik7yJ7-P_2F7XoV9yj4gf9y8lMz0iSgKxSbO3PM7i8QqRb1ywWlzl4_5CdMgzUbTY3zkUmp0kPjwTpfDfE>)

MDC color attributes with light baseline values

![Image](<https://lh3.googleusercontent.com/AqxNVkIrZ5zZfOBclmWoilz62CVP3slvtf9sxXO1GorK-P4omr2w2VsiQFQqZMqAhYY3ZMzo7vArP25a4jrzcR7O5q9Yx5K33j_WFA>)

MDC color attributes with dark baseline values

Material Components use these color attributes to tint elements of the widgets.

![Button color attributes: ?attr/colorOnPrimary for + icon and button text, ?attr/textAppearanceButton for button text, ?attr/shapeAppearanceSmallComponent for button shape, ?attr/colorPrimary for background color](<https://lh3.googleusercontent.com/9rKRhZ0G1kkjF4zHDhdQOEV96itrBYsek1aiudofB72zqDSLL9-AtrIWZ7_HFTmwT4t2N2l4soobdSukX1jqWfAZvkKACZNTRBCodA>)

Color attributes used by a button

They are applied in layouts and widget styles like this, for example:

You might recognize some of these color attributes like `colorPrimary`. That’s because a few of them are inherited from AppCompat and the platform, while the rest have been newly introduced by MDC. The table below illustrates the origin of each attribute.

## Picking colors

Figuring out which color values to use for each slot may be the responsibility of a designer, or derived from your product’s brand. However, it’s still useful to know about the role of each color, the relationship between them, and how to meet accessibility requirements:

-   `colorPrimary` and `colorSecondary` represent the colors of your brand
-   `colorPrimaryVariant` and `colorSecondaryVariant` are lighter or darker shades of your brand colors
-   `colorSurface` is used for “sheets” of material (like cards and bottom sheets)
-   `android:colorBackground` is the window background color of your app
-   `colorError` is, as the name suggests, for errors and warnings
-   The various “On” colors (`colorOnPrimary`, `colorOnSecondary`, `colorOnSurface`, etc.) are used to tint foreground content (such as text and icons) displayed on top of the other colors. They need to meet accessibility requirements and have sufficient contrast against the colors they’re displayed on.

## Color tools

Material Design provides useful tools for previewing colors and determining suitable variants and “On” colors:

-   [Material color tool](https://material.io/resources/color/): Get light/dark variants of your primary and secondary colors as well as the appropriate “On” color. Preview how these will look in sample screens.
-   [Material palette generator](https://material.io/design/color/the-color-system.html#tools-for-picking-colors): Generate a full tonal palette (shade 50 - 900) of a color. Get suggestions for complementary, analogous, and triadic colors.

![Image](<https://lh3.googleusercontent.com/MAmKwjV4z13vVbNGNeW3XgegBXDDAGJzC6OUDWgINp9LSKziWM90NwmzA46ox3Zd3cc7JnHaUkDzyGN_ehqFKRSjPBTtNVb8HuMaeQ>)

Material color tool (left) and Material palette generator (right)

## Things to consider

-   You almost always want to override `colorPrimary`, `colorSecondary` and their variants, unless your brand happens to use the exact same purple/teal hex values as the baseline Material Theme.
-   You don’t have to override all colors. Some, such as `colorSurface`, use neutral colors so relying on the default values is perfectly fine.
-   If your brand does not define any kind of secondary or accent color, it’s ok to use a single color for both `colorPrimary` and `colorSecondary`. The same can be said of variants (eg. `colorPrimary` and `colorPrimaryVariant` could be the same).
-   Despite being separate attributes, there’s an inherent link between a color, its variant (if one exists), and its “On” color (eg. `colorPrimary`, `colorPrimaryVariant` and `colorOnPrimary`). Overriding one means you need to check the others to see if they make sense and meet accessibility requirements.

## Additional color slots

Your design system may call for additional color slots outside of the 12 that Material Theming specifies. Thankfully this is relatively easy to do on Android by declaring a color attr:

## Color resources

Color values are defined as `&lt;color&gt;`  resources. For custom colors we recommend two approaches to help separate concerns, and create a single source of truth for color theming values in your app:

-   Store all day `&lt;color&gt;`s in a single res/values/colors.xml file
-   Use literal names for `&lt;color&gt;`s that describe the value as opposed to how it's used:
    -   Doing so encourages the use of `?attr/` references when using colors, which is a recommended approach for supporting dark themes
    -   Use names like `green_500` or `brand_name_yellow`
    -   Avoid semantic names like `color_primary`

## Overriding colors in an app theme

Let’s take a look at how you can add your chosen color palette to your app theme by overriding relevant attributes.

First, we recommend setting up your theme(s) to gracefully handle light and dark color palettes while reducing repetition with base themes. For more on this topic, take a look at Chris Banes’ article on [dark theme](https://medium.com/androiddevelopers/dark-theme-with-mdc-4c6fc357d956) as well as the "[Developing Themes with Style](https://chris.banes.dev/talks/2019/developing-themes-with-style-berlin/)" talk given by him and Nick Butcher.

Once set up, override the color attributes you wish to change in your light and dark themes:

Material Components will respond to theme-level color overrides:

![UI components changing from baseline colors to navy and dark green](<https://lh3.googleusercontent.com/COT53HX7UojIhPnhJQG4tnsFmooxO9OoYXP1uQmhFI1JY6wxNqyz_jVH-7FgAy3lnHJhmNOtvrEpu1r7WbxfaCI6Y9hU7lOd75pWzxM>)

## Color reusability and best practice

There are many circumstances which involve using colors in layouts, drawables, styles, and elsewhere. We’re going to go through some approaches to make your code as reusable as possible, regardless of the color values specified in your app theme.

## Prefer attrs

The most important thing we suggest is to use `?attr/` color references. This is the recommended approach to creating reusable layouts and default styles that support multiple themes like light/dark.

Take a look at Nick Butcher’s [“Android Styling: Prefer Theme Attributes”](https://medium.com/androiddevelopers/android-styling-prefer-theme-attributes-412caa748774) article for further explanations and some exceptions to the rule.

## Colors with alpha

There are times when you may want to use one of the colors from your MDC theme with an alpha value eg. `colorPrimary` at 60%. Examples of this include touch ripples and checked state overlays.

Android `&lt;color&gt;`  resources do allow for an alpha channel:

However, with this approach we need to maintain separate color resources per alpha value. It also means we can’t use these as `?attr/`s and goes against our single source of truth approach mentioned above.

Rather, we suggest taking advantage of `ColorStateList`s stored in your res/color directory. A CSL can have a single item which includes a color reference and an alpha value, which is perfect for our use case:

Using these might cause you to raise an eyebrow – they’re referenced with the `@color/primary_60` notation – but this is fine considering we’re dealing with a CSL that itself uses `?attr/` to reference the underlying theme color.

## Colors per state and theme overlays

`ColorStateList`s are more commonly used to switch between colors (and alpha values) depending on the state of the view. MDC widgets use this generously for disabled states, hovered vs. pressed states and so on. Here’s an example of a button’s background tint from the MDC [source code](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/res/color/mtrl_btn_bg_color_selector.xml):

Keeping with the button example, suppose you want to change the main background tint from primary to secondary:

![Primary button with green background and black text. An arrow points to a secondary button with navy background and white text](<https://lh3.googleusercontent.com/jjQy3wbrigJjqel0UJVAh3OcgM-MfUQ-a0H7uS1t9sI6QMeW_RI3Kp1mclZ-4m0DIywAlKNMyTkXBpNGLsdjomH8I6pnDq8G8elP7g>)

Primary button (left) and secondary button (right)

You _could_ copy the above source file into your codebase and change `colorPrimary` to `colorSecondary`, but this is tedious and becomes problematic if the source code happens to change.

A better way to do this is to use theme overlays. Nick Butcher goes into detail about this in his [“Android Styling: Themes Overlay”](https://medium.com/androiddevelopers/android-styling-themes-overlay-1ffd57745207) post. Essentially we can replace the value of a specific theme attribute (`colorPrimary` in our case) for a particular `View` or `ViewGroup` and any descendants (in our case a button).

A basic theme overlay can be seen below. Note the empty parent, which ensures we only override the attrs we wish to change:

When applying theme overlays in XML, there are two options to consider:

-   `android:theme`: Works with all widgets, doesn’t work in default styles
-   `app:materialThemeOverlay`: Only works with MDC widgets, works in default styles

## API compatibility

Platform support for `?attr/`s in CSLs and elsewhere was only added in API 23. If your minSdk is below this, don’t worry: compatibility classes do exist! In fact, both MDC and AppCompat widgets make use of these under-the-hood so no additional work is required when using them.

For scenarios where you need to use CSLs programmatically, use [`AppCompatResources`](https://developer.android.com/reference/androidx/appcompat/content/res/AppCompatResources):

## Color in MDC widgets

Earlier we said that MDC widgets respond to overrides of theme level color attributes. But how would you know, for example, that a button uses `colorPrimary` as its background tint and `colorOnPrimary` for its icon and text label? Let’s take a look at a few options.

## MDC developer docs

The MDC developer docs have recently been refreshed. As part of this we’ve included attribute tables which include design terminology and default values used in the library. For example, check out the “Anatomy and key properties” sections of the updated [buttons doc](https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md).

![Image](<https://lh3.googleusercontent.com/k4xePGofQjSnzipMkSXxc2lz6WhETD-wMhAkF7Yid__0nj7kNy4ggdzsyyLMSwANGJv1IZRMYFI1VXIEr4KuDezTY1CJOfbj84cvYQ>)

MDC button dev doc attribute table with default color values

## Source code

Inspecting the MDC source code is arguably the most reliable approach. MDC uses default styles to achieve Material Theming so it’s a good idea to look at these as well as any styleable attrs and the java file(s). For example, check out the [styles](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/res/values/styles.xml), [attrs](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/res/values/attrs.xml) and [java file](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/MaterialButton.java) for `MaterialButton`.

![Image](<https://lh3.googleusercontent.com/d5qv8fRyb6QuacMZ-3DzdnjXqOF8wgVgrDouJS5dDCyu01h3WeutmL5QlyZ86xvOr0SFY8oOADKnS5HsDE2Sp4_uKMJ6diEL10hAJQ>)

MDC button default style with color values

## Color in custom views

Your app may include custom widgets you’ve built or gotten from an existing library. Making these views responsive to Material Theming is useful when using them alongside standard MDC widgets. Let’s take a look at what to keep in mind when supporting color theming for custom widgets.

## Use MDC attrs in &lt;declare-styleable&gt;s and default styles

Allowing your custom views to be styled involves using a `&lt;declare-styleable&gt;`. Reusing attr names from MDC can be useful for consistency. Default styles that use `&lt;declare-styleable&gt;`s  can also reference MDC theme color attrs for their values:

## MaterialColors utility class

Resolving theme color attrs programmatically can be done with a handy new MDC class – `MaterialColors` – which may also be useful for custom views:

## OK Google, what’s next?

We’ve been through the process of implementing color theming in your Android app using MDC. Be sure to check out our other posts in this series on [why we recommend using MDC](https://medium.com/androiddevelopers/we-recommend-material-design-components-81e6d165c2dd), [type theming](https://material.io/blog/android-material-theme-type), [shape theming](https://material.io/blog/android-material-theme-shape), [dark theme](https://medium.com/androiddevelopers/dark-theme-with-mdc-4c6fc357d956), and Material’s [motion system](https://material.io/blog/android-material-motion).

As always, we encourage you to file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also be sure to check out our Android [companion example apps](https://github.com/material-components/material-components-android-examples).

If you’ve successfully implemented color theming or if you’re having trouble doing so, reach out to us on Twitter [@MaterialDesign](https://twitter.com/materialdesign).
