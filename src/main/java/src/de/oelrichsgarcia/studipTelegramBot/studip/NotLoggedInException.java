package de.oelrichsgarcia.studipTelegramBot.studip;

public class NotLoggedInException extends GeneralBotException {


    public NotLoggedInException() {
        super("YOu have to login first");
    }
}
