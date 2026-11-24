# Google Search Console Current Status

## Code Layout

- `scripts/gsc_client.py`: GSC report CLI with property listing, dimensional queries, filters, URL inspection, and `table/csv/json` output
- `scripts/analyze.py`: GSC analysis CLI for `overview / queries / pages / countries / devices`
- `tests/`: regression tests
- `references/`: maintenance notes and follow-up analysis context

## Verified Capabilities

- supports project-level `.env` loading and `--env-file`
- supports service account, OAuth client config, and saved credentials authentication paths
- can list properties available to the current account
- can query live Search Console data and output `json / table / csv`
- can compare the current period with the previous period
- can fetch `query / page / device / country` dimensions
- can inspect a specific URL through the URL Inspection API and output `json / table`
- live Search Console integration has been validated

## Example Commands

- `google-search-console/.venv/Scripts/python google-search-console/scripts/gsc_client.py --env-file .env --site-url sc-domain:example.com --days 7 --dimensions page --limit 1 --format json`
- `google-search-console/.venv/Scripts/python google-search-console/scripts/gsc_client.py --env-file .env --site-url sc-domain:example.com --inspect-url https://example.com/ --format json`

## Current Conventions

- recommended interpreter: `google-search-console/.venv/Scripts/python`
- prefer the shared variable `GOOGLE_SERVICE_ACCOUNT_CREDENTIALS`
- `.env` value `GOOGLE_SEARCH_CONSOLE_SITE_URL` must be:
  - `https://www.example.com/`
  - `sc-domain:example.com`
- not a GA property ID
- the property identifier must exactly match an accessible Search Console property; when the account exposes only a domain property, prefer `sc-domain:...`
- OAuth remains an alternative authentication path:
  - `GOOGLE_SEARCH_CONSOLE_OAUTH_CLIENT_CONFIG`
  - `GOOGLE_SEARCH_CONSOLE_OAUTH_CREDENTIALS`

## Current Limitations

- `analyze.py` is still a quick diagnostic tool, not a full SEO auditor
- combined breakdowns across device, country, and query still require manual `gsc_client.py` composition
- there is no dedicated command yet for analyses such as top mobile query/page declines
- URL inspection is currently limited to single-URL inspection only
- there is still no sitemap or site-management workflow

## Related Files

- environment example: `../../.env.example`
- usage notes: `../SKILL.md`
- setup guide: `../../docs/google-service-account-setup.md`
