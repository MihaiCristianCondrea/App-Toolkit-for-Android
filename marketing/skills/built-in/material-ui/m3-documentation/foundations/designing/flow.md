---
title: "Designing"
source_url: "https://m3.material.io/foundations/designing/flow"
lastmod: "2025-10-08"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/74ae75052e62-m0qvgj8o-5.png"
  - "assets/images/628f16a6a073-lwj43qm4-6.png"
  - "assets/images/6e0539dd2a88-lwj441r3-7.png"
  - "assets/images/74bd8fcbae52-lwj44h3h-8.png"
---

# Designing

Implement intuitive, accessible layouts, considering structure, color, and flow

## Flow

## Focus order & key traversal

People should be able to navigate and interact with your app without the use of a traditional mouse or touch screen. To support navigation by keyboard, screen reader, or other assistive technology, goals should be achievable by using **tab**, **arrow**, **and other common navigation keys**. 

Simplify your flows by: 

-   strategically ordering tab stops 
-   reducing overall page complexity

### Use defaults

Avoid adding more work for yourself by using **predefined tab ordering,** unless a user journey needs special tailoring. The **default order follows the DOM** (the order of content as it's written in the source code) and **generally flows from left to right**; **top to bottom**. Keyboard navigation (key traversal) may be pre-defined within common components. Use the defaults unless you have a UX pattern or custom component that breaks from the default pattern.

### Determining user flows

#### **1\. Group product use cases**

Group product use cases into primary and secondary user journeys. The priority of your use cases should influence the decisions you make about the priority of user flows.

#### **2\. Define initial focus and component-level focus**

Focus refers to which control is currently the active target of user interactions, such as mouse clicks or keyboard taps. Generally, the **tab** key moves focus between interactive elements. 

Define the **initial focus** when a user loads a screen, as well as initial focus for components with multiple interactive elements, like a complex card or a dialog.

![A Google home screen showing the initial focus on the search bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qvgj8o-5.png?alt=media&token=dabd6a95-ac7c-4bba-bbc0-c348916035b8>)

_In the case of the Google homepage, even though there are links and buttons above and surrounding the search field on the page, it makes sense to put the user's initial focus on the element that supports the most common user goal_

Focus is particularly important when an element is activated by the user or the user changes context. 

For example, when a dialog is triggered, check for the following:

-   Focus is set to the dialog component, likely to a specific interactive element within the dialog such as a text input field or edit button
-   When the user closes or cancels the dialog, focus returns to the interactive element that initiated the action

![Diagram showing changing points of focus as a user opens and closes a calendar card.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj43qm4-6.png?alt=media&token=a3d35c37-a97f-4262-8451-6bfc922a671c>)

_Define initial focus and component-level focus_

#### **3\. Define any atypical key traversal through the page and components**

Users should be able to complete the primary and secondary user journeys using tab, arrow keys, and other keyboard shortcuts.

![Card with three interactive elements in the top right corner.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj441r3-7.png?alt=media&token=ad672a23-24bb-45b6-b2f2-633553c3a1c8>)

_Navigating the interactive elements on a card via tab_

**Tab** typically moves focus between interactive elements and is often used as primary navigation. **Tab +** **Shift** reverses direction.

**Arrow keys** are typically used to navigate within components (for example, moving between cells in a form or traversing items in a menu.)

**Enter** activates a link or button, or sends a form when a form item has focus.

![Diagram showing a group of three sub-elements, with navigation within the group assigned to arrow keys.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwj44h3h-8.png?alt=media&token=d1d4349e-09f9-4ee5-ad33-143a7d576537>)

_In the case of unique layouts and use cases, it can help to group a collection of interactive elements as one tab stop, and use arrow keys to traverse sub-elements Using Tab navigation to focus group; Using arrrow key to traverse sub-elements_

## Keyboard shortcuts

Keyboard shortcuts help users access menus and app functions without using a mouse on desktop apps and websites.

### Requirements

These requirements are important for helping speech users avoid activating multiple shortcuts at once and for keyboard-only users to minimize unwanted actions.

-   Keyboard shortcuts should use a combination of two or more keys by default.
-   Include a tutorial, list, or help center page of all custom keyboard shortcuts in your product. For example, Cmd+Z (Ctrl+Z) to undo deleting an event in Google Calendar.
-   If a keyboard shortcut is activated with a single key, provide users with a way to take at least one of these actions:
    -   **(Most preferred)** Remap the shortcut to include one or more non-printable keyboard keys.
    -   **(Preferred)** Activate the shortcut only when a relevant component is focused.
    -   **(Not preferred, only use as a temporary solution)** Turn off the keyboard shortcut.
