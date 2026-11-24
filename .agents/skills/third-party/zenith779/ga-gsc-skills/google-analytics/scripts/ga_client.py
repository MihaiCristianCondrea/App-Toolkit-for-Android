#!/usr/bin/env python3
"""
Google Analytics 4 Data API Client

Repository-specific wrapper:
- supports project-level configuration via --env-file
- discovers .env from the current working directory chain first, then the home directory chain
"""

import argparse
import json
import os
import sys
from pathlib import Path
from typing import Dict, List, Optional

ROOT_DIR = Path(__file__).resolve().parents[2]
if str(ROOT_DIR) not in sys.path:
    sys.path.insert(0, str(ROOT_DIR))

from shared.env_loader import (
    discover_default_env_file as shared_discover_default_env_file,
    load_project_env as shared_load_project_env,
)

try:
    from google.analytics.data_v1beta import BetaAnalyticsDataClient
    from google.analytics.data_v1beta.types import (
        DateRange,
        Dimension,
        Filter,
        FilterExpression,
        Metric,
        MetricAggregation,
        OrderBy,
        RunReportRequest,
    )
except ImportError as e:
    print(f"Error: Required package not installed: {e}", file=sys.stderr)
    print(
        "Install with: pip install -r google-analytics/requirements.txt",
        file=sys.stderr,
    )
    sys.exit(1)

def discover_default_env_file() -> Path:
    return shared_discover_default_env_file()


DEFAULT_ENV_FILE = discover_default_env_file()


def load_project_env(env_file: Optional[str]) -> Path:
    """Load the project-level env file."""
    return shared_load_project_env(env_file, default_env_file=DEFAULT_ENV_FILE)


def first_env(*keys: str) -> Optional[str]:
    for key in keys:
        value = os.environ.get(key)
        if value:
            return value
    return None


class GoogleAnalyticsClient:
    """Client for interacting with Google Analytics 4 Data API."""

    def __init__(self, env_file: Optional[str] = None):
        self.env_path = load_project_env(env_file)

        self.property_id = os.environ.get("GOOGLE_ANALYTICS_PROPERTY_ID")
        if not self.property_id:
            raise ValueError(
                "GOOGLE_ANALYTICS_PROPERTY_ID is not set. "
                f"Provide it in {self.env_path} or in the current environment."
            )

        credentials_path = first_env(
            "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS",
            "GOOGLE_ANALYTICS_CREDENTIALS",
            "GOOGLE_APPLICATION_CREDENTIALS",
        )
        if not credentials_path:
            raise ValueError(
                "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS is not set. "
                f"Provide it in {self.env_path} or in the current environment. "
                "Legacy names GOOGLE_ANALYTICS_CREDENTIALS / "
                "GOOGLE_APPLICATION_CREDENTIALS are still supported."
            )

        credentials_file = Path(credentials_path).expanduser()
        if not credentials_file.exists():
            raise FileNotFoundError(
                f"Service account file not found: {credentials_file}"
            )

        # Map the repository-level name back to the standard Google SDK env var.
        os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = str(credentials_file)

        try:
            self.client = BetaAnalyticsDataClient()
        except Exception as e:
            raise RuntimeError(
                f"Failed to initialize Google Analytics client: {e}\n"
                "Confirm that the service account has Viewer access to the GA4 property."
            )

    def run_report(
        self,
        start_date: str,
        end_date: str,
        metrics: List[str],
        dimensions: Optional[List[str]] = None,
        limit: int = 10,
        order_by: Optional[str] = None,
        filter_expression: Optional[str] = None,
        include_totals: bool = False,
    ) -> Dict:
        request = RunReportRequest(
            property=f"properties/{self.property_id}",
            date_ranges=[DateRange(start_date=start_date, end_date=end_date)],
            metrics=[Metric(name=m) for m in metrics],
            dimensions=[Dimension(name=d) for d in (dimensions or [])],
            limit=limit,
        )

        if include_totals:
            request.metric_aggregations = [MetricAggregation.TOTAL]

        if order_by:
            desc = True
            if order_by.startswith("+"):
                desc = False
                order_by = order_by[1:]
            elif order_by.startswith("-"):
                order_by = order_by[1:]

            if order_by in metrics:
                request.order_bys = [
                    OrderBy(
                        metric=OrderBy.MetricOrderBy(metric_name=order_by),
                        desc=desc,
                    )
                ]
            elif dimensions and order_by in dimensions:
                request.order_bys = [
                    OrderBy(
                        dimension=OrderBy.DimensionOrderBy(dimension_name=order_by),
                        desc=desc,
                    )
                ]

        if filter_expression and ":" in filter_expression:
            field_name, value = filter_expression.split(":", 1)
            request.dimension_filter = FilterExpression(
                filter=Filter(
                    field_name=field_name,
                    string_filter=Filter.StringFilter(value=value),
                )
            )

        try:
            response = self.client.run_report(request)
        except Exception as e:
            raise RuntimeError(f"Failed to run report: {e}")

        return self._parse_response(response)

    def _parse_response(self, response) -> Dict:
        result = {
            "dimension_headers": [h.name for h in response.dimension_headers],
            "metric_headers": [
                {"name": h.name, "type": h.type_.name} for h in response.metric_headers
            ],
            "rows": [],
            "row_count": response.row_count,
            "metadata": {},
        }

        if response.totals:
            result["totals"] = [
                {"value": v.value} for v in response.totals[0].metric_values
            ]

        for row in response.rows:
            parsed_row = {"dimensions": {}, "metrics": {}}

            for i, value in enumerate(row.dimension_values):
                dim_name = result["dimension_headers"][i]
                parsed_row["dimensions"][dim_name] = value.value

            for i, value in enumerate(row.metric_values):
                metric_info = result["metric_headers"][i]
                metric_name = metric_info["name"]
                parsed_row["metrics"][metric_name] = value.value

            result["rows"].append(parsed_row)

        return result


def parse_date_range(days: Optional[int], start: Optional[str], end: Optional[str]):
    if days:
        return f"{days}daysAgo", "yesterday"
    if start and end:
        return start, end
    return "7daysAgo", "yesterday"


def format_as_table(result: Dict) -> str:
    lines = []

    headers = result["dimension_headers"] + [m["name"] for m in result["metric_headers"]]
    header_line = " | ".join(headers)
    lines.append(header_line)
    lines.append("-" * len(header_line))

    for row in result["rows"]:
        values = []
        for dim in result["dimension_headers"]:
            values.append(row["dimensions"].get(dim, ""))
        for metric in result["metric_headers"]:
            values.append(row["metrics"].get(metric["name"], ""))
        lines.append(" | ".join(values))

    if "totals" in result:
        lines.append("-" * len(header_line))
        total_values = ["TOTAL"] + [""] * (len(result["dimension_headers"]) - 1)
        total_values += [t["value"] for t in result["totals"]]
        lines.append(" | ".join(total_values))

    lines.append("")
    lines.append(f"Total rows: {result['row_count']}")
    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser(
        description="Fetch Google Analytics 4 data",
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )

    parser.add_argument(
        "--env-file",
        help=(
            "Path to the env file. Defaults to the discovered .env from the current "
            f"working directory chain first, then the home directory chain (currently {DEFAULT_ENV_FILE})."
        ),
    )

    date_group = parser.add_mutually_exclusive_group()
    date_group.add_argument("--days", type=int, help="Lookback window in days, for example 30.")
    date_group.add_argument("--start", help="Start date in YYYY-MM-DD format.")
    parser.add_argument("--end", help="End date in YYYY-MM-DD / today / yesterday format.")

    parser.add_argument(
        "--metrics",
        required=True,
        help="Comma-separated metrics, for example sessions,activeUsers,bounceRate.",
    )
    parser.add_argument(
        "--dimensions",
        help="Comma-separated dimensions, for example country,deviceCategory.",
    )
    parser.add_argument("--limit", type=int, default=10, help="Maximum number of rows to return.")
    parser.add_argument(
        "--order-by",
        help="Sort field. Prefix with - for descending or + for ascending.",
    )
    parser.add_argument("--filter", help="Filter expression, for example country:United States.")
    parser.add_argument(
        "--format",
        choices=["json", "table"],
        default="json",
        help="Output format.",
    )
    parser.add_argument("--output", help="Output file path.")

    args = parser.parse_args()

    try:
        client = GoogleAnalyticsClient(env_file=args.env_file)
        start_date, end_date = parse_date_range(args.days, args.start, args.end)

        metrics = [m.strip() for m in args.metrics.split(",")]
        dimensions = (
            [d.strip() for d in args.dimensions.split(",")] if args.dimensions else None
        )

        result = client.run_report(
            start_date=start_date,
            end_date=end_date,
            metrics=metrics,
            dimensions=dimensions,
            limit=args.limit,
            order_by=args.order_by,
            filter_expression=args.filter,
        )

        output = json.dumps(result, indent=2) if args.format == "json" else format_as_table(result)

        if args.output:
            with open(args.output, "w", encoding="utf-8", newline="\n") as f:
                f.write(output)
            print(f"Report saved to {args.output}", file=sys.stderr)
        else:
            print(output)
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()
