# Custom store listings

| Listing | Audience | Status |
| --- | --- | --- |
| [Speaker Water Eject](speaker-water-eject/README.md) | People whose phone speaker got wet | planned |
| [Ruler and Protractor](ruler-and-protractor/README.md) | People who need an on-screen ruler and an interactive protractor for everyday measurements | planned |
| [Decision Wheel](decision-wheel/README.md) | People wanting a wheel spinner or a random pick for everyday decisions | planned |
| [Unit Converter](unit-converter/README.md) | People converting everyday length, mass, temperature, volume and decimal storage units | planned |

Each has English copy, keywords and graphics prepared. They stay planned until the app's production rollout is confirmed, the keywords are reviewed in Play Console and the listing is published.

Each listing has the same source and upload-file split as default:

```text
<id>/
├── listing.json      audience, targeting, status and dated log
├── keywords.txt      search terms, one per line
├── README.md         audience, copy, creative direction, verification and limits
├── keyword-review.md how the keywords were chosen and Play Console results (once reviewed)
├── review/           contact sheets and verification reports (listings with a verifier)
├── source/           reproducible visual source
└── listing/<locale>/ text, graphics and alt text
```

Status is planned, live, paused or retired. Log each status change. Retired listings remain as records of what was published.
