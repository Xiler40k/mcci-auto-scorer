package com.xiler.mcciautoscorer.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpPostSender {

    public static void sendScore(String username, String game, int kills, int wins) {
        try {
            URI uri = new URI("http://localhost:3000/score");
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"username\":\"%s\",\"game\":\"%s\",\"kills\":%d,\"wins\":%d}",
                    username, game, kills, wins
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("[HTTP POST] Response code: " + code);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}