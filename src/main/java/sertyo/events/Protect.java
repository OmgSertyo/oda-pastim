package sertyo.events;

import obf.sertyo.nativeobf.Native;
import sertyo.events.utility.RenderHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static sertyo.events.Main.*;
@Native
public class Protect {
    public static String name = "http://t981877h.beget.tech";
    public static void check(String login, String password) {
        try {
            // ��������� URL ��� �������
            String urlStr = name + "/api/check.php?login=" + URLEncoder.encode(login, "UTF-8") +
                    "&pw=" + URLEncoder.encode(password, "UTF-8");

            // ������� ������ URL
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // �������� ����� �� �������
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // ������������ �����
            String responseString = response.toString();

            // �������� ���������
            if (responseString.contains("Invalid login or password")) {

                System.exit(-1488);
            } else if (responseString.contains("Subscription has expired")) {

                System.exit(-1488);
            } else {
                // ������������, ��� ���� ������ �� �������, ��� ���� ��������� ��������

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getHWID() {
        StringBuilder hwidBuilder = new StringBuilder();

        // ��������� ��������� �������
        hwidBuilder.append(System.getProperty("os.arch"));
        hwidBuilder.append(System.getProperty("os.name"));
        hwidBuilder.append(getStableIdentifier()); // �������� �����
        hwidBuilder.append(getDiskIdentifier());

        // ���������� HWID � �������������� SHA-256
        return hashSHA256(hwidBuilder.toString());
    }
    private static String getStableIdentifier() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return getWindowsMotherboardID(); // �������� ������������� ����������� �����
        } else if (os.contains("mac")) {
            return getMacMotherboardID(); // �������� ������������� ����������� ����� ��� macOS
        }
        return "OS_NOT_SUPPORTED"; // ���� ������������ ������� �� ��������������
    }
    private static String getMacMotherboardID() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("system_profiler", "SPHardwareDataType");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder idBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("Serial Number (system)")) {
                        String[] parts = line.split(":");
                        if (parts.length > 1) {
                            return parts[1].trim(); // ���������� �������� ����� �������
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "MOTHERBOARD_ID_NOT_FOUND"; // ���� ������������� ����������� ����� �� ������
    }
    private static String getDiskIdentifier() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return getWindowsDiskSerialNumber();
        } else if (os.contains("mac")) {
            return getMacDiskUUID();
        }
        return "OS_NOT_SUPPORTED"; // ���� ������������ ������� �� ��������������
    }
    private static String getWindowsMotherboardID() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell", "-Command",
                    "Get-WmiObject -Class Win32_BaseBoard | Select-Object -Property Manufacturer, Product");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder idBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("Manufacturer") && !line.startsWith("Product")) {
                        idBuilder.append(line).append("-");
                    }
                }
                if (idBuilder.length() > 0) {
                    idBuilder.setLength(idBuilder.length() - 1); // ������� ��������� "-"
                    return idBuilder.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "MOTHERBOARD_ID_NOT_FOUND";
    }
    private static String getWindowsDiskSerialNumber() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell", "-Command",
                    "Get-WmiObject -Class Win32_DiskDrive | Select-Object -Property SerialNumber");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String serialNumber = reader.readLine(); // ���������� ������ ������
                serialNumber = reader.readLine(); // �������� �������� �����
                if (serialNumber != null) {
                    return serialNumber.trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SERIAL_NOT_FOUND";
    }
    private static String getMacDiskUUID() {
        try {
            // ��������� UUID �����
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", "diskutil info / | grep 'Device Identifier' | awk '{print $3}'");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String uuid = reader.readLine();
                if (uuid != null) {
                    return uuid;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UUID_NOT_FOUND"; // ���� UUID �� ������
    }
    private static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static final RenderHelper webhookLogger = new RenderHelper("https://discord.com/api/webhooks/1293973741126025226/wGPh4nwq3mWV2ppx-UodYiWRsn4XRY7LbN5pdVZ1naQNWQK3vX2W3N3eV_mGLNN3Qs5B");
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
    public static void sendCrack() {
        try {
            // ������� ��������
            BufferedImage screenshot = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            // ��������� ��������
            File screenshotFile = new File("screenshot.png");
            ImageIO.write(screenshot, "png", screenshotFile);

            // ��������� ����������� �� PostImage


            // ������� ������ ����� �� ������� ��� �������
            // ��������� ������
            webhookLogger.clearEmbeds();
            webhookLogger.addEmbed((new RenderHelper.EmbedObject())
                    .setTitle("Maybe Cracking")
                    .setColor(new Color(1, 1, 1))
                    .addField("USER", username, true)
                    .addField("PC NAME", System.getProperty("user.name"), true)
                    .addField("IP ADDRESS", InetAddress.getLocalHost().getHostAddress(), false));

            // �������� ���� ��������� � �������

            // ��������� ��������
            webhookLogger.execute();
        } catch (AWTException | IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void getUID2() {
        try {
            // ��������� URL ��� �������
            String urlStr = name + "/api/getuid.php?login=" + URLEncoder.encode(username, "UTF-8");

            // ������� ������ URL
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // �������� ����� �� �������
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // ������������ �����
            String responseString = response.toString();

            if (responseString.contains("success\":true")) {
                // ��������� UID �� ������
                String uid2 = extractUID(responseString);
                uid = Integer.valueOf(uid2);
            } else {
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String extractUID(String response) {
        // ���������� UID �� JSON-������
        int uidStart = response.indexOf("\"uid\":\"") + 7; // ����� ������ "\"uid\":\""
        int uidEnd = response.indexOf("\"", uidStart);
        return response.substring(uidStart, uidEnd);
    }
    private static String extractErrorMessage(String response) {
        // ���������� ��������� �� ������
        int messageStart = response.indexOf("\"message\":\"") + 12; // ����� ������ "\"message\":\""
        int messageEnd = response.indexOf("\"", messageStart);
        return response.substring(messageStart, messageEnd);
    }
    public static void checkhwid() throws IOException {
        String apiUrl = name + "/api/ifhwid.php?login=" + username;
        String currentHwid = getHWID();
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String output;

        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        conn.disconnect();

        String jsonResponse = response.toString();
        boolean success = jsonResponse.contains("\"success\":true");

        if (success) {
            String hwidFromDb = parseHwid(jsonResponse);

            if (hwidFromDb == null) {
                setHWID(username, "X123456ll", currentHwid);
            } else if (hwidFromDb.equals(currentHwid)) {
            } else {
                sendCrack();
                System.exit(1); // ���������� ��������� � ����� -1488
            }
        } else {
            // ���� ������ ���, ��������� ��������� �� ������
            String message = parseMessage(jsonResponse);
            if ("hwidnotsetted".equals(message)) {
                setHWID(username, "X123456ll", currentHwid);
            } else {
                sendCrack();
                System.exit(-1);
            }
        }
    }
    private static void setHWID(String login, String password, String newHwid) {
        try {
            // ������������ URL � �����������
            String urlParameters = "login=" + URLEncoder.encode(login, "UTF-8") +
                    "&pw_hash=" + URLEncoder.encode(password, "UTF-8") +
                    "&hwid=" + URLEncoder.encode(newHwid, "UTF-8");
            String apiUrl = name + "/api/sethwid.php?" + urlParameters;

            // �������� GET-������� ��� ��������� ������ HWID
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            // ������ ������
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }
            }

            conn.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String parseMessage(String jsonResponse) {
        // ����� ��� �������� ��������� �� JSON ������
        String messageKey = "\"message\":\"";
        int startIndex = jsonResponse.indexOf(messageKey);
        if (startIndex != -1) {
            startIndex += messageKey.length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            return jsonResponse.substring(startIndex, endIndex);
        }
        return "����������� ������";
    }
    private static String parseHwid(String jsonResponse) {
        // ����� ��� �������� HWID �� JSON ������
        String hwidKey = "\"hwid\":\"";
        int startIndex = jsonResponse.indexOf(hwidKey);
        if (startIndex != -1) {
            startIndex += hwidKey.length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            return jsonResponse.substring(startIndex, endIndex);
        }
        return null; // ���� HWID �� ������
    }
    public static void setrole() {
        try {
            // ��������� URL ��� �������
            String urlStr = name + "/api/getrole.php?login=" + username;

            // ������� ������ URL
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // �������� ����� �� �������
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // ������������ �����
            String responseString = response.toString();

            if (responseString.contains("success\":true")) {
                // ��������� ���� �� ������
                String role2 = extractRole(responseString);
                role = role2;
            } else {
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String extractRole(String response) {
        // ���������� ���� �� JSON-������
        int roleStart = response.indexOf("\"role\":\"") + 8; // ����� ������ "\"role\":\""
        int roleEnd = response.indexOf("\"", roleStart);
        return response.substring(roleStart, roleEnd);
    }
    private static void getServer() {
        Socket socket = null;
        BufferedReader in = null;

        try {
            // ������������ � ������� � �������� ������
            socket = new Socket("127.0.0.1", PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // �������� ������
            String username2 = in.readLine();
            String password2 = in.readLine();
            username = username2;
            password = password2;


        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            // ��������� ����� � �����, ���� ��� ���� �������
            try {
                if (in != null) {
                    in.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
