# Recommended Events

## Purpose

Google recommended events provide common semantics and can unlock built-in reporting and
integrations.

Use them when the product behavior actually matches the documented event meaning.

Do not use this file as a checklist that every app must implement.

## Common app events

Examples that may apply to Android applications include:

```text
tutorial_begin
tutorial_complete
select_content
search
share
login
sign_up
purchase
```

Other recommended events may apply depending on the product.

Always verify the current official Google documentation before relying on an exact parameter
requirement or monetization behavior.

## tutorial_begin and tutorial_complete

Use when the application has a real onboarding or tutorial journey.

Good use:

```text
first-run onboarding opened
tutorial_begin

first-run onboarding completed
tutorial_complete
```

Do not use these events for unrelated help pages or normal feature walkthroughs unless they
genuinely represent the onboarding/tutorial flow you want to measure.

## select_content

Use when the user selects meaningful content from a collection.

Typical parameters include a bounded content type and stable item identifier.

Good examples:

```text
select_content
content_type = lesson
item_id = compose_basics
```

or:

```text
select_content
content_type = scanner_tab
item_id = local_network
```

Do not place sensitive or high-cardinality values in the item identifier.

## search

Use only for a real user search experience.

Do not use `search` as a generic substitute for:

- Network probing.
- LAN discovery.
- Scanning storage.
- Filtering a fixed list when no search term exists.

Use the documented search parameters correctly.

If the actual search term may contain sensitive or private information, reconsider whether the raw
term should be collected.

## share

Use when the user shares content, a report, an item, or another meaningful object.

Prefer bounded parameters that describe what was shared and how when analytically useful.

Do not send the shared content itself.

## login and sign_up

Use only when the application has a real account flow.

Do not invent authentication events in apps without accounts.

Never send credentials, email addresses, tokens, or personal details as parameters.

## purchase

Use Google's documented purchase semantics for real transactions.

Before adding or modifying purchase events, inspect existing Billing, Firebase, Play, or ecommerce
integrations so revenue is not duplicated or reported inconsistently.

## Decision rule

For every potential recommended event, answer:

1. Does the user action genuinely match Google's meaning?
2. Can the documented parameters be populated safely?
3. Is the event already collected automatically or elsewhere?
4. Would a product-specific custom event describe the behavior more accurately?

If the answer to the first question is no, do not use the recommended event.
