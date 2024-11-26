package me.sertyo.api.connection;

import me.sertyo.api.ApiConstants;
import me.sertyo.j2c.J2c;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class CheatConnection extends Connection {

    public CheatConnection(String path) {
        super(path);
    }

    public boolean download(String path) {
        try {
            File file = new File(path);
            HttpURLConnection connection = openConnection();
            connection.addRequestProperty("User-Agent", ApiConstants.USER_AGENT);
            ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
            FileOutputStream fos = new FileOutputStream(path);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            return file.exists();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    public String sendPost(String content) {
        try {
            HttpURLConnection connection = openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", ApiConstants.USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(content.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(content);
            dos.close();
            return readStream();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            closeConnection();
        }
        return null;
    }

    @J2c
    private String readStream() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getConnection().getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        bufferedReader.close();
        return response.toString();
    }

    @J2c
    public String getSession() throws IOException {
        String path = String.format("%s%s", ApiConstants.TEMP_DIR, ApiConstants.SESSION_FILE_NAME);
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String session = reader.readLine();
        reader.close();
        return session;
    }

}
