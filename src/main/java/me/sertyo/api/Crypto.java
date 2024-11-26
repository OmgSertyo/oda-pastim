package me.sertyo.api;

import lombok.SneakyThrows;
import me.sertyo.j2c.J2c;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Random;

public class Crypto {

    @J2c
    private static String[] getKeys() {
        return new String[]{"58wALQuVJdlK1qCkrjS6n86WBkqBYmQs", "AD8814BAA5CA668A"};
    }

    @J2c
    public static String encrypt(String data) {
        String[] keys = getKeys();
        String dynamicKey = generateKey(32);
        String dynamicIv = generateKey(16);
        String encoded = encryptBase64(data, dynamicKey, dynamicIv);
        String result = encryptBase64(String.format("%s%s%s", dynamicKey, dynamicIv, encoded), keys[0], keys[1]);
        return result.replace("/", "$").replace("+", "_");
    }

    @J2c
    public static String decrypt(String data) {
        data = data.replace("$", "/").replace("_", "+");
        String[] keys = getKeys();
        String decoded = decryptBase64(data, keys[0], keys[1]);
        String dynamicKey = decoded.substring(0, 32);
        String dynamicIv = decoded.substring(32, 48);
        String decodedData = decoded.substring(48);
        return decryptBase64(decodedData, dynamicKey, dynamicIv);
    }

    @SneakyThrows
    public static String encryptBase64(String data, String key, String iv) {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        Key secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encoded = Base64.encodeBase64(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static String decryptBase64(String data, String key, String iv) {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        Key secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decoded = Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8));
        return new String(cipher.doFinal(decoded));
    }

    private static String generateKey(int length) {
        String charsStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        char[] chars = charsStr.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            stringBuilder.append(chars[new Random().nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }

}
