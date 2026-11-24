import importlib.util
import sys
import unittest
from pathlib import Path


MODULE_PATH = (
    Path(__file__).resolve().parents[1] / "scripts" / "analyze.py"
)


def load_module():
    spec = importlib.util.spec_from_file_location("analyze", MODULE_PATH)
    module = importlib.util.module_from_spec(spec)
    sys.modules["analyze"] = module
    spec.loader.exec_module(module)
    return module


class AnalyzeHelpersTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.module = load_module()

    def test_build_metric_summary_calculates_change_percent(self):
        summary = self.module.build_metric_summary(
            current={"clicks": 120, "impressions": 1000, "ctr": 0.12, "position": 6.5},
            previous={"clicks": 100, "impressions": 900, "ctr": 0.1, "position": 5.5},
        )

        self.assertEqual(20.0, summary["clicks"]["change_percent"])
        self.assertEqual(11.11, summary["impressions"]["change_percent"])
        self.assertEqual(20.0, summary["ctr"]["change_percent"])
        self.assertEqual(1.0, summary["position"]["change"])

    def test_generate_overview_insights_marks_position_regression(self):
        insights = self.module.generate_overview_insights(
            {
                "clicks": {"change_percent": -15.0, "change": -15},
                "impressions": {"change_percent": 3.0, "change": 30},
                "ctr": {"change_percent": -12.0, "change": -0.01},
                "position": {"change_percent": 18.18, "change": 1.0},
            }
        )

        self.assertTrue(any("Clicks decreased" in item for item in insights))
        self.assertTrue(any("CTR decreased" in item for item in insights))
        self.assertTrue(any("Average position worsened" in item for item in insights))

    def test_find_query_opportunities_flags_high_impression_low_ctr(self):
        rows = [
            {
                "query": "seo checklist",
                "clicks": 20,
                "impressions": 1200,
                "ctr": 0.016,
                "position": 5.8,
            },
            {
                "query": "brand keyword",
                "clicks": 100,
                "impressions": 500,
                "ctr": 0.2,
                "position": 1.2,
            },
        ]

        opportunities = self.module.find_query_opportunities(rows)

        self.assertEqual(1, len(opportunities))
        self.assertEqual("seo checklist", opportunities[0]["query"])
        self.assertEqual("HIGH", opportunities[0]["priority"])

    def test_find_page_opportunities_flags_pages_with_high_impressions_and_mid_positions(self):
        rows = [
            {
                "page": "/blog/technical-seo",
                "clicks": 30,
                "impressions": 2000,
                "ctr": 0.015,
                "position": 9.4,
            },
            {
                "page": "/pricing",
                "clicks": 80,
                "impressions": 400,
                "ctr": 0.2,
                "position": 2.1,
            },
        ]

        opportunities = self.module.find_page_opportunities(rows)

        self.assertEqual(1, len(opportunities))
        self.assertEqual("/blog/technical-seo", opportunities[0]["page"])
        self.assertIn("title", opportunities[0]["reason"].lower())

    def test_compare_periods_uses_gsc_compatible_dates(self):
        module = self.module

        class FakeClient:
            def __init__(self):
                self.calls = []

            def run_report(self, **kwargs):
                self.calls.append(kwargs)
                return {
                    "rows": [
                        {
                            "clicks": 10,
                            "impressions": 100,
                            "ctr": 0.1,
                            "position": 2.0,
                        }
                    ]
                }

        analyzer = module.SearchConsoleAnalyzer.__new__(module.SearchConsoleAnalyzer)
        analyzer.client = FakeClient()

        analyzer.compare_periods(current_days=7, search_type="web")

        first_call = analyzer.client.calls[0]
        second_call = analyzer.client.calls[1]
        self.assertEqual("yesterday", first_call["end_date"])
        self.assertRegex(first_call["start_date"], r"^\d{4}-\d{2}-\d{2}$")
        self.assertRegex(second_call["start_date"], r"^\d{4}-\d{2}-\d{2}$")
        self.assertRegex(second_call["end_date"], r"^\d{4}-\d{2}-\d{2}$")


if __name__ == "__main__":
    unittest.main()
