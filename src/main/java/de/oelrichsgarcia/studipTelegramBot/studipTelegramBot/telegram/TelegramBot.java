package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram.api.RequestException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram.api.TelegramApi;
import org.json.JSONObject;

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

}
