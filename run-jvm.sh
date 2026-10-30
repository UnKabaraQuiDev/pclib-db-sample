#!/usr/bin/env bash
set -Eeuo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"

command -v docker >/dev/null || { echo 'Docker with Compose v2 is required.' >&2; exit 1; }

cleanup() {
  status=$?
  trap - EXIT INT TERM
  if [[ "${KEEP_DATA:-0}" == 1 ]]; then
    echo 'KEEP_DATA=1: preserving JVM Compose containers and volumes.'
  else
    docker compose -f docker-compose-jvm.yml --profile samples down --volumes --remove-orphans || {
      echo 'WARNING: JVM Compose cleanup failed; run ./clean-jvm.sh.' >&2
      if [[ "$status" -eq 0 ]]; then status=1; fi
    }
  fi
  exit "$status"
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

echo '=== Starting MySQL and PostgreSQL ==='
docker compose -f docker-compose-jvm.yml --profile samples up -d --wait mysql postgres
for version in 1 2 3 4; do
  echo "=== Running JVM schema version v${version} ==="
  docker compose -f docker-compose-jvm.yml run --rm "app-v${version}"
done
echo 'All four JVM schema versions passed.'
