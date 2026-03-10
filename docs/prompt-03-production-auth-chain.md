# Prompt 3 — Production Auth Chain (Microsoft/Xbox/Minecraft)

This prompt completes the account auth chain beyond the previous scaffold.

## Delivered
- Extended HTTP transport abstraction for JSON POST/GET with optional bearer token.
- Added production chain service:
  1. Microsoft access token -> Xbox Live auth
  2. Xbox Live -> XSTS authorization
  3. XSTS -> Minecraft services login
  4. Minecraft access token -> Minecraft profile fetch
- Added domain records for chain stages:
  - `XboxLiveToken`
  - `XstsToken`
  - `MinecraftAccessToken`
  - `MinecraftProfile`
- Updated `AuthFacade.completeMicrosoftLogin` to execute full chain and return a final Minecraft session profile.
- Enriched `SessionProfile` with `playerId` (Minecraft profile id).

## Launcher impact
- Existing launcher login commands now persist a Minecraft-ready token from the full auth chain:
  - `ms-login-start`
  - `ms-login-complete <username> <deviceCode>`

## Validation coverage
- Added unit tests for chain parsing and orchestration:
  - `MinecraftAuthChainServiceTest`
  - `AuthFacadeChainTest`
