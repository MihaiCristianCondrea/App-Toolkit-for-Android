---
title: "Radio button"
source_url: "https://m3.material.io/components/radio-button/accessibility"
lastmod: "2026-02-16"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/4eccbf5fd5ad-mcelaxg0-GM3_Radio-button_Accessibility_01_IA_v01.mp4"
  - "assets/images/7f9f78f1db18-mc94qafr-02.png"
  - "assets/images/9b45af1cbfc9-mg0z51u6-03.png"
  - "assets/images/3a99c8d291c5-md3jpo7w-04.png"
  - "assets/images/bfcb6be762d0-mg0z56zm-05.png"
---

# Radio button

Radio buttons let people select one option from a set of options

## Accessibility

## Use cases

People should be able to do the following with assistive technology:

-   Navigate to a radio button

-   Select a radio button

-   Get appropriate feedback based on input type

## Interaction & style

A radio button can be either selected or unselected. Selecting one radio button deselects any others. A radio group can start with one radio button selected, or none selected.

Once a radio button is selected, the group can’t be deselected. To let people opt out of their selection, either provide a **Not applicable** or **No option** radio button, or provide a separate way to deselect all radio buttons, like **Clear selection**.

People should be able to select either the text label or the radio button to select an option.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmcelaxg0-GM3_Radio-button_Accessibility_01_IA_v01.mp4?alt=media&amp;token=73b3e621-4cdd-443f-ad2d-3348de408d8d" title="Selecting between radio buttons for a home and office address."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmcelaxg0-GM3_Radio-button_Accessibility_01_IA_v01.mp4?alt=media&token=73b3e621-4cdd-443f-ad2d-3348de408d8d>)

### Avoid applying density by default

Don't apply density to radio buttons by default. This lowers their targets below Material's recommendation of 48x48 CSS pixels. Instead, give people a way to choose a higher density, like selecting a denser layout or changing the theme.

To ensure this density setting can be easily reverted when it's active, keep all targets to change it at a minimum of 48x48 CSS pixels each.

## Initial focus

When outside the radio group, **Tab** moves focus directly to the selected radio button, or the first one if none are selected. 

**Shift+Tab** instead focuses on the last radio if none are selected. 

Use the **arrows** to navigate between options.

![The Tab key focuses on the first of 2 stacked radio buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmc94qafr-02.png?alt=media&token=638e9dfc-3ae8-41e6-932c-be168461fa20>)

_Tab brings the focus to the initially selected item or the initial radio option_

![The up and down arrow keys move through a list of stacked radio buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0z51u6-03.png?alt=media&token=3d528eb7-dbb4-4460-a556-b4660f3bc8f0>)

_Arrows move to next element in a list_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| **Tab** | Moves focus into the group to the selected radio button, or the first if none are selected |
| **Shift** + **Tab** | Moves focus into the group to the selected radio button, or the last if none are selected |
| **Arrows** | Moves focus and selects the previous or next radio button. Wraps focus and selection between the first and last radio buttons. |
| **Space** | Selects a focused radio button. If already selected, does nothing. |

## Labeling elements

If the UI text is correctly linked to the radio button, assistive tech such as a screenreader will read the UI text, followed by the component’s role.

The accessibility label for a group of radio buttons is typically the same as its title. The role is **Radio group**.

![Accessibility tags for a radio button group.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmd3jpo7w-04.png?alt=media&token=1f68a095-10b8-4914-9273-7f58c99a7f5e>)

_Label the radio group based on the category title_

The accessibility label for an individual radio button is typically the same as its adjacent text label.

![Accessibility tags for a radio button.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0z56zm-05.png?alt=media&token=eff0ba7d-34fe-434b-b962-755ddac009ad>)

_Label the radio button based on its label text_
