# Prompt 5 — QA Pass + Smoke Tests

## Delivered
- Added reproducible smoke test runner script:
  - `scripts/qa/smoke_test.sh`
- Added QA matrix document:
  - `docs/qa/test-matrix.md`
- Smoke script validates:
  - Compile
  - Launcher profile/login/policy/dry-run
  - Installer generation and bundle entries
  - Maven unit tests (environment-aware warning behavior for remote resolution failures)
- Smoke script emits report:
  - `target/qa/smoke-test-report.txt`

## How to run
```bash
./scripts/qa/smoke_test.sh
```
