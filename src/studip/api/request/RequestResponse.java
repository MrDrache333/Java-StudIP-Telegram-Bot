package studip.api.request;

/**
 * The type Request response.
 */
public class RequestResponse {

    private int resonseCode;
    private String resonesMessage;

    /**
     * Instantiates a new Request response.
     */
    public RequestResponse() {

    }

    /**
     * Instantiates a new Request response.
     *
     * @param resonseCode    the resonse code
     * @param resonesMessage the resones message
     */
    public RequestResponse(int resonseCode, String resonesMessage) {
        this.resonesMessage = resonesMessage;
        this.resonseCode = resonseCode;
    }

    /**
     * Gets resonse code.
     *
     * @return the resonse code
     */
    public int getResonseCode() {
        return resonseCode;
    }

    /**
     * Sets resonse code.
     *
     * @param resonseCode the resonse code
     */
    public void setResonseCode(int resonseCode) {
        this.resonseCode = resonseCode;
    }

    /**
     * Gets resones message.
     *
     * @return the resones message
     */
    public String getResonesMessage() {
        return resonesMessage;
    }

    /**
     * Sets resones message.
     *
     * @param resonesMessage the resones message
     */
    public void setResonesMessage(String resonesMessage) {
        this.resonesMessage = resonesMessage;
    }
}
