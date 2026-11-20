# Google Analytics Current Status

## Code Layout

- `scripts/ga_client.py`: GA4 Data API report CLI with metrics, dimensions, filters, sorting, and table output
- `scripts/analyze.py`: lightweight GA4 analysis CLI for `overview / sources / content / devices`
- `tests/`: current regression tests

## Verified Capabilities

- supports project-level `.env` loading and `--env-file`
- supports GA4 Property + service account authentication
- can query live GA4 data through `ga_client.py`
- can compare the current period with the previous period through `analyze.py --analysis-type overview --compare`
- `ga_client.py --help` and `analyze.py --help` run successfully
- fixed the previous default `.env` resolution issue that incorrectly pointed to `D:\.env`
- live GA4 integration has been validated with a minimal report and an overview comparison run

## Example Commands

- `google-analytics/.venv/Scripts/python google-analytics/scripts/ga_client.py --env-file .env --days 7 --metrics sessions,activeUsers,bounceRate --limit 1 --format json`
- `google-analytics/.venv/Scripts/python google-analytics/scripts/analyze.py --env-file .env --analysis-type overview --days 7 --compare`

## Current Conventions

- recommended interpreter: `google-analytics/.venv/Scripts/python`
- `.env` needs at least:
  - `GOOGLE_SERVICE_ACCOUNT_CREDENTIALS`
  - `GOOGLE_ANALYTICS_PROPERTY_ID`
- keep the service account file outside the repository
- the same service account can be granted access to both GA4 and GSC

## Current Limitations

- current tests mainly cover helper logic and env resolution, not live API responses
- `analyze.py` is still a quick diagnosis tool, not a full attribution analyzer

## Related Files

- environment example: `../../.env.example`
- usage notes: `../SKILL.md`
- setup guide: `../../docs/google-service-account-setup.md`
