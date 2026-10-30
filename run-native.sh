#!/usr/bin/env bash
set -Eeuo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker with the Compose plugin is required." >&2
  exit 1
fi

cleanup() {
  status=$?
  trap - EXIT INT TERM
  if [[ "${KEEP_DATA:-0}" == "1" ]]; then
    echo "KEEP_DATA=1: preserving Compose containers and database volumes."
  else
    echo "=== Cleanup: stopping Compose and removing its database/SQLite volumes ==="
    docker compose -f docker-compose-native.yml --profile samples down --volumes --remove-orphans || {
      echo "WARNING: Docker cleanup did not complete. Run ./clean.sh manually." >&2
      if [[ "$status" -eq 0 ]]; then status=1; fi
    }
  fi
  exit "$status"
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

echo "=== Starting MySQL and PostgreSQL; waiting for health checks ==="
docker compose -f docker-compose-native.yml --profile samples up --detach --wait mysql postgres

for version in 1 2 3 4; do
  echo
  echo "=== Running schema version v${version} ==="
  docker compose -f docker-compose-native.yml run --rm "app-v${version}"
done

echo
echo "All four schema versions passed."
