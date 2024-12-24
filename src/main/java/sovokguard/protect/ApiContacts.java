package sovokguard.protect;

import me.sertyo.j2c.J2c;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@J2c
public class ApiContacts {
    public static String username; //USERNAME
    public static int uid; //UID
    public static String role; //ROLE
    public static void start() {
            username = "Sertyo";
            uid = 1;
            role = "Developer";
    }
}
