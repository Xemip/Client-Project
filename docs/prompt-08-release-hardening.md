# Prompt 8 — Release Hardening Completed

## Delivered
- Added secure local session persistence service:
  - `SessionPersistenceService`
  - `SecureTokenStore` (obfuscated local token vault)
- Added updater plumbing:
  - `UpdateChecker`
  - `UpdateInfo`
  - `UpdateCheckResult`
- Added launcher maintenance commands:
  - `session-show`
  - `session-clear`
  - `check-updates [manifestUrl]`
- Added launcher version constant for update comparison:
  - `Branding.LAUNCHER_VERSION`

## Notes
- `SecureTokenStore` provides lightweight local obfuscation, not hardware-backed key storage.
- `check-updates` expects a JSON manifest format with:
  - `latestVersion`
  - `downloadUrl`
  - `notes`
