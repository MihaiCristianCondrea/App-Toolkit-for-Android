# Getting Started

App Toolkit gives your Android app a Compose shell, shared settings, and navigation
contracts. This guide follows the APIs on the `develop` branch. Use the version
published for your app when choosing a JitPack dependency.

## Add App Toolkit

Add JitPack to the dependency repositories in `settings.gradle.kts`.

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

Add the library to your application module. Replace `<version>` with your published
App Toolkit version.

```kotlin
dependencies {
    implementation("com.github.MihaiCristianCondrea.App-Toolkit-for-Android:apptoolkit:<version>")
}
```

## Connect the host application

1. Add `appToolkitModules(...)` and your app's provider bindings to the host Koin graph.
2. Use `toolkitGraph { ... }` to combine your app's destinations with Toolkit pages.
3. Pass the graph to `ShellHost` inside `AppTheme` in the host activity.

See [Shell and Navigation](shell-and-navigation.html) for how the graph, host, and
navigation fit together. The [Toolkit module README](https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/blob/develop/library/apptoolkit/README.md)
covers provider bindings and host configuration.
