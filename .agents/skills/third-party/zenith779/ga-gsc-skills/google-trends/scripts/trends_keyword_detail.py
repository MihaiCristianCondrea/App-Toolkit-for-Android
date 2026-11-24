import json
import importlib.util
import time
from datetime import datetime, timezone
from pathlib import Path
from urllib.parse import urlencode

import requests

BASE_DIR = Path(__file__).resolve().parent
TRENDS_CORE_PATH = BASE_DIR / 'trends.py'
LEGACY_RADAR_PATH = BASE_DIR / 'radar.py'
RADAR_PATH = TRENDS_CORE_PATH if TRENDS_CORE_PATH.exists() else LEGACY_RADAR_PATH
spec = importlib.util.spec_from_file_location('radar_shared', RADAR_PATH)
radar_shared = importlib.util.module_from_spec(spec)
spec.loader.exec_module(radar_shared)

LIST_PATH = BASE_DIR / 'trends_us_list.py'
list_spec = importlib.util.spec_from_file_location('trends_list_shared', LIST_PATH)
trends_list_shared = importlib.util.module_from_spec(list_spec)
list_spec.loader.exec_module(trends_list_shared)

DEFAULT_GEO = 'US'
DEFAULT_TIME_RANGE = 'now 7-d'
EXPLORE_URL = 'https://trends.google.com/trends/api/explore'
MULTILINE_URL = 'https://trends.google.com/trends/api/widgetdata/multiline'
RELATED_SEARCHES_URL = 'https://trends.google.com/trends/api/widgetdata/relatedsearches'
DETAIL_LIMITATION_TEMPLATE = 'Keyword detail collection failed: {error}'
DETAIL_RETRY_DELAYS = [2, 5, 10]


def strip_xssi_prefix(text):
    return text.lstrip(")]}',\n")


def read_trends_json(url, params=None, session=None, headers=None):
    session = session or radar_shared.build_requests_session()
    resp = session.get(url, params=params, timeout=30, headers=headers)
    resp.raise_for_status()
    return json.loads(strip_xssi_prefix(resp.text))


def read_trends_json_with_retries(url, params=None, session=None, headers=None, retry_delays=None):
    retry_delays = DETAIL_RETRY_DELAYS if retry_delays is None else retry_delays
    last_error = None
    for index in range(len(retry_delays) + 1):
        try:
            return read_trends_json(url, params=params, session=session, headers=headers)
        except requests.HTTPError as exc:
            last_error = exc
            status_code = getattr(getattr(exc, 'response', None), 'status_code', None)
            message = str(exc)
            if status_code != 429 and '429' not in message:
                raise
            if index >= len(retry_delays):
                raise
            time.sleep(retry_delays[index])
    raise last_error


def build_explore_request(keyword, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE):
    return {
        'comparisonItem': [
            {
                'keyword': radar_shared.normalize_keyword(keyword),
                'geo': geo,
                'time': time_range,
            }
        ],
        'category': 0,
        'property': '',
    }


def find_widget(widgets, widget_id):
    for widget in widgets:
        if widget.get('id') == widget_id:
            return widget
    raise ValueError(f'missing widget: {widget_id}')


def build_explore_referer(keyword, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE):
    return f'https://trends.google.com/trends/explore?geo={geo}&date={time_range.replace(" ", "%20")}&q={keyword.replace(" ", "%20")}'


def warmup_explore_page(keyword, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE, session=None):
    session = session or radar_shared.build_requests_session()
    referer = build_explore_referer(keyword, geo=geo, time_range=time_range)
    resp = session.get(referer, timeout=30)
    return resp.status_code


def parse_interest_over_time(data):
    points = []
    for item in data.get('default', {}).get('timelineData', []):
        values = item.get('value') or []
        if not values:
            continue
        ts = int(item['time'])
        points.append({
            'date': datetime.fromtimestamp(ts, tz=timezone.utc).isoformat(),
            'value': values[0],
        })
    return points


def parse_related_queries(data):
    rows = []
    for ranked_list in data.get('default', {}).get('rankedList', []):
        ranking_type = ranked_list.get('rankingType') or ranked_list.get('name') or 'UNKNOWN'
        for item in ranked_list.get('rankedKeyword', []):
            query = (item.get('query') or '').strip()
            if not query:
                continue
            rows.append({
                'query': query,
                'value': item.get('value'),
                'type': ranking_type,
            })
    return rows


def fetch_keyword_detail_data(keyword, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE, session=None):
    session = session or radar_shared.build_requests_session()
    req = build_explore_request(keyword, geo=geo, time_range=time_range)
    referer = build_explore_referer(keyword, geo=geo, time_range=time_range)
    warmup_explore_page(keyword, geo=geo, time_range=time_range, session=session)
    explore = read_trends_json(
        EXPLORE_URL,
        params={
            'hl': 'en-US',
            'tz': 0,
            'req': json.dumps(req, separators=(',', ':')),
        },
        session=session,
        headers={'Referer': referer, 'X-Requested-With': 'XMLHttpRequest'},
    )
    widgets = explore.get('widgets', [])
    timeseries_widget = find_widget(widgets, 'TIMESERIES')
    related_widget = find_widget(widgets, 'RELATED_QUERIES')
    interest_over_time = []
    related_queries = []
    errors = []
    try:
        interest = read_trends_json_with_retries(
            MULTILINE_URL,
            params={
                'hl': 'en-US',
                'tz': 0,
                'req': json.dumps(timeseries_widget['request'], separators=(',', ':')),
                'token': timeseries_widget['token'],
            },
            session=session,
            headers={'Referer': referer, 'X-Requested-With': 'XMLHttpRequest'},
        )
        interest_over_time = parse_interest_over_time(interest)
    except Exception as exc:
        errors.append(f'interest_over_time failed: {exc}')
    try:
        related = read_trends_json_with_retries(
            RELATED_SEARCHES_URL,
            params={
                'hl': 'en-US',
                'tz': 0,
                'req': json.dumps(related_widget['request'], separators=(',', ':')),
                'token': related_widget['token'],
            },
            session=session,
            headers={'Referer': referer, 'X-Requested-With': 'XMLHttpRequest'},
        )
        related_queries = parse_related_queries(related)
    except Exception as exc:
        errors.append(f'related_queries failed: {exc}')
    return {
        'interest_over_time': interest_over_time,
        'related_queries': related_queries,
        'errors': errors,
    }


def build_interest_summary(points):
    values = [point['value'] for point in points] if points else []
    if not values:
        return {
            'points': 0,
            'peak_value': 0,
            'latest_value': 0,
        }
    return {
        'points': len(points),
        'peak_value': max(values),
        'latest_value': values[-1],
    }


def build_detail_payload(keyword, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE, interest_over_time=None, related_queries=None, limitation=None):
    interest_over_time = interest_over_time or []
    related_queries = related_queries or []
    payload = {
        'keyword': radar_shared.normalize_keyword(keyword),
        'translation_zh': trends_list_shared.translate_keyword_zh(keyword),
        'geo': geo,
        'time_range': time_range,
        'interest_over_time': interest_over_time,
        'interest_summary': build_interest_summary(interest_over_time),
        'related_queries': related_queries,
    }
    if limitation:
        payload['limitation'] = limitation
    return payload


def collect_keyword_detail(keyword, geo=DEFAULT_GEO, time_range=DEFAULT_TIME_RANGE):
    try:
        data = fetch_keyword_detail_data(keyword, geo=geo, time_range=time_range)
        return build_detail_payload(
            keyword=keyword,
            geo=geo,
            time_range=time_range,
            interest_over_time=data['interest_over_time'],
            related_queries=data['related_queries'],
            limitation='; '.join(data['errors']) if data.get('errors') else None,
        )
    except Exception as exc:
        return build_detail_payload(
            keyword=keyword,
            geo=geo,
            time_range=time_range,
            interest_over_time=[],
            related_queries=[],
            limitation=DETAIL_LIMITATION_TEMPLATE.format(error=str(exc)),
        )
