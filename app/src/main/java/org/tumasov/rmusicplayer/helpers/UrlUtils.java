package org.tumasov.rmusicplayer.helpers;

public class UrlUtils {
    public static String normilize(String url, boolean secureConnection) {
        StringBuilder address = new StringBuilder();
        address.append(!url.startsWith("http") ? (secureConnection ? "https://" : "http://") : "");
        address.append(url);
        address.append(!url.endsWith("/") ? "/" : "");
        return address.toString();
    }

    public static String normilize(String url) {
        return normilize(url, false);
    }
}
