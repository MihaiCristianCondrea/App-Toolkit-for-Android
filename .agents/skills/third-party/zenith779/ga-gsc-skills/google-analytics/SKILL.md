---
name: google-analytics
description: Use when analyzing Google Analytics 4 property data, comparing recent traffic periods, reviewing source or medium performance, diagnosing weak content engagement, or checking device-level conversion differences.
---

# Google Analytics

Use this skill when the task is about GA4 reporting or lightweight diagnosis for a property that the configured account can already access.

This repository currently focuses on practical GA4 reporting and quick analysis:

- recent traffic totals and trend comparison
- source / medium performance review
- content engagement diagnosis
- device performance comparison
- raw report output as `table` or `json`

It does **not** currently provide dedicated workflows for:

- full attribution modeling
- funnel exploration
- cohort or retention analysis
- advanced event debugging
- a full marketing analytics audit

For the currently validated implementation scope, see [references/current-status.md](references/current-status.md).

Further reading in this skill directory:

- [REFERENCE.md](REFERENCE.md) for the repo-supported GA4 fields, CLI syntax, and output contract
- [EXAMPLES.md](EXAMPLES.md) for copy-paste scenarios and answer patterns
- [references/current-status.md](references/current-status.md) for validated capabilities and live verification notes

## When to Use

Use this skill when the user asks for things like:

- "show GA4 traffic for the last 7 / 30 / 90 days"
- "compare this period with the previous one"
- "which channels or sources are underperforming?"
- "which pages have high bounce or weak engagement?"
- "is mobile converting worse than desktop?"
- "export a GA4 report as JSON"

Do **not** use this skill when the user primarily needs:

- Search Console query or page ranking analysis
- Google Trends topic discovery
- deep attribution or MMM-style analysis
- live debugging of GA instrumentation tags

## Prerequisites

This skill discovers `.env` from the current working directory chain first, then the home directory chain, and also supports `--env-file`.

Start by copying [`.env.example`](../.env.example) to `.env` in your project directory or one of its parent directories.

Recommended variables:

```env
GOOGLE_SERVICE_ACCOUNT_CREDENTIALS=C:/path/to/google-service-account.json
GOOGLE_ANALYTICS_PROPERTY_ID=123456789
```

Credential setup checklist:

- create or reuse a Google Cloud service account
- enable `Google Analytics Data API`
- download the JSON key to a local path outside this repository
- add the service account email to the target GA4 property with read access
- copy the numeric GA4 `Property ID` from `Admin` -> `Property details`

The same shared service account can be reused across this repository's Google skills.
For setup details, see [docs/google-service-account-setup.md](../docs/google-service-account-setup.md).

Install dependencies:

```bash
python -m venv google-analytics/.venv
google-analytics/.venv/Scripts/python -m ensurepip --upgrade
google-analytics/.venv/Scripts/python -m pip install -r google-analytics/requirements.txt
```

## Recommended Entry Points

Get a baseline report:

```bash
google-analytics/.venv/Scripts/python google-analytics/scripts/ga_client.py ^
  --env-file .env ^
  --days 30 ^
  --metrics sessions,activeUsers,bounceRate
```

Review source / medium performance:

```bash
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type sources ^
  --days 30
```

Review content engagement issues:

```bash
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type content ^
  --days 30
```

Compare the current period with the previous period:

```bash
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type overview ^
  --days 30 ^
  --compare
```

## Verified Commands

These command shapes have been validated against a real GA4 property in this repository:

```bash
google-analytics/.venv/Scripts/python google-analytics/scripts/ga_client.py ^
  --env-file .env ^
  --days 7 ^
  --metrics sessions,activeUsers,bounceRate ^
  --limit 1 ^
  --format json
```

```bash
google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py ^
  --env-file .env ^
  --analysis-type overview ^
  --days 7 ^
  --compare
```

For the environment-specific validation record, see [references/current-status.md](references/current-status.md).

## Common Requests

### 1. Traffic Overview

Goal:
- review sessions, active users, and bounce or engagement signals for a recent period
- compare against the previous period when trend direction matters

Recommended path:
- `analyze.py --analysis-type overview`
- or `ga_client.py --metrics sessions,activeUsers,bounceRate`

### 2. Source / Medium Review

Goal:
- identify traffic sources with strong volume but weak conversion
- find source / medium combinations worth further landing-page or campaign work

Recommended path:
- `analyze.py --analysis-type sources`

### 3. Content Diagnosis

Goal:
- find pages with high traffic but poor engagement quality
- review high-bounce or low-duration content candidates

Recommended path:
- `analyze.py --analysis-type content`

### 4. Device Comparison

Goal:
- compare mobile, desktop, and other device categories
- spot conversion or engagement gaps by device

Recommended path:
- `analyze.py --analysis-type devices`

## Output Contract

Current output modes:

- `table`: human-readable report output from `ga_client.py`
- `json`: structured output from `ga_client.py` and `analyze.py`

Current analysis coverage:

- `overview`
- `sources`
- `content`
- `devices`

For custom GA4 reports, prefer `ga_client.py` with explicit `--metrics` and optional `--dimensions`.

## Common Failure Modes

### Authentication or Access

Typical symptoms:

- credential load failures
- permission errors from the GA4 Data API
- empty or blocked responses for a valid property

Checks:

- verify `GOOGLE_SERVICE_ACCOUNT_CREDENTIALS`
- confirm the service account has access to the GA4 property
- confirm `GOOGLE_ANALYTICS_PROPERTY_ID` is the GA4 property ID

### Env Resolution or Property Configuration

Typical symptoms:

- wrong `.env` file being used
- missing property configuration
- commands running with incomplete settings

Checks:

- pass `--env-file` explicitly when using multiple env files
- ensure `.env` includes both required variables
- prefer a project-level `.env` discovered from the current working directory chain over scattered per-skill copies

### Analysis Scope Confusion

Typical symptoms:

- expecting attribution-grade answers from a quick diagnosis command
- expecting event-level or funnel-level explanations from aggregate reports

Checks:

- use `analyze.py` for fast directional diagnosis
- use `ga_client.py` when you need a more custom metric / dimension report
- treat current recommendations as heuristics, not a full analytics model

## Trust Boundary

Current repository model:

- credentials stay local and are loaded from `.env` plus a local file path
- the GA4 property is accessed directly through the Google SDK
- this implementation does not depend on a third-party OAuth broker

Do not:

- commit service account JSON files
- paste raw secrets or private analytics exports into the repository
- present heuristic output from `analyze.py` as authoritative causal analysis

## Notes

- Do not commit real credentials.
- Keep the service account JSON file outside the repository.
- Prefer `GOOGLE_SERVICE_ACCOUNT_CREDENTIALS`.
- The current scripts support project-level env loading from the current working directory chain first and do not depend on a global skill directory.
- `analyze.py` is a quick diagnosis helper, not a full attribution analyzer.
- For current capabilities and validated status, see [references/current-status.md](references/current-status.md).
