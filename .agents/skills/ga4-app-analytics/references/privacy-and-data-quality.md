# Privacy and Data Quality

## Privacy rule

Analytics should describe behavior without exposing the person, their private content, or effectively unique values.

Do not send sensitive or identifying information to GA4.

## High-risk values

Treat these as unsafe by default:

```text
email
phone number
full name
account identifier
device identifier
IP address
MAC address
URL
URI
file name
file path
precise location
raw search query
user-entered text
exception message
stack trace
authentication token
random UUID
```

A project may have additional domain-specific sensitive values.

## Bounded categories

Prefer small, stable value sets:

```text
outcome = success | timeout | error | cancelled
network_type = wifi | cellular | ethernet
level = beginner | intermediate | advanced
source = home | settings | onboarding
cleaner_type = duplicates | large_files | empty_folders
```

Bounded values are easier to analyze and reduce cardinality problems.

## High cardinality

Avoid dimensions that create a very large number of unique values.

Common causes:

- Random identifiers.
- Timestamps.
- Full URLs.
- Full file paths.
- Database row IDs.
- User-entered text.
- Dynamic screen names.
- Raw exception strings.

If a parameter is useful only because every value is unique, it usually belongs in another system rather than GA4.

## Numerical values

Use numeric parameters or custom metrics for measurements when the value itself is useful.

Do not convert numbers into strings merely to create dimensions.

Before adding raw measurements, ask whether the metric creates real product insight and whether aggregation is more appropriate.

## Error reporting

GA4 is not a replacement for Crashlytics.

Prefer bounded product outcomes such as:

```text
outcome = connection_error
```

rather than exception text.

Use Crashlytics for stack traces, exception messages, and debugging details.

## Search terms

A search event may be semantically correct but still unsafe if the search term can contain private information.

Review the product context before collecting raw search text.

A bounded category may sometimes be safer than the original text.

## User properties

Use user properties sparingly.

A user property should represent a stable, non-sensitive segmentation attribute that has clear analytical value.

Do not use user properties for:

- Unique IDs.
- Temporary state.
- Sensitive demographics.
- Raw behavior that belongs in events.

## Naming quality

Event and parameter names should be:

- Stable.
- Descriptive.
- Lowercase with underscores when using project conventions.
- Free from timestamps, IDs, and dynamic fragments.

Changing an event name creates a new analytics series. Treat schema names as a contract.
