---
title: "First Batch of Color Fonts Arrives on Google Fonts"
source_url: "https://m3.material.io/blog/color-fonts-are-here"
lastmod: "2024-12-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/e9fda8a8fcab-iKp5uW5tPvj58pi9KYkpkWvc5HcmN5ac8Yv0QGrEI0vJ-RTMUpqpzLdlYL-6dcZvGs_c40UeT9eksFlyil0XhJuCOyLwTuoJ.png"
  - "assets/images/e4501fac31b7-15q8Mesh9vuqN0WwlpfAFQLkGmZHlewh-euAdzw8LRZIUl2v2N4aVkudZ-1p6gXTpe_rmd6ppHAsytUkG_AyOAXvYNhlT_eI.png"
  - "assets/images/b3454f56d0af-liq_8Gjm_jZ6VTbFM84YhvfV8CxYw3Lq0-_BChadKZtaCCQqWly7A_xQeoCjtVEv58i8ezS0QCS63DSdgukD2Dl9WKLV2sIi.png"
  - "assets/images/7040da6c372a-82T1-SHs1T4N7X_nTuhXy2KHqfwARHVMpo-EWC9BnDVt09opdvq6zaRD1yf71T4bTEPIeZJEU6doKTje6Tlr6NWk45O_qGSe.png"
  - "assets/images/5705e185e471-fWyXFi2FqhR7k1WYjdZO6qDqPuYKSHg74FLQQ4T5oB33bwEYrhHmQI8h9g99-L5ZgDaYpIOcOq9naTGunQ7lhP3NekiO2Kno.png"
  - "assets/images/0fe19082e058-oI0QdaqH_VIX4x7Ni-tI1oqrHtttsLVBFCh7NlM67c9TEbkuQN8SZAzZ5H3gSKx5bK8YWY8GIqYFbFYTo_X11y6ZLrvCLePb.png"
  - "assets/images/430088d13f34-Zr3fVKKWrRkxi5AsrhJ3rbHcZMDVLrM0QSGtSdRVFzhWudkCLp5jUnAZ9sSe5sKODk1iUBBcFywB-p2mdWZq7SpB4zMfXRC_.png"
  - "assets/images/5b88497fdb44-AZQAKPvbnB0bTy-1DYwjTow6tgP_34w8zdhhiN9H03uyEpvwerGxvTweQD_Xkdc5FdIERzwb9BM3xAbS8zcOnKsYQgwPzxkC.png"
  - "assets/images/bdc822a4f8b0-twfaIo6HkFuLvbba3Bw-4m72JyPcOkoZthb3X4BYY5UR0b8qPE795iCyPgciZSvc3n1-_tg3Dl8GtM5UYUf_2SBsoFpQqryT.png"
  - "assets/images/021e05a107ae-w4gWoJQ1XDkt_KCDf96fh2Z7z8sdU6l6XQMhWWIHGcdqi8Ot-hJ7GXPP_8w2M4DbwTdFJCKY2m_Pl_ksKNjTfiA4QcUxzKu7.png"
  - "assets/images/fa03089f1ee6-zGksvBrRrwIsAN23II3CYq4hmyd2rlTv0HoEsCJ5KScrz_AZvOgbo_vLm05BFw5YYMn9rpcmyzTN0a0uz3N2AQgPLzwyxwTw.png"
  - "assets/images/0cdbf3e1f6f3-1-Wsiqq7hvGCntDYI3sMmQEpfUYx-WsTmXFok0JlB2zf3dWMvkLzHKeFB9fRQHlao7LmtcEqL_Wr0jQcKEGgktZp1HB-DmCg.png"
  - "assets/images/fe7b47d52c29-SJy-cDo7R-oDP7Az15nn51lgrb7aC0ODkGXAN4GZHUKK4IEDGIZoettzERlGQpQ1G-T7_s2jCr9JmDj9rFK6cOL2z8AX5_qL.png"
  - "assets/images/b47a580d626f-zlOa8ukSBhWnoBdM5_gA6mrQ0C5GxGKl3VYrHx8hU2mrWmZ3EqachtBPo5fzdhpSTpNi2QsPGg4Vl5Di6tQ63ryYx41BphmH.png"
---

# First Batch of Color Fonts Arrives on Google Fonts

Customize color palettes, add gradients, and more with COLRv1

![Illustration that says "color fonts" with various examples.](<https://lh3.googleusercontent.com/iKp5uW5tPvj58pi9KYkpkWvc5HcmN5ac8Yv0QGrEI0vJ-RTMUpqpzLdlYL--6dcZvGs_c40UeT9eksFlyil0XhJuCOyLwTuoJ3wHaIfYy4tNsW1hmA>)

Published: 2022-09-13T09:00:00

Even if you’ve never heard of “color fonts,” you probably use them everyday; emoji are color fonts. Color fonts enable color specification within the font file—and one [glyph](https://fonts.google.com/knowledge/glossary/glyph) can have multiple colors. Non-color fonts (i.e., most existing fonts) only specify where color goes. A glyph can only be one color, and if you want your type to be something other than black, you have to manually change it.

Emoji is the most obvious use case for color fonts, but type designers are running with this technology to make highly expressive and customizable typefaces—especially in the newest format, COLRv1.

We’re so excited about color fonts, we even added a search filter for them on [Google Fonts](https://fonts.google.com/).

## What’s so special about the COLRv1 font format?

There are a few things. First, COLRv1 is a binary vector format (unlike the bitmap color fonts of yore), which means fonts can scale without becoming pixelated. And by layering glyphs and then reusing those layers in similar glyphs, file sizes stay compact.

COLRv1, which is already available in both Chrome, Android, and the Google Fonts API, adds support for:

-   **Gradients**
    All the common CSS gradients are possible, and multiple gradients can be included in a single glyph.

![Type samples with various gradient types applied.](<https://lh3.googleusercontent.com/15q8Mesh9vuqN0WwlpfAFQLkGmZHlewh-euAdzw8LRZIUl2v2N4aVkudZ-1p6gXTpe_rmd6ppHAsytUkG_AyOAXvYNhlT_eI6gNJCYaO61eoNE6KrCM>)

-   **Multiple color palettes
    **The typeface designer can choose to include multiple color palette options in the font for use in CSS (`base-palette`).

![Type samples with six different gradient colors applied.](<https://lh3.googleusercontent.com/liq_8Gjm_jZ6VTbFM84YhvfV8CxYw3Lq0-_BChadKZtaCCQqWly7A_xQeoCjtVEv58i8ezS0QCS63DSdgukD2Dl9WKLV2sIiayRSq7a5IAVZp6FwNWg>)

-   **Color palette customization**
    You can customize color palettes in CSS (`font-palette-values`).

![Type sample comparing two different sets of gradient color values.](<https://lh3.googleusercontent.com/82T1-SHs1T4N7X_nTuhXy2KHqfwARHVMpo-EWC9BnDVt09opdvq6zaRD1yf71T4bTEPIeZJEU6doKTje6Tlr6NWk45O_qGSe80So2UzmjyswNU3-f_w>)

-   **OpenType [Variations](https://fonts.google.com/knowledge/glossary/variable_fonts)**
    With the ability to add variable axes too, the possibilities for customizing a COLRv1 font are literally infinite!

![Type sample showing extrusion depth and edge highlight variables at various values.](<https://lh3.googleusercontent.com/fWyXFi2FqhR7k1WYjdZO6qDqPuYKSHg74FLQQ4T5oB33bwEYrhHmQI8h9g99-L5ZgDaYpIOcOq9naTGunQ7lhP3NekiO2KnolIoYl1x5t1Ef4gijooqy>)

## See COLRv1 in action with nine new typefaces, available now on Google Fonts

The ability to represent multiple colors within a font is an important advancement for expression and communication.

Traditionally, calligraphy was the highest form of Arabic art. And, as it has been used as much for beauty as communication since the earliest versions of the Quran, Arabic calligraphy is inherently colorful. But Arabic is also unique in its functional use of two-color glyphs to clarify pronunciation/add diacritics. Today, colors are still often used for this purpose to help people learn the language.

#### 1\. **[Reem Kufi Ink](https://fonts.google.com/specimen/Reem+Kufi+Ink)** and 2. **[Reem Kufi Fun](https://fonts.google.com/specimen/Reem+Kufi+Fun)** (Arabic and Latin alphabets)
Khaled Hosny (with Latin components by Santiago Orozco)

Expanding on the Reem Kufi typeface, Reem Kufi Ink and Reem Kufi Fun are based on early Arabic Kufic calligraphy.

Reem Kufi Ink is inspired by designs of the late master of Arabic calligraphy Mohammed Abdul Qadir, who revived this art in the 20th century and formalized its rules. It uses color gradients as an artistic interpretation of what the ink flow of traditional calligraphy would look like when written with a nib (the pointed end of a pen) and ink.

![Reem Kufi Ink specimen.](<https://lh3.googleusercontent.com/oI0QdaqH_VIX4x7Ni-tI1oqrHtttsLVBFCh7NlM67c9TEbkuQN8SZAzZ5H3gSKx5bK8YWY8GIqYFbFYTo_X11y6ZLrvCLePbPrisrWrluCSlTG3hmYw>)

Reem Kufi Fun is weight-variable and is also based on early Kufic (Mushafi) models, but is retrofitted to the Fatimid Kufic grid and borrows from its forms.

![Reem Kufi fun specimen.](<https://lh3.googleusercontent.com/Zr3fVKKWrRkxi5AsrhJ3rbHcZMDVLrM0QSGtSdRVFzhWudkCLp5jUnAZ9sSe5sKODk1iUBBcFywB-p2mdWZq7SpB4zMfXRC_e8admn5iXKdBCO82pJ0>)

#### 3\. **[Aref Ruqaa Ink](https://fonts.google.com/specimen/Aref+Ruqaa+Ink)** (Arabic and Latin alphabets)
Abdullah Aref, Khaled Hosny, Hermann Zapf

Aref Ruqaa Ink is an Arabic typeface inspired by the classical Ruqaa calligraphic style, which is the the most common type of handwriting in the Arabic script. The use of gradients creates the look of simple, hand-drawn calligraphy.

![Aref Ruqaa Ink specimen.](<https://lh3.googleusercontent.com/AZQAKPvbnB0bTy-1DYwjTow6tgP_34w8zdhhiN9H03uyEpvwerGxvTweQD_Xkdc5FdIERzwb9BM3xAbS8zcOnKsYQgwPzxkCup0HklbeLDB_tb5LtA>)

#### 4\. **[Amiri Quran](https://fonts.google.com/specimen/Amiri+Quran)** (Arabic and Latin alphabets)
Khaled Hosny and Sebastian Kosch

The [Amiri](https://fonts.google.com/?query=amiri) typeface family revives one of the few metal typefaces that were used in typesetting the Koran, a Naskh-style Arabic face. Amiri Quran adds color to reflect how the text appears in early 20th century printed versions of the scripture.

![Amiri Quran specimen.](<https://lh3.googleusercontent.com/-twfaIo6HkFuLvbba3Bw-4m72JyPcOkoZthb3X4BYY5UR0b8qPE795iCyPgciZSvc3n1-_tg3Dl8GtM5UYUf_2SBsoFpQqryTZ1qQSNgHpk-lLfRUvw>)

#### 5\. **[Cairo Play](https://fonts.google.com/specimen/Cairo+Play)** (Arabic and Latin alphabets)
Mohamed Gaber, Accademia di Belle Arti di Urbino (Fine Arts Academy in Urbino, Italy)

Cairo Play is a new, weight-variable color font and member of the Cairo family. Cairo’s design is based on the Kufi calligraphy style. The Kufi style is the oldest style of Arabic calligraphy. Because it had to be drawn on parchment (before paper, which was much smoother, was widely available), its shapes are more angular than later, more cursive styles. This makes it perfect for balancing classic and contemporary tastes. Cairo Play’s Arabic component has a wide glyph set that supports the Arabic, Farsi and Urdu languages.

![Cairo Play specimen.](<https://lh3.googleusercontent.com/w4gWoJQ1XDkt_KCDf96fh2Z7z8sdU6l6XQMhWWIHGcdqi8Ot-hJ7GXPP_8w2M4DbwTdFJCKY2m_Pl_ksKNjTfiA4QcUxzKu7zXWwRjTcjSVnRpV__S3m>)

#### 6\. **[Blaka Ink](https://fonts.google.com/noto/specimen/Blaka+Ink)** (Arabic and Latin alphabets)
Mohamed Gaber

Blaka Ink, part of the experimental [Blaka](https://fonts.google.com/?query=blaka) family from Mohamed Gaber of Principle Design, is inspired by Kufic letterforms hand-drawn with a reed pen. Sharp edges, thick strokes, and gradients create a striking style and the appearance of overlapping ink strokes.

![Blaka Ink specimen.](<https://lh3.googleusercontent.com/zGksvBrRrwIsAN23II3CYq4hmyd2rlTv0HoEsCJ5KScrz_AZvOgbo_vLm05BFw5YYMn9rpcmyzTN0a0uz3N2AQgPLzwyxwTw1ZyH0iT3xtYszDUgly4s>)

#### 7\. [**Noto Color Emoji**](https://fonts.google.com/noto/specimen/Noto+Color+Emoji)
Google

Last Spring we debuted [Noto Emoji](https://fonts.google.com/noto/specimen/Noto+Emoji?query=noto+emoji&vfonly=true), a variable, monochrome version of Noto Color Emoji. And now the chromatic version is available on fonts.google, too. Coinciding with the new Unicode 15.0 Update, this launch includes 21 new characters, bringing us to a total of 3,664 emoji! Thanks to COLRv1, you can customize them all. Go ahead and [give the duck a whirl](https://developers.googleblog.com/2022/09/updates-to-emoji-new-characters-animation-colors-and-more.html).

![Noto Color Emoji specimen showing multicolored ducks.](<https://lh3.googleusercontent.com/1-Wsiqq7hvGCntDYI3sMmQEpfUYx-WsTmXFok0JlB2zf3dWMvkLzHKeFB9fRQHlao7LmtcEqL_Wr0jQcKEGgktZp1HB-DmCgbxIv9ME6qyYxqTpnIg>)

#### 8\. [**Bungee Spice**](https://fonts.google.com/specimen/Bungee+Spice) (Latin alphabet)
David Jonathan Ross

This newest member of the urban signage-inspired Bungee family includes multiple gradients and color palettes for you to play with.

![Bungee Spice specimen.](<https://lh3.googleusercontent.com/SJy-cDo7R-oDP7Az15nn51lgrb7aC0ODkGXAN4GZHUKK4IEDGIZoettzERlGQpQ1G-T7_s2jCr9JmDj9rFK6cOL2z8AX5_qLjWjIycjX8YLTnck-Ptg>)

#### 9\. **[Nabla](https://fonts.google.com/specimen/Nabla)** (Latin alphabet)
Arthur Reinders Folmer, Just van Rossum

Nabla was inspired by isometric computer games (remember [Q-Bert](https://freeqbert.org/)?). It makes use of COLRv1 capabilities to create perspective, with smooth gradients, sharp highlights, and blended shadows. It includes multiple color palettes and two font variation axes—one for the depth of the letters, and another for the thickness of the highlight.

![Nabla specimen.](<https://lh3.googleusercontent.com/zlOa8ukSBhWnoBdM5_gA6mrQ0C5GxGKl3VYrHx8hU2mrWmZ3EqachtBPo5fzdhpSTpNi2QsPGg4Vl5Di6tQ63ryYx41BphmHImRGN0ee-Nq5vfNYNpA>)

Learn more about COLRv1 in “[COLRv1 Color Gradient Vector Fonts in Chrome 98](https://developer.chrome.com/blog/colrv1-fonts/)” on the Chrome Developers blog. [Try and download](https://fonts.google.com/?coloronly=true) all nine new color fonts on Google Fonts.
