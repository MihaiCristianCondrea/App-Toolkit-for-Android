# Keyword review

Search keywords for this custom listing live in `keywords.txt`, one per line, for Play Console's bulk add. The list is broad on purpose, for discovery research: it is not a claim that every phrase has search demand or improves ranking.

## What the data supports

Play Console's acquisition reports for the 85 days reviewed showed 120 store-listing acquisitions (59 from Google Play explore, 59 from ads and referrals) and only aggregate search-term categories; no individual keyword demand was disclosed. English (US) and English (UK) led with 42 and 17. So English search intent comes first, and no term here is presented as having verified volume or a ranking benefit.

## Counts

| Step | Terms |
| --- | --- |
| Before (commit 07b60ea6) | 117 |
| Candidates added | 169 |
| Rejected | 6 |
| In keywords.txt | 280 |

Every term is unique ignoring case and at most 80 characters (the longest is 46); Play allows 500.

## Priority

1. **Established, direct:** water eject, speaker water eject, speaker cleaner, wet speaker, remove water from speaker, get water out of phone speaker, and their Android and phone variants.
2. **Natural language:** how to get or remove water from a phone speaker, what to do when a speaker is wet.
3. **Exploratory:** symptom queries (muffled or quiet after water, splash, rain), the speaker grille, and specific frequency (165 Hz) and timer or countdown phrases. Treat these as experiments, not proven opportunities.
4. **Brand:** App Toolkit with Water Eject, wet speaker or speaker tone.

## Rejected

Rejected because they would bring people expecting something the app does not do.

| Term | Reason |
| --- | --- |
| phone speaker tone | too generic: tone generators and ringtones |
| stop speaker tone | ambiguous: people trying to silence a tone their phone plays |
| low frequency speaker tone | tone-generator and bass-test intent; the app has no frequency control |
| speaker tone timer | timer and beep-timer intent, not a wet speaker |
| speaker tone with timer | timer and beep-timer intent, not a wet speaker |
| 30 seconds speaker tone | generic tone or timer intent, not a wet speaker |

Never add: iPhone or iOS-only targeting, microphone cleaning or repair, dust removal, speaker repair, hardware diagnostics or audio enhancement, ultrasonic cleaning, competitors' names, or frequency controls, automatic modes and speaker tests the app does not have.

## Before publishing

Play Console's keyword selector has not been checked yet: this repository has no Console access. In Console:

1. Add `keywords.txt` with the bulk add and note which terms the selector recognises.
2. Look at Google's suggested variations and translations, and prefer the most relevant bundles it offers.
3. Remove any term that leads to an audience expecting functionality the app does not provide.
4. Record the outcome below, keeping the three groups separate.

### Selected

Not yet reviewed in Play Console.

### Unavailable in Play Console

Not yet reviewed in Play Console.

### Rejected in Play Console

Not yet reviewed in Play Console.
