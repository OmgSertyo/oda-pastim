package me.sertyo.api.connection;

import me.sertyo.api.ApiConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.net.HttpURLConnection;
import java.net.URL;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Connection {

    final URL url;

    @Getter
    HttpURLConnection connection;

    @SneakyThrows
    public Connection(String path) {
        this.url = new URL(String.format("%s%s", ApiConstants.SERVER_URL, path));
    }

    @SneakyThrows
    public HttpURLConnection openConnection() {
        return connection = (HttpURLConnection) url.openConnection();
    }

    public void closeConnection() {
        connection.disconnect();
    }

}
