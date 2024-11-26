package sertyo.events.command.impl;

import net.minecraft.util.text.TextFormatting;
import org.json.JSONObject;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;
import sertyo.events.utility.misc.ChatUtility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Command(
        name = "events",
        description = "Send near events on funtime"
)
public class ircCommand extends CommandAbstract {
    public void error() {
    }
        static int GOYDA;

        static List<String> eventList = new ArrayList<>();

        public static void update() {
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

                        eventList.add("/an" + eventNumber + " - " + time);
                    }
                }

                // Выводим общее количество событий

                // Рендерим все события в список

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void execute(String[] args) throws Exception {
        update();
        for (String event : eventList) {
            ChatUtility.addChatMessage(event);
        }
        ChatUtility.addChatMessage("Всего событий " + GOYDA);

    }
}
