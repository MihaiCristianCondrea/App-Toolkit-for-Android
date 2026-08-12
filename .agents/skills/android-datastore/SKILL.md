---
name: android-datastore
description: >
  Implement, review, refactor, migrate, and debug Jetpack DataStore in Android
  and Kotlin projects. Use when working with Preferences DataStore, typed
  DataStore, Proto DataStore, JSON DataStore, serializers, preference keys,
  DataStore migrations, corruption handling, multi-process DataStore,
  repositories backed by DataStore, or replacing SharedPreferences.
---

# Android DataStore

Use Jetpack DataStore for small persistent datasets such as user preferences,
settings, flags, and small typed configuration objects.

Read `references/datastore.md` when implementation details, setup,
serialization, multi-process behavior, corruption handling, or API-specific
decisions are needed.

Follow the project's existing architecture and conventions when they do not
conflict with DataStore correctness requirements.

## Decide whether DataStore is appropriate

Use DataStore for:

- user preferences;
- application settings;
- feature configuration;
- small key-value datasets;
- small typed persistent objects.

Do not use DataStore merely because data must be persisted.

Prefer Room when the data requires:

- large or complex datasets;
- queries;
- partial updates;
- relationships between records;
- referential integrity.

Prefer ordinary files when storing large standalone blobs or files where
DataStore's state-oriented API provides no benefit.

## Choose the DataStore type

Choose the simplest representation that preserves the required correctness.

### Preferences DataStore

Prefer Preferences DataStore when:

- the data consists of independent key-value preferences;
- a predefined schema is unnecessary;
- individual settings are simple primitive values;
- compatibility with a SharedPreferences-like model is useful.

Remember that Preferences DataStore does not provide a predefined typed schema.

### Typed DataStore

Prefer typed DataStore when several values form one coherent object or when
schema-level type safety is useful.

A typed DataStore requires a `Serializer<T>`.

Serialization may use:

- Protocol Buffers;
- Kotlin serialization with JSON;
- another suitable serialization format.

### Proto DataStore

Consider Proto DataStore when:

- a strongly typed schema is desirable;
- schema evolution matters;
- the stored object has multiple related fields;
- protobuf is already appropriate for the project.

Do not introduce protobuf automatically for a handful of unrelated primitive
preferences.

### JSON DataStore

Consider JSON DataStore when:

- typed storage is useful;
- Kotlin serialization is already used;
- human-readable persisted data is useful;
- protobuf infrastructure would add unnecessary complexity.

Do not assume Proto is mandatory for typed DataStore.

## Instance ownership

Never create multiple active DataStore instances for the same file in the same
process.

Treat this as a correctness requirement, not merely a style preference.

Create and provide the DataStore from a stable application-level location.

On Android, common approaches include:

- a top-level `preferencesDataStore` delegate;
- a top-level typed `dataStore` delegate;
- dependency injection providing a single instance;
- `DataStoreFactory` when explicit construction is required.

Do not instantiate DataStore repeatedly inside:

- repositories;
- ViewModels;
- Activities;
- Fragments;
- composables;
- functions called repeatedly.

When dependency injection is already used, prefer injecting the DataStore or
the data-layer abstraction that owns it.

## Data ownership

DataStore belongs to the data layer.

Prefer:

UI -> ViewModel -> Repository/DataStore abstraction -> DataStore

Do not access DataStore directly from production composables.

Do not make composables responsible for:

- preference keys;
- serialization;
- DataStore reads;
- DataStore writes;
- migrations;
- corruption recovery.

ViewModels should consume application-facing APIs rather than know how values
are physically persisted.

## DataStore abstraction

Follow the architecture already used by the project.

A small application may use a repository directly around DataStore.

A larger application may use a dedicated data-source class such as:

`UserPreferencesDataSource`
`SettingsDataSource`
`NotificationPreferencesDataSource`

with a repository above it when repository-level coordination or mapping is
useful.

Do not introduce both a DataStore data source and repository if one would only
forward every function without providing a meaningful boundary.

Do not expose `DataStore<Preferences>` itself to the UI layer.

## Group related preferences

Group preferences according to responsibility rather than putting every
setting in one global DataStore.

For example:

`UserPreferencesDataStore`
`NotificationPreferencesDataStore`
`NewsPreferencesDataStore`

may be preferable to:

`AppDataStore`

when the settings have clearly different ownership and lifecycles.

At the same time, do not create a separate DataStore file for every individual
preference.

Prefer cohesive groups of related settings.

## Reading data

DataStore exposes stored data through `Flow`.

Prefer exposing meaningful application values rather than raw
`Preferences` objects.

Example:

```kotlin
val themeMode: Flow<ThemeMode> =
    dataStore.data.map { preferences ->
        preferences[THEME_MODE]
            ?.let(ThemeMode::valueOf)
            ?: ThemeMode.SYSTEM
    }