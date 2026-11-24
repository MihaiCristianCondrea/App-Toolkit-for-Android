import importlib.util
import json
import os
import re
import sys
import urllib.request
import xml.etree.ElementTree as ET
from datetime import datetime, timedelta, timezone
from pathlib import Path

import requests

ROOT_DIR = Path(__file__).resolve().parents[2]
if str(ROOT_DIR) not in sys.path:
    sys.path.insert(0, str(ROOT_DIR))

from shared.env_loader import (
    discover_default_env_file as shared_discover_default_env_file,
    load_project_env as shared_load_project_env,
)

BASE_DIR = Path(__file__).resolve().parent
SKILL_DIR = BASE_DIR.parent
STATE_DIR = SKILL_DIR / "state"
STATE_DIR.mkdir(parents=True, exist_ok=True)

SEEN_PATH = STATE_DIR / "seen_keywords.json"
WATCH_PATH = STATE_DIR / "watchlist.json"
NOTIFIED_PATH = STATE_DIR / "notified.json"
LATEST_PATH = STATE_DIR / "latest_report.json"

GEOS = ["US", "GB", "CA", "AU", "IN"]
TREND_URL = "https://trends.google.com/trending/rss?geo={geo}"
COOLDOWN_DAYS = 3
P1_LIMIT = 3
DEFAULT_PROXY_URL = None
DEFAULT_SOURCE_MODE = "csv_only"
DEFAULT_REQUEST_HEADERS = [
    ("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36"),
    ("Accept-Language", "en-US,en;q=0.9"),
]


def discover_default_env_file() -> Path:
    return shared_discover_default_env_file()


DEFAULT_ENV_FILE = discover_default_env_file()


def load_project_env(env_file=None) -> Path:
    return shared_load_project_env(env_file, default_env_file=DEFAULT_ENV_FILE)


load_project_env()

INTENT_PATTERNS = {
    "question": ["how to", "why", "what is", "how does", "can i", "best way to", "how do i"],
    "tool": ["generator", "checker", "converter", "calculator", "editor", "formatter", "summarizer", "remover"],
    "decision": ["vs", "alternative", "review", "compare", "pricing", "best", "top"],
    "template": ["template", "examples", "prompt", "sample", "format"],
}

BLACKLIST = [
    "score", "result", "live score", "match", "fc", "nba", "nfl", "mlb", "ufc",
    "celebrity", "gossip", "dating", "boyfriend", "girlfriend", "wife", "husband", "leaked", "scandal",
    "horoscope", "lottery",
]

SPORTS_TERMS = [
    "vs", "versus", "timberwolves", "clippers", "lakers", "warriors", "nuggets", "rockets", "hornets",
    "celtics", "heat", "arsenal", "chelsea", "man utd", "real madrid", "barcelona", "flamengo", "cruzeiro",
    "djokovic", "wimbledon", "nba finals", "premier league", "indian wells",
]

ENTERTAINMENT_TERMS = [
    "tour", "album", "lyrics", "concert", "episode", "netflix", "celebrity", "movie", "trailer",
]

PERSON_EVENT_TERMS = [
    "adelaide", "sydney", "melbourne", "delhi", "miami", "oscar", "grammy", "festival", "tour", "concert",
    "quarterfinal", "semifinal", "final", "open", "championship",
]

KNOWN_PERSON_OR_BAND_TERMS = [
    "linkin park", "kawhi leonard", "novak djokovic", "avril lavigne", "jack osbourne", "elena rybakina",
    "cameron johnson",
]

NON_SITE_TERMS = [
    "power outage", "outage near me", "share price", "share", "stock price", "layoffs", "weather",
    "tariffs", "near me", "school", "tonight",
]

CAUTION = ["election", "earthquake", "war", "shooting", "death", "funeral", "weather"]

NOUN_HINTS = ["ai", "gpt", "model", "api", "app", "studio", "agent", "workflow", "feature", "launch"]

SITE_TYPES = {
    "question": "Tutorial / FAQ page",
    "tool": "Tool page / single-purpose site",
    "decision": "Comparison / review page",
    "template": "Template / example page",
    "noun": "Tutorial / directory / glossary page",
}

TRANSLATION_MAP = {
    "ai": "AI",
    "image": "图片",
    "generator": "生成器",
    "model": "模型",
    "review": "评测",
    "how to": "如何",
    "what is": "是什么",
    "why": "为什么",
    "vs": "对比",
    "alternative": "替代方案",
    "template": "模板",
    "examples": "示例",
    "prompt": "提示词",
    "checker": "检查器",
    "converter": "转换器",
    "api": "接口",
    "app": "应用",
    "agent": "智能体",
    "workflow": "工作流",
    "feature": "功能",
    "launch": "发布",
    "pricing": "价格",
    "best": "最佳",
    "top": "Top",
}

OPPORTUNITY_SUFFIXES = [
    ("What is", "what is {keyword}", "Glossary / introductory page"),
    ("Tutorial", "how to use {keyword}", "Tutorial / getting-started guide"),
    ("Review", "{keyword} review", "Review / hands-on page"),
    ("Pricing", "{keyword} pricing", "Pricing / pricing comparison page"),
    ("Alternatives", "{keyword} alternatives", "Alternatives aggregation page"),
    ("Examples", "{keyword} examples", "Examples / case-study page"),
]


def now_utc():
    return datetime.now(timezone.utc)


def iso(dt):
    return dt.astimezone(timezone.utc).replace(microsecond=0).isoformat()


def load_json(path, default):
    if not path.exists():
        return default
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except Exception:
        return default


def save_json(path, data):
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")


def normalize_keyword(text):
    text = text.strip().lower()
    text = re.sub(r"\s+", " ", text)
    return text


def get_env_with_legacy(*keys):
    for key in keys:
        value = os.environ.get(key, "").strip()
        if value:
            return value
    return ""


def resolve_proxy_url():
    value = get_env_with_legacy(
        "GOOGLE_TRENDS_PROXY",
        "GOOGLE_TRENDS_RADAR_PROXY",
    )
    if value:
        return value
    return DEFAULT_PROXY_URL


def resolve_source_mode():
    value = get_env_with_legacy(
        "GOOGLE_TRENDS_SOURCE_MODE",
        "GOOGLE_TRENDS_RADAR_SOURCE_MODE",
    ).lower()
    if value in {"csv_only", "csv_plus_rss", "rss_only"}:
        return value
    return DEFAULT_SOURCE_MODE


def build_url_opener(proxy_url=None):
    proxy = proxy_url or resolve_proxy_url()
    proxy_map = {"http": proxy, "https": proxy} if proxy else {}
    handler = urllib.request.ProxyHandler(proxy_map)
    opener = urllib.request.build_opener(handler)
    opener.addheaders = DEFAULT_REQUEST_HEADERS
    return opener


def build_requests_session(proxy_url=None):
    proxy = proxy_url or resolve_proxy_url()
    session = requests.Session()
    session.trust_env = False
    if proxy:
        session.proxies.update({"http": proxy, "https": proxy})
    session.headers.update(dict(DEFAULT_REQUEST_HEADERS))
    return session


def fetch_geo(geo, opener=None, session=None):
    url = TREND_URL.format(geo=geo)
    if session is not None:
        resp = session.get(url, timeout=20)
        resp.raise_for_status()
        xml_bytes = resp.content
    else:
        opener = opener or build_url_opener()
        with opener.open(url, timeout=20) as resp:
            xml_bytes = resp.read()
    root = ET.fromstring(xml_bytes)
    items = []
    for item in root.findall("./channel/item"):
        title = (item.findtext("title") or "").strip()
        if title:
            items.append({"keyword": title, "geo": geo, "source": url})
    return items


def load_csv_trends():
    module_path = BASE_DIR / 'trends_us_list.py'
    spec = importlib.util.spec_from_file_location('trends_us_list_runtime', module_path)
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    payload = module.collect_list(geo='US', time_range='7d')
    items = [
        {
            'keyword': item['keyword'],
            'geo': item['geo'],
            'source': 'us_csv',
        }
        for item in payload.get('items', [])
    ]
    errors = []
    if not payload.get('period_applied', True):
        errors.append({
            'geo': 'US',
            'error': f"CSV period mismatch: expected {payload.get('period_label')} got {payload.get('applied_period_label')}",
        })
    return items, errors


def load_rss_trends(source_label='rss'):
    results = []
    errors = []
    session = build_requests_session()
    for geo in GEOS:
        try:
            items = fetch_geo(geo, session=session)
            for item in items:
                item['source'] = source_label
            results.extend(items)
        except Exception as e:
            errors.append({"geo": geo, "error": str(e)})
    return results, errors


def collect_trends():
    results = []
    errors = []
    source_mode = resolve_source_mode()
    csv_failed = False
    if source_mode in {'csv_only', 'csv_plus_rss'}:
        try:
            csv_items, csv_errors = load_csv_trends()
            results.extend(csv_items)
            errors.extend(csv_errors)
        except Exception as e:
            csv_failed = True
            errors.append({"geo": "US", "error": f"CSV fetch failed: {e}"})
    if source_mode == 'rss_only':
        rss_items, rss_errors = load_rss_trends(source_label='rss')
        results.extend(rss_items)
        errors.extend(rss_errors)
    elif source_mode == 'csv_plus_rss':
        rss_items, rss_errors = load_rss_trends(source_label='rss')
        results.extend(rss_items)
        errors.extend(rss_errors)
    elif source_mode == 'csv_only' and csv_failed:
        rss_items, rss_errors = load_rss_trends(source_label='rss_fallback')
        results.extend(rss_items)
        errors.extend(rss_errors)
    return results, errors


def is_blacklisted(keyword):
    return any(term in keyword for term in BLACKLIST)


def is_non_site_keyword(keyword):
    return any(term in keyword for term in NON_SITE_TERMS)


def is_noise_keyword(keyword):
    sports_hits = [term for term in SPORTS_TERMS if term in keyword]
    entertainment_hits = [term for term in ENTERTAINMENT_TERMS if term in keyword]
    person_event_hits = [term for term in PERSON_EVENT_TERMS if term in keyword]
    known_person_hits = [term for term in KNOWN_PERSON_OR_BAND_TERMS if term in keyword]
    if len(sports_hits) >= 2:
        return True, "Matched sports / entertainment noise filter"
    if sports_hits and any(term in keyword for term in ["vs", "versus"]):
        return True, "Matched sports / entertainment noise filter"
    if len(entertainment_hits) >= 2:
        return True, "Matched sports / entertainment noise filter"
    if known_person_hits:
        return True, "Matched person / show / event noise filter"
    if person_event_hits and len(keyword.split()) <= 4 and not noun_hint_hits(keyword):
        return True, "Matched person / show / event noise filter"
    return False, ""


def caution_hits(keyword):
    return [term for term in CAUTION if term in keyword]


def match_intents(keyword):
    hits = {}
    for name, patterns in INTENT_PATTERNS.items():
        found = [p for p in patterns if p in keyword]
        if found:
            hits[name] = found
    return hits


def noun_hint_hits(keyword):
    return [term for term in NOUN_HINTS if term in keyword]


def translate_keyword_zh(keyword):
    result = keyword
    for en in sorted(TRANSLATION_MAP.keys(), key=len, reverse=True):
        zh = TRANSLATION_MAP[en]
        result = re.sub(rf"\b{re.escape(en)}\b", zh, result, flags=re.IGNORECASE)
    return result


def format_keyword_for_display(item):
    return item["keyword"]


def expand_keyword_opportunities(keyword, category):
    norm = normalize_keyword(keyword)
    if category in {"New model name", "New product name", "Generic emerging keyword", "High tool-intent keyword"}:
        return [
            {
                "label": label,
                "query": pattern.format(keyword=norm),
                "site_type": site_type,
            }
            for label, pattern, site_type in OPPORTUNITY_SUFFIXES
        ]
    return []


def score_keyword(keyword, regions_count, seen_count):
    score = 2  # newness baseline
    reasons = []
    if regions_count > 1:
        bonus = min(4, regions_count - 1)
        score += bonus
        reasons.append(f"Appeared across multiple regions +{bonus}")
    if seen_count > 1:
        score += 1
        reasons.append("Repeated within this run +1")

    intents = match_intents(keyword)
    if intents.get("question"):
        score += 4
        reasons.append("Strong question intent +4")
    if intents.get("tool"):
        score += 6
        reasons.append("Strong tool intent +6")
    if intents.get("decision"):
        score += 4
        reasons.append("Strong decision intent +4")
    if intents.get("template"):
        score += 4
        reasons.append("Strong template intent +4")

    noun_hits = noun_hint_hits(keyword)
    if noun_hits:
        score += 2
        reasons.append("Emerging noun potential +2")

    if noun_hits and regions_count >= 3:
        score += 3
        reasons.append("Emerging noun spreading across regions +3")
    elif noun_hits and regions_count >= 2:
        score += 2
        reasons.append("Emerging noun spreading across regions +2")

    caution = caution_hits(keyword)
    if caution:
        score -= 2
        reasons.append("News / event risk -2")

    return score, reasons, intents, noun_hits, caution


def classify_type(keyword, intents, noun_hits):
    if intents.get("tool"):
        return "High tool-intent keyword", SITE_TYPES["tool"]
    if intents.get("question"):
        return "High question-intent keyword", SITE_TYPES["question"]
    if intents.get("decision"):
        return "High decision-intent keyword", SITE_TYPES["decision"]
    if intents.get("template"):
        return "High template-intent keyword", SITE_TYPES["template"]
    if noun_hits:
        if "model" in noun_hits or "gpt" in noun_hits:
            return "New model name", SITE_TYPES["noun"]
        if "feature" in noun_hits:
            return "New platform feature name", SITE_TYPES["noun"]
        if "api" in noun_hits or "workflow" in noun_hits:
            return "New technical term", SITE_TYPES["noun"]
        return "New product name", SITE_TYPES["noun"]
    return "Generic emerging keyword", "Observation / glossary page"


def priority_from_score(score):
    if score >= 8:
        return "P1"
    if score >= 4:
        return "P2"
    return "P3"


def load_states():
    return (
        load_json(SEEN_PATH, {}),
        load_json(WATCH_PATH, {}),
        load_json(NOTIFIED_PATH, {}),
    )


def in_cooldown(notified, keyword, current_time):
    item = notified.get(keyword)
    if not item:
        return False
    try:
        until = datetime.fromisoformat(item["cooldown_until"])
        return until > current_time
    except Exception:
        return False


def scan():
    current = now_utc()
    seen, watchlist, notified = load_states()
    raw_items, errors = collect_trends()

    grouped = {}
    for item in raw_items:
        norm = normalize_keyword(item["keyword"])
        if not norm:
            continue
        bucket = grouped.setdefault(norm, {
            "keyword": item["keyword"],
            "normalized": norm,
            "regions": set(),
            "sources": set(),
            "count": 0,
        })
        bucket["regions"].add(item["geo"])
        bucket["sources"].add(item["source"])
        bucket["count"] += 1

    p1, p2, p3 = [], [], []
    for norm, item in grouped.items():
        if is_blacklisted(norm):
            record = {
                "keyword": item["keyword"],
                "normalized": norm,
                "priority": "P3",
                "reason": "Matched blacklist filter",
                "regions": sorted(item["regions"]),
            }
            p3.append(record)
            continue

        is_noise, noise_reason = is_noise_keyword(norm)
        if is_noise:
            record = {
                "keyword": item["keyword"],
                "normalized": norm,
                "priority": "P3",
                "reason": noise_reason,
                "regions": sorted(item["regions"]),
            }
            p3.append(record)
            continue

        if is_non_site_keyword(norm):
            record = {
                "keyword": item["keyword"],
                "normalized": norm,
                "priority": "P3",
                "reason": "Matched non-site-intent filter",
                "regions": sorted(item["regions"]),
            }
            p3.append(record)
            continue

        score, reasons, intents, noun_hits, caution = score_keyword(norm, len(item["regions"]), item["count"])
        kind, site_type = classify_type(norm, intents, noun_hits)
        priority = priority_from_score(score)

        prev_seen = seen.get(norm, {})
        first_seen = prev_seen.get("first_seen_at", iso(current))
        seen[norm] = {
            "keyword": item["keyword"],
            "first_seen_at": first_seen,
            "last_seen_at": iso(current),
            "seen_count": int(prev_seen.get("seen_count", 0)) + 1,
            "regions": sorted(set(prev_seen.get("regions", [])) | item["regions"]),
            "sources": sorted(set(prev_seen.get("sources", [])) | item["sources"]),
        }

        record = {
            "keyword": item["keyword"],
            "translation_zh": translate_keyword_zh(item["keyword"]),
            "normalized": norm,
            "priority": priority,
            "score": score,
            "category": kind,
            "site_type": site_type,
            "reasons": reasons,
            "regions": sorted(item["regions"]),
            "count": item["count"],
            "risk": "Higher news / event sensitivity" if caution else ("Brand-term site fit still unproven" if kind.startswith("New") and not intents else "Reasonably buildable"),
            "action": "Build a landing page now" if priority == "P1" else ("Move to watchlist and wait for derivative queries" if priority == "P2" else "Drop"),
            "opportunity_angles": expand_keyword_opportunities(item["keyword"], kind),
        }

        if priority == "P1":
            if not in_cooldown(notified, norm, current):
                p1.append(record)
                notified[norm] = {
                    "keyword": item["keyword"],
                    "notified_at": iso(current),
                    "notification_type": "instant",
                    "cooldown_until": iso(current + timedelta(days=COOLDOWN_DAYS)),
                    "summary_hash": f"{score}:{kind}:{','.join(sorted(item['regions']))}",
                }
            else:
                watchlist[norm] = {
                    "keyword": item["keyword"],
                    "category": kind,
                    "reason": "P1 during cooldown, moved to watchlist",
                    "score": score,
                    "watch_started_at": watchlist.get(norm, {}).get("watch_started_at", iso(current)),
                    "last_updated_at": iso(current),
                    "status": "watching",
                }
        elif priority == "P2":
            watchlist[norm] = {
                "keyword": item["keyword"],
                "category": kind,
                "reason": "Trend exists, but the signal is not strong enough yet",
                "score": score,
                "watch_started_at": watchlist.get(norm, {}).get("watch_started_at", iso(current)),
                "last_updated_at": iso(current),
                "status": "watching",
            }
            p2.append(record)
        else:
            p3.append(record)
            if norm in watchlist:
                watchlist[norm]["status"] = "dropped"
                watchlist[norm]["last_updated_at"] = iso(current)

    p1 = sorted(p1, key=lambda x: (-x["score"], x["keyword"]))[:P1_LIMIT]
    p2 = sorted(p2, key=lambda x: (-x["score"], x["keyword"]))
    p3 = sorted(p3, key=lambda x: x["keyword"])

    report = {
        "generated_at": iso(current),
        "errors": errors,
        "stats": {
            "raw_items": len(raw_items),
            "unique_keywords": len(grouped),
            "p1": len(p1),
            "p2": len(p2),
            "p3": len(p3),
        },
        "p1": p1,
        "p2": p2,
        "p3": p3,
    }

    save_json(SEEN_PATH, seen)
    save_json(WATCH_PATH, watchlist)
    save_json(NOTIFIED_PATH, notified)
    save_json(LATEST_PATH, report)
    return report


def summary():
    report = load_json(LATEST_PATH, {})
    if not report:
        return {"generated_at": iso(now_utc()), "message": "No scan result yet. Run scan first."}
    result = {
        "generated_at": iso(now_utc()),
        "today_do": report.get("p1", [])[:3],
        "watch": report.get("p2", [])[:10],
        "drop": report.get("p3", [])[:10],
        "stats": report.get("stats", {}),
    }
    return result


def print_scan_report(report):
    print(f"Google Trends scan completed @ {report['generated_at']}")
    print(f"Raw items: {report['stats']['raw_items']} | Unique keywords: {report['stats']['unique_keywords']} | P1: {report['stats']['p1']} | P2: {report['stats']['p2']} | P3: {report['stats']['p3']}")
    if report["errors"]:
        print("Fetch errors:")
        for err in report["errors"]:
            print(f"- {err['geo']}: {err['error']}")
    if report["p1"]:
        print("\n[P1 Notify Now]")
        for item in report["p1"]:
            print(f"- {format_keyword_for_display(item)} | {item['category']} | score={item['score']} | Site type: {item['site_type']} | Action: {item['action']} | Risk: {item['risk']}")
    else:
        print("\n[P1 Notify Now]\n- No new P1 items in this run")


def print_summary_report(report):
    print(f"Google Trends daily summary @ {report['generated_at']}")
    if report.get("message"):
        print(report["message"])
        return
    print("\n[Build Today]")
    if report["today_do"]:
        for item in report["today_do"]:
            print(f"- {format_keyword_for_display(item)} | {item['category']} | score={item['score']} | Site type: {item['site_type']}")
    else:
        print("- None")
    print("\n[Keep Watching]")
    if report["watch"]:
        for item in report["watch"][:10]:
            print(f"- {format_keyword_for_display(item)} | {item['category']} | score={item['score']}")
    else:
        print("- None")
    print("\n[Dropped Today]")
    if report["drop"]:
        for item in report["drop"][:10]:
            print(f"- {item['keyword']} | {item.get('reason', 'Dropped')}")
    else:
        print("- None")


def print_usage():
    print("Usage: python3 trends.py [scan|summary]")
    print("       python3 trends.py --help")


def main():
    if len(sys.argv) < 2:
        print_usage()
        sys.exit(1)
    command = sys.argv[1]
    if command in {"-h", "--help", "help"}:
        print_usage()
        sys.exit(0)
    if command not in {"scan", "summary"}:
        print_usage()
        sys.exit(1)
    if command == "scan":
        report = scan()
        print_scan_report(report)
    else:
        report = summary()
        print_summary_report(report)


if __name__ == "__main__":
    main()
