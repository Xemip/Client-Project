package com.clientproject.launcher.ui;

import com.clientproject.auth.AuthFacade;
import com.clientproject.auth.AuthMode;
import com.clientproject.auth.JdkHttpTransport;
import com.clientproject.auth.MicrosoftAuthService;
import com.clientproject.auth.MinecraftAuthChainService;
import com.clientproject.auth.MicrosoftDeviceCodeStart;
import com.clientproject.auth.OfflineAuthService;
import com.clientproject.auth.SessionProfile;
import com.clientproject.branding.Branding;
import com.clientproject.launcher.ClientLaunchConfig;
import com.clientproject.launcher.ClientLauncher;
import com.clientproject.launcher.LaunchPlan;
import com.clientproject.launcher.LauncherConfigStore;
import com.clientproject.launcher.LauncherProfile;
import com.clientproject.launcher.LauncherSettingsService;
import com.clientproject.multiplayer.MultiplayerAccessPolicy;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public final class LauncherGuiMain {
    private LauncherGuiMain() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LauncherGuiMain().show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void show() throws Exception {
        Path launcherRoot = Path.of(System.getProperty("user.home"), ".x-tweaks");
        LauncherSettingsService settingsService = new LauncherSettingsService(new LauncherConfigStore(), launcherRoot);
        LauncherProfile profile = settingsService.load();

        JFrame frame = new JFrame(Branding.CLIENT_NAME + " Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(860, 560));

        JTabbedPane tabs = new JTabbedPane();
        JTextArea diagnostics = new JTextArea();
        diagnostics.setEditable(false);

        tabs.addTab("Home", homePanel(profile, diagnostics));
        tabs.addTab("Login", loginPanel(profile, settingsService, diagnostics));
        tabs.addTab("Settings", settingsPanel(profile, settingsService, diagnostics));
        tabs.addTab("Diagnostics", diagnosticsPanel(diagnostics));

        frame.getContentPane().add(tabs, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel homePanel(LauncherProfile profile, JTextArea diagnostics) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel info = new JPanel(new GridLayout(0, 1));
        info.add(new JLabel("Client: " + Branding.CLIENT_NAME));
        info.add(new JLabel("Version: " + Branding.TARGET_VERSION));
        info.add(new JLabel("Profile user: " + profile.username()));
        info.add(new JLabel("Auth mode: " + profile.authMode()));

        JButton launchDryRun = new JButton("Generate Launch Command");
        launchDryRun.addActionListener(evt -> {
            try {
                ClientLaunchConfig cfg = ClientLaunchConfig.fromProfile(profile, Branding.TARGET_VERSION);
                LaunchPlan plan = new ClientLauncher().buildLaunchPlan(cfg, profile);
                diagnostics.append("[HOME] Dry-run command:\n" + String.join(" ", plan.command()) + "\n\n");
                JOptionPane.showMessageDialog(null, "Launch command generated. See Diagnostics tab.");
            } catch (Exception ex) {
                diagnostics.append("[HOME] ERROR: " + ex.getMessage() + "\n");
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton policyCheck = new JButton("Check Multiplayer Access");
        policyCheck.addActionListener(evt -> {
            MultiplayerAccessPolicy policy = new MultiplayerAccessPolicy();
            SessionProfile session = new SessionProfile(profile.authMode(), profile.username(), profile.accessToken(), profile.authMode() == AuthMode.MICROSOFT, "");
            if (policy.canJoinOnline(session)) {
                diagnostics.append("[HOME] Multiplayer allowed for " + session.username() + "\n");
                JOptionPane.showMessageDialog(null, "Multiplayer allowed");
            } else {
                String reason = policy.denyReason(session);
                diagnostics.append("[HOME] Multiplayer denied: " + reason + "\n");
                JOptionPane.showMessageDialog(null, reason, "Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel actions = new JPanel();
        actions.add(launchDryRun);
        actions.add(policyCheck);

        panel.add(info, BorderLayout.NORTH);
        panel.add(actions, BorderLayout.CENTER);
        return panel;
    }

    private JPanel loginPanel(LauncherProfile profile, LauncherSettingsService settingsService, JTextArea diagnostics) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

        JTextField username = new JTextField(profile.username());
        JTextField msClientId = new JTextField(System.getenv().getOrDefault("XTWEAKS_MS_CLIENT_ID", ""));
        JTextField deviceCodeField = new JTextField();

        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Microsoft Client ID"));
        panel.add(msClientId);
        panel.add(new JLabel("Device Code"));
        panel.add(deviceCodeField);

        JButton start = new JButton("Start Microsoft Device Login");
        start.addActionListener(evt -> {
            try {
                AuthFacade auth = new AuthFacade(
                        new MicrosoftAuthService(new JdkHttpTransport(), msClientId.getText().trim()),
                        new OfflineAuthService(),
                        new MinecraftAuthChainService(new JdkHttpTransport()));
                MicrosoftDeviceCodeStart dc = auth.startMicrosoftLogin();
                deviceCodeField.setText(dc.deviceCode());
                String msg = "Open " + dc.verificationUri() + " and enter code: " + dc.userCode();
                diagnostics.append("[LOGIN] " + msg + "\n");
                JOptionPane.showMessageDialog(null, msg);
            } catch (Exception ex) {
                diagnostics.append("[LOGIN] ERROR: " + ex.getMessage() + "\n");
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton complete = new JButton("Complete Microsoft Login");
        complete.addActionListener(evt -> {
            try {
                AuthFacade auth = new AuthFacade(
                        new MicrosoftAuthService(new JdkHttpTransport(), msClientId.getText().trim()),
                        new OfflineAuthService(),
                        new MinecraftAuthChainService(new JdkHttpTransport()));
                SessionProfile session = auth.completeMicrosoftLogin(username.getText().trim(), deviceCodeField.getText().trim());
                settingsService.updateSession(profile, session.username(), session.accessToken(), session.authMode());
                diagnostics.append("[LOGIN] Microsoft session saved for " + session.username() + "\n");
                JOptionPane.showMessageDialog(null, "Microsoft login complete for " + session.username());
            } catch (Exception ex) {
                diagnostics.append("[LOGIN] ERROR: " + ex.getMessage() + "\n");
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton offline = new JButton("Save Offline Session");
        offline.addActionListener(evt -> {
            try {
                SessionProfile session = new OfflineAuthService().createLocalSession(username.getText().trim());
                settingsService.updateSession(profile, session.username(), session.accessToken(), session.authMode());
                diagnostics.append("[LOGIN] Offline session saved for " + session.username() + "\n");
                JOptionPane.showMessageDialog(null, "Offline session saved");
            } catch (Exception ex) {
                diagnostics.append("[LOGIN] ERROR: " + ex.getMessage() + "\n");
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(start);
        panel.add(complete);
        panel.add(offline);
        panel.add(new JLabel(""));
        return panel;
    }

    private JPanel settingsPanel(LauncherProfile profile, LauncherSettingsService settingsService, JTextArea diagnostics) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField javaExec = new JTextField(profile.javaExecutable());
        JTextField minRam = new JTextField(String.valueOf(profile.minMemoryMb()));
        JTextField maxRam = new JTextField(String.valueOf(profile.maxMemoryMb()));
        JTextField gameDir = new JTextField(profile.gameDirectory().toString());
        JTextField assetsDir = new JTextField(profile.assetsDirectory().toString());
        JTextField libsDir = new JTextField(profile.librariesDirectory().toString());

        panel.add(new JLabel("Java executable")); panel.add(javaExec);
        panel.add(new JLabel("Min RAM (MB)")); panel.add(minRam);
        panel.add(new JLabel("Max RAM (MB)")); panel.add(maxRam);
        panel.add(new JLabel("Game directory")); panel.add(gameDir);
        panel.add(new JLabel("Assets directory")); panel.add(assetsDir);
        panel.add(new JLabel("Libraries directory")); panel.add(libsDir);

        JButton save = new JButton("Save Settings");
        save.addActionListener(evt -> {
            try {
                settingsService.updateRuntimeSettings(
                        profile,
                        javaExec.getText().trim(),
                        Integer.parseInt(minRam.getText().trim()),
                        Integer.parseInt(maxRam.getText().trim()),
                        Path.of(gameDir.getText().trim()),
                        Path.of(assetsDir.getText().trim()),
                        Path.of(libsDir.getText().trim()));
                diagnostics.append("[SETTINGS] Saved runtime settings\n");
                JOptionPane.showMessageDialog(null, "Settings saved");
            } catch (Exception ex) {
                diagnostics.append("[SETTINGS] ERROR: " + ex.getMessage() + "\n");
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Settings Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(save); panel.add(new JLabel(""));
        return panel;
    }

    private JPanel diagnosticsPanel(JTextArea diagnostics) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(diagnostics, BorderLayout.CENTER);
        JButton clear = new JButton("Clear");
        clear.addActionListener(evt -> diagnostics.setText(""));
        panel.add(clear, BorderLayout.SOUTH);
        return panel;
    }
}
