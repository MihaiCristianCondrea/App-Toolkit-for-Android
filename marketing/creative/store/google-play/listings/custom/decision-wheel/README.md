# Decision Wheel custom listing

Status: planned, not published. The English creative package contains 6 phone, 4 tablet and 4 desktop screenshots, a feature graphic and the official icon. Upload remains conditional on confirming that the production release includes the Decision Wheel and the other Play & Decide chance tools as shown, and on reviewing the search keywords in Play Console.

## Audience and positioning

People looking for a simple wheel spinner or a random pick for everyday decisions: what to eat, what to do, who goes first. They enter 2 to 12 choices, spin, and see the pick below the wheel. Random Number, Coin Flip and Dice Roll sit beside it in Play & Decide. The title leads with the search intent, "Spin the Wheel", and keeps App Toolkit's name. The copy says the tools are for playful everyday decisions, not prize drawings or decisions that need a verified random process.

## Listing text

| File | Text | Length |
| --- | --- | --- |
| title.txt | Spin the Wheel: App Toolkit | 27 / 30 |
| short-description.txt | Add your choices, spin the wheel and let a random pick help you decide. | 71 / 80 |
| full-description.txt | Wheel, everyday examples, the other chance tools, where to find them, limits | 1,126 / 4,000 |

Every claim was checked against the Android source (sample/feature/tiles): the wheel accepts 2 to 12 choices, one per line, up to 1,000 characters, and picks with `Random.nextInt`; Random Number takes any whole-number range, includes both ends and offers 1 to 2, 1 to 6, 1 to 10 and 1 to 100 presets; Coin Flip shows Heads or Tails; Dice Roll rolls one six-sided die; Coin Flip and Dice Roll are Android Quick Settings tiles; all four sit in Play & Decide's Chance section.

The keywords describe intent, not measured demand; see keyword-review.md for the priority, the rejected terms and what to check in Play Console. See [Google Play custom store listings](https://support.google.com/googleplay/android-developer/answer/9867158).

## Creative direction

Six phone screenshots on a soft lilac field with coral and lilac MaterialShapes and confetti scattered across each image, captions in a deep plum with a lilac pill. The app keeps its own captured colours. Phones are Material-Next's porcelain 3D Pixel 9 Pro XL, each in a gentle solved pose, centred, with the caption above or below as the composition needs. Every image shows a different screen or state:

1. Spin the wheel, let chance decide: the wheel ready with four food choices.
2. One spin, one random pick: six activities, settled on Music.
3. Your choices, your wheel: the choice entry with four names.
4. Pick a random number: 37 in the range 1 to 100.
5. Heads or tails, let it flip: Coin Flip settled on Tails.
6. Roll the dice, make a choice: Dice Roll showing five.

Tablet and desktop screenshots show the app's own large-screen layouts on the Pixel 9 Pro Fold and the Pixelbook Go, centred at true size with no captions, as Play asks. The feature graphic sets "Spin the Wheel" with App Toolkit small above it beside a large drawn wheel in the campaign's colours, running off the right edge, with confetti scattered over the open space; it has no device frame, no icon and no claims.

States, the composition system, its hard rules and preferences are described in source/README.md; review/ holds the contact sheets, the verification report, the package audit and the repeatability check.

## Verification and limits

The layout solver, the verifier (geometry, caption contrast and interference on the composited pixels, balance), the renderer, TypeScript, the package audit, metadata injection and readback and the Play listing validator were run. Screens were compared against the Compose source and resources; no connected-device comparison or production rollout confirmation is claimed. Search targeting still needs to be selected from the terms Play Console makes available.
