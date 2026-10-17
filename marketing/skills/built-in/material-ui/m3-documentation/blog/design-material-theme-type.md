---
title: "Designing a Material Theme: Typography"
source_url: "https://m3.material.io/blog/design-material-theme-type"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/274c7fdc6642-n34a-EytCCSC3lu2skTYXmY1E6s_5oadqEcrQGxh5aYclnWcoqRfysXFtn2XOFuDQ-jeJ3euEoKSpe5ZBIzOXf_UYYcj8TWN.png"
  - "assets/images/9e6ba5d43ed6-xFpmz-s2DUM1AUDTxqbjox4xURmKtJIoGgzoqikg3UqoWLbEhcIlY6taH307449xx8pNeL0VgLYBmO_US4ATKeetbqqwz7W-.png"
  - "assets/images/9c28a3892877-kvaMoCi2jQWHPJhOryAadro9Y_F_jBjwJnUVeZ6iRuuy4lB0NOVCA9_dIOYL4fnH2YomezbPgSrUK7_bTfkuLSoWWBPmHbmi.png"
  - "assets/images/398f3a2f0c56-63wDIS3-GRtsF-K6amQhoqT4aHIxR0a1cdAMp7pO7l-ROOC4Ez8DmSN0knLI2eVxo9d93SBLFLjRpe5uLwTsekDlSoIVncb9.png"
  - "assets/images/05d362e8ac89-hZZg6Zlk5au6LgcXdZvIUe-bZMsxN8F5Mz8-eNywbcywVD-N7PQmPTzQz3z2muIb84k_H_6TFKSkxc1Rcg0qIeJGX2hPQxpl.png"
  - "assets/images/8176b269d907-9BdkyQQnR0prGBNQZ8EFOA6Xsd0uc49XYrZ1SPxMgZJUZoC5sfgnlXd9TMgOlMC0uKHS-y-4EsRKoO0HSDHrVanI2uZwf3qc.png"
  - "assets/images/b6a20745607e-3sysWyo02ySeZ-fIRVnrnnUvQqVDwzXW3ZTrJK22LzVuK6igWARmr3_Yy9QMUoHjHFMymQtBqTLY7j18s77X9uH5YaqarCjl.png"
  - "assets/images/36e37a6118b4-vImwY-bKxtxdWH3hA-xD2qqo2SW-aqQmekePR8WBAGjQQENEraoMEdUU1ZEAPYQlxPRvRkv1qMZK3kxvBiF4VPBFlMzycSjz.png"
  - "assets/images/4d4c7d1fb41f-nKWdzaiOwsglOgdEYkoxBBQsYcBEqs-ZkDOc6i6V4IrVXDDBwK5nNxxQIPZaCVSRwMQI6CchzUJi2aHip6dgfX4LcG_r7k5_.png"
  - "assets/images/8b59e60b123a-srg0KjLE-gYZEDjzkzhxH-7BOWpIyoHsDZxufO7FrHhzGt0rm3BCC6FR8WB-8LRpErIfw2lOwat_cs3PNxfUnNU-fJ7XTPta.png"
  - "assets/images/ac52d3d99cf1-uF1m2otNZxNEoDsGL9X-iGJ-LyhhguvaZkwZn8KtMvgFjYWKUipySYU1Lb7FWq4GvpdyRVQVPeBdoa4JVyNHZuUPwfU2K8K2.png"
---

# Designing a Material Theme: Typography

Tailoring your typography with Material Design and Figma

![Illustration depicting various elements of typography](<https://lh3.googleusercontent.com/n34a-EytCCSC3lu2skTYXmY1E6s_5oadqEcrQGxh5aYclnWcoqRfysXFtn2XOFuDQ-jeJ3euEoKSpe5ZBIzOXf_UYYcj8TWN9gO->)

Published: 2020-09-01T13:06:00

When reading a book, you aren’t likely to stop and think, “what an interesting font this book is using.” Well-executed typography can be invisible, serving as the scaffolding for communicating ideas, actions, instructions, stories, and feelings. But that doesn’t mean that the typography in your app should be purely utilitarian. Material Design’s typography system provides a structure that allows text styles in your app to communicate clearly and effectively, with enough room to express a unique brand, identity, and tone.

## Typography in Material

Material Design’s type system is centered on the [type scale](https://material.io/design/typography/the-type-system.html#type-scale). A type scale is a selection of font styles that can be reused across an app, providing enough stylistic flexibility to accommodate a range of purposes while remaining consistent and recognizable.

![Diagram of the baseline Material Design type scale](<https://lh3.googleusercontent.com/-xFpmz-s2DUM1AUDTxqbjox4xURmKtJIoGgzoqikg3UqoWLbEhcIlY6taH307449xx8pNeL0VgLYBmO_US4ATKeetbqqwz7W-j0lkw>)

The baseline Material type scale

The Material type scale includes a series of reusable styles from headlines to captions, as well as specialized styles for components like buttons.

![Animated diagram depicting how type styles can map to a card showing a weather forecast](<https://lh3.googleusercontent.com/kvaMoCi2jQWHPJhOryAadro9Y_F_jBjwJnUVeZ6iRuuy4lB0NOVCA9_dIOYL4fnH2YomezbPgSrUK7_bTfkuLSoWWBPmHbmilaY4>)

When customizing a type style, the change cascades down to all the components that use that style. Changing the “button” style, for example, will inform the typography on all components that use the button typography style – like dialogs, extended FABs, and tab labels.

Other styles are more flexible – for example, the type scale includes multiple headline styles to accommodate a rich information hierarchy and a range of options for presenting headline content. To see this in action, take a look at the Material Study [Fortnightly](https://material.io/design/material-studies/fortnightly.html), an example newspaper app built around real product and design constraints.

![Screen from Fortnightly app with hero image and article “Poverty to Empowerment in Chicago” by Betty Eghan](<https://lh3.googleusercontent.com/63wDIS3-GRtsF-K6amQhoqT4aHIxR0a1cdAMp7pO7l-ROOC4Ez8DmSN0knLI2eVxo9d93SBLFLjRpe5uLwTsekDlSoIVncb9tfCX>)

Fortnightly's front page mostly uses the app’s H4 headline style. The smaller headline style works there because there are many article headlines to parse, so space and attention are at a premium. But on the single article page above, for example, we see a more expressive H3 style. It’s larger, bolder, and more opinionated than its front page counterpart because the article has space enough for more vivid expression.

![Screen from Fortnightly app with a name highlighted in body text and a bottom navigation drawer displaying the person’s biography](<https://lh3.googleusercontent.com/hZZg6Zlk5au6LgcXdZvIUe-bZMsxN8F5Mz8-eNywbcywVD-N7PQmPTzQz3z2muIb84k_H_6TFKSkxc1Rcg0qIeJGX2hPQxplmRO9_ds>)

Having multiple body styles can come in handy when presenting content at similar sizes but with different purposes or meanings. In Fortnightly, the Body 1 style is used for article text, while Body 2 is used for content that supplements the text of the article with additional information.

![Diagram showing Fortnightly's custom type scale](<https://lh3.googleusercontent.com/9BdkyQQnR0prGBNQZ8EFOA6Xsd0uc49XYrZ1SPxMgZJUZoC5sfgnlXd9TMgOlMC0uKHS-y-4EsRKoO0HSDHrVanI2uZwf3qcLxAjpw>)

Fortnightly's custom type scale

You might notice that Fortnightly uses a combination of two fonts across its type scale. [Libre Franklin](https://fonts.google.com/specimen/Libre+Franklin) and [Merriweather](https://fonts.google.com/specimen/Merriweather) are present across Headline and Body styles. While the baseline type scale is based on Roboto, adding a second typeface can create additional levels of distinction. Pairing fonts this way can help users understand what type of content they’re seeing across an app and segment that information more easily.

In the example above, the text “US – Poverty” is set in Libre Franklin, making it distinct from the Merriweather text surrounding it. This distinction helps the user recognize that it is secondary to article content and navigational in nature, rather than a caption or part of the headline.

## Creating your type theme

**Starting from scratch**

Without established brand typography for your app, you might be starting your type theme from scratch. Similar to creating your color theme from scratch, a good starting point is to think about the nature and tone of your app. Specifically, think about the content the app is presenting and how it contributes to the app’s personality and voice. In the other [Material Studies](https://material.io/design/material-studies/about-our-material-studies.html), you can see a variety of approaches to typography based on content and personality.

![Screens from the Reply app showing different header articles, layouts and type styles](<https://lh3.googleusercontent.com/3sysWyo02ySeZ-fIRVnrnnUvQqVDwzXW3ZTrJK22LzVuK6igWARmr3_Yy9QMUoHjHFMymQtBqTLY7j18s77X9uH5YaqarCjlWkwzxg>)

Is your app task-based like the email app [Reply](https://material.io/design/material-studies/reply.html), aiming to get users through their inbox as efficiently as possible? Consider a type scale that optimizes for readable typefaces, using multiple styles from a single type family to present content consistently without distraction.

![Screens from the Fortnightly app showing different header articles, layouts and type styles](<https://lh3.googleusercontent.com/vImwY-bKxtxdWH3hA-xD2qqo2SW-aqQmekePR8WBAGjQQENEraoMEdUU1ZEAPYQlxPRvRkv1qMZK3kxvBiF4VPBFlMzycSjzcEBKbw>)

Does your app have an editorial opinion to express, like the news app [Fortnightly](https://material.io/design/material-studies/fortnightly.html)? Consider pairing highly readable typefaces with more expressive typefaces for key moments and content in the app.

![Screens from theOwl app showing different header articles, layouts and type styles](<https://lh3.googleusercontent.com/nKWdzaiOwsglOgdEYkoxBBQsYcBEqs-ZkDOc6i6V4IrVXDDBwK5nNxxQIPZaCVSRwMQI6CchzUJi2aHip6dgfX4LcG_r7k5_uP6o>)

If your app has very strong visual expression through color and imagery, like the educational app [Owl](https://material.io/design/material-studies/owl.html), consider using a readable typeface that maintains some playfulness. Owl uses heavier weights of Rubik across the type scale, mimicking shapes from its logo while remaining simple and predictable.

![Screens from the Shrine app showing different header articles, layouts and type styles](<https://lh3.googleusercontent.com/srg0KjLE-gYZEDjzkzhxH-7BOWpIyoHsDZxufO7FrHhzGt0rm3BCC6FR8WB-8LRpErIfw2lOwat_cs3PNxfUnNU-fJ7XTPta0RYg4A>)

If your app’s voice and content seek to convey elegance and modernity, like the shopping app [Shrine](https://material.io/design/material-studies/shrine.html), consider a type scale that focuses primarily on lighter weights at larger sizes, while ensuring readability for body, caption, and subtitle styles.

When looking for typefaces to use in your theme, consider starting with [Google Fonts](http://fonts.google.com). The catalog has thousands of free fonts to use, and developers can easily pull from the catalog in code.

**Type pairing techniques**

If you’ve found a typeface that works for your app, but want to expand your type scale with another typeface, one technique for pairing fonts is to think of them as existing on a color wheel.

Try finding a typeface that has characteristics adjacent or related to your first typeface (for instance similar terminal shapes or letter forms), or a typeface that contrasts those characteristics (for instance pairing serif and sans serif fonts, or a human, expressive font with something more rationalized).

## Create your type scale

If you’re using Google Fonts, you can quickly create a type scale with Material using the [type tool](https://material.io/design/typography/the-type-system.html#type-scale), embedded below. Just select your chosen typefaces in the panel on the right. (You can type in the name of the typeface to autocomplete.)

**If you already have brand typefaces**

If you already have brand typefaces, then you may already have a branded type scale. If this is the case, compare your existing type scale to the typographic styles used in Material Design, and select styles from your type scale that can map to the stylistic slots available.

If you have branded typography but don’t have a type scale yet, explore the styles available in your brand typefaces using the techniques outlined above to create a type scale that customizes the baseline scale with your chosen styles.

## Visualizing your theme

Once you have a type scale, it’s time to visualize your new type theme in a design environment. To start, make a copy of our [Baseline Design Kit for Figma](https://www.figma.com/@materialdesign). On the Material Theme page within the kit, you’ll see a frame called Typography which gives a comprehensive look at your type scale.

![Type scale in the Material baseline design kit in Figma showing edit styles for Headline 1 set to Roboto](<https://lh3.googleusercontent.com/uF1m2otNZxNEoDsGL9X-iGJ-LyhhguvaZkwZn8KtMvgFjYWKUipySYU1Lb7FWq4GvpdyRVQVPeBdoa4JVyNHZuUPwfU2K8K20LLs>)

On the right side of the screen, you’ll see a panel that contains global styles matching the styles in your theme under Text Styles. To start plugging new text styles into your theme, click the edit icon next to each style. Refer back to your type scale in the type tool on material.io and copy each value in the tool into the appropriate styles in Figma.

## What’s next?

Once your styles are added to Figma, you’ll see them propagate through every component on the Stickersheet page. From there, you can start applying your new styles to content in your app, experimenting with the range of styles afforded by the type scale to see what works best. You can learn more about techniques for setting text in your app in our [typography guidance](https://material.io/design/typography/understanding-typography.html#type-properties).
