# YouTube description

Part of [`social-captions`](../SKILL.md). The file `captions/youtube.txt` holds only the description. The title is `copy.title` in the campaign manifest (under 60 characters, the product name first).

## Template

```text
I'm excited to present the official trailer for <Product> for Android. <Product> is built to <core purpose>. <One sentence listing what it helps you do, with active verbs.>

Discover more about <Product>:
Google Play: https://play.google.com/store/apps/details?id=<packageId>

Discover more about <Creator>:
<Profile name>: <profile URL>
<one line per profile, in the project block's order>

<10 to 15 hashtags>
```

## Rules

- The first two lines show above "Show more": the product name and what it does go there.
- 2 or 3 sentences of body, readable on a phone. Up to 5,000 characters is allowed; use far less.
- Add chapters (`0:00 Opening`, one per line, starting at 0:00) when the video is longer than a minute.
- Hashtags: the product's own first, then platform and category, then the audience's topics (see [`keywords.md`](keywords.md)), then the format (`#AppTrailer`, `#MotionDesign`). YouTube shows the first three above the title.

## Tags (`captions/youtube-tags.txt`)

YouTube Studio's "Tags" field (Details, Show more) takes up to 500 characters. Write them in `youtube-tags.txt` as one comma-separated line, ready to paste.

- Tags matter little for discovery (the title, thumbnail and description matter more); they help most when the product's name or topic is commonly misspelled. Include those misspellings ("android insider app", "androidinsider").
- 10 to 20 tags, specific first: the product name and its variants, the category, the audience's problem, then broader topics. Take them from the manifest's `copy.keywords` (see [`keywords.md`](keywords.md)).
- No hashtags (`#`), no tags unrelated to the video, no competitors' names: excessive or misleading tags break YouTube's spam policy.
- Count the characters: the total, commas included, must stay under 500.

Example: `Smart Cleaner, smart cleaner app, smartcleaner, phone cleaner, storage cleaner, free up space, phone storage full, delete duplicate photos, clean cache, android cleaner app, android storage, app trailer`

## Example (Smart Cleaner, `com.d4rk.cleaner`)

```text
I'm excited to present the official trailer for Smart Cleaner for Android. Smart Cleaner is built to make managing your phone's storage simpler, faster and more visual. It helps you find large and forgotten files, remove duplicates, review photos you may not need, clean cache and empty folders, and see what is taking up space.

Discover more about Smart Cleaner:
Google Play: https://play.google.com/store/apps/details?id=com.d4rk.cleaner

Discover more about Mihai-Cristian Condrea:
GitHub: https://github.com/MihaiCristianCondrea
X / Twitter: https://x.com/MihaiCristianC
Facebook: https://www.facebook.com/dev.mihaicristiancondrea
TikTok: https://www.tiktok.com/@dev.mihaicristiancondrea
Instagram: https://www.instagram.com/dev.mihaicristiancondrea

#SmartCleaner #Android #AndroidApp #CleanerApp #StorageCleaner #FreeUpSpace #PhoneStorage #PhoneTips #Productivity #AppTrailer #MotionDesign
```
