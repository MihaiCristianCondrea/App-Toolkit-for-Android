# Ruler and Protractor creative source

This is a work in progress. The first render contains five phone, four tablet and four desktop screenshots, a feature graphic and an icon. It is not ready to upload: the phone compositions need more variety and the supporting sequence does not yet show Bubble Level. The renderer also logged intermittent font request failures that require investigation.

The source adapts the speaker-water-eject composition and verification code. It mounts the Marketing UI's ruler, protractor, converter and Measure states. The background uses a pale blue field and a connected graduation line.

From this directory, run:

```text
npm ci
npm run lint
npm run probe -- ruler protractor advanced converter measure ruler@fold protractor@fold advanced@fold measure@fold ruler@desktop protractor@desktop advanced@desktop measure@desktop
npm run solve
npm run verify -- en-US
npm run render -- en-US
npm run metadata
```

The verification output and contact sheets are in ignored `output/verify/`. The first full render completed and metadata readback passed. The Google Play listing validator passed, but does not establish visual quality or production availability.
