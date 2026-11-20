---
name: google-trends
description: Use when collecting Google Trends list data, fetching keyword detail, checking applied time windows, or exporting structured Google Trends payloads for downstream analysis.
---

# Google Trends

Use this skill when the task is about collecting structured Google Trends data for downstream analysis, monitoring, or orchestration.

This repository currently focuses on two primary workflows:

- trending-list collection
- keyword-detail collection

Advanced workflow:

- optional radar scoring that classifies topics into `P1 / P2 / P3` and estimates site-building potential

This means the primary identity of the skill is **data collection**, not opportunity scoring.

For the currently validated implementation scope, see [references/current-status.md](references/current-status.md).

Further reading in this skill directory:

- [REFERENCE.md](REFERENCE.md) for entry points, output shapes, env conventions, and caveats
- [EXAMPLES.md](EXAMPLES.md) for copy-paste scenarios and answer patterns
- [references/current-status.md](references/current-status.md) for verified capabilities and current limitations

## When to Use

Use this skill when the user asks for things like:

- "fetch the latest Google Trends list"
- "collect Google Trends data as JSON"
- "get keyword detail for this query"
- "tell me whether the requested period actually applied"
- "return interest over time and related queries"
- "give me a trends payload that another tool can consume"

Do **not** use this skill when the user primarily needs:

- GA4 traffic or conversion analysis
- Search Console performance analysis
- a full business recommendation engine from trends data alone

## Prerequisites

This skill discovers `.env` from the current working directory chain first, then the home directory chain.
Start by copying [`.env.example`](../.env.example) to `.env` in your project directory or one of its parent directories.

Recommended variables:

```env
GOOGLE_TRENDS_PROXY=
GOOGLE_TRENDS_SOURCE_MODE=csv_only
```

Important setup notes:

- before the first CSV-based list collection, run `python -m playwright install chromium`
- this repository's Google Trends collectors do not require an official Google API credential
- no proxy is injected by default
- when a proxy is needed, set `GOOGLE_TRENDS_PROXY` to your own proxy URL in `.env`
- valid `GOOGLE_TRENDS_SOURCE_MODE` values are `csv_only`, `csv_plus_rss`, and `rss_only`

Install dependencies:

```bash
python -m venv google-trends/.venv
google-trends/.venv/Scripts/python -m ensurepip --upgrade
google-trends/.venv/Scripts/python -m pip install -r google-trends/requirements.txt
```

## Primary Entry Points

Fetch the US trending list and convert it to structured JSON:

```bash
google-trends/.venv/Scripts/python -c "from scripts.trends_us_list import collect_list; import json; print(json.dumps(collect_list(geo='US', time_range='7d'), ensure_ascii=False))"
```

Fetch keyword detail:

```bash
google-trends/.venv/Scripts/python -c "from scripts.trends_keyword_detail import collect_keyword_detail; import json; print(json.dumps(collect_keyword_detail('OpenAI', geo='US', time_range='7d'), ensure_ascii=False))"
```

## Advanced Entry Point

Run the radar workflow:

```bash
google-trends/.venv/Scripts/python google-trends/scripts/trends.py scan
```

Use this only when you explicitly want the repository's heuristics for:

- `P1 / P2 / P3`
- buildability judgment
- watchlist behavior
- opportunity-angle expansion

## Output Contract

### List collection

Expected payload characteristics:

- structured JSON
- list-level metadata such as `geo`, `time_range`, `period_label`, `applied_period_label`
- `period_applied` flag
- `items` array with normalized keyword rows

### Keyword detail collection

Expected payload characteristics:

- normalized `keyword`
- `translation_zh`
- `interest_over_time`
- `interest_summary`
- `related_queries`
- optional `limitation` when live collection degrades softly

### Advanced radar workflow

Expected payload characteristics:

- `stats`
- `p1`
- `p2`
- `p3`
- repository-specific heuristics and action labels

## Common Failure Modes

### Time Window Mismatch

Typical symptoms:

- the requested period does not actually take effect

Checks:

- read `period_applied`
- compare `period_label` with `applied_period_label`

### Endpoint Degradation

Typical symptoms:

- `related_queries` missing
- partial keyword detail payloads
- `limitation` appears in the result

Checks:

- treat `limitation` as part of the output contract
- do not assume every endpoint returns complete live data

### Proxy / Browser Issues

Typical symptoms:

- Playwright download failures
- proxy tunnel errors

Checks:

- set `GOOGLE_TRENDS_PROXY` explicitly when needed
- verify Playwright Chromium is installed

## Trust Boundary

Current repository model:

- Google Trends data is collected through browser automation and direct endpoint calls
- proxy behavior is explicit rather than inherited from generic host proxy env vars
- advanced radar judgments are repository heuristics, not authoritative market truth

Do not:

- present radar output as objective business validation
- assume `related_queries` completeness under rate limits
- treat one Trends snapshot as stable demand evidence
