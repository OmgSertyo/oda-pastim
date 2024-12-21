package me.sertyo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        try {
            // URL для отправки запроса
            URL url = new URL("https://newcode.fun/api/api.php"); // Замените на нужный URL

            // Открываем соединение
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Настраиваем метод запроса (GET)
            connection.setRequestMethod("GET");

            // Устанавливаем заголовки, если это требуется
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Проверяем код ответа
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // Если ответ 200 OK
                // Читаем ответ сервера
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Вывод ответа
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            // Закрываем соединение
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
