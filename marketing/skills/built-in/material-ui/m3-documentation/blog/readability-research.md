---
title: "Adjusting Grade for Mode"
source_url: "https://m3.material.io/blog/readability-research"
lastmod: "2024-04-15"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/29d86a0b1965-Kj5Hi7eTnTTkho_DbsWuvoTo1LmyQpVJXGnmCD9u0eYZ6EErSX71gzh4zsi2MpyS4N5WoAqRsX5eapGrkPTd-GmhveRMckuM.png"
  - "assets/images/10a69e348e11-UNg4f2lBdyWLA9GgmFSSfmHCB3NS2jqKAB4a3a-z8xK7Jz9TTOQM5-B48wNjwndohoCKQaFlcyB1E4JQA2bi7bMV8ucgFals.png"
  - "assets/images/6f1e91af9950-QtIYUc2KV4oek2V3PocYGzc8MlTMcgbCgvAmgHdRC2evRpffuGJnLKVANE07iEM-UVbzEGIDbM8qTBGkUPSlBEdINL45jW14.png"
  - "assets/images/4d173b5d65a3-Fs0jmhIGONaK5azzaPi1hcpbixcrLCoBhCmCzI7fTz2XU3dvq8WnBXHCkq4rjRJHSAoU_vWlh4iBJjZ8qFXM-V21e4VJg16a.png"
  - "assets/images/cb2b3c0d6a19-L1yLlLcewN03yJZHNNg6gCDcxdad47bJ0hIEvcNlmLIIg5GyrvspcD3kSaInASs3r3vgmxaU-xCWqHAR5Sis4amEid8ct5Zi.png"
  - "assets/images/394b59ded6b5-TV7TozFl8Zk-ANMLlc91yL2DiCx9YdVvVetGL3Y58886VWezw8k8-IMf6-xbxgvYPDf6McQhZbuF9begLY54993QZxjePxx_.png"
  - "assets/images/d41e06e72eef-OrVr4xEo02eAO3aPb1zGsQF1y9fOsssjWzLYxV1hx1K2L4qQVfqSyTvY37EFPb45D5Wem5ImUXf8sLwpUyblwPUM_DiO9Lp-.png"
  - "assets/images/40cfda7edc3a-u0Y_JNSbFzBdT-phyhew_8DsEcbdzkQgkIltSXKdU-mVchOFQZ0ZYeVep4apZt1jZSNHaRKLNhinzpRYadel2dSbkgaYtfEr.png"
---

# Adjusting Grade for Mode

Measuring the impact font grade has on text readability

![Image](<https://lh3.googleusercontent.com/Kj5Hi7eTnTTkho_DbsWuvoTo1LmyQpVJXGnmCD9u0eYZ6EErSX71gzh4zsi2MpyS4N5WoAqRsX5eapGrkPTd-GmhveRMckuMvScAoAGOh1cp8r3SokH2>)

Published: 2022-05-11T09:00:00

Consider this scenario: you’re designing a product that has both a light mode and dark mode. You’ve heard that text in dark mode might be easier to read if it’s more **bold**. You’d like to make sure your users can have an equally delightful (and highly readable!) experience across both light mode and dark mode. How do you find exactly how bold the text should be to be similarly readable across both modes?

If that’s a scenario you’ve found yourself in… well, you and I are in the same boat. As researchers on the Material Design team, we were recently asked this very question.

This blog post will describe, first, how we answered that question; second, what we found; and third, how might you use the method we describe here to use Variable Fonts to improve readability for your products, and your users.

Let’s dive in!

## First: How we answered the question, what font grade is equally readable in light mode versus dark mode?

Before we get started, let’s make sure we all understand what variable fonts are. The best way to describe variable fonts may be in comparison to traditional fonts. Traditional fonts, for instance, need a different font file for every available style, like thin, normal, or bold (see the image below for an example). Historically, letters were cast in metal and foundries would create a new letter set for each style, and traditional digital fonts still follow this pattern.

![Image showing the text “thin, normal, bold, black” in Roboto font, demonstrating the range of font weights between thin to black](<https://lh3.googleusercontent.com/UNg4f2lBdyWLA9GgmFSSfmHCB3NS2jqKAB4a3a-z8xK7Jz9TTOQM5-B48wNjwndohoCKQaFlcyB1E4JQA2bi7bMV8ucgFalss0oZ_7LTdddGA989zQ>)

Roboto font showing thin, normal, bold, and black weights.

Variable fonts, however, provide the ability to use _**type as software**_ and benefit from the flexibility that digital media can provide. Rather than requiring a new font file for every style, variable fonts provide the ability to modify the design of your text along a continuous scale for a number of visual characteristics. So, rather than requiring three font files to display thin, normal, or bold fonts, you can use the same variable font to display every weight on a continuous scale, from the very thinnest possible style to the very boldest.

Similar to the weight variable font characteristic, you can also modify text grade. Grade can be thought of like weight in most ways, but grade maintains text width, so you can increase grade from the minimum to the maximum values while minimizing the impact on the overall design of text blocks or causing line reflows. An example of text cycling through the minimum to maximum grade values for a Variable Font is shown below.

![An animation showing the Roboto Flex Variable Font displaying the text “maine lobster,” cycling from the minimum to the maximum value for the grade Variable Font characteristic.](<https://lh3.googleusercontent.com/QtIYUc2KV4oek2V3PocYGzc8MlTMcgbCgvAmgHdRC2evRpffuGJnLKVANE07iEM-UVbzEGIDbM8qTBGkUPSlBEdINL45jW14S1To1raY96OBoYjyszo>)

Roboto Flex Variable Font, demonstrating the continuous range between the minimum and maximum values for grade.

Now that we’re on the same page with variable fonts and grade, how might we approach the question: What font grade is equally readable in light mode versus dark mode? To answer this question, along with oh-so-many other questions we’ve been asked, we designed an experiment.

Our experiment was pretty simple: we designed a prototype that would show study participants a two word phrase. We asked participants to read the phrase, remember it, and then hit the enter key. Once they hit the enter key the phrase would disappear, and we asked participants to type the same two word phrase into a text box. We measured, first, the time it took participants to read the phrase; second, the time it took to type their response; and third, the accuracy of their response, allowing “correct” responses to include minor misspellings.

And that’s pretty much it! This is one of those instances where a straightforward question really does benefit from a straightforward experiment.

If you’re still curious about the details of our study design, here’s a brief rundown that will hopefully help you tackle readability questions in a similar way. - **Participant count**: We launched the experiment with 184 users. This number was arrived at after completing a [power analysis](https://en.wikipedia.org/wiki/Power_of_a_test) to decide on the minimum number of people needed to detect a significant result if there is one. We included extra participants to account for drop out rates and took into account corrections given the number of analyses we ran. - **Word selection**: All words used for this study were selected from the [Dale-Chall word list](https://www.readabilityformulas.com/articles/dale-chall-readability-word-list.php). This list identifies words that the average 4th grade student should know (specifically, once 80% of 4th graders know the word it can be added to their ~3000 word list). We intentionally opted for a word list that was based on familiarity rather than difficulty since we wanted to minimize the confound of an individual's reading ability impacting response times (i.e., a word's difficulty is a characteristic of the word; it's familiarity is a characteristic of the broader population, statistically speaking). By identifying a list of words that aim to be familiar to a broad set of the population, we hoped to minimize the impact that individual familiarity may have on overall readability - **Word order**: Each of the phrases included a two syllable and three syllable word. The order of the words was randomized in each trial, again, with the aim of minimizing any potential impact of word complexity on response times. - **Dependent variables**: We measured (1) the time to read a phrase, (2) the time to repeat that phrase, and (3) the accuracy of the repeated message. Phrase accuracy was measured by calculating the [Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance) between the typed and displayed phrase. - **Analysis**: Data were analyzed with a two-way repeated measures Analysis of Variance (ANOVA). We ran post hoc tests with bonferroni corrections. And that’s it! If you’d like to do a similar study the setup is pretty simple, all things considered. By doing the above, you can determine the impact of grade on readability like we did. But you could also determine the impact width, optical size, or any other variable font characteristics has on text readability!

## Second: What we found

This part is easy because we can describe all our results in a single graph:

![Graph showing readability results across multiple values of grade, between both light mode and dark mode](<https://lh3.googleusercontent.com/Fs0jmhIGONaK5azzaPi1hcpbixcrLCoBhCmCzI7fTz2XU3dvq8WnBXHCkq4rjRJHSAoU_vWlh4iBJjZ8qFXM-V21e4VJg16aQ66sQ35oPW3Gpv9oiA>)

Graph showing readability results across multiple values of grade, between both light mode and dark mode

From a quick glance at this graph we can see a couple things right away. First, readability in dark mode (the blue line, on top) appears to be consistently higher than readability in light mode (the orange line, below). And second, while readability in dark mode appears to be relatively stable across values of grade, readability in light mode appears to improve as grade values increase (that is, the time to read a phrase decreases as the value for grade increases, shown by the orange line going down and to the right). But are these apparent differences _significant_, in the [statistical sense](https://en.wikipedia.org/wiki/Statistical_significance)? We had three main findings, described below using the same graph.

![Showing readability across light mode vs dark mode, here calling out no significant difference in readability for all values of grade for dark mode](<https://lh3.googleusercontent.com/L1yLlLcewN03yJZHNNg6gCDcxdad47bJ0hIEvcNlmLIIg5GyrvspcD3kSaInASs3r3vgmxaU-xCWqHAR5Sis4amEid8ct5ZikbeYom0veerCc4G6CT8>)

Showing readability across light mode vs dark mode, here calling out no significant difference in readability for all values of grade for dark mode

_**First**_, we didn’t find any impact of grade on readability for dark mode. What does this mean, you might be asking? Are you free to run wild and use any value of grade in dark mode wherever you want, willy nilly, without fear of repercussion or rebuke?

Not _quite_. While we didn’t find a significant impact of grade on readability in dark mode, it’s possible that in other contexts—for instance, reading long paragraphs instead of short phrases—the difference might be significant. But for the contexts we tested, we’d suggest that design can select a value of grade they feel is most appealing, without that grade value negatively impacting readability.

![Showing readability across light mode vs dark mode, here calling out a significant difference in readability between the lowest and highest grade values for dark mode](<https://lh3.googleusercontent.com/TV7TozFl8Zk-ANMLlc91yL2DiCx9YdVvVetGL3Y58886VWezw8k8-IMf6-xbxgvYPDf6McQhZbuF9begLY54993QZxjePxx_vnGRKaaf55yefrRJgXM>)

Showing readability across light mode vs dark mode, here calling out a significant difference in readability between the lowest and highest grade values for dark mode

_**Second**_, we _did_ find a significant difference between the lowest and highest values of grade in light mode. As the graph above shows, readability appears to gradually increase (i.e., reading time decreases) as the _grade_ value increases. You can see the greatest difference between the reading time grade at 150 versus grade at -112.5 is about a tenth of a second (1.92 to 2.02 seconds). This may not sound like a lot, but remember that this is the average difference in time it takes to read two short words—that difference can add up!

So what does this mean? Basically, in light mode, higher values of grade result in text that is more readable. This difference doesn’t appear to exist when the grade value is 0 (the default), so increasing grade arbitrarily from the default won’t improve readability significantly. But if you’re considering adopting the _minimum_ value of grade (i.e., -200 in the graph above), you should be sensitive to the fact that this may have an adverse impact on the readability of your text. Which is not to say that you should never use these values, but consider using them in a context where that impact is balanced by the aesthetic or brand goals you’re trying to achieve.

![Showing readability across light mode vs dark mode, here calling out a significant difference in readability between the highest grade value in light mode and grade values in dark mode above around -115](<https://lh3.googleusercontent.com/OrVr4xEo02eAO3aPb1zGsQF1y9fOsssjWzLYxV1hx1K2L4qQVfqSyTvY37EFPb45D5Wem5ImUXf8sLwpUyblwPUM_DiO9Lp-LSarzklZceTGvtnl4Kc>)

Showing readability across light mode vs dark mode, here calling out a significant difference in readability between the highest grade value in light mode and grade values in dark mode above around -115

And _**third**_, we did find a significant difference between the highest values of grade in light mode, and most values of grade in dark mode above around -115. This is where it gets interesting (I mean, I think the whole thing above was _interesting_ too, but this is where we get to answer the question we set out to answer, around finding equivalent readability between dark and light modes). This graph allows us to draw two final conclusions.

First, if we’re looking to identify grade values that result in equivalent readability between light and dark mode, when you’re using the highest values of grade in light mode, you’ll need to approach the lowest values of grade in dark mode before you’ll start to see equivalent readability (i.e., before you’ll start to see reading times that _aren’t_ significantly different between light and dark modes).

However, the visual difference between minimum and maximum grade values is a dramatic change! If you’re using the maximum grade value in light mode and are creating an equivalent dark mode, consider the balance between equivalent readability and equivalent visual design—you may be able to approach equivalent readability by minimizing grade in dark mode, but you should be sensitive to the impact this may have on how people perceive the two modes of your product as complimentary.

And last, revisiting the question we set out to answer at the top: _**what font grade is equally readable in light mode versus dark mode?**_ We can now answer: if you’re using the default value of grade (0), our results suggest there is _not_ a significant difference in readability between light and dark mode. So, in the context we tested, there is no need to modify grade between light and dark modes to achieve equivalent readability.

And _that_, is how I met your mother. Jk, but that’s pretty much how we tackled this research question, and how you might be able to tackle similar questions for your own products, in your own contexts.

## Third: Where you can go from here

I hope the above shows you a bit more clearly how you might _collect_ and _interpret_ data that allows you to more effectively measure text readability. A reasonable next question might be: where do we go from here? How might we use this information to build better experiences?

Most immediately, I’d suggest that the above should provide a more clear path towards understanding and improving readability for a _specific audience_ in a _known context_. If you know who is using your products and how those products are being used, you should be able to adopt the above approach to fine tune text design for those specific contexts of use.

More broadly, though, I think there’s a _**huge**_ opportunity to use approaches like the one described above to not only iteratively improve existing product experiences, but to envision new ways of making interactions more _usable_ and more _accessible_ in any situation.

Consider for example designing a product that can be used both when you’re right next to it or when you’re across the room.

![Animation of a small screen in tablet orientation showing the text, “Your order is 7 mins away.” As the viewer backs away from the screen the text size and grade increases, as the text changes to “7 mins away,” showing that the text is always readable at multiple distances](<https://lh3.googleusercontent.com/u0Y_JNSbFzBdT-phyhew_8DsEcbdzkQgkIltSXKdU-mVchOFQZ0ZYeVep4apZt1jZSNHaRKLNhinzpRYadel2dSbkgaYtfEr3rVJIilT8Zr8H7GTKS8>)

An example of how the approach to readability research described above could be used to refine the experiences of future interactions

Using the approach we’ve described above, you can start to optimize for readability _no matter how the product is used_. With this type of approach, we can start to move from readability and accessibility as a static and stable factor in design, to a future where accessibility can be sensitive to _any situation_, and even to _any individual person_.

This is a pretty neat future, I think. And I’m excited to continue doing the work that helps us get there.
