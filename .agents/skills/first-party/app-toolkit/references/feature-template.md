# Assemble a feature

Use [placement and declaration rules](architecture.md) to choose files. These starters work
together as an inventory catalog. Replace `<base>`, the feature segment `x`, and the `X` names
consistently. Put each file in the package declared in its header and remove the `.txt` suffix.
Use the [consumer integration guidance](consumer-integration.md) for the Toolkit APIs available
to the target. Standalone Android apps can use the same layout with the AndroidX patterns in
[ViewModel conventions](viewmodel-conventions.md).

| Template group | Static screen | Remote content | Reactive settings |
| --- | --- | --- | --- |
| Screen, views, previews | As needed | Yes | Yes |
| ViewModel, state, events | If state or work needs an owner | Yes | Yes |
| Repository | If the screen reads application data | Yes | Reuse the preferences repository or add one if the feature owns storage |
| Remote source and DTO | Omit | Yes | Omit unless backed by a service |
| Local source | Omit | If a local snapshot is useful | If the feature owns local storage |
| Mapping | Where representations differ | Where representations differ | Where representations differ |
| Use case | Only for a meaningful application operation | Optional | Optional |
| Navigation and settings search | Where the host exposes the screen | Navigation as needed | Navigation and search as needed |

## Remote catalog starters

- **Data.** [XRepository.kt](../templates/data/XRepository.kt.txt),
  [DefaultXRepository.kt](../templates/data/DefaultXRepository.kt.txt),
  [XLocalDataSource.kt](../templates/data/XLocalDataSource.kt.txt),
  [XRemoteDataSource.kt](../templates/data/XRemoteDataSource.kt.txt),
  [XDto.kt](../templates/data/XDto.kt.txt), [XModel.kt](../templates/data/XModel.kt.txt),
  [XMappers.kt](../templates/data/XMappers.kt.txt).
- **Domain.** [XUseCase.kt](../templates/domain/XUseCase.kt.txt). Select this only when the feature offers quantity-based
  availability. The basic catalog ViewModel reads the repository directly.
- **DI.** [XModule.kt](../templates/di/XModule.kt.txt).
- **UI.** [XScreen.kt](../templates/ui/XScreen.kt.txt),
  [XScreenPreview.kt](../templates/ui/XScreenPreview.kt.txt),
  [XViewModel.kt](../templates/ui/XViewModel.kt.txt), [XUiState.kt](../templates/ui/XUiState.kt.txt),
  [XEvent.kt](../templates/ui/XEvent.kt.txt),
  [XOperationNames.kt](../templates/ui/XOperationNames.kt.txt),
  [XConstants.kt](../templates/ui/XConstants.kt.txt), [XAnalytics.kt](../templates/ui/XAnalytics.kt.txt),
  [XNavigation.kt](../templates/ui/XNavigation.kt.txt),
  [XSettingsSearchProvider.kt](../templates/ui/XSettingsSearchProvider.kt.txt),
  [XItem.kt](../templates/ui/XItem.kt.txt), [XUiMappers.kt](../templates/ui/XUiMappers.kt.txt),
  [XView.kt](../templates/ui/XView.kt.txt), [XViewPreview.kt](../templates/ui/XViewPreview.kt.txt).
- **Tests.** [XViewModelTest.kt](../templates/testing/XViewModelTest.kt.txt),
  [XRepositoryTest.kt](../templates/testing/XRepositoryTest.kt.txt).

The full catalog uses a Ktor client configured for JSON, Kotlin serialization, Compose, Koin,
and the Toolkit UI, navigation and network APIs. The host loads `xModule`, binds the client and
`TelemetryRepository`, and supplies the endpoint as a named String binding using `xEndpoint`.
Provide localized `x_title` and `x_load_failed` resources in the feature, or rename the resource
references to existing feature strings. The endpoint returns a JSON array with `id`, `title`
and `availableQuantity` fields.

The default repository can use an offline snapshot through `XLocalDataSource`. If the host
needs it, implement that contract on the existing local storage and pass it to the repository
binding. For a remote-only feature, omit the contract and remove its constructor argument and
offline fallback from the repository starter. The repository test exercises the full version.

If the host uses the example navigation, call `xScreen(onOpenDetails = ...)` in its shell graph
and use the host's existing key serialization configuration. Use the settings search provider
only if the destination should appear in settings search, and register it under a distinct
Koin qualifier in the owning module. Reuse a registered destination when one already fits.

## Adapt by behavior

- For a static screen, keep the screen and stateless content, preview and any view files. Render
  the screen's direct inputs and use its host tracking contract if required. Omit the repository,
  ViewModel and loading states when the screen has no asynchronous work.
- For reactive settings, keep the state and event shape and use the existing preferences
  repository. Observe its Flow using the [ViewModel stream and recovery rules](viewmodel-conventions.md).
  Handle writes independently and make the observer recoverable. Select a DataStore source only
  if the feature owns those preferences. Adapt tests to cover initial values, changes and recovery.
- Validate an assembled catalog with the host's normal compilation and tests. The test starters
  use JUnit Jupiter and coroutine test support, with MockK for the repository and Toolkit test fakes
  for the ViewModel. See [validation](validation.md) for the distinction between placement checks
  and behavior checks.
