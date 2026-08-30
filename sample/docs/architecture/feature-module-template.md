# Feature Module Template

This document describes the recommended structure for a new feature module in the App Toolkit Sample.

## Directory Structure

```text
feature/my-feature/
├── src/main/kotlin/com/.../feature/myfeature/
│   ├── di/
│   │   └── MyFeatureModule.kt      # Koin module definition
│   ├── ui/
│   │   ├── MyScreen.kt             # Main entry point composable
│   │   ├── MyViewModel.kt          # Feature ViewModel
│   │   ├── contracts/              # Action/Event/UiState
│   │   └── views/                  # Smaller UI components
│   └── data/                       # Feature-specific repositories/datasources
├── src/main/res/                   # Feature resources (strings, icons)
├── build.gradle.kts                # Module build script
└── README.md                       # Module documentation
```

## Build Script

New feature modules should apply the `sample-module` plugin:

```kotlin
plugins {
    id("com.mihaicristiancondrea.android.apptoolkit.sample-module")
}

android {
    namespace = "com.mihaicristiancondrea.android.apps.apptoolkit.feature.myfeature"
}

dependencies {
    // Only core dependencies, never sibling features
    api(project(":sample:core:navigation"))
    api(project(":sample:core:ui"))
}
```

## Registering with the shell

A feature is invisible until it contributes itself. Both contracts live in
`:sample:core:navigation` and are aggregated by the shell with Koin's `getAll()`, so each binding
needs a qualifier unique to the feature — two unqualified bindings of the same type override each
other and the lost entry fails silently.

```kotlin
val myFeatureModule: Module = module {
    // Drawer entry, optionally conditional.
    single<NavigationItemContribution>(qualifier = named(name = "my_feature")) { ... }

    // Selectable startup screen in Settings.
    single<StartupScreenContribution>(qualifier = named(name = "my_feature")) { ... }
}
```

## Documentation

Every feature module must have a `README.md` that documents ownership, dependencies, public
contracts, important flows, and current risks. Follow the repository's
[module boundaries](module-boundaries.md).
