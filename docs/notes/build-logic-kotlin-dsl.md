# Why `build-logic` pins an explicit `kotlin-dsl` version

`build-logic/convention/build.gradle.kts` applies the plugin like this:

```kotlin
plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "6.7.3"
}
```

Both halves of that line are deliberate, and each one has bitten this repo before.

## Why the full id and an explicit version, rather than the `` `kotlin-dsl` `` accessor

The idiomatic form in a normal project is the accessor:

```kotlin
plugins {
    `kotlin-dsl`
}
```

That accessor is generated for the build script from the Gradle distribution running the build, and
it carries no coordinates. JitPack resolves this project's included build separately from the main
build when it produces a publication, and the accessor is not on the plugin classpath it assembles —
the build fails there while succeeding locally. Spelling out the plugin id and version makes the
plugin resolvable from coordinates alone, which is what JitPack needs.

This is the reason the version cannot simply be deleted, even though Gradle's own warning text
suggests deleting it.

## Why the version must match the Gradle release

`kotlin-dsl` is versioned in lockstep with Gradle, and it drags in the Kotlin version that Gradle
embeds. Applying a version other than the one bundled with the Gradle in
`gradle/wrapper/gradle-wrapper.properties` produces two warnings on every single task:

```
This version of Gradle expects version '6.7.3' of the `kotlin-dsl` plugin but version '6.7.6' has
been applied to project ':build-logic:convention'.

WARNING: Unsupported Kotlin plugin version.
The `embedded-kotlin` and `kotlin-dsl` plugins rely on features of Kotlin `2.4.0` that might work
differently than in the requested version `2.4.10`.
```

The second one is the one that matters: a mismatched `kotlin-dsl` pulls a different Kotlin compiler
and standard library into the build-logic classpath than the one Gradle's Kotlin DSL was compiled
against. That is unsupported, not merely noisy.

Note that this is independent of the `kotlin` version in `gradle/libs.versions.toml` (currently
`2.4.10`), which is the Kotlin the *application and library modules* compile with. Build logic
compiles against Gradle's embedded Kotlin; the two do not have to agree and should not be kept in
sync with each other.

## When bumping Gradle

1. Update the wrapper.
2. Run any task (`./gradlew help` is enough).
3. If Gradle warns about the `kotlin-dsl` version, it names the version it expects — set that value
   here.
4. Re-run to confirm both warnings are gone.

| Gradle | `kotlin-dsl` |
|--------|--------------|
| 9.7    | 6.7.3        |
