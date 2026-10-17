---
title: "Material Design 3 for Compose 1.2"
source_url: "https://m3.material.io/blog/material-3-compose-1-2"
lastmod: "2026-05-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/1494ff21eb63-s3xVT-1wuUuzcoWJpUphvitXeO1RI2It2CvsRpJeizKORSkVbuCJ3Zen8kPJqj04iU9_Q31zeecGiaAyQeWuSRt22_IZejbk.png"
  - "assets/images/7f98b3acc785-MRGpPtlLR_oFFP5q9tXAcF_G6gcFGi_r1jnFTvg4SOntdC7Am9DVO15PgHkuBA0j0gh09F4rG9isWXUkbe0uG50KWzWw_K_L.png"
  - "assets/images/204a26328ec2-j8sTaLIqknCT9c2yqitgk2zF4W_HaOkYHW2g07Hjn-UWDytZNTCTz6MrSUJeBoI6VkwUnBEEckpF3Xz5L1Fw-NbawkpJaPTB.png"
  - "assets/images/0e16a5d6cf3d-oPc29x-FxE_KGdXW0B3AqQ8vgDJVuQzA_SjnKMKUEtbrfD7PW1YEQbRw0EzKyyPBJoMNsyFUPCR7yYzRVeXlbTIsBhxXy5FL.png"
  - "assets/images/a3e7aab77bba-2MxUFjAePtUPSAh_nqoOLP5CdnjlploeyoFwlMYJ8quSmima8XmJYSpK5UGv62Z-e72wx6PqYHP9NKWzrXlrbITXWeLc4TUu.png"
  - "assets/images/51c2305d93df-jAliy_eXJWNsSEZei7ySjjja4IptR2gCM-8efiykeYXP9aP2VC_Z_xx0Vry1UD7TSEI-H3wNLZWaBOWd5h3D3mIXofFPu13-.png"
  - "assets/images/019d4fbae53d-6YedDwTRqxhyco3sDt_0zfAq_BpA2PgWiY5Npblf5fJHuV8HXN0yP3UHwDU9CCLjPzEImikuZAC1Ly0dKA4oKUBiW8T97PRY.png"
  - "assets/images/b444ea93bc58-DPXzSGYraDha9ZwQi0WMQnWIhx_YrPkMUf6H5Yys4FVzVBXTt25UQFd19F0qUx2-yMH3nkVtMqkoUm3Gsw-8hJ6WrIAPNfTh.png"
---

# Material Design 3 for Compose 1.2

Exploring the 1.2 release of Material Design 3 for Compose

![abstract illustration pale green and peach tones](<https://lh3.googleusercontent.com/s3xVT-1wuUuzcoWJpUphvitXeO1RI2It2CvsRpJeizKORSkVbuCJ3Zen8kPJqj04iU9_Q31zeecGiaAyQeWuSRt22_IZejbkjPKMQAQqOgZn_gPJPhYI>)

Published: 2024-02-07T13:00:00

The 1.2 release of Compose Material 3 is here, and with it comes new components, some component changes and an expansion of the Material3 color system.

## Component Changes, Demotions and Promotions

`Segmented Button` is a new experimental component. There are single select and multiple selection variants.

![segmented button](<https://lh3.googleusercontent.com/MRGpPtlLR_oFFP5q9tXAcF_G6gcFGi_r1jnFTvg4SOntdC7Am9DVO15PgHkuBA0j0gh09F4rG9isWXUkbe0uG50KWzWw_K_LkhPXS82flPHXTEjDL9k>)

![segmented button with two items selected](<https://lh3.googleusercontent.com/j8sTaLIqknCT9c2yqitgk2zF4W_HaOkYHW2g07Hjn-UWDytZNTCTz6MrSUJeBoI6VkwUnBEEckpF3Xz5L1Fw-NbawkpJaPTBJ1mslD2yTE986QO1hbY>)

`BottomAppBar` has a `BottomAppBarScrollBehavior` to auto-hide itself when content is scrolled.

`SwipeToDismiss` has been refactored into `SwipeDismissBox` and remains in experimental status.

`Badge` and `BadgedBox` have been promoted to stable.

![Badge on icon](<https://lh3.googleusercontent.com/oPc29x-FxE_KGdXW0B3AqQ8vgDJVuQzA_SjnKMKUEtbrfD7PW1YEQbRw0EzKyyPBJoMNsyFUPCR7yYzRVeXlbTIsBhxXy5FLFqj7ZzczxnsGhobyaw>)

The `Chip` APIs have been promoted to stable.

![Chips](<https://lh3.googleusercontent.com/2MxUFjAePtUPSAh_nqoOLP5CdnjlploeyoFwlMYJ8quSmima8XmJYSpK5UGv62Z-e72wx6PqYHP9NKWzrXlrbITXWeLc4TUu6whTehBInpJsSUSEibx9>)

## Color Changes

Material3 Compose 1.2 has an expanded color set giving you more ways to personalize your apps. Notable additions are more surface options to represent emphasis of information in your UIs. Components have been updated to make fuller use of the new surface values.

![Light scheme](<https://lh3.googleusercontent.com/jAliy_eXJWNsSEZei7ySjjja4IptR2gCM-8efiykeYXP9aP2VC_Z_xx0Vry1UD7TSEI-H3wNLZWaBOWd5h3D3mIXofFPu13-6OqkHM93XeX_jtV6Qxw>)

![Dark scheme](<https://lh3.googleusercontent.com/6YedDwTRqxhyco3sDt_0zfAq_BpA2PgWiY5Npblf5fJHuV8HXN0yP3UHwDU9CCLjPzEImikuZAC1Ly0dKA4oKUBiW8T97PRYpu5cuVPucSpVdHX3l-S1>)

A small implementation note is that the ColorScheme object is now immutable allowing Jetpack Compose to skip it and possibly optimize successive compositions when the object hasn’t changed.

If your code currently modifies the colors in [ColorScheme](https://developer.android.com/reference/kotlin/androidx/compose/material3/ColorScheme) directly, you will need to make use of the [copy](https://developer.android.com/reference/kotlin/androidx/compose/material3/ColorScheme#copy\(androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color\)) method now to change colors.

#### Surfaces

Previously in Material, we provided a single surface value and calculated interpolated variants of it at runtime by blending with the primary color. This was frustrating for developers and designers when it came to implementing UIs because they were unable to assign those colors directly.

Color roles simulating different elevation levels have been added to theming. There are now three surface color options and five surface container options.

![Surface roles from a light color scheme](<https://lh3.googleusercontent.com/DPXzSGYraDha9ZwQi0WMQnWIhx_YrPkMUf6H5Yys4FVzVBXTt25UQFd19F0qUx2-yMH3nkVtMqkoUm3Gsw-8hJ6WrIAPNfThqEZRTIY41Oh6ebJjCSQ>)

`Surface Bright` is guaranteed to be the lightest surface color. It is not currently in use by any of our provided components.

`Surface` was the former backing color for many components. It has been replaced largely by the five surface container in 1.2. It is still in the spec so your components using it will not break.

`Surface Dim` is guaranteed to be the darkest surface color. Like Surface Bright, they are not in use in any existing components.

`Surface Container Lowest`, `Low`, `High` and `Highest` are additional color roles that can provide more or less emphasis in contrast to Surface Container.

The new color roles are already available in [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/).

## Where can I find more information about Material Compose ?

The components or features we highlight in these posts are only a fraction of the work that lands in each release. Check out the [release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3) for a full listing.

You can file [bug reports](https://b.corp.google.com/issues/new?component=742043&template=1346811) and follow [open issues](https://b.corp.google.com/issues?q=componentid:742043%20status:open) on Buganizer. You can also follow the progress of new versions on [cs.android.com](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/;bpv=1;bpt=0). Check out the [catalog app](https://play.google.com/store/apps/details?id=androidx.compose.material.catalog) to see the components in action.
