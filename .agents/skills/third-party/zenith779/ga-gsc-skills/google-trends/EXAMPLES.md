# Google Trends Examples

These examples reflect what this repository actually supports today.

The primary examples are data-collection examples.
Radar examples are included only as advanced workflows.

## Example 1: Fetch The Latest US Trending List

User request:

```text
Fetch the latest Google Trends trending list as structured JSON.
```

Recommended command:

```powershell
google-trends/.venv/Scripts/python -c "from scripts.trends_us_list import collect_list; import json; print(json.dumps(collect_list(geo='US', time_range='7d'), ensure_ascii=False))"
```

What to look for:

- `items`
- `count`
- `period_applied`
- `applied_period_label`

## Example 2: Fetch Keyword Detail

User request:

```text
Get keyword detail for OpenAI in the US for the last 7 days.
```

Recommended command:

```powershell
google-trends/.venv/Scripts/python -c "from scripts.trends_keyword_detail import collect_keyword_detail; import json; print(json.dumps(collect_keyword_detail('OpenAI', geo='US', time_range='7d'), ensure_ascii=False))"
```

What to look for:

- `interest_over_time`
- `interest_summary`
- `related_queries`
- optional `limitation`

## Example 3: Verify Whether The Requested Period Applied

User request:

```text
Did the requested Google Trends period actually apply?
```

Check these fields:

```text
period_label
applied_period_label
period_applied
```

Interpretation:

- if `period_applied` is `true`, the browser path selected the expected period
- if `period_applied` is `false`, downstream logic should trust the applied value, not the requested one

## Example 4: Handle Partial Keyword Detail Gracefully

User request:

```text
Fetch keyword detail, but tell me if any endpoint degraded.
```

Recommended reading pattern:

```text
if limitation exists:
  treat payload as partial but still usable
else:
  treat payload as full live result
```

## Example 5: Advanced Radar Scan

User request:

```text
Run the repository's heuristic radar on current trends.
```

Recommended command:

```powershell
google-trends/.venv/Scripts/python google-trends/scripts/trends.py scan
```

Interpretation:

- `p1` means strongest heuristic signal
- `p2` means watchlist candidate
- `p3` means weak or dropped candidate

Important note:

- this is an advanced heuristic workflow layered on top of collected data
- it is not the core identity of the skill
