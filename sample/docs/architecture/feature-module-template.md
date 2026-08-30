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

## Documentation

Every feature module must have a `README.md` that documents ownership, dependencies, public
contracts, important flows, and current risks. Follow the repository's
[module boundaries](module-boundaries.md).
