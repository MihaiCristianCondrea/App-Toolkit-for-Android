---
title: "Principles and Techniques for Effective Localization"
source_url: "https://m3.material.io/blog/localization-principles-techniques"
lastmod: "2024-12-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/68c15071f532-JyAlIoZ7WIdN6N61L5CMHMWcMmKzO51edjGN1oiNHMRaXhlpZlpXEXPI8lXnhX8MLSD_WDvKQ0_W-veWXqk3iNZNSYWEuBZ5.png"
  - "assets/images/73796e5e54f7-2c32JY8yN6CPHad12GIjOyicpSZJF_zEdjpu2YN6gCr8aIi_u72a-r8CsaPEG9C-LlED3IZduosC7zWeKwnzDQ6belonZ9sl.png"
  - "assets/images/01d4263b0031-ppomeRpFhsjEKU2tadCYE_TM0kcetR1CuoiLBFLXd744xqUGScmcM_Iz_PLJF0pEZaSLvFZb4yqfEOnHcB2OqwQv_EeDUCBk.png"
  - "assets/images/466ef5137ae2-HGq7Z9Gjyj3Sh44lxpi_ZTAfk0Nqo9XgrTCoDlSS7hIrQYWtBSUba3XfmBbp143nFCXy7lg1iV-XkyTC6ruRauaBLgX5CfPw.png"
  - "assets/images/fad2c00f9c92-9oYgU8K4qB_dN9BzIBhEP1HhpW1K_XD5V1648h1PZ-C5mWr54mLrdcyvuNfZCZDtktFixdben3q-B8qoon7ayIhCP4y3JEZ-.png"
  - "assets/images/c6da7a620ad9-h-JG_Z38wxYLrKQ4jhJ-Tm7vmsFv4zLcs5UhpfEysikULJqYhuubaVuAZmnSXxNyH_NWvxVIQkw5yj36Ic4tv5NbMMgEL63h.png"
  - "assets/images/37dd5d76ab18-PkUNbE2Gtxc6xEILVQGRGCtwELhTIJx8DKV5a_ZLm-kJHC8EN08En3lqTkJnyvJ2jJfaDXKmiJ1p2BQCvGRAEIpWK8f-6FPo.png"
  - "assets/images/73c7e835b95a-G7N0Dntib78CqzLKcq0L4iyKPqVaV4dl827NPr_JtJvrOJEQmj8h0Hb_3nYYQsYU0qx-BDJD8iDkIw2I-yODlE-1CBMjlxBG.png"
  - "assets/images/d8eee79d8fd2-WChAxtWkiiOW10vR7AaP5WPyh9nBrTUeDCknCP8HEVg2Yj4v4n5aHpQAVTVQ8d8bWFLed0hAAo_3k7uHCj1HM27gF1uqv3_b.png"
  - "assets/images/ab426013ca67-5EZBwjROoA5MBrpwkxIzOOp6HSasji0g45byIROc7rOziSuve1hPkAoRwtSCG11BQmwoSpwfNwM9wQuNLXmtcSeXYV-pugop.png"
  - "assets/images/75fe362440e7-zgRec7Z9t7Kxs2fxUKe2604zYFAM38I2gEzpm6kNsOp_3e1BZXQngWck6iPREIoo0R2GYwjdKI6E2gtCIMOQXAfDpZNT8yZU.png"
  - "assets/images/e6afb29e706c-zVyfuwg8pl8uGXorg9te6J5fmn9eGBcaBC24EV4xAt4pR_O5uY8Bj4N-3Y5Oq17SYeWb9DsOUCKo4Gaf4bZLrJyYFjFdeRl_.png"
  - "assets/images/c90696519587-EigP05Ra3PUo30vDwbmlzcAvSpc8FMf-Rskm-2krDFcqpY4UTEPgox1iLbwJzJAOars4fJRpv9skJ1Q3Jq0RiIqeN2YKDBrn.png"
  - "assets/images/0ea67e580200-ttq1s7H521wkNAtkgCI7NQTyd34sYIJqfXyeUount8zdm9BapvCGTeKIr4Q02trFOlgckBXcOKjrim7tdAqszO060wOZpjUg.png"
---

# Principles and Techniques for Effective Localization

Going beyond translation to create more inclusive experiences

![Image](<https://lh3.googleusercontent.com/JyAlIoZ7WIdN6N61L5CMHMWcMmKzO51edjGN1oiNHMRaXhlpZlpXEXPI8lXnhX8MLSD_WDvKQ0_W-veWXqk3iNZNSYWEuBZ5EvuiluCDWis8yLC0qNBG>)

Published: 2021-08-30T09:00:00

Localization is the process of adapting messages, imagery, brand voice, features, and products to achieve a linguistic, cultural, and geographic fit for a given audience. The goal is to make user experiences for everyone as comfortable as possible across different cultural and regional contexts. It is important to consider localization if your product will be used globally, since a poorly or insufficiently localized product or content could create confusion.

The process of localization is often misunderstood to mean translation, but in fact localization deals with much more than word-by-word translation of text and content. Translation strives for a linguistic equivalent, while localization broadly considers adaptation for a market or cultural context.

Localization is also different from [internationalization](https://developers.google.com/international), a practice and procedure for applications to support different local conventions, such as time, date and phone number formats, currency, measurement, and other data.

This post introduces principles and techniques to ensure that your products and experiences will work well across different contexts. Get to know the considerations and best practices for adapting content, imagery, and more.

## Design flexible and concise messages

Expect messages to shrink or expand after translation and localization. For some languages, length can expand by 30% or might even double when rendered in a different language. For others, the localized text may be shorter but take up more vertical space. Lengthy text can get cut off, overlap, and lead to an overall poor experience.

To avoid text being cut off or overlapping, consider keeping messages concise while designing an interface that’s flexible enough to accommodate different languages.

_Learn more: For help with flexible size constraints, see our [guidance on component behavior](https://material.io/design/layout/component-behavior.html) and [making apps compatible with different devices](https://medium.com/google-design/to-make-apps-accessible-make-them-compatible-with-different-devices-11298c6d3f06). For ideas on how to make your app as easy-to-understand as possible, read about [improving comprehension through intuitive actions](https://medium.com/google-design/improving-comprehension-through-intuitive-actions-f7e6336e12e6)._ 

If you already know the languages that your product will be translated into, it helps to design for the longest possible translation of a given text. Consider using [Google Translate](http://translate.google.com) for a quick test of translated text length, or consult a localization specialist.

Leave open space around condensed UI components, such as buttons and tabs. Consider building extra room or a buffer into your design to accommodate the overflow of text. For longer text, establish a component’s maximum width that allows lengthier passages to wrap.

![Image](<https://lh3.googleusercontent.com/2c32JY8yN6CPHad12GIjOyicpSZJF_zEdjpu2YN6gCr8aIi_u72a-r8CsaPEG9C-LlED3IZduosC7zWeKwnzDQ6belonZ9slh0C9J50mtqx3IBewt3be>)

The height of the “Accept terms and conditions” button in English (1) is only one-line, and is increased to fit two lines of text in Malayalam (2).

![Image](<https://lh3.googleusercontent.com/ppomeRpFhsjEKU2tadCYE_TM0kcetR1CuoiLBFLXd744xqUGScmcM_Iz_PLJF0pEZaSLvFZb4yqfEOnHcB2OqwQv_EeDUCBkzXGMabqhWdPE7S4uzltm>)

The text in Bulgarian (2) is cut off after being translated from English (1).

## Ensure that language and imagery are inclusive

Whenever possible, edit any jargon, slang, or language that’s used by a narrow or specialized group.

When designing for many cultures, use imagery and avatars that display various cultures, backgrounds, ethnicities, and demographic groups.

Avoid imagery and avatars that represent a single culture, gender, or demographic characteristic.

![Image with text 'Last active users', a search bar and 20 diverse avatars. Each avatar contains a small dot in the lower right corner indicating their activity status.](<https://lh3.googleusercontent.com/HGq7Z9Gjyj3Sh44lxpi_ZTAfk0Nqo9XgrTCoDlSS7hIrQYWtBSUba3XfmBbp143nFCXy7lg1iV-XkyTC6ruRauaBLgX5CfPw3_3zfqYclEjiNmzSpg>)

Use diverse avatars in imagery

![Image with text 'Last active users' , a search bar and 20 avatars who appear to be white. Each avatar contains a small dot in the lower right corner indicating their activity status.](<https://lh3.googleusercontent.com/9oYgU8K4qB_dN9BzIBhEP1HhpW1K_XD5V1648h1PZ-C5mWr54mLrdcyvuNfZCZDtktFixdben3q-B8qoon7ayIhCP4y3JEZ-LxHhP6OxThuk0M3Xw6c>)

Use avatars and images of just one gender or race.

_Learn more: For help with communicating with international users, see our guidance on [writing for a global audience](https://medium.com/google-design/writing-for-global-audiences-d339d23e9612)._

#### Icons and emoji

Although icons and emoji are widely used for applications and websites, not all visual elements are interpreted in the same way across cultures. An icon or emoji might have a positive impact in one culture, but could be offensive in another.

To ensure that the icons you use are understandable for your audience, consult a localization specialist if available, or someone who can serve as a subject matter expert for the target culture or market, such as a local user, an in-market support team, or a translator.

To make text with emoji appropriate for a local context, you may need to move emoji around in the UI, add explanatory text, or change the icon or emoji to one that’s better suited for the target region.

For example, in the United States the icons that look like a flame or fire 🔥 might connote “trending” or “exciting.” However, fire iconography as a positive value isn’t universally understood. For a Polish audience, the up arrow is a more appropriate way to signal “trending.” It will aid overall comprehension if the trending icon is paired with the word “trending.”

When words are replaced with many emoji in a single line, it increases the difficulty for someone from a different culture with a different mental model to interpret the meaning. The translation effort becomes more challenging as well. As a result, users may get something that deviates from the original intention of the emoji. Additionally, multiple emoji in a single line can appear cluttered and visually distracting.

![Photos of cat overlaid with text and open trunk of station wagon](<https://lh3.googleusercontent.com/h-JG_Z38wxYLrKQ4jhJ-Tm7vmsFv4zLcs5UhpfEysikULJqYhuubaVuAZmnSXxNyH_NWvxVIQkw5yj36Ic4tv5NbMMgEL63h0wyO_gSVrVh-fE5wSw>)

By combining the flame icon with “Trending Posts,” the symbol is more likely to be understood.

![Photos of cat overlaid with text and open trunk of station wagon](<https://lh3.googleusercontent.com/PkUNbE2Gtxc6xEILVQGRGCtwELhTIJx8DKV5a_ZLm-kJHC8EN08En3lqTkJnyvJ2jJfaDXKmiJ1p2BQCvGRAEIpWK8f-6FPoX5FQ4RlMs3jp8aPJ_IA>)

Replace words in a sentence with emoji.

#### Fonts

Choosing the right typography can impact how your product text appears in different languages.

System fonts usually support a limited range of weights. However, if your chosen font doesn’t support various scripts, text in those scripts may be rendered differently depending upon the operating system and platform. The system default fonts also vary across operating systems. Chinese, Japanese, and Korean (CJK) system fonts (the typefaces that are pre-installed on computers and devices) typically use different font weights when compared to Latin characters.

It’s recommended to use [Google’s Noto fonts](https://www.google.com/get/noto/) if your product uses CJK or other scripts. Noto’s name comes from the phrase “no more tofu,” because the fonts eliminate the tofu-like boxes (𛲢𛲡𛲠) for missing glyphs that appear when a font is not available for a user’s text. [Noto CJK](https://www.google.com/get/noto/help/cjk/), for example, provides full coverage for CJK characters in four scripts with seven weights: Simplified Chinese, Traditional Chinese, Japanese, and Korean.

_Learn more about [Noto fonts](https://blog.google/outreach-initiatives/accessibility/preserving-endangered-languages-noto-fonts/)._ 

![Google in Latin text and Japanese characters](<https://lh3.googleusercontent.com/G7N0Dntib78CqzLKcq0L4iyKPqVaV4dl827NPr_JtJvrOJEQmj8h0Hb_3nYYQsYU0qx-BDJD8iDkIw2I-yODlE-1CBMjlxBGmivBBhTI_FMGoVRZIJ0_>)

With Noto fonts, both the Latin and Japanese text have the same weight.

![Google in Latin text and Japanese characters](<https://lh3.googleusercontent.com/WChAxtWkiiOW10vR7AaP5WPyh9nBrTUeDCknCP8HEVg2Yj4v4n5aHpQAVTVQ8d8bWFLed0hAAo_3k7uHCj1HM27gF1uqv3_bHHA9c9m_fH1ep63L8Q>)

Avoid mixing font weights in the same sentence. The Latin type used for “Google” is rendered in a thin font weight, while the Japanese characters are rendered in a bolder weight.

#### Line breaks

Line breaks can appear incorrectly in languages that do not use spaces between words, such as many CJK languages.

[Budou](https://github.com/google/budou) is an open-source tool that automatically creates line breaks for CJK languages, which improves text readability.

![dialog in Japanese on top of photos and text in Japanese](<https://lh3.googleusercontent.com/5EZBwjROoA5MBrpwkxIzOOp6HSasji0g45byIROc7rOziSuve1hPkAoRwtSCG11BQmwoSpwfNwM9wQuNLXmtcSeXYV-pugop75GbsSJy1Pv2eayjQR0>)

Budou makes correct line breaks in Japanese. The first line says “Chatter Privacy” and the second line says “Policy.”

![dialog in Japanese on top of photos and text in Japanese](<https://lh3.googleusercontent.com/zgRec7Z9t7Kxs2fxUKe2604zYFAM38I2gEzpm6kNsOp_3e1BZXQngWck6iPREIoo0R2GYwjdKI6E2gtCIMOQXAfDpZNT8yZUm9_LULpwfzEAxidtyA>)

Don’t break words. The Japanese word for “policy” is split in half across the line break.

_Learn more: [Language enablement line breaks](https://w3c.github.io/typography/#line_breaking) and [approaches to line breaks](https://www.w3.org/International/articles/typography/linebreak.en#table)._ 

## Account for local design practices

#### Color

Color choice can directly influence an individual’s brand perception, While some colors are interpreted across many cultures with relative consistency, others convey a wide variety of symbolic meanings across cultures.

Learn more about applying color in the [Material Design color guidelines](https://material.io/design/color/the-color-system.html#color-usage-and-palettes).

#### Information density

Preferences for information density and design styles vary across regions and cultures. Density and style preferences can be explored with help from a localization specialist or expert in the culture. Within the same product or brand, designs may be changed to better serve audiences with different backgrounds or needs.

![9 photos with text in Japanese](<https://lh3.googleusercontent.com/zVyfuwg8pl8uGXorg9te6J5fmn9eGBcaBC24EV4xAt4pR_O5uY8Bj4N-3Y5Oq17SYeWb9DsOUCKo4Gaf4bZLrJyYFjFdeRl_v9f2UtL3ez5Up2GlP1s>)

This page in Japanese uses a dense UI,small photos, and a text menu on the right side of the page.

![6 photos with Trending posts title at the top](<https://lh3.googleusercontent.com/EigP05Ra3PUo30vDwbmlzcAvSpc8FMf-Rskm-2krDFcqpY4UTEPgox1iLbwJzJAOars4fJRpv9skJ1Q3Jq0RiIqeN2YKDBrn8N_V3F_F9xHlQBTcJw>)

The same car page in English uses medium-sized photos, no text, and a menu icon for more information.

#### Bidirectionality

Languages such as Arabic, Hebrew, and Farsi are read from right-to-left (RTL). If you’re designing for left-to-right languages as the default, take time to ensure that your design is optimally mirrored for RTL languages. Learn more about [bidirectionality](https://material.io/design/usability/bidirectionality.html#mirroring-layout).

#### Data formats

Different regions have varying conventions for data formats, including addresses, phone numbers, names, calendars, measurements, currencies, payment methods, and more.

![contact form with name, address](<https://lh3.googleusercontent.com/ttq1s7H521wkNAtkgCI7NQTyd34sYIJqfXyeUount8zdm9BapvCGTeKIr4Q02trFOlgckBXcOKjrim7tdAqszO060wOZpjUgZZn50NaBfTs000ftzOc>)

Canadian and French address forms are formatted differently to conform with regional norms.

Learn more:

-   [CLDR](http://cldr.unicode.org/) (Unicode Common Locale Data Repository): The international standard for automatic formatting of numbers, currency, and more.
-   Material Design [Data Formats](https://material.io/design/communication/data-formats.html#date-and-time) guidance

## Create and share guidance or visual references

If you work with a translator or localization team, creating and sharing documentation around your goals will empower their work. Some examples of helpful documentation include:

-   Product goals, intent, and details
-   User personas and demographic features
-   User journeys
-   Target markets
-   Brand voice guidelines
-   Tone and style considerations
-   Design mocks or sketches at any stage
-   How much freedom the translator can have in adapting your content. (You may consider [transcreation](https://borntobeglobal.com/2020/06/02/transcreation/), a process to rewrite messages to fit the target culture.)

## Don’t forget to test!

Standard research and testing, such as functional testing done by a professional tester and native speaker, will help reveal technical and functional errors in localized content.

To learn if your product, content, or design is effective and enjoyable across locales, test localized mocks with a small group of your potential users. Ask them to walk through a user journey and learn from their struggles or successes with task completion. Testing provides a better understanding of how likely your product or content resonates with a region or culture, and where any potential risks lie.

Even after launching a product, it’s best to continue testing or user interviews to gather feedback as the product evolves. Distill those findings and insights and incorporate them into the next iteration of the product and content. Establish a cycle of research and embrace regional and cultural inclusion when designing and writing for international audiences.
