# Serialization Boundaries

## Purpose

Define which serialization strategy is allowed at each layer to keep performance, architecture
boundaries, and maintainability aligned.

## Rules

- **Data/remote layer DTOs must use `kotlinx.serialization` (`@Serializable`)** for HTTP/JSON
  payloads.
- **Android UI state transport must use `Parcelable` (`@Parcelize`)** for Bundle-compatible
  persistence (for example `rememberSaveable`, SavedState, and navigation keys).
- Do not annotate domain models with `@Parcelize` unless the domain type itself is explicitly a UI
  transport model.

## Why

- `@Serializable` is the right tool for API encoding/decoding and keeps network contracts explicit.
- `@Parcelize` is optimized for Android framework state transport and avoids unnecessary JSON-style
  encoding for restore paths.

## Checklist for code reviews

- If a model crosses a network boundary, ensure it remains `@Serializable`.
- If a model is saved/restored through Android state transport, prefer a Parcelable transport model.
- If a type is used in both contexts, split it into dedicated transport models instead of mixing
  responsibilities.
