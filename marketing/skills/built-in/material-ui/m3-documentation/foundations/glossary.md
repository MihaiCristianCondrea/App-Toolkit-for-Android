---
title: "Material A-Z"
source_url: "https://m3.material.io/foundations/glossary"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets: []
---

# Material A-Z

Get to know the key terms and concepts behind Material, from A to Z

### Adaptive design

A design approach in which the interface changes based on known user, device, or environmental conditions. Adaptive design in Material includes layout and component adaptations.

### App bar

A component that displays information and actions at the top of a screen. [Learn more about app bars](../components/app-bars/overview.md)

### Banner

A component displaying a prominent message and related optional actions.

### Button

A component helping people initiate actions such as sending an email, sharing a document, or liking a post. [Learn more about buttons](../components/all-buttons.md)

### Bottom sheet

A component containing supplementary content that’s anchored to the bottom of the screen. [Learn more about bottom sheets](../components/bottom-sheets/overview.md)

### Card

A component containing content and actions that relate information about a subject. [Learn more about cards](https://m3.material.io/m3/pages/cards/overview/)

### Checkbox

A component allowing users to select one or more items from a set. Checkboxes can turn an option on or off. [Learn more about checkboxes](../components/checkbox/overview.md)

### Chip

A component for helping people enter information, make selections, filter content, or trigger actions. Chips can show multiple interactive elements together in the same area, such as a list of selectable movie times, or a series of email contacts. [Learn more about chips](https://m3.material.io/m3/pages/chips/overview/)

### Color: Baseline scheme

The **baseline scheme** refers to the group of selected tones that make up the default colors values used for light and dark themes.

### Color: Dynamic color

A customization feature in which a user-generated color scheme is mapped to an app’s color scheme.

Dynamic color is reflected in apps that accept user-generated schemes as inputs for color roles in an app. Dynamic color isn’t simply the output of the Material Theme Builder, or an algorithmic color scheme, but is the presence of a changeable color role that renders an app’s appearance dynamic in response to user input.

### Color: Extended color 

A color specified (in addition to key colors) in order to fill color roles for custom schemes. Extended color is a category for idiosyncratic color roles and applications, such as brand expression or conventional (semantic) meanings.

### Color: Key color

A key color is not an extracted color–it’s a derivation of the source color. Key colors are a concept useful to understanding dynamic color, but are not used in code. The term describes any color that undergoes hue and chroma transformation of a source color. A key color is the basis for a tonal palette.

### Color: Scheme

Any mapping of color roles to specific tones from a tonal palette. Schemes consist of multiple color roles. A **dark scheme** informally describes a group of colors using the tones from a group of palettes that are mapped for a dark theme.

Note: A **light scheme** is not the same as a **light theme.** _See theme._

### Color: Source color

The single color that’s extracted to define all five key colors is called a source color. While it may not be needed in code, it’s a useful distinction for understanding that a dynamic color scheme has its root in one initiating color, AKA hue, chroma, and tone. **Key colors** are defined in relationship to the **source color’s** _hue_.

### Color: Tonal palette

A tonal palette comprises a 13-tone range that serves as the basis for mapping tones to specific roles. A tonal palette offers variations in tonality so that a color scheme automatically provides contrast and visual differentiation within any color group, such as primary.

### Color: Tone

A grouping of tones are colors with the same hue and chroma. Informally, tone means degrees of lightness. 

**In code**: Tone.of(hex code).get(tone) or Tone.of(hue, chroma).get(tone)

**In design guidance**: “A change in a color’s tone can be used to distinguish interaction states or visual interest to component elements.”

### Color: User-generated schemes

An aspect of dynamic color that describes the colors derived and applied from an individual user’s wallpaper selection or Android preset colors.

### Condition 

A signal that determines when and how an adaptive layout or component should adapt.

### Contrast 

Difference between colors. For accessibility, contrast refers strictly to the difference in tone. A difference of 40 in tone guarantees a [WCAG](https://www.w3.org/WAI/standards-guidelines/wcag/) contrast ratio ≥ 3.0; a difference of 50 in tone guarantees a contrast ratio ≥ 4.5.

### Customization

A modification made to a UI reflecting an app, OEM, or individual user's visual preferences and brand. Customization can focus on individual UI elements (for example, styling the color of a component or adding optional actions to a template), or it can be implemented globally (for example, applying a set of styles as a theme across the UI).

### Dark theme

A dark theme is a low-light UI that displays mostly dark surfaces.

### Data table

A component displaying sets of data across rows and columns.

### Date picker

A component that lets users select a date, or a range of dates. [Learn more about date pickers](../components/date-pickers/overview.md)

### Design attribute

The style aspect that a design token or hard-coded value applies to, such as color or font.

### Design guidelines

Guidelines are descriptive written and illustrated docs that demonstrate usage and behavior mainly through examples. They're the long-form discussion of specs that help designers and developers with problem-solving and decision-making.

### Design specs

Specs are annotated designs and docs that specify the values and parameters that define a component or a feature’s coded capabilities.

### Design System Package (DSP) 

An open-format folder structure created by Adobe to help teams share design system information across tools. [Learn more about the DSP format](https://github.com/AdobeXD/design-system-package-dsp)

### Design tokens

A design token represents a small, reusable design decision that’s part of a design system's visual style. Tokens replace static values with self-explanatory names. [Learn more on the design tokens page](design-tokens/overview.md)

### Design tokens: Context

The set of conditions in which tokens can point to non-default values (for example, **dark theme, dense layout**).

### Design tokens: Role

A shortened version of the system token name (for example, **Secondary container color**; **Headline 1**).

### Design tokens: Types

-   **Reference tokens**: All available tokenized values. (for example, **md.ref.palette.secondary200**)
-   **Component tokens**: Component tokens define the design attributes of elements in a component, such as the color of a button container
-   **System tokens**: The choices or roles that make the system, from color to typography, elevation, and shape. (for example, **md.sys.color.secondary-container**)

### Design tokens: Value

The information defining a design attribute, either stored in a token or hard-coded.

### Dialog

A component providing important prompts in a user flow. They can require an action, communicate information, or help users accomplish a task. [Learn more about dialogs](https://m3.material.io/m3/pages/dialogs/overview/)

### Divider

A thin line that groups content in lists and layouts. [Learn more about dividers](../components/divider/overview.md)

### Element 

The part of the component that a token or value applies to, such as the container or label text.

### Extended FAB

A component for helping people take primary actions. They're wider than FABs to accommodate a text label and larger target area. [Learn more about extended FABs](https://m3.material.io/m3/pages/extended-fab/overview/)

### Floating action button (FAB)

A component representing the most important action on a screen. They put key actions within reach. [Learn more about FABs](https://m3.material.io/m3/pages/fab/overview/)

### HCT

HCT is an abbreviation of hue, chroma, tone. It’s the name of the color space that enables dynamic color. HCT is based on CAM16 **hue** and **chroma**; the L\* construct for luminance from L\*a\*b\* (CIELAB, 1976) is denoted as T for **tone**.

### Image list

A component displaying a collection of images in an organized grid.

### Libraries

Developer libraries supported by Material 3 include Android and Jetpack Compose, with in-progress support being developed for Flutter and Web.

### List

A component comprised of continuous, vertical indexes of text or images. [Learn more about lists](../components/lists/overview.md)

### Material Components

Open-source UI elements created to help developers implement Material Design across Android, Flutter, and the web.

### Material Design

Material is an adaptable system of guidelines, components, and tools that support the best practices of user interface design. Backed by open-source code, Material streamlines collaboration between designers and developers, and helps teams quickly build beautiful products. 

Material was launched in 2014 and has evolved over the years.

**Material Design 1** (M1): The first generation of Material Design, launched in 2014, is archived and accessible at: material.io/archive/guidelines.

**Material Design 2** (M2): The second generation of Material Design, launched in 2018, introduced Material Theming and code for Material Components.

**Material Design 3** (M3): The third generation of Material, launched in 2021, includes the Material You features like dynamic color. 

-   **Material You**: The new Material visual style and set of features embracing the personal and expressive needs of individual users, part of Material Design 3

### Material Theming

The ability to systematically customize Material Design to better reflect your product’s brand.

While a scheme represents selections or a subset of a single style group, **theme** describes a set of multiple styles and attributes in combination. Themes adjust global styles to adjust for a given user context or preference, such as low light or high contrast. For example, **dark theme** describes design decisions beyond color since adjustments to elevation and state are also integral to expressing a dark UI effectively.

### Menu

A component that displays a list of choices on a temporary surface. [Learn more about menus](../components/menus/overview.md)

### Mode

Binary setting provided by a system to help people use the device better. Examples: **Focus mode**, **airplane mode**, and **battery saver mode**.

### Navigation bar

A component offering a persistent and convenient way to switch between primary destinations in an app. [Learn more about navigation bars](https://m3.material.io/m3/pages/navigation-bar/overview/)

### Navigation drawer

A component providing ergonomic access to destinations in an app. [Learn more about navigation drawers](https://m3.material.io/m3/pages/navigation-drawer/)

### Navigation rail

A component providing access to primary destinations in apps when using tablet and desktop screens. [Learn more about navigation rails](https://m3.material.io/m3/pages/navigation-rail/overview/)

### Progress indicator

A component displaying an unspecified wait time or the length of a process. [Learn more about progress indicators](../components/progress-indicators/overview.md)

### Orbiter

Floating UI elements that control the content within spatial panels.

### Pane

The building blocks of a layout. Content and actions are grouped into panes, which adapt the content to best fit the screen.

### Radio button

A component allowing users to select one option from a set. [Learn more about radio buttons](../components/radio-button/overview.md)

### Role

Short nickname describing the purpose of a design token within a design system. Also known as slots. Examples: **On surface**; **Body 1**.

### Side sheet

A component containing supplementary content that’s anchored to the left or right edge of the screen.

### Slider

A component that allows users to make selections from a range of values.

### Snackbar

A component that provides brief messages about app processes at the bottom of the screen. [Learn more about snackbars](https://m3.material.io/m3/pages/snackbar/overview/)

### Spatial

Relates to the placement of UI in space through extended reality (XR).

### Style

One or more properties, typically customizable, that define an aspect of a UI's appearance, such as [color](../styles/color/system/how-the-system-works.md), [typography](../styles/typography/overview.md), or shape.

### Switch

A component that toggles the state of a single item on or off. [Learn more about switches](../components/switch/overview.md)

### Tab

A component used to organize content across different screens, data sets, and other interactions. [Learn more about tabs](../components/tabs/overview.md)

### Text field

A component that lets users enter and edit text. [Learn more about text fields](../components/text-fields/overview.md)

### Theme

A set of styles, such as color, elevation, and type, that can be applied globally to an app's UI components as a way to consistently adjust aspects of appearance in an app.

### Time picker

A component that helps users select and set a specific time. [Learn more about time pickers](../components/time-pickers/overview.md)

### Toolbar

A component displaying navigation and key actions at the bottom of mobile screens. [Learn more about toolbar](../components/toolbars/overview.md)

### Tooltip

A component that displays informative text when users hover over, focus on, or tap an element.

### XR

Extended reality includes UI viewed in virtual reality or in passthrough blended with the physical environment.
