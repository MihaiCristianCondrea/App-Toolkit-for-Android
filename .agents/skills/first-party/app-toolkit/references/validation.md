# Validation

Inspect the target's Gradle configuration and verification tasks. For implementation, start with
the affected module's tests, then relevant integration and repository checks. App Toolkit has
`checkModuleBoundaries`. Verify that this or any other named task exists before using it in a
consumer or standalone app. Broaden compilation, JVM tests, or lint as the change warrants. Run Gradle validations
serially in the same checkout.

For ViewModel changes, verify observable state and messages with the project's coroutine and
telemetry test support. When fixing a failed observer, test failure, the retry event, and a later
emission reaching state. Test duplicate requests only when the job policy matters. For provider
or graph changes, check composition with the resolved dependencies. Check on a device or emulator
when behavior depends on the real shell, navigation, or snackbar action.

For a public API change, compile a representative caller and run any compatibility checks that
the repository provides. Report the exact checks that passed and any unresolved environment or
runtime limitation.
