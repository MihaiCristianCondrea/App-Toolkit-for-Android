"""Run with: python -m unittest discover -s tests -v"""

import importlib.util
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[1]
VALIDATOR = ROOT / "scripts" / "validate_release_notes.py"
spec = importlib.util.spec_from_file_location("release_notes_validator", VALIDATOR)
validator = importlib.util.module_from_spec(spec)
sys.modules[spec.name] = validator
spec.loader.exec_module(validator)


class ParseAndValidateTest(unittest.TestCase):
    def test_valid_console_two_locales(self):
        text = "<en-US>\nAdded\n• Search.\n</en-US>\n<ro>\nAdăugate\n• Căutare.\n</ro>\n"
        mode, results, errors = validator.validate(text)
        self.assertEqual("console", mode)
        self.assertFalse(errors)
        self.assertEqual(["en-US", "ro"], [r.locale for r in results])
        self.assertTrue(all(r.status == "ok" for r in results))
        self.assertEqual(len("Added\n• Search."), results[0].chars)

    def test_exactly_499_is_valid_by_skill(self):
        _, results, errors = validator.validate("x" * 499, mode="plain")
        self.assertFalse(errors)
        self.assertEqual("ok", results[0].status)
        self.assertEqual(499, results[0].chars)

    def test_500_is_rejected_by_skill_default(self):
        _, results, _ = validator.validate("x" * 500, mode="plain")
        self.assertEqual("error", results[0].status)
        self.assertIn("exceeds 499", results[0].errors[0])

    def test_500_is_valid_when_google_limit_requested(self):
        _, results, _ = validator.validate("x" * 500, mode="plain", max_chars=500)
        self.assertEqual("ok", results[0].status)

    def test_501_exceeds_google_limit(self):
        _, results, _ = validator.validate("x" * 501, mode="plain", max_chars=500)
        self.assertEqual("error", results[0].status)

    def test_newlines_are_counted(self):
        text = "<en-US>\na\nb\n\n</en-US>"
        _, results, errors = validator.validate(text, mode="console")
        self.assertFalse(errors)
        self.assertEqual(len("a\nb\n"), results[0].chars)

    def test_console_excludes_wrapper_delimiters(self):
        text = "<en-US>\nHi\n</en-US>"
        _, results, _ = validator.validate(text, mode="console")
        self.assertEqual(2, results[0].chars)

    def test_crlf_is_normalized(self):
        text = "<en-US>\r\nAdded\r\n• Hi\r\n</en-US>\r\n"
        _, results, errors = validator.validate(text, mode="console")
        self.assertFalse(errors)
        self.assertEqual(len("Added\n• Hi"), results[0].chars)

    def test_emojis_count_as_multiple_code_points(self):
        text = "👩‍💻"
        _, results, _ = validator.validate(text, mode="plain")
        self.assertEqual(3, results[0].chars)

    def test_romanian_diacritics_are_counted(self):
        text = "Știri, îmbunătățiri și corecții"
        _, results, _ = validator.validate(text, mode="plain")
        self.assertEqual(len(text), results[0].chars)

    def test_duplicate_locale_is_error(self):
        text = "<en-US>\na\n</en-US>\n<en-US>\nb\n</en-US>"
        _, results, errors = validator.validate(text, mode="console")
        self.assertEqual(2, len(results))
        self.assertTrue(any("duplicate" in e for e in errors))

    def test_mismatched_closing_tag(self):
        text = "<en-US>\na\n</ro>"
        _, _, errors = validator.validate(text, mode="console")
        self.assertTrue(any("does not match" in e for e in errors))
        self.assertTrue(any("missing" in e for e in errors))

    def test_unclosed_tag_is_error(self):
        _, _, errors = validator.validate("<en-US>\nhello", mode="console")
        self.assertTrue(any("missing" in e for e in errors))

    def test_content_outside_locale_is_error(self):
        text = "Note outside\n<en-US>\nValid\n</en-US>"
        _, _, errors = validator.validate(text, mode="console")
        self.assertTrue(any("outside" in e for e in errors))

    def test_plain_mode_rejects_tags(self):
        _, _, errors = validator.validate("<en-US>\nNote\n</en-US>", mode="plain")
        self.assertTrue(any("locale tags" in e for e in errors))

    def test_empty_note_is_error(self):
        _, results, _ = validator.validate("<en-US>\n\n</en-US>", mode="console")
        self.assertEqual("error", results[0].status)

    def test_short_notes_are_allowed(self):
        _, results, _ = validator.validate("Fixed crashes.", mode="plain")
        self.assertEqual("ok", results[0].status)
        self.assertTrue(any("do not pad" in w for w in results[0].warnings))

    def test_required_locales(self):
        _, _, errors = validator.validate(
            "<en-US>\nHello\n</en-US>", mode="console", required_locales=["en-US", "ro"]
        )
        self.assertTrue(any("missing locales: ro" in e for e in errors))

    def test_required_locales_with_plain_mode(self):
        _, _, errors = validator.validate("hello", mode="plain", required_locales=["en-US"])
        self.assertTrue(any("requires console mode" in e for e in errors))

    def test_nested_tag_is_error(self):
        text = "<en-US>\nHi\n<ro>\nWorld\n</en-US>"
        _, _, errors = validator.validate(text, mode="console")
        self.assertTrue(any("nested" in e for e in errors))

    def test_multilanguage_limit_is_independent(self):
        text = "<en-US>\n" + "a" * 499 + "\n</en-US>\n<ro>\n" + "x" * 500 + "\n</ro>"
        _, results, _ = validator.validate(text, mode="console")
        self.assertEqual(["ok", "error"], [r.status for r in results])

    def test_trailing_file_newline_is_excluded_plain(self):
        _, results, errors = validator.validate("Hello\n", mode="plain")
        self.assertFalse(errors)
        self.assertEqual(5, results[0].chars)

    def test_extra_trailing_blank_line_is_counted_plain(self):
        _, results, _ = validator.validate("Hello\n\n", mode="plain")
        self.assertEqual(6, results[0].chars)

    def test_bundled_examples_pass(self):
        text = (ROOT / "examples" / "play-console.txt").read_text(encoding="utf-8")
        _, results, errors = validator.validate(text, mode="console", required_locales=["en-US", "ro"])
        self.assertFalse(errors)
        self.assertEqual(2, len(results))
        self.assertTrue(all(item.status == "ok" for item in results))
        for locale in ("en-US", "ro"):
            body = (ROOT / "examples" / "fastlane" / locale / "100100.txt").read_text(encoding="utf-8")
            _, result, error = validator.validate(body, mode="plain")
            self.assertFalse(error)
            self.assertEqual("ok", result[0].status)

    def test_cli_json_output(self):
        script_output = subprocess.run(
            [sys.executable, str(VALIDATOR), str(ROOT / "examples" / "play-console.txt"),
             "--json", "--required-locales", "en-US,ro"],
            capture_output=True, text=True, check=False,
        )
        self.assertEqual(0, script_output.returncode, script_output.stderr)
        import json
        data = json.loads(script_output.stdout)
        self.assertTrue(data["valid"])

    def test_cli_exit_nonzero_on_overflow(self):
        with tempfile.TemporaryDirectory() as d:
            f = Path(d) / "note.txt"
            f.write_text("x" * 500, encoding="utf-8")
            result = subprocess.run([sys.executable, str(VALIDATOR), str(f)],
                                    capture_output=True, text=True, check=False)
            self.assertEqual(1, result.returncode)


if __name__ == "__main__":
    unittest.main()
