package studip;

abstract class AbstractBotException extends Exception implements BotException {

    private final String message;

    public AbstractBotException(String message) {
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
