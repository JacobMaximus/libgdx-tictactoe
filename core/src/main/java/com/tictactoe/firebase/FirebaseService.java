package com.tictactoe.firebase;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class FirebaseService {
    private static final String FIREBASE_URL = "https://tictactoemultiplayer-1de41-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public static String createRoom() {
        try {
            String roomCode = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            URL url = new URL(FIREBASE_URL + "rooms/" + roomCode + ".json");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

//            String data = "{ \"player1\": true, \"player2\": false, \"state\": \"waiting\" }";
            String data = "{ \"player1\": true, \"player2\": false, \"state\": \"waiting\", " +
                "\"turn\": 1, " +
                "\"board\": [[0,0,0],[0,0,0],[0,0,0]] }";

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Room created: " + roomCode);
                return roomCode;
            } else {
                System.out.println("Failed to create room. Code: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean joinRoom(String roomCode) {
        try {
            // Check if room exists
            URL url = new URL(FIREBASE_URL + "/rooms/" + roomCode + ".json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch room.");
                return false;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            reader.close();

            if (response == null || response.equals("null")) {
                System.out.println("Room does not exist.");
                return false;
            }

            // Join the room as player2
            URL postUrl = new URL(FIREBASE_URL + "/rooms/" + roomCode + "/player2.json");
            HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();
            postConnection.setRequestMethod("PUT");
            postConnection.setDoOutput(true);
            postConnection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = postConnection.getOutputStream();
            os.write("true".getBytes());
            os.flush();
            os.close();

            int postResponse = postConnection.getResponseCode();
            return postResponse == 200;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
