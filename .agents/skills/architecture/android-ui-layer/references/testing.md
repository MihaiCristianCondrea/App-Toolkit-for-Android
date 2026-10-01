# Testing

What to test at each part of a screen. Template: `templates/XViewModelTest.kt.txt`. Reference:
`AboutViewModelTest.kt` and `AboutMappersTest.kt`. For frameworks and for choosing between fakes
and mocks, follow `testing-setup`.

## ViewModel

The ViewModel holds the screen's behavior, so it carries most of the tests. They run on JUnit 5
with `UnconfinedDispatcherExtension` from `:library:core:testing`:

```kotlin
class XViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val firebaseController = FakeFirebaseController()

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `repository error shows the failure state with a retry`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = XViewModel(repository = FailingXRepository(), firebaseController = firebaseController)
        advance()

        val items = viewModel.state.value.items
        assertThat(items).isInstanceOf(Loadable.Failed::class.java)
        assertThat((items as Loadable.Failed).retryable).isTrue()
    }
}
```

- Assert on `state.value` and `messages.value`. Both are plain `StateFlow`s, so a test reads the
  current value after `advance()`, with no collector.
- Cover at least: the first load, a failure, a retry after the failure, and each event's effect on
  state or messages.
- Test the message, not the snackbar: `messages.value.single().text` is the `UiTextHelper` the
  ViewModel raised. After `messageShown(id)` the queue is empty.
- `FakeFirebaseController.loggedEvents` records GA4 events, so a test can check that a failure sent
  `vm_op_error` with the right `action`. It does not record breadcrumbs or Crashlytics reports.
- A platform rule the ViewModel depends on comes from a repository a fake can set, as
  `FakeClipboardRepository(confirmsCopies = true)` does in `AboutViewModelTest`, instead of
  stubbing `Build.VERSION` or `ClipData` with static mocks.

## Mappers

Mappers are pure functions, so their tests need no dispatcher: build the input, call the mapper,
and assert on the keys, order and positions the content relies on. Put them under
`test/.../ui/mappers/`.

## Screen content

`XScreenContent` takes plain values and lambdas, so a Compose test sets it with a state and checks
what it draws and which callbacks fire:

```kotlin
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class XScreenContentTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun tapping_a_row_opens_its_details() {
        var opened: String? = null
        compose.setContent {
            MaterialTheme {
                XScreenContent(
                    state = XUiState(items = Loadable.Ready(persistentListOf(sampleItem))),
                    onEvent = {},
                    onOpenDetails = { opened = it },
                )
            }
        }

        compose.onNodeWithText(sampleItem.title).performClick()

        assertThat(opened).isEqualTo(sampleItem.key)
    }
}
```

- These tests run under Robolectric with JUnit 4. A module needs `ui-test-junit4`,
  `ui-test-manifest` and the JUnit vintage engine wired, which `:library:shell` has
  (`ShellScreenshotTest`) and feature modules do not yet. Build files are the maintainer's: ask
  before adding the dependencies.
- Test the content's own behavior with a `Ready` state: callbacks, visual state such as a tap
  counter, and what each state field changes on screen.
- The default empty and failure screens inject Koin bindings, so leave them to `:library:core:ui`
  unless the screen passes its own `onEmpty` or `onError`.
- Do not test `XScreen` itself. It only wires parts that are tested on their own.
