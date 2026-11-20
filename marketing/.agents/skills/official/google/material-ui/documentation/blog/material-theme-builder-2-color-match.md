---
title: "An updated theming experience with Material Theme Builder 2.0"
source_url: "https://m3.material.io/blog/material-theme-builder-2-color-match"
lastmod: "2024-12-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/e0561f0dabb2-G-lo9iMbLB2HmLzapkkKwExJ-Ow6njtQzTv_BiqyiSdz7N8ghpctryKncyYmOloM7Ea9LnRDeouN6kHxHul3Nb3hns_KKBql.png"
  - "assets/images/47f84f1c789c-9lfda79q7aRQKOuRgn7ChT1WnneB0Lj9BjxALujmRibwAf4l09ryVqI0JavYt48aNMiY7tO_wacXLr7XAnpR4Vl3YLNpFPYZ.png"
  - "assets/images/7f1d0166a312-pKph2Rvn0RBzADbXVpuJqsGc6zbNQ2ALxMzsjHySv0_9HJr1PqZ38eNTyBBI5QAxzSbYSR438lStkfCZ8F1WF3NeTKTJT7Np.png"
  - "assets/images/109e4cf92ade-9Q1KJXjQirR4uDCMN1bdkZw_IEE8kmipdp12l0_SiAt8Lz0-lpbvfMKu3CQIR43qLDslAUgqylf0xWwPplJx8Gu5pZpXNsM3.png"
  - "assets/images/65c6a8226e3a-t34eWFLVlhhuVptBrYhrG3UjrzQkzHkR9Rb_ED0jKT3qvzrpo9_hSKe9BCAT_MVO9HhXsFrrev7UMctcPGH1D9ea7S88aBK3.png"
  - "assets/images/4026849b2668-vmxrh2qhHt616-jWnNtSwola0NDrfEXjieK16ld1ync-7N7I9xVb5zQXfcjZGA9bFW0XR9rfB9FSXmP2s-7fpGuIKAvGREqy.png"
  - "assets/images/cf670f3814cc-wc_pVebXmApiecVYzqmhFKzTeWV7G_qjDLMw45gvRqiMaLVwAbjkSND9ZHujdHyftjWAQ-Zv_DMFZAmCW9MUbnpTOcky2s_1.png"
  - "assets/images/8c80af633b04-Ut67JWwKXvG9Cfp64hx5vVL-0ZZcCFsyPaMUANxA74iI3orwLc7GcIkFQKYDZ8tVGwp0lf7VbW9mE_6tmczIoxL92CjTYzY_.png"
  - "assets/images/f9f0dedd24f4-UJOhYP5Wukmbi3BkKhOo31qJJ7u6WC9ZbuUGCF6v4UvvI-TPFy7T169neRuhVgGYJzzbkKejumV6zH4FniO-jhxyFs06hJGL.png"
---

# An updated theming experience with Material Theme Builder 2.0

Celebrating two years of MTB with a major update including Color Match

![illustration of the number 2.0](<https://lh3.googleusercontent.com/G-lo9iMbLB2HmLzapkkKwExJ-Ow6njtQzTv_BiqyiSdz7N8ghpctryKncyYmOloM7Ea9LnRDeouN6kHxHul3Nb3hns_KKBqlU75poDENeQ3UvKhcn7w>)

Published: 2023-12-19T13:00:00

Material Theme Builder (MTB)  turned two recently, and with that birthday we’re excited to announce the release of an updated MTB for Figma!

## Tooling journey

Working on the Material Theme Builder has been an exciting journey starting from a simple prototype to help display the, at the time, upcoming Material You dynamic color for designers in a usable format into a tool on multiple platforms and features.

Along the way there have been architecture changes. The very first prototypes were two separate modules: one to visualize colors and export to the now defunct Design System Package format and a separate project to create Android XML theming files from a DSP archive. The projects were merged, more platforms were added, and export was re-written. In a couple cases, we outgrew the framework and had to move to another, justifying the pain of a rewrite if the change was better for our users. You also get to benefit from the lessons you've learned along the way to fix the technical debt.Featured in a few conferences, seeing MTB featured in the I/O keynote was a highlight moment. Showing how important small experiences can make a big impact.

And plenty of listening to our users! We learned how users were understanding dynamic color, wanted to express their branding, what new platforms they wanted to use, and more. Y’all have guided the direction of Material Theme Builder the most and our 2.0 features reflect that.

As part of Material DevRel, our goal has always been about empowering app makers to create wonderful experiences. MTB started with the premise to make Material 3 easier to implement and we hope to continue on that mission.

![MTB screenshots](<https://lh3.googleusercontent.com/9lfda79q7aRQKOuRgn7ChT1WnneB0Lj9BjxALujmRibwAf4l09ryVqI0JavYt48aNMiY7tO_wacXLr7XAnpR4Vl3YLNpFPYZcnauvrT7G5aREJc0WQ>)

## On to the updates

First, and possibly the biggest change: no more switching between dynamic and color tabs. The color features all live together in one panel.

This creates an overall faster experience as a result, and allows the creation of a Material compatible color scheme through any Material Color Utilities, MCU, features.

![Screenshot highlighting the "swap to theme" button](<https://lh3.googleusercontent.com/pKph2Rvn0RBzADbXVpuJqsGc6zbNQ2ALxMzsjHySv0_9HJr1PqZ38eNTyBBI5QAxzSbYSR438lStkfCZ8F1WF3NeTKTJT7NpOzv_VyMJoxkCixbOsEY>)

Theme control is still up top, but now has the overall theme swap button next to the Theme menu dropdown.

Speaking of the theme menu, it now contains theme management actions included to rename and delete themes.

![Contrast theme examples](<https://lh3.googleusercontent.com/9Q1KJXjQirR4uDCMN1bdkZw_IEE8kmipdp12l0_SiAt8Lz0-lpbvfMKu3CQIR43qLDslAUgqylf0xWwPplJx8Gu5pZpXNsM3qYo0BDXTmBB5sB5sJKE>)

Swapping to other theme variants is now within the color panel. Light and dark themes are still there. You can now future proof your themes. MTB creates new contrast themes, announced at [I/O 2023](https://youtu.be/vnDhq8W98O4?si=VdAI1rDCfSsqh8Xv&t=175), for even more accessible apps. Support will vary across platform code export.

![Screenshots of source image and core colors](<https://lh3.googleusercontent.com/t34eWFLVlhhuVptBrYhrG3UjrzQkzHkR9Rb_ED0jKT3qvzrpo9_hSKe9BCAT_MVO9HhXsFrrev7UMctcPGH1D9ea7S88aBK3FMXFG2kGdIknGhf3yN0>)

Generating a color scheme from an image or one source color is still possible, using the wallpaper and content setting.

Or override the all key color sources, which now include error and neutral variants.

You’ll also find the latest [tonal surface color roles](../styles/color/roles.md#89f972b1-e372-494c-aabc-69aea34ed591) to use in your color schemes.

![HCT color picker](<https://lh3.googleusercontent.com/vmxrh2qhHt616-jWnNtSwola0NDrfEXjieK16ld1ync-7N7I9xVb5zQXfcjZGA9bFW0XR9rfB9FSXmP2s-7fpGuIKAvGREqyAp3AiKVtQIny3RFBuw>)

## And a few other UX changes

Brand new color picker gives greater look at HCT, along with confirmation buttons.

It’s easier to play around with color schemes in the plugin without losing your current scheme. When done editing your theme, click **Update** to apply changes. **Cancel** will stop the process of updating for any last minute changes.

## What about brand colors?

Color fidelity is here. Turn on **Color match** in settings to have resulting schemes more closely match your key color inputs. The resulting scheme is still produced by the MCU algorithm to produce tonal values and assign color roles, with your input colors landing into container slots.

![Image](<https://lh3.googleusercontent.com/wc_pVebXmApiecVYzqmhFKzTeWV7G_qjDLMw45gvRqiMaLVwAbjkSND9ZHujdHyftjWAQ-Zv_DMFZAmCW9MUbnpTOcky2s_1GIdH6RhnuMr3WnpL8A>)

Color match diagram

## Plus Variable support

After Figma’s variable announcement, we heard plenty of requests to bring variables in the Material Theme Builder. So with 2.0, variables will be generated alongside styles with some options, like generating reference values as variables. Variables do require access to additional modes, but styles are still available.

![Screenshot of figma variables](<https://lh3.googleusercontent.com/Ut67JWwKXvG9Cfp64hx5vVL-0ZZcCFsyPaMUANxA74iI3orwLc7GcIkFQKYDZ8tVGwp0lf7VbW9mE_6tmczIoxL92CjTYzY_Lo42-71RUsi2vujA-Hw>)

## Looking forward

It’s been a fun and exciting couple years for the theme builder and we look forward to providing more updates, including the planned 2.0 update for web.

Check out [Material Theme Builder](https://www.figma.com/community/plugin/1034969338659738588/material-theme-builder) and [M3 Design Kit](https://www.figma.com/community/file/1035203688168086460/material-3-design-kit) on the Figma Community and happy theming.

![Illustration of the number 2.0](<https://lh3.googleusercontent.com/UJOhYP5Wukmbi3BkKhOo31qJJ7u6WC9ZbuUGCF6v4UvvI-TPFy7T169neRuhVgGYJzzbkKejumV6zH4FniO-jhxyFs06hJGLeWbyO1lDYzgJkG-m7ob6>)
