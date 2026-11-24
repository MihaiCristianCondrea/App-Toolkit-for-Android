---
name: google-search-console
description: Use when analyzing Google Search Console performance for a verified property, investigating query or page CTR issues, comparing recent periods, or breaking down organic search data by page, query, country, or device.
---

# Google Search Console

Use this skill when the task is about search-performance analysis for a Search Console property that the configured account can already access.

This repository currently focuses on the Search Analytics side of Google Search Console:

- performance totals for a recent period
- query analysis
- page analysis
- country and device breakdowns
- current period vs. previous period comparison
- URL inspection for a specific page
- raw export as `table`, `json`, or `csv`

It does **not** currently provide dedicated workflows for:

- sitemap submit/delete flows
- site verification or property management
- full SEO auditing beyond the available GSC metrics

For the currently validated implementation scope, see [references/current-status.md](references/current-status.md).

Further reading in this skill directory:

- [references/current-status.md](references/current-status.md) for validated capabilities and live verification notes

## When to Use

Use this skill when the user asks for things like:

- "show GSC performance for the last 7 / 28 / 90 days"
- "find high-impression, low-CTR queries"
- "which pages are underperforming in Search Console?"
- "compare this period with the previous one"
- "break performance down by country or device"
- "inspect this URL and tell me whether Google can index it"
- "export Search Console rows to CSV or JSON"

Do **not** use this skill when the user primarily needs:

- GA4 traffic and conversion analysis
- Google Trends opportunity discovery
- sitemap administration

## Prerequisites

This skill discovers `.env` from the current working directory chain first, then the home directory chain, and also supports `--env-file`.

Start by copying [`.env.example`](../.env.example) to `.env` in your project directory or one of its parent directories.

Recommended variables:

```env
GOOGLE_SERVICE_ACCOUNT_CREDENTIALS=C:/path/to/google-service-account.json
GOOGLE_SEARCH_CONSOLE_SITE_URL=sc-domain:example.com
```

Credential setup checklist:

- create or reuse a Google Cloud service account
- enable `Google Search Console API`
- download the JSON key to a local path outside this repository
- add the same service account email to the target Search Console property with read access
- use `--list-sites` after authorization if you need to confirm the exact property identifier for `GOOGLE_SEARCH_CONSOLE_SITE_URL`

The same shared service account can be reused across this repository's Google skills.
For setup details, see [docs/google-service-account-setup.md](../docs/google-service-account-setup.md).

Install dependencies:

```bash
python -m venv google-search-console/.venv
google-search-console/.venv/Scripts/python -m ensurepip --upgrade
google-search-console/.venv/Scripts/python -m pip install -r google-search-console/requirements.txt
```

## Recommended Entry Points

List accessible properties:

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/gsc_client.py ^
  --env-file .env ^
  --list-sites
```

Fetch a page report for the last 28 days:

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/gsc_client.py ^
  --env-file .env ^
  --days 28 ^
  --dimensions page ^
  --limit 50 ^
  --format table
```

Find query opportunities:

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type queries ^
  --days 28
```

Find page opportunities:

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type pages ^
  --days 28
```

Compare the current period with the previous period:

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type overview ^
  --days 28 ^
  --compare
```

Inspect a specific URL:

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/gsc_client.py ^
  --env-file .env ^
  --inspect-url https://example.com/blog/post ^
  --format table
```

## Verified Commands

These command shapes have been validated against a real Search Console property in this repository:

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/gsc_client.py ^
  --env-file .env ^
  --site-url <verified-property-id> ^
  --days 7 ^
  --dimensions page ^
  --limit 1 ^
  --format json
```

```bash
google-search-console/.venv/Scripts/python google-search-console/scripts/gsc_client.py ^
  --env-file .env ^
  --site-url <verified-property-id> ^
  --inspect-url https://example.com/ ^
  --format json
```

For the exact validated property form and current notes, see [references/current-status.md](references/current-status.md).

## Common Requests

### 1. Performance Overview

Goal:
- understand clicks, impressions, CTR, and average position for a recent period
- compare with the previous period when trend direction matters

Recommended path:
- `analyze.py --analysis-type overview`

### 2. Query Opportunity Review

Goal:
- find search terms with strong impressions but weak CTR
- identify keywords sitting near page-one thresholds

Recommended path:
- `analyze.py --analysis-type queries`
- or `gsc_client.py --dimensions query --format csv` for custom export

### 3. Page Opportunity Review

Goal:
- find pages with meaningful impressions but middling average position
- review which URLs deserve title, snippet, or content improvements

Recommended path:
- `analyze.py --analysis-type pages`
- or `gsc_client.py --dimensions page`

### 4. Country or Device Breakdown

Goal:
- see whether organic performance differs by geography or device type

Recommended path:
- `analyze.py --analysis-type countries`
- `analyze.py --analysis-type devices`
- or `gsc_client.py --dimensions country` / `device`

### 5. URL Inspection

Goal:
- check whether a specific URL is indexable
- review verdict, coverage state, crawl outcome, and canonical hints

Recommended path:
- `gsc_client.py --inspect-url https://example.com/path --format table`
- use `--format json` when downstream tooling needs the raw response shape

## Output Contract

Current output modes:

- `table`: human-readable CLI summary
- `json`: structured raw output for downstream processing
- `csv`: spreadsheet-friendly export

URL inspection currently supports:

- `table`
- `json`

Current analysis coverage:

- `overview`
- `queries`
- `pages`
- `countries`
- `devices`

For combined or more custom breakdowns, prefer `gsc_client.py` with explicit dimensions and filters.

## Common Failure Modes

### Authentication or Access

Typical symptoms:

- `401 Unauthorized`
- `403 Forbidden`
- property not listed under `--list-sites`

Checks:

- verify `GOOGLE_SERVICE_ACCOUNT_CREDENTIALS`
- confirm the service account was added to the Search Console property
- confirm `GOOGLE_SEARCH_CONSOLE_SITE_URL` points to the GSC property, not a GA property ID

### Invalid Property or Dates

Typical symptoms:

- bad request responses
- empty or confusing result windows

Checks:

- use `https://.../` or `sc-domain:...` for `GOOGLE_SEARCH_CONSOLE_SITE_URL`
- use absolute dates in `YYYY-MM-DD` when passing `--start` / `--end`

### Filter Confusion

Current filter syntax:

- `dimension:expression`
- `dimension:operator:expression`

Examples:

```text
query:brand
page:contains:/blog/
country:equals:USA
```

### URL Inspection Scope Confusion

Typical symptoms:

- expecting sitemap-level or property-management actions from the inspection command
- expecting CSV export from `--inspect-url`

Checks:

- use `--inspect-url` only for one inspected URL at a time
- use `json` or `table` output for inspection
- keep sitemap and site-management work out of this skill for now

## Trust Boundary

Current repository model:

- credentials stay local and are loaded from `.env` plus local credential files
- Search Console data is accessed directly through Google-authenticated client libraries and APIs
- URL inspection uses the official Google endpoint with the current account's property access

Do not:

- commit real credential files
- assume the property identifier in `.env` is valid without checking `--list-sites`
- present inspection output as a full SEO audit or site-management capability

## Notes

- Do not commit real credentials.
- Prefer `GOOGLE_SERVICE_ACCOUNT_CREDENTIALS`.
- OAuth remains available as an alternative:
  - `GOOGLE_SEARCH_CONSOLE_OAUTH_CLIENT_CONFIG`
  - `GOOGLE_SEARCH_CONSOLE_OAUTH_CREDENTIALS`
- `analyze.py` is a diagnostic helper, not a complete SEO auditor.
- For validated capabilities and known limitations, see [references/current-status.md](references/current-status.md).
