# Material 3 Expressive

M3 Expressive is an evolution of Material 3, not a new version. M3 is not deprecated and this is not
"M4". It adds features, updated components, and design tactics for emotionally engaging UX.

Official pages: `documentation/blog/building-with-m3-expressive.md`,
`documentation/foundations/usability/applying-m3-expressive.md`,
`documentation/foundations/usability/overview.md`. Images:
`grep -i "^building-with-m3-expressive" visuals/INDEX.tsv`.

## The research

Material calls M3 Expressive its most researched update since the system launched in 2014: 46 studies
with more than 18,000 participants. The published takeaways:

- People of all ages preferred expressive designs.
- Expressive designs scored higher on playfulness, energy, creativity, and friendliness.
- Users were more likely to switch to products using M3 Expressive components and techniques.
- Participants spotted key UI elements up to four times faster in expressive screens.

The last finding is the one to design around. Expression here is a usability mechanism: it makes the
important element easier to find. Every expressive choice should answer one question: does this guide
attention to what matters on this screen?

## The seven tactics

From "Start building with Material 3 Expressive". Each tactic is one axis along which a component,
layout, or product can become more expressive. Material presents them as guidance and encourages
experimentation. The Usability guidelines describe tactics "like containment, size, shape, color, and
typography"; that list is illustrative, and the blog's seven tactics are the fuller enumeration.

### 1. Use a variety of shapes

Shape sets the tone at first glance.

- Combining shapes and corner radii creates tension or cohesion and directs focus.
- Combine classic and abstract shapes to create distinct silhouettes or groupings.
- Use the shape library and the new corner radii to mix round and square shapes for contrast.
- Break from the surrounding shape style to draw attention to one element.

Failure mode: smaller shapes make essential actions look less important.

### 2. Apply rich and nuanced colors

- Mix primary, secondary, and tertiary on key components or visual elements to emphasize the screen's
  main takeaway.
- Build hierarchy with surface tones.
- Use contrast between accent roles to prioritize actions and simplify navigation.

Failure mode: without contrast, elements blend together.

### 3. Guide attention with typography

- Use emphasized type styles on important UI elements such as headlines and actions.
- Create editorial moments with emphasized typography.
- Heavier weights, larger sizes, color, and spacing direct attention to key information.
- Additional styles from the type scale create hierarchy within and between blocks of content.

### 4. Contain content for emphasis

- Organize content into logical groups or containers.
- Give the most important content, tasks, or actions prominence through ample space and the brightest
  surface mapping.
- Use size, spacing, rhythm, similarity, and other grouping principles to make important elements
  distinct.

Failure mode: ungrouped information blends together.

This tactic is about grouping that clarifies priority. It does not ask for more borders or nested
cards; see `design-quality.md`.

### 5. Add fluid and natural motion

- Use shape morph or surface effects to make interactions feel responsive.
- Apply the expressive motion springs or custom micro-animations. See `motion.md`.

### 6. Use component flexibility

- UI should adapt to the user's context.
- Shift components or controls with the environment to make tasks easier.
- Adapt content to foldables and large screens through canonical layouts or targeted changes. See
  `adaptive-layouts.md`.

### 7. Combine tactics for hero moments

Hero moments apply several tactics at once to break from uniform design. They make a standalone
statement or frame essential information in an editorial way. A hero moment is a focusing mechanism.

Material describes hero moments as brief, delightful, surprising, and unexpected, and advises: "Stick
to one or two hero moments in your product. Too many are overwhelming or distracting."

To find a hero moment, ask:

- Is this interaction emotionally significant? Can a design choice highlight that reaction or
  familiarity?
- Is this a key interaction in the product? Can a detail or flow be emphasized for clarity, such as
  making an important button dominant?

## What shipped

Fourteen new or updated components, with more configuration, shape options, and emphasized text:

App bars, button groups (new), common buttons, extended FAB, FAB menu (new), FABs, icon buttons,
loading indicator (new), navigation bar, navigation rail, progress indicators, sliders, split button
(new), toolbars (new).

Styles: the motion physics system (spatial and effects springs), emphasized typography, the expanded
shape library (35 shapes and shape morph), and more colorful color schemes. Later updates refreshed
lists, menus, search, and search app bars (`documentation/blog/whats-new-at-io26.md`).

## Deciding how much to use

This section is the skill's guidance, built from the tactics above. It is not Material text.

Expressive features pay off where attention, feedback, or tone matters:

- A primary action or transition needs faster recognition.
- A consumer product needs more emotional character.
- A state change is clearer with shape or spatial continuity.
- An official expressive component fits the job: button group, split button, FAB menu, toolbar,
  loading indicator, flexible app bar.

Use them sparingly where speed and density matter:

- Dense admin, settings, finance, data-entry, or repeated-work screens.
- Motion that would slow scanning or repeated actions.
- A target platform that lacks the component (see `platforms.md`).
- A project that cannot accept an experimental API.

Restraint does not mean flat. A calm screen still uses the type scale for hierarchy, surface tones
for grouping, the correct component emphasis, and responsive state feedback. Removing all shape and
color contrast produces a screen where nothing stands out, which fails the same research finding
from the other direction.

### Intensity tiers

A working rule for planning a screen:

| Tier | Typical elements |
|---|---|
| Quiet | Page surfaces, body text, routine list rows, frequent utility controls. |
| Clear | Navigation, selected states, section identity, secondary actions. |
| Expressive | One primary action, one key transition, an important progress state, or a brand moment. |

Most of a working screen stays quiet. If every region is expressive, nothing is emphasized.

### Introducing Expressive into an existing app

1. Keep navigation, state, and behavior unchanged.
2. Change the theme first (color scheme, shapes, motion scheme, type), then screens.
3. Migrate one component family or flow at a time.
4. Compare before and after at the same content and window sizes.
5. Check performance and interaction, not only appearance.
6. Remove old overrides that fight the new component defaults.

A phased migration can mix old and new components; make the boundary deliberate.

## Expressive review

- Each expressive treatment is attached to a user goal or an important state.
- The primary interaction is easier to find or use than before.
- Utility interactions are calmer than prominent ones.
- The screen works with motion disabled.
- Shapes and type stay legible with long localized text and large font scale.
- Code uses public theme and component APIs, not copied token internals.
- Every experimental API and opt-in is intentional.
- No morph, color, or shape could be removed without losing meaning.
- No transparency or blur exists only to imitate a glass effect.

## Official resources

- Guidelines: https://m3.material.io/
- Figma Design Kit: https://www.figma.com/community/file/1035203688168086460 (includes the shape
  library)
- Expressive research write-up: https://design.google/library/expressive-material-design-google-research
- Google Sans Flex: https://design.google/library/google-sans-flex-font
