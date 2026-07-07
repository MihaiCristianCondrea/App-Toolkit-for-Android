# Versioning Strategy

This project uses a structured versioning scheme to ensure unique, monotonically increasing version
codes and human-readable version names.

## 1. Version Code (`versionCode`)

The version code is a technical identifier for Google Play. It follows the scheme: **`P SS UUUU`**

* **P** (Product Family):
    * `1`: Phone / Tablet
    * `2`: Android TV
    * `3`: Wear OS
* **SS** (Target SDK):
    * The Android API level (e.g., `35`, `37`).
* **UUUU** (Upload Counter):
    * A 4-digit global counter that increments with every build uploaded to the Play Store.

### Example Calculation

| Product   | SDK | Upload | Resulting `versionCode` |
|:----------|:----|:-------|:------------------------|
| Phone (1) | 37  | 1      | `1,370,001`             |
| Phone (1) | 37  | 42     | `1,370,042`             |
| TV (2)    | 37  | 5      | `2,370,005`             |

---

## 2. Version Name (`versionName`)

The version name is human-readable and displayed to users. It follows the scheme: **`YY.MM.UUUU`**

* **YY**: Current year (last two digits, e.g., `26`).
* **MM**: Current month (01-12).
* **UUUU**: The same upload counter used in the version code.

### Example

* **July 2026, Upload #42**: `26.07.42`

---

## 3. How to Release a New Version

To release a new version of the app, follow these steps in `release.properties`:

1. **Check SDKs**: Ensure `MIN_SDK`, `TARGET_SDK`, and `COMPILE_SDK` are correct.
2. **Increment Upload**: Increase `PHONE_UPLOAD` by at least 1.
    * *Note: Never decrease this value once a build has been uploaded to Google Play.*
3. **Sync Gradle**: Gradle will automatically calculate the new `versionCode` and `versionName`
   based on the updated properties and the current system date.

```properties
# release.properties example
TARGET_SDK=37
PHONE_UPLOAD=43  # Increased from 42
```
