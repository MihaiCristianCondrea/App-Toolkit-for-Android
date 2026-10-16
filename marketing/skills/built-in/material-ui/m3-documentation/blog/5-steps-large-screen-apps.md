---
title: "Start Here: 5 Exercises to Prepare Your App for Large Screens"
source_url: "https://m3.material.io/blog/5-steps-large-screen-apps"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/8ecd02eefe62-pN5xtyQvPk3HXVVz-gVDc0YYvJ-UzjfFjMcEemwIKWfVmZGCcyV4XNEAjehGLIA9VnuCRzSC0HDNy6p0tDChJvEWjRVsqCvB.png"
  - "assets/images/bd97b0ab46fa-HTJEmQvSXdNsI1MN7oJAPbkYfsf9y62V0p5ukJkUj1QuFv815XMIQXnwcQfTQMGFgCvf1Yy3PPCvGXk_N8OZaLwSk6X33DiF.png"
  - "assets/images/cb67f65eedc6-agyP5i_2bR0xJSQAvhpHz4LHTSs4yJAQcAoVR7E3FJzstYjiOwQ9XKMQknd7m0W353Lt2dscLdrkLJly1QTu1_T6V69EiDoC.png"
  - "assets/images/2a50eecfc4a1-fPUTCNko0Reqxs5F1jBfEjfRoChAgxVvSLi9ZyY4D6BtM2wokuo9za5I1COjpVPLeEM3pgvmBm4pgipA7BmbXAll89Wvj6Bt.png"
  - "assets/images/735b299a4d2a-NcVgDQiMD8lmLG66Tg6tRF9aLzg6qe1kXOKBEb2APkBGYQJdkiRk300X6BxK9QlkBcArB8dXovECYR_b1yqqAUvROt5lbYSs.png"
  - "assets/images/b3b64dec6421-p2g-lbdnIM7_hSDuAvZucHJdOEDbKG2QOKo4jxRIKCDao70r3MRuBvQ8ZVY_IQy1gh8bD0c11a1Cb38-_kcazdd5sz58P1Bi.png"
  - "assets/images/95e05492f47d-WAav5X47sOqP2CONOUM12XbgRJff3usN5e3cEDOIMXXo8Uq5Ul-0wyDNvVV-Fpx4JVQK8bhu_8LfDDXfyd5ZQVWD7LTmv_XC.png"
  - "assets/images/7cd5946fa0a7-5InlToHTWzHokPZd74ZJD9rAKV_poZMvMzVtX6Ciwg4r83WMN38HC4rbfP5ipIV0B4IKOWMjl-m8aWXMAPKRvEdGnkxYs-Ke.png"
  - "assets/images/7f7cbdc6205e-NqL6VO-rJcuYtBM2bUs1ai9c4-X_B5lAAC1Lm5DkKk_nAhTwHI5rvdmxTJiANygV3NrBMP3L3zwH3_E-Gy8O1wMdEawcpczp.png"
  - "assets/images/8ca414214705-NzQcEITLAASk0MiPUUSxu12yFwopV2RemkIS1mo5ahDV7G2vqszZLy6EJdx_IpxKncCwuaiWJLIyRKGD-nX3aFzIEzSh11qG.png"
  - "assets/images/366be6d8ef41-rptD0QAcpqglEJpcQRzi0z4s-Pa6T_53kwZTh7q4OrcULe6OUtrI1o-nxNOLdl1qQtwh-Nkgh239G8iGsa-3vW1-yyxS1b6f.png"
  - "assets/images/0c00afae1995-K7RzFHBBHTcIBQa1iMLVJXyVcZ6PkzOZ20flGZ1oa8jKXOYX6QPQbFYQ0znixG-5mrBwAYayfjTjsEGuTRaZfDgJ2HwkWHaz.png"
  - "assets/images/5ee4f68762e2-gs6kR69R30TyIQAd5y_YdWvvjnOFCareTuUwOCVZNXENRETEEJSX9-ZFCrDbZ-QqqLo60lHXA9LgH_99aR9NAIJqKV-G4Ex-.png"
  - "assets/images/f0ae4bdfce20-uNzhLODJe2pcjJ96Kad-mLok84YJnT6izhbbUn4Py4dtISgV7J6oskQ2L9aY-GxqXuu64b31ALZB8nDGxHWSSVU-3XyA__gz.png"
  - "assets/videos/d1b5706c9991-mio-staging_mio-components_1584058305895_assets_1n49HQ1AjiDC5aqG38DrtswaJFrR3exKf_rail-adaptatio.mp4"
---

# Start Here: 5 Exercises to Prepare Your App for Large Screens

What to prioritize when building a responsive experience

![Illustration of abstract shape morphing within 5 boxes](<https://lh3.googleusercontent.com/pN5xtyQvPk3HXVVz-gVDc0YYvJ-UzjfFjMcEemwIKWfVmZGCcyV4XNEAjehGLIA9VnuCRzSC0HDNy6p0tDChJvEWjRVsqCvBWJ56sKjP-YvWxWfXdg>)

_Illustration by Simone Noronha_

Published: 2021-05-19T13:00:00

As developers build apps that work on more than one type of device, and with the introduction of new form factors like foldables, the need for responsive apps that work well at every size has never been greater. To that end, Material Design recently introduced a comprehensive update to layout guidance and components that expands the system’s adaptive capabilities to help prepare your apps for all kinds of screens.

To accompany [our talk at Google I/O 2021](https://events.google.com/io/session/b2f0dee6-b649-4e20-8ad6-69266e353f05?lng=en), this post will walk through 5 things to prioritize when adapting your app for large screens, without requiring a full redesign or rewrite.

## 1: Conceptualize your column grid

The first step in preparing your app is conceptualizing a column grid. If you’re building for the web, the notion of a column grid (and its importance) may already be familiar to you, but if not you may be wondering, “why use columns?” Columns are a great way to conceptualize interface designs for a few reasons.

First, they create a convenient structure for aligning elements of your layout. Column grids have been in use in print and graphic design for a long time, and that’s because they create the structure that print layouts need – helping make printed materials ergonomic and maximally readable. Printers have used and refined these layouts to help their users – readers or viewers – get the most out of the material, and we can use them to the same ends in interface design.

![Layout organized in a single column](<https://lh3.googleusercontent.com/HTJEmQvSXdNsI1MN7oJAPbkYfsf9y62V0p5ukJkUj1QuFv815XMIQXnwcQfTQMGFgCvf1Yy3PPCvGXk_N8OZaLwSk6X33DiFM4w39wAVWDUWem9z6PI>)

Layouts that consider the screen as one big canvas often arrange elements only in relation to one another, rather than to an underlying structure.

Right now, many apps consider the screen to consist of one big canvas or column, with elements primarily stacked on top of one another, or constrained to the left or right side of the layout, but ultimately drawn in relation to one another in horizontal and vertical terms. This approach can work well in small or narrow screens, but the problems begin to become obvious at larger sizes.

![Layout arranged into multiple columns](<https://lh3.googleusercontent.com/agyP5i_2bR0xJSQAvhpHz4LHTSs4yJAQcAoVR7E3FJzstYjiOwQ9XKMQknd7m0W353Lt2dscLdrkLJly1QTu1_T6V69EiDoCowSG0IRc1c6yKvQbNeA>)

A layout that uses a column grid can better organize elements on screen.

Applying a column grid to the layout allows for a large-screen experience that feels more intentionally organized. Elements on screen are organized relative to each other and the overall layout in a way that directs the user’s eye along a specified route according to the content’s informational hierarchy. This makes the experience feel more organic to the device and context.

![Large screen layout next to the same layout adapted for mobile](<https://lh3.googleusercontent.com/fPUTCNko0Reqxs5F1jBfEjfRoChAgxVvSLi9ZyY4D6BtM2wokuo9za5I1COjpVPLeEM3pgvmBm4pgipA7BmbXAll89Wvj6BtAAEjhXc9HCXnFHAoFgxC>)

The responsive column grid allows layouts to easily adapt across screen sizes.

The column grid also helps to respond across screen sizes, large and small. In our example (from Fortnightly, a Material Study app), we can see that the layout comprises three primary regions: the photo, the headline and metadata, and the body of the article.

Since each of these regions spans four or more columns in our large-screen layout, we can easily reflow them into a vertical orientation for mobile, maintaining the same information hierarchy along the way.

To start conceptualizing a column grid in your own layouts, grab our [Material Design Kit](https://www.figma.com/community/file/778763161265841481) from the Material Figma Community, as well as your latest mockups. Begin to move your elements onto a sample column grid from the Design Kit, exploring how the elements align.

![Example layout grid from the Material Design Kit](<https://lh3.googleusercontent.com/NcVgDQiMD8lmLG66Tg6tRF9aLzg6qe1kXOKBEb2APkBGYQJdkiRk300X6BxK9QlkBcArB8dXovECYR_b1yqqAUvROt5lbYSs-B3Y7b3KXgzVfdt7mA>)

Sample layout grids have columns, margins, and layout regions that correspond to Material guidance.

Check out our [_understanding layout_](https://material.io/design/layout/understanding-layout.html) and [_responsive column grid_](https://material.io/design/layout/responsive-layout-grid.html) spec articles for detailed information on designing for full responsiveness.

To implement a column grid you can use flexible layouts. When using a row you can specify weights or flex values for each column in the row to have it flex predictably. On android this can be accomplished using the RecyclerView by creating a horizontal chain. You can visit the [Android Developers site](https://developer.android.com/training/constraint-layout) to learn more.

## 2: Create a composition that scales

When adapting your designs for large screens, be sure to keep visual composition in mind. On a larger screen, information hierarchy is crucial. The greater screen space means that more information will naturally be present on screen. Since the information won’t all be vertically oriented, it’s important to create a visual composition that strengthens information hierarchy, giving users a sense of which elements are related – through style, proximity, etc. – and how they fit together in an interactive context.

#### Layout regions

One way that we think about this in Material is to construct a scaling layout using what we call layout regions.

![Diagram of three layout regions](<https://lh3.googleusercontent.com/p2g-lbdnIM7_hSDuAvZucHJdOEDbKG2QOKo4jxRIKCDao70r3MRuBvQ8ZVY_IQy1gh8bD0c11a1Cb38-_kcazdd5sz58P1Bi-MbLCDErZHquxPKPUt8>)

When working on guidance for large screens, we looked at the most common elements of an app’s overall layout, and came up with a way of thinking about them that allows for consistent responsive behavior.

We then divided the screen into three primary regions, with the first being the body region.

![Sample layout with only the body region](<https://lh3.googleusercontent.com/WAav5X47sOqP2CONOUM12XbgRJff3usN5e3cEDOIMXXo8Uq5Ul-0wyDNvVV-Fpx4JVQK8bhu_8LfDDXfyd5ZQVWD7LTmv_XCZhURN0l3wNNyBMth9w>)

Fortnightly's article content comprises its body region

In our example from Fortnightly, that would be the article content. This container is based on the column grid from before, and has flexible margins and widths defined by the breakpoints described in the [_responsive layout grid_](https://material.io/design/layout/responsive-layout-grid.html#breakpoints) article.

Next is the navigation region, which is comprised of navigational components to move the user between destinations and trigger key actions. It maintains a consistent width of 256dp when expanded and 72dp when collapsed. This container edges into the screen, condensing the dimensions of the body container.

![Sample layout with body and navigation regions](<https://lh3.googleusercontent.com/5InlToHTWzHokPZd74ZJD9rAKV_poZMvMzVtX6Ciwg4r83WMN38HC4rbfP5ipIV0B4IKOWMjl-m8aWXMAPKRvEdGnkxYs-Ke5DFom3foUWHlIFJXG-4>)

Fortnightly's navigation region allows users to move between sections of the app.

In our example, you can see how the body of the article is now slightly more narrow to accommodate the fully expanded navigation container.

Finally, we have the app bar, which gives users access to key actions like opening and closing the navigation region, searching, or moving to supporting destinations.

![Sample layout with body, navigation, and app bar regions](<https://lh3.googleusercontent.com/NqL6VO-rJcuYtBM2bUs1ai9c4-X_B5lAAC1Lm5DkKk_nAhTwHI5rvdmxTJiANygV3NrBMP3L3zwH3_E-Gy8O1wMdEawcpczpdf09lv2ADJw5IBVbcsUP>)

Fortnightly's app bar adds controls for the navigation region and other actions.

Now our example is feeling like a more complete and comprehensive layout. The three regions of the screen are clear, and we can reasonably predict how they’ll look at other sizes.

With the basics laid out, it’s important to reflect on the internal composition of these layout regions. As screen size increases, screen space increases as well. As more information appears on screen to fill this space, it becomes more important to give that information structure and hierarchy so that the user’s mental model of it is maintained.

We’ve identified two main techniques for grouping information inside these elements that can be helpful as you think about the layout of your own app.

#### Visual grouping

The first is visual grouping, or putting related elements together—whether they have similar content, functionality, or meaning—by the use of open space and typography.

![Sample layout with items visually grouped using open space and typography](<https://lh3.googleusercontent.com/NzQcEITLAASk0MiPUUSxu12yFwopV2RemkIS1mo5ahDV7G2vqszZLy6EJdx_IpxKncCwuaiWJLIyRKGD-nX3aFzIEzSh11qGAOMHhoH8iwtBNBAdOA>)

Fortnightly groups some news items using open space and typography.

You may already be using this technique in your app today if, for example, you’re using a ListView or GridView on Android, which can be combined with Material cards or tiles.

#### Containment

The second technique, containment, can be a more explicit approach that relies on clear boundaries, either through elevation or visible dividers, to achieve the same effect as the more implicit visual grouping.

![Sample layout with items grouped using explicit containers](<https://lh3.googleusercontent.com/rptD0QAcpqglEJpcQRzi0z4s-Pa6T_53kwZTh7q4OrcULe6OUtrI1o-nxNOLdl1qQtwh-Nkgh239G8iGsa-3vW1-yyxS1b6fqbZcZiUIQr_eYkrEDXs>)

Fortnightly groups some news items using explicit dividers.

Material offers many components that implement containment techniques by default.

## 3: Use the right components for the job

Adapting to large screens means adapting to the ergonomics and size constraints of more form factors. With our recent updates, articles for 14 Material components have been updated to describe how they behave when scaling across devices.

#### Visual presentation

The first method of adaptation described for Material components is “visual presentation.” This can include changes in scale and placement of the elements on screen, as well as their relationships to one another. For example, a text list on mobile may adjust its margins, vertical spacing, and density to more appropriately fill space on a tablet screen. This type of adaptation is crucial when moving between screen sizes, as it preserves the user’s understanding of how each element behaves and maintains usability.

One way this is implemented is through size constraints. Most Material components have minimum and maximum dimensions, and several components (including buttons, text fields, and sheets) have been updated for Android with corresponding default Max Width values.

![Diagram of snackbar size constraints on tablet and mobile screens](<https://lh3.googleusercontent.com/K7RzFHBBHTcIBQa1iMLVJXyVcZ6PkzOZ20flGZ1oa8jKXOYX6QPQbFYQ0znixG-5mrBwAYayfjTjsEGuTRaZfDgJ2HwkWHaz4VU3uHwjQhloCRA_wHM>)

Snackbars, for example, have minimum and maximum dimensions on small and large screens. On small screens, the snackbar can expand upward to accommodate its messaging, while on larger screens, it expands horizontally to keep its content on one line while utilizing available space.

![Diagram of a dialog at its maximum size](<https://lh3.googleusercontent.com/gs6kR69R30TyIQAd5y_YdWvvjnOFCareTuUwOCVZNXENRETEEJSX9-ZFCrDbZ-QqqLo60lHXA9LgH_99aR9NAIJqKV-G4Ex-qHaDFUI-iLZX5Tf5zuZy>)

Similarly, dialogs can expand horizontally on large screens to support their content, reaching a max width that keeps in mind the readability of longer line lengths. At this max width, it can expand vertically up to a maximum height.

As a developer, you can also take advantage of components’ ability to use fixed or fluid dimensions, based on what makes sense for your layout, within each component’s size constraints.

#### Component swapping

Component swapping is another, more rare type of adaptation in which functionally equivalent components can actually swap with each other to meet the ergonomic expectations of different devices.

[Video: Example UI adapting from mobile to large screen, swapping nav components](https://kstatic.googleusercontent.com/files/8e5938591ed55cfa290357cc229b0fc2610da447067aa88fbe80511863a265199aac605992e5fbf634c151dcbda70821d98bc62decfb8fc169e0e8cc5e6ec3f7)

For example, a bottom navigation bar may switch to a navigation rail on tablets, putting the same nav destinations at easier reach, and opening up more vertical space for your layout on horizontally-oriented screens.

The transformation may continue by swapping to a Navigation Drawer on larger screens to provide quicker access to secondary destinations, with more descriptive labels. On Flutter, this set of transformations can be implemented as an automatic behavior using the [adaptive\_navigation](https://pub.dev/packages/adaptive_navigation) package.

Components can switch types as well. For example, a full-screen dialog on mobile can  be exchanged with a simple dialog on larger screens. This component change maintains the function of the dialog, while also making use of screen space in a way that preserves a user’s context by revealing underlying content.

![Example of a full-screen dialog on mobile and simple dialog on tablet, both with the same content](<https://lh3.googleusercontent.com/uNzhLODJe2pcjJ96Kad-mLok84YJnT6izhbbUn4Py4dtISgV7J6oskQ2L9aY-GxqXuu64b31ALZB8nDGxHWSSVU-3XyA__gzRqJ6gfcw7B5PTGdRWjc>)

Use caution when swapping components by ensuring that the interchangeable components are functionally equivalent. Do not, for example, swap a button for a chip. Use caution when changing between list items and cards. The component swap should always serve a functional and ergonomic purpose for the user.

## 4: Ensure your app maintains continuity

When adapting your app for large screens, make sure the experience does not break when the device configuration changes. There are multiple events to look out for such as screen resize, orientation change, or fold and unfold.

Fold and unfold events will usually trigger the “screenLayout”, “screenSize” and “smallestScreenSize” events. Since your configuration is subject to change, it’s important not to cache or hardcode any values about display size, window size or orientation since they will change at runtime.

To ensure the best experience across configuration changes make sure to save info regarding scroll position, text that has been input in text fields, current component state such as a video playback position and other interactive aspects.

For more information on saving UI states in Android, check out the [Android Developers site](https://developer.android.com/topic/libraries/architecture/saving-states).

## 5: Test input support for touch, keyboard, mouse, and more

When adapting for large screens there will be many new forms of input to account for. For example, consider keyboards, mice and trackpads, and the ergonomics of touch input on different screens. When running on some large screens your app could even be interacted with exclusively through a gamepad or a remote control with a directional input.

If you're building for Android, the Android Developers site has more information on accounting for [game controllers](https://developer.android.com/training/game-controllers) as well as [mice/pointers](https://developer.android.com/training/gestures/movement).

Testing for these input types will allow you to keep in mind the particular expectations of each form factor. For example, a hardware keyboard will mean that part of your layout will no longer be hidden when a text field is active.

Finally, more input types will also mean more states to account for such as hover and focus.

Have more questions? Reach out to us [on Twitter @MaterialDesign](https://twitter.com/materialdesign) any time!
