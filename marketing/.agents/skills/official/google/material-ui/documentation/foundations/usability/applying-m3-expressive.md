---
title: "Usability"
source_url: "https://m3.material.io/foundations/usability/applying-m3-expressive"
lastmod: "2026-01-22"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/83efb4db933d-mhjgyvci-Core-Flow.png"
  - "assets/images/df02fe3e0013-mejir1iw-Visual-Hierarchy-(1).png"
  - "assets/videos/c3887c2bc048-mhjh7by4-GM3_Usability_Aura-app_Hold-included_IA_v02.mp4"
  - "assets/images/89442e9a4575-mejiz13p-Breathing-Report-Do.png"
  - "assets/images/6d9a9be6a7b3-mejj1yj5-Progress-WIP.png"
  - "assets/images/745a1916ee5c-mejj3nr8-Home-Caution-Uglyfied.png"
  - "assets/images/85bcc68cebd8-mejj4ua6-Home-Right.png"
  - "assets/images/75e5e24e31a2-mejj6t5h-Home-Caution-Uglyfied-Deux.png"
  - "assets/images/9bff28e50fe6-mejj8ce1-Frame-2147228808.png"
  - "assets/images/2b50627e0711-mejjaaz6-BEFORE-Primary-Secondary-roles.png"
  - "assets/images/280a459b44a5-mejjb7xf-AFTER-Primary-Secondary-roles.png"
  - "assets/images/8e29ab1598b4-mejjcz6i-Breathing-Report-Caution.png"
  - "assets/images/4db843849516-mejjk8lk-Breathing-Report-Do-(1).png"
  - "assets/videos/835f25600fed-mhjhu4pj-GM3_Usability_Aura-app-Caution_IA_v01.mp4"
  - "assets/videos/a726fb5c2097-mhjhxmxp-GM3_Usability_Aura-app-Do_IA_v02.mp4"
---

# Usability

Usability focuses on making products intuitive and easy to understand for everyone

## Applying M3 Expressive

## Aura: An example app showcasing usability with M3 Expressive

![Four key screens of the breathing app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhjgyvci-Core%20Flow.png?alt=media&token=9ddfd7a6-8f5c-4a4a-a00e-1c37dbb7af84>)

_The four goals in the Aura breathing app: Start a breathing session; Experience and complete a breathing session; View the breathing session results; Check progress towards personal goals_

Aura is a conceptual breathing app that illustrates how [Material 3 (M3) Expressive design tactics](../../blog/building-with-m3-expressive.md#what-rsquo-s-in-the-update) can make an app more usable and draw the user’s attention to the most important actions. It’s used with a smart watch to measure heart rate. It was created based on the [eye tracking and focus group research](https://design.google/library/expressive-material-design-google-research) that played a key role in the creation of M3 Expressive. Research showed that participants were able to spot key UI elements up to **four times faster** in the M3 Expressive designs compared to other designs.  

Examples include:

-   Using scale, color, and containment to guide people to start a breathing session
-   Using shape, color, and empty space to guide people to breathe slowly and with intention
-   Minimizing cognitive load by using empty space and fewer actions, so people can stay focused on their breath
-   Balancing primary tasks with supportive data to show progress and impact of a session

## Example 1: Start a breathing session

Starting the session is the primary goal when opening the app. Size, placement, color, and contrast guide the user to the **Start breathing** button. The button’s large size and low placement makes it easy to reach when holding a phone.

The **settings** use a secondary color to draw attention, but they’re not as emphasized as the button.

The **daily message** is the least emphasized, but uses large containment and type to draw attention.

Expressive components used:

-   [Extra large button](../../components/buttons/overview.md)
-   [Button groups](../../components/button-groups/overview.md)
-   [Switch](../../components/switch/overview.md)
-   [Navigation bar](../../components/navigation-bar/overview.md)

![3 elements of the landing page of a breathing app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejir1iw-Visual%20Hierarchy%20(1).png?alt=media&token=491a7dfb-2018-4564-a226-807a2586fdd5>)

_App elements in order of emphasis: The Start breathing button is the primary goal of the app; Breathing session settings; Daily welcome message_

### Usability design tactics

| Color & contrast | The prominent dark purple **Start breathing** button (1) on a soft light purple background uses [Material primary color roles](../../styles/color/roles.md) to create high contrast, making the button easy to find and remember. |
| --- | --- |
| Hierarchy | The main goal is to tap the large **Start breathing** button (1). The daily message (3) and settings (2) are in lighter colors and in subtle containers because they are supportive actions, but not required. |
| Placement | The button (1) is close to the bottom so it’s easy to reach. It’s the final, most prominent element in the vertical flow, naturally guiding the eye down the screen without competing with other content. |
| Shape | The rounded button form reinforces it as a distinct, touchable control. |
| Size | The button (1) is extra large to make it the most emphasized element on the screen. |
| Spacing | Generous spacing separates the button (1) from the message (3) and settings (2). |
| Typography | The button (1) has larger text to emphasize the primary action. |
| Visual harmony & hierarchy | The daily message is placed at the top in a soft blue container with medium sized text, setting a reflective tone without drawing too much focus. The hierarchy guides the user from the daily message (3) to settings (2) and finally to the **Start breathing** button (1). |

## Example 2: Breathing session (inhale & exhale)

The guided breathing exercise is the [hero moment](../../blog/building-with-m3-expressive.md). A large central flower expands and contracts, serving as the visual guide for each breath, while a countdown shows remaining seconds for inhaling, exhaling, and holding the breath. 

The **pause** and **stop** buttons are less prominent than the flower to encourage people to focus on the session. The buttons are placed at the bottom so they’re easy to reach.

Expressive components and styles used:

-   [Large buttons](../../components/buttons/overview.md) in a [button group](../../components/button-groups/overview.md)
-   [Material shape library](../../styles/shape/overview-principles.md#579dd4ba-39f3-4e60-bd9b-1d97ed6ef1bf) (“flower” and “sunny”)
-   [Emphasized typography](../../styles/typography/type-scale-tokens.md#c898d7e2-4833-440c-9dba-9a95c8f50ac9)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhjh7by4-GM3_Usability_Aura%20app_Hold%20included_IA_v02.mp4?alt=media&amp;token=308f9d53-cdec-4275-b0ae-4647844e6366" title=" A flower shape getting larger as a user inhales, and smaller as they exhale."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhjh7by4-GM3_Usability_Aura%20app_Hold%20included_IA_v02.mp4?alt=media&token=308f9d53-cdec-4275-b0ae-4647844e6366>)

### Usability design tactics

| Color & contrast | The vibrant yellow appears when inhaling to contrast the soft purple background and be obvious. |
| --- | --- |
| Motion | The flower expands and contracts to guide the pace of the breath. The motion uses [Material Spring Motion Tokens](http://figma.com/community/plugin/1397759704974764283/material-motion). |
| Placement | The **pause** and **stop** buttons are at the bottom, spaced away from the flower, but easy to reach. The navigation bar hides during the breathing journey. |
| Shape | The flower uses the “flower” and “sunny” [Material shapes](https://m3.material.io/google-material-3/pages/shape/overview-principles#76fb0225-57af-4497-83d0-2b8827505fba) to draw attention and clearly stand apart from the simple **pause** and **stop** buttons. |
| Size | The size of the animating, breathing flower dominates the screen to draw attention. |
| Typography | The countdown numbers are very large in comparison to the **inhale**, **hold**, and **exhale** text to focus attention on the exercise. This provides strong visual contrast, while still keeping the instructions associated with the countdown. |

## Example 3: Breathing report

The breathing report comes after the breathing exercise. It’s the secondary goal in the app, not the primary hero moment, so it uses fewer design tactics to reduce cognitive load.

It draws attention to each data point on the page using shapes and decreasing size. The button is less emphasized than the **Start breathing** button on the landing page.

Expressive components and elements used:

-   [Medium button](../../components/buttons/overview.md)
-   [Material shape library](../../styles/shape/overview-principles.md#579dd4ba-39f3-4e60-bd9b-1d97ed6ef1bf)
-   [Emphasized typography](../../styles/typography/type-scale-tokens.md#c898d7e2-4833-440c-9dba-9a95c8f50ac9)

![The breathing report provides data (total breaths taken, exercise duration, heart rate/beats per minute (BPM), and breaths per minute) from the user’s breathing session. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejiz13p-Breathing%20Report%20Do.png?alt=media&token=76515d71-3997-4020-8d6d-2ed04f8d3a7d>)

_Shapes and smaller sizes emphasize key information and draw attention down the screen to the Finish button_

### Usability design tactics

| Color & contrast | Dark text on the light flower shapes creates a strong contrast, making each metric easy to read at a glance. The solid, dark purple **Finish** button stands out clearly against the light purple background, guiding users to the next step. |
| --- | --- |
| Placement | The metrics are spread out across the screen in a loose cluster, guiding the user from one to the next. The **Finish** button is centered at the bottom so it’s easy to reach. |
| Shape | The metrics are inside Material flower shapes, making achievements stand out. The use of flower shapes make the design look consistent. |
| Size & typography | Key numbers like **18** and **3min** are larger and use emphasized styles, making it easy to scan the most important data. |
| Spacing & grouping | Ample spacing around each metric avoids clutter, while making the layout tide any scannable. Playful Material shapes serve as clear containers, making the grouping feel lively. |

## Example 4: Check progress

The progress report is a tertiary goal in the app to track statistics and see progress over a monthly view. Since it’s not a primary goal or hero moment, it uses more subtle design tactics to make the app usable and draw more attention.

The key data is dark on a light background to draw attention, while the yellow shapes on the calendar highlight completed sessions.

Expressive components and elements used:

-   [App bar](../../components/app-bars/accessibility.md)
-   [Progress indicator](../../components/progress-indicators/overview.md)
-   [Medium button](../../components/buttons/overview.md)
-   [Navigation bar](../../components/navigation-bar/overview.md)
-   [Material shape library](../../styles/shape/overview-principles.md#579dd4ba-39f3-4e60-bd9b-1d97ed6ef1bf)
-   [Emphasized typography](../../styles/typography/type-scale-tokens.md#c898d7e2-4833-440c-9dba-9a95c8f50ac9)

![A progress screen shows data in dark primary colors, then statuses in secondary yellow colors in a progress bar and calendar view.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejj1yj5-Progress%20-%20WIP.png?alt=media&token=5fc4dfb1-176f-49d0-8f7b-765f892be572>)

_Large text and colorful completed sessions draw attention to the key progress information_

### Usability design tactics

| Color & contrast | The data uses the **primary** role to be darker than all other elements and draw attention. The yellow accents on the daily goal bar and calendar use the **secondary** roles to highlight progress. |
| --- | --- |
| Grouping & spacing | The metrics (days, breaths, and minutes) are grouped together. The equal vertical spacing between elements makes it easy to scan data. |
| Shape | The rounded Material flower shape on the highlighted days in the calendar makes data more expressive. The flower shapes remind the user of the breathing exercise visualization. |
| Size | Custom-scaled numbers for key statistics such as days, breaths, and minutes are large enough to scan, but they don’t dominate the screen. |

## Testing & iteration improves the experience

By testing the experience with users, it’s easy to identify usability issues and address them. From version 1 to 2, the design shifts from cluttered to calm and simplified.

![A button to start breathing followed by loosely-grouped settings for duration, sound, and haptics, all with their own styles. ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejj3nr8-Home%20Caution%20Uglyfied.png?alt=media&token=e2a6e7f0-9258-4058-9ccd-12b0153b9683>)

_Version 1: No containment, similar sizes, and inconsistent colors_

![A list of settings consistently styled followed by an extra large button to start breathing.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejj4ua6-Home%20Right.png?alt=media&token=bb67336a-ca85-47c7-b256-8cfdd3c57717>)

_Version 2: Neatly grouped settings, an extra large button, and consistent secondary color usage_

Version 1 loaded the screen with settings for duration, sound, and haptics all fighting for attention, creating unnecessary complexity. In Version 2, these options are neatly grouped as list items above the main action. The focus stays on starting the breathing session.

By carefully using hierarchy, containment, shape, and color, the final design is easier to follow and more intuitive. Exploring different design refinements can result in an experience that feels intuitive and easy for the user to follow.

## Best practices for applying usability design tactics

### Use clear scale and placement

Avoid crowding the screen with too many large or equally prominent elements. Scale and placement create a clear focal point.

![The aura app home page with similar-sized elements competing for attention.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejj6t5h-Home%20Caution%20Uglyfied%20Deux.png?alt=media&token=a214af02-3324-4217-8569-0127b5c35574>)

_All the elements were large and competed for attention; The visual hierarchy was unclear; The settings weren’t grouped together_

![The aura app home page with a clear visual hierarchy and appropriate emphasis per element.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejj8ce1-Frame%202147228808.png?alt=media&token=301c51d8-85ae-48e3-ab93-65118c37d8e9>)

_The large Start breathing button is a strong visual focal point; The supporting controls are clear, but less prominent than the Start breathing button_

### Reinforce with consistent color roles

Use different color roles for actions and data to create a visual hierarchy that makes it simple for users to identify what they can do.

![3 color roles incorrectly used in the aura app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejjaaz6-BEFORE%20Primary%20Secondary%20roles.png?alt=media&token=28275bf0-9d9a-4459-a8b1-04d56af20d17>)

_The same color roles, primary (1, 2) and primary container (3), are used for all actions and data_

![3 color roles used in the aura app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejjb7xf-AFTER%20Primary%20Secondary%20roles.png?alt=media&token=1090a0c2-bb81-424a-b7fb-723f1ac53548>)

_The primary color role (1) makes the button clear and prominent; The secondary color on selected settings (2) and secondary container color on the dates (3) contrast with the background and primary colors_

### Create calm, balanced layouts

Use uniform shapes and sizes. Add space between shapes and data to make it simple to compare data. Create gentle visual rhythm by aligning elements in a consistent flow to support a serene, focused experience.

![Breathing report statistics in containers that overlap each other.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejjcz6i-Breathing%20Report%20Caution.png?alt=media&token=aa6a75cb-0658-464a-a8b2-ab3d742a17b4>)

_The shapes have different forms and sizes; The shapes overlap_

![Breathing reports statistics in containers neatly organized on the page.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmejjk8lk-Breathing%20Report%20Do%20(1).png?alt=media&token=7bda3385-364d-4afb-8d04-abc30149a700>)

_There’s more even spacing between shapes; The shapes are uniform_

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhjhu4pj-GM3_Usability_Aura%20app%20Caution_IA_v01.mp4?alt=media&amp;token=2b123aee-30a0-41e0-b1c4-ceaf89dc0fc2" title="Arrows, countdown numbers and text move along with the purple and yellow flower that opens and compresses. "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhjhu4pj-GM3_Usability_Aura%20app%20Caution_IA_v01.mp4?alt=media&token=2b123aee-30a0-41e0-b1c4-ceaf89dc0fc2>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhjhxmxp-GM3_Usability_Aura%20app%20Do_IA_v02.mp4?alt=media&amp;token=7d74c30b-2055-46d0-b60e-9c58aa3068a8" title="The purple and yellow flower expands and contracts, numbers appear for the exhale, hold and inhale"></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmhjhxmxp-GM3_Usability_Aura%20app%20Do_IA_v02.mp4?alt=media&token=7d74c30b-2055-46d0-b60e-9c58aa3068a8>)
