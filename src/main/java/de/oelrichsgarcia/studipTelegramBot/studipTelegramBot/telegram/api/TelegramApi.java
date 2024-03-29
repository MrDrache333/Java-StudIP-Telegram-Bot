package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram.api;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TelegramApi {

    private final String token;

    public TelegramApi(String token) {
        this.token = token;
    }

    public void sendMessage(JSONObject payload) throws RequestException {
        if (token.equals("")) throw new NullPointerException("Telegram BotToken must not be Null");
        httpPost(payload.toString());

    }

    /**
     * Function to Push the Content of the given Data to the http endpoint
     *
     * @param Data The Data
     */
    private void httpPost(String Data) throws RequestException {
        //Nachricht senden, wenn sie gesendet werden soll
        try {
            //URL erstellen, HTTP Post mit übergebenen Daten durchführen
            URL url = new URL("https://api.telegram.org/bot" + token + "/" + "sendMessage");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");
            conn.setFixedLengthStreamingMode(Data.getBytes(StandardCharsets.UTF_8).length);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            OutputStream os = conn.getOutputStream();
            os.write(Data.getBytes(StandardCharsets.UTF_8));

            //Antwortnachricht lesen
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if (responseCode != 200) {
                throw new RequestException(responseCode, responseMessage);
            }

            os.close();
            conn.disconnect();
        } catch (IOException ignored) {
        }

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
}
