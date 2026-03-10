package com.clientproject.launcher;

import com.clientproject.auth.AuthFacade;
import com.clientproject.auth.AuthMode;
import com.clientproject.auth.JdkHttpTransport;
import com.clientproject.auth.MicrosoftAuthService;
import com.clientproject.auth.MicrosoftDeviceCodeStart;
import com.clientproject.auth.MinecraftAuthChainService;
import com.clientproject.auth.OfflineAuthService;
import com.clientproject.auth.SessionProfile;
import com.clientproject.branding.Branding;
import com.clientproject.multiplayer.MultiplayerAccessPolicy;
import com.clientproject.launcher.ui.LauncherGuiMain;
import com.clientproject.security.SessionPersistenceService;
import com.clientproject.updater.UpdateCheckResult;
import com.clientproject.updater.UpdateChecker;
import java.nio.file.Path;

public final class LauncherMain {
    private LauncherMain() {
    }

    public static void main(String[] args) throws Exception {
        Path launcherRoot = Path.of(System.getProperty("user.home"), ".x-tweaks");
        LauncherConfigStore store = new LauncherConfigStore();
        LauncherProfile profile = store.loadOrCreate(launcherRoot);
        SessionPersistenceService sessionStore = new SessionPersistenceService();

        String command = args.length == 0 ? "help" : args[0];
        switch (command) {
            case "gui" -> LauncherGuiMain.main(new String[0]);
            case "profile-show" -> printProfile(profile);
            case "offline-login" -> {
                requireArgs(args, 2, "offline-login <username>");
                SessionProfile session = new OfflineAuthService().createLocalSession(args[1]);
                LauncherProfile updated = withSession(profile, session);
                store.save(launcherRoot.resolve(LauncherConfigStore.PROFILE_FILE), updated);
                sessionStore.save(launcherRoot, session);
                System.out.println("Offline profile saved for " + session.username());
            }
            case "ms-login-start" -> {
                AuthFacade auth = new AuthFacade(new MicrosoftAuthService(new JdkHttpTransport(), clientId()), new OfflineAuthService(), new MinecraftAuthChainService(new JdkHttpTransport()));
                MicrosoftDeviceCodeStart start = auth.startMicrosoftLogin();
                System.out.println("Open: " + start.verificationUri());
                System.out.println("Code: " + start.userCode());
                System.out.println("DeviceCode: " + start.deviceCode());
            }
            case "ms-login-complete" -> {
                requireArgs(args, 3, "ms-login-complete <username> <deviceCode>");
                AuthFacade auth = new AuthFacade(new MicrosoftAuthService(new JdkHttpTransport(), clientId()), new OfflineAuthService(), new MinecraftAuthChainService(new JdkHttpTransport()));
                SessionProfile session = auth.completeMicrosoftLogin(args[1], args[2]);
                LauncherProfile updated = withSession(profile, session);
                store.save(launcherRoot.resolve(LauncherConfigStore.PROFILE_FILE), updated);
                sessionStore.save(launcherRoot, session);
                System.out.println("Microsoft session saved for " + session.username());
            }
            case "play" -> {
                boolean dryRun = args.length > 1 && "--dry-run".equals(args[1]);
                ClientLaunchConfig config = ClientLaunchConfig.fromProfile(profile, Branding.TARGET_VERSION);
                ClientLauncher launcher = new ClientLauncher();
                LaunchPlan plan = launcher.buildLaunchPlan(config, profile);
                if (dryRun) {
                    System.out.println(String.join(" ", plan.command()));
                    return;
                }
                launcher.launch(plan);
            }
            case "multiplayer-check" -> {
                MultiplayerAccessPolicy policy = new MultiplayerAccessPolicy();
                SessionProfile session = new SessionProfile(profile.authMode(), profile.username(), profile.accessToken(), profile.authMode() == AuthMode.MICROSOFT, "");
                if (policy.canJoinOnline(session)) {
                    System.out.println("Online multiplayer allowed.");
                } else {
                    System.out.println("Online multiplayer denied: " + policy.denyReason(session));
                }
            }
            case "session-show" -> {
                SessionProfile session = sessionStore.load(launcherRoot);
                System.out.println("Session authMode: " + session.authMode());
                System.out.println("Session username: " + session.username());
                System.out.println("Session multiplayerAllowed: " + session.multiplayerAllowed());
                System.out.println("Session playerId: " + session.playerId());
                System.out.println("Session tokenPresent: " + (!session.accessToken().isBlank()));
            }
            case "session-clear" -> {
                sessionStore.clear(launcherRoot);
                System.out.println("Stored session cleared.");
            }
            case "check-updates" -> {
                String url = args.length > 1 ? args[1] : "https://example.com/x-tweaks-update.json";
                UpdateChecker checker = new UpdateChecker(new JdkHttpTransport());
                UpdateCheckResult result = checker.check(Branding.LAUNCHER_VERSION, url);
                if (result.updateAvailable()) {
                    System.out.println("Update available: " + result.updateInfo().latestVersion());
                    System.out.println("Download: " + result.updateInfo().downloadUrl());
                } else {
                    System.out.println("Launcher up to date: " + result.currentVersion());
                }
            }
            default -> printHelp();
        }
    }

    private static LauncherProfile withSession(LauncherProfile profile, SessionProfile session) {
        return new LauncherProfile(
                profile.profileName(),
                profile.gameDirectory(),
                profile.assetsDirectory(),
                profile.librariesDirectory(),
                profile.javaExecutable(),
                profile.minMemoryMb(),
                profile.maxMemoryMb(),
                session.username(),
                session.accessToken(),
                session.authMode()
        );
    }

    private static String clientId() {
        String value = System.getenv("XTWEAKS_MS_CLIENT_ID");
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing XTWEAKS_MS_CLIENT_ID environment variable.");
        }
        return value;
    }

    private static void requireArgs(String[] args, int required, String usage) {
        if (args.length < required) {
            throw new IllegalArgumentException("Usage: " + usage);
        }
    }

    private static void printProfile(LauncherProfile profile) {
        System.out.println("Profile: " + profile.profileName());
        System.out.println("GameDir: " + profile.gameDirectory());
        System.out.println("Assets: " + profile.assetsDirectory());
        System.out.println("Libraries: " + profile.librariesDirectory());
        System.out.println("User: " + profile.username());
        System.out.println("AuthMode: " + profile.authMode());
    }

    private static void printHelp() {
        System.out.println(Branding.CLIENT_NAME + " launcher commands:");
        System.out.println("  gui");
        System.out.println("  profile-show");
        System.out.println("  offline-login <username>");
        System.out.println("  ms-login-start");
        System.out.println("  ms-login-complete <username> <deviceCode>");
        System.out.println("  multiplayer-check");
        System.out.println("  session-show");
        System.out.println("  session-clear");
        System.out.println("  check-updates [manifestUrl]");
        System.out.println("  play [--dry-run]");
    }
}
