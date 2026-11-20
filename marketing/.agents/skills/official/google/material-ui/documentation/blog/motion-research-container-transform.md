---
title: "Choosing the Right Transitions"
source_url: "https://m3.material.io/blog/motion-research-container-transform"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/3e9c9da48278-h-AzVFy6YUsxqT83bikUXRQjBwVSDTb_bBTYEiwl_PKBXh3RqZ5peG5rjIVVYuHUfY9aiHO9nbE15rxhZHFXyOFbJfLHovKc.png"
  - "assets/videos/8d92bca38fc1-Basil_-_Line_up.mp4"
---

# Choosing the Right Transitions

Making interfaces feel "fancy" with animated transitions

![container transform animation](<https://lh3.googleusercontent.com/h-AzVFy6YUsxqT83bikUXRQjBwVSDTb_bBTYEiwl_PKBXh3RqZ5peG5rjIVVYuHUfY9aiHO9nbE15rxhZHFXyOFbJfLHovKcO2pn7K-Eqi_r1h7AM5c>)

Published: 2022-01-20T13:00:00

Imagine you're browsing through a restaurant menu on your phone, and you tap one of the entrees.  What should happen?  Should the entree's description appear instantly, or should the [card](https://material.io/components/cards) you tapped animate, expanding to show the additional detail?

Material Research wanted to find out.

## The Study

[Video: a series of 9 animated transitions](https://kstatic.googleusercontent.com/files/9155ea019c8996a54129615a9cc9bbfcc496da455d757ad8a67cbdb4cbecfb314bedf19164360d0d97384d291a8e0af4fb0f9f6fc4f593c1fbb31ed51944c965)

We constructed a food ordering experience and showed it to a series of users in a [research study](https://userresearch.google.com/).  As participants tapped through the menu, they would see one of seven transitions, or no transition at all (also called a [jump cut](https://en.wikipedia.org/wiki/Jump_cut)).  We interviewed these participants to learn which transition best suited a grid of cards, and why.

## The Results

A clear majority of participants told us they prefer the [container transform](https://material.io/design/motion/the-motion-system.html#container-transform). Container transform is a motion pattern that creates a visible connection between two UI elements by transforming one element into another to reinforce the relationship of the two elements. Participants shared a variety of reasons for this preference, ranging from how well the transition fit expectations to the emotions the transition inspired. We'll dig into some of their thoughts below.

#### Participants thought it perfectly fit the context

Some expressed that the transition felt like it ‘fit’ the experience. The card and its details represent the same information - in this case, a particular plate of food.  Having one seamlessly transform into the other helps users build a mental model of the application, so they don’t get lost. In fact, some participants expressed that it felt like they never left the page.

Furthermore, some told us that container transform evoked the feeling of picking something up to get a closer look, or pulling a menu in closer. Material tries to leverage users' familiarity with the physical world to make the digital one easier to navigate. Participants noticed, comparing their interactions with our digital prototype to handling a real menu. Taking it further, not only was it reminiscent of a restaurant but for some it made them feel like they were being well taken care of at a fancy restaurant – which takes us to the next reason for the preference.

#### Participants liked the feeling the transition added to the experience

With the container transform transition, many participants told us that it felt warm and inviting - even cozy! Only the container transform transition was perceived as conveying positive emotions and felt high end – as we called out above, participants mentioned it feeling like they were at a fancy restaurant. In contrast, the few participants who preferred a [jump cut](https://en.wikipedia.org/wiki/Jump_cut) or a [fade through](https://material.io/design/motion/the-motion-system.html#transition-patterns) transition called those transitions “efficient” and “quick.” Notably however, those transitions were never preferred for enhancing the feeling of an experience, and were considered overwhelming and uncomfortable for most.

## Conclusion

With Material You, we aim to embrace emotion and expressiveness. Our results show that something as simple as the container transformation – the animated transition between two pages – can be a meaningful part of making an experience just feel right. It’s even able to inspire a positive emotional response in the people who see it.

As we say at Material, "design is never done."  We always have more work to do and more insights to uncover.  No one transition fits every case; however, if you want to give your users a seamless, premium experience as they navigate through a list of information, give container transform a try. You can check out more details and get started adding motion into your own products by taking a look at the [motion guidance on material.io](https://material.io/design/motion/the-motion-system.html#container-transform).
