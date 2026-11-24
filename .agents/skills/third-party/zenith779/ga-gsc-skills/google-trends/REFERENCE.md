# Google Trends Reference

This reference is for the Google Trends capabilities currently exposed by this repository.

Use it when you need to remember:

- which entry point collects which kind of data
- what the output payloads look like
- which env vars control proxy and source behavior
- which parts are primary data collection versus advanced heuristics

## Quick Map

| Need | Entry Point | Notes |
|------|-------------|-------|
| Trending list collection | `collect_list(...)` in `scripts/trends_us_list.py` | Primary workflow |
| Keyword detail collection | `collect_keyword_detail(...)` in `scripts/trends_keyword_detail.py` | Primary workflow |
| Advanced scoring workflow | `python scripts/trends.py scan` | Secondary workflow with heuristics |
| Summary of latest radar run | `python scripts/trends.py summary` | Reads saved state |

## Implemented Entry Points

### `scripts/trends_us_list.py`

Primary purpose:

- download the US trending CSV through Playwright
- parse it into structured JSON rows

Current constraints:

- `collect_list(...)` currently supports `US` only
- browser automation requires Playwright Chromium

### `scripts/trends_keyword_detail.py`

Primary purpose:

- collect keyword-level detail from Google Trends
- return `interest_over_time` and `related_queries`

Current constraints:

- endpoint reliability varies
- `related_queries` can degrade under `429`

### `scripts/trends.py`

Advanced purpose:

- apply repository-specific filtering and scoring heuristics
- emit `P1 / P2 / P3`
- maintain watchlist and latest report state

This is not the primary identity of the skill.

## Output Shapes

### List collection payload

Expected top-level keys:

- `geo`
- `time_range`
- `count`
- `items`
- `download_path`
- `period_label`
- `applied_period_label`
- `period_applied`

### Keyword detail payload

Expected top-level keys:

- `keyword`
- `translation_zh`
- `geo`
- `time_range`
- `interest_over_time`
- `interest_summary`
- `related_queries`
- optional `limitation`

### Radar payload

Expected top-level keys:

- `generated_at`
- `errors`
- `stats`
- `p1`
- `p2`
- `p3`

## Proxy Conventions

Env names:

- `GOOGLE_TRENDS_PROXY`
- `GOOGLE_TRENDS_SOURCE_MODE`
- `GOOGLE_TRENDS_PROXY_SETTINGS`

Repository behavior:

- generic host `HTTP_PROXY` / `HTTPS_PROXY` are not the primary control surface
- explicit Google Trends proxy env vars are preferred

## Time Window Caveats

Important caveat:

- Google Trends period selection is not always trustworthy from input alone

Use:

- `period_label`
- `applied_period_label`
- `period_applied`

to verify what actually happened.

## Trust Boundary

Primary workflows:

- collect raw or lightly structured Google Trends data

Advanced workflow:

- apply repository heuristics for buildability and prioritization

Do not treat:

- `P1 / P2 / P3`
- risk labels
- recommended actions

as objective truth. They are heuristic outputs layered on top of collected data.
