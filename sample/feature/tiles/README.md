# `:sample:feature:tiles`

## Purpose

The native Quick Tools, Measure and Play & Decide workspaces, their tool sheets, and their Quick Settings services. The approved visual reference is `marketing/ui`.

## Ownership

- `ToolkitTilesRepository` publishes the source-neutral catalogue for all three workspaces and Quick Settings membership. UI mappers own resources, shapes and artwork.
- Sensor, breathing, torch, Morse, SOS, counter, conversion, bill and ruler repositories retain their existing responsibilities. `WaterEjectRepository` is the entry point to finite speaker playback; `AndroidSpeakerAudioDataSource` owns AudioTrack and audio focus.
- Dedicated `LoggedScreenViewModel` classes own tool state and events. Koin supplies their repositories and `SavedStateHandle` where input restoration is needed. No feature depends on another feature.
- The app module owns serializable navigation keys and the shell graph. This feature exposes screens without importing app-owned routes.
- Shared widget geometry lives in `ui/views/catalog`. The compass and level clip to the same 8-sided and 16-sided brand SVG paths as the web kit.

## Workspaces

| Destination | Sections and tools |
| --- | --- |
| Quick Tools | Sensors: Compass, Bubble Level. Light & signals: Flash Dimmer, combined Morse & SOS. Hardware & sound: Water Eject. Wellbeing: Breathe. Colors, last: Material Colors. |
| Measure | Length & angle: Surface Ruler, Protractor. Convert & split: Unit Converter, Tip & Bill Splitter. Count & time: Counter, Reaction Test. |
| Play & Decide | Decision Wheel, Random Number, Coin Flip, Dice Roll. |

The widgets follow the marketing kit's (`marketing/ui`): `views/catalog/LiveWidget.kt` draws its live widget (a small uppercase label, the tool's face in its shape on a tint of its accent, a bold value and a note), and the tool accents in `iconColors()` are the kit's `--tk-*` tokens. `RedesignVisualTest` renders the three tabs, light and dark, to `build/outputs/redesign/` for side-by-side review with the kit.

Tool headings and individual overview cards join the shared screen entrance cascade. Sections do not animate as a second enclosing layer. Motion settings and reduced motion remain owned by the library. `ToolDesignTheme` supplies fixed light and dark product accent colours to the workspaces and their tool sheets. The app theme retains ownership of the screen background and neutral surfaces. ToolWorkspace reserves the shell padding outside its clipped scrolling viewport, so cards and their animations cannot draw under the navigation bars. The marketing preview uses the same roles in `src/theme/toolkit.css`.

All three primary tabs read their localized search query from `LocalShellSearch`. Empty sections disappear when searching. Quick Tools no longer shows category chips or collapse controls; the old expansion preference is retained for compatibility and is not applied to the new overview.

The overview and sheet use the same entry-owned tool ViewModels. Currency, tip selection, converter values, counter updates and chance results therefore stay in sync. Dice and coin visuals are directly interactive; the surrounding card opens their sheet. Reaction Test Start both opens the sheet and starts a round.

## State and lifecycle

- Counter remains DataStore-backed and shared with its Quick Settings service. Accepted writes survive the caller disappearing; storage failures are reported.
- Bill, converter, random-range, ruler draft calibration/reading/range, protractor, Morse input, dice/coin outcomes and wheel choices use `SavedStateHandle`. Dismissing a sheet retains inputs and results. A new process never resumes audio or flashing automatically.
- Compass and level overview instances have distinct keys from sheet instances. Each starts sampling only while its lifecycle is started and stops on stop or disposal. The sheet retains its existing foreground sensor handling.
- Torch strength writes cancel superseded UI requests. Leaving the foreground or disposing the tool stops its in-app torch work. Morse and SOS share the existing serialized playback owner, so replacement waits for the previous final torch-off command.
- Water Eject starts only from an explicit action. It requests transient audio focus and plays a finite 30-second 165 Hz cycle at a conservative track gain. Stop, focus loss, dismissal, backgrounding and ViewModel cancellation release playback and audio focus. It never changes system volume or requests a foreground service, microphone access or a wake lock. The UI does not promise waterproofing or repair.
- Wheel choices accept 2–12 non-blank lines. A spin selects one index uniformly, locks editing and resolves that segment under the fixed pointer. Dismissal settles the selected result rather than drawing again. Motion and haptic feedback belong to the Compose view, and animation follows the system motion scale.
- Reaction Test cancels its pending signal when the app stops. Foreground return does not manufacture a reaction result.

## Numeric tools

Bill splitting uses decimal arithmetic and rounds each share up to cover the total. USD, EUR, GBP, RON and INR use two fractional digits; JPY uses whole yen. Currency changes formatting and precision, not exchange rates. Turning off the tip preserves the chosen rate but applies zero until re-enabled.

Unit Converter retains bidirectional conversion and physical validation. Swap exchanges units and values while keeping the active quantity. Native menus and focus behavior replace the web controls.

Surface Ruler and Protractor use full-screen native dialogs. The ruler covers the whole display, cutout included, with the system bars hidden, so its scales start at the physical edge. Its scales run down the long side: in portrait with the vertical DPI, in landscape with the horizontal DPI, times the persisted calibration. Simple shows the scales and a reading dragged from zero; advanced adds the measurement range and the settings in `RulerSettings` (units, edge, zero end, inch steps), which `RulerRepository` saves as they change. Calibration keeps room for its reference line at maximum scale so its controls do not move. Protractor places its baseline on the left in portrait and on the bottom with a right control dock in wide windows. Its touch mapping shares geometry with drawing and retains an accessible slider and angle presets.

## Ads and Quick Settings

The existing Quick Tools native placement follows Sensors and is suppressed during search. `QuickToolsNativeAdViewFactory` lays it out as the marketing kit's `.qt-ad`: the disclosure label drawn as a "Sponsored content" section title inside the `NativeAdView`, then a card with the icon in a nine-sided cookie and a full-width tonal action. Tool sheets and the other redesigned overviews do not add ad placements. Native ad rendering and ownership remain in core UI.

Quick Settings service names, request keys and the released application ID remain unchanged. Android 13+ uses the existing system add-tile request. Older releases retain manual setup instructions. The catalogue still hides torch tools on devices without a torch. No new manifest permission is introduced by this redesign.

## Validation

Run the tiles unit and Compose tests, app navigation/DI tests, module-boundary verification and Android lint. `RedesignStateTest` covers currency/tip restoration, converter swaps, wheel locking and selection, protractor geometry and audio cancellation/retry. `RedesignVisualTest` renders native overview captures under Robolectric for comparison with marketing. Hardware behavior still needs a device check, especially sensor calibration, torch strength and speaker output.
