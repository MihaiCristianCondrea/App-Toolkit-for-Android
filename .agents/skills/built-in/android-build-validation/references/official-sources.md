# Official Android Sources

Use official Android documentation when package names, SDK installation commands, compatibility
requirements, or CLI behavior may have changed.

## Android CLI

Overview:

<https://developer.android.com/tools/agents/android-cli>

Downloads:

<https://developer.android.com/tools/agents/android-cli/download>

The current Android CLI includes SDK package management through commands such as:

```text
android sdk list
android sdk install
android sdk update
android sdk remove
```

## `sdkmanager`

Documentation:

<https://developer.android.com/tools/sdkmanager>

Google currently marks `sdkmanager` as deprecated in favor of the newer Android CLI SDK commands.

Keep `sdkmanager` as a compatibility path for existing environments.

## Android Studio and Command-Line Tools downloads

<https://developer.android.com/studio>

Use this page when the raw Command-Line Tools archive is needed.

Do not hardcode a temporary archive build number into the skill.

## SDK platform releases

<https://developer.android.com/tools/releases/platforms>

Use the current platform release documentation when a project uses a recent or preview SDK.

## Android Gradle Plugin

<https://developer.android.com/build/releases/about-agp>

Use current AGP compatibility guidance when a project targets a recent Android API.

## Platform setup guides

Examples:

<https://developer.android.com/about/versions/16/setup-sdk>

<https://developer.android.com/about/versions/17/setup-sdk>

Use the guide matching the project's required Android version.

Do not automatically use the newest platform guide.

## NDK and CMake

<https://developer.android.com/studio/projects/install-ndk>

Use the project's declared side-by-side NDK and CMake versions.
