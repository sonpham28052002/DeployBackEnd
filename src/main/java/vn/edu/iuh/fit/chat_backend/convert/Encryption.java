package vn.edu.iuh.fit.chat_backend.convert;

import java.util.Base64;

public class Encryption {
    private AES aes = new AES();
    public static String base64ToHex(String base64String) {
        byte[] byteArray = Base64.getDecoder().decode(base64String);
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    public static String bytesToHex(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String deCode(String password,String key){
        String inputHex = bytesToHex(aes.pad(password.trim().getBytes()));
        String keyHex = bytesToHex(key.trim().getBytes());
        return aes.encryption(inputHex, keyHex);
    }

}
