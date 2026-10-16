#!/usr/bin/env sh
# Opens the marketing UI gallery; installs dependencies on the first run.
cd "$(dirname "$0")" || exit 1
[ -d node_modules ] || npm install
npm run dev -- --open
