#!/usr/bin/env bash
set -Eeuo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"

command -v mvn >/dev/null || { echo 'Maven is required.' >&2; exit 1; }
command -v docker >/dev/null || { echo 'Docker is required.' >&2; exit 1; }

# JVM builds skip Spring AOT processing. Repackage creates an executable Boot JAR.
for version in 1 2 3 4; do
  echo "=== Packaging JVM v${version} ==="
  mvn -B -ntp -pl "v${version}" -DskipTests -Dspring-boot.aot.skip=true \
    clean package spring-boot:repackage
done

echo '=== Building JVM Docker images ==='
docker compose -f docker-compose-jvm.yml --profile samples build app-v1 app-v2 app-v3 app-v4
echo 'All four JVM images are built.'
