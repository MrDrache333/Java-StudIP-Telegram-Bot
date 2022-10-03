package src.de.oelrichsgarcia.studipTelegramBot.telegram.api;

/**
 * The type Request response.
 */
public class RequestResponse {

    private int responseCode;
    private String responseMessage;

    /**
     * Instantiates a new Request response.
     *
     * @param responseCode    the response code
     * @param responseMessage the response message
     */
    public RequestResponse(int responseCode, String responseMessage) {
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
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
     * Gets response message.
     *
     * @return the response message
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets response message.
     *
     * @param responseMessage the response message
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
