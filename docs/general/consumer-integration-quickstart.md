# Consumer Integration Quickstart

This guide is a fast path for integrating **App Toolkit for Android** into a host application.

## 1) Add the dependency (JitPack + artifact)

Use the same repository and artifact published in the project README.

```kotlin
// settings.gradle.kts
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
```

```kotlin
// app/build.gradle.kts (or the host module that consumes the toolkit)
dependencies {
    implementation("com.github.MihaiCristianCondrea:apptoolkit:2.0.2")
}
```

Reference: [README dependency setup](../../README.md#integration).

## 2) Minimum host Android requirements

Derived from `apptoolkit/build.gradle.kts`:

- **`minSdk = 26`**.
- **Compose must be enabled** in the host Android module (`buildFeatures { compose = true }`).
- Toolkit is built with **Java 21 + Kotlin JVM target 21**. Keep host-side Kotlin/JVM settings compatible.

Suggested host module baseline:

```kotlin
android {
    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
}
```

## 3) Minimal startup checklist (host app)

1. **Initialize DI at app startup**
   - The toolkit uses Koin for injected services and ViewModels.
   - If your app already uses Koin, include toolkit-related modules/providers in your existing graph.
   - If not, initialize Koin (or provide an equivalent setup) before launching toolkit screens.

2. **Launch one toolkit screen**
   - Start with `StartupActivity` to validate end-to-end wiring quickly.

3. **Validate runtime dependencies**
   - Confirm merged manifest/service metadata for Google Play services and Firebase-related integrations.
   - Verify required runtime libraries/resources are packaged (Gradle sync + app startup smoke test).

## 4) “5-minute integration” snippet

### Step A — Application-level DI init

```kotlin
// HostApplication.kt
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class HostApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HostApplication)
            modules(
                module {
                    // Register/bridge host implementations required by toolkit contracts.
                    // Add toolkit + host modules here.
                }
            )
        }
    }
}
```

### Step B — Launch a toolkit Activity

```kotlin
// Any host Activity/Fragment
import android.content.Intent
import com.d4rk.android.libs.apptoolkit.app.startup.ui.StartupActivity

startActivity(Intent(this, StartupActivity::class.java))
```

### Expected result

- App opens toolkit startup flow (instead of crashing with DI/manifest/runtime errors).
- You can confirm integration by seeing the startup UI and navigation to the next configured route.

## 5) Troubleshooting

### Missing DI binding

**Symptom**
- `NoBeanDefFoundException`, `No definition found for ...`, or ViewModel creation errors.

**Fix**
- Ensure Koin is initialized in `Application.onCreate()`.
- Register required toolkit contracts and any host-provided implementations.
- Verify qualifier names match exactly when named dependencies are used.

### Activity not found

**Symptom**
- `ActivityNotFoundException` or failure to resolve toolkit Activity class.

**Fix**
- Confirm the dependency is added to the correct module and Gradle sync completed.
- Use the fully-qualified toolkit Activity import.
- Rebuild after cache cleanup if classpath looks stale.

### Missing service metadata / Google services config

**Symptom**
- Runtime failures around Firebase Messaging, Analytics, or Ads startup.

**Fix**
- Verify your merged manifest includes required service and `<meta-data>` entries.
- Ensure host app includes required Google/Firebase configuration for enabled features.
- Compare with toolkit manifest requirements and disable unsupported features until configuration is complete.

### Compose/runtime mismatch

**Symptom**
- Build errors from Compose compiler, JVM target mismatch, or desugaring/toolchain incompatibility.

**Fix**
- Align host module Java/Kotlin targets with toolkit compatibility.
- Ensure Compose is enabled in the consuming module.
- Re-import Gradle project after version alignment.

## 6) Advanced docs

- **DI contracts and conventions:** [Style guidance](./style-guidance.md) and [Native ads DI qualifiers](./native-ads.md)
- **Manifest requirements reference:** [Toolkit AndroidManifest](../../apptoolkit/src/main/AndroidManifest.xml)
- **Feature matrix / capability overview:** [README Features](../../README.md#features) and [Library overview](./app-toolkit-library.md)
