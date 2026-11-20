# Google Analytics Reference

This reference is for the GA4 capabilities that are currently exposed by this repository.

Use it when you need to remember:

- which CLI supports which task
- which analysis types are implemented
- which metrics and dimensions are already used in this repo
- what filter, sort, date, and output syntax the scripts actually support

For the validated implementation scope, see [references/current-status.md](references/current-status.md).

## Quick Map

| Need | Entry Point | Notes |
|------|-------------|-------|
| Raw GA4 report | `scripts/ga_client.py` | Custom metrics, dimensions, sorting, and one string dimension filter |
| Period comparison | `scripts/analyze.py --analysis-type overview --compare` | Returns JSON summary with change percentages |
| Source / medium analysis | `scripts/analyze.py --analysis-type sources` | Includes recommendations |
| Content diagnosis | `scripts/analyze.py --analysis-type content` | Flags high-bounce pages |
| Device comparison | `scripts/analyze.py --analysis-type devices` | Highlights mobile vs desktop gaps |

## Implemented Analysis Types

`scripts/analyze.py` currently supports:

- `overview`
- `sources`
- `content`
- `devices`

Notes:

- `--compare` only changes behavior in `overview` mode
- there is no `funnel`, `cohort`, or attribution analysis mode in this repo today

## CLI Contract

### `scripts/ga_client.py`

Purpose:
- run a direct GA4 Data API report

Supported options:

- `--env-file`
- `--days`
- `--start`
- `--end`
- `--metrics` (required)
- `--dimensions`
- `--limit`
- `--order-by`
- `--filter`
- `--format` with `json` or `table`
- `--output`

Important behavior:

- `--metrics` is required
- `--filter` currently expects `field:value`
- the current filter implementation builds a string dimension filter
- `--order-by` supports `-field` for descending and `+field` for ascending
- `--order-by` can target either a requested metric or a requested dimension

### `scripts/analyze.py`

Purpose:
- run one of the repository's opinionated GA4 analyses

Supported options:

- `--env-file`
- `--analysis-type`
- `--days`
- `--compare`
- `--output`

Output:

- JSON only

## Date Syntax

### Relative windows

For `ga_client.py`:

- `--days 30` becomes `30daysAgo` to `yesterday`
- default is `7daysAgo` to `yesterday`

For `analyze.py`:

- all current analysis flows use relative windows internally such as `30daysAgo`

### Absolute windows

`ga_client.py` supports:

- `--start YYYY-MM-DD --end YYYY-MM-DD`

Example:

```powershell
google-analytics/.venv/Scripts/python google-analytics/scripts/ga_client.py ^
  --env-file .env ^
  --start 2026-03-01 ^
  --end 2026-03-31 ^
  --metrics sessions,activeUsers
```

## Repo-Used Metrics

These are the metrics already used by this repository's scripts and analyses:

| Metric | Used In | Typical Meaning |
|--------|---------|-----------------|
| `sessions` | `ga_client.py`, `analyze.py` | Traffic volume |
| `activeUsers` | `ga_client.py`, `analyze.py` | Engaged audience size |
| `newUsers` | `analyze.py` | New-user acquisition |
| `bounceRate` | `ga_client.py`, `analyze.py` | Single-page or weak session behavior |
| `engagementRate` | `analyze.py` | Quality of visits |
| `averageSessionDuration` | `analyze.py` | Time spent in session |
| `conversions` | `analyze.py` | Conversion volume |
| `screenPageViews` | `analyze.py` | Page view volume |

## Repo-Used Dimensions

These are the dimensions already used by this repository's scripts and analyses:

| Dimension | Used In | Typical Meaning |
|-----------|---------|-----------------|
| `sessionSource` | `analyze.py` | Source of session traffic |
| `sessionMedium` | `analyze.py` | Medium of session traffic |
| `pagePath` | `analyze.py` | Page path |
| `pageTitle` | `analyze.py` | Page title |
| `deviceCategory` | `analyze.py` | Device type such as mobile or desktop |

The generic report CLI can also request other valid GA4 dimensions, but this document only lists fields already exercised by the repository code.

## Common Metric / Dimension Combos

### Traffic baseline

```text
metrics: sessions,activeUsers,bounceRate
dimensions: none
```

### Source / medium review

```text
metrics: sessions,engagementRate,bounceRate,conversions
dimensions: sessionSource,sessionMedium
```

### Content diagnosis

```text
metrics: screenPageViews,bounceRate,averageSessionDuration,conversions
dimensions: pagePath,pageTitle
```

### Device comparison

```text
metrics: sessions,bounceRate,averageSessionDuration,conversions,engagementRate
dimensions: deviceCategory
```

## Filter Syntax

Current filter syntax in `ga_client.py`:

```text
field:value
```

Examples:

```text
country:United States
deviceCategory:mobile
pagePath:/blog
```

Notes:

- current implementation splits on the first `:`
- current implementation sends this as a string dimension filter
- numeric filters and compound filter groups are not implemented in this repository

## Sorting Syntax

Current `--order-by` syntax:

- `-sessions` for descending by metric
- `+pagePath` for ascending by dimension
- `engagementRate` also sorts descending because no explicit `+` prefix is given

Examples:

```text
--order-by -sessions
--order-by +pagePath
--order-by -screenPageViews
```

## Output Shapes

### `ga_client.py --format json`

Top-level keys:

- `dimension_headers`
- `metric_headers`
- `rows`
- `row_count`
- `metadata`
- `totals` when available

Each row shape:

```json
{
  "dimensions": {
    "sessionSource": "google",
    "sessionMedium": "organic"
  },
  "metrics": {
    "sessions": "1250",
    "bounceRate": "0.45"
  }
}
```

### `ga_client.py --format table`

Shape:

- header row
- one line per row
- optional totals line
- final `Total rows: N`

### `analyze.py`

Current analysis output always returns JSON and usually contains:

- a period label
- one or more result lists such as `sources`, `problem_pages`, or `devices`
- `recommendations`

## Recommendation Logic Currently Embedded In `analyze.py`

### Sources

Current heuristics:

- highest conversion-rate source is flagged for scaling
- high-traffic, low-conversion sources can be flagged for optimization

### Content

Current heuristics:

- pages with bounce rate above `0.6` and views above `100` are candidates
- page issues are inferred from bounce rate and average duration

### Devices

Current heuristics:

- mobile is flagged when its conversion rate is more than 30% below desktop

Treat these as repository heuristics, not universal analytics truth.

## Common Mistakes

### Expecting unsupported analysis types

Not supported today:

- funnels
- cohorts
- retention
- MMM or attribution modeling

### Treating `analyze.py` as a raw report API

`analyze.py` is an opinionated diagnosis helper.
Use `ga_client.py` when you need a custom metric / dimension report.

### Forgetting the credential path is local

The service account file should live outside the repository and be referenced by path through `.env`.

## Trust Boundary

Current repository model:

- credentials are local
- the GA4 property is accessed directly through the Google SDK
- no third-party OAuth broker is required by this implementation

Do not commit:

- service account JSON files
- real property IDs tied to private projects
- copied raw analytics outputs containing sensitive business data
