---
title: "Designing a Material Theme: Shape"
source_url: "https://m3.material.io/blog/design-material-theme-shape"
lastmod: "2024-06-04"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/cc6476f2351b-eDkSmsI2fH-LaxjLooxE4US4WlJ2RYul60PfZhunjsMlzTnp24gQv6_7u0D0Zzi9QGGWzf5x9xdr8H6pxFw3E6sXRKWntSKd.png"
  - "assets/images/8a80a3637a61-svJ4gR0Gwp_kcmdEJqpV3iZTIlNAdzRIqJDMKftCk-RI_k8PqDM6g9dZ5enKgQREdHxXnE77Jh-vQJmdu8GFG3vd1yoyAr4G.png"
  - "assets/images/1993a3ba366a-WcQwxd_ga2M5AuWK0jIhZRYQcAYnbmYgw2jf4wviVl22IIUnX2F98b8FeMdLzmaCqi33fbe00F-EBVwdwkAhTOeiG7-VplHU.png"
  - "assets/images/005836f07157-_vqi2auEgDaEYP8D7CtLeqrED53qA5NRiXiuD6-oT9vPEFQwEXUSlL2pYBQvRuddSzClPQu2WHgWgfycddVObU3i6IRkYrur.png"
  - "assets/images/5026f817cedc-uD1MPmDKKNhbsryU05FrtMp2SMT-MQLiW6qtDIVerrNseseUhfww4IlsPyIoyWwyR60l4Dz44VKwLosQUkA6jviQ7_4fVpRV.png"
  - "assets/images/9174fd40895b-E68r4iOW7KMBIODWuEqDwi7vDAaeX4ph4ly-cxjUV0Bl-_lXARgZA74fSanqBjbkqKv7ZL1j_QFemIKVDQ1qRV25xrEbmlcl.png"
  - "assets/images/de07da1f672e-XO8SXbVc8ab0v5ZJX-jOT-XIB4iaMPmk5HzSliz9Rchm5orIsGYbOORj-PMZBQoiS6SDIotn5WhkY4qhcDcb4-Ysp0HSp9xV.png"
  - "assets/images/abfa0ecc3c1d-R9_2KknIkD0kmFakfmticDs2sVV6Q4geuMY0512vJQSXintAh6PaIAsAUp2AdK44VcATD6bXHZcfbBwF5zyvHy0nqjyDHd18.png"
---

# Designing a Material Theme: Shape

Rounded or angled? Choosing the right shape family with Material Design and Figma

![Image](<https://lh3.googleusercontent.com/eDkSmsI2fH-LaxjLooxE4US4WlJ2RYul60PfZhunjsMlzTnp24gQv6_7u0D0Zzi9QGGWzf5x9xdr8H6pxFw3E6sXRKWntSKd4bEC>)

Published: 2020-09-01T13:04:00

Shape, like color and typography, can be a powerful tool for creating a beautiful, usable interface. Unlike color and typography, shape is often subtle. Everything in an interface has a shape, sure, but the ways in which shape can work to guide users, suggest interaction, and visually distinguish elements on screen is often unnoticed.

![Android screen with bottom app bar and two cards containing images, title and secondary text](<https://lh3.googleusercontent.com/svJ4gR0Gwp_kcmdEJqpV3iZTIlNAdzRIqJDMKftCk-RI_k8PqDM6g9dZ5enKgQREdHxXnE77Jh-vQJmdu8GFG3vd1yoyAr4GwSuRzOg>)

Intentional use of shape can create visual hierarchy among components, separating content from key actions like the FAB.

![Backdrop pattern used to access the menu in the Shrine app](<https://lh3.googleusercontent.com/WcQwxd_ga2M5AuWK0jIhZRYQcAYnbmYgw2jf4wviVl22IIUnX2F98b8FeMdLzmaCqi33fbe00F-EBVwdwkAhTOeiG7-VplHUVuaQ0A>)

Shape can also suggest interactivity, hinting with an asymmetric corner that some affordance is present.

![Shrine logo and Shrine app screen. Tapping the logo reveals the backdrop](<https://lh3.googleusercontent.com/_vqi2auEgDaEYP8D7CtLeqrED53qA5NRiXiuD6-oT9vPEFQwEXUSlL2pYBQvRuddSzClPQu2WHgWgfycddVObU3i6IRkYrurtnh4-w>)

And, of course, shape can be another tool in your kit for creating an experience that feels like it belongs to your brand, elevating the expression of your app’s unique identity with an extra custom touch.

Material Theming introduced a wide variety of possibilities for shaping elements in your UI and, like the other systems we’ve covered in similar guides, shape can be defined and controlled at a global level, creating a comprehensive shape theme that automatically sets the appearance of every Material Component in your app.

## Shape in Material

Material Design supports two primary shape families by default: rounded and angled. Once a shape family is selected, the size of the corner shape can be adjusted for shape expressions that are subtle or dramatic.

![9 shapes illustrating the range of shape options from very rounded to slightly rounded, sharp, and cut](<https://lh3.googleusercontent.com/uD1MPmDKKNhbsryU05FrtMp2SMT-MQLiW6qtDIVerrNseseUhfww4IlsPyIoyWwyR60l4Dz44VKwLosQUkA6jviQ7_4fVpRVGpzq8w>)

Material Components are divided into three categories: small, medium, and large.

Components in the “small” category include things like buttons, chips, floating action buttons, snackbars, and tooltips. Components in the “medium” category include cards, dialogs, image list items, and menus. And components like the backdrop, modal navigation drawer, and sheets belong to the “large” category.

Grouping components this way allows for the creation of a shape theme that’s comprehensive (applied to all components) but thoughtful (applied differently based on the size and prominence of each component). Optically, applying a 4dp rounded corner to a button and a nav drawer would end up feeling very different. For the button, which is 40dp tall, one rounded corner represents 10% of its total height. For a nav drawer that spans the entire vertical space of the screen, this is 0.6% of the total height or less. So treating different sized components with different shape styles allows your app to have an optically balanced and sensible approach to shape, in addition to leaving space for shape to act as a tool in the ways described above.

## Creating your shape theme

When deciding which shape family to use for your app, you can look to other elements of the brand for inspiration. A good starting point is the logo.

![Owl app logo and components including the top app bar, list, buttons, and cards](<https://lh3.googleusercontent.com/E68r4iOW7KMBIODWuEqDwi7vDAaeX4ph4ly-cxjUV0Bl-_lXARgZA74fSanqBjbkqKv7ZL1j_QFemIKVDQ1qRV25xrEbmlclwToEog>)

The [Material Studies](https://material.io/design/material-studies/about-our-material-studies.html) (example apps designed around real world use cases and product constraints) often borrow shape inspiration from each app’s logo. The roundness of [Owl](https://material.io/design/material-studies/owl.html)’s bird logo, for example, informs corner treatments for selected cards and the round playlist button in the bottom right corner of some screens.

![Shrine app logo and components including app bars, list, buttons, cards, and chips](<https://lh3.googleusercontent.com/XO8SXbVc8ab0v5ZJX-jOT-XIB4iaMPmk5HzSliz9Rchm5orIsGYbOORj-PMZBQoiS6SDIotn5WhkY4qhcDcb4-Ysp0HSp9xVdYvq>)

As we saw before, the angular forms of [Shrine](https://material.io/design/material-studies/shrine.html)’s diamond logo influences the shape of everything from buttons to backdrops.

Your product’s typography can also serve as inspiration. For example, if you’re using a typeface with rounded corners or terminals, the shape family you choose can either amplify or contrast that geometry, depending on your preferred expression.

To get a feel for how each shape family might look, check out [the shape tool](https://material.io/design/shape/about-shape.html#shape-customization-tool) from the Material Design guidance on shape. The tool will give you a feel for how different shape families and values will impact Material Components, allowing you to make informed decisions about your shape theme before visualizing it in Figma or in code.

## Visualizing your theme

To visualize your new shape theme in a design environment, it will be useful to refer to the component groupings described in [the shape guidance on material.io](https://material.io/design/shape/applying-shape-to-ui.html#shape-scheme), applying the desired shapes and values to your components as needed.

To get started with a full Material Design stickersheet, make a copy of our [Baseline Design Kit for Figma](https://www.figma.com/@materialdesign). On the Components page, you’ll find the main components that serve as parents for all the Material Components shown on the Stickersheet page.

![Material baseline design kit in Figma showing button components and editing module](<https://lh3.googleusercontent.com/R9_2KknIkD0kmFakfmticDs2sVV6Q4geuMY0512vJQSXintAh6PaIAsAUp2AdK44VcATD6bXHZcfbBwF5zyvHy0nqjyDHd188PRZvmM>)

Here, you can begin applying shape values to components by changing the corner radius values in the panel on the right side of the screen for each component’s States, Color, and Elevation layers.

## What’s next?

From here, start placing shaped components into your mockups to see how they fit in. Try experimenting with asymmetric shapes and different values to heighten both the appearance and function of shaped components within your app.
