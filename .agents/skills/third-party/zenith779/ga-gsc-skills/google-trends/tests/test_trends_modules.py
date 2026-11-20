import csv
import io
import importlib.util
import json
import os
import sys
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

BASE_DIR = Path(__file__).resolve().parents[1] / 'scripts'


def load_module(name: str, relative_path: str):
    module_path = BASE_DIR / relative_path
    spec = importlib.util.spec_from_file_location(name, module_path)
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


def write_fake_python_stub(path: Path, out_csv: Path, csv_text: str, applied_period_label: str, args_log: Path | None = None):
    args_log_expr = f"Path(r'''{args_log}''').write_text('\\n'.join(sys.argv[1:]), encoding='utf-8')" if args_log else "None"
    path.write_text(
        "import json\n"
        "import sys\n"
        "from pathlib import Path\n"
        "sys.stdin.read()\n"
        f"{args_log_expr}\n"
        f"Path(r'''{out_csv}''').write_text(r'''{csv_text}''', encoding='utf-8')\n"
        "print(json.dumps({"
        f"'download_path': r'''{out_csv}''', "
        "'row_count': 1, "
        f"'applied_period_label': r'''{applied_period_label}'''"
        "}, ensure_ascii=False))\n",
        encoding='utf-8',
    )


class TrendsUSListTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.list_mod = load_module('trends_us_list', 'trends_us_list.py')

    def test_normalize_time_range_defaults_to_7d(self):
        self.assertEqual(self.list_mod.normalize_time_range(None), 'now 7-d')
        self.assertEqual(self.list_mod.normalize_time_range('7d'), 'now 7-d')
        self.assertEqual(self.list_mod.normalize_time_range('24h'), 'now 1-d')
        self.assertEqual(self.list_mod.normalize_time_range('30d'), 'today 1-m')

    def test_period_label_maps_from_time_range(self):
        self.assertEqual(self.list_mod.period_label_for_time_range('now 1-d'), 'Past 24 hours')
        self.assertEqual(self.list_mod.period_label_for_time_range('now 7-d'), 'Past 7 days')
        self.assertEqual(self.list_mod.period_label_for_time_range('today 1-m'), 'Past 30 days')

    def test_resolve_python_bin_prefers_current_python_on_windows(self):
        with patch.object(self.list_mod.sys, 'platform', 'win32'):
            self.assertEqual(self.list_mod.resolve_python_bin(), sys.executable)

    def test_resolve_python_bin_prefers_current_python_on_non_windows(self):
        with patch.object(self.list_mod.sys, 'platform', 'linux'):
            self.assertEqual(self.list_mod.resolve_python_bin(), sys.executable)

    def test_resolve_chromium_path_prefers_playwright_install_on_windows(self):
        fake_path = r'C:\Users\demo\AppData\Local\ms-playwright\chromium-1208\chrome-win64\chrome.exe'
        with patch.object(self.list_mod.sys, 'platform', 'win32'), \
             patch.object(self.list_mod, 'glob', return_value=[fake_path]):
            self.assertEqual(self.list_mod.resolve_chromium_path(), fake_path)

    def test_resolve_chromium_path_prefers_explicit_env_on_non_windows(self):
        with patch.object(self.list_mod.sys, 'platform', 'linux'), \
             patch.dict(self.list_mod.os.environ, {'PLAYWRIGHT_CHROMIUM_EXECUTABLE_PATH': '/opt/chromium/chrome'}, clear=True):
            self.assertEqual(self.list_mod.resolve_chromium_path(), '/opt/chromium/chrome')

    def test_resolve_chromium_path_returns_none_without_detected_install(self):
        with patch.object(self.list_mod.sys, 'platform', 'linux'), \
             patch.dict(self.list_mod.os.environ, {}, clear=True):
            self.assertIsNone(self.list_mod.resolve_chromium_path())

    def test_collect_list_uses_resolved_chromium_path_when_not_provided(self):
        with patch.object(self.list_mod, 'run_browser_csv_download', return_value={'download_path': __file__, 'applied_period_label': 'Past 7 days'}) as run_mock, \
             patch.object(self.list_mod, 'parse_trending_csv', return_value=[]):
            self.list_mod.collect_list(geo='US', time_range='7d')
        self.assertIsNone(run_mock.call_args.kwargs['chromium_path'])

    def test_build_playwright_proxy_settings_parses_credentials(self):
        settings = self.list_mod.build_playwright_proxy_settings('http://user:pass@proxy.example:8080')
        self.assertEqual(settings['server'], 'http://proxy.example:8080')
        self.assertEqual(settings['username'], 'user')
        self.assertEqual(settings['password'], 'pass')

    def test_build_playwright_proxy_settings_returns_none_without_proxy(self):
        self.assertIsNone(self.list_mod.build_playwright_proxy_settings(None))

    def test_run_browser_csv_download_injects_proxy_settings_into_subprocess_env(self):
        completed = type('Completed', (), {'stdout': '{"download_path":"x.csv"}\n'})()
        with patch.object(self.list_mod.subprocess, 'run', return_value=completed) as run_mock:
            self.list_mod.run_browser_csv_download(
                python_bin='python',
                chromium_path='/fake/chromium',
                output_dir='/tmp/out',
                proxy_url='http://user:pass@proxy.example:8080',
            )
        env = run_mock.call_args.kwargs['env']
        settings = json.loads(env['GOOGLE_TRENDS_RADAR_PROXY_SETTINGS'])
        self.assertEqual(settings['server'], 'http://proxy.example:8080')
        self.assertEqual(settings['username'], 'user')
        self.assertEqual(settings['password'], 'pass')

    def test_run_browser_csv_download_prefers_google_trends_proxy_settings_name(self):
        completed = type('Completed', (), {'stdout': '{"download_path":"x.csv"}\n'})()
        with patch.object(self.list_mod.subprocess, 'run', return_value=completed) as run_mock, \
             patch.object(self.list_mod, 'build_playwright_proxy_settings', return_value={'server': 'http://proxy.example:8080'}):
            self.list_mod.run_browser_csv_download(
                python_bin='python',
                chromium_path='/fake/chromium',
                output_dir='/tmp/out',
                proxy_url='http://proxy.example:8080',
            )
        env = run_mock.call_args.kwargs['env']
        self.assertIn('GOOGLE_TRENDS_PROXY_SETTINGS', env)

    def test_parse_trending_csv_returns_structured_rows(self):
        csv_text = (
            'Trends,Search volume,Started,Ended,Trend breakdown,Explore link\n'
            '"iran california",100K+,"March 11, 2026 at 6:10:00 PM UTC",,"iran california,california",'
            'https://trends.google.com/trends/explore?q=iran%20california&geo=US&hl=en-US\n'
            '"timberwolves vs clippers",50K+,"March 12, 2026 at 2:10:00 AM UTC",,"kawhi leonard,clippers",'
            'https://trends.google.com/trends/explore?q=timberwolves%20vs%20clippers&geo=US&hl=en-US\n'
        )
        rows = self.list_mod.parse_trending_csv(csv_text, geo='US', time_range='now 7-d')
        self.assertEqual(len(rows), 2)
        self.assertEqual(rows[0]['keyword'], 'iran california')
        self.assertEqual(rows[0]['translation_zh'], '伊朗 加州')
        self.assertEqual(rows[0]['search_volume'], '100K+')
        self.assertEqual(rows[0]['geo'], 'US')
        self.assertEqual(rows[0]['time_range'], 'now 7-d')
        self.assertEqual(rows[0]['trend_breakdown'], ['iran california', 'california'])
        self.assertIn('explore_link', rows[0])

    def test_build_list_payload_wraps_rows(self):
        rows = [
            {
                'keyword': 'iran california',
                'translation_zh': '伊朗 加州',
                'search_volume': '100K+',
                'started_at': '2026-03-11T18:10:00+00:00',
                'ended_at': None,
                'trend_breakdown': ['iran california', 'california'],
                'explore_link': 'https://trends.google.com/trends/explore?q=iran%20california&geo=US&hl=en-US',
                'geo': 'US',
                'time_range': 'now 7-d',
                'source': 'browser_csv',
            }
        ]
        payload = self.list_mod.build_list_payload(rows, geo='US', time_range='now 7-d')
        self.assertEqual(payload['geo'], 'US')
        self.assertEqual(payload['time_range'], 'now 7-d')
        self.assertEqual(payload['count'], 1)
        self.assertEqual(payload['items'][0]['keyword'], 'iran california')

    def test_collect_list_uses_browser_script_output_csv(self):
        csv_text = (
            'Trends,Search volume,Started,Ended,Trend breakdown,Explore link\n'
            '"iran california",100K+,"March 11, 2026 at 6:10:00 PM UTC",,"iran california,california",'
            'https://trends.google.com/trends/explore?q=iran%20california&geo=US&hl=en-US\n'
        )
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp = Path(tmpdir)
            out_csv = tmp / 'download.csv'
            stub = tmp / 'fake_python.py'
            write_fake_python_stub(stub, out_csv, csv_text, 'Past 7 days')
            payload = self.list_mod.collect_list(
                geo='US',
                time_range='7d',
                python_bin=str(stub),
                chromium_path='/fake/chromium',
                output_dir=str(tmp),
            )
            self.assertEqual(payload['geo'], 'US')
            self.assertEqual(payload['time_range'], 'now 7-d')
            self.assertEqual(payload['count'], 1)
            self.assertEqual(payload['items'][0]['keyword'], 'iran california')
            self.assertEqual(payload['items'][0]['source'], 'browser_csv')
            self.assertEqual(payload['download_path'], str(out_csv))
            self.assertEqual(payload['applied_period_label'], 'Past 7 days')
            self.assertTrue(payload['period_applied'])

    def test_collect_list_passes_period_label_to_browser_script(self):
        csv_text = (
            'Trends,Search volume,Started,Ended,Trend breakdown,Explore link\n'
            '"iran california",100K+,"March 11, 2026 at 6:10:00 PM UTC",,"iran california,california",'
            'https://trends.google.com/trends/explore?q=iran%20california&geo=US&hl=en-US\n'
        )
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp = Path(tmpdir)
            out_csv = tmp / 'download.csv'
            args_log = tmp / 'args.txt'
            stub = tmp / 'fake_python_args.py'
            write_fake_python_stub(stub, out_csv, csv_text, 'Past 24 hours', args_log=args_log)
            payload = self.list_mod.collect_list(
                geo='US',
                time_range='24h',
                python_bin=str(stub),
                chromium_path='/fake/chromium',
                output_dir=str(tmp),
            )
            args = args_log.read_text(encoding='utf-8').splitlines()
            self.assertIn('Past 24 hours', args)
            self.assertEqual(payload['applied_period_label'], 'Past 24 hours')
            self.assertTrue(payload['period_applied'])

    def test_collect_list_marks_period_not_applied_when_browser_reports_mismatch(self):
        csv_text = (
            'Trends,Search volume,Started,Ended,Trend breakdown,Explore link\n'
            '"iran california",100K+,"March 11, 2026 at 6:10:00 PM UTC",,"iran california,california",'
            'https://trends.google.com/trends/explore?q=iran%20california&geo=US&hl=en-US\n'
        )
        with tempfile.TemporaryDirectory() as tmpdir:
            tmp = Path(tmpdir)
            out_csv = tmp / 'download.csv'
            stub = tmp / 'fake_python_mismatch.py'
            write_fake_python_stub(stub, out_csv, csv_text, 'Past 7 days')
            payload = self.list_mod.collect_list(
                geo='US',
                time_range='24h',
                python_bin=str(stub),
                chromium_path='/fake/chromium',
                output_dir=str(tmp),
            )
            self.assertEqual(payload['period_label'], 'Past 24 hours')
            self.assertEqual(payload['applied_period_label'], 'Past 7 days')
            self.assertFalse(payload['period_applied'])


class TrendsMainCliTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.module = load_module('trends_main', 'trends.py')

    def test_main_supports_standard_help_flag(self):
        fake_stdout = io.StringIO()
        with patch.object(self.module.sys, 'argv', ['trends.py', '--help']), \
             patch('sys.stdout', fake_stdout):
            with self.assertRaises(SystemExit) as exc:
                self.module.main()
        self.assertEqual(exc.exception.code, 0)
        self.assertIn('Usage:', fake_stdout.getvalue())


class TrendsKeywordDetailTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.detail_mod = load_module('trends_keyword_detail', 'trends_keyword_detail.py')

    def test_strip_xssi_prefix(self):
        self.assertEqual(self.detail_mod.strip_xssi_prefix(")]}',\n{\"a\":1}"), '{"a":1}')

    def test_read_trends_json_uses_requests_session(self):
        response = type('Resp', (), {'text': ")]}',\n{\"ok\":true}", 'raise_for_status': lambda self: None})()
        session = type('Session', (), {'get': lambda self, url, params=None, timeout=None, headers=None: response})()
        data = self.detail_mod.read_trends_json('https://example.com', params={'a': 1}, session=session)
        self.assertTrue(data['ok'])

    def test_read_trends_json_with_retries_sleeps_after_429(self):
        class Resp:
            def __init__(self, status_code, text):
                self.status_code = status_code
                self.text = text

            def raise_for_status(self):
                if self.status_code >= 400:
                    import requests
                    raise requests.HTTPError(f'{self.status_code} boom')

        class Session:
            def __init__(self):
                self.calls = 0

            def get(self, url, params=None, timeout=None, headers=None):
                self.calls += 1
                if self.calls == 1:
                    return Resp(429, 'too many requests')
                return Resp(200, ")]}',\n{\"ok\":true}")

        session = Session()
        with patch.object(self.detail_mod.time, 'sleep') as sleep_mock:
            data = self.detail_mod.read_trends_json_with_retries('https://example.com', session=session, retry_delays=[0])
        self.assertTrue(data['ok'])
        sleep_mock.assert_called_once()

    def test_parse_interest_over_time_extracts_points(self):
        data = {
            'default': {
                'timelineData': [
                    {'time': '1710115200', 'formattedTime': 'Mar 10, 2024', 'value': [12]},
                    {'time': '1710201600', 'formattedTime': 'Mar 11, 2024', 'value': [48]},
                ]
            }
        }
        points = self.detail_mod.parse_interest_over_time(data)
        self.assertEqual(points[0]['value'], 12)
        self.assertEqual(points[1]['value'], 48)
        self.assertTrue(points[0]['date'].endswith('+00:00'))

    def test_parse_related_queries_combines_top_and_rising(self):
        data = {
            'default': {
                'rankedList': [
                    {
                        'rankingType': 'TOP',
                        'rankedKeyword': [
                            {'query': 'openai sora model review', 'value': 100},
                        ],
                    },
                    {
                        'rankingType': 'RISING',
                        'rankedKeyword': [
                            {'query': 'what is openai sora model', 'value': 'Breakout'},
                        ],
                    },
                ]
            }
        }
        rows = self.detail_mod.parse_related_queries(data)
        self.assertEqual(rows[0]['type'], 'TOP')
        self.assertEqual(rows[1]['type'], 'RISING')
        self.assertEqual(rows[1]['value'], 'Breakout')

    def test_build_detail_payload_contains_interest_and_related_queries(self):
        payload = self.detail_mod.build_detail_payload(
            keyword='openai sora model',
            geo='US',
            time_range='now 7-d',
            interest_over_time=[
                {'date': '2026-03-10T00:00:00+00:00', 'value': 12},
                {'date': '2026-03-11T00:00:00+00:00', 'value': 48},
            ],
            related_queries=[
                {'query': 'openai sora model review', 'value': 'Breakout'},
                {'query': 'what is openai sora model', 'value': 250},
            ],
        )
        self.assertEqual(payload['keyword'], 'openai sora model')
        self.assertEqual(payload['translation_zh'], 'OpenAI Sora 模型')
        self.assertEqual(payload['geo'], 'US')
        self.assertEqual(payload['time_range'], 'now 7-d')
        self.assertEqual(len(payload['interest_over_time']), 2)
        self.assertEqual(payload['interest_summary']['peak_value'], 48)
        self.assertEqual(payload['interest_summary']['latest_value'], 48)
        self.assertEqual(payload['related_queries'][0]['query'], 'openai sora model review')

    def test_collect_keyword_detail_returns_documented_limitation_on_failure(self):
        with patch.object(self.detail_mod, 'fetch_keyword_detail_data', side_effect=RuntimeError('boom')):
            payload = self.detail_mod.collect_keyword_detail('openai sora model')
        self.assertEqual(payload['keyword'], 'openai sora model')
        self.assertEqual(payload['translation_zh'], 'OpenAI Sora 模型')
        self.assertEqual(payload['interest_over_time'], [])
        self.assertEqual(payload['related_queries'], [])
        self.assertIn('failed', payload['limitation'].lower())

    def test_collect_keyword_detail_uses_live_shape_when_fetch_succeeds(self):
        with patch.object(
            self.detail_mod,
            'fetch_keyword_detail_data',
            return_value={
                'interest_over_time': [
                    {'date': '2026-03-10T00:00:00+00:00', 'value': 12},
                    {'date': '2026-03-11T00:00:00+00:00', 'value': 48},
                ],
                'related_queries': [
                    {'query': 'openai sora model review', 'value': 100, 'type': 'TOP'},
                ],
            },
        ):
            payload = self.detail_mod.collect_keyword_detail('openai sora model')
        self.assertNotIn('limitation', payload)
        self.assertEqual(payload['interest_summary']['peak_value'], 48)
        self.assertEqual(payload['related_queries'][0]['type'], 'TOP')


if __name__ == '__main__':
    unittest.main()
