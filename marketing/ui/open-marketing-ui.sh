#!/usr/bin/env sh
# Refresh dependencies after pulls, including an existing node_modules folder.
set -eu
cd "$(dirname "$0")"
if ! command -v node >/dev/null 2>&1 || ! command -v npm >/dev/null 2>&1; then
  echo "Node.js and npm must be installed and available on PATH." >&2
  exit 1
fi
npm install --no-fund
exec npm run dev -- --open
