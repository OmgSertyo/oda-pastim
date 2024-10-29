package sertyo.events.ui.ab.manager;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IgnoreManager {
    @Getter
    private static final List<String> ignore = new ArrayList<>();

    public static void add(String add) {
        if (!ignore.contains(add)) {
            ignore.add(add);
        }
    }

    public static void remove(String add) {
        ignore.remove(add);
    }

    public static void clear() {
        ignore.clear();
    }

    public static void load() {
        File file = new File("\\neiron\\ignore.txt");

        if (!file.exists()) {
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                ignore.add(line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save() {
        File file = new File("\\neiron\\ignore.txt");

        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (String s : ignore) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}