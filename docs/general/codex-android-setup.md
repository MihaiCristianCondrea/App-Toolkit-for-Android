# Codex Android (Kotlin) setup script

## Summary
Community testing shows that Codex’s non-interactive terminal can break `sdkmanager` unless
license acceptance and installs are piped through a subshell. The most reliable pattern is:

- Set `ANDROID_HOME` and `ANDROID_SDK_ROOT` to `/usr/lib/android-sdk`.
- Install the command line tools and update `sdkmanager`.
- Accept licenses explicitly.
- Install required SDK components in a single `sdkmanager` invocation.
- Write `local.properties` so Gradle picks up the SDK path.
- Initialize git submodules.

## Recommended script
```bash
set -euo pipefail

echo "Getting Android command line tools"
wget -O android-commandlinetools.zip \
  https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip

echo "Unpacking command line tools"
unzip android-commandlinetools.zip -d /usr/lib/android-sdk

# Point shell + Gradle at the same SDK path
export ANDROID_HOME=/usr/lib/android-sdk
export ANDROID_SDK_ROOT=$ANDROID_HOME
export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH

echo "sdk.dir=$ANDROID_HOME" > /workspace/App-Toolkit-for-Android/local.properties

echo "Updating sdkmanager"
/usr/lib/android-sdk/cmdline-tools/bin/sdkmanager --sdk_root=/usr/lib/android-sdk/ --update

echo "Accepting Android SDK licenses"
/usr/lib/android-sdk/cmdline-tools/bin/sdkmanager --sdk_root=/usr/lib/android-sdk/ --licenses <<EOF_LICENSES
y
y
y
y
y
y
y
y
EOF_LICENSES

echo "Installing Android SDK components"
/usr/lib/android-sdk/cmdline-tools/bin/sdkmanager --sdk_root=/usr/lib/android-sdk \
  "platform-tools" \
  "platforms;android-35" \
  "build-tools;35.0.0" <<EOF_INSTALL
y
EOF_INSTALL

echo "Git Submodule Init"
git submodule update --init --recursive
```

## Notes
- Using a `bash -c 'yes | sdkmanager ...'` or a here-doc for licenses prevents “broken pipe”
  errors in non-interactive Codex runs.
- Update the SDK versions if the project’s Gradle files require different API levels.
