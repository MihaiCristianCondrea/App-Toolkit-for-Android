# Custom store listings

None yet. One custom store listing per tool is planned, after the redesign and the new default listing. Each one gets
a folder here, in the same layout as `default/`:

```text
<id>/
├── listing.json     audience, targeting (link &listing=<id>, countries, Google Ads), status and its dated log
├── keywords.txt     search keywords it targets: one per line, up to 500, each up to 80 characters
├── source/          what renders its visuals, or a README naming the listing whose graphics it uses
└── listing/<locale>/  what is uploaded: text, plus graphics/ and alt-text.txt only when it has its own
```

- `status` is `planned`, `live` or `paused`; a change is a new line in the log, nothing moves.
- A listing that stops being used stays where it is with status `retired` and a last log line, and is not edited
  again.
