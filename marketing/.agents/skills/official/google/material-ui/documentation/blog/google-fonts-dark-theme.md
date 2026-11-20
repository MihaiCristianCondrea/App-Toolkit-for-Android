---
title: "Building a Dark Theme for Google Fonts"
source_url: "https://m3.material.io/blog/google-fonts-dark-theme"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/56d326f05b52-XyYL1gtZmsH8yqGNcybRTDjeFw9cwez-3m5GL0dQF2zUW5yolzoq7j7mXtNigUMD91YmGVMpM3sfSUypLK3n9n2BzJr7vB4l.png"
  - "assets/images/239854ca03dd-hCrqDkj_XwfIRMp0MsZqxy61dlIIIizBTCF4WJmKBo3L_5CT9qQTZ4seg-mZEBDQda-_V2p_fY-KqD-8D5_te78o9gjJAqBz.png"
  - "assets/images/85e90cbdd4f5-iTiNCaHTGGTCPtmMB5ABF6PhYk_ujzvDIVkL1eqYjmIOsrWiGXmdb-lIkf7Z-r2V9mKw5-k6bGKMRlsgPnQHwI-Th7u04k80.png"
---

# Building a Dark Theme for Google Fonts

How Google Fonts implemented a dark theme in its Angular App

![illustration of the Google Fonts homepage](<https://lh3.googleusercontent.com/XyYL1gtZmsH8yqGNcybRTDjeFw9cwez-3m5GL0dQF2zUW5yolzoq7j7mXtNigUMD91YmGVMpM3sfSUypLK3n9n2BzJr7vB4lvkT1eg>)

Published: 2021-02-22T13:00:00

Our latest Google Fonts catalog feature is... a new dark theme! Using the [Material dark theme guidelines](https://material.io/design/color/dark-theme.html), we were able to turn off the lights on the Google Fonts catalog while maintaining both core functionality and the Google Fonts brand.

Besides offering a fresh aesthetic, a dark theme can help users avoid eye strain in a low lighting environment. For the Fonts team, the dark theme feature takes on another important meaning, because we offer ways for users to compare and evaluate font selections. With dark themes becoming more popular, we wanted to showcase how fonts may look in a dark themed environment.

We even have fonts that are specialized for dark themes, like [Signika Negative](https://fonts.google.com/specimen/Signika+Negative). This alternative version of Signika is optimized for situations where light text on a dark background generally seems thicker, such as light emitting displays from computer screens to airport signage panels.

## Designing the dark from the light

As we all know, color plays an important role in a dark theme. A dark color scheme is layered on top of an existing UI to maintain visibility and legibility of text and other visual elements.

The first step was selecting a darker background color. In our case, dark grey was the color of choice. The Material [dark theme guidelines](https://material.io/design/color/dark-theme.html) recommend using dark grey, which can express a wider range of colors, elevations and depths in comparison to pure black.

Next, we modified the primary color to work better with the dark background. In the light theme, we use a blue from Google’s brand palette as our primary color across the pages and components. Instead of applying the same blue in the dark theme, we selected a desaturated version to meet the accessibility requirements and to avoid a sense of visual vibration that can come from using intensely saturated colors on dark backgrounds.

We also adjusted text and icon colors, using 3 different shades of grey depending on the level of emphasis and hierarchy. We use lighter greys for primary text and navigational icons, and a darker grey for secondary text.

![Screenshot of Google Fonts website in light and dark themes](<https://lh3.googleusercontent.com/hCrqDkj_XwfIRMp0MsZqxy61dlIIIizBTCF4WJmKBo3L_5CT9qQTZ4seg-mZEBDQda-_V2p_fY-KqD-8D5_te78o9gjJAqBzcqiBuw>)

Lastly, we added a different treatment for the card component. In light theme, shadows are used to express elevation on hovered, focused and pressed states. But when we applied the same default shadow values for the dark theme, the shadows did not show enough contrast and visual cues for different element states on the dark background. So we added a lighter overlay to the dark themed card components to preserve the same visual expression and experience as the light theme. This also helps with the components’ legibility and ensures elevation expression that’s distinguable from the dark background.

## Implementing the dark from the light

#### Angular Material theming and Angular View Encapsulation

There is already quite some information about the [Angular Material theming system](https://material.angular.io/guide/theming), which comes with several pre-built theme CSS files and includes the possibility to define a custom theme by creating your own theme file. By doing so, only the Angular Material components will be styled accordingly, not your custom components. To [style your own components](https://material.angular.io/guide/theming-your-components) with Angular Material, you can create mixins that take your `$theme` as an input parameter and define all theme-related styles there. The rest of the styles used by your own components can be included via `styleUrl` in the corresponding TS files to keep them scoped.

But what should you do if many of your custom components are used only once, and you want to keep all of the related styles scoped? Or, what if in addition to `$primary`, `$accent` and `$warn` color palettes that can be defined in an Angular Material theme, you want to use additional palettes or one-off colors? These were questions we faced when implementing a dark theme on Google Fonts, and [the benefits](https://blog.angular-university.io/angular-host-context/) of using [Angular View Encapsulation](https://angular.io/guide/view-encapsulation) quickly became obvious: no accidental class collisions, easier maintenance, and shorter, simpler class names.

### Setting up the app

For the Google Fonts catalog, we have a file with SCSS variables that define a curated palette of color-related semantics, for example `$app-primary-palette`, `$app-secondary-palette`, `$app-accent-palette`, `$app-warn-palette` and `$app-background`. Our custom theme definition uses those variables and we theme Angular Material components by creating mixins. Our custom components import this color palette file in their corresponding scoped SCSS files. It means that we can simply change our color-related values in one scss file and it will be changed in the whole app consistently.

#### Introducing dark theme throughout the codebase

So, how did we add a dark theme to such a setup? The challenge is that there is no 1:1 mapping between the light theme and the dark theme. Here’s how we tackled it:

1.  We created a theme toggle component and a theme toggle service that adds `.gf-dark-theme` class to the body element.
2.  We created an SCSS file with dark-theme-related color variables, similar to the one we already had.
3.  We defined a custom angular dark theme using the `mat-dark-theme` Angular Material function and included it inside the `.gf-dark-theme` class
    `.gf-dark-theme {`
       `@include mat-dark-theme(...);`
       `}`
4.  We added dark theme styles to the theme mixins when needed, by using the `$theme` config parameter.
    `$is-dark-theme: map-get($color-config, is-dark);`
5.  In custom scoped components, we used `:host-context(.gf-dark-theme)` to style them when needed.

Finally, we made our existing visual regression tests parameterized, to be able to run them for both light and dark themes. For that we used the [parameterized runner](https://github.com/junit-team/junit4/wiki/parameterized-tests), a JUnit 4 feature. Visual regression tests validate the correctness of the UI changes introduced by a new code by comparing the old and new screenshots of our rendered elements. By adding a theme parameter to them, the exact same set of tests is executed twice – once when UI elements are rendered in the light theme and once – in the dark theme.

## See it in action

![GIF showing Google Fonts dark theme toggling on and off](<https://lh3.googleusercontent.com/iTiNCaHTGGTCPtmMB5ABF6PhYk_ujzvDIVkL1eqYjmIOsrWiGXmdb-lIkf7Z-r2V9mKw5--k6bGKMRlsgPnQHwI-Th7u04k80-y26g>)

No need to wait any longer: the dark theme for [Google Fonts catalog](https://fonts.google.com) is already available! Check it out and share your feedback with us on Twitter [@GoogleFonts](https://twitter.com/googlefonts?lang=en), via email (fonts@google.com) or via the feedback feature in the bottom left corner of our app. We hope you enjoy it! 🌚

And one last thing for developers – if you are implementing a dark theme in your own application, there’s a novel digital technique to enhance your typography: try finessing variable fonts that have weight or grade axes. Learn more over about this over on [CSS-Tricks](https://css-tricks.com/using-css-custom-properties-to-adjust-variable-font-weights-in-dark-mode/).
