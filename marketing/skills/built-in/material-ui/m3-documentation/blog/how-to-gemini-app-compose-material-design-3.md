---
title: "Using the Gemini app with Material Design 3"
source_url: "https://m3.material.io/blog/how-to-gemini-app-compose-material-design-3"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/79d8bab656a2-_GAV0tea_Vg-fwl92xjOoBHoZDyxl01gNGKMZJdDgPBkEIfJfkjvDGgLRmIQhDXkRyh3vfhhkznA7Zx74IFqADVf1nQoSAa5.png"
  - "assets/images/f249b87a20a2-HSSMuL9CDG3tJeHLhaClmW-SgTVZtAdPTtSIsqlfujD2HqU2wb-e3sQKpskYrAtAbORmlUiZ9i1jsb-AcPEa5GBYHb8Nl6Sb.png"
---

# Using the Gemini app with Material Design 3

Example prompts for designers and developers

![Image](<https://lh3.googleusercontent.com/_GAV0tea_Vg-fwl92xjOoBHoZDyxl01gNGKMZJdDgPBkEIfJfkjvDGgLRmIQhDXkRyh3vfhhkznA7Zx74IFqADVf1nQoSAa59JWy3o3LyTUGs0gGLO4>)

Published: 2024-05-07T13:00:00

As a UX researcher on the Material Design team, I run lots of research studies with designers and developers. More and more, I see Android developers try to use Large Language Models (LLM) tools as they learn about and use Material Design 3 components on Compose. When I see developers struggle to get helpful results from these powerful tools, I want to help.

How can I help? My first thought was that I wanted to change the information in the model itself, however, that is not really possible. So, I decided to take a look at general AI prompts that are used to get informational output from the model and how to expand the prompts specifically related to learning and using Material Design. These example prompts can be used by designers and developers!

I’d recommend taking a look at the [Prompt Best Practices](https://ai.google.dev/gemini-api/docs/prompting-strategies). I reviewed the prompt recommendations and considered use cases specific to using Material Design and user interface design and development process.

So, let’s go over a few basic AI prompt writing best practices and show some Material Design specific examples.

_Just a heads up: These prompts are suggestions for use with [the Gemini app](https://gemini.google.com/). Your results may vary._

## 1\. Prompting with text and an image

**Why It Matters**

Working on the user interface for an app or website, designers create a mockup to handoff to developers to reference during the coding and implementation phase.  There are several different user experience (UX) aspects that designers and developers need to take into consideration such as:  Portrait vs landscape orientation, light and dark mode, and any accessibility concerns, just for a start. You can use an image to determine which specific Material Design component aligns with patterns shown in the mockup.

**Example 1: Identify UX aspects based on a mockup**

**Prompt 1:**

Review this mockup showing a Tablet UI and show me all of the Material design 3 components I should use based on the mockup. Include portrait and landscape view considerations, include light and dark mode and any accessibility needs for the app. Then, show me Compose Kotlin code for this screen based on the descriptions generated.

![Android mockup](<https://lh3.googleusercontent.com/HSSMuL9CDG3tJeHLhaClmW-SgTVZtAdPTtSIsqlfujD2HqU2wb-e3sQKpskYrAtAbORmlUiZ9i1jsb-AcPEa5GBYHb8Nl6SbhcxVHyMbDowO617w9RsL>)

Uploaded mockup

**Gemini app output:**

Sure, I can review the mockup and discuss Material Design 3 components, portrait and landscape considerations, light and dark mode, and accessibility needs for the app. However, I can't generate the Compose Kotlin code for this specific screen since it requires knowledge of the app's functionalities and data structures which are not available from the image.

**Material Design 3 components in the mockup:**

-   **Top app bar:** The mockup uses a top app bar with the app name "Your Family Tree" centered. Material Design 3 recommends using large titles centered in the toolbar for tablet apps Material 3 design tokens.
-   **Navigation rail:** The mockup uses a navigation rail on the left hand side of the screen with five destinations: "Home", "Tree View", "Map view", "Story view", and "History view". Material Design 3 recommends using navigation rails for applications with more than three destinations Material 3 navigation: [https://material.io/components/navigation-rail/](https://material.io/components/navigation-rail/).
-   **Cards:** The mockup uses cards to display information about the user's family tree. Material Design 3 uses cards to group and present information. Cards can be used with other UI components to create a variety of layouts Material 3 cards: [https://material.io/components/cards](https://material.io/components/cards)**.**
-   **Buttons:** The mockup uses multiple buttons including an elevated button with the text "Add Person" and a text button with the text "Close". Material Design 3 offers different button types including text buttons and elevated buttons Material 3 buttons: [https://material.io/components/buttons](https://material.io/components/buttons).
-   **Text fields:** The mockup shows a text field where the user can input their name. Material Design 3 offers a variety of text field styles including outlined text fields Material 3 text fields: [https://material.io/components/text-fields](https://material.io/components/text-fields).

**Portrait and Landscape view considerations:**

-   In portrait view, the navigation rail can be displayed on the left side of the screen, taking advantage of the additional screen real estate of tablets.
-   In landscape view, you may consider using a bottom navigation bar instead of a navigation rail, as it may be more convenient for users to reach with their thumbs.

**Light and dark mode:**

-   Material Design 3 supports both light and dark themes. When switching between light and dark mode, you should ensure that there is sufficient color contrast between text and background colors to meet WCAG accessibility guidelines WCAG contrast checker:

**Accessibility needs:**

-   The app should use sufficient color contrast between text and background colors.
-   The app should use clear and concise language in the UI.
-   The app should be compatible with screen readers.

I hope this helps! Let me know if you have any other questions.

✧

**Comments about the output:**

1\. There is a tool that Android developers can use called the [Accessibility Scanner app](https://play.google.com/store/apps/details?id=com.google.android.apps.accessibility.auditor) that developers can use to check screens and will alert contrast issues.

2\. Accessibility needs section is missing information on touch targets.

## 2\. Break down complex requests into multiple smaller prompts

**Why It Matters**

Using easier-to-understand chunks or tasks will result in better output from the LLM. This allows you to iterate and focus on specific aspects of the model's output. It’s almost like having a conversation.

After Example 1, I wanted to break down the one complex prompt into smaller chunks to compare the Gemini app output.

**Revised Prompt 1:**

Review this mockup showing a Tablet UI and show me all of the Material design 3 components I should use based on the mockup. Include portrait and landscape orientation considerations.

**Gemini app output:**

Based on the mockup you sent, here are the Material Design 3 components you can use:

**Top App Bar**

-   A top app bar  is used at the top of the screen and  contains the app title  and  other actions, such as a search bar or  avatar.  In the portrait view, you can use the app title and a  close button. In landscape orientation, you can include additional actions such as  Search  and  Contact Us.
-   Material Design 3 recommends using a large font for the title on tablets  [https://m3.material.io/components/top-app-bar](https://m3.material.io/components/top-app-bar).

**Navigation**

-   **Navigation drawer**:  A navigation drawer  is a panel that slides in from the left or right side of the screen.  This is a good option for portrait  as it  efficiently uses screen space. Material Design 3 recommends using a top app bar  alongside a navigation drawer in tablet apps [https://m3.material.io/components/navigation-drawer/specs](../components/navigation-drawer/specs.md).
-   **Tabs**:  You can use tabs  in landscape mode to organize content across different screens and views. Tabs are a good way to  improve the  discoverability of  different functions  within the app [https://m3.material.io/components/tabs/guidelines](../components/tabs/guidelines.md).

**Content**

-   **Cards**: You can use cards to display  information  like  "John Scott" and "History view". Cards  elevate content priority and  bring focus to specific elements of your app [https://m3.material.io/components/cards/](https://m3.material.io/components/cards/).
-   **Buttons**:  Different  types of buttons  can be used throughout the app  depending on their function.  In the mockup, you have a text button that says "Close" and  a floating action button (FAB)  with a plus sign (+) which  could be used to create a new family member entry. Material Design 3  offers various button options including  text buttons, outlined buttons,  and  contained buttons [https://m3.material.io/components/buttons/](https://m3.material.io/components/buttons/).
-   **Typography**: Material Design 3 emphasizes  using a strong typographical hierarchy  to  improve the readability of  your app. Use a combination of  headings, subheadings and body text  to  structure  your content [https://m3.material.io/styles/typography](https://m3.material.io/styles/typography).

**Responsive Design Considerations**

-   Material Design 3 promotes responsive design which means your app layout should adapt to different screen sizes and orientations. In this mockup, the content can be stacked on top of each other in portrait mode and laid out side by side in landscape mode.

I hope this helps!

✧

**Revised Prompt 2:**

What are any accessibility needs for this screen, including light and dark mode?

**Gemini app output:**

Here are some accessibility needs for the tablet UI screen you're designing, considering both light and dark mode:

**Color Contrast**:

-   Ensure a **high color contrast ratio** between text and background elements in both light and dark mode. This is crucial for users with visual impairments.  Use a contrast checker tool to verify your choices meet WCAG (Web Content Accessibility Guidelines) standards
-   **Avoid relying solely on color** to differentiate information. Use **text labels** in addition to color-coded elements.

**Text Size and Readability**:

-   Use a **large enough font size** for body text and headings, especially on tablets.
-   Choose a **clear and easy-to-read font** that works well in different sizes.

**Light and Dark Mode Consistency**:

-   Make sure the **information hierarchy** and **visual elements** remain consistent across both light and dark modes. Users shouldn't be confused by drastic changes in layout or functionality when switching modes.
-   Use **appropriate color adjustments** for icons and other visual elements to maintain clarity in both light and dark themes.

**Additional Considerations**:

-   **Focus indicators**: Clearly indicate which element has focus when using the keyboard to navigate the app. This helps users with motor impairments or those who rely on screen readers.
-   **Alternative text (alt text)**: Provide alt text descriptions for images to convey their meaning to screen reader users.
-   **Touch targets**: Make sure buttons and other interactive elements have a large enough touch target size for users with motor impairments.

By considering these accessibility needs, you can create a tablet UI that's usable and comfortable for everyone, regardless of their visual abilities or how they choose to interact with the app.

✧

**Comments about the output:**

Comparing the outputs from the two revised prompts in Example 2, the model did provide a bit more detail about accessibility. This is something to consider when writing your prompts is adding the right level of context without trying to add all questions at one time.

## 3\. Provide contextual information

Providing context helps the model understand in more detail what you're asking for. Here are some suggestions of contextual information specific to Compose Material 3 to include in your prompts.

-   m3.material.io
-   Material Design 3
-   Compose Material
-   Jetpack Compose
-   Kotlin
-   New to Compose Material

**Why It Matters**

While using the name “Material Design”  aligns with  our design guidance and code samples, we are now on our third version. Using just that “Material Design” name may not be enough for the prompt. Design guidance is agnostic and  we do have code examples for different programming languages, such as Java-Views, Kotlin, Flutter and Compose. Vague prompts might lead to incorrect information or code for your project.

Plus, by including your knowledge level, such as “new to Material Compose,” you ensure more learning-focused content.

Example prompts with contextual information in bold:

1.  What is the migration effort for a **Compose app** from **Material Design 2** to **Material Design 3**?
2.  Using **m3.material.io**, show me **Kotlin** code for a **Jetpack Compose** app where the theme has the primary color = #1e555c and the secondary color = #f4d8cd. font = roboto. I am **new to Compose Material 3**.

Another suggestion is that you use any generated code as a starting point. Any code generated may need adjustments.

[FAQ: Can Gemini help with coding?](https://gemini.google.com/corp/faq#coding)

Yes, Gemini can help with coding and topics about coding, but you are responsible for

your use of code or coding explanations. So you should use discretion and carefully test and review all code for errors, bugs, and vulnerabilities before relying on it.”  Source: [https://gemini.google.com/faq#coding](https://gemini.google.com/faq#coding)

✧

These LLM tools are still evolving and for our example use cases, I expect to see improvements in the future.

Over on Material Design, we’re incredibly excited to see how we can use Gemini in different ways  to help designers and developers learn and implement our system for their Android apps.

Thanks to Shalom Gibly, Nikita Singh, Mark McDonald, Liam Spradlin, Barbara Eldredge,  Lorie Napolitano and Frank Bentley for their contributions to this article!
