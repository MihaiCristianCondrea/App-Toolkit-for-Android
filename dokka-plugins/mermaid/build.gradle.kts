plugins { alias(libs.plugins.kotlin.jvm) }

group = "com.mihaicristiancondrea.android.apptoolkit.dokka"
version = "1.0"

dependencies {
    compileOnly(libs.dokka.core)
    compileOnly(libs.dokka.base)
    compileOnly(libs.dokka.kotlinx.html)
    compileOnly(kotlin("stdlib"))
}
