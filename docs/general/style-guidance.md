# Style Guidance

- Follow official Kotlin coding conventions.
- Prefer immutable `val` properties and keep functions small and focused.
- Name classes and files using `PascalCase`; use `camelCase` for functions and variables.
- Each file should end with a trailing newline.
- Compose UI uses Material 3 theming; reference `MaterialTheme` for colors, typography, and spacing.
- Use Kotlin Coroutines and Flow for asynchronous work and state streams.
- Inject dependencies with Koin; obtain ViewModels via Koin helpers.

## Serialization checklist

- Keep remote/data DTOs on `kotlinx.serialization` (`@Serializable`).
- Use `@Parcelize` only for Android state transport boundaries (Bundle/SavedState/navigation keys).
- If one model appears to need both, split it into dedicated transport models.
