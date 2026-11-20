#!/usr/bin/env python3
"""
Google Search Console Analysis Tool

Repository-specific wrapper:
- shares the project-level env workflow with gsc_client.py
- supports overview / queries / pages / countries / devices analysis modes
"""

from __future__ import annotations

import argparse
import json
import sys
from datetime import date, timedelta
from pathlib import Path
from typing import Dict, List, Optional


SCRIPT_DIR = Path(__file__).resolve().parent
if str(SCRIPT_DIR) not in sys.path:
    sys.path.insert(0, str(SCRIPT_DIR))

from gsc_client import SearchConsoleClient, parse_date_range


def previous_period_range(days: int) -> tuple[str, str]:
    current_start, _ = parse_date_range(days=days, start=None, end=None)
    current_start_date = date.fromisoformat(current_start)
    previous_end = current_start_date - timedelta(days=1)
    previous_start = previous_end - timedelta(days=days - 1)
    return previous_start.isoformat(), previous_end.isoformat()


def metric_value(row: Dict, key: str) -> float:
    value = row.get(key, 0)
    return float(value or 0)


def row_totals(rows: List[Dict]) -> Dict[str, float]:
    if not rows:
        return {"clicks": 0.0, "impressions": 0.0, "ctr": 0.0, "position": 0.0}

    first = rows[0]
    if {"clicks", "impressions", "ctr", "position"}.issubset(first.keys()):
        return {
            "clicks": metric_value(first, "clicks"),
            "impressions": metric_value(first, "impressions"),
            "ctr": metric_value(first, "ctr"),
            "position": metric_value(first, "position"),
        }

    impressions = sum(metric_value(row, "impressions") for row in rows)
    clicks = sum(metric_value(row, "clicks") for row in rows)
    weighted_position = sum(
        metric_value(row, "position") * metric_value(row, "impressions") for row in rows
    )
    ctr = (clicks / impressions) if impressions else 0.0
    position = (weighted_position / impressions) if impressions else 0.0
    return {
        "clicks": clicks,
        "impressions": impressions,
        "ctr": ctr,
        "position": position,
    }


def build_metric_summary(current: Dict[str, float], previous: Dict[str, float]) -> Dict:
    summary = {}
    for metric in ["clicks", "impressions", "ctr", "position"]:
        current_value = float(current.get(metric, 0) or 0)
        previous_value = float(previous.get(metric, 0) or 0)
        change = current_value - previous_value
        change_percent = (change / previous_value * 100) if previous_value else 0
        summary[metric] = {
            "current": round(current_value, 4),
            "previous": round(previous_value, 4),
            "change": round(change, 4),
            "change_percent": round(change_percent, 2),
        }
    return summary


def generate_overview_insights(summary: Dict) -> List[str]:
    insights = []
    labels = {
        "clicks": "Clicks",
        "impressions": "Impressions",
        "ctr": "CTR",
    }

    for metric in ["clicks", "impressions", "ctr"]:
        change_percent = summary.get(metric, {}).get("change_percent", 0)
        if abs(change_percent) < 5:
            continue
        direction = "increased" if change_percent > 0 else "decreased"
        insights.append(
            f"{labels[metric]} {direction} by {abs(change_percent):.1f}%"
        )

    position_change = summary.get("position", {}).get("change", 0)
    if position_change >= 0.5:
        insights.append(f"Average position worsened by {position_change:.2f}")
    elif position_change <= -0.5:
        insights.append(f"Average position improved by {abs(position_change):.2f}")

    return insights


def find_query_opportunities(rows: List[Dict]) -> List[Dict]:
    opportunities = []
    for row in rows:
        impressions = metric_value(row, "impressions")
        ctr = metric_value(row, "ctr")
        position = metric_value(row, "position")
        if impressions >= 1000 and ctr < 0.03 and 3 <= position <= 15:
            opportunities.append(
                {
                    "priority": "HIGH",
                    "query": row.get("query", ""),
                    "clicks": metric_value(row, "clicks"),
                    "impressions": impressions,
                    "ctr": round(ctr * 100, 2),
                    "position": round(position, 2),
                    "reason": "High impressions but weak CTR. Rewrite the title and description first, then improve search-intent alignment.",
                }
            )
    return opportunities


def find_page_opportunities(rows: List[Dict]) -> List[Dict]:
    opportunities = []
    for row in rows:
        impressions = metric_value(row, "impressions")
        ctr = metric_value(row, "ctr")
        position = metric_value(row, "position")
        if impressions >= 1000 and ctr < 0.04 and 5 <= position <= 15:
            opportunities.append(
                {
                    "priority": "HIGH",
                    "page": row.get("page", ""),
                    "clicks": metric_value(row, "clicks"),
                    "impressions": impressions,
                    "ctr": round(ctr * 100, 2),
                    "position": round(position, 2),
                    "reason": "The page is already close to page one. Improve the title, description, internal linking, and content structure first.",
                }
            )
    return opportunities


class SearchConsoleAnalyzer:
    """Performs analysis on Google Search Console data."""

    def __init__(self, env_file: Optional[str] = None, site_url: Optional[str] = None):
        self.client = SearchConsoleClient(env_file=env_file, site_url=site_url)

    def compare_periods(self, current_days: int = 28, search_type: str = "web") -> Dict:
        current_start, current_end = parse_date_range(
            days=current_days,
            start=None,
            end=None,
        )
        previous_start, previous_end = previous_period_range(current_days)

        current = self.client.run_report(
            start_date=current_start,
            end_date=current_end,
            search_type=search_type,
            limit=1,
        )
        previous = self.client.run_report(
            start_date=previous_start,
            end_date=previous_end,
            search_type=search_type,
            limit=1,
        )

        current_totals = row_totals(current["rows"])
        previous_totals = row_totals(previous["rows"])
        metrics = build_metric_summary(current_totals, previous_totals)

        return {
            "current_period": f"Last {current_days} days",
            "previous_period": f"Previous {current_days} days",
            "metrics": metrics,
            "insights": generate_overview_insights(metrics),
        }

    def analyze_queries(self, days: int = 28, limit: int = 1000, search_type: str = "web") -> Dict:
        start_date, end_date = parse_date_range(days=days, start=None, end=None)
        report = self.client.run_report(
            start_date=start_date,
            end_date=end_date,
            dimensions=["query"],
            search_type=search_type,
            limit=limit,
        )
        rows = report["rows"]
        top_queries = sorted(rows, key=lambda row: metric_value(row, "clicks"), reverse=True)[:20]

        return {
            "period": f"Last {days} days",
            "top_queries": top_queries,
            "opportunities": find_query_opportunities(rows)[:20],
        }

    def analyze_pages(self, days: int = 28, limit: int = 1000, search_type: str = "web") -> Dict:
        start_date, end_date = parse_date_range(days=days, start=None, end=None)
        report = self.client.run_report(
            start_date=start_date,
            end_date=end_date,
            dimensions=["page"],
            search_type=search_type,
            limit=limit,
        )
        rows = report["rows"]
        top_pages = sorted(rows, key=lambda row: metric_value(row, "clicks"), reverse=True)[:20]

        return {
            "period": f"Last {days} days",
            "top_pages": top_pages,
            "opportunities": find_page_opportunities(rows)[:20],
        }

    def analyze_countries(
        self, days: int = 28, limit: int = 100, search_type: str = "web"
    ) -> Dict:
        start_date, end_date = parse_date_range(days=days, start=None, end=None)
        report = self.client.run_report(
            start_date=start_date,
            end_date=end_date,
            dimensions=["country"],
            search_type=search_type,
            limit=limit,
        )
        countries = sorted(
            report["rows"],
            key=lambda row: metric_value(row, "clicks"),
            reverse=True,
        )[:20]
        return {"period": f"Last {days} days", "countries": countries}

    def analyze_devices(
        self, days: int = 28, limit: int = 100, search_type: str = "web"
    ) -> Dict:
        start_date, end_date = parse_date_range(days=days, start=None, end=None)
        report = self.client.run_report(
            start_date=start_date,
            end_date=end_date,
            dimensions=["device"],
            search_type=search_type,
            limit=limit,
        )
        devices = sorted(
            report["rows"],
            key=lambda row: metric_value(row, "clicks"),
            reverse=True,
        )[:20]
        return {"period": f"Last {days} days", "devices": devices}


def main():
    parser = argparse.ArgumentParser(
        description="Analyze Google Search Console data",
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )
    parser.add_argument(
        "--env-file",
        help=(
            "Path to the env file. Defaults to the discovered .env from the current "
            "working directory chain first, then the home directory chain."
        ),
    )
    parser.add_argument("--site-url", help="Site URL or property identifier. Overrides env configuration.")
    parser.add_argument(
        "--analysis-type",
        choices=["overview", "queries", "pages", "countries", "devices"],
        default="overview",
        help="Analysis type.",
    )
    parser.add_argument("--days", type=int, default=28, help="Lookback window in days.")
    parser.add_argument("--limit", type=int, default=1000, help="Maximum number of rows to fetch.")
    parser.add_argument(
        "--search-type",
        choices=["web", "image", "video", "discover", "googleNews"],
        default="web",
        help="Search type.",
    )
    parser.add_argument("--output", help="Output file path.")

    args = parser.parse_args()

    try:
        analyzer = SearchConsoleAnalyzer(
            env_file=args.env_file,
            site_url=args.site_url,
        )

        if args.analysis_type == "queries":
            result = analyzer.analyze_queries(
                days=args.days,
                limit=args.limit,
                search_type=args.search_type,
            )
        elif args.analysis_type == "pages":
            result = analyzer.analyze_pages(
                days=args.days,
                limit=args.limit,
                search_type=args.search_type,
            )
        elif args.analysis_type == "countries":
            result = analyzer.analyze_countries(
                days=args.days,
                limit=args.limit,
                search_type=args.search_type,
            )
        elif args.analysis_type == "devices":
            result = analyzer.analyze_devices(
                days=args.days,
                limit=args.limit,
                search_type=args.search_type,
            )
        else:
            result = analyzer.compare_periods(
                current_days=args.days,
                search_type=args.search_type,
            )

        output = json.dumps(result, indent=2, ensure_ascii=False)
        if args.output:
            with open(args.output, "w", encoding="utf-8", newline="\n") as handle:
                handle.write(output)
            print(f"Analysis saved to {args.output}", file=sys.stderr)
        else:
            print(output)
    except Exception as exc:
        print(f"Error: {exc}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()
