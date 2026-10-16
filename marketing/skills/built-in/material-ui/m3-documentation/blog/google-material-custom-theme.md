---
title: "How Google created a custom Material theme"
source_url: "https://m3.material.io/blog/google-material-custom-theme"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/6956bb8eeb86-ZxnXueIhFlAFYyCid-0_IOTlpsw9ws2FWcqkn_kEiaz91wqM4PuKAWxYoodZel_00d42dl1G9i7d1eZcb0Epx2DhXj7huNsy.png"
  - "assets/images/955b08f05474-ff7qFXB5nhnCK2aydRfnL2r3ea5x71pHNah2mIYaTVbFJ0_-QTSzgiYqy8Pzo0rnUShK_qCWNzOzHHgOtyQuVXO4iAiHlOsC.png"
  - "assets/images/97919177c7b5-lJcnHKAVNYQVxPPct5_L1XFch3X_sdUsxMZWLy4t4puVWjzvWxmdaF2-c6XLlHtpe3HrK36JTBBaZXqEZbJ84iZk_I8Lq8Od.png"
  - "assets/videos/f89801e86002-Gmail-Mocks_1Up-Responsive01_1x.mp4"
  - "assets/images/2a210d399580-ZyJ8t-wHntyxP7vb-ePrgbtEtbvKL1Qp52HQ7JcrNmNXpJcQt8JMNWrUKEbrHI0ewcVfd4GzUugo3A0usimNNqsMWL4vNaYS.png"
  - "assets/images/183649331e41-V30qe06R0H-0kLn1_gTlNyByb1ZuxdXX2_e_ffsoEqzzwIjZm3cD9VVtmW0tUJEo4gIpG9BTVkdgcZ4rzjfU4A8xGhj9oCqd.png"
  - "assets/images/22545151ff0d-cMtELx51P7CaaN3TFORcocQWk1GmxoZISWzSr5r1wzWky6UsD5xunGjZxULL0m6D_BkJxZHLwPeSTBdxZWznQeg4ZKEB1mcg.png"
  - "assets/images/3fd57bd3883d-gNbA7g_oEuuYf0hNMs1DlIEUD4mL2OAVn_Bk68KCsPrfgwUMkWjI_6KcDcQ_n8hMspM_vo5t_x4HGLgtJvk72he20rHabYpb.png"
  - "assets/images/edc5383a76e0-_-D5FrTFa8tYszb9QXl98ctdujT7jCDClDLZ96u2LTem5vcEx58QKqHknoX5erjxaue4U3zmkYrp3lGK0aOgyNOrO9i6RtGl.png"
  - "assets/images/4c7a4fef00d5-HBsEKUoY9TB6sLYConEHfAsPpHVt_tp-6EUsppClQOz9v_swW0aje3J4fu01UHdRdbFHrchDkxa6_wCZDRgrDR6S8cQ1Tuqd.png"
  - "assets/images/8e047493b5b0-VrEHiGorOJDYUY5OcZ5Dqha3w31HkAFD76UOI4CjXROcvQk1V7SBWxSpJEfzt_7XeKaYUnaFsxqjVbJ9_u4AkxRVZPDkH-An.png"
  - "assets/images/eeccc7c515f1-sO0WlHvJOFQ4Xio3yAUf4-VzwpMmLqjc5kKia_sZ0jOX7e8B8vAodiNTjAY5-RPxNreS2tQ2tk6NEZd239NClxhsZAx7-EDh.png"
  - "assets/videos/8c3a74b67f7c-Home-Mocks_OOBE.mp4"
---

# How Google created a custom Material theme

Learn how Gmail, Google News, Google Pay, and Google Home use Material Design

![Image](<https://lh3.googleusercontent.com/ZxnXueIhFlAFYyCid-0_IOTlpsw9ws2FWcqkn_kEiaz91wqM4PuKAWxYoodZel_00d42dl1G9i7d1eZcb0Epx2DhXj7huNsyPvct>)

Published: 2018-05-09T13:00:00

What makes a product look and feel unique? And how do you communicate that identity to the people on the other side of the screen. At Google, we make hundreds of digital products under the same colorful, quirky logo. And while Google’s brand has evolved and changed over the years, design remains a common thread tying together our varied constellation of apps, services, and technologies.

With the launch of [Material Theming](https://material.io/design/material-theming/overview.html) – our end-to-end capability that allows teams to easily and systematically express a brand’s unique identity – design details, like type, shape, and color can be baked into custom Material themes and applied easily across a digital experience. We’ve built the Google Material Theme to express what makes Google uniquely Google. Details like the use of open space, the four Google colors, and our custom typeface, Google Sans, work together to convey a familiarity and trust to our users. Our theme gives Google apps a new, modern interface built with all the best practices of Material Design, but customized and adapted so that every part of the UI works together to serve that product’s function.

A number of product teams, including Gmail, Google News, Google Pay, and Google Home, helped define and test-drive the Google Material Theme. The redesigned interfaces for all four products launched recently, and we’ll be rolling out even more refreshed Google experiences in the coming months. Anyone can use Material Theming to make their product stand out, from a small startup to a large company with a diverse set of apps. Learn from our experience developing and applying the Google Material Theme to better understand how you can use themed components to create a shared, expressive brand experience that’s flexible and uniquely yours.

## Gmail: Using color to focus on content

![Gmail components including buttons, extended FAB, lists, bottom app bar, search, chips, and left navigation](<https://lh3.googleusercontent.com/ff7qFXB5nhnCK2aydRfnL2r3ea5x71pHNah2mIYaTVbFJ0_-QTSzgiYqy8Pzo0rnUShK_qCWNzOzHHgOtyQuVXO4iAiHlOsCvyaEWg>)

“Gmail is a challenging product to design because there are so many users – 1.4 billion – and such a breadth of ways that people use it,” says Gmail’s lead designer, Jeroen Jillissen. “It’s a productivity tool, and you never want to break people’s workflow.” Gmail’s design team approached the app’s [recent redesign](https://www.blog.google/products/gmail/stay-composed-heres-quick-rundown-new-gmail/) with a clear goal: keep the focus on content. The Google Material Theme helps support user attention and productivity, as people tailor their email experience through density, labels, and inbox organization.

Color proved to be a key technique in directing a user’s attention to important messages and actions in the refreshed interface. New emails turn the inbox label an eye-catching red, while intelligent features like nudge and smart reply appear in shades of orange and blue respectively. The new extended FAB sits prominently at the top of the navigation drawer, to focus attention toward one of the product’s primary user actions: composing new emails. The use of [elevation](https://material.io/design/environment/elevation.html) and a [responsive layout grid](https://material.io/design/layout/responsive-layout-grid.html) also contribute to the revamped user experience.

![Gmail desktop screen for composing email](<https://lh3.googleusercontent.com/lJcnHKAVNYQVxPPct5_L1XFch3X_sdUsxMZWLy4t4puVWjzvWxmdaF2-c6XLlHtpe3HrK36JTBBaZXqEZbJ84iZk_I8Lq8OdujsR>)

Gmail’s conversation view includes a new, more limited range of Material elevation, using a shadow to underscore the open reply box. The reply box’s send button showcases another colorful touch – using a filled background color to highlight the main action on the page. Together, both treatments help users focus on important, action-oriented aspects of the UI.

[Video: Updated guidance on the responsive layout grid allowed the designers to pare down layers of legacy features while introducing multiple panes of content in a collapsible, left-side navigation, as well as a new right-hand panel providing quick access to Calendar, Tasks, and other multi-tasking features.](https://kstatic.googleusercontent.com/files/38dc0abce694215ec178600e778c8152996b5fcf672ff06c6ba6d246ec3ebf87eeb082c5a4db686e8e9453edd032750962bbb0e1428acda62c9685ff7463e2e1)

## Google News: Designing an AI-powered experience

![Google News logo and components including list, cards, bottom app bar, and left navigation drawer](<https://lh3.googleusercontent.com/ZyJ8t-wHntyxP7vb-ePrgbtEtbvKL1Qp52HQ7JcrNmNXpJcQt8JMNWrUKEbrHI0ewcVfd4GzUugo3A0usimNNqsMWL4vNaYSDLFvcA>)

“Machine learning is not a replacement for design, it has to operate in service to the overall experience,” says Google News lead designer Tae Wan An. “The experience itself must communicate the benefits of AI in the product.” For the creators of Google News – which uses artificial intelligence to curate journalism from around the globe – that means relying on visual clarity and content hierarchy to help introduce new concepts to the user.

Typography plays an important role in visually supporting the app’s information hierarchy, drawing the eye to important headlines. Material’s typographic scale allows for 12 different type styles, optimized for legibility and systematically applied throughout an interface from button labels to body copy. Google Sans is the display font of the Google Material Theme, and is used to feature important headline text across all of Google News’ platforms. The body text for articles is displayed in platform-native typefaces to maintain visual continuity on a specific device or operating system. That means reading articles in [Roboto](https://fonts.google.com/specimen/Roboto) on Android and the web, and in San Francisco on iOS.

![Google News mobile and desktop screens with list of top stories](<https://lh3.googleusercontent.com/V30qe06R0H-0kLn1_gTlNyByb1ZuxdXX2_e_ffsoEqzzwIjZm3cD9VVtmW0tUJEo4gIpG9BTVkdgcZ4rzjfU4A8xGhj9oCqddkGgHA>)

Using a single typeface for every headline works well on mobile, where the user only sees a few articles at a time. But on the web – where more headlines are visible at once – this proves visually overwhelming. Material type guidance suggests creating a type scale of 12 fonts and text sizes to use throughout a UI. The designers of Google News were able to use their type scale’s additional headline styles to create more variation on the web while still supporting a consistent visual hierarchy across platforms.

![Google News mobile screen with Headlines selected in the bottom app bar](<https://lh3.googleusercontent.com/cMtELx51P7CaaN3TFORcocQWk1GmxoZISWzSr5r1wzWky6UsD5xunGjZxULL0m6D_BkJxZHLwPeSTBdxZWznQeg4ZKEB1mcgE9XA>)

Newscasts are a Google News feature where users swipe through AI-curated articles, videos, and insights displayed on a series of cards. These cards have a raised elevation to highlight their importance and draw a user’s attention.

## Google Pay: Using shape to express friendliness

![Google Pay logo and components including bottom app bar, buttons, cards, and lists](<https://lh3.googleusercontent.com/gNbA7g_oEuuYf0hNMs1DlIEUD4mL2OAVn_Bk68KCsPrfgwUMkWjI_6KcDcQ_n8hMspM_vo5t_x4HGLgtJvk72he20rHabYpbFjAVcw>)

“Users might be nervous about interacting with digital payments, so anything we can do to make it feel approachable is something we’re conscious about,” says Google Pay’s lead designer Seth Hamlin. The concept of making digital payments is still relatively new, so the team wanted to use playful imagery and friendly rounded shapes to communicate an approachable vibe. At the same time, the recognizable details of the Google Material Theme signal familiarity and help generate trust.

The Google Pay app also uses shape in an [expressive way](https://material.io/design/shape/shape-as-expression.html#expressing-brand) – echoing the playfulness of the Google logo through round-cornered cards and circular avatars. Custom illustrations draw focus and communicate core concepts in a clear way.

![Google Pay app showing home screen and examples of rounded cards displaying lists](<https://lh3.googleusercontent.com/_-D5FrTFa8tYszb9QXl98ctdujT7jCDClDLZ96u2LTem5vcEx58QKqHknoX5erjxaue4U3zmkYrp3lGK0aOgyNOrO9i6RtGlaqpS>)

The home screen uses rounded cards to display relevant information to users at varying stages of onboarding – offering helpful hints on how to make Google Pay more useful. Users can tap the cards for more details or swipe them away.

![Google Pay screen showing details for an American Express card](<https://lh3.googleusercontent.com/HBsEKUoY9TB6sLYConEHfAsPpHVt_tp-6EUsppClQOz9v_swW0aje3J4fu01UHdRdbFHrchDkxa6_wCZDRgrDR6S8cQ1TuqdjtsT7Q>)

Google Pay uses elevation to “lift” its round-cornered cards, visually echoing the look of a credit card resting on a white surface. When completing a transaction with a payment terminal, the digital cards transform through motion to underscore the action of payment.

## Google Home: Creating a delightful onboarding experience

![Google Home logo, illustrations, and list component](<https://lh3.googleusercontent.com/VrEHiGorOJDYUY5OcZ5Dqha3w31HkAFD76UOI4CjXROcvQk1V7SBWxSpJEfzt_7XeKaYUnaFsxqjVbJ9_u4AkxRVZPDkH-An6PYSSg>)

“Our challenge was to avoid an overly cloying and optimistic tone of ‘awesome, everything is done’ throughout the onboarding flow,” says Triona Butler, lead designer for Google Home. “There are many steps a user has to go through before they reach a success state, like playing a song on their device.”

Onboarding is an important part of any app, but for Google Home, it’s unmissable. Users must move step-by-step through the process of connecting to their Wi-Fi network and setting up their device in order for the experience to work. Google Home’s design team helped make the onboarding a delightful, visually engaging experience with colors and animations aligned to the Google Material Theme. Because the eye is naturally drawn to motion and vibrant hues, these animations proved to be an effective technique for encouraging users to make it all the way through device setup.

[Video: The onboarding animations use Google colors to guide attention and create a coherent flow. Abstract metaphors help communicate complicated technical steps in a user-friendly way. Balanced imagery means success, motion cycles mean processing, and collapsed ‘blocks’ indicate a failure state.](https://kstatic.googleusercontent.com/files/85c3f547fe3c7767c94674394c1997a7bfdefa15b4f2cfceb0f6c0a0d1867fd8a5c2092acf27e327a04987b7e4593d48004f26f01a2b33bd801d740c0f698cd3)

![Google Home app screen for adding a home](<https://lh3.googleusercontent.com/sO0WlHvJOFQ4Xio3yAUf4-VzwpMmLqjc5kKia_sZ0jOX7e8B8vAodiNTjAY5-RPxNreS2tQ2tk6NEZd239NClxhsZAx7-EDhU29lig>)

Several screens require people to select an item, like choosing your favorite music provider or a Wi-Fi network. The design team didn’t want to litter these screens with checkboxes, but they still needed to communicate tappability and action. The solution was to use a visual language of color and iconography to indicate selection – the icons transform into a check mark and change to blue, showing advancement through the flow.
