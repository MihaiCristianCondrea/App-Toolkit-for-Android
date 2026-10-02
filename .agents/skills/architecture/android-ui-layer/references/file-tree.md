# File tree

Where each part of a screen lives. `X` stands for the screen name; `layered-tree-review` covers
placement beyond the UI layer.

## One screen

```text
feature/x/
├── build.gradle.kts
├── README.md
└── src/
    ├── main/kotlin/<base>/feature/x/
    │   ├── data/                         # android-data-layer; omit when shared repositories serve
    │   │   └── exceptions/               # the feature's own failures, if its repository throws any
    │   ├── di/
    │   │   └── XModule.kt                # val xModule: repositories and the ViewModel
    │   └── ui/
    │       ├── contracts/
    │       │   └── XEvent.kt             # sealed interface of user events
    │       ├── mappers/
    │       │   ├── XErrorMappers.kt      # the feature's failures to Loadable.Failed, then the defaults
    │       │   └── XMappers.kt           # data or domain model to UI model
    │       ├── models/
    │       │   └── XItem.kt              # render models, when the data model does not fit
    │       ├── navigation/               # routes and page registration
    │       ├── states/
    │       │   └── XUiState.kt           # @Immutable data class with Loadable fields
    │       ├── views/                    # stateless pieces used inside the content
    │       ├── XScreen.kt                # XScreen, then XScreenContent and its @Previews
    │       └── XViewModel.kt
    └── test/kotlin/<base>/feature/x/
        └── ui/
            ├── mappers/
            │   └── XMappersTest.kt
            ├── XScreenContentTest.kt     # only where the module runs Compose tests
            └── XViewModelTest.kt
```

Create only the folders the screen needs. A screen whose data model renders as it is has no
`models/` or `mappers/`. A screen with no ViewModel-driven state has no `states/` or `contracts/`.

## Feature with several screens

Each screen gets its own set of files, still flat in `ui/`, sharing the folders:

```text
ui/
├── contracts/
│   ├── XListEvent.kt
│   └── XDetailsEvent.kt
├── states/
│   ├── XListUiState.kt
│   └── XDetailsUiState.kt
├── XListScreen.kt
├── XListViewModel.kt
├── XDetailsScreen.kt
└── XDetailsViewModel.kt
```

A secondary surface with a ViewModel of its own, such as a bottom sheet, repeats the set under
`ui/features/<name>/`. `layered-tree-review` decides when that is right.

## Naming

| File                 | Declares                                    | Visibility            |
|----------------------|---------------------------------------------|-----------------------|
| `XScreen.kt`         | `fun XScreen(...)`, screen name constants   | public                |
|                      | `fun XScreenContent(...)`, its previews     | `internal`, `private` |
| `XViewModel.kt`      | `class XViewModel`                          | public, for Koin      |
| `XUiState.kt`        | `data class XUiState`                       | public                |
| `XEvent.kt`          | `sealed interface XEvent`                   | public                |
| `XMappers.kt`        | `fun XData.toXItems()` extension functions  | `internal`            |
| `XModule.kt`         | `val xModule: Module`                       | public                |

One top-level class or interface per file, named after it. An interface and its implementation are
two files side by side, as `AboutRepository.kt` and `DefaultAboutRepository.kt` are. Private
helpers, constants and extension functions may share the file that uses them.

`XScreen` and `XScreenContent` share `XScreen.kt`, the screen first and the content below it, as
`AboutScreen.kt` does. The two are read and changed together, so one file keeps the screen in one
place; the content never gets a file of its own. A dialog or sheet follows the same rule:
`ChangelogDialog` and `ChangelogDialogContent` share `ChangelogDialog.kt`.

The screen name passed to `TrackScreenView`, `TrackScreenState` and the ViewModel's `screenName` is
the same literal. Declare it once at the top of `XScreen.kt` as `private const val X_SCREEN_NAME`;
the content in the same file reports with it too.

A `views/` file holds a composable the content uses, such as a row or a card. `XScreenContent`
itself stays in `XScreen.kt`, because it is the screen's body, not a reusable piece.
