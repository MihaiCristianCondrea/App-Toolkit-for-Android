# Design handoff contract

Part of [`marketing-ui`](../SKILL.md). Use this contract to carry an approved prototype from
`marketing/ui` into Android Jetpack Compose implementation and back through parity verification.

Keep the handoff lightweight. Record it in the task plan, feature note, or `marketing/ui/README.md`
for the screen being designed. Fill only the sections that apply to the change.

## Handoff checklist

1. **Screen purpose and user journey**
   - What job the screen performs, where users enter from, primary actions, and exit paths.
2. **Approved layout and component structure**
   - Top-level scaffold, section order, primary vs secondary actions, and component mapping to
     Material 3 (`androidx.compose.material3`) and the app's existing shared composables (App
     Toolkit's, when the app depends on it).
3. **Material 3 theme tokens**
   - Color roles, typography styles and typeface, shape corners, and surface/container roles used by
     the design, taken from [`material-ui`](../../../../official/google/material-ui/SKILL.md).
   - Mark each value as from M3 or as a design decision.
   - Do not invent raw colors or off-system fonts when an app theme token already exists.
4. **UI states and interactions**
   - Every supported state (`loading`, `empty`, `populated`, `error`, selection, disabled) and how
     user actions map to events, dialogs, sheets, or snackbar messages.
5. **Navigation behavior**
   - Destination route or arguments, callbacks vs ViewModel-triggered navigation, and back behavior.
6. **Motion and transitions**
   - Choreography, timing, and easing or spring feel guided by
     [`motion-design`](../../../../official/lottiefiles/motion-design/SKILL.md), translated into
     native Compose and Material motion APIs, or the app's own animation helpers (never Remotion or
     CSS assumptions).
7. **Adaptive layouts**
   - Behavior across supported form factors (`phone`, `fold`, `desktop`), window size breakpoints,
     pane splits, and readable max-width rules matching the app's layout policy.
8. **Accessibility**
   - Content descriptions, text contrast pairings, minimum 48 dp touch targets on Android, focus
     and reading order, and reduced-motion fallbacks.
9. **Reusable components and dependencies**
   - Existing shared app composables (App Toolkit's, when present) to reuse before creating new UI
     code.
10. **Approved deviations**
    - Any intentional deviation from existing app patterns, plus expected browser-only rendering
      differences in `marketing/ui`.
11. **Implementation and parity verification criteria**
    - Required Android build/test checks (`android-build-validation`) and parity checks
      (`npm run qa`, `npm run parity`, or side-by-side screenshots across approved states and
      windows).

## Synchronization rule

- Compose implementation must match the approved handoff contract. Do not introduce unapproved
  visual or behavioral changes silently during implementation.
- The Android application remains authoritative for runtime behavior, supported states, and
  technical contracts. If implementation uncovers a constraint that changes the design, update the
  handoff note and `marketing/ui` so both stay synchronized.
