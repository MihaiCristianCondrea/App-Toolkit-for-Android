import importlib.util
import sys
import unittest
from pathlib import Path
from unittest.mock import patch


GA_CLIENT_MODULE_PATH = Path(__file__).resolve().parents[1] / "scripts" / "ga_client.py"
ANALYZE_MODULE_PATH = Path(__file__).resolve().parents[1] / "scripts" / "analyze.py"


def load_modules():
    ga_spec = importlib.util.spec_from_file_location("ga_client", GA_CLIENT_MODULE_PATH)
    ga_module = importlib.util.module_from_spec(ga_spec)
    sys.modules["ga_client"] = ga_module
    ga_spec.loader.exec_module(ga_module)

    analyze_spec = importlib.util.spec_from_file_location("analyze", ANALYZE_MODULE_PATH)
    analyze_module = importlib.util.module_from_spec(analyze_spec)
    sys.modules["analyze"] = analyze_module
    analyze_spec.loader.exec_module(analyze_module)
    return analyze_module


class AnalyzeHelpersTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.module = load_modules()

    def test_compare_periods_populates_metrics_from_totals(self):
        current = {
            "totals": [
                {"value": "100"},
                {"value": "90"},
                {"value": "30"},
                {"value": "0.4"},
                {"value": "0.6"},
                {"value": "120"},
            ]
        }
        previous = {
            "totals": [
                {"value": "80"},
                {"value": "70"},
                {"value": "20"},
                {"value": "0.5"},
                {"value": "0.55"},
                {"value": "100"},
            ]
        }

        fake_client = type(
            "FakeClient",
            (),
            {
                "__init__": lambda self, env_file=None: None,
                "run_report": lambda self, **kwargs: current if kwargs["start_date"] == "7daysAgo" else previous,
            },
        )

        with patch.object(self.module, "GoogleAnalyticsClient", fake_client):
            analyzer = self.module.AnalyticsAnalyzer()
            result = analyzer.compare_periods(current_days=7)

        self.assertIn("sessions", result["metrics"])
        self.assertEqual(100.0, result["metrics"]["sessions"]["current"])
        self.assertEqual(80.0, result["metrics"]["sessions"]["previous"])
        self.assertEqual(25.0, result["metrics"]["sessions"]["change_percent"])


if __name__ == "__main__":
    unittest.main()
