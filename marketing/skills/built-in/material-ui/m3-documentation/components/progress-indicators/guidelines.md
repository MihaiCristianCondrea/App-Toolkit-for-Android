---
title: "Progress indicators"
source_url: "https://m3.material.io/components/progress-indicators/guidelines"
lastmod: "2026-02-27"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/videos/b7b18e2ec836-m37ri5xn-Progress-Indicator-A_1.mp4"
  - "assets/videos/59c753b73518-m0h3mssg-Progress-Indicator-B.mp4"
  - "assets/videos/fc21e413b2f6-m0h3n5x5-Progress-Indicator-C.mp4"
  - "assets/videos/b98aa4ecdc28-m0h3nu79-Progress_Indicator-C.mp4"
  - "assets/videos/3548b48b01f0-m0h3o5um-Progress_Indicator-D.mp4"
  - "assets/videos/bbd9eb03843c-m37rcxcs-Loading-Indicator-E_2.mp4"
  - "assets/images/8c7fca1f0044-mlep5ivu-07.png"
  - "assets/videos/e2d4cb4b9b5b-m0qw0hzu-Progress-Indicator-G.mp4"
  - "assets/videos/97ef0a8b77e8-m0qw2h3o-Progress-Indicator-K.mp4"
  - "assets/videos/a6d864c0afc3-m0qw333o-Progress-Indicator-H.mp4"
  - "assets/videos/1c11bb2da6dd-m0qw3uto-Progress-Indicator-I.mp4"
  - "assets/images/b0eefcd82a25-mlep6b3j-12.png"
  - "assets/videos/0bad54f1ef64-m0qw4l6g-Progress-Indicator-J.mp4"
  - "assets/images/c012959ed0d1-mlep7pxt-16.png"
  - "assets/images/dcefdad972bc-mlep87pn-17.png"
  - "assets/videos/fd5f56bb631a-m0qw57zf-Progress-Indicator-L.mp4"
  - "assets/videos/cb72be7aaafb-m0qw6vq0-Progress-Indicator-M.mp4"
  - "assets/videos/f662d941e493-m37rivp2-Progress-Indicator-N_1.mp4"
  - "assets/videos/a4d463b9b088-m0qw7zgb-Progress-Indicator-O.mp4"
  - "assets/videos/4a2b30ac9433-m0qw8fla-Progress-Indicator-P.mp4"
  - "assets/videos/b12ab7095d52-m0qwbhkm-Progress-Indicator-Q.mp4"
  - "assets/videos/1bb18232133d-m0qwbvqt-Progress-Indicator-R.mp4"
  - "assets/images/7e3b449ef86e-mlep96rs-25.png"
  - "assets/images/a0906f93d9c7-mlep9qg1-26.png"
  - "assets/images/b5e6a7871bae-mlepa8w9-27.png"
---

# Progress indicators

## Guidelines

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm37ri5xn-Progress-Indicator-A_1.mp4?alt=media&amp;token=12f49d7a-8022-4387-ba18-e01026d196d6" title="Primary colored horizontal line fills a contrasting track from left to right and reaches the end when the app is loaded."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm37ri5xn-Progress-Indicator-A_1.mp4?alt=media&token=12f49d7a-8022-4387-ba18-e01026d196d6>)

## Usage

Use progress indicators to show the status of ongoing processes, such as loading an app, submitting a form, or saving updates.

When multiple items are loading, use a single progress indicator to show progress for the group. Don’t add progress indicators to every activity.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3mssg-Progress-Indicator-B.mp4?alt=media&amp;token=49cf61f1-00e0-431c-ac53-9f7c366540c7" title="One progress indicator showing the loading progress for a page of podcasts."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3mssg-Progress-Indicator-B.mp4?alt=media&token=49cf61f1-00e0-431c-ac53-9f7c366540c7>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3n5x5-Progress-Indicator-C.mp4?alt=media&amp;token=540710ca-d428-4f87-8099-a891280ffa6f" title="Two progress indicators showing the loading progress of two podcasts."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3n5x5-Progress-Indicator-C.mp4?alt=media&token=540710ca-d428-4f87-8099-a891280ffa6f>)

Choose a loading or progress indicator that corresponds to the expected wait time and kind of process. 

If the wait is very long, consider allowing people to navigate away from the page while the process finishes up.

| **Expected wait time** | **Recommendation** |
| --- | --- |
| Instant (under 200ms) | No indicator |
| Short (between 200ms and 5s) | Loading indicator |
| Long (Over 5s) | Progress indicator |

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3nu79-Progress_Indicator-C.mp4?alt=media&amp;token=1ec2bfd0-5878-4530-8f1a-2ae5d8c21407" title="Screen without latency without progress indicator."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3nu79-Progress_Indicator-C.mp4?alt=media&token=1ec2bfd0-5878-4530-8f1a-2ae5d8c21407>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3o5um-Progress_Indicator-D.mp4?alt=media&amp;token=d6cf8057-eef7-41e2-b309-49ef255c9e12" title="Screen with slight lag with loading indicator."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0h3o5um-Progress_Indicator-D.mp4?alt=media&token=d6cf8057-eef7-41e2-b309-49ef255c9e12>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm37rcxcs-Loading-Indicator-E_2.mp4?alt=media&amp;token=15080baf-7d26-41aa-b6ed-f6e3ac35aab9" title="Screen with long lag and progress indicator."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm37rcxcs-Loading-Indicator-E_2.mp4?alt=media&token=15080baf-7d26-41aa-b6ed-f6e3ac35aab9>)

There are two variants of progress indicators:

1.  Linear

2.  Circular


**Linear** indicators are best when placed on the edge of a container.

**Circular** indicators are best when centered in an element.

A process should be represented by the same variant of progress indicator throughout the product. For example, if refreshing uses a circular indicator in one place, it should use circular indicators everywhere.

![1. A primary colored horizontal line fills a contrasting track from left to right. 2. A circle appears from 0 to 360 degrees.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlep5ivu-07.png?alt=media&token=4c51b00b-6b08-4638-a331-1e6340c7d5ea>)

_Linear indicator; Circular indicator; / /_

Progress indicators behave differently based on the time of progress being tracked:

-   **Determinate**: Known progress and wait time
-   **Indeterminate**: Unknown progress and wait time

When using a **determinate** indicator, the indicator must accurately represent the progress of what it's measuring. 

Use **indeterminate** indicators to show that a process is happening, but the wait time is unknown.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw0hzu-Progress-Indicator-G.mp4?alt=media&amp;token=22494899-6e26-4535-be27-9e21a1d9b7de" title="Linear and circular progress indicators show both determinate and indeterminate progress."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw0hzu-Progress-Indicator-G.mp4?alt=media&token=22494899-6e26-4535-be27-9e21a1d9b7de>)

As more information about a process becomes available, a progress indicator should change from **indeterminate** to **determinate**.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw2h3o-Progress-Indicator-K.mp4?alt=media&amp;token=20e232fe-707a-4619-b7a6-295516430e93" title="On a track, a contrasting primary-colored horizontal line grows and shrinks, then fills it from left to right as app loads."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw2h3o-Progress-Indicator-K.mp4?alt=media&token=20e232fe-707a-4619-b7a6-295516430e93>)

## Anatomy

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw333o-Progress-Indicator-H.mp4?alt=media&amp;token=f8f9dfed-e386-4c89-9fce-7f3ef92439c7" title="1. The track is a horizontal line. 2. A primary colored line over the contrasting colored track is the progress indicator."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw333o-Progress-Indicator-H.mp4?alt=media&token=f8f9dfed-e386-4c89-9fce-7f3ef92439c7>)

### Active indicator

The active indicator shows the progress that has been made so far. 

In indeterminate processes, it grows and shrinks along the track repeatedly.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw3uto-Progress-Indicator-I.mp4?alt=media&amp;token=220d7779-79b1-4dc5-a07e-b3aaa5ee2e38" title="A primary colored horizontal line fills a contrasting line from left to right.  "></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw3uto-Progress-Indicator-I.mp4?alt=media&token=220d7779-79b1-4dc5-a07e-b3aaa5ee2e38>)

The active indicator appears as soon as progress begins. At low percentages where space is limited, this should appear as a dot to help people understand that there’s progress underway.

![A linear and circular progress indicator at 1% progress, where the active indicator has only just appeared.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlep6b3j-12.png?alt=media&token=8aa71e47-95c1-48b7-9949-1b12b43093f6>)

_When progress first begins, the active indicator appears as a dot_

The active indicator has two shape options: **flat** and **wavy**. Use the shape that best fits the product’s tone.

The wavy shape can make longer processes feel less static and is best used when a more expressive style is appropriate.

When using the wavy shape, the overall height of the component changes. At very small sizes, the wavy shape may not be as visible.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw4l6g-Progress-Indicator-J.mp4?alt=media&amp;token=f46594f6-6dec-4153-98e5-2684fff598d9" title="Examples of flat and wavy active indicator."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw4l6g-Progress-Indicator-J.mp4?alt=media&token=f46594f6-6dec-4153-98e5-2684fff598d9>)

### Stop indicator

The stop indicator is a 4dp circle that marks the end of a linear determinate progress indicator to meet Material's accessibility standards.

It's not used for indeterminate or circular progress indicators.

The stop indicator is required if the track has a contrast below 3:1 with its container or the surface behind the container.

![A primary colored horizontal  line fills a contrasting line from left to right.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlep7pxt-16.png?alt=media&token=9f8c2fcb-6249-4ff4-bd3c-d8ee67658bf7>)

_Use a stop indicator when placing the progress indicator inside a container with low contrast_

![An invisible circular track fills with color from 0 to 360 degrees.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlep87pn-17.png?alt=media&token=c35ef411-e39f-4bc4-abe8-f4372b570d19>)

_Only remove the end stop indicator if there's a visual contrast of at least 3:1 with surrounding surfaces_

## Placement

Place a linear progress indicator along the edge of a container that’s loading. If the container changes shape, place it on the edge that animates. It can also be placed in the middle of a container.

Use a single progress indicator at the top of a page to show progress of the whole group. Don’t add one for every element unless they’re activated independently.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw57zf-Progress-Indicator-L.mp4?alt=media&amp;token=7c60ef03-e605-4c80-8259-ef607744c7f5" title="Progress indicator at top of screen, indicating page content is loading.
"></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw57zf-Progress-Indicator-L.mp4?alt=media&token=7c60ef03-e605-4c80-8259-ef607744c7f5>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw6vq0-Progress-Indicator-M.mp4?alt=media&amp;token=870b64b5-6c08-4507-add6-b40864b888d2" title="Progress indicator on a card, indicating that the card's content is loading."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw6vq0-Progress-Indicator-M.mp4?alt=media&token=870b64b5-6c08-4507-add6-b40864b888d2>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm37rivp2-Progress-Indicator-N_1.mp4?alt=media&amp;token=078eb6df-fe3e-4b23-af7e-68bf89206761" title="Linear indicator shows the loading progress of a selected news article in a list."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm37rivp2-Progress-Indicator-N_1.mp4?alt=media&token=078eb6df-fe3e-4b23-af7e-68bf89206761>)

Circular progress indicators should be centered directly on the container or page that's loading, such as a button or card.

When loading more items on a page, place the circular progress indicator in the empty space where the new content will appear, not overlapping existing content.

However, if the content does not take long to load, consider using a loading indicator instead.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw7zgb-Progress-Indicator-O.mp4?alt=media&amp;token=31eeacfa-6698-4f50-b5ad-bfcb1bcb5a69" title="As content loads, the screen is blank except for a circular progress indicator."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw7zgb-Progress-Indicator-O.mp4?alt=media&token=31eeacfa-6698-4f50-b5ad-bfcb1bcb5a69>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw8fla-Progress-Indicator-P.mp4?alt=media&amp;token=6a1386e7-6461-47a1-98ac-703c0ee61b3c" title="As container content loads, the container is blank except for a circular progress indicator."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qw8fla-Progress-Indicator-P.mp4?alt=media&token=6a1386e7-6461-47a1-98ac-703c0ee61b3c>)

### Progress indicators in buttons

A circular indicator can be placed in a button to show that the button’s action is currently in progress.

In very small buttons, use the flat shape since the wavy shape is not as visible at that size.

To ensure a minimum 3:1 contrast ratio, change the active indicator color to be the same color as the button’s icon or label text, and remove the track.

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qwbhkm-Progress-Indicator-Q.mp4?alt=media&amp;token=7dd18739-daa4-4e6f-bba1-7b24e0943703" title="Determinate circular indicator active for 2 seconds after download button is selected, reflecting download progress."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qwbhkm-Progress-Indicator-Q.mp4?alt=media&token=7dd18739-daa4-4e6f-bba1-7b24e0943703>)

<video controls src="https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qwbvqt-Progress-Indicator-R.mp4?alt=media&amp;token=4b18b8f4-63f2-4e78-baf8-cc6ef6e4166b" title="Shows idea of indeterminate circular indicators active on multiple buttons as  cluttered and confusing."></video>

[Open video](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fm0qwbvqt-Progress-Indicator-R.mp4?alt=media&token=4b18b8f4-63f2-4e78-baf8-cc6ef6e4166b>)

## Responsive layout

### Right-to-left languages

Linear progress indicators should be mirrored horizontally for products using right-to-left (RTL) languages. 

Circular progress indicators don’t need to be mirrored.

![Mirrored right-to-left progress indicator.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlep96rs-25.png?alt=media&token=75581f65-e1d4-4ac1-99a3-e8c371691127>)

_Linear progress indicators can flow from right to left in right-to-left (RTL) languages_

### Large screens

Circular progress indicators have flexible sizes. They can range from 24dp to 240dp, depending on the placement and the window size. Avoid exceeding the minimum and maximum sizes.

Reserve very large progress indicators for large and extra-large windows, such as desktop.

![Circular progress indicators can range in size from 24dps to 240dps.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlep9qg1-26.png?alt=media&token=3406dfaa-947f-4dec-8e16-66b6c3da848f>)

_The waveform should scale with the size so the proportions look the same across sizes_

Linear progress indicators dynamically adjust to fit the width of the window or element they’re placed within, such as a card. They shouldn’t be used in any elements smaller than 40dp.

The padding on each end should be 4dp minimum, but can be modified.

![Linear progress indicators can dynamically adjust to any width.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmlepa8w9-27.png?alt=media&token=b5dada5d-07ab-408a-a520-1eee7956020e>)

_The linear progress indicator should always span the width of the UI element it’s placed within_
