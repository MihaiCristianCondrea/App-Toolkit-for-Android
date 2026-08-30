# App Toolkit for Android

<p>
  <img alt="Android" height="28" src="https://ziadoua.github.io/m3-Markdown-Badges/badges/Android/android1.svg">
  <img alt="Kotlin" height="28" src="https://ziadoua.github.io/m3-Markdown-Badges/badges/Kotlin/kotlin1.svg">
  <img alt="Android Studio" height="28" src="https://ziadoua.github.io/m3-Markdown-Badges/badges/AndroidStudio/androidstudio1.svg">
  <img alt="GitHub" height="28" src="https://ziadoua.github.io/m3-Markdown-Badges/badges/Github/github1.svg">
  <img alt="GPLv3 license" height="28" src="https://ziadoua.github.io/m3-Markdown-Badges/badges/LicenceGPLv3/licencegplv31.svg">
</p>

[![Android CI](https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/actions/workflows/android.yml/badge.svg)](https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/actions/workflows/android.yml)
[![Release](https://jitpack.io/v/MihaiCristianCondrea/App-Toolkit-for-Android.svg)](https://jitpack.io/#MihaiCristianCondrea/App-Toolkit-for-Android)

App Toolkit for Android is a modular collection of reusable Android foundations, feature screens,
integrations, and navigation components. The repository also includes an installable sample app that
demonstrates the library in a real application.

## Repository structure

- [`library/apptoolkit`](library/apptoolkit/README.md) is the host-facing façade. It exports the
  toolkit modules and assembles their Koin modules and Navigation 3 destinations.
- [`library/core`](library/core/README.md) contains shared models, data, design-system, networking,
  testing, and UI foundations.
- [`library/feature`](library/feature/README.md) contains reusable About, Help, Issue Reporter,
  Onboarding, Permissions, Settings, and Support features.
- [`library/integration`](library/integration/README.md) contains optional integrations for ads,
  billing, consent, Firebase, in-app review, and in-app updates.
- [`library/navigation`](library/navigation/README.md) provides shared navigation contracts, models,
  UI, and back-stack helpers.
- [`sample`](sample/README.md) documents the sample application and its user-facing features.

Each module has a README describing its responsibilities, dependencies, public contracts, and known
risks.

## Use the library

App Toolkit is published through [JitPack](https://jitpack.io/#MihaiCristianCondrea/App-Toolkit-for-Android).
Add JitPack to the dependency repositories in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

Then add the façade artifact to the consuming module:

```kotlin
dependencies {
    implementation("com.github.MihaiCristianCondrea.App-Toolkit-for-Android:apptoolkit:3.0.0-pre10")
}
```

The published version is maintained as `PUBLISHING_VERSION` in [`gradle.properties`](gradle.properties).
For host configuration, dependency-injection entry points, and navigation integration details, see
the [`:library:apptoolkit` documentation](library/apptoolkit/README.md).

## Explore the sample

The sample app is both a component showcase and a collection of practical utilities. Its feature
list, screenshots, Play Store link, and usage overview now live in the
[`sample` README](sample/README.md). Implementation details for its application composition root are
in [`sample/app/README.md`](sample/app/README.md).


# Sample Architecture

App Toolkit uses a modular, layered architecture designed for scalability and maintainability.

The sample follows one dependency direction: `:sample:app` composes independent feature, core,
integration, and widget modules. Reusable modules never depend back on the app.

| Module type | Path                    | Responsibility                                                                                        |
|-------------|-------------------------|-------------------------------------------------------------------------------------------------------|
| App         | `:sample:app`           | Android packaging, startup, the complete DI graph, navigation aggregation, and cross-feature bridges. |
| Core        | `:sample:core:*`        | Neutral contracts and capabilities reused by multiple features.                                       |
| Feature     | `:sample:feature:*`     | A user-facing vertical slice, including its routes, state, data ownership, and DI bindings.           |
| Integration | `:sample:integration:*` | Host configuration for an external or reusable SDK boundary.                                          |
| Widget      | `:sample:widget`        | Home-screen widget UI, receiver, and widget-specific data access.                                     |

Each module has a local `README.md` that documents its ownership, dependencies, public contracts,
important flows, and current risks. Package trees start at the module namespace. Feature modules
organize implementation directly into the layers they actually use:

```text
feature/my-feature/src/main/kotlin/com/.../feature/myfeature/
├── data/       # repositories, data sources, data models, and data mappings
├── domain/     # optional domain models, mappings, and use cases
├── ui/         # screens, ViewModels, state, navigation, and UI components
└── di/         # dependency injection bindings
```

Do not add empty layers. Repository contracts stay beside their implementations in `data`, and a
domain layer is added only when the feature has domain-specific models or reusable business logic.
New feature modules use the `com.mihaicristiancondrea.android.apptoolkit.sample-module` convention
plugin and depend only on the core, integration, and library modules they need. Features never
depend on sibling features.

A feature owns and exports its route key, route identifier, screen, and Koin module. It does not
register itself. `:sample:app` aggregates navigation entry builders, drawer items, bottom-bar items,
startup choices, and cross-feature adapters because it is the only module allowed to see the whole
feature set. Route identifiers are declared once and referenced through their route constants.

The build enforces these boundaries:

- Core and integration modules cannot depend on feature modules.
- Feature modules cannot depend on sibling features.
- Core, feature, and integration modules cannot depend on `:sample:app`.
- Kotlin packages cannot be split across sample modules.
- App composition packages cannot be imported from reusable sample modules.
- Core navigation cannot import feature implementations.
- Analytics screen names come from `AppScreenTracking`, not inline literals.

Run `./gradlew checkModuleBoundaries` to execute the repository-wide source checks. These targeted
guards complement compilation, unit tests, lint, and on-device validation.

The current modularization keeps runtime and tests on the same `sampleAppModules` DI declaration.
Feature Android components are declared by their owning manifests, advertising configuration lives
in `:sample:integration:ads`, and product-specific analytics identifiers live in
`:sample:core:analytics`. The app remains the intentional merge point for the final manifest,
runtime graph, and complete destination set. This can produce conflicts when several destinations
are added at once, and the shared sample convention still enables Compose for contract-only sample
modules. Existing boundary checks reduce these risks but do not replace feature-specific tests.

## Build and test

This project uses the Gradle wrapper. From the repository root:

```bash
./gradlew build
```

Run the sample app with Android Studio or install a debug build on a connected device with:

```bash
./gradlew :sample:app:installDebug
```

## Contributing

Please read the [Code of Conduct](CODE_OF_CONDUCT.md) and follow the existing module architecture and
conventions when proposing changes. Security issues should be reported according to
[SECURITY.md](SECURITY.md).

## License

This project is distributed under the GNU General Public License v3.0. See [LICENSE.md](LICENSE.md)
for the complete terms.
