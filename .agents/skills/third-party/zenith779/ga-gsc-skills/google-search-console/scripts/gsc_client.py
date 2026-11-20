#!/usr/bin/env python3
"""
Google Search Console API Client

Repository-specific wrapper:
- supports project-level configuration via --env-file
- discovers .env from the current working directory chain first, then the home directory chain
- prefers the shared service-account path while keeping OAuth2 and legacy variable compatibility
"""

from __future__ import annotations

import argparse
import csv
import io
import json
import os
import sys
from datetime import date, timedelta
from pathlib import Path
from typing import Dict, Iterable, List, Optional

ROOT_DIR = Path(__file__).resolve().parents[2]
if str(ROOT_DIR) not in sys.path:
    sys.path.insert(0, str(ROOT_DIR))

from shared.env_loader import (
    discover_default_env_file as shared_discover_default_env_file,
    load_project_env as shared_load_project_env,
)


SEARCH_TYPES = ["web", "image", "video", "discover", "googleNews"]
DATA_STATES = ["final", "all"]
DEFAULT_METRICS = ["clicks", "impressions", "ctr", "position"]
FILTER_OPERATORS = {
    "equals",
    "contains",
    "notContains",
    "notEquals",
    "includingRegex",
    "excludingRegex",
}


def discover_default_env_file() -> Path:
    return shared_discover_default_env_file()


DEFAULT_ENV_FILE = discover_default_env_file()


def load_project_env(env_file: Optional[str]) -> Path:
    return shared_load_project_env(env_file, default_env_file=DEFAULT_ENV_FILE)


def first_env(*keys: str) -> Optional[str]:
    for key in keys:
        value = os.environ.get(key)
        if value:
            return value
    return None


def looks_like_service_account_file(path: Optional[str]) -> bool:
    if not path:
        return False

    try:
        payload = json.loads(Path(path).expanduser().read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError):
        return False

    return payload.get("type") == "service_account"


def parse_date_range(days: Optional[int], start: Optional[str], end: Optional[str]):
    if days:
        if days <= 0:
            raise ValueError("--days must be a positive integer")
        yesterday = date.today() - timedelta(days=1)
        start_date = yesterday - timedelta(days=days - 1)
        return start_date.isoformat(), "yesterday"
    if start:
        return start, end or "yesterday"
    if end:
        raise ValueError("Providing --end without --start is ambiguous. Pass --start as well or use --days.")
    default_start = date.today() - timedelta(days=7)
    return default_start.isoformat(), "yesterday"


def parse_filter(raw: str) -> Dict[str, str]:
    parts = raw.split(":")
    if len(parts) < 2:
        raise ValueError(
            "Invalid filter format. Use dimension:expression or dimension:operator:expression."
        )

    dimension = parts[0]
    operator = "equals"

    if len(parts) >= 3 and parts[1] in FILTER_OPERATORS:
        operator = parts[1]
        expression = ":".join(parts[2:])
    else:
        expression = ":".join(parts[1:])

    if not dimension or not expression:
        raise ValueError("Filter dimension and expression must both be non-empty.")

    return {
        "dimension": dimension,
        "operator": operator,
        "expression": expression,
    }


def parse_filters(raw_filters: Optional[Iterable[str]]) -> List[Dict[str, str]]:
    return [parse_filter(item) for item in (raw_filters or [])]


def coerce_value(value):
    if isinstance(value, (int, float)):
        return value
    if isinstance(value, str):
        try:
            number = float(value)
        except ValueError:
            return value
        if number.is_integer():
            return int(number)
        return round(number, 6)
    return value


def build_report_dict(
    report,
    site_url: str,
    start_date: str,
    end_date: str,
    dimensions: Optional[List[str]],
    search_type: str,
    data_state: str,
) -> Dict:
    dimension_list = dimensions or []
    rows = []
    metrics = []

    for row in getattr(report, "rows", []):
        raw = row._asdict() if hasattr(row, "_asdict") else dict(row)
        if not metrics:
            metrics = [key for key in raw.keys() if key not in dimension_list]
        rows.append({key: coerce_value(value) for key, value in raw.items()})

    if not metrics:
        metrics = [metric for metric in DEFAULT_METRICS if metric not in dimension_list]

    return {
        "site_url": site_url,
        "date_range": {"start": start_date, "end": end_date},
        "dimensions": dimension_list,
        "metrics": metrics,
        "search_type": search_type,
        "data_state": data_state,
        "rows": rows,
        "row_count": len(rows),
    }


def build_inspection_dict(
    response: Dict,
    site_url: str,
    inspection_url: str,
    language_code: str,
) -> Dict:
    return {
        "site_url": site_url,
        "inspection_url": inspection_url,
        "language_code": language_code,
        "inspection_result": response.get("inspectionResult", {}),
    }


def format_as_table(result: Dict) -> str:
    headers = result["dimensions"] + result["metrics"]
    header_line = " | ".join(headers)
    lines = [header_line, "-" * len(header_line)]

    for row in result["rows"]:
        values = [str(row.get(column, "")) for column in headers]
        lines.append(" | ".join(values))

    lines.append("")
    lines.append(f"Total rows: {result['row_count']}")
    return "\n".join(lines)


def format_inspection_as_table(result: Dict) -> str:
    inspection = result.get("inspection_result", {})
    index_status = inspection.get("indexStatusResult", {})
    mobile = inspection.get("mobileUsabilityResult", {})
    rich_results = inspection.get("richResultsResult", {})

    lines = [
        f"Inspection URL: {result.get('inspection_url', '')}",
        f"Site URL: {result.get('site_url', '')}",
        f"Language Code: {result.get('language_code', '')}",
    ]

    if index_status:
        lines.extend(
            [
                f"Index verdict: {index_status.get('verdict', '')}",
                f"Coverage state: {index_status.get('coverageState', '')}",
                f"Indexing state: {index_status.get('indexingState', '')}",
                f"Robots.txt state: {index_status.get('robotsTxtState', '')}",
                f"Page fetch state: {index_status.get('pageFetchState', '')}",
            ]
        )
        if index_status.get("lastCrawlTime"):
            lines.append(f"Last crawl time: {index_status['lastCrawlTime']}")
        if index_status.get("googleCanonical"):
            lines.append(f"Google canonical: {index_status['googleCanonical']}")
        if index_status.get("userCanonical"):
            lines.append(f"User canonical: {index_status['userCanonical']}")

    if mobile:
        lines.append(f"Mobile usability verdict: {mobile.get('verdict', '')}")

    if rich_results:
        lines.append(f"Rich results verdict: {rich_results.get('verdict', '')}")

    if inspection.get("inspectionResultLink"):
        lines.append(f"Inspection result link: {inspection['inspectionResultLink']}")

    return "\n".join(lines)


def format_as_csv(result: Dict) -> str:
    headers = result["dimensions"] + result["metrics"]
    buffer = io.StringIO()
    writer = csv.DictWriter(buffer, fieldnames=headers)
    writer.writeheader()
    for row in result["rows"]:
        writer.writerow({key: row.get(key, "") for key in headers})
    return buffer.getvalue()


class SearchConsoleClient:
    """Client for interacting with Google Search Console."""

    def __init__(self, env_file: Optional[str] = None, site_url: Optional[str] = None):
        self.env_path = load_project_env(env_file)
        self.site_url = site_url or first_env(
            "GOOGLE_SEARCH_CONSOLE_SITE_URL",
            "SEARCHCONSOLE_WEBPROPERTY_URI",
        )
        self.client_config = first_env(
            "GOOGLE_SEARCH_CONSOLE_OAUTH_CLIENT_CONFIG",
            "GOOGLE_SEARCH_CONSOLE_CLIENT_CONFIG",
            "SEARCHCONSOLE_CLIENT_CONFIG",
        )
        self.credentials = first_env(
            "GOOGLE_SEARCH_CONSOLE_OAUTH_CREDENTIALS",
            "GOOGLE_SEARCH_CONSOLE_CREDENTIALS",
            "SEARCHCONSOLE_CREDENTIALS",
        )
        self.service_account = first_env(
            "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS",
            "GOOGLE_SEARCH_CONSOLE_SERVICE_ACCOUNT",
            "SEARCHCONSOLE_SERVICE_ACCOUNT",
        )
        if not self.service_account and looks_like_service_account_file(self.credentials):
            self.service_account = self.credentials
            self.credentials = None
        self._account = None

    def _import_searchconsole(self):
        try:
            import searchconsole
        except ImportError as exc:
            raise RuntimeError(
                "Missing searchconsole dependency. Run: "
                "pip install -r google-search-console/requirements.txt"
            ) from exc
        return searchconsole

    def authenticate_account(
        self,
        serialize_credentials: Optional[str] = None,
        flow: str = "web",
    ):
        if self._account is not None:
            return self._account

        searchconsole = self._import_searchconsole()

        if self.service_account:
            self._account = searchconsole.authenticate(
                service_account=self.service_account
            )
        elif self.credentials:
            kwargs = {"credentials": self.credentials}
            if self.client_config:
                kwargs["client_config"] = self.client_config
            self._account = searchconsole.authenticate(**kwargs)
        elif self.client_config:
            self._account = searchconsole.authenticate(
                client_config=self.client_config,
                flow=flow,
            )
        else:
            raise ValueError(
                "Google Search Console authentication is not configured. "
                f"Provide one of the supported settings in {self.env_path} or in the environment: "
                "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS / "
                "GOOGLE_SEARCH_CONSOLE_OAUTH_CLIENT_CONFIG / "
                "GOOGLE_SEARCH_CONSOLE_OAUTH_CREDENTIALS / "
                "GOOGLE_SEARCH_CONSOLE_SERVICE_ACCOUNT. "
                "Legacy names GOOGLE_SEARCH_CONSOLE_CLIENT_CONFIG / "
                "GOOGLE_SEARCH_CONSOLE_CREDENTIALS are still supported."
            )

        if serialize_credentials:
            self._account.serialize_credentials(serialize_credentials)

        return self._account

    def list_sites(self) -> List[Dict[str, str]]:
        account = self.authenticate_account()
        return [
            {
                "site_url": property_.url,
                "permission": property_.permission,
            }
            for property_ in account.webproperties
        ]

    def _get_webproperty(self, site_url: Optional[str] = None):
        target_url = site_url or self.site_url
        if not target_url:
            raise ValueError(
                "GOOGLE_SEARCH_CONSOLE_SITE_URL is not set. Provide it via env or --site-url."
            )

        account = self.authenticate_account()
        webproperty = account[target_url]
        if webproperty is None:
            raise ValueError(f"Property not found under the current account: {target_url}")
        return webproperty

    def _build_search_console_service(self, credentials):
        try:
            from apiclient import discovery
        except ImportError as exc:
            raise RuntimeError(
                "Missing google-api-python-client dependency. Run: "
                "pip install -r google-search-console/requirements.txt"
            ) from exc

        return discovery.build(
            serviceName="searchconsole",
            version="v1",
            credentials=credentials,
            cache_discovery=False,
        )

    def run_report(
        self,
        start_date: str,
        end_date: str,
        dimensions: Optional[List[str]] = None,
        filters: Optional[List[Dict[str, str]]] = None,
        search_type: str = "web",
        data_state: str = "final",
        limit: int = 1000,
        offset: int = 0,
        site_url: Optional[str] = None,
    ) -> Dict:
        if search_type not in SEARCH_TYPES:
            raise ValueError(f"Unsupported search type: {search_type}")
        if data_state not in DATA_STATES:
            raise ValueError(f"Unsupported data_state: {data_state}")

        webproperty = self._get_webproperty(site_url=site_url)
        query = webproperty.query.range(start_date, end_date)

        if dimensions:
            query = query.dimension(*dimensions)
        if search_type:
            query = query.search_type(search_type)
        if data_state:
            query = query.data_state(data_state)
        for filter_item in filters or []:
            query = query.filter(
                filter_item["dimension"],
                filter_item["expression"],
                filter_item["operator"],
            )

        query = query.limit(limit, offset) if offset else query.limit(limit)
        report = query.get()

        return build_report_dict(
            report=report,
            site_url=webproperty.url,
            start_date=start_date,
            end_date=end_date,
            dimensions=dimensions,
            search_type=search_type,
            data_state=data_state,
        )

    def inspect_url(
        self,
        inspection_url: str,
        language_code: str = "en-US",
        site_url: Optional[str] = None,
        flow: str = "web",
    ) -> Dict:
        if not inspection_url:
            raise ValueError("Inspection URL is required.")

        webproperty = self._get_webproperty(site_url=site_url)
        account = self.authenticate_account(flow=flow)
        service = self._build_search_console_service(
            credentials=account.credentials._credentials
        )
        response = (
            service.urlInspection()
            .index()
            .inspect(
                body={
                    "inspectionUrl": inspection_url,
                    "siteUrl": webproperty.url,
                    "languageCode": language_code,
                }
            )
            .execute()
        )

        return build_inspection_dict(
            response=response,
            site_url=webproperty.url,
            inspection_url=inspection_url,
            language_code=language_code,
        )


def write_output(output: str, output_path: Optional[str]):
    if output_path:
        with open(output_path, "w", encoding="utf-8", newline="\n") as handle:
            handle.write(output)
        print(f"Report saved to {output_path}", file=sys.stderr)
    else:
        print(output)


def main():
    parser = argparse.ArgumentParser(
        description="Fetch Google Search Console data",
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )
    parser.add_argument(
        "--env-file",
        help=(
            "Path to the env file. Defaults to the discovered .env from the current "
            f"working directory chain first, then the home directory chain (currently {DEFAULT_ENV_FILE})."
        ),
    )
    parser.add_argument("--site-url", help="Site URL or property identifier. Overrides env configuration.")
    parser.add_argument(
        "--inspect-url",
        help="Inspect a specific URL via the Search Console URL Inspection API.",
    )
    parser.add_argument(
        "--list-sites",
        action="store_true",
        help="List Search Console properties available to the current account.",
    )
    parser.add_argument(
        "--serialize-credentials",
        help="Serialize OAuth credentials to the specified path after the initial login flow.",
    )
    parser.add_argument(
        "--flow",
        choices=["web", "console"],
        default="web",
        help="OAuth flow type.",
    )
    parser.add_argument(
        "--language-code",
        default="en-US",
        help="Language code for URL inspection requests. Defaults to en-US.",
    )

    date_group = parser.add_mutually_exclusive_group()
    date_group.add_argument("--days", type=int, help="Lookback window in days, for example 28.")
    date_group.add_argument("--start", help="Start date in YYYY-MM-DD format.")
    parser.add_argument("--end", help="End date in YYYY-MM-DD / today / yesterday format.")

    parser.add_argument(
        "--dimensions",
        help="Comma-separated dimensions, for example query,page,country,device,date.",
    )
    parser.add_argument(
        "--filter",
        action="append",
        dest="filters",
        help="Filter expression. Repeatable. Format: dimension:expression or dimension:operator:expression.",
    )
    parser.add_argument(
        "--search-type",
        choices=SEARCH_TYPES,
        default="web",
        help="Search type.",
    )
    parser.add_argument(
        "--data-state",
        choices=DATA_STATES,
        default="final",
        help="Data state. Use all to include fresher but less finalized data.",
    )
    parser.add_argument("--limit", type=int, default=1000, help="Maximum number of rows to return.")
    parser.add_argument("--offset", type=int, default=0, help="Starting row offset.")
    parser.add_argument(
        "--format",
        choices=["json", "table", "csv"],
        default="json",
        help="Output format.",
    )
    parser.add_argument("--output", help="Output file path.")

    args = parser.parse_args()

    try:
        client = SearchConsoleClient(env_file=args.env_file, site_url=args.site_url)
        if args.list_sites:
            output = json.dumps(client.list_sites(), indent=2, ensure_ascii=False)
            write_output(output, args.output)
            return

        if args.inspect_url:
            if args.format == "csv":
                raise ValueError(
                    "CSV output is not supported for URL inspection. Use json or table."
                )

            if args.serialize_credentials:
                client.authenticate_account(
                    serialize_credentials=args.serialize_credentials,
                    flow=args.flow,
                )

            result = client.inspect_url(
                inspection_url=args.inspect_url,
                language_code=args.language_code,
                site_url=args.site_url,
                flow=args.flow,
            )
            if args.format == "table":
                output = format_inspection_as_table(result)
            else:
                output = json.dumps(result, indent=2, ensure_ascii=False)
            write_output(output, args.output)
            return

        start_date, end_date = parse_date_range(args.days, args.start, args.end)
        dimensions = (
            [item.strip() for item in args.dimensions.split(",")]
            if args.dimensions
            else None
        )
        filters = parse_filters(args.filters)

        if args.serialize_credentials:
            client.authenticate_account(
                serialize_credentials=args.serialize_credentials,
                flow=args.flow,
            )

        result = client.run_report(
            start_date=start_date,
            end_date=end_date,
            dimensions=dimensions,
            filters=filters,
            search_type=args.search_type,
            data_state=args.data_state,
            limit=args.limit,
            offset=args.offset,
        )

        if args.format == "table":
            output = format_as_table(result)
        elif args.format == "csv":
            output = format_as_csv(result)
        else:
            output = json.dumps(result, indent=2, ensure_ascii=False)

        write_output(output, args.output)
    except Exception as exc:
        print(f"Error: {exc}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()
