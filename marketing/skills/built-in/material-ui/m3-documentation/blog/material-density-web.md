---
title: "Using Material Density on the Web"
source_url: "https://m3.material.io/blog/material-density-web"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/0b98092aff42-qGEdgWMxq2N_sW_BueogGolL8ogI1OH-bGMzhZNeLW7eWTMFgIuECRquT-to3lVaPz2xB0Of1gkclBGRM_crDbuQksdm8JxZ.png"
  - "assets/images/04c47d6166fc-F8hhbo6k-xtQobrVXyVPq5sMtq55LdYNK-vF1FuoiHNAskmChocIq25FY_5zwXKhEfBTv9v1FDSo9z9ajBT4C_w5waSj0WLt.png"
  - "assets/images/5b2636e47c06-EVnaNboEUFzvG7vAgiB1ms8RVVq_eoEnbWeIL-qANCNshmQPlYnOz0MZ83iJUyyKy1AmELd2DdsKQJXHv1lJbBsuRjUMMq7O.png"
  - "assets/images/70cc9fa85ae7-SvqXdrALyOlRHQnYC4HtXjAjlgKg8wmlyWI7_IoSGhJtWYnA7_stDRe2RcAHGdLmuZ44gxQomC3Ou892hRxnJWIHOdoh9dO9.png"
  - "assets/images/72dc03308db2-6LmJ81p_ndW4jg3xhQX71w38pETsqLzveFlWS-FUlft6y9UH_VfBogRFc1L7VfUe7i4pVfsUDv7uxsl7LM9XwCmmNm6jzFnx.png"
  - "assets/images/5f33c7d1606c-Vi-Xzj_EFwXoGr5AXM_EAlH3fD1mRpUSHLJx-Q_46M_jRBNtFmX1U3kP0D8E7bvmlZ8VdPbi4sjBSK7AqcAVq9_bSZb0UWMX.png"
  - "assets/images/c9ab7ac79b51-j516C0DxGclTIpdxPWDWVXMKdOggEuO0Bd4_ZRuCFUW8XTkc-EA6L1O0KgxA8fOBddURVPNR2iZnHytWCnCgzKcPSZR-vNtu.png"
  - "assets/images/c77b6ae4e17a-HxVn_7TPzEqlIktSr3gXhkAt6gmQxjZ251Qyr9MzV6PtEffngRdgyaNztV8_2UunNabj6m6cQ2eN-HunK7-PaiJ0i4W0GXe0.png"
---

# Using Material Density on the Web

A hands-on guide to applying default, comfortable, and compact density to your application

![Image](<https://lh3.googleusercontent.com/qGEdgWMxq2N_sW_BueogGolL8ogI1OH-bGMzhZNeLW7eWTMFgIuECRquT-to3lVaPz2xB0Of1gkclBGRM_crDbuQksdm8JxZ5fqN>)

Published: 2020-01-16T13:00:00

A common issue for any front-end developer is layout and spacing. How dense should your user interface be? Can a design system really account for all the different layouts in a product? Material is here to help.

The latest update to our density guidelines includes [a new systematic approach to applying density](https://material.io/design/layout/applying-density.html) and spacing in digital products. Now, you can change the spacing of your product to suit layouts for different use cases, such as data-heavy layouts vs. a layout with a single call-to-action.

> **Just as you use Material Theming to systematically apply color, typography, and shape to your UI, now you can systematically apply density.**

![Image](<https://lh3.googleusercontent.com/F8hhbo6k-xtQobrVXyVPq5sMtq55LdYNK-vF1FuoiHNAskmChocIq25FY_5zwXKhEfBTv9v1FDSo9z9ajBT4C_w5waSj0WLtBO-kQg>)

Various Material themes created by adjusting color, shape, and typography.

Global density changes affect the spacing between all elements at a macro level. However, you can also apply bespoke density values to any individual component.

Material Design’s density scale includes three levels of density: **default**, **comfortable**, and **compact**.

![Image](<https://lh3.googleusercontent.com/EVnaNboEUFzvG7vAgiB1ms8RVVq_eoEnbWeIL-qANCNshmQPlYnOz0MZ83iJUyyKy1AmELd2DdsKQJXHv1lJbBsuRjUMMq7OnkJgTw>)

A demo of Material’s density scale in action, created by engineer Abhinay Omkar.

## The Rules of Applying Density

Compact density can be great in data-rich applications that need to display a lot of information in a small amount of space. For example, data tables or long forms may benefit from an increase in density. This would allow more information to be seen on the page, giving the user important relational context at any given time.

![List items and text fields shown in default, comfortable, and compact densities](<https://lh3.googleusercontent.com/SvqXdrALyOlRHQnYC4HtXjAjlgKg8wmlyWI7_IoSGhJtWYnA7_stDRe2RcAHGdLmuZ44gxQomC3Ou892hRxnJWIHOdoh9dO98OBI>)

https://material.io/design/layout/applying-density.html#usage

However, an increased density display may be less useful in layouts that involve focused tasks or small user inputs. For example, date pickers need ample space for the user to be able to accurately make a selection. High density spacing can also make alerts and messages difficult to notice and read.

An increase in density can also decrease the accessibility of your UI in some cases, so make sure you’re applying density in places where user feedback is not affected. This dense calendar UI is an example of how increasing density could make it harder to use an application.

![Date picker displaying September and October, with highlighted dates](<https://lh3.googleusercontent.com/6LmJ81p_ndW4jg3xhQX71w38pETsqLzveFlWS-FUlft6y9UH_VfBogRFc1L7VfUe7i4pVfsUDv7uxsl7LM9XwCmmNm6jzFnxDvfX8g>)

An example of where not to use a higher density display. The increased density makes it more difficult for users to interact with the dates in this calendar.

It’s important to keep accessibility in mind when you’re applying density to your UI. **No matter the density, all touch targets should be at least \`48px\` in size.**

![Image](<https://lh3.googleusercontent.com/Vi-Xzj_EFwXoGr5AXM_EAlH3fD1mRpUSHLJx-Q_46M_jRBNtFmX1U3kP0D8E7bvmlZ8VdPbi4sjBSK7AqcAVq9_bSZb0UWMXrefZYg>)

Even though density can visually decrease, it’s important for touch targets to remain at least 48px in size.

For the buttons in dense layouts, we’ve applied external padding so that even when they appear smaller and condensed, they still have an accessible touch target size.

It’s important to maintain a good design equilibrium in dense layouts, so when increasing component density you should also _decrease_ your layout or grid density. This means that the more dense your components become, the larger your margins and gutter widths should be. This helps keep your UI legible.

![Dense app overlaid with columns illustrating its wider horizontal spacing](<https://lh3.googleusercontent.com/j516C0DxGclTIpdxPWDWVXMKdOggEuO0Bd4_ZRuCFUW8XTkc-EA6L1O0KgxA8fOBddURVPNR2iZnHytWCnCgzKcPSZR-vNtuFMTPi6M>)

Increase horizontal spacing as you decrease vertical spacing.

## Using Density on the Web

**The Density Scale**

Density is defined by a scale starting at 0 for default density. The scale moves to negative numbers (-1, -2, -3) as the space between components decreases, resulting in a higher density layout. Each increment also decreases the height of a component by 4px. But it does not affect the horizontal spacing within the component. For example, in an icon button, increasing the density will not make the icon appear closer to the text.

The height or size of a component is calculated with the following formula:

**Let’s See Some Code**

When you update to the latest version of MDC (4.0.0 or greater), density is included as mixins within the components. You can apply a density of \`-3\` (the most dense option) to a button by applying the following code:

In your terminal, install an MDC button via :

In your HTML, Add the following button markup:

_Note: this markup is different than the previous version of MDC (3.0.0)_

In your Sass file, Import the button mixins and apply the density as a mixin to your custom button:

![Image](<https://lh3.googleusercontent.com/HxVn_7TPzEqlIktSr3gXhkAt6gmQxjZ251Qyr9MzV6PtEffngRdgyaNztV8_2UunNabj6m6cQ2eN-HunK7-PaiJ0i4W0GXe07jTrUA>)

Various density values change the vertical spacing in the button component.

The following components currently provide density Sass mixins:

-   [Button](https://material.io/components/web/catalog/buttons/#sass-mixins)
-   [Checkbox](https://material.io/components/web/catalog/input-controls/checkboxes/#sass-mixins)
-   [Chip](https://material.io/components/web/catalog/chips/#sass-mixins)
-   [Data Table](https://material.io/components/web/catalog/data-tables/#sass-mixins)
-   [Icon Button](https://material.io/components/web/catalog/buttons/icon-buttons/#sass-mixins)
-   [List](https://material.io/components/web/catalog/lists/#sass-mixins)
-   [Radio](https://material.io/components/web/catalog/input-controls/radio-buttons/#sass-mixins)
-   [Switch](https://material.io/components/web/catalog/input-controls/switches/#sass-mixins)
-   [Tab Bar](https://material.io/components/web/catalog/tabs/tab-bar/#sass-mixins)
-   [Text Field](https://material.io/components/web/catalog/input-controls/text-field/#sass-mixins)

We recommend customizing density using the provided density mixins rather than arbitrarily applying component height.

That’s pretty much it! You can check out the [updated design guidance](https://material.io/design/layout/applying-density.html) for more details, but applying the density scale is a breeze with the new MDC 4.0. If you added density to your application, reach out to us on Twitter [@MaterialDesign](https://twitter.com/materialdesign). We’d love to see it.
