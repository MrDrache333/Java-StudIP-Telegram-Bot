package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip;

public class NotLoggedInException extends AbstractBotException {


    public NotLoggedInException() {
        super("YOu have to login first");
    }
}
