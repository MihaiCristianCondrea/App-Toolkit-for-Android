# Monetization and Marketing

## Purpose

Analytics can influence monetization in several different ways. Keep these mechanisms separate so recommendations stay accurate.

## 1. Product monetization analysis

Any meaningful event can help explain which behaviors correlate with revenue.

Examples:

```text
cleaner_completed
lesson_completed
scan_completed
feature_opened
```

When GA4 also contains ad or purchase revenue, these events can help answer:

- Which features are used by high-value users?
- Which content categories correlate with more revenue?
- Which workflows are associated with stronger retention?
- Which users see more ads without losing engagement?
- Which actions predict subscriptions or purchases?

This is one of the strongest reasons to add product-specific custom events.

## 2. AdMob personalization

Google documents specific App Analytics Connection behavior for automatically collected and recommended Analytics events.

Do not automatically claim that arbitrary custom events directly improve AdMob ad personalization.

When this distinction matters, verify the current AdMob documentation before making a strong statement.

Use wording such as:

```text
This custom event is valuable for product and monetization analysis.
Current Google documentation should be checked before claiming that it directly participates in AdMob personalization.
```

## 3. Audiences and remarketing

Custom events and custom dimensions can be valuable for audience building when allowed by current platform rules and user consent.

Examples of product segments:

```text
users who complete advanced Kotlin lessons
users who successfully use a specific cleaner category
users who repeatedly use speed tests
```

Do not use sensitive or identifying attributes to build audiences.

## 4. Key events and Google Ads

A small number of important product events can be marked as key events in GA4 and used for marketing measurement or Google Ads optimization.

Potential examples depend on the product:

```text
purchase
subscription_started
tutorial_complete
lesson_completed
cleanup_completed
account_created
```

Do not mark every event as a key event.

Key events should represent outcomes important to the product or business.

## 5. Experiments

GA4 events can be useful as goals or analysis signals for Firebase experiments and Remote Config changes.

Examples:

- Compare onboarding variants using `tutorial_complete`.
- Compare ad placement variants using retention and revenue outcomes.
- Compare feature presentation using completion events.
- Compare lesson recommendations using `lesson_opened` and `lesson_completed`.

Do not change monetization behavior solely because an event exists. Use experiments when the tradeoff between revenue and product experience is uncertain.

## 6. Automatic ad events

Ad integrations can already provide automatic ad events and revenue data.

Before adding ad-specific events, inspect:

- AdMob integration.
- Mediation.
- Impression-level revenue handling.
- Manual paid event logging.
- Existing Firebase linkage.

Do not duplicate automatic ad impressions, clicks, exposures, or revenue.

## Classification for recommendations

For every proposed event, include one or more of these labels:

```text
Product insight
Funnel
Retention
Monetization analysis
Marketing
Ad personalization
Experimentation
Diagnostics
```

This forces the strategy to explain why the event exists.

## Example: cleaner app

```text
cleaner_completed
cleaner_type = empty_folders
outcome = success
```

Possible value:

```text
Product insight          strong
Funnel                   strong
Retention                useful
Monetization analysis    strong
Marketing                possible
Ad personalization       do not claim directly without current Google support
Experimentation          useful
```

## Example: tutorials app

```text
lesson_opened
language = kotlin
level = intermediate
```

This can reveal which content categories attract engagement and which categories correlate with completion, retention, ad revenue, or purchases.

That information can be more valuable to the product than an unrelated recommended event.
