---
title: "Material Icons: Sehee Lee"
source_url: "https://m3.material.io/blog/material-icons-sehee-lee-interview"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/f169302adc03-GT3F4Oj3YQOOZr2GtR7PL_AAkvMBuf_E2EdPfra77TwbRepf7IBoQxygVVTHNMNfyyQlGGtkTbxwmacljHDjueZRtLxI3CIK.png"
---

# Material Icons: Sehee Lee

An interview with Senior Visual Designer leading icons and design systems for Google Fonts, Sehee Lee

![Illustration showing SeHee interacting with abstract graphic elements](<https://lh3.googleusercontent.com/GT3F4Oj3YQOOZr2GtR7PL_AAkvMBuf_E2EdPfra77TwbRepf7IBoQxygVVTHNMNfyyQlGGtkTbxwmacljHDjueZRtLxI3CIKapiFFqBvlGKIuC9z-Q>)

Published: 2022-05-11T10:00:00

Sehee Lee is a Senior Visual Designer on Material Design, leading iconography and design systems for Google Fonts. Originally from Seoul, Korea, Lee is based in Brooklyn, NY.

Lee has been with Material since before it was Material, contributing to the original launch of the system back in 2014. During that period, as platform guidelines, navigation patterns, and components took shape, it became clear that there was no central system for creating action icons.

The effort to build more structure into the creation of these icons, which appear frequently throughout every Material interface, continued into the buildup to 2018’s Material Theming launch, when Lee took ownership of creating Google’s first proprietary icon set and open-sourcing five different styles of Material icons that stayed true to the system while tweaking parameters that could create new types of expression.

Four years later, the team has [introduced Material Symbols](https://material.io/blog/introducing-symbols), a variable font that captures over 2,500 individual glyphs with multiple stylistic axes in a single font file, exploding the expressive capacity of Material Icons. Coinciding with the launch, I got a chance to speak with Lee about her work, the project, and what it’s like to design something iconic.

## Designing an icon

**Iconography is already a complex topic - so let’s start with how you conceptualize icons in the first place.**

The key is to capture the essence of the term or action I’m trying to represent as an icon. The first goal would be to find intuitive, actionable and easily understood metaphors. And this ideation portion would definitely be the most time consuming part.

**How does the process look, from start to finish?**

I start with an image search with the term to understand the metaphor. The reason for doing a search is so that I can have a universal perspective as metaphors can be shaped based on my background, location or even age.

Once I pick a few suitable representations, I establish a base shape using the 4 design metrics: keyline shapes, material grid, stroke weight and radii corners. Then I add my essential lines to create the image. Last but not least, I subtract the unnecessary details from the completed image if they are not absolutely necessary. Squinting my eyes definitely helps to eliminate unnecessary details.

To publish the icon, I name the icon file according to our naming convention and add icon metadata that helps with searching for the icon in our platform. I consider icon discovery a critical feature as users won’t use the icon if they can’t find it!  Then I upload the asset to our repository which then goes in our icon pipeline. The pipeline auto generates SVGs, PNGs and icon fonts. Finally, the icon is linked to the repository and will be published at fonts.google.com/icons.

**This must be especially complex for a product like Material Icons, where the library wants to support all sorts of use cases - applications built all over the world for different audiences, even physical spaces. How is that accounted for in the work?**

This is why we take a systematic approach to handle the majority of our edge cases. And for the most part, the Material Design System provides a clear cut way to create icons that can fit 99% of our use cases. However, it’s also important to recognize the metaphor that can be offensive or have different contexts in different areas of the world. For example, we had user feedback from a Cyrillic native speaker and the user informed us about our 4k icon at the time. For English speakers, it was just an icon with ‘4k’ letters but those letters could be interpreted differently in Cyrillic. It turned out that the icon could be seen as a symbol of Secret Police working under Lenin. We had no idea! As soon as we heard of the feedback, we took an immediate action by modifying the icon so that it properly reads as 4k even in Cyrillic languages. So, sometimes we have unforeseeable events like this but our goal is to always create and maintain iconography that is socially unbiased and universally applicable.

**How do you maintain an editorial perspective when the catalog has over 2,000 glyphs across 5 styles, and especially now that there are variable axes to work with?**

The reason our iconography is popular with our users is because using our Material Design metrics and system grid, new icons can easily be created in the Material Design style. The way I think about this is essentially, by sticking to a system that’s proven to produce icons with consistent style, our iconography was able to reach millions of users while maintaining objectivity throughout the process.

## From Material Icons to Material Symbols

**Making the icons variable opens up a new dimension of possibility, but also of complexity - how were the axes chosen and designed?**

I wouldn’t say this is complexity but it is in fact flexibility. Because one can always choose not to utilize the new features but only take advantage of the file size reduction. Also, we’re providing SVGs as well for those who want to use Material Symbols as before without changing their integration process.

For v1 of Material Symbols, we chose the axes based on popularity and what worked best in the context of icons. The four axes we chose for Material Symbols were what users were most familiar with and would work best in terms of offering design flexibility.

**Why was Material Symbols created, what is the utility?**

I was intrigued by variable fonts when I first learned about its capabilities for fonts and thought about carrying over these awesome capabilities over to our icons.

We saw high-value opportunities in evolving our traditional icon format to Material Symbols using the Variable Font technology to improve icons’ design flexibility, user experience, and accessibility benefits. In fact, Google Fonts developed numerous Variable fonts and it is common in typography, but applying the Variable Font concept to icons seemed like a wild idea in the beginning. We spent a lot of time researching and prototyping the new symbols to find the right approach to develop the Material Symbols.

There are 2 big benefits we considered for creating Material Symbols. First, Material Symbols don’t require additional files for more variants and are therefore optimized for size and speed. Second, Material Symbols offer an ability to make adjustments that weren’t possible previously with the traditional icon format.

For designers, it provides subtle yet invaluable adjustments for optimizing the user experience and accessibility benefits; the four axes Material Symbols provide enables a wide range of styles for icon styles that opens up design flexibility greatly.

For developers, Traditional font/symbol storage requires multiple files, and takes up large amounts of storage space and network latency. Variable fonts can put all variations in one file, optimizing for storage space.

For both, when using variable fonts, there’s a benefit of not having to choose just one or a few styles to speed up performance. Variable fonts are able to provide users with all available styles with just one file, which also means that now you can do CSS animation between different styles of icons with one file.

**How does it work?**

Traditionally icons are designed by hand one by one. If we needed icons with different stroke weights, those would be created manually. Material Symbols are technically a new font and not a simple vector drawing. Since Material Symbols are created in a font development tool, the produced symbols also have the characteristics only seen in fonts and users are able to take advantage of features like the 4 stylistic attributes: Fill, Optical Size, Weight and Grade.

## Identifying with icons

**Have you ever been surprised by where Material Icons show up, or how people are using them, whether it’s the context, or the meaning ascribed to them?**

The material icons are everywhere in the digital and physical world! Our icons are open source icons and it’s truly amazing to see them on physical digital screens and randomly see them while browsing the web. If you ever visit and walk around NYC, you would see our Material icons on LinkNYC machine, which is a public digital billboard on the streets. On the other hand, we partnered up with MoMa design studio to use Material icons as the museum’s physical navigational signages, which was an incredible sight to see.

**Where do you see yourself reflected in the work? Your perspective, personality, experience, etc?**

My personality is actually the opposite of what I do for icons. I’m clumsy, would spill coffee everywhere and don’t stick to schedules. I think my icon creation speaks to my inner Hyde where I need to stick to a strict system, be pixel perfect and work for every context imaginable. As funny as it sounds, I believe icons are sort of an outlet for me to be more balanced in my life.
