package de.oelrichsgarcia.studipTelegramBot.telegram;

import de.oelrichsgarcia.studipTelegramBot.telegram.api.RequestException;
import de.oelrichsgarcia.studipTelegramBot.telegram.api.TelegramApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import static de.oelrichsgarcia.studipTelegramBot.utils.Debugger.Sout;

/**
 * The type Telegram bot.
 */
public class TelegramBot {

    private final TelegramApi telegramApi;

    /**
     * Instantiates a new Telegram bot.
     *
     * @param token the token
     */
    public TelegramBot(String token) {
        telegramApi = new TelegramApi(token);
    }

    public void sendMessage(int chat_id, String text, TelegramApi.parseMode type) throws RequestException {
        if (chat_id == 0) return;
        JSONObject payload = new JSONObject();
        payload.put("chat_id", chat_id);
        if (type != TelegramApi.parseMode.TEXT) {
            if (type == TelegramApi.parseMode.HTML) text = text.replaceAll("\n", "<br>");
            payload.put("parse_mode", type.name());
        }
        payload.put("text", text);
        telegramApi.sendMessage(payload);
    }

    /**
     * Configure default telegram chat id long.
     *
     * @return the long
     */
    public long configureDefaultTelegramChatId() {
        //Telegram Default ChatId
        Sout("Füge den Bot als Admin in deiner Gruppe hinzu und schreibe ihn an, oder direkt. Schreibe Ihm folgende Indentifikations-ID:");
        int rand = Math.abs(new Random().nextInt());
        Sout("\"" + rand + "\"");
        Sout("Warte auf Nachricht...(Abbruch: Strg+C)");
        int chatId = 0;
        while (true) {
            try {
                String response = telegramApi.getUpdates();
                //System.out.println(response);
                JSONObject json = new JSONObject(response);
                JSONArray messages = json.getJSONArray("result");
                for (int i = 0; i < messages.length(); i++) {
                    JSONObject message = messages.getJSONObject(i);
                    String id = message.getJSONObject("message").getString("text");
                    if (id.equals("" + rand)) {
                        chatId = message.getJSONObject("message").getJSONObject("chat").getInt("id");
                        sendMessage(chatId, "Configuration Successfull!\nYour ChatId is: " + chatId, TelegramApi.parseMode.TEXT);
                        return chatId;
                    }
                }

            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }


}
