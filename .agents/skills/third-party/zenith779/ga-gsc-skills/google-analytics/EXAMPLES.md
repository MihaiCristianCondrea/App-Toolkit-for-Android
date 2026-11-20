# Google Analytics Examples

These examples are based on what this repository actually supports today.

Use them as:

- copy-paste command starters
- answer templates for common GA4 questions
- examples of how to turn raw GA4 output into actionable recommendations

## Example 1: Traffic Overview

User request:

```text
Review our GA4 traffic for the last 30 days and compare it with the previous 30 days.
```

Recommended command:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type overview ^
  --days 30 ^
  --compare
```

What to look for:

- changes in `sessions`
- changes in `activeUsers`
- changes in `newUsers`
- changes in `bounceRate`
- changes in `engagementRate`

Suggested answer shape:

```text
Summary:
- Sessions increased / decreased by X%
- Active users increased / decreased by Y%

Key Findings:
- New-user acquisition improved / weakened
- Engagement quality improved / weakened

Recommended Actions:
- Inspect the main acquisition sources behind the period change
- Validate whether growth is coming from qualified traffic

Confidence / Limitation:
- This is a period comparison based on aggregate metrics, not attribution analysis
```

## Example 2: Source / Medium Review

User request:

```text
Which traffic sources are underperforming?
```

Recommended command:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type sources ^
  --days 30
```

What to look for:

- high `sessions` but low `conversion_rate`
- weak `engagement_rate`
- high `bounce_rate`

Suggested answer shape:

```text
Summary:
- Top traffic volume comes from A / B / C

Key Findings:
- Source X has strong traffic but weak conversion
- Source Y has the best conversion rate and is a scaling candidate

Recommended Actions:
- Increase investment in the highest-converting source
- audit landing pages for high-volume low-conversion traffic

Confidence / Limitation:
- Conversion rate here is computed from aggregate sessions and conversions in the current script
```

## Example 3: Content Diagnosis

User request:

```text
Which pages should we fix first?
```

Recommended command:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type content ^
  --days 30
```

What to look for:

- pages with high views
- bounce rate above the script threshold
- weak average session duration
- problem pages repeated in recommendations

Suggested answer shape:

```text
Summary:
- N pages were flagged as high-bounce candidates

Key Findings:
- Page A likely has content mismatch
- Page B may be missing a strong CTA

Recommended Actions:
- improve intent match on the highest-view problem page
- add clearer next steps or CTA blocks on pages with long reading time but poor conversion

Confidence / Limitation:
- The script uses heuristics, so these pages are candidates for review, not definitive failures
```

## Example 4: Mobile vs Desktop

User request:

```text
Is mobile performing worse than desktop?
```

Recommended command:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type devices ^
  --days 30
```

What to look for:

- `conversion_rate` gap between `mobile` and `desktop`
- `bounce_rate` gap
- `engagement_rate` gap
- whether the built-in recommendation marks this as critical

Suggested answer shape:

```text
Summary:
- Mobile accounts for X sessions and converts at Y%
- Desktop converts at Z%

Key Findings:
- Mobile conversion is materially below desktop
- Mobile engagement is stronger / weaker than desktop

Recommended Actions:
- review mobile checkout friction
- inspect page speed, form length, and CTA visibility on mobile

Confidence / Limitation:
- This is a device-level aggregate comparison, not a UX session replay analysis
```

## Example 5: Direct Custom Report

User request:

```text
Export a GA4 report by source and medium as JSON.
```

Recommended command:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/ga_client.py ^
  --env-file .env ^
  --days 30 ^
  --metrics sessions,engagementRate,bounceRate,conversions ^
  --dimensions sessionSource,sessionMedium ^
  --order-by -sessions ^
  --format json
```

What to look for:

- top rows by traffic volume
- metric strings under each row's `metrics`
- totals when present

When to prefer this over `analyze.py`:

- when you need custom metrics
- when you need a custom dimension combination
- when you want raw rows instead of repository heuristics

## Example 6: Filtered Mobile Content Report

User request:

```text
Show mobile-only page performance for the last 30 days.
```

Recommended command:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/ga_client.py ^
  --env-file .env ^
  --days 30 ^
  --metrics screenPageViews,bounceRate,averageSessionDuration ^
  --dimensions pagePath,pageTitle ^
  --filter deviceCategory:mobile ^
  --order-by -screenPageViews ^
  --limit 25 ^
  --format table
```

Important note:

- the current filter implementation only supports one simple `field:value` string filter

## Example 7: Absolute Date Window

User request:

```text
Run a report for March 2026 only.
```

Recommended command:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/ga_client.py ^
  --env-file .env ^
  --start 2026-03-01 ^
  --end 2026-03-31 ^
  --metrics sessions,activeUsers,bounceRate ^
  --format table
```

Use this when:

- monthly finance or reporting windows need exact boundaries
- you do not want the relative `daysAgo` style window

## Example 8: Answer Template For Agents

Use this response structure when turning GA output into a recommendation:

```text
Summary:
- one or two lines on the overall trend

Key Findings:
- finding 1 with metric evidence
- finding 2 with metric evidence
- finding 3 with metric evidence

Recommended Actions:
- HIGH: action with reason
- MEDIUM: action with reason
- LOW: action with reason

Confidence / Limitation:
- state whether this is a raw report, heuristic diagnosis, or unverified causal inference
```

## Not Yet Supported In This Repo

Do not present these as implemented repository features:

- funnel analysis
- cohort analysis
- retention analysis
- attribution modeling
- Admin API account or property management
