#!/usr/bin/env bash
set -Eeuo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"

if ! command -v mvn >/dev/null 2>&1; then
  echo "Maven (mvn) is required." >&2
  exit 1
fi

build_jobs="${BUILD_JOBS:-2}"

if ! [[ "$build_jobs" =~ ^[1-9][0-9]*$ ]]; then
  echo "BUILD_JOBS must be a positive integer." >&2
  exit 1
fi

log_dir="$PWD/target/native-build-logs"
mkdir -p "$log_dir"

echo "Building with $build_jobs parallel jobs..."

printf '%s\n' 1 2 3 4 |
  xargs -P "$build_jobs" -n 1 bash -c '
    set -Eeuo pipefail

    log_dir="$1"
    version="$2"
    log="$log_dir/v${version}.log"

    echo "[v${version}] Building..."

    if mvn -B -ntp -pl "v${version}" \
        -DskipTests clean spring-boot:build-image; then
      echo "[v${version}] Build succeeded."
    else
      echo "[v${version}] Build FAILED:" >&2
      tail -n 80 "$log" >&2
      exit 1
    fi
  ' _ "$log_dir"

echo "All four native images are built."
