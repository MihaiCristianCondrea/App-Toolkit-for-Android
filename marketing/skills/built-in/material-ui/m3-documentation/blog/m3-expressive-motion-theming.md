---
title: "Adding Motion Physics with Jetpack Compose"
source_url: "https://m3.material.io/blog/m3-expressive-motion-theming"
lastmod: "2026-05-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/1ded508971c3-Jq3O_D_spnxd-2_CxeWYFAgtXFYlUS-FT4FUiLdPzRa2pnXVq8IllmGem9q4q1lY1YaLkGRTST_fIPpbuCr40sI987QAmJD1.png"
  - "assets/images/4ec49be76949-nr4Lzg90Pi43VPk19ydsKZogx59MyiQ29sO0PK7DUhD6g7npr2PYQQ3wN7CMQH4x5lk12Y0dSBpTdNsTam05eJKKR3Pt8RCI.png"
  - "assets/images/8088ccbd4359-UvAI0qHlQhpz1tV9K0xAnalQMay8smnTOrGJAVUPG3oDxcGEVJPht01G-4OLy_0YzcddIPEnnvmVmh5DmOgPYoF3wpskglbw.png"
  - "assets/images/36767ab4c104-3zyb7Zu4E4U17JPWpN2XB5iExsTn67ovGkBG4o8_H40q7ZxgoWbozkaDVakbPWYtUHT-_rFbi5sZGTjbTuwNk5NrwCFgcvLT.png"
  - "assets/images/9c2d9b4fc56d-N8Th_8EIKMq-xMouwQQ2tjtbO2XdY1U-yDojksW9rYgzx3segowuOv492aKXtpaOtTnGtmfGFkw7PGYVNi9A0dfwiDk3ZRf3.png"
  - "assets/images/ff305a9e857f-l56ZHFumvW8u3zLLBExCxaTeFCYIiALm2-Tgp7gxQJgkY_YcUdeJWCFK3ahROWwkUkKbASY_-TiZwezM34pCaKyqo6wtMhbt.png"
  - "assets/images/46937504787d-biWfMF0JCrTW9s3TaFGTBmRGsvdRH_Y2scaa6In-W7lNKF_YaAozCEl5qS03zYQ-FnORST43I32tg1YmOCCVCTbfd2oNQYCa.png"
  - "assets/videos/ec18b971754b-040925_Motion_Physics_16-9-aspect.mp4"
  - "assets/videos/5f474a07a546-Scheme_Spring_Expressive_Export.mp4"
  - "assets/videos/54f759e05041-Scheme_Spring_Standard_Export.mp4"
  - "assets/videos/4bc1d43c11d2-AllSpeeds_Standard_Export.mp4"
  - "assets/videos/136c314546bf-AllSpeeds_Expressive_Export.mp4"
  - "assets/videos/18edb9b21f4f-00_compare_tween.mp4"
  - "assets/videos/fa690358d4ec-00_compare_spring.mp4"
  - "assets/videos/07ed363db37e-Customization_Fab_Instant_Export.mp4"
  - "assets/videos/8cf0fdbce8bd-Customization_Fab_Soft_Export.mp4"
---

# Adding Motion Physics with Jetpack Compose

Supercharge your Android transitions and animations with the new M3 Expressive motion theming system.

![Image](<https://lh3.googleusercontent.com/Jq3O_D_spnxd-2_CxeWYFAgtXFYlUS-FT4FUiLdPzRa2pnXVq8IllmGem9q4q1lY1YaLkGRTST_fIPpbuCr40sI987QAmJD13DqpdFV7VUc3vkwaDA>)

Published: 2025-05-20T08:00:00

Material Design has an exciting new preview release — [Material 3 Expressive](building-with-m3-expressive.md). In this latest version 1.4.0-alpha14 of Material 3, you get access to a new theming system: motion!

Previously, Material motion was defined through non-customizable easing and duration values. Today, we’re introducing a new, customizable motion scheme using motion physics defined through a set of motion properties. These can be customized or overridden as needed, giving you more control than ever over how motion works and feels in your product.

Read on to learn how the new motion physics scheme works, how existing APIs have changed, and how to get started.

[Video: Video](https://kstatic.googleusercontent.com/files/ccb9b474586dcb0579532da1df0fd8ac33b2ec8e62e75052f509eef9d0949599e465b3c8991e0d890a62b33f86cc7789d87507ca642a7690522be97e5273085b)

## Why Material motion?

As a product developer, you know that motion can significantly enhance the user experience. But achieving consistent motion across a complex app can be challenging. The new Material motion system solves this with:

-   **Centralized control**: Define your motion theme once, and all Material Components and even your own custom components will inherit it, creating a unified and polished experience, eliminating scattered animation specs.


-   **Simplified theming**: Stop fussing with individual duration or easing values. Choose from predefined schemes or create your own, using physics-based spring animations for a natural and engaging feel.


-   **Adaptive animations**: Ensure that movement feels fast in the context of the device and adjusts based on user input since animations are not based on predefined time sets.

![Image](<https://lh3.googleusercontent.com/nr4Lzg90Pi43VPk19ydsKZogx59MyiQ29sO0PK7DUhD6g7npr2PYQQ3wN7CMQH4x5lk12Y0dSBpTdNsTam05eJKKR3Pt8RCIF8ETqUs0U3hk3DLFnw>)

## Getting setup

First, make sure you’re using the latest version of Compose Material 3:

If you do nothing more than update the Compose dependency, your apps will benefit from applying the standard motion scheme to all your uses of Material Components. But if you do want that extra bit of control, you can customize your scheme to suit your app.

_Note: When 1.4.0 goes to stable, the Material Expressive APIs will move to the next alpha (1.5.0-alphaX), and will no longer be available in 1.4.0. The APIs will go stable in the 1.5.0 release._ 

## Fundamentals of the motion system

### Motion schemes: Expressive or Standard

The physics system has two preset motion schemes: **Expressive** and **Standard**. The scheme you choose will define how your product feels. While most motion in a product should use the same scheme, advanced customizations allow you to swap the scheme to emphasize key moments.

-   **Expressive** is Material’s recommended motion scheme, and should be used for most situations, particularly hero moments and key interactions.

-   **Standard**, with its small amount of bounce, feels more functional and should be used for utilitarian products.


[Video: Expressive: The Expressive motion scheme overshoots the final values to add bounce.](https://kstatic.googleusercontent.com/files/7db862ca6e52d1940e5d07fd94c8be8d9104776c1fd7cca1a64a9e2e3057c1bd2e8f88da66dcba4c8a128fc58603a81c49aa69a79ba210ed998596c8755cb81f)

[Video: Standard: The Standard motion scheme eases into the final values.](https://kstatic.googleusercontent.com/files/0e85f916f4faa031d36b03fa119752471791cba6ba6b913d8dac1f560cb7b6bca61d99f36bc855c1102384bed4bcff97a4843a63ef7cf2f1bc7750dddf627a77)

## Animation specs: Spatial or Effect

Two distinct kinds of specifications make up the motion scheme: **Spatial** animation specs and **Effect** animation specs.

**Spatial** specs are used to animate changes in an object's position, orientation, size, and shape. The spring overshoots the final value and bounces into place.

![Spatial springs applied to movement](<https://lh3.googleusercontent.com/UvAI0qHlQhpz1tV9K0xAnalQMay8smnTOrGJAVUPG3oDxcGEVJPht01G-4OLy_0YzcddIPEnnvmVmh5DmOgPYoF3wpskglbwSsp10Gnz79cTXYYBdZ0>)

Spatial springs applied to movement.

![Spatial springs applied to rotation](<https://lh3.googleusercontent.com/3zyb7Zu4E4U17JPWpN2XB5iExsTn67ovGkBG4o8_H40q7ZxgoWbozkaDVakbPWYtUHT-_rFbi5sZGTjbTuwNk5NrwCFgcvLTD2peC-m7xCOuR59epQ>)

Spatial springs applied to rotation.

**Effect** specs are used to animate an object’s properties such as color and opacity, where there shouldn’t be any overshoot.

![Effects springs applied to opacity](<https://lh3.googleusercontent.com/N8Th_8EIKMq-xMouwQQ2tjtbO2XdY1U-yDojksW9rYgzx3segowuOv492aKXtpaOtTnGtmfGFkw7PGYVNi9A0dfwiDk3ZRf3NdNK5qyQB95W2MDo9b0>)

Effects springs applied to opacity.

![Effects springs applied to color](<https://lh3.googleusercontent.com/l56ZHFumvW8u3zLLBExCxaTeFCYIiALm2-Tgp7gxQJgkY_YcUdeJWCFK3ahROWwkUkKbASY_--TiZwezM34pCaKyqo6wtMhbtxG3qrfkcRXitIQ3F8c>)

Effects springs applied to color.

Each animation can also have one of three speeds: **default**, **fast**, and **slow**. Most motion should use the default speed, but smaller elements may benefit from the fast speed and larger elements from the slow.

| Speed | Spatial example | Effects example |
| --- | --- | --- |
| Default | Animations that partially cover the screen, such as [bottom sheets](../components/bottom-sheets/overview.md) or [expanded navigation rails](../components/navigation-rail/overview.md) | Opacity of the content within a navigation rail |
| Fast | Animations for small components such as [switches](../components/switch/overview.md) and [buttons](../components/all-buttons.md) | Color change of the switch handle |
| Slow | Full-screen [animations](../styles/motion/transitions/transition-patterns.md) | Full-screen content refresh |

**Speed tokens work across devices**. For example, the **Spatial “fast”** token will always be faster than **“default”** or **“slow,”** but the exact values of each token will differ depending on whether the device is a wearable, phone, or tablet. This ensures the movement feels fast in the context of the device. This also applies when using spring tokens in a custom motion scheme.

[Video: Effects motion in fast, default, and slow speeds](https://kstatic.googleusercontent.com/files/d62cb289af2e55417fabf599bf536c7c178fc97d1e24f608d347e3ef248591ec08e92d97b4fed5812454d7fbb99584919b9fc0684c3416841a5d7cd9435e0c1c)

[Video: Spatial motion in fast, default, and slow speeds](https://kstatic.googleusercontent.com/files/91bd68f49329126d9f25ff901319506995864fa01834449d92b78bc077297c94d1d332c8cb755eb13e3e1155b3bb84cff8360a53548ea994d0e518897226674a)

Notice that the Expressive and Standard schemes are presets of opinionated motion values. This makes it easier to swap schemes without changing the underlying property names.

![Expressive and Standard MotionScheme](<https://lh3.googleusercontent.com/biWfMF0JCrTW9s3TaFGTBmRGsvdRH_Y2scaa6In-W7lNKF_YaAozCEl5qS03zYQ-FnORST43I32tg1YmOCCVCTbfd2oNQYCaDQeMC1bDLc834dwf0Go>)

Expressive and Standard MotionScheme

## Why use motion springs?

If you look at the predefined specifications on the motion schemes, you may notice that springs are used for the backing specifications. Why is this? Spring animations appear more natural by allowing for the ability to interrupt and retarget the animation when required.

For example, consider the difference between using tween and springs:

[Video: Tween interruptions.](https://kstatic.googleusercontent.com/files/8d28ee2d908d757bac49c82d50da3f34cb0c8078ac6acfe18387d068fc90b86bc5698e7b877b2a00050a0bddc749094aeb00badd9a06a816a6b3234496806000)

[Video: Spring interruptions.](https://kstatic.googleusercontent.com/files/8b82ac87cc4b567f7ef613879db379ae180cad58ab7e83cd59a0e42f0cf33d6d32369bf3dd136a56496535906836ba9dd173becdfc9f8adef5f13c92e72f1d8c)

When interrupted and retargeted to a new destination, the spring animation uses its current velocity to perform a more seamless transition between the two states than tween.

It’s also beneficial to use springs to ensure your animations easily **adapt to different screen sizes**, as the tokens will always be slow or fast in the context of the device since it is not time that is specified  but rather damping and stiffness. For more details on the benefits of springs, check out the [documentation](https://developer.android.com/develop/ui/compose/animation/customize#spring?utm_source=blog&utm_medium=motion&utm_campaign=IO25).

## Customizing your motion scheme

The real power of the new system lies in its flexibility. You can tailor the motion scheme to reflect your app’s brand or to underscore an important interaction.

### Choosing between Standard and Expressive schemes

Most developers won’t want to change much in terms of motion theming, but there are two out-of-the-box options for you to choose from: `Standard` or `Expressive`. Choosing `MaterialExpressiveTheme` at the top level will default to the `Expressive` motion scheme.

Make this choice once for your application at the theme level, and any Material Components with movement built in will use the selected spec from the theme.

With the `expressive()` scheme your apps take on the Material-recommended motion scheme, whereas with the `standard()`, the motion is, well, pretty standard — there is a minimal amount of bounce, even as both use springs under the hood.

### Creating your own Motion scheme

For more fine-grained control, you can create your own `MotionScheme` object and return a different `AnimationSpec` for each property.

In the code snippet below, we create a whole new `playfulMotionScheme` that by default adds a lot of bounce to components. This demonstrates how you can customize your `MotionScheme`.

[Video: FAB menu with an extra-stiff custom scheme.](https://kstatic.googleusercontent.com/files/3184635283c35a0300e2ce204951402677a6fb1baee9c5024a8dafacfb0df18a2e38ec510167e4d1391a6dd3461e3e565eb3ef75d0c39f7e85c231a56092a915)

[Video: FAB menu with very low stiffness custom scheme.](https://kstatic.googleusercontent.com/files/3bae4ef379c27d4b1a3a3241dcde090f8833233e5b394b7ba6d352639cf95ed9b1bc30d546421a750ba803584de991c11e9649613b4cde65f02afdeab58a5ede)

## Custom component animations

To maintain consistency with other Material Components, ensure your custom components adopt the recommended motion changes and use the motion specs available through `MaterialTheme.motionScheme`.

For example, the following code uses `tween()` calls to animate the size and color of a text component when pressing and releasing its container:

Since `scale` represents a `Spatial` motion and `color` falls under `Effects` motion, we can leverage the motion scheme for consistent application. This approach aligns your component’s motion to the overall app experience and allows it to adapt automatically when switching between `Standard` and `Expressive` schemes/themes.

## Individual Material Component changes

In the expressive update, most Material 3 components use the motion physics system by default.

To add the motion-physics system to other components, including those that are custom built, use the specifications from above. See the full [M3 Expressive announcement blog post](building-with-m3-expressive.md) for more details on the new components.

## Get started today

Try out the new motion subsystem in Material 3 Expressive today, and let us know how you like it by tagging us on social media @GoogleDesign. If you find any issues, please file them on the [issue tracker.](https://b.corp.google.com/issues/new?component=742043&template=1590761?utm_source=blog&utm_medium=referral&utm_campaign=IO25)

Happy theming!

_Special thanks to Material motion designers Gus Winkelman and Gustavo Gonzalez._
