---
title: "What does your UI say to your users?"
source_url: "https://m3.material.io/blog/testing-material-3"
lastmod: "2025-04-30"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/8536a2e7bf88-_XkvCAsnfDAAVGqZXYBAeFrrjv6QyITEGAtOkKyL0U_utVllNJL7N-rJ-zyghQKZ-l51W9KwKH8zdbAOnGacMnhtM7tKuf3k.png"
  - "assets/images/36c4debb0964-oSqPFI3-KfdacrPxv3_5q-OYxSROzVv3AuXPjGmna0MVCuhwCV3HFHZ2HwzF79hW-dQrthh03Mp-_uCG2vkVdg7c_y7x74Em.png"
---

# What does your UI say to your users?

How we tested M2 and M3 interfaces to understand the impact of visual changes

![abstract animation](<https://lh3.googleusercontent.com/_XkvCAsnfDAAVGqZXYBAeFrrjv6QyITEGAtOkKyL0U_utVllNJL7N-rJ-zyghQKZ-l51W9KwKH8zdbAOnGacMnhtM7tKuf3kFrkQcnQBOOcWI4aOiQ>)

Published: 2024-05-13T13:00:00

Imagine you want to update the design of your app (or, if you're us, an entire design system). You have some ideas, but you're not sure how users will perceive them—does the new design feel easier to use, or will the differences be overwhelming? Are your critical user journeys still getting the right amount of emphasis? How do the changes line up with your brand voice, or how you want the app to be perceived?

In the decade-long history of Material Design, we've constantly answered questions like these through careful UX research. When designing the latest iteration of our design system, Material 3, we wanted to focus on creating a design system that’s [adaptive, personal, and expressive](https://design.google/library/making-material-you). We knew that in order to keep improving the system—and validate that our changes moved us closer to our goals—we’d need to keep improving our approach to research, too.

The first step was figuring out the right questions to ask. We needed to find a way to ask users the right questions at scale, to better inform designers’ decision-making.

We started by interviewing Google designers to ask what interfaces are intended to accomplish, and users to understand what they actually accomplish. One thing we learned from this process was how much apps use visual cues to communicate important information.

Specifically, we heard that interfaces need to communicate:

1.  **Hierarchy**: The relative importance of the different pieces of an app.
2.  **Utility**: What each piece does, and how to use it.
3.  **Style**: What is its brand or “vibe?” Who made this, and who’s it for?

This helped us build out a framework for further research—the questions should directly measure a design's hierarchy, utility, and style. Using questions tuned to these properties, we could ask research participants to compare designs and tell us which one best accomplishes the design's intent.

With a standardized set of questions, we could gather feedback from hundreds of participants in just a few hours, automating the data parsing to see the results for any pair instantly. We replicated this across a collection of many designs that represent the different contexts Material might be used in. By systematizing the research like this, we’ve been able to study the design system as a whole, not just individual changes.

## What did we ask, and why?

The insights from our initial conversations with designers and users were essential in helping us understand which survey questions to include. They don't tell us about behavior, like "can users complete important tasks?" That's what usability tests are for. Instead, they’re intended to help understand a design's first impressions, like "is it welcoming or overwhelming?"

Here’s a rundown of what we asked and why:

**1\. Hierarchy**

Guiding the audience's gaze is a crucial responsibility in any creative pursuit, including interface design. We're constantly bombarded with information. Effective use of hierarchy—through size, placement, contrast, and motion—helps people know what‘s most important. Therefore, we asked how "effectively" a design "draws your attention to the most important and useful features."

Hierarchy can also be efficient or inefficient. For example, a blank interface with a single, giant button would be an inefficient use of space, but have clear hierarchy. To understand the hierarchy's efficiency, we asked how "informative" each design is.

**2\. Perceived utility**

Through immersion in the digital world, users pick up the "language" of app design–click the blue link to learn more, tap the X to close. Leveraging the user's learned experience is one of the key benefits of using a design system; it allows interfaces to make more space for content and action, rather than instruction.

Relying on these conventions allows interfaces to communicate a lot of information very quickly, but a designer can never know every user's mental model. To test if a design is communicating utility effectively, we asked if the design is “obvious to use” and if it has a “clear main function.”

**3\. Style**

Style signals so many things: Who made this? How do they want you to feel when you use it? Can you trust this app with your data? Because a single stylistic change can affect so much, we included questions to understand precisely how users are perceiving an app: how "modern," "clean," and "visually appealing" the design feels.

In our interviews with designers and users, we found that some people feel excited while using colorful, loud interfaces. Others find parsing the same aesthetic draining. Asking how “energetic” and “emotive” a design is provided insight into how an overall design makes users feel.

Our interviews also suggested that the spectrum between serious and fun is a critical one, so we measured “positivity,” “playfulness,” “friendliness,” and “creativity.”

Finally, we had participants rate “personality,” and “vibe or feel.” These ratings offer holistic, evaluative measures of the design’s style.

## Measuring Material 3

We've been systematically using these questions to evaluate Material 3 as a design system, by asking participants to compare Material 2 designs to their Material 3 counterparts across a range of sample use cases. Trends in the responses help us steer the evolution of the design system.

In one experiment, we asked 229 US-based participants to rank Material 2 against Material 3 in the context of an email app.

![Study results comparing Material 2 to Material 3 versions of an email app](<https://lh3.googleusercontent.com/oSqPFI3-KfdacrPxv3_5q-OYxSROzVv3AuXPjGmna0MVCuhwCV3HFHZ2HwzF79hW-dQrthh03Mp-_uCG2vkVdg7c_y7x74Em0wftJziFmJ6dZ__hCs0>)

Note: The horizontal axis is an estimate of the percent of the population that would choose the design for each question. For example, we estimate 92% of users would find the Material 3 design to be more informative than the Material 2 version of this email app. “\*” indicates a statistically significant difference.

## What did we learn?

The **hierarchy** in the Material 3 version resonated strongly with participants. **81%** felt it more effectively guided their attention, and **92%** found it to be more informative.

The Material 3 version also performed strongly in **perceived utility**. **77%** of participants thought it was more obvious how to use the Material 3 design, and **76%** found its main function was more clear.

Finally, we found statistically-significant **style** differences in a few areas. **63%** of respondents called the Material 3 version more creative, and **63%** also preferred its personality.

In most of the areas we measured, participants showed a preference for the Material 3 version, but one went the opposite way: **65%** of respondents thought the Material 2 design was more playful.

## Where do we go from here?

The Material team's motto is "design is never done." And we believe research isn't either.

Comparing totally different designs–like Material 2 to Material 3–is exploratory research; it's intended to provoke questions to better understand which changes matter to users. It doesn't give us all the answers, but instead points us in the direction where we might find them.

In this exploration, participants found Material 3 to be better across all three dimensions–hierarchy, utility, and style. What direction does this point us?

**Let's look at the big differences between the tested designs.**

The Compose button is bigger in the Material 3 design, and includes a text label. We hypothesize these changes make it more visible with a more obvious function.

The Material 3 version also makes names of the people you're chatting with more prominent. Perhaps the most important characteristic of a conversation is who it's with.

The search treatment changed significantly between the designs. In the Material 2 version it's a small icon button, but the Material 3 design opts instead for a much larger search bar. Maybe that makes it feel easier to find what you're looking for–more obvious to use.

Finally, the Material 2 version featured a colorful top app bar. The only metric that participants rated that version higher in was playfulness. If you want your app to feel playful, consider including big splashes of color.

## Final thoughts

This is not the end of the road or the research process, but it is an important step in understanding what these designs are communicating to users through their hierarchy, perceived utility, and style.

This experiment is one example of how we're evaluating the evolution of the Material Design system. We've been asking these same questions across a breadth of experiences to understand how users feel about Material 3 and to help our designers discover how to make it even better.
