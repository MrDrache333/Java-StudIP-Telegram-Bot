package utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import static utils.Debugger.writeerror;

/**
 * Project: Stud.IP-App
 * Package: StudIP_App.utils
 * Created by Keno Oelrichs García on 14.11.16. Yannik sitzt dumm daneben
 */
public class Password {

    private static SecretKeySpec secretKeySpec = null;

    private static void initialize() {
        try {
            // Das Passwort bzw der Schluesseltext
            String keyStr = "Uni Oldenburg";
            // byte-Array erzeugen
            byte[] key = (keyStr).getBytes(StandardCharsets.UTF_8);
            // aus dem Array einen Hash-Wert erzeugen mit MD5 oder SHA
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            // nur die ersten 128 bit nutzen
            key = Arrays.copyOf(key, 16);
            // der fertige Schluessel
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            writeerror(e);
        }
    }

    /**
     * Lock string.
     *
     * @param message the message
     * @return the string
     */
    public static String lock(String message) {
        if (secretKeySpec == null) initialize();
        try {
            // Verschluesseln
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(message.getBytes());

            // bytes zu Base64-String konvertieren (dient der Lesbarkeit)
            return Arrays.toString(Base64.getEncoder().encode(encrypted));
        } catch (Exception e) {
            writeerror(e);
        }
        return null;
    }

    /**
     * Entlock string.
     *
     * @param input the input
     * @return the string
     */
    public static String entlock(String input) {
        if (secretKeySpec == null) initialize();
        try {

            // BASE64 String zu Byte-Array konvertieren
            byte[] crypted2 = Base64.getDecoder().decode(input);

            // Entschluesseln
            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] cipherData2 = cipher2.doFinal(crypted2);
            return new String(cipherData2);
        } catch (Exception ignored) {
        }
        return null;
    }
}