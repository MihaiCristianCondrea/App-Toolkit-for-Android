---
title: "Dialogs"
source_url: "https://m3.material.io/components/dialogs/guidelines"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/68c0d68b0c0b-m8sf9qay-01.png"
  - "assets/images/5a83b92362be-m8sfc7pz-02.png"
  - "assets/images/2a1ac7ea588f-m8sfcqhr-03_do.png"
  - "assets/images/8d72e57553b5-m8sfdc9w-03_dont.png"
  - "assets/images/2e5c14f2fb3b-mk29xmfo-05-3P.png"
  - "assets/images/f43e2f6859e9-m8sfhos4-07.png"
  - "assets/images/e1f2ebc441bd-m8sfick9-08.png"
  - "assets/images/c12d338c63b5-m8sfkf69-09.png"
  - "assets/images/3005ac8a7032-m8sfl8j1-10_do.png"
  - "assets/images/cb93420c4936-m8sflmm0-11_don't.png"
  - "assets/images/aec4bc7d7b07-m8sfm2yt-12_Caution.png"
  - "assets/images/52bdf11c9faf-m8sfmezr-13_do.png"
  - "assets/images/13682c558c7d-m8sfmqqd-14_do.png"
  - "assets/images/c429265de68d-m8sfn276-15_don't.png"
  - "assets/images/e55f733114df-m8sfq9tm-16_do.png"
  - "assets/images/adc7f98339ff-m8sfqm2i-17_don't.png"
  - "assets/images/16e187a18df7-m8sfqzp1-18_do.png"
  - "assets/images/65f3db822f85-m8sfre4i-19_caution.png"
  - "assets/images/a30479224065-m8sfrskh-20.png"
  - "assets/images/60bf0a95128b-m8sfumns-21.png"
  - "assets/images/72910fb3e183-m8sfuxlx-22.png"
  - "assets/images/dc551dc323b4-m8sfzk1g-23.png"
  - "assets/images/73ee67854bb1-m8sfxxnd-24.png"
  - "assets/videos/1b9adc245bca-mbh08c2v-GM3_Dialogs_Guidelines_25_IA_v02.mp4"
  - "assets/videos/c6770af6c866-mbh098mh-GM3_Dialogs_Guidelines_26_IA_v01.mp4"
  - "assets/images/83bba68e7400-m8sg5wkr-27_do.png"
  - "assets/images/90e965be2a17-m8sg67kv-28_don't.png"
  - "assets/images/cc6acd80cedf-m8sg91cq-29_do.png"
  - "assets/images/b3d5ca89bd70-m8sg9bz5-30_don't.png"
  - "assets/images/7a8e8bd321d1-m8sg9nzm-31_do.png"
  - "assets/images/bf9d87d70df6-m8sg9xoh-32_caution.png"
  - "assets/images/f9b0cf4c1831-m8sgbmj3-33.png"
  - "assets/images/d35a9869f73a-m8sgc676-34.png"
  - "assets/images/cb38994ebed9-m8sgj2r1-35.png"
  - "assets/images/63d995855586-m8sgjir4-36.png"
  - "assets/videos/f66f23831234-mbh0yk3u-GM3_Dialogs_Guidelines_37_IA_v01.mp4"
  - "assets/images/2cd98a20af27-m8sgqe15-38.png"
  - "assets/videos/20ce13f6c76a-mbh0z34p-GM3_Dialogs_Guidelines_39_IA_v01.mp4"
---

# Dialogs

Dialogs provide important prompts in a user flow

## Guidelines

![Basic dialog in isolation](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sf9qay-01.png?alt=media&token=5e8c7d9f-1451-4104-b5a4-c64b2e98296f>)

_A basic dialog_

## Usage

A dialog is a modal window that appears in front of app content to provide critical information or ask for a decision. Dialogs disable all app functionality when they appear, and remain on screen until confirmed, dismissed, or a required action has been taken.

Dialogs are purposefully interruptive, so they should be used sparingly. A less disruptive alternative is to use a dropdown menu, which provides options without interrupting a user’s experience.

![Diagram of basic and full-screen dialogs.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfc7pz-02.png?alt=media&token=48e473e3-7381-4089-8e91-11de31b32586>)

_There are two variants of dialogs: Basic dialog; Full-screen dialog_

![Dialog in front of app content.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfcqhr-03_do.png?alt=media&token=3655fc78-54b9-44f6-a239-d93670bb087e>)

_Use dialogs for prompts that block an app’s normal operation, and for critical information that requires a specific user task, decision, or acknowledgement_

![Low-priority dialog in front of app content.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfdc9w-03_dont.png?alt=media&token=6a548db0-3131-46af-9f44-d30b74de4904>)

_Don’t use dialogs for low- or medium-priority information. Instead use a snackbar, which can be dismissed or disappear automatically._

### Similar components

Snackbars are also designed to show important messages.

Choose the right component based on the importance of the message. This component messaging strategy helps avoid overusing dialogs.

![Snackbar on a phone saying that new photos were synced to the device. No buttons exist.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmk29xmfo-05-3P.png?alt=media&token=89e12852-9fec-44ab-82ca-d281460b0ba7>)

_Snackbars can disappear automatically_

| **Component** | **Importance** | **Action needed** |
| --- | --- | --- |
| Snackbar | Low importance | Optional: Snackbars may not have a button, and can disappear automatically |
| Dialog | High importance | Required: Dialogs block the main content until an action is confirmed |

## Anatomy

### Basic dialog

![Diagram of 7 elements of basic dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfhos4-07.png?alt=media&token=c26b3dc0-c5bd-4fe6-8081-13019a21814c>)

_Container; Icon (optional); Headline (optional); Supporting text; Divider (optional); Buttons label text; Scrim_

### Full-screen dialog

![6 elements of full-screen dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfick9-08.png?alt=media&token=7287dba3-0638-4a9f-afde-dcc94ce608e2>)

_Container; Header region; Icon (close affordance); Headline (optional); Button label text; Divider (optional)_

### Container and scrim

Dialog containers appear above other screen elements and hold the dialog’s headline, text, buttons, and list items.

To focus attention on the dialog, surfaces behind the container are scrimmed with a temporary overlay to make them less prominent.

![Basic dialog shown above a scrim overlay that reduces the prominence of the background elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfkf69-09.png?alt=media&token=76b7d3c2-c1db-4dc4-aabb-8df0851f4dd7>)

_Basic dialogs appear over a background scrim_

### Headline (optional)

A dialog’s purpose should be communicated by its headline and buttons or actionable items.

Headlines should:

-   Contain a brief, clear statement or question
-   Avoid apologies (“Sorry for the interruption”), alarm (“Warning!”), or ambiguity (“Are you sure?”)

![Dialog title asking “Use location service?”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfl8j1-10_do.png?alt=media&token=6685809e-faa9-4002-a1ca-7b6355cc0c4a>)

_This dialog title poses a specific question, concisely explains what’s involved in the request, and provides clear actions_

![Dialog title asking “Are you sure?”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sflmm0-11_don't.png?alt=media&token=2b32fc1c-10ea-4041-8719-5a0bbd515d5e>)

_Don’t use dialog titles that pose an ambiguous question_

Headlines should always be succinct. They can wrap to a second line if necessary, and be truncated.

In full-screen dialogs, long headlines or headlines of variable lengths (such as translations), can be placed in the content area instead of the app bar.

![Example full-screen dialog with truncated long headline.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfm2yt-12_Caution.png?alt=media&token=bb20aab8-9a35-4eaf-9f8c-f2fc29f20a6c>)

_Avoid placing long headlines in a full-screen dialog’s app bar (1), as the truncated text may lead to misunderstanding_

![Example full-screen dialog with short headline, and longer text in content area.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfmezr-13_do.png?alt=media&token=5244a6e2-9faa-44ec-998d-12d486666021>)

_Find ways to shorten app bar text, and place longer headlines into the content area (1) of a full-screen dialog_

### Buttons 

Dialog actions are most often represented as buttons and allow users to confirm, dismiss, or acknowledge something.

Buttons are aligned to the trailing edge of the dialog for easier interaction. The confirmation button is always closest to the edge. 

Button alignment responds automatically for right-to-left languages, where the confirmation button is aligned to the left edge.

![Dialog with the confirmation button disabled because a required radio selection is missing.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfmqqd-14_do.png?alt=media&token=12972233-4c46-4c82-9b80-86136ae125ca>)

_Disable confirming actions (1) until a choice is made. Dismissive actions are never disabled._

![Dialog with the dismissing action "Cancel" on the right of the 2 buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfn276-15_don't.png?alt=media&token=51d3809f-8761-4ad0-91d1-d3b4b9bc21dc>)

_Don’t place dismissive actions (1) to the right of confirming actions. Instead, place them to the left of confirming actions._

![Dialog with a single-action button: “OK”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfq9tm-16_do.png?alt=media&token=1ac568e8-5f77-443d-9a0f-ee24e2d5ae71>)

_A single action may be provided only if it’s an acknowledgement_

![Dialog with 2 button choices: “Cancel”, “Got it”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfqm2i-17_don't.png?alt=media&token=834d6d37-9c33-4d38-a72a-ba3fc1c84783>)

_Avoid presenting people with unclear choices. Cancel doesn't make sense here because no clear action is proposed._

Dialogs should contain a maximum of two actions.

-   If a single action is provided, it must be an acknowledgement action
-   If two actions are provided, one must be a confirming action, and the other a dismissing action

![Dialog with 2 buttons side-by-side: “Disagree”, “Agree”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfqzp1-18_do.png?alt=media&token=0c0f6350-ea79-41f6-a82b-8adcb6ef4f5d>)

_Display two text buttons next to one another_

![Dialog with 2 stacked buttons: “Turn on speed boost”, “No thanks”.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfre4i-19_caution.png?alt=media&token=e19e944e-1e50-437b-81e7-643e4dd9dd6b>)

_Stacked buttons accommodate longer button text, but take up more room. Confirming actions appear above dismissive actions._

Providing a third action, such as **Learn more**, is not recommended as it navigates the user away from the dialog, leaving the dialog task unfinished.

Rather than adding a third action, an inline expansion can display more information. If more extensive information is needed, provide it prior to entering the dialog.

![Dialog with 3 text buttons: Learn more, Disagree, Agree.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfrskh-20.png?alt=media&token=ce701d0c-c122-4fda-9244-be331795f9a5>)

_The Learn more action (1) navigates away from this dialog, potentially leaving it in an indeterminate state_

## Basic dialog

Basic dialogs interrupt users with urgent information, details, or actions. Common use cases for basic dialogs include alerts, quick selection, and confirmation.

![Example of basic dialog action request.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfumns-21.png?alt=media&token=f60188e3-24f3-4ede-a5c2-ae41ebbda58f>)

_Basic dialogs require a person to take action before it will close_

![Example of basic dialog confirmation.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfuxlx-22.png?alt=media&token=356b591f-4aa3-4889-95f5-2c579e0c699f>)

_Basic dialogs can give people the ability to provide confirmation of a choice before committing to it_

Basic dialogs most often appear as alerts or lists, but can have a variety of layouts and component combinations, including lists, date pickers, and time pickers.

![Date picker dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfzk1g-23.png?alt=media&token=5d0f7fb7-2646-4c2d-9450-71e463c72f7f>)

_Date picker dialogs allow people to tap a date, then confirm it by tapping OK_

![Time picker dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sfxxnd-24.png?alt=media&token=3b205d81-1479-4bf5-aa8c-c803aa41e7de>)

_Time picker dialogs allow people to move the clock hand and then confirm by tapping OK_

## Full-screen dialog

Full-screen dialogs fill the entire screen, containing actions that require a series of tasks to complete. One example is creating a calendar entry with the event title, date, location, and time.

Because they take up the entire screen, full-screen dialogs are the only dialogs over which other dialogs can appear.

Use a [container transform](../../styles/motion/transitions/transition-patterns.md#b67cba74-6240-4663-a423-d537b6d21187) pattern to transition a FAB into a full-screen dialog.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh08c2v-GM3_Dialogs_Guidelines_25_IA_v02.mp4?alt=media&amp;token=4d9bc267-de3b-41ba-a59a-b96aa5200dc8" title="A FAB transitioning into a full-screen dialog."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh08c2v-GM3_Dialogs_Guidelines_25_IA_v02.mp4?alt=media&token=4d9bc267-de3b-41ba-a59a-b96aa5200dc8>)

When a full-screen dialog is closed without being saved, a basic dialog appears in front of it to confirm selections should be discarded without saving changes.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh098mh-GM3_Dialogs_Guidelines_26_IA_v01.mp4?alt=media&amp;token=77730ed0-06cc-49a3-a5cd-1c2c8cf1606a" title="Closing a full-screen dialog triggers a basic dialog asking to discard the changes."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh098mh-GM3_Dialogs_Guidelines_26_IA_v01.mp4?alt=media&token=77730ed0-06cc-49a3-a5cd-1c2c8cf1606a>)

Full-screen dialogs may be used for content or tasks that meet any of these criteria:

-   Dialogs that include components which require keyboard input, such as form fields

-   When changes aren’t saved instantly

-   When components within the dialog open additional dialogs


Full-screen dialogs are for compact window sizes only, like mobile devices. For medium and expanded window sizes, use a basic dialog.

### Saving selections

To save a selection in a full-screen dialog, use **Save**.  The close icon or dismissive action, such as **Cancel** or **Back**, should close the dialog.

### Confirmation

The confirmation action should be clear about what happens next, like **Send** or **Create**. Avoid using vague terms like **Done**, **OK**, or **Close**. Only trigger an additional basic dialog if the action fails. Don’t disable the confirmation button.

![Full-screen dialog with create button as confirmation action.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sg5wkr-27_do.png?alt=media&token=8aa1b2c7-d3e9-4c8d-a171-dd8f5920102a>)

_A Create button is clear that the event will be created_

![Full-screen dialog with an additional basic dialog asking if you want to create this event.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sg67kv-28_don't.png?alt=media&token=a6a5c838-d758-4465-b05e-f29ac5eb0d3f>)

_Don’t trigger a basic dialog when the confirming action is selected_

### Dismissing

When someone dismisses a full-screen dialog, a basic dialog should appear to confirm that they want to discard the unsaved changes.

![A basic dialog with options to either keep editing or discard unsaved changes.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sg91cq-29_do.png?alt=media&token=1829089e-6440-4fcf-8d7b-5b4dd688ad92>)

_Use a basic dialog to confirm that the user wants to discard unsaved changes_

![A full-screen dialog with a Close button as the confirming action.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sg9bz5-30_don't.png?alt=media&token=0d99e0df-e3f2-4c02-9a9c-598c311f8e7c>)

_Don’t use the confirming action to dismiss the full-screen dialog_

### Error messages

Errors about the dialog fields should always appear inline where they occur. Some components like text fields have built-in error messaging, while others like checkboxes and radio buttons need error messages to be added next to the fields.

General errors such as network issues preventing saving or submitting should appear in a basic dialog when the confirming action fails.

Error messages should clearly but briefly explain the source of the error and how to fix it. Show all errors on the page at once so people can fix everything before trying again.

![A full-screen dialog with inline error messages for text fields.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sg9nzm-31_do.png?alt=media&token=80b7cdd4-6892-4661-a674-6e85c62b9122>)

_Error messages related to the fields should be displayed inline_

![A basic dialog mentioning that entries were not saved due to a connection issue.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sg9xoh-32_caution.png?alt=media&token=5d180627-2303-4b00-92e5-39d90741c8fd>)

_Errors unrelated to the fields can be displayed in a basic dialog_

### Dialog windows

Launching a full-screen dialog temporarily resets the app’s perceived elevation, allowing simple menus or dialogs to appear above the full-screen dialog. They cover the screen and don’t appear as a floating modal window.

### Navigation

Because full-screen dialogs can only be completed, dismissed, or closed, the close “X” icon button should be the only navigation option in the app bar.

## Adaptive design

Dialogs can swap variants as the window size class changes. For example, a full-screen dialog can change into a basic dialog at larger breakpoints.

![Example of full-screen dialog on left, simple dialog on right](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sgbmj3-33.png?alt=media&token=17fd8879-6435-409a-a3a9-8bd390799892>)

_Full-screen dialog on mobile; Dialog on a tablet_

### Medium window size

Basic dialogs appear in a center position by default.

Their position can be overridden to provide a more ergonomic experience.

![Basic dialog on tablet photos app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sgc676-34.png?alt=media&token=9bc72c88-69cf-49e2-a631-070890d60754>)

_Dialog custom positioned on the right side of the screen_

### Expanded window size

Dialogs on expanded window sizes, like desktop, are modal windows above a scrim. This puts the dialog at the forefront of a person's view, calling attention to the action prompted in the dialog.

![Example of desktop dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sgj2r1-35.png?alt=media&token=a9f16af1-69fe-43cc-9806-883818a87845>)

_Desktop dialogs call attention to the required action_

Basic dialogs can be custom-positioned anywhere on larger screens, respecting margins to prevent edge collision.

![Basic dialog position diagram.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sgjir4-36.png?alt=media&token=e2dd2b5a-1e2e-4051-a0da-4ec15079f2ce>)

_Custom placement area for basic dialogs that respects a 56dp margin from the edges of the screen_

## Behavior

### Appearing

Dialogs appear without warning, requiring users to stop their current task. They should be used sparingly, as not every choice or setting warrants interruption.

Dialogs use an [enter and exit](../../styles/motion/transitions/transition-patterns.md#e1c2a650-d7a4-4a6d-9025-e6b7845291ed) transition pattern to appear on screen.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh0yk3u-GM3_Dialogs_Guidelines_37_IA_v01.mp4?alt=media&amp;token=2b4cff4a-041a-4ade-9ef3-5ba676b77f4d" title="Dialog entering and exiting screen using fade transition."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh0yk3u-GM3_Dialogs_Guidelines_37_IA_v01.mp4?alt=media&token=2b4cff4a-041a-4ade-9ef3-5ba676b77f4d>)

### Position

Dialogs retain focus until dismissed or an action has been taken, such as choosing a setting. They shouldn’t be obscured by other elements or appear partially on screen, with the exception of full-screen dialogs.

![A basic dialog covering a full-screen dialog.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm8sgqe15-38.png?alt=media&token=9a9c7716-0f17-4ee5-a3c9-a6fb5114547a>)

_Dialogs shouldn’t be obscured by other elements except for full-screen dialogs_

### Scrolling

Most dialog content should avoid scrolling. Even when scrolling is required, the dialog title is pinned at the top, with buttons pinned at the bottom. This ensures selected content remains visible alongside the title and buttons, even upon scroll.

Dialogs don’t scroll with elements outside of the dialog, such as the background.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh0z34p-GM3_Dialogs_Guidelines_39_IA_v01.mp4?alt=media&amp;token=5c6d756b-f497-46fa-9ccb-973051ae5d8b" title="Example of fixed dialog title and buttons."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmbh0z34p-GM3_Dialogs_Guidelines_39_IA_v01.mp4?alt=media&token=5c6d756b-f497-46fa-9ccb-973051ae5d8b>)
