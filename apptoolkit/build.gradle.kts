import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val publishingArtifactId = providers.gradleProperty("PUBLISHING_ARTIFACT_ID")
val jitpackGroupId = providers.gradleProperty("JITPACK_GROUP_ID")
val publishingVersion = providers.gradleProperty("PUBLISHING_VERSION")

group = jitpackGroupId.get()
version = publishingVersion.get()

plugins {
    alias(notation = libs.plugins.android.library)
    alias(notation = libs.plugins.mannodermaus)
    alias(notation = libs.plugins.compose.compiler)
    alias(notation = libs.plugins.about.libraries)
    alias(notation = libs.plugins.kotlin.serialization)
    `maven-publish`
}

android {

    namespace = "com.d4rk.android.libs.apptoolkit"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile(name = "proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.jvmArgs("-XX:+EnableDynamicAgentLoading")
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    // AndroidX
    api(dependencyNotation = libs.bundles.androidx.core)

    // Compose
    api(dependencyNotation = platform(libs.androidx.compose.bom))
    api(dependencyNotation = libs.bundles.androidx.compose)
    api(dependencyNotation = libs.bundles.androidx.navigation3)
    api(dependencyNotation = libs.androidx.material3.window.size)
    api(dependencyNotation = libs.androidx.window)

    // Lifecycle
    api(dependencyNotation = libs.bundles.androidx.lifecycle)

    // Firebase
    api(dependencyNotation = platform(libs.firebase.bom))
    api(dependencyNotation = libs.bundles.firebase)

    // Google Play services & Play Store APIs
    api(dependencyNotation = libs.bundles.google.play)

    // Image loading
    api(dependencyNotation = libs.bundles.coil)

    // Kotlin Coroutines & Serialization
    api(dependencyNotation = libs.bundles.kotlinx)

    // Networking (Ktor)
    api(dependencyNotation = platform(libs.ktor.bom))
    api(dependencyNotation = libs.bundles.ktor)

    // Dependency Injection
    api(dependencyNotation = libs.bundles.koin)

    // UI utilities
    api(dependencyNotation = libs.bundles.ui.effects)
    api(dependencyNotation = libs.bundles.ui.richtext)

    // Unit Tests
    testImplementation(dependencyNotation = libs.bundles.unitTest)
    testRuntimeOnly(dependencyNotation = libs.bundles.unitTestRuntime)

    // Instrumentation Tests
    androidTestImplementation(dependencyNotation = libs.bundles.instrumentationTest)
    debugImplementation(dependencyNotation = libs.androidx.ui.test.manifest)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = group.toString()
            artifactId = publishingArtifactId.get()
            version = publishingVersion.get()

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("App Toolkit for Android")
                description.set("Reusable Compose toolkit with common UI and infrastructure building blocks.")
                url.set("https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android")
                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    }
                }
                developers {
                    developer {
                        id.set("MihaiCristianCondrea")
                        name.set("Mihai-Cristian Condrea")
                        url.set("https://github.com/MihaiCristianCondrea")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/MihaiCristianCondrea/App-Toolkit-for-Android.git")
                    developerConnection.set("scm:git:ssh://git@github.com/MihaiCristianCondrea/App-Toolkit-for-Android.git")
                    url.set("https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android")
                }
            }
        }
    }
}
