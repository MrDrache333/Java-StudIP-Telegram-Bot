package de.oelrichsgarcia.studipTelegramBot.studip;

/**
 * The type Login exception.
 */
public class LoginException extends GeneralBotException {

    /**
     * Instantiates a new Login exception.
     *
     * @param errorMessage the error message
     */
    public LoginException(String errorMessage) {
        super(errorMessage);
    }
}
