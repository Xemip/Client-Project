# Prompt 2 — Launcher Runtime Wiring Completed

This prompt implements runtime wiring required by Prompt 1:

## Delivered
- Persistent launcher profile config at `~/.x-tweaks/launcher-profile.properties`.
- Profile model includes memory, java path, auth mode, token, username, and runtime directories.
- Runtime resolution for:
  - Version jar (`versions/<mcVersion>/<mcVersion>.jar`)
  - Library jars (recursive scan under `libraries/`)
- Launch plan builder composes real Java command using classpath + `net.minecraft.client.main.Main`.
- Process orchestrator executes launch plan with working directory and inherited IO.
- CLI launcher commands:
  - `profile-show`
  - `offline-login <username>`
  - `ms-login-start`
  - `ms-login-complete <username> <deviceCode>`
  - `multiplayer-check`
  - `play [--dry-run]`

## Notes
- Microsoft login requires `XTWEAKS_MS_CLIENT_ID` environment variable.
- `play --dry-run` is recommended for validating command assembly before launch.
