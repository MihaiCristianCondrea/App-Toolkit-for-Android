# Custom store listings

| Listing | Audience | Status |
| --- | --- | --- |
| [Speaker Water Eject](speaker-water-eject/README.md) | People whose phone speaker got wet | live |
| [Ruler and Protractor](ruler-and-protractor/README.md) | People who need an on-screen ruler and an interactive protractor for everyday measurements | live |
| [Decision Wheel](decision-wheel/README.md) | People wanting a wheel spinner or a random pick for everyday decisions | live |
| [Unit Converter](unit-converter/README.md) | People converting everyday length, mass, temperature, volume and decimal storage units | live |
| [Tip & Bill Splitter](tip-and-bill-splitter/README.md) | People calculating a restaurant tip and splitting a bill equally between friends | live |

All five were approved by Google Play review and are live. Each keeps a dated log in its listing.json; record Play Console keyword selections in a keyword-review.md as they are made.

Each listing has the same source and upload-file split as default:

```text
<id>/
├── listing.json      audience, targeting, status and dated log
├── keywords.txt      search terms, one per line
├── README.md         audience, copy, creative direction, verification and limits
├── keyword-review.md how the keywords were chosen and Play Console results (once reviewed)
├── source/           reproducible visual source
└── listing/<locale>/ text, graphics and alt text
```

Status is planned, live, paused or retired. Log each status change. Retired listings remain as records of what was published.
