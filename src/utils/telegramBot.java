package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The type Telegram bot.
 */
public class telegramBot {

    private static String TOKEN;

    /**
     * Send message.
     *
     * @param chat_id the chat id
     * @param text    the text
     * @param type    the type
     * @param send    the send
     */
    public static void sendMessage(int chat_id, String text, parseMode type, boolean send) {
        if (chat_id == 0) return;
        if (text.contains("\""))
            text = text.replace("\"", "\\\"");
        String data = "{\"chat_id\":\"" + chat_id + "\"" + (type != parseMode.TEXT ? ",\"parse_mode\":\"" + type.name() + "\"" : "") + ",\"text\":\"" + text + "\"}";
        if (type == parseMode.HTML) data = data.replaceAll("\n", "<br>");
        if (send) {
            if (TOKEN.equals("")) throw new NullPointerException("Telegram BotToken must not be Null");
            httpPost(data, "sendMessage", true);
        } else
            System.out.println(data);
    }

    /**
     * Function to Push the Content of the given Data to the http endpoint
     *
     * @param Data       The Data
     * @param methodname The Method Name
     * @param send       If the Message should be send
     */
    private static void httpPost(String Data, String methodname, boolean send) {
        //Nachricht senden, wenn sie gesendet werden soll
        if (send) {
            int trys = 1;
            do {
                try {
                    //URL erstellen, HTTP Post mit übergebenen Daten durchführen
                    URL url = new URL("https://api.telegram.org/bot" + TOKEN + "/" + methodname);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setFixedLengthStreamingMode(Data.getBytes(StandardCharsets.UTF_8).length);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");

                    OutputStream os = conn.getOutputStream();
                    os.write(Data.getBytes(StandardCharsets.UTF_8));

                    //Antwortnachricht lesen
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String result = convertStreamToString(in);
                    //System.out.println(result);
                    //TODO Was ist mit der Response? Evtl. Fehlercode?

                    os.close();
                    conn.disconnect();
                    trys = 0;
                } catch (Exception e) {
                    if (trys > 2) {
                        System.err.println("Failed to send Message. Probably to long?");
                        System.err.println(e.getMessage());
                    }
                    trys++;
                }
            } while (trys > 0 && trys < 4);
        }


    }

    /**
     * Instantiates a new Telegram bot.
     *
     * @param token the token
     */
    public telegramBot(String token) {
        TOKEN = token;

    }

    private static String httpGet(String methodname, boolean send) {
        //Nachricht senden, wenn sie gesendet werden soll
        if (send) {
            int trys = 1;
            do {
                try {
                    //URL erstellen, HTTP Post mit übergebenen Daten durchführen
                    URL url = new URL("https://api.telegram.org/bot" + TOKEN + "/" + methodname);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(false);
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");

                    //Antwortnachricht lesen
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String result = convertStreamToString(in);
                    //TODO Was ist mit der Response? Evtl. Fehlercode?

                    conn.disconnect();
                    trys = 0;
                    return result;
                } catch (Exception e) {
                    if (trys > 2) {
                        System.err.println("Failed to connect. Down?");
                        System.err.println(e.getMessage());
                    }
                    trys++;
                }
            } while (trys > 0 && trys < 4);
        }


        return null;
    }

    public static String getUpdates() {
        return httpGet("getUpdates", true);
    }

    /**
     * The enum Parse mode.
     */
    public enum parseMode {
        /**
         * Html parse mode.
         */
        HTML,
        /**
         * Markdown parse mode.
         */
        MARKDOWN,
        /**
         * Text parse mode.
         */
        TEXT
    }

    //Konvertiert die Eingabe eines InputStreams in einen String und gibt ihn zurück
    private static String convertStreamToString(InputStream is)
            throws IOException {
            /*
             * To convert the InputStream to String we use the
             * Reader.read(char[] buffer) method. We iterate until the
    35.         * Reader return -1 which means there's no more data to
    36.         * read. We use the StringWriter class to produce the string.
    37.         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

}
