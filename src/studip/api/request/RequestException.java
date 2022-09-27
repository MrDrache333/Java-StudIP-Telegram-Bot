package studip.api.request;

public class RequestException extends APIException {


    public RequestException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
