# Android Apps Metadata API

App Toolkit consumes the public Android Apps Metadata Worker API for the developer-app catalog,
expanded app metadata, and package-specific changelogs.

## Contract and documentation

- Base URL: `https://android-apps-metadata-backend.mihaicristiancondrea.workers.dev`
- Swagger UI: [`/docs`](https://android-apps-metadata-backend.mihaicristiancondrea.workers.dev/docs)
- OpenAPI document:
  [`/openapi.json`](https://android-apps-metadata-backend.mihaicristiancondrea.workers.dev/openapi.json)

Runtime code must use the public `/api/v1` routes. Routes under `/admin/api` are catalog-management
surfaces and may include unpublished applications; they are not app-client endpoints.

## Public endpoints

| Purpose | Method and route | App behavior |
|---|---|---|
| Compact catalog | `GET /api/v1/apps` | Loaded once for the Apps & Tools grid |
| Full app metadata | `GET /api/v1/apps/{package_name}` | Loaded only when an app is selected |
| Changelog Markdown | `GET /api/v1/apps/{package_name}/changelog.md` | Loaded for the host package |

`ApiHost` is the source of truth for the base URL and route construction. Package names are
encoded as path segments before a request is made.

## Host package and version injection

`AppToolkitHostBuildConfig.applicationId`, `versionName`, and `versionCode` are registered by
`appToolkitFoundationModules(...)` as a `BuildInfoProvider`. This keeps library code independent
from any host app's generated `BuildConfig`.

The same injected application ID drives the changelog route:

```text
AppToolkitHostBuildConfig.applicationId
    -> BuildInfoProvider.packageName
    -> GetChangelogUseCase
    -> ChangelogRepository.fetchChangelog(packageName)
    -> ApiHost.appChangelogUrl(packageName)
```

The Apps & Tools catalog itself is not scoped to the host package. Each compact item supplies its
own `package_name`, which is passed to the full-details endpoint when the item is expanded.

## Apps list and details mapping

The list endpoint maps to `AppSummary`:

- `name`
- `package_name`
- `icon_logo`
- `short_description`
- `category`

The details endpoint maps to `AppDetails`:

- the summary fields
- full `description`
- every valid `screenshot`, including aspect ratio and device type
- every valid labeled link
- optional `latest_version` metadata when supplied by the API

DTOs remain in the data layer. Mappers sanitize HTTP(S) URLs and convert the API's snake_case
payload into domain models. The UI never parses API responses.

The grid intentionally does not fetch full details for every item. `AppsListViewModel` requests
details after `HomeEvent.AppSelected`, displays the compact summary immediately, and ignores a
late response if another app has been selected in the meantime.

## Changelog selection and fallback

`ChangelogRepositoryImpl` requests raw Markdown from the public package endpoint. The repository
and use case apply the following behavior:

| Condition | Result |
|---|---|
| Worker returns Markdown containing the exact current-version heading | Show that version section |
| Worker returns non-blank Markdown without the current-version heading | Show the full history |
| Worker returns an empty Markdown body | Show the localized no-updates message |
| Package name is blank, or the Worker endpoint returns HTTP 404 | Fetch the existing raw GitHub changelog |
| Worker returns another HTTP/network failure | Show the retry state; do not hide the failure with stale fallback data |
| GitHub fallback also fails | Show the retry state |

The GitHub URL is therefore a compatibility fallback, not a second primary source. Exact Markdown
heading matching prevents a version mentioned in release-note text, or a partial version such as
`1.2.3` inside `11.2.30`, from being selected accidentally.

## Layer ownership

- **Core:** `ApiHost`, `BuildInfoProvider`, shared network/error primitives.
- **Data:** API DTOs, mappers, `DeveloperAppsRepositoryImpl`, and
  `ChangelogRepositoryImpl`.
- **Domain:** repository contracts, `FetchDeveloperAppsUseCase`,
  `FetchAppDetailsUseCase`, and `GetChangelogUseCase`.
- **UI:** `AppsListViewModel`, `ChangelogViewModel`, immutable screen state, and Compose
  rendering.

The named Koin qualifier `ANDROID_APPS_METADATA_API_BASE_URL` supplies the base URL to both app
metadata and changelog repositories. Tests may override that binding with a mock server URL.

## Verification checklist

When the backend contract changes:

1. Compare the public endpoints and schemas against the OpenAPI document.
2. Update DTOs and mappers without leaking DTOs into domain/UI.
3. Verify compact list loading and a full-details request for at least one package.
4. Verify current-version, full-history, empty-body, HTTP 404 fallback, and retry cases.
5. Run repository/use-case/ViewModel unit tests and a debug compile.
