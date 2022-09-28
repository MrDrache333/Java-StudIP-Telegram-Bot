package de.oelrichsgarcia.studipTelegramBot.studip;

abstract class GeneralBotException extends Exception implements BotException {

    private final String message;

    public GeneralBotException(String message) {
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
