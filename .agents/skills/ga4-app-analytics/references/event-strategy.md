# Event Strategy

## Event classes

Every event should belong to one of these groups.

### Automatically collected

Collected by Firebase, GA4, AdMob, or an existing integration without a custom event call.

Do not duplicate these events.

### Google recommended

Use when the product behavior genuinely matches Google's documented event meaning.

Recommended events can provide better built-in reporting, integrations, and common semantics.

### Product-specific custom

Use when an important product behavior has no suitable recommended event.

Custom events are not second-class events. They often provide the most useful understanding of the application because each product has its own features, content, and success states.

### Operational or diagnostics

Events or breadcrumbs that describe application operations, loading, errors, or technical behavior.

Keep these distinct from product value signals when possible.

## Design events around meaning

Prefer events that describe user intent or outcomes.

Good examples:

```text
lesson_opened
lesson_completed
cleanup_started
cleanup_completed
scan_completed
report_shared
subscription_started
```

Weak examples:

```text
button_clicked
icon_pressed
row_tapped
card_clicked
```

A click event is justified only when the click itself answers a useful product question.

## Use parameters for bounded context

When several features represent the same underlying action, use one event with parameters.

Example:

```text
cleaner_completed
cleaner_type = empty_folders | duplicates | large_files | apk
outcome = success | cancelled | error
```

This is usually better than:

```text
empty_folders_cleaner_success
duplicates_cleaner_success
large_files_cleaner_success
apk_cleaner_success
```

The shared event is easier to query and keeps the vocabulary stable.

## Separate events when concepts are different

Do not collapse materially different actions into a vague event.

Prefer:

```text
speed_test_completed
wake_on_lan_sent
lesson_completed
cleanup_completed
```

instead of:

```text
feature_action
```

with many unrelated parameter combinations.

## Track impressions carefully

A component being visible is not the same as a user choosing it.

Distinguish:

```text
battery_card_impression
battery_details_opened
```

Only track impressions when exposure itself matters to the analysis.

## Track completion separately from start when useful

For an important workflow, start and completion can create a useful funnel.

Example:

```text
cleanup_started
cleanup_completed
```

If failures are meaningful, completion can include a bounded outcome:

```text
outcome = success | cancelled | error
```

Do not add separate start and completion events to trivial actions that complete immediately.

## Recommended versus custom

Use a recommended event when the meaning matches.

Use a custom event when it does not.

Do not rename product behavior to resemble a Google event.

Example:

```text
LAN discovery
```

is not automatically a GA4 `search` event.

## Custom event examples by app type

### Cleaner

```text
cleaner_opened
cleaner_completed

cleaner_type = quick_clean | duplicates | large_files | empty_folders | apk
outcome = success | cancelled | error
```

### Tutorials

```text
lesson_opened
lesson_completed

language = kotlin | java | python
level = beginner | intermediate | advanced
```

### Network utility

```text
network_scan_started
network_scan_completed
speed_test_completed
wake_on_lan_sent
```

### Media

```text
content_opened
playback_started
playback_completed
favorite_changed
```

Use these only as design examples. The project review must determine the actual vocabulary.

## Event contract

Prefer a centralized host-owned contract containing:

- Event names.
- Parameter names.
- Allowed bounded values when useful.
- Required parameters.
- Forbidden parameter names.
- Contract validation tests.

This makes the analytics schema reviewable and prevents inline event strings from drifting across features.
