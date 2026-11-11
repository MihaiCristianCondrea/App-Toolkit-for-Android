plugins { alias(libs.plugins.kotlin.jvm) }

group = "com.mihaicristiancondrea.android.apptoolkit.dokka"
version = "1.0"

dependencies {
    compileOnly(libs.dokka.core)
    compileOnly(libs.dokka.base)
    compileOnly(libs.dokka.kotlinx.html)
    compileOnly(libs.dokka.jsoup)
    implementation(libs.flexmark.parser)
    implementation(libs.flexmark.tables)
    implementation(libs.flexmark.html.to.markdown) { exclude(group = "org.jsoup", module = "jsoup") }
    compileOnly(kotlin("stdlib"))
}

tasks.processResources {
    from(rootProject.layout.projectDirectory.dir("../docs/site")) {
        include("*.md")
        into("site/guides")
    }
}
