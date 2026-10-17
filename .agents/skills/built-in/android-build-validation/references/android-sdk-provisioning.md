# Android SDK Provisioning

Use this reference when an Android project cannot configure or test because the local Android SDK is
missing or incomplete.

The goal is to prepare the current machine without changing the project to fit the environment.

## First inspect the project

Before downloading anything, determine:

- `compileSdk`
- whether a preview or minor SDK platform is required
- AGP version
- Gradle version
- JDK requirement
- Java or Kotlin toolchain configuration
- build-tools version when explicitly configured
- NDK version when native code is used
- CMake version when configured
- emulator or system-image needs when instrumented tests are required

Common sources include:

```text
build.gradle
build.gradle.kts
gradle/libs.versions.toml
gradle.properties
release.properties
version.properties
build-logic/
convention plugins
CI workflow files
```

Do not choose the SDK from `targetSdk` alone.

`compileSdk` is normally the platform needed to compile the project.

## Check whether an SDK already exists

Run:

```bash
command -v android || true
command -v sdkmanager || true
printf 'ANDROID_HOME=%s\n' "${ANDROID_HOME:-}"
```

Also inspect:

- `local.properties`
- standard Android SDK directories for the current OS
- CI environment variables
- project-specific environment setup

If a usable SDK already exists, install only the missing packages.

## Preferred current tooling

Google now provides the Android CLI and recommends its SDK package commands.

Check:

```bash
command -v android
android info
```

If available, prefer it for new setup.

### Linux

When the Android CLI is not installed and the environment has network access:

```bash
curl -fsSL https://dl.google.com/android/cli/latest/linux_x86_64/install.sh | bash
```

Then refresh the shell environment if the installer requests it.

Verify:

```bash
command -v android
android info
```

### macOS

Apple silicon:

```bash
curl -fsSL https://dl.google.com/android/cli/latest/darwin_arm64/install.sh | bash
```

Intel:

```bash
curl -fsSL https://dl.google.com/android/cli/latest/darwin_x86_64/install.sh | bash
```

Homebrew can also be used when available:

```bash
brew tap android/tap
brew install android-cli
```

### Windows

When `winget` is available:

```powershell
winget install --id Google.AndroidCLI
```

If this is not available, use the current Android CLI installation instructions from the official
Android documentation.

## Choose a writable SDK directory

In temporary Linux or CI environments, use a user-owned directory.

Example:

```bash
export ANDROID_HOME="$HOME/android-sdk"
mkdir -p "$ANDROID_HOME"
```

A temporary scratch directory is also acceptable when the environment is ephemeral.

Do not install the SDK into the repository unless the environment explicitly requires it.

## Discover package names before installing

Do not guess preview or minor SDK names.

With Android CLI:

```bash
android --sdk="$ANDROID_HOME" sdk list "platforms/android-.*" --all-versions
```

Narrow the pattern to the required API when practical.

Typical Android CLI package syntax looks like:

```text
platforms/android-34
build-tools/34.0.0
platform-tools
```

For new or preview Android versions, list the available package first and install the exact package
that matches the project's configuration.

## Install required packages

A typical project may need:

- platform matching `compileSdk`
- platform tools
- build tools

Example shape:

```bash
android --sdk="$ANDROID_HOME" sdk install \
  platforms/android-<api> \
  build-tools/<version> \
  platform-tools
```

Replace the placeholders with values resolved from the project and package listing.

Do not install unrelated API levels.

## `sdkmanager` compatibility path

Many existing Android environments still use the older Android SDK Command-Line Tools and
`sdkmanager`.

Google currently marks `sdkmanager` as deprecated in favor of Android CLI, but it remains useful for
existing CI environments.

If it is already installed:

```bash
sdkmanager --list
```

Accept licenses when needed:

```bash
yes | sdkmanager --licenses
```

Install packages using the exact names returned by `--list`.

Example shape:

```bash
sdkmanager --sdk_root="$ANDROID_HOME" \
  "platform-tools" \
  "platforms;android-<api>" \
  "build-tools;<version>"
```

Do not assume the exact package name for preview or minor platform releases.

List them first.

## Raw Command-Line Tools fallback

Use this only when:

- Android CLI is unavailable
- `sdkmanager` is unavailable
- the environment allows downloading the Android Command-Line Tools archive

### 1. Find the current archive

Use the official Android Studio download page:

<https://developer.android.com/studio>

Do not hardcode the archive build number into a reusable skill because it changes.

### 2. Download the archive

Example shape:

```bash
curl -o /tmp/cmdline.zip "<current-official-download-url>"
```

### 3. Extract the expected layout

The SDK root should contain:

```text
$ANDROID_HOME/
└── cmdline-tools/
    └── latest/
        ├── bin/
        ├── lib/
        ├── NOTICE.txt
        └── source.properties
```

A common failure is extracting the archive to:

```text
cmdline-tools/
```

without the required version directory.

If `sdkmanager` reports:

```text
Could not determine SDK root
```

check this layout first.

### 4. Accept licenses

```bash
yes | "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" \
  --sdk_root="$ANDROID_HOME" \
  --licenses
```

### 5. List packages

```bash
"$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" \
  --sdk_root="$ANDROID_HOME" \
  --list
```

### 6. Install only what the project requires

Use package names from the listing.

## `local.properties`

Gradle can be pointed to the SDK with:

```properties
sdk.dir=/absolute/path/to/android-sdk
```

Before writing `local.properties`:

- check whether it already exists
- preserve unrelated existing entries
- confirm it is not intended for source control
- do not commit a machine-specific path

In temporary environments, environment variables may be cleaner than modifying workspace files.

## JDK and Gradle

The Android SDK is only part of the build environment.

Always check:

```bash
java -version
./gradlew --version
```

Resolve the required Java version from:

- Gradle wrapper compatibility
- AGP requirements
- Java toolchain declarations
- Kotlin toolchain declarations
- repository documentation
- CI configuration

If the installed Java version is incompatible, select or install the required JDK.

Do not upgrade AGP or Gradle simply because the machine has a different JDK.

## Android Gradle Plugin compatibility

Recent Android API levels may require a minimum AGP version.

If a project requests a platform that its current AGP does not support:

1. confirm the intended AGP version
2. check the official AGP compatibility guidance
3. distinguish a repository configuration problem from a missing SDK

Do not silently upgrade AGP during environment setup.

## NDK and CMake

If the project declares:

```text
ndkVersion
externalNativeBuild
cmake.version
```

install the exact side-by-side versions required by the project.

Do not install the NDK for a project that does not use native code.

## Emulator setup

JVM unit tests and static Gradle checks do not require an emulator after the SDK is configured.

Instrumented tests may require:

- emulator package
- system image
- AVD
- `adb`
- hardware acceleration or compatible fallback
- sufficient RAM
- sufficient disk space

Do not download large emulator images unless runtime validation is needed.

## Cold Gradle cache

A fresh container may need network access for:

- Gradle wrapper
- Gradle plugins
- Maven dependencies
- Kotlin artifacts
- AGP artifacts

Do not use:

```bash
--offline
```

on a cold cache unless the dependencies are already present.

A dependency-resolution failure is not automatically a product-code failure.

## Long Gradle runs

Avoid hiding progress behind commands that buffer all output.

Prefer:

```bash
./gradlew test --console=plain > build.log 2>&1
```

or:

```bash
./gradlew test --console=plain 2>&1 | tee build.log
```

Then inspect the log while the build runs when the environment allows it.

## Disk usage

Android SDKs, Gradle caches, build tools, and emulator images can use substantial disk space.

In constrained environments:

- delete downloaded archives after extraction
- avoid unused API levels
- avoid emulator images unless needed
- remove stale build output when disk pressure becomes a real problem

Do not assume one fixed SDK size. It varies by platform and tool revision.

## Ephemeral containers

Temporary sessions may discard the SDK when the environment is destroyed.

Treat the setup as session-scoped unless the platform documents persistent caching.
