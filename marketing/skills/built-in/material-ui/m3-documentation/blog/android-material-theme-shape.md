---
title: "Building a Material Theme on Android: Shape"
source_url: "https://m3.material.io/blog/android-material-theme-shape"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/77c7547bb983-xDCjGLXfjLAHyaA1ZYQrDNPUm8P6zVsK4Zk6AFDzxpnKe10X-FrECxi10pB3hTWxzl-pyJ8SY3ZBreGZpD3lhHBWK-mrFL5H.png"
  - "assets/images/4dce8978f798-VCtdEjXvwAg3Gdgje_AEvZEfwaB6__Cwq5ZeTrVV6DJJzo1SCEWDOZXLjYqkCXa7bZzdE4qtprD4x6pt6VQJAJX3-hFYsMXS.png"
  - "assets/images/c493179629b1-GaJLAG38YYsUT05HM9DWhFl06KQcKOHQ5g-eQ0wQpNqVGtF7FrePVUOHxwn2cTTEntTMqT2hJnP6IxP8B4CjmmoaWxAwzhFe.png"
  - "assets/images/d5920d91a3ec-tfDkjI_T9f9pqZCE96_A_94k5vvogrG6FKM-If4UIRDiehK5iMd7X0a1D5eHQPHthjr28AwPX5d2yhQkHtran3othj3S3lSY.png"
  - "assets/images/791c036cd041-yaLZ_6n-zgJcQNo4HTlO6GQLqO1CtdzLXpBEnw2zrKjvdT__Oa2b0MNCk5MUFhsjYW9gbRC2NVnacmgrCtjl1hU_SLumbagR.png"
  - "assets/images/36e421530415-gnKKrcjEX9IBXFAMTn_9YMa-ECxn3FBHsw__UTqAowjlttLEBwY9kRp0eizEyiunRs12IzywO6u69zRa_qvZXgam8p-HMnDT.png"
  - "assets/images/fd9f67864248-27Uy-sb7tyds8kgaooepHk1KcwizdC-UYZLqW_MqkYFjOFG4_Ijiymvyz-TsU33b1Ccw8_AbXSUaujrT9K5FRKK2iz5fqcyv.png"
  - "assets/images/27f5464b8bcb-Y9sF_I0nve-cuSFQ4DHPuJXZG994yrDDg-Aea8E7HmHqIYr7PJLpDe9cvpaWLBYl1TzYeHRkYD-kLb81ET0krvUwjsWGLwbN.png"
  - "assets/images/ef6fb4112eb7-mNnyGGni-I24PhbGl5XH6VLYRlaPlIUDAPULyRcSRdVQkMJbbe_QHvGm-MCfPF1r-qOSwRsyrab91FCqJ0qwZhiQw5vU8J3E.png"
---

# Building a Material Theme on Android: Shape

Shape theming on Android using the MDC library

![Image](<https://lh3.googleusercontent.com/xDCjGLXfjLAHyaA1ZYQrDNPUm8P6zVsK4Zk6AFDzxpnKe10X-FrECxi10pB3hTWxzl-pyJ8SY3ZBreGZpD3lhHBWK-mrFL5H_FH2zg>)

Published: 2020-09-01T13:03:00

[Material Theming](https://material.io/design/material-theming/overview.html#material-theming) is a way to customize [Material Components](https://material.io/components) to align with your brand. A Material theme includes [color](https://material.io/design/color/), [typography](https://material.io/design/typography/) and [shape](https://material.io/design/shape/) parameters which you can adjust to get near-infinite variations of the components – all while maintaining their core anatomy and usability.

![Material button transforming from baseline style to show off different options for color, shape, typography, and label](<https://lh3.googleusercontent.com/VCtdEjXvwAg3Gdgje_AEvZEfwaB6__Cwq5ZeTrVV6DJJzo1SCEWDOZXLjYqkCXa7bZzdE4qtprD4x6pt6VQJAJX3-hFYsMXSGm2LoA>)

On Android, Material Theming can be implemented using the [Material Components (MDC) library](https://github.com/material-components/material-components-android), from version `1.1.0` onwards. If you’re looking to migrate from the Design Support Library or MDC `1.0.0`, take a look at our [migration guide](https://medium.com/androiddevelopers/migrating-to-material-components-for-android-ec6757795351).

This article will be focusing on **shape theming**.

Most widgets have a background shape, but have you ever thought about the ways that shape influences user behavior? Just like color and typography, shape can guide a user’s attention, suggest interaction, and visually distinguish elements in your UI. Material’s shape theming gives you the ability to define global shape values that change the style of components across your app – for example, giving all your cards, dialogs, and menus really friendly rounded corners.

## Shape attributes

Material Design provides 3 shape “categories” that are applied to the shapeable widgets of your app. Each of these have a design term (eg. “Small components”) along with a corresponding shape attribute that can be overridden in your app theme (eg. \``shapeAppearanceSmallComponent`\`). There are default “baseline” values (corner size, corner family, etc.) for each category.

![ShapeAppearanceSmallComponent with rounded, 4 dp corners. ShapeAppearanceMediumComponent with rounded, 4 dp corners. ShapeAppearanceLargeComponent with rounded, 0 dp corners.](<https://lh3.googleusercontent.com/GaJLAG38YYsUT05HM9DWhFl06KQcKOHQ5g-eQ0wQpNqVGtF7FrePVUOHxwn2cTTEntTMqT2hJnP6IxP8B4CjmmoaWxAwzhFeAu2RbQ>)

MDC shape attributes with baseline values

Material Components use these shape attributes to style backgrounds of the widgets.

![Button color attributes: ?attr/colorOnPrimary for + icon and button text, ?attr/textAppearanceButton for button text, ?attr/shapeAppearanceSmallComponent for button shape, ?attr/colorPrimary for background color](<https://lh3.googleusercontent.com/tfDkjI_T9f9pqZCE96_A_94k5vvogrG6FKM-If4UIRDiehK5iMd7X0a1D5eHQPHthjr28AwPX5d2yhQkHtran3othj3S3lSYxeZXog>)

Shape attributes used by a button

They are applied with eg.

in layouts and widget styles.

In the MDC themes these attributes map to styles, eg.

`ShapeAppearance` styles and the corresponding attributes are new to MDC. These are discussed in more detail in the Shape resources section below.

## Picking shape

Figuring out which shape categories to use and the values within them may be the responsibility of a designer, or derived from your product’s brand. However, it’s still useful to know about the role of each category and in which scenarios they should be used:

-   `shapeAppearanceSmallComponent` is for small-size components like buttons and text fields

-   `shapeAppearanceMediumComponent` is for medium-size components like cards and dialogs

-   `shapeAppearanceLargeComponent` is for large-size components like bottom sheets


See the [shape guidelines](https://material.io/design/shape/applying-shape-to-ui.html#shape-scheme) for a complete list of mappings from component to shape categories.

## Shape tool

Material Design provides a useful [shape customization tool](https://material.io/design/shape/about-shape.html#shape-customization-tool) for previewing shape categories and how changes apply to the corners of various components.

![Image](<https://lh3.googleusercontent.com/yaLZ_6n-zgJcQNo4HTlO6GQLqO1CtdzLXpBEnw2zrKjvdT__Oa2b0MNCk5MUFhsjYW9gbRC2NVnacmgrCtjl1hU_SLumbagRD6XPQg>)

Shape customization tool

## Shape resources

Shape resources consist mainly of `ShapeAppearance` styles. These are analogous to `TextAppearance` styles for type theming; in this case a “style” only concerned with shape attributes. Let’s take a look at what’s available on Android vs. MDC and a few things to keep in mind when declaring styles.

## XML shapes and android:background

Prior to MDC, you’d typically define a custom background in the res/drawable directory, eg.

which is applied to a widget like so:

This is a simplified example. XML shape drawables can include a number of other elements, like `&lt;inset&gt;`s, `&lt;stroke&gt;`s, `&lt;gradient&gt;`s, etc. or support multiple states.

There are times when this approach is necessary but there are drawbacks to consider:

-   It lacks many of the useful features of other theming systems (like color and type); predefined attributes to specify shape at the theme level, overlays and the ability to abstract away shape values in styles

-   Material Design’s [shape system](https://material.io/design/shape/about-shape.html#shaping-material) supports both rounded and cut corners, but there isn’t an elegant solution to achieve cut corners in XML or programmatically

-   Applying complex shape treatments, such as the top edge indent on a [bottom app bar](https://material.io/components/app-bars-bottom), would not be possible and would require implementing a custom `Drawable`


## ShapeAppearance styles

MDC offers a new way to define shape. `ShapeAppearance` styles can be seen as the Android equivalent of Material Design shape categories. They give you a means of defining shape characteristics without needing to deal with drawables directly. They currently only work with MDC widgets and are backed by a new `MaterialShapeDrawable` class, which is discussed in more detail below.

When defining your shape theme, we recommend two approaches to help separate concerns and create a single source of truth for shape theming values in your app:

-   Store all `ShapeAppearance` styles in a single res/values/shape.xml file

-   Use the MDC `ShapeAppearance` styles as parents and adhere to the same naming convention


Attributes and values you can use within these styles align with those supported by `MaterialShapeDrawable`:

-   `cornerFamily` is the family of all corners, either \`rounded\` or \`cut\`

-   `cornerFamilyTopLeft`, `cornerFamilyTopRight`, `cornerFamilyBottomLeft` and `cornerFamilyBottomRight` allow you to change the family of specific corners and take precedence over `cornerFamily`

-   `cornerSize` is the size of all corners, typically a `dp` dimension

-   `cornerSizeTopLeft`, `cornerSizeTopRight`, `cornerSizeBottomLeft` and `cornerSizeBottomRight` allow you to change the size of specific corners and take precedence over `cornerSize`


## ShapeAppearance overlays

You can also define `ShapeAppearance` overlays which support all of the same attributes, and act similarly to theme overlays.

These can be applied alongside regular `ShapeAppearance` styles with `app:shapeAppearanceOverlay` to change the value of specific corner attributes. Here’s an example of a bottom sheet’s overlay, which changes the bottom corners to be flush with the screen, from the MDC source code:

_**Note**: Some MDC widgets have overlays applied by default which you may need to consider when adjusting their `shapeAppearance`. Examples of this include [`FloatingActionButton`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/floatingactionbutton/FloatingActionButton.java)and [`Chip`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/chip/Chip.java) which both set their `cornerSize` to 50% via an overlay._

## Fill and stroke

Unlike XML drawables, `ShapeAppearance` styles don’t include any notion of fill or stroke. MDC tends to separate concerns by specifying this separately in the main widget styles:

_**Note**: `ShapeAppearance` styles and the backing `MaterialShapeDrawable` class only support solid colors for fills and strokes. There is currently no support for gradients and you would need to use XML drawables with a `&lt;gradient&gt;` in this case._

## Overriding shape categories in an app theme

Let’s take a look at how you can add your chosen shape categories to your app theme by overriding relevant attributes.

First, we recommend setting up your theme(s) to gracefully handle light and dark color palettes while reducing repetition with base themes. For more on this topic, take a look at Chris Banes’ article on [dark theme](https://medium.com/androiddevelopers/dark-theme-with-mdc-4c6fc357d956) as well as the ["Developing Themes with Style"](https://chris.banes.dev/talks/2019/developing-themes-with-style-berlin/) talk given by him and Nick Butcher.

Once set up, override the shape attributes you wish to change in your base theme:

Material Design components will respond to theme-level shape overrides:

![UI components changing from baseline shape to cornerFamily=cut, with corner sizes 8 dp, 12 dp, 16 dp.](<https://lh3.googleusercontent.com/gnKKrcjEX9IBXFAMTn_9YMa-ECxn3FBHsw__UTqAowjlttLEBwY9kRp0eizEyiunRs12IzywO6u69zRa_qvZXgam8p-HMnDTW3hmJw>)

Material Design components responding to theme-level shape overrides

## MaterialShapeDrawable

Shape theming is powered by [`MaterialShapeDrawable`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/shape/MaterialShapeDrawable.java). It’s the default background drawable for all MDC widgets and handles rendering shape. Unlike other drawables, it’s not usable in XML and needs to be handled programmatically.

![Image](<https://lh3.googleusercontent.com/27Uy-sb7tyds8kgaooepHk1KcwizdC-UYZLqW_MqkYFjOFG4_Ijiymvyz-TsU33b1Ccw8_AbXSUaujrT9K5FRKK2iz5fqcyvn9Vj>)

MaterialShapeDrawable and ShapeAppearanceModel visualized

A `MaterialShapeDrawable` can be instantiated like so:

## ShapeAppearanceModel

[`ShapeAppearanceModel`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/shape/ShapeAppearanceModel.java)is the programmatic equivalent of a \`ShapeAppearance\` style and stores data regarding families and sizes of shape corners and edges. `MaterialShapeDrawable` uses this class to render its shape.

A builder pattern is used to instantiate a `ShapeAppearanceModel`:

For a more advanced example involving edges and custom paths, see [`BottomAppBarCutCornersTopEdge`](https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/bottomappbar/BottomAppBarCutCornersTopEdge.java) from the MDC catalog.

## Fill and stroke

`MaterialShapeDrawable` handles rendering of fill and stroke. A number of methods exist to adjust these properties:

## Elevation and overlays

`MaterialShapeDrawable` is responsible for rendering overlays to convey elevation in a dark theme. MDC widgets handle this by default. Here’s how this functionality is enabled and used:

Take a look at the [color theming](https://material.io/blog/android-material-theme-color) article as well as Chris Banes’ article on [dark theme](https://medium.com/androiddevelopers/dark-theme-with-mdc-4c6fc357d956) for more information.

## Shadow rendering

Elevation shadow rendering by the platform is only supported from API 21 onwards. `MaterialShapeDrawable` offers optional support to backport shadow rendering:

## Corner interpolation

`MaterialShapeDrawable` offers methods to interpolate the sizes of all corners. This is a \[0.0, 1.0\] multiplier on the set corner sizes and can be useful for animations and transitions.

## ShapeableImageView

Applying path clips to images is a common use case (eg. Circular avatars). To solve this MDC has a handy class called `ShapeableImageView`. As the name suggests this is an extension of `AppCompatImageView` that understands shape theming. It leverages familiar concepts—`ShapeableImageView` can read `shapeAppearance` and `shapeAppearanceOverlay`, and even supports attributes for applying strokes.

## Shape in MDC widgets

Earlier we said that MDC widgets respond to overrides of theme level shape attributes. But how would you know, for example, that a button uses `shapeAppearanceSmallComponent` as the style for its container? Let’s take a look at a few options.

## MDC developer docs

The MDC developer docs have recently been refreshed. As part of this we’ve included attribute tables which include design terminology and default values used in the library. For example, check out the “Anatomy and key properties” sections of the updated [buttons doc](https://material.io/develop/android/components/buttons/#contained-button).

![Image](<https://lh3.googleusercontent.com/Y9sF_I0nve-cuSFQ4DHPuJXZG994yrDDg-Aea8E7HmHqIYr7PJLpDe9cvpaWLBYl1TzYeHRkYD-kLb81ET0krvUwjsWGLwbN2mVdKw>)

MDC button dev doc attribute table with default shape values

## Source code

Inspecting the MDC source code is arguably the most reliable approach. MDC uses default styles to achieve Material Theming so it’s a good idea to look at these as well as any styleable attrs and the java file(s). For example, check out the [styles](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/res/values/styles.xml), [attrs](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/res/values/attrs.xml) and [java file](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/MaterialButton.java) for \`MaterialButton\`.

An interesting observation is how MDC widgets use default styles to ensure `MaterialShapeDrawable` is the default background. The general approach is:

-   Set `android:background` to `@null` or `@empty` in the widget default style

-   If no background is detected when parsing attributes, instantiate a `MaterialShapeDrawable` programmatically and set this as the background

-   If a background has been set (eg. in a layout or custom style) then respect this and do not use `MaterialShapeDrawable`


![Image](<https://lh3.googleusercontent.com/mNnyGGni-I24PhbGl5XH6VLYRlaPlIUDAPULyRcSRdVQkMJbbe_QHvGm-MCfPF1r--qOSwRsyrab91FCqJ0qwZhiQw5vU8J3ETV4WBg>)

MDC button default style with shape values

## Shape in custom views

Your app may include custom widgets you’ve built or gotten from an existing library. Making these views responsive to Material Theming is useful when using them alongside standard MDC widgets. Let’s take a look at what to keep in mind when supporting shape theming for custom widgets.

## Use MDC attrs in &lt;declare-styleable&gt;s and default styles

Allowing your custom views to be styled involves using a `&lt;declare-styleable&gt;`. Reusing attr names from MDC can be useful for consistency. Default styles that use `&lt;declare-styleable&gt;`s  can also reference MDC theme shape attrs for their values while also using the `@null`/`@empty` approach for `MaterialShapeDrawable` backgrounds:

## Keep elevation and overlays in mind

If you want your custom view to support elevation overlays or backported shadow rendering, it’s a good idea to override the `setElevation` method and pass the value to the `MaterialShapeDrawable` background:

## OK Google, what’s next?

We’ve been through the process of implementing color theming in your Android app using MDC. Be sure to check out our other posts in this series on [why we recommend using MDC](https://medium.com/androiddevelopers/we-recommend-material-design-components-81e6d165c2dd), [color theming](https://material.io/blog/android-material-theme-color), [type theming](https://material.io/blog/android-material-theme-type), [dark theme](https://medium.com/androiddevelopers/dark-theme-with-mdc-4c6fc357d956), and Material's [motion system](https://material.io/blog/android-material-motion).

As always, we encourage you to file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also be sure to check out our Android [companion example apps](https://github.com/material-components/material-components-android-examples).

* * *

If you’ve successfully implemented shape theming or if you’re having trouble doing so, leave a comment below or reach out to us on Twitter [@MaterialDesign](https://twitter.com/materialdesign) and [@AndroidDev](https://twitter.com/AndroidDev).
