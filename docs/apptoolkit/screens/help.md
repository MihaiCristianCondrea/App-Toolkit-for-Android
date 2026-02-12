# Help (AppToolkit)

The **Help** feature provides a reusable help/FAQ experience that can be embedded into any AppToolkit
host app. It supports:

- **FAQ loading with fallback** (remote → local resources)
- a **polished Compose UI** (expandable questions + “Contact us”)
- optional **in-app review entry point** (with online help fallback)
- an optional **single native ad placement** that respects layout and labeling requirements
- **structured logging hooks** via `LoggedScreenViewModel` (and `FirebaseController`)

This module is intentionally built as a “ready-to-go” screen: host apps can use it as-is, or swap out
data sources, ad config, links, and UI pieces while keeping the same contracts.

---

## Architecture

### Data
- **`HelpRemoteDataSource`**
  - Fetches:
    - the remote FAQ catalog (`FaqCatalogDto`)
    - question lists (`List<FaqQuestionDto>`) from each configured source URL.
- **`HelpLocalDataSource`**
  - Loads offline FAQs from string resources and maps them into `FaqItem`.
- **`FaqRepositoryImpl`**
  - Orchestrates the retrieval strategy:
    1. try remote catalog → matching product → all question sources
    2. if remote yields no items, fall back to local bundled questions
    3. if both are empty, emit an error

### Domain
- **Models**
  - `FaqId` (inline value class)
  - `FaqItem` (id + question + answer)
- **Repository contract**
  - `FaqRepository.fetchFaq(): Flow<DataState<List<FaqItem>, Errors>>`
- **Use case**
  - `GetFaqUseCase`:
    - delegates to the repository
    - normalizes data (trim, filter blanks, de-dupe by id)

### UI
- **Entry**
  - `HelpActivity : BaseActivity` → `HelpScreen(config)`
- **Screen**
  - `HelpScreen` is a full screen scaffold:
    - `LargeTopAppBarWithScaffold`
    - floating action button for feedback / online help
    - `ScreenStateHandler` for Loading / Empty / Error / Success
- **Content**
  - `HelpScreenContent` renders:
    1. title ("Popular help resources")
    2. FAQ list (`HelpQuestionsList`)
    3. native ad (`HelpNativeAdCard`)
    4. "Contact us" card
- **Components**
  - `QuestionCard` (expand/collapse, animated size, haptics + click sound)
  - `ContactUsCard` (haptics + click sound)
  - `HelpScreenMenuActions` (Play Store, version info dialog, beta, terms, privacy, OSS licenses)

---

## Data flow (how the screen works)

### FAQ loading
1. `HelpViewModel` triggers `HelpEvent.LoadFaq` in `init`.
2. `loadFaq()` starts collecting `GetFaqUseCase()`.
3. UI state transitions:
   - `Loading` while collecting
   - `Success` with a non-empty list
   - `NoData` when list is empty
   - `Error` when repository returns an error / exception

### Feedback / review CTA
- The FAB changes behavior based on Google Play in-app review availability:
  - If review is available → request in-app review (`HelpEvent.RequestReview`)
  - Otherwise → open online help (fallback URL)

If the review flow fails (or doesn’t launch), the ViewModel emits:
- `HelpAction.OpenOnlineHelp(url = HelpConstants.FAQ_BASE_URL)`

---

## Ads

The Help screen includes an optional **single native ad** placed **between**:
- the FAQ list, and
- the "Contact us" card

Implementation notes:
- The placement is in `HelpScreenContent` as a dedicated `item` in the `LazyColumn`.
- The banner uses a named `AdsConfig`:
  - `koinInject(qualifier = named("help_large_banner_ad"))`
- The ad rendering is handled by `HelpNativeAdCard(...)`.
- The ad component should show an **“Ad”** label and maintain spacing so it never crowds content.

For overall ad configuration, see: `docs/shared/guide/ads/index.md`
(or your project’s equivalent “ads overview” doc).

---

## Logging and observability

### `LoggedScreenViewModel`
`HelpViewModel` extends `LoggedScreenViewModel`, so screen operations are consistently logged with:
- `startOperation(action = ...)`
- `launchReport(...)`
- `catchReport(...)`

This gives you uniform breadcrumbs and error reporting across all AppToolkit screens.

### Launch Help

```kotlin
startActivity(Intent(context, HelpActivity::class.java))
```

### Provide remote FAQ configuration

`FaqRepositoryImpl` depends on:

* `catalogUrl`
* `productId`

So the host app (or DI module) must supply them. Typical approach:

* define per-app constants (or BuildConfig fields)
* bind them through Koin when wiring the help feature

---

## Customization points (host apps)

You can override pieces without rewriting the whole screen:

* **FAQ content**

    * use your own `catalogUrl/productId`
    * swap `HelpLocalDataSource` resources
    * change DTO schema and mapper if your backend differs
* **Links**

    * replace `HelpConstants.FAQ_BASE_URL`
    * adjust menu URLs (terms, privacy, beta)
* **Ads**

    * provide a different `AdsConfig` qualifier
    * remove the ad item entirely if the host app disables ads
* **UI**

    * replace `QuestionCard` visuals
    * change the FAB action (e.g., always email, always open FAQ, etc.)

---

## Related docs

* Common screen state pattern: `docs/apptoolkit/guide/ui/screen-state.md`
* Ads overview + configs: `docs/shared/guide/ads/index.md`
* Logging conventions: `docs/shared/guide/observability/logging.md`