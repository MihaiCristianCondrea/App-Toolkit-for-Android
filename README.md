# App Toolkit for Android

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
