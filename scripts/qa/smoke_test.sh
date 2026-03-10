#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

REPORT_DIR="target/qa"
REPORT_FILE="$REPORT_DIR/smoke-test-report.txt"
mkdir -p "$REPORT_DIR"

pass() { echo "[PASS] $1" | tee -a "$REPORT_FILE"; }
warn() { echo "[WARN] $1" | tee -a "$REPORT_FILE"; }
fail() { echo "[FAIL] $1" | tee -a "$REPORT_FILE"; exit 1; }

: > "$REPORT_FILE"
echo "X Tweaks Prompt-5 smoke test report" | tee -a "$REPORT_FILE"
echo "Timestamp: $(date -u +%Y-%m-%dT%H:%M:%SZ)" | tee -a "$REPORT_FILE"

if javac -d target/classes $(find src/main/java -name '*.java'); then
  pass "Compile main sources"
else
  fail "Compile main sources"
fi

if java -cp target/classes com.clientproject.launcher.LauncherMain profile-show >/tmp/xt_profile.txt 2>&1; then
  pass "Launcher profile-show"
else
  cat /tmp/xt_profile.txt >> "$REPORT_FILE"
  fail "Launcher profile-show"
fi

if java -cp target/classes com.clientproject.launcher.LauncherMain offline-login SmokeUser >/tmp/xt_offline.txt 2>&1; then
  pass "Launcher offline-login"
else
  cat /tmp/xt_offline.txt >> "$REPORT_FILE"
  fail "Launcher offline-login"
fi

if java -cp target/classes com.clientproject.launcher.LauncherMain multiplayer-check >/tmp/xt_mp.txt 2>&1; then
  pass "Launcher multiplayer-check"
else
  cat /tmp/xt_mp.txt >> "$REPORT_FILE"
  fail "Launcher multiplayer-check"
fi

if java -cp target/classes com.clientproject.launcher.LauncherMain play --dry-run >/tmp/xt_dryrun.txt 2>&1; then
  pass "Launcher play --dry-run"
else
  cat /tmp/xt_dryrun.txt >> "$REPORT_FILE"
  fail "Launcher play --dry-run"
fi

if java -cp target/classes com.clientproject.installer.InstallerMain target/X-Tweaks-installer.zip >/tmp/xt_installer.txt 2>&1; then
  pass "Installer bundle creation"
else
  cat /tmp/xt_installer.txt >> "$REPORT_FILE"
  fail "Installer bundle creation"
fi

python - <<'PY' || fail "Installer bundle verification"
import zipfile, sys
z=zipfile.ZipFile('target/X-Tweaks-installer.zip')
required={
 'X-Tweaks/install.sh',
 'X-Tweaks/install.bat',
 'X-Tweaks/uninstall.sh',
 'X-Tweaks/uninstall.bat',
 'X-Tweaks/bin/launch-x-tweaks.sh',
 'X-Tweaks/bin/launch-x-tweaks.bat',
 'X-Tweaks/manifest.json',
 'X-Tweaks/payload/client-project-launcher.jar',
}
missing=sorted(required-set(z.namelist()))
if missing:
    print('Missing entries: ' + ', '.join(missing))
    sys.exit(1)
print('Installer bundle entries OK')
PY
pass "Installer bundle verification"

if mvn test -q >/tmp/xt_mvn.txt 2>&1; then
  pass "mvn test"
else
  if rg -q "403|Forbidden|PluginResolutionException" /tmp/xt_mvn.txt; then
    warn "mvn test blocked by Maven Central/plugin resolution environment issue"
    cat /tmp/xt_mvn.txt >> "$REPORT_FILE"
  else
    cat /tmp/xt_mvn.txt >> "$REPORT_FILE"
    fail "mvn test"
  fi
fi

echo "Smoke test completed." | tee -a "$REPORT_FILE"
