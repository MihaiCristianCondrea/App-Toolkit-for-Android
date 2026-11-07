# Speaker Water Eject listing source

This listing uses the default listing's Remotion renderer as a local starting point, with an aquatic palette and a Water Eject first screen. It uses the shared Marketing UI and Material-Next device frame, not a separate device model. Its outputs belong only to this listing.

## Blocked before rendering

The Marketing UI Water Eject panel does not match the Android implementation: it shows a 30-second readout and extra playing text when idle, while Android shows 165 Hz; its guidance and note also differ. Its running preview resets itself unless an audio session has started. No Android device was connected to compare a rendered capture. Do not render or upload these planned graphics until the Water Eject UI and running state match the app and have been visually checked against it.

The Play page did not establish that version 26.11.02 is available to the intended audience. Confirm rollout in Play Console before publishing images of its features.

After resolving those issues, remove the guard in `scripts/render.mjs`. From this folder run `npm install`, `npm run lint` and `npm run render -- en-US`. Inspect every image, review the per-file `metadata.json`, run the metadata skill's injector and check, and run the Play listing validator. Rendering overwrites this listing's graphics folders.

The first two planned images show the ready and running Water Eject states. The remaining images introduce the surrounding toolbox. Captions and alt text are in `strings/en-US.json`; layouts are in `src/`.
