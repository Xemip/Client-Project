# X Tweaks — Minecraft 1.8.9 PvP/Performance Client Foundation

X Tweaks is a **1.8.9-focused performance client foundation** with modular features, launcher scaffolding, installer packaging, and authentication plumbing.

## Important policy note
X Tweaks in this repository supports:
- Microsoft account login flow scaffolding,
- local offline sessions for non-online/local use.

It does **not** include cracked-client bypass logic for unauthorized online access.

## What is implemented

### Client modules & optimizations
- Visual/performance/HUD/gameplay/chat module registry and grouped feature pack
- PvP features: keystrokes, CPS, reach, combo, HUD overlays, toggle sprint/sneak, back view
- Optimization primitives: smart animations flags, culling flags, lazy-load scheduler, frame-time smoother

### Auth services
- Microsoft OAuth device-code primitives (`MicrosoftAuthService`)
- Local offline session support (`OfflineAuthService`)
- Unified entrypoint facade (`AuthFacade`)
- Multiplayer policy check requiring Microsoft-authenticated sessions (`MultiplayerAccessPolicy`)

### Launcher and installer
- Launcher command builder for 1.8.9 (`ClientLaunchConfig`, `ClientLauncher`, `LauncherMain`)
- Installer bundle generator (`InstallerPackager`, `InstallerMain`)
- Output installer artifact: `target/X-Tweaks-installer.zip`

## Quick start (download/install/use)

1. Build the launcher jar:
```bash
mvn -DskipTests package
```

2. Build installer zip:
```bash
java -cp target/classes com.clientproject.installer.InstallerMain target/X-Tweaks-installer.zip
```

3. Install:
   - Linux/macOS:
```bash
unzip -o target/X-Tweaks-installer.zip -d /tmp/x-tweaks-installer
bash /tmp/x-tweaks-installer/X-Tweaks/install.sh
```
   - Windows (PowerShell):
```powershell
Expand-Archive target\X-Tweaks-installer.zip -DestinationPath $env:TEMP\x-tweaks-installer -Force
& "$env:TEMP\x-tweaks-installer\X-Tweaks\install.bat"
```

4. Use launcher:
```bash
java -cp target/classes com.clientproject.launcher.LauncherMain profile-show
java -cp target/classes com.clientproject.launcher.LauncherMain offline-login Player
java -cp target/classes com.clientproject.launcher.LauncherMain play --dry-run
java -cp target/classes com.clientproject.launcher.LauncherMain gui
```

For Microsoft login commands, set `XTWEAKS_MS_CLIENT_ID` first.

## Build and package

```bash
mvn -DskipTests package
```

If your environment blocks Maven downloads, fallback manual packaging works:

```bash
mkdir -p target/classes
javac -d target/classes $(find src/main/java -name '*.java')
jar cfe target/client-project-launcher.jar com.clientproject.launcher.LauncherMain -C target/classes .
java -cp target/classes com.clientproject.installer.InstallerMain target/X-Tweaks-installer.zip
```

## Test

```bash
mvn test
```

## Delivery plan
- Prompt 1 (current): Definition of Done and acceptance criteria are captured in `docs/prompt-01-definition-of-done.md`.
- Next prompts implement launcher wiring, auth persistence, installer hardening, and end-user docs in that order.

## Prompt 2 status (launcher runtime wiring)
- Persistent profile config: `~/.x-tweaks/launcher-profile.properties`
- Runtime command uses Minecraft main class + resolved classpath from version jar and libraries.
- Launcher CLI:
  - `java -cp target/classes com.clientproject.launcher.LauncherMain profile-show`
  - `java -cp target/classes com.clientproject.launcher.LauncherMain offline-login <username>`
  - `java -cp target/classes com.clientproject.launcher.LauncherMain multiplayer-check`
  - `java -cp target/classes com.clientproject.launcher.LauncherMain play --dry-run`

Microsoft login commands:
- `java -cp target/classes com.clientproject.launcher.LauncherMain ms-login-start`
- `java -cp target/classes com.clientproject.launcher.LauncherMain ms-login-complete <username> <deviceCode>`


## Prompt 3 status (production auth chain)
- Microsoft login completion now executes full chain:
  1) Microsoft OAuth token
  2) Xbox Live auth
  3) XSTS token
  4) Minecraft services login
  5) Minecraft profile fetch
- `SessionProfile` now stores the Minecraft profile id (`playerId`).


## Prompt 4 status (installer hardening)
Installer bundle now includes:
- Real install/uninstall scripts for Linux/macOS and Windows
- Launcher shortcut scripts in `bin/`
- Rollback-on-failure behavior during install
- Payload launcher jar deployment to user install directory
- Manifest metadata fields for installer version + bundle entry inventory


## Prompt 5 status (QA + smoke pass)
- Reproducible QA matrix: `docs/qa/test-matrix.md`
- Smoke runner script: `scripts/qa/smoke_test.sh`
- Smoke report output: `target/qa/smoke-test-report.txt`

Run:
```bash
./scripts/qa/smoke_test.sh
```


## Prompt 6 status (launcher GUI polish)
- Added launcher desktop GUI (`LauncherGuiMain`) with pages:
  - Home, Login, Settings, Diagnostics
- CLI now supports:
  - `java -cp target/classes com.clientproject.launcher.LauncherMain gui`
- GUI settings persist to launcher profile via `LauncherSettingsService`.


## Prompt 7 status (performance tuning pass)
- Tuned PvP defaults:
  - OptiFine shaders disabled by default, render distance = 8
  - Launcher default max memory = 3072 MB
  - Added JVM GC tuning args for lower pause profile
- Added benchmark script:
  - `./scripts/perf/benchmark.sh`
  - report: `target/bench/benchmark.txt`


## Prompt 8 status (release hardening)
- Added secure local session persistence service:
  - `SessionPersistenceService`
  - `SecureTokenStore`
- Added update checker plumbing:
  - `UpdateChecker`, `UpdateInfo`, `UpdateCheckResult`
- New launcher commands:
  - `session-show`
  - `session-clear`
  - `check-updates [manifestUrl]`

Example update manifest JSON:
```json
{
  "latestVersion": "0.9.0",
  "downloadUrl": "https://example.com/x-tweaks.zip",
  "notes": "Performance and stability improvements"
}
```

