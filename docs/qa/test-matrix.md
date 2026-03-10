# Prompt 5 — QA Reproducible Test Matrix

## Scope
This matrix validates launcher runtime wiring, auth boundary behavior, installer hardening, and smoke-level packaging checks.

## Environments
- Primary: Linux CI/container shell
- Secondary target: Windows 10/11 (script presence + install logic validated by bundle inspection)

## Matrix

| Area | Check | Command | Expected |
|---|---|---|---|
| Compile | Main source compile | `javac -d target/classes $(find src/main/java -name '*.java')` | Exit 0 |
| Launcher | Profile load | `java -cp target/classes com.clientproject.launcher.LauncherMain profile-show` | prints profile |
| Launcher/Auth | Offline login | `java -cp target/classes com.clientproject.launcher.LauncherMain offline-login SmokeUser` | profile persisted |
| Multiplayer policy | Access gate | `java -cp target/classes com.clientproject.launcher.LauncherMain multiplayer-check` | denied for offline profile |
| Launcher command | Dry run | `java -cp target/classes com.clientproject.launcher.LauncherMain play --dry-run` | valid command string |
| Installer | Bundle generate | `java -cp target/classes com.clientproject.installer.InstallerMain target/X-Tweaks-installer.zip` | zip produced |
| Installer contents | Entry verification | python zip inspection | required scripts/manifest/payload present |
| Unit tests | JUnit test suite | `mvn test -q` | pass (or documented env warning) |

## Reproducible smoke runner
Use `scripts/qa/smoke_test.sh` to run all checks and emit `target/qa/smoke-test-report.txt`.
