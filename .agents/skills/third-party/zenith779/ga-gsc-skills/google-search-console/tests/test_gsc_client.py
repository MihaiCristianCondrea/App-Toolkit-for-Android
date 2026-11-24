import importlib.util
import io
import json
import os
import sys
import tempfile
import unittest
from datetime import date, timedelta
from pathlib import Path
from types import SimpleNamespace
from unittest import mock


MODULE_PATH = (
    Path(__file__).resolve().parents[1] / "scripts" / "gsc_client.py"
)


def load_module():
    spec = importlib.util.spec_from_file_location("gsc_client", MODULE_PATH)
    module = importlib.util.module_from_spec(spec)
    sys.modules["gsc_client"] = module
    spec.loader.exec_module(module)
    return module


class FakeRow:
    def __init__(self, **kwargs):
        self._data = kwargs

    def _asdict(self):
        return dict(self._data)


class GscClientHelpersTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.module = load_module()

    def test_parse_date_range_prefers_days(self):
        start, end = self.module.parse_date_range(days=30, start=None, end=None)
        yesterday = date.today() - timedelta(days=1)
        expected_start = (yesterday - timedelta(days=29)).isoformat()
        self.assertEqual((expected_start, "yesterday"), (start, end))

    def test_parse_filter_supports_dimension_operator_expression(self):
        parsed = self.module.parse_filter("page:contains:/blog/")
        self.assertEqual(
            {
                "dimension": "page",
                "operator": "contains",
                "expression": "/blog/",
            },
            parsed,
        )

    def test_parse_filter_defaults_operator_to_equals(self):
        parsed = self.module.parse_filter("country:USA")
        self.assertEqual(
            {
                "dimension": "country",
                "operator": "equals",
                "expression": "USA",
            },
            parsed,
        )

    def test_parse_filter_keeps_colons_inside_expression(self):
        parsed = self.module.parse_filter("page:https://example.com/foo")
        self.assertEqual(
            {
                "dimension": "page",
                "operator": "equals",
                "expression": "https://example.com/foo",
            },
            parsed,
        )

    def test_parse_filter_rejects_invalid_input(self):
        with self.assertRaises(ValueError):
            self.module.parse_filter("invalid-filter")

    def test_parse_date_range_accepts_start_without_end(self):
        start, end = self.module.parse_date_range(
            days=None,
            start="2026-03-01",
            end=None,
        )
        self.assertEqual(("2026-03-01", "yesterday"), (start, end))

    def test_load_project_env_rejects_missing_explicit_env_file(self):
        missing = Path(tempfile.gettempdir()) / "missing-google-search-console.env"
        if missing.exists():
            missing.unlink()

        with self.assertRaises(FileNotFoundError):
            self.module.load_project_env(str(missing))

    def test_search_console_client_prefers_new_oauth_env_names(self):
        env = {
            "GOOGLE_SEARCH_CONSOLE_SITE_URL": "sc-domain:example.com",
            "GOOGLE_SEARCH_CONSOLE_OAUTH_CLIENT_CONFIG": "C:/tmp/client.json",
            "GOOGLE_SEARCH_CONSOLE_OAUTH_CREDENTIALS": "C:/tmp/credentials.json",
        }

        with unittest.mock.patch.dict(os.environ, env, clear=True):
            client = self.module.SearchConsoleClient()

        self.assertEqual("sc-domain:example.com", client.site_url)
        self.assertEqual("C:/tmp/client.json", client.client_config)
        self.assertEqual("C:/tmp/credentials.json", client.credentials)

    def test_search_console_client_prefers_shared_service_account_credentials(self):
        env = {
            "GOOGLE_SEARCH_CONSOLE_SITE_URL": "sc-domain:example.com",
            "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS": "C:/tmp/shared-service-account.json",
            "GOOGLE_SEARCH_CONSOLE_SERVICE_ACCOUNT": "C:/tmp/legacy-gsc-service-account.json",
        }

        with unittest.mock.patch.dict(os.environ, env, clear=True):
            client = self.module.SearchConsoleClient()

        self.assertEqual("sc-domain:example.com", client.site_url)
        self.assertEqual(
            "C:/tmp/shared-service-account.json",
            client.service_account,
        )

    def test_search_console_client_falls_back_to_legacy_oauth_env_names(self):
        env = {
            "GOOGLE_SEARCH_CONSOLE_SITE_URL": "sc-domain:example.com",
            "GOOGLE_SEARCH_CONSOLE_CLIENT_CONFIG": "C:/tmp/legacy-client.json",
            "GOOGLE_SEARCH_CONSOLE_CREDENTIALS": "C:/tmp/legacy-credentials.json",
        }

        with unittest.mock.patch.dict(os.environ, env, clear=True):
            client = self.module.SearchConsoleClient()

        self.assertEqual("C:/tmp/legacy-client.json", client.client_config)
        self.assertEqual("C:/tmp/legacy-credentials.json", client.credentials)

    def test_legacy_credentials_path_with_service_account_json_is_promoted(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            service_account_path = Path(tmpdir) / "legacy-service-account.json"
            env_file = Path(tmpdir) / "test.env"
            env_file.write_text("", encoding="utf-8")
            service_account_path.write_text(
                json.dumps(
                    {
                        "type": "service_account",
                        "project_id": "demo-project",
                        "private_key_id": "demo-key-id",
                        "private_key": "-----BEGIN PRIVATE KEY-----\\nabc\\n-----END PRIVATE KEY-----\\n",
                        "client_email": "demo@example.iam.gserviceaccount.com",
                        "client_id": "1234567890",
                        "auth_uri": "https://accounts.google.com/o/oauth2/auth",
                        "token_uri": "https://oauth2.googleapis.com/token",
                        "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
                        "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/demo",
                        "universe_domain": "googleapis.com",
                    }
                ),
                encoding="utf-8",
            )

            env = {
                "GOOGLE_SEARCH_CONSOLE_SITE_URL": "sc-domain:example.com",
                "GOOGLE_SEARCH_CONSOLE_CREDENTIALS": str(service_account_path),
            }

            with unittest.mock.patch.dict(os.environ, env, clear=True):
                client = self.module.SearchConsoleClient(env_file=str(env_file))

        self.assertEqual(str(service_account_path), client.service_account)
        self.assertIsNone(client.credentials)

    def test_build_report_dict_serializes_rows(self):
        report = SimpleNamespace(
            rows=[
                FakeRow(date="2026-04-01", clicks=10, impressions=100, ctr=0.1),
                FakeRow(date="2026-04-02", clicks=12, impressions=110, ctr=0.109),
            ]
        )

        result = self.module.build_report_dict(
            report=report,
            site_url="https://example.com/",
            start_date="2026-03-26",
            end_date="yesterday",
            dimensions=["date"],
            search_type="web",
            data_state="final",
        )

        self.assertEqual("https://example.com/", result["site_url"])
        self.assertEqual(["date"], result["dimensions"])
        self.assertEqual(["clicks", "impressions", "ctr"], result["metrics"])
        self.assertEqual(2, result["row_count"])
        self.assertEqual("2026-04-01", result["rows"][0]["date"])

    def test_format_as_table_outputs_headers_and_totals(self):
        result = {
            "dimensions": ["page"],
            "metrics": ["clicks", "impressions", "ctr", "position"],
            "rows": [
                {
                    "page": "/blog/a",
                    "clicks": 10,
                    "impressions": 100,
                    "ctr": 0.1,
                    "position": 4.2,
                }
            ],
            "row_count": 1,
        }

        output = self.module.format_as_table(result)

        self.assertIn("page | clicks | impressions | ctr | position", output)
        self.assertIn("/blog/a | 10 | 100 | 0.1 | 4.2", output)
        self.assertIn("Total rows: 1", output)

    def test_build_inspection_dict_wraps_response(self):
        response = {
            "inspectionResult": {
                "inspectionResultLink": "https://search.google.com/search-console/inspect/example",
                "indexStatusResult": {
                    "verdict": "PASS",
                    "coverageState": "Submitted and indexed",
                },
            }
        }

        result = self.module.build_inspection_dict(
            response=response,
            site_url="sc-domain:example.com",
            inspection_url="https://example.com/blog/post",
            language_code="en-US",
        )

        self.assertEqual("sc-domain:example.com", result["site_url"])
        self.assertEqual("https://example.com/blog/post", result["inspection_url"])
        self.assertEqual("en-US", result["language_code"])
        self.assertEqual(
            "PASS",
            result["inspection_result"]["indexStatusResult"]["verdict"],
        )

    def test_format_inspection_as_table_outputs_key_status_fields(self):
        result = {
            "site_url": "sc-domain:example.com",
            "inspection_url": "https://example.com/blog/post",
            "language_code": "en-US",
            "inspection_result": {
                "inspectionResultLink": "https://search.google.com/search-console/inspect/example",
                "indexStatusResult": {
                    "verdict": "PASS",
                    "coverageState": "Submitted and indexed",
                    "indexingState": "INDEXING_ALLOWED",
                    "robotsTxtState": "ALLOWED",
                    "pageFetchState": "SUCCESSFUL",
                },
                "mobileUsabilityResult": {"verdict": "PASS"},
                "richResultsResult": {"verdict": "PASS"},
            },
        }

        output = self.module.format_inspection_as_table(result)

        self.assertIn("Inspection URL: https://example.com/blog/post", output)
        self.assertIn("Site URL: sc-domain:example.com", output)
        self.assertIn("Index verdict: PASS", output)
        self.assertIn("Coverage state: Submitted and indexed", output)
        self.assertIn("Mobile usability verdict: PASS", output)
        self.assertIn("Rich results verdict: PASS", output)

    def test_inspect_url_calls_google_api_with_expected_body(self):
        env = {
            "GOOGLE_SEARCH_CONSOLE_SITE_URL": "sc-domain:example.com",
            "GOOGLE_SERVICE_ACCOUNT_CREDENTIALS": "C:/tmp/shared-service-account.json",
        }

        executed_body = {}
        execute_mock = mock.Mock(
            return_value={
                "inspectionResult": {
                    "indexStatusResult": {"verdict": "PASS"},
                }
            }
        )
        def inspect(body):
            executed_body["body"] = body
            return SimpleNamespace(execute=execute_mock)

        inspect_resource = SimpleNamespace(inspect=inspect)
        fake_service = SimpleNamespace(
            urlInspection=lambda: SimpleNamespace(index=lambda: inspect_resource)
        )

        with unittest.mock.patch.dict(os.environ, env, clear=True):
            client = self.module.SearchConsoleClient()

        fake_account = SimpleNamespace(
            credentials=SimpleNamespace(_credentials=object()),
        )
        fake_webproperty = SimpleNamespace(url="sc-domain:example.com")

        with mock.patch.object(client, "_get_webproperty", return_value=fake_webproperty), mock.patch.object(
            client,
            "authenticate_account",
            return_value=fake_account,
        ), mock.patch.object(
            client,
            "_build_search_console_service",
            return_value=fake_service,
        ):
            result = client.inspect_url(
                inspection_url="https://example.com/blog/post",
                language_code="en-US",
            )

        self.assertEqual(
            {
                "inspectionUrl": "https://example.com/blog/post",
                "siteUrl": "sc-domain:example.com",
                "languageCode": "en-US",
            },
            executed_body["body"],
        )
        self.assertEqual(
            "PASS",
            result["inspection_result"]["indexStatusResult"]["verdict"],
        )

    def test_main_routes_inspection_requests_through_cli(self):
        inspect_result = {
            "site_url": "sc-domain:example.com",
            "inspection_url": "https://example.com/blog/post",
            "language_code": "en-US",
            "inspection_result": {
                "indexStatusResult": {"verdict": "PASS"},
            },
        }

        with mock.patch.object(
            self.module,
            "SearchConsoleClient",
        ) as client_cls, mock.patch.object(self.module, "write_output") as write_output:
            client_cls.return_value.inspect_url.return_value = inspect_result
            argv = [
                "gsc_client.py",
                "--inspect-url",
                "https://example.com/blog/post",
                "--format",
                "json",
            ]

            with mock.patch.object(sys, "argv", argv):
                self.module.main()

        client_cls.return_value.inspect_url.assert_called_once_with(
            inspection_url="https://example.com/blog/post",
            language_code="en-US",
            site_url=None,
            flow="web",
        )
        write_output.assert_called_once()
        written_output = write_output.call_args.args[0]
        self.assertIn('"inspection_url": "https://example.com/blog/post"', written_output)

    def test_main_rejects_csv_format_for_url_inspection(self):
        with mock.patch.object(
            self.module,
            "SearchConsoleClient",
        ), mock.patch("sys.stderr", new_callable=io.StringIO) as fake_stderr:
            argv = [
                "gsc_client.py",
                "--inspect-url",
                "https://example.com/blog/post",
                "--format",
                "csv",
            ]

            with mock.patch.object(sys, "argv", argv):
                with self.assertRaises(SystemExit) as exc:
                    self.module.main()

        self.assertEqual(1, exc.exception.code)
        self.assertIn("CSV output is not supported for URL inspection", fake_stderr.getvalue())


if __name__ == "__main__":
    unittest.main()
