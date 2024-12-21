package me.sertyo.api.profile;


import me.sertyo.api.ApiConstants;
import me.sertyo.api.Crypto;
import me.sertyo.api.connection.CheatConnection;
import lombok.Getter;
import me.sertyo.j2c.J2c;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static me.sertyo.api.connection.CheatConnection.*;

@J2c
@Getter
public class CheatProfile {
    private final int id;
    private final String name;
    private final CheatRole role;
    private final Date expireDate;

    public CheatProfile(int id, String name, CheatRole role, Date expireDate) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.expireDate = expireDate;
    }

    public File getAvatarFile() {
        return ApiConstants.AVATAR_DIRECTORY;
    }
    public static String getHwid()
    {
        return md5Hex(
                System.getenv("os") +
                        System.getProperty("os.arch") +
                        System.getenv("PROCESSOR_ARCHITEW6432") +
                        System.getenv("PROCESSOR_LEVEL") +
                        System.getProperty("os.version") +
                        System.getProperty("os.name") +
                        System.getenv("PROCESSOR_REVISION") +
                        System.getenv("PROCESSOR_IDENTIFIER") +
                        System.getenv("PROCESSOR_ARCHITECTURE") +
                        System.getenv("COMPUTERNAME") +
                        System.getenv("PHYSICAL_MEMORY_SIZE")
        );
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
    public static CheatProfile create() {
        try {
            CheatConnection cheatConnection = new CheatConnection("/api/ClientAuth.php");
            String response = cheatConnection.sendPost(getSession());
            System.out.println(getSession());
            String[] split = response.split("<>");
            int id = parseIntSafe(split[0], "id");
            CheatRole role = CheatRole.values()[Integer.parseInt(split[1])];
            String name = split[2];
            String hwid = split[3].trim();
            if (hwid.trim().equals("nema")) {
                CheatConnection hwidapi = new CheatConnection("/api/sethwid.php");
                String respone = hwidapi.sendPost(String.format("hwid=%s&login=%s&pw_hash=%s", getHwid(), getLogin(), getPassword()));
                if (respone.contains("HWID updated successfully"))
                    System.out.println("[!] Hwid updated");
                else
                    System.out.printf("[!] Hwid error contact admin code (%s%n)", respone);
            }
            if (!hwid.trim().equals(getHwid())) {
                System.out.println(hwid);
                System.out.println(getHwid());
                System.exit(0);
            }
            Date expireDate = null;

            if (role == CheatRole.NULL || role == CheatRole.VISITOR) {

                System.exit(-1);
            }
            if (!System.getProperty("java.version").equals("21-AutistLeak")) {
                System.out.println("SOSI YAICHA"); //Тут можешь удалить как хоч кароч
                System.exit(-1); //Тут реализуй бан
            }
            if (!System.getProperty("java.runtime.version").equals("21-AutistLeak-adhoc.Sertyo.openjdk")) {
                //TODO: тут тоже самое
                System.out.println("SOSI YAICHA");
                System.exit(-1);
            }
            return new CheatProfile(id, name, role, expireDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private static int parseIntSafe(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer for " + fieldName + ": " + value, e);
        }
    }


    public boolean downloadAvatar(boolean shouldServerCut) {
        try {
            String data = Crypto.encrypt(String.format("%s</>%s", getId(), String.valueOf(!shouldServerCut)));
            CheatConnection cheatConnection = new CheatConnection(String.format("/api/GetAvatar?data=%s", data));
            return cheatConnection.download(getAvatarFile().getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getRoleName() {
        return toFisrtUpperCase(getRole().name());
    }

    public static String toFisrtUpperCase(String str) {
        return Character.toString(str.toLowerCase().charAt(0)).toUpperCase()
                + str.toLowerCase().substring(1);
    }
}
