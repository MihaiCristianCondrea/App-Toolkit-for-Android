---
title: "Switch"
source_url: "https://m3.material.io/components/switch/accessibility"
lastmod: "2026-01-06"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/d748c57ec7ba-mg68ycaw-01.png"
  - "assets/images/2020f5b5c9ce-mg68z1uk-02.png"
  - "assets/images/502073a7eff6-mg692flh-03.png"
  - "assets/images/6a614e175e52-mg692zv4-04.png"
  - "assets/images/6f4d17ec98aa-mg695w5m-05.png"
  - "assets/images/412c304a2ad5-mg698bn2-06.png"
---

# Switch

Switches toggle the selection of an item on or off

## Accessibility

## Use cases

People should be able to do the following with assistive technology:

-   Navigate to a switch with a keyboard or switch input

-   Toggle the switch on and off

-   Get appropriate feedback based on input type documented under [Interaction & style](accessibility.md#c0e9fae1-48df-428b-b028-4f7be071ada3)

## Interaction & style

The switch handle increases in size to indicate interactivity for both touch and cursor control interactions.

**Touch**
When tapped or dragged, the handle size grows, providing interaction feedback.

**Cursor**
When hovered (in both on and off states), the hover area grows, providing a visual cue that the handle is interactive. When clicked, the handle size grows.

![The switch handle increases in size when tapped and dragged.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg68ycaw-01.png?alt=media&token=9c11889d-7f24-4d5a-bb90-9b469937fc21>)

_Touch: Tap, Drag_

![The cursor changes from an arrow to a hand pointer when hovering over and clicking the switch.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg68z1uk-02.png?alt=media&token=bc75a5a2-35c9-4f57-aea5-6b74a1befbed>)

_Cursor: Hover, Click_

### Avoid applying density by default

Don't apply density to switches by default — this lowers their targets below our best practice of 48x48 CSS pixels. Instead, give people a way to choose a higher density, like selecting a denser layout or changing the theme.

To ensure that this density setting can easily be reverted when it's active, keep all targets to change it at a minimum 48x48 CSS pixels each.

## Initial focus

Initial focus lands directly on the switch’s handle, since it’s the primary interactive element of the component.

![The focus is on the switch handle, which is toggled on.
](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg692flh-03.png?alt=media&token=ab8f62a5-12ae-463c-9d74-a96cdbdb270c>)

_Focus lands on the switch handle_

![Space or Enter is used to toggle the switch off.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg692zv4-04.png?alt=media&token=e69e20fa-da0a-4bbe-814a-1bbfbf3d0973>)

_The switch is toggled using Space or Enter_

## Keyboard navigation

| Keys | Actions |
| --- | --- |
| **Tab** | Focus lands on the switch handle |
| **Space** or **Enter** | Toggles the handle on and off |

## Labeling elements

The accessibility label for a switch uses the adjacent label text if implemented correctly. 

Assistive tech such as a screen reader will read the UI text followed by the component’s role.

![“Dark theme” is the switch’s adjacent label text and the accessibility label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg695w5m-05.png?alt=media&token=dda98de5-3d58-48c6-bf03-15d563c4e742>)

_A switch’s accessibility label can incorporate its adjacent UI text_

When the visible UI text is ambiguous, accessibility labels need to be more descriptive. For example, a switch visibly labelled **Photo album** would benefit from additional information to clarify the switch’s function.

Consider making the adjacent label text more descriptive when possible. This reduces the need for different accessibility text.

![The accessibility label for the switch is “Photo album access” though the label text is “photo album.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmg698bn2-06.png?alt=media&token=5ef0f417-dae7-4548-8214-6003ed547f91>)

_While the visible label text reads Photo album, the accessibility label for this switch clarifies its function: Photo album access_
