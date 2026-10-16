---
title: "Building Beautiful Transitions with Material Motion for Android"
source_url: "https://m3.material.io/blog/android-material-motion"
lastmod: "2024-12-19"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/fd8aab0a2a96-dS_AmHPhlU7QlWQB8Ra69FyHf8_7rO8BdTBkOlp2sE0_MUYTqvdAXj7oy4dDIpbNTSosYjyHvH91lpo0wO5WoRccqfIGCBO0.png"
  - "assets/images/d8cf70569c87-AnhVnJUJrCtLcWWWn6T8ZKSfClW20_1oSGc1eNtTEgAJdGiy2xlhkyjpuALSrhZhDKkojVyGc1sGjBIXd7HsxRJE7OXqbKMI.png"
  - "assets/images/f9399960fbaa-RZWkyHEl4Gd7-6ay5-RtpEmT4KuQZM1iHx-C_q4XOtkU6qHInwzGP3cUbkhpzE2igDuvaityQQXAy17ETtqcF8M6ht37quar.png"
  - "assets/images/c49633af9c94-cHY9pZOafRsr48xcMbF5Z5C97ikL_EQVay4jHafJy6wsPL9o3Xoa4wOErBjlmg371UOBGvCaDCMh3aEDhZwlLLdYrKmebb6J.png"
---

# Building Beautiful Transitions with Material Motion for Android

Introducing four transition patterns and how to add them to your app

![Image](<https://lh3.googleusercontent.com/dS_AmHPhlU7QlWQB8Ra69FyHf8_7rO8BdTBkOlp2sE0_MUYTqvdAXj7oy4dDIpbNTSosYjyHvH91lpo0wO5WoRccqfIGCBO0l3efAw>)

Published: 2020-09-01T13:02:00

The [Material motion system](https://material.io/design/motion/the-motion-system.html), recently released as part of the [MDC-Android library (v 1.2.0)](https://material.io/blog/android-stable-release-1-2), distills common transitions into a group of simple patterns for a smoother, more understandable user experience. Material motion currently includes four transitions:

-   [Container transform](https://material.io/design/motion/the-motion-system.html#container-transform)
-   [Shared axis](https://material.io/design/motion/the-motion-system.html#shared-axis) 
-   [Fade through](https://material.io/design/motion/the-motion-system.html#fade-through)
-   [Fade](https://material.io/design/motion/the-motion-system.html#fade)

We’ve implemented these transitions on top of the [Android platform](https://developer.android.com/reference/android/transition/package-summary) & [AndroidX Transition system](https://developer.android.com/reference/androidx/transition/package-summary) so they can easily be used when moving between Activities, Fragments, and Views.

This post introduces each pattern and explains how to add them to your app. I’ll illustrate each step by implementing it for our example app [Reply](https://material.io/design/material-studies/reply.html), a simple and easy-to-use email client. Three of the app’s flows will get the motion transitions: **opening an email, opening the search page,** and **switching mailboxes**.

If you’re more of a hands-on learner and want to get right into the code, then consider doing the [Material motion codelab](https://codelabs.developers.google.com/codelabs/material-motion-android), which lets you practice these techniques by executing each step as you go along.

## Container transform: Opening an email

![Image](<https://lh3.googleusercontent.com/AnhVnJUJrCtLcWWWn6T8ZKSfClW20_1oSGc1eNtTEgAJdGiy2xlhkyjpuALSrhZhDKkojVyGc1sGjBIXd7HsxRJE7OXqbKMIkaHQUw>)

The “hero” of transitions, container transform is used when one thing turns into another thing. Examples include a list item that expands into a details page, a [FAB](https://material.io/components/buttons-floating-action-button) that morphs into a toolbar, or a [chip](https://material.io/components/chips) that expands into a floating [card](https://material.io/components/cards). In each case there is one component transforming into another, maintaining a shared “outer” container while animating a swap of “inner” content. Using a container transform to animate between views can help reinforce their relationship and maintain a user’s [navigational context](https://material.io/design/navigation/navigation-transitions.html#hierarchical-transitions).

In Reply, we’re adding a container transform between a Fragment holding a list of emails ([`HomeFragment`](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/home/HomeFragment.kt)) and an email details fragment (`[EmailFragment](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/email/EmailFragment.kt)`). If you’re familiar with [Android shared element transitions](https://developer.android.com/training/transitions/start-activity), the setup is pretty similar!

Start by identifying our two shared element views and give them each a [transition name](https://developer.android.com/reference/android/view/View#attr_android:transitionName). The first is a single email list item card where we will use [Data Binding](https://developer.android.com/topic/libraries/data-binding) to make sure each item has a unique transition name.

The second is the full-screen card inside of our `[EmailFragment](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/email/EmailFragment.kt)`, which can be given a static transition name since it’s the only one in the view hierarchy. Note that our first and second shared elements don’t need to use the same transition name.

These two views will be picked up by our container transform. Under the hood, both will be placed inside a drawable whose bounds are clipped inside a “container” that animates its shape from a list item to a details page. During the transition, the container’s contents (the list item and the details page) are swapped by fading the incoming screen in on top of the outgoing screen.

Now that we’ve marked our shared element views, let’s create and set our destination Fragment’s [`sharedElementEnterTransition`](https://developer.android.com/reference/androidx/fragment/app/Fragment#setSharedElementEnterTransition%28java.lang.Object%29) to a new instance of `[MaterialContainerTransform](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/transition/MaterialContainerTransform.java)`. By default, this `sharedElementEnterTransition` will also be automatically reversed and played when going back from the details page.

_For details on `MaterialContainerTransform` parameters, see the [motion documentation](https://github.com/material-components/material-components-android/blob/master/docs/theming/Motion.md#container-transform)._

Now when an email is clicked, all we need to do is supply our Fragment transaction with mappings between our start and end view transition names. With this information, the detail fragment’s shared element transition is able to find and animate the two views using our supplied `MaterialContainerTransform`.

_In the above snippet, we’re also setting an exit and reenter transition for the outgoing list fragment. Material Components provides two helper transitions to smoothly animate a Fragment that is being replaced: `[Hold](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/transition/Hold.java)` and `[MaterialElevationScale](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/transition/MaterialElevationScale.java)`. In addition to a fade, `MaterialElevationScale` will scale out our list of emails when exiting and scale them back in when reentering. `Hold` would simply keep our list of emails in place. Without setting an exit transition, our list of emails would immediately be removed and disappear from view._

If we were to run the code at this point and navigate back to our list of emails from the details page, the return transition wouldn’t run. This is because the transition system isn’t able to find the two views which are mapped to our transition names, since the email list adapter hasn’t yet been populated when our transition starts. Luckily, we have two handy methods at our disposal: `postponeEnterTransition` and `startPostponedEnterTransition`. These allow us to delay the transition until we know our shared elements are laid out and can be found by the transition system. In Reply, we can postpone until we are sure our `RecyclerView` adapter has been populated and our list items have been bound with transition names using the following snippet:

In your own app, you may need to experiment with these two methods to find the right time to start postponed transitions depending on how and when you’re populating your UI. If you find your return animation isn’t running, starting transitions before a shared element is ready is likely the cause.

Moving on to our search screen!

## Shared Axis: Opening the search page

![Image](<https://lh3.googleusercontent.com/RZWkyHEl4Gd7-6ay5-RtpEmT4KuQZM1iHx-C_q4XOtkU6qHInwzGP3cUbkhpzE2igDuvaityQQXAy17ETtqcF8M6ht37quartcwEVw>)

The shared axis pattern is used for transitions between UI elements that have a spatial or navigational relationship. In Reply, opening search takes the user to a new page that sits on top of the list of emails. To illustrate this 3-dimensional model, we can use a shared z-axis transition between the list of emails (`[HomeFragment](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/home/HomeFragment.kt)`) and the search page (`[SearchFragment](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/search/SearchFragment.kt)`).

Shared axis transitions operate on two targets simultaneously to create the final, choreographed transition. This means that “pairs” of transitions run together to create a continuous, “directional” animation. For fragments, these pairs are

-   FragmentA’s `exitTransition` and Fragment B’s `enterTransition`
-   FragmentA’s `reenterTransition` and FragmentB’s `returnTransition`

[`MaterialSharedAxis`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/transition/MaterialSharedAxis.java), the class implementing the shared axis pattern, accepts a property called `forward` to control this concept of directionality. For each transition pair, `forward` must be set to the same value to have the pair’s animations coordinate correctly.

_See the [motion documentation](https://github.com/material-components/material-components-android/blob/master/docs/theming/Motion.md#shared-axis) for more details on shared axis directionality._

In Reply, here’s how we can set up the exit and reenter transitions for our current fragment ([`HomeFragment`](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/home/HomeFragment.kt)).

In our destination fragment ([`SearchFragment`](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/search/SearchFragment.kt)), we set up the enter and return transitions.

Notice the current fragment’s exit transition and the search fragment’s enter transition use the same value for `forward` –  true. The same goes for the current fragment’s reenter transition and the search fragment’s return transition.

Next, by default, transitions run on all child views within their scene root hierarchy. This means that a shared axis transition will be applied to each email in the list page and each child of the search page. This can be a neat feature if you want to “propagate” or “[stagger](https://material.io/archive/guidelines/motion/choreography.html#choreography-creation)” animations, but since we want to animate the root of each fragment as a whole, we need to set \`android:transitionGroup="true"\` on both our [email list `RecyclerView`](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/res/layout/fragment_home.xml#L18) and our [search page root view group](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/res/layout/fragment_search.xml#L17).

With that, we should have a nice shared z-axis transition to and from our search page! Shared axis is a really flexible transition that can work in a lot of different scenarios  –  from page transitions, to smart reply selections, to onboarding or vertical stepper flows. Now that you have the set up configured, you can also experiment with `MaterialSharedAxis`’ s \`axis\` parameter to check out what the other axis animations look like.

## Fade Through: Switching mailboxes

![Image](<https://lh3.googleusercontent.com/cHY9pZOafRsr48xcMbF5Z5C97ikL_EQVay4jHafJy6wsPL9o3Xoa4wOErBjlmg371UOBGvCaDCMh3aEDhZwlLLdYrKmebb6JjKQNY-8>)

The last pattern we’ll cover is the fade through pattern. A fade through can be used to transition between UI elements that do not have a strong relationship. When transitioning between mailboxes, we don’t want the user to think their sent emails are navigationally related to their inbox. Since each mailbox is a top-level destination, fade through is an appropriate choice. In Reply, we’ll be replacing the list of emails ([`HomeFragment`](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/home/HomeFragment.kt)) with a different list of emails ([`HomeFragment`](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/java/com/materialstudies/reply/ui/home/HomeFragment.kt) with new arguments).

Since [`MaterialFadeThrough`](https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/transition/MaterialFadeThrough.java) doesn’t have the idea of directionality, the set up is even easier. We can just set an exit transition on our outgoing fragment and an enter transition on our incoming fragment.

The need to set **`android:transitionGroup="true"`** on our [list of email’s `RecyclerView`](https://github.com/material-components/material-components-android-examples/blob/develop/Reply/app/src/main/res/layout/fragment_home.xml#L25) also applies here, but this has already been taken care of during our shared axis configuration step.

That’s it for the fade through transition! Find fun places to use the fade through pattern in your own apps – like switching between bottom navigation destinations, swapping a list of items, or replacing a toolbar menu.

## Keep on moving!

That’s a brief look into the Material motion system on Android. There are lots of things you can do to [customize motion](https://material.io/design/motion/customization.html) using the patterns provided, making motion a part of your brand’s experience. Also keep in mind that while we looked at Fragment transitions, the motion system can be used to transition between Activities all the way down to Views. Check out the full [motion spec](https://material.io/design/motion/the-motion-system.html) for some inspiring examples to get you thinking about where you might be able topolish your app’s core experience or add some extra delight in small places.

To keep learning, check out the following additional resources:

[Material Motion Developer Documentation](https://github.com/material-components/material-components-android/blob/master/docs/theming/Motion.md)

Lots of customization options and tips on animating between Activities and Views can be found in Material Android’s motion documentation!

[Material Motion Codelab](https://codelabs.developers.google.com/codelabs/material-motion-android/#0)

A full, step-by-step developer tutorial covering how to add Material motion to Reply.

[Google Drive for Android](https://play.google.com/store/apps/details?id=com.google.android.apps.docs)

Take a look at Google Drive on Android to see the motion system in action. Clicking on folders, opening search, and navigating between bottom navigation destinations all use transitions from MDC-Android.
