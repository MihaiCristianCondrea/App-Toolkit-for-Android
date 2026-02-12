# Stability in Compose  |  Jetpack Compose
Compose considers types to be either stable or unstable. A type is stable if it is immutable, or if it is possible for Compose to know whether its value has changed between recompositions. A type is unstable if Compose can't know whether its value has changed between recompositions.

Compose uses the stability of a composable's parameters to determine whether it can skip the composable during [recomposition](about:/develop/ui/compose/mental-model#recomposition):

*   **Stable parameters:** If a composable has stable parameters that have not changed, Compose skips it.
*   **Unstable parameters:** If a composable has unstable parameters, Compose always recomposes it when it recomposes the component's parent.

If your app includes many unnecessarily unstable components that Compose always recomposes, you might observe performance issues and other problems.

This document details how you can increase the stability of your app to improve performance and overall user experience.

Immutable objects
-----------------

The following snippets demonstrates the general principles behind stability and recomposition.

The `Contact` class is an immutable data class. This is because all its parameters are primitives defined with the `val` keyword. Once you create an instance of `Contact`, you cannot change the value of the object's properties. If you attempted to do so, you would create a new object.

```
data class Contact(val name: String, val number: String)

```


The `ContactRow` composable has a parameter of type `Contact`.

```
@Composable
fun ContactRow(contact: Contact, modifier: Modifier = Modifier) {
   var selected by remember { mutableStateOf(false) }

   Row(modifier) {
      ContactDetails(contact)
      ToggleButton(selected, onToggled = { selected = !selected })
   }
}

```


Consider what happens when the user clicks the toggle button and the `selected` state changes:

1.  Compose evaluates if it should recompose the code inside `ContactRow`.
2.  It sees that the only argument for `ContactDetails` is of type `Contact`.
3.  Because `Contact` is an immutable data class, Compose is sure that none of the arguments for `ContactDetails` have changed.
4.  As such, Compose skips `ContactDetails` and does not recompose it.
5.  On the other hand, the arguments for `ToggleButton` have changed, and Compose recomposes that component.

### Mutable objects

While the preceding example uses an immutable object, it is possible to create a mutable object. Consider the following snippet:

```
data class Contact(var name: String, var number: String)

```


As each parameter of `Contact` is now a `var`, the class is no longer immutable. If its properties changed, Compose wouldn't become aware. This is because Compose only tracks changes to Compose [State objects](https://developer.android.com/reference/kotlin/androidx/compose/runtime/MutableState).

Compose considers such a class unstable. Compose doesn't skip recomposition of unstable classes. As such, if `Contact` were defined in this way, `ContactRow` in the previous example would recompose any time `selected` changed.

Implementation in Compose
-------------------------

It can be helpful, though not crucial, to consider how exactly Compose determines which functions to skip during recomposition.

When the Compose compiler runs on your code, it marks each function and type with one of several tags. These tags reflect how Compose handles the function or type during recomposition.

### Functions

Compose can mark functions as `skippable` or `restartable`. Note that it may mark a function as one, both, or neither of these:

*   **Skippable**: If the compiler marks a composable as skippable, Compose can skip it during recomposition if all its arguments are equal with their previous values.
*   **Restartable**: A composable that is restartable serves as a "scope" where recomposition can start. In other words, the function can be a point of entry for where Compose can start re-executing code for recomposition after state changes.

### Types

Compose marks types as either immutable or stable. Each type is one or the other:

*   **Immutable**: Compose marks a type as immutable if the value of its properties can never change and all methods are referentially transparent.
    *   Note that all primitive types are marked as immutable. These include `String`, `Int`, and `Float`.
*   **Stable**: Indicates a type whose properties can change after construction. If and when those properties change during runtime, Compose becomes aware of those changes.

Debug stability
---------------

If your app is recomposing a composable whose parameters have not changed, first check its definition for parameters that are clearly mutable. Compose always recomposes a component if you pass in a type with `var` properties, or a `val` property that use a known unstable type.

For detailed information about how to diagnose complex issues with stability in Compose, see the [Debug stability](https://developer.android.com/develop/ui/compose/performance/stability/diagnose) guide.

Fix stability issues
--------------------

For information about how to bring stability to your Compose implementation, see the [Fix stability issues](https://developer.android.com/develop/ui/compose/performance/stability/fix) guide.

Summary
-------

Overall, you should note the following points:

*   **Parameters**: Compose determines the stability of each parameter of your composables to determine which composables it should skip during recomposition.
*   **Immediate fixes**: If you notice your composable isn't being skipped _and it is causing a performance issue_, you should check the obvious causes of instability like `var` parameters first.
*   **Compiler reports**: You can use the [compiler reports](https://developer.android.com/develop/ui/compose/performance/stability/diagnose) to determine what stability is being inferred about your classes.
*   **Collections**: Compose always considers collection classes unstable, such as `List, Set` and `Map`. This is because it cannot be guaranteed that they are immutable. You can use [Kotlinx immutable collections](about:/develop/ui/compose/performance/stability/fix#immutable-collections) instead or annotate your classes as `@Immutable` or `@Stable`.
*   **Other modules**: Compose always considers unstable where they are from modules in which the Compose compiler does not run. Wrap the classes in UI model classes if required.

Further reading
---------------

*   **Performance**: For more debugging tips on Compose performance, check out our [best practices guide](http://goo.gle/compose-performance) and [I/O talk](https://www.youtube.com/watch?v=EOQB8PTLkpY).