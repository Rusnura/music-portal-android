package org.tumasov.rmusicplayer.helpers;

public class UrlUtils {
    public static String normalize(String url, boolean secureConnection) {
        StringBuilder address = new StringBuilder();
        address.append(!url.startsWith("http") ? (secureConnection ? "https://" : "http://") : "");
        address.append(url);
        address.append(!url.endsWith("/") ? "/" : "");
        return address.toString();
    }

    public static String normalize(String url) {
        return normalize(url, false);
    }
}
