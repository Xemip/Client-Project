package com.clientproject.updater;

import com.clientproject.auth.HttpTransport;
import java.io.IOException;

public final class UpdateChecker {
    private final HttpTransport transport;

    public UpdateChecker(HttpTransport transport) {
        this.transport = transport;
    }

    public UpdateCheckResult check(String currentVersion, String manifestUrl) throws IOException, InterruptedException {
        String json = transport.getJson(manifestUrl, "");
        UpdateInfo info = parse(json);
        boolean available = compare(info.latestVersion(), currentVersion) > 0;
        return new UpdateCheckResult(available, currentVersion, info);
    }

    static UpdateInfo parse(String json) {
        return new UpdateInfo(
                extract(json, "latestVersion"),
                extract(json, "downloadUrl"),
                extract(json, "notes")
        );
    }

    static int compare(String left, String right) {
        String[] l = left.split("\\.");
        String[] r = right.split("\\.");
        int n = Math.max(l.length, r.length);
        for (int i = 0; i < n; i++) {
            int li = i < l.length ? parseInt(l[i]) : 0;
            int ri = i < r.length ? parseInt(r[i]) : 0;
            if (li != ri) {
                return Integer.compare(li, ri);
            }
        }
        return 0;
    }

    private static int parseInt(String v) {
        try {
            return Integer.parseInt(v.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private static String extract(String json, String field) {
        String token = "\"" + field + "\":\"";
        int i = json.indexOf(token);
        if (i < 0) {
            return "";
        }
        int start = i + token.length();
        int end = json.indexOf('"', start);
        return end < 0 ? "" : json.substring(start, end);
    }
}
