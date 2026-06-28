#!/usr/bin/env bash
set -euo pipefail

cleanup_compose() {
  docker compose down -v --remove-orphans
}
trap cleanup_compose EXIT

docker compose up -d mysql postgres

docker compose --profile sample run --rm sample-v1
docker compose --profile sample run --rm sample-v2
docker compose --profile sample run --rm sample-cleanup
