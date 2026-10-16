---
title: "Text fields"
source_url: "https://m3.material.io/components/text-fields/accessibility"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/2a1cbc7df27d-lx32f0bp-1.png"
  - "assets/images/76a9216b0a66-lx32f68l-2.png"
  - "assets/images/9280798ebe72-lx32k7wf-3.png"
  - "assets/images/1fa18834de38-lx32kef6-4.png"
  - "assets/images/d7832e08b0f6-lx32ks8b-5.png"
  - "assets/images/50d9b17f6521-lx32l5y8-6.png"
  - "assets/images/fc8ad7cfa039-lx32leg0-7.png"
  - "assets/images/0d54ecfc61ca-lx32lojq-8.png"
  - "assets/images/be83b2a6dc8b-lx32m4pa-9.png"
  - "assets/images/5044f9c5cfa9-lx32meu7-10.png"
  - "assets/images/7da5afd51a38-lx32mlzg-11.png"
---

# Text fields

Text fields let users enter text into a UI

## Accessibility

## Use cases

User should be able to:

-   Navigate to and activate a text field with assistive technology
-   Input information into the text field
-   Receive and understand supporting text and error messages
-   Navigate to and select interactive icons

## Interaction & style

The containers for both filled and outlined text fields provide the same functionality. Changes to color and thickness of stroke help provide clear visual cues for interaction.

![Filled text field in enabled (empty) state and in focused (populated state) have visual cues to identify their state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32f0bp-1.png?alt=media&token=2037cfd2-09f9-48c6-a723-7a73f2dd6d36>)

_Filled text fields_

![Outlined text field in enabled (empty) state and in focused (populated state) have visual cues to identify their state.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32f68l-2.png?alt=media&token=aa76ef86-da54-4f76-8437-26c0181d85c2>)

_Outlined text fields_

Containers improve the discoverability of text fields by creating contrast between the text field and surrounding content. 

In some contexts, outlined text fields can improve the perception of the fields with a 3:1 or greater contrast ratio between the container outline and the background.

![An outlined text field with label text that passes the minimum contrast of 3:1.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32k7wf-3.png?alt=media&token=679bc72b-1edd-4a37-9f0c-462d91c195a6>)

_Make sure the container outline has a minimum contrast of 3:1 to the background / /_

![An outlined text field with label text fails the minimum 3:1 contrast.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32kef6-4.png?alt=media&token=9677297e-c97b-4210-9a25-2c9268b02806>)

_Don't choose colors that won't pass Material's minimum contrast of 3:1_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| Tab | Focus lands on (non-disabled) text field |

## Labeling elements

If the UI text is correctly linked, assistive tech (such as a screenreader) will read the UI text followed by the component’s role.

The accessibility label for a text field is the same as the text field label.

![The text field  and accessibility label both read “Email.” The role is “textbox.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32ks8b-5.png?alt=media&token=12c43d38-d18f-417c-b8fc-13110c145ffa>)

_A text field’s label should include its UI text_

For text fields with interactive trailing icons, the accessibility label clarifies its function.

For example, when a password is hidden, the label for the view icon is "Show password," and when the password is visible, the label is "Hide password."

When an icon has no actionable role, like an error icon, the label is "Error."

![The trailing icon’s accessibility label “Show Password.” The role is “Button.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32l5y8-6.png?alt=media&token=b4271bd0-47bf-4af4-b9f7-fb693bf843be>)

_When a trailing icon in the field acts as a button, the label should clarify function, while the role explains the component type_

The prefix and suffix of a text field provides symbols and abbreviations to help users enter the correct values. 
 
The accessibility label for prefix and suffix needs to have a unique id attribute, for example, the currency name for a currency symbol prefix.

![Text field accessibility labels “UI text” are “Euro” for a currency prefix and “At gmail dot com” for the email address suffix.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32leg0-7.png?alt=media&token=9d09f464-5f11-40c9-8310-af201dd18e29>)

_A form containing fields with both a prefix for currency, and a suffix for email address_

When there is an error, "alert" is applied to the role and the error message to the text label.

If a text field displays both supporting text and error text, the label should include the supporting text first, followed by the error text.

![The text field accessibility labels is: UI text “Not a valid ZIP code.” The role is “Alert.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32lojq-8.png?alt=media&token=a55926cc-d2d2-4451-8fe2-74a515db3a16>)

_Text field error messages should be given an “alert” role in accessibility labels_

The accessibility label for the character counter clarifies the number of characters that can be entered into the text field.

![A character counter's accessibility label  reads: UI text (“Character count, 5/20”)](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32m4pa-9.png?alt=media&token=aac93da1-54a2-4b87-a1bc-68807f59bbd4>)

_The remaining character counter should be called “character count” within the label_

The text displayed in the supporting text is also used for its accessibility label.

![The accessibility label uses the supporting text. It reads: UI text (“Please use the company email address”). Role [No role].](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32meu7-10.png?alt=media&token=4caf165e-0a75-4e05-8be8-410f681c8874>)

_Text field supporting text should have its own accessibility label_

If a text field requires input, indicate so with an asterisk at the end of the text field label. The accessibility label must include the asterisk.

![Accessibility label reads: UI text (“Username*”).  The role is “Textbox.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx32mlzg-11.png?alt=media&token=72a43825-5286-49d6-9f49-7bb37a11e0f4>)

_A required text field’s accessibility label should include any supporting text_
