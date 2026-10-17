---
title: "Designing a Material Theme: Color"
source_url: "https://m3.material.io/blog/design-material-theme-color"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/f7529fd099e8-JYnQiXTLxC5hupKlxKhpe-MqayvYo6jlQqo-a7avSxlYeBfeGXu5xPR-o02RlNNxEKPiOSia-oR9OFk2hIe-iEJkEo-18g33.png"
  - "assets/images/dac64c284e98-qPIgbhKubYWR16dQrxTyAy4ZAJ-d3D9rBA-AjbhD5XvwbQwJE3mUG9hDru1tkPLhi-YqxYtbiGJwNkkYgDcCBgnzIDKN4dmF.png"
  - "assets/images/a192275ef2d4-EF44xt9wKpOK_zlRaDzD4DlHdvwKK0cIAdZFplsnqxaKZm4o1uN2twcAUHIB_JYArrJyhn0fOZ0iG9Cc_N-xuki3z5WtyGm5.png"
  - "assets/images/789b541569a9-1VQgOPxvnllToKQuGkrj24pk0dyoMeteT4_nW2l2kv0rlVGl7GV6GxxPHQNCF-7WOgWWjyGzJ4Lx5Ro5z3jIOvCD_4_gcVoQ.png"
  - "assets/images/a7f0b030e7a6-f5AhiCZp50k7HocQqV4OH9DxOiBOKg2eNvT2KP4GICHpvk3OlaTBp8Eg7W3HytmYLOCmV45W2ps8hqrxHD7ax4BC1HnWJzUC.png"
  - "assets/images/3d10a20c384c-cAHzNGtcaJoRQu9eXuGPZb0s-DiuRC6kBhtcZ18nLNT7Z-C2XfLCstoE3BgZo2pdY6VW22g-7MMTlzfKFX8_fy2DAl3sVd_P.png"
  - "assets/images/1c62aee8a522-7r40YOeAX6PjWMM03J_8IEyg0QlFDKEciks1_SZ2SJ_okJYH_mOwIXYGC0pJsb5veeSoeukTURA7qn0DeG9KuVEOPHBmHyhc.png"
  - "assets/images/69bac5f30a4c-SXf-wfa51Q0uZ6MptE0jsadSMgTgaznYPstjh6aTYiQoSNTvx8zDgRfISvx9ho7KGlkLIQ8AzdP425nzasi2KWrZymz3ds94.png"
  - "assets/images/e8ee4bb5a362-LVV912brVZvc0xEKeKWZwaOhgboHAX9rOWKOGoZODSf4m067Hg_Kkbp9VieB8JOm0eGeFKYlQfDQts7rQx6PstQCIcRgZ74T.png"
---

# Designing a Material Theme: Color

Crafting the perfect palette with Material Design and Figma

![Image](<https://lh3.googleusercontent.com/JYnQiXTLxC5hupKlxKhpe-MqayvYo6jlQqo-a7avSxlYeBfeGXu5xPR-o02RlNNxEKPiOSia-oR9OFk2hIe-iEJkEo--18g33hEQ5Q>)

Published: 2020-09-01T13:08:00

Color is one of the clearest methods of expression in design, particularly when it comes to expressing your brand or product’s identity across all the places people encounter it. An app’s interface is rich ground for using color to not only create an identity, but – because the user is directly touching and manipulating the interface – express that identity in a way that connects with someone’s life and experience and reinforces the function, utility, and personality of your product.

Material Design includes a comprehensive color system that allows designers and developers to make decisions at a global level, and have those color decisions cascade through their product, taking care of things like states, emphasis, and contrast.

## Color in Material

To accomplish all of that, Material uses a defined set of color slots which comprise a “color theme.” The color theme has slots for primary and secondary colors, which influence key components, as well as slots for a custom background color, surface color (for elevated surfaces or “sheets”), and error color.

![Diagram of Material color theme displaying the baseline Material color theme](<https://lh3.googleusercontent.com/qPIgbhKubYWR16dQrxTyAy4ZAJ-d3D9rBA-AjbhD5XvwbQwJE3mUG9hDru1tkPLhi-YqxYtbiGJwNkkYgDcCBgnzIDKN4dmFi3J2qzk>)

The baseline Material color theme

There are also several slots for “On” colors, named for the fact that they are colors that appear “on” top of other elements. On Primary, for example, is used for text, icons, or other elements that appear on top of the app’s primary color. To visualize this, picture an app bar with a menu icon and title. Both use an “On” color to ensure that appropriate contrast and readability are maintained.

![Screen with purple top navigation containing an expandable menu icon, Page title, favorite icon, search icon, and more icons](<https://lh3.googleusercontent.com/EF44xt9wKpOK_zlRaDzD4DlHdvwKK0cIAdZFplsnqxaKZm4o1uN2twcAUHIB_JYArrJyhn0fOZ0iG9Cc_N-xuki3z5WtyGm5ENIB>)

For more nuanced color applications like states, Material uses “tonal palettes.” A tonal palette provides a range of ten values based on the colors you choose, providing lighter and darker options that make your color theme more flexible.

![Screen with color scheme using primary purple 500, secondary purple 700, and tonal purple 200](<https://lh3.googleusercontent.com/1VQgOPxvnllToKQuGkrj24pk0dyoMeteT4_nW2l2kv0rlVGl7GV6GxxPHQNCF-7WOgWWjyGzJ4Lx5Ro5z3jIOvCD_4_gcVoQCzT38g>)

This UI uses a primary color and two primary variants, mapped across all components

## Selecting brand colors

**Starting from scratch**

If you’re building a color theme from scratch – that is, if you don’t have an existing brand palette – a good starting point would be to think about the nature of your app. In our [Material Studies](https://material.io/design/material-studies/about-our-material-studies.html) (example apps designed around real world use cases and product constraints), you can see a variety of approaches to branding with color, based on each app’s function and personality.

Is your app a utility that helps people efficiently parse information like the dark-hued personal finance app Rally? Is it a modern and sophisticated shopping app that inspires users with its refined aesthetic and pink tones, like Shrine? Or maybe it’s an education app like Owl that seeks to make education and learning a fun experience, with a fun palette to match. In each of these cases, color plays an important role in creating and reinforcing the app’s personality.

![Color scheme for the Rally app, mapping to different UI components](<https://lh3.googleusercontent.com/f5AhiCZp50k7HocQqV4OH9DxOiBOKg2eNvT2KP4GICHpvk3OlaTBp8Eg7W3HytmYLOCmV45W2ps8hqrxHD7ax4BC1HnWJzUCo9W4-g>)

Rally uses two colors from its color theme for each infographic on screen

For an app like [Rally](https://material.io/design/material-studies/rally.html) that’s all about seeing and managing personal finance data, it’s important to take a reserved approach to color. The app’s primary color is green. Rally’s On color is white, and the background color is a dark grey tone. Other colors are strictly reserved for the practical presentation of data on a variety of dashboard screens.

![3 screens for the Owl app showing three themes with primary colors for theme 1, yellow 500, theme 2, blue 700, theme 3, pink 500](<https://lh3.googleusercontent.com/cAHzNGtcaJoRQu9eXuGPZb0s-DiuRC6kBhtcZ18nLNT7Z-C2XfLCstoE3BgZo2pdY6VW22g-7MMTlzfKFX8_fy2DAl3sVd_PEXO0yg>)

Owl uses three color themes to distinguish different parts of the app

[Owl](https://material.io/design/material-studies/owl.html), on the other end of the spectrum, creates a fun and playful environment by using three distinct primary colors in its color theme. Despite the fact that Owl could be seen as having three different color themes in the same app, they’re all tied together by vivid, bright colors that mimic the primary colors of the color wheel - yellow, red, and blue.

![Color scheme for the Shrine app with primary pink 100, secondary pink 50 and highlight brown 900](<https://lh3.googleusercontent.com/7r40YOeAX6PjWMM03J_8IEyg0QlFDKEciks1_SZ2SJ_okJYH_mOwIXYGC0pJsb5veeSoeukTURA7qn0DeG9KuVEOPHBmHyhcFHiYeg>)

Shrine applies a simple color theme to key components and typography

[Shrine](https://material.io/design/material-studies/shrine.html) falls somewhere in the middle. The app has a distinctly opinionated take on color, using creamy pink hues for primary and secondary colors, and a deep brown On color. While Shrine’s color palette is distinct, its usage in the app is focused and refined, which reflects the personality Shrine wants to portray as a modern and elegant place for fashion and lifestyle shopping.

When making your own theme from scratch, think about where your app may fall on the spectrum created by these examples. Start out by choosing a primary color that you feel best expresses the personality and purpose of your app. Then, choose a secondary color that either heightens or moderates those qualities, depending on how you want the app to feel. Think about how you might use On colors, or apply your new colors to surfaces or backgrounds to reinforce the expression.

**If you already have a brand palette**

When building a color theme for your app, you may already have a brand color palette that can easily be made into a Material color theme. If this is the case, you may already have brand guidelines covering which colors to prioritize and how to conceptualize the personality or tone of the brand. You can combine this information as described above to decide which colors to include in your color theme.

If you have a brand palette but no guidance, take a look through existing branded materials or interfaces and compare to the palette to determine which colors should be prioritized or thought of as primary and secondary. [Test out](https://material.io/tools/color) these colors to determine whether they can support accessible contrast ratios. If not, you can select a variation from the tonal palette that does. 

**Expand your palette with the palette generator**

To do that, check out [the palette generator](https://material.io/design/color/the-color-system.html#tools-for-picking-colors) embedded below. Just add your chosen colors on the right, and click each generated color swatch to copy its hex code.

## Visualizing your theme

Once you’ve chosen some colors and expanded them into [tonal palettes](https://material.io/design/color/the-color-system.html#tools-for-picking-colors), you’re ready to actually create the color theme by plugging colors into the slots Material Design provides.

To do this in a design environment (and get an instant preview of your theme on Material Components), make a copy of our [Baseline Design Kit for Figma](https://www.figma.com/@materialdesign). On the Material Theme page within the kit, you’ll see a frame called Color which gives a comprehensive look at your color theme, including tonal palettes.

![Material baseline design kit in Figma showing color palettes](<https://lh3.googleusercontent.com/SXf-wfa51Q0uZ6MptE0jsadSMgTgaznYPstjh6aTYiQoSNTvx8zDgRfISvx9ho7KGlkLIQ8AzdP425nzasi2KWrZymz3ds94ZDNv66A>)

On the right side of the screen, you’ll see a panel that contains global styles matching the colors in your theme under Color Styles. To start plugging colors into your theme, click the edit icon next to each color style. Go back over to your tonal palettes in the generator and click each swatch in the tool to copy its hex code, entering them into the appropriate styles in Figma. 

![Material baseline design kit in Figma with right nav editing style for Primary color to FDF2EG](<https://lh3.googleusercontent.com/LVV912brVZvc0xEKeKWZwaOhgboHAX9rOWKOGoZODSf4m067Hg_Kkbp9VieB8JOm0eGeFKYlQfDQts7rQx6PstQCIcRgZ74TqK3DU6c>)

Once your primary and secondary swatches are taken care of, take a look at the Stickersheet page in Figma to see how your theme looks on each component.

## What’s next?

From here, you can also experiment with surface, background, and error colors to refine your new color theme. You can also try getting even more creative with the color system by creating multiple themes like the Owl example above.

Once you’ve come up with the perfect color theme, you may be ready to implement. For guidance on that, check out Nick’s post on [implementing a Material color theme](https://material.io/blog/android-material-theme-color).
