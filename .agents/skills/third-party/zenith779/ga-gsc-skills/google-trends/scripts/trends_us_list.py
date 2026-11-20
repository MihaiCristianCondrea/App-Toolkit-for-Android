import csv
from glob import glob
import json
import os
import subprocess
import sys
import tempfile
from datetime import datetime, timezone
from io import StringIO

import importlib.util
from pathlib import Path
from urllib.parse import unquote, urlparse

BASE_DIR = Path(__file__).resolve().parent
TRENDS_CORE_PATH = BASE_DIR / 'trends.py'
LEGACY_RADAR_PATH = BASE_DIR / 'radar.py'
RADAR_PATH = TRENDS_CORE_PATH if TRENDS_CORE_PATH.exists() else LEGACY_RADAR_PATH
spec = importlib.util.spec_from_file_location('radar_shared', RADAR_PATH)
radar_shared = importlib.util.module_from_spec(spec)
spec.loader.exec_module(radar_shared)

DEFAULT_GEO = 'US'
DEFAULT_TIME_RANGE = 'now 7-d'
DEFAULT_TRENDS_URL = 'https://trends.google.com/trending?geo=US'
DEFAULT_PLAYWRIGHT_PYTHON = None
DEFAULT_CHROMIUM_PATH = None
TIME_RANGE_ALIASES = {
    None: DEFAULT_TIME_RANGE,
    '7d': 'now 7-d',
    'now 7-d': 'now 7-d',
    '24h': 'now 1-d',
    '1d': 'now 1-d',
    'now 1-d': 'now 1-d',
    '30d': 'today 1-m',
    '1m': 'today 1-m',
    'today 1-m': 'today 1-m',
}
PERIOD_LABELS = {
    'now 1-d': 'Past 24 hours',
    'now 7-d': 'Past 7 days',
    'today 1-m': 'Past 30 days',
}
PHRASE_TRANSLATIONS = {
    'iran': '伊朗',
    'california': '加州',
    'openai': 'OpenAI',
    'sora': 'Sora',
}
PLAYWRIGHT_COLLECTOR_SCRIPT = r'''import json
import os
import re
import sys
from pathlib import Path
from playwright.sync_api import sync_playwright

out_dir = Path(sys.argv[1])
trends_url = sys.argv[2]
chromium_path = sys.argv[3]
period_label = sys.argv[4]
out_dir.mkdir(parents=True, exist_ok=True)
proxy_settings = json.loads(
    os.environ.get('GOOGLE_TRENDS_PROXY_SETTINGS')
    or os.environ.get('GOOGLE_TRENDS_RADAR_PROXY_SETTINGS')
    or 'null'
)

with sync_playwright() as p:
    launch_kwargs = {'headless': True}
    if chromium_path:
        launch_kwargs['executable_path'] = chromium_path
    if proxy_settings:
        launch_kwargs['proxy'] = proxy_settings
    browser = p.chromium.launch(**launch_kwargs)
    context = browser.new_context(accept_downloads=True)
    page = context.new_page()
    page.set_default_timeout(30000)
    try:
        page.goto(trends_url, wait_until='commit', timeout=30000)
    except Exception:
        pass
    try:
        page.wait_for_load_state('domcontentloaded', timeout=10000)
    except Exception:
        pass
    try:
        page.wait_for_load_state('networkidle', timeout=10000)
    except Exception:
        pass
    try:
        page.get_by_role('button', name=re.compile('got it', re.I)).click(timeout=3000)
    except Exception:
        pass
    applied_period_label = None
    if period_label:
        try:
            page.get_by_role('button', name=re.compile('select period', re.I)).click(timeout=5000)
            page.get_by_role('option', name=re.compile(re.escape(period_label), re.I)).click(timeout=5000)
            try:
                page.wait_for_load_state('networkidle', timeout=10000)
            except Exception:
                pass
        except Exception:
            pass
    try:
        applied_period_label = page.get_by_role('button', name=re.compile('select period', re.I)).inner_text(timeout=3000).strip()
    except Exception:
        applied_period_label = None
    with page.expect_download(timeout=45000) as dl_info:
        page.get_by_role('button', name=re.compile('export', re.I)).click()
        page.get_by_role('menuitem', name=re.compile('download csv', re.I)).click()
    download = dl_info.value
    dest = out_dir / 'trending_us.csv'
    download.save_as(dest)
    print(json.dumps({
        'download_path': str(dest),
        'suggested_filename': download.suggested_filename,
        'period_label': period_label,
        'applied_period_label': applied_period_label,
    }, ensure_ascii=False))
    browser.close()
'''


def normalize_time_range(value):
    normalized = (value or '').strip().lower() or None
    return TIME_RANGE_ALIASES.get(normalized, DEFAULT_TIME_RANGE)


def period_label_for_time_range(value):
    normalized = normalize_time_range(value)
    return PERIOD_LABELS.get(normalized, 'Past 7 days')


def parse_started_at(value):
    if not value:
        return None
    dt = datetime.strptime(value, '%B %d, %Y at %I:%M:%S %p UTC')
    return dt.replace(tzinfo=timezone.utc).isoformat()


def split_breakdown(value):
    if not value:
        return []
    return [item.strip() for item in value.split(',') if item.strip()]


def translate_keyword_zh(keyword):
    translated = radar_shared.translate_keyword_zh(keyword)
    tokens = []
    for token in translated.split():
        tokens.append(PHRASE_TRANSLATIONS.get(token.lower(), token))
    return ' '.join(tokens)


def parse_trending_csv(csv_text, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE):
    reader = csv.DictReader(StringIO(csv_text))
    rows = []
    for row in reader:
        keyword = (row.get('Trends') or '').strip()
        if not keyword:
            continue
        rows.append({
            'keyword': keyword,
            'normalized': radar_shared.normalize_keyword(keyword),
            'translation_zh': translate_keyword_zh(keyword),
            'search_volume': (row.get('Search volume') or '').strip() or None,
            'started_at': parse_started_at((row.get('Started') or '').strip()),
            'ended_at': parse_started_at((row.get('Ended') or '').strip()),
            'trend_breakdown': split_breakdown((row.get('Trend breakdown') or '').strip()),
            'explore_link': (row.get('Explore link') or '').strip() or None,
            'geo': geo,
            'time_range': time_range,
            'source': 'browser_csv',
        })
    return rows


def build_list_payload(rows, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE):
    return {
        'geo': geo,
        'time_range': time_range,
        'count': len(rows),
        'items': rows,
    }


def resolve_python_bin(python_bin=None):
    if python_bin:
        return python_bin
    if getattr(sys, 'executable', None):
        return sys.executable
    return 'python3'


def resolve_chromium_path(chromium_path=None):
    if chromium_path:
        return chromium_path
    if sys.platform == 'win32':
        matches = sorted(glob(os.path.expandvars(r'%USERPROFILE%\AppData\Local\ms-playwright\chromium-*\chrome-win64\chrome.exe')))
        if matches:
            return matches[-1]
    env_path = os.environ.get('PLAYWRIGHT_CHROMIUM_EXECUTABLE_PATH', '').strip()
    if env_path:
        return env_path
    return DEFAULT_CHROMIUM_PATH


def build_playwright_proxy_settings(proxy_url=None):
    proxy_url = proxy_url or radar_shared.resolve_proxy_url()
    if not proxy_url:
        return None
    parsed = urlparse(proxy_url)
    if not parsed.scheme or not parsed.hostname:
        raise ValueError('proxy url must include scheme and hostname')
    server = f'{parsed.scheme}://{parsed.hostname}'
    if parsed.port:
        server = f'{server}:{parsed.port}'
    settings = {'server': server}
    if parsed.username:
        settings['username'] = unquote(parsed.username)
    if parsed.password:
        settings['password'] = unquote(parsed.password)
    return settings


def run_browser_csv_download(python_bin=None, chromium_path=None, output_dir=None, trends_url=DEFAULT_TRENDS_URL, period_label='Past 7 days', proxy_url=None):
    python_bin = resolve_python_bin(python_bin)
    chromium_path = resolve_chromium_path(chromium_path)
    chromium_arg = chromium_path or ''
    if output_dir is None:
        output_dir = tempfile.mkdtemp(prefix='trends-us-')
    cmd = [python_bin, '-', str(output_dir), trends_url, chromium_arg, period_label]
    if python_bin.endswith('.py'):
        cmd = [sys.executable, python_bin, '-', str(output_dir), trends_url, chromium_arg, period_label]
    elif python_bin.endswith('.sh'):
        cmd = ['bash', python_bin, '-', str(output_dir), trends_url, chromium_arg, period_label]
    elif sys.platform == 'win32' and os.path.splitext(python_bin)[1] == '':
        # On Windows a non-.exe launcher may still be a script; let PATHEXT/Shell resolve it.
        cmd = [python_bin, '-', str(output_dir), trends_url, chromium_arg, period_label]
    env = os.environ.copy()
    proxy_settings = build_playwright_proxy_settings(proxy_url)
    if proxy_settings:
        env['GOOGLE_TRENDS_PROXY_SETTINGS'] = json.dumps(proxy_settings, ensure_ascii=False)
        env['GOOGLE_TRENDS_RADAR_PROXY_SETTINGS'] = json.dumps(proxy_settings, ensure_ascii=False)
    else:
        env.pop('GOOGLE_TRENDS_PROXY_SETTINGS', None)
        env.pop('GOOGLE_TRENDS_RADAR_PROXY_SETTINGS', None)
    result = subprocess.run(
        cmd,
        input=PLAYWRIGHT_COLLECTOR_SCRIPT,
        text=True,
        capture_output=True,
        check=True,
        env=env,
    )
    stdout = result.stdout.strip().splitlines()
    if not stdout:
        raise RuntimeError('browser collector returned empty stdout')
    return json.loads(stdout[-1])


def collect_list(geo=DEFAULT_GEO, time_range='7d', python_bin=None, chromium_path=None, output_dir=None, proxy_url=None):
    if geo != 'US':
        raise ValueError('collect_list currently supports US only')
    normalized_range = normalize_time_range(time_range)
    period_label = period_label_for_time_range(normalized_range)
    meta = run_browser_csv_download(
        python_bin=python_bin,
        chromium_path=chromium_path,
        output_dir=output_dir,
        trends_url=DEFAULT_TRENDS_URL,
        period_label=period_label,
        proxy_url=proxy_url,
    )
    csv_path = Path(meta['download_path'])
    csv_text = csv_path.read_text(encoding='utf-8')
    rows = parse_trending_csv(csv_text, geo=geo, time_range=normalized_range)
    payload = build_list_payload(rows, geo=geo, time_range=normalized_range)
    applied_period_label = meta.get('applied_period_label') or period_label
    payload['download_path'] = str(csv_path)
    payload['period_label'] = period_label
    payload['applied_period_label'] = applied_period_label
    payload['period_applied'] = applied_period_label == period_label
    return payload
