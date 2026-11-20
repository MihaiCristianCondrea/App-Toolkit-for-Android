#!/usr/bin/env python3
"""
Google Analytics Data Analysis Tool

Repository-specific wrapper:
- shares the project-level env workflow with ga_client.py
- supports --env-file
- only exposes analysis types currently implemented in this repo
"""

import argparse
import json
import sys
from typing import Dict, List, Optional

from ga_client import GoogleAnalyticsClient


class AnalyticsAnalyzer:
    """Performs analysis on Google Analytics data."""

    def __init__(self, env_file: Optional[str] = None):
        self.client = GoogleAnalyticsClient(env_file=env_file)

    def compare_periods(
        self, current_days: int = 30, metrics: Optional[List[str]] = None
    ) -> Dict:
        if metrics is None:
            metrics = [
                "sessions",
                "activeUsers",
                "newUsers",
                "bounceRate",
                "engagementRate",
                "averageSessionDuration",
            ]

        current = self.client.run_report(
            start_date=f"{current_days}daysAgo",
            end_date="yesterday",
            metrics=metrics,
            limit=1,
            include_totals=True,
        )
        previous = self.client.run_report(
            start_date=f"{current_days * 2}daysAgo",
            end_date=f"{current_days + 1}daysAgo",
            metrics=metrics,
            limit=1,
            include_totals=True,
        )

        comparison = {
            "current_period": f"Last {current_days} days",
            "previous_period": f"Previous {current_days} days",
            "metrics": {},
        }

        if current.get("totals") and previous.get("totals"):
            for i, metric in enumerate(metrics):
                current_val = float(current["totals"][i]["value"])
                previous_val = float(previous["totals"][i]["value"])
                change_pct = (
                    ((current_val - previous_val) / previous_val) * 100
                    if previous_val != 0
                    else 0
                )

                comparison["metrics"][metric] = {
                    "current": current_val,
                    "previous": previous_val,
                    "change": current_val - previous_val,
                    "change_percent": round(change_pct, 2),
                }

        comparison["insights"] = self._generate_insights(comparison["metrics"])
        return comparison

    def analyze_traffic_sources(self, days: int = 30, limit: int = 20) -> Dict:
        result = self.client.run_report(
            start_date=f"{days}daysAgo",
            end_date="yesterday",
            metrics=["sessions", "engagementRate", "bounceRate", "conversions"],
            dimensions=["sessionSource", "sessionMedium"],
            limit=limit,
            order_by="-sessions",
        )

        sources = []
        for row in result["rows"]:
            sessions = int(row["metrics"]["sessions"])
            conversions = int(row["metrics"].get("conversions", 0))
            conv_rate = (conversions / sessions * 100) if sessions > 0 else 0

            sources.append(
                {
                    "source": row["dimensions"]["sessionSource"],
                    "medium": row["dimensions"]["sessionMedium"],
                    "sessions": sessions,
                    "engagement_rate": round(
                        float(row["metrics"]["engagementRate"]) * 100, 2
                    ),
                    "bounce_rate": round(
                        float(row["metrics"]["bounceRate"]) * 100, 2
                    ),
                    "conversions": conversions,
                    "conversion_rate": round(conv_rate, 2),
                }
            )

        return {
            "period": f"Last {days} days",
            "sources": sources,
            "recommendations": self._recommend_source_optimizations(sources),
        }

    def analyze_content_performance(self, days: int = 30, limit: int = 50) -> Dict:
        result = self.client.run_report(
            start_date=f"{days}daysAgo",
            end_date="yesterday",
            metrics=[
                "screenPageViews",
                "bounceRate",
                "averageSessionDuration",
                "conversions",
            ],
            dimensions=["pagePath", "pageTitle"],
            limit=limit,
            order_by="-screenPageViews",
        )

        problem_pages = []
        for row in result["rows"]:
            views = int(row["metrics"]["screenPageViews"])
            bounce = float(row["metrics"]["bounceRate"])
            avg_duration = float(row["metrics"]["averageSessionDuration"])

            if bounce > 0.6 and views > 100:
                problem_pages.append(
                    {
                        "path": row["dimensions"]["pagePath"],
                        "title": row["dimensions"]["pageTitle"],
                        "views": views,
                        "bounce_rate": round(bounce * 100, 2),
                        "avg_duration": round(avg_duration, 2),
                        "issue": self._diagnose_page_issue(bounce, avg_duration),
                    }
                )

        return {
            "period": f"Last {days} days",
            "total_pages": result["row_count"],
            "high_bounce_pages": len(problem_pages),
            "problem_pages": problem_pages[:10],
            "recommendations": self._recommend_content_improvements(problem_pages),
        }

    def analyze_device_performance(self, days: int = 30) -> Dict:
        result = self.client.run_report(
            start_date=f"{days}daysAgo",
            end_date="yesterday",
            metrics=[
                "sessions",
                "bounceRate",
                "averageSessionDuration",
                "conversions",
                "engagementRate",
            ],
            dimensions=["deviceCategory"],
            limit=10,
            order_by="-sessions",
        )

        devices = []
        for row in result["rows"]:
            sessions = int(row["metrics"]["sessions"])
            conversions = int(row["metrics"].get("conversions", 0))
            conv_rate = (conversions / sessions * 100) if sessions > 0 else 0

            devices.append(
                {
                    "device": row["dimensions"]["deviceCategory"],
                    "sessions": sessions,
                    "bounce_rate": round(
                        float(row["metrics"]["bounceRate"]) * 100, 2
                    ),
                    "avg_duration": round(
                        float(row["metrics"]["averageSessionDuration"]), 2
                    ),
                    "conversion_rate": round(conv_rate, 2),
                    "engagement_rate": round(
                        float(row["metrics"]["engagementRate"]) * 100, 2
                    ),
                }
            )

        return {
            "period": f"Last {days} days",
            "devices": devices,
            "recommendations": self._recommend_device_optimizations(devices),
        }

    def _generate_insights(self, metrics: Dict) -> List[str]:
        insights = []
        for metric, data in metrics.items():
            change_pct = data["change_percent"]
            if abs(change_pct) >= 5:
                direction = "increased" if change_pct > 0 else "decreased"
                insights.append(
                    f"{metric.replace('_', ' ').title()}: {direction} by {abs(change_pct):.1f}%"
                )
        return insights

    def _recommend_source_optimizations(self, sources: List[Dict]) -> List[Dict]:
        recommendations = []
        if not sources:
            return recommendations

        best_source = max(sources, key=lambda x: x["conversion_rate"])
        recommendations.append(
            {
                "priority": "HIGH",
                "action": f"Scale {best_source['source']}/{best_source['medium']}",
                "reason": f"Highest conversion rate ({best_source['conversion_rate']}%)",
                "expected_impact": "Increase overall conversions by 20-30%",
            }
        )

        for source in sources[:5]:
            if source["conversion_rate"] < 2.0 and source["sessions"] > 1000:
                recommendations.append(
                    {
                        "priority": "MEDIUM",
                        "action": f"Optimize {source['source']}/{source['medium']}",
                        "reason": f"High traffic ({source['sessions']} sessions) but low conversion ({source['conversion_rate']}%)",
                        "expected_impact": "Potential conversion rate improvement of 50-100%",
                    }
                )
        return recommendations

    def _recommend_content_improvements(self, problem_pages: List[Dict]) -> List[Dict]:
        recommendations = []
        if not problem_pages:
            return [
                {
                    "priority": "INFO",
                    "action": "Content performing well",
                    "reason": "No pages with critically high bounce rates",
                    "expected_impact": "Continue monitoring",
                }
            ]

        problem_pages.sort(key=lambda x: x["views"], reverse=True)
        for page in problem_pages[:3]:
            recommendations.append(
                {
                    "priority": "HIGH",
                    "action": f"Improve {page['path']}",
                    "reason": f"{page['issue']} ({page['bounce_rate']}% bounce rate)",
                    "expected_impact": "Reduce bounce rate by 20-30%",
                }
            )
        return recommendations

    def _recommend_device_optimizations(self, devices: List[Dict]) -> List[Dict]:
        recommendations = []
        if len(devices) < 2:
            return recommendations

        mobile = next((d for d in devices if d["device"] == "mobile"), None)
        desktop = next((d for d in devices if d["device"] == "desktop"), None)

        if mobile and desktop and desktop["conversion_rate"] > 0:
            conv_diff = (
                (desktop["conversion_rate"] - mobile["conversion_rate"])
                / desktop["conversion_rate"]
                * 100
            )
            if conv_diff > 30:
                recommendations.append(
                    {
                        "priority": "CRITICAL",
                        "action": "Mobile experience optimization",
                        "reason": f"Mobile conversion rate {mobile['conversion_rate']}% vs desktop {desktop['conversion_rate']}%",
                        "expected_impact": "Improve mobile conversion by 30-50%",
                    }
                )
        return recommendations

    def _diagnose_page_issue(self, bounce_rate: float, avg_duration: float) -> str:
        if bounce_rate > 0.7 and avg_duration < 30:
            return "Content mismatch - users leave quickly"
        if bounce_rate > 0.6 and avg_duration > 60:
            return "Missing CTA - users read but don't act"
        if bounce_rate > 0.6:
            return "High bounce - needs investigation"
        return "Performance issue"


def main():
    parser = argparse.ArgumentParser(
        description="Analyze Google Analytics data",
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )
    parser.add_argument(
        "--env-file",
        help=(
            "Path to the env file. Defaults to the discovered .env from the current "
            "working directory chain first, then the home directory chain."
        ),
    )
    parser.add_argument(
        "--analysis-type",
        choices=["overview", "sources", "content", "devices"],
        default="overview",
        help="Analysis type.",
    )
    parser.add_argument("--days", type=int, default=30, help="Lookback window in days.")
    parser.add_argument("--compare", action="store_true", help="Compare with the previous period in overview mode.")
    parser.add_argument("--output", help="Output file path.")

    args = parser.parse_args()

    try:
        analyzer = AnalyticsAnalyzer(env_file=args.env_file)

        if args.analysis_type == "overview" and args.compare:
            result = analyzer.compare_periods(current_days=args.days)
        elif args.analysis_type == "sources":
            result = analyzer.analyze_traffic_sources(days=args.days)
        elif args.analysis_type == "content":
            result = analyzer.analyze_content_performance(days=args.days)
        elif args.analysis_type == "devices":
            result = analyzer.analyze_device_performance(days=args.days)
        else:
            result = analyzer.compare_periods(current_days=args.days)

        output = json.dumps(result, indent=2)
        if args.output:
            with open(args.output, "w", encoding="utf-8", newline="\n") as f:
                f.write(output)
            print(f"Analysis saved to {args.output}", file=sys.stderr)
        else:
            print(output)
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()
