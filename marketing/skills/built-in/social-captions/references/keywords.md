# Keywords and hashtags

Part of [`social-captions`](../SKILL.md), shared with [`metadata`](../../metadata/SKILL.md). Keywords and hashtags describe what the audience searches for, not how the product was built.

## Start from the audience

1. Read the project block (`project.category`, `project.keywords`) and `marketing/briefs/product-positioning.md`: who is the product for, and what problem does it solve for them?
2. Write the words that person would type into a search box or follow as a tag. A storage cleaner: "free up space", "phone storage", "delete duplicate photos". A recipe app: "easy dinner", "meal prep". A developer tool: "Jetpack Compose", "Kotlin".
3. Check them where possible: the platform's search suggestions, the tags on popular posts in the category, the Play Store listing's own keywords.

## Build the list

| Layer | Example (storage cleaner) | Example (developer learning app) |
| --- | --- | --- |
| Product | `#SmartCleaner` | `#AndroidInsider` |
| Platform | `#Android` `#AndroidApp` | `#Android` |
| Category | `#StorageCleaner` `#Productivity` | `#AndroidDev` `#DeveloperTools` |
| Audience problem | `#FreeUpSpace` `#PhoneTips` | `#JetpackCompose` `#Kotlin` |
| Format | `#AppTrailer` | `#AppTrailer` |

- Technology tags (the framework, the language, the design system) only when the audience is developers or designers. Users don't search for how an app was built.
- Specific beats broad: `#FreeUpSpace` reaches people with the problem; `#App` reaches nobody in particular.
- No competitors' names, no trending tags unrelated to the post, no invented statistics.
- Store the final keywords in the campaign manifest (`copy.keywords`, `copy.hashtags`) so the files and the captions agree.
