---
title: "Material Design Components for Android 1.5.0"
source_url: "https://m3.material.io/blog/android-stable-release-1-5"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/8ade652e5ca8-S2BoUveRJNGtg2eQEJX4WGF0_PoalerbcfsvmjePTcqg0kK5aDB3i4mfUnueIPOatM6RKVWHbbhSAaB7NoaGshb9IfryPGKl.png"
  - "assets/images/3c3282821f6b-tRsZvSk4VuvHJmxG6czy4eljvcVjV2RmPxQ5UBfkMuLy_vuzMrbLyfs2CNYY7G0Mc9qrfGaeMTlcPCVkK_G_ZaqSwOdB68h7.png"
  - "assets/images/39e3a36cbea2-xwFFAMhc52NEO9Ke0v_ysbqXlppp3hRlqc5bXBZx4Z015-y41-Yt7x-GeRZnqqoelzz222yIt-05A8StJzEf6qafjl06b2qv.png"
  - "assets/images/2730e740122c-H-6OuN26_CI982zVAnuoad9l-fERsmDObwT9h-gi24QZDbOnsIbsMtxdMJUiF9v1qN96NW8qILKy3b9StPsRjPj-Y1iofgU8.png"
  - "assets/images/6bab24dd5881-ropJhw9RhD5BedUUPJKIAgN0XzOj7Ooe0T0A7bD7pwj91son28V9Ntuutets019kc4Jte-OGAvcAkg4A-1l3B8UpqVJqqlEn.png"
  - "assets/images/6d1a681c4b4d-63qXR7bKdN0wh6-P1i4HlFvSerCFRLTPWZ4KzbDHSUSsvvh1hMyuZX8f3OzdWmwriX2uqVy2-hzRo3nRy8hfszPbvyZ10UGA.png"
  - "assets/images/4eb288cff5ed-wHVjc6HRf1Ma-Igg2EpY2LJAwKTP48lQ7q6o5qJAcoPaKr2NaI4MePW1c2X70W0-lcRb6uIKv_J0UrzdbPKbIYs_vSTitINl.png"
  - "assets/images/7d85f886f056-caEM6pKSg8hUfzEy0V2gmat5t6d5dMrHqtge5_FjI5OcA01lp_dj8FjcsPXN8L5lORmolB3MpwaKsGFcrox95enE8s3ZnQZr.png"
---

# Material Design Components for Android 1.5.0

With Material Design 3 refinements and more color utilities

![Illustration of a plant sprouting through an abstract form](<https://lh3.googleusercontent.com/S2BoUveRJNGtg2eQEJX4WGF0_PoalerbcfsvmjePTcqg0kK5aDB3i4mfUnueIPOatM6RKVWHbbhSAaB7NoaGshb9IfryPGKlfL2TTaHdNjS6qw3_6ZA>)

Published: 2022-02-17T13:00:00

Back in October, at [Android Dev Summit](https://developer.android.com/events/dev-summit), we released a preview of Material Design 3 for Material Design Components (MDC).

Now we’re ready to announce the stable release of MDC 1.5.0 with more Material Design 3 support, component fixes and more color utilities. You can check out the release notes [here](https://github.com/material-components/material-components-android/releases/tag/1.5.0).

If you held off on migrating last year, now is a great time to explore the new design system. Beyond the resources listed above, some areas you should focus your attention on are the Material 3 Catalog App and extended color utilities.

## Material 3 Catalog App

Our engineering team uses the Material Catalog app to verify and show example implementations of new components and features relating to Material Design. It’s a great source for discovering the new system.

If you haven’t yet migrated to Material 3 and dynamic color, the catalog app offers a settings area allowing you to see how a default light, dark, or user-generated theme will look throughout the app.

![Home screen of Material Components Catalog App](<https://lh3.googleusercontent.com/tRsZvSk4VuvHJmxG6czy4eljvcVjV2RmPxQ5UBfkMuLy_vuzMrbLyfs2CNYY7G0Mc9qrfGaeMTlcPCVkK_G_ZaqSwOdB68h7Tgvtq6OH21nzcMR2QGg>)

![Theme and Dynamic Color Settings Selection in Material Components Catalog App](<https://lh3.googleusercontent.com/xwFFAMhc52NEO9Ke0v_ysbqXlppp3hRlqc5bXBZx4Z015-y41-Yt7x-GeRZnqqoelzz222yIt-05A8StJzEf6qafjl06b2qvp9PM-7FPDIvN19enI9Ob>)

You can download the apk file [here](https://github.com/material-components/material-components-android/releases/download/1.5.0/catalog-debug.apk). For each screen, there’s also source code to [check out](https://github.com/material-components/material-components-android/tree/master/catalog/java/io/material/catalog), organized by component.

## Extended Color Utilities

Also at Android Dev Summit, we launched the Material Theme Builder for [Figma](https://www.figma.com/community/plugin/1034969338659738588/Material-Theme-Builder) and [the web](https://material-foundation.github.io/material-theme-builder/). It provides designers and developers a means to experiment with dynamic color and Material 3 themes with the option to export as code.

![Default UI of Material Theme Builder](<https://lh3.googleusercontent.com/H-6OuN26_CI982zVAnuoad9l-fERsmDObwT9h-gi24QZDbOnsIbsMtxdMJUiF9v1qN96NW8qILKy3b9StPsRjPj-Y1iofgU8Y4wJD_5FoJ61062OWT9r>)

We've been pleasantly surprised by the community adoption of Material Theme Builder. We've also heard from you that you want to incorporate color roles from more than the standard key colors. At present, we only export the seed color for each extended (non-key) color, despite showing a visualization of them.

![Material Theme Builder view with extended colors visualized](<https://lh3.googleusercontent.com/ropJhw9RhD5BedUUPJKIAgN0XzOj7Ooe0T0A7bD7pwj91son28V9Ntuutets019kc4Jte-OGAvcAkg4A-1l3B8UpqVJqqlEnSR5xQl0W__LWrXRGhqA>)

In anticipation of more color features coming in the library, MDC 1.5.0 has a function in the MaterialColors class named `getColorRoles` that will return:

-   `accent`
-   `onAccentColor`
-   `accentContainer`
-   `onAccentContainer`

The full signature is as follows:

With this ColorRoles object, you could retheme a component at runtime. In the following snippet, we change the colorContainer and onColorContainer values for a button. Always remember to reassess the onColorContainer(text) tone when altering the colorContainer tone to ensure text has proper contrast and readability.

![Android sample project with no modifications applied](<https://lh3.googleusercontent.com/63qXR7bKdN0wh6-P1i4HlFvSerCFRLTPWZ4KzbDHSUSsvvh1hMyuZX8f3OzdWmwriX2uqVy2-hzRo3nRy8hfszPbvyZ10UGAH2dOUYOG5DxWbGAGEZA>)

![Image](<https://lh3.googleusercontent.com/wHVjc6HRf1Ma-Igg2EpY2LJAwKTP48lQ7q6o5qJAcoPaKr2NaI4MePW1c2X70W0-lcRb6uIKv_J0UrzdbPKbIYs_vSTitINl9DDhVkVjkXjjjsUl5Lo>)

![Runtime generation of green light theme color roles applied to a button](<https://lh3.googleusercontent.com/caEM6pKSg8hUfzEy0V2gmat5t6d5dMrHqtge5_FjI5OcA01lp_dj8FjcsPXN8L5lORmolB3MpwaKsGFcrox95enE8s3ZnQZreFNSSecHTHWdB5H1eRc>)

## What’s next for MDC?

We’re fast at work on the next major version of MDC. You can follow the progress, file [bug reports](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BComponent+name%5D+Short+description+of+issue) and [feature requests](https://github.com/material-components/material-components-android/issues/new?assignees=&labels=feature+request&template=feature_request.md&title=%5BComponent+name%5D+Short+description+of+request) on GitHub. Also feel free to reach out to us on Twitter [@materialdesign](https://twitter.com/materialdesign).
