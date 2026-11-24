# Google Trends Current Status

## Code Layout

- `scripts/trends.py`: advanced radar workflow that scores trends and assigns `P1 / P2 / P3`
- `scripts/trends_us_list.py`: primary list-collection entry point for US trending CSV data
- `scripts/trends_keyword_detail.py`: primary keyword-detail collector for `interest_over_time` and `related_queries`
- `tests/`: regression tests
- `state/`: runtime state for the advanced radar workflow

## Verified Capabilities

- the US CSV download path is working
- CSV parsing, `translation_zh`, and period-label round-tripping are implemented
- keyword detail is connected to the live Explore API collector and degrades softly on failure
- the advanced radar workflow uses filtering, scoring, Chinese translation, and opportunity-angle expansion
- the main radar prefers US CSV by default and can fall back to RSS if CSV collection fails
- unit tests cover the main decision paths

## Example Commands

- `python -c "from trends_keyword_detail import collect_keyword_detail; import json; print(json.dumps(collect_keyword_detail('OpenAI', geo='US', time_range='7d'), ensure_ascii=False))"`

Expected result shape:

- returns normalized `keyword`
- returns a valid payload shape
- may return `limitation` instead of live detail rows when upstream endpoints are rate-limited

## Current Conventions

- the default config source is the repository root `.env`
- env names are:
  - `GOOGLE_TRENDS_PROXY`
  - `GOOGLE_TRENDS_SOURCE_MODE`
  - `GOOGLE_TRENDS_PROXY_SETTINGS`
- upstream orchestration should use `period_applied` to confirm whether the requested time window actually took effect
- upstream orchestration should read `limitation` to determine whether keyword detail returned live data or a degraded placeholder

## Current Limitations

- `trends_us_list.py` currently supports `US` only
- the Google Trends period selector is still unstable; rely on `period_applied`
- `collect_keyword_detail(...)` still depends on endpoints with variable rate limits
- `widgetdata/relatedsearches` can still return `429`
- the browser CSV collection path can currently fail in this environment with `RuntimeError: This event loop is already running`
- the Playwright browser-detail fallback can hit proxy tunnel failures in some environments
- radar output is heuristic and should not be treated as authoritative market validation

## Related Files

- skill entry: `../SKILL.md`
- reference: `../REFERENCE.md`
- examples: `../EXAMPLES.md`
- environment example: `../../.env.example`
