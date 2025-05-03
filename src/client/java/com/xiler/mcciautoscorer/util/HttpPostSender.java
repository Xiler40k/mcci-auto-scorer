package com.xiler.mcciautoscorer.util;

import net.minidev.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpPostSender {

    public static void sendScore(Map<String, Object> scoreData) {
        try {
            URI uri = new URI("http://localhost:3000/score");
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            //NEED TO CHANGE
            //Possible values: Username, game, kills, survival, wins, placement, course 1, course 2, course 3 scores, round 1-8??
            //Got to figure out a smart way of doing this.
            //Per game? Then take data on backend and figure out there?

            JSONObject json = new JSONObject();
            for (Map.Entry<String, Object> entry : scoreData.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }

            String jsonInputString = json.toString();

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