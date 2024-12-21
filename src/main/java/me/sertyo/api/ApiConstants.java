package me.sertyo.api;

import java.io.File;

public class ApiConstants {
    public static final String NAME = "Neironclient";
    public static final String FILE_FORMAT = ".neiron";
    public static final String SESSION_FILE_NAME = "session" + FILE_FORMAT;
    public static final String SERVER_URL = "http://23.88.122.49:11588";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.84";
    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    public static final File MAIN_DIRECTORY = new File("C:/" + NAME);
    public static final File AVATAR_DIRECTORY = new File(MAIN_DIRECTORY, "avatar");
    public static final File CACHE_DIRECTORY = new File(MAIN_DIRECTORY, "cache");
}
