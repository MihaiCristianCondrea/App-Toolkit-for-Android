import io
import json
import tempfile
import unittest
from contextlib import redirect_stdout
from pathlib import Path
from unittest.mock import MagicMock, patch

import importlib.util

MODULE_PATH = Path(__file__).resolve().parents[1] / 'scripts' / 'trends.py'
spec = importlib.util.spec_from_file_location('trends', MODULE_PATH)
radar = importlib.util.module_from_spec(spec)
spec.loader.exec_module(radar)


class RadarTests(unittest.TestCase):
    def test_default_env_path_falls_back_to_workspace(self):
        expected = Path(__file__).resolve().parents[2] / ".env"
        self.assertEqual(expected, radar.DEFAULT_ENV_FILE)

    def test_load_project_env_rejects_missing_explicit_env_file(self):
        missing = Path(tempfile.gettempdir()) / "missing-google-trends.env"
        if missing.exists():
            missing.unlink()

        with self.assertRaises(FileNotFoundError):
            radar.load_project_env(str(missing))

    def test_resolve_source_mode_defaults_to_csv_only(self):
        with patch.dict(radar.os.environ, {}, clear=True):
            self.assertEqual(radar.resolve_source_mode(), 'csv_only')

    def test_resolve_source_mode_allows_explicit_rss_enable(self):
        with patch.dict(radar.os.environ, {'GOOGLE_TRENDS_RADAR_SOURCE_MODE': 'csv_plus_rss'}, clear=True):
            self.assertEqual(radar.resolve_source_mode(), 'csv_plus_rss')

    def test_resolve_source_mode_prefers_google_trends_name(self):
        with patch.dict(radar.os.environ, {'GOOGLE_TRENDS_SOURCE_MODE': 'csv_plus_rss'}, clear=True):
            self.assertEqual(radar.resolve_source_mode(), 'csv_plus_rss')

    def test_collect_trends_uses_csv_only_by_default(self):
        with patch.object(radar, 'load_csv_trends', return_value=([{'keyword': 'apple iphone 18 pro max', 'geo': 'US', 'source': 'csv'}], [])) as load_csv, \
             patch.object(radar, 'fetch_geo') as fetch_geo, \
             patch.dict(radar.os.environ, {}, clear=True):
            items, errors = radar.collect_trends()
        self.assertEqual(len(items), 1)
        self.assertEqual(items[0]['keyword'], 'apple iphone 18 pro max')
        self.assertEqual(errors, [])
        load_csv.assert_called_once()
        fetch_geo.assert_not_called()

    def test_collect_trends_can_append_rss_when_enabled(self):
        with patch.object(radar, 'load_csv_trends', return_value=([{'keyword': 'apple iphone 18 pro max', 'geo': 'US', 'source': 'csv'}], [])) as load_csv, \
             patch.object(radar, 'fetch_geo', side_effect=lambda geo, session=None, opener=None: [{'keyword': f'{geo} rss', 'geo': geo, 'source': 'rss'}]), \
             patch.dict(radar.os.environ, {'GOOGLE_TRENDS_RADAR_SOURCE_MODE': 'csv_plus_rss'}, clear=True):
            items, errors = radar.collect_trends()
        self.assertEqual(len(items), 1 + len(radar.GEOS))
        self.assertEqual(errors, [])
        load_csv.assert_called_once()

    def test_collect_trends_falls_back_to_rss_when_csv_only_fails(self):
        with patch.object(radar, 'load_csv_trends', side_effect=RuntimeError('csv broken')) as load_csv, \
             patch.object(radar, 'fetch_geo', side_effect=lambda geo, session=None, opener=None: [{'keyword': f'{geo} rss', 'geo': geo, 'source': 'rss_fallback'}]), \
             patch.dict(radar.os.environ, {}, clear=True):
            items, errors = radar.collect_trends()
        self.assertEqual(len(items), len(radar.GEOS))
        self.assertTrue(any('CSV fetch failed' in err['error'] for err in errors))
        self.assertTrue(all(item['source'] == 'rss_fallback' for item in items))
        load_csv.assert_called_once()

    def test_resolve_proxy_url_defaults_to_none(self):
        with patch.dict(radar.os.environ, {}, clear=True):
            self.assertIsNone(radar.resolve_proxy_url())

    def test_resolve_proxy_url_prefers_explicit_env(self):
        with patch.dict(radar.os.environ, {'GOOGLE_TRENDS_RADAR_PROXY': 'http://override:1234'}, clear=True):
            self.assertEqual(radar.resolve_proxy_url(), 'http://override:1234')

    def test_resolve_proxy_url_prefers_google_trends_name(self):
        with patch.dict(radar.os.environ, {'GOOGLE_TRENDS_PROXY': 'http://override:1234'}, clear=True):
            self.assertEqual(radar.resolve_proxy_url(), 'http://override:1234')

    def test_resolve_proxy_url_still_accepts_legacy_name(self):
        with patch.dict(radar.os.environ, {'GOOGLE_TRENDS_RADAR_PROXY': 'http://legacy:8080'}, clear=True):
            self.assertEqual(radar.resolve_proxy_url(), 'http://legacy:8080')

    def test_resolve_proxy_url_ignores_generic_http_proxy_env(self):
        with patch.dict(radar.os.environ, {'HTTP_PROXY': 'http://generic:8080', 'HTTPS_PROXY': 'http://generic:8080'}, clear=True):
            self.assertEqual(radar.resolve_proxy_url(), radar.DEFAULT_PROXY_URL)

    def test_build_url_opener_uses_proxy_for_http_and_https(self):
        fake_opener = MagicMock()
        with patch.object(radar.urllib.request, 'build_opener', return_value=fake_opener) as build_opener:
            opener = radar.build_url_opener('http://proxy.example:8080')
        self.assertIs(opener, fake_opener)
        handler = build_opener.call_args.args[0]
        self.assertEqual(handler.proxies['http'], 'http://proxy.example:8080')
        self.assertEqual(handler.proxies['https'], 'http://proxy.example:8080')
        self.assertEqual(fake_opener.addheaders, radar.DEFAULT_REQUEST_HEADERS)

    def test_build_requests_session_uses_proxy_and_headers(self):
        session = radar.build_requests_session('http://proxy.example:8080')
        self.assertEqual(session.proxies['http'], 'http://proxy.example:8080')
        self.assertEqual(session.proxies['https'], 'http://proxy.example:8080')
        self.assertFalse(session.trust_env)
        self.assertIn('Mozilla/5.0', session.headers['User-Agent'])
        self.assertEqual(session.headers['Accept-Language'], 'en-US,en;q=0.9')

    def test_build_requests_session_disables_env_proxy_by_default(self):
        session = radar.build_requests_session()
        self.assertEqual(session.proxies, {})
        self.assertFalse(session.trust_env)

    def test_fetch_geo_uses_custom_opener(self):
        xml_bytes = b'<rss><channel><item><title>OpenAI Sora Model</title></item></channel></rss>'

        class FakeResponse:
            def __enter__(self):
                return self

            def __exit__(self, exc_type, exc, tb):
                return False

            def read(self):
                return xml_bytes

        opener = MagicMock()
        opener.open.return_value = FakeResponse()
        items = radar.fetch_geo('US', opener=opener)
        self.assertEqual(items[0]['keyword'], 'OpenAI Sora Model')
        opener.open.assert_called_once()

    def test_fetch_geo_uses_requests_session_when_given(self):
        response = MagicMock()
        response.content = b'<rss><channel><item><title>OpenAI Sora Model</title></item></channel></rss>'
        response.raise_for_status.return_value = None
        session = MagicMock()
        session.get.return_value = response
        items = radar.fetch_geo('US', session=session)
        self.assertEqual(items[0]['keyword'], 'OpenAI Sora Model')
        session.get.assert_called_once()

    def test_normalize_keyword_collapses_spaces_and_lowercases(self):
        self.assertEqual(radar.normalize_keyword('  How   To   Build  '), 'how to build')

    def test_score_keyword_prioritizes_tool_intent(self):
        score, reasons, intents, noun_hits, caution = radar.score_keyword('ai image generator', 2, 1)
        self.assertGreaterEqual(score, 10)
        self.assertIn('tool', intents)
        self.assertTrue(noun_hits)
        self.assertFalse(caution)

    def test_priority_thresholds(self):
        self.assertEqual(radar.priority_from_score(8), 'P1')
        self.assertEqual(radar.priority_from_score(4), 'P2')
        self.assertEqual(radar.priority_from_score(3), 'P3')

    def test_sports_vs_keyword_should_be_filtered_not_promoted(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            state_dir = Path(tmpdir)
            with patch.object(radar, 'STATE_DIR', state_dir), \
                 patch.object(radar, 'SEEN_PATH', state_dir / 'seen_keywords.json'), \
                 patch.object(radar, 'WATCH_PATH', state_dir / 'watchlist.json'), \
                 patch.object(radar, 'NOTIFIED_PATH', state_dir / 'notified.json'), \
                 patch.object(radar, 'LATEST_PATH', state_dir / 'latest_report.json'), \
                 patch.object(radar, 'collect_trends', return_value=([
                     {'keyword': 'Clippers vs Timberwolves', 'geo': 'US', 'source': 'mock://us'},
                 ], [])):
                report = radar.scan()
                self.assertEqual(report['stats']['p1'], 0)
                self.assertEqual(report['stats']['p2'], 0)
                self.assertEqual(report['stats']['p3'], 1)
                self.assertEqual(report['p3'][0]['reason'], 'Matched sports / entertainment noise filter')

    def test_person_name_with_entertainment_context_should_be_filtered(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            state_dir = Path(tmpdir)
            with patch.object(radar, 'STATE_DIR', state_dir), \
                 patch.object(radar, 'SEEN_PATH', state_dir / 'seen_keywords.json'), \
                 patch.object(radar, 'WATCH_PATH', state_dir / 'watchlist.json'), \
                 patch.object(radar, 'NOTIFIED_PATH', state_dir / 'notified.json'), \
                 patch.object(radar, 'LATEST_PATH', state_dir / 'latest_report.json'), \
                 patch.object(radar, 'collect_trends', return_value=([
                     {'keyword': 'Linkin Park Adelaide Tour', 'geo': 'AU', 'source': 'mock://au'},
                 ], [])):
                report = radar.scan()
                self.assertEqual(report['stats']['p2'], 0)
                self.assertEqual(report['stats']['p3'], 1)
                self.assertEqual(report['p3'][0]['reason'], 'Matched person / show / event noise filter')

    def test_power_outage_query_should_be_dropped(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            state_dir = Path(tmpdir)
            with patch.object(radar, 'STATE_DIR', state_dir), \
                 patch.object(radar, 'SEEN_PATH', state_dir / 'seen_keywords.json'), \
                 patch.object(radar, 'WATCH_PATH', state_dir / 'watchlist.json'), \
                 patch.object(radar, 'NOTIFIED_PATH', state_dir / 'notified.json'), \
                 patch.object(radar, 'LATEST_PATH', state_dir / 'latest_report.json'), \
                 patch.object(radar, 'collect_trends', return_value=([
                     {'keyword': 'power outage near me', 'geo': 'US', 'source': 'mock://us'},
                     {'keyword': 'power outage near me', 'geo': 'CA', 'source': 'mock://ca'},
                 ], [])):
                report = radar.scan()
                self.assertEqual(report['stats']['p2'], 0)
                self.assertEqual(report['stats']['p3'], 1)
                self.assertEqual(report['p3'][0]['reason'], 'Matched non-site-intent filter')

    def test_share_price_query_should_be_dropped(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            state_dir = Path(tmpdir)
            with patch.object(radar, 'STATE_DIR', state_dir), \
                 patch.object(radar, 'SEEN_PATH', state_dir / 'seen_keywords.json'), \
                 patch.object(radar, 'WATCH_PATH', state_dir / 'watchlist.json'), \
                 patch.object(radar, 'NOTIFIED_PATH', state_dir / 'notified.json'), \
                 patch.object(radar, 'LATEST_PATH', state_dir / 'latest_report.json'), \
                 patch.object(radar, 'collect_trends', return_value=([
                     {'keyword': 'axis bank share price', 'geo': 'IN', 'source': 'mock://in'},
                 ], [])):
                report = radar.scan()
                self.assertEqual(report['stats']['p3'], 1)
                self.assertEqual(report['p3'][0]['reason'], 'Matched non-site-intent filter')

    def test_layoff_news_should_be_dropped(self):
        with tempfile.TemporaryDirectory() as tmpdir:
            state_dir = Path(tmpdir)
            with patch.object(radar, 'STATE_DIR', state_dir), \
                 patch.object(radar, 'SEEN_PATH', state_dir / 'seen_keywords.json'), \
                 patch.object(radar, 'WATCH_PATH', state_dir / 'watchlist.json'), \
                 patch.object(radar, 'NOTIFIED_PATH', state_dir / 'notified.json'), \
                 patch.object(radar, 'LATEST_PATH', state_dir / 'latest_report.json'), \
                 patch.object(radar, 'collect_trends', return_value=([
                     {'keyword': 'atlassian layoffs', 'geo': 'IN', 'source': 'mock://in'},
                 ], [])):
                report = radar.scan()
                self.assertEqual(report['stats']['p2'], 0)
                self.assertEqual(report['stats']['p3'], 1)
                self.assertEqual(report['p3'][0]['reason'], 'Matched non-site-intent filter')

        with tempfile.TemporaryDirectory() as tmpdir:
            state_dir = Path(tmpdir)
            with patch.object(radar, 'STATE_DIR', state_dir), \
                 patch.object(radar, 'SEEN_PATH', state_dir / 'seen_keywords.json'), \
                 patch.object(radar, 'WATCH_PATH', state_dir / 'watchlist.json'), \
                 patch.object(radar, 'NOTIFIED_PATH', state_dir / 'notified.json'), \
                 patch.object(radar, 'LATEST_PATH', state_dir / 'latest_report.json'), \
                 patch.object(radar, 'collect_trends', return_value=([
                     {'keyword': 'OpenAI Sora Model', 'geo': 'US', 'source': 'mock://us'},
                     {'keyword': 'OpenAI Sora Model', 'geo': 'GB', 'source': 'mock://gb'},
                     {'keyword': 'OpenAI Sora Model', 'geo': 'CA', 'source': 'mock://ca'},
                 ], [])):
                report = radar.scan()
                self.assertEqual(report['stats']['p1'], 1)
                self.assertEqual(report['p1'][0]['category'], 'New model name')
                self.assertIn('translation_zh', report['p1'][0])

    def test_expand_keyword_opportunities_generates_buildable_angles(self):
        angles = radar.expand_keyword_opportunities('OpenAI Sora Model', 'New model name')
        labels = [item['label'] for item in angles]
        queries = [item['query'] for item in angles]
        self.assertIn('What is', labels)
        self.assertIn('Tutorial', labels)
        self.assertIn('Review', labels)
        self.assertIn('openai sora model review', queries)
        self.assertIn('what is openai sora model', queries)

        with tempfile.TemporaryDirectory() as tmpdir:
            state_dir = Path(tmpdir)
            with patch.object(radar, 'STATE_DIR', state_dir), \
                 patch.object(radar, 'SEEN_PATH', state_dir / 'seen_keywords.json'), \
                 patch.object(radar, 'WATCH_PATH', state_dir / 'watchlist.json'), \
                 patch.object(radar, 'NOTIFIED_PATH', state_dir / 'notified.json'), \
                 patch.object(radar, 'LATEST_PATH', state_dir / 'latest_report.json'), \
                 patch.object(radar, 'collect_trends', return_value=([
                     {'keyword': 'AI Image Generator', 'geo': 'US', 'source': 'mock://us'},
                     {'keyword': 'AI Image Generator', 'geo': 'GB', 'source': 'mock://gb'},
                     {'keyword': 'Celebrity Gossip', 'geo': 'US', 'source': 'mock://us'},
                 ], [])):
                report = radar.scan()
                self.assertEqual(report['stats']['p1'], 1)
                self.assertEqual(report['stats']['p3'], 1)
                self.assertEqual(report['p1'][0]['normalized'], 'ai image generator')
                self.assertTrue((state_dir / 'latest_report.json').exists())
                self.assertIn('translation_zh', report['p1'][0])

    def test_print_summary_report_hides_translation_zh_in_default_english_output(self):
        report = {
            'generated_at': '2026-04-15T00:00:00+00:00',
            'today_do': [
                {
                    'keyword': 'AI Image Generator',
                    'translation_zh': 'AI 图片 生成器',
                    'category': 'High tool-intent keyword',
                    'score': 12,
                    'site_type': 'Tool page / single-purpose site',
                }
            ],
            'watch': [
                {
                    'keyword': 'OpenAI Sora Model',
                    'translation_zh': 'OpenAI Sora 模型',
                    'category': 'New model name',
                    'score': 7,
                }
            ],
            'drop': [],
        }

        output = io.StringIO()
        with redirect_stdout(output):
            radar.print_summary_report(report)

        rendered = output.getvalue()
        self.assertIn('AI Image Generator | High tool-intent keyword', rendered)
        self.assertIn('OpenAI Sora Model | New model name', rendered)
        self.assertNotIn('图片', rendered)
        self.assertNotIn('模型', rendered)

    def test_print_scan_report_hides_translation_zh_in_default_english_output(self):
        report = {
            'generated_at': '2026-04-15T00:00:00+00:00',
            'stats': {
                'raw_items': 1,
                'unique_keywords': 1,
                'p1': 1,
                'p2': 0,
                'p3': 0,
            },
            'errors': [],
            'p1': [
                {
                    'keyword': 'AI Image Generator',
                    'translation_zh': 'AI 图片 生成器',
                    'category': 'High tool-intent keyword',
                    'score': 12,
                    'site_type': 'Tool page / single-purpose site',
                    'action': 'Build a landing page now',
                    'risk': 'Reasonably buildable',
                }
            ],
        }

        output = io.StringIO()
        with redirect_stdout(output):
            radar.print_scan_report(report)

        rendered = output.getvalue()
        self.assertIn('AI Image Generator | High tool-intent keyword', rendered)
        self.assertNotIn('图片', rendered)


if __name__ == '__main__':
    unittest.main()
