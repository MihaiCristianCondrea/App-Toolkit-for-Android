![App Toolkit for Android](/app/src/main/play/listings/en-US/graphics/feature-graphic/play_store_feature_graphic.png "App Toolkit for Android")

<a href="https://play.google.com/store/apps/details?id=com.d4rk.android.apps.apptoolkit"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>

App Toolkit for Android
==================

**Explore smart components that powers all of our Android apps.**

App Toolkit is a clean and lightweight demo app that showcases the reusable screens, components, and
architecture powering my Android projects.

It includes a live preview of all the shared UI elements I’ve built for my apps — such as Settings,
Help, Support, and more — as well as a dynamic list of my published apps from Google Play.

Whether you’re a developer, designer, or just curious about how modern Android apps are structured,
App Toolkit gives you a hands-on look at the foundational UI blocks behind my work.

Our app is designed to be simple and easy to use, while also being fast and lightweight. Plus, it's
free and open-source software!

# Features

- Preview reusable screens
- Lists all my published apps
- Launch apps or open Play Store
- Dynamic content
- Supports Material You theming

# Benefits

- See how shared components work
- Build your own UI toolkit faster
- Discover my other apps
- Explore real, modular app structure

# Screenshots

<!--suppress CheckImageSize -->
<table>
  <tr>
    <td><img src="/app/src/main/play/listings/en-US/graphics/phone-screenshots/1-screenshot_welcome.png" width="300" alt="Screenshot Welcome"></td>
    <td><img src="/app/src/main/play/listings/en-US/graphics/phone-screenshots/2-screenshot_apps.png" width="300" alt="Screenshot Apps"></td>
    <td><img src="/app/src/main/play/listings/en-US/graphics/phone-screenshots/3-screenshot_onboarding_theme.png" width="300" alt="Screenshot Onboarding Theme"></td>
  </tr>
</table>

# How it works

App Toolkit uses a modular architecture with a shared core that powers every screen. The home screen
dynamically fetches all apps I’ve published on Google Play and allows you to open or install them
with one tap. Every screen is live and functional — just like it appears in real apps.

# Get started today

Download App Toolkit from the Google Play Store and explore the internal structure of real Android
apps. It's free, easy to navigate, and the perfect way to discover how reusable design can elevate
any project.

# Use the library in your project

[![Release](https://jitpack.io/v/MihaiCristianCondrea/App-Toolkit-for-Android.svg)](https://jitpack.io/#MihaiCristianCondrea/App-Toolkit-for-Android)

App Toolkit’s shared components are available exclusively through JitPack. Add the JitPack
repository
at the end of your list so Gradle resolves other dependencies first, and scope lookups to this
library’s coordinates for faster resolution.

### Gradle (Kotlin DSL)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl(url = "https://jitpack.io")
        }
    }
}

dependencies {
    implementation("com.github.MihaiCristianCondrea:apptoolkit:2.0.1")
}
```

# Feedback

We are constantly updating and improving App Toolkit to give you the best possible experience. If you
have any suggested features or improvements, please leave a review. In case something is not working
correctly please let me know. When posting a low rating please describe what is wrong to give the
possibility to fix that issue.

Thank you for choosing App Toolkit! We hope you enjoy using our app as much as we enjoyed creating it
for you!

# License

__Privacy Policy__ [here](https://mihaicristiancondrea.github.io/profile/#privacy-policy-end-user-software).
__Terms of Service__ [here](https://mihaicristiancondrea.github.io/profile/#terms-of-service-end-user-software).

![license](https://imgur.com/QQlcEVT.png)
