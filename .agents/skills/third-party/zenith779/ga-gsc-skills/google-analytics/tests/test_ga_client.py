import importlib.util
import os
import sys
import tempfile
import unittest
from pathlib import Path
from types import SimpleNamespace
from unittest.mock import Mock, patch


MODULE_PATH = Path(__file__).resolve().parents[1] / "scripts" / "ga_client.py"


def load_module():
    spec = importlib.util.spec_from_file_location("ga_client", MODULE_PATH)
    module = importlib.util.module_from_spec(spec)
    sys.modules["ga_client"] = module
    spec.loader.exec_module(module)
    return module


class GaClientHelpersTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.module = load_module()

    def test_default_env_path_falls_back_to_workspace(self):
        self.assertEqual(Path.cwd() / ".env", self.module.DEFAULT_ENV_FILE)

    def test_parse_date_range_prefers_days(self):
        start, end = self.module.parse_date_range(days=30, start=None, end=None)
        self.assertEqual(("30daysAgo", "yesterday"), (start, end))

    def test_prefers_new_credentials_env_name_and_maps_sdk_env(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            credentials_file = Path(tmpdir) / "ga-service-account.json"
            credentials_file.write_text("{}", encoding="utf-8")

            with patch.dict(
                os.environ,
                {
                    "GOOGLE_ANALYTICS_PROPERTY_ID": "123456789",
                    "GOOGLE_ANALYTICS_CREDENTIALS": str(credentials_file),
                },
                clear=True,
            ):
                fake_client = object()
                with patch.object(
                    self.module,
                    "BetaAnalyticsDataClient",
                    return_value=fake_client,
                ):
                    client = self.module.GoogleAnalyticsClient()
                    mapped_credentials = os.environ["GOOGLE_APPLICATION_CREDENTIALS"]

            self.assertEqual("123456789", client.property_id)
            self.assertEqual(str(credentials_file), mapped_credentials)

    def test_prefers_shared_google_service_account_credentials(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            credentials_file = Path(tmpdir) / "shared-service-account.json"
            credentials_file.write_text("{}", encoding="utf-8")

            with patch.dict(
                os.environ,
                {
                    "GOOGLE_ANALYTICS_PROPERTY_ID": "123456789",
                    "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS": str(credentials_file),
                },
                clear=True,
            ):
                with patch.object(
                    self.module,
                    "BetaAnalyticsDataClient",
                    return_value=object(),
                ):
                    client = self.module.GoogleAnalyticsClient()
                    mapped_credentials = os.environ["GOOGLE_APPLICATION_CREDENTIALS"]

            self.assertEqual("123456789", client.property_id)
            self.assertEqual(str(credentials_file), mapped_credentials)

    def test_falls_back_to_legacy_google_application_credentials(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            credentials_file = Path(tmpdir) / "legacy-ga-service-account.json"
            credentials_file.write_text("{}", encoding="utf-8")

            with patch.dict(
                os.environ,
                {
                    "GOOGLE_ANALYTICS_PROPERTY_ID": "123456789",
                    "GOOGLE_APPLICATION_CREDENTIALS": str(credentials_file),
                },
                clear=True,
            ):
                with patch.object(
                    self.module,
                    "BetaAnalyticsDataClient",
                    return_value=object(),
                ):
                    client = self.module.GoogleAnalyticsClient()

            self.assertEqual("123456789", client.property_id)

    def test_run_report_requests_total_metric_aggregation(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            credentials_file = Path(tmpdir) / "ga-service-account.json"
            credentials_file.write_text("{}", encoding="utf-8")

            fake_response = SimpleNamespace(
                dimension_headers=[],
                metric_headers=[],
                rows=[],
                row_count=0,
                metadata={},
                totals=[],
            )
            run_report_mock = Mock(return_value=fake_response)

            with patch.dict(
                os.environ,
                {
                    "GOOGLE_ANALYTICS_PROPERTY_ID": "123456789",
                    "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS": str(credentials_file),
                },
                clear=True,
            ):
                fake_client = SimpleNamespace(
                    run_report=run_report_mock
                )
                with patch.object(
                    self.module,
                    "BetaAnalyticsDataClient",
                    return_value=fake_client,
                ):
                    client = self.module.GoogleAnalyticsClient()
                    client.run_report(
                        start_date="7daysAgo",
                        end_date="yesterday",
                        metrics=["sessions", "activeUsers"],
                        limit=1,
                        include_totals=True,
                    )

            request = run_report_mock.call_args.args[0]
            self.assertEqual(1, len(request.metric_aggregations))


if __name__ == "__main__":
    unittest.main()
