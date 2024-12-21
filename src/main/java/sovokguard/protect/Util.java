package sovokguard.protect;

import me.sertyo.j2c.J2c;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static sovokguard.protect.ApiContacts.token;
@J2c
public class Util {
    public static String getHwid() {
        String input = System.getenv("os") +
                System.getenv("PROCESSOR_ARCHITEW6432") +
                System.getenv("PROCESSOR_LEVEL") +
                System.getenv("PROCESSOR_REVISION") +
                System.getenv("PROCESSOR_IDENTIFIER") +
                System.getenv("PROCESSOR_ARCHITECTURE") +
                System.getenv("COMPUTERNAME") +
                System.getenv("PHYSICAL_MEMORY_SIZE");


        return md5Hex(input);
    }
    public static boolean isSubscriptionExpired(String expirationDate) {
        try {
            OffsetDateTime expiration = OffsetDateTime.parse(expirationDate);

            LocalDate currentDate = LocalDate.now();
            LocalDate expirationLocalDate = expiration.toLocalDate();

            return expirationLocalDate.isBefore(currentDate);
        } catch (Exception e) {
            System.out.println("Ошибка обработки даты подписки: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
    static String md5Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
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
    public static void getinfo() {
        RequestConfig config = RequestConfig.custom()
                .setRedirectsEnabled(true)
                .build();

        try (CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build()) {

            HttpGet httpGet = new HttpGet(ApiContacts.SERVER_URL + "/stat/get_myself/");
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Authorization", "Bearer " + token);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);

                JSONObject jsonResponse = new JSONObject(responseString);
                int id = jsonResponse.getInt("id");
                String username = jsonResponse.getString("username");
                String hwid = jsonResponse.isNull("hwid") ? null : jsonResponse.getString("hwid");
                String role = jsonResponse.getString("role");
                role = role.replace("\n", "");
                String expirationDate = jsonResponse.getJSONObject("subscription").getString("expiration_date");
                if (expirationDate == null) {
                    System.exit(0);
                }
                if (isSubscriptionExpired(expirationDate)) {
                    System.exit(-2);
                }

                if (hwid == null) {
                    Crasher.crash();
                } else if (!hwid.equals(Util.getHwid())) {
                    System.exit(-1);
                }
                ApiContacts.uid = id;
                ApiContacts.username = username;
                ApiContacts.role = role;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateinformation() {
        RequestConfig config = RequestConfig.custom()
                .setRedirectsEnabled(true)
                .build();
        JSONObject jsonData = new JSONObject();
        jsonData.put("new_last_launch", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        jsonData.put("increment_launches", true);
        try (CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build()) {

            HttpPatch httpPatch = new HttpPatch(ApiContacts.SERVER_URL + "/stat/add_stat/");
            httpPatch.setHeader("Content-Type", "application/json");
            httpPatch.setHeader("Authorization", "Bearer " + token);
            StringEntity entity = new StringEntity(jsonData.toString());
            httpPatch.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(httpPatch)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
