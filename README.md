# Client-Project — Minecraft 1.8.9 PvP/Performance Client

This repo is now structured as a **1.8.9-targeted PvP/performance client core** with:
- modular feature toggles,
- launcher packaging,
- Microsoft account login flow scaffolding,
- and explicit performance-mod integration planning.

## Target profile
- Client profile: `Client-Project PvP`
- Minecraft version: **1.8.9**
- Focus: PvP responsiveness, FPS stability, low input latency.

## Included feature systems

### Performance & Visuals
- OptiFine Ultra module settings (shaders / zoom / render distance)
- BetterSky, Particles Mod, Fullbright, Clear Glass/Water, NoHurtCam module toggles
- Performance flags: Input Lag Reduction, Entity & Tile Culling, Lazy Data Loading, Smart Animations

### HUD & Information
- Keystrokes (KeyMod), CPS, Armor HUD, Status Effects HUD
- FPS/Ping display, Reach display, Combo counter, TNT timer
- `HudTracker` tracks CPS + combo + last reach in runtime.

### Gameplay & Utility
- ToggleSprint, ToggleSneak, BackView (360°)
- Auto-GG / Auto-Thank
- Chat customization (nickname highlights + color-code support)

## Microsoft account login feature
`MicrosoftAuthService` implements OAuth device-code flow primitives for launcher sign-in:
1. Start device login (`startDeviceLogin`),
2. Show user code and verification URL,
3. Poll token endpoint (`pollForToken`),
4. Use access token for downstream Minecraft/Xbox auth bridge.

> Important: This repository currently provides the OAuth launcher-step implementation and token plumbing scaffold. Full production Minecraft auth chain (Xbox Live/XSTS/Minecraft services) should be completed in the next iteration.

## OptiFine + performance mod import handling
`PerformanceModPack189` + `ModInstallerPlanner` define which integrations can be redistributed vs user-provided:
- OptiFine 1.8.9 is marked **user-provided** (non-redistributable).
- Other performance mods are planned as redistributable metadata entries.

## Build & package launcher
Create executable launcher JAR:

```bash
mvn -DskipTests package
```

Output target (shade plugin):
- `target/client-project-launcher.jar`
- Main class: `com.clientproject.launcher.LauncherMain`

## Run tests
```bash
mvn test
```
