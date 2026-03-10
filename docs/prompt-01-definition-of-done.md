# Prompt 1 — X Tweaks Definition of Done (DoD)

This document locks scope for the first deliverable so follow-up prompts can implement against clear acceptance criteria.

## Product target
- Name: **X Tweaks**
- Minecraft target: **Java 1.8.9**
- Use case: PvP/performance-focused client runtime + launcher pipeline.

## Platform target for this release
- Primary supported OS: **Windows 10/11**
- Secondary (best-effort developer support): Linux/macOS via CLI launcher only.

## Security and policy boundaries
- Online multiplayer requires Microsoft-authenticated session.
- Offline local profile allowed for local/singleplayer workflows.
- No cracked-client bypass behavior.

## Release scope (must-have)
1. **Launcher Runtime**
   - Persistent config directory (`.x-tweaks`) with profile file.
   - CLI launcher commands for `login`, `offline-login`, `play`.
   - Command assembly for Minecraft 1.8.9 launch.

2. **Authentication**
   - Microsoft device-code flow end-to-end in launcher UX.
   - Session persistence with token storage abstraction.
   - Offline local session mode with multiplayer lockout.

3. **Multiplayer Policy**
   - Gate online join behind policy checks.
   - Human-readable deny reasons surfaced in launcher output.

4. **Installer**
   - Produce an installer artifact zip including launcher jar, manifest, and install script.
   - Install script writes files to user directory and creates launch shortcut script.

5. **Core Feature Modules**
   - Keep currently implemented visual/HUD/gameplay/chat/performance module registry.
   - Preserve optimization presets and lazy-load/culling primitives.

6. **Documentation**
   - End-user install and usage section (Windows first).
   - Troubleshooting section for Java/runtime/auth errors.

## Out of scope for this release
- Full custom Minecraft renderer replacement.
- Anti-cheat bypass features.
- Cracked/unauthorized online auth bypass.

## Acceptance criteria (release gate)
- Build: project compiles from clean checkout.
- Launcher: `play` command resolves runtime command without exceptions.
- Auth: Microsoft login flow can start and produce session object; offline mode works.
- Policy: online attempt with offline session is denied with explicit reason.
- Installer: creates zip with expected files and non-empty manifest.
- Docs: a new user can follow install steps without reading source code.

## Prompt sequence for remaining work
- Prompt 2: Launcher runtime wiring + persistent profile config.
- Prompt 3: Auth UX + token/session persistence improvements.
- Prompt 4: Installer hardening + shortcut creation and layout cleanup.
- Prompt 5: End-user docs + troubleshooting + smoke test checklist.
