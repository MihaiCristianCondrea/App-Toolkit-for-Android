# Fix stability issues  |  Jetpack Compose
When faced with an [unstable class](https://developer.android.com/develop/ui/compose/performance/stability/diagnose) that causes performance issues, you should make it stable. This document outlines several techniques you can use to do so.

Enable strong skipping
----------------------

You should first try to enable strong skipping mode. Strong skipping mode allows composables with unstable parameters to be skipped and is the easiest method to fix performance issues caused by stability.

See [Strong skipping](https://developer.android.com/develop/ui/compose/performance/stability/strongskipping) for more information.

Make the class immutable
------------------------

You can also try to make an unstable class completely immutable.

*   **Immutable**: Indicates a type where the value of any properties can never change after an instance of that type is constructed, and all methods are referentially transparent.
    *   Make sure all the class's properties are both `val` rather than `var`, and of immutable types.
    *   Primitive types such as `String, Int`, and `Float` are always immutable.
    *   If this is impossible, then you must use Compose state for any mutable properties.
*   **Stable**: Indicates a type that is mutable. The Compose runtime does not become aware if and when any of the type's public properties or method behavior would yield different results from a previous invocation.

Immutable collections
---------------------

A common reason why Compose considers a class unstable are collections. As noted in the [Diagnose stability issues](https://developer.android.com/develop/ui/compose/performance/stability/diagnose) page, the Compose compiler cannot be completely sure that collections such as `List, Map`, and `Set` are truly immutable and therefore marks them as unstable.

To resolve this, you can use immutable collections. The Compose compiler includes support for [Kotlinx Immutable Collections](https://github.com/Kotlin/kotlinx.collections.immutable). These collections are guaranteed to be immutable, and the Compose compiler treats them as such. This library is still in alpha, so expect possible changes to its API.

Consider again this unstable class from the [Diagnose stability issues](https://developer.android.com/develop/ui/compose/performance/stability/diagnose) guide:

```
unstable class Snack {
  …
  unstable val tags: Set<String>
  …
}

```


You can make `tags` stable using an immutable collection. In the class, change type of `tags` to `ImmutableSet<String>`:

```
data class Snack{
    …
    val tags: ImmutableSet<String> = persistentSetOf()
    …
}

```


After doing so, all the class's parameters are immutable, and the Compose compiler marks the class as stable.

Annotate with `Stable` or `Immutable`
-------------------------------------

A possible path to resolving stability issues is to annotate unstable classes with either `@Stable` or `@Immutable`.

Annotating a class is overriding what the compiler would otherwise [infer](https://developer.android.com/develop/ui/compose/performance/stability) about your class. It is similar to the [`!!`](https://kotlinlang.org/docs/null-safety.html#the-operator) [operator in Kotlin](https://kotlinlang.org/docs/null-safety.html#the-operator). You should be very careful about how you use these annotations. Overriding the compiler behavior could lead you to unforeseen bugs, such as your composable not recomposing when you expect it to.

If it is possible to make your class stable without an annotation, you should strive to achieve stability that way.

The following snippet provides a minimal example of a data class annotated as immutable:

```
@Immutable
data class Snack(
…
)

```


Whether you use the `@Immutable` or `@Stable` annotation, the Compose compiler marks the `Snack` class as stable.

### Annotated classes in collections

Consider a composable that includes a parameter of type `List<Snack>`:

```
restartable scheme("[androidx.compose.ui.UiComposable]") fun HighlightedSnacks(
  …
  unstable snacks: List<Snack>
  …
)

```


Even if you annotate `Snack` with `@Immutable`, the Compose compiler still marks the `snacks` parameter in `HighlightedSnacks` as unstable.

Parameters face the same problem as classes when it comes to collection types, _the Compose compiler always marks a parameter of type `List` as unstable_, even when it is a collection of stable types.

You cannot mark an individual parameter as stable, nor can you annotate a composable to always be skippable. There are multiple paths forwards.

There are several ways you can get around the problem of unstable collections. The following subsections outline these different approaches.

#### Configuration file

If you are happy to abide by the stability contract in your codebase, then you can opt in to considering Kotlin collections as stable by adding `kotlin.collections.*` to your [stability configuration file](#configuration-file).

#### Immutable collection

For compile time safety of immutability, you can use a kotlinx immutable collection, instead of `List`.

```
@Composable
private fun HighlightedSnacks(
    …
    snacks: ImmutableList<Snack>,
    …
)

```


#### Wrapper

If you cannot use an immutable collection, you could make your own. To do so, wrap the `List` in an annotated stable class. A generic wrapper is likely the best choice for this, depending on your requirements.

```
@Immutable
data class SnackCollection(
   val snacks: List<Snack>
)

```


You can then use this as the type of the parameter in your composable.

```
@Composable
private fun HighlightedSnacks(
    index: Int,
    snacks: SnackCollection,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
)

```


#### Solution

After taking either of these approaches, the Compose compiler now marks the `HighlightedSnacks` Composable as both `skippable` and `restartable`.

```
restartable skippable scheme("[androidx.compose.ui.UiComposable]") fun HighlightedSnacks(
  stable index: Int
  stable snacks: ImmutableList<Snack>
  stable onSnackClick: Function1<Long, Unit>
  stable modifier: Modifier? = @static Companion
)

```


During recomposition, Compose can now skip `HighlightedSnacks` if none of its inputs have changed.

Stability configuration file
----------------------------

Beginning with Compose Compiler 1.5.5, a configuration file of classes to consider stable can be provided at compile time. This allows for considering classes you don't control, such as standard library classes like `LocalDateTime`, as stable.

The configuration file is a plain text file with one class per row. Comments, single, and double wildcards are supported. An example configuration:

```
// Consider LocalDateTime stable
java.time.LocalDateTime
// Consider my datalayer stable
com.datalayer.*
// Consider my datalayer and all submodules stable
com.datalayer.**
// Consider my generic type stable based off it's first type parameter only
com.example.GenericClass<*,_>

```


To enable this feature, pass the path of the configuration file to the `composeCompiler` options block of the [Compose compiler Gradle plugin](https://developer.android.com/develop/ui/compose/compiler) configuration.

```
composeCompiler {
  stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

```


As the Compose compiler runs on each module in your project separately, you can provide different configurations to different modules if needed. Alternatively, have one configuration at the root level of your project and pass that path to each module.

Multiple modules
----------------

Another common issue involves multi-module architecture. The Compose compiler can only infer whether a class is stable if all of the non-primitive types that it references are either explicitly marked as stable or in a module that was also built with the Compose compiler.

If your data layer is in a separate module to your UI layer, which is the recommended approach, this may be an issue you encounter.

### Solution

To solve this issue you can take one of the following approaches:

1.  Add the classes to your [Compiler configuration file](#configuration-file).
2.  Enable the Compose compiler on your data layer modules, or tag your classes with `@Stable` or `@Immutable` where appropriate.
    *   This involves adding a Compose dependency to your data layer. However, it is just the dependency for the Compose runtime and not for `Compose-UI`.
3.  Within your UI module, wrap your data layer classes in UI-specific wrapper classes.

The same issue also occurs when using external libraries if they don't use the Compose compiler.

Not every composable should be skippable
----------------------------------------

When working to fix issues with stability, you shouldn't attempt to make every composable skippable. Attempting to do so can lead to premature optimisation that introduces more issues than it fixes.

There are many situations where being skippable doesn't have any real benefit and can lead to hard to maintain code. For example:

*   A composable that is not recomposed often, or at all.
*   A composable that in itself just calls skippable composables.
*   A composable with a large number of parameters with expensive equals implementations. In this case, the cost of checking if any parameter has changed could outweigh the cost of a cheap recomposition.

When a composable is skippable it adds a small overhead which may not be worth it. You can even annotate your composable to be [non-restartable](https://developer.android.com/reference/kotlin/androidx/compose/runtime/NonRestartableComposable) in cases where you determine that being restartable is more overhead than it's worth.