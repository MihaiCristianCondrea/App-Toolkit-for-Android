---
title: "Building a Material Theme on Android: Typography"
source_url: "https://m3.material.io/blog/android-material-theme-type"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/93bc901d9394-QYmMEVMQnST3qpYC1r5OEFSdyvZDQHI-RawscL-YRf1eJaNkJeGoImxSfb1jmIIcHEjrv96pq-HOsUIf7EkKuT6yJMwegbDu.png"
  - "assets/images/7d7229222698-U91QcPXwd4vZy5mqgfKRmrq0glL82XeN-xPOqUzo_kbALtFkQpGoqh8eWUwNx3Injktu5M9NwBQwSOICAmGRthgvD7s0oeky.png"
  - "assets/images/0affc33bef6d-rnDV8xpr0i6fN_tEZTR-mrZij-SYvqglfBhyrsyjgbfs5VZmxkf0LM1CGH6dUSZ_ecmMHYSp8Wp5A-YnVgoa3kLhS3kKvUcS.png"
  - "assets/images/50f296a1d24e-9kGrGMUJPNYUO8H0bYGNE64YuP_GQSKBCV7bcjq8EfWjLhrGoY3-CeeGU-JNKpEOLG2tZOS4b0BDiAUMB9M0e2zPhZo9SGq8.png"
  - "assets/images/69e6b44be279-jZcVyxHpKXR6YpDok5dfjDmUDKr9PCq5eWoCQTXL6NpLbW7oXTJflgQHukq-e9Gt9dTEhjVX8pKbkQ4Fi22kGMxGFdlQ3Qru.png"
  - "assets/images/9a085a7986b2-XEbUmo28RCg7uD9xGXDBfRhYmnLmuXsr3JOhaNnFM2D3aAWSWgZhxiwWyZk7OGGKP16_QHes75C7v5jm4j29nh2AgMaSfPf6.png"
  - "assets/images/30f6739e3e3b-gVJklJ4z31ZhHhSoM2v4Ygzn9UZ1U3HPagBbdc1qMqV5XFI06PGjWsFoliXLFHS-3pPffwIT1utpJx0BglyjnAKfoizzO4q4.png"
  - "assets/images/b08238741194-vnGxmsUU83cC-oODzqEnx0zUmYUyJGugivWuMRZ-qrrtBZJB9jzc687Ghgj4Jdqzj0M4P-7KbDZ0Uq-_uikZ4x5H1jJXD569.png"
  - "assets/images/35bb501e2779-6f8oySsomvC-CdaCeRzHUleoflDzBWmcQEpUHHoDemEPu94bJV6TTCTzqKX4jecgt1hZOpbS2IEldm8kGemc8lL5ifTA3wdC.png"
---

# Building a Material Theme on Android: Typography

Type theming on Android using the MDC library

![Image](<https://lh3.googleusercontent.com/QYmMEVMQnST3qpYC1r5OEFSdyvZDQHI-RawscL-YRf1eJaNkJeGoImxSfb1jmIIcHEjrv96pq-HOsUIf7EkKuT6yJMwegbDuzHk1sw>)

Published: 2020-09-01T13:05:00

[Material Theming](https://material.io/design/material-theming/overview.html#material-theming) is a way to customize [Material Components](https://material.io/components) to align with your brand. A Material theme includes [color](https://material.io/design/color/), [typography](https://material.io/design/typography/) and [shape](https://material.io/design/shape/) parameters which you can adjust to get near-infinite variations of the components – all while maintaining their core anatomy and usability.

![Material button transforming from baseline style to show off different options for color, shape, typography, and label](<https://lh3.googleusercontent.com/U91QcPXwd4vZy5mqgfKRmrq0glL82XeN-xPOqUzo_kbALtFkQpGoqh8eWUwNx3Injktu5M9NwBQwSOICAmGRthgvD7s0oeky9CniMQ>)

On Android, Material Theming can be implemented using the [Material Components (MDC) library](https://github.com/material-components/material-components-android), from version `1.0.0` onwards. If you’re looking to migrate from the Design Support Library or MDC `1.0.0`, take a look at [our migration guide](https://medium.com/androiddevelopers/migrating-to-material-components-for-android-ec6757795351).

This article will be focusing on **type** theming.

## Type attributes

Material Design provides 13 type “styles” that are applied to all the text in your app. Each of these have a design term (eg. “Body 1”) along with a corresponding type attribute that can be overridden in your app theme (eg. `textAppearanceBody1`). There are default “baseline” values (text size, letter spacing, capitalization, etc.) for each style.

![MDC type attributes with examples from different text sizes](<https://lh3.googleusercontent.com/rnDV8xpr0i6fN_tEZTR-mrZij-SYvqglfBhyrsyjgbfs5VZmxkf0LM1CGH6dUSZ_ecmMHYSp8Wp5A-YnVgoa3kLhS3kKvUcSbInN>)

MDC type attributes with baseline styling

Material Components use these type attributes to style textual elements of the widgets (those parts that subclass or comprise one or more `TextView`s).

![Button color attributes: ?attr/colorOnPrimary for + icon and button text, ?attr/textAppearanceButton for button text, ?attr/shapeAppearanceSmallComponent for button shape, ?attr/colorPrimary for background color](<https://lh3.googleusercontent.com/9kGrGMUJPNYUO8H0bYGNE64YuP_GQSKBCV7bcjq8EfWjLhrGoY3-CeeGU-JNKpEOLG2tZOS4b0BDiAUMB9M0e2zPhZo9SGq8wxs72w>)

Type attributes used by a button

They are applied in layouts and widget styles like this, for example:

Take a look at Nick Butcher’s “[What’s your text’s appearance?](https://medium.com/androiddevelopers/whats-your-text-s-appearance-f3a1729192d)” article for more information on using this and the order of precedence with other styling techniques.

In the MDC themes these attributes map to styles, eg.

You might recognize `TextAppearance` styles from AppCompat or the platform and these are discussed in more detail in the **Type resources** section below. The corresponding attributes are new to MDC and give you the ability to vary typography by theme.

## Picking type

Figuring out which type styles to use and the values within them may be the responsibility of a designer, or derived from your product’s brand. However, it’s still useful to know about the role of each style and in which scenarios they should be used:

-   `textAppearanceHeadline*` styles are for headings
-   `textAppearanceSubtitle*` styles are for subheadings
-   `textAppearanceBody*` styles are for multiline body text
-   `textAppearanceButton` is for buttons but also maps to parts of other components like tabs and dialog actions
-   `textAppearanceCaption` is for smaller text like text field hints and errors
-   `textAppearanceOverline` is also for smaller text but capitalization and increased letter spacing make it suitable for small titles and labels like date picker headers

## Type tool

Material Design provides a useful tool for previewing type scales, integrating with [Google Fonts](https://fonts.google.com/) and exporting code. See "Type scale generator" in the [Material Design type scale guidelines](https://material.io/design/typography/the-type-system.html#type-scale).

![Image](<https://lh3.googleusercontent.com/jZcVyxHpKXR6YpDok5dfjDmUDKr9PCq5eWoCQTXL6NpLbW7oXTJflgQHukq-e9Gt9dTEhjVX8pKbkQ4Fi22kGMxGFdlQ3Qrusk6v4zo>)

Google Fonts (left) and Type scale generator (right)

## Type resources

Type resources consist of fonts as well as `TextAppearance` styles. Let’s take a look at what’s available on Android and a few things to keep in mind when declaring styles.

**XML and downloadable fonts**

Fonts are stored in the res/font directory and referenced with `@font/` notation. You can use either local XML fonts or downloadable fonts. Android Studio offers a built-in wizard to get you started with downloadable fonts including the necessary certificates and manifest metadata. You may also want to check out “[The Android Developer’s Guide to Better Typography](https://medium.com/google-design/the-android-developers-guide-to-better-typography-97e11bb0e261)” by Rod Sheeter for a more detailed guide and further optimizations to font preloading.

In general we recommend using downloadable fonts as they reduce the size of your app bundle by leveraging the shared font provider cache. However, downloadable fonts currently only work with those available on Google Fonts. If your app requires a purchased or proprietary font, use XML fonts.

It’s also worth mentioning that variable fonts are supported on Android from API 26 onwards. Take a look at Rebbeca Franks’ “[Variable Fonts in Android O 🖍](https://medium.com/over-engineering/variable-fonts-in-android-p-c5c918275646)” article for more information.

## TextAppearance styles

`TextAppearance` styles can be seen as the Android equivalent of Material Design type styles. For custom styles, we recommend two approaches to help separate concerns and create a single source of truth for type theming values in your app:

-   Store all `TextAppearance` styles in a single res/values/type.xml file
-   Use the MDC `TextAppearance` styles as parents and adhere to the same naming convention

Attributes and values you can use within these styles align with those supported by `TextView`:

-   `fontFamily` is the font family, typically an `@font/` resource referring to an XML or downloadable font
-   `android:textSize` is the size of the text, typically an `sp` dimension
-   `android:textColor` is the color of text
-   `android:letterSpacing` is the spacing between characters
-   `android:textAllCaps` is a boolean to toggle text capitalization
-   `android:textFontWeight` is the weight of the font, used to select the closest match from a font family, but is only available on API 28 and above. `android:textStyle` can also be used to apply transformations such as `bold` and `italic`.

## Calculating letterSpacing

Letter spacing values on Android tend to use a different unit of measurement (em) to those used in design tools like Sketch (tracking). The [Material Design typography guidelines](https://material.io/design/typography/the-type-system.html#type-scale) provide a relatively simple equation for determining suitable em values from tracking values:

**(Tracking from Sketch / font size in sp) = letter spacing**

## MaterialTextView and lineHeight

The platform `TextView` added support for the `android:lineHeight` attribute in API 28. MDC provides backported support for this via the `MaterialTextView` class. You don’t need to use this class directly in layouts as `&lt;TextView&gt;`s will get auto-inflated as `MaterialTextView`s by `MaterialComponentsViewInflater`.

You can use `lineHeight` in various scenarios:

-   Included as an item in `TextAppearance` styles (applied with `android:textAppearance="..."`)
-   Included as an item in widget styles with parent `Widget.MaterialComponents.TextView` (applied with `style=”...”`)
-   Applied directly to `&lt;Texview&gt;`s in layouts

![Image](<https://lh3.googleusercontent.com/XEbUmo28RCg7uD9xGXDBfRhYmnLmuXsr3JOhaNnFM2D3aAWSWgZhxiwWyZk7OGGKP16_QHes75C7v5jm4j29nh2AgMaSfPf6zRwI>)

Different lineHeight values

## Things to consider

-   You don’t have to override all type styles. However, bear in mind that default MDC styles use the system font (typically Roboto). Be sure to check which type styles your widgets and `TextView`s are using.
-   While `TextAppearance` does support `android:textColor`, MDC tends to separate concerns by specifying this separately in the main widget styles:

## Additional type styles

Your design system may call for additional type styles outside of the 13 that Material Theming specifies. Thankfully this is relatively easy to do on Android by declaring a type attr:

## Overriding type styles in an app theme

Let’s take a look at how you can add your chosen type styles to your app theme by overriding relevant attributes.

First, we recommend setting up your theme(s) to gracefully handle light and dark color palettes while reducing repetition with base themes. For more on this topic, take a look at Chris Banes’ article on [dark theme](https://medium.com/androiddevelopers/dark-theme-with-mdc-4c6fc357d956) as well as the "[Developing Themes with Style](https://chris.banes.dev/talks/2019/developing-themes-with-style-berlin/)" talk given by him and Nick Butcher.

Once set up, override the type attributes you wish to change in your base theme:

Material Components will respond to theme-level type overrides:

![UI components changing from baseline typography to fontFamily=roboto_mono and textAppearanceButton. textAllCaps=false.](<https://lh3.googleusercontent.com/gVJklJ4z31ZhHhSoM2v4Ygzn9UZ1U3HPagBbdc1qMqV5XFI06PGjWsFoliXLFHS-3pPffwIT1utpJx0BglyjnAKfoizzO4q4yJ517A>)

Material Design components responding to theme-level type overrides

## Type in MDC widgets

Earlier we said that MDC widgets respond to overrides of theme level type attributes. But how would you know, for example, that a button uses `textAppearanceButton` as the style for its text label? Let’s take a look at a few options.

## MDC developer docs

The MDC developer docs have recently been refreshed. As part of this we’ve included attribute tables which include design terminology and default values used in the library. For example, check out the “Anatomy and key properties” sections of the updated [buttons doc](https://material.io/develop/android/components/buttons/#contained-button).

![Image](<https://lh3.googleusercontent.com/vnGxmsUU83cC-oODzqEnx0zUmYUyJGugivWuMRZ-qrrtBZJB9jzc687Ghgj4Jdqzj0M4P-7KbDZ0Uq-_uikZ4x5H1jJXD569PfCm>)

MDC button dev doc attribute table with default type values

## Source code

Inspecting the MDC source code is arguably the most reliable approach. MDC uses default styles to achieve Material Theming so it’s a good idea to look at these as well as any styleable attrs and the java file(s). For example, check out the [styles](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/res/values/styles.xml), [attrs](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/res/values/attrs.xml) and [java file](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/button/MaterialButton.java) for `MaterialButton`.

![Image](<https://lh3.googleusercontent.com/6f8oySsomvC-CdaCeRzHUleoflDzBWmcQEpUHHoDemEPu94bJV6TTCTzqKX4jecgt1hZOpbS2IEldm8kGemc8lL5ifTA3wdC0R2eqw>)

MDC button default style with type values

## Type in custom views

Your app may include custom widgets you’ve built or gotten from an existing library. Making these views responsive to Material Theming is useful when using them alongside standard MDC widgets. Let’s take a look at what to keep in mind when supporting type theming for custom widgets.

## Use MDC attrs declare-styleables and default styles

Allowing your custom views to be styled involves using a `&lt;declare-styleable&gt;`. Reusing attr names from MDC can be useful for consistency. Default styles that use `&lt;declare-styleable&gt;`s  can also reference MDC theme type attrs for their values:

## OK Google, what’s next?

We’ve been through the process of implementing color theming in your Android app using MDC. Be sure to check out our other posts in this series on [why we recommend using MDC](https://medium.com/androiddevelopers/we-recommend-material-design-components-81e6d165c2dd), [color theming](https://material.io/blog/android-material-theme-color), [shape theming](https://material.io/blog/android-material-theme-shape), [dark theme](https://medium.com/androiddevelopers/dark-theme-with-mdc-4c6fc357d956), and Material's [motion system](https://material.io/blog/android-material-motion).

As always, we encourage you to file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also be sure to check out our [Android companion example apps](https://github.com/material-components/material-components-android-examples).

If you’ve successfully implemented type theming or if you’re having trouble doing so, reach out to us on Twitter [@MaterialDesign](https://twitter.com/materialdesign).
