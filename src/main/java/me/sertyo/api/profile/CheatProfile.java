package me.sertyo.api.profile;


import me.sertyo.api.ApiConstants;
import me.sertyo.api.Crypto;
import me.sertyo.api.connection.CheatConnection;
import lombok.Getter;
import me.sertyo.j2c.J2c;

import java.io.File;
import java.util.Date;

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

    @J2c
    public static CheatProfile create() {
        try {
            CheatConnection cheatConnection = new CheatConnection("/api/ClientAuth.php");
            String response = cheatConnection.sendPost("username=Sertyo&password=X123456ll");
            String[] split = response.split("<>");
            int id = parseIntSafe(split[0], "id");
            CheatRole role = CheatRole.values()[Integer.parseInt(split[1])];
            String name = split[2];
            String ticksString = split[3].trim(); // Убираем пробелы
            System.out.println("ticksString (after trim): '" + ticksString + "'");

            Date expireDate = null;

            if (role == CheatRole.NULL || role == CheatRole.VISITOR) {
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


    @J2c
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
