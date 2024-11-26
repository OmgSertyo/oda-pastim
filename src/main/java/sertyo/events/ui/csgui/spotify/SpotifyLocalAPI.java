package sertyo.events.ui.csgui.spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import net.minecraft.util.Util;
import sertyo.events.utility.misc.ChatUtility;

import static sertyo.events.ui.csgui.spotify.Spotify.trackInfo;

public class SpotifyLocalAPI {

    public static String accessToken;

    public static void main(String[] args) throws IOException {

            initiateAuthorizationProcess();

    }
    public static boolean isPlaying = false;

    public static void initiateAuthorizationProcess() throws IOException {
        // ������ ���������� �������
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        server.createContext("/callback", new AuthHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8888");
        // �������� ������ ����������� � ��������
        openAuthorizationUrl();

        // �������� ��������� ���� �����������
        while (AuthHandler.authorizationCode == null) {
            try {
                Thread.sleep(1000);  // �������� ������ �������
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

            // ����� ���� ����������� �� ����� �������
        accessToken = getAccessToken(AuthHandler.authorizationCode);

        // ���������� ������ � ���� � �����������

        // ������������� ������ ��� ������� � �������� �����
        if (trackInfo.length > 2) {
            Spotify.isConnected = true;
        } else {
            ChatUtility.addChatMessage("Spotify api был отключен потому что у вас нет активного трека для работы спотифая включите любой трек по желанию можете его поставить на паузу или оставить играть и потом начинайте настраивать spotify api");
            server.stop(0);
            Spotify.isConnected = false;
        }
    }

    private static void openAuthorizationUrl() {
        String clientId = "4e54d21376ae468b8871c780e15a9b21";
        String redirectUri = "http://localhost:8888/callback";
        String scopes = "user-read-currently-playing user-read-playback-state";

        try {
            // ����������� ���������� URL
            String encodedScopes = URLEncoder.encode(scopes, StandardCharsets.UTF_8.toString());
            String url = String.format("https://accounts.spotify.com/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s",
                    clientId, redirectUri, encodedScopes);
            Util.getOSType().openURI(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class AuthHandler implements HttpHandler {
        private static String authorizationCode;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String response = "Authorization code received. You can close this window.";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            // ���������� ���� ����������� �� URL
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("code=")) {
                    authorizationCode = param.split("=")[1];
                }
            }
        }
    }

    private static String getAccessToken(String authorizationCode) throws IOException {
        String clientId = "4e54d21376ae468b8871c780e15a9b21";
        String clientSecret = "fb240b3931e54dd1a792203ca13b8b3c";
        String redirectUri = "http://localhost:8888/callback";

        String url = "https://accounts.spotify.com/api/token";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        String credentials = clientId + ":" + clientSecret;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

        String body = "grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri=" + redirectUri;
        connection.setFixedLengthStreamingMode(body.getBytes().length);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", basicAuth);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            // �������������� ������ � JsonObject
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(content.toString());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return jsonObject.get("access_token").getAsString();
        }
    }


    private static String readFromFile(String filename) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String[] getCurrentTrack(String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/me/player/currently-playing";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }


                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(content.toString());

                if (!jsonElement.isJsonObject()) {
                    System.out.println("Received JSON is not an object: " + jsonElement);
                    return new String[]{"No track currently playing", "N/A", "", "0:00", "0:00", "N/A"};
                }

                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject item = jsonObject.getAsJsonObject("item");

                if (item != null) {
                    String trackName = item.get("name").getAsString();
                    JsonArray artistsArray = item.getAsJsonArray("artists");
                    String artistNamesString = "Unknown Artist";

                    if (artistsArray != null && artistsArray.size() > 0) {
                        List<String> artistNames = new ArrayList<>();
                        for (JsonElement artistElement : artistsArray) {
                            JsonObject artistObject = artistElement.getAsJsonObject();
                            String artistName = artistObject.get("name").getAsString();
                            artistNames.add(artistName);
                        }
                        artistNamesString = String.join(", ", artistNames);
                    }

                    JsonObject album = item.getAsJsonObject("album");
                    JsonObject images = album.getAsJsonArray("images").get(0).getAsJsonObject();
                    String coverUrl = images.get("url").getAsString();

                    int progressMs = jsonObject.has("progress_ms") ? jsonObject.get("progress_ms").getAsInt() : 0;
                    int trackDurationMs = item.get("duration_ms").getAsInt();
                    String trackProgress = formatMillisToMinutesSeconds(progressMs);
                    String trackEndTime = formatMillisToMinutesSeconds(trackDurationMs);
                    isPlaying = jsonObject.has("is_playing") && jsonObject.get("is_playing").getAsBoolean();

                    String albumReleaseDate = album.has("release_date") ? album.get("release_date").getAsString() : "N/A";

                    // ���������� ������ ������ � ������������� ����������
                    return new String[]{trackName, artistNamesString, coverUrl, trackProgress, trackEndTime, albumReleaseDate};
                } else {
                    return new String[]{"No track currently playing", "N/A", "", "0:00", "0:00", "N/A"};
                }
            }
        } else if (responseCode == 204) {
            System.out.println("No content: No track currently playing.");
            return new String[]{"No track currently playing", "N/A", "", "0:00", "0:00", "N/A"};
        } else if (responseCode == 429) {
            int retryAfter = connection.getHeaderFieldInt("Retry-After", 5);
            System.out.println("Rate limited. Retrying after " + retryAfter + " seconds.");
            try {
                Thread.sleep(retryAfter * 10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("Received unexpected response code: " + responseCode);
        }

        return new String[]{"Error", "N/A", "", "0:00", "0:00", "N/A"};
    }
    private static String formatMillisToMinutesSeconds(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static String getPlayPauseIcon() {
        return isPlaying ? "Pause" : "Play"; // Замените на пути к вашим иконкам
    }
    public static boolean getPlayPausestatus() {
        return isPlaying ? true : false; // Замените на пути к вашим иконкам
    }
    public static void resumePlayback(String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/me/player/play";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Length", "0");  // Добавляем Content-Length

        int responseCode = connection.getResponseCode();
        if (responseCode == 204) {
            System.out.println("Playback resumed successfully.");
        } else {
            System.out.println("Failed to resume playback: " + responseCode);
        }
    }
    public static void pausePlayback() throws IOException, InterruptedException {
        String url = "https://api.spotify.com/v1/me/player/pause";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json") // Убедитесь, что Content-Type установлен

                .header("Content-Length", "0")

                .POST(HttpRequest.BodyPublishers.noBody())  // PUT-запрос без тела
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            System.out.println("Playback paused successfully.");
        } else {
            System.out.println("Failed to pause playback: " + response.statusCode());
        }
    }
    private static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
