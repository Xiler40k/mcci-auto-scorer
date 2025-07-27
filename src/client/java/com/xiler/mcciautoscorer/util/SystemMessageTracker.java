package com.xiler.mcciautoscorer.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemMessageTracker {

    private static final Map<String, Long> messageTimestamps = new ConcurrentHashMap<>();
    private static final long EXPIRY_MS = 5000; // message lives 5 seconds

    public static void add(String message) {
        long now = System.currentTimeMillis();
        messageTimestamps.put(message, now);
        messageTimestamps.entrySet().removeIf(entry -> now - entry.getValue() >= EXPIRY_MS);
    }

    public static boolean isSystem(String message) {
        Long timestamp = messageTimestamps.get(message);
        if (timestamp == null) return false;

        long now = System.currentTimeMillis();
        boolean stillValid = now - timestamp < EXPIRY_MS;

        if (!stillValid) {
            messageTimestamps.remove(message);
        }

        return stillValid;
    }

}