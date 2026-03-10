#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"
mkdir -p target/bench
javac -d target/classes $(find src/main/java -name '*.java')
java -cp target/classes com.clientproject.benchmark.PerformanceBenchmarkMain | tee target/bench/benchmark.txt
