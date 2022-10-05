package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request;

/**
 * The type Api exception.
 */
abstract class AbstractAPIException extends Exception {

    private int errorCode;
    private String errorMessage;

    /**
     * Instantiates a new Api exception.
     *
     * @param errorCode    the error code
     * @param errorMessage the error message
     */
    public AbstractAPIException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Gets error code.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Sets error code.
     *
     * @param errorCode the error code
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
