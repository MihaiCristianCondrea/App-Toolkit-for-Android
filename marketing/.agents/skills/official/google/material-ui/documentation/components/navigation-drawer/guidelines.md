---
title: "Navigation drawer"
source_url: "https://m3.material.io/components/navigation-drawer/guidelines"
lastmod: "2026-05-18"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/33f4035768b1-lwopun3e-1.png"
  - "assets/images/2af25d0f64f9-lwopv1qw-2.png"
  - "assets/images/ac4f5a5de583-lwopvjco-3.png"
  - "assets/images/c43eac014795-lwopw575-4.png"
  - "assets/images/d3a5355caf66-lwopwhen-5.png"
  - "assets/images/5a5b0a9d639f-lwopx4a0-6.png"
  - "assets/images/d1e45b2bb0f1-lwopxvvj-7.png"
  - "assets/images/5f4a88ff3a85-lwopy7b1-8.png"
  - "assets/images/1cec3bea4376-lwopyjta-9.png"
  - "assets/images/41b8c40fb315-lwopzqbr-10.png"
  - "assets/images/70dfc5e1ff72-lwoq08ti-11.png"
  - "assets/images/8848c460c4ed-lwoq1xf0-12.png"
  - "assets/images/ab15380a717a-lwoq29df-13.png"
  - "assets/images/08f54c3dbd2c-lwoq2qbs-14.png"
  - "assets/images/81a7a70d7891-lwoq358i-15.png"
  - "assets/images/59c372d97520-lwoq3i6v-16.png"
  - "assets/images/43274b91b059-lwoq4gjj-17.png"
  - "assets/images/4da30d3f1f4f-lwoq4tkk-18.png"
  - "assets/images/6548e457211f-lwoq56gb-19.png"
  - "assets/images/91c68d906fca-lwoq67nf-20.png"
  - "assets/images/15ac240a88e9-lwoq6mto-21.png"
  - "assets/images/6b69285af8dc-lwoq710s-22.png"
  - "assets/images/94cd9f6d0e39-lwoq7esu-23.png"
  - "assets/images/899fc428ab99-lwoq7wck-24.png"
  - "assets/images/c4c99efbba92-lwoq8evf-25.png"
  - "assets/videos/c6b65b4baa12-lwoqh0d1-GM3-Components-NavDrawer-Guidelines-1-v01.mp4"
  - "assets/images/ba16f3e06197-lx54v3bi-27.png"
  - "assets/videos/07f895d41cf1-lwoqihhy-GM3-Components-NavDrawer-Guidelines-2-v01.mp4"
  - "assets/images/5a42728e6615-lwoqb1nm-27.png"
  - "assets/images/654f6ef8f1b7-lwoqf0gp-28.png"
  - "assets/videos/69f587ce7aa8-lwoqk3j4-GM3-Components-NavDrawer-Guidelines-3-v01.mp4"
---

# Navigation drawer

Navigation drawers let people switch between UI views on larger devices

## Guidelines

star

Note:

The navigation drawer is no longer recommended in the Material 3 Expressive update. For those who have updated, use an [expanded navigation rail](https://m3.material.io/m3/pages/navigation-rail/overview/), which has mostly the same functionality of the navigation drawer and adapts better across window size classes.

![Navigation drawer with 4 primary destinations ](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopun3e-1.png?alt=media&token=220f8c9f-c032-4185-9a2c-4ac8950d763f>)

## Usage

Navigation drawers provide access to destinations and app functionality, such as switching accounts. They can either be permanently on-screen or opened and closed by a navigation menu icon. One navigation destination is always active.

Navigation drawers are recommended for:

-   Apps with 5 or more top-level destinations
-   Apps with 2 or more levels of navigation hierarchy
-   Quick navigation between unrelated destinations
-   Replacing the navigation rail or navigation bar on large screens

![Navigation drawer with multiple destinations in a mail app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopv1qw-2.png?alt=media&token=a8455b6f-5918-42d9-aa1c-04d158c8eba1>)

_Use a navigation drawer for 5 or more primary destinations, or more than 1 level of navigation hierarchy_

Avoid using a navigation drawer with other primary navigation components, such as a navigation bar.

Instead, choose a single navigation component based on product requirements, breakpoints, and window size class:

-   Navigation bars for compact window sizes

-   Navigation rails for medium and expanded window sizes 

-   Standard navigation drawers for expanded, large and extra-large window sizes

![Standard navigation drawer and navigation bar used together.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopvjco-3.png?alt=media&token=9df37deb-e7d3-4dea-88d1-b5b6a6040cab>)

_Avoid using two navigation components on the same screen_

There are two variants of navigation drawers:

1.  Standard navigation drawer

2.  Modal navigation drawer

![Standard navigation drawer with destinations in mail app.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopw575-4.png?alt=media&token=0970e3a0-0963-4060-9aae-22d9e3689652>)

_Standard navigation drawer_

![Modal navigation drawer with destinations and scrim.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopwhen-5.png?alt=media&token=81d4f11e-1cba-4620-ba30-78d3cc70ab55>)

_Modal navigation drawer_

### Standard navigation drawer

Standard navigation drawers provide access to drawer destinations and app content for layouts in expanded, large, and extra-large window sizes. 

Standard drawers can be permanently visible (best for frequently switching destinations) or opened and closed by tapping a menu icon (best for focusing more on screen content).

In medium and compact window sizes, use modal drawers instead.

![Standard navigation drawer in a mail app with active destination “Inbox” next to app content.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopx4a0-6.png?alt=media&token=439663a8-dc1f-442d-a29a-54430caeb836>)

_Standard navigation drawer providing access to drawer destinations next to app content_

### Modal navigation drawer

Modal navigation drawers use a scrim to block interaction with the rest of an app’s content, and don’t affect the screen’s layout grid.

Modal navigation drawers can be used in any window size, but are primarily used in compact and medium sizes where space is limited or prioritized for app content.

They can be swapped with standard drawers on expanded, large, and extra-large window sizes.

![Modal navigation drawer with 1 active destination and scrim.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopxvvj-7.png?alt=media&token=5d45f347-e521-48af-9ae9-d4c05965f42f>)

_Modal navigation drawer using a scrim to block interaction with the rest of an app’s content_

Modal navigation drawers are always opened by an action outside of the drawer, such as clicking a navigation menu icon in a navigation rail.

Modal drawers can be dismissed by:

-   Selecting a drawer item
-   Tapping the scrim
-   Swiping toward the drawer’s anchoring edge (for example, swiping right-to-left for a left-aligned navigation drawer)

![Diagram noting a navigation menu icon in a navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopy7b1-8.png?alt=media&token=5ea39e51-4a7b-4e9b-ad02-2fef81e461cd>)

_A modal drawer opened by an action such as clicking a navigation menu icon (1)_

Modal drawers can be dismissed by tapping the scrim or swiping the drawer toward its anchoring screen edge.

![2 modal navigations illustrating tapping the scrim or swiping to dismiss a modal drawer](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopyjta-9.png?alt=media&token=3090bcc0-a205-4695-b11b-931243b71061>)

_1. Dismiss by tapping the scrim / 2. Dismiss by swiping the drawer_

## Anatomy

Navigation drawers are essentially a list contained within a side sheet. They can also include headers, subheads, and dividers to organize longer lists.

![Navigation drawer diagram numbering 8 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwopzqbr-10.png?alt=media&token=4325d34c-753c-4cfa-8b60-fbf6aaa56358>)

_Navigation drawers can include headers, subheads, and dividers to organize longer lists Active Indicator; Icon; Label; Badge label; Sheet; Divider; Section label (optional); Scrim_

### Sheet

A sheet holds all navigation drawer elements. Side sheets are used as the container for standard and modal navigation drawers.

Navigation drawers that open from the side are always placed on the start edge of the screen, on the left for left-to-right (LTR) languages, and on the right for right-to-left (RTL) languages.

![Modal navigation drawer opening from left side of screen.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq08ti-11.png?alt=media&token=fa4df5c4-43a7-4979-83dd-4a50c3c28454>)

_A navigation drawer opens from the left side of the screen for left-to-right languages_

### Divider (optional)

Dividers can be used to separate groups of destinations within the navigation drawer.

![Navigation drawer using horizontal dividers to separate a group of destinations](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq1xf0-12.png?alt=media&token=1d041701-d74b-4495-a337-c858a9bd81b5>)

_Use full-width dividers (1) to separate groups of destinations_

![Navigation drawer using horizontal dividers to separate individual destinations](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq29df-13.png?alt=media&token=612b4caa-99c1-4c3b-a835-2c1ed0bda36a>)

_Don’t use dividers to separate individual destinations_

### Active indicator

The active indicator is a background shape communicating which destination of the navigation drawer is currently being displayed.

![Navigation drawer diagram numbering 1 element.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq2qbs-14.png?alt=media&token=cefe950a-7624-4c32-a31d-3b596d729417>)

_The active indicator (1) is a background shape communicating which destination of the navigation drawer is currently being displayed_

### Label text and icons

Destinations in a navigation drawer take the form of actionable list items. Each item describes its destination using label text and an optional icon.

![Navigation drawer diagram numbering 2 elements.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq358i-15.png?alt=media&token=ad709e8b-f932-4c40-a63c-cf893ff65b71>)

_Actionable list items in a navigation drawer describe each destination using (1) an optional icon and (2) required label text_

Label text should be clear and short enough that it isn’t cut off by the sheet.

![Navigation drawer using only label text for 4 destinations. Label text “Inbox” in active destination.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq3i6v-16.png?alt=media&token=e855eb07-211a-44ba-a56f-9414dce6237b>)

_Navigation drawers can use text labels without icons_

![Navigation drawer with 1 truncated text label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq4gjj-17.png?alt=media&token=44f71647-6325-43c9-b93e-6a3bc059ae87>)

_Keep text labels concise, but truncate them if they extend beyond the container width_

![Navigation drawer with 1 text label with wrapped label text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq4tkk-18.png?alt=media&token=c152a72c-b231-4f97-81aa-1b14d827c8b9>)

_Don’t wrap label text_

![Navigation drawer with 1 text label featuring smaller text.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq56gb-19.png?alt=media&token=218c541f-0e68-40b4-bf8c-59983f554512>)

_Don’t shrink text size in order to fit a text label on a single line_

Icons can supplement labels as indicators of a destination. When used, they should always be placed before text. Other app components and content should reference these icons.

![Navigation drawer with active destination “Inbox” featuring recognizable icon.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq67nf-20.png?alt=media&token=f5cdaabd-b70e-495e-9d1e-5cce1e0bb81d>)

_Use recognizable icons when conventions exist_

![Navigation drawer with 4 destinations, 2 with text label and icon, 2 with only text label.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq6mto-21.png?alt=media&token=70a07030-1191-4f84-9e14-19c188d0452b>)

_Don’t apply icons to some destinations and not others. Icons should be used for all destinations, or none._

### Section label (optional)

Short subhead section labels can help group related destinations in the navigation drawer.

![Navigation drawer showing subhead section labels.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq710s-22.png?alt=media&token=f9fc1eb9-1347-4515-bf7f-461d9100c605>)

_Related destinations can be grouped using short subhead section labels in the navigation drawer_

### Scrim (modal only)

Modal navigation drawers use a scrim to block interaction with the rest of the app. The scrim is placed directly behind the drawer’s sheet and can be tapped or clicked to dismiss the drawer.

![Modal navigation drawer with scrim placed behind.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq7esu-23.png?alt=media&token=41cf34a6-52f4-48ad-8408-16f71ce0b744>)

_Scrim applied behind a modal navigation drawer_

## Responsive layout

A product’s navigation component should change to suit the window size class and form factor of the screen.

Modal navigation drawers can be used at any window size but are most common in compact and medium window sizes.

Standard navigation drawers are best for expanded, large, and extra-large window sizes. 

Use a transition when swapping components. For example, when switching from a portrait to landscape layout, the navigation rail should transform into a navigation drawer.

![Navigation rail changing to navigation. drawer on a larger screen](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq7wck-24.png?alt=media&token=2e030b27-4348-4860-aac6-78e423e11c79>)

_Standard navigation drawers change size to suit the device’s screen_

### Compact window size

Use modal navigation drawers in compact window sizes. Or swap the drawer for a navigation bar.

On web, when the screen size is smaller than 320 CSS pixels, swap the navigation drawer for a navigation bar to ensure accessibility.

![Modal navigation drawer with 1 active destination.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoq8evf-25.png?alt=media&token=ef3a29d1-8e04-4b51-9774-8c7f118fbad4>)

_Use a modal navigation drawer on mobile screens_

### Medium & expanded window sizes

Use a modal navigation drawer alone or with a navigation rail on medium and expanded window sizes.

When a navigation rail and modal navigation drawer are used together, the drawer can repeat destinations in the navigation rail as long as the drawer offers enough visual separation between levels of the navigation hierarchy.

A standard navigation drawer can be used in [single pane layouts](https://m3.material.io/m3/pages/understanding-layout/parts-of-layout) in expanded window sizes.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqh0d1-GM3-Components-NavDrawer-Guidelines-1-v01.mp4?alt=media&amp;token=dbffbe2f-e8e7-4434-b4ab-c0d41e2068ad" title="Navigation drawer on tablet with 1 active destination."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqh0d1-GM3-Components-NavDrawer-Guidelines-1-v01.mp4?alt=media&token=dbffbe2f-e8e7-4434-b4ab-c0d41e2068ad>)

### Large and extra-large window sizes

For web experiences on laptop and desktop devices, use either a standard navigation drawer, or a navigation rail that transitions into a modal navigation drawer.

![Navigation drawer showing 1 active destination.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flx54v3bi-27.png?alt=media&token=444359b1-1e21-4971-b70d-bc624d900b19>)

_Use a standard navigation drawer on large and desktop screens_

## Behavior

### Scrolling

Navigation drawers can be vertically scrolled, independent of the rest of the screen’s content and UI. If the list of navigation destinations is longer than the height of the drawer, the drawer’s contents can be scrolled within the drawer.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqihhy-GM3-Components-NavDrawer-Guidelines-2-v01.mp4?alt=media&amp;token=f5dbf149-2261-4a0f-988d-d72cc2c6a093" title="A navigation drawer on the left region of an email app is scrolled vertically without disturbing the rest of the content."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqihhy-GM3-Components-NavDrawer-Guidelines-2-v01.mp4?alt=media&token=f5dbf149-2261-4a0f-988d-d72cc2c6a093>)

### Visibility

**Dismissible standard drawers** can be used for layouts that prioritize content (such as a photo gallery) or for apps where users are unlikely to switch destinations often. They should use a visible navigation menu icon to open and close the drawer.

![Side-by-side standard navigation drawer opened and then closed after tapping menu bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqb1nm-27.png?alt=media&token=ed09af6e-928f-41f7-a7ca-fcf766ea4050>)

_A standard dismissible navigation drawer is opened and closed by tapping the navigation menu icon in the app bar (1), and remains open until the menu icon is tapped again (2)_

**Permanently visible standard drawers** allow quick navigation between unrelated destinations. They can’t be closed or dismissed by the user.

![Standard navigation drawer moving between destinations.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqf0gp-28.png?alt=media&token=627872b2-dc97-4b2c-8234-2339a7730b18>)

_A permanently-visible standard navigation drawer on desktop_

### Appearing

When a navigation drawer animates on screen, it uses an [enter and exit](../../styles/motion/transitions/applying-transitions.md) transition pattern.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqk3j4-GM3-Components-NavDrawer-Guidelines-3-v01.mp4?alt=media&amp;token=0f92a1ef-5990-4cfc-a8c0-932477d20478" title="In an email app, navigation drawer opens on menu icon click, and closes on outside click."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flwoqk3j4-GM3-Components-NavDrawer-Guidelines-3-v01.mp4?alt=media&token=0f92a1ef-5990-4cfc-a8c0-932477d20478>)
