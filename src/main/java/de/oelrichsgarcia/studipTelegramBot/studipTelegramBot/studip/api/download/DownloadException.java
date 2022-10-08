package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download;

/**
 * The type Download exception.
 */
public class DownloadException extends Exception {

    private int responseCode;
    private String responseMessage;

    /**
     * Instantiates a new Download exception.
     *
     * @param responseCode    the response code
     * @param responseMessage the resonse message
     */
    public DownloadException(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    /**
     * Gets response code.
     *
     * @return the response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Sets response code.
     *
     * @param responseCode the response code
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Gets resonse message.
     *
     * @return the resonse message
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets resonse message.
     *
     * @param responseMessage the resonse message
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
