# App Versioning System

This project uses a custom Gradle versioning plugin that centralizes release metadata in a root `release.properties` file. This ensures consistent versioning across modules and automates `versionCode` and `versionName` generation.

## 1. Source of Truth: `release.properties`

The `release.properties` file at the root of the project stores the following metadata:
- **SDK Versions:** `MIN_SDK`, `TARGET_SDK`, `COMPILE_SDK`.
- **Public App Version:** `VERSION_MAJOR`, `VERSION_MINOR`.
- **Product Families:** `PHONE_PRODUCT_FAMILY`.
- **Upload Counters:** `PHONE_UPLOAD`.

## 2. Gradle Integration

App modules apply the custom versioning plugin:
```kotlin
plugins {
    id("com.d4rk.android.apptoolkit.versioning")
}
```

The plugin provides a `versioning` extension. You can retrieve version info for the phone app:
```kotlin
val versioning = extensions.getByType<VersioningExtension>()
val appVersion = versioning.phoneVersion()

android {
    compileSdk = appVersion.compileSdk
    defaultConfig {
        minSdk = appVersion.minSdk
        targetSdk = appVersion.targetSdk
        versionCode = appVersion.versionCode
        versionName = appVersion.versionName
    }
}
```

## 3. `versionName` Format

The `versionName` is date-based and follows the format: `yy.MM.upload`.
- The date is generated from the build date in the `Europe/Bucharest` timezone.
- Example: `26.07.1` means year 2026, month July, upload 1.

## 4. `versionCode` Format

The `versionCode` is calculated using the formula:
`versionCode = versionCodeBase + appVersion * 1000 + upload`

Where:
- `appVersion = VERSION_MAJOR * 100 + VERSION_MINOR`
- `versionCodeBase = PRODUCT_FAMILY * 10,000,000 + TARGET_SDK * 100,000`

The resulting format is **PP VVV UUU** (8 digits):
- **PP:** Product family and target SDK base.
- **VVV:** Public app version (Major + Minor).
- **UUU:** Upload counter.

This format ensures that the version code is always increasing and stays safely above the legacy hardcoded version code (e.g., `127`).

## 5. Safety Checks

The plugin performs the following validations during the build:
- `MIN_SDK` must be <= `TARGET_SDK`.
- `TARGET_SDK` must be <= `COMPILE_SDK`.
- `VERSION_MAJOR` must be in `0..9`.
- `VERSION_MINOR` must be in `0..99`.
- `PRODUCT_FAMILY` must be in `1..9`.
- `UPLOAD` must be in `1..999`.
- `versionCode` must be <= `2,100,000,000`.

## 6. Release Process

- **Same version upload:** Increment the `PHONE_UPLOAD` counter.
- **New public version:** Increment `VERSION_MAJOR` or `VERSION_MINOR` and reset `PHONE_UPLOAD` to `1`.
- **Target SDK bump:** Update `TARGET_SDK` and `COMPILE_SDK`. The `versionCodeBase` will automatically increase, keeping the new `versionCode` above previous versions.
