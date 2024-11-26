package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.event.player.EventUpdate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventParserFromURL {
    static int GOYDA;

    // Список для хранения событий
    static List<String> eventList = new ArrayList<>();

   @EventTarget
   public void onUpdate(EventUpdate event) {
        try {
            String urlString = "https://backend2-2-7jk1.onrender.com/closestevents";

            // Создаем соединение с сервером
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Чтение ответа от сервера
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Преобразуем ответ в строку
            String jsonString = response.toString();

            // Разбиваем ответ на отдельные события
            String[] events = jsonString.split("\\},\\{");

            // Обрабатываем каждое событие
            for (String eventData : events) {
                eventData = eventData.replaceAll("[\\[\\]{}]", "");

                // Разделяем данные по ключам и значениям
                String[] eventDetails = eventData.split("\",\"");

                String eventName = "";
                String time = "";

                // Извлекаем значения по ключам
                for (String detail : eventDetails) {
                    if (detail.contains("event")) {
                        eventName = detail.split("\":\"")[1];
                    }
                    if (detail.contains("time")) {
                        time = detail.split("\":\"")[1];
                    }
                }

                // Если это событие "Анархия", добавляем в список
                if (eventName.startsWith("Анархия")) {
                    GOYDA++;
                    String eventNumber = eventName.replace("Анархия #", "");

                    // Добавляем событие в список
                    eventList.add("/an" + eventNumber + " - " + time);

                    // Выводим только если это событие "Анархия"
                    System.out.println("/an" + eventNumber + " - " + time);
                    System.out.println();
                }
            }

            // Выводим общее количество событий

            // Рендерим все события в список
            renderEvents();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для рендера событий
    public static void renderEvents() {
        System.out.println("Все события:");
        for (String event : eventList) {
            System.out.println(event);
        }
    }
}