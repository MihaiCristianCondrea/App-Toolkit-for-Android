# Release-note editorial guidelines

## What deserves space

Typical order of priority:
1. New tools or major capabilities users can access now.
2. Meaningful UI/navigation changes, including removed ads or changed controls.
3. Significant accessibility, offline, privacy, or reliability changes **when supported**.
4. Specific user-visible bug fixes.
5. Internal work only if its real impact is verified and worth mentioning.

Small patches may lead with the fix. A release that adds one feature should not bury it below eight generic maintenance bullets.

## Category rules

| Source type | Category | Example user-facing wording |
|---|---|---|
| Search added to top bar | Added | `• Search tools and apps from the top bar.` |
| Tablet layout changed | Changed | `• A new side rail and two-pane layout on tablets.` |
| Bottom banner removed | Changed | `• Removed the bottom banner ad.` |
| Broken sensor service no longer runs continuously | Fixed | `• Fixed battery drain caused by background sensors.` |
| Profile-guided startup improvements | Improved only when tested | `• Improved app startup responsiveness.` |
| Dependency injection refactor | Omit, absent impact | No user-facing claim just from DI |
| Main-thread offloading | Improved if validated | `• Smoother navigation while loading installed apps.` (only if corroborated) |
| New unit tests or CI improvements | Omit | Not user-visible |
| Internal crash log change | Omit unless fix verified | Avoid claims about fewer crashes without evidence |
| New fallback or retry for consent interruptions | Fixed | `• Consent setup now recovers from interruptions.` |

## Technically correct does not always equal user-relevant

- **Baseline profiles**: users do not need to know the profiling mechanism; say `Startup and scrolling improvements` only if the effect has been observed or established.
- **ViewModels / dependency injection / repository splits**: these describe architecture, not a feature. Usually omit.
- **Coroutines / threads**: mention a user-visible stall or responsiveness issue that was actually fixed, not a presumed speed-up.
- **Recomposition optimizations**: avoid claiming battery savings. If the animation behavior was improved, explain that behavior.
- **Caching**: only promise offline availability if the cached content can actually be accessed offline, not merely because a database/cache exists.
- **Ads**: mention removal or a meaningful visible change without promoting it.
- **Bug fixes**: use specific descriptions whenever source notes support them; combine small fixes into `• Fixed several layout and stability issues.` only when nothing more relevant fits.

## Compression examples

Original:
`Offloaded package-manager reads from the main thread and switched background work to an IO dispatcher.`

Safe if the source confirms freezes were fixed:
`• Fixed freezes while loading the apps list.`

Otherwise:
Omit, or explicitly describe it in a technical changelog separate from the Play note.

Original:
`Fixed orientation changes in the compass sensor lifecycle handler, preventing continued listening after leaving the screen.`

User-facing:
`• Fixed background compass sensor activity.`

Original:
`Implemented list/detail adaptive navigation and side rail at expanded width.`

User-facing:
`• Added tablet-friendly side navigation and two-pane layouts.`

## Style and voice

- Keep feature labels consistent with the app UI, including branded capitalization.
- Write from the user's perspective. Prefer `Search tools directly` to `Implemented in-app search subsystem`.
- Use sentence fragments in bullets if they scan better. Stay grammatically consistent within a release.
- Avoid hyperbole: revolutionary, best ever, lightning-fast, flawless, guaranteed.
- Avoid unsupported intensity: dramatically faster, major battery savings, crash-free.
- Avoid unfriendly abbreviations and giant compound bullets.
- Opening and section headings are optional; cut them before important content when space is tight.
- Do not use code fences or Markdown syntax in content meant for Play Console.

## Fact check protocol

For each proposed sentence ask: **Which supplied change establishes this claim?** If none, remove or qualify it. If the source says `planned`, `behind a flag`, `debug only`, or `not yet shipped`, exclude it from production notes. Reverts cancel corresponding additions. Prefer finalized changes over commit messages superseded by later work.
